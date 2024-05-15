安卓系统学习

一.名词解释
1.JDK：java的语言开发包（包含java的运行环境，工具集合，基础类库等内容。）
2.SDK：软件开发工具包。

二.四大组件
1.activity
1.1显式启动  
一般常用的方式
val intent=Intent(this,TwoActivity::class.java)
startActivity(intent)

/**最佳跳转方式 通过静态写在目标activity里面*/

```Kotlin
companion object {
    fun starAboutActivity(mContent: Context, data1: String, data2: String) {
        val intent = Intent(mContent, AboutActivity::class.java)
        intent.putExtra("param1", data1)
        intent.putExtra("param1", data2)
        mContent.startActivity(intent)
    }
}
```

1.2隐式启动
1.2.1在TwoActivity的清单文件中

```xml

<activity android:name=".module.about.AboutActivity" android:screenOrientation="portrait"
    android:theme="@style/ActivityTheme">
    <intent-filter>
        <action android:name="com.example.zrframe.Two.Action_Start"></action>
        <category android:name="com.example.zrframe.category.DEFAUT"></category>
        <data></data>
    </intent-filter>
</activity>
```

1.2.2使用方式

```xml
```

```kotlin
```

```kotlin
val intent = Intent("com.example.zrframe.Two.Action_Start")
//需要同时匹配action category(这里是默认所以不用)
startActivity(intent)

//1.2.3
val intent = Intent("com.example.zrframe.Two.Action_Start")//需要同时匹配action category(这里是默认所以不用)
intent.addCategory("com.example.zrframe.category.xxxx")//这里添加了另外一种类别 category可以有多个
action只能有一个
startActivity(intent)
//（注意在代码中添加了 需要在清单文件中添加）
```

1.3扩展
打开一个网页
val intent=Intent(Intent.ACTION_VIEW)//需要同时匹配action category(这里是默认所以不用)
intent.data=Uri.parse("http://www.baidu.com")
startActivity(intent)

1.4 Activity的回调方式
1.4.1 Activity的回调方式
val intent=Intent(this,AboutActivity::class.java)
startActivityForResult(intent,1)//kotlin中舍弃了这种写法 用到了再看看
//接收
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
super.onActivityResult(requestCode, resultCode, data)
when(requestCode){
1->if (resultCode== RESULT_OK){
val returnData=data?.getStringExtra("data_return")
Log.e("AboutActivity","返回的数据${returnData}")
}
}
}

1.4.2 AboutActivity中
val inten=Intent()
intent.putExtra("data_return","hello MainActivity")
setResult(RESULT_OK,intent)
finish

activity启动模式
standard //默认普通模式 启动一次创建一个
singleTop //判断是否在栈顶 有的话就使用它
singleTask //单一实例 有的话把之上的统统出栈
singleInstance //启用新的返回栈
singleInstancePerTask

该懒加载的实现，是在 onResume 方法中操作，当然你可以在其他生命周期函数中控制。但是建
议在该方法中执行懒加载。
ViewPager+Fragment 模式下的老方案
使用传统方式处理 ViewPager 中 Fragment 的懒加载，我们需要控制 setUserVisibleHint(boolean
isVisibleToUser) 函数，该函数的声明如下所示：
该函数与之前我们介绍的 onHiddenChanged() 作用非常相似，都是通过传入的参数值来判断当前
Fragment 是否对用户可见，只是 onHiddenChanged() 是在 add+show+hide 模式下使用，而
setUserVisibleHint 是在 ViewPager+Fragment 模式下使用。
在本节中，我们用 FragmentPagerAdapter + ViewPager 为例，向大家讲解如何实现 Fragment 的懒
加载。
注意：在本例中没有调用 setOffscreenPageLimit 方法去设置 ViewPager 预缓存的 Fragment
个数。默认情况下 ViewPager 预缓存 Fragment 的个数为 1 。
初始化 ViewPager 查看内部 Fragment 生命周期函数调用情况：
viewpager1.png
观察上图，我们能发现 ViePager 初始化时，默认会调用其内部 Fragment 的 setUserVisibleHint 方
法，因为其预缓存 Fragment 个数为 1 的原因，所以只有 Fragment_1 与 Fragment_2 的生命周期函数
被调用。
我们继续切换到 Fragment_2，查看各个Fragment的生命周期函数的调用变化。如下图所示：
isLoaded = false }//懒加载方法 abstract fun lazyInit() }

//activity调用fragment的方法
val f=supportFragmentManager.findFragmentById(R.id.label_fragment_container) as LabelDetailsFragment
f.testMethod()

//fragment调用activity方法

//fragment调用其他fragment方法
先通过父类的activity方法 然后父类去调用其他的fragment的父类activity 再调用其内部的fragment

```Kotlin
abstract class LazyFragment : Fragment() {
    private var isLoaded = false //控制是否执行懒加载 
    override fun onResume() {
        super.onResume()
        judgeLazyInit()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isVisibleToUser = !hidden
        judgeLazyInit()
    }
    private fun judgeLazyInit() {
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    //懒加载方法 
    abstract fun lazyInit()
}
```

2.广播 BroadCastReceiver
2.1标准广播 完全异步 所有接受者同时收到 不可截断
2.2有序广播 同步执行 同一时刻只有一个接收者收到 有先后顺序 可截断

系统广播
动态注册：
/**时间变化系统广播 动态注册方式*/
lateinit var timeChangeReceiver:TimeChangeReceiver
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
val intentFilter=IntentFilter()
intentFilter.addAction("android.intent.action.TIME_TICK")
timeChangeReceiver=TimeChangeReceiver()
registerReceiver(timeChangeReceiver,intentFilter)
}
inner class TimeChangeReceiver:BroadcastReceiver(){
override fun onReceive(context: Context?, intent: Intent?) {
//操作
}
}
override fun onDestroy() {//必须取消注册
super.onDestroy()
unregisterReceiver(timeChangeReceiver)
}
静态注册： new other
class MyReceiver : BroadcastReceiver() {
override fun onReceive(context: Context, intent: Intent) {
// This method is called when the BroadcastReceiver is receiving an Intent broadcast.
TODO("MyReceiver.onReceive() is not implemented")
}
}
//清单文件
<receiver
android:name=".module.about.MyReceiver"
android:enabled="true"      //是否允许接收别的程序广播
android:exported="true"/>   //是否启用这个广播

3.服务
服务默认在主线程中运行，所有有些耗时操作依然需要在子线程中处理。

异步消息处理机制

1.也就是handler机制
1.message 消息 用于线程中传递 what标记 obj对象
2.handler 处理者主要用于发送消息 post() sendmessage()
3.messageQueue 消息队列 用于存放所有handler发送的消息，这部分消息会一直存在于消息队列。每个线程只会有一个messagequeue
4.是消息队列的管家，调用loop()方法之后就会进入一个无限的循环之中，发现有消息
就会取出然后传递到handler的hanlemessage()方法中，每个线程只有一个Looper对象

2.AsyncTask
  
  

