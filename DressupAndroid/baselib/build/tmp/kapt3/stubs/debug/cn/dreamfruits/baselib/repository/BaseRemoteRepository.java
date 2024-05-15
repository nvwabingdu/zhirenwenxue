package cn.dreamfruits.baselib.repository;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0005R\u0013\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\t"}, d2 = {"Lcn/dreamfruits/baselib/repository/BaseRemoteRepository;", "T", "Lcn/dreamfruits/baselib/repository/IRemoteDataSource;", "Lcn/dreamfruits/baselib/repository/IRepository;", "remoteDataSource", "(Lcn/dreamfruits/baselib/repository/IRemoteDataSource;)V", "getRemoteDataSource", "()Lcn/dreamfruits/baselib/repository/IRemoteDataSource;", "Lcn/dreamfruits/baselib/repository/IRemoteDataSource;", "baselib_debug"})
public class BaseRemoteRepository<T extends cn.dreamfruits.baselib.repository.IRemoteDataSource> implements cn.dreamfruits.baselib.repository.IRepository {
    @org.jetbrains.annotations.NotNull
    private final T remoteDataSource = null;
    
    public BaseRemoteRepository(@org.jetbrains.annotations.NotNull
    T remoteDataSource) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final T getRemoteDataSource() {
        return null;
    }
}