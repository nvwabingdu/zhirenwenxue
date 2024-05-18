package com.example.dragpopwindow

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.module.main.home.MaxRecyclerView

@SuppressLint("CheckResult")
class CustomDragPop : PopupWindow {
    private var mActivity: Activity
    private var mPointPosition = Point()                             //手指按下坐标
    private var record = arrayOf(0, 0)                               //存放手指按下坐标和时间戳
    private var defaultTop = 0                                       //弹框原始距离顶部位置

    //构造方法
    @SuppressLint("ClickableViewAccessibility")
    constructor(activity: Activity, inflate:View, mWidth:Int, mHeight:Int, popReLayout: RelativeLayout,
                popMyRelativeLayout:MyRelativeLayout, popRecyclerView:RecyclerView,) : super(activity) {

        this.mActivity = activity
        this.contentView = inflate//设置dragPopupWindow的View
        this.width = mWidth//设置dragPopupWindow弹出窗体的宽
        this.height = mHeight//设置dragPopupWindow弹出窗体的高
        this.setBackgroundDrawable(ColorDrawable(-0x00000000))//设置dragPopupWindow弹出窗体的背景

        //顶部手势监听
        popMyRelativeLayout.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //每次按下时记录坐标
                    mPointPosition.y = event.rawY.toInt()
                    record[0] = event.rawY.toInt()
                    record[1] = System.currentTimeMillis().toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    //每次重绘都会根据上一次最后触屏的mPointPosition.y坐标算出新移动的值
                    val dy = event.rawY.toInt() - mPointPosition.y
                    //变化中的顶部距离
                    val top = v.top + dy
                    //获取到layoutParams后改变属性 在设置回去
                    val layoutParams = v.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.topMargin = top
                    v.layoutParams = layoutParams
                    //记录最后一次移动的位置
                    mPointPosition.y = event.rawY.toInt()
                }
                MotionEvent.ACTION_UP -> {
                    //先根据时间算 如果在0.5秒内的话且向下拉的话 就直接销毁弹框
                    if (System.currentTimeMillis().toInt() - record[1] < 500 && event.rawY.toInt() > record[0]) {
                        dismiss()
                    } else {    //然后再根据移动距离判断是否销毁弹框
                        //下移超过200就销毁 否则弹回去
                        if (event.rawY.toInt() - record[0] > 300) {
                            dismiss()
                        } else {
                            //获取到layoutParams后改变属性 在设置回去
                            val layoutParams = v.layoutParams as RelativeLayout.LayoutParams
                            layoutParams.topMargin = defaultTop
                            v.layoutParams = layoutParams
                        }
                    }
                    popMyRelativeLayout.setIntercept(false)
                }
            }
            //刷新界面
            popReLayout.invalidate()
            true
        }

        //RecyclerView监听
        popRecyclerView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //每次按下时记录坐标
                    mPointPosition.y = event.rawY.toInt()
                    record[0] = event.rawY.toInt()
                    record[1] = System.currentTimeMillis().toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    //下拉
                    if (event.rawY <= record[0]) {
                        popMyRelativeLayout.setIntercept(false)
                    } else {
                        //滑到顶部了
                        if (!v.canScrollVertically(-1)) {
                            popMyRelativeLayout.setIntercept(true)
                        }
                    }
                }
            }
            false
        }

        /*inflate添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框*/
        inflate.setOnTouchListener { _, event ->
            val height = popMyRelativeLayout.top
            val y = event.y
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss()
                }
            }
            true
        }
    }


    //构造方法
    @SuppressLint("ClickableViewAccessibility")
    constructor(activity: Activity, inflate:View, mWidth:Int, mHeight:Int, popReLayout: RelativeLayout,
                popMyRelativeLayout:MyRelativeLayout, popRecyclerView:MaxRecyclerView,) : super(activity) {

        this.mActivity = activity
        this.contentView = inflate//设置dragPopupWindow的View
        this.width = mWidth//设置dragPopupWindow弹出窗体的宽
        this.height = mHeight//设置dragPopupWindow弹出窗体的高
        this.setBackgroundDrawable(ColorDrawable(-0x00000000))//设置dragPopupWindow弹出窗体的背景

        //顶部手势监听
        popMyRelativeLayout.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //每次按下时记录坐标
                    mPointPosition.y = event.rawY.toInt()
                    record[0] = event.rawY.toInt()
                    record[1] = System.currentTimeMillis().toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    //每次重绘都会根据上一次最后触屏的mPointPosition.y坐标算出新移动的值
                    val dy = event.rawY.toInt() - mPointPosition.y
                    //变化中的顶部距离
                    val top = v.top + dy
                    //获取到layoutParams后改变属性 在设置回去
                    val layoutParams = v.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.topMargin = top
                    v.layoutParams = layoutParams
                    //记录最后一次移动的位置
                    mPointPosition.y = event.rawY.toInt()
                }
                MotionEvent.ACTION_UP -> {
                    //先根据时间算 如果在0.5秒内的话且向下拉的话 就直接销毁弹框
                    if (System.currentTimeMillis().toInt() - record[1] < 500 && event.rawY.toInt() > record[0]) {
                        dismiss()
                    } else {    //然后再根据移动距离判断是否销毁弹框
                        //下移超过200就销毁 否则弹回去
                        if (event.rawY.toInt() - record[0] > 300) {
                            dismiss()
                        } else {
                            //获取到layoutParams后改变属性 在设置回去
                            val layoutParams = v.layoutParams as RelativeLayout.LayoutParams
                            layoutParams.topMargin = defaultTop
                            v.layoutParams = layoutParams
                        }
                    }
                    popMyRelativeLayout.setIntercept(false)
                }
            }
            //刷新界面
            popReLayout.invalidate()
            true
        }

        //RecyclerView监听
        popRecyclerView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //每次按下时记录坐标
                    mPointPosition.y = event.rawY.toInt()
                    record[0] = event.rawY.toInt()
                    record[1] = System.currentTimeMillis().toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    //下拉
                    if (event.rawY <= record[0]) {
                        popMyRelativeLayout.setIntercept(false)
                    } else {
                        //滑到顶部了
                        if (!v.canScrollVertically(-1)) {
                            popMyRelativeLayout.setIntercept(true)
                        }
                    }
                }
            }
            false
        }

        /*inflate添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框*/
        inflate.setOnTouchListener { _, event ->
            val height = popMyRelativeLayout.top
            val y = event.y
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss()
                }
            }
            true
        }
    }

}