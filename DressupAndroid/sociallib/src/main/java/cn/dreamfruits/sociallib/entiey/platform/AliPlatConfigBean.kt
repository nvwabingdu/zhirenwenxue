package cn.dreamfruits.sociallib.entiey.platform

import cn.dreamfruits.sociallib.config.PlatformType


/**
 * description:支付宝的配置实体类
 */
data class AliPlatConfigBean(
    override val name: PlatformType, // 平台类型
    override var appkey: String?,          // 应用id
    var authToken: String   // 授权token
) : PlatformConfig