# Kā Izskatās Android Studio ar Property Manager

## 📱 Galvenais Logs

```
┌──────────────────────────────────────────────────────────────────────────────────┐
│ File  Edit  View  Navigate  Code  Analyze  Refactor  Build  Run  Tools  Help    │
├──────────────────────────────────────────────────────────────────────────────────┤
│  ◀  ▶  ↻  PropertyManager  │  MainActivity.kt  ▼  │  [app] ▼  Pixel 6 API 33 ▼  ▶ 🐞 │
├────────┬──────────────────────────────────────────────────────────────────┬──────┤
│        │                                                                  │      │
│ 📁 app │  1  package com.propertymanager                                 │      │
│  📁 manifests                                                             │      │
│   📄 AndroidManifest.xml                                                 │      │
│  📁 java                                                                  │      │
│   📁 com.propertymanager                                                 │ C    │
│    📄 MainActivity.kt          ←  [Šis fails atvērts]                    │ o    │
│    📁 models                                                              │ d    │
│     📄 Issue.kt                                                           │ e    │
│     📄 Payment.kt                                                         │      │
│     📄 Property.kt                                                        │ S    │
│     📄 Tenant.kt                                                          │ t    │
│     📄 User.kt                                                            │ r    │
│     📄 UserRole.kt                                                        │ u    │
│    📁 repository                                                          │ c    │
│     📄 FirebaseRepository.kt                                              │ t    │
│    📁 ui                                                                  │ u    │
│     📄 Navigation.kt                                                      │ r    │
│     📁 screens                                                            │ e    │
│      📄 AddPaymentScreen.kt                                               │      │
│      📄 AddPropertyScreen.kt                                              │      │
│      📄 AddTenantScreen.kt                                                │      │
│      📄 LoginScreen.kt                                                    │      │
│      📄 OwnerDashboardScreen.kt                                           │      │
│      📄 PropertyDetailsScreen.kt                                          │      │
│      📄 ReportIssueScreen.kt                                              │      │
│      📄 RoleSelectionScreen.kt                                            │      │
│      📄 SignUpScreen.kt                                                   │      │
│      📄 TenantDashboardScreen.kt                                          │      │
│      📄 TenantRegistrationScreen.kt                                       │      │
│     📁 theme                                                              │      │
│      📄 Theme.kt                                                          │      │
│      📄 Type.kt                                                           │      │
│     📁 viewmodels                                                         │      │
│      📄 AuthViewModel.kt                                                  │      │
│      📄 IssueViewModel.kt                                                 │      │
│      📄 PaymentViewModel.kt                                               │      │
│      📄 PropertyViewModel.kt                                              │      │
│  📁 res                                                                   │      │
│   📁 values                                                               │      │
│    📄 strings.xml                                                         │      │
│    📄 themes.xml                                                          │      │
│   📁 xml                                                                  │      │
│    📄 backup_rules.xml                                                    │      │
│    📄 data_extraction_rules.xml                                           │      │
│  📄 build.gradle.kts                                                      │      │
│  📄 google-services.json                                                  │      │
│  📄 proguard-rules.pro                                                    │      │
│ 📁 Gradle Scripts                                                         │      │
│  📄 build.gradle.kts (Project)                                            │      │
│  📄 build.gradle.kts (Module: app)                                        │      │
│  📄 settings.gradle.kts                                                   │      │
│  📄 gradle.properties                                                     │      │
│                                                                           │      │
├───────┴──────────────────────────────────────────────────────────────────┴──────┤
│ 🔍 TODO  ⚠️ Problems  📋 Build  ▶️ Run  📱 Logcat  🔧 Terminal  📊 Profiler      │
│─────────────────────────────────────────────────────────────────────────────────│
│ Logcat:                                                                          │
│ 2024-01-15 10:30:45.123 12345-12345/com.propertymanager D/MainActivity: App... │
│ 2024-01-15 10:30:45.456 12345-12345/com.propertymanager I/FirebaseAuth: Si... │
└──────────────────────────────────────────────────────────────────────────────────┘
```

## 🎯 Run Configuration Izvēlne

Augšējā labajā stūrī:

```
┌─────────────────────────────────────────────────────┐
│ [app] ▼  │  Pixel 6 API 33 ▼  │  ▶  🐞  ⏹  │
└─────────────────────────────────────────────────────┘
   │           │                    │   │   │
   │           │                    │   │   └─ Stop
   │           │                    │   └───── Debug
   │           │                    └─────────  Run
   │           └──────────────────────────────  Device Selection
   └──────────────────────────────────────────  Configuration (app)
```

### Kad noklikšķina uz [app] ▼:
```
┌──────────────────────────────┐
│ ✓ app                        │  ← Jūsu izveidotā konfigurācija
│ ─────────────────────────    │
│ Edit Configurations...       │  ← Lai mainītu iestatījumus
│ Save 'app' Configuration     │
└──────────────────────────────┘
```

## ⚙️ Edit Configurations Logs

Kad atveriet "Edit Configurations...":

```
┌────────────────────────────────────────────────────────────────────────┐
│  Run/Debug Configurations                                       × │
├──────────────────┬─────────────────────────────────────────────────────┤
│                  │  Name: app                                          │
│ + ▼ Templates    │                                                     │
│ - ▼ Android App  │  Module: PropertyManager.app.main            ▼     │
│   ✓ app     ← Jūsu configuration                                      │
│                  │  Installation Options:                              │
│                  │  ☑ Deploy                                           │
│                  │  ☐ Deploy APK from app bundle                       │
│                  │                                                     │
│                  │  Launch Options:                                    │
│                  │  ⦿ Default Activity                                 │
│                  │  ○ Specified Activity                               │
│                  │  ○ Nothing                                          │
│                  │                                                     │
│                  │  Deployment Target Options:                         │
│                  │  Target: ⦿ Open Select Deployment Target Dialog    │
│                  │          ○ USB Device                               │
│                  │          ○ Emulator                                 │
│                  │                                                     │
│                  │  Before launch:                                     │
│                  │  ⚙ Gradle-aware Make                               │
│                  │                                                     │
├──────────────────┴─────────────────────────────────────────────────────┤
│                                          [Apply]  [OK]  [Cancel]       │
└────────────────────────────────────────────────────────────────────────┘
```

## 📱 Device Manager

Tools → Device Manager:

```
┌─────────────────────────────────────────────────┐
│  Device Manager                          ×      │
├─────────────────────────────────────────────────┤
│  [Virtual]  [Physical]                          │
├─────────────────────────────────────────────────┤
│                                                 │
│  📱 Pixel 6 API 33                              │
│     Android 13.0 (Tiramisu) | x86_64            │
│                              [▶] [✏] [⋮]        │
│                                                 │
│  📱 Pixel 5 API 30                              │
│     Android 11.0 (R) | x86                      │
│                              [▶] [✏] [⋮]        │
│                                                 │
│  [+] Create Device                              │
│                                                 │
└─────────────────────────────────────────────────┘
```

## 🔥 Firebase Assistant

Tools → Firebase:

```
┌─────────────────────────────────────────────────┐
│  Firebase                                       │
├─────────────────────────────────────────────────┤
│  🔍 Search Firebase features...                 │
├─────────────────────────────────────────────────┤
│  📊 Analytics                                   │
│  🔐 Authentication                    ▼         │
│    Email and password authentication           │
│    ✓ Connect to Firebase                       │
│    ✓ Add Firebase Authentication to your app   │
│  🗄 Cloud Firestore                  ▼         │
│    ✓ Connect to Firebase                       │
│    ✓ Add Cloud Firestore to your app          │
│  💾 Cloud Storage                    ▼         │
│    ✓ Connect to Firebase                       │
│    ✓ Add Cloud Storage to your app            │
└─────────────────────────────────────────────────┘
```

## 📊 Build Variants

Build → Select Build Variant:

```
┌─────────────────────────────────────┐
│  Build Variants                     │
├─────────────────────────────────────┤
│  Module        │  Active Build Variant│
├─────────────────┼──────────────────────┤
│  :app          │  debug          ▼   │
│                │  • debug             │
│                │  • release           │
└─────────────────────────────────────┘
```

## 🎨 Compose Preview

Kad atveriet screen failu ar @Preview:

```
┌──────────────────────────────────────────────────────────────────┐
│  RoleSelectionScreen.kt  │  [Code] [Split] [Design]              │
├──────────────────────────┼───────────────────────────────────────┤
│  Code                    │  Interactive Preview                  │
│  @Composable             │  ┌──────────────────────────────┐   │
│  fun RoleSelection...    │  │  Property Manager            │   │
│                          │  │  ──────────────────────────  │   │
│                          │  │                              │   │
│                          │  │  Izvēlieties lomu            │   │
│                          │  │                              │   │
│                          │  │  ┌────────────────────────┐ │   │
│                          │  │  │   🏢                   │ │   │
│                          │  │  │   Īpašnieks            │ │   │
│                          │  │  └────────────────────────┘ │   │
│                          │  │                              │   │
│                          │  │  ┌────────────────────────┐ │   │
│                          │  │  │   🏠                   │ │   │
│                          │  │  │   Īrnieks              │ │   │
│                          │  │  └────────────────────────┘ │   │
│                          │  │                              │   │
│                          │  └──────────────────────────────┘   │
│                          │                                      │
│                          │  ⚙ Build & Refresh  🔄  ↻  📱      │
└──────────────────────────┴───────────────────────────────────────┘
```

## 📋 Logcat Filtri

```
┌──────────────────────────────────────────────────────────────────┐
│  Logcat                                                           │
├──────────────────────────────────────────────────────────────────┤
│  [Pixel 6 API 33] ▼  [com.propertymanager] ▼  [Verbose] ▼  🔍  │
├──────────────────────────────────────────────────────────────────┤
│  2024-01-15 10:30:45.123  D/MainActivity: onCreate called        │
│  2024-01-15 10:30:45.234  I/FirebaseAuth: Checking auth state   │
│  2024-01-15 10:30:45.345  D/Compose: Recomposing screen          │
│  2024-01-15 10:30:45.456  I/PropertyViewModel: Loading...        │
└──────────────────────────────────────────────────────────────────┘
```

## 🔧 Terminal

View → Tool Windows → Terminal:

```
┌──────────────────────────────────────────────────────────────────┐
│  Terminal                                              ⚙  ×  -   │
├──────────────────────────────────────────────────────────────────┤
│  user@computer:~/PropertyManager$                                │
│  user@computer:~/PropertyManager$ ./gradlew assembleDebug        │
│  > Task :app:preBuild UP-TO-DATE                                 │
│  > Task :app:compileDebugKotlin                                  │
│  > Task :app:mergeDebugResources                                 │
│  > Task :app:processDebugManifest                                │
│  > Task :app:packageDebug                                        │
│                                                                   │
│  BUILD SUCCESSFUL in 45s                                         │
│  123 actionable tasks: 87 executed, 36 up-to-date                │
│  user@computer:~/PropertyManager$ _                              │
└──────────────────────────────────────────────────────────────────┘
```

## 🎯 Kā Atvērt Katru Logu

| Logs              | Keyboard Shortcut | Menu Path                    |
|-------------------|-------------------|------------------------------|
| Project           | Alt+1 / Cmd+1     | View → Tool Windows → Project|
| Logcat            | Alt+6 / Cmd+6     | View → Tool Windows → Logcat |
| Terminal          | Alt+F12 / Cmd+F12 | View → Tool Windows → Terminal|
| Build             | Alt+4 / Cmd+4     | View → Tool Windows → Build  |
| Run               | Alt+4 / Cmd+4     | View → Tool Windows → Run    |
| Device Manager    | -                 | Tools → Device Manager       |
| Firebase          | -                 | Tools → Firebase             |
| Gradle            | -                 | View → Tool Windows → Gradle |

## 🎨 Tēmas

Android Studio var izmantot:
- **Light Mode**: File → Settings → Appearance → Theme → IntelliJ Light
- **Dark Mode**: File → Settings → Appearance → Theme → Darcula
- **High Contrast**: File → Settings → Appearance → Theme → High Contrast

## 💡 Noderīgi Padomi

1. **Double Shift** - Meklēt jebko projektā
2. **Ctrl+N / Cmd+O** - Meklēt klasi
3. **Ctrl+Shift+N / Cmd+Shift+O** - Meklēt failu
4. **Ctrl+Alt+L / Cmd+Opt+L** - Formatēt kodu
5. **Ctrl+/** - Komentēt/atkomentēt
6. **Alt+Enter** - Quick fix
7. **Ctrl+Space** - Code completion
8. **Ctrl+Shift+A / Cmd+Shift+A** - Find Action

## 🚀 Kad viss ir gatavs

Jūsu Android Studio izskatīsies apmēram kā ASCII art augšā, ar:
- ✅ Projekta koks kreisajā pusē
- ✅ Koda editors centrā
- ✅ Run configuration augšā labajā stūrī
- ✅ Tools (Logcat, Terminal) apakšā
- ✅ Device Manager pieejams
- ✅ Gradle sync pabeigts

Tagad varat sākt kodēt! 🎉
