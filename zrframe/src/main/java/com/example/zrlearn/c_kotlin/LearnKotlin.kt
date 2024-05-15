package com.example.zrlearn.c_kotlin

import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KProperty

/**
 * @Author qiwangi
 * @Date 2023/7/25
 * @TIME 09:55
 */
fun main1() {//旁边的箭头表示可以直接点击运行这行代码
    println("hello kotlin")//和安卓无关的打印 要用这种方式 不能用log的方式
}


/**
 * 主构造函数
 */
class Student(val age: Int, val grade: Int) {// 主构造函数只能有一个  可以没有主构造函数

    init {//想要在主构造函数中编写逻辑  就在这里写
        println("age is$age")
        println("grade is$grade")
    }

    constructor() : this(0, 0)//次级构造函数可以有多个

    companion object {
        @JvmStatic  //表示真正的静态方法  java和kotlin都可以调用   另外.kt文件中的方法 表示顶层方法
        fun setSomething() {
        }
    }


    /**
     * 延迟初始化
     */
    private lateinit var mStudent: Student
    fun method() {
        if (!::mStudent.isLateinit) {//判断是否初始化
            mStudent = Student()
        }
    }
}


/**
 * 接口
 */
//interface Fly {
//    fun fly()//必须实现的方法
//
//    fun jump() {//有方法体之后  可以不用实现
//        println("我会跳")
//    }
//}


/**
 * java和kotlin的单例对比
 */
/**
public class Singleton{
private  static Singleton instance;//私有静态成员变量
private Singleton(){}//私有构造方法
public synchronized static Singleton getInstance(){//公开的同步静态方法  同步判断如果创建了实例 则直接用
if (instance==null){
instace=new Singleton;
}
return instance;
}
}
 */

//kotlin的单例类   直接用object替换class
object Single {
    //方法
    //变量等
}


/**
 * 两种线程匿名内部类的写法
 */
/**
new Thread(new Runnable(){
@Override
public void run{

}
})
 */

fun method() {
    Thread(object : Runnable {
        //匿名内部类 因为舍弃new关键字  所以用object
        override fun run() {

        }
    })

    Thread { }//上面的简化版
}


/**
 * 高阶函数  标准函数
 */
fun method2(student: Student) {
    /**kotlin标准函数1  let*/
    student?.let { //let 标准函数一种  可以省略很多写法
        it.age
    }

    /**kotlin标准函数2  with*/
    //with 接收两个参数 第一个是任意类型的对象 第二个是lambda表达式
    val result = with(student) {
        //这个lambda里面的上下文都是student
        //最后一行表示返回值
    }

    /**kotlin标准函数3  run*/
    val result2 = student.run {
        //这里和with差不多了
        //最后一行表示返回值
    }

    /**kotlin标准函数4  apply*/
    val result4 = student.apply {
        //无法指定返回值 简单的说只能对其操作
    }
}


/**
 * 扩展函数 相当于   类.需要的方法  就可以啦
 */
fun String.sub(params1: String, params2: String) {
    //这里写操作
}


/**
 * 高阶函数
 */
fun method(p1: Int, p2: Int): Int {
    return p1 - p2
}

fun p1AndP2(a: Int, b: Int, method: (Int, Int) -> Int): Int {
    return method(a, b)//好了这就是尼玛的高阶函数
}

inline fun p1AndP2_(a: Int, b: Int, method: (Int, Int) -> Int): Int {
    return method(a, b)//好了这就是尼玛的高阶函数  内联方式 避免多余的匿名开销
}

//注意此种写法
//inline fun p1AndP2_(a:Int,b:Int,method2:Int.(Int,Int)->Int):Int{ //注意这里的Int.  表示第一个参数是Int类型的  ClassName.()这是正确的写法 表示在哪一个类中的方法
//    return method(a,b)//好了这就是尼玛的高阶函数  内联方式 避免多余的匿名开销
//}

fun test() {
    p1AndP2_(1, 2, ::method)//这里::method表示传递的是一个函数   ::表示函数的引用式写法

    p1AndP2_(1, 2, { p1, p2 -> p1 + p2 })//这里表示传递的是一个lambda表达式

    p1AndP2_(1, 2) { p1, p2 -> p1 + p2 }//这里表示传递的是一个lambda表达式  简化版
}

/**
 * 数据持久化  将文件保存在本地data/data/包名/files/
 */
fun save(mContext: Context, inputText: String) {
    try {
        val ouput =
            mContext.openFileOutput("data", Context.MODE_PRIVATE)//MODE_PRIVATE 替换  MODE_APPEND 追加
        val writer = BufferedWriter(OutputStreamWriter(ouput))
        writer.use {
            it.write(inputText)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

/**
 * 数据持久化  将保存在本地data/data/包名/files/的文件读取出来
 */
fun load(mContext: Context): String {
    val content = StringBuilder()
    try {
        val input = mContext.openFileInput("data")
        val reader = BufferedReader(InputStreamReader(input))
        reader.use {
            reader.forEachLine {
                content.append(it)
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return content.toString()
}


/**
 * 泛型方法  泛型类
 */

class One<T> {

    fun <T> method(params: T): T {//泛型方法
        return params
    }

    fun <T : Int> method(params: T): T {//指定上限的泛型方法
        return params
    }

}

class Two {
    fun foo() {
        ball<String>()
    }


    inline fun <T> ball() {
        //这里写操作
    }


    //被实化的泛型   满足两个条件即可  1.inline    2.reified
    inline fun <reified T> setBall() {
        //这里写操作
    }


    //泛型实化的牛逼之处
    fun main() {
        val result1 = setBall<String>()
        val result2 = setBall<Int>()
        println("result1 is $result1")
        println("result2 is $result2")
    }

    //泛型实化的使用例子
    inline fun <reified T> startActivity(context: Context) {
        val intent = Intent(context, T::class.java)
        context.startActivity(intent)
    }

    //泛型实化+高阶函数的跳转
    inline fun <reified T> startActivity(
        context: Context,
        block: Intent.() -> Unit
    ) {//这里的block:Intent.()->Unit  表示的是Intent类的方法  方便用apply
        val intent = Intent(context, T::class.java)
        intent.block()
        context.startActivity(intent)
    }

    //上诉使用方式
    //startActivity<MainActivity>(this){
    //            putExtra("key","value")
    //        }
}


/**
 * 类委托和属性委托
 */
class MySet<T>(val helperSet: HashSet<T>) : Set<T> by helperSet {

    override val size: Int
        get() = helperSet.size

    override fun isEmpty(): Boolean {
        return helperSet.isEmpty()
    }
}

//通过类委托实现  通过 by 实现类委托 效果和上面一致
class MySet2<T>(val helperSet: HashSet<T>) : Set<T> by helperSet {
    fun helloWorld() = println("hello world")

    override fun isEmpty() = false

    //其他set中的方法和hashset保持一致
}

/**
 * 属性委托
 */
class MyClass {
    var p by Delegate()


    //自己实现的延迟函数逻辑
    val p2 by later {//不知道这种写法对不对 之后测试
        10
        -19
    }

    //自己实现的延迟函数逻辑
    val p3 by later {
        val p3 = 10
        10
        108
        p3
    }

}

/**
 * 属性委托类的标准模板
 */
class Delegate {

    var propValue: Any? = null

    /**
     * 注意点：
     * 1.必须实现getValue和setValue方法 且必须是operator修饰的
     * 2.两个参数 第一个参数表示这个属性所在的类的对象  第二个参数表示这个属性的操作类 一般用不到
     */
    operator fun getValue(myClass: MyClass, prop: KProperty<*>): Any? {
        return propValue
    }

    /**
     * 和上面不同的是  第三个参数表示具体要赋值给委托属性的值  和上面getValue的返回值类型一致
     */
    operator fun setValue(myClass: MyClass, prop: KProperty<*>, value: Any?) {
        propValue = value
    }
}

/**
 * 属性委托的使用  写一个类似lazy的函数
 */
class Later<T>(val block: () -> T) {
    var value: Any? = null

    /**
     * 第一个参数指定为Any？表示希望在所有类中都可以使用
     */
    operator fun getValue(any: Any?, prop: KProperty<*>): T {
        if (value == null) {
            value = block()
        }
        return value as T
    }
}

fun <T> later(block: () -> T) = Later(block)


fun main2() {
    //协程作用域
//    GlobalScope.launch {
//        println("这是协程作用域，直接运行不会生效，因为协程会跟随应用程序一起销毁")
////        delay(1500)//延迟这个协程的执行时间  不会阻塞当前线程
//        println("这是协程作用域，直接运行不会生效，因为协程会跟随应用程序一起销毁 延迟后的代码")
//    }
//    Thread.sleep(1000)//阻塞当前线程   通过这句代码 让程序延迟一段时间 就可以运行正常打印上面那句话了

    //协程的作用域 会一直运行直到协程执行完毕 也就是说协程的作用域会一直运行阻塞当前线程并执行完毕  一般用于测试中 实际开发会影响性能？
//    runBlocking { //协程作用域
//
//
//        //luach1 luanch2 会同时执行  因为他们是在同一个协程作用域中  所以他们的执行顺序是不确定的
//
//        launch { //必须在协程作用域中执行   父协程执行结束  它也会跟着结束
//            println("luach1开始")
//            delay(1000)
//            println("launch1结束")
//        }
//
//        launch { //必须在协程作用域中执行   父协程执行结束  它也会跟着结束
//            println("launch2开始")
//            delay(1000)
//            println("launch2结束")
//        }


    val start = System.currentTimeMillis()
    runBlocking {
        repeat(100000) {
            launch {
                print(".")
            }
        }
        println("耗时：${System.currentTimeMillis() - start}")
    }


}

//协程的挂起函数 挂起函数之间可以互相调用  缺点只能将一个函数声明称挂起函数  无法提供一个作用域
suspend fun printDot() {
    print(".")
    delay(1000)
}


suspend fun printDot2() = coroutineScope {//和runblocking一样会保证作用域中的代码执行完毕
    launch {
        print(".")
        delay(1000)
    }
}

//混合使用
fun main3() {
    runBlocking {//和GlobalScope.launch(每次创建的都是顶层写成也不建议使用) 一样可以在任意地方调用 但是会阻塞当前线程 一般不建议实际项目中使用
        coroutineScope {//只能在协程作用域中使用 或者在挂起函数中使用
            launch {
                (0..10).forEach {
                    println(it)
                    delay(1000)
                }
            }
        }
        println("这里是runBlocking的代码 说明coroutineScope结束了")
    }
    println("这里是main函数的代码 说明runBlocking结束了")
}


fun main4() {
//    val job=GlobalScope.launch {
//        println("这是协程作用域，直接运行不会生效，因为协程会跟随应用程序一起销毁")
//    }
//    job.cancel()//通过job对象 取消协程


    //项目实操
    val job = Job()
    val scope = CoroutineScope(job)
    scope.launch {
        println("这是协程作用域，直接运行不会生效，因为协程会跟随应用程序一起销毁")
    }
    job.cancel()
}

fun main() {
//    runBlocking {
//        val result=async {//和launch一样会开启子协程 async函数会返回一个Deferred对象  通过await方法可以获取到async函数的返回值
//            delay(1000)
//            5+5
//        }.await()//await方法会阻塞当前线程 直到async函数执行完毕(或者说直到取得结果)
//    }


    //设计串行执行的协程
    runBlocking {
        val start = System.currentTimeMillis()
        val result1 = async {
            delay(1000)
            5 + 5
        }.await()
        val result2 = async {
            delay(1000)
            5 + 5
        }.await()
        println("result1+result2=${result1 + result2}")
        println("耗时：${System.currentTimeMillis() - start}")
    }


    //设计并行执行的协程
    runBlocking {
        val start = System.currentTimeMillis()
        val result1 = async {
            delay(1000)
            5 + 5
        }
        val result2 = async {
            delay(1000)
            5 + 5
        }
        println("result1+result2=${result1.await() + result2.await()}")
        println("耗时：${System.currentTimeMillis() - start}")//结果时间少一半
    }

    /**
     * 一般用这种方式
     * 1.Dispatchers.Default  默认低并发
     * 1.Dispatchers.IO       高并发
     * 1.Dispatchers.Main     安卓中使用 主线程中运行
     */
    runBlocking {
        //类似于 async{}.await()  但是必须指定一个线程参数  （所有作用域都可以指定线程参数 但是只有withcontent必须指定）
        val result =
            withContext(Dispatchers.Default) {//withContext函数会阻塞当前线程 直到withContext函数中的代码执行完毕
                delay(1000)
                5 + 5
            }


    }


    //通过协程简化匿名类的写法
    suspend fun test() {
        //suspendCoroutine必须在协程作用域，或者挂起函数中才能调用，并接收一个lambda表达式
        //主要作用是将当前协程挂起，然后在一个普通的线程中执行lambda表达式中的代码，
        // lambda表达式的参数列表上回传入一个Continuation参数，调用它的resume方法（或者resumeWithException()）可以让协程恢复执行
        suspendCoroutine<String> {

        }
    }


}
//通过协程简化匿名类的写法


//解构的解释
//Kotlin 解构(destructuring)的语法允许我们同时声明和初始化多个变量，以及从一个复合类型（如一个集合、一个类或一个函数）中提取多个值。
//解构声明的一般语法如下：
/*
val (var1, var2, ...) = expression
其中 val 是用于声明变量的关键字，var1, var2, ... 是要声明的变量名称，expression 是要进行解构的表达式。
例如，我们可以解构一个带有两个元素的列表：
val (a, b) = listOf(1, 2)
这样，变量 a 的值将为 1，变量 b 的值将为 2。

我们也可以用在函数调用时进行解构，例如：

fun myFunction(): Pair<String, Int> {
    return Pair("abc", 123)
}

val (str, num) = myFunction()
这样，变量 str 的值将为 "abc"，变量 num 的值将为 123。

我们也可以使用下划线 _ 表示我们不关心的变量，例如：
val (_, _, c) = triple
这里我们解构了一个三元组，但是我们只关心第三个元素，所以使用 _ 表示前两个元素我们不关心。

还有一种使用解构的方式是在 for 循环中，例如：
val map = mapOf("a" to 1, "b" to 2, "c" to 3)

for ((key, value) in map) {
    println("$key = $value")
}
这样，我们可以遍历 map 中的每个键值对，并使用解构来获取键和对应的值。
总结一下，Kotlin 解构语法允许我们同时声明和初始化多个变量，并且从一个复合类型中提取多个值。可以在变量声明、函数返回值、函数调用以及循环中使用解构。
*/

