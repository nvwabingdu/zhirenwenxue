package cn.dreamfruits.yaoguo.module.selectclothes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.ImageLoader
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean

/**
 * 已选择的单品 adapter
 */
class SelectedClothesAdapter(
    val onDeleteClick: ((ClothesBean) -> Unit)? = null,
    val isShowDel: Boolean = true,
) :
    RecyclerView.Adapter<SelectedClothesAdapter.SelectedClothesViewHolder>() {

    private var dataList = mutableListOf<ClothesBean>()


    fun setData(data: List<ClothesBean>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedClothesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_clothes, parent, false)
        return SelectedClothesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SelectedClothesViewHolder, position: Int) {
        val data = dataList[position]

        val cover = holder.cover
        ImageLoader.loadImage(
            data.coverUrl,
            cover,
            cover.measuredWidth,
            cover.measuredHeight
        )

        holder.deleteClothesIv.setOnClickListener {

            dataList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataList.size - position)
            onDeleteClick?.invoke(data)
        }

        if (!isShowDel) {
            holder.deleteClothesIv.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class SelectedClothesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover: ImageView = itemView.findViewById(R.id.iv_clothes_cover)
        val deleteClothesIv: ImageView = itemView.findViewById(R.id.iv_delete_clothes)
    }

}