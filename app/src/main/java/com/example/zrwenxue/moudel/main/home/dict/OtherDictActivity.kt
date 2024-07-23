package com.example.zrwenxue.moudel.main.home.dict

import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.BaseActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class OtherDictActivity : BaseActivity() {

    private var tempLayout: View? = null
    private var mRv0: RecyclerView? = null
    private var mRv1: RecyclerView? = null
    private var mWeb: WebView? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: OtherDictAdapter? = null
    private var mList: MutableList<OtherDictBean>? = ArrayList()
    private var receivedValue = "0"
    override fun layoutResId(): Int = R.layout.activity_other_dict

    override fun init() {
        tempLayout = findViewById(R.id.temp_layout)
        mRv0 = findViewById(R.id.rv_0)
        mRv1 = findViewById(R.id.rv_1)
        mWeb = findViewById(R.id.web_1)
        // 设置 WebView 可滚动
        mWeb!!.isVerticalScrollBarEnabled = true
        mWeb!!.isHorizontalScrollBarEnabled = false

        //获取传递过来的值
        receivedValue = intent.getStringExtra("dict").toString()

        when (val flag = receivedValue.toInt()) {
            0 -> {
                setData(flag, "中医方剂大全", "各类词典/中医方剂大全.txt")
            }

            1 -> {
                setData(flag, "佛学大辞典", "各类词典/佛学大辞典.txt")
            }

            2 -> {
                setData(flag, "全唐诗", "各类词典/全唐诗.txt")
            }

            3 -> {
                setData(flag, "古汉语常用词典", "各类词典/古汉语常用词典.txt")
            }

            4 -> {
                setData(flag, "唐诗三百首", "各类词典/唐诗三百首.txt")
            }

            5 -> {
                setData(flag, "姓氏起源", "各类词典/姓氏起源.txt")
            }

            6 -> {
                setData(flag, "宋词鉴赏大辞典", "各类词典/宋词鉴赏大辞典.txt")
            }

            7 -> {
                setData(flag, "家常菜", "各类词典/家常菜.txt")
            }

            8 -> {
                setData(flag, "成语词典", "各类词典/成语词典.txt")
            }

            9 -> {
                setData(flag, "掌上法律库", "各类词典/掌上法律库.txt")
            }

            10 -> {
                setData(flag, "本草纲目", "各类词典/本草纲目.txt")
            }

            11 -> {
                setData(flag, "唐代诗人简介", "各类词典/唐代诗人简介.txt")
            }
        }
    }

    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView(str: String) {
        topView = findViewById(R.id.title_view)
        topView!!.setTitle(str)
        //左边返回
        topView!!.setOnclickLeft(
            View.VISIBLE,
            View.OnClickListener {
                if (tempLayout != null && tempLayout!!.getVisibility() == View.GONE) {
                    tempLayout!!.setVisibility(View.VISIBLE)
                } else {
                    finish()
                }
            }
        )
        //右边弹出pop
        topView!!.setOnclickRight(View.INVISIBLE, View.OnClickListener { })

        topView!!.setOnclickRight(
            View.VISIBLE, resources.getDrawable(R.drawable.show_yb2)
        ) {
            if (tempLayout != null && tempLayout!!.getVisibility() == View.GONE) {
                tempLayout!!.setVisibility(View.VISIBLE)
            } else {
                tempLayout!!.setVisibility(View.GONE)
            }
        }
    }

    /**
     * 复写下这个方法
     */
    override fun onBackPressed() {
        if (tempLayout != null && tempLayout!!.getVisibility() == View.GONE) {
            tempLayout!!.setVisibility(View.VISIBLE)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 设置数据
     */
    fun setData(tagNum: Int, dict_name: String, as_dict_name: String) {
        //设置顶部
        setTopView(dict_name)

        var firstWebText = ""
        var isFirst = true
        //第一步：读取assets文件夹下的文本内容
        //第二步：取出相应的内容装在集合
        val assetManager = this.assets
        try {
            val inputStream = assetManager.open(as_dict_name)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            var num:Int=1
            while (reader.readLine().also { line = it } != null) {

                //用于第一次使用
                if (isFirst) {
                    firstWebText = setDict2(line.toString());
                    isFirst = false
                }


                //一条一条的装
                mList!!.add(
                    OtherDictBean(
                        setDict1(line.toString(),"<",num++),
                        setDict2(line.toString()),
                        false
                    )
                )

                mList!![0].isOnclick = true
            }
            reader.close()

            //适配器
            mRv1!!.isNestedScrollingEnabled = false//解决滑动冲突
            mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            mRv1!!.layoutManager = mLayoutManager
            mAdapter = OtherDictAdapter(this, mList!!)
            mRv1!!.adapter = mAdapter

        } catch (e: Exception) {
            Log.e("tag", e.toString())
        }
        //加载第一次
        mWeb!!.loadDataWithBaseURL(null, firstWebText, "text/html", "UTF-8", null)

        //回调
        mAdapter!!.setOtherDictAdapterCallBack(object : OtherDictAdapter.InnerInterface {
            override fun onclick(description: String) {
                //点击之后 设置
                mWeb!!.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null)
            }
        })

    }


    /**
     * 不同词典做一点不同的处理
     */
    private fun setDict1(line: String,tag:String,num:Int): String {
        when(receivedValue.toInt()) {
            2 -> {
                return Single.splitStringAtFirstTag(line, tag)[0]
            }

            else -> {
                return num.toString()+"."+Single.splitStringAtFirstTag(line, tag)[0]
            }
        }
    }

    /**
     * 不同词典做一点不同的处理
     */
    private fun setDict2(line: String): String {
        when(receivedValue.toInt()) {
            1 -> {
                    return Single.splitStringAtFirstTag(line, "<")[1].replace("\\n", "")
                        .replace(Regex("\\[[A-Za-z0-9]{4}]"), "")
            }
            11 -> {
                return Single.splitStringAtFirstTag(line, "<")[1].replace("\\n", "")
                        .replace(Regex("No:\\d{4}"), "")
            }

            else -> {
                return Single.splitStringAtFirstTag(line, "<")[1].replace("\\n", "")
            }
        }
    }
}