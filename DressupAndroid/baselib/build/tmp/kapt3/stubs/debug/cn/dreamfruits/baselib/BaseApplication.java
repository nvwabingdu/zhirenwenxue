package cn.dreamfruits.baselib;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0014J\b\u0010\t\u001a\u00020\u0006H\u0016J\b\u0010\n\u001a\u00020\u0006H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcn/dreamfruits/baselib/BaseApplication;", "Landroid/app/Application;", "()V", "mAppDelegate", "Lcn/dreamfruits/baselib/delegate/AppLifecycle;", "attachBaseContext", "", "base", "Landroid/content/Context;", "onCreate", "onTerminate", "baselib_debug"})
public class BaseApplication extends android.app.Application {
    private cn.dreamfruits.baselib.delegate.AppLifecycle mAppDelegate;
    
    public BaseApplication() {
        super();
    }
    
    @java.lang.Override
    protected void attachBaseContext(@org.jetbrains.annotations.NotNull
    android.content.Context base) {
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @java.lang.Override
    public void onTerminate() {
    }
}