package cn.dreamfruits.baselib.integration

import android.app.Application
import android.content.Context
import cn.dreamfruits.baselib.delegate.AppLifecycle
import cn.dreamfruits.baselib.di.GlobalConfigModule

/**
 * 项目中一些全局配置的接口.
 */
interface ConfigModule {

    /**
     * 使用 [GlobalConfigModule.Builder] 给框架配置一些配置参数
     */
    fun applyOptions(context: Context, builder: GlobalConfigModule.Builder)


    /**
     * 使用[AppLifecycle]在Application的声明周期中注入一些操作
     */
    fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycle>)


    /**
     * 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作
     */
    fun injectActivityLifecycle(
        context: Context,
        lifecycles: List<Application.ActivityLifecycleCallbacks>
    )

}