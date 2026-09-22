# Khuluma — Tester Guide (The Viltrumites, PROG7314)

Khuluma is a gamified isiZulu learning app: a native Android client (Kotlin + Jetpack
Compose) talking to a custom Node/Express REST API backed by MongoDB.

This bundle has two projects:

- `khuluma-android/` — the Android app (open this in **Android Studio**)
- `khuluma-api/`     — the REST API (only needed for the offline "Demo mode" below)

You have two ways to run it. **Demo mode is the fastest** and needs no accounts or keys.

---

## Option A — Demo mode (recommended, ~5 min, no accounts needed)

Runs everything on your own machine with an in-memory database. No Firebase, no secrets.

**1. Start the API** (needs Node 18+):

```
cd khuluma-api
npm install
npm run dev
```

You should see `[db] DEMO MODE — in-memory MongoDB started` and
`[server] Khuluma API listening on :8080`. Leave this running.

**2. Run the app** in Android Studio:

- Open the `khuluma-android` folder in Android Studio and let Gradle sync.
- Pick an **Android Emulator** (Pixel, API 26+). *Demo mode uses `10.0.2.2`, which only
  works on an emulator, not a physical phone.*
- Press **Run** (the green ▶). The `debug` build automatically points at your local API.
- On the login screen, tap **"Skip for now (demo)"** — no Google account needed.

You'll land on the home dashboard with the isiZulu course, XP, streaks, lessons and the
AI buddy screen. (AI chat/explain will say the AI is unavailable unless a Gemini key is
set — that's expected in demo mode.)

---

## Option B — Test against the LIVE cloud API

The API is already deployed at **https://khuluma-api.onrender.com**. To point the app at
it you build the `release` variant (Build ▸ Select Build Variant ▸ release, then Run).

Note: the live API enforces real authentication, so the demo "Skip" button won't work
against it — you must **Sign in with Google**. For Google sign-in to succeed on a build
you compile yourself, your machine's debug signing key (SHA-1) must be registered in the
Khuluma Firebase project, and the real `google-services.json` must be in `app/`. Ask
McKale to (a) send you the real `google-services.json` and (b) add your SHA-1 — get yours
with:

```
cd khuluma-android
./gradlew signingReport      # copy the SHA1 under "Variant: debug"
```

If you just want to see the app working, **use Option A** — it avoids all of this.

---

## Troubleshooting

- **App shows a loading bird / network error in demo mode** → the API isn't running, or
  you're on a physical device instead of an emulator. Start the API and use an emulator.
- **First request to the live API is very slow (~50s)** → the free Render host spins down
  when idle and takes a moment to wake. It's fast afterward.
- **Gradle sync fails** → make sure you opened the `khuluma-android` folder itself (the one
  with `settings.gradle.kts`), not its parent, and use a recent Android Studio (Koala+).
