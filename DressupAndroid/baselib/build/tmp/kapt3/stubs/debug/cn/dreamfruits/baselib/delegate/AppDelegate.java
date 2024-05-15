package cn.dreamfruits.baselib.delegate;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0004H\u0016J\u001e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0003\u001a\u00020\u00042\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0002J\u0010\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u0010H\u0016J\u0010\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u0010H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0006\u001a\u0004\u0018\u00010\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcn/dreamfruits/baselib/delegate/AppDelegate;", "Lcn/dreamfruits/baselib/delegate/AppLifecycle;", "Lorg/koin/core/component/KoinComponent;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "mActivityLifecycle", "Landroid/app/Application$ActivityLifecycleCallbacks;", "getMActivityLifecycle", "()Landroid/app/Application$ActivityLifecycleCallbacks;", "mActivityLifecycle$delegate", "Lkotlin/Lazy;", "mActivityLifecycles", "Ljava/util/ArrayList;", "mAppLifecycles", "mApplication", "Landroid/app/Application;", "mModules", "", "Lcn/dreamfruits/baselib/integration/ConfigModule;", "attachBaseContext", "", "base", "getGlobeConfigModule", "Lcn/dreamfruits/baselib/di/GlobalConfigModule;", "modules", "onCreate", "application", "onTerminate", "baselib_debug"})
public final class AppDelegate implements cn.dreamfruits.baselib.delegate.AppLifecycle, org.koin.core.component.KoinComponent {
    private final android.content.Context context = null;
    private android.app.Application mApplication;
    private java.util.List<? extends cn.dreamfruits.baselib.integration.ConfigModule> mModules;
    private java.util.ArrayList<cn.dreamfruits.baselib.delegate.AppLifecycle> mAppLifecycles;
    private java.util.ArrayList<android.app.Application.ActivityLifecycleCallbacks> mActivityLifecycles;
    private final kotlin.Lazy mActivityLifecycle$delegate = null;
    
    public AppDelegate(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    private final android.app.Application.ActivityLifecycleCallbacks getMActivityLifecycle() {
        return null;
    }
    
    @java.lang.Override
    public void attachBaseContext(@org.jetbrains.annotations.NotNull
    android.content.Context base) {
    }
    
    @java.lang.Override
    public void onCreate(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
    }
    
    @java.lang.Override
    public void onTerminate(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
    }
    
    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明[ConfigModule]的实现类,和Glide的配置方式相似
     */
    private final cn.dreamfruits.baselib.di.GlobalConfigModule getGlobeConfigModule(android.content.Context context, java.util.List<? extends cn.dreamfruits.baselib.integration.ConfigModule> modules) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public org.koin.core.Koin getKoin() {
        return null;
    }
}