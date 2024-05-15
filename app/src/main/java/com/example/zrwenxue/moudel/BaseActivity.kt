package com.example.zrwenxue.moudel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.example.zrframe.base.ActivityCollector
import com.example.zrtool.swipeback.BGASwipeBackHelper
import com.example.zrwenxue.MainActivity
import com.example.zrwenxue.moudel.login.LoginActivity

/**
 * Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), BGASwipeBackHelper.Delegate {
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        /**base功能1：实现沉浸式状态栏*/
        BarUtils.transparentStatusBar(this)
        BarUtils.transparentNavBar(this)
        BarUtils.setStatusBarLightMode(this, true)
        /**base功能4：实现左滑返回 需要在super.onCreate(savedInstanceState)之前调用该方法*/
        initSwipeBackFinish()
        super.onCreate(savedInstanceState)
        /**base功能0：Activity控制*/
        ActivityCollector.addActivity(this)//wq
        /**base功能0：实现自定义布局*/
        if (layoutResId() != 0) {
            setContentView(layoutResId())
        }
        /**base功能0：需要子类需要实现的方法 为了扩展性更好，目前仅添加一个方法*/
        init()
    }

    @LayoutRes
    protected abstract fun layoutResId(): Int
    protected abstract fun init()

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)//wq
    }

    /**
     * base功能2：实现强制下线
     */
    lateinit var receiver: BroadcastReceiver
    override fun onResume() {
        super.onResume()
        /**注册广播接收器*/
        val intentFilter = IntentFilter()
        intentFilter.addAction("FORCE_OFFLINE")
        receiver = ForceOffLineReceiver()
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        /**销毁广播*/
        unregisterReceiver(receiver)
    }

    inner class ForceOffLineReceiver : BroadcastReceiver() {
        /**
         * 收到广播之后强制下线
         */
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action) {
                "FORCE_OFFLINE" -> {
                    AlertDialog.Builder(context!!).apply {
                        setTitle("warning")
                        setMessage("您被强制下线，请重新登录")
                        setCancelable(false)
                        setPositiveButton("OK") { _, _ ->
                            ActivityCollector.finishAll()
                            val intent = Intent(context!!, LoginActivity::class.java)
                            context.startActivity(intent)
                        }
                        show()
                    }
                }
            }
        }
    }

    /**
     * base功能3：双击返回键退出
     * 双击back键返回
     */
    private var mIsExit = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (this.javaClass == MainActivity::class.java) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mIsExit) {
                    ActivityCollector.finishAll()
                } else {
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
                    mIsExit = true
                    Handler().postDelayed({ mIsExit = false }, 2000)
                }
                return true
            }
        } else {
            return super.onKeyDown(keyCode, event)
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * base功能4：初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private var mSwipeBackHelper: BGASwipeBackHelper? = null
    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper!!.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper!!.setIsOnlyTrackingLeftEdge(false)
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper!!.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper!!.setShadowResId(com.example.zrframe.R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper!!.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper!!.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper!!.setSwipeBackThreshold(0.4f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper!!.setIsNavigationBarOverlap(true)
    }

    override fun isSupportSwipeBack(): Boolean {
        //是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
        return true
    }

    override fun onSwipeBackLayoutSlide(slideOffset: Float) {
        //正在滑动返回 @param slideOffset 从 0 到 1
    }

    override fun onSwipeBackLayoutCancel() {
        //没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
    }

    override fun onSwipeBackLayoutExecuted() {
        //滑动返回执行完毕，销毁当前 Activity
        mSwipeBackHelper!!.swipeBackward()
    }

    override fun onBackPressed() {
        //正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper!!.isSliding) {
            return
        }
        mSwipeBackHelper!!.backward()
    }

    /**
     * base功能5：跳转到指定Activity
     */
    fun startActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }
}