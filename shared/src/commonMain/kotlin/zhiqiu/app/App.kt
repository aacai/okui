package zhiqiu.app

import zhiqiu.applewidgets.kmp.oink.basic.*
import zhiqiu.applewidgets.kmp.oink.data.*
import zhiqiu.applewidgets.kmp.oink.form.*
import zhiqiu.applewidgets.kmp.oink.layout.*
import zhiqiu.applewidgets.kmp.oink.nav.*
import zhiqiu.applewidgets.kmp.oink.overlay.*
import zhiqiu.applewidgets.kmp.oink.primitives.*
import zhiqiu.applewidgets.kmp.oink.responsive.*
import zhiqiu.applewidgets.kmp.oink.theme.*

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import zhiqiu.app.component.arrowSection
import zhiqiu.app.component.badgeSection
import zhiqiu.app.component.basicComponentSection
import zhiqiu.app.component.bottomSheetSection
import zhiqiu.app.component.breadcrumbBarSection
import zhiqiu.app.component.buttonSection
import zhiqiu.app.component.cardSection
import zhiqiu.app.component.checkboxSection
import zhiqiu.app.component.colorPickerSection
import zhiqiu.app.component.dateTimePickerSection
import zhiqiu.app.component.dialogSection
import zhiqiu.app.component.dropdownSection
import zhiqiu.app.component.liquidGlassSection
import zhiqiu.app.component.macOsSection
import zhiqiu.app.component.numberPickerSection
import zhiqiu.app.component.oinkSection
import zhiqiu.app.component.otherSection
import zhiqiu.app.component.pickerViewSection
import zhiqiu.app.component.progressIndicatorSection
import zhiqiu.app.component.radioButtonSection
import zhiqiu.app.component.searchBarSection
import zhiqiu.app.component.sliderSection
import zhiqiu.app.component.snackbarSection
import zhiqiu.app.component.spinnerSection
import zhiqiu.app.component.switchSection
import zhiqiu.app.component.tabRowSection
import zhiqiu.app.component.textFieldSection
import zhiqiu.app.component.themeSection
import zhiqiu.app.component.tooltipSection
import zhiqiu.applewidgets.kmp.basic.ToastHost
import zhiqiu.applewidgets.kmp.layout.NavigationBar
import zhiqiu.applewidgets.kmp.theme.AppleWidgetsTheme
import zhiqiu.applewidgets.kmp.theme.ColorSchemeMode
import zhiqiu.applewidgets.kmp.theme.ThemeController
import zhiqiu.app.oink.OinkShowcase

enum class AppThemeStyle {
    Oink,
    Apple,
}

@Stable
class AppThemeState(
    style: AppThemeStyle = AppThemeStyle.Oink,
    accent: OinkAccent = OinkAccent.Pink,
    colorSchemeMode: ColorSchemeMode = ColorSchemeMode.System,
) {
    var style by mutableStateOf(style)
    var accent by mutableStateOf(accent)
    val controller = ThemeController(colorSchemeMode)
}

@Composable
fun AppThemeState.currentDark(): Boolean = when (controller.colorSchemeMode) {
    ColorSchemeMode.Light -> false
    ColorSchemeMode.Dark -> true
    ColorSchemeMode.System -> isSystemInDarkTheme()
}

@Composable
fun AppTheme(state: AppThemeState, content: @Composable () -> Unit) {
    when (state.style) {
        AppThemeStyle.Oink -> OinkTheme(
            darkTheme = state.currentDark(),
            accent = state.accent,
            content = content,
        )
        AppThemeStyle.Apple -> AppleWidgetsTheme(controller = state.controller) {
            content()
        }
    }
}

@Composable
@Preview
fun App(
    initialStyle: AppThemeStyle = AppThemeStyle.Oink,
    initialMode: ColorSchemeMode = ColorSchemeMode.System,
) {
    val themeState = remember { AppThemeState(initialStyle, colorSchemeMode = initialMode) }
    var showOink by remember { mutableStateOf(false) }
    if (showOink) {
        OinkShowcase(
            onBack = { showOink = false },
            initialDark = themeState.currentDark(),
            initialAccent = themeState.accent,
        )
        return
    }
    AppTheme(themeState) {
        val dark = themeState.currentDark()
        var toastMessage by remember { mutableStateOf<String?>(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppleWidgetsTheme.colorScheme.systemGroupedBackground),
        ) {
            Column(Modifier.fillMaxSize()) {
                NavigationBar(title = "Home", largeTitle = true)
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // 顺序对齐 miuix MainPage
                    oinkSection(
                        onOpen = { showOink = true },
                        dark = dark,
                        accent = themeState.accent,
                        oinkTheme = themeState.style == AppThemeStyle.Oink,
                    )
                    searchBarSection()
                    liquidGlassSection()
                    basicComponentSection()
                    switchSection()
                    checkboxSection()
                    radioButtonSection()
                    buttonSection()
                    tabRowSection()
                    breadcrumbBarSection()
                    arrowSection()
                    dialogSection()
                    bottomSheetSection()
                    dropdownSection()
                    spinnerSection()
                    snackbarSection(onShowToast = { toastMessage = it })
                    textFieldSection()
                    progressIndicatorSection()
                    pickerViewSection()
                    sliderSection()
                    cardSection()
                    tooltipSection()
                    badgeSection()
                    numberPickerSection()
                    colorPickerSection()
                    dateTimePickerSection()
                    otherSection(onShowToast = { toastMessage = it })
                    macOsSection()
                    themeSection(
                        state = themeState,
                        aboutSummary = Greeting().greet(),
                    )
                    item { Spacer(Modifier.height(12.dp)) }
                }
            }
            ToastHost(
                message = toastMessage,
                onDismiss = { toastMessage = null },
            )
        }
    }
}