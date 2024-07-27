package com.example.zrwenxue.moudel.main.pagefour

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.main.pagefour.letters.LetterAdapter
import com.example.zrwenxue.moudel.main.pagefour.letters.LettersBean
import com.example.zrwenxue.moudel.main.pagefour.pinyin.PyAdapter
import com.example.zrwenxue.moudel.main.pagetwo.FlexBoxLayoutMaxLines
import com.example.zrwenxue.moudel.main.pagetwo.MaxRecyclerView
import com.example.zrwenxue.moudel.main.word.MyStatic
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

        return mRootView
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
        topView!!.setOnclickRight(View.INVISIBLE, View.OnClickListener { })
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData2()
        Log.e("tag123", "" + mList!!.size)
    }

    private var tempL: MutableList<DicBean.Item>? = null
    private var tempLetters: MutableList<String>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData2() {
        mList!!.clear()

        //第一步：读取assets文件夹下的文本内容
        //第二步：取出相应的内容装在集合
        val assetManager = context!!.assets
        try {

            val inputStream = assetManager.open("dict/汉字字典2万.txt")
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

                mList!!.add(
                    DicBean(
                        extractStringBeforePlus(line!!),
                        "close",
                        line!!.split("=")[1].split("-").size,
                        tempL!!
                    )
                )


            }
            reader.close()

            //延时
            kotlinx.coroutines.runBlocking {
                delay(100)
                // 需要延迟执行的代码

                //设置适配器
                charLinearLayoutManager = LinearLayoutManager(requireActivity())
                mRecyclerview!!.layoutManager = charLinearLayoutManager
                mList!![0].pinyin="open"
                mList!![1].pinyin="open"
                val mAdapter = DicAdapter_four(mList!!, requireActivity())
                mRecyclerview!!.adapter = mAdapter



                /**
                 * Recyclerview的滚动监听
                 */
//                mRecyclerview!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                        super.onScrolled(recyclerView, dx, dy)
//
////                        Log.e("dadasdsad","1111111111111111")
//                    }
//
//                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                        super.onScrollStateChanged(recyclerView, newState)
//
////                        Log.e("dadasdsad","22222222222")
//                        // 处理滚动状态变化事件
//                        if (isflag){
//                            isflag=false
//                            kotlinx.coroutines.runBlocking {
//                                delay(100)
//                                // 需要延迟执行的代码
//
//                                val firstP = charLinearLayoutManager!!.findFirstVisibleItemPosition()
//                                lettersList!!.forEach {
//                                    it.isClick = false
//                                    if (it.letter.lowercase() == mList!![firstP].hanzi[0].lowercase()) {
//                                        it.isClick = true
//                                    }
//                                }
//
//                                lettersAdapter!!.notifyDataSetChanged()
//                                isflag=true
//                            }
//                        }
//
//
//                    }
//                })


                //右边集合
                setData3()
            }


        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        }
    }
    private var charLinearLayoutManager:LinearLayoutManager?=null
    private var isflag=true




    private fun extractTextBetweenTags(input: String, leftStr: String, rightStr: String): String {
        val startIndex = input.indexOf(leftStr)
        val endIndex = input.indexOf(rightStr)

        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            input.substring(startIndex + leftStr.length, endIndex)
        } else {
            ""
        }
    }


    fun extractStringBeforePlus(inputString: String): String {
        val plusIndex = inputString.indexOf('=')
        return if (plusIndex != -1) {
            inputString.substring(0, plusIndex)
        } else {
            inputString
        }
    }


    /**
     * 设置右边弹窗
     */
    private fun setData3() {
        lettersList!!.clear()
        //装入letters
        letters.forEach {
            if (it.toString().lowercase()!="i"&&it.toString().lowercase()!="v"&&it.toString().lowercase()!="u"){
                val itemList: MutableList<LettersBean.Item>?= ArrayList()

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
        lettersList!![0].isClick=true//默认第一个选中
        lettersAdapter = LetterAdapter(requireActivity(),lettersList!!)
        mRecyclerview_ABC!!.adapter = lettersAdapter

        /**
         * 点击右侧字母列表
         */
        lettersAdapter!!.setLetterAdapterCallBack(object : LetterAdapter.InnerInterface {
            override fun onclick(letter: String, position: Int, count: Int) {
                //滑动到对应的位置
                charLinearLayoutManager!!.scrollToPosition(count)

                //延时
                kotlinx.coroutines.runBlocking {
                    delay(100)
                    // 需要延迟执行的代码
                    //弹出小弹窗
                    setPyPop(lettersList!![position].pinyinList!!, count)
                }
            }
        })
    }


    /**
     * 弹窗
     */
    private var mPyRecyclerview: MaxRecyclerView? = null
    private var pyCancel: TextView? = null
    private var mPyFlexboxLayoutManager: FlexBoxLayoutMaxLines? = null
    private var mPyAdapter: PyAdapter? = null
    private var mPyWindow: PopupWindow? = null
    
    fun setPyPop(mPyList: MutableList<LettersBean.Item>,count:Int){
        val inflateView: View = layoutInflater.inflate(R.layout.dialog_py, null)
        mPyRecyclerview = inflateView.findViewById(R.id.re_py)
        pyCancel = inflateView.findViewById(R.id.py_cancel)


        val mColor= MyStatic.getContrastingColors()
        pyCancel!!.setBackgroundColor(mColor[0])
        pyCancel!!.setTextColor(mColor[1])

        //设置适配器
        mPyRecyclerview!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        mPyFlexboxLayoutManager = FlexBoxLayoutMaxLines(requireActivity())
        mPyFlexboxLayoutManager!!.flexWrap = FlexWrap.WRAP // 设置换行方式为换行
        mPyRecyclerview!!.layoutManager = mPyFlexboxLayoutManager
        mPyRecyclerview!!.setHasFixedSize(false)//设置item大小是否固定 关键参数  这里设置为不固定
        mPyAdapter = PyAdapter(requireActivity(), mPyList)
        mPyRecyclerview!!.adapter = mPyAdapter


        /**
         * 再次定位
         */
        mPyAdapter!!.setPyAdapterCallBack(object :PyAdapter.InnerInterface{
            override fun onclick(pinyin: String, position: Int) {
                charLinearLayoutManager!!.scrollToPosition(count+position)
                mPyWindow!!.dismiss()
            }

        })

        /**
         * pop实例
         */
        mPyWindow = PopupWindow(
            inflateView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        pyCancel!!.setOnClickListener {
            mPyWindow!!.dismiss()
        }

        //动画
//        mPyWindow!!.animationStyle = R.style.BottomDialogAnimation
        mPyWindow?.setOnDismissListener {
            Single.bgAlpha(requireActivity(), 1f) //恢复透明度
        }
        if (mPyWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            mPyWindow!!.dismiss()
        } else {
            mPyWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//?????这
            mPyWindow!!.isOutsideTouchable = true
            mPyWindow!!.isTouchable = true
            mPyWindow!!.isFocusable = true
            mPyWindow!!.showAtLocation(inflateView, Gravity.CENTER, 0, 0)
            Single.bgAlpha(requireActivity(), 0.3f) //设置透明度0.5
        }
    }




}