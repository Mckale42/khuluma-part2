# Khuluma — Android App

**Gamified, AI-assisted app for learning South African languages (starting with isiZulu).**
PROG7314 / OPSC7312 — POE Part 2 · Group: *The Viltrumites*

Native Android (Kotlin + Jetpack Compose, MVVM) client for the **Khuluma REST API**. The app handles presentation and interaction; the API does the heavy lifting (content, XP, streaks, spaced repetition, AI).

> **Demo video:** App Demo Video: https://www.youtube.com/watch?v=uBEMbZ__q5U - API Demo Video: https://www.youtube.com/watch?v=2IccMX2n4cQ
                  

---

## Features (mapped to the Part 2 rubric)

| Feature | Rubric item | Where |
|---|---|---|
| Google **Single Sign-On** via Firebase | SSO sign-in (10) | `LoginScreen`, `AuthManager` |
| **Settings** that persist through the API | Settings menu (10) | `SettingsScreen` → `PUT /me/settings` |
| Full **REST API integration** with Retrofit | Integrate API (10) | `data/remote`, `KhulumaRepository` |
| **Gamification** — XP, streaks, levels, achievements | User-Defined 1 (10) | `StatsBar`, lesson feedback, `Profile` |
| **AI Buddy tutor** + "explain my mistake" | User-Defined 2 (10) | `BuddyScreen`, lesson feedback → `/ai/*` |
| **Adaptive lessons** (mcq/listen/match/free-text) | User-Defined 3 (10) | `LessonScreen` → `/lessons/:id/next`, `/attempts` |
| Consistent Material 3 UI, input validation | UI (10) | `ui/theme`, all screens |
| Logging via `android.util.Log` | (Source & logging) | throughout |

Weekly **leaderboard** and **word-of-the-day** are included as extra gamification evidence.
POE-only features (biometric, offline sync, FCM, multi-language, Play Store) are intentionally **not** in this prototype.

## Architecture

```
UI (Jetpack Compose)  ──  ViewModel (StateFlow)  ──  Repository  ──  Retrofit (KhulumaApi)
                                                                          │ Firebase ID token
                                                                          ▼
                                                              Khuluma REST API (Render)
```

Strict MVVM (design doc NR-6): Compose screens observe `StateFlow` from ViewModels; ViewModels call the repository; the repository is the only thing that talks to Retrofit. A lightweight `ServiceLocator` wires everything up.

## Tech stack

Kotlin · Jetpack Compose (Material 3) · Navigation-Compose · Lifecycle ViewModel · Retrofit + Gson + OkHttp · Firebase Auth (Google SSO) · Coroutines · JUnit.

## Project structure

```
app/src/main/java/com/viltrumites/khuluma/
  data/
    remote/   Dtos, KhulumaApi (Retrofit), ApiClient, AuthInterceptor
    repo/     KhulumaRepository
    AuthManager.kt   (Google SSO + Firebase)
  ui/
    theme/    Material 3 brand theme
    navigation/  Routes, KhulumaApp (NavHost + bottom bar)
    components/  StatsBar, BottomBar
    auth/ home/ lesson/ profile/ settings/ buddy/ leaderboard/   (screen + ViewModel each)
  ServiceLocator.kt, KhulumaApplication.kt, MainActivity.kt
app/src/test/    JUnit unit tests
.github/workflows/android.yml   build + test on every push
```

## Setup

### 1. Firebase (Google SSO)
1. Create a Firebase project and add an Android app with package `com.viltrumites.khuluma`.
2. Enable **Authentication → Google**.
3. Download the real **`google-services.json`** and replace the template at `app/google-services.json`.
4. Copy the **Web client ID** (OAuth 2.0) into `app/src/main/res/values/strings.xml` → `default_web_client_id`.
5. Add your debug **SHA-1** to the Firebase Android app (needed for Google sign-in).

### 2. Point the app at the API
`app/build.gradle.kts` sets `API_BASE_URL`:
- **debug** defaults to `http://10.0.2.2:8080/api/` (emulator → API on your PC's localhost).
- **release** uses your deployed Render URL — change the placeholder before building release.

### 3. Run
Open in **Android Studio**, let it sync (this generates the Gradle wrapper), then Run on a
physical device or emulator. Make sure the Khuluma API is running/deployed and seeded.

## Testing & CI

```bash
./gradlew testDebugUnitTest   # JUnit unit tests
./gradlew assembleDebug       # build the debug APK
```

`.github/workflows/android.yml` runs both on every push — satisfying the Part 2 requirement for
GitHub Actions automated build + unit tests.

> **Note:** commit the Gradle wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`).
> Android Studio generates it automatically the first time you open the project; the CI needs it.

## Team — The Viltrumites

Shaun Bruce McClatchie (ST10434042) · McKale Keanu Naidoo (ST10066440) · Akwande Gqoboka (ST10198519) · Akshar Jadoonandan (ST10441329)

Each member commits under their own GitHub account so the repository shows genuine multi-contributor history.

## Academic note

Built for the IIE PROG7314 POE. Code is the team's own work; libraries and patterns are credited in
the Part 1 design document and in code comments.
