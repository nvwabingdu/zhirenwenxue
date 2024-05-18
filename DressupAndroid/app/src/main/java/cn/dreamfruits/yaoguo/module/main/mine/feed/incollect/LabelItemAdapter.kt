package cn.dreamfruits.yaoguo.module.main.mine.feed.incollect

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.repository.bean.feed.GetCollectListBean
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @Author qiwangi
 * @Date 2023/3/27
 * @TIME 16:41
 */
class LabelItemAdapter (var mContent: Context, private var dataList: MutableList<GetCollectListBean.Item.FeedPic>) :
    RecyclerView.Adapter<LabelItemAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabelItemAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_center_collect_label_img, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: LabelItemAdapter.MyViewHolder,
        position: Int
    ) {

        Glide.with(mContent)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .error(R.color.white)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .load(dataList[position].url)
            .into(holder.img!!)

    }

    override fun getItemCount(): Int = dataList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.img)
    }
}