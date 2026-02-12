import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.org.sonarqube)
}

android {
    namespace = "com.hilmysf.fundamental"
    compileSdk = 36

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://event-api.dicoding.dev/\"")
        applicationId = "com.hilmysf.fundamental"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    sonar {
        properties {
            property("sonar.projectName", "Your-Project-Name")
            property("sonar.projectKey", "Your-Unique-Project-Key")
            property("sonar.host.url", "https://your-sonarqube-server.com")
            // Point to XML reports for test coverage
            property("sonar.coverage.jacoco.xmlReportPaths", "${project.buildDir}/reports/jacoco/testDebugUnitTestReport/testDebugUnitTestReport.xml")
            property("sonar.android.lint.reportPaths", "build/reports/lint-results-debug.xml")
            // Pastikan mengarah ke variant yang benar
            property("sonar.android.variant", "debug")
        }
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.glide)
    implementation(libs.bundles.network)

    //Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)

    implementation(libs.shimmer)
    implementation(libs.data.store)
    implementation(libs.data.store.core)

    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //Work Manager
    implementation(libs.worker.runtime)
}
