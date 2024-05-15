package com.example.zrtool.net.httpurlconnection

import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.DataOutputStream
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
 * @TIME 21:35
 */
class MyHttpUrlConnection : AppCompatActivity() {
    fun main() {
        sendRequestWithHttpURLConnection()
    }

    /**
     * 理解基础的网络请求原理   HttpCLient在6.0废弃   HttpUrlConnection推荐使用
     */
    private fun sendRequestWithHttpURLConnection() {
        //开启线程发起网络请求
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuilder()
                val url = URL("https://www.baidu.com")
                connection = url.openConnection() as HttpURLConnection

                //用post方式
                /*connection.requestMethod="POST"
                val output= DataOutputStream(connection.outputStream)
                output.writeBytes("username=aa&password=12121")*/

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
                /**1.切换到UI线程，展示控件什么的*/
                showResponse(response.toString())
            } catch (e: Exception) {
                /**2.失败打印异常*/
                e.printStackTrace()
            } finally {
                /**3.关闭连接*/
                connection?.disconnect()
            }
        }
    }


    /**
     * 在这里进行UI操作 相当于切换到UI线程
     */
    private fun showResponse(response: String) {
        runOnUiThread {
            /**在这里进行UI操作，将结果显示到界面上*/
        }
    }












}