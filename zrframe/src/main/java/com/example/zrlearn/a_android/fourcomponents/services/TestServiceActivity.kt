package com.example.zrlearn.a_android.fourcomponents.services

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.zrframe.R

class TestServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_service)

        /**
         * 绑定service
         * 标志位 BIND_AUTO_CREATE  表示activity和service绑定后会自动创建service 但不会走到onStartCommand()方法
         */
        bindService(Intent(this, MyService::class.java),connection, BIND_AUTO_CREATE)
    }

    /**
     * 和服务相关逻辑
     */
    lateinit var downloadBinder: MyService.DownloadBinder

    private val connection=object:ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //activity和service绑定时调用
            downloadBinder=service as MyService.DownloadBinder
            downloadBinder.startDownLoad()
            downloadBinder.getProgress()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            //进程崩溃 或者被杀掉的时候采用
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * 解绑服务
         */
        unbindService(connection)
    }

}