# Android Studio Iestatīšana un Palaišana

## 📥 1. Projekta Atvēršana Android Studio

### Metode 1: Atvērt eksistējošu projektu
1. Atveriet Android Studio
2. Izvēlieties **"Open"** vai **"Open an Existing Project"**
3. Navigējiet uz projekta direktoriju: `/home/user/PropertyManager`
4. Noklikšķiniet uz **"OK"**

### Metode 2: No komandrindas
```bash
cd /home/user/PropertyManager
studio .
```

## ⚙️ 2. Pirmā Atvēršana

Kad atversiet projektu pirmo reizi, Android Studio:

1. **Sāks sinhronizēt Gradle**
   - Apakšējā daļā redzēsiet: "Gradle Sync in Progress..."
   - Tas var aizņemt 2-5 minūtes
   - Gaidiet, līdz pabeigs!

2. **Lejupielādēs atkarības**
   - Kotlin bibliotēkas
   - Jetpack Compose
   - Firebase SDK
   - Citas atkarības no `app/build.gradle.kts`

3. **Indeksēs failus**
   - Labējā apakšējā stūrī redzēsiet progresu

## 🔧 3. Edit Configuration Iestatīšana

### Automātiskā Konfigurācija (Ieteicams)
Es jau esmu izveidojis run configuration! Jums vajadzētu redzēt:

1. Augšējā labējā stūrī blakus ▶️ (Run) pogai
2. Dropdown menu ar nosaukumu **"app"**
3. Ja to neredz, skatiet manuālo metodi zemāk

### Manuālā Konfigurācija
Ja automātiskā konfigurācija nedarbojas:

1. Noklikšķiniet uz dropdown (augšā labajā stūrī)
2. Izvēlieties **"Edit Configurations..."**
3. Noklikšķiniet **"+"** (Add New Configuration)
4. Izvēlieties **"Android App"**
5. Iestatiet:
   - **Name:** `app`
   - **Module:** `PropertyManager.app.main`
   - **Deploy:** Default Activity
   - **Target:** Device/Emulator (jūsu izvēle)

6. Noklikšķiniet **"Apply"** un **"OK"**

## 📱 4. Ierīces Iestatīšana

### Opcija A: Android Emulator (Ieteicams sākumā)

1. **Izveidot jaunu emulatoru:**
   - Tools → Device Manager
   - Noklikšķiniet **"Create Device"**
   - Izvēlieties ierīci (piemēram, "Pixel 6")
   - Izvēlieties System Image (piemēram, "Tiramisu" - Android 13, API 33)
   - Ja nav lejupielādēts, noklikšķiniet "Download"
   - Noklikšķiniet "Finish"

2. **Palaidiet emulatoru:**
   - Device Manager → Jūsu ierīce → ▶️ (Launch)

### Opcija B: Fiziska ierīce

1. **Android ierīcē:**
   - Atveriet Settings
   - Dodieties uz "About Phone"
   - Spiediet "Build Number" 7 reizes (aktivizē Developer Mode)
   - Dodieties atpakaļ → Developer Options
   - Ieslēdziet "USB Debugging"

2. **Savienojiet ar datoru:**
   - Pievienojiet USB kabeli
   - Ierīcē apstiprināt "Allow USB debugging"
   - Android Studio automātiski atpazīs ierīci

## 🚀 5. Aplikācijas Palaišana

### Metode 1: Ar pogu
1. Pārliecinieties, ka izvēlēts **"app"** configuration
2. Izvēlieties ierīci/emulatoru dropdown
3. Noklikšķiniet ▶️ **"Run"** pogu (vai Shift+F10)

### Metode 2: Debug režīmā
1. Noklikšķiniet 🐞 **"Debug"** pogu (vai Shift+F9)
2. Ļauj izmantot breakpoints un debug funkcijas

### Metode 3: Terminālis
```bash
./gradlew installDebug
```

## 🔥 6. Firebase Konfigurācija (SVARĪGI!)

Pirms pirmās palaišanas:

1. **Dodieties uz Firebase Console:**
   - https://console.firebase.google.com/

2. **Izveidojiet projektu:**
   - Click "Add project" vai "Create a project"
   - Nosaukums: "Property Manager" (vai jebkurš cits)
   - Izslēdziet Google Analytics (nav nepieciešams)
   - Click "Create project"

3. **Pievienojiet Android aplikāciju:**
   - Projekta pārskatā → Click Android ikona
   - Android package name: `com.propertymanager`
   - App nickname: "Property Manager"
   - Click "Register app"

4. **Lejupielādējiet google-services.json:**
   - Click "Download google-services.json"
   - Aizstājiet `/home/user/PropertyManager/app/google-services.json` ar lejupielādēto

5. **Ieslēdziet Authentication:**
   - Firebase Console → Build → Authentication
   - Click "Get started"
   - Sign-in methods → Email/Password → Enable
   - Click "Save"

6. **Izveidojiet Firestore Database:**
   - Firebase Console → Build → Firestore Database
   - Click "Create database"
   - Izvēlieties "Start in test mode"
   - Izvēlieties region (piemēram, "europe-west1")
   - Click "Enable"

7. **Izveidojiet Storage:**
   - Firebase Console → Build → Storage
   - Click "Get started"
   - Start in test mode
   - Click "Done"

## 📂 7. Projekta Struktūra Android Studio

```
PropertyManager/
├── 📁 app/
│   ├── 📁 manifests/
│   │   └── AndroidManifest.xml           # App permissions un components
│   ├── 📁 java/
│   │   └── com.propertymanager/
│   │       ├── MainActivity.kt            # Galvenā aktivitāte
│   │       ├── 📁 models/                # Datu modeļi
│   │       ├── 📁 repository/            # Firebase operācijas
│   │       ├── 📁 ui/
│   │       │   ├── 📁 screens/          # UI ekrāni
│   │       │   ├── 📁 viewmodels/       # Business logic
│   │       │   └── 📁 theme/            # UI tēma
│   ├── 📁 res/                           # Resources
│   │   ├── 📁 values/
│   │   │   ├── strings.xml              # Teksta strings
│   │   │   └── themes.xml               # Tēmas
│   ├── build.gradle.kts                  # App-level Gradle
│   └── google-services.json              # Firebase config
├── build.gradle.kts                      # Project-level Gradle
├── settings.gradle.kts                   # Gradle settings
└── gradle.properties                     # Gradle properties
```

## 🎨 8. Kā Apskatīt UI Preview

### Compose Preview:
1. Atveriet jebkuru screen failu (piemēram, `RoleSelectionScreen.kt`)
2. Labējā pusē redzēsiet "Split" vai "Design" tab
3. Noklikšķiniet, lai redzētu UI preview
4. Ja nerāda, pārbaudiet vai ir `@Preview` anotācija

### Layout Inspector (Running app):
1. Palaidiet aplikāciju
2. Tools → Layout Inspector
3. Redzēsiet real-time UI hierarhiju

## 🐛 9. Debugging

### Logcat:
1. Apakšējā daļā → Logcat tab
2. Filtrējiet pēc package: `com.propertymanager`
3. Redzēsiet visus log messages

### Breakpoints:
1. Noklikšķiniet uz rindas numura, lai pievienotu breakpoint
2. Palaidiet Debug režīmā
3. Aplikācija apstāsies pie breakpoint

## ❗ 10. Bieži Sastopamās Problēmas

### "Gradle sync failed"
**Risinājums:**
```bash
# Termināli:
./gradlew clean
# Tad Android Studio: File → Invalidate Caches → Invalidate and Restart
```

### "SDK not found"
**Risinājums:**
1. File → Project Structure → SDK Location
2. Iestatiet Android SDK path (parasti `~/Android/Sdk`)

### "google-services.json not found"
**Risinājums:**
- Pārliecinieties, ka esat lejupielādējis failu no Firebase
- Faila atrašanās vieta: `app/google-services.json`

### "Manifest merger failed"
**Risinājums:**
1. Build → Clean Project
2. Build → Rebuild Project

### Emulator nedarbojas
**Risinājums:**
1. Tools → SDK Manager → SDK Tools
2. Pārbaudiet vai instalēts "Android Emulator"
3. Tools → Device Manager → Delete un Create jaunu

## 📊 11. Gradle Commands

Varat izmantot termināli (View → Tool Windows → Terminal):

```bash
# Clean project
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on device
./gradlew installDebug

# Run tests
./gradlew test

# Check dependencies
./gradlew dependencies
```

## 🎯 12. Kā Testēt Aplikāciju

### Īpašnieka Flow:
1. Launch app
2. Izvēlieties "Īpašnieks"
3. Reģistrējieties ar e-pastu un paroli
4. Pievienojiet īpašumu
5. Pievienojiet īrnieku
6. Nokopējiet ģenerēto kodu

### Īrnieka Flow:
1. Launch app (citā emulatorā vai izmantojiet logout)
2. Izvēlieties "Īrnieks"
3. Reģistrējieties
4. Ievadiet uzaicinājuma kodu
5. Skatiet maksājumus

## 📸 13. Logcat Filteri

Noderīgi filtri:
```
# Tikai errors
level:error

# Jūsu aplikācija
package:com.propertymanager

# Firebase
tag:FirebaseAuth

# Crash reports
tag:AndroidRuntime
```

## 🔍 14. Profiler Tools

### CPU Profiler:
- View → Tool Windows → Profiler
- Ļauj redzēt CPU lietojumu

### Memory Profiler:
- Redzēt memory usage un leaks

### Network Profiler:
- Firebase API calls

## ✅ 15. Pārbaudes Saraksts Pirms Palaišanas

- [ ] Android Studio instalēts
- [ ] JDK 17 instalēts
- [ ] Android SDK instalēts
- [ ] Gradle sync pabeigts bez kļūdām
- [ ] Firebase projekts izveidots
- [ ] google-services.json lejupielādēts un aizstāts
- [ ] Email/Password authentication ieslēgts Firebase
- [ ] Firestore database izveidots
- [ ] Storage izveidots
- [ ] Emulator vai fiziska ierīce pieejama
- [ ] Run configuration ("app") izveidots

## 🎉 Gatavs Sākt!

Tagad varat:
1. ▶️ Run the app
2. 🐞 Debug ar breakpoints
3. 📱 Test uz emulator vai real device
4. 🔥 Skatīt Firebase datus real-time

Veiksmi ar aplikācijas izstrādi! 🚀
