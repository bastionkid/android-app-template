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
    }
}

includeBuild("build-logic")

rootProject.name = "android-app-template"
include(":app")
include(":feature")
include(":feature:login")
include(":feature:login:data")
include(":feature:login:domain")
include(":feature:login:presentation")
include(":core")
include(":core:data")
include(":core:network")
include(":core:database")
include(":core:log")
include(":core:common")
include(":core:model")
include(":core:ui")
