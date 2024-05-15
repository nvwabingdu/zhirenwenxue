package com.example.zrframe.module.main

import com.example.zrframe.base.BaseViewModel
import com.example.zrframe.constant.PageName

class MainViewModel : BaseViewModel() {

    @PageName
    override fun getPageName() = PageName.HOME
}