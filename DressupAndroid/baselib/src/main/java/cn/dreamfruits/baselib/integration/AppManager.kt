package cn.dreamfruits.baselib.integration

import android.app.Activity
import android.app.Application
import android.content.Intent
import java.util.*

class AppManager {

    private var mApplication: Application? = null

    //管理所有存活的 Activity, 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
    private var mActivityList: MutableList<Activity>? = null

    // 当前在前台的 Activity
    private var mCurrentActivity: Activity? = null

    companion object {
        const val IS_NOT_ADD_ACTIVITY_LIST = "is_not_add_activity_list"

        @JvmStatic
        val sAppManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AppManager()
        }
    }

    fun init(application: Application): AppManager {
        return sAppManager
    }

    /**
     * 让在栈顶的 {@link Activity} ,打开指定的 {@link Activity}
     *
     * @param intent
     */
    fun startActivity(intent: Intent) {

    }

    /**
     * 释放资源
     */
    fun release(){
        mActivityList?.clear()
        mActivityList = null
        mCurrentActivity = null
        mApplication = null
    }


    /**
     * 将在前台的 {@link Activity} 赋值给 {@code currentActivity}, 注意此方法是在 {@link Activity#onResume} 方法执行时将栈顶的 {@link Activity} 赋值给 {@code currentActivity}
     * 所以在栈顶的 {@link Activity} 执行 {@link Activity#onCreate} 方法时使用 {@link #getCurrentActivity()} 获取的就不是当前栈顶的 {@link Activity}, 可能是上一个 {@link Activity}
     * 如果在 App 启动第一个 {@link Activity} 执行 {@link Activity#onCreate} 方法时使用 {@link #getCurrentActivity()} 则会出现返回为 {@code null} 的情况
     * 想避免这种情况请使用 {@link #getTopActivity()}
     *
     * @param currentActivity
     */
    fun setCurrentActivity(currentActivity: Activity?) {
        this.mCurrentActivity = currentActivity
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
    fun getCurrentActivity(): Activity? = this.mCurrentActivity


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
    fun getTopActivity(): Activity? = mActivityList?.last()


    /**
     * 返回一个存储所有未销毁的 {@link Activity} 的集合
     *
     * @return
     */
    fun getActivityList(): MutableList<Activity> {

        return mActivityList ?: LinkedList()
    }


    /**
     * 添加 {@link Activity} 到集合
     */
    fun addActivity(activity: Activity) {
        synchronized(AppManager::class.java) {
            val activities = getActivityList()
            if (!activities.contains(activity)) {
                activities.add(activity)
            }
        }
    }

    /**
     * 删除集合里的指定的 {@link Activity} 实例
     *
     * @param {@link Activity}
     */
    fun removeActivity(activity: Activity) {
        synchronized(AppManager::class.java) {

            mActivityList?.let { activityList ->
                if (activityList.contains(activity)) {
                    activityList.remove(activity)
                }
            }
        }
    }

    /**
     *
     * 删除集合里的指定位置的activity
     */
    fun removeActivity(location: Int): Activity? {
        if (mActivityList == null) {
            return null
        }
        synchronized(AppManager::class.java) {
            if (location > 0 && location < mActivityList!!.size) {
                return mActivityList!!.removeAt(location)
            }
        }
        return null
    }

    /**
     * 关闭指定的 {@link Activity} class 的所有的实例
     *
     * @param activityClass
     */
    fun killActivity(activityClass: Class<*>) {
        mActivityList?.let { activityList ->
            for (activity in activityList) {
                if (activity.javaClass == activityClass) {
                    activity.finish()
                }
            }
        }
    }

    /**
     * 指定的activity实例是否存活
     */
    fun activityInstanceIsLive(activity: Activity): Boolean {
        var isAlive = false
        mActivityList?.let {
            isAlive = it.contains(activity)
        }
        return isAlive
    }


    /**
     * 指定的activity class是否存活(一个activity可能有多个实例)
     */
    fun activityClassIsLive(activityClass: Class<*>): Boolean {
        var isAlive = false
        mActivityList?.let {
            for (activity in it) {
                if (activity.javaClass == activityClass) {
                    isAlive = true
                    break
                }
            }
        }
        return isAlive
    }


    /**
     * 关闭所有activity
     */
    fun killAll() {

        synchronized(AppManager::class.java) {
            val iterator = getActivityList().iterator()
            while (iterator.hasNext()) {
                iterator.next().finish()
                iterator.remove()
            }
        }
    }


    /**
     * 退出应用程序
     */
    fun appExit() {
        try {
            killAll()
            if (mActivityList != null) {
                mActivityList = null
            }
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}