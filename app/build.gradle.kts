plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.newzr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.newzr"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //zr系列
    implementation(project(":zrframe"))
    implementation(project(":zrdrawingboard"))

    //指示器库/wq    need   maven { url 'https://jitpack.io' }
    implementation("com.github.hackware1993:MagicIndicator:1.7.0")

    //SmartRefreshLayout/wq     https://github.com/scwang90/SmartRefreshLayout
    //如果使用 AndroidX 先在 gradle.properties 中添加，两行都不能少噢~
    implementation("androidx.appcompat:appcompat:1.0.0")                 //必须 1.0.0 以上
    implementation("io.github.scwang90:refresh-layout-kernel:2.0.5")      //核心必须依赖
    implementation("io.github.scwang90:refresh-header-classics:2.0.5")   //经典刷新头
    implementation("io.github.scwang90:refresh-footer-classics:2.0.5")    //经典加载
    implementation("io.github.scwang90:refresh-header-two-level:2.0.6")   //二级刷新头

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //evenbus
    implementation("org.greenrobot:eventbus:3.1.1")

    //flexbox/wq
    implementation("com.google.android.flexbox:flexbox:3.0.0")
}