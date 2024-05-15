package com.example.zrwenxue.moudel.main.home.child

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import com.example.newzr.R
import com.example.zrwenxue.moudel.BaseFragment

class HomeChildM001Fragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeChildM001Fragment()
    }

    private lateinit var viewModel: HomeChildM001ViewModel
    override fun setLayout(): Int= R.layout.fragment_home_child_m001
    override fun initView() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeChildM001ViewModel::class.java)
    }

}