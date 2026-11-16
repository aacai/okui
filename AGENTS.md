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
| `…/theme/` | `AppleWidgetsTheme`、`Colors`、`TextStyles`、`ComponentStyles`、`ThemeController` |
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
- **禁止在组件里写死圆角 / 控件高度**；一律 `AppleWidgetsTheme.componentStyles.*`
  - 需要允许调用方覆盖时用可空参数：`shape: Shape? = null`、`minHeight: Dp? = null`，null 表示走主题令牌
  - `ComponentStyles` 是主题令牌，缺省值即 iOS 观感；皮肤（Oink）通过 `AppleWidgetsTheme(styles = …)` 覆盖

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

## Theme 架构（单入口 + 皮肤）

```
AppleWidgetsTheme          ← 唯一主题入口，提供三类令牌
  ├─ Colors                ← 语义色（iOS 槽位）
  ├─ TextStyles            ← 文本样式
  └─ ComponentStyles       ← 形状 + 尺寸
OinkTheme                  ← 皮肤：包装 AppleWidgetsTheme，注入 Oink 派生令牌
```

- 组件只读 `AppleWidgetsTheme.*`，不感知当前皮肤
- 换皮肤 = 换令牌，组件实现不换；Apple 组件在 Oink 主题下自动变 Oink 形状与配色
- 新增皮肤：在 `applewidgets-ui` 之外提供 `Colors` / `TextStyles` / `ComponentStyles` 三件套，
  通过 `AppleWidgetsTheme(colors, textStyles, styles)` 注入
- Oink 专属视觉（超椭圆 `OinkShape`、`OinkMetrics`）留在 `oink/` 内部，不反向污染组件库

## 组件实现覆盖（皮肤换实现）

- 可被皮肤替换的组件提供两个东西：`XxxParams`（参数包）+ `LocalXxxImpl`（`compositionLocalOf` 槽）
- 组件体第一件事：查槽 —— 有覆盖交给覆盖实现，没有走组件库默认实现
- 皮肤在 `oink/theme/OinkOverrides.kt` 注入自己的实现（`ProvideOinkComponents`）
- 效果：主页继续用 applewidgets-ui 的组件名，切到 Oink 主题即整体呈现 Oink 视觉，实现代码只有一份
- 已覆盖组件：Switch / Checkbox / RadioButton / Slider / TextField / SearchBar / Card / Badge /
  SegmentedControl / TabRow / ProgressView / LinearProgressView / DropdownMenu / Sheet / Stepper /
  ColorPicker / Toast / Alert / CalendarDatePicker
- 新增可覆盖组件时同步更新两份清单（组件槽 + `OinkOverrides.kt`）

## 组件能力层（能力在组件库，皮肤只调色）

- 组件的**能力**（变体 / 状态机 / 动效 / 交互）统一写在 `applewidgets-ui`，皮肤只提供令牌与配色
- 现有能力基线：
  - `ButtonStyle`（Neutral / Filled / Tinted / Bordered / Plain / Inverted / Destructive）
  - `ButtonState`（Idle / Loading / Success / Error）+ `ButtonStateGlyphs.kt` 状态字形
  - `Motion`（`theme/Motion.kt`）动效基线；`OinkMotion` 只是转发别名
- `oink/` 下的同名组件是**薄委托**（如 `OinkButton` → `basic.Button`），不重复实现动画与状态机
- 新增皮肤：只写 `Colors` / `TextStyles` / `ComponentStyles` 三件套 + 必要的语义别名，**不要复制组件实现**

## Critical Constraints

1. **Theme 自建 + 单入口**：不依赖 Material3 主题（Demo 宿主可暂留依赖，组件库 API 不暴露 Material）；
   `AppleWidgetsTheme` 是唯一主题入口，`OinkTheme` 只是皮肤包装
2. **Preference 不重造行布局**：一律基于 `BasicComponent`
3. **颜色只走 Theme**：accent / label / fill / separator 等语义色
4. **形状尺寸只走 Theme**：圆角与控件高度取 `AppleWidgetsTheme.componentStyles.*`，不在组件里写死
5. **新组件必须配 Gallery Section**：`shared/.../component/{Name}Section.kt` 并在主页注册
6. **全端可用 / 有就用没有就不用**：
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
