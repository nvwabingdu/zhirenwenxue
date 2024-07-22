package com.example.zrwenxue.moudel.main.home.dict

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.moudel.main.word.MyStatic

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class OtherDictAdapter(
    private var activity: Activity,
    private var dataList: MutableList<OtherDictBean>
) : RecyclerView.Adapter<OtherDictAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_other_dict, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = dataList[position].name
        holder.name.setOnClickListener {
            //回调
            mInterface!!.onclick(dataList[position].description)


            dataList.forEach {
                it.isOnclick=false
            }

            dataList[position].isOnclick=true

            notifyDataSetChanged()
        }

        val mColor=MyStatic.getContrastingColors()
        if ( dataList[position].isOnclick) {
//            holder.name.background = ContextCompat.getDrawable(activity, R.color.theme_up)
//            holder.name.setTextColor(ContextCompat.getColor(activity, R.color.theme_text))
            holder.name.setBackgroundColor(mColor[0])
            holder.name.setTextColor(mColor[1])
        } else {
            holder.name.background = ContextCompat.getDrawable(activity, R.color.theme_down)
            holder.name.setTextColor(ContextCompat.getColor(activity, R.color.theme_text))
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.item_tv_other)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData() {
        notifyDataSetChanged()
    }

    //回调
    interface InnerInterface {
        fun onclick(description: String)
    }

    private var mInterface: InnerInterface? = null

    fun setOtherDictAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }


}
