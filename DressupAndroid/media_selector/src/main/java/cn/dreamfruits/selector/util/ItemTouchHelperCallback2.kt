package cn.dreamfruits.selector.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.selector.PictureSelectedAdapter

/**
 * 交换顺序
 */
class ItemTouchHelperCallback2(val adapter:PictureSelectedAdapter) : ItemTouchHelper.Callback(){


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 得到item 原来的position
        val fromPosition = viewHolder.absoluteAdapterPosition
        // 得到目标 position
        val toPosition = target.absoluteAdapterPosition

        adapter.itemMove(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}