package com.example.zrframe.base

import com.example.zrframe.constant.PageName

/**
 * 获取页面名称通用接口
 */
interface IGetPageName {

    @PageName
    fun getPageName(): String

}