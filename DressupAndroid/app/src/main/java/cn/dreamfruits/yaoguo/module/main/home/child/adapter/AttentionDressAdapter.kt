package cn.dreamfruits.yaoguo.module.main.home.child.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.SingleDetailsActivity
import cn.dreamfruits.yaoguo.repository.bean.attention.RecommendUserListBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/27
 * @TIME 16:41
 */
class AttentionDressAdapter (var mContent: Context, private var dataList: MutableList<WaterfallFeedBean.Item.Info.Single>) :
    RecyclerView.Adapter<AttentionDressAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttentionDressAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_item_attention_dress, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: AttentionDressAdapter.MyViewHolder,
        position: Int
    ) {
        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .error(R.color.white)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .load(dataList[position].coverUrl)
            .into(holder.img!!)

        holder.itemView.setOnClickListener {
            val intent = Intent(mContent, SingleDetailsActivity::class.java)//帖子详情页
            intent.putExtra("id", dataList[position].id)
            mContent.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dataList.size



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.img_dress)
    }


}