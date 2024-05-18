package cn.dreamfruits.yaoguo.module.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.os.Build
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.home.*
import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.CommentFragment
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.diy.DiyActivity
import cn.dreamfruits.yaoguo.module.main.diy.mine.DiyMineFragment
import cn.dreamfruits.yaoguo.module.main.message.MessageFragment
import cn.dreamfruits.yaoguo.module.main.mine.MineFragment
import cn.dreamfruits.yaoguo.module.main.stroll.StrollFragment
import com.gyf.immersionbar.ktx.immersionBar
import com.nirvana.tools.core.AppUtils


@RequiresApi(Build.VERSION_CODES.O)
open class MainActivity : BaseActivity(), OnClickListener {

    override fun layoutResId(): Int = R.layout.home_activity_temp_main

    private var mActivity: Activity? = null//仅用于评论回复时，获取当前activity
    private var mLifecycleOwner: LifecycleOwner? = null

    companion object {
        @JvmStatic
        fun start(context: Context, type: Int, position: Int) {
            val starter = Intent(context, MainActivity::class.java)
                .putExtra("position", position)
                .putExtra("type", type)
            context.startActivity(starter)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent != null) {
            var type = intent.getIntExtra("type", -1)
            var position = intent.getIntExtra("position", 0)

            if (type == 1) {
                if (position == 0) {
                    initBottomLayout()
                    turnHome()
                    homefragment!!.turnToPosition(0)
                }
            }
        }
    }


    override fun initView() {
//        Log.e("MainActivity",com.blankj.utilcode.util.AppUtils.getAppSignaturesSHA256("cn.dreamfruits.yaoguo").toString())
        mActivity = this//赋值
        mLifecycleOwner = this//赋值
        //设置监听
        initTabView()
        //设置pager
        initPager()
        //防止键盘冲突
        initTabbarListener()
    }

//    /**
//     * 重写startActivity方法，取消切换动画
//     */
//    override fun startActivity(intent: Intent?) {
//        super.startActivity(intent)
//        overridePendingTransition(0, 0)
//    }
//
//    /**
//     * 重写startActivityForResult方法，取消切换动画
//     */
//    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
//        super.startActivityForResult(intent, requestCode)
//        overridePendingTransition(0, 0)
//    }


    override fun initData() {
        setBroadcast()//注册广播
    }

    override fun onCreateBefore() {

    }

    override fun initStatus() {
        val params: WindowManager.LayoutParams = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.attributes = params
        immersionBar {
            fitsSystemWindows(false)
            statusBarDarkFont(true)
        }
    }


    lateinit var viewPager: NoScrollViewPager
    val fragments: ArrayList<Fragment> = ArrayList()
    var homefragment: HomeFragment2? = null
    private fun initPager() {
        viewPager = findViewById<NoScrollViewPager>(R.id.viewPager)
//        val fragments: ArrayList<Fragment> = ArrayList()
        //添加Fragment页面
        homefragment = HomeFragment2()
        fragments.add(homefragment!!)
        fragments.add(StrollFragment())
//        fragments.add(DiyMineFragment())
        fragments.add(MessageFragment())
        fragments.add(MineFragment())

        val fragmentPagerAdapter = MyFragmentAdapter(this.supportFragmentManager, fragments)
        viewPager.adapter = fragmentPagerAdapter
        viewPager.offscreenPageLimit = 4//预加载写死 避免切换的时候，状态改变
        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                //监听用户滑动，这里面的changeTab是自定义函数为了将滑动和点击tabbar的视觉效果统一
                changeTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    /**
     * 适配器
     */
    class MyFragmentAdapter(fm: FragmentManager, var fragments: ArrayList<Fragment>) :
        FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]

        }
    }

    lateinit var tabHome: TextView
    lateinit var tabStroll: TextView
    lateinit var tabIssue: View
    lateinit var tabIssueimg: ImageView
    lateinit var tabMessage: TextView
    lateinit var tabMine: TextView
    private fun initTabView() {
        //tab_home 是为了获取整个图标和文字部分iv_home 获取的是图标，目的是将图标设计成点亮态和未访问态。
        tabHome = findViewById<TextView>(R.id.bottom_nav_textview_home)
        tabHome.setOnClickListener(this)

        // TODO:
        tabHome.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))

        tabStroll = findViewById<TextView>(R.id.bottom_nav_textview_stroll)
        tabStroll.setOnClickListener(this)

        tabIssue = findViewById<View>(R.id.bottom_nav_view_issue)
        tabIssue.setOnClickListener(this)

        tabIssueimg = findViewById<ImageView>(R.id.bottom_nav_view_issue_img)
        tabIssueimg.setOnClickListener(this)

        tabMessage = findViewById<TextView>(R.id.bottom_nav_textview_message)
        tabMessage.setOnClickListener(this)

        tabMine = findViewById<TextView>(R.id.bottom_nav_textview_mine)
        tabMine.setOnClickListener(this)
    }

    open fun turnHome() {
        tabHome.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
        viewPager.currentItem = 0
    }

    /**
     * 点击切换
     */
    override fun onClick(view: View) {
        //初始化
        if (view.id != R.id.bottom_nav_view_issue_img)
            initBottomLayout()
        when (view.id) {
            R.id.bottom_nav_textview_home -> {
                turnHome()
            }
            R.id.bottom_nav_textview_stroll -> {
                tabStroll.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                viewPager.currentItem = 1
//                Singleton.showStrollPop(this)//逛逛弹窗
            }
            R.id.bottom_nav_view_issue_img -> {
//                tabIssue.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                //viewPager.currentItem = 2
                val intent = Intent(this, DiyActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay)

//                homefragment!!.turnToPosition(0)

            }
            R.id.bottom_nav_textview_message -> {
                tabMessage.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                viewPager.currentItem = 2
            }
            R.id.bottom_nav_textview_mine -> {
                tabMine.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                viewPager.currentItem = 3
            }
        }
    }

    /**
     * 改变tab
     */
    private fun changeTab(position: Int) {
        //初始化
        when (position) {
            0 -> {
                tabHome.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                viewPager.currentItem = 0
            }
            1 -> {
                tabStroll.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                viewPager.currentItem = 1
            }

            2 -> {
                tabMessage.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                viewPager.currentItem = 2
            }
            3 -> {
                tabMine.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                viewPager.currentItem = 3
            }
        }
    }

    /**
     * 初始化底部
     */
    @SuppressLint("ResourceAsColor")
    fun initBottomLayout() {
        tabHome.setTextColor(R.color.bottom_nav_textview_color)
        tabStroll.setTextColor(R.color.bottom_nav_textview_color)
        //中间暂时没有效果
        tabMessage.setTextColor(R.color.bottom_nav_textview_color)
        tabMine.setTextColor(R.color.bottom_nav_textview_color)
    }

    /**
     * 防止键盘和底部导航冲突
     */
    private fun initTabbarListener() {
        val softKeyboardStateHelper =
            SoftKeyboardStateHelper(findViewById<View>(R.id.activity_main_root_layout))
        softKeyboardStateHelper.addSoftKeyboardStateListener(object :
            SoftKeyboardStateHelper.SoftKeyboardStateListener {
            override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
                //键盘打开
                //Log.d("nightowl", "键盘打开");
                //将tabbar设置成不可见
                findViewById<View>(R.id.bottom_nav_root_layout).visibility = View.INVISIBLE
            }

            override fun onSoftKeyboardClosed() {
                //键盘关闭
                // Log.d("nightowl", "键盘关闭");
                findViewById<View>(R.id.bottom_nav_root_layout).visibility = View.VISIBLE
            }
        })
    }

    /**
     * 显示隐藏底部评论布局
     */
    private var bottomComment: View? = null
    fun setBottomCommentVisibility(isShow: Boolean) {
        if (bottomComment == null) {
            bottomComment = findViewById(R.id.comment_bottom)
        }
        if (isShow) {//显示
            val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_bottom_in)
            bottomComment!!.startAnimation(anim)
            bottomComment!!.visibility = View.VISIBLE
        } else {//隐藏
            val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_bottom_out)
            bottomComment!!.startAnimation(anim)
            bottomComment!!.visibility = View.GONE
        }
    }


    /**
     *  ==================================广播==================================
     */
    private var myBroadcast = MyBroadcast()    //注册广播用于接收是否弹出评论弹窗
    private fun setBroadcast() {
        //发送广播 在需要点击的地方
//        val intent = Intent("com.skypan.update")
//        intent.putExtra("name", "我是广播")
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

        //广播接收注册
        val intentFilter = IntentFilter()//创建意图过滤器 筛选我需要接收哪些广播
        intentFilter.addAction("care_comment_pop")//接收广播的名字
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcast, intentFilter)//开始接收广播

        myBroadcast.setMyBroadcastInterface(object : MyBroadcast.MyBroadcastInterface {

            override fun getMyBroadcastAction(feedId: Long) {
                setBottomCommentVisibility(true)
                //点击后回调在这里  feedId是帖子id 用于请求评论列表 并弹出评论弹窗
                val fragment = CommentFragment(mActivity!!, mLifecycleOwner!!, feedId, 0L, false, "", "")
                supportFragmentManager.beginTransaction()
                    .add(R.id.comment_fragment_container, fragment).commit()
            }
        })
    }

    /**
     * 广播接收器
     */
    class MyBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                "care_comment_pop" -> {
                    Log.e("TAG21", "onReceive: 接收到广播")
                    val feedId = intent.getLongExtra("feedId", 0) // 处理接收到的消息
                    myBroadcastInterface!!.getMyBroadcastAction(feedId)
                }
            }
        }

        //定义一个接口 通过接口实现回调
        interface MyBroadcastInterface {
            fun getMyBroadcastAction(feedId: Long)
        }

        private var myBroadcastInterface: MyBroadcastInterface? = null
        fun setMyBroadcastInterface(myBroadcastInterface: MyBroadcastInterface) {
            this.myBroadcastInterface = myBroadcastInterface
        }
    }

    /**
     * 销毁广播
     */
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcast)
    }
}

