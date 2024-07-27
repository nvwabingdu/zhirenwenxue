package com.example.zrwenxue.moudel.main.pagefour.letters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.moudel.main.word.MyStatic

class LetterAdapter (
    private val mActivity: Activity,
    private var dataList: List<LettersBean>
) : RecyclerView.Adapter<LetterAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_letter, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.letterTextView.text = dataList[position].letter

        if (dataList[position].isClick){
            holder.letterTextView.setBackgroundResource(R.drawable.bg_letter_click)
            holder.letterTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.theme_up))
        }else{
            holder.letterTextView.setBackgroundResource(R.drawable.bg_letter)
            holder.letterTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.theme_text))
        }



        //点击回调事件
        holder.letterTextView.setOnClickListener {
            // 手指释放时的操作
            dataList.forEach {
                it.isClick=false
            }
            if (dataList[position].isClick){
                dataList[position].isClick=false
            }else{
                dataList[position].isClick=true
            }
            notifyDataSetChanged()
            var tempNum=0
            for (i in 0..position-1){
                tempNum=tempNum+dataList[i].pinyinList!!.size
            }
            mInterface!!.onclick(dataList[position].letter,position,tempNum)
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var letterTextView: TextView = itemView.findViewById(R.id.letter_text_view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    //回调
    interface  InnerInterface{
        fun onclick(letter: String,position: Int,count:Int)
    }
    private var mInterface: InnerInterface? = null

    fun setLetterAdapterCallBack(mInterface: InnerInterface){
        this.mInterface=mInterface
    }

}
