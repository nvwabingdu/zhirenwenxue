package cn.dreamfruits.baselib.integration


import retrofit2.Retrofit

/**
 * 用来管理网络请求层,
 */
class RepositoryManager(val mRetrofit: Retrofit) : IRepositoryManager {


    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     */
    @Synchronized
    override fun <T> obtainRetrofitService(serviceClass: Class<T>): T {
        return mRetrofit.create(serviceClass)
    }



}