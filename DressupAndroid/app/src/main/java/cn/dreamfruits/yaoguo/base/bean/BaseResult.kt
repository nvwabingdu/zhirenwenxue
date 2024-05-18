package cn.dreamfruits.yaoguo.base.bean

//网络请求返回的基类
data class BaseResult<out T>(
    val code: Int,//状态码
    val msg: String,//状态
    val data: T?//数据
)