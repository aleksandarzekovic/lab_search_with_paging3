import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("dagger.hilt.android.plugin")
}



android {
    namespace = "me.aleksandarzekovic.labsearchwithpaging3"
    compileSdk = Constants.compileSdkVersion

    val apikeyPropertiesFile = rootProject.file("keystore.properties")
    val apikeyProperties = Properties().apply {
        load(FileInputStream(apikeyPropertiesFile))
    }

    val STRING = "String"
    val TMDB_API_KEY = "TMDB_API_KEY"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "me.aleksandarzekovic.labsearchwithpaging3"
        minSdk = Constants.minSdkVersion
        targetSdk = Constants.targetSdkVersion
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = Constants.androidTestInstrumentation
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField(STRING, TMDB_API_KEY, apikeyProperties.getProperty(TMDB_API_KEY))
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            buildConfigField(STRING, TMDB_API_KEY, apikeyProperties.getProperty(TMDB_API_KEY))
        }

        create("staging") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".debugStaging"
            buildConfigField(STRING, TMDB_API_KEY, apikeyProperties.getProperty(TMDB_API_KEY))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation("androidx.activity:activity-ktx:${Constants.activityKtx}")
    implementation("androidx.appcompat:appcompat:${Constants.appCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Constants.constraintLayoutVersion}")
    implementation("com.google.android.material:material:${Constants.material}")

    // Arch components
    implementation("androidx.core:core-ktx:${Constants.coreVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Constants.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Constants.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Constants.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${Constants.lifecycleVersion}")

    // Room
    implementation("androidx.room:room-runtime:${Constants.roomVersion}")
    implementation("androidx.room:room-ktx:${Constants.roomVersion}")
    kapt("androidx.room:room-compiler:${Constants.roomVersion}")

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:${Constants.pagingVersion}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Constants.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Constants.retrofitVersion}")
    implementation("com.squareup.retrofit2:retrofit-mock:${Constants.retrofitVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Constants.okhttpLoggingInterceptorVersion}")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Constants.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Constants.coroutines}")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Constants.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Constants.hilt}")
    kapt("androidx.hilt:hilt-compiler:${Constants.hiltCompiler}")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Constants.serialization}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Constants.serializationConverter}")

    // Glide
    implementation("com.github.bumptech.glide:glide:${Constants.glide}")
    annotationProcessor("com.github.bumptech.glide:compiler:${Constants.glide}")
}