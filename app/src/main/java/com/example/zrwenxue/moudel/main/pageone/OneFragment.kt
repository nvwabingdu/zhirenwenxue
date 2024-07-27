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
    private var mList: MutableList<DictionaryOfChinesePoetryBean>? = ArrayList()
    private var mList2: MutableList<DictionaryOfChinesePoetryBean>? = ArrayList()
    private var mList3: MutableList<DictionaryOfChinesePoetryBean>? = ArrayList()
    private var mList4: MutableList<DictionaryOfChinesePoetryBean>? = ArrayList()

    //设置筛选
    private var items1: MutableList<ListItem>? = ArrayList()
    private var items2: MutableList<ListItem>? = ArrayList()
    private var items3: MutableList<ListItem>? = ArrayList()
    private var items4: MutableList<ListItem>? = ArrayList()

    var s1 = ""
    var s2 = ""
    var s4 = ""
    fun set1() {
        //第一步：读取assets文件夹下的文本内容
        //第二步：取出相应的内容装在集合
        val assetManager = context!!.assets
        try {
            val inputStream = assetManager.open("dict/中国诗词朝代.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                items1!!.add(ListItem(line!!, false))
            }

            reader.close()

            //设置默认值
            items1!![0].isSelected=true
            s1 = items1!![0].title

            val adapter1 = HorizontalListAdapter(items1, requireActivity())
            val recyclerView1 = mRootView?.findViewById<RecyclerView>(R.id.re_1)
            recyclerView1!!.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView1.adapter = adapter1

            //第一个回调
            adapter1.setHorizontalListAdapterCallBack(object :
                HorizontalListAdapter.InnerInterface {
                override fun onclick(item: String) {
                    s1 = item
                    set2()

                    mList2!!.clear()
                    mList3!!.clear()
//                    mList4!!.clear()
                    //3 开始筛选
                    for (item in mList!!) {
                        if (item.dynasty.trim() == s1.trim()) {
                            mList2!!.add(item)
                        }
                    }

                    for (item2 in mList2!!) {
                        if (item2.author.trim() == s2.trim()) {
                            mList3!!.add(item2)
                        }
                    }

                    //4.赋值
                    mSize = mList3!!.size

                    set4()

                    //设置适配器
                    mList4 = mList3!!.subList(s4.split("-")[0].toInt(), s4.split("-")[1].toInt())

                    //适配器1
                    mLayoutManager = LinearLayoutManager(context)
                    mRecyclerview!!.layoutManager = mLayoutManager
                    mAdapter = DictionaryOfChinesePoetryAdapter(mList4!!)
                    mRecyclerview!!.adapter = mAdapter
                }
            })
        } catch (e: Exception) {
            Log.e("111111111", e.toString())
        }
    }

    fun set2() {
        items2!!.clear()
        val assetManager = context!!.assets
        val inputStream2 = assetManager.open("dict/中国诗词作者.txt")
        val reader2 = BufferedReader(InputStreamReader(inputStream2))

        var line2: String?
        while (reader2.readLine().also { line2 = it } != null) {
            if (line2!!.split("-")[0].split("·")[0] == s1.trim()) {
                items2!!.add(ListItem(line2!!.split("-")[0].split("·")[1], false))
            }
        }
        reader2.close()
        
        //初始化
        items2!![0].isSelected = true
        s2 = items2!![0].title
        
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
                s2 = item

                mList3!!.clear()
//                mList4!!.clear()

                for (item2 in mList2!!) {
                    if (item2.author.trim() == s2.trim()) {
                        mList3!!.add(item2)
                    }
                }

                //4.赋值
                mSize = mList3!!.size

                set4()

                //设置适配器
                mList4 = mList3!!.subList(s4.split("-")[0].toInt(), s4.split("-")[1].toInt())

                //适配器1
                mLayoutManager = LinearLayoutManager(context)
                mRecyclerview!!.layoutManager = mLayoutManager
                mAdapter = DictionaryOfChinesePoetryAdapter(mList4!!)
                mRecyclerview!!.adapter = mAdapter

            }
        })
    }

    fun set4() {
        items4!!.clear()

        val numbers = (0..<mSize+1).toList()
        val result = groupNumbersAndGetRanges(numbers, 100)//println(result) // 输出 ["1-5", "6-10", "11-15", "16-20", "21-25", "26-30", "31-35"]
        result.forEach {
            items4!!.add(ListItem(it, false))
        }

        //初始化
        items4!![0].isSelected = true
        s4 = items4!![0].title

        val adapter4 = HorizontalListAdapter(items4, requireActivity())
        val recyclerView4 = mRootView?.findViewById<RecyclerView>(R.id.re_4)
        recyclerView4!!.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView4.adapter = adapter4

        adapter4.setHorizontalListAdapterCallBack(object :
            HorizontalListAdapter.InnerInterface {
            override fun onclick(item: String) {
                s4 = item

                mList4 = mList3!!.subList(s4.split("-")[0].toInt(), s4.split("-")[1].toInt())
                //适配器1
                mLayoutManager = LinearLayoutManager(context)
                mRecyclerview!!.layoutManager = mLayoutManager
                mAdapter = DictionaryOfChinesePoetryAdapter(mList4!!)
                mRecyclerview!!.adapter = mAdapter
            }
        })
    }

    private fun groupNumbersAndGetRanges(numbers: List<Int>, groupSize: Int): List<String> {
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
        topView!!.setOnclickRight(View.INVISIBLE, View.OnClickListener {

        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        set1()
        set2()
        setData()
    }

    var mSize = 1

    //查找所有的数据 并形成集合
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    fun setData() {
        //1.初始化三个集合
        mList2!!.clear()
        mList3!!.clear()
        mList4!!.clear()

        //2.读取assets文件夹下的文本内容
        if (mList!!.size == 0) {
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
                            extractTextBetweenTags(line!!, "<4>", "<5>")//体裁
                        )//体裁
                    )
                }
                reader.close()
            } catch (e: Exception) {
                Log.e("111111111", e.toString())
            }
        }

        //3 开始筛选
        for (item in mList!!) {
            if (item.dynasty.trim() == s1.trim()) {
                mList2!!.add(item)
            }
        }

        for (item2 in mList2!!) {
            if (item2.author.trim() == s2.trim()) {
                mList3!!.add(item2)
            }
        }

        //4.赋值
        mSize = mList3!!.size

        set4()

        //设置适配器
        mList4 = mList3!!.subList(s4.split("-")[0].toInt(), s4.split("-")[1].toInt())

        Log.e("tag13", "筛选的长度是" + mSize.toString())
        Log.e("tag13", "mList!!.size   " + mList!!.size.toString() + "")
        Log.e("tag13", "mList2!!.size  " + mList2!!.size.toString() + "")
        Log.e("tag13", "mList3!!.size  " + mList3!!.size.toString() + "")
        Log.e("tag13", "mList4!!.size  " + mList4!!.size.toString() + "")

        //适配器1
        mLayoutManager = LinearLayoutManager(context)
        mRecyclerview!!.layoutManager = mLayoutManager
        mAdapter = DictionaryOfChinesePoetryAdapter(mList4!!)
        mRecyclerview!!.adapter = mAdapter
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