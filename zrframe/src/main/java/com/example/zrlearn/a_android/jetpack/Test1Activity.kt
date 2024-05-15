package com.example.zrlearn.a_android.jetpack

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zrframe.R

/**
 * @Author qiwangi
 * @Date 2023/8/5
 * @TIME 19:28
 */
class Test1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_about)
    }


    /**
     * ==========================================1.viewModel普通使用方式
     */
    lateinit var viewmodel1: Test1ViewModel
    fun initData1() {
        /**    固定写法 ViewModelProvider(this).get(xxxxViewModel::class.java)      */
        viewmodel1 = ViewModelProvider(this).get(Test1ViewModel::class.java)
        viewmodel1.counter = 0
    }


    /**
     * 2.需要给viewModel构造传参的方式    用于要保存数据情况 一般用共享参数保存，然后取出，在AC初始化的时候通过这种方式传入
     */
    lateinit var viewmodel2: Test2ViewModel

    val viewmodel3: Test2ViewModel by lazy {//懒加载写法
        ViewModelProvider(this, Test2ViewModelFactory(9)).get(Test2ViewModel::class.java)
    }

    /**
     * 通过此种方式构造传参
     */
    class Test2ViewModelFactory(private val count: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {//必须实现的方法
            return Test2ViewModel(count) as T
        }
    }

    fun initData2() {
        viewmodel2 =
            ViewModelProvider(this, Test2ViewModelFactory(9)).get(Test2ViewModel::class.java)


        /**加入livedata之后的使用  数据有变化 主动告知Activity */
        viewmodel2.sum.observe(this, Observer {
            //观察者模式
            //此处的it就是sum的值
            findViewById<TextView>(R.id.tv_about).text=it.toString()
        })


    }


    /**
     * ==================================lifecycle   1.第一种方式手动写代码实现生命周期的观察  如下
     */
    lateinit var observer: MyObserver

    class MyObserver {
        fun activityCreate() {
            //activity创建时调用
        }

        fun activityStart() {
            //activity启动时调用
        }

        fun activityResume() {
            //activity恢复时调用
        }

        fun activityPause() {
            //activity暂停时调用
        }
    }

    /**
     * 和本activity绑定实现生命周期的观察
     */
    override fun onRestart() {
        super.onRestart()
        observer.activityStart()
    }

    override fun onResume() {
        super.onResume()
        observer.activityResume()
    }

    override fun onPause() {
        super.onPause()
        observer.activityPause()
    }

    /**
     * 2.第二种方式通过lifecycle库实现生命周期的观察
     */
    class MyObserver2 : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)//ON_ANY表示匹配所有生命周期
        fun activityCreate() {
            //activity创建时调用
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun activityStart() {
            //activity启动时调用
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun activityResume() {
            //activity恢复时调用
        }
    }

    /**
     * 3.第3种方式通过lifecycle库实现生命周期的观察 通过构造传参的方式 实现观察通知
     */
    class MyObserver3(val lifecycle: Lifecycle) : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)//ON_ANY表示匹配所有生命周期
        fun activityCreate() {
            //activity创建时调用
            lifecycle.currentState//获取当前生命周期状态
            when (lifecycle.currentState) {
                Lifecycle.State.INITIALIZED -> {
                    //初始化状态
                }
                Lifecycle.State.CREATED -> {
                    //创建状态
                }
                Lifecycle.State.STARTED -> {
                    //启动状态
                }
                Lifecycle.State.RESUMED -> {
                    //恢复状态
                }
                Lifecycle.State.DESTROYED -> {
                    //销毁状态
                }
                else -> {}
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun activityStart() {
            //activity启动时调用
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun activityResume() {
            //activity恢复时调用
        }
    }


    //lifecycleOwner.lifecycle.addObserver(MyObserver2())//绑定生命周期观察者
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //第二种方式
        lifecycle.addObserver(MyObserver2())//绑定生命周期观察者

        //第三种方式
        lifecycle.addObserver(MyObserver3(lifecycle))//绑定生命周期观察者

    }



}