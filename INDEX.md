# 📚 Property Manager - Dokumentācijas Indekss

Visu dokumentāciju un resursu saraksts Property Manager Android aplikācijai.

---

## 🎯 Sākt Šeit

### ⚡ Ātrs Sākums
**[QUICK_START.md](QUICK_START.md)** - SĀCIET NO ŠEJIENES!
- Visas galvenās komandas vienā vietā
- Firebase iestatīšana 5 minūtēs
- Build un run instrukcijas
- Bieži sastopamās problēmas

---

## 📖 Galvenā Dokumentācija

### 1. 📱 Aplikācijas Apraksts
**[README.md](README.md)**
- Projekta pārskats
- Galvenās funkcijas (īpašniekiem un īrniekiem)
- Tehnoloģiju steks
- Firebase konfigurācija
- Projekta struktūra
- Build instrukcijas

### 2. 💻 Android Studio Iestatīšana
**[ANDROID_STUDIO_SETUP.md](ANDROID_STUDIO_SETUP.md)** - Detalizēta instrukcija
- Kā atvērt projektu
- Edit Configuration iestatīšana
- Emulator setup
- Fiziskas ierīces pievienošana
- Firebase detalizēta konfigurācija
- Debugging tools
- Problēmu risināšana
- Gradle komandas

### 3. 🎨 Android Studio Vizualizācija
**[ANDROID_STUDIO_LAYOUT.md](ANDROID_STUDIO_LAYOUT.md)** - Vizuāls ceļvedis
- Android Studio galvenais logs (ASCII art)
- Edit Configurations logs
- Device Manager
- Firebase Assistant
- Logcat un filtri
- Compose Preview
- Keyboard shortcuts
- Tips & tricks

### 4. 📸 UI Preview
**[APP_SCREENSHOTS.md](APP_SCREENSHOTS.md)** - Aplikācijas UI
- Visu 11 ekrānu vizualizācija
- Lomu izvēles ekrāns
- Īpašnieka dashboard
- Īrnieka dashboard
- Maksājumu sistēma
- Problēmu ziņošana
- Krāsu paletes
- Badge un statusi
- Interaktivitāte

---

## 🔧 Utility Scripts

### 5. 🔄 Git Reset Skripts
**[reset_to_remote.sh](reset_to_remote.sh)** - Executable script
```bash
./reset_to_remote.sh
```
- Drošs veids reset projektu
- Interaktīvs apstiprinājums
- Pārraksta lokālās izmaiņas ar remote
- Notīra untracked failus

### 6. 📊 Status Checker
**[check_status.sh](check_status.sh)** - Executable script
```bash
./check_status.sh
```
- Git status pārbaude
- Sync ar remote
- Failu skaits
- Nepieciešamie faili
- Gradle un build status

---

## 📘 Git Operācijas

### 7. 🔀 Git Operāciju Pamācība
**[GIT_OPERATIONS.md](GIT_OPERATIONS.md)** - Pilna Git dokumentācija
- Kā ielādēt visu no Git
- Reset operācijas
- Saglabāt izmaiņas pirms reset
- Bieži sastopamās Git problēmas
- Android Studio specifiskas darbības
- Best practices
- Sync ar remote

---

## 📁 Projekta Struktūra

### Kods

```
app/src/main/java/com/propertymanager/
├── MainActivity.kt              # Galvenā aktivitāte + navigācija
│
├── models/                      # Datu modeļi (6 faili)
│   ├── User.kt
│   ├── UserRole.kt
│   ├── Property.kt
│   ├── Tenant.kt
│   ├── Payment.kt
│   └── Issue.kt
│
├── repository/                  # Firebase operācijas
│   └── FirebaseRepository.kt
│
├── ui/
│   ├── Navigation.kt           # Screen routing
│   │
│   ├── screens/                # 11 UI ekrāni
│   │   ├── RoleSelectionScreen.kt
│   │   ├── LoginScreen.kt
│   │   ├── SignUpScreen.kt
│   │   ├── OwnerDashboardScreen.kt
│   │   ├── TenantDashboardScreen.kt
│   │   ├── PropertyDetailsScreen.kt
│   │   ├── AddPropertyScreen.kt
│   │   ├── AddTenantScreen.kt
│   │   ├── TenantRegistrationScreen.kt
│   │   ├── AddPaymentScreen.kt
│   │   └── ReportIssueScreen.kt
│   │
│   ├── viewmodels/             # Business logic (4 ViewModels)
│   │   ├── AuthViewModel.kt
│   │   ├── PropertyViewModel.kt
│   │   ├── PaymentViewModel.kt
│   │   └── IssueViewModel.kt
│   │
│   └── theme/                  # Material Design 3
│       ├── Theme.kt
│       └── Type.kt
```

### Konfigurācija

```
PropertyManager/
├── app/
│   ├── build.gradle.kts        # App dependencies
│   ├── google-services.json    # Firebase config
│   └── proguard-rules.pro      # ProGuard rules
│
├── .idea/                      # Android Studio config
│   ├── runConfigurations/
│   │   └── app.xml            # Run configuration
│   ├── compiler.xml
│   └── misc.xml
│
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
│
├── build.gradle.kts            # Project-level Gradle
├── settings.gradle.kts         # Gradle settings
├── gradle.properties           # Gradle properties
├── gradlew                     # Gradle wrapper (Unix)
└── gradlew.bat                # Gradle wrapper (Windows)
```

---

## 🎯 Quick Links

| Vajadzība | Fails |
|-----------|-------|
| 🚀 Es sāku pirmo reizi | [QUICK_START.md](QUICK_START.md) |
| 💻 Kā atvērt Android Studio? | [ANDROID_STUDIO_SETUP.md](ANDROID_STUDIO_SETUP.md) |
| 🔥 Kā iestatīt Firebase? | [QUICK_START.md](QUICK_START.md#-firebase-iestatīšana-obligāts) |
| 🔄 Reset uz remote | `./reset_to_remote.sh` |
| 📊 Pārbaudīt statusu | `./check_status.sh` |
| 🎨 Kā izskatās UI? | [APP_SCREENSHOTS.md](APP_SCREENSHOTS.md) |
| 🔀 Git problēmas | [GIT_OPERATIONS.md](GIT_OPERATIONS.md) |
| 🐛 Debugging | [ANDROID_STUDIO_SETUP.md](ANDROID_STUDIO_SETUP.md#-debugging) |
| ⚙️ Edit Configuration | [ANDROID_STUDIO_SETUP.md](ANDROID_STUDIO_SETUP.md#-edit-configuration-iestatīšana) |
| 📱 Emulator setup | [ANDROID_STUDIO_SETUP.md](ANDROID_STUDIO_SETUP.md#-ierīces-iestatīšana) |

---

## 🎓 Mācību Ceļš

### Pilnīgs iesācējs?

1. ✅ Lasīt [QUICK_START.md](QUICK_START.md)
2. ✅ Instalēt Android Studio
3. ✅ Sekot [ANDROID_STUDIO_SETUP.md](ANDROID_STUDIO_SETUP.md)
4. ✅ Iestatīt Firebase
5. ✅ Run aplikāciju!

### Jau zini Android?

1. ✅ Ātrs pārskats: [README.md](README.md)
2. ✅ Atvērt projektu Android Studio
3. ✅ Run `./check_status.sh`
4. ✅ Firebase setup
5. ✅ Build & Run!

### Git problēmas?

1. ✅ [GIT_OPERATIONS.md](GIT_OPERATIONS.md)
2. ✅ Vai vienkārši: `./reset_to_remote.sh`

---

## 📊 Statistika

| Kategorija | Skaits |
|------------|--------|
| **Kotlin Faili** | 26 |
| UI Screens | 11 |
| ViewModels | 4 |
| Models | 6 |
| **Dokumentācija** | 8 faili |
| **Utility Scripts** | 2 |
| **Koda Rindiņas** | ~4,300 |

---

## 🔑 Galvenie Koncepti

### Arhitektūra
- **MVVM** (Model-View-ViewModel)
- **Repository Pattern** (FirebaseRepository)
- **Unidirectional Data Flow** (StateFlow)

### UI
- **Jetpack Compose** - Deklaratīvs UI
- **Material Design 3** - Moderna tēma
- **Navigation Compose** - Screen routing

### Backend
- **Firebase Authentication** - User login
- **Cloud Firestore** - NoSQL datubāze
- **Firebase Storage** - Attēlu glabāšana

---

## 🆘 Palīdzība

### Bieži Uzdotie Jautājumi

**Q: Kur sākt?**
A: [QUICK_START.md](QUICK_START.md)

**Q: Kā reset projektu?**
A: `./reset_to_remote.sh`

**Q: Firebase nedarbojas?**
A: Pārbaudi `app/google-services.json` un [QUICK_START.md](QUICK_START.md#-firebase-iestatīšana-obligāts)

**Q: Gradle sync failed?**
A: `./gradlew clean` + File → Invalidate Caches

**Q: Kur ir kods?**
A: `app/src/main/java/com/propertymanager/`

---

## 📝 Dokumentācijas Izmaiņu Vēsture

| Datums | Izmaiņas |
|--------|----------|
| 2024 | Izveidoti visi pamata dokumenti |
| 2024 | Pievienoti utility scripts |
| 2024 | Pievienots INDEX.md |

---

## 🌟 Noderīgi Resursi

### Android Development
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)

### Firebase
- [Firebase Console](https://console.firebase.google.com/)
- [Firebase Android Setup](https://firebase.google.com/docs/android/setup)
- [Firestore Documentation](https://firebase.google.com/docs/firestore)

### Git
- [Git Documentation](https://git-scm.com/doc)
- [GitHub Guides](https://guides.github.com/)

---

## ✅ Pārbaudes Saraksts

Pirms sākat:

- [ ] Android Studio instalēts
- [ ] JDK 17 instalēts
- [ ] Git instalēts
- [ ] Firebase konts izveidots

Pirms pirmās palaišanas:

- [ ] Projekts atvērts Android Studio
- [ ] Gradle sync pabeigts
- [ ] Firebase projekts izveidots
- [ ] google-services.json lejupielādēts
- [ ] Authentication ieslēgts
- [ ] Firestore izveidots
- [ ] Storage izveidots

---

**🎉 Tagad esat gatavs sākt ar Property Manager!**

Izmantojiet šo INDEX.md kā ceļvedi pa visu dokumentāciju.
