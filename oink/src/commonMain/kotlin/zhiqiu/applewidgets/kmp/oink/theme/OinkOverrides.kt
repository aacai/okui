package zhiqiu.applewidgets.kmp.oink.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import zhiqiu.applewidgets.kmp.basic.BadgeParams
import zhiqiu.applewidgets.kmp.basic.CardParams
import zhiqiu.applewidgets.kmp.basic.CheckboxParams
import zhiqiu.applewidgets.kmp.basic.LocalBadgeImpl
import zhiqiu.applewidgets.kmp.basic.LocalCardImpl
import zhiqiu.applewidgets.kmp.basic.LocalCheckboxImpl
import zhiqiu.applewidgets.kmp.basic.LocalProgressViewImpl
import zhiqiu.applewidgets.kmp.basic.LocalSegmentedControlImpl
import zhiqiu.applewidgets.kmp.basic.LocalTabRowImpl
import zhiqiu.applewidgets.kmp.basic.LocalRadioButtonImpl
import zhiqiu.applewidgets.kmp.basic.LocalSearchBarImpl
import zhiqiu.applewidgets.kmp.basic.LocalSliderImpl
import zhiqiu.applewidgets.kmp.basic.LocalSwitchImpl
import zhiqiu.applewidgets.kmp.basic.LocalTextFieldImpl
import zhiqiu.applewidgets.kmp.basic.ProgressViewParams
import zhiqiu.applewidgets.kmp.basic.RadioButtonParams
import zhiqiu.applewidgets.kmp.basic.SegmentedControlParams
import zhiqiu.applewidgets.kmp.basic.SearchBarParams
import zhiqiu.applewidgets.kmp.basic.SliderParams
import zhiqiu.applewidgets.kmp.basic.SwitchParams
import zhiqiu.applewidgets.kmp.basic.TabRowParams
import zhiqiu.applewidgets.kmp.basic.TextFieldParams
import zhiqiu.applewidgets.kmp.oink.basic.OinkBadge
import zhiqiu.applewidgets.kmp.oink.basic.OinkCard
import zhiqiu.applewidgets.kmp.oink.basic.OinkCheckbox
import zhiqiu.applewidgets.kmp.oink.basic.OinkRadio
import zhiqiu.applewidgets.kmp.oink.basic.OinkSegmented
import zhiqiu.applewidgets.kmp.oink.basic.OinkSlider
import zhiqiu.applewidgets.kmp.oink.basic.OinkSpinner
import zhiqiu.applewidgets.kmp.oink.basic.OinkSwitch
import zhiqiu.applewidgets.kmp.oink.basic.OinkTabs
import zhiqiu.applewidgets.kmp.oink.form.OinkSearchInput
import androidx.compose.foundation.layout.ColumnScope
import zhiqiu.applewidgets.kmp.oink.form.OinkTextField

/**
 * Oink 皮肤对 applewidgets-ui 组件的实现覆盖。
 *
 * 组件库提供"默认实现 + 可替换槽"，皮肤注入自己的实现：
 * 主页继续用 applewidgets-ui 的组件名，切到 Oink 主题即整体呈现 Oink 视觉，
 * 实现代码只有一份（Oink 版），组件库不需要塞进 Oink 专属参数。
 */
@Composable
internal fun ProvideOinkComponents(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalSwitchImpl provides { params -> OinkSwitchImpl(params) },
        LocalCheckboxImpl provides { params -> OinkCheckboxImpl(params) },
        LocalRadioButtonImpl provides { params -> OinkRadioImpl(params) },
        LocalSliderImpl provides { params -> OinkSliderImpl(params) },
        LocalTextFieldImpl provides { params -> OinkTextFieldImpl(params) },
        LocalSearchBarImpl provides { params -> OinkSearchBarImpl(params) },
        LocalCardImpl provides { params, content -> OinkCardImpl(params, content) },
        LocalBadgeImpl provides { params -> OinkBadgeImpl(params) },
        LocalSegmentedControlImpl provides { params -> OinkSegmentedImpl(params) },
        LocalTabRowImpl provides { params -> OinkTabsImpl(params) },
        LocalProgressViewImpl provides { params -> OinkSpinnerImpl(params) },
    ) {
        content()
    }
}

@Composable
internal fun OinkSwitchImpl(params: SwitchParams) {
    OinkSwitch(
        checked = params.checked,
        onCheckedChange = { params.onCheckedChange?.invoke(it) },
        modifier = params.modifier,
        enabled = params.enabled,
    )
}

@Composable
internal fun OinkCheckboxImpl(params: CheckboxParams) {
    OinkCheckbox(
        checked = params.checked,
        onCheckedChange = { params.onCheckedChange?.invoke(it) },
        modifier = params.modifier,
        enabled = params.enabled,
    )
}

@Composable
internal fun OinkRadioImpl(params: RadioButtonParams) {
    OinkRadio(
        selected = params.selected,
        onClick = { params.onClick?.invoke() },
        modifier = params.modifier,
        enabled = params.enabled,
    )
}

@Composable
internal fun OinkSliderImpl(params: SliderParams) {
    OinkSlider(
        value = params.value,
        onValueChange = params.onValueChange,
        modifier = params.modifier,
        enabled = params.enabled,
        valueRange = params.valueRange,
    )
}

@Composable
internal fun OinkTextFieldImpl(params: TextFieldParams) {
    OinkTextField(
        value = params.value,
        onValueChange = params.onValueChange,
        modifier = params.modifier,
        enabled = params.enabled,
        placeholder = params.placeholder,
        showClear = params.showClear,
        singleLine = params.singleLine,
        visualTransformation = params.visualTransformation,
        keyboardOptions = params.keyboardOptions,
        keyboardActions = params.keyboardActions,
    )
}

@Composable
internal fun OinkSearchBarImpl(params: SearchBarParams) {
    OinkSearchInput(
        value = params.value,
        onValueChange = params.onValueChange,
        modifier = params.modifier,
        enabled = params.enabled,
        placeholder = params.placeholder,
    )
}

@Composable
internal fun OinkCardImpl(
    params: CardParams,
    content: @Composable ColumnScope.() -> Unit,
) {
    OinkCard(modifier = params.modifier, content = content)
}

@Composable
internal fun OinkBadgeImpl(params: BadgeParams) {
    val count = params.count
    OinkBadge(
        text = count?.let { if (it > 99) "99+" else it.toString() } ?: "",
        modifier = params.modifier,
        dot = count == null,
    )
}

@Composable
internal fun OinkSegmentedImpl(params: SegmentedControlParams) {
    OinkSegmented(
        options = params.options,
        selectedIndex = params.selectedIndex,
        onSelectedChange = params.onSelect,
        modifier = params.modifier,
    )
}

@Composable
internal fun OinkTabsImpl(params: TabRowParams) {
    OinkTabs(
        tabs = params.tabs,
        selectedIndex = params.selectedIndex,
        onSelectedChange = params.onSelect,
        modifier = params.modifier,
    )
}

@Composable
internal fun OinkSpinnerImpl(params: ProgressViewParams) {
    OinkSpinner(
        modifier = params.modifier,
        size = params.size,
    )
}
