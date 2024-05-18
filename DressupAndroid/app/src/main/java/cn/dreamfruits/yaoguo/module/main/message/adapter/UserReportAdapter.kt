package cn.dreamfruits.yaoguo.module.main.message.adapter

import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.module.main.message.bean.UserReportEntity

/**
 * @author Lee
 * @createTime 2023-07-03 11 GMT+8
 * @desc :
 */
class UserReportAdapter :
    BaseQuickAdapter<UserReportEntity, BaseViewHolder>(R.layout.item_chart_user_report) {
    override fun convert(holder: BaseViewHolder, item: UserReportEntity) {
        holder.setText(R.id.tv_title, item.content)

        holder.setImageResource(
            R.id.iv_sel,
            if (item.isSel) R.drawable.ic_green_selected else R.drawable.sel
        )
    }
}