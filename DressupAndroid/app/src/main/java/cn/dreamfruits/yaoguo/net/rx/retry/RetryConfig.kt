package cn.dreamfruits.yaoguo.net.rx.retry

import io.reactivex.rxjava3.core.Single

/**
 * 请求重连配置
 */
data class RetryConfig(
    //最大重连次数
    val maxRetries: Int = 1,
    //重试间隔次数
    val delay: Int = 500,
    val condition:() ->Single<Boolean> = { Single.just(false) }
)