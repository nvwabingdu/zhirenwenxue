package com.example.zrprint

import java.util.ArrayList

/**
 * @Author qiwangi
 * @Date 2023/7/3
 * @TIME 00:40
 */
class FIndViewByIdToool {

    /**
     * 入口  输出需要的找id的文本
     * fragmentTag  表示 view.   mRootView.  这个是fragment的根布局
     */
     fun out(text: String,fragmentTag:String){
                splitTags(text).forEach {
                    getss(it,fragmentTag)
                }

                val stu = StringBuffer()
                mL1!!.forEach {
                    stu.append(it+"\r\n")
                }

                println("\r\n"+stu.toString()+"\r\n")

                val stu1 = StringBuffer()
                mL0!!.forEach {
                    stu1.append(it+"\r\n")
                }
                println("\r\n"+stu1.toString()+"\r\n")
    }



    /**
     * 1.拆分标签
     */
    private fun splitTags(text: String): Array<String> {
        val tagList = mutableListOf<String>()
        var startIndex = 0
        while (true) {
            val startTagIndex = text.indexOf("<", startIndex)
            if (startTagIndex == -1) {
                break
            }
            val endTagIndex = text.indexOf(">", startTagIndex)
            if (endTagIndex == -1) {
                break
            }
            tagList.add(text.substring(startTagIndex, endTagIndex + 1))
            startIndex = endTagIndex + 1
        }
        return tagList.toTypedArray()
    }

    /**
     * 2.判断拆分出来的标签
     */
    var mL0: MutableList<String>?= ArrayList()
    var mL1: MutableList<String>?= ArrayList()
    private fun getss(str: String,fragmentTag:String){
        if (str.contains("android:id=\"@+id/")){//说明包含id
            //截取第一部分
            var tempS = extractVariableName(str, "android:id=\"@+id/", "\"")
            mL0!!.add("${toCamelCase(tempS)} = ${fragmentTag}findViewById(R.id.${tempS})")
            //截取第二部分
            mL1!!.add("private var ${toCamelCase(tempS)}: ${extractText(str.split("\r\n")[0])}? = null")
        }
    }


    /**
     *
    val xmlString1 = "<text"
    val xmlString2 = "<nini.niia.text"

    val extractedText1 = extractText(xmlString1)
    val extractedText2 = extractText(xmlString2)

    println(extractedText1)  // 输出 "text"
    println(extractedText2)  // 输出 "text"
     */
    private fun extractText(xmlString: String): String {
        val startIndex = xmlString.indexOf("<")
        if (startIndex == -1) {
            return ""
        }
        var endIndex = xmlString.indexOf(" ", startIndex)
        if (endIndex == -1) {
            endIndex = xmlString.length
        }
        var text = xmlString.substring(startIndex + 1, endIndex).trim()
        val lastDotIndex = text.lastIndexOf(".")
        if (lastDotIndex != -1) {
            text = text.substring(lastDotIndex + 1)
        }
        return text
    }


    /**
     * 返回小驼峰命名法的字符串
     */
    private fun toCamelCase(str: String): String {
        val words = str.split("_")
        val result = StringBuilder()
        for (word in words) {
            result.append(word.capitalize())
        }
        return result.toString().decapitalize()
    }

    /**
     * 3.截取id
     */
    private fun extractVariableName(xmlString: String, prefix: String, suffix: String): String {
        val startIndex = xmlString.indexOf(prefix) + prefix.length
        val endIndex = xmlString.indexOf(suffix, startIndex)
        if (startIndex == -1 || endIndex == -1) {
            return ""
        }
        val variableName = xmlString.substring(startIndex, endIndex)
        return variableName
    }

}