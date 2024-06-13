package com.example.zrwenxue.moudel.main.pageone

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newzr.R

class HorizontalListAdapter(private val items: MutableList<ListItem>?,private val mActivity: Activity) : RecyclerView.Adapter<HorizontalListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horizontal_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items!![position]
        holder.titleTextView.text = item.title


        holder.titleTextView.setOnClickListener {

            if (!item.isSelected){
                items.forEach{
                    it.isSelected=false
                }
                item.isSelected=true
                notifyDataSetChanged()
            }

            //回调
            mInterface!!.onclick(item.title)
        }

        if (item.isSelected){
            holder.titleTextView.setBackgroundResource(R.drawable.shape_solid_f5f5f5_coners_32dp_select)
            holder.titleTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.theme_up))
        }else{
            holder.titleTextView.setBackgroundResource(R.drawable.shape_solid_f5f5f5_coners_32dp)
            holder.titleTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.theme_text))
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.lsss_content_textview)
    }

    //回调
    interface  InnerInterface{
        fun onclick(item: String)
    }
    private var mInterface: InnerInterface? = null

    fun setHorizontalListAdapterCallBack(mInterface: InnerInterface){
        this.mInterface=mInterface
    }



}
