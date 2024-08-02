package com.example.zrwenxue.moudel.main.drawer

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R
import com.example.zrprint.extractTextBeforeDelimiter
import com.example.zrprint.extractTextBeforeDelimiter2
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.main.pagefour.DicBean
import com.example.zrwenxue.moudel.main.pagefour.pinyin.PyAdapter
import com.example.zrwenxue.moudel.main.pagetwo.FlexBoxLayoutMaxLines
import com.example.zrwenxue.moudel.main.word.MyStatic
import com.google.android.flexbox.FlexWrap
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class DrawerAdapter(
    private var dataList: List<DicBean>,
    private val mActivity: Activity
) : RecyclerView.Adapter<DrawerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_drawer, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hanzi.text = dataList[position].hanzi
        holder.num.text = dataList[position].num.toString()

        //设置适配器
        holder.recyclerView.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        val m = FlexBoxLayoutMaxLines(mActivity)
        m.flexWrap = FlexWrap.WRAP // 设置换行方式为换行
        holder.recyclerView.layoutManager = m
        holder.recyclerView.setHasFixedSize(false)//设置item大小是否固定 关键参数  这里设置为不固定
        val mDrawerAdapter = DrawerItemAdapter(dataList[position].list)
        holder.recyclerView.adapter = mDrawerAdapter


        //调用子级
        mDrawerAdapter.setDrawerItemAdapterCallBack(object :DrawerItemAdapter.InnerInterface{
            override fun onclick(str: String) {

                //继续网上面调用
                mInterface!!.onclick(str)
            }
        })
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hanzi: TextView = itemView.findViewById(R.id.tv1)
        var num: TextView = itemView.findViewById(R.id.tv2)
        var itemLayout: View = itemView.findViewById(R.id.item_layout)
        var recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    //回调
    interface InnerInterface {
        fun onclick(str: String)
    }

    private var mInterface: InnerInterface? = null

    fun setDrawerAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }

}
