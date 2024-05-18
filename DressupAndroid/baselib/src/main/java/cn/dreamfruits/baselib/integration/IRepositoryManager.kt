package cn.dreamfruits.baselib.integration

import androidx.annotation.NonNull


/**
 * 用来管理网络请求层,以及数据缓存层
 */
interface IRepositoryManager {

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service Retrofit service class
     * @param <T>     Retrofit service 类型
     * @return Retrofit service
     */
    fun <T> obtainRetrofitService(@NonNull service: Class<T>): T


}