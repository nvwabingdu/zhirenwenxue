package com.example.zrwenxue.moudel.main.memory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:52
 */
class SoundMarkAdapter (private var mContent: Context,
                        var dataList: ArrayList<String>) :
    RecyclerView.Adapter<SoundMarkAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sound_mark, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv.text=dataList[position]
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView =itemView.findViewById(R.id.tv)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}