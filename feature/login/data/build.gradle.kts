plugins {
    id("android.library")
}

android {
    namespace = "com.azuredragon.login.data"

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.network)

    testImplementation(libs.junit4)
}