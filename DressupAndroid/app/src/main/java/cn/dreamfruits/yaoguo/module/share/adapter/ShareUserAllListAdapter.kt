package cn.dreamfruits.yaoguo.module.share.adapter

import android.content.Context
import android.widget.ImageView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseSectionQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.module.share.bean.ShareUserListBean
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.util.loadRoundImg

/**
 * @author Lee
 * @createTime 2023-07-06 16 GMT+8
 * @desc :
 */
class ShareUserAllListAdapter(var mContext: Context) :
    BaseSectionQuickAdapter<UserInfo, BaseViewHolder>(
        R.layout.item_share_user_head,
        R.layout.item_share_user_list
    ) {

    override fun convert(holder: BaseViewHolder, item: UserInfo) {
        holder.getView<ImageView>(R.id.iv_user_head_img)
            .loadRoundImg(mContext, item.avatarUrlX)
        holder.setText(R.id.tv_nick_name, item.nickName)
        holder.getView<ImageView>(R.id.iv_sel).isSelected = item.isSel

    }

    override fun convertHeader(helper: BaseViewHolder, item: UserInfo) {
        helper.setText(R.id.tv_header_text, item.firstKey.uppercase())

        helper.getView<ImageView>(R.id.iv_user_head_img)
            .loadRoundImg(mContext, item.avatarUrlX)
        helper.setText(R.id.tv_nick_name, item.nickName)
        helper.getView<ImageView>(R.id.iv_sel).isSelected = item.isSel


    }
}