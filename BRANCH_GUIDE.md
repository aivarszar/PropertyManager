# Property Manager - Branch Ceļvedis

Property Manager ir pieejams divās versijās: **Android (Kotlin + Firebase)** un **Web (PHP + MySQL)**.

---

## 📱 Android Versija

**Branch:** `claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi`

### Pārslēgties uz Android:

```bash
git checkout claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

### Tehnoloģijas:
- Kotlin
- Jetpack Compose
- Firebase (Auth, Firestore, Storage)
- Material Design 3
- MVVM arhitektūra

### Dokumentācija:
- `README.md` - Galvenais apraksts
- `QUICK_START.md` - Ātrs sākums
- `ANDROID_STUDIO_SETUP.md` - Android Studio setup
- `INDEX.md` - Visu dokumentāciju saraksts

### Galvenie Faili:
```
app/src/main/java/com/propertymanager/
├── MainActivity.kt
├── models/ (6 faili)
├── ui/
│   ├── screens/ (11 ekrāni)
│   └── viewmodels/ (4 ViewModels)
```

### Run:
1. Open in Android Studio
2. Sync Gradle
3. Setup Firebase
4. Run on emulator/device

---

## 🌐 PHP + MySQL Versija

**Branch:** `claude/property-manager-php-mysql-01SKf6hNvrFLNzsS4iA7mQRi`

### Pārslēgties uz PHP:

```bash
git checkout claude/property-manager-php-mysql-01SKf6hNvrFLNzsS4iA7mQRi
```

### Tehnoloģijas:
- PHP 7.4+
- MySQL 5.7+
- PDO
- Session-based auth
- HTML/CSS/JavaScript

### Dokumentācija:
- `README.md` - Galvenais apraksts  
- `PHP_VERSION_SUMMARY.md` - Pilns pārskats
- Instalācijas vednis: `install.php`

### Galvenie Faili:
```
PropertyManager/
├── install.php           # Instalācijas vednis
├── public/
│   └── index.php        # Sākuma lapa
├── src/
│   ├── Database.php     # DB klase
│   └── Auth.php         # Auth klase
├── database/
│   └── schema.sql       # MySQL shēma
└── config/
    └── config.example.php
```

### Run:
1. `chmod 777 public/uploads/ logs/ config/`
2. Open `http://localhost/install.php`
3. Follow 5-step wizard
4. Delete `install.php`
5. Open `http://localhost/`

---

## 🔄 Pārslēgšanās Starp Versijām

### No Android uz PHP:

```bash
# Save current work (if any)
git stash

# Switch to PHP
git checkout claude/property-manager-php-mysql-01SKf6hNvrFLNzsS4iA7mQRi

# Pull latest
git pull origin claude/property-manager-php-mysql-01SKf6hNvrFLNzsS4iA7mQRi
```

### No PHP uz Android:

```bash
# Save current work (if any)
git stash

# Switch to Android
git checkout claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi

# Pull latest
git pull origin claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

### Pārbaudīt Pašreizējo Branch:

```bash
git branch
```

Output:
```
* claude/property-manager-php-mysql-01SKf6hNvrFLNzsS4iA7mQRi   # Aktīvais
  claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

---

## 📊 Salīdzinājums

| Feature | Android | PHP |
|---------|---------|-----|
| **Platform** | Mobile App | Web App |
| **Language** | Kotlin | PHP |
| **UI** | Jetpack Compose | HTML/CSS/JS |
| **Backend** | Firebase | MySQL |
| **Auth** | Firebase Auth | Session + DB |
| **Storage** | Firebase Storage | Local files |
| **Real-time** | Firestore | Manual refresh |
| **Offline** | Yes | No |
| **Installation** | APK install | Web server |
| **Updates** | App update | Server update |

---

## 🎯 Kad Izvēlēties Kuru?

### Izvēlieties **Android**, ja:
- Vēlaties mobile app
- Vajag offline funkcionalitāti
- Vēlaties real-time updates
- Vēlaties izmantot Firebase ecosystem
- Plānojat Google Play release

### Izvēlieties **PHP**, ja:
- Vēlaties web app (pieejams jebkurā pārlūkprogrammā)
- Jau ir web hosting
- Vēlaties pilnu kontroli pār datiem
- Nepieciešama custom backend loģika
- Budget constraints (nav Firebase costs)

---

## 🔧 Development Workflow

### Strādāt uz Abām Versijām:

1. **Izveidot feature uz Android:**
   ```bash
   git checkout claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
   git checkout -b feature/my-feature-android
   # ... develop ...
   git commit -am "Add feature"
   ```

2. **Port to PHP:**
   ```bash
   git checkout claude/property-manager-php-mysql-01SKf6hNvrFLNzsS4iA7mQRi
   git checkout -b feature/my-feature-php
   # ... port feature ...
   git commit -am "Add feature (PHP version)"
   ```

3. **Merge back:**
   ```bash
   # Merge Android feature
   git checkout claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
   git merge feature/my-feature-android

   # Merge PHP feature
   git checkout claude/property-manager-php-mysql-01SKf6hNvrFLNzsS4iA7mQRi
   git merge feature/my-feature-php
   ```

---

## 📁 Project Files By Branch

### Android Branch Faili:

```
app/
gradlew, gradlew.bat
build.gradle.kts
settings.gradle.kts
ANDROID_STUDIO_*.md
APP_SCREENSHOTS.md
QUICK_START.md
INDEX.md
```

### PHP Branch Faili:

```
public/
src/
config/
database/
logs/
install.php
.htaccess
PHP_VERSION_SUMMARY.md
```

### Kopīgie Faili (abos branch):

```
README.md
.gitignore
GIT_OPERATIONS.md
check_status.sh
reset_to_remote.sh
```

---

## ⚠️ Svarīgi

1. **Neizmantojiet `git merge` starp Android un PHP branch!**
   - Tie ir pilnīgi atšķirīgi projekti
   - Izmantojiet manual porting features

2. **Config faili ir atšķirīgi:**
   - Android: `app/google-services.json`
   - PHP: `config/config.php`
   - Abi ir `.gitignore`

3. **Pull pirms work:**
   ```bash
   git pull origin <current-branch>
   ```

4. **Pārbaudīt remote sync:**
   ```bash
   ./check_status.sh
   ```

---

## 🚀 Quick Commands

```bash
# List all branches
git branch -a

# Current branch
git branch --show-current

# Switch to Android
git checkout claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi

# Switch to PHP
git checkout claude/property-manager-php-mysql-01SKf6hNvrFLNzsS4iA7mQRi

# Pull latest
git pull

# Check status
./check_status.sh

# Reset to remote
./reset_to_remote.sh
```

---

## 📚 Dokumentācija

### Android:
- Start: `INDEX.md` vai `QUICK_START.md`
- Full setup: `ANDROID_STUDIO_SETUP.md`
- UI preview: `APP_SCREENSHOTS.md`

### PHP:
- Start: `README.md`
- Full guide: `PHP_VERSION_SUMMARY.md`
- Install: Open `install.php` in browser

---

**Happy coding! 🎉**

Jautājumi? Check dokumentāciju vai run `./check_status.sh`
