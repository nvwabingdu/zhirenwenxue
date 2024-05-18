package cn.dreamfruits.yaoguo.module.main.home.searchhistory.guessyouwanttosearch

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.search.GetGuessHotWordsBean
import kotlinx.coroutines.NonDisposableHandle.parent

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 15:30
 */
class GuessYouWantToSearchAdapter (private var dataList:MutableList<String>) : RecyclerView.Adapter<GuessYouWantToSearchAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_textview_width_dp154, parent, false)
        var viewHolder= MyViewHolder(view)






        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.contentTextView.text=dataList[position]
        holder.contentTextView.setOnClickListener {
            mInterface!!.onclick(dataList[position])
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentTextView: TextView =itemView.findViewById(R.id.cnxs_content_textview)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<String>, isClear: Boolean) {
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

    fun setGuessYouWantToSearchAdapterCallBack(mInterface: InnerInterface){
        this.mInterface=mInterface
    }

}
