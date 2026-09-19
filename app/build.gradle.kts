plugins {
    id 'com.android.application'
}

android {
    compileSdk 34
    defaultConfig {
        applicationId "com.example.petcare"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
        resValue "string" "en" "en"
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
                targetCompatibility JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding true
    }
}

dependencies {
    implementation(libs.room.runtime.android)
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
    implementation 'androidx.room:room-runtime:2.6.1'
    annotationProcessor 'androidx.room:room-compiler:2.6.1'
    // SMS & Permissions
    implementation 'androidx.activity:activity-ktx:1.8.2' // only if using Kotlin, skip if pure Java
    // Maps (Geotagging feature)
    implementation 'com.google.android.gms:play-services-maps:18.2.0'
    implementation 'com.google.android.gms:play-services-location:21.1.0'
}