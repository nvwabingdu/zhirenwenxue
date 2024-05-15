package com.example.zrlearn.a_android.fourcomponents.services

import android.app.IntentService
import android.content.Intent
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

/**
 * 异步的可以自动开启子线程自动停止的服务 已过时
 */
class MyIntentService : IntentService("MyIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        //这个方法在子线程中运行

        //这里刚好写一个网络请求
        var connection:HttpURLConnection?=null
        try {
            val response=StringBuilder()
            val url =URL("http://wwww.baidu.com")
            connection=url.openConnection() as HttpURLConnection
            //用post方式
            connection.requestMethod="POST"
            val output=DataOutputStream(connection.outputStream)
            output.writeBytes("username=aa&password=12121")

            connection.connectTimeout=8000
            connection.readTimeout=8000
            val input=connection.inputStream
            //下面对输入流进行读取
            val  reader=BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    response.append(it)
                }
            }
            showResponse(response.toString())
        }catch (e:Exception){
            //异常输出
        }
    }

    private fun showResponse(response:String){
//        runOnuiThreead{
//            //在这里进行UI操作
//            response.text=response
//        }
    }

}