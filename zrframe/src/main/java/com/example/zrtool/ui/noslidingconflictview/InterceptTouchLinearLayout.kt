package com.example.zrtool.ui.noslidingconflictview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * @Author qiwangi
 * @Date 2023/3/8
 * @TIME 09:14
 * 用于主页根布局 为了拦截触摸事件 配合内容反馈使用
 * 可拦截触摸事件LinearLayout
 */
class InterceptTouchLinearLayout : LinearLayout {

    var mOnInterceptTouchEventListener: OnInterceptTouchEventListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = mOnInterceptTouchEventListener?.onInterceptTouchEvent(ev) ?: false

    interface OnInterceptTouchEventListener {
        fun onInterceptTouchEvent(ev: MotionEvent?): Boolean
    }
}





