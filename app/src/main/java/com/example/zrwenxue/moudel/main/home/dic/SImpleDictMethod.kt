//package com.example.zrwenxue.moudel.main.home.dic
//
////读取新华字典并加载单个字符 创建简表
//import android.util.Log
//import com.example.zrlearn.c_kotlin.test
//import com.example.zrwenxue.moudel.main.word.HtmlStatic
//
//
//import java.io.BufferedReader
//import java.io.File
//
////fun main1() {
////    val filePath ="""C:\Users\wq\Desktop\新华字典.txt""" // 替换为实际的文件路径
////    val lines = mutableListOf<String>()
////
////    val file = File(filePath)
////    try {
////        val reader = BufferedReader(file.reader())
////
////        var line: String?
////        while (reader.readLine().also { line = it } != null) {
////            lines.add(line!!)
////        }
////
////        reader.close()
////    } catch (e: Exception) {
////        e.printStackTrace()
////    }
////
////    lines.forEach { line ->
////        println(line)
////    }
////}
//
//
//import java.io.*
//
//fun main1() {
//    val inputFilePath = """C:\Users\wq\Desktop\新华字典.txt"""  // 替换为实际的输入文件路径
//    val outputFilePath = """C:\Users\wq\Desktop\新华字典_单字.txt"""  // 替换为实际的输出文件路径
//    val lines = mutableListOf<String>()
//    println("开始")
//    val inputFile = File(inputFilePath)
//    try {
//        val reader = BufferedReader(inputFile.reader())
//
//        var line: String?
//        while (reader.readLine().also { line = it } != null) {
//
//            val firstTagIndex = line!!.indexOf("`")
//
//
//            if (firstTagIndex != -1) {
//                val substring = line!!.substring(0, firstTagIndex)
//
//                when(substring.trim().length){
//                    1->{
//                        lines.add(line!!)
//                    }
//                    2->{
//                        val testStr=substring.substring(1,2)
////                        println(substring)
//                        if (testStr == "1" ||testStr == "2" ||testStr == "3" || testStr == "4" ||testStr == "5" ||testStr == "6" ||testStr == "7" ||testStr == "8" ||testStr == "9" ){//判断截取后的最后一个是否是数字  因为有可能是多音字 这样的 行 行1  行3
//                            lines.add(line!!)
//                        }
//                    }
//                    3->{
//                        val testStr=substring.substring(1,3)
//                        println(substring)
//                        if (testStr == "10" ||testStr == "11" ||testStr == "12" || testStr == "13" ||testStr == "14" ||testStr == "15" ||testStr == "16" ||testStr == "17" ||testStr == "18" ){//判断截取后的最后一个是否是数字  因为有可能是多音字 这样的 行 行1  行3
//                            lines.add(line!!)
//                        }
//                    }
//                }
//
//            } else {
//                print("字符串中没有tag===")
//                println(line)
//            }
//        }
//
//        reader.close()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    lines.forEach { line ->
////        println(line)
//    }
//
//    val outputFile = File(outputFilePath)
//    try {
//        val writer = BufferedWriter(FileWriter(outputFile))
//
//        lines.forEach { line ->
//            writer.write(line)
//            writer.newLine()
//        }
//
//        writer.close()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}
//
////
//fun main() {
//    val inputFile = File("""C:\Users\wq\Desktop\新华字典_单字.txt""")
//    val outputFile = File("""C:\Users\wq\Desktop\新华字典_单字_拼音.txt""")
//
//    val lines = mutableListOf<String>()
//
//    inputFile.forEachLine { line ->
//        lines.add( HtmlStatic.subStr(line,"""`2`""","""<br>""",1))
//    }
//
//    val uniqueLines = lines.toSet()
//
//    outputFile.printWriter().use { writer ->
//        uniqueLines.forEach { line ->
//            writer.println(line)
//        }
//    }
//
//    println("操作完成，结果已写入到 output.txt 文件。")
//}
