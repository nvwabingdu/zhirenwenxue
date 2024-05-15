package com.example.zrlearn.a_android.fourcomponents.broadcastreceiver.custom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.zrframe.R

class TestBroadReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_broad_receiver)


        val intent= Intent("com.example.zrtool.learn.and.broadcastreceiver.custom.MY_BROADCASTRECEIVER")
        intent.setPackage(packageName)
        sendBroadcast(intent)//标准广播
//        sendOrderedBroadcast(intent,null)//有序广播  第二个参数表示与权限相关
    }



}