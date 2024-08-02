package com.example.zrspider


import android.os.Build
import androidx.annotation.RequiresApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.random.Random
import kotlin.streams.toList
import kotlin.system.measureTimeMillis
var time:Int = 0
@RequiresApi(Build.VERSION_CODES.O)
fun main() {




//    "一画-二画-三画-四画-五画-六画-七画-八画-九画-十画-十一画-十二画-十三画-十四画-十五画-十六画-十七画-十八画-十九画-二十画-二十一画-二十二画-二十三画-二十四画-二十五画-二十六画-二十七画-二十八画-二十九画-三十画-三十一画-三十二画-三十三画-三十四画".split("-").forEach{
//        temp02(it)
//    }


    get006("")




//    //提取网页字典
//    val url = "https://zd.hwxnet.com/search.do?keyword=" // 替换为你想爬取的网页地址
//    ('\u4e00'..'\u9fff').map {
//        temp1(url,it.toString())
//    }
}


fun temp1(html: String,hz:String){

    val doc: Document = Jsoup.connect(html+hz).get()

    // 提取所有文本
    val ss = doc.body().toString().replace(Regex("\\s+"), "")
    val sss = ss.substring(ss.indexOf("解释及意思") + 5, ss.lastIndexOf("分享到"))
    val text0 = "$hz   <h1>$hz</h1>" + sss.replace(
        sss.substring(
            sss.indexOf("分享到"),
            sss.lastIndexOf("基本字义解释") - 4
        ), ""
    )


    val file = File("C:\\Users\\1\\Desktop\\132\\123.txt")

    // 如果文件不存在,则创建文件
    if (!file.exists()) {
        file.createNewFile()
    }

    // 以追加模式打开文件
    file.appendText("$text0\n", Charset.forName("UTF-8"))

    Thread.sleep(Random.nextInt(20, 100).toLong())// 随机睡眠

    println(time++)

}


fun temp02(tag:String){
    val inputFile = File("C:\\Users\\1\\Desktop\\字典.txt")
    val outputFile = File("C:\\Users\\1\\Desktop\\1\\$tag.txt")


//    println(tag)

    // 读取输入文件的内容
    val inputLines = mutableListOf<String>()
    inputFile.bufferedReader().use { reader ->
        reader.lineSequence().forEach { line ->
            inputLines.add(line)
        }
    }

    // 对每一行进行操作
    val outputLines = inputLines.map { line ->
        // 在这里对 line 进行操作
//        line.toUpperCase()

        if (line.contains("总笔画：</div><spanclass=\"spwid80\">"+tag)){
//            println(extractTextBeforeDelimiter(line, "<").trim())
          extractTextBeforeDelimiter(line, "<").trim()

        }else{
            ""
        }

//        line.replace(Regex("\\s+"), "")
//        println(line.replace(Regex("\\s+"), ""))

    }
//
    // 将操作后的内容写入输出文件
    outputFile.bufferedWriter().use { writer ->
        outputLines.forEach { line ->
            writer.write(line)
//            writer.newLine()
        }
    }

    println("文件处理完成!")
}



fun extractTextBeforeDelimiter(line: String, delimiter: String): String {
    val index = line.indexOf(delimiter)
    return if (index != -1) {
        line.substring(0, index)
    } else {
        line
    }
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


/**
 * 将所有txt文件内容合并输出到一个txt文件
 */
@RequiresApi(Build.VERSION_CODES.O)
fun get006(html: String) {
    val sourceDirectory = "C:\\Users\\1\\Desktop\\1"
    val outputFile = "C:\\Users\\1\\Desktop\\file.txt"

    // 创建输出文件
    val outputFilePath = Paths.get(outputFile)
    Files.createFile(outputFilePath)

    // 遍历源目录下的所有 TXT 文件
    val txtFiles = Files.walk(Paths.get(sourceDirectory))
        .filter { Files.isRegularFile(it) && it.toString().endsWith(".txt") }
        .toList()

    // 将所有文件内容写入输出文件
    BufferedWriter(FileWriter(outputFile, true)).use { writer ->
        for (file in txtFiles) {

//            writer.write("${file.fileName}")
            writer.write(file.fileName.toString().removeSuffix(".txt")+"|")
            Files.readAllLines(file).forEach { line ->
                writer.write("$line\n")
            }
        }
    }

    println("文件内容已成功输出至 $outputFile")

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