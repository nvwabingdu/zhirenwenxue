package cn.dreamfruits.baselib.di;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 2, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0006\u0010\u0006\u001a\u00020\u0007\u001a\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b\u001a\u0010\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u000f\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\"\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\u0010"}, d2 = {"KODEIN_MODULE_APP_TAG", "", "appModule", "Lorg/koin/core/module/Module;", "getAppModule", "()Lorg/koin/core/module/Module;", "bindActivityLifecycle", "Lcn/dreamfruits/baselib/integration/ActivityLifecycle;", "bindRepositoryManager", "Lcn/dreamfruits/baselib/integration/RepositoryManager;", "retrofit", "Lretrofit2/Retrofit;", "provideAppManager", "Lcn/dreamfruits/baselib/integration/AppManager;", "application", "Landroid/app/Application;", "baselib_debug"})
public final class AppModuleKt {
    
    /**
     * 提供一些框架必须的实例的 Module
     */
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KODEIN_MODULE_APP_TAG = "koin_module_app_tag";
    @org.jetbrains.annotations.NotNull
    private static final org.koin.core.module.Module appModule = null;
    
    @org.jetbrains.annotations.NotNull
    public static final org.koin.core.module.Module getAppModule() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public static final cn.dreamfruits.baselib.integration.AppManager provideAppManager(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.integration.RepositoryManager bindRepositoryManager(@org.jetbrains.annotations.NotNull
    retrofit2.Retrofit retrofit) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.integration.ActivityLifecycle bindActivityLifecycle() {
        return null;
    }
}