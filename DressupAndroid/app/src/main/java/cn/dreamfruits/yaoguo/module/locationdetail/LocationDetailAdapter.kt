package cn.dreamfruits.yaoguo.module.locationdetail


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.editprofile.ProfileCurrentLocalBean
import cn.dreamfruits.yaoguo.repository.bean.editprofile.ProfileLocationBean
import com.blankj.utilcode.util.StringUtils


/**
 * 位置详情适配器
 */
class LocationDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_LOCATION_DETAIL = 0
        const val TYPE_CURRENT_LOCATION = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && dataList[position] is ProfileCurrentLocalBean) TYPE_CURRENT_LOCATION else TYPE_LOCATION_DETAIL
    }

    var dataList = mutableListOf<Any>()

    private var mCountry: String? = null
    private var mProvince: String? = null
    private var mCity: String? = null



    fun setData(data: MutableList<Any>) {
        this.dataList.clear()
        this.dataList.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_LOCATION_DETAIL -> {
                val view = LayoutInflater
                    .from(parent.context).inflate(R.layout.location_item_layout, parent, false)
                LocationDetailViewHolder(view)
            }
            TYPE_CURRENT_LOCATION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.current_location_item_layout, parent, false)
                CurrentLocationViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("viewType is not correct")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_CURRENT_LOCATION) {
            val data = dataList[position] as ProfileCurrentLocalBean
            val holder = holder as CurrentLocationViewHolder

            if (data.isError) {

                holder.addressName.text = "正在定位…"//wq  和下面交换
                holder.locationIcon.setBackgroundResource(R.drawable.ic_red_alert)

            } else {
                holder.locationIcon.setBackgroundResource(R.drawable.ic_blue_location)
                if (StringUtils.isEmpty(data.country)){
                holder.addressName.text = "无法获取到你的位置，可以在设置中开启定位"//wq

                }else{
                    holder.addressName.text = "${data.country} ${data.province} ${data.city}"
                }
            }

            holder.itemView.setOnClickListener {
                if (!StringUtils.isEmpty(data.country)){
                    listener?.chose(data.country, data.province, data.city)
                }
            }

        } else if (getItemViewType(position) == TYPE_LOCATION_DETAIL) {
            val data = dataList[position]

            //如果是国家
            if (data is ProfileLocationBean.Country) {
                bindState(
                    holder as LocationDetailViewHolder,
                    data.name,
                    data.isChose,
                    data.administrative_area != null && data.administrative_area.size > 0,
                    data.firstItem,
                    data.lastItem
                )
                // 如果是省份
            } else if (data is ProfileLocationBean.Province) {
                bindState(
                    holder as LocationDetailViewHolder,
                    data.name,
                    data.isChose,
                    data.sub_administrative_area != null && data.sub_administrative_area.size > 0,
                    data.firstItem,
                    data.lastItem
                )
                // 如果是城市
            } else if (data is ProfileLocationBean.City) {
                bindState(
                    holder as LocationDetailViewHolder,
                    data.name,
                    data.isChose,
                    false,
                    data.firstItem,
                    data.lastItem
                )
            }

            holder.itemView.setOnClickListener {
                if (data is ProfileLocationBean.Country) {
                    mCountry = data.name
                    if (data.administrative_area != null && data.administrative_area.size > 0) {

                        listener?.next(data)
                    } else {
                        listener?.chose(mCountry, null, null)
                    }

                } else if (data is ProfileLocationBean.Province) {
                    mProvince = data.name
                    if (data.sub_administrative_area != null && data.sub_administrative_area.size > 0) {

                        listener?.next(data)
                    } else {
                        listener?.chose(mCountry, mProvince, null)
                    }
                } else if (data is ProfileLocationBean.City) {
                    listener?.chose(mCountry, mProvince, data.name)
                }
            }
        }
    }

    /**
     * 绑定
     */
    private fun bindState(
        holder: LocationDetailViewHolder,
        addressName: String,
        isChose: Boolean,
        hasNext: Boolean,
        isFirst: Boolean,
        isLast: Boolean,
    ) {

        if (isFirst) holder.allLocation.visibility =
            View.VISIBLE else holder.allLocation.visibility = View.GONE

        if (isChose) holder.isChose.visibility =
            View.VISIBLE else holder.isChose.visibility = View.GONE

        if (isLast) holder.itemDivider.visibility =
            View.GONE else holder.itemDivider.visibility = View.VISIBLE

        if (hasNext) holder.nextIcon.visibility =
            View.VISIBLE else holder.nextIcon.visibility = View.GONE

        holder.addressName.text = addressName
    }


    override fun getItemCount(): Int {
        return this.dataList.size
    }


    /**
     * 地址详情viewholder
     */
    class LocationDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val allLocation: TextView = itemView.findViewById(R.id.all_location_view)
        val addressName: TextView = itemView.findViewById(R.id.address_name)
        val nextIcon: ImageView = itemView.findViewById(R.id.right_next)
        val isChose: TextView = itemView.findViewById(R.id.is_chose)
        val itemDivider: View = itemView.findViewById(R.id.first_item_divider)
    }

    /**
     * 当前位置viewholder
     */
    class CurrentLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationIcon: ImageView = itemView.findViewById(R.id.location_icon)
        val addressName: TextView = itemView.findViewById(R.id.current_address_name)
    }


    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {

        fun next(data: Any)

        fun chose(country: String?, province: String?, city: String?)
    }


}