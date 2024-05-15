package com.example.zrtool.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager

/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 12:27
 */
object ScreenUtils {
    /**
     * 透明度
     */
    fun setBgAlpha(context: Activity, f: Float) { //透明函数
        val lp: WindowManager.LayoutParams = context.window.attributes
        lp.alpha = f
        context.window.attributes = lp
    }

    /**
     * 获取底部导航条高度
     */
     fun getNavigationBarHeight(mContext: Context): Int {
        if (!isNavigationBarShow(mContext)) {
            return 0
        }
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 底部导航是否显示
     */
    private fun isNavigationBarShow(mContext: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            return realSize.y !== size.y
        } else {
            val menu = ViewConfiguration.get(mContext).hasPermanentMenuKey()
            val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            return if (menu || back) false else true
        }
    }


}