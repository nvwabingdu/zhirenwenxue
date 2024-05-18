package cn.dreamfruits.yaoguo.module.main.message

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.repository.bean.message.BlackListBean
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.util.loadRoundImg
import com.blankj.utilcode.util.ColorUtils

/**
 * @author Lee
 * @createTime 2023-07-03 19 GMT+8
 * @desc :
 */
class BlackListAdapter(var mContext: Context) :
    BaseQuickAdapter<BlackListBean, BaseViewHolder>(R.layout.item_black_list) {

    init {
        addChildClickViewIds(R.id.tv_black)
    }

    override fun convert(holder: BaseViewHolder, item: BlackListBean) {
        holder.getView<ImageView>(R.id.iv_user_head_img)
            .loadRoundImg(mContext, item.avatarUrl.decodePicUrls())

        holder.setText(R.id.tv_name, item.nickName)

//        var relation: Int,//0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
        if (item.relation == 1 || item.relation == 3) {
            holder.setText(R.id.tv_black, "取消拉黑")
            holder.setBackgroundResource(R.id.tv_black, R.drawable.shape_black_list_item_btn_bg)
            holder.setTextColor(R.id.tv_black, Color.parseColor("#B3B3B3"))
        } else {
            holder.setBackgroundResource(R.id.tv_black, R.drawable.bg_black)
            holder.setText(R.id.tv_black, "拉黑")
            holder.setTextColor(R.id.tv_black, ColorUtils.getColor(R.color.white))
        }
    }
}