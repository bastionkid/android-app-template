plugins {
    id("android.library")
}

android {
    namespace = "com.azuredragon.core.log"

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    testImplementation(libs.junit4)
}