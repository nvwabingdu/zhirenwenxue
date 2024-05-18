package cn.dreamfruits.baselib.repository

import cn.dreamfruits.baselib.integration.IRepositoryManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseRemoteDataSource : IRemoteDataSource, KoinComponent {

    private val repositoryManager: IRepositoryManager by inject()

    fun <T> retrofitService(service: Class<T>): T =
         repositoryManager.obtainRetrofitService(service)


}