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

private val LightColorScheme = lightColorScheme(
  primary = DeepPlum,
  onPrimary = Color.White,
  primaryContainer = DeepPlumContainer,
  onPrimaryContainer = DeepPlum,
  secondary = TerracottaCoral,
  onSecondary = Color.White,
  secondaryContainer = TerracottaCoralContainer,
  onSecondaryContainer = TerracottaCoral,
  tertiary = MutedTeal,
  onTertiary = Color.White,
  tertiaryContainer = MutedTealContainer,
  onTertiaryContainer = MutedTeal,
  background = WarmIvoryBackground,
  onBackground = CharcoalTextPrimary,
  surface = WarmIvorySurface,
  onSurface = CharcoalTextPrimary,
  surfaceVariant = WarmIvorySurfaceSubtle,
  onSurfaceVariant = CharcoalTextSecondary,
  outline = WarmIvoryBorder,
  outlineVariant = WarmIvoryBorder,
  error = StatusDangerCoral
)

private val DarkColorScheme = darkColorScheme(
  primary = DarkDeepPlum,
  onPrimary = Color(0xFF20071C),
  primaryContainer = DarkPlumContainer,
  onPrimaryContainer = Color(0xFFFBEBF7),
  secondary = TerracottaCoral,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFF381715),
  onSecondaryContainer = Color(0xFFFDE8E6),
  tertiary = MutedTeal,
  onTertiary = Color.White,
  tertiaryContainer = Color(0xFF0F312D),
  onTertiaryContainer = Color(0xFFD3F2EF),
  background = DarkCreamBackground,
  onBackground = DarkCharcoalTextPrimary,
  surface = DarkCreamSurface,
  onSurface = DarkCharcoalTextPrimary,
  surfaceVariant = DarkCreamSurfaceSubtle,
  onSurfaceVariant = DarkCharcoalTextSecondary,
  outline = DarkCreamBorder,
  outlineVariant = DarkCreamBorder,
  error = StatusDangerCoral
)

@Composable
fun EcoLeafTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep intentional creative-minimalist palette
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  EcoLeafTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
