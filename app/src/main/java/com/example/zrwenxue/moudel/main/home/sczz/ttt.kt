package com.example.zrwenxue.moudel.main.home.sczz

import android.util.Log
import com.example.zrwenxue.app.ZrApp.Companion.context
import com.example.zrwenxue.moudel.main.pagefour.DicBean
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.ArrayList

private var mList: MutableList<DicBean.Item>? = ArrayList()//推荐页面的feed列表
fun main(){
    //第一步：读取assets文件夹下的文本内容
    //第二步：取出相应的内容装在集合
    try {





        // 指定要读取的文件路径
        val filePath = "C:\\Users\\1\\Desktop\\新建文件夹\\zgsccd.txt"

        try {
            // 读取文件内容
            val file = File(filePath)
            file.bufferedReader().use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    // 对每一行做一些操作
                    mList!!.add(
                        DicBean.Item(
                            extractTextBetweenTags(
                                line!!,
                                "<2>",
                                "<3>"
                            ).split("·")[0] + "·" + extractTextBetweenTags(
                                line!!,
                                "<2>",
                                "<3>"
                            ).split("·")[1],
                            "",
                            ""
                        )
                    )
                }
            }
        } catch (e: Exception) {
            println("读取文件时出现错误: ${e.message}")
        }


//           val  mList3=mList!!.distinctBy { it.hanzi }//去重后的集合

        /**
         * 去重加上统计数量
         */
        val mList3 = mList!!.groupingBy { it.hanzi }
            .eachCount()
            .map { (hanzi, count) -> DicBean.Item("$hanzi -$count", "","") }
            .toList()

//            Log.e("adad",mList3.toString())





        // 创建一个文件对象
        val file = File("C:\\Users\\1\\Desktop\\新建文件夹\\1.txt")


        val m2: MutableList<String> = ArrayList()
        mList3.forEach{
            m2.add(it.hanzi.split("·")[0])
        }

        // 使用 toSet() 函数去重
        val uniqueDynasties = m2.toSet()

        // 将集合遍历输出到文件
        file.bufferedWriter().use { writer ->
            for (item in uniqueDynasties) {
                writer.write(item)
                writer.newLine() // 添加换行符
            }
        }

        println("内容已写入到 output.txt 文件中。")


//
//            //设置适配器
//            mRecyclerview!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
//            val mFlexboxLayoutManager= FlexBoxLayoutMaxLines(requireActivity())
//            mFlexboxLayoutManager.flexWrap = FlexWrap.WRAP // 设置换行方式为换行
//            mRecyclerview!!.layoutManager = mFlexboxLayoutManager
//            mRecyclerview!!.setHasFixedSize(false)//设置item大小是否固定 关键参数  这里设置为不固定
//            val mAdapter = CommonAdapter(mList2)
//            mRecyclerview!!.adapter = mAdapter

//            //回调
//            mAdapter.setCommonAdapterCallBack(object : CommonAdapter.InnerInterface{
//                override fun onclick(explan: String) {
//
//                }
//            })

    } catch (e: Exception) {

        println(e)
    }
}

private fun extractTextBetweenTags(input: String, leftStr:String, rightStr:String): String {
    val startIndex = input.indexOf(leftStr)
    val endIndex = input.indexOf(rightStr)

    return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
        input.substring(startIndex + leftStr.length, endIndex)
    }else{
        ""
    }
}