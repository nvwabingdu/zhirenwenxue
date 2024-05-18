package cn.dreamfruits.yaoguo.adapter.base.listener

import android.view.View
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter

/**
 * @author Lee
 * @createTime 2023-06-20 14 GMT+8
 * @desc :
 */
interface OnItemLongClickListener {
    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param adapter  the adapter
     * @param view     The view whihin the RecyclerView that was clicked and held.
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int): Boolean
}