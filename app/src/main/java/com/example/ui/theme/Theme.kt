package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = NeonGreen,
    onPrimary = Color.Black,
    secondary = SoftGreen,
    onSecondary = PureWhite,
    tertiary = NeonGreen,
    background = DarkBackground,
    onBackground = PureWhite,
    surface = CardBackground,
    onSurface = PureWhite,
    error = ElectricRed,
    onError = PureWhite,
    outline = BorderColor
  )

private val LightColorScheme = DarkColorScheme // Force dark neon styling in all modes to maintain brand identity

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark theme by default
  dynamicColor: Boolean = false, // Disable dynamic colors to maintain strict visual branding
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
