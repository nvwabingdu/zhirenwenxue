package com.example.zrframe.module.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.zrframe.R
import com.example.zrframe.base.BaseActivity
import com.example.zrframe.constant.EventName
import com.example.zrframe.constant.PageName
import com.example.zrframe.databinding.ActivityAboutBinding
import com.example.zrframe.eventbus.XEventBus
import com.gyf.immersionbar.ktx.immersionBar

class AboutActivity : BaseActivity<ActivityAboutBinding>() {

//    private val viewModel: AboutViewModel by viewModels()
    override val inflater: (inflater: LayoutInflater) -> ActivityAboutBinding
        get() = ActivityAboutBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        initSystemBar()

        viewBinding.tvAbout.setOnClickListener {
            XEventBus.post(EventName.TEST, "来自关于页面的消息")
        }
    }

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

    @PageName
    override fun getPageName() = PageName.ABOUT


    /**最佳跳转 通过静态写在目标activity里面*/
    companion object{
        fun startAboutActivity(mContent:Context,data1:String,data2: String){
            //灵活运用apply
            val intent=Intent(mContent,AboutActivity::class.java).apply {
                putExtra("param1", data1)
                putExtra("param1", data2)
            }
            mContent.startActivity(intent)
        }
    }



}