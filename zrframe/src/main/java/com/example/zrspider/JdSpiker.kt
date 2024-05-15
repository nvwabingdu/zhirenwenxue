package com.example.zrspider



import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

fun main() {
    val url = "https://www.xiaohongshu.com/explore/640af45e000000001300e6ee" // 替换为你想爬取的网页地址
    val doc: Document = Jsoup.connect(url).get()

    // 提取所有文本
    val text = doc.body().text()
    println(text)

    // 提取所有图片链接
    val images = doc.getElementsByTag("img")
    for (image in images) {
        val imgUrl: String = image.attr("abs:src")
        println(imgUrl)
    }
}