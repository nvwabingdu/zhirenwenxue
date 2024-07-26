package com.example.zrwenxue.moudel.main.center.zr


import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.newzr.R
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.BaseActivity
import com.example.zrwenxue.moudel.main.word.singleworddetails.tabwebview.ViewPagerFragmentAdapter
import com.google.android.material.tabs.TabLayout

class ZrActivity : BaseActivity() {
    override fun layoutResId(): Int = R.layout.activity_zr

    override fun init() {
        //设置顶部
        setTopView()
        //设置联动布局
        setTabLayout_viewpager_fragment()
    }

    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView() {
        topView = findViewById(R.id.title_bar)
        topView!!.setTitle("智人社区")
        //左边返回
        topView!!.setOnclickLeft(
            View.VISIBLE,
            View.OnClickListener { finish() })
        //右边弹出pop
        topView!!.setOnclickRight(View.INVISIBLE, View.OnClickListener { })
    }

    /**
     * ====================================================设置setTabLayout  + fragment
     */
    private var mViewPager: ViewPager? = null
    private var mTabLayout: TabLayout? = null

    fun setTabLayout_viewpager_fragment() {
        mViewPager = findViewById<ViewPager>(R.id.fragment_word_viewpager)
        mTabLayout = findViewById<TabLayout>(R.id.fragment_word_tabs)
        //tab文字的集合
        val mTitleList = ArrayList<String>()
        mTitleList.add("涂鸦画作")
        mTitleList.add("智人币")
        mTitleList.add("获取智人币")
        mTitleList.add("购买涂鸦画")
        mTitleList.add("赠送")
        mTitleList.add("打赏")

        val webAdapter =
            ViewPagerFragmentAdapter(
                getSupportFragmentManager(),
                getFragmentList(),
                mTitleList
            ) //数组转化为集合
        //给ViewPager设置适配器
        mViewPager!!.adapter = webAdapter

        //将TabLayout和ViewPager关联起来。
        mTabLayout!!.setupWithViewPager(mViewPager)
        //给TabLayout设置适配器
        mTabLayout!!.setTabsFromPagerAdapter(webAdapter)
    }

    /**
     * @return 获取带参数的fragment集合 用于装载在viewpager里面
     */
    private val fragmentArrayList = ArrayList<Fragment>()

    fun getFragmentList(): ArrayList<Fragment> {
        val f0 = C0Fragment()
        fragmentArrayList.add(f0)

        val f1 = C1Fragment()
        fragmentArrayList.add(f1)

        val f2 = C2Fragment()
        fragmentArrayList.add(f2)

        val f3 = C3Fragment()
        fragmentArrayList.add(f3)

        val f4 = C4Fragment()
        fragmentArrayList.add(f4)

        val f5 = C5Fragment()
        fragmentArrayList.add(f5)

        return fragmentArrayList
    }


}

