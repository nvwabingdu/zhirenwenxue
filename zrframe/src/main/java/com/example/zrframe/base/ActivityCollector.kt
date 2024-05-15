package com.example.zrframe.base

import android.app.Activity

/**
 * @Author qiwangi
 * @Date 2023/7/25
 * @TIME 11:27
 *
 * 退出app 直接调用 ActivityCollector.finishAll()//退出app
 *
 */
object ActivityCollector {
    private val activitys=ArrayList<Activity>()

    fun addActivity(activity: Activity){
        activitys.add(activity)
    }

    fun removeActivity(activity: Activity){
        activitys.remove(activity)
    }

    fun finishAll(){
        for (activity in activitys){
            if (!activity.isFinishing){
                activity.finish()
            }
        }
        activitys.clear()
       // android.os.Process.killProcess(android.os.Process.myPid())//杀掉进程
    }
}