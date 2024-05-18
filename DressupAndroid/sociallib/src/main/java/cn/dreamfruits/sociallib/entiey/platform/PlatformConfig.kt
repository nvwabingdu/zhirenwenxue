package cn.dreamfruits.sociallib.entiey.platform

import cn.dreamfruits.sociallib.config.PlatformType

/**
 *
 */
interface PlatformConfig {
    val name: PlatformType     // 平台类型
    var appkey:String?          // 应用id
}