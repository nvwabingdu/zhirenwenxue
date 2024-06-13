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
import com.example.zrwenxue.app.TitleBarView
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

/**
 * 中国诗词词典
 */
class OneFragment : Fragment() {
    private var mRootView: View? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mRecyclerview: RecyclerView? = null
    private var mAdapter: DictionaryOfChinesePoetryAdapter? = null
    private var mList: MutableList<DictionaryOfChinesePoetryBean>? = ArrayList()//推荐页面的feed列表

    //设置筛选

    private var items1: MutableList<ListItem>? = ArrayList()//推荐页面的feed列表
    private var items2: MutableList<ListItem>? = ArrayList()//推荐页面的feed列表
    private var items3: MutableList<ListItem>? = ArrayList()//推荐页面的feed列表
    private var items4: MutableList<ListItem>? = ArrayList()//推荐页面的feed列表
    fun setS() {
        //第一步：读取assets文件夹下的文本内容
        //第二步：取出相应的内容装在集合
        val assetManager = context!!.assets
        try {
            val inputStream = assetManager.open("dict/中国诗词朝代.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line!!.equals("先秦")) {
                    items1!!.add(ListItem(line!!, true))
                } else {
                    items1!!.add(ListItem(line!!, false))
                }

            }

            reader.close()

            val adapter1 = HorizontalListAdapter(items1, requireActivity())
            val recyclerView1 = mRootView?.findViewById<RecyclerView>(R.id.re_1)
            recyclerView1!!.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView1.adapter = adapter1

            //第一个回调
            adapter1.setHorizontalListAdapterCallBack(object :
                HorizontalListAdapter.InnerInterface {
                override fun onclick(item: String) {

                    items2!!.clear()

                    val inputStream2 = assetManager.open("dict/中国诗词作者.txt")
                    val reader2 = BufferedReader(InputStreamReader(inputStream2))

                    var line2: String?
                    while (reader2.readLine().also { line2 = it } != null) {

                        if (line2!!.split("-")[0].split("·")[0] == item) {
                            items2!!.add(ListItem(line2!!.split("-")[0].split("·")[1], false))
                        }
                    }

                    reader2.close()

                    val adapter2 = HorizontalListAdapter(items2, requireActivity())
                    val recyclerView2 = mRootView?.findViewById<RecyclerView>(R.id.re_2)
                    recyclerView2!!.layoutManager = LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    recyclerView2.adapter = adapter2

                    adapter2.setHorizontalListAdapterCallBack(object :
                        HorizontalListAdapter.InnerInterface {
                        override fun onclick(item: String) {



                        }
                    })


                }
            })

        } catch (e: Exception) {
            Log.e("111111111", e.toString())
        }





        items3!!.add(ListItem("五言", false))
        items3!!.add(ListItem("诗歌", false))
        items3!!.add(ListItem("曲", false))
        items3!!.add(ListItem("词", false))

        val adapter3 = HorizontalListAdapter(items3, requireActivity())
        val recyclerView3 = mRootView?.findViewById<RecyclerView>(R.id.re_3)
        recyclerView3!!.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.adapter = adapter3
        adapter3.setHorizontalListAdapterCallBack(object :
            HorizontalListAdapter.InnerInterface {
            override fun onclick(item: String) {



            }
        })


        val numbers = (0..9854).toList()
        val result = groupNumbersAndGetRanges(numbers)
        println(result) // 输出 ["1-5", "6-10", "11-15", "16-20", "21-25", "26-30", "31-35"]


        result.forEach {
            items4!!.add(ListItem(it, false))
        }


        val adapter4 = HorizontalListAdapter(items4, requireActivity())
        val recyclerView4 = mRootView?.findViewById<RecyclerView>(R.id.re_4)
        recyclerView4!!.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView4.adapter = adapter4

        adapter4.setHorizontalListAdapterCallBack(object :
            HorizontalListAdapter.InnerInterface {
            override fun onclick(item: String) {



            }
        })


    }

    private fun groupNumbersAndGetRanges(numbers: List<Int>, groupSize: Int = 100): List<String> {
        val ranges = mutableListOf<String>()
        for (i in numbers.indices.step(groupSize)) {
            val group = numbers.subList(i, minOf(i + groupSize, numbers.size))
            val min = group.minOf { it }
            val max = group.maxOf { it }
            ranges.add("$min-$max")
        }
        return ranges
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_one, container, false)
        mRecyclerview = mRootView?.findViewById(R.id.recyclerView)
        Log.e("adadadada", "我进来了哦")

        setTopView()

        return mRootView
    }


    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView() {
        topView = mRootView?.findViewById(R.id.title_bar)
        topView!!.title = "诗词词典大全"
        //左边返回
        topView!!.setOnclickLeft(
            View.INVISIBLE,
            View.OnClickListener { })
        //右边弹出pop
        topView!!.setOnclickRight(View.VISIBLE, View.OnClickListener { })
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setS()
        setData()
//        setData2("","","","","")
        Log.e("tag123", "" + mList!!.size)
    }


    private var mTempList: MutableList<DictionaryOfChinesePoetryBean>? = ArrayList()//推荐页面的feed列表
    fun setData2(
        name: String,
        chaodai: String,
        zuozhe: String,
        ticai: String,
        num: String
    ): List<DictionaryOfChinesePoetryBean> {

        mList!!.forEach {
            if (ticai == "全部") {
                if (chaodai == it.dynasty && zuozhe == it.author) {
                    mTempList!!.add(it)
                }
            } else {
                if (chaodai == it.dynasty && zuozhe == it.author && ticai == it.genre) {
                    mTempList!!.add(it)
                }
            }
        }

        return mTempList!!.slice(num.split("-")[0].toInt() until num.split("-")[1].toInt())
    }


    //查找所有的数据 并形成集合
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    fun setData() {
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
                        extractTextBetweenTags(line!!, "<1>", "<2>"),//名字
                        extractTextBetweenTags(line!!, "<2>", "<3>").split("·")[0],//朝代
                        extractTextBetweenTags(line!!, "<2>", "<3>").split("·")[1],//作者
                        extractTextBetweenTags(line!!, "<3>", "<4>"),//内容
                        extractTextBetweenTags(line!!, "<4>", "<5>")
                    )//体裁
                )
            }
            reader.close()

            //适配器1
            mLayoutManager = LinearLayoutManager(context)
            mRecyclerview!!.layoutManager = mLayoutManager
            mAdapter = DictionaryOfChinesePoetryAdapter(mList!!)
            mRecyclerview!!.adapter = mAdapter

        } catch (e: Exception) {
            Log.e("111111111", e.toString())
        }
    }

    private fun extractTextBetweenTags(input: String, leftStr: String, rightStr: String): String {
        val startIndex = input.indexOf(leftStr)
        val endIndex = input.indexOf(rightStr)

        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            input.substring(startIndex + leftStr.length, endIndex)
        } else {
            ""
        }
    }
}