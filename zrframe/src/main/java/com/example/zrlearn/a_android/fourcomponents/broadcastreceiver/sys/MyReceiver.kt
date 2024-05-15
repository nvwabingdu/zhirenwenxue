package com.example.zrlearn.a_android.fourcomponents.broadcastreceiver.sys

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 静态 系统广播
 */
class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        TODO("MyReceiver.onReceive() is not implemented")
        //系统广播 这里表示开机之后收到消息 并操作

    }
}