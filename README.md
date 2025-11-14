# Property Manager - Android Aplikācija

Property Manager ir Android aplikācija, kas ļauj namu īpašniekiem un īrniekiem efektīvi pārvaldīt īres attiecības, maksājumus un komunicēt par saimnieciskajām problēmām.

## Galvenās Funkcijas

### Īpašniekiem:
- Reģistrēt un pārvaldīt īpašumus (dzīvokļus, mājas)
- Pievienot īrniekus ar uzaicinājuma kodiem
- Nosūtīt maksājuma informāciju īrniekiem
- Apstiprināt maksājuma dokumentus ar attēliem
- Saņemt paziņojumus par īrnieku ziņotajām problēmām
- Skatīt pārskatus par maksājumiem
- Mainīt īrniekus, saglabājot vēsturi

### Īrniekiem:
- Reģistrēties ar īpašnieka nosūtīto kodu
- Skatīt maksājuma informāciju
- Augšupielādēt maksājuma apstiprinājumus
- Ziņot par saimnieciskajām problēmām ar attēliem
- Saņemt paziņojumus par jauniem maksājumiem

## Tehnoloģijas

- **Kotlin** - programmēšanas valoda
- **Jetpack Compose** - modernā Android UI
- **Firebase Authentication** - lietotāju autentifikācija
- **Firebase Firestore** - datu bāze
- **Firebase Storage** - attēlu glabāšana
- **Material Design 3** - lietotāja interfeiss

## Firebase Iestatīšana

1. Dodieties uz [Firebase Console](https://console.firebase.google.com/)
2. Izveidojiet jaunu projektu vai izvēlieties esošu
3. Pievienojiet Android aplikāciju ar package name: `com.propertymanager`
4. Lejupielādējiet `google-services.json` failu
5. Aizstājiet placeholder `app/google-services.json` failu ar jūsu lejupielādēto failu

### Firebase Authentication
1. Firebase Console → Authentication → Sign-in method
2. Ieslēdziet "Email/Password" metodi

### Firestore Database
1. Firebase Console → Firestore Database
2. Izveidojiet datubāzi (sāciet test režīmā, vēlāk nomainiet uz production ar noteikumiem)

### Firebase Storage
1. Firebase Console → Storage
2. Izveidojiet storage bucket

### Firestore Security Rules (ieteicams)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }

    match /properties/{propertyId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }

    match /tenants/{tenantId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }

    match /payments/{paymentId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }

    match /issues/{issueId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
```

### Storage Security Rules (ieteicams)
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
  }
}
```

## Projekta Struktūra

```
app/src/main/java/com/propertymanager/
├── MainActivity.kt                 # Galvenā aktivitāte
├── models/                         # Datu modeļi
│   ├── User.kt
│   ├── Property.kt
│   ├── Tenant.kt
│   ├── Payment.kt
│   └── Issue.kt
├── repository/                     # Datu piekļuves slānis
│   └── FirebaseRepository.kt
├── ui/
│   ├── Navigation.kt              # Navigācijas definīcijas
│   ├── screens/                   # UI ekrāni
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
│   ├── viewmodels/                # ViewModel klases
│   │   ├── AuthViewModel.kt
│   │   ├── PropertyViewModel.kt
│   │   ├── PaymentViewModel.kt
│   │   └── IssueViewModel.kt
│   └── theme/                     # UI tēma
│       ├── Theme.kt
│       └── Type.kt
```

## Lietošanas Instrukcijas

### Īpašniekam:

1. **Reģistrācija**
   - Atveriet aplikāciju
   - Izvēlieties "Īpašnieks"
   - Aizpildiet reģistrācijas formu
   - Pierakstieties

2. **Īpašuma Pievienošana**
   - Spiediet "+" pogu
   - Ievadiet īpašuma informāciju (adrese, pilsēta, stāvs, dzīvokļa nr.)
   - Saglabājiet

3. **Īrnieka Pievienošana**
   - Atveriet īpašumu
   - Spiediet "Pievienot īrnieku"
   - Ievadiet īrnieka datus
   - Kopējiet ģenerēto kodu un nosūtiet to īrniekam pa e-pastu

4. **Maksājuma Pievienošana**
   - Atveriet īpašumu
   - Izvēlieties īrnieku
   - Spiediet "Pievienot maksājumu"
   - Ievadiet maksājuma informāciju
   - Atzīmējiet, vai maksājums ir atkārtojošs

5. **Maksājuma Apstiprināšana**
   - Kad īrnieks augšupielādē maksājuma apstiprinājumu
   - Pārbaudiet attēlu
   - Atzīmējiet maksājumu kā apstiprinātu

### Īrniekam:

1. **Reģistrācija**
   - Atveriet aplikāciju
   - Izvēlieties "Īrnieks"
   - Aizpildiet reģistrācijas formu
   - Ievadiet saņemto uzaicinājuma kodu

2. **Maksājuma Apstiprināšana**
   - Veiciet maksājumu ārpus aplikācijas
   - Atveriet aplikāciju
   - Spiediet "Augšupielādēt maksājuma apstiprinājumu"
   - Izvēlieties bankas izraksta vai maksājuma kvīts attēlu

3. **Problēmas Ziņošana**
   - Spiediet "+" pogu
   - Aprakstiet problēmu
   - Pievienojiet attēlus
   - Nosūtiet ziņojumu

## Build un Palaišana

### Prasības:
- Android Studio Hedgehog | 2023.1.1 vai jaunāka
- Android SDK 26+
- Kotlin 1.9.20+

### Build Komandas:

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Instalēt uz ierīci
./gradlew installDebug
```

## Problēmu Risināšana

### "google-services.json not found"
- Pārliecinieties, ka esat lejupielādējis un aizstājis placeholder google-services.json failu

### Firebase Authentication kļūda
- Pārbaudiet, vai Email/Password autentifikācija ir ieslēgta Firebase Console

### Attēlu augšupielādes kļūda
- Pārbaudiet Firebase Storage iestatījumus un security rules

## Autors

Property Manager Android aplikācija izveidota ar Claude Code.

## Licence

Šis projekts ir izstrādes versija un domāts demonstrācijas nolūkiem.
