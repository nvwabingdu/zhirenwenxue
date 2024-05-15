package cn.dreamfruits.baselib.repository;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u0002*\b\b\u0001\u0010\u0003*\u00020\u00042\u00020\u0005B\u0015\u0012\u0006\u0010\u0006\u001a\u00028\u0000\u0012\u0006\u0010\u0007\u001a\u00028\u0001\u00a2\u0006\u0002\u0010\bR\u0013\u0010\u0007\u001a\u00028\u0001\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0006\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\f\u0010\r\u00a8\u0006\u000f"}, d2 = {"Lcn/dreamfruits/baselib/repository/BaseRepository;", "T", "Lcn/dreamfruits/baselib/repository/IRemoteDataSource;", "R", "Lcn/dreamfruits/baselib/repository/ILocalDataSource;", "Lcn/dreamfruits/baselib/repository/IRepository;", "remoteDataSource", "localDataSource", "(Lcn/dreamfruits/baselib/repository/IRemoteDataSource;Lcn/dreamfruits/baselib/repository/ILocalDataSource;)V", "getLocalDataSource", "()Lcn/dreamfruits/baselib/repository/ILocalDataSource;", "Lcn/dreamfruits/baselib/repository/ILocalDataSource;", "getRemoteDataSource", "()Lcn/dreamfruits/baselib/repository/IRemoteDataSource;", "Lcn/dreamfruits/baselib/repository/IRemoteDataSource;", "baselib_debug"})
public class BaseRepository<T extends cn.dreamfruits.baselib.repository.IRemoteDataSource, R extends cn.dreamfruits.baselib.repository.ILocalDataSource> implements cn.dreamfruits.baselib.repository.IRepository {
    @org.jetbrains.annotations.NotNull
    private final T remoteDataSource = null;
    @org.jetbrains.annotations.NotNull
    private final R localDataSource = null;
    
    public BaseRepository(@org.jetbrains.annotations.NotNull
    T remoteDataSource, @org.jetbrains.annotations.NotNull
    R localDataSource) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final T getRemoteDataSource() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final R getLocalDataSource() {
        return null;
    }
}