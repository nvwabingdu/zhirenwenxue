package com.example.zrwenxue

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.newzr.R
import com.example.zrtool.ui.noslidingconflictview.NoScrollViewPager
import com.example.zrtool.ui.uibase.BaseLinearLayout
import com.example.zrtool.ui.uibase.BaseRelativeLayout
import com.example.zrtool.ui.uibase.BaseTextView
import com.example.zrtool.utilsjava.SoftKeyboardStateHelper
import com.example.zrwenxue.moudel.BaseActivity
import com.example.zrwenxue.moudel.main.center.CenterActivity
import com.example.zrwenxue.moudel.main.home.HomeFragment
import com.example.zrwenxue.moudel.main.memory.MemoryFragment
import com.example.zrwenxue.moudel.main.word.WordFragment
import com.example.zrwenxue.moudel.main.mine.MineFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class MainActivity : BaseActivity(), View.OnClickListener {
    override fun layoutResId(): Int = R.layout.activity_main

    override fun init() {
        bottomNavLogic()
        initBottomNavListener()

        //初始化单例集合


        //初始化单例集合
//        try {
//            WordFromShardToSingleton.getWordBeanList(this)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }


    private lateinit var handler: Handler
    override fun onResume() {
        super.onResume()
        setNavSelected(2)
//        handler = Handler(Looper.getMainLooper())
//        val timer = Timer()
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                // 发送消息到主线程
//                handler.post {
//                //强制下线广播
//                    val intent = Intent("com.example.zrword.moudel.FORCE_OFFLINE")
//                    sendBroadcast(intent)
//                }
//            }
//        }, 1000)


//        //通知基础知识
//        val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
////            val channel=NotificationChannel("normal","Normal",NotificationManager.IMPORTANCE_DEFAULT)
//            val channel=NotificationChannel("important","Important", NotificationManager.IMPORTANCE_HIGH)//高等级通知
//            manager.createNotificationChannel(channel)
//        }
//
//        findViewById<TextView>(R.id.tv).setOnClickListener {
//            Toast.makeText(this,"",Toast.LENGTH_LONG).show()
//            val intent=Intent(this,LoginActivity::class.java)
//            val pi=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//            val notification=NotificationCompat.Builder(this,"important")
//                .setContentTitle("标题")
//                .setContentText("内容")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_launcher_foreground))
//                .setContentIntent(pi)
//                .setAutoCancel(true)//1.自动取消
//                .build()
//
//            manager.notify(1,notification)
//        }

        //2手动取消
//        val manager2=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager2.cancel(1)
    }


    /**
     * 项目中常用写法
     */
    fun coroutineScope() {
        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            //处理具体的逻辑
        }


        //
    }

    fun main() {
        /**
         * 协程如何返回结果
         */
        runBlocking {
            val result = async {
                5 + 5
            }

            print("${result.await()}")//await表示等到有结果  在使用的时候调用方法
        }


        /**
         * 一般用这种方式
         * 1.Dispatchers.Default  默认低并发
         * 1.Dispatchers.IO       高并发
         * 1.Dispatchers.Main     安卓中使用 主线程中运行
         */
        //类似的without
        runBlocking {
            val result = withContext(Dispatchers.Default) {
                5 + 2
            }

            print(result)
        }
    }

    /**
     * 底部导航栏逻辑 手写仿tab布局+viewpager联动 更好控制和扩展
     */
    private var acMainViewPager: NoScrollViewPager? = null
    private var acMainLinearLayout: BaseLinearLayout? = null
    private var acMainTvOne: BaseTextView? = null
    private var acMainTvTwo: BaseTextView? = null
    private var acMainRelativeLayoutCenter: BaseRelativeLayout? = null
    private var acMainIvCenter: View? = null
    private var acMainTvThree: BaseTextView? = null
    private var acMainTvFour: BaseTextView? = null

    private var homeFragment: HomeFragment? = null
    private val fragmentList = ArrayList<Fragment>()

    private fun bottomNavLogic() {
        //1.初始化控件
        acMainViewPager = findViewById(R.id.ac_main_viewPager)
        acMainLinearLayout = findViewById(R.id.ac_main_linearLayout)
        acMainTvOne = findViewById(R.id.ac_main_tv_one)
        acMainTvTwo = findViewById(R.id.ac_main_tv_two)
        acMainRelativeLayoutCenter = findViewById(R.id.ac_main_relativeLayout_center)
        acMainIvCenter = findViewById(R.id.ac_main_iv_center)
        acMainTvThree = findViewById(R.id.ac_main_tv_three)
        acMainTvFour = findViewById(R.id.ac_main_tv_four)

        acMainTvOne!!.setOnClickListener(this)
        acMainTvTwo!!.setOnClickListener(this)
        acMainRelativeLayoutCenter!!.setOnClickListener(this)
        acMainTvThree!!.setOnClickListener(this)
        acMainTvFour!!.setOnClickListener(this)


        //2.添加Fragment页面
        homeFragment = HomeFragment()
        fragmentList.add(homeFragment!!)
        fragmentList.add(MemoryFragment())
        //中间或还有一个
        fragmentList.add(WordFragment())
        fragmentList.add(MineFragment())

        //3.设置适配器
        val fragmentPagerAdapter = MyFragmentAdapter(this.supportFragmentManager, fragmentList)
        acMainViewPager!!.adapter = fragmentPagerAdapter
        acMainViewPager!!.offscreenPageLimit = 4//预加载写死 避免切换的时候，状态改变
        acMainViewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                //监听用户滑动，这里面的changeTab是自定义函数为了将滑动和点击tabbar的视觉效果统一
                setNavSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    class MyFragmentAdapter(fm: FragmentManager, var fragments: ArrayList<Fragment>) :
        FragmentStatePagerAdapter(fm) {
        /**
         * 适配器
         */
        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onClick(v: View?) {
        //4.点击事件
        when (v!!.id) {
            R.id.ac_main_tv_one -> {
                setNavSelected(0)
            }

            R.id.ac_main_tv_two -> {
                setNavSelected(1)
            }

            R.id.ac_main_relativeLayout_center -> {
                //中间的按钮
                CenterActivity.startAc(this, "", "")
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
            }

            R.id.ac_main_tv_three -> {
                setNavSelected(2)
            }

            R.id.ac_main_tv_four -> {
                setNavSelected(3)
            }
        }
    }

    private fun setNavSelected(tab: Int) {
        /**
         * 选中的时候的效果 并初始化其他的效果
         */
        //1.初始化
        acMainTvOne!!.setTextColor(getColor(R.color.bottom_nav_textview_color))
        acMainTvTwo!!.setTextColor(getColor(R.color.bottom_nav_textview_color))
        //中间这个是图片
        acMainTvThree!!.setTextColor(getColor(R.color.bottom_nav_textview_color))
        acMainTvFour!!.setTextColor(getColor(R.color.bottom_nav_textview_color))

        //2.设置选中
        when (tab) {
            0 -> {
                acMainTvOne!!.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                acMainViewPager!!.currentItem = tab
            }

            1 -> {
                acMainTvTwo!!.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                acMainViewPager!!.currentItem = tab
            }

            2 -> {
                acMainTvThree!!.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                acMainViewPager!!.currentItem = tab
            }

            3 -> {
                acMainTvFour!!.setTextColor(getColor(R.color.bottom_nav_textview_color_ed))
                acMainViewPager!!.currentItem = tab
            }
        }
    }

    private fun initBottomNavListener() {
        /**
         * 防止键盘和底部导航冲突
         */
        val softKeyboardStateHelper =
            SoftKeyboardStateHelper(findViewById<View>(R.id.ac_main_root_layout))
        softKeyboardStateHelper.addSoftKeyboardStateListener(object :
            SoftKeyboardStateHelper.SoftKeyboardStateListener {
            override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
                //键盘打开
                //Log.d("nightowl", "键盘打开");
                //将tabbar设置成不可见
                findViewById<View>(R.id.ac_main_linearLayout).visibility = View.INVISIBLE
            }

            override fun onSoftKeyboardClosed() {
                //键盘关闭
                // Log.d("nightowl", "键盘关闭");
                findViewById<View>(R.id.ac_main_linearLayout).visibility = View.VISIBLE
            }
        })
    }


    /**
     * 需要跳转到本页面的时候调用此方法
     */
    companion object {
        fun startAc(mContent: Context, data1: String, data2: String) {
            //灵活运用apply
            val intent = Intent(mContent, MainActivity::class.java).apply {
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
     * 本页面不需要左滑返回
     */
    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    /**
     * 本页面屏蔽返回键
     */
    override fun onBackPressed() {
        super.onBackPressed()
        return
    }


}