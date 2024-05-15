package com.example.zrframe.bean

import com.example.zrframe.base.BaseFragment
import com.example.zrframe.constant.TabId
import kotlin.reflect.KClass

data class Tab(
    @TabId
    val id: String,
    val title: String,
    val icon: Int,
    val fragmentClz: KClass<out BaseFragment<*>>
)