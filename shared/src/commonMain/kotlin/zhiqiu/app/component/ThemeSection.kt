package zhiqiu.app.component

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import zhiqiu.app.AppThemeState
import zhiqiu.app.AppThemeStyle
import zhiqiu.applewidgets.kmp.basic.Card
import zhiqiu.applewidgets.kmp.basic.SmallTitle
import zhiqiu.applewidgets.kmp.preference.ArrowPreference
import zhiqiu.applewidgets.kmp.theme.ColorSchemeMode

fun LazyListScope.themeSection(
    state: AppThemeState,
    aboutSummary: String,
) {
    item(key = "theme") {
        SmallTitle(text = "Theme")
        Card(modifier = Modifier.demoCard()) {
            ArrowPreference(
                title = "主题风格",
                value = when (state.style) {
                    AppThemeStyle.Oink -> "OinkUI"
                    AppThemeStyle.Apple -> "Apple"
                },
                onClick = {
                    state.style = when (state.style) {
                        AppThemeStyle.Oink -> AppThemeStyle.Apple
                        AppThemeStyle.Apple -> AppThemeStyle.Oink
                    }
                },
            )
            if (state.style == AppThemeStyle.Oink) {
                ArrowPreference(
                    title = "强调色",
                    value = state.accent.label,
                    onClick = {
                        state.accent = OinkAccent.entries[(state.accent.ordinal + 1) % OinkAccent.entries.size]
                    },
                )
            }
            ArrowPreference(
                title = "外观模式",
                value = when (state.controller.colorSchemeMode) {
                    ColorSchemeMode.Light -> "浅色"
                    ColorSchemeMode.Dark -> "深色"
                    ColorSchemeMode.System -> "系统"
                },
                onClick = {
                    state.controller.colorSchemeMode = when (state.controller.colorSchemeMode) {
                        ColorSchemeMode.System -> ColorSchemeMode.Light
                        ColorSchemeMode.Light -> ColorSchemeMode.Dark
                        ColorSchemeMode.Dark -> ColorSchemeMode.System
                    }
                },
            )
            ArrowPreference(
                title = "关于",
                summary = aboutSummary,
                onClick = {},
            )
        }
    }
}