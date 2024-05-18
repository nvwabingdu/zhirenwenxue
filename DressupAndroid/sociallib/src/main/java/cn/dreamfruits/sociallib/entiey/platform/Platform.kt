package cn.dreamfruits.sociallib.entiey.platform

import cn.dreamfruits.sociallib.config.OperationType


/**
 * description: 平台信息
 *
 */
data class Platform(
    var platConfig: PlatformConfig,                   // 平台配置
    var availableOperationType: List<OperationType>
)  // 平台操作