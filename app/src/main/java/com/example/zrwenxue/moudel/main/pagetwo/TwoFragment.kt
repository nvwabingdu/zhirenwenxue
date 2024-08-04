package com.example.zrwenxue.moudel.main.pagetwo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrtool.ui.custom.MyDrawerLayout
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.main.drawer.DrawerAdapter
import com.example.zrwenxue.moudel.main.pagefour.DicBean
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class TwoFragment : Fragment() {

    private var mRootView: View? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mRecyclerview: RecyclerView? = null

    private var mAdapter: DictionaryIdiomAdapter? = null
    private var mList: MutableList<DictionaryIdiomIBean>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_two, container, false)
        mRecyclerview = mRootView?.findViewById(R.id.recyclerView)

        setTopView()
        setDrawerLayout()
        return mRootView
    }

    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView() {
        topView = mRootView?.findViewById(R.id.title_bar)
        topView!!.title = "成语词典"
        //左边返回
        topView!!.setOnclickLeft(
            View.INVISIBLE,
            View.OnClickListener { })
        //右边弹出pop
        topView!!.setOnclickRight(
            View.VISIBLE, resources.getDrawable(R.drawable.show_yb2)
        ) {
            //侧边逻辑
            if (mDrawerLayout!!.isOpen) {
                mDrawerLayout!!.close()
            } else {
                mDrawerLayout!!.open()
            }

        }
    }

    private var mDrawerLayout: MyDrawerLayout? = null
    private var drawerRecyclerView: RecyclerView? = null
    private var mDrawerList: MutableList<DicBean>? = ArrayList()
    private var tempL: MutableList<DicBean.Item>? = null
    private var mDrawerLayoutManager: LinearLayoutManager? = null
    private var title = "成语词典"

    /**
     * 设置侧滑布局
     */
    private fun setDrawerLayout() {
        //设置侧滑布局
        mDrawerLayout = mRootView?.findViewById<MyDrawerLayout>(R.id.drawer)
        drawerRecyclerView = mRootView?.findViewById<RecyclerView>(R.id.ll_re)

        val assetManager = context!!.assets
        try {
            val inputStream = assetManager.open("dict/成语索引.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {

                tempL = ArrayList()
                line!!.split("|")[1].split("-").forEach {
                    //先装子集合
                    tempL!!.add(
                        DicBean.Item(
                            it,
                            "",
                            "",
                        )
                    )
                }

                //再装大集合
                if (tempL!!.size != 0) {
                    mDrawerList!!.add(
                        DicBean(
                            line!!.split("|")[0],
                            "open",
                            tempL!!.size,
                            tempL!!
                        )
                    )
                }

            }
            reader.close()
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        }

        //设置适配器
        mDrawerLayoutManager = LinearLayoutManager(requireActivity())
        drawerRecyclerView!!.layoutManager = mDrawerLayoutManager
        val mDrawerAdapter = DrawerAdapter(mDrawerList!!, requireActivity())
        drawerRecyclerView!!.adapter = mDrawerAdapter


        //回调
        mDrawerAdapter.setDrawerAdapterCallBack(object : DrawerAdapter.InnerInterface {
            override fun onclick(str: String) {
                //点击后隐藏侧边布局
                mDrawerLayout!!.closeDrawer(Gravity.LEFT)
                title = str
            }
        })

        mDrawerLayout!!.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // 抽屉滑动时的回调
            }

            override fun onDrawerOpened(drawerView: View) {
                // 抽屉打开时的回调
            }

            override fun onDrawerClosed(drawerView: View) {
                // 抽屉关闭时的回调
                if(!topView!!.title.contains(title)){

                    setData(title)


                    if (title == "全选") {
                        topView!!.title = "成语词典-($num)"
                    } else {
                        topView!!.title = "成语词典-$title($num)"
                    }

                }
            }

            override fun onDrawerStateChanged(newState: Int) {
                // 抽屉状态改变时的回调
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData("全选")
    }


    private var mItemList: MutableList<DictionaryIdiomIBean.Item>? = null

    var num=0
    var isOpen=true
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tag: String) {
        isOpen = !(tag=="四字"||tag=="全选")

        num=0
        mList!!.clear()
        //第一步：读取assets文件夹下的文本内容
        //第二步：取出相应的内容装在集合
        val assetManager = context!!.assets
        try {

            var inputStream: InputStream? = null
            inputStream =
                if (tag.length == 5 && "AABB式-AABC式-ABAB式-ABAC式-ABCC式-ABBC式-ABCB式-ABCA式".contains(tag)) {
                    assetManager.open("dict/cysy/$tag.txt")
                } else {
                    assetManager.open("dict/成语词典_索引.txt")
                }

            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                //第一步先取出成语集合

                mItemList = ArrayList()
                line!!.split("|")[1].split("-").forEach {
                    //筛选操作
                    setData2(tag.trim(), it)
                }

                if (mItemList!!.size != 0) {
                    num+=mItemList!!.size//统计一下数量
                    //一条一条的装
                    mList!!.add(
                        DictionaryIdiomIBean(
                            line!!.split("|")[0],
                            isOpen,
                            mItemList!!
                        )
                    )
                }
            }
            reader.close()

            //适配器
            mRecyclerview!!.isNestedScrollingEnabled = false//解决滑动冲突
            mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mRecyclerview!!.layoutManager = mLayoutManager
            mAdapter = DictionaryIdiomAdapter(requireActivity(), mList!!)
            mRecyclerview!!.adapter = mAdapter

        } catch (e: Exception) {
            Log.e("tag", e.toString())
        }

    }


    /**
     *
     */
    private fun setData2(tag: String, item: String) {
        if (item != "") {

            when (tag.length) {
                1 -> {//单字的直接匹配
                    if (item.contains(tag)) {
                        mItemList!!.add(
                            DictionaryIdiomIBean.Item(
                                item
                            )
                        )
                    }
                }
                else -> {//多个字的
                    when (tag) {
                        "全选" -> {
                            mItemList!!.add(
                                DictionaryIdiomIBean.Item(
                                    item
                                )
                            )
                        }

                        "三字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 3) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "四字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 4) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "五字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 5) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "六字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 6) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "七字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 7) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "八字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 8) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "九字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 9) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "十字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 10) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "十一字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 11) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "十二字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 12) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }

                        "十四字" -> {
                            if (countCharsWithoutPunctuation(item.trim()) == 14) {
                                mItemList!!.add(
                                    DictionaryIdiomIBean.Item(
                                        item
                                    )
                                )
                            }
                        }
                        else -> {
                            mItemList!!.add(
                                DictionaryIdiomIBean.Item(
                                    item
                                )
                            )
                        }
                    }

                }
            }
        }
    }

    private fun countCharsWithoutPunctuation(str: String): Int {
        return str.replace("，", "").length
    }


    fun areFirstTwoCharsEqual(str: String, a1: Int, a2: Int): Boolean {
        return str.length >= 4 && str[a1] == str[a2]
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