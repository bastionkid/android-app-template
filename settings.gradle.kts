enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://androidx.dev/storage/compose-compiler/repository")
    }
}

includeBuild("build-logic")

rootProject.name = "android-app-template"
include(":app")
include(":core")
include(":core:analytics")
include(":core:data")
include(":core:network")
include(":core:log")
include(":core:common")
include(":core:ui")
include(":core:localstorage")
include(":core:domain")
include(":core:time")
include(":feature")
include(":feature:login")
include(":feature:login:data")
include(":feature:login:domain")
include(":feature:login:presentation")
include(":feature:home")
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":baselineprofile")
