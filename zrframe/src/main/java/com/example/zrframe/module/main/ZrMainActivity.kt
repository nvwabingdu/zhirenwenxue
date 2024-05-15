package com.example.zrframe.module.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.zrframe.R
import com.example.zrframe.base.BaseActivity
import com.example.zrframe.bean.Tab
import com.example.zrframe.constant.PageName
import com.example.zrframe.constant.TabId
import com.example.zrframe.databinding.ZrActivityMainBinding
import com.example.zrframe.module.acgn.AcgnFragment
import com.example.zrframe.module.discovery.DiscoveryFragment
import com.example.zrframe.module.home.HomeFragment
import com.example.zrframe.module.mine.MineFragment
import com.example.zrframe.widget.NavigationView
import com.example.zrframe.widget.TabIndicatorView
import com.gyf.immersionbar.ktx.immersionBar

/**
 * 首页
 */
class ZrMainActivity : BaseActivity<ZrActivityMainBinding>() {

//    private val viewModel: MainViewModel by viewModels()
    override val inflater: (inflater: LayoutInflater) -> ZrActivityMainBinding
        get() = ZrActivityMainBinding::inflate

    // 当前选中的底栏ID
    @TabId
    private var currentTabId = TabId.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSystemBar()
        updateTitle()
        initTabs()
    }

    @PageName
    override fun getPageName() = PageName.MAIN

    /**
     * 禁用左滑返回
     */
    override fun swipeBackEnable() = false

    /**
     * 状态栏导航栏初始化
     */
    private fun initSystemBar() {
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(true)
            navigationBarColor(R.color.white)
            navigationBarDarkIcon(true)
        }
    }

    /**
     * 初始化底栏
     */
    private fun initTabs() {
//        val tabs = listOf(
//            Tab(TabId.HOME, getString(R.string.page_home), R.drawable.selector_btn_home, HomeFragment::class),
//            Tab(TabId.ACGN, getString(R.string.page_acgn), R.drawable.selector_btn_acgn, AcgnFragment::class),
//            // Tab(TabId.SMALL_VIDEO, getString(R.string.page_small_video), R.drawable.selector_btn_small_video, SmallVideoFragment::class),
//            // Tab(TabId.GOLD, getString(R.string.page_gold), R.drawable.selector_btn_gold, GoldFragment::class),
//            Tab(TabId.DISCOVERY, getString(R.string.page_discovery), R.drawable.selector_btn_discovery, DiscoveryFragment::class),
//            Tab(TabId.MINE, getString(R.string.page_mine), R.drawable.selector_btn_mine, MineFragment::class)
//        )
//
//        viewBinding.fragmentTabHost.run {
//            // 调用setup()方法，设置FragmentManager，以及指定用于装载Fragment的布局容器
//            setup(this@ZrMainActivity, supportFragmentManager, viewBinding.fragmentContainer.id)
//            tabs.forEach {
//                val (id, title, icon, fragmentClz) = it
//                val tabSpec = newTabSpec(id).apply {
//                    setIndicator(TabIndicatorView(this@ZrMainActivity).apply {
//                        viewBinding.tabIcon.setImageResource(icon)
//                        viewBinding.tabTitle.text = title
//                    })
//                }
//                addTab(tabSpec, fragmentClz.java, null)
//            }
//
//            setOnTabChangedListener { tabId ->
//                currentTabId = tabId
//                updateTitle()
//            }
//        }
    }

    /**
     * 更新标题
     */
    private fun updateTitle() {
        val title = when (currentTabId) {
            TabId.HOME -> getString(R.string.page_home)
            TabId.SMALL_VIDEO -> getString(R.string.page_small_video)
            TabId.ACGN -> getString(R.string.page_acgn)
            TabId.GOLD -> getString(R.string.page_gold)
            TabId.MINE -> getString(R.string.page_mine)
            TabId.DISCOVERY -> getString(R.string.page_discovery)
            else -> ""
        }

        viewBinding.navigationBar.setParameter(
            NavigationView.ParameterBuilder()
                .setShowBack(false)
                .setShowTitle(true)
                .setTitle(title)
        )
    }

    /**
     * 设置当前选中的TAB
     */
    private fun setCurrentTab(@TabId tabID: String) {
        viewBinding.fragmentTabHost.setCurrentTabByTag(tabID)
    }

    /**
     * 需要跳转到本页面的时候调用此方法
     */
    companion object{
        fun startAc(mContent: Context, data1:String, data2: String){
            //灵活运用apply
            val intent= Intent(mContent, ZrMainActivity::class.java).apply {
                putExtra("param1", data1)
                putExtra("param1", data2)
            }
            mContent.startActivity(intent)
        }
    }

}