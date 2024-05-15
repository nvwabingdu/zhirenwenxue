package com.example.zrwenxue.moudel.main.home.led

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:23
 */
class LEDdoubleAdapter (var mContent: Context, var dataList:ArrayList<LedDoubleColorBean>) : RecyclerView.Adapter<LEDdoubleAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ac_led_pop_double_color_, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.doubleTv.text=dataList[position].tvStr
        //文本颜色
        holder.doubleTv.setBackgroundColor(
            Color.argb(
                255,
                dataList[position].bgColor_R,
                dataList[position].bgColor_G,
                dataList[position].bgColor_B,
            )
        )
        holder.doubleTv.setTextColor(
            Color.argb(
                255,
                dataList[position].tvColor_R,
                dataList[position].tvColor_G,
                dataList[position].tvColor_B,
            )
        )

        holder.doubleTv.setOnClickListener {
            mInterface!!.onclick(
                dataList[position].tvStr,
                dataList[position].bgColor_R,
                dataList[position].bgColor_G,
                dataList[position].bgColor_B,

                dataList[position].tvColor_R,
                dataList[position].tvColor_G,
                dataList[position].tvColor_B,
            )//回调
        }
    }

     class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var doubleTv: TextView =itemView.findViewById(R.id.ttv)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: ArrayList<LedDoubleColorBean>, isClear: Boolean) {
        if (isClear) {
            Log.e("a1212",tempList.size.toString());
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }


    /**
     * 回调
     */
    //回调
        interface  InnerInterface{
            fun onclick(str:String,
                        bg1:Int,
                        bg2:Int,
                        bg3:Int,
                        tv1:Int,
                        tv2:Int,
                        tv3:Int,
            )
        }
        private var mInterface: InnerInterface? = null

        fun setLEDdoubleAdapterCallBack(mInterface: InnerInterface){
            this.mInterface=mInterface
        }


}