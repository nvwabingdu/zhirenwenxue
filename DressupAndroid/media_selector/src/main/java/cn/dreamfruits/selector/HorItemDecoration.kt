package cn.dreamfruits.selector

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils

/**
 * 水平decoration
 */
class HorItemDecoration(private val horizontalItemSpacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.apply {
            left = SizeUtils.dp2px(horizontalItemSpacing.toFloat() / 2)
            right = SizeUtils.dp2px(horizontalItemSpacing.toFloat() / 2)
        }
    }
}