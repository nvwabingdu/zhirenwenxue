package com.example.zrframe.network

import com.example.zrframe.bean.VideoBean
import com.example.zrframe.network.base.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface INetworkService {

    @GET("videodetail")
    suspend fun requestVideoDetail(@Query("id") id: String): BaseResponse<VideoBean>
}