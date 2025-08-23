package com.sortisplus.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary Colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Extended Color Palette
val SortisPrimary = Color(0xFF0066CC)
val SortisSecondary = Color(0xFF03DAC6)
val SortisError = Color(0xFFB00020)
val SortisWarning = Color(0xFFFF9800)
val SortisSuccess = Color(0xFF4CAF50)

// Neutral Colors
val Neutral10 = Color(0xFF1C1B1F)
val Neutral20 = Color(0xFF313033)
val Neutral30 = Color(0xFF484649)
val Neutral40 = Color(0xFF605D62)
val Neutral50 = Color(0xFF787579)
val Neutral60 = Color(0xFF939094)
val Neutral70 = Color(0xFFAEAAAE)
val Neutral80 = Color(0xFFC9C5CA)
val Neutral90 = Color(0xFFE6E1E5)
val Neutral95 = Color(0xFFF4EFF4)
val Neutral99 = Color(0xFFFFFBFE)

// Light Color Scheme
val LightColorScheme = lightColorScheme(
    primary = SortisPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2F1),
    onPrimaryContainer = Color(0xFF002114),

    secondary = SortisSecondary,
    onSecondary = Color(0xFF003735),
    secondaryContainer = Color(0xFFB2DFDB),
    onSecondaryContainer = Color(0xFF00201E),

    tertiary = Pink40,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),

    error = SortisError,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),

    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color.Black,
    inverseSurface = Neutral20,
    inverseOnSurface = Neutral95,
    inversePrimary = Color(0xFF69F0AE)
)

// Dark Color Scheme
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF69F0AE),
    onPrimary = Color(0xFF003826),
    primaryContainer = Color(0xFF005138),
    onPrimaryContainer = Color(0xFF85F4B3),

    secondary = Color(0xFF4FD0C7),
    onSecondary = Color(0xFF003735),
    secondaryContainer = Color(0xFF004F4C),
    onSecondaryContainer = Color(0xFF6FF6ED),

    tertiary = Pink80,
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral10,
    onSurface = Neutral90,
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    scrim = Color.Black,
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral20,
    inversePrimary = SortisPrimary
)
