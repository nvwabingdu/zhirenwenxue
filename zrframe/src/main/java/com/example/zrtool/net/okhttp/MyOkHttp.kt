package com.example.zrtool.net.okhttp

import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @Author qiwangi
 * @Date 2023/8/1
 * @TIME 21:47
 */
class MyOkHttp : AppCompatActivity() {
    /**
     * 最基本的用法
     */
    fun method() {
        //配置客户端
        val client = okhttp3.OkHttpClient()
        //创建请求对象
        val request = okhttp3.Request.Builder()
            .url("https://www.baidu.com")
            .build()
        //发起请求
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                TODO("Not yet implemented")
            }
        })

        //另一种写法
        /*val response=client.newCall(request).execute()
        val responseData=response.body?.string()*/
    }

    /**
     * 用法二 post
     */
    fun method2() {
        //配置客户端
        val client = okhttp3.OkHttpClient()
        //创建请求体
        val requestBody = FormBody.Builder()
            .add("username", "aa")
            .add("password", "12121")
            .build()
        //创建请求对象
        val request = Request.Builder()
            .url("https://www.baidu.com")
            .post(requestBody)
            .build()
        //发起请求
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                //失败
            }

            override fun onResponse(call: Call, response: Response) {
                //成功
            }
        })
        //另一种写法
        /*val response=client.newCall(request).execute()
        val responseData=response.body?.string()*/
    }


}