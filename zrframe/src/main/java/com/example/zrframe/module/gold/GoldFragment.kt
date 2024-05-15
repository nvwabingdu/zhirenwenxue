package com.example.zrframe.module.gold

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.zrframe.base.BaseFragment
import com.example.zrframe.constant.EventName
import com.example.zrframe.constant.PageName
import com.example.zrframe.databinding.FragmentGoldBinding
import com.example.zrframe.eventbus.XEventBus

/**
 * 领现金
 */
class GoldFragment : BaseFragment<FragmentGoldBinding>() {

    private val viewModel: GoldViewModel by viewModels()
    override val inflater: (LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) -> FragmentGoldBinding
        get() = FragmentGoldBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewBinding.tvGold.setOnClickListener {
            XEventBus.post(EventName.REFRESH_HOME_LIST, "领现金页面通知首页刷新数据")
        }
    }

    @PageName
    override fun getPageName() = PageName.GOLD

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        // 这里可以添加页面打点
    }
}