package cn.dreamfruits.baselib.integration;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\t\u001a\u00020\n2\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\fJ\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u0005J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\u0005J\u0006\u0010\u0011\u001a\u00020\u0010J\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J\b\u0010\u0013\u001a\u0004\u0018\u00010\u0005J\b\u0010\u0014\u001a\u0004\u0018\u00010\u0005J\u000e\u0010\u0015\u001a\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u0007J\u0012\u0010\u0017\u001a\u00020\u00102\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\fJ\u0006\u0010\u0018\u001a\u00020\u0010J\u0006\u0010\u0019\u001a\u00020\u0010J\u000e\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\u0005J\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001b\u001a\u00020\u001cJ\u0010\u0010\u001d\u001a\u00020\u00102\b\u0010\u001e\u001a\u0004\u0018\u00010\u0005J\u000e\u0010\u001f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020!R\u0016\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcn/dreamfruits/baselib/integration/AppManager;", "", "()V", "mActivityList", "", "Landroid/app/Activity;", "mApplication", "Landroid/app/Application;", "mCurrentActivity", "activityClassIsLive", "", "activityClass", "Ljava/lang/Class;", "activityInstanceIsLive", "activity", "addActivity", "", "appExit", "getActivityList", "getCurrentActivity", "getTopActivity", "init", "application", "killActivity", "killAll", "release", "removeActivity", "location", "", "setCurrentActivity", "currentActivity", "startActivity", "intent", "Landroid/content/Intent;", "Companion", "baselib_debug"})
public final class AppManager {
    private android.app.Application mApplication;
    private java.util.List<android.app.Activity> mActivityList;
    private android.app.Activity mCurrentActivity;
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.integration.AppManager.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String IS_NOT_ADD_ACTIVITY_LIST = "is_not_add_activity_list";
    @org.jetbrains.annotations.NotNull
    private static final kotlin.Lazy sAppManager$delegate = null;
    
    public AppManager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public static final cn.dreamfruits.baselib.integration.AppManager getSAppManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final cn.dreamfruits.baselib.integration.AppManager init(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        return null;
    }
    
    /**
     * 让在栈顶的 {@link Activity} ,打开指定的 {@link Activity}
     *
     * @param intent
     */
    public final void startActivity(@org.jetbrains.annotations.NotNull
    android.content.Intent intent) {
    }
    
    /**
     * 释放资源
     */
    public final void release() {
    }
    
    /**
     * 将在前台的 {@link Activity} 赋值给 {@code currentActivity}, 注意此方法是在 {@link Activity#onResume} 方法执行时将栈顶的 {@link Activity} 赋值给 {@code currentActivity}
     * 所以在栈顶的 {@link Activity} 执行 {@link Activity#onCreate} 方法时使用 {@link #getCurrentActivity()} 获取的就不是当前栈顶的 {@link Activity}, 可能是上一个 {@link Activity}
     * 如果在 App 启动第一个 {@link Activity} 执行 {@link Activity#onCreate} 方法时使用 {@link #getCurrentActivity()} 则会出现返回为 {@code null} 的情况
     * 想避免这种情况请使用 {@link #getTopActivity()}
     *
     * @param currentActivity
     */
    public final void setCurrentActivity(@org.jetbrains.annotations.Nullable
    android.app.Activity currentActivity) {
    }
    
    /**
     * 获取在前台的 {@link Activity} (保证获取到的 {@link Activity} 正处于可见状态, 即未调用 {@link Activity#onStop()}), 获取的 {@link Activity} 存续时间
     * 是在 {@link Activity#onStop()} 之前, 所以如果当此 {@link Activity} 调用 {@link Activity#onStop()} 方法之后, 没有其他的 {@link Activity} 回到前台(用户返回桌面或者打开了其他 App 会出现此状况)
     * 这时调用 {@link #getCurrentActivity()} 有可能返回 {@code null}, 所以请注意使用场景和 {@link #getTopActivity()} 不一样
     * <p>
     * Example usage:
     * 使用场景比较适合, 只需要在可见状态的 {@link Activity} 上执行的操作
     * 如当后台 {@link Service} 执行某个任务时, 需要让前台 {@link Activity} ,做出某种响应操作或其他操作,如弹出 {@link Dialog}, 这时在 {@link Service} 中就可以使用 {@link #getCurrentActivity()}
     * 如果返回为 {@code null}, 说明没有前台 {@link Activity} (用户返回桌面或者打开了其他 App 会出现此状况), 则不做任何操作, 不为 {@code null}, 则弹出 {@link Dialog}
     *
     * @return
     */
    @org.jetbrains.annotations.Nullable
    public final android.app.Activity getCurrentActivity() {
        return null;
    }
    
    /**
     * 获取最近启动的一个 {@link Activity}, 此方法不保证获取到的 {@link Activity} 正处于前台可见状态
     * 即使 App 进入后台或在这个 {@link Activity} 中打开一个之前已经存在的 {@link Activity}, 这时调用此方法
     * 还是会返回这个最近启动的 {@link Activity}, 因此基本不会出现 {@code null} 的情况
     * 比较适合大部分的使用场景, 如 startActivity
     * <p>
     * Tips: mActivityList 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     *
     * @return
     */
    @org.jetbrains.annotations.Nullable
    public final android.app.Activity getTopActivity() {
        return null;
    }
    
    /**
     * 返回一个存储所有未销毁的 {@link Activity} 的集合
     *
     * @return
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<android.app.Activity> getActivityList() {
        return null;
    }
    
    /**
     * 添加 {@link Activity} 到集合
     */
    public final void addActivity(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
    }
    
    /**
     * 删除集合里的指定的 {@link Activity} 实例
     *
     * @param {@link Activity}
     */
    public final void removeActivity(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
    }
    
    /**
     * 删除集合里的指定位置的activity
     */
    @org.jetbrains.annotations.Nullable
    public final android.app.Activity removeActivity(int location) {
        return null;
    }
    
    /**
     * 关闭指定的 {@link Activity} class 的所有的实例
     *
     * @param activityClass
     */
    public final void killActivity(@org.jetbrains.annotations.NotNull
    java.lang.Class<?> activityClass) {
    }
    
    /**
     * 指定的activity实例是否存活
     */
    public final boolean activityInstanceIsLive(@org.jetbrains.annotations.NotNull
    android.app.Activity activity) {
        return false;
    }
    
    /**
     * 指定的activity class是否存活(一个activity可能有多个实例)
     */
    public final boolean activityClassIsLive(@org.jetbrains.annotations.NotNull
    java.lang.Class<?> activityClass) {
        return false;
    }
    
    /**
     * 关闭所有activity
     */
    public final void killAll() {
    }
    
    /**
     * 退出应用程序
     */
    public final void appExit() {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R!\u0010\u0005\u001a\u00020\u00068FX\u0087\u0084\u0002\u00a2\u0006\u0012\n\u0004\b\n\u0010\u000b\u0012\u0004\b\u0007\u0010\u0002\u001a\u0004\b\b\u0010\t\u00a8\u0006\f"}, d2 = {"Lcn/dreamfruits/baselib/integration/AppManager$Companion;", "", "()V", "IS_NOT_ADD_ACTIVITY_LIST", "", "sAppManager", "Lcn/dreamfruits/baselib/integration/AppManager;", "getSAppManager$annotations", "getSAppManager", "()Lcn/dreamfruits/baselib/integration/AppManager;", "sAppManager$delegate", "Lkotlin/Lazy;", "baselib_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final cn.dreamfruits.baselib.integration.AppManager getSAppManager() {
            return null;
        }
        
        @kotlin.jvm.JvmStatic
        @java.lang.Deprecated
        public static void getSAppManager$annotations() {
        }
    }
}