package com.example.zrtool.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.PopupWindow
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 11:46
 */
object ToolUtils {
    /**
     * 获取MD5加密  小写
     */
    private fun getMD5Hash(text: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(text.toByteArray())
            val digest = md.digest()
            val builder = StringBuilder()
            for (b in digest) {
                builder.append(String.format("%02x", b.toInt() and 0xff))
            }
            return builder.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
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

    //回调
    //    interface  InnerInterface{
    //        fun onclick(id: Long)
    //    }
    //    private var mInterface: InnerInterface? = null
    //
    //    fun setXXXCallBack(mInterface: InnerInterface){
    //        this.mInterface=mInterface
    //    }


//    回调   java
//    interface  InnerInterface{
//        void onclick(Long id);
//    }
//    private InnerInterface  mInterface= null;
//
//    public void  setXXXCallBack(InnerInterface mInterface){
//        this.mInterface=mInterface;
//    }


}