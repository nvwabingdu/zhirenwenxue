package cn.dreamfruits.yaoguo.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.StatusBarUtil
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.view.swipeback.BGASwipeBackHelper
import com.gyf.immersionbar.ktx.immersionBar


abstract class BaseActivity : AppCompatActivity(), BGASwipeBackHelper.Delegate {
    lateinit var mContext: Context

    protected var mSwipeBackHelper: BGASwipeBackHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//透明状态栏
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN)// 全屏展示


        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        initSwipeBackFinish()

        super.onCreate(savedInstanceState)
        mContext = this
        this.onCreateBefore()

        //  this.initStatus()
        if (layoutResId() != 0) {
            setContentView(layoutResId())
        }
        this.initStatus()
        initView()
        initData()
    }



    /** 这里可以做一些setContentView之前的操作,如全屏、常亮、设置Navigation颜色、状态栏颜色等  */
    protected open fun onCreateBefore() {
        StatusBarUtil.setWhiteStatusBar(this)//状态栏白底黑字
    }

    /**
     * 可见状态判断是否有网络
     */
    override fun onResume() {
        super.onResume()

        popupHandler.sendEmptyMessageDelayed(0, 1000)//是否有网络
    }


    @SuppressLint("HandlerLeak")
    private val popupHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> isNetConnected()//网络判断
                else -> {}
            }
        }
    }

    /**
     * 判断是否有网络
     */
    fun isNetConnected() {
        Singleton.isNetConnectedToast(this)
    }


    /**
     * 布局资源
     */
    @LayoutRes
    protected abstract fun layoutResId(): Int


    /**
     * 适配状态栏
     */
    protected open fun initStatus() {
        val params: WindowManager.LayoutParams = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.attributes = params


        immersionBar {
            fitsSystemWindows(false)
            statusBarDarkFont(true)
        }


//
//
//        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
//            val window: Window = window
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = Color.TRANSPARENT
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //19表示4.4
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            //虚拟键盘也透明
//            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
    }

    /**
     * 初始化view
     */
    protected abstract fun initView()


    /**
     * 初始化数据相关
     */
    protected abstract fun initData()


    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private  fun initSwipeBackFinish() {
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
        mSwipeBackHelper!!.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper!!.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper!!.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper!!.setSwipeBackThreshold(0.4f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper!!.setIsNavigationBarOverlap(false)
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    override fun onSwipeBackLayoutSlide(slideOffset: Float) {}

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    override fun onSwipeBackLayoutCancel() {}

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper!!.swipeBackward()
    }

    override fun onBackPressed() {

        // 正在滑动返回的时候取消返回按钮事件

        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper!!.isSliding) {
            return
        }
        mSwipeBackHelper!!.backward()
    }


}