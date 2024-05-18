package cn.dreamfruits.baselib.di

import android.app.Application
import cn.dreamfruits.baselib.integration.ActivityLifecycle
import cn.dreamfruits.baselib.integration.AppManager
import cn.dreamfruits.baselib.integration.IRepositoryManager
import cn.dreamfruits.baselib.integration.RepositoryManager
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit


/**
 * 提供一些框架必须的实例的 Module
 */
const val KODEIN_MODULE_APP_TAG = "koin_module_app_tag"

val appModule = module {
    named(KODEIN_MODULE_APP_TAG)

    single { provideAppManager(get()) }

    single(named("ActivityLifecycle")) { bindActivityLifecycle() } bind Application.ActivityLifecycleCallbacks::class

    single{ bindRepositoryManager(get()) } bind IRepositoryManager::class
}



fun provideAppManager(application: Application): AppManager? {
    return AppManager.sAppManager.init(application)
}


fun bindRepositoryManager(retrofit: Retrofit): RepositoryManager {
    return RepositoryManager(retrofit)
}

fun bindActivityLifecycle(): ActivityLifecycle {
    return ActivityLifecycle()
}

