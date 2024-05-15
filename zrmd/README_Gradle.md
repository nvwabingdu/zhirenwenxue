
1.build.gradle基础知识详解

plugins {
id 'com.android.application'//表示应用程序
id 'com.android.library'//表示库模块
id 'org.jetbrains.kotlin.android'//kotlin插件，用与kotlin开发
}

android {
namespace 'com.example.zrword'
compileSdk 33
    defaultConfig {//基础配置
        applicationId "com.example.zrword"//应用唯一标识符 可修改 不能重复
        minSdk 24
        targetSdk 33
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {//用于指定生成安装文件的配置
        release {
            minifyEnabled false //是否混淆  true表示混淆
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {//程序默认没有 自己添加
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
    //使用viewBinding
    buildFeatures {
        viewBinding true
    }
}

dependencies {//本地依赖   库依赖    远程依赖 三种依赖方式

implementation fileTree //本地依赖 表示将libs目录下的所有.jar后缀的文件都添加到构建路径中

implementation 'androidx.core:core-ktx:1.8.0' //类似的都是远程依赖
implementation 'androidx.appcompat:appcompat:1.5.1'
implementation 'com.google.android.material:material:1.5.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
testImplementation 'junit:junit:4.13.2'
androidTestImplementation 'androidx.test.ext:junit:1.1.3'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
//zr系列
implementation(project(':zrframe'))  //库依赖方式  
}



2.