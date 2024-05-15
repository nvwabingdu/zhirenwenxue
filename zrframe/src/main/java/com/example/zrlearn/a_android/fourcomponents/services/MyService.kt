package com.example.zrlearn.a_android.fourcomponents.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.zrframe.R

/**
 * 启动服务
 * startService(Intent(this,MyService::java.class))
 *
 * bindService(xxx)
 *
 * 停止服务
 * stopService(Intent(this,MyService::java.class))
 *
 * unbindService
 *
 * 自我停止
 * stopSelf()
 *
 * 注意点
 * 1.如果同时调用startService(xxx) bindService(xxx)  则需要销毁服务 需要在同时调用两种取消方法
 * 2.后台服务可能被随时回收
 *
 * 服务分类：
 * 1.后台服务  即本类
 * 2.前台服务  通知栏会一直有个图标显示   只需要修改部分代码
 *
 *
 *
 */
class MyService : Service() {

    private val mBinder= DownloadBinder()

    class DownloadBinder:Binder(){
        fun startDownLoad(){
            Log.e("MyService","startDownload executed")
        }
        fun getProgress():Int{
            Log.e("MyService","getProgress executed")
            return 0
        }

    }

    /**
     * 服务自带的方法 用于绑定给一个binder
     */
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }


    /**
     * 创建服务时
     */
    override fun onCreate() {
        super.onCreate()

        //前台服务逻辑
        val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel=NotificationChannel("my_service","前台Service通知",NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent=Intent(this, TestServiceActivity::class.java)
        val pi=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification=NotificationCompat.Builder(this,"my_service")
            .setContentTitle("前台服务标题")
            .setContentText("前台服务的一些内容")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_launcher_foreground))
            .setContentIntent(pi)
            .build()
        startForeground(1,notification)
        //必须在清单文件中声明前台服务   <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>


    }

    /**
     * 启动服务时调用
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread{
            //处理具体的逻辑

            //处理完毕之后
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)

//        stopSelf()//自我停止
    }

    /**
     * 销毁时
     */
    override fun onDestroy() {
        super.onDestroy()
    }
}