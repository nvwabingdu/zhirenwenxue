package cn.dreamfruits.yaoguo.adapter.base.listener

import androidx.recyclerview.widget.RecyclerView

/**
 * @author Lee
 * @createTime 2023-06-20 14 GMT+8
 * @desc :
 */
interface OnItemDragListener {
    fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int)

    fun onItemDragMoving(
        source: RecyclerView.ViewHolder?,
        from: Int,
        target: RecyclerView.ViewHolder?,
        to: Int,
    )

    fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int)
}