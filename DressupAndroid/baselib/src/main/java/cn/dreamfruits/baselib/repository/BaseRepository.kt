package cn.dreamfruits.baselib.repository


open class BaseRepository<T : IRemoteDataSource, R : ILocalDataSource>(
    val remoteDataSource: T,
    val localDataSource: R
) : IRepository

open class BaseLocalRepository<T : ILocalDataSource>(
    val localDataSource: T
) : IRepository

open class BaseRemoteRepository<T : IRemoteDataSource>(val remoteDataSource: T) : IRepository

open class BaseRepositoryNothing() : IRepository