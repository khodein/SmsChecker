package com.sms.checker.forwarder.feature.email.data

import com.sms.checker.forwarder.feature.email.data.mapper.SmtpEmailDataMapper
import com.sms.checker.forwarder.feature.email.db.SmtpEmailDao
import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.model.ForwardingSource
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class EmailRepositoryImpl(
    private val smtpEmailDataMapper: SmtpEmailDataMapper,
    private val smtpEmailDao: SmtpEmailDao,
) : EmailRepository {

    override suspend fun getByForwardingSource(source: ForwardingSource): List<SmtpEmailModel> {
        return smtpEmailDao.getByForwardingSource(source.name)
            .takeIf { it.isNotEmpty() }
            ?.map(smtpEmailDataMapper::toModel)
            ?: emptyList()
    }

    override suspend fun toggleForwardingSource(id: Long, source: ForwardingSource) {
        val entity = smtpEmailDao.getById(id) ?: return
        val current = entity.forwardingSources
            .split(",")
            .filter { it.isNotBlank() }
            .toMutableList()
        if (source.name in current) {
            current.remove(source.name)
        } else {
            current.add(source.name)
        }
        smtpEmailDao.updateForwardingSources(id, current.joinToString(","))
    }
}