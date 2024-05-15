package com.example.zrframe.module.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.zrframe.base.BaseFragment
import com.example.zrframe.constant.PageName
import com.example.zrframe.databinding.FragmentMineBinding

/**
 * 我的
 */
class MineFragment : BaseFragment<FragmentMineBinding>() {

    private val viewModel: MineViewModel by viewModels()
    override val inflater: (LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) -> FragmentMineBinding
        get() = FragmentMineBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
//        viewBinding.itemHistory.setOnClickListener {
//
//        }
//        viewBinding.itemCollection.setOnClickListener {
//
//        }
//        viewBinding.itemSubscribe.setOnClickListener {
//
//        }
//        viewBinding.itemSetting.setOnClickListener {
//
//        }
    }

    @PageName
    override fun getPageName() = PageName.MINE

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        // 这里可以添加页面打点
    }
}