package com.example.zrwenxue.moudel.login

import androidx.lifecycle.MutableLiveData
import com.example.zrwenxue.moudel.BaseViewModel

/**
 * @Author qiwangi
 * @Date 2023/7/26
 * @TIME 14:49
 */
class LoginViewModel(sums:Int): BaseViewModel() {

    var sum=MutableLiveData<Int>()

    init{
      sum.value=sums
    }


}