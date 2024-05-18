package cn.dreamfruits.yaoguo.view

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import cn.dreamfruits.yaoguo.R
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.impl.LoadingPopupView
import com.lxj.xpopup.util.XPopupUtils

/**
 * @author Lee
 * @createTime 2023-07-08 18 GMT+8
 * @desc :
 */
class LoadingPop(var mContext: Context) : CenterPopupView(mContext) {
    enum class Style {
        Spinner, ProgressBar
    }

    private var loadingStyle = Style.Spinner
    private var tv_title: TextView? = null
    private var progressBar: View? = null
    private var spinnerView: android.view.View? = null
    override fun getImplLayoutId(): Int = R.layout.layout_loading_pop
    override fun onCreate() {
        super.onCreate()
        tv_title = findViewById(R.id.tv_title)
        progressBar = findViewById(R.id.loadProgress)
        spinnerView = findViewById(R.id.loadview)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupImplView.elevation = 10f
        }
        if (bindLayoutId == 0) {
            popupImplView.background =
                XPopupUtils.createDrawable(Color.parseColor("#212121"), popupInfo.borderRadius)
        }
        setup()
    }
    private var firstShow = true

    override fun onShow() {
        super.onShow()
        firstShow = false
    }
    protected fun setup() {
        post {
            if (!firstShow) {
                val set = TransitionSet()
                    .setDuration(animationDuration.toLong())
                    .addTransition(Fade())
                    .addTransition(ChangeBounds())
                TransitionManager.beginDelayedTransition(centerPopupContainer, set)
            }
            if (title == null || title!!.length == 0) {
                XPopupUtils.setVisible(tv_title, false)
            } else {
                XPopupUtils.setVisible(tv_title, true)
                if (tv_title != null) tv_title!!.text = title
            }
            if (loadingStyle == Style.Spinner) {
                XPopupUtils.setVisible(progressBar, false)
                XPopupUtils.setVisible(spinnerView, true)
            } else {
                XPopupUtils.setVisible(progressBar, true)
                XPopupUtils.setVisible(spinnerView, false)
            }
        }
    }

    private var title: CharSequence? = null

    fun setTitle(title: CharSequence): LoadingPop {
        this.title = title
        setup()
        return this
    }

    fun setStyle(style: Style): LoadingPop {
        loadingStyle = style
        setup()
        return this
    }

}