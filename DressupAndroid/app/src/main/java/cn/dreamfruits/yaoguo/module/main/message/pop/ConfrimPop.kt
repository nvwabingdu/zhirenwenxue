package cn.dreamfruits.yaoguo.module.main.message.pop

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import cn.dreamfruits.yaoguo.R
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener

/**
 * @author Lee
 * @createTime 2023-07-03 12 GMT+8
 * @desc :
 */
class ConfrimPop(
    var mContext: Context,
    var title: String = "",
    var content: String = "",
    var leftString: String = "",
    var rightString: String = "",
    var titleColor: Int = ColorUtils.string2Int("#333333"),
    var contentColor: Int = ColorUtils.string2Int("#333333"),
    var leftColor: Int = ColorUtils.string2Int("#222222"),
    var rightColor: Int = ColorUtils.string2Int("#ea4359"),
    var onLeftClick: OnCancelListener? = null,
    var onRightClick: OnConfirmListener? = null,
) :
    CenterPopupView(mContext) {


    lateinit var tv_title: TextView
    lateinit var tv_content: TextView
    lateinit var tv_left: TextView
    lateinit var tv_right: TextView
    override fun getImplLayoutId(): Int {
        return R.layout.layout_confim_pop
    }

    override fun initPopupContent() {
        super.initPopupContent()
        tv_title = findViewById(R.id.tv_title)
        tv_content = findViewById(R.id.tv_content)
        tv_left = findViewById(R.id.tv_left)
        tv_right = findViewById(R.id.tv_right)

        if (TextUtils.isEmpty(title)) {
            tv_title.visibility = View.GONE
        } else {
            tv_title.visibility = View.VISIBLE
            tv_title.text = title
        }

        tv_content.text = content


        tv_title.setTextColor(titleColor)
        tv_content.setTextColor(contentColor)
        tv_left.setTextColor(leftColor)
        tv_right.setTextColor(rightColor)

        if (!TextUtils.isEmpty(leftString)) {
            tv_left.text = leftString
        }
        if (!TextUtils.isEmpty(rightString)) {
            tv_right.text = rightString
        }

        tv_left.setOnClickListener {
            if (onLeftClick != null) {
                onLeftClick!!.onCancel()
            }
            dismiss()
        }

        tv_right.setOnClickListener {
            if (onRightClick != null) {
                onRightClick!!.onConfirm()
            }
            dismiss()
        }


    }

    override fun getMaxWidth(): Int {
        return (ScreenUtils.getScreenWidth() * 0.80).toInt()
    }
}