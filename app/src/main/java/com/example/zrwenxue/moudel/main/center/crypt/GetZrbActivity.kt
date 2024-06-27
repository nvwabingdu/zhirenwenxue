package com.example.zrwenxue.moudel.main.center.crypt

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newzr.R
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.BaseActivity

class GetZrbActivity : BaseActivity() {

    override fun layoutResId(): Int = R.layout.activity_get_zrb

    override fun init() {
        setTopView()
    }

    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView() {
        topView = findViewById(R.id.title_bar)
        topView!!.setTitle("获取智人币")
        //左边返回
        topView!!.setOnclickLeft(
            View.VISIBLE,
            View.OnClickListener { finish() })
        //右边弹出pop
        topView!!.setOnclickRight(View.INVISIBLE, View.OnClickListener { })
    }


}