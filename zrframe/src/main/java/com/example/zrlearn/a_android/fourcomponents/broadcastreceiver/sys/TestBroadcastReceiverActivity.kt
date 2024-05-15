package com.example.zrlearn.a_android.fourcomponents.broadcastreceiver.sys

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.zrframe.base.BaseActivity
import com.example.zrframe.constant.PageName
import com.example.zrframe.databinding.ActivityAboutBinding
import com.example.zrframe.module.about.AboutViewModel

class TestBroadcastReceiverActivity : BaseActivity<ActivityAboutBinding>() {

//    private val viewModel: AboutViewModel by viewModels()
    override val inflater: (inflater: LayoutInflater) -> ActivityAboutBinding
        get() = ActivityAboutBinding::inflate

    @PageName
    override fun getPageName() = PageName.ABOUT



    /**时间变化系统广播 动态注册方式*/
    lateinit var timeChangeReceiver: TimeChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentFilter=IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver=TimeChangeReceiver()
        registerReceiver(timeChangeReceiver,intentFilter)

    }

    inner class TimeChangeReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            //操作
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timeChangeReceiver)
    }

}