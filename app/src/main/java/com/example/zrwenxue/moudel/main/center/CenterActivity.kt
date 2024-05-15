package com.example.zrwenxue.moudel.main.center

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.example.newzr.R
import com.example.zrwenxue.moudel.BaseActivity
import com.example.zrwenxue.moudel.main.center.tetris.TetrisListFragment

class CenterActivity : BaseActivity() {

    override fun layoutResId(): Int = R.layout.activity_center
//    override fun layoutResId(): Int =R.layout.da

    override fun init() {
        val fragment = TetrisListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container_fragment, fragment).commit()

        //中间图标点击退出
        findViewById<ImageView>(R.id.img_back).setOnClickListener {
            finish()
        }
    }



    /**
     * 需要跳转到本页面的时候调用此方法
     */
    companion object {
        fun startAc(mContent: Context, data1: String, data2: String) {
            //灵活运用apply
            val intent = Intent(mContent, CenterActivity::class.java).apply {
                putExtra("param1", data1)
                putExtra("param1", data2)
            }
            mContent.startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            var type = intent.getIntExtra("param1", -1)
            var position = intent.getIntExtra("param1", 0)
        }
    }


    /**
     * 保证页面下滑的动画的执行
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }

    /**
     * 本页面不需要左滑返回
     */
    override fun isSupportSwipeBack(): Boolean {
        return false
    }
}