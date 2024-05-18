package cn.dreamfruits.yaoguo.application

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import cn.dreamfruits.baselib.delegate.AppLifecycle
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.platform.CommPlatConfigBean
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.constants.ThirdKey
import cn.dreamfruits.yaoguo.di.remoteDataSourceModule
import cn.dreamfruits.yaoguo.di.repositoryModule
import cn.dreamfruits.yaoguo.repository.OauthRepository
import cn.dreamfruits.yaoguo.repository.UserRepository
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import org.koin.core.context.loadKoinModules

class AppLifeCycleImpl : AppLifecycle {


    override fun attachBaseContext(base: Context) {}

    override fun onCreate(application: Application) {
        INSTANCE = application
        MMKV.initialize(application)
        Utils.init(application)

        loadKoinModules(listOf(remoteDataSourceModule, repositoryModule))
        initCacheData()
        initToast()
        initThirdPlatform(application)
    }


    //初始化缓存数据
    private fun initCacheData() {
        OauthRepository.init()
        UserRepository.init()
    }

    /**
     * 初始化三方平台
     */
    private fun initThirdPlatform(context: Context) {
        Social.init(
            context,
            CommPlatConfigBean(PlatformType.WEIXIN, ThirdKey.WEICHAT_APP_ID),
            CommPlatConfigBean(PlatformType.QQ, ThirdKey.TENCENT_APPID),
            CommPlatConfigBean(PlatformType.WEIXIN_CIRCLE, ThirdKey.WEICHAT_APP_ID),
        )
    }

    private fun initToast() {
        val screenHeight = ScreenUtils.getScreenHeight()
        ToastUtils.getDefaultMaker().apply {
            setBgResource(R.drawable.bg_black)
            setGravity(Gravity.TOP, 0, (screenHeight * 0.3).toInt())
            setTextColor(Color.WHITE)
        }
    }


    override fun onTerminate(application: Application) {}

    companion object{
        lateinit var INSTANCE: Application
    }

}