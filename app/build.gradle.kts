plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.padi.pilipili"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.padi.pilipili"
        minSdk = 27
        targetSdk = 36
        versionCode = 103
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }

        androidComponents {
            onVariants(selector().all()) { variant ->
                variant.outputs.map { it as com.android.build.api.variant.impl.VariantOutputImpl }
                    .forEach { output ->
                        output.outputFileName =
                            "PILIPILI_v${output.versionName.get()}(${variant.name}).apk"
                    }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    aaptOptions {
        additionalParameters += listOf("--package-id", "0x90", "--allow-reserved-package-id")
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    compileOnly(fileTree("compileOnly"))
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.github.suzhelan:XpHelper:3.0")

    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    implementation("io.coil-kt:coil:2.4.0")

    implementation("com.google.android.material:material:1.14.0-alpha06")

    implementation("io.github.huajiqaq:nanohttpdx:2.3.2")

    val dialogXVersion = "0.0.50.beta38"
    //引入DialogX主体
    implementation("com.github.suzhelan.DialogX:DialogX:$dialogXVersion")
    //非必须 DialogX官方提供的主题样式
    implementation("com.github.suzhelan.DialogX:DialogXKongzueStyle:$dialogXVersion")
    implementation("com.github.suzhelan.DialogX:DialogXMIUIStyle:$dialogXVersion")
    implementation("com.github.suzhelan.DialogX:DialogXIOSStyle:$dialogXVersion")
    implementation("com.github.suzhelan.DialogX:DialogXMaterialYou:$dialogXVersion")


    implementation("top.yukonga.miuix.kmp:miuix-android:0.8.0")
    implementation("top.yukonga.miuix.kmp:miuix-icons-android:0.8.0")
    implementation("androidx.navigation3:navigation3-runtime:1.1.0-alpha03")
    implementation("top.yukonga.miuix.kmp:miuix-navigation3-ui:0.8.0")
    implementation("top.yukonga.miuix.kmp:miuix-navigation3-adaptive:0.8.0")

    implementation("io.coil-kt.coil3:coil-compose:3.3.0")


}