package com.example.zrspider


import android.os.Build
import androidx.annotation.RequiresApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
var time:Int = 0
@RequiresApi(Build.VERSION_CODES.O)
fun main() {




//    "一画-二画-三画-四画-五画-六画-七画-八画-九画-十画-十一画-十二画-十三画-十四画-十五画-十六画-十七画-十八画-十九画-二十画-二十一画-二十二画-二十三画-二十四画-二十五画-二十六画-二十七画-二十八画-二十九画-三十画-三十一画-三十二画-三十三画-三十四画".split("-").forEach{
//        temp02(it)
//    }





//    //提取网页字典
//    val url = "https://zd.hwxnet.com/search.do?keyword=" // 替换为你想爬取的网页地址
//    ('\u4e00'..'\u9fff').map {
//        temp1(url,it.toString())
//    }
}


/**
 * 提取所有网页代码
 */
fun get001(html: String) {
    val doc: Document = Jsoup.connect(html).get()
    println(doc)
}

/**
 * 提取body源代码
 */
fun get002(html: String) {
    val doc: Document = Jsoup.connect(html).get()
    val text = doc.body()
    println(text)
}

/**
 * 提取所有文本
 */
fun get003(html: String) {
    val doc: Document = Jsoup.connect(html).get()
    val text = doc.body().text()
    println(text)
}

/**
 * 提取所有图片链接
 */
fun get004(html: String) {
    val doc: Document = Jsoup.connect(html).get()
    val images = doc.getElementsByTag("img")
    for (image in images) {
        val imgUrl: String = image.attr("abs:src")
        println(imgUrl)
    }
}

/**
 * 获取有这个字符的标记对
 *
 * <p>sdiohohgihoidiaoihoi str dadadsadada</p>  这个就可以获取到
 */
fun get005(html: String,str:String) {
    val doc: Document = Jsoup.connect(html).get()
    val text0 = doc.getElementsContainingOwnText(str)
    println(text0)
}

fun get007(html: String) {}
fun get008(html: String) {}
fun get009(html: String) {}
fun get0010(html: String) {}
fun get0011(html: String) {}
fun get0012(html: String) {}
fun get0013(html: String) {}
fun get0014(html: String) {}
fun get0015(html: String) {}
fun get0016(html: String) {}
fun get0017(html: String) {}
fun get0018(html: String) {}
fun get0019(html: String) {}


fun extractTextBeforeDelimiter(line: String, delimiter: String): String {
    val index = line.indexOf(delimiter)
    return if (index != -1) {
        line.substring(0, index)
    } else {
        line
    }
}