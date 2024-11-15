import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.example.plotting_fe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.plotting_fe"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildTypes {
            buildConfigField("String", "BASE_URL", properties.getProperty("BASE_URL"))
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Naver Map SDK
    implementation("com.naver.maps:map-sdk:3.19.1")

    // 스플래쉬 화면
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Material Design 라이브러리 (BottomNavigationView 사용을 위해 필요)
    implementation("com.google.android.material:material:1.9.0")

    // Navigation Component 라이브러리
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0")

    // appcompat lib
    implementation("androidx.appcompat:appcompat:1.3.0")

    //retrofit2 - 서버통신
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Http 통신 라이브러리
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // glide - 이미지 로딩
    implementation(libs.glide)

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // BarChart
    implementation(libs.mpandroidchart)
}