package com.example.zrwenxue.moudel.main.pagefour

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
open class CommonAdapter(
    private var dataList: List<DicBean>
                            ) : RecyclerView.Adapter<CommonAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dictionaryidiom_item, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hanzi.text = dataList[position].hanzi

        //点击回调事件用于  全唐诗作者
        holder.hanzi.setOnClickListener {
            mInterface!!.onclick(dataList[position].num.toString())
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hanzi: TextView = itemView.findViewById(R.id.lsss_content_textview)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    //回调
        interface  InnerInterface{
            fun onclick(explan: String)
        }
        private var mInterface: InnerInterface? = null

        fun setCommonAdapterCallBack(mInterface: InnerInterface){
            this.mInterface=mInterface
        }

}
