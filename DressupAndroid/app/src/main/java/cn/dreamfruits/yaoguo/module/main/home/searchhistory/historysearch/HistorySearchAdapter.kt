package cn.dreamfruits.yaoguo.module.main.home.searchhistory.historysearch

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.HomeSearchDetailsActivity
import cn.dreamfruits.yaoguo.module.main.home.searchhistory.HistoryBean
import cn.dreamfruits.yaoguo.module.main.home.searchhistory.HomeSearchHistoryActivity
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.Singleton

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 15:25
 * 历史搜索adapter
 */
class HistorySearchAdapter (private val mContext: Context,private var dataList:MutableList<HistoryBean.Item>) : RecyclerView.Adapter<HistorySearchAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_textview_width_dp141, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean=dataList[position]//获取一个实体对象
        holder.contentTextView.text=setText(bean.mStr)

        holder.contentTextView.setOnClickListener {
            //跳转到搜索详情页
            mInterface!!.onclick(dataList[position].mStr)
        }
    }



    fun setText(mStr:String):String{
        return if (mStr.length>10){
            mStr.substring(0,10)+"..."
        }else{
            mStr
        }
    }



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentTextView: TextView =itemView.findViewById(R.id.lsss_content_textview)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<HistoryBean.Item>, isClear: Boolean) {
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
            fun onclick(str: String)
        }
        private var mInterface: InnerInterface? = null

        fun setHistorySearchAdapterCallBack(mInterface: InnerInterface){
            this.mInterface=mInterface
        }

}
