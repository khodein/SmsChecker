package com.sms.checker.forwarder.framework.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TODO: после добавления файлов nunito_regular.ttf, nunito_medium.ttf, nunito_bold.ttf
//       в framework/src/main/res/font/ заменить на:
//
// import androidx.compose.ui.text.font.Font
// import com.sms.checker.forwarder.framework.R
//
// private val NunitoFontFamily = FontFamily(
//     Font(R.font.nunito_regular, FontWeight.Normal),
//     Font(R.font.nunito_medium, FontWeight.Medium),
//     Font(R.font.nunito_bold, FontWeight.Bold)
// )
private val NunitoFontFamily = FontFamily.Default

@Immutable
data class AppTypography(
    val headlineLarge: TextStyle = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    val headlineMedium: TextStyle = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    val titleLarge: TextStyle = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    val titleMedium: TextStyle = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    val labelLarge: TextStyle = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    val labelMedium: TextStyle = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

internal val LocalAppTypography = staticCompositionLocalOf { AppTypography() }
