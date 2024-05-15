package com.example.zrwenxue.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author qiwangi
 * @Date 2023/8/5
 * @TIME 21:08
 * Retrofit构建器
 */
object RetrofitCreator {
    //baseUrl
    private const val BASE_URL = "https://www.baidu.com"

    //retrofit实例
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //创建retrofit实例
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    //泛型实化
    inline fun <reified T> create(): T = create(T::class.java)
}