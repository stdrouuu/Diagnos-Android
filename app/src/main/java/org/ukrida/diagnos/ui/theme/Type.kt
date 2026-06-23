package org.ukrida.diagnos.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import org.ukrida.diagnos.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Plus Jakarta Sans")

val PlusJakartaSansFontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider),
    Font(googleFont = fontName, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = fontName, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = fontName, fontProvider = provider, weight = FontWeight.Bold)
)

private val defaultTypography = Typography()

// Set of Material typography styles using Plus Jakarta Sans
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = PlusJakartaSansFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = PlusJakartaSansFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = PlusJakartaSansFontFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = PlusJakartaSansFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = PlusJakartaSansFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = PlusJakartaSansFontFamily),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = PlusJakartaSansFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = PlusJakartaSansFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = PlusJakartaSansFontFamily),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = PlusJakartaSansFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = PlusJakartaSansFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = PlusJakartaSansFontFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = PlusJakartaSansFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = PlusJakartaSansFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = PlusJakartaSansFontFamily)
)
