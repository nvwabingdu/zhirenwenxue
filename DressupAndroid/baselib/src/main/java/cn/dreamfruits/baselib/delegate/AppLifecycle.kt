package cn.dreamfruits.baselib.delegate

import android.app.Application
import android.content.Context

/**
 *用于代理 [Application]的生命周期
 */
interface AppLifecycle {

    fun attachBaseContext(base: Context)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)

}