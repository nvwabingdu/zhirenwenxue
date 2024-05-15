package com.example.zrtool.ui.noslidingconflictview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

/**
 * @Author qiwangi
 * @Date 2023/5/8
 * @TIME 17:39
 * 可拦截触摸事件RelativeLayout
 */
class InterceptTouchRelativeLayout : RelativeLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = mOnInterceptTouchEventListener?.onInterceptTouchEvent(ev) ?: false

    private var mOnInterceptTouchEventListener: OnInterceptTouchEventListener? = null

    interface OnInterceptTouchEventListener {
        fun onInterceptTouchEvent(ev: MotionEvent?): Boolean
    }

    fun onInterceptTouchEvent(onInterceptTouchEventListener: OnInterceptTouchEventListener) {
        this.mOnInterceptTouchEventListener=onInterceptTouchEventListener
    }
}

