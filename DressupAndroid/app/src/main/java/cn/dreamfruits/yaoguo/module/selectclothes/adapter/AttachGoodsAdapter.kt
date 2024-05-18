package cn.dreamfruits.yaoguo.module.selectclothes.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.module.selectclothes.SelectClothesViewModel
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean

/**
 * @author Lee
 * @createTime 2023-06-20 11 GMT+8
 * @desc :
 */
class AttachGoodsAdapter(var mContext: Context, var viewModle: SelectClothesViewModel) :
    BaseQuickAdapter<ClothesBean, BaseViewHolder>(R.layout.item_attach_goods_sel) {

    /**
     * 最多可以选择数量
     */
    var maxSel = 9

    override fun convert(holder: BaseViewHolder, item: ClothesBean) {
        holder.setText(R.id.tv_title, item.name)
        GlideApp.with(mContext)
            .load(item.coverUrl)
            .into(holder.getView(R.id.iv_image))

        holder.getView<ImageView>(R.id.iv_sel).isSelected = item.isSel

        holder.itemView.setOnClickListener {

            if (item.isSel) {
                item.isSel = false
                viewModle.cancel(item)

            } else {
                if (viewModle.selectedClothesList.size >= maxSel) {
                    return@setOnClickListener
                }
                item.isSel = true
                viewModle.select(item)

            }
            notifyDataSetChanged()
        }
    }

}