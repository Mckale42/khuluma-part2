# Khuluma — South African Language Learning Platform

[![Khuluma CI](https://github.com/Mckale42/khuluma-part2/actions/workflows/ci.yml/badge.svg)](https://github.com/Mckale42/khuluma-part2/actions/workflows/ci.yml)

**Gamified, AI-assisted platform for learning South African languages (starting with isiZulu).**  
**Course:** PROG7314 / OPSC7312 — POE Part 2  
**Group:** *The Viltrumites*

---

## Demo Videos
- **App Demo Video:** [Watch on YouTube](https://www.youtube.com/watch?v=uBEMbZ__q5U)
- **API Demo Video:** [Watch on YouTube](https://www.youtube.com/watch?v=2IccMX2n4cQ)

---

## Team Contributions

| Member | Focus Area | Key Contributions |
|---|---|---|
| **Mckale Naidoo** | Cloud API & Backend Architecture | Express/MongoDB REST API, Gemini Flash AI integration, Spaced Repetition engine, Render deployment |
| **Shaun McClatchie** | Authentication & Profile | Firebase Auth (Google SSO), User Profile management, Settings screen & API sync |
| **Akshar Singh** | Learning Engine & Interactive UI | Jetpack Compose Lesson flows (MCQ, Listen, Match, Free-text), Attempt tracking |
| **Akwane Dlamini** | Gamification & Social | XP system, Streaks, Level progression, Achievements, Weekly Leaderboard, Word of the Day |

---

## Features Mapped to Part 2 Rubric

| Feature | Rubric Criterion | Implementation Details |
|---|---|---|
| **Google Single Sign-On (SSO)** | SSO Sign-in (10) | Firebase Authentication with Google Sign-In; secure JWT bearer token validation in API |
| **User Settings Menu** | Settings Menu (10) | Preferences (daily goal, sound, haptics, reminders, theme) persisted via `PUT /me/settings` |
| **Full REST API Integration** | Integrate API (10) | Retrofit 2 + OkHttp client in Android talking to deployed Render API (`https://khuluma-api.onrender.com`) |
| **Gamification System** | User-Defined Feature 1 (10) | XP calculation with speed bonuses, streak freezes, dynamic levels, and unlocked achievement badges |
| **AI Buddy & Tutor** | User-Defined Feature 2 (10) | Google Gemini AI assistant for interactive conversational practice and context-aware error explanation |
| **Adaptive Learning Flow** | User-Defined Feature 3 (10) | Multi-format lessons (MCQ, listening, matching, fill-in-the-blank) with SuperMemo SM-2 spaced repetition |
| **Modern UI & Validation** | User Interface (10) | Material 3 Jetpack Compose design, responsive states, input validation, and full error handling |
| **CI/CD & Testing** | Source & Automation | GitHub Actions automated workflow running JUnit Android tests and Jest API tests on every push |

---

## Architecture Overview

```
Android Client (Kotlin + Jetpack Compose)
       │
       ├── UI Layer (Screens, Material 3 Components)
       ├── ViewModel Layer (StateFlow, Coroutines)
       └── Repository Layer (KhulumaRepository)
              │
              ▼ HTTPS (Firebase ID Token)
Khuluma REST API (Node.js + Express + TypeScript)
       │
       ├── Auth Middleware (Firebase Admin SDK)
       ├── Controllers & Services (Gamification, Spaced Repetition SM-2)
       ├── AI Service (Google Gemini Flash)
       └── Data Layer (Mongoose + MongoDB Atlas)
```

---

## Repository Structure

- **`khuluma-android/`**: Native Android application (Kotlin, Jetpack Compose, MVVM, Retrofit, Firebase). Open in Android Studio.
- **`khuluma-api/`**: TypeScript REST API (Node.js, Express, MongoDB, Gemini AI). Deployed live on Render.
- **`.github/workflows/`**: Continuous Integration workflow automating Android unit tests, APK build, and API test suites.

---

## Quick Start Guide

### 1. Android App
1. Open the `khuluma-android` folder in **Android Studio** (Koala or newer).
2. Allow Gradle to sync.
3. Select an Android Emulator (API 26+) or physical device.
4. Run the `debug` or `release` build.
   - For demo testing without accounts, tap **"Skip for now (demo)"** on the login screen.

### 2. Local REST API (Optional)
```bash
cd khuluma-api
npm install
npm run dev
```
The local API starts in demo mode with an in-memory MongoDB database on `http://localhost:8080`.
The live cloud API is always available at: **https://khuluma-api.onrender.com**
