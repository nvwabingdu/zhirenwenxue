package com.example.zrwenxue.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.example.newzr.R
import kotlin.random.Random


/**
 * @Author qiwangi
 * @Date 2023/8/19
 * @TIME 09:51
 */
object Single {
    fun generateRandomColors(): Triple<String, String, String> {
        val colorSet = HashSet<Int>()
        val colors = arrayListOf<String>()

        fun getRandomColor(): String {
            val red = Random.nextInt(0, 256)
            val green = Random.nextInt(0, 256)
            val blue = Random.nextInt(0, 256)
            return "#%02X%02X%02X".format(red, green, blue)
        }

        fun getColorDist(color: String, existingColors: List<String>): Int {
            val colorInt = color.substring(1).toInt(16)
            var minDist = Int.MAX_VALUE
            for (existingColor in existingColors) {
                val existingColorInt = existingColor.substring(1).toInt(16)
                val dist = Math.abs(colorInt - existingColorInt)
                if (dist < minDist) {
                    minDist = dist
                }
            }
            return minDist
        }

        while (colors.size < 3) {
            val color = getRandomColor()
            val minDist = getColorDist(color, colors)
            if (!colorSet.contains(minDist) && minDist > 100000) {
                colors.add(color)
                colorSet.add(minDist)
            }
        }

        return Triple(colors[0], colors[1], colors[2])
    }



    @SuppressLint("MissingInflatedId")
    fun showHtml(mActivity: Activity, textContent:String){
        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_html, null)
        val webView = inflate.findViewById<WebView>(R.id.dialog_web_view)
        val v1 = inflate.findViewById<View>(R.id.v1)
        val v2 = inflate.findViewById<View>(R.id.v2)
        val l1 = inflate.findViewById<LinearLayout>(R.id.l1)

        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        v1.setOnClickListener { mPopupWindow.dismiss() }
        v2.setOnClickListener { mPopupWindow.dismiss() }
        l1.setOnClickListener { mPopupWindow.dismiss() }

        webView.loadDataWithBaseURL(null, textContent, "text/html", "UTF-8", null)
        // 设置 WebView 可滚动
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = false



        //动画
        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            bgAlpha(mActivity, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                bgAlpha(mActivity, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        lifeCycleSet(mActivity, mPopupWindow)
    }

    /**
     * 透明度
     */
    fun bgAlpha(context: Activity, f: Float) { //透明函数
        val lp: WindowManager.LayoutParams = context.window.attributes
        lp.alpha = f
        context.window.attributes = lp
    }

    /**
     * 获取app的生命周期，包括所有activity  找到现在使用的 并进行 防止内存泄露的操作  一般用于关闭pop
     */
    fun lifeCycleSet(mActivity: Activity, tempPop: PopupWindow) {
        (mActivity.application).registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {
                    if (mActivity != null && mActivity === activity) {//判断是否是依附的pop
                        if (tempPop != null && tempPop.isShowing) {//如果正在显示，关闭弹窗。
                            tempPop.dismiss()
                        }
                    }
                }

                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            })
    }


}