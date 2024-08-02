package com.example.zrwenxue.moudel.main.pagetwo

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
import com.example.zrtool.ui.noslidingconflictview.MaxRecyclerView
import com.example.zrwenxue.app.Single
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class DictionaryIdiomAdapter(
    val mContext: Activity,
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
        holder.title.text = "【"+dataList[position].pinyinIndex+"】音的成语共有"+dataList[position].list.size+"个"

        holder.title.setOnClickListener {
            if (dataList[position].isOpen) {
                dataList[position].isOpen=false
            }else{
                dataList[position].isOpen=true
            }
            notifyDataSetChanged()
        }


        if (dataList[position].isOpen) {
            holder.mRecyclerView.visibility = View.VISIBLE
            //设置适配器
            val m= StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            holder.mRecyclerView.layoutManager = m
            holder.mRecyclerView.isNestedScrollingEnabled=false//解决滑动冲突
            val mAdapter =DictionaryIdiomItemAdapter(mContext,dataList[position].list)
            holder.mRecyclerView.adapter = mAdapter
        } else {
            holder.mRecyclerView.visibility = View.GONE
        }
    }


    /**
     * 添加一个内部的适配器
     */
    class DictionaryIdiomItemAdapter(
        val mContext: Activity,
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
            holder.idiom.setOnClickListener {
                Log.e("tag11", "======")


                val assetManager = mContext.assets
                try {
                    val inputStream = assetManager.open("dict/zgcddcd.txt")
                    val reader = BufferedReader(InputStreamReader(inputStream))

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        if (extractTextBeforeDelimiter(line!!,"<").contains(dataList[position].idiom)){
                            Single.showHtml(mContext,extractTextBeforeDelimiter2(line!!,"<").trim().replace("\\n", System.getProperty("line.separator")))
                            break
                        }
                    }
                    reader.close()
                } catch (e: Exception) {
                    Log.e("tag11", "DictionaryIdiomAdapter"+e.toString())
                }

            }
        }

        fun extractTextBeforeDelimiter(line: String, delimiter: String): String {
            val index = line.indexOf(delimiter)
            return if (index != -1) {
                line.substring(0, index)
            } else {
                line
            }
        }

        private fun extractTextBeforeDelimiter2(line: String, delimiter: String): String {
            val index = line.indexOf(delimiter)
            return if (index != -1) {
                line.substring(index, line.length)
            } else {
                line
            }
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
