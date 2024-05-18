package cn.dreamfruits.yaoguo.module.share.adapter

import android.content.Context
import android.widget.ImageView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
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
class ShareUserSelAdapter(var mContext: Context) :
    BaseQuickAdapter<UserInfo, BaseViewHolder>(
        R.layout.item_share_act_sel_user
    ) {

    override fun convert(holder: BaseViewHolder, item: UserInfo) {
        holder.getView<ImageView>(R.id.iv_head_img)
            .loadRoundImg(mContext, item.avatarUrlX)
    }

}