package cn.dreamfruits.yaoguo.module.main.mine.feed.incollect

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.labeldetails.LabelDetailsActivity
import cn.dreamfruits.yaoguo.repository.bean.feed.GetCollectListBean

/**
 * @Author qiwangi
 * @Date 2023/3/5
 * @TIME 23:23
 */
class CollectLabelAdapter (var mContent: Context, private var dataList:MutableList<GetCollectListBean.Item>) : RecyclerView.Adapter<CollectLabelAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_center_label, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /**预加载回调*/
        if (dataList.size > 3 && position == dataList.size - 3) {
            mInterface!!.onPreload()
        }
        holder.labelTitle.text=dataList[position].name
        holder.labelNumber.text=dataList[position].feedCount.toString()+"篇帖子"


        holder.itemView.setOnClickListener {
            //跳转到话题详情

            // mRichTextInterface.onclick(tempName,tempType);
            val intent = Intent(mContent, LabelDetailsActivity::class.java) //视频列表页面
            intent.putExtra("type", "#")
            intent.putExtra("label", dataList[position].name)
            intent.putExtra("id", dataList[position].id.toString()) //string的id
            mContent.startActivity(intent)

        }
        setImgList(holder, position)
    }

    private fun setImgList(holder:MyViewHolder, position: Int) {
        var mLayoutManager = LinearLayoutManager(mContent, LinearLayoutManager.HORIZONTAL, false)
        holder.labelRecyclerview!!.layoutManager = mLayoutManager

        var adapter = LabelItemAdapter(mContent, dataList[position].feedPics)
        holder.labelRecyclerview!!.adapter = adapter
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labelTitle: TextView = itemView.findViewById(R.id.label_title)
        var labelNumber: TextView = itemView.findViewById(R.id.label_number)
        var labelRecyclerview: RecyclerView = itemView.findViewById(R.id.label_Recyclerview)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 设置数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(tempList: MutableList<GetCollectListBean.Item>, isClear: Boolean) {
        if (isClear) {
            Log.e("a1212",tempList.size.toString());
            this.dataList.clear()
            this.dataList = tempList
            notifyDataSetChanged()
        } else {
            this.dataList += tempList
            notifyItemRangeChanged(dataList.size, dataList.size+tempList.size)
        }
    }


    interface InnerInterface {
        fun onCareUser(id: Long, isCare: Boolean)
        fun onPreload()
    }

    private var mInterface: InnerInterface? = null

    fun setCollectLabelAdapterCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }

}