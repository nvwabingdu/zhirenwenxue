package cn.dreamfruits.baselib.integration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 *  Application.ActivityLifecycleCallbacks 默认实现类
 */
class ActivityLifecycle : Application.ActivityLifecycleCallbacks,KoinComponent{

    private val mApplication: Application = get()
    private val mAppManager: AppManager = get()


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //如果 intent 包含了此字段,并且为 true 说明不加入到 list 进行统一管理
        var isNotAdd = false
        if (activity.intent != null)
            isNotAdd = activity.intent.getBooleanExtra(AppManager.IS_NOT_ADD_ACTIVITY_LIST,false)

        if (!isNotAdd)
            mAppManager.addActivity(activity)


    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        mAppManager.setCurrentActivity(activity)
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        if (mAppManager.getCurrentActivity() === activity) {
            mAppManager.setCurrentActivity(null)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        mAppManager.removeActivity(activity)
    }

}