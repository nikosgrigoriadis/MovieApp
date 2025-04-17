plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.movieapp"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }


    defaultConfig {
        applicationId = "com.example.movieapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.sqliteassethelper)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation (libs.glide)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    //Room Coroutines
    implementation(libs.androidx.room.ktx)
    //Gson
    implementation(libs.gson)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    //Lottie
    implementation (libs.lottie)
    //LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    //SwipeRefresh
    implementation(libs.androidx.swiperefreshlayout)

}