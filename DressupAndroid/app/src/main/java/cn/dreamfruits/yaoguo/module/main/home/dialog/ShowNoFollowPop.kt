package cn.dreamfruits.yaoguo.module.main.home.dialog

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.lifeCycleSet

/**
 * @Author qiwangi
 * @Date 2023/7/13
 * @TIME 17:21
 */
class ShowNoFollowPop {

    fun showPop(mActivity: Activity) {
        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.home_care_confirm, null)
        var confirm = inflate.findViewById<TextView>(R.id.confirm)
        var cancel = inflate.findViewById<TextView>(R.id.cancel)

        val mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        //点击确定
        confirm.setOnClickListener {
            Log.e("qiwangi", "点击确定")
            mInterface?.onclick(1)
            mPopupWindow.dismiss()
        }

        //点击取消
        cancel.setOnClickListener {
            Log.e("qiwangi", "点击取消")
            mInterface?.onclick(0)
            mPopupWindow.dismiss()
        }

        //动画
        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.CENTER, 0, 0)
            Singleton.bgAlpha(mActivity, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                Singleton.bgAlpha(mActivity, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        lifeCycleSet(mActivity, mPopupWindow)
    }


    //回调
    interface InnerInterface {
        fun onclick(state: Int)//0表示取消  1表示确定
    }

    private var mInterface: InnerInterface? = null

    fun setShowNoFollowPopCallBack(mInterface: InnerInterface) {
        this.mInterface = mInterface
    }
}