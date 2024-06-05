package com.example.zrwenxue.moudel.main.pagefour

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newzr.R

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class DicAdapter_four(
    private var dataList: List<DicBean>
                            ) : RecyclerView.Adapter<DicAdapter_four.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dic_four, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hanzi.text = dataList[position].hanzi



        //设置适配器
        val m= StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL)
        holder.recyclerView_dic.layoutManager = m
        holder.recyclerView_dic.isNestedScrollingEnabled=false//解决滑动冲突
        val mAdapter = DicAdapter(dataList[position].list)
        holder.recyclerView_dic.adapter = mAdapter
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hanzi: TextView = itemView.findViewById(R.id.tv1)
        var recyclerView_dic: RecyclerView = itemView.findViewById(R.id.recyclerView_dic)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
