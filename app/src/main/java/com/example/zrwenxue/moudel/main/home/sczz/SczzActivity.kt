package com.example.zrwenxue.moudel.main.home.sczz

import android.view.View
import com.example.newzr.R
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.BaseActivity

class SczzActivity : BaseActivity()  {

    override fun layoutResId(): Int = R.layout.activity_memory

    override fun init() {
        setTopView()

        val fragment = SczzFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container_fragment, fragment).commit()
    }

    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView() {
        topView = findViewById(R.id.title_bar)
        topView!!.setTitle("中国诗词作者大全")
        //左边返回
        topView!!.setOnclickLeft(
            View.VISIBLE,
            View.OnClickListener { finish() })
        //右边弹出pop
        topView!!.setOnclickRight(View.INVISIBLE, View.OnClickListener { })
    }

    /**
     * 本页面不需要左滑返回
     */
    override fun isSupportSwipeBack(): Boolean {
        return false
    }
}