package com.example.zrwenxue.moudel.main.pageone

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class DictionaryOfChinesePoetryAdapter(
    private var dataList: MutableList<DictionaryOfChinesePoetryBean>
                            ) : RecyclerView.Adapter<DictionaryOfChinesePoetryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dictionaryofchinesepoetry, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = dataList[position].title
        holder.author.text = dataList[position].dynasty+"·"+dataList[position].author
        holder.content.text = dataList[position].content.replace("\\n", System.getProperty("line.separator"))
        holder.genre.text = dataList[position].genre
        if ( dataList[position].genre.equals("")){
            holder.genre.visibility=View.GONE
        }else{
            holder.genre.visibility=View.VISIBLE
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tv1)
        var author: TextView = itemView.findViewById(R.id.tv2)
        var content: TextView = itemView.findViewById(R.id.tv3)
        var genre: TextView = itemView.findViewById(R.id.tv4)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(){
        notifyDataSetChanged()
    }
}
