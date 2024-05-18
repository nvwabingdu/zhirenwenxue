package cn.dreamfruits.yaoguo.module.publish.adapter


import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.VibrateUtils
import java.util.*

/**
 * 拖动item 交互
 */
class ItemTouchHelperCallback(val adapter: MediaListAdapter) : ItemTouchHelper.Callback() {


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        val itemViewType = viewHolder.itemViewType
        if (itemViewType == MediaListAdapter.ADAPTER_TYPE_IMAGE) {
//            viewHolder.itemView.alpha = 0.2f
            VibrateUtils.vibrate(1000)
            viewHolder.itemView.findViewById<View>(R.id.view_cover).visibility = View.VISIBLE
        }
        return makeMovementFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {

        // 得到item 原来的position
        val fromPosition = viewHolder.absoluteAdapterPosition
        val originItem = viewHolder.itemViewType
        // 得到目标 position
        val toPosition = target.absoluteAdapterPosition
        val itemViewType = target.itemViewType

        LogUtils.e(">>>> originItem = " + originItem)

        if (originItem != MediaListAdapter.ADAPTER_TYPE_ADD && itemViewType != MediaListAdapter.ADAPTER_TYPE_ADD) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(adapter.getData(), i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(adapter.getData(), i, i - 1)
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }
        return false
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        val itemViewType = viewHolder.itemViewType
        if (itemViewType == MediaListAdapter.ADAPTER_TYPE_IMAGE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }


    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//        viewHolder.itemView.alpha = 1.0f
        viewHolder.itemView.findViewById<View>(R.id.view_cover).visibility = View.GONE
        adapter.notifyDataSetChanged()
        super.clearView(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


    }


}