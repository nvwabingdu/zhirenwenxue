package cn.dreamfruits.yaoguo.view.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.util.SizeUtil

/**
 * @date: 2021/4/5
 */
class HItemDecoration(private val horizontalItemSpacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.apply {
            left = SizeUtil.dp2px(horizontalItemSpacing.toFloat() / 2)
            right = SizeUtil.dp2px(horizontalItemSpacing.toFloat() / 2)
        }
    }
}