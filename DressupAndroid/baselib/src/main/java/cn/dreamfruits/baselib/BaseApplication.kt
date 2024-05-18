package cn.dreamfruits.baselib

import android.app.Application
import android.content.Context
import cn.dreamfruits.baselib.delegate.AppDelegate
import cn.dreamfruits.baselib.delegate.AppLifecycle

open class BaseApplication() : Application() {
    private var mAppDelegate: AppLifecycle? = null


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null) {
            this.mAppDelegate = AppDelegate(base)
        }
        this.mAppDelegate?.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        this.mAppDelegate?.onCreate(this)

    }


    override fun onTerminate() {
        super.onTerminate()
        this.mAppDelegate?.onTerminate(this)
    }
}