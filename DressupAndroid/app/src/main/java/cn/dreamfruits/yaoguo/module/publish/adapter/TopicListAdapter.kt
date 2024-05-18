package cn.dreamfruits.yaoguo.module.publish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.mention.TopicBean
import cn.dreamfruits.yaoguo.util.NumberUtils

/**
 * 话题列表 adapter
 */
class TopicListAdapter(
    val onTopicClick: ((TopicBean) -> Unit)? = null
) : RecyclerView.Adapter<TopicListAdapter.TopicViewHolder>() {

    private val dataList = mutableListOf<TopicBean>()


    fun setData(data: List<TopicBean>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)

        return TopicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val data = dataList[position]

        holder.topicName.text = data.name
        holder.viewCount.text = NumberUtils.formatToStr(data.viewCount)
        holder.itemView.setOnClickListener {
            onTopicClick?.invoke(data)
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicName: TextView = itemView.findViewById(R.id.tv_topic_name)
        val viewCount: TextView = itemView.findViewById(R.id.tv_view_count)
    }


}