plugins {
    id("android.library")
    id("android.library.compose")
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
    implementation(projects.login.data)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
}