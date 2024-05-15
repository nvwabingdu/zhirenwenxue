package com.example.zrlearn.a_android.jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.zrframe.bean.User

/**
 * @Author qiwangi
 * @Date 2023/7/26
 * @TIME 14:49
 */
class Test2ViewModel(countReserved:Int):ViewModel() {
    /**构造传参给counter*/
    var counter=countReserved


    /**
     * 1.通过LiveData实现数据共享 主动通知Activity
     * livedata的特点：
     * 1.数据改变时，只会通知处于活跃状态的观察者，且只会通知一次，且只会通知最新的数据
     * 2，使用数据读写的方式   getvalue() setvalue()(仅能主线程中调用) postvalue()(子线程中调用)
     *
     * ps:如果想要在子线程中调用setvalue()，可以使用postvalue()，postvalue()内部也是通过handler实现的
     */
    var sum=MutableLiveData<Int>()

    init{
      sum.value=countReserved
    }

    fun plusOne(){
        val count=sum.value?:0
        sum.value=count+1
    }

    fun clear(){
        sum.value=0// getvalue() setvalue()的语法糖
    }


    /**
     * 更加完美的写法  官方推荐 模板代码
     */
    val sum2:LiveData<Int> get() = _sum2

    private val _sum2=MutableLiveData<Int>()

    init {
        _sum2.value=countReserved
    }

    private fun plusOne2(){
        val count=_sum2.value?:0
        _sum2.value=count+1
    }

    private fun clear2(){
        _sum2.value=0
    }


    /**
     * map和switchmap的使用
     * map：将一个LiveData转换成另一个LiveData  比如一个user类只需要年龄这个字段 其他的都不用
     * switchmap：viewmodel中的某个livedata对象是调用另外的方法获得
     */
    data class MyUser(val name:String,val age:Int)


    private val userLiveData=MutableLiveData<MyUser>()
    val userName:LiveData<String> =Transformations.map(userLiveData){
            user->user.name+"年龄"+user.age
    }

//    val userName:LiveData<String> =userLiveData.map {
//        user->user.name
//    }


    /***
     * swichmap的使用  假设通过别的地方获取到的livedata对象
     */
    object Repository{
        fun getUser(userId:String):LiveData<User>{
            val liveData=MutableLiveData<User>()
            //模拟网络请求
            liveData.value= User(1,"zhangsan","lisi",null)
            return liveData
        }

        //自己写的
        fun refresh():LiveData<Any>{
            val liveData=MutableLiveData<Any>()
            //模拟网络请求
            liveData.value= Any()
            return liveData
        }
    }


    /**
     * 此时 activity中不能通过  viewmodel2.sum.observe(this, Observer {//逻辑    })这种方式获取，不然每次获取的新的livedata实例
     */
    fun getUser(userId:String):LiveData<User>{
        return Repository.getUser(userId)
    }

    /**
     * 使用switchmap转换一下
     */
    private val userIdLiveData=MutableLiveData<String>()

    val user:LiveData<User> =Transformations.switchMap(userIdLiveData){
        userId->
        Repository.getUser(userId)
    }

    fun getUser2(userId:String){
        userIdLiveData.value=userId
    }

    /**
     * 没有可观察数据的情况下
     */
    private val refreshLiveData=MutableLiveData<Any>()

    val refreshResult:LiveData<Any> =Transformations.switchMap(refreshLiveData){
        Repository.refresh()//假设这个方法返回的是一个livedata对象 假设Repository已经定义了次方法
    }

    fun refresh(){
        refreshLiveData.value=refreshLiveData.value//这里只是为了触发livedata的value值改变 调用value的set方法 从而触发switchmap的转换
    }

















}