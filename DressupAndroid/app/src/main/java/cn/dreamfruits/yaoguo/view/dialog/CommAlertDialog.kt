package cn.dreamfruits.yaoguo.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.dialog.BaseFragmentDialog

import com.blankj.utilcode.util.ScreenUtils


/**
 * description: 通用的提示框
 *
 */
class CommAlertDialog : BaseFragmentDialog() {

    /**
     * 提示框的类型
     */
    var type = AlertDialogType.DOUBLE_BUTTON

    var content: String? = null

    /**
     * 默认 取消
     */
    var cancelText: String? = null

    /**
     * 默认 确定
     */
    var confirmText: String? = null
    var confirmTextColor: Int? = null
    var cancelCallback: (() -> Unit)? = null
    var confirmCallback: (() -> Unit)? = null

    private lateinit var mAlertContentTv: TextView
    private lateinit var mAlertCancelTv: TextView
    private lateinit var mAlertConfirmTv: TextView
    private lateinit var mSplitLine: View


    override fun setView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_comm_alert, container, false)

        initView(view)
        initType()
        return view
    }

    private fun initType(){

        when(type){
           AlertDialogType.DOUBLE_BUTTON ->{
               mAlertConfirmTv.isVisible = true
               mSplitLine.isVisible = true
           }
           AlertDialogType.SINGLE_BUTTON ->{
               mAlertConfirmTv.isVisible = false
               mSplitLine.isVisible = false
           }
        }
    }


    private fun initView(view: View) {
        mAlertContentTv = view.findViewById(R.id.tv_alert_content)
        mAlertCancelTv = view.findViewById(R.id.tv_alert_cancel)
        mAlertConfirmTv = view.findViewById(R.id.tv_alert_confirm)
        mSplitLine = view.findViewById(R.id.split_line)

        content?.let {
            mAlertContentTv.text = it
        }

        cancelText?.let {
            mAlertCancelTv.text = it
        }

        confirmText?.let {
            mAlertConfirmTv.text = it
        }

        cancelCallback?.let {callBack ->
            mAlertCancelTv.setOnClickListener {
                callBack.invoke()
                dismissAllowingStateLoss()
            }
        }

        confirmCallback?.let {callBack ->
            mAlertConfirmTv.setOnClickListener {
                callBack.invoke()
                dismissAllowingStateLoss()
            }
        }

    }


    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            (ScreenUtils.getScreenWidth() * 0.72f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG0", "onDestroy: ")
    }

    companion object {
        fun newInstance(): CommAlertDialog = CommAlertDialog()
    }
}

/**
 * 提示框类型
 */
enum class AlertDialogType{

    /**
     * 单个按钮的文本提示
     */
    SINGLE_BUTTON,


    /**
     * 取消、确认按钮的文本提示
     */
    DOUBLE_BUTTON

}



//  DSL style
inline fun commAlertDialog(
    fragmentManager: androidx.fragment.app.FragmentManager,
    tag: String = "CommAlertDialog",
    dsl: CommAlertDialog.() -> Unit
) {
    val dialog = CommAlertDialog.newInstance()
        .apply(dsl)
    dialog.mGravity = Gravity.CENTER
    dialog.mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    dialog.touchOutside = false
    dialog.show(fragmentManager, tag)
}
