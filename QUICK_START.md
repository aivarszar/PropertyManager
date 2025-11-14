# ⚡ Property Manager - Ātrs Sākums

## 🎯 Galvenās Komandas

### 📊 Pārbaudīt Projekta Statusu

```bash
./check_status.sh
```

Parāda:
- ✅ Git status un lokālās izmaiņas
- ✅ Pašreizējo branch
- ✅ Sinhronizāciju ar remote
- ✅ Pēdējos commits
- ✅ Failu skaitu
- ✅ Vai ir visi nepieciešamie faili

---

### 🔄 Ielādēt No Git (Reset)

```bash
./reset_to_remote.sh
```

Automātiski:
- 📥 Lejupielādē jaunākās izmaiņas
- 🗑️ Dzēš visas lokālās izmaiņas
- 🧹 Notīra untracked failus
- ✅ Pārslēdzas uz pareizo branch

**Vai manuāli:**

```bash
# Ātrs reset
git fetch origin && \
git reset --hard origin/claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi && \
git clean -fd
```

---

## 🚀 Android Studio

### Atvērt Projektu

```bash
cd /home/user/PropertyManager
studio .
```

Vai: **File → Open → PropertyManager**

### Pirmā Atvēršana

1. ⏳ Nogaidiet Gradle Sync (2-5 min)
2. 🔥 Iestatiet Firebase (skatīt zemāk)
3. ▶️ Run aplikāciju

---

## 🔥 Firebase Iestatīšana (OBLIGĀTS!)

### 1. Izveidot Firebase Projektu

1. Dodieties: https://console.firebase.google.com/
2. **Add project** → Nosaukums: "Property Manager"
3. Disable Google Analytics
4. **Create project**

### 2. Pievienot Android App

1. Click Android ikona
2. Package name: `com.propertymanager`
3. App nickname: "Property Manager"
4. **Register app**

### 3. Lejupielādēt Config

1. **Download google-services.json**
2. Aizstāt failu:

```bash
# Pārvietot lejupielādēto failu
cp ~/Downloads/google-services.json /home/user/PropertyManager/app/google-services.json
```

### 4. Ieslēgt Servīsus

**Authentication:**
- Build → Authentication → Get started
- Sign-in methods → Email/Password → Enable

**Firestore:**
- Build → Firestore Database → Create database
- Test mode → Region: europe-west1

**Storage:**
- Build → Storage → Get started
- Test mode → Done

✅ **Gatavs!**

---

## 🏗️ Build Operācijas

```bash
# Clean project
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Build + Install + Run
./gradlew installDebug && adb shell am start -n com.propertymanager/.MainActivity
```

---

## 📱 Emulator/Device

### Palaist Emulatoru

Android Studio:
- Tools → Device Manager
- Izvēlieties ierīci → ▶️

### Fiziska Ierīce

1. Ierīcē: Settings → About → Tap "Build Number" 7x
2. Developer Options → USB Debugging ✓
3. Pievienojiet USB
4. Apstiprināt "Allow USB debugging"

---

## 🔍 Debugging

```bash
# View logs
adb logcat | grep "PropertyManager"

# View app logs
adb logcat -s "MainActivity:D" "FirebaseAuth:D"

# Clear app data
adb shell pm clear com.propertymanager
```

---

## 📋 Projekta Informācija

| Info | Vērtība |
|------|---------|
| Package | `com.propertymanager` |
| Branch | `claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi` |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Backend | Firebase |

---

## 📚 Dokumentācija

| Fails | Saturs |
|-------|--------|
| `README.md` | Galvenais projekta apraksts |
| `ANDROID_STUDIO_SETUP.md` | Detalizēta Android Studio instrukcija |
| `ANDROID_STUDIO_LAYOUT.md` | Android Studio vizuālais pārskats |
| `APP_SCREENSHOTS.md` | Aplikācijas UI preview |
| `GIT_OPERATIONS.md` | Git operāciju pamācība |
| `QUICK_START.md` | Šis fails - ātrs sākums |

---

## ⚡ Ātrie Scenāriji

### Scenario 1: Pirmo Reizi Atveru

```bash
cd /home/user/PropertyManager
./check_status.sh           # Pārbaudīt statusu
# Atvērt Android Studio
# Nogaidīt Gradle sync
# Iestatīt Firebase
# Run ▶️
```

### Scenario 2: Kaut Kas Salūza, Gribu Reset

```bash
cd /home/user/PropertyManager
./reset_to_remote.sh        # Interaktīvs reset
# Vai
git fetch origin && git reset --hard origin/claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

### Scenario 3: Pārbaudīt Vai Viss OK

```bash
cd /home/user/PropertyManager
./check_status.sh           # Status check
./gradlew assembleDebug     # Test build
```

### Scenario 4: Gradle Problēmas

```bash
./gradlew clean
rm -rf .gradle
rm -rf app/build
# Android Studio: File → Invalidate Caches → Restart
```

### Scenario 5: Nav google-services.json

1. Firebase Console → Project Settings ⚙️
2. Your apps → Android app
3. Download google-services.json
4. Copy to `app/google-services.json`

---

## 🎯 Pārbaudes Saraksts Pirms Run

- [ ] Git status clean: `./check_status.sh`
- [ ] Firebase projekts izveidots
- [ ] google-services.json ir uz vietas
- [ ] Authentication ieslēgts
- [ ] Firestore izveidots
- [ ] Storage izveidots
- [ ] Gradle sync pabeigts
- [ ] Emulator/Device pieejams
- [ ] Run configuration `[app]` izvēlēts

---

## 🆘 Palīdzība

### Gradle sync failed
```bash
./gradlew clean
# File → Invalidate Caches → Restart
```

### Can't find google-services.json
```bash
ls -la app/google-services.json
# Ja nav, lejupielādēt no Firebase
```

### Build failed
```bash
./gradlew clean assembleDebug --stacktrace
```

### Emulator won't start
```bash
# Tools → Device Manager → Delete → Create new
```

### App crashes immediately
```bash
# Pārbaudīt Firebase setup
# Pārbaudīt Logcat kļūdas
adb logcat | grep -E "AndroidRuntime|FirebaseAuth"
```

---

## 💡 Pro Padomi

1. **Izmantojiet skriptus** - `./check_status.sh` un `./reset_to_remote.sh`
2. **Regulāri sync** - File → Sync Project with Gradle Files
3. **Invalidate caches** ja dīvaini - File → Invalidate Caches
4. **Logcat filtri** - Package: com.propertymanager
5. **Keyboard shortcuts** - Double Shift (search anywhere)

---

## 🚀 Gatavs Sākt!

```bash
# 1. Status check
./check_status.sh

# 2. Open Android Studio
studio .

# 3. Run app
# Click ▶️ Run button
```

**Veiksmi ar Property Manager aplikāciju!** 🎉
