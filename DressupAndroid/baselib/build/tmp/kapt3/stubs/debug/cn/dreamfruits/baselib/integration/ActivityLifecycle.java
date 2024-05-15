package cn.dreamfruits.baselib.integration;

import java.lang.System;

/**
 * Application.ActivityLifecycleCallbacks 默认实现类
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001a\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\u000f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\u0010\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\u0011\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\rH\u0016J\u0010\u0010\u0013\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\u0014\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcn/dreamfruits/baselib/integration/ActivityLifecycle;", "Landroid/app/Application$ActivityLifecycleCallbacks;", "Lorg/koin/core/component/KoinComponent;", "()V", "mAppManager", "Lcn/dreamfruits/baselib/integration/AppManager;", "mApplication", "Landroid/app/Application;", "onActivityCreated", "", "activity", "Landroid/app/Activity;", "savedInstanceState", "Landroid/os/Bundle;", "onActivityDestroyed", "onActivityPaused", "onActivityResumed", "onActivitySaveInstanceState", "outState", "onActivityStarted", "onActivityStopped", "baselib_debug"})
public final class ActivityLifecycle implements android.app.Application.ActivityLifecycleCallbacks, org.koin.core.component.KoinComponent {
    private final android.app.Application mApplication = null;
    private final cn.dreamfruits.baselib.integration.AppManager mAppManager = null;
    
    public ActivityLifecycle() {
        super();
    }
    
    @java.lang.Override
    public void onActivityCreated(@org.jetbrains.annotations.NotNull
    android.app.Activity activity, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    public void onActivityStarted(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
    }
    
    @java.lang.Override
    public void onActivityResumed(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
    }
    
    @java.lang.Override
    public void onActivityPaused(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
    }
    
    @java.lang.Override
    public void onActivityStopped(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
    }
    
    @java.lang.Override
    public void onActivitySaveInstanceState(@org.jetbrains.annotations.NotNull
    android.app.Activity activity, @org.jetbrains.annotations.NotNull
    android.os.Bundle outState) {
    }
    
    @java.lang.Override
    public void onActivityDestroyed(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
    }
    
    @org.jetbrains.annotations.NotNull
    public org.koin.core.Koin getKoin() {
        return null;
    }
}