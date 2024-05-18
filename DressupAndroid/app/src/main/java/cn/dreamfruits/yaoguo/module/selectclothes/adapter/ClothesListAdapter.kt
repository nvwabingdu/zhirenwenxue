package cn.dreamfruits.yaoguo.module.selectclothes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.ImageLoader
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean

/**
 * 单品列表 adapter
 */
class ClothesListAdapter(val onAddClick:((ClothesBean) ->Unit)? =null) : RecyclerView.Adapter<ClothesListAdapter.ClothesListViewHolder>() {

    private var dataList = mutableListOf<ClothesBean>()


    fun addData(data:List<ClothesBean>){
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData(){
        dataList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clothes, parent, false)

        return ClothesListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClothesListViewHolder, position: Int) {
        val data = dataList[position]

        ImageLoader.loadImage(
           data.picUrls!![0],
           holder.cover,
           holder.cover.measuredWidth,
           holder.cover.measuredHeight
        )
        holder.label.text = "官方"

        holder.add.setOnClickListener {
            onAddClick?.invoke(data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ClothesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover: ImageView = itemView.findViewById(R.id.iv_clothes_cover)
        val label: TextView = itemView.findViewById(R.id.tv_clothes_label)
        val add: ImageView = itemView.findViewById(R.id.iv_add_clothes)

    }


}