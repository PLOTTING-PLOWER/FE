import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.example.plotting_fe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.plotting_fe"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        buildTypes {
            buildConfigField("String", "BASE_URL", properties.getProperty("BASE_URL"))
            buildConfigField ("String", "NAVER_CLIENT_ID", properties.getProperty("NAVER_CLIENT_ID"))
            buildConfigField ("String", "NAVER_CLIENT_SECRET", properties.getProperty("NAVER_CLIENT_SECRET"))
            buildConfigField ("String", "NAVER_CLIENT_ID_MAP", properties.getProperty("NAVER_CLIENT_ID_MAP"))
            buildConfigField ("String", "NAVER_CLIENT_SECRET_MAP", properties.getProperty("NAVER_CLIENT_SECRET_MAP"))
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
    implementation(libs.play.services.location)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Naver Map SDK
    implementation("com.naver.maps:map-sdk:3.19.1")

    // 스플래쉬 화면
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Material Design 라이브러리 (BottomNavigationView 사용을 위해 필요)
    implementation("com.google.android.material:material:1.9.0")

    //Gson
    implementation ("com.google.code.gson:gson:2.10.1")

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

    //위치 추적
    implementation ("com.google.android.gms:play-services-location:18.0.0")

    // BarChart
    implementation(libs.mpandroidchart)

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")

    // 구글
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.firebase:firebase-messaging:23.2.0")

    //네아로
    implementation("com.navercorp.nid:oauth:5.10.0")

    //json
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.0")

    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.4")

    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    //token
    implementation ("com.auth0:java-jwt:3.18.2")

}