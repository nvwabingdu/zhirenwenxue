package com.example.zrspider

import com.example.zrtool.utilsjava.HtmlUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

/**
 * @Author qiwangi
 * @Date 2023/8/13
 * @TIME 17:52
 */



//解析网页的文字和下载图片
fun main() {
    val url = "https://item.jd.com/100063915202.html" // 要爬取的网页URL

    try {
        // 发送HTTP GET请求获取网页内容
         val doc = Jsoup.connect(url).get()

        // 获取网页的文字内容
        val text = doc.text()
        println("网页文字内容：$text")

        // 获取网页的图片链接
        val images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]")
        for (image in images) {
            val imageUrl = image.absUrl("src")

            // 下载图片到本地
            val fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
            val imageStream = URL(imageUrl).openStream()

            val output = FileOutputStream("C:\\Users\\wq\\Desktop\\PinSheng_spiker\\$fileName")
//            val output = FileOutputStream("path/to/save/$fileName")
            output.write(imageStream.readBytes())
            output.close()
            imageStream.close()

            println("已下载图片：$fileName")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


//解析网页的源码 就是html
fun main0() {
    val url = URL("https://www.liuhaiying.cn/33561.html")
    val connection = url.openConnection()
    val reader = BufferedReader(InputStreamReader(connection.getInputStream()))

    var inputLine: String?
    val content = StringBuilder()

    while (reader.readLine().also { inputLine = it } != null) {
        content.append(inputLine)
    }
    reader.close()

    println(content.toString())
    println(com.example.zrtool.utils.HtmlUtils.keepSomeTags(content.toString(),
        "body"))

}