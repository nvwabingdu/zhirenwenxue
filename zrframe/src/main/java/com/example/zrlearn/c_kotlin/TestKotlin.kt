//package com.example.zrlearn.c_kotlin
//import kotlinx.coroutines.*
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.channels.ReceiveChannel
//import kotlinx.coroutines.channels.produce
//import kotlinx.coroutines.flow.*
//import kotlin.system.measureTimeMillis
//
///**
// * Created by Android Studio.
// * User: 86182
// * Date: 2022-11-05
// * Time: 13:14
// */
//
//
//suspend fun main() {
//
//}
//
//
///**
// * 1.协程概述
// */
//fun tips_1() {
//// suspend fun coroutines(){
/////协程:提供了一种避免阻塞线程并且更简单，更可控的操作替代线程阻塞的方法：协程挂起 ==异步+回调？
////协程可以被挂起而无需阻塞线程，几乎无代价，不需要上下文切换，或者os的任何其他干预  可以在挂起的时候，做一些时间拦截，或者记录日志的功能
//
////协程的挂起 可以由我们用户来控制
////launch参数有三个入参：content     start         block
////                   协程上下文    协程启动选项    block是真正要执行的代码，必须是suepend修饰的挂起函数
//
////launch:返回一个job类型的，job是协程创建的后台持有的该协程的引用（job接口实际上是继承自CorutineContext类型）
////一个job有三种状态
////           new      新建    可选的初始状态
////           active   活动中
////           complete 完成
//
////delay（）:类似于thread.sleep（）  但更好的是，它不会阻塞线程，而是挂起协程本身，当协程等待时，线程会回到池中，当等待完成时,协程将在空闲的线程上恢复
//
////commonpool对象:代表共享线程池 主要作用是调度计算密集型任务时的协程的执行
////delay（3000l，timeUnit.milliseconds） 用suspend关键字修饰的，我们称之为挂起函数，挂起函数只能在协程代码内部调用，普通的非协程代码不能调用
//
////job.cancel  取消协程.但是和线程一般  并不能实际取消？
//
////通过两种方式取消协程
////1.显式检查协程代码的取消方式
////if(!isActive){ return@launch}
//
////2.循环调用一个挂起函数yield（）
////循环中直接使用  yield()函数   抛异常的方式取消？
//
////3.当取消协程时   finally{}代码块依然会执行  不需要的话  可以加入 delay(1000L)   就不会执行
//
////4.finally{
////run(Noncancellable){我是必然会执行的方法}
//
////5.设置超时时间
////WithTimeOUt(3000L){
////repeat(100){i -> print("dadadada") delay(500L)}}
//
////挂起函数
////while（10000）{new Thread 打印 1221}  //内存溢出  线程
////while（10000）{launch 打印 1221}  //正常执行      协程
//
////协程和线程的区别？
////协程是编译器级别                                                                                  线程是操作系统级别的
////协程是非抢占式的（需要用户自己释放使用权来切换到其他协程，因此同一时间只有一个协程拥有运行权，相当于单线程？？？）     线程是抢占式的
//
////协程的好处： 1.cpu低消耗 2.简化了多线程同步的复杂性  3.摆脱一部编程的一堆callback函数
//}
//
///**
// * 2.GlobalScope进程级别的协程 （在使用之前需要添加依赖）
// *
// * GlobalScope是生命周期是process级别的，即使Activity或Fragment已经被销毁，协程仍然在执行。所以需要绑定生命周期。
// * lifecycleScope只能在Activity、Fragment中使用，会绑定Activity和Fragment的生命周期
// * viewModelScope只能在ViewModel中使用，绑定ViewModel的生命周期
// *
// * 协程依赖： implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'
// */
//fun tips_2() {
//    GlobalScope.launch { // 在后台启动一个新的协程并继续
//        delay(1000L) //非阻塞
//        println("World!")
//    }
//    println("Hello,") // 主线程中的代码会立即执行
//    runBlocking {     // 但是这个表达式阻塞了主线程 调用了 runBlocking 的主线程会一直 阻塞 直到 runBlocking 内部的协程执行完毕。
//        delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
//    }
//}
//
///**
// * 3.通过runBlocking函数来包裹main函数
// */
//fun tips_3() {
//    fun main3() = runBlocking<Unit> { // 开始执行主协程
//        GlobalScope.launch { // 在后台启动一个新的协程并继续
//            delay(1000L)
//            println("World!")
//        }
//        println("Hello,") // 主协程在这里会立即执行
//        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
//    }
//}
//
///**
// *4.不通过join而是通过在runBlocking 作用域中启动新协程的方式 让协程自动取消
// */
//suspend fun tips_4() {
//    //suspend表示为挂起函数
//    //runBlocking{}协程构建器
//    class MyTest {
//        @Test
//        fun testMySuspendingFunction() = runBlocking<Unit> {
//            // 这里我们可以使用任何喜欢的断言（结果为真，还是为假，为真继续执行）风格来使用挂起函数
//        }
//    }
//    //等待一个作业
//    //延迟一段时间来等待另一个协程运行并不是一个好的选择。让我们显式（以非阻塞方式）等待所启动的后台 Job 执行结束：
//    val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
//        delay(1000L)
//        println("World!")
//    }
//    println("Hello,")
//    job.join() // 等待直到子协程执行结束
//
//    /**
//     * 简化版 通常采用此种模式
//     */
//    fun main4() = runBlocking { // this: CoroutineScope
//        launch { // 在 runBlocking 作用域中启动一个新协程
//            delay(1000L)
//            println("World!")
//        }
//        println("Hello,")
//    }
//}
//
///**
// * 5.作用域构建器   这里记一下运行顺序
// * 除了由不同的构建器提供协程作用域之外，还可以使用 coroutineScope 构建器声明自己的作用域。
// * 它会创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束。
// * runBlocking 与 coroutineScope 可能看起来很类似，因为它们都会等待其协程体以及所有子协程结束。
// * 主要区别在于，runBlocking 方法会阻塞当前线程来等待，
// * 而 coroutineScope 只是挂起， 会释放底层线程用于其他用途。 由于存在这点差异，runBlocking 是常规函数，
// * 而 coroutineScope 是挂起函数。
// */
//fun tips_5() {
//    fun main5() = runBlocking { // this: CoroutineScope
//        launch {
////            delay(200L)
//            println("2")
//        }
//        coroutineScope { // 创建一个协程作用域
//            launch {
////                delay(500L)
//                println("3")
//            }
////            delay(100L)
//            println("1") // 这一行会在内嵌 launch 之前输出
//        }
//        println("4") // 这一行在内嵌 launch 执行完毕后才输出
//    }
//}
//
///**
// * 6.在launch中写普通函数
// */
//fun tips_6() {
//    fun main6() = runBlocking {
////        launch { doWorld() }
//        println("Hello,")
//    }
//
//    // 这是你的第一个挂起函数
//    suspend fun doWorld() {
//        delay(1000L)
//        println("World!")
//    }
//}
//
///**
// * 7.启动大量协程repeat
// */
//fun tips_7() {
//    fun main() = runBlocking {
//        repeat(100_000) { // 启动大量的协程
//            launch {
//                delay(5000L)
//                print(".")
//            }
//        }
//    }
//}
//
///**
// * 8.关于协程的取消和等待结束
// */
//suspend fun tips_8() {
//    val job = GlobalScope.launch {
//        repeat(1000) { i ->
//            println("job: I'm sleeping $i ...")
//            delay(500L)
//        }
//    }
//    delay(1300L) // 延迟一段时间
//    println("main: I'm tired of waiting!")
////    job.cancel() // 取消该作业
////    job.join() // 等待作业执行结束
//    job.cancelAndJoin()//取消并等待结束
//    println("main: Now I can quit.")
//}
//
///**
// * 9.取消循环中的协程  一般还是让其正常结束
// */
//suspend fun tips_9() {
//    val startTime = System.currentTimeMillis()
//    val job = GlobalScope.launch(Dispatchers.Default) {
//        var nextPrintTime = startTime
//        var i = 0
////        while (i < 8) { // 一个执行计算的循环，只是为了占用 CPU 此种方式即使取消了 也会继续运行
//        while (isActive) { // 一个执行计算的循环，只是为了占用 CPU     改为isActive之后只打印了三次
//            // 每秒打印消息两次
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                println("job: I'm sleeping ${i++} ...")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    delay(1300L) // 等待一段时间
//    println("main: I'm tired of waiting!")
//    job.cancelAndJoin() // 取消一个作业并且等待它结束
//    println("main: Now I can quit.")
//}
//
///**
// * 10.在 finally 中释放资源
// */
//suspend fun tips_10() {
//    val job = GlobalScope.launch {
//        try {
//            repeat(1000) { i ->
//                println("job: I'm sleeping $i ...")
//                delay(500L)
//            }
//        } finally {
//            println("job: I'm running finally 资源释放写这里")
//        }
//    }
//    delay(1300L) // 延迟一段时间
//    println("main: I'm tired of waiting!")
//    job.cancelAndJoin() // 取消该作业并且等待它结束
//    println("main: Now I can quit.")
//}
//
///**
// * 11.通过超时来处理协程
// */
//suspend fun tips_11() {
//    /**
//     * 通过超时来处理协程
//     */
//    withTimeout(1300L) {
//        repeat(1000) { i ->
//            println("I'm sleeping $i ...")
//            delay(500L)
//        }
//    }
//
//    /**
//     * 通过超时来设定协程
//     */
//    val result = withTimeoutOrNull(1300L) {
//        repeat(1000) { i ->
//            println("I'm sleeping $i ...")
//            delay(500L)
//        }
//        print("构思的") //不会打印
//        "Done" // 不会打印 在它运行得到结果之前取消它
//    }
//    println("Result is $result")
//}
//
///**
// * 12.组合挂起函数  顺序执行，和并发执行
// */
//suspend fun tips_12() {
//    /**
//     * 顺序执行
//     */
////    val time = measureTimeMillis {
////        val one = funOne()
////        val two = funTwo()
////        println("The answer is ${one + two}")
////    }
////    println("Completed in $time ms")
//
//    /**
//     * 并发执行
//     * （速度快一倍）没有依赖的情况下
//     * 这就是 async 可以帮助我们的地方。
//     * 在概念上，async 就类似于 launch。
//     * 它启动了一个单独的协程，
//     * 这是一个轻量级的线程并与其它所有的协程一起并发的工作。
//     * launch：返回一个 Job 并且不附带任何结果值，
//     * async： 返回一个 Deferred —— 一个轻量级的非阻塞 future，
//     * 这代表了一个将会在稍后提供结果的 promise。
//     * 你可以使用 .await() 在一个延期的值上得到它的最终结果，
//     * 但是 Deferred 也是一个 Job，所以如果需要的话，你可以取消它。
//     */
//    GlobalScope.launch {
//        val time2 = measureTimeMillis {
//            val one = async { funOne() }
//            val two = async { funTwo() }
//            println("The answer is ${one.await() + two.await()}")
//        }
//        println("Completed in $time2 ms")
//    }
//    runBlocking {//必须加这个阻塞不然上面代码不会运行
//        delay(3000)
//    }
//}
//
//suspend fun funOne(): Int {
//    delay(1000L) // 假设我们在这里做了一些有用的事
//    return 13
//}
//
//suspend fun funTwo(): Int {
//    delay(1000L) // 假设我们在这里也做了一些有用的事
//    return 29
//}
//
///**
// * 13.关于并发执行的高阶操作   CoroutineStart.LAZY  惰性启动
// * async与launch一样都是开启一个协程，
// *
// * async：返回一个Deferred对象，该Deferred也是一个job  async函数类似于 launch函数.它启动了一个单独的协程,这是一个轻量级的线程并与其它所有的协程一起并发的工作.
// * launch：返回一个 Job 并且不附带任何结果值
// *
// * 而 async 返回一个 Deferred —— 一个轻量级的非阻塞 future,这代表了一个将会在稍后提供结果的 promise.
// * 你可以使用 .await() 在一个延期的值上得到它的最终结果， 但是 Deferred 也是一个 Job
// */
//suspend fun tips_13() {
//    GlobalScope.launch {
//        val time = measureTimeMillis {
//            val one = async(start = CoroutineStart.LAZY) { funOne() }
//            val two = async(start = CoroutineStart.LAZY) { funTwo() }
//            // 执行一些计算
//            one.start() // 启动第一个
//            two.start() // 启动第二个
//            println("The answer is ${one.await() + two.await()}")
//        }
//        println("Completed in $time ms")
//    }
//
//    /**
//     * 安全性的
//     */
//    suspend fun concurrentSum(): Unit = coroutineScope {
//        val one = async(CoroutineName("v2coroutine")) { funOne() }//可以加上名字用于调试
//        val two = async(Dispatchers.Default + CoroutineName("test")) { funTwo() }//用+加上调度器和名字
//        one.await() + two.await()
//    }
//
//    val time = measureTimeMillis {
//        println("The answer is ${concurrentSum()}")
//    }
//    println("Completed in $time ms")
//
//
//}
//
///**
// * 14.协程调度器
// * 当协程在 GlobalScope 中启动时，使用的是由 Dispatchers.Default
// */
//fun tips_14() {
//    GlobalScope.launch { // 运行在父协程的上下文中，即 runBlocking 主协程
//        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
//        delay(1000)
//        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
//    }
//    GlobalScope.launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
//        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
//        delay(500)
//        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
//    }
//    GlobalScope.launch(Dispatchers.Default) { // 将会获取默认调度器
//        println("Default               : I'm working in thread ${Thread.currentThread().name}")
//    }
//    GlobalScope.launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
//        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
//    }
//
//
//    /**
//     * 上面仅仅是协程launch， 不是GlobalScope.launch
//     *     Unconfined            : I'm working in thread main
//    Default               : I'm working in thread DefaultDispatcher-worker-1
//    newSingleThreadContext: I'm working in thread MyOwnThread
//    main runBlocking      : I'm working in thread main
//     */
//
//}
//
///**
// *15.协程的调试
// *     用 IDEA 调试
// *     Kotlin 插件的协程调试器简化了 IntelliJ IDEA 中的协程调试.
// *     调试适用于 1.3.8 或更高版本的 kotlinx-coroutines-core。
// */
//fun tips_15() {
//    /**
//     *  1.使用协程调试器，你可以：
//     *  检查每个协程的状态。
//     *  查看正在运行的与挂起的的协程的局部变量以及捕获变量的值。
//     *  查看完整的协程创建栈以及协程内部的调用栈。栈包含所有带有变量的栈帧，甚至包含那些在标准调试期间会丢失的栈帧。
//     *  获取包含每个协程的状态以及栈信息的完整报告。要获取它，请右键单击 Coroutines 选项卡，然后点击 Get Coroutines Dump。
//     *  要开始协程调试，你只需要设置断点并在调试模式下运行应用程序即可。
//     */
//
//    /**
//     *  2.用日志调试
//     *  另一种调试线程应用程序而不使用协程调试器的方法
//     *  是让线程在每一个日志文件的日志声明中打印线程的名字。
//     *  这种特性在日志框架中是普遍受支持的。但是在使用协程时，
//     *  单独的线程名称不会给出很多协程上下文信息，所以 kotlinx.coroutines 包含了调试工具来让它更简单。
//     */
//}
//
///**
// * 16.协程和生命周期绑定 下面提出关键代码
// */
//fun tips_16() {
//    val mainScope = MainScope()
//    fun destroy() {
//        mainScope.cancel()
//    }
//
//    // 在 Activity 类中
//    fun doSomething() {
//        // 在示例中启动了 10 个协程，且每个都工作了不同的时长
//        repeat(10) { i ->
//            mainScope.launch {
//                delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
//                println("Coroutine $i is done")
//            }
//        }
//    }
//
////    val activity = Activity()
////    activity.doSomething() // 运行测试函数
////    println("Launched coroutines")
////    delay(500L) // 延迟半秒钟
////    println("Destroying activity!")
////    activity.destroy() // 取消所有的协程
////    delay(1000) // 为了在视觉上确认它们没有工作
//}
//
///**
// * 17.异步流flow
// * 名为 flow 的 Flow 类型构建器函数。flow { ... } 构建块中的代码可以挂起。
// * 流使用 emit 函数 发射 值。
// * 流使用 collect 函数 收集 值。
// *
// * 我们可以在 simple 的 flow { ... } 函数体内使用 Thread.sleep 代替 delay 以观察主线程在本案例中被阻塞了。
// * 流是冷的  Flow 是一种类似于序列的冷流 — 这段 flow 构建器中的代码直到流被收集的时候才运行。这在以下的示例中非常明显：
// */
//suspend fun tips_17() {
//    fun simple(): Flow<Int> = flow {
//        println("Flow started")
//        for (i in 1..3) {
//            delay(100)
//            emit(i)
//        }
//    }
//
//    fun main() = runBlocking<Unit> {
//        println("Calling simple function...")
//        val flow = simple()
//        println("Calling collect...")
//        flow.collect { value -> println(value) }
//        println("Calling collect again...")
//        flow.collect { value -> println(value) }
//    }
//
//
//    /**
//     * 简单的demo
//     */
//    fun simple17(): Flow<Int> = flow { // 流构建器
//        for (i in 1..3) {
//            delay(100) // 假装我们在这里做了一些有用的事情
//            emit(i) // 发送下一个值
//        }
//    }
//
//    fun main17() = runBlocking<Unit> {
//        // 启动并发的协程以验证主线程并未阻塞
//        launch {
//            for (k in 1..3) {
//                println("I'm not blocked $k")
//                delay(100)
//            }
//        }
//        // 收集这个流
//        simple().collect { print(it) }
//    }
//
//    /**
//     *   流取消   流采用与协程同样的协作取消。
//     *   像往常一样，流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消。
//     *   以下示例展示了当 withTimeoutOrNull 块中代码在运行的时候流是如何在超时的情况下取消并停止执行其代码的：
//     */
//    fun simple18(): Flow<Int> = flow {
//        for (i in 1..3) {
//            delay(100)
//            println("Emitting $i")
//            emit(i)
//        }
//    }
//
//    fun main18() = runBlocking<Unit> {
//        withTimeoutOrNull(250) { // 在 250 毫秒后超时
//            simple().collect { value -> println(value) }
//        }
//        println("Done")
//    }
//
//    /**
//     * 使用一个整数区间的打印流
//     */
//    (1..3).asFlow().collect { value -> println(value) }
//
//
//    /**
//     *基础的操作符拥有相似的名字，比如 map 与 filter。
//     * 流与序列的主要区别在于这些操作符中的代码可以调用挂起函数。
//     * 举例来说，一个请求中的流可以使用 map 操作符映射出结果，
//     * 即使执行一个长时间的请求操作也可以使用挂起函数来实现：
//     */
//    suspend fun performRequest(request: Int): String {
//        delay(1000) // 模仿长时间运行的异步工作
//        return "response $request"
//    }
//
//    fun main19() = runBlocking<Unit> {
//        (1..3).asFlow() // 一个请求流
////            .filter {   }
//            .map { request -> performRequest(request) }
//            .collect { response -> println(response) }
//    }
//
//
//    /**
//     * 在流转换操作符中，最通用的一种称为 transform。它可以用来模仿简单的转换，
//     * 例如 map 与 filter，以及实施更复杂的转换。
//     * 使用 transform 操作符，我们可以 发射 任意值任意次。
//     * 比如说，使用 transform 我们可以在执行长时间运行的异步请求之前发射一个字符串并跟踪这个响应：
//     */
//    (1..3).asFlow() // 一个请求流
//        .transform { request ->
//            emit("Making request $request")
//            emit(performRequest(request))
//        }
//        .collect { response -> println(response) }
//
//    /**
//     * 限长操作符
//     * 限长过渡操作符（例如 take）在流触及相应限制的时候会将它的执行取消。
//     * 协程中的取消操作总是通过抛出异常来执行，
//     * 这样所有的资源管理函数（如 try {...} finally {...} 块）会在取消的情况下正常运行：
//     */
//    fun numbers(): Flow<Int> = flow {
//        try {
//            emit(1)
//            emit(2)
//            println("This line will not execute")
//            emit(3)
//        } finally {
//            println("Finally in numbers")
//        }
//    }
//
//    fun main20() = runBlocking<Unit> {
//        numbers()
//            .take(2) // 只获取前两个emit
//            .collect { value -> println(value) }
//    }
//
//    /**
//     * 末端流操作符
//     * 末端操作符是在流上用于启动流收集的挂起函数。 collect 是最基础的末端操作符，但是还有另外一些更方便使用的末端操作符：
//     * 转化为各种集合，例如 toList 与 toSet。
//     * 获取第一个（first）值与确保流发射单个（single）值的操作符。
//     * 使用 reduce 与 fold 将流规约到单个值。
//     * 举例来说：
//     */
//    val sum = (1..5).asFlow()
//        .map { it * it } // 数字 1 至 5 的平方
//        .reduce { a, b -> a + b } // 求和（末端操作符）
//    println(sum)
//
//    /**
//     * 过滤偶数
//     */
//    (1..5).asFlow()
//        .filter {
//            println("Filter $it")
//            it % 2 == 0
//        }
//        .map {
//            println("Map $it")
//            "string $it"
//        }.collect {
//            println("Collect $it")
//        }
//
//    /**
//     * 流的该属性称为 上下文保存
//     * 所以默认的，flow { ... } 构建器中的代码运行在相应流的收集器提供的上下文中。
//     * 举例来说，考虑打印线程的一个 simple 函数的实现， 它被调用并发射三个数字：
//     */
//    fun simple21(): Flow<Int> = flow {
//        println("Started simple flow")
//        for (i in 1..3) {
//            emit(i)
//        }
//    }
//
//    fun main21() = runBlocking<Unit> {
//        simple().collect { value -> println("Collected $value") }
//    }
//
//    /**
//     *  flowOn 操作符
//     *  例外的是 flowOn 函数，该函数用于更改流发射的上下文。
//     *  以下示例展示了更改流上下文的正确方法，
//     *  该示例还通过打印相应线程的名字以展示它们的工作方式：
//     */
//
//    fun simple22(): Flow<Int> = flow {
//        for (i in 1..3) {
//            Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
//            println("Emitting $i")
//            emit(i) // 发射下一个值
//        }
//    }.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式
//
//    fun main22() = runBlocking<Unit> {
//        simple22().collect { value ->
//            println("Collected $value")
//        }
//    }
//
//    /**
//     *使用buffer缓冲发射 无需等待  （比如发射一个需要100 接受一个需要300   ）
//     */
//    val time = measureTimeMillis {
//        simple()
//            .buffer() // 缓冲发射项，无需等待
//            .collect { value ->
//                delay(300) // 假装我们花费 300 毫秒来处理它
//                println(value)
//            }
//    }
//
//    /**
//     *  合并
//     *  当流代表部分操作结果或操作状态更新时，
//     *  可能没有必要处理每个值，而是只处理最新的那个。
//     *  在本示例中，当收集器处理它们太慢的时候，
//     *  conflate 操作符可以用于跳过中间值。构建前面的示例：
//     */
//
//    val time23 = measureTimeMillis {
//        simple()
//            .conflate() // 合并发射项，不对每个值进行处理
//            .collect { value ->
//                delay(300) // 假装我们花费 300 毫秒来处理它
//                println(value)
//            }
//    }
//    println("Collected in $time23 ms")
//
//
//    /**
//     * 当发射器和收集器都很慢的时候，合并是加快处理速度的一种方式。
//     * 它通过删除发射值来实现。
//     * 另一种方式是取消缓慢的收集器，
//     * 并在每次发射新值的时候重新启动它。
//     * 有一组与 xxx 操作符执行相同基本逻辑的 xxxLatest 操作符，
//     * 但是在新值产生的时候取消执行其块中的代码。
//     * 让我们在先前的示例中尝试更换 conflate 为 collectLatest：
//     */
//    val time24 = measureTimeMillis {
//        simple()
//            .collectLatest { value -> // 取消并重新发射最后一个值
//                println("Collecting $value")
//                delay(300) // 假装我们花费 300 毫秒来处理它
//                println("Done $value")
//            }
//    }
//    println("Collected in $time24 ms")
//
//    /**
//     * Zip
//     * 就像 Kotlin 标准库中的 Sequence.zip 扩展函数一样， 流拥有一个 zip 操作符用于组合两个流中的相关值：
//     */
//
//    val nums = (1..3).asFlow() // 数字 1..3
//    val strs = flowOf("one", "two", "three") // 字符串
//    nums.zip(strs) { a, b -> "$a -> $b" } // 组合单个字符串
//        .collect { println(it) } // 收集并打印
//
//    /**
//     * Combine  联合 组合
//     * 当流表示一个变量或操作的最新值时（请参阅相关小节 conflation），
//     * 可能需要执行计算，这依赖于相应流的最新值，并且每当上游流产生值的时候都需要重新计算。
//     * 这种相应的操作符家族称为 combine。
//     * 例如，先前示例中的数字如果每 300 毫秒更新一次，但字符串每 400 毫秒更新一次，
//     * 然后使用 zip 操作符合并它们，但仍会产生相同的结果， 尽管每 400 毫秒打印一次结果：
//     * 我们在本示例中使用 onEach 过渡操作符来延时每次元素发射并使该流更具说明性以及更简洁。
//     */
//
//    val nums1 = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
//    val strs1 = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
//    val startTime1 = System.currentTimeMillis() // 记录开始的时间
//    nums1.zip(strs1) { a, b -> "$a -> $b" } // 使用“zip”组合单个字符串
//        .collect { value -> // 收集并打印
//            println("$value at ${System.currentTimeMillis() - startTime1} ms from start")
//        }
//
//    // 然而，当在这里使用 combine 操作符来替换 zip：
//    val nums2 = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
//    val strs2 = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
//    val startTime2 = System.currentTimeMillis() // 记录开始的时间
//    nums2.combine(strs2) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串
//        .collect { value -> // 收集并打印
//            println("$value at ${System.currentTimeMillis() - startTime2} ms from start")
//        }
//    //我们得到了完全不同的输出，其中，nums 或 strs 流中的每次发射都会打印一行：
//
//
//    /**
//     * 展平流  （Flow<Flow<String>>） 类似于这个flatten函数的作用
//     */
//    fun requestFlow(i: Int): Flow<String> = flow {
//        emit("$i: First")
//        delay(500) // 等待 500 毫秒
//        emit("$i: Second")
//    }
//    //1.连接模式由 flatMapConcat 与 flattenConcat 操作符实现
//    val startTime = System.currentTimeMillis() // 记录开始时间
//    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
//        .flatMapConcat { requestFlow(it) }
//        .collect { value -> // 收集并打印
//            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
//        }
//
//    //2.它由 flatMapMerge 与 flattenMerge 操作符实现。
//    val startTime23 = System.currentTimeMillis() // 记录开始时间
//    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
//        .flatMapMerge { requestFlow(it) }
//        .collect { value -> // 收集并打印
//            println("$value at ${System.currentTimeMillis() - startTime23} ms from start")
//        }
//
//    //3.flatMapLatest
//    //    与 collectLatest 操作符类似（在"处理最新值" 小节中已经讨论过），
//    //    也有相对应的“最新”展平模式，在发出新流后立即取消先前流的收集。
//    //    这由 flatMapLatest 操作符来实现。
//    val startTime24 = System.currentTimeMillis() // 记录开始时间
//    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
//        .flatMapLatest { requestFlow(it) }
//        .collect { value -> // 收集并打印
//            println("$value at ${System.currentTimeMillis() - startTime24} ms from start")
//        }
//
//
//}
//
///**
// * 流异常
// */
//suspend fun tips_18() {
//    fun simple(): Flow<Int> = flow {
//        for (i in 1..3) {
//            println("Emitting $i")
//            emit(i) // 发射下一个值
//        }
//    }
//
//    fun main18() = runBlocking<Unit> {
//        try {
//            simple().collect { value ->
//                println(value)
//                check(value <= 1) { "Collected $value" }
//            }
//        } catch (e: Throwable) {
//            println("Caught $e")
//        }
//    }
//    /**
//     * 声明式捕获
//     */
//    simple()
//        .onEach { value ->
//            check(value <= 1) { "Collected $value" }
//            println(value)
//        }
//        .catch { e -> println("Caught $e") }
//        .collect()
//
//    /**
//     *  与 catch 操作符的另一个不同点是
//     *  onCompletion 能观察到所有异常并且仅在上游流成功完成（没有取消或失败）的情况下接收一个 null 异常。
//     *  onCompletion 操作符与 catch 不同，
//     *  它不处理异常。我们可以看到前面的示例代码，
//     *  异常仍然流向下游。它将被提供给后面的 onCompletion 操作符，并可以由 catch 操作符处理
//     */
//
//
//    fun simple19(): Flow<Int> = (1..3).asFlow()
//
//    fun main() = runBlocking<Unit> {
//        simple19()
//            .onCompletion { cause -> println("Flow completed with $cause") }
//            .collect { value ->
//                check(value <= 1) { "Collected $value" }
//                println(value)
//            }
//    }
//}
//
///**
// * 19.如果我们在 onEach 之后使用 collect 末端操作符，那么后面的代码会一直等待直至流被收集：
// *  onEach { ... }.launchIn(scope) 成对
// */
//fun tips_19() {
//
//    // 模仿事件流
//    fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }
//
//    fun main() = runBlocking<Unit> {
//        events()
//            .onEach { event -> println("Event: $event") }
////            .collect() // <--- 等待流收集
//            .launchIn(this) // <--- 在单独的协程中执行流
//        println("Done")
//    }
//
//}
//
///**
// * 20.流取消检测
// * 为方便起见，流构建器对每个发射值执行附加的 ensureActive
// * 检测以进行取消。 这意味着从 flow { ... } 发出的繁忙循环是可以取消的：
// */
//fun tips_20() {
//    fun foo(): Flow<Int> = flow {
//        for (i in 1..5) {
//            println("Emitting $i")
//            emit(i)
//        }
//    }
//
//    /**
//     * cancel()
//     */
//    fun main() = runBlocking<Unit> {
//        foo().collect { value ->
//            if (value == 3) cancel()
//            println(value)
//        }
//    }
//
//    /**
//     *   在协程处于繁忙循环的情况下，必须明确检测是否取消 cancellable()
//     */
//    fun main2() = runBlocking<Unit> {
//        (1..5).asFlow().cancellable().collect { value ->
//            if (value == 3) cancel()
//            println(value)
//        }
//    }
//
//}
//
//fun CoroutineScope.produceNumbers() = produce<Int> {
//    var x = 1
//    while (true) send(x++) // 在流中开始从 1 生产无穷多个整数
//}
//
////并且另一个或多个协程开始消费这些流，做一些操作，并生产了一些额外的结果。 在下面的例子中，对这些数字仅仅做了平方操作：
//fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
//    for (x in numbers) send(x * x)
//}
//
///**
// * 21.通道
// * 延期的值提供了一种便捷的方法使单个值在多个协程之间进行相互传输。 通道提供了一种在流中传输值的方法。
// * 通道基础
// * 一个 Channel 是一个和 BlockingQueue 非常相似的概念。
// * 其中一个不同是它代替了阻塞的 put 操作并提供了挂起的 send，
// * 还替代了阻塞的 take 操作并提供了挂起的 receive。
// */
//suspend fun tips_21() {
//    val channel = Channel<Int>()
//    GlobalScope.launch {
//        // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
//        for (x in 1..5) channel.send(x * x)
//    }
//// 这里我们打印了 5 次被接收的整数：
//    repeat(5) { println(channel.receive()) }
//    println("Done!")
//
//    /**
//     * 关闭通道close
//     */
////    val channel = Channel<Int>()
////    GlobalScope.launch {
////        for (x in 1..5) channel.send(x * x)
////        channel.close() // 我们结束发送
////    }
////// 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
////    for (y in channel) println(y)
////    println("Done!")
//
//
//    /**
//     *  这里有一个名为 produce 的便捷的协程构建器，可以很容易的在生产者端正确工作，
//     *  并且我们使用扩展函数 consumeEach 在消费者端替代 for 循环：
//     */
////    val squares2 = produceSquares()
////    squares2.consumeEach { println(it) }
////    println("Done!")
//
//
////    val numbers = produceNumbers() // 从 1 开始生成整数
////    val squares = square(numbers) // 整数求平方
////    repeat(5) {
////        println(squares.receive()) // 输出前五个
////    }
////    println("Done!") // 至此已完成
////    coroutineContext.cancelChildren() // 取消子协程 取消所有的子协程来让主协程结束
//}
//
//
//
//
//
