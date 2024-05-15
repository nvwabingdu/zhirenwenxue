package com.example.zrframe.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zrframe.base.list.base.BaseItemViewDelegate
import com.example.zrframe.databinding.ItemGoodsBinding

class GoodsViewDelegate : BaseItemViewDelegate<GoodsViewData, GoodsViewDelegate.ViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, context: Context, parent: ViewGroup): ViewHolder {
        return ViewHolder(ItemGoodsBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: GoodsViewData) {
        super.onBindViewHolder(holder, item)
    }

    class ViewHolder(val viewBinding: ItemGoodsBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    }
}