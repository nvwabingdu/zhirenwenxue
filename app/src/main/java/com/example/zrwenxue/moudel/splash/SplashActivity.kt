package com.example.zrwenxue.moudel.splash

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.newzr.R
import com.example.zrwenxue.MainActivity
import com.example.zrwenxue.moudel.BaseActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun layoutResId(): Int = R.layout.activity_splash

    override fun init() {
        setDelayed()
    }

    /**
     * 延迟两秒做操作
     */
    val handler = android.os.Handler()
    private var sharedPreferences: SharedPreferences? = null

    private fun setDelayed(){
        //延迟2秒跳转到登录页面
        handler.postDelayed({
                /**
                 * 直接跳到主页
                 */
                MainActivity.startAc(this,"","")
        }, 2000)
    }


    /**
     * 本页面不需要左滑返回
     */
    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    /**
     * 本页面屏蔽返回键
     */
    override fun onBackPressed() {
        return
    }

}