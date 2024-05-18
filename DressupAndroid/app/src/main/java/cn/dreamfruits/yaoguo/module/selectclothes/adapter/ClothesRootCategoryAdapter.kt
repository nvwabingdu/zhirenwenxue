package cn.dreamfruits.yaoguo.module.selectclothes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesRootCategoryBean


/**
 * 版型一级分类的 adapter
 */
class ClothesRootCategoryAdapter(
    val data: MutableList<ClothesRootCategoryBean>,
    val onItemSelected: ((category: ClothesRootCategoryBean) -> Unit)? = null
) :
    RecyclerView.Adapter<ClothesRootCategoryAdapter.ViewHolder>() {

    var curSelectedIndex: Int = if (data.size > 0) 0 else -1

    init {
        onItemSelected?.invoke(data[curSelectedIndex])
    }

    /**
     * 获取当前选择的分类
     */
    fun getCurRootCategory():ClothesRootCategoryBean = data[curSelectedIndex]


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clothes_root_category, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].let { item ->
            holder.categoryName.text = item.name

            if (position == curSelectedIndex) {
                holder.categoryIndicator.visibility = View.VISIBLE
            } else {
                holder.categoryIndicator.visibility = View.GONE
            }

            if (!item.enable) {
                holder.categoryName.setTextColor(Color.parseColor("#AFADAD"))
                holder.itemView.isEnabled = false
            }else{
                holder.categoryName.setTextColor(Color.parseColor("#222222"))
                holder.itemView.isEnabled = true
            }

            holder.itemView.setOnClickListener {
                if (position == curSelectedIndex) return@setOnClickListener

                curSelectedIndex = position
                onItemSelected?.invoke(item)
                notifyDataSetChanged()
            }
        }
    }


    //移动到下一个分类
    fun moveNext() {
        data[curSelectedIndex].enable = false
        var index = curSelectedIndex
        for (i in 0 .. data.size){
           if (index == data.size){
               index = 0
           }
           if (data[index].enable){
               curSelectedIndex = index
               onItemSelected?.invoke(data[index])
               notifyDataSetChanged()
               break
           }
           index ++
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.tv_category_name)
        val categoryIndicator: View = itemView.findViewById(R.id.category_indicator)
    }

}