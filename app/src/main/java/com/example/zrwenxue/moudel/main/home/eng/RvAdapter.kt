package com.example.zrwenxue.moudel.main.home.eng

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R
import com.example.zrwenxue.moudel.main.word.singleworddetails.WordPronunciation

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-15
 * Time: 21:01
 */
open class RvAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mDataList = mutableListOf<String>()


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = mDataList[position]
        //找控件
        holder.itemView.findViewById<TextView>(R.id.eng_item_textView).text = data
        holder.itemView.setOnClickListener {
            setOnItemClickListener(data)
        }
    }


    override fun getItemCount(): Int = mDataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mContext=parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.fragment_word_recycler_item, parent, false)
        return ViewHolder(view)
    }

    //设置点击事件  没有用回调
    private fun setOnItemClickListener(data: String) {
        val mp3url= "http://ssl.gstatic.com/dictionary/static/sounds/oxford/${data.toLowerCase()}--_us_1.mp3"
        val wordPronunciation = WordPronunciation(MediaPlayer(), mp3url)
        wordPronunciation.play()
    }

    /**
     * 添加一个内部类设置id，绑定view
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



    fun setData(dataList: List<String>) {
        mDataList.clear()
        mDataList.addAll(dataList)
        notifyDataSetChanged()
    }

}