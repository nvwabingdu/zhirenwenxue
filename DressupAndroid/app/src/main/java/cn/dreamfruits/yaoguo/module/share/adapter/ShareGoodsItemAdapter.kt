package cn.dreamfruits.yaoguo.module.share.adapter

import android.content.Context
import android.widget.ImageView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.util.loadRectImg

/**
 * @author Lee
 * @createTime 2023-07-10 16 GMT+8
 * @desc :
 */
class ShareGoodsItemAdapter(var mContext: Context) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_share_goods_item) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.getView<ImageView>(R.id.iv_cover).loadRectImg(mContext, item)

    }
}