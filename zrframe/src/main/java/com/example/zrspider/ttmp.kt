package com.example.zrspider

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun main() {
    val url = "https://mzd.diyifanwen.com/zidian/A/0931804030228200795.htm"
    val document: Document = Jsoup.connect(url).get()
    val sourceCode = document.html()
    println(sourceCode)
}