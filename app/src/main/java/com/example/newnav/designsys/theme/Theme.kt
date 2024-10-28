/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.newnav.designsys.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Light default theme color scheme
 */
@androidx.annotation.VisibleForTesting
val LightDefaultColorScheme = androidx.compose.material3.lightColorScheme(
    primary = Purple40,
    onPrimary = androidx.compose.ui.graphics.Color.Companion.White,
    primaryContainer = com.example.newnav.designsys.theme.Purple90,
    onPrimaryContainer = com.example.newnav.designsys.theme.Purple10,
    secondary = com.example.newnav.designsys.theme.Orange40,
    onSecondary = androidx.compose.ui.graphics.Color.Companion.White,
    secondaryContainer = com.example.newnav.designsys.theme.Orange90,
    onSecondaryContainer = com.example.newnav.designsys.theme.Orange10,
    tertiary = com.example.newnav.designsys.theme.Blue40,
    onTertiary = androidx.compose.ui.graphics.Color.Companion.White,
    tertiaryContainer = com.example.newnav.designsys.theme.Blue90,
    onTertiaryContainer = com.example.newnav.designsys.theme.Blue10,
    error = com.example.newnav.designsys.theme.Red40,
    onError = androidx.compose.ui.graphics.Color.Companion.White,
    errorContainer = com.example.newnav.designsys.theme.Red90,
    onErrorContainer = com.example.newnav.designsys.theme.Red10,
    background = com.example.newnav.designsys.theme.DarkPurpleGray99,
    onBackground = com.example.newnav.designsys.theme.DarkPurpleGray10,
    surface = com.example.newnav.designsys.theme.DarkPurpleGray99,
    onSurface = com.example.newnav.designsys.theme.DarkPurpleGray10,
    surfaceVariant = com.example.newnav.designsys.theme.PurpleGray90,
    onSurfaceVariant = com.example.newnav.designsys.theme.PurpleGray30,
    inverseSurface = com.example.newnav.designsys.theme.DarkPurpleGray20,
    inverseOnSurface = com.example.newnav.designsys.theme.DarkPurpleGray95,
    outline = com.example.newnav.designsys.theme.PurpleGray50,
)

/**
 * Dark default theme color scheme
 */
@androidx.annotation.VisibleForTesting
val DarkDefaultColorScheme = androidx.compose.material3.darkColorScheme(
    primary = com.example.newnav.designsys.theme.Purple80,
    onPrimary = com.example.newnav.designsys.theme.Purple20,
    primaryContainer = com.example.newnav.designsys.theme.Purple30,
    onPrimaryContainer = com.example.newnav.designsys.theme.Purple90,
    secondary = com.example.newnav.designsys.theme.Orange80,
    onSecondary = com.example.newnav.designsys.theme.Orange20,
    secondaryContainer = com.example.newnav.designsys.theme.Orange30,
    onSecondaryContainer = com.example.newnav.designsys.theme.Orange90,
    tertiary = com.example.newnav.designsys.theme.Blue80,
    onTertiary = com.example.newnav.designsys.theme.Blue20,
    tertiaryContainer = com.example.newnav.designsys.theme.Blue30,
    onTertiaryContainer = com.example.newnav.designsys.theme.Blue90,
    error = com.example.newnav.designsys.theme.Red80,
    onError = com.example.newnav.designsys.theme.Red20,
    errorContainer = com.example.newnav.designsys.theme.Red30,
    onErrorContainer = com.example.newnav.designsys.theme.Red90,
    background = com.example.newnav.designsys.theme.DarkPurpleGray10,
    onBackground = com.example.newnav.designsys.theme.DarkPurpleGray90,
    surface = com.example.newnav.designsys.theme.DarkPurpleGray10,
    onSurface = com.example.newnav.designsys.theme.DarkPurpleGray90,
    surfaceVariant = com.example.newnav.designsys.theme.PurpleGray30,
    onSurfaceVariant = com.example.newnav.designsys.theme.PurpleGray80,
    inverseSurface = com.example.newnav.designsys.theme.DarkPurpleGray90,
    inverseOnSurface = com.example.newnav.designsys.theme.DarkPurpleGray10,
    outline = com.example.newnav.designsys.theme.PurpleGray60,
)

/**
 * Light Android theme color scheme
 */
@androidx.annotation.VisibleForTesting
val LightAndroidColorScheme = androidx.compose.material3.lightColorScheme(
    primary = com.example.newnav.designsys.theme.Green40,
    onPrimary = androidx.compose.ui.graphics.Color.Companion.White,
    primaryContainer = com.example.newnav.designsys.theme.Green90,
    onPrimaryContainer = com.example.newnav.designsys.theme.Green10,
    secondary = com.example.newnav.designsys.theme.DarkGreen40,
    onSecondary = androidx.compose.ui.graphics.Color.Companion.White,
    secondaryContainer = com.example.newnav.designsys.theme.DarkGreen90,
    onSecondaryContainer = com.example.newnav.designsys.theme.DarkGreen10,
    tertiary = com.example.newnav.designsys.theme.Teal40,
    onTertiary = androidx.compose.ui.graphics.Color.Companion.White,
    tertiaryContainer = com.example.newnav.designsys.theme.Teal90,
    onTertiaryContainer = com.example.newnav.designsys.theme.Teal10,
    error = com.example.newnav.designsys.theme.Red40,
    onError = androidx.compose.ui.graphics.Color.Companion.White,
    errorContainer = com.example.newnav.designsys.theme.Red90,
    onErrorContainer = com.example.newnav.designsys.theme.Red10,
    background = com.example.newnav.designsys.theme.DarkGreenGray99,
    onBackground = com.example.newnav.designsys.theme.DarkGreenGray10,
    surface = com.example.newnav.designsys.theme.DarkGreenGray99,
    onSurface = com.example.newnav.designsys.theme.DarkGreenGray10,
    surfaceVariant = com.example.newnav.designsys.theme.GreenGray90,
    onSurfaceVariant = com.example.newnav.designsys.theme.GreenGray30,
    inverseSurface = com.example.newnav.designsys.theme.DarkGreenGray20,
    inverseOnSurface = com.example.newnav.designsys.theme.DarkGreenGray95,
    outline = com.example.newnav.designsys.theme.GreenGray50,
)

/**
 * Dark Android theme color scheme
 */
@androidx.annotation.VisibleForTesting
val DarkAndroidColorScheme = androidx.compose.material3.darkColorScheme(
    primary = com.example.newnav.designsys.theme.Green80,
    onPrimary = com.example.newnav.designsys.theme.Green20,
    primaryContainer = com.example.newnav.designsys.theme.Green30,
    onPrimaryContainer = com.example.newnav.designsys.theme.Green90,
    secondary = com.example.newnav.designsys.theme.DarkGreen80,
    onSecondary = com.example.newnav.designsys.theme.DarkGreen20,
    secondaryContainer = com.example.newnav.designsys.theme.DarkGreen30,
    onSecondaryContainer = com.example.newnav.designsys.theme.DarkGreen90,
    tertiary = com.example.newnav.designsys.theme.Teal80,
    onTertiary = com.example.newnav.designsys.theme.Teal20,
    tertiaryContainer = com.example.newnav.designsys.theme.Teal30,
    onTertiaryContainer = com.example.newnav.designsys.theme.Teal90,
    error = com.example.newnav.designsys.theme.Red80,
    onError = com.example.newnav.designsys.theme.Red20,
    errorContainer = com.example.newnav.designsys.theme.Red30,
    onErrorContainer = com.example.newnav.designsys.theme.Red90,
    background = com.example.newnav.designsys.theme.DarkGreenGray10,
    onBackground = com.example.newnav.designsys.theme.DarkGreenGray90,
    surface = com.example.newnav.designsys.theme.DarkGreenGray10,
    onSurface = com.example.newnav.designsys.theme.DarkGreenGray90,
    surfaceVariant = com.example.newnav.designsys.theme.GreenGray30,
    onSurfaceVariant = com.example.newnav.designsys.theme.GreenGray80,
    inverseSurface = com.example.newnav.designsys.theme.DarkGreenGray90,
    inverseOnSurface = com.example.newnav.designsys.theme.DarkGreenGray10,
    outline = com.example.newnav.designsys.theme.GreenGray60,
)

/**
 * Light Android gradient colors
 */
val LightAndroidGradientColors =
    GradientColors(container = DarkGreenGray95)

/**
 * Dark Android gradient colors
 */
val DarkAndroidGradientColors = GradientColors(container = Color.Black)

/**
 * Light Android background theme
 */
val LightAndroidBackgroundTheme = BackgroundTheme(color = DarkGreenGray95)

/**
 * Dark Android background theme
 */
val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)

/**
 * Now in Android theme.
 *
 * @param darkTheme Whether the theme should use a dark color scheme (follows system by default).
 * @param androidTheme Whether the theme should use the Android theme color scheme instead of the
 *        default theme.
 * @param disableDynamicTheming If `true`, disables the use of dynamic theming, even when it is
 *        supported. This parameter has no effect if [androidTheme] is `true`.
 */
@Composable
fun NiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    // Color scheme
    val colorScheme = when {
        androidTheme -> if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }
    // Gradient colors
    val emptyGradientColors = GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))
    val defaultGradientColors = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface,
    )
    val gradientColors = when {
        androidTheme -> if (darkTheme) DarkAndroidGradientColors else LightAndroidGradientColors
        !disableDynamicTheming && supportsDynamicTheming() -> emptyGradientColors
        else -> defaultGradientColors
    }
    // Background theme
    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )
    val backgroundTheme = when {
        androidTheme -> if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
        else -> defaultBackgroundTheme
    }
    val tintTheme = when {
        androidTheme -> TintTheme()
        !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }
    // Composition locals
    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = NiaTypography,
            content = content,
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
