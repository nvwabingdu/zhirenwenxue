package com.example.zrwenxue.moudel.main.pagefour

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrtool.ui.custom.MyDrawerLayout
import com.example.zrtool.ui.noslidingconflictview.MaxRecyclerView
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.main.drawer.DrawerAdapter
import com.example.zrwenxue.moudel.main.pagefour.letters.LetterAdapter
import com.example.zrwenxue.moudel.main.pagefour.letters.LettersBean
import com.example.zrwenxue.moudel.main.pagefour.pinyin.PyAdapter
import com.example.zrwenxue.moudel.main.pagetwo.FlexBoxLayoutMaxLines
import com.google.android.flexbox.FlexWrap
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.InputStreamReader


class FourFragment : Fragment() {
    private var mRootView: View? = null
    private var mRecyclerview: RecyclerView? = null
    private var mRecyclerview_ABC: RecyclerView? = null
    private var mList: MutableList<DicBean>? = ArrayList()
    private var lettersList: MutableList<LettersBean>? = ArrayList()
    private var lettersAdapter: LetterAdapter? = null


    //右侧字母列表
    // 创建一个包含大写字母的数组
    val letters = ('A'..'Z').toList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_four, container, false)
        mRecyclerview = mRootView?.findViewById(R.id.recyclerView)
        setTopView()
        setDrawerLayout()
        return mRootView
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData2(title)
    }

    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView() {
        topView = mRootView?.findViewById(R.id.title_bar)
        topView!!.title = "字典"
        //左边返回
        topView!!.setOnclickLeft(
            View.INVISIBLE,
            View.OnClickListener { })
        //右边弹出pop
        topView!!.setOnclickRight(View.VISIBLE, resources.getDrawable(R.drawable.show_yb2)) {
            //侧边逻辑
            if (mDrawerLayout!!.isOpen) {
                mDrawerLayout!!.close()
            } else {
                mDrawerLayout!!.open()
            }
        }
    }

    /**
     * 设置侧滑布局
     */
    private var mDrawerLayout: MyDrawerLayout? = null
    private var drawerRecyclerView: RecyclerView? = null
    private var mDrawerList: MutableList<DicBean>? = ArrayList()
    private var mDrawerLayoutManager: LinearLayoutManager? = null
    private var title="通用字"
    private fun setDrawerLayout() {
        //设置侧滑布局
        mDrawerLayout = mRootView?.findViewById<MyDrawerLayout>(R.id.drawer)
        drawerRecyclerView = mRootView?.findViewById<RecyclerView>(R.id.ll_re)

        val assetManager = context!!.assets
        try {
            val inputStream = assetManager.open("dict/字典_索引.txt")
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
                title=str
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
                when(title){
                    "全选"->{
                        topView!!.title="字典"
                    }
                    "常用字"->{
                        topView!!.title=title
                    }
                    "通用字"->{
                        topView!!.title=title
                    }
                    "次常用字"->{
                        topView!!.title=title
                    }
                    "生僻字"->{
                        topView!!.title=title
                    }
                    else->{
                        topView!!.title="字典("+title+")"
                    }
                }

                setData2(title)
            }

            override fun onDrawerStateChanged(newState: Int) {
                // 抽屉状态改变时的回调
            }
        })
    }

    private var tempL: MutableList<DicBean.Item>? = null
    private var tempLetters: MutableList<String>? = null
    @SuppressLint("NotifyDataSetChanged")
    fun setData2(tag: String) {
        mList!!.clear()

        //第一步：读取assets文件夹下的文本内容
        //第二步：取出相应的内容装在集合
        val assetManager = context!!.assets
        try {

            val inputStream = assetManager.open("dict/zdsx/$tag.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            tempLetters = ArrayList()
            while (reader.readLine().also { line = it } != null) {

                tempLetters!!.add(
                    line!!.split("=")[0]
                )

                tempL = ArrayList()
                line!!.split("=")[1].split("-").forEach {
                    tempL!!.add(
                        DicBean.Item(
                            it,
                            "",
                            "",
                        )
                    )

                }

                if (tempL!!.size != 0) {
                    mList!!.add(
                        DicBean(
                            extractStringBeforePlus(line!!),
                            "open",
                            tempL!!.size,
                            tempL!!
                        )
                    )
                }
            }
            reader.close()

            //延时
            kotlinx.coroutines.runBlocking {
                delay(100)
                // 需要延迟执行的代码

                //设置适配器
                charLinearLayoutManager = LinearLayoutManager(requireActivity())
                mRecyclerview!!.layoutManager = charLinearLayoutManager
                val mAdapter = DicAdapter_four(mList!!, requireActivity())
                mRecyclerview!!.adapter = mAdapter

                //右边集合
                setData3()
            }

        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        }
    }

    /**
     * 设置右侧列表
     */
    private var charLinearLayoutManager: LinearLayoutManager? = null
    private fun setData3() {
        lettersList!!.clear()
        //装入letters
        letters.forEach {
            if (it.toString().lowercase() != "i" && it.toString()
                    .lowercase() != "v" && it.toString().lowercase() != "u"
            ) {
                val itemList: MutableList<LettersBean.Item>? = ArrayList()

                for (i in 0..<tempLetters!!.size) {
                    if (tempLetters!![i][0].toString().lowercase() == it.toString().lowercase()) {
                        itemList!!.add(
                            LettersBean.Item(
                                tempLetters!![i],
                                false
                            )

                        )
                    }
                }

                lettersList!!.add(
                    LettersBean(
                        it.toString(),
                        false,
                        itemList
                    )
                )
            }
        }

        mRecyclerview_ABC = mRootView?.findViewById(R.id.RecyclerView_ABC)
        mRecyclerview_ABC!!.layoutManager = LinearLayoutManager(requireActivity())
        lettersList!![0].isClick = true//默认第一个选中
        lettersAdapter = LetterAdapter(requireActivity(), lettersList!!)
        mRecyclerview_ABC!!.adapter = lettersAdapter

        /**
         * 点击右侧字母列表
         */
        lettersAdapter!!.setLetterAdapterCallBack(object : LetterAdapter.InnerInterface {
            override fun onclick(letter: String, position: Int, count: Int) {
                //滑动到对应的位置
                charLinearLayoutManager!!.scrollToPosition(count)


//                //这个弹窗不好看
//                //延时
//                kotlinx.coroutines.runBlocking {
//                    delay(3000)
//                    // 需要延迟执行的代码
//                    //弹出小弹窗   不好看 暂时隐藏
//                    setPyPop(lettersList!![position].pinyinList!!, count)
//                }


            }
        })
    }

    /**
     * 用于截取
     */
    private fun extractStringBeforePlus(inputString: String): String {
        val plusIndex = inputString.indexOf('=')
        return if (plusIndex != -1) {
            inputString.substring(0, plusIndex)
        } else {
            inputString
        }
    }

}