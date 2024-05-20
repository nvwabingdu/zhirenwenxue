package com.example.zrwenxue.moudel.main.pageone

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

class OneFragment : Fragment() {
    private var mRootView: View? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mRecyclerview: RecyclerView? = null
    private var mAdapter: DictionaryOfChinesePoetryAdapter? = null
    private var mList: MutableList<DictionaryOfChinesePoetryBean>? = ArrayList()//推荐页面的feed列表

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
                    DictionaryOfChinesePoetryBean(
                        extractTextBetweenTags(line!!,"<1>","<2>"),
                        extractTextBetweenTags(line!!,"<2>","<3>").split("·")[0],
                        extractTextBetweenTags(line!!,"<2>","<3>").split("·")[1],
                        extractTextBetweenTags(line!!,"<3>","<4>"),
                        extractTextBetweenTags(line!!,"<4>","<5>"))
                )
            }
            reader.close()

            //适配器
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerview!!.layoutManager = mLayoutManager
            mAdapter = DictionaryOfChinesePoetryAdapter(mList!!)
            mRecyclerview!!.adapter = mAdapter

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