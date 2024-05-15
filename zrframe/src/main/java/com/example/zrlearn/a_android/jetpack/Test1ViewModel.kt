package com.example.zrlearn.a_android.jetpack

import androidx.lifecycle.ViewModel

/**
 * @Author qiwangi
 * @Date 2023/7/26
 * @TIME 14:49
 */
class Test1ViewModel:ViewModel() {
    /**
     * 理论基础：
     * viewModel的生命周期和activity的生命周期绑定，且长于activity。
     * viewModel存放和界面相关的数据，和相关变量的逻辑处理。
     */
    var counter=0
}