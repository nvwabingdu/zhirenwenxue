package cn.dreamfruits.yaoguo.module.main.home.searchhistory.acidheatindex

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.search.GetYgHotFeedListBean
import kotlinx.coroutines.NonDisposableHandle.parent

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 15:27
 */
class AcidHeatIndexAdapter(var mContent: Context, private var dataList: MutableList<GetYgHotFeedListBean.Item>) :
    RecyclerView.Adapter<AcidHeatIndexAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_item_textview_num_textview_content, parent, false)
        var viewHolder = MyViewHolder(view)



        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean = dataList[position]//获取一个实体对象
        holder.contentTextView.text = bean.title
        holder.numberTextView.text = (position+1).toString()
        when (position) {
            0 -> {
                holder.numberTextView.background = mContent.getDrawable(R.drawable.home_hot_1)
                holder.numberTextView.setTextColor(mContent.getColor(R.color.transparent))
            }
            1 -> {
                holder.numberTextView.background = mContent.getDrawable(R.drawable.home_hot_2)
                holder.numberTextView.setTextColor(mContent.getColor(R.color.transparent))
            }
            2 -> {
                holder.numberTextView.background = mContent.getDrawable(R.drawable.home_hot_3)
                holder.numberTextView.setTextColor(mContent.getColor(R.color.transparent))
            }
            else -> holder.numberTextView.setTextColor(mContent.getColor(R.color.color_a16355))
        }

        holder.itemView.setOnClickListener {
            mInterface!!.onclick(bean.title)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentTextView: TextView = itemView.findViewById(R.id.srb_content)
        var numberTextView: TextView = itemView.findViewById(R.id.srb_num)
        var view: View = itemView.findViewById(R.id.srb_view)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<GetYgHotFeedListBean.Item>, isClear: Boolean) {
        if (isClear) {
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }


    //回调
        interface  InnerInterface{
            fun onclick(title: String)
        }
        private var mInterface: InnerInterface? = null

        fun setAcidHeatIndexAdapterCallBack(mInterface: InnerInterface){
            this.mInterface=mInterface
        }

}
