package com.example.zrwenxue.moudel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Author qiwangi
 * @Date 2023/8/18
 * @TIME 19:21
 */
abstract class BaseViewModel:ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    protected fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    protected fun setErrorMessage(errorMessage: String) {
        _errorMessage.value = errorMessage
    }

}