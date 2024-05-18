package cn.dreamfruits.yaoguo.module.publish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.mention.TopicBean

/**
 * 热门话题 adapter
 */
class HotTopicListAdapter(
    val onItemClickListener: ((TopicBean) -> Unit)? = null
) : RecyclerView.Adapter<HotTopicListAdapter.HotTopicViewHolder>() {

     var dataList = mutableListOf<TopicBean>()
         private set


    fun addData(data: List<TopicBean>) {
        dataList.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotTopicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hot_topic, parent, false)
        return HotTopicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HotTopicViewHolder, position: Int) {
        val data = dataList[position]

        holder.topicNameTv.text = data.name
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class HotTopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicNameTv: TextView = itemView.findViewById(R.id.tv_topic_name)
    }
}