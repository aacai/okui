# AppleWidgets Agent Guide

iOS / macOS 视觉的 Compose Multiplatform 组件库。工程理念对齐 miuix：**自建 Theme、行原语 + Preference、Defaults/Colors 约定、Gallery Demo**。

## Quick Start

```bash
./gradlew :applewidgets-ui:compileKotlinJvm
./gradlew :desktopApp:run
```

## Repository Structure

| 路径 | 职责 |
|------|------|
| `oink/` | Oink 组件库（独立 Gradle 模块，依赖 `applewidgets-ui`） |
| `applewidgets-ui/` | 组件库（当前单模块，后续可拆 preference/shape） |
| `…/theme/` | `AppleWidgetsTheme`、`Colors`、`TextStyles`、`ThemeController`、`Metrics` |
| `…/basic/` | 基础组件（Button、Switch、`BasicComponent`、SearchBar、TabRow、Chip…） |
| `…/preference/` | 设置行（Switch/Checkbox/Radio/Slider/Dropdown/Arrow） |
| `…/layout/` | Scaffold、GroupedSection、NavigationBar、TabBar |
| `…/overlay/` | Alert、Sheet、ActionSheet、ListPopup |
| `…/macos/` | macOS 视觉组件（Sidebar、Toolbar、Inspector…） |
| `shared/` | Demo / 宿主应用 |
| `shared/…/component/` | Gallery Section（每个组件一块） |

## API Conventions

### 参数顺序

```kotlin
@Composable
fun ComponentName(
    // 1. 必需功能参数（回调优先）
    onClick: () -> Unit,
    // 2. Modifier
    modifier: Modifier = Modifier,
    // 3. Boolean
    enabled: Boolean = true,
    // 4. 视觉参数（来自 XxxDefaults）
    colors: ComponentColors = ComponentDefaults.componentColors(),
    insideMargin: PaddingValues = ComponentDefaults.InsideMargin,
    // 5. content 永远最后
    content: @Composable () -> Unit,
)
```

### Defaults + Colors

- 每个组件一个 `XxxDefaults`：尺寸常量用 `val`，颜色工厂必须 `@Composable`
- 颜色用 `@Immutable data class XxxColors`
- **禁止在组件里写死色值**；一律 `AppleWidgetsTheme.colorScheme.*`
- 文本样式一律 `AppleWidgetsTheme.textStyles.*`
- 状态取色优先 `@Stable fun color(enabled: Boolean): Color`

### 分层

| 层 | 包 | 规则 |
|----|-----|------|
| basic | `…basic` | 独立控件；列表行基座是 `BasicComponent` |
| preference | `…preference` | 必须委托 `BasicComponent`，控件放 `endActions` / `startAction` |
| layout | `…layout` | 页面骨架与分组容器 |
| overlay | `…overlay` | 浮层；需在 Scaffold / OverlayHost 内使用 |
| macos | `…macos` | macOS 密度与布局；正文默认 13sp 语义（后续可独立 TextStyles） |

### Key Patterns

- 薄包装用 `@NonRestartableComposable`
- 长生命周期闭包里的回调用 `rememberUpdatedState`
- Toggle 类：触觉反馈在 **点击回调里** 触发，不要监听 state 变化
- 正确设置 `Role`（Button / Switch / Checkbox…）
- 单文件：主 Composable + Defaults + Colors + 私有 helper

## Critical Constraints

1. **Theme 自建**：不依赖 Material3 主题（Demo 宿主可暂留依赖，组件库 API 不暴露 Material）
2. **Preference 不重造行布局**：一律基于 `BasicComponent`
3. **颜色只走 Theme**：accent / label / fill / separator 等语义色
4. **新组件必须配 Gallery Section**：`shared/.../component/{Name}Section.kt` 并在主页注册
5. **全端可用 / 有就用没有就不用**：
   - 组件 API 必须在 commonMain 可编译、各 target 可运行
   - 平台专有能力走 `AppleCapabilities`（`platform/`）；false 时降级或跳过，禁止硬崩
   - 触感：`performToggleHaptic`；Insets：`safeStatusBarsPadding` / `safeNavigationBarsPadding`
   - 浮层：`PlatformDialogOverlay` / `PlatformAnchoredPopup`（无 window overlay 时退化为全屏遮罩）
   - 真实 blur / 系统菜单栏等：能力未就绪就不做，用半透明等视觉降级

## Adding a Component Checklist

1. 源码：`basic/` 或 `preference/` 或 `macos/`
2. 遵循参数顺序 + Defaults + Colors
3. Gallery：`shared/.../component/{Name}Section.kt`
4. 主页注册 Section
5. `./gradlew :applewidgets-ui:compileKotlinJvm :shared:compileKotlinJvm`
6. Desktop 目视确认默认态 / 变体 / disabled

## Git Commit Style

中文简短说明意图，例如：`规范化：BasicComponent 与 Preference 分层`。
