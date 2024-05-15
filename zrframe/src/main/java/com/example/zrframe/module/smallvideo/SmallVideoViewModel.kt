package com.example.zrframe.module.smallvideo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.zrframe.base.BaseViewModel
import com.example.zrframe.bean.VideoBean
import com.example.zrframe.constant.PageName
import com.example.zrframe.network.NetworkApi
import kotlinx.coroutines.launch

class SmallVideoViewModel : BaseViewModel() {

    val helloWorldLiveData = MutableLiveData<Result<VideoBean>>()

    fun requestVideoDetail(id: String) {
        viewModelScope.launch {
            val result = NetworkApi.requestVideoDetail(id)
            helloWorldLiveData.value = result
        }
    }

    @PageName
    override fun getPageName() = PageName.SMALL_VIDEO
}