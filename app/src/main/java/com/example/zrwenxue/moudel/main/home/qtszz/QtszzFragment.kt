package com.example.zrwenxue.moudel.main.home.qtszz

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.moudel.main.pagefour.CommonAdapter
import com.example.zrwenxue.moudel.main.pagefour.DicBean
import com.example.zrwenxue.moudel.main.pagetwo.FlexBoxLayoutMaxLines
import com.google.android.flexbox.FlexWrap
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

/**
 * 中国诗词词典 作者篇
 */
class QtszzFragment : Fragment() {
    private var mRootView: View? = null
    private var mRecyclerview: RecyclerView? = null
    private var mList: MutableList<DicBean>? = ArrayList()//推荐页面的feed列表

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
            val inputStream = assetManager.open("dict/qtszz.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                mList!!.add(
                    DicBean(
                        extractTextBetweenTags(line!!,"<1>","<2>"),
                        "",
                        extractTextBetweenTags(line!!,"<2>","<3>"),
                    )
                )
            }

            reader.close()

            //设置适配器
            mRecyclerview!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
            val mFlexboxLayoutManager= FlexBoxLayoutMaxLines(requireActivity())
            mFlexboxLayoutManager.flexWrap = FlexWrap.WRAP // 设置换行方式为换行
            mRecyclerview!!.layoutManager = mFlexboxLayoutManager
            mRecyclerview!!.setHasFixedSize(false)//设置item大小是否固定 关键参数  这里设置为不固定
            val mAdapter = CommonAdapter(mList!!)
            mRecyclerview!!.adapter = mAdapter

            //回调
            mAdapter.setCommonAdapterCallBack(object : CommonAdapter.InnerInterface{
                override fun onclick(explan: String) {

                    //显示作者的生平简述
                    // 创建 AlertDialog 对象
                    val builder = AlertDialog.Builder(requireActivity())

                    // 设置对话框的内容
                    val authorInfoTextView = TextView(requireActivity())
                    authorInfoTextView.text = explan
                    authorInfoTextView.gravity = Gravity.CENTER // 设置文字居中
                    authorInfoTextView.setPadding(32, 32, 32, 32) // 添加一些内边距

                    // 设置对话框的视图
                    builder.setView(authorInfoTextView)

                    // 显示对话框
                    val dialog = builder.create()
                    dialog.show()
                }
            })

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





    //生平简述弹窗




}