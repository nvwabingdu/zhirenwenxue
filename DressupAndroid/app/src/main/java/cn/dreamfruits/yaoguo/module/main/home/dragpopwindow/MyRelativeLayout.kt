package com.example.dragpopwindow

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

/**
 * 重写RelativeLayout的事件传递 设置动态拦截
 * @Author qiwangi
 * @Date 2023/5/4
 * @TIME 17:59
 */
class MyRelativeLayout : RelativeLayout {
    private var mIsIntercept = false    //是否拦截

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    //动态设置是否拦截
    fun setIntercept(isIntercept: Boolean) {
        this.mIsIntercept = isIntercept
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var intercepted = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = intercepted
            }
            MotionEvent.ACTION_MOVE -> {
                //方法传进来 如果为true就拦截事件
                intercepted = mIsIntercept
            }
            MotionEvent.ACTION_UP -> {
                //松开手再赋值为false
                mIsIntercept = false
                intercepted = false
            }
            else -> {
            }
        }
        return intercepted
    }
}