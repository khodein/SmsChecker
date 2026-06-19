package com.sms.checker.forwarder.feature.email.data

import com.sms.checker.forwarder.feature.email.data.mapper.SmtpEmailDataMapper
import com.sms.checker.forwarder.feature.email.db.SmtpEmailDao
import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.exception.EmailException
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

internal class EmailRepositoryImpl(
    private val smtpEmailDao: SmtpEmailDao,
    private val smtpEmailDataMapper: SmtpEmailDataMapper
) : EmailRepository {

    private val _isSmtpLimitFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isSmtpLimitFlow = _isSmtpLimitFlow.asStateFlow()

    override suspend fun sendSmtpMessage(
        message: String,
        model: SmtpEmailModel,
    ) {
        withContext(Dispatchers.IO) {
            val session = Session.getInstance(
                Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.host", model.server.host)
                    put("mail.smtp.port", model.server.port)
                    when (model.server.port) {
                        "465" -> put("mail.smtp.ssl.enable", "true")
                        "587" -> put("mail.smtp.starttls.enable", "true")
                    }
                },
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication =
                        PasswordAuthentication(model.user.name, model.user.password)
                },
            )

            val recipients = InternetAddress.parse(model.recipient.email)

            val mimeMessage = MimeMessage(session).apply {
                this.setFrom(InternetAddress(model.from.email, model.from.name))
                this.setRecipients(Message.RecipientType.TO, recipients)
                this.setSubject(model.recipient.subject)
                this.setText(message)
            }

            Transport.send(mimeMessage)
        }
    }

    override suspend fun getSmtpConfigById(id: Long): SmtpEmailModel {
        val entity = smtpEmailDao.getById(id) ?: throw EmailException.SmtpConfigNotFound(id)
        return smtpEmailDataMapper.toModel(entity)
    }

    override suspend fun setSmtpConfig(model: SmtpEmailModel): Long {
        val entity = smtpEmailDataMapper.toEntity(model)
        val insert = smtpEmailDao.insert(entity)
        getSmtpList()
        return insert
    }

    override suspend fun updateSmtpConfig(model: SmtpEmailModel): Long {
        val entity = smtpEmailDataMapper.toEntity(model)
        val rowsAffected = smtpEmailDao.update(entity)
        if (rowsAffected == 0) throw EmailException.SmtpConfigNotUpdated(entity.id)
        getSmtpList()
        return entity.id
    }

    override suspend fun getSmtpList(): List<SmtpEmailModel> {
        val list = smtpEmailDao.getAll().map(smtpEmailDataMapper::toModel)
        setSmtpLimit(list.size)
        return list
    }

    override suspend fun getEnabledSmtpList(): List<SmtpEmailModel> {
        return smtpEmailDao.getAllEnabled().map(smtpEmailDataMapper::toModel)
    }

    override suspend fun getEnabledSmtpIds(): List<Long> {
        return smtpEmailDao.getAllEnabledIds()
    }

    override suspend fun deleteSmtpConfigById(id: Long) {
        smtpEmailDao.deleteById(id)
        getSmtpList()
    }

    private fun setSmtpLimit(size: Int) {
        _isSmtpLimitFlow.value = size <= SMTP_LIMIT
    }

    private companion object {
        const val SMTP_LIMIT = 3
    }
}