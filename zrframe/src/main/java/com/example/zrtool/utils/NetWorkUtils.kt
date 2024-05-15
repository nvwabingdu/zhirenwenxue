package com.example.zrtool.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 11:33
 */
object NetWorkUtils {

    //注意工具类中使用静态方法
    fun isNetworkConnected(context: Context?): Boolean {
        //判断上下文是不是空的
        if (context != null) {
            //获取连接管理器
            val mConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //获取网络状态mConnectivityManager.getActiveNetworkInfo();
            val mNnetNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNnetNetworkInfo != null) {
                //判断网络是否可用//如果可以用就是 true  不可用就是false
                return mNnetNetworkInfo.isAvailable
            }
        }
        return false
    }

}