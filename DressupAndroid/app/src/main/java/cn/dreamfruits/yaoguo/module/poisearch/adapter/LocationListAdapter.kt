package cn.dreamfruits.yaoguo.module.poisearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.location.PoiItemBean

/**
 * 周边位置 adapter
 */
class LocationListAdapter: RecyclerView.Adapter<LocationListAdapter.LocationViewHolder>() {

    private val dataList = mutableListOf<PoiItemBean>()


    companion object {

        const val TYPE_NO_SELECT = 1

        const val TYPE_LOCATION = 2
    }


    fun setData(data: List<PoiItemBean>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_NO_SELECT
        } else {
            TYPE_LOCATION
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == TYPE_NO_SELECT) {
            holder.select.visibility = View.GONE
            holder.address.visibility = View.GONE
            holder.title.text = "不显示位置"

            holder.itemView.setOnClickListener {
                mListener?.onNoSelect()
            }
        } else if (viewType == TYPE_LOCATION) {
            val data = dataList[position - 1]
            holder.title.text = data.title

            holder.address.text = "${data.provinceName}${data.cityName}${data.snippet}"
            if (data.isSelect) {
                holder.select.visibility = View.VISIBLE
            } else {
                holder.select.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                mListener?.onLocationClick(data)
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size + 1
    }


    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val address: TextView = itemView.findViewById(R.id.tv_address)
        val select: ImageView = itemView.findViewById(R.id.iv_selected)
    }


    interface OnItemClickListener {
        fun onLocationClick(poiItemBean: PoiItemBean)

        fun onNoSelect()
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

}