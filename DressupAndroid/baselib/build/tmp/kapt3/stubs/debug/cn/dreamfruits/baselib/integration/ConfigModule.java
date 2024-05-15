package cn.dreamfruits.baselib.integration;

import java.lang.System;

/**
 * 项目中一些全局配置的接口.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u001e\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH&J\u001e\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH&\u00a8\u0006\u000f"}, d2 = {"Lcn/dreamfruits/baselib/integration/ConfigModule;", "", "applyOptions", "", "context", "Landroid/content/Context;", "builder", "Lcn/dreamfruits/baselib/di/GlobalConfigModule$Builder;", "injectActivityLifecycle", "lifecycles", "", "Landroid/app/Application$ActivityLifecycleCallbacks;", "injectAppLifecycle", "", "Lcn/dreamfruits/baselib/delegate/AppLifecycle;", "baselib_debug"})
public abstract interface ConfigModule {
    
    /**
     * 使用 [GlobalConfigModule.Builder] 给框架配置一些配置参数
     */
    public abstract void applyOptions(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    cn.dreamfruits.baselib.di.GlobalConfigModule.Builder builder);
    
    /**
     * 使用[AppLifecycle]在Application的声明周期中注入一些操作
     */
    public abstract void injectAppLifecycle(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<cn.dreamfruits.baselib.delegate.AppLifecycle> lifecycles);
    
    /**
     * 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作
     */
    public abstract void injectActivityLifecycle(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<? extends android.app.Application.ActivityLifecycleCallbacks> lifecycles);
}