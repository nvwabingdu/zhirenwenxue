package cn.dreamfruits.yaoguo.application

import android.app.Application
import android.content.Context
import cn.dreamfruits.baselib.delegate.AppLifecycle
import cn.dreamfruits.baselib.di.GlobalConfigModule
import cn.dreamfruits.baselib.integration.ConfigModule
import cn.dreamfruits.yaoguo.constants.ApiConstants
import cn.dreamfruits.yaoguo.net.handler.HeaderHttpHandler


/**
 * app配置 在清单文件中注册
 */
class AppConfig : ConfigModule {


    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {
        builder.baseurl(ApiConstants.Host.BASE_URL)
            .globalHttpHandler(HeaderHttpHandler())
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycle>) {
        lifecycles.add(AppLifeCycleImpl())

    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycles: List<Application.ActivityLifecycleCallbacks>,
    ) {

    }

    companion object {
        // 0,else; 1,imdemo
        var IS_IM_DEMO = 0

        // app flavor
        var DEMO_FLAVOR_VERSION: String = "local"

        // app build version
        var DEMO_VERSION_NAME = "7.3.4358"

        // 0,classic; 1,minimalist
        var DEMO_UI_STYLE = 0

        // long connection addr: china、india ...
        var DEMO_TEST_ENVIRONMENT = 0

        // logined appid
        var DEMO_SDK_APPID = 1400715776
    }
}