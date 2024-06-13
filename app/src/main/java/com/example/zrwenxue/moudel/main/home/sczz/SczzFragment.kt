package com.example.zrwenxue.moudel.main.home.sczz

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.moudel.main.pagefour.CommonAdapter
import com.example.zrwenxue.moudel.main.pagefour.DicBean
import com.example.zrwenxue.moudel.main.pagetwo.FlexBoxLayoutMaxLines
import com.google.android.flexbox.FlexWrap
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.ArrayList

/**
 * 中国诗词词典 作者篇
 */
class SczzFragment : Fragment() {
    private var mRootView: View? = null
    private var mRecyclerview: RecyclerView? = null
    private var mList: MutableList<DicBean.Item>? = ArrayList()//推荐页面的feed列表

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_one, container, false)
        mRecyclerview = mRootView?.findViewById(R.id.recyclerView)
        Log.e("adadadada","我进来了哦")
        return mRootView
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
        Log.e("tag123",""+mList!!.size)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(){
        //第一步：读取assets文件夹下的文本内容
        //第二步：取出相应的内容装在集合
        val assetManager = context!!.assets
        try {
            val inputStream = assetManager.open("dict/zgsccd.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                mList!!.add(
                    DicBean.Item(
                        extractTextBetweenTags(line!!,"<2>","<3>").split("·")[0]+"·"+extractTextBetweenTags(line!!,"<2>","<3>").split("·")[1],
                        "",
                        ""
                    )
                )
            }

            reader.close()

//           val  mList3=mList!!.distinctBy { it.hanzi }//去重后的集合

            /**
             * 去重加上统计数量
             */
            val mList3 = mList!!.groupingBy { it.hanzi }
                .eachCount()
                .map { (hanzi, count) -> DicBean.Item("$hanzi ($count)", "","") }
                .toList()

//            Log.e("adad",mList3.toString())





            // 创建一个文件对象
            val file = File("C:\\Users\\1\\Desktop\\新建文件夹\\1.txt", "rw")

            // 将集合遍历输出到文件
            file.bufferedWriter().use { writer ->
                for (item in mList3) {
                    writer.write(item.hanzi)
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
            Log.e("111111111", e.toString())
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
}