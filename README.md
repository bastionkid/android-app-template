# android-app

Reference: https://github.com/android/nowinandroid


## Tech Stack

- Kotlin
- Retrofit + kotlinx-serialization (networking)
- Jetpack Compose (ui toolkit)
- Coroutines (concurrency) & Flows (reactive streams)
- Dagger/Koin + Hilt (DI)
- Room (local storage)
- Lottie (animations)


## Project Structure/Features

- Gradle KTS - https://developer.android.com/studio/build/migrate-to-kts
- Gradle Version Catalogs for dependency management - https://www.droidcon.com/2022/05/13/gradle-version-catalogs-for-an-awesome-dependency-management/
- Gradle include builds for custom plugin management - https://github.com/android/nowinandroid/tree/main/build-logic#readme
- Type safe project accessors - https://proandroiddev.com/using-type-safe-project-dependencies-on-gradle-493ab7337aa


## Project Setup

1. After cloning the repo. You’ll need a **secrets.properties** file from one of the teammate as we don’t push the keys to VCS. We use https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin to read the keys from local file.
