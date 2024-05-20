package com.example.zrwenxue.moudel.main.pagetwo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R
import com.google.android.flexbox.FlexWrap

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class DictionaryIdiomAdapter(
    val mContext: Context,
    private var dataList: MutableList<DictionaryIdiomIBean>
) : RecyclerView.Adapter<DictionaryIdiomAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dictionaryidiom, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = dataList[position].pinyinIndex
        if (dataList[position].isOpen) {
            holder.mRecyclerView.visibility = View.VISIBLE

//            //设置适配器
//            holder.mRecyclerView.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
//            val mFlexboxLayoutManager= FlexBoxLayoutMaxLines(mContext)
//            mFlexboxLayoutManager.flexWrap = FlexWrap.WRAP // 设置换行方式为换行
//            holder.mRecyclerView.layoutManager = mFlexboxLayoutManager
//            holder.mRecyclerView.setHasFixedSize(false)//设置item大小是否固定 关键参数  这里设置为不固定
//            val mAdapter =DictionaryIdiomItemAdapter(dataList[position].list)
//            holder.mRecyclerView.adapter = mAdapter


            //设置适配器
            val m= StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            holder.mRecyclerView.layoutManager = m
            holder.mRecyclerView.isNestedScrollingEnabled=false//解决滑动冲突
            val mAdapter =DictionaryIdiomItemAdapter(dataList[position].list)
            holder.mRecyclerView.adapter = mAdapter


        } else {
            holder.mRecyclerView.visibility = View.GONE
        }
    }


    /**
     * 添加一个内部的适配器
     */
    class DictionaryIdiomItemAdapter(
        private var dataList: MutableList<DictionaryIdiomIBean.Item>
    ) : RecyclerView.Adapter<DictionaryIdiomItemAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dictionaryidiom_item, parent, false)
            return MyViewHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.idiom.text = dataList[position].idiom

        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var idiom: TextView = itemView.findViewById(R.id.lsss_content_textview)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.pinyin)
        var mRecyclerView: MaxRecyclerView = itemView.findViewById(R.id.recyclerView)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
