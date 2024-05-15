package com.example.zrtool.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.zrframe.R

/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 11:22
 */
object ToastUtils {
    var lastRunTime = 0L // 上次运行时间
    val lock = Any() // 锁对象
    /**
     * 中央吐司
     */
    fun showCenterToast(context: Context,msg:String) {
        synchronized(lock) { // 加锁避免并发问题
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastRunTime >= 3000) { // 时间间隔超过3秒，可以运行方法
                lastRunTime = currentTime
                try {
                    /**吐司*/
                    val toast = Toast(context)
                    val mView: View = LayoutInflater.from(context).inflate(R.layout.logutils_dialog_center_tv, null)
                    val tv=mView.findViewById<View>(R.id.center_toast_text) as android.widget.TextView
                    tv.text=msg
                    toast.setView(mView)
                    toast.duration = Toast.LENGTH_SHORT
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 通用吐司
     */
    fun showCommonToast(context: Context,msg:String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}