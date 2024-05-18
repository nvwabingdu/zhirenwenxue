package cn.dreamfruits.sociallib

import android.content.Context
import android.util.Log
import cn.dreamfruits.sociallib.config.OperationType
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.platform.Platform
import cn.dreamfruits.sociallib.entiey.platform.PlatformConfig
import cn.dreamfruits.sociallib.handler.SSOHandler
import cn.dreamfruits.sociallib.handler.qq.QQHandler
import cn.dreamfruits.sociallib.handler.wx.WXHandler

/***
 * 平台配置管理类
 */
internal object PlatformManager {

    private const val TAG = "PlatformManager"

    /**
     * 保存可用的平台信息
     */
    var availablePlatMap: HashMap<PlatformType, Platform> = HashMap()


    /**
     * 当前正在处理的handler
     */
    var currentHandler: SSOHandler? = null


    /**
     * 初始化平台配置
     * @param config 平台配置
     * @return 是否初始化成功
     */
    fun initPlat(context: Context, config: PlatformConfig): Boolean {
        when (config.name) {
            PlatformType.WEIXIN -> {
                val wxHandler = WXHandler(context, config)
                if (wxHandler.isInstalled) {
                    // 微信平台支持的操作
                    val opList = wxHandler.getAvailableOperations()
                    wxHandler.release()
                    availablePlatMap[PlatformType.WEIXIN] = Platform(config, opList)
                    //availablePlatMap[PlatformType.WEIXIN_CIRCLE] = Platform(config, opList)
                    return true
                }
            }
            PlatformType.WEIXIN_CIRCLE -> {
                val wxHandler = WXHandler(context, config)
                if (wxHandler.isInstalled) {
                    // 微信平台支持的操作
                    val opList = wxHandler.getAvailableOperations()
                    wxHandler.release()
                    availablePlatMap[PlatformType.WEIXIN_CIRCLE] = Platform(config, opList)
                    //availablePlatMap[PlatformType.WEIXIN_CIRCLE] = Platform(config, opList)
                    return true
                }
            }
            PlatformType.QQ ->{
                val qqHandler = QQHandler(context,config)
                if (qqHandler.isInstalled){
                    val opList = qqHandler.getAvailableOperation()
                    qqHandler.release()
                    availablePlatMap[PlatformType.QQ] = Platform(config, opList)
                    return true
                }
            }

            //......
            else -> {
                Log.d(TAG, "初始化出错：不存在的平台")
                return false
            }
        }

        return false
    }

    /**
     * 平台是否支持对应的操作
     * @param platType 平台类型
     * @param opType 操作类型
     */
    fun available4PlatAndOperation(platType: PlatformType, opType: OperationType): Boolean {
        if (!availablePlatMap.containsKey(platType)) {
            return false
        }
        val platform: Platform? = availablePlatMap[platType]
        return platform?.availableOperationType?.contains(opType) ?: false
    }


    /**
     * 获取对应的handler
     */
    fun getPlatHandler(context: Context, type: PlatformType): SSOHandler? {
        return when (type) {
            PlatformType.WEIXIN ->{
                val config = availablePlatMap[PlatformType.WEIXIN]?.platConfig ?: return null
                WXHandler(context,config)
            }
            PlatformType.WEIXIN_CIRCLE ->{
                val config = availablePlatMap[PlatformType.WEIXIN_CIRCLE]?.platConfig ?: return null
                WXHandler(context,config)
            }
            PlatformType.QQ -> {
                val config = availablePlatMap[PlatformType.QQ]?.platConfig ?: return null
                QQHandler(context, config)
            }

            else -> return null
        }
    }


}