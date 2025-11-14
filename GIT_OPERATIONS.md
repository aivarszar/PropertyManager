# Git Operācijas - Property Manager

## 🔄 Kā Ielādēt Visu No Git, Pārrakstot Lokālās Izmaiņas

### ⚡ Ātrs Veids - Izmantot Skriptu

```bash
cd /home/user/PropertyManager
./reset_to_remote.sh
```

Šis skripts:
- ✅ Pārbauda vai ir lokālās izmaiņas
- ✅ Prasa apstiprinājumu
- ✅ Lejupielādē jaunākās izmaiņas
- ✅ Pārraksta visas lokālās izmaiņas
- ✅ Notīra untracked failus
- ✅ Pārslēdzas uz pareizo branch

---

## 📋 Manuālās Metodes

### Metode 1: Pilnīgs Reset (Vispārīgākais)

```bash
cd /home/user/PropertyManager

# 1. Lejupielādēt jaunākās izmaiņas
git fetch origin

# 2. Pārrakstīt visas lokālās izmaiņas
git reset --hard origin/claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi

# 3. Notīrīt untracked failus
git clean -fd

# 4. Pārliecināties ka esat uz pareizā branch
git checkout claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

### Metode 2: Viena Komanda

```bash
git fetch origin && \
git reset --hard origin/claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi && \
git clean -fd
```

### Metode 3: Dzēst Visu un Klonēt No Jauna

Ja ir lielas problēmas:

```bash
cd /home/user
rm -rf PropertyManager
git clone <repo-url> PropertyManager
cd PropertyManager
git checkout claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

---

## 🎯 Izskaidrojumi

### `git fetch origin`
- Lejupielādē jaunākās izmaiņas no remote
- **NEpārraksta** lokālos failus

### `git reset --hard origin/branch`
- Pārraksta ALL lokālos failus ar remote versiju
- **DZĒŠ visas uncommitted izmaiņas**
- ⚠️ Nav undo!

### `git clean -fd`
- `-f` = force (obligāts)
- `-d` = dzēš direktorijas
- Izdzēš visus failus, kas nav Git tracking

### `git checkout branch`
- Pārslēdzas uz norādīto branch
- Pārliecinās, ka esat uz pareizā branch

---

## 📊 Pārbaudīt Statusu Pirms/Pēc

### Pirms reset:

```bash
# Skatīt lokālās izmaiņas
git status

# Skatīt detalizētas izmaiņas
git diff

# Skatīt uncommitted failus
git status -s
```

### Pēc reset:

```bash
# Pārbaudīt vai viss ir tīrs
git status
# Vajadzētu redzēt: "nothing to commit, working tree clean"

# Pārbaudīt branch
git branch
# Vajadzētu redzēt: * claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi

# Skatīt pēdējos commits
git log --oneline -5
```

---

## 🛡️ Saglabāt Izmaiņas Pirms Reset (Ja Nepieciešams)

### Opcija 1: Izveidot Backup Branch

```bash
# Saglabāt pašreizējās izmaiņas jaunā branch
git checkout -b backup-$(date +%Y%m%d-%H%M%S)
git add -A
git commit -m "Backup pirms reset"

# Atgriezties un veikt reset
git checkout claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
git reset --hard origin/claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

### Opcija 2: Git Stash (Īslaicīga saglabāšana)

```bash
# Saglabāt izmaiņas stash
git stash push -m "Manas izmaiņas"

# Veikt reset
git reset --hard origin/claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi

# Ja vēlies atgriezt izmaiņas (vēlāk)
git stash pop
```

### Opcija 3: Vienkārši Kopēt Failus

```bash
# Izveidot backup direktoriju
cp -r /home/user/PropertyManager /home/user/PropertyManager_backup_$(date +%Y%m%d)

# Tad veikt reset
cd /home/user/PropertyManager
git reset --hard origin/claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

---

## 🚨 Bieži Sastopamās Problēmas

### "fatal: refusing to merge unrelated histories"

```bash
git pull origin claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi --allow-unrelated-histories
```

### "error: Your local changes would be overwritten"

```bash
# Vienkārši force reset
git reset --hard HEAD
git clean -fd
git pull origin claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

### "Permission denied" vai file lock problēmas

```bash
# Aizvērt Android Studio
# Tad:
git reset --hard
git clean -fd
```

### Gradle cache problēmas pēc reset

```bash
# Notīrīt Gradle cache
./gradlew clean
rm -rf .gradle
rm -rf app/build

# Android Studio:
# File → Invalidate Caches → Invalidate and Restart
```

---

## 📱 Android Studio Specifiskas Darbības

### Pēc Git Reset Android Studio:

1. **Sync Gradle**
   - File → Sync Project with Gradle Files
   - Vai tikai nogaidīt auto-sync

2. **Invalidate Caches** (ja ir problēmas)
   - File → Invalidate Caches and Restart
   - Izvēlieties "Invalidate and Restart"

3. **Rebuild Project**
   - Build → Clean Project
   - Build → Rebuild Project

4. **Restart Android Studio**
   - Aizvērt pilnībā
   - Atvērt no jauna

---

## 🔍 Pārbaudīt Vai Esat Sinhronizēti ar Remote

```bash
# Skatīt attālumu starp local un remote
git status

# Detalizētāk
git log --oneline --graph --all -10

# Skatīt remote info
git remote -v

# Skatīt remote branches
git branch -r

# Salīdzināt ar remote
git diff origin/claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi
```

---

## ✅ Pārbaudes Saraksts Pēc Reset

- [ ] `git status` rāda "nothing to commit, working tree clean"
- [ ] Pareizais branch: `claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi`
- [ ] Android Studio Gradle sync pabeigts
- [ ] Visi faili ir uz vietas (pārbaudīt Project tree)
- [ ] google-services.json eksistē
- [ ] Aplikācija build'ojas bez kļūdām: `./gradlew assembleDebug`

---

## 🎯 Kopsavilkums

| Komanda | Ko dara | Droša? |
|---------|---------|--------|
| `git fetch` | Lejupielādē izmaiņas | ✅ Jā |
| `git pull` | Fetch + merge | ⚠️ Var būt konflikti |
| `git reset --hard` | Pārraksta lokālos failus | ❌ Zaudē izmaiņas |
| `git clean -fd` | Dzēš untracked failus | ❌ Zaudē failus |
| `git stash` | Saglabā izmaiņas | ✅ Jā |

---

## 💡 Best Practices

1. **Vienmēr pārbaudīt statusu pirms reset:**
   ```bash
   git status
   ```

2. **Ja ir svarīgas izmaiņas, izveidot backup:**
   ```bash
   git stash push -m "Important changes"
   ```

3. **Pēc reset, pārbaudīt vai viss strādā:**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Regulāri commit un push:**
   - Mazāk risks zaudēt kodu
   - Vieglāk sync ar team

5. **Izmantot feature branches:**
   - Nestrādāt tieši uz main branch
   - Vieglāk eksperimentēt

---

Izmantojiet `./reset_to_remote.sh` skriptu drošai un vieglai reset operācijai! 🚀
