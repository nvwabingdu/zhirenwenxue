package com.example.zrlearn.a_android.fourcomponents.broadcastreceiver.custom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 标准广播
 */
class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        TODO("MyBroadcastReceiver.onReceive() is not implemented")
        //这里接收标准广播

        abortBroadcast()//表示截断广播  对应 sendOrderedBroadcast(intent,null)//有序广播  才能截断
    }
}