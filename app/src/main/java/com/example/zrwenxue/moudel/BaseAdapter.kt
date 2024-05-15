package com.example.zrwenxue.moudel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author qiwangi
 * @Date 2023/8/18
 * @TIME 18:12
 */
abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val items: MutableList<T> = mutableListOf()

    fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        return onCreateViewHolder(view, viewType)
    }

    abstract fun getLayoutId(viewType: Int): Int

    abstract fun onCreateViewHolder(view: View, viewType: Int): VH

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        onBindViewHolder(holder, item, position)
    }

    abstract fun onBindViewHolder(holder: VH, item: T, position: Int)
}