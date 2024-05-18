package cn.dreamfruits.yaoguo.adapter.base.listener

import android.view.View
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter

/**
 * @author Lee
 * @createTime 2023-06-20 14 GMT+8
 * @desc :
 */
interface OnItemChildClickListener {
    /**
     * callback method to be invoked when an item child in this view has been click
     *
     * @param adapter  BaseQuickAdapter
     * @param view     The view whihin the ItemView that was clicked
     * @param position The position of the view int the adapter
     */
    fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int)
}