# MD3 Showcase — Material Design 3 for Android (Native Java)

A comprehensive, fully native Android application demonstrating **Material Design 3** components and patterns, built entirely in **Java** with XML layouts — no Jetpack Compose, no web frameworks. Designed as a reference project for Sketchware Pro and standard Android Studio.

---

## 📱 Screens & Features

| Activity | Description |
|---|---|
| `MainActivity` | Home dashboard with MD3 cards and navigation entry points |
| `ComponentsActivity` | Full showcase of MD3 components (buttons, chips, FAB, sliders, switches, etc.) |
| `TypographyActivity` | Material Type Scale — Display, Headline, Title, Body, Label |
| `NavigationDemoActivity` | Bottom Nav Bar, Navigation Drawer, Navigation Rail |
| `TopAppBarActivity` | Small, Medium, Large, and Center-Aligned Top App Bars |
| `FeedbackActivity` | Snackbars, Progress indicators, Dialogs, Badges |
| `ListPatternsActivity` | Sticky headers, swipe-to-dismiss, stacked cards |
| `PickersTabsSearchActivity` | Date/Time pickers, Tab layouts, SearchView |
| `CarouselActivity` | Material 3 Carousel with RecyclerView |
| `AdvancedActivity` | Motion, shimmer, bottom sheets, reveal animations |

---

## 🏗️ Project Structure

```
md3showcase/
├── app/
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/md3showcase/
│       │   ├── MainActivity.java
│       │   ├── ComponentsActivity.java
│       │   ├── TypographyActivity.java
│       │   ├── NavigationDemoActivity.java
│       │   ├── TopAppBarActivity.java
│       │   ├── FeedbackActivity.java
│       │   ├── ListPatternsActivity.java
│       │   ├── PickersTabsSearchActivity.java
│       │   ├── CarouselActivity.java
│       │   ├── AdvancedActivity.java
│       │   ├── FileUtil.java
│       │   └── SketchwareUtil.java
│       └── res/
│           ├── drawable/         # Vector icons & shape backgrounds
│           ├── layout/           # XML layouts for all activities
│           ├── menu/             # Navigation & toolbar menus
│           ├── values/           # Colors, themes, strings, styles
│           └── values-night/     # Dark theme overrides
├── build.gradle
├── settings.gradle
└── gradle.properties
```

---

## ⚙️ Requirements

- **Android Studio** Hedgehog or newer (or **Sketchware Pro**)
- **Min SDK:** 21
- **Target SDK:** 35
- **Language:** Java
- **Dependencies:** Material Components for Android (`com.google.android.material`)

---

## 🚀 Getting Started

1. Clone or download the source zip from [Releases](../../releases)
2. Extract and open in **Android Studio** (`File → Open`)
3. Let Gradle sync
4. Run on a device or emulator (API 21+)

> **Sketchware Pro users:** Import the file structure directly from the zip. All layouts and Java files map to standard Sketchware project structure.

---

## 🎨 Theming

The app uses a full **Material You** dynamic color-compatible theme defined in `res/values/themes.xml` and `res/values-night/themes.xml`. Colors are defined in `res/values/colors.xml` with MD3 role naming (`md_theme_light_primary`, `md_theme_dark_surface`, etc.).

---

## 📦 Releases

| Version | Notes |
|---|---|
| v1.0.0 | Initial release — full source code |

APK will be added to future releases.

---

## 📄 License

```
MIT License — free to use, modify, and distribute.
```

---

> Built with ❤️ using native Android Java + Material Design 3
