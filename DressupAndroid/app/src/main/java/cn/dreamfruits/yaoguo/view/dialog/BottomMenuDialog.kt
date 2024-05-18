package cn.dreamfruits.yaoguo.view.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.dialog.BaseFragmentDialog
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils

/**
 * 底部菜单Dialog
 */
class BottomMenuDialog : BaseFragmentDialog() {

    var menuList: List<String>? = null
    var menuItemClick: ((position: Int) -> Unit)? = null
    var cancelClick: (() -> Unit)? = null
    var cancelText: String? = null
    var cancelTextColor: Int? = null

    private lateinit var mMenuCancelTv: TextView
    private lateinit var mMenuLl: LinearLayout

    override fun setView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_bottom_menu, container, false)
        mMenuCancelTv = view.findViewById(R.id.dialog_bottom_menu_cancel)
        mMenuLl = view.findViewById(R.id.dialog_bottom_menu_ll)
        initCancel(mMenuCancelTv)
        addMenuItem(mMenuLl)
        return view
    }

    private fun initCancel(textView: TextView) {
        textView.text = cancelText ?: "取消"
        textView.setTextColor(cancelTextColor ?: resources.getColor(R.color.black_222222))
        textView.setOnClickListener {
            cancelClick?.invoke()
            dismissAllowingStateLoss()
        }
    }

    private fun addMenuItem(container: LinearLayout) {
        menuList?.also {
            val layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    SizeUtils.dp2px(50f)
                )

            val lineLayoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)

            it.forEachIndexed { index, s ->
                val textView: TextView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_bottom_menu_item, null, false) as TextView

                textView.text = s
                if (s == "删除") {
                    textView.setTextColor(Color.parseColor("#EA4359"))
                }

                textView.setOnClickListener {
                    menuItemClick?.invoke(index)
                    dismissAllowingStateLoss()
                }
                val line = View(context)
                line.setBackgroundResource(R.color.gray_E6E6E6)
                container.addView(textView, layoutParams)
                container.addView(line, lineLayoutParams)

            }

        }

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            (ScreenUtils.getScreenWidth() * 0.90f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        fun newInstance(): BottomMenuDialog = BottomMenuDialog()
    }
}

//  DSL style
fun bottomMenu(
    fragmentManager: androidx.fragment.app.FragmentManager,
    tag: String = "BottomMenuDialog",
    dsl: BottomMenuDialog.() -> Unit
): BottomMenuDialog {

    val dialog = BottomMenuDialog.newInstance().apply(dsl)
    dialog.mGravity = Gravity.BOTTOM
    dialog.mAnimation = R.style.BottomDialogAnimation
    dialog.mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    dialog.mOffsetY = 30
    dialog.show(fragmentManager, tag)
    return dialog
}
