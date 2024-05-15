package cn.dreamfruits.baselib.delegate;

import java.lang.System;

/**
 * 用于代理 [Application]的生命周期
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\n"}, d2 = {"Lcn/dreamfruits/baselib/delegate/AppLifecycle;", "", "attachBaseContext", "", "base", "Landroid/content/Context;", "onCreate", "application", "Landroid/app/Application;", "onTerminate", "baselib_debug"})
public abstract interface AppLifecycle {
    
    public abstract void attachBaseContext(@org.jetbrains.annotations.NotNull
    android.content.Context base);
    
    public abstract void onCreate(@org.jetbrains.annotations.NotNull
    android.app.Application application);
    
    public abstract void onTerminate(@org.jetbrains.annotations.NotNull
    android.app.Application application);
}