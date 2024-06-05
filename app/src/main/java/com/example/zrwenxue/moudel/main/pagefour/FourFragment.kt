package com.example.zrwenxue.moudel.main.pagefour

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R
import com.example.zrtool.ui.custom.TitleBarView
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.main.pagetwo.TwoFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList


class FourFragment : Fragment() {
    private var mRootView: View? = null
    private var mRecyclerview: RecyclerView? = null
    private var mRecyclerview_ABC: RecyclerView? = null
    private var mList: MutableList<DicBean>? = ArrayList()//推荐页面的feed列表

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_four, container, false)
        mRecyclerview = mRootView?.findViewById(R.id.recyclerView)
        setTopView()


        mRecyclerview_ABC = mRootView?.findViewById(R.id.RecyclerView_ABC)
        mRecyclerview_ABC!!.layoutManager = LinearLayoutManager(requireActivity())
        mRecyclerview_ABC!!.adapter = LetterAdapter(letters)

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
            View.OnClickListener {  })
        //右边弹出pop
        topView!!.setOnclickRight(View.VISIBLE, View.OnClickListener { })
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData2()
        Log.e("tag123", "" + mList!!.size)
    }

//    @OptIn(DelicateCoroutinesApi::class)
//    @SuppressLint("NotifyDataSetChanged")
//    fun setData() {
//        //第一步：读取assets文件夹下的文本内容
//        //第二步：取出相应的内容装在集合
//        val assetManager = context!!.assets
//        try {
//            val inputStream = assetManager.open("dict/xhzd.txt")
//            val reader = BufferedReader(InputStreamReader(inputStream))
//
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                mList!!.add(
//                    DicBean(
//                        extractTextBetweenTags(line!!, "<1>", "<2>"),
//                        "",
//                        extractTextBetweenTags(line!!, "<2>", "<3>")
//                    )
//                )
//            }
//            reader.close()
//
//
//            //设置适配器
//            val m= StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL)
//            mRecyclerview!!.layoutManager = m
//            mRecyclerview!!.isNestedScrollingEnabled=false//解决滑动冲突
//            val mAdapter = DicAdapter(mList!!)
//            mRecyclerview!!.adapter = mAdapter
//
//
//            //回调
//            mAdapter.setDicAdapterCallBack(object : DicAdapter.InnerInterface {
//                override fun onclick(explan: String) {
//                    Single.showHtml(requireActivity(), explan)
//                }
//            })
//
//
//        } catch (e: Exception) {
//            Log.e("tag", e.toString())
//        }
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData2() {
        mList!!.clear()

        //第一步：读取assets文件夹下的文本内容
        //第二步：取出相应的内容装在集合
        val assetManager = context!!.assets
        try {
            val inputStream = assetManager.open("dict/xhzd.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {

                if (extractTextBetweenTags(line!!, "<1>", "<2>").length == 1) {
                    mList!!.add(
                        DicBean(
                            extractTextBetweenTags(line!!, "<1>", "<2>"),
                            "",
                            extractTextBetweenTags(line!!, "<2>", "<3>"),
                            null
                        )
                    )
                }

            }
            reader.close()

//            //设置适配器
//            mRecyclerview!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
//            val mFlexboxLayoutManager = FlexBoxLayoutMaxLines(requireActivity())
//            mFlexboxLayoutManager.flexWrap = FlexWrap.WRAP // 设置换行方式为换行
//            mRecyclerview!!.layoutManager = mFlexboxLayoutManager
//            mRecyclerview!!.setHasFixedSize(false)//设置item大小是否固定 关键参数  这里设置为不固定
//            val mAdapter = DicAdapter(mList!!)
//            mRecyclerview!!.adapter = mAdapter

            //设置适配器
            val m= StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL)
            mRecyclerview!!.layoutManager = m
            mRecyclerview!!.isNestedScrollingEnabled=false//解决滑动冲突
            val mAdapter = DicAdapter_four(mList!!)
            mRecyclerview!!.adapter = mAdapter


        } catch (e: Exception) {
            Log.e("tag", e.toString())
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










    //右侧字母列表
    // 创建一个包含大写字母的数组
    val letters = ('A'..'Z').toList()

    // 创建 RecyclerView 的 Adapter
    class LetterAdapter(private val letters: List<Char>) : RecyclerView.Adapter<LetterViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_letter, parent, false)
            return LetterViewHolder(view)
        }

        override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
            holder.bind(letters[position])
        }

        override fun getItemCount() = letters.size
    }

    // 创建 RecyclerView 的 ViewHolder
    class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val letterTextView: TextView = itemView.findViewById(R.id.letter_text_view)

        fun bind(letter: Char) {
            letterTextView.text = letter.toString()
        }
    }



}