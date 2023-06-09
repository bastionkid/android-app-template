import com.android.build.gradle.internal.core.Abi

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.navigation.safeargs)
    id("com.spotify.ruler")
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = "com.azuredragon.app"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.azuredragon.app"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        when (properties["resConfig"]) {
            "en" -> resConfigs("en")
        }
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ".debug"
        }
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            //TODO(akashkhunt): 18/09/22 Abstract the signing configuration to a separate file to avoid hardcoding this.
            //signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = org.gradle.api.JavaVersion.VERSION_17
        targetCompatibility = org.gradle.api.JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        buildConfig = true
        compose = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    splits {
        abi {
            val onlyInclude = fun(abiTag: kotlin.String) {
                // Enables building multiple APKs per ABI.
                isEnable = true

                // By default all ABIs are included, so use reset() and include to specify that
                // you only want APKs for e.g. arm64-v8a, armeabi-v7a & x86_64.

                // Resets the list of ABIs for Gradle to create APKs for to none.
                reset()

                include(abiTag)
            }

            when (properties["targetAbi"]) {
                Abi.ARM64_V8A.tag -> onlyInclude(Abi.ARM64_V8A.tag)
                Abi.ARMEABI_V7A.tag -> onlyInclude(Abi.ARMEABI_V7A.tag)
                Abi.X86_64.tag -> onlyInclude(Abi.X86_64.tag)
            }
        }

        density {
            val onlyInclude = fun (density: kotlin.String) {
                // Enables building multiple APKs per ABI.
                isEnable = true

                // By default all ABIs are included, so use reset() and include to specify that
                // you only want APKs for e.g. xxxhdpi, xxhdpi & xhdpi.

                // Resets the list of ABIs for Gradle to create APKs for to none.
                reset()

                include(density)
            }

            when (properties["targetDensity"]) {
                "xxxhdpi" -> onlyInclude("xxxhdpi")
                "xxhdpi" -> onlyInclude("xxhdpi")
                "xhdpi" -> onlyInclude("xhdpi")
            }
        }
    }

    lint {
        isCheckReleaseBuilds = true
        isAbortOnError = true
//        enable("StopShip")
//        fatal("StopShip")
    }

    experimentalProperties["android.experimental.art-profile-r8-rewriting"] = true
    experimentalProperties["android.experimental.r8.dex-startup-optimization"] = true
}

baselineProfile {
    enableR8BaselineProfileRewrite = true

    // This should be enabled on runners supporting GMD, which required support
    // for nested-virtualization
    automaticGenerationDuringBuild = false
}

ruler {
    abi.set("arm64-v8a")
    locale.set("en")
    screenDensity.set(480)
    sdkVersion.set(33)

    ownershipFile.set(project.file("./../.github/scripts/ownership.yml"))
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.network)
    implementation(projects.core.log)
    implementation(projects.core.localstorage)

    implementation(projects.feature.login.data)
    implementation(projects.feature.login.domain)
    implementation(projects.feature.login.presentation)

    implementation(projects.feature.home.data)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.home.presentation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.performance)

    implementation(libs.barcode)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(libs.androidx.profileinstaller)
    baselineProfile(projects.baselineprofile)
}