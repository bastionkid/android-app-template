# android-app

Reference: https://github.com/android/nowinandroid


## Tech Stack

- Kotlin
- Retrofit + kotlinx-serialization (networking)
- Jetpack Compose (ui toolkit)
- Coroutines (concurrency) & Flows (reactive streams)
- Koin (DI)
- Room (local storage)
- Lottie (animations)
- Custom lints to find errors during development and for format checking of TODO & STOPSHIP
- MacroBenchmark for app benchmarking


## Project Structure/Features

- Gradle KTS - https://developer.android.com/studio/build/migrate-to-kts
- Gradle Version Catalogs for dependency management - https://www.droidcon.com/2022/05/13/gradle-version-catalogs-for-an-awesome-dependency-management/
- Type safe project accessors - https://proandroiddev.com/using-type-safe-project-dependencies-on-gradle-493ab7337aa


## Project Setup

1. After cloning the repo. You’ll need a **keystore.properties** & **nolo.jks** files from one of the teammate as we don’t push the signing keys to VCS.
   - Put **keystore.properties** file in your root directory. i.e. (same hierarchy as **app** or **core** module)
   - Update the **storeFile=JKS_FILE_PATH_ON_YOUR_SYSTEM** property in keystore.properties
2. Then you’ll need a **secrets.properties** file from one of the teammate as we don’t push the keys to VCS. We use https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin to read the keys from local file.


## TODO

- [ ] Add Compose checks - https://github.com/twitter/compose-rules
- [ ] Add material3 support & dynamic color scheme for Android 12+
- [ ] Add https://github.com/littlerobots/version-catalog-update-plugin to know about dependency updates
- [ ] Add lint checks for TODO & STOPSHIP formatter
- [ ] Add lint check so that NavController is not directly used in Fragments. SafeNavigation should be used in Fragments to avoid app crash when both navigation attempt and lifecycle is destroying at the same time.
- [ ] Add JankStats compose extensions from https://github.com/android/nowinandroid/blob/main/core/ui/src/main/java/com/google/samples/apps/nowinandroid/core/ui/JankStatsExtensions.kt
- [ ] Integrate https://github.com/jayasuryat/mendable for better readability of compose compiler metrics data
- [ ] Integrate https://github.com/jraska/modules-graph-assert for assertion of module dependencies
