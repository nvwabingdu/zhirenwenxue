package com.example.zrwenxue.net

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @Author qiwangi
 * @Date 2023/8/5
 * @TIME 21:40
 */
object GetApi {
    private val apiHomeService = RetrofitCreator.create(ApiHomeService::class.java)

    suspend fun getAppData() = apiHomeService.getAppData().await()

    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {


                    val body = response.body()
                    if(body!=null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))


                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }


}