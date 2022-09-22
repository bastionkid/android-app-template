plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "com.azuredragon.login.domain"

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
    implementation(projects.feature.login.data)

    api(libs.androidx.lifecycle.viewModelCompose)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
}