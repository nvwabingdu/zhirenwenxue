package cn.dreamfruits.baselib.delegate

import android.app.Application
import android.content.Context
import android.util.Log
import cn.dreamfruits.baselib.di.ClientModule
import cn.dreamfruits.baselib.di.GlobalConfigModule
import cn.dreamfruits.baselib.di.appModule
import cn.dreamfruits.baselib.integration.ConfigModule
import cn.dreamfruits.baselib.integration.ManifestParser
import cn.dreamfruits.baselib.network.GlobalHttpHandler
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import retrofit2.Retrofit


class AppDelegate(private val context: Context) : AppLifecycle, KoinComponent {

    private var mApplication: Application? = null
    private var mModules: List<ConfigModule>? = null
    private var mAppLifecycles = ArrayList<AppLifecycle>()
    private var mActivityLifecycles = ArrayList<Application.ActivityLifecycleCallbacks>()

    private val mActivityLifecycle: Application.ActivityLifecycleCallbacks? by inject(named("ActivityLifecycle"))


    init {
        //用反射, 将 AndroidManifest.xml 中带有 ConfigModule 标签的 class 转成对象集合（List<ConfigModule>）
        this.mModules = ManifestParser(context).parse()
        mModules!!.forEach { module ->

            //将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 mAppLifecycles 集合 (此时还未注册回调)
            module.injectAppLifecycle(context, mAppLifecycles)

            //将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLifecycles 集合 (此时还未注册回调)
            module.injectActivityLifecycle(context, mActivityLifecycles)

        }
    }


    override fun attachBaseContext(base: Context) {

        //遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
        mAppLifecycles.forEach { lifecycle ->
            lifecycle.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        this.mApplication = application

        startKoin {
            androidLogger()
            androidContext(application)
            modules(
                appModule,
                ClientModule.clientModule,
                getGlobeConfigModule(mApplication!!, mModules!!).globalConfigModule,
            )
        }


        //注册框架内部已实现的 Activity 生命周期逻辑
        mApplication!!.registerActivityLifecycleCallbacks(mActivityLifecycle)

        //注册框架外部, 开发者扩展的 Activity 生命周期逻辑
        for (lifecycle in mActivityLifecycles) {
            mApplication!!.registerActivityLifecycleCallbacks(lifecycle)
        }


        //执行框架外部, 开发者扩展的 App onCreate 逻辑
        for (appLifecycle in mAppLifecycles) {
            appLifecycle.onCreate(mApplication!!)
        }

    }

    override fun onTerminate(application: Application) {
        if (mAppLifecycles.size > 0) {
            for (lifecycle in mAppLifecycles) {
                lifecycle.onTerminate(mApplication!!)
            }
        }
    }


    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明[ConfigModule]的实现类,和Glide的配置方式相似
     */
    private fun getGlobeConfigModule(
        context: Context,
        modules: List<ConfigModule>
    ): GlobalConfigModule {
        val builder = GlobalConfigModule.builder()

        for (module in modules) {
            module.applyOptions(context, builder)
        }

        return builder.build()
    }
}