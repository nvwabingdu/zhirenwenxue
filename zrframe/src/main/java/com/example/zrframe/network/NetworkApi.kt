package com.example.zrframe.network

import com.example.zrframe.network.INetworkService
import com.example.zrframe.network.base.BaseNetworkApi

/**
 * 网络请求具体实现
 * 需要部署服务端：https://github.com/huannan/XArchServer
 */
object NetworkApi : BaseNetworkApi<INetworkService>("http://172.16.47.112:8080/XArchServer/") {

    suspend fun requestVideoDetail(id: String) = getResult {
        service.requestVideoDetail(id)
    }
}