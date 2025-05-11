plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.aplicacioncitas"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.aplicacioncitas"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    viewBinding {
        enable = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/INDEX.LIST",
                "META-INF/io.netty.versions.properties",
                "META-INF/DEPENDENCIES",
                "META-INF/*.md" // Opcional: excluye archivos markdown
            )
        }
    }
}



dependencies {
    //APIS HUELLA DACTILAR
    implementation("androidx.biometric:biometric:1.2.0-alpha04")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.airbnb.android:lottie:6.1.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //navigation
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.common)

    implementation(libs.circleimageview)

    //cardView
    implementation(libs.androidx.cardview)
    //RecyclerView
    implementation(libs.androidx.recyclerview)

    //corrutinas
    implementation(libs.kotlinx.coroutines.android)

    //viewmodel
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.fragment.ktx)

    // LiveData
    implementation (libs.androidx.lifecycle.livedata.ktx)


    //retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //Glide
    implementation (libs.glide)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.appdistribution.gradle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}