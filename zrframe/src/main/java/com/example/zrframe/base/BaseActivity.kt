package com.example.zrframe.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import me.imid.swipebacklayout.lib.app.SwipeBackActivity


/**
 * Activity基类
 */
abstract class BaseActivity<T : ViewBinding> : SwipeBackActivity(), IGetPageName {

    protected lateinit var viewBinding: T
    protected abstract val inflater: (inflater: LayoutInflater) -> T
    private val compositeDisposable = CompositeDisposable()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)//wq
        viewBinding = inflater(layoutInflater)
        setContentView(viewBinding.root)
        setSwipeBackEnable(swipeBackEnable())
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        // 这里可以添加页面打点
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        // 这里可以添加页面打点
    }

    @CallSuper
    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
        ActivityCollector.removeActivity(this)//wq
    }

    /**
     * 默认开启左滑返回,如果需要禁用,请重写此方法
     */
    protected open fun swipeBackEnable() = true

    /**
     * 添加Disposable
     */
    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * 跳转到指定的Activity
     * startTo(AnotherActivity.class);
     */
    protected open fun startTo(destinationActivity: Class<*>?) {
        val intent = Intent(this, destinationActivity)
        startActivity(intent)
    }

    /**
     * 带参数跳转到指定的Activity
     * Bundle extras = new Bundle();
     * extras.putString("key", "value");
     * startTo(AnotherActivity.class, extras);
     *
     * intent.getLongExtra("feedId", 0)
     */
    protected open fun startTo(destinationActivity: Class<*>?, extras: Bundle?) {
        val intent = Intent(this, destinationActivity)
        if (extras != null) {
            intent.putExtras(extras)
        }
        startActivity(intent)
    }

    /**
     * 带参数和标志位跳转到指定的Activity
     * startTo(AnotherActivity.class, extras, Intent.FLAG_ACTIVITY_CLEAR_TOP);
     */
    protected open fun startTo(destinationActivity: Class<*>?, extras: Bundle?, flags: Int) {
        val intent = Intent(this, destinationActivity)
        if (extras != null) {
            intent.putExtras(extras)
        }
        intent.flags = flags
        startActivity(intent)
    }




}