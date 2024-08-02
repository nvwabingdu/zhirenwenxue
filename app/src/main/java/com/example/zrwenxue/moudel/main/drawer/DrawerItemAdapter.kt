package com.example.zrwenxue.moudel.main.drawer

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrprint.extractTextBeforeDelimiter2
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.main.pagefour.DicBean
import com.example.zrwenxue.moudel.main.word.MyStatic
import kotlin.random.Random

/**
 * @Author wq
 * @Date 2023/2/22-17:20
 */
open class DrawerItemAdapter(
    private var dataList: List<DicBean.Item>
) : RecyclerView.Adapter<DrawerItemAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_drawer_item_1, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hanzi.text = dataList[position].hanzi

//        val mColor = MyStatic.getContrastingColors()
//        holder.hanzi.setBackgroundColor(mColor[0])
//        holder.hanzi.setTextColor(mColor[1])

        holder.hanzi.setOnClickListener {
            mInterface!!.onclick(dataList[position].hanzi)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hanzi: TextView = itemView.findViewById(R.id.tv1)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    //回调
    interface InnerInterface {
        fun onclick(str: String)
    }

    private var mInterface: InnerInterface? = null

    fun setDrawerItemAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }

}
