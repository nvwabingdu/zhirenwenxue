package com.example.zrwenxue.moudel.main.pagefour.pinyin

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.moudel.main.pagefour.letters.LettersBean
import com.example.zrwenxue.moudel.main.word.MyStatic

class PyAdapter (
    private val mActivity: Activity,
    private var dataList: List<LettersBean.Item>
) : RecyclerView.Adapter<PyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_py, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvPy.text = dataList[position].pinyin

//        val mColor= MyStatic.getContrastingColors()
//        holder.tvPy.setBackgroundColor(mColor[0])
//        holder.tvPy.setTextColor(mColor[1])

        //点击回调事件
        holder.tvPy.setOnClickListener {
            mInterface!!.onclick(dataList[position].pinyin,position)
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPy: TextView = itemView.findViewById(R.id.tv_py)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    //回调
    interface  InnerInterface{
        fun onclick(pinyin: String,position: Int)
    }
    private var mInterface: InnerInterface? = null

    fun setPyAdapterCallBack(mInterface: InnerInterface){
        this.mInterface=mInterface
    }

}
