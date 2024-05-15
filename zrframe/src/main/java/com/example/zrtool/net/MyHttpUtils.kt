package com.example.zrtool.net

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @Author qiwangi
 * @Date 2023/8/1
 * @TIME 21:57
 */
object MyHttpUtils {
    //测试  这里是使用方法
    fun method() {
        //调用HttpURLConnection的方式
        sendRequestWithHttpURLConnection("www.baidu.com", object : MyHttpCallback {
            override fun onSuccess(response: String) {
                //成功
            }

            override fun onError(e: Exception) {
                //失败
            }
        })

        //调用okhttp的方式  已经写好了callback
        sendRequestOkHttp("www.baidu.com", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                TODO("Not yet implemented")
            }

        })

    }

    /**
     * =================================使用HttpURLConnection
     */
    private fun sendRequestWithHttpURLConnection(BaseUrl: String, listener: MyHttpCallback) {
        thread {
            var connection: HttpURLConnection? = null
            //开启线程发起网络请求
            try {
                val response = StringBuilder()
                val url = URL(BaseUrl)
                connection = url.openConnection() as HttpURLConnection
                //用post方式
//                connection.requestMethod="POST"
//                val output= DataOutputStream(connection.outputStream)
//                output.writeBytes("username=aa&password=12121")
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                val input = connection.inputStream
                //下面对获取到的输入流进行读取
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                //return response.toString()
                listener.onSuccess(response.toString())//用回调设置成功返回
            } catch (e: Exception) {
                e.printStackTrace()
                //return e.message.toString()
                listener.onError(e)//用回调设置错误返回
            } finally {
                connection?.disconnect()
            }
        }
    }

    /**
     * 回调接口
     */
    interface MyHttpCallback {
        fun onSuccess(response: String)
        fun onError(e: Exception)
    }

    /**
     * 通过suspendCoroutine简化普通回调逻辑
     */
    //1.创建请求方法 已有
    //2.创建回调接口 已有
    //3.通过挂起函数调用suspendCoroutine
    private suspend fun request(address: String): String {
        //调用suspendCoroutine会立即挂起，lambda表达式会在子线程中运行
        return suspendCoroutine { continuation ->
            //这里是网络请求的代码
            sendRequestWithHttpURLConnection(address, object : MyHttpCallback {
                override fun onSuccess(response: String) {
                    continuation.resume(response)//恢复协程并传入返回值 也是suspendCoroutine的返回值
                }

                override fun onError(e: Exception) {
                    continuation.resumeWithException(e)//恢复被挂起的协程并传入异常原因
                }

            })
        }
    }
    //4.调用  由于request方法是挂起函数 因此它调用request的时候当前协程会被立刻挂起，然后一直等待网络请求成功或者失败后，当前协程才能恢复。这样即使不使用回调的写法，我们也能获取异步请求的结果，而且请求失败也会进入catch语句当中。
    suspend fun getBaidu() {
        try {
            //关键点
            val response = request("https://www.baidu.com")
            //对服务器响应的数据进行处理
        } catch (e: Exception) {
            //对异常情况进行处理
        }
    }










    /**
     * =================================使用OkHttp
     */
    private fun sendRequestOkHttp(address: String, callback: okhttp3.Callback) {
        val client = okhttp3.OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url(address)
            .build()
        client.newCall(request).enqueue(callback)//enqueue方法内部开好了子线程
    }
}