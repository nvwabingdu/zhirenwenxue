package cn.dreamfruits.yaoguo.module.selectclothes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.ImageLoader
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesCategoryBean

/**
 * 单品分类 adapter
 */
class ClothesCategoryAdapter(
    val onItemClickListener: ((ClothesCategoryBean) -> Unit)? = null
) : RecyclerView.Adapter<ClothesCategoryAdapter.ClothesCategoryViewHolder>() {


    private val dataList = mutableListOf<ClothesCategoryBean>()


    fun addData(data: List<ClothesCategoryBean>) {
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData(){
        dataList.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesCategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clothes_category, parent, false)
        return ClothesCategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClothesCategoryViewHolder, position: Int) {
        val data = dataList[position]

        ImageLoader.loadImage(
            data.coverUrl,
            holder.cover,
            holder.cover.measuredWidth,
            holder.cover.measuredHeight
        )

        holder.name.text = data.cvName

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(data)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ClothesCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover: ImageView = itemView.findViewById(R.id.iv_clothes_category_cover)
        val name: TextView = itemView.findViewById(R.id.iv_clothes_category_name)

    }

}