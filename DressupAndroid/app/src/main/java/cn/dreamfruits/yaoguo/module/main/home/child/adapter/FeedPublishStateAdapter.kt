package cn.dreamfruits.yaoguo.module.main.home.child.adapter

import android.text.TextUtils
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import cn.dreamfruits.baselib.network.imageloader.glide.transformations.RoundedCornersTransformation
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.repository.bean.publish.FeedPublishStateBean
import com.blankj.utilcode.util.SizeUtils

/**
 * 发布帖子状态 adapter
 */

class FeedPublishStateAdapter :
    BaseQuickAdapter<FeedPublishStateBean, BaseViewHolder>(R.layout.item_publish_state) {

    init {
        addChildClickViewIds(R.id.tv_cancel_publish, R.id.iv_retry)
    }

    override fun convert(holder: BaseViewHolder, item: FeedPublishStateBean) {
        val data = item
        GlideApp.with(holder.itemView.context)
            .asBitmap()
            .centerCrop()
            .override(SizeUtils.dp2px(38f), SizeUtils.dp2px(38f))
            .transform(RoundedCornersTransformation(SizeUtils.dp2px(4f), 0))
            .load(data.cover)
            .into(holder.getView(R.id.iv_feed_cover))

        if (data.publishFail) {
            holder.setText(R.id.tv_uploading_title, "帖子上传失败")
            holder.setGone(R.id.iv_retry, false)
            holder.setGone(R.id.tv_error_hint, false)
            holder.setGone(R.id.tv_cancel_publish, false)
            holder.setGone(R.id.pb_error_mask, false)
            if (!TextUtils.isEmpty(data.msg)) {
                holder.setText(R.id.tv_error_hint, data.msg)
            }
        } else {
            holder.setText(R.id.tv_uploading_title, "正在上传中")
            holder.setGone(R.id.iv_retry, true)
            holder.setGone(R.id.tv_error_hint, true)
            holder.setGone(R.id.tv_cancel_publish, true)
            holder.setGone(R.id.pb_error_mask, true)
        }
    }


}
//class FeedPublishStateAdapter :
//    RecyclerView.Adapter<FeedPublishStateAdapter.FeedPublishStateViewHolder>() {
//
//    private var dataList = mutableListOf<FeedPublishStateBean>()
//
//
//    fun setData(publishStateBean: FeedPublishStateBean) {
//        dataList.add(publishStateBean)
//        notifyDataSetChanged()
//    }
//
//    fun getData() : MutableList<FeedPublishStateBean> = dataList
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedPublishStateViewHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_publish_state, parent, false)
//        return FeedPublishStateViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: FeedPublishStateViewHolder, position: Int) {
//        val data = dataList[position]
//        GlideApp.with(holder.itemView.context)
//            .asBitmap()
//            .centerCrop()
//            .override(SizeUtils.dp2px(38f), SizeUtils.dp2px(38f))
//            .transform(RoundedCornersTransformation(SizeUtils.dp2px(4f),0))
//            .load(data.cover)
//            .into(holder.cover)
//
//        if (data.publishFail){
//            holder.uploadingTitle.text = "帖子上传失败"
//            holder.retry.visibility = View.VISIBLE
//            holder.cancelPublish.visibility = View.VISIBLE
//            holder.errorHint.visibility = View.VISIBLE
//            holder.errorMask.visibility = View.VISIBLE
//        }
//
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size
//    }
//
//
//    class FeedPublishStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val cover: ImageView = itemView.findViewById(R.id.iv_feed_cover)
//        val uploadingTitle: TextView = itemView.findViewById(R.id.tv_uploading_title)
//        val errorHint: TextView = itemView.findViewById(R.id.tv_error_hint)
//        val retry: ImageView = itemView.findViewById(R.id.iv_retry)
//        val cancelPublish: ImageView = itemView.findViewById(R.id.tv_cancel_publish)
//        val textUpProgress: TextView = itemView.findViewById(R.id.tv_uploading_progress)
//        val horizontalUpProgress: ProgressBar = itemView.findViewById(R.id.pb_uploading_progress)
//        val errorMask:View = itemView.findViewById(R.id.pb_error_mask)
//    }
//
//}