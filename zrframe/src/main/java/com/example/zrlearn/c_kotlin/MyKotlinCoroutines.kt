package com.example.zrlearn.c_kotlin

import kotlinx.coroutines.*

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-11-25
 * Time: 16:52
 */

/**
 * 协程的简单应用
 */
suspend fun coroutines() {
    /*协程:提供了一种避免阻塞线程并且更简单，更可控的操作替代线程阻塞的方法：协程挂起
==异步+回调？

协程可以被挂起而无需阻塞线程，  几乎无代价，不需要上下文切换，或者os的任何其他干预  可以在挂起的时候，做一些时间拦截，或者记录日志的功能


协程的挂起 可以由我们由我们用户来控制


launch参数有三个入参，content start block
 协程上下文    协程启动选项   block是真正要执行的代码，必须是suepend修饰的挂起函数

launch函数返回一个job类型的，job是协程创建的后台任何，持有该协程的引用，   job接口实际上是继承自CorutineContext类型，
一个job有三种状态

new   新建  可选的初始状态
active  活动中
complete 完成

delay（）函数类似于thread.sleep（）  但更好的是，它不会阻塞线程，而是挂起协程本身，当协程等待时，线程会回到池中，当等待完成时，
协程将在空闲的线程上恢复

commonpool对象  代表共享线程池 主要作用是调度计算密集型任务时的协程的执行
delay（3000l，timeUnit.milliseconds）   用suspend关键字修饰的，我们称之为挂起函数，挂起函数只能在协程代码内部调用，普通的非协程代码不能调用

job.cancel  取消协程

但是和线程一般  并不能实际取消？
通过两种方式取消协程
1.显式检查协程代码的取消方式

if(!isActive){ return@launch}

2.循环调用一个挂起函数yield（）

循环中直接使用  yield()函数   抛异常的方式取消？


3.当取消协程时   finally{}代码块依然会执行  不需要的话  可以加入 delay(1000L)   就不会执行

4.finally{
run(Noncancellable){//我是必然会执行的方法}
}


5.设置超时时间

WithTimeOUt(3000L){
repeat(100){i -> print("dadadada") delay(500L)}
}



挂起函数

while（10000）{new Thread 打印 1221}  //内存溢出
while（10000）{launch 打印 1221}  //正常执行



协程式编译器级别     线程是操作系统级别的

线程是抢占式的  协程是非抢占式的 需要用户自己释放使用权来切换到其他协程，因此同一时间只有一个协程拥有运行权，相当于单线程？？？

协程的好处：
cpu低消耗
简化了多线程同步的复杂性
摆脱一部编程的一堆callback函数
*/
//    GlobalScope是生命周期是process级别的，即使Activity或Fragment已经被销毁，协程仍然在执行。所以需要绑定生命周期。
//    lifecycleScope只能在Activity、Fragment中使用，会绑定Activity和Fragment的生命周期
//    viewModelScope只能在ViewModel中使用，绑定ViewModel的生命周期


    //GlobalScope使用的是DefaultDispatcher，会自动切换到后台线程，不能做UI操作
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        println("World!") // 在延迟后打印输出
    }
    println("Hello,") // 协程已在等待时主线程还在继续
    Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活


    //第一个示例在同一段代码中混用了 非阻塞的 delay(……) 与 阻塞的 Thread.sleep(……)。
    // 这容易让我们记混哪个是阻塞的、哪个是非阻塞的。 让我们显式使用 runBlocking 协程构建器来阻塞：
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L)
        println("World!")
    }
    println("Hello,") // 主线程中的代码会立即执行
    runBlocking {     // 但是这个表达式阻塞了主线程
        delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
    }

    //结果是相似的，但是这些代码只使用了非阻塞的函数 delay。
    //调用了 runBlocking 的主线程会一直 阻塞 直到 runBlocking 内部的协程执行完毕。


    //    协程基础
    fun main1() {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
    }
//    代码运行的结果：
//    Hello,
//    World!
//    本质上，协程是轻量级的线程。 它们在某些 CoroutineScope 上下文中与 launch 协程构建器 一起启动。 这里我们在 GlobalScope 中启动了一个新的协程，
//    这意味着新协程的生命周期只受整个应用程序的生命周期限制。
//    可以将 GlobalScope.launch { …… } 替换为 thread { …… }，并将 delay(……) 替换为 Thread.sleep(……) 达到同样目的。
//    试试看（不要忘记导入 kotlin.concurrent.thread）。
//    如果你首先将 GlobalScope.launch 替换为 thread，编译器会报以下错误：
//    Error: Kotlin: Suspend functions are only allowed to be called from a coroutine or another suspend function

//    这是因为 delay 是一个特殊的 挂起函数 ，它不会造成线程阻塞，但是会 挂起 协程，并且只能在协程中使用。

    //    桥接阻塞与非阻塞的世界
//    第一个示例在同一段代码中混用了 非阻塞的 delay(……) 与 阻塞的 Thread.sleep(……)。 这容易让我们记混哪个是阻塞的、哪个是非阻塞的。
//    让我们显式使用 runBlocking 协程构建器来阻塞：
    fun main2() {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主线程中的代码会立即执行
        runBlocking {     // 但是这个表达式阻塞了主线程
            delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
        }
    }
//    结果是相似的，但是这些代码只使用了非阻塞的函数 delay。 调用了 runBlocking 的主线程会一直 阻塞
//    直到 runBlocking 内部的协程执行完毕。
//    这个示例可以使用更合乎惯用法的方式重写，使用 runBlocking 来包装 main 函数的执行：

    fun main3() = runBlocking<Unit> { // 开始执行主协程
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
    }

//    这里的 runBlocking<Unit> { …… } 作为用来启动顶层主协程的适配器。 我们显式指定了其返回类型 Unit，
//    因为在 Kotlin 中 main 函数必须返回 Unit 类型。
//    这也是为挂起函数编写单元测试的一种方式：


    class MyTest {
        @Test
        fun testMySuspendingFunction() = runBlocking<Unit> {
            // 这里我们可以使用任何喜欢的断言（结果为真，还是为假，为真继续执行）风格来使用挂起函数
        }
    }
//    等待一个作业
//    延迟一段时间来等待另一个协程运行并不是一个好的选择。让我们显式（以非阻塞方式）等待所启动的后台 Job 执行结束：


    val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join() // 等待直到子协程执行结束


//    现在，结果仍然相同，但是主协程与后台作业的持续时间没有任何关系了。好多了。

//    结构化的并发
//    协程的实际使用还有一些需要改进的地方。 当我们使用 GlobalScope.launch 时，我们会创建一个顶层协程。
//    虽然它很轻量，但它运行时仍会消耗一些内存资源。如果我们忘记保持对新启动的协程的引用，它还会继续运行。
//    如果协程中的代码挂起了会怎么样（例如，我们错误地延迟了太长时间），
//    如果我们启动了太多的协程并导致内存不足会怎么样？
//    必须手动保持对所有已启动协程的引用并 join 之很容易出错。
//    有一个更好的解决办法。我们可以在代码中使用结构化并发。
//    我们可以在执行操作所在的指定作用域内启动协程，
//    而不是像通常使用线程（线程总是全局的）那样在 GlobalScope 中启动。
//    在我们的示例中，我们使用 runBlocking 协程构建器将 main 函数转换为协程。
//    包括 runBlocking 在内的每个协程构建器都将 CoroutineScope 的实例添加到其代码块所在的作用域中。
//    我们可以在这个作用域中启动协程而无需显式 join 之，
//    因为外部协程（示例中的 runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束。
//    因此，可以将我们的示例简化为：

    fun main4() = runBlocking { // this: CoroutineScope
        launch { // 在 runBlocking 作用域中启动一个新协程
            delay(1000L)
            println("World!")
        }
        println("Hello,")
    }

//    作用域构建器
//    除了由不同的构建器提供协程作用域之外，还可以使用 coroutineScope 构建器声明自己的作用域。
//    它会创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束。
//    runBlocking 与 coroutineScope 可能看起来很类似，因为它们都会等待其协程体以及所有子协程结束。
//    主要区别在于，runBlocking 方法会阻塞当前线程来等待，
//              而 coroutineScope 只是挂起， 会释放底层线程用于其他用途。 由于存在这点差异，runBlocking 是常规函数，
//              而 coroutineScope 是挂起函数。
//    可以通过以下示例来演示：


    fun main5() = runBlocking { // this: CoroutineScope
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }

        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }


//    请注意，（当等待内嵌 launch 时）紧挨“Task from coroutine scope”消息之后，
//    就会执行并输出“Task from runBlocking”——尽管 coroutineScope 尚未结束。
//    提取函数重构
//    提取函数重构我们来将 launch { …… } 内部的代码块提取到独立的函数中。
//    当你对这段代码执行“提取函数”重构时，你会得到一个带有 suspend 修饰符的新函数。
//    这是你的第一个挂起函数。在协程内部可以像普通函数一样使用挂起函数，
//    不过其额外特性是，同样可以使用其他挂起函数（如本例中的 delay）来挂起协程的执行。


    fun main6() = runBlocking {
//        launch { doWorld() }
        println("Hello,")
    }

    // 这是你的第一个挂起函数
    suspend fun doWorld() {
        delay(1000L)
        println("World!")
    }

//    但是如果提取出的函数包含一个在当前作用域中调用的协程构建器的话，该怎么办？ 在这种情况下，
//    所提取函数上只有 suspend 修饰符是不够的。
//    为 CoroutineScope 写一个 doWorld 扩展方法是其中一种解决方案，
//    但这可能并非总是适用，因为它并没有使 API 更加清晰。
//    惯用的解决方案是要么显式将 CoroutineScope 作为包含该函数的类的一个字段，
//    要么当外部类实现了 CoroutineScope 时隐式取得。 作为最后的手段，
//    可以使用 CoroutineScope(coroutineContext)，不过这种方法结构上不安全，
//    因为你不能再控制该方法执行的作用域。只有私有 API 才能使用这个构建器。
//    协程很轻量


    //    运行以下代码：
    fun main() = runBlocking {
        repeat(100_000) { // 启动大量的协程
            launch {
                delay(5000L)
                print(".")
            }
        }
    }

//    它启动了 10 万个协程，并且在 5 秒钟后，每个协程都输出一个点。
//    现在，尝试使用线程来实现。会发生什么？（很可能你的代码会产生某种内存不足的错误）
//    全局协程像守护线程
//    以下代码在 GlobalScope 中启动了一个长期运行的协程，
//    该协程每秒输出“I'm sleeping”两次，之后在主函数中延迟一段时间后返回。


    GlobalScope.launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
//    delay(1300L) // 在延迟后退出


//    你可以运行这个程序并看到它输出了以下三行后终止：
//    I'm sleeping 0 ...
//    I'm sleeping 1 ...
//    I'm sleeping 2 ...
}

annotation class Test

/**
 * 取消与超时
 */
fun coroutines2() {
    /*取消与超时
    这一部分包含了协程的取消与超时。
    取消协程的执行
    在一个长时间运行的应用程序中，你也许需要对你的后台协程进行细粒度的控制。 比如说，
    一个用户也许关闭了一个启动了协程的界面，
    那么现在协程的执行结果已经不再被需要了，这时，它应该是可以被取消的。
    该 launch 函数返回了一个可以被用来取消运行中的协程的 Job：
    val job = launch {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancel() // 取消该作业
    job.join() // 等待作业执行结束
    println("main: Now I can quit.")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    程序执行后的输出如下：

    job: I'm sleeping 0 ...
    job: I'm sleeping 1 ...
    job: I'm sleeping 2 ...
    main: I'm tired of waiting!
    main: Now I can quit.
    一旦 main 函数调用了 job.cancel，我们在其它的协程中就看不到任何输出，因为它被取消了。
    这里也有一个可以使 Job 挂起的函数 cancelAndJoin 它合并了对 cancel 以及 join 的调用。

    取消是协作的
    协程的取消是 协作 的。一段协程代码必须协作才能被取消。
    所有 kotlinx.coroutines 中的挂起函数都是 可被取消的 。
    它们检查协程的取消，
    并在取消时抛出 CancellationException。
    然而，如果协程正在执行计算任务，并且没有检查取消的话，
    那么它是不能被取消的，就如如下示例代码所示：

    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个作业并且等待它结束
    println("main: Now I can quit.")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    运行示例代码，并且我们可以看到它连续打印出了“I'm sleeping”，甚至在调用取消后，
    作业仍然执行了五次循环迭代并运行到了它结束为止。
    使计算代码可取消
    我们有两种方法来使执行计算的代码可以被取消。
    第一种方法是定期调用挂起函数来检查取消。
    对于这种目的 yield 是一个好的选择。
    另一种方法是显式的检查取消状态。让我们试试第二种方法。
    将前一个示例中的 while (i < 5) 替换为 while (isActive) 并重新运行它。


    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // 可以被取消的计算循环
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并等待它结束
    println("main: Now I can quit.")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    你可以看到，现在循环被取消了。isActive 是一个可以被使用在 CoroutineScope 中的扩展属性。
    在 finally 中释放资源
    我们通常使用如下的方法处理在被取消时抛出 CancellationException
    的可被取消的挂起函数。
    比如说，try {……} finally {……}
    表达式以及 Kotlin 的 use 函数一般在协程被取消的时候执行它们的终结动作：

    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("job: I'm running finally")
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并且等待它结束
    println("main: Now I can quit.")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    join 和 cancelAndJoin 等待了所有的终结动作执行完毕， 所以运行示例得到了下面的输出：

    job: I'm sleeping 0 ...
    job: I'm sleeping 1 ...
    job: I'm sleeping 2 ...
    main: I'm tired of waiting!
    job: I'm running finally
    main: Now I can quit.
    运行不能取消的代码块

    在前一个例子中任何尝试在 finally 块中调用挂起函数的行为都会抛出 CancellationException，因为这里持续运行的代码是可以被取消的。通常，这并不是一个问题，所有良好的关闭操作（关闭一个文件、取消一个作业、或是关闭任何一种通信通道）通常都是非阻塞的，并且不会调用任何挂起函数。然而，在真实的案例中，当你需要挂起一个被取消的协程，你可以将相应的代码包装在 withContext(NonCancellable) {……} 中，并使用 withContext 函数以及 NonCancellable 上下文，见如下示例所示：


    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并等待它结束
    println("main: Now I can quit.")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    超时

    在实践中绝大多数取消一个协程的理由是它有可能超时。
     当你手动追踪一个相关 Job 的引用并启动了
     一个单独的协程在延迟后取消追踪，
     这里已经准备好使用 withTimeout 函数来做这件事。 来看看示例代码：


    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    运行后得到如下输出：

    I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    Exception in thread "main" kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
    withTimeout 抛出了 TimeoutCancellationException，
    它是 CancellationException 的子类。
    我们之前没有在控制台上看到堆栈跟踪信息的打印。
    这是因为在被取消的协程中
    CancellationException 被认为是协程执行结束的正常原因。
    然而，在这个示例中我们在 main 函数中正确地使用了 withTimeout。

    由于取消只是一个例外，所有的资源都使用常用的方法来关闭。
    如果你需要做一些各类使用超时的特别的额外操作，
    可以使用类似 withTimeout 的 withTimeoutOrNull 函数，
    并把这些会超时的代码包装在 try {...} catch
    (e: TimeoutCancellationException) {...} 代码块中，
    而 withTimeoutOrNull 通过返回 null 来进行超时操作，从而替代抛出一个异常：

    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done" // 在它运行得到结果之前取消它
    }
    println("Result is $result")


    I'm sleeping 0 ...
    I'm sleeping 1 ...
    I'm sleeping 2 ...
    Result is null
    Asynchronous timeout and resources

            The timeout event in withTimeout is asynchronous with respect to the code running in its block and may happen at any time, even right before the return from inside of the timeout block. Keep this in mind if you open or acquire some resource inside the block that needs closing or release outside of the block.

    For example, here we imitate a closeable resource with the Resource class, that simply keeps track of how many times it was created by incrementing the acquired counter and decrementing this counter from its close function. Let us run a lot of coroutines with the small timeout try acquire this resource from inside of the withTimeout block after a bit of delay and release it from outside.


    var acquired = 0
    ​
    class Resource {
        init { acquired++ } // Acquire the resource
        fun close() { acquired-- } // Release the resource
    }
    ​
    fun main() {
        runBlocking {
            repeat(100_000) { // Launch 100K coroutines
                launch {
                    val resource = withTimeout(60) { // Timeout of 60 ms
                        delay(50) // Delay for 50 ms
                        Resource() // Acquire a resource and return it from withTimeout block
                    }
                    resource.close() // Release the resource
                }
            }
        }
        // Outside of runBlocking all coroutines have completed
        println(acquired) // Print the number of resources still acquired
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    You can get the full code here.
    If you run the above code you'll see that it does not always print zero, though it may depend on the timings of your machine you may need to tweak timeouts in this example to actually see non-zero values.

    Note, that incrementing and decrementing acquired counter here from 100K coroutines is completely safe, since it always happens from the same main thread. More on that will be explained in the next chapter on coroutine context.
    To workaround this problem you can store a reference to the resource in the variable as opposed to returning it from the withTimeout block.


    runBlocking {
        repeat(100_000) { // Launch 100K coroutines
            launch {
                var resource: Resource? = null // Not acquired yet
                try {
                    withTimeout(60) { // Timeout of 60 ms
                        delay(50) // Delay for 50 ms
                        resource = Resource() // Store a resource to the variable if acquired
                    }
                    // We can do something else with the resource here
                } finally {
                    resource?.close() // Release the resource if it was acquired
                }
            }
        }
    }
// Outside of runBlocking all coroutines have completed
    println(acquired) // Print the number of resources still acquired
    Target platform: JVMRunning on kotlin v. 1.6.0
    You can get the full code here.*/

}

/**
 * 组合挂起函数
 */
fun coroutines3() {
    //    /*组合挂起函数
//    本节介绍了将挂起函数组合的各种方法。
//    默认顺序调用
//    假设我们在不同的地方定义了两个进行某种调用远程服务或者进行计算的挂起函数。
//    我们只假设它们都是有用的，但是实际上它们在这个示例中只是为了该目的而延迟了一秒钟：
//
//    suspend fun doSomethingUsefulOne(): Int {
//        delay(1000L) // 假设我们在这里做了一些有用的事
//        return 13
//    }
//    ​
//    suspend fun doSomethingUsefulTwo(): Int {
//        delay(1000L) // 假设我们在这里也做了一些有用的事
//        return 29
//    }
//    如果需要按 顺序 调用它们，
//    我们接下来会做什么——首先调用 doSomethingUsefulOne
//    接下来 调用 doSomethingUsefulTwo，
//    并且计算它们结果的和吗？
//    实际上，
//    如果我们要根据第一个函数的结果来决定是否我们需要调用第二个函数或者决定如何调用它时，我们就会这样做。
//
//    我们使用普通的顺序来进行调用，
//    因为这些代码是运行在协程中的，
//    只要像常规的代码一样 顺序 都是默认的。
//    下面的示例展示了测量执行两个挂起函数所需要的总时间：
//
//
//    val time = measureTimeMillis {
//        val one = doSomethingUsefulOne()
//        val two = doSomethingUsefulTwo()
//        println("The answer is ${one + two}")
//    }
//    println("Completed in $time ms")
//    Target platform: JVMRunning on kotlin v. 1.6.0
//    可以在这里获取完整代码。
//    它的打印输出如下：
//
//    The answer is 42
//    Completed in 2017 ms
//            使用 async 并发
//    如果 doSomethingUsefulOne 与 doSomethingUsefulTwo 之间没有依赖，
//    并且我们想更快的得到结果，
//    让它们进行 并发 吗？
//    这就是 async 可以帮助我们的地方。
//    在概念上，async 就类似于 launch。
//    它启动了一个单独的协程，
//    这是一个轻量级的线程并与其它所有的协程一起并发的工作。
//    不同之处在于 launch 返回一个 Job 并且不附带任何结果值，
//    而 async 返回一个 Deferred —— 一个轻量级的非阻塞 future，
//    这代表了一个将会在稍后提供结果的 promise。
//    你可以使用 .await() 在一个延期的值上得到它的最终结果，
//    但是 Deferred 也是一个 Job，所以如果需要的话，你可以取消它。
//
//
//    val time = measureTimeMillis {
//        val one = async { doSomethingUsefulOne() }
//        val two = async { doSomethingUsefulTwo() }
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")
//    Target platform: JVMRunning on kotlin v. 1.6.0
//    可以在这里获取完整代码。
//    它的打印输出如下：
//
//    The answer is 42
//    Completed in 1017 ms
//            这里快了两倍，因为两个协程并发执行。 请注意，使用协程进行并发总是显式的。
//
//    惰性启动的 async
//            可选的，async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
//             在这个模式下，只有结果通过 await 获取的时候协程才会启动，
//             或者在 Job 的 start 函数调用的时候。运行下面的示例：
//
//
//    val time = measureTimeMillis {
//        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
//        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
//        // 执行一些计算
//        one.start() // 启动第一个
//        two.start() // 启动第二个
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")
//    Target platform: JVMRunning on kotlin v. 1.6.0
//    可以在这里获取完整代码。
//    它的打印输出如下：
//
//    The answer is 42
//    Completed in 1017 ms
//            因此，在先前的例子中这里定义的两个协程没有执行，
//            但是控制权在于程序员准确的在开始执行时调用 start。
//            我们首先 调用 one，然后调用 two，接下来等待这个协程执行完毕。
//
//    注意，如果我们只是在 println 中调用 await，
//    而没有在单独的协程中调用 start，
//    这将会导致顺序行为，
//    直到 await 启动该协程 执行并等待至它结束，
//    这并不是惰性的预期用例。
//    在计算一个值涉及挂起函数时，
//    这个 async(start = CoroutineStart.LAZY) 的用例用于替代标准库中的 lazy 函数。
//
//    async 风格的函数
//            我们可以定义异步风格的函数来
//            异步 的调用 doSomethingUsefulOne 和
//            doSomethingUsefulTwo
//            并使用 async 协程建造器并带有一个显式的 GlobalScope 引用。
//             我们给这样的函数的名称中加上“……Async”后缀来突出表明：
//             事实上，它们只做异步计算并且需要使用延期的值来获得结果。
//
//
//    // somethingUsefulOneAsync 函数的返回值类型是 Deferred<Int>
//    fun somethingUsefulOneAsync() = GlobalScope.async {
//        doSomethingUsefulOne()
//    }
//    ​
//    // somethingUsefulTwoAsync 函数的返回值类型是 Deferred<Int>
//    fun somethingUsefulTwoAsync() = GlobalScope.async {
//        doSomethingUsefulTwo()
//    }
//    注意，这些 xxxAsync 函数不是 挂起 函数。
//    它们可以在任何地方使用。
//    然而，它们总是在调用它们的代码中意味着异步（这里的意思是 并发 ）执行。
//
//    下面的例子展示了它们在协程的外面是如何使用的：
//
//
//    // 注意，在这个示例中我们在 `main` 函数的右边没有加上 `runBlocking`
//    fun main() {
//        val time = measureTimeMillis {
//            // 我们可以在协程外面启动异步执行
//            val one = somethingUsefulOneAsync()
//            val two = somethingUsefulTwoAsync()
//            // 但是等待结果必须调用其它的挂起或者阻塞
//            // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
//            runBlocking {
//                println("The answer is ${one.await() + two.await()}")
//            }
//        }
//        println("Completed in $time ms")
//    }
//    Target platform: JVMRunning on kotlin v. 1.6.0
//    可以在这里获取完整代码。
//    这种带有异步函数的编程风格仅供参考，
//    因为这在其它编程语言中是一种受欢迎的风格。
//    在 Kotlin 的协程中使用这种风格是强烈不推荐的， 原因如下所述。
//    考虑一下如果 val one = somethingUsefulOneAsync()
//    这一行和 one.await() 表达式这里在代码中有逻辑错误，
//    并且程序抛出了异常以及程序在操作的过程中中止，将会发生什么。
//     通常情况下，一个全局的异常处理者会捕获这个异常，
//     将异常打印成日记并报告给开发者，
//     但是反之该程序将会继续执行其它操作。
//     但是这里我们的 somethingUsefulOneAsync 仍然在后台执行，
//      尽管如此，启动它的那次操作也会被终止。
//      这个程序将不会进行结构化并发，如下一小节所示。
//
//    使用 async 的结构化并发
//
//    让我们使用使用 async 的并发这一小节的例子
//    并且提取出一个函数并发的调用 doSomethingUsefulOne
//    与 doSomethingUsefulTwo 并且返回它们两个的结果之和。
//     由于 async 被定义为了 CoroutineScope 上的扩展，
//     我们需要将它写在作用域内，并且这是 coroutineScope 函数所提供的：
//
//
//    suspend fun concurrentSum(): Int = coroutineScope {
//        val one = async { doSomethingUsefulOne() }
//        val two = async { doSomethingUsefulTwo() }
//        one.await() + two.await()
//    }
//    这种情况下，如果在 concurrentSum 函数内部发生了错误，并且它抛出了一个异常，
//    所有在作用域中启动的协程都会被取消。
//
//
//    val time = measureTimeMillis {
//        println("The answer is ${concurrentSum()}")
//    }
//    println("Completed in $time ms")
//    Target platform: JVMRunning on kotlin v. 1.6.0
//    可以在这里获取完整代码。
//    从上面的 main 函数的输出可以看出，我们仍然可以同时执行这两个操作：
//
//    The answer is 42
//    Completed in 1017 ms
//            取消始终通过协程的层次结构来进行传递：
//
//
//    import kotlinx.coroutines.*
//
    fun main() = runBlocking<Unit> {
        try {
//            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }
    }

    suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }
//    Target platform: JVMRunning on kotlin v. 1.6.0
//    可以在这里获取完整代码。
//    请注意，如果其中一个子协程（即 two）失败，第一个 async 以及等待中的父协程都会被取消：
//
//    Second child throws an exception
//    First child was cancelled
//            Computation failed with ArithmeticException*/
}

/**
 *协程上下文和调度器
 */
fun coroutines4() {
    /*     协程上下文与调度器

    协程总是运行在一些以 CoroutineContext 类型为代表的上下文中，它们被定义在了 Kotlin 的标准库里。
    协程上下文是各种不同元素的集合。其中主元素是协程中的 Job，
    我们在前面的文档中见过它以及它的调度器，而本文将对它进行介绍。
    调度器与线程
    协程上下文包含一个 协程调度器
    参见 CoroutineDispatcher）它确定了相关的协程在哪个线程或哪些线程上执行。
    协程调度器:
    可以将协程限制在一个特定的线程执行，
    或将它分派到一个线程池，
    亦或是让它不受限地运行。

    所有的协程构建器诸如
    launch 和 async 接收一个可选的 CoroutineContext 参数，
    它可以被用来显式的为一个新协程或其它上下文元素指定一个调度器。

    尝试下面的示例：


    launch { // 运行在父协程的上下文中，即 runBlocking 主协程
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) { // 将会获取默认调度器
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    它执行后得到了如下输出（也许顺序会有所不同）：

    Unconfined            : I'm working in thread main
    Default               : I'm working in thread DefaultDispatcher-worker-1
    newSingleThreadContext: I'm working in thread MyOwnThread
    main runBlocking      : I'm working in thread main

    当调用 launch { …… } 时不传参数，
    它从启动了它的 CoroutineScope 中承袭了上下文（以及调度器）。
    在这个案例中，它从 main 线程中的 runBlocking 主协程承袭了上下文。

    Dispatchers.Unconfined 是一个特殊的调度器且似乎也运行在 main 线程中，
    但实际上， 它是一种不同的机制，这会在后文中讲到。

    当协程在 GlobalScope 中启动时，使用的是由 Dispatchers.Default
    代表的默认调度器。 默认调度器使用共享的后台线程池。
    所以 launch(Dispatchers.Default) { …… }
    与 GlobalScope.launch { …… } 使用相同的调度器。

    newSingleThreadContext 为协程的运行启动了一个线程。
     一个专用的线程是一种非常昂贵的资源。
     在真实的应用程序中两者都必须被释放，
     当不再需要的时候，使用 close 函数，
     或存储在一个顶层变量中使它在整个应用程序中被重用。

    非受限调度器 vs 受限调度器

    Dispatchers.Unconfined 协程调度器在调用它的线程启动了一个协程，
    但它仅仅只是运行到第一个挂起点。挂起后，它恢复线程中的协程，
    而这完全由被调用的挂起函数来决定。
    非受限的调度器非常适用于执行不消耗 CPU 时间的任务，
    以及不更新局限于特定线程的任何共享数据（如UI）的协程。

    另一方面，该调度器默认继承了外部的 CoroutineScope。
    runBlocking 协程的默认调度器，特别是，
     当它被限制在了调用者线程时，
     继承自它将会有效地限制协程在该线程运行并且具有可预测的 FIFO 调度。


    launch(Dispatchers.Unconfined) { // 非受限的——将和主线程一起工作
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
    launch { // 父协程的上下文，主 runBlocking 协程
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    执行后的输出：

    Unconfined      : I'm working in thread main
    main runBlocking: I'm working in thread main
    Unconfined      : After delay in thread kotlinx.coroutines.DefaultExecutor
    main runBlocking: After delay in thread main
    所以，该协程的上下文继承自 runBlocking {...} 协程并在 main 线程中运行，
    当 delay 函数调用的时候，非受限的那个协程在默认的执行者线程中恢复执行。

    非受限的调度器是一种高级机制，
    可以在某些极端情况下提供帮助而不需要调度协程以便稍后执行或产生不希望的副作用，
    因为某些操作必须立即在协程中执行。 非受限调度器不应该在通常的代码中使用。
    调试协程与线程

    协程可以在一个线程上挂起并在其它线程上恢复。
     如果没有特殊工具，甚至对于一个单线程的调度器也是难以弄清楚协程在何时何地正在做什么事情。

    用 IDEA 调试

    Kotlin 插件的协程调试器简化了 IntelliJ IDEA 中的协程调试.

    调试适用于 1.3.8 或更高版本的 kotlinx-coroutines-core。
    调试工具窗口包含 Coroutines 标签。在这个标签中，你可以同时找到运行中与已挂起的协程的相关信息。
    这些协程以它们所运行的调度器进行分组。

    Debugging coroutines

            使用协程调试器，你可以：

    检查每个协程的状态。
    查看正在运行的与挂起的的协程的局部变量以及捕获变量的值。
    查看完整的协程创建栈以及协程内部的调用栈。栈包含所有带有变量的栈帧，甚至包含那些在标准调试期间会丢失的栈帧。
    获取包含每个协程的状态以及栈信息的完整报告。要获取它，请右键单击 Coroutines 选项卡，然后点击 Get Coroutines Dump。
    要开始协程调试，你只需要设置断点并在调试模式下运行应用程序即可。

    在这篇教程中学习更多的协程调试知识。

    用日志调试

    另一种调试线程应用程序而不使用协程调试器的方法
    是让线程在每一个日志文件的日志声明中打印线程的名字。
    这种特性在日志框架中是普遍受支持的。但是在使用协程时，
    单独的线程名称不会给出很多协程上下文信息，所以 kotlinx.coroutines 包含了调试工具来让它更简单。

    使用 -Dkotlinx.coroutines.debug JVM 参数运行下面的代码：


    val a = async {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这里有三个协程，包括 runBlocking 内的主协程 (#1) ， 以及计算延期的值的另外两个协程 a (#2) 和 b (#3)。 它们都在 runBlocking 上下文中执行并且被限制在了主线程内。 这段代码的输出如下：

    [main @coroutine#2] I'm computing a piece of the answer
    [main @coroutine#3] I'm computing another piece of the answer
    [main @coroutine#1] The answer is 42
    这个 log 函数在方括号种打印了线程的名字，并且你可以看到它是 main 线程，并且附带了当前正在其上执行的协程的标识符。这个标识符在调试模式开启时，将连续分配给所有创建的协程。

    当 JVM 以 -ea 参数配置运行时，调试模式也会开启。 你可以在 DEBUG_PROPERTY_NAME 属性的文档中阅读有关调试工具的更多信息。
    在不同线程间跳转

    使用 -Dkotlinx.coroutines.debug JVM 参数运行下面的代码（参见调试）：


    newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->
            runBlocking(ctx1) {
                log("Started in ctx1")
                withContext(ctx2) {
                    log("Working in ctx2")
                }
                log("Back to ctx1")
            }
        }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    它演示了一些新技术。其中一个使用 runBlocking 来显式指定了一个上下文，
    并且另一个使用 withContext 函数来改变协程的上下文，而仍然驻留在相同的协程中，正如可以在下面的输出中所见到的：

    [Ctx1 @coroutine#1] Started in ctx1
    [Ctx2 @coroutine#1] Working in ctx2
    [Ctx1 @coroutine#1] Back to ctx1
    注意，在这个例子中，当我们不再需要某个在 newSingleThreadContext
     中创建的线程的时候， 它使用了 Kotlin 标准库中的 use 函数来释放该线程。

    上下文中的作业

    协程的 Job 是上下文的一部分，并且可以使用 coroutineContext [Job] 表达式在上下文中检索它：


    println("My job is ${coroutineContext[Job]}")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    在调试模式下，它将输出如下这些信息：

    My job is "coroutine#1":BlockingCoroutine{Active}@6d311334
    请注意，CoroutineScope 中的 isActive 只是 coroutineContext[Job]?.isActive == true 的一种方便的快捷方式。

    子协程

    当一个协程被其它协程在 CoroutineScope 中启动的时候，
    它将通过 CoroutineScope.coroutineContext 来承袭上下文，
    并且这个新协程的 Job 将会成为父协程作业的 子 作业。当一个父协程被取消的时候，所有它的子协程也会被递归的取消。

    然而，当使用 GlobalScope 来启动一个协程时，则新协程的作业没有父作业。
    因此它与这个启动的作用域无关且独立运作。


// 启动一个协程来处理某种传入请求（request）
    val request = launch {
        // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
        GlobalScope.launch {
            println("job1: I run in GlobalScope and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the request")
        }
        // 另一个则承袭了父协程的上下文
        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() // 取消请求（request）的执行
    delay(1000) // 延迟一秒钟来看看发生了什么
    println("main: Who has survived request cancellation?")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码的输出如下：

    job1: I run in GlobalScope and execute independently!
    job2: I am a child of the request coroutine
    job1: I am not affected by cancellation of the request
    main: Who has survived request cancellation?
    父协程的职责

    一个父协程总是等待所有的子协程执行结束。
    父协程并不显式的跟踪所有子协程的启动，
    并且不必使用 Job.join 在最后的时候等待它们：


// 启动一个协程来处理某种传入请求（request）
    val request = launch {
        repeat(3) { i -> // 启动少量的子作业
            launch  {
                delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still active")
    }
    request.join() // 等待请求的完成，包括其所有子协程
    println("Now processing of the request is complete")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    结果如下所示：

    request: I'm done and I don't explicitly join my children that are still active
    Coroutine 0 is done
    Coroutine 1 is done
    Coroutine 2 is done
    Now processing of the request is complete
    命名协程以用于调试

    当协程经常打印日志并且你只需要关联来自同一个协程的日志记录时，
    则自动分配的 id 是非常好的。
    然而，当一个协程与特定请求的处理相关联时或做一些特定的后台任务，最好将其明确命名以用于调试目的。
    CoroutineName 上下文元素与线程名具有相同的目的。
    当调试模式开启时，它被包含在正在执行此协程的线程名中。

    下面的例子演示了这一概念：


    log("Started main coroutine")
// 运行两个后台值计算
    val v1 = async(CoroutineName("v1coroutine")) {
        delay(500)
        log("Computing v1")
        252
    }
    val v2 = async(CoroutineName("v2coroutine")) {
        delay(1000)
        log("Computing v2")
        6
    }
    log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    程序执行使用了 -Dkotlinx.coroutines.debug JVM 参数，输出如下所示：

    [main @main#1] Started main coroutine
    [main @v1coroutine#2] Computing v1
    [main @v2coroutine#3] Computing v2
    [main @main#1] The answer for v1 / v2 = 42
    组合上下文中的元素

    有时我们需要在协程上下文中定义多个元素。
    我们可以使用 + 操作符来实现。 比如说，
    我们可以显式指定一个调度器来启动协程并且同时显式指定一个命名：


    launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码使用了 -Dkotlinx.coroutines.debug JVM 参数，输出如下所示：

    I'm working in thread DefaultDispatcher-worker-1 @test#2
    协程作用域

    让我们将关于上下文，子协程以及作业的知识综合在一起。
    假设我们的应用程序拥有一个具有生命周期的对象，
    但这个对象并不是一个协程。举例来说，
    我们编写了一个 Android 应用程序并在 Android 的 activity 上下文中
    启动了一组协程来使用异步操作拉取并更新数据以及执行动画等等。
    所有这些协程必须在这个 activity 销毁的时候取消以避免内存泄漏。
    当然，我们也可以手动操作上下文与作业，
    以结合 activity 的生命周期与它的协程，
    但是 kotlinx.coroutines 提供了一个封装：CoroutineScope 的抽象。
     你应该已经熟悉了协程作用域，因为所有的协程构建器都声明为在它之上的扩展。

    我们通过创建一个 CoroutineScope 实例来管理协程的生命周期，
    并使它与 activity 的生命周期相关联。
    CoroutineScope 可以通过
     CoroutineScope() 创建或者通过MainScope() 工厂函数。
     前者创建了一个通用作用域，
     而后者为使用 Dispatchers.Main 作为默认调度器的 UI 应用程序 创建作用域：


    class Activity {
        private val mainScope = MainScope()
        ​
        fun destroy() {
            mainScope.cancel()
        }
        // 继续运行……
        现在，我们可以使用定义的 scope 在这个 Activity 的作用域内启动协程。
        对于该示例，我们启动了十个协程，它们会延迟不同的时间：


        // 在 Activity 类中
        fun doSomething() {
            // 在示例中启动了 10 个协程，且每个都工作了不同的时长
            repeat(10) { i ->
                mainScope.launch {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间
                    println("Coroutine $i is done")
                }
            }
        }
    } // Activity 类结束
    在 main 函数中我们创建 activity，
    调用测试函数 doSomething，
    并且在 500 毫秒后销毁这个 activity。
    这取消了从 doSomething 启动的所有协程。
    我们可以观察到这些是由于在销毁之后，
    即使我们再等一会儿，activity 也不再打印消息。


    val activity = Activity()
    activity.doSomething() // 运行测试函数
    println("Launched coroutines")
    delay(500L) // 延迟半秒钟
    println("Destroying activity!")
    activity.destroy() // 取消所有的协程
    delay(1000) // 为了在视觉上确认它们没有工作
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这个示例的输出如下所示：

    Launched coroutines
            Coroutine 0 is done
    Coroutine 1 is done
    Destroying activity!
    你可以看到，只有前两个协程打印了消息，而另一个协程在 Activity.destroy() 中单次调用了 job.cancel()。

    注意，Android 在所有具有生命周期的实体中都对协程作用域提供了一等的支持。 请查看相关文档。
    线程局部数据

    有时，能够将一些线程局部数据传递到协程与协程之间是很方便的。
     然而，由于它们不受任何特定线程的约束，如果手动完成，可能会导致出现样板代码。

    ThreadLocal， asContextElement 扩展函数在这里会充当救兵。
    它创建了额外的上下文元素， 且保留给定 ThreadLocal 的值，并在每次协程切换其上下文时恢复它。

    它很容易在下面的代码中演示：


    threadLocal.set("main")
    println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
        println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        yield()
        println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }
    job.join()
    println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    在这个例子中我们使用 Dispatchers.Default 在后台线程池中启动了一个新的协程，
    所以它工作在线程池中的不同线程中，
    但它仍然具有线程局部变量的值，
    我们指定使用 threadLocal.asContextElement(value = "launch")，
    无论协程执行在哪个线程中都是没有问题的。 因此，其输出如（调试）所示：

    Pre-main, current thread: Thread[main @coroutine#1,5,main], thread local value: 'main'
    Launch start, current thread: Thread[DefaultDispatcher-worker-1 @coroutine#2,5,main], thread local value: 'launch'
    After yield, current thread: Thread[DefaultDispatcher-worker-2 @coroutine#2,5,main], thread local value: 'launch'
    Post-main, current thread: Thread[main @coroutine#1,5,main], thread local value: 'main'
    这很容易忘记去设置相应的上下文元素。如果运行协程的线程不同，
    在协程中访问的线程局部变量则可能会产生意外的值。
    为了避免这种情况，建议使用 ensurePresent 方法并且在不正确的使用时快速失败。

    ThreadLocal 具有一流的支持，可以与任何 kotlinx.coroutines 提供的原语一起使用。
    但它有一个关键限制，即：当一个线程局部变量变化时，
    则这个新值不会传播给协程调用者
    （因为上下文元素无法追踪所有 ThreadLocal 对象访问），
    并且下次挂起时更新的值将丢失。
    使用 withContext 在协程中更新线程局部变量，详见 asContextElement。

    另外，一个值可以存储在一个可变的域中，
    例如 class Counter(var i: Int)，是的，反过来，
     可以存储在线程局部的变量中。然而，在这个案例中你完全有责任来进行同步可能的对这个可变的域进行的并发的修改。

    对于高级的使用，例如，那些在内部使用线程局部传递数据的用于与日志记录 MDC 集成，
    以及事务上下文或任何其它库，请参见需要实现的 ThreadContextElement 接口的文档。
*/
}

/**
 *异步流
 */
fun coroutines5() {
    /*异步流

    挂起函数可以异步的返回单个值，但是该如何异步返回多个计算好的值呢？这正是 Kotlin 流（Flow）的用武之地。

    表示多个值

    在 Kotlin 中可以使用集合来表示多个值。
    比如说，我们有一个 simple 函数，它返回一个包含三个数字的 List， 然后使用 forEach 打印它们：


    fun simple(): List<Int> = listOf(1, 2, 3)

    fun main() {
        simple().forEach { value -> println(value) }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码输出如下：

    1
    2
    3
    序列

    如果使用一些消耗 CPU 资源的阻塞代码计算数字（每次计算需要 100 毫秒）那么我们可以使用 Sequence 来表示数字：


    fun simple(): Sequence<Int> = sequence { // 序列构建器
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们正在计算
            yield(i) // 产生下一个值
        }
    }
    ​
    fun main() {
        simple().forEach { value -> println(value) }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码输出相同的数字，但在打印每个数字之前等待 100 毫秒。

    挂起函数

    然而，计算过程阻塞运行该代码的主线程。
    当这些值由异步代码计算时，
    我们可以使用 suspend 修饰符标记函数 simple，
    这样它就可以在不阻塞的情况下执行其工作并将结果作为列表返回：


    suspend fun simple(): List<Int> {
        delay(1000) // 假装我们在这里做了一些异步的事情
        return listOf(1, 2, 3)
    }
    ​
    fun main() = runBlocking<Unit> {
        simple().forEach { value -> println(value) }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码将会在等待一秒之后打印数字。

    流

    使用 List 结果类型，意味着我们只能一次返回所有值。
    为了表示异步计算的值流（stream），
    我们可以使用 Flow 类型（正如同步计算值会使用 Sequence 类型）：



    fun simple(): Flow<Int> = flow { // 流构建器
        for (i in 1..3) {
            delay(100) // 假装我们在这里做了一些有用的事情
            emit(i) // 发送下一个值
        }
    }
    ​
    fun main() = runBlocking<Unit> {
        // 启动并发的协程以验证主线程并未阻塞
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
        }
        // 收集这个流
        simple().collect { print(it) }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码在不阻塞主线程的情况下每等待 100 毫秒打印一个数字。在主线程中运行一个单独的协程每
    100 毫秒打印一次 “I'm not blocked” 已经经过了验证。

    I'm not blocked 1
    1
    I'm not blocked 2
    2
    I'm not blocked 3
    3
    注意使用 Flow 的代码与先前示例的下述区别：

    名为 flow 的 Flow 类型构建器函数。
    flow { ... } 构建块中的代码可以挂起。
    函数 simple 不再标有 suspend 修饰符。
    流使用 emit 函数 发射 值。
    流使用 collect 函数 收集 值。
    我们可以在 simple 的 flow { ... } 函数体内使用 Thread.sleep 代替 delay 以观察主线程在本案例中被阻塞了。
    流是冷的

    Flow 是一种类似于序列的冷流 — 这段 flow 构建器中的代码直到流被收集的时候才运行。这在以下的示例中非常明显：



    fun simple(): Flow<Int> = flow {
        println("Flow started")
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }
    ​
    fun main() = runBlocking<Unit> {
        println("Calling simple function...")
        val flow = simple()
        println("Calling collect...")
        flow.collect { value -> println(value) }
        println("Calling collect again...")
        flow.collect { value -> println(value) }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    打印如下：

    Calling simple function...
    Calling collect...
    Flow started
            1
    2
    3
    Calling collect again...
    Flow started
            1
    2
    3
    这是返回一个流的 simple 函数没有标记 suspend 修饰符的主要原因。
    通过它自己，simple() 调用会尽快返回且不会进行任何等待。
    该流在每次收集的时候启动， 这就是为什么当我们再次调用 collect 时我们会看到“Flow started”。

    流取消基础

    流采用与协程同样的协作取消。
    像往常一样，流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消。
    以下示例展示了当 withTimeoutOrNull 块中代码在运行的时候流是如何在超时的情况下取消并停止执行其代码的：



    fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }
    ​
    fun main() = runBlocking<Unit> {
        withTimeoutOrNull(250) { // 在 250 毫秒后超时
            simple().collect { value -> println(value) }
        }
        println("Done")
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    注意，在 simple 函数中流仅发射两个数字，产生以下输出：

    Emitting 1
    1
    Emitting 2
    2
    Done
    See Flow cancellation checks section for more details.

    流构建器

    先前示例中的 flow { ... } 构建器是最基础的一个。还有其他构建器使流的声明更简单：

    flowOf 构建器定义了一个发射固定值集的流。
    使用 .asFlow() 扩展函数，可以将各种集合与序列转换为流。
    因此，从流中打印从 1 到 3 的数字的示例可以写成：


// 将一个整数区间转化为流
    (1..3).asFlow().collect { value -> println(value) }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    过渡流操作符

    可以使用操作符转换流，就像使用集合与序列一样。 过渡操作符应用于上游流，并返回下游流。
    这些操作符也是冷操作符，就像流一样。这类操作符本身不是挂起函数。它运行的速度很快，返回新的转换流的定义。

    基础的操作符拥有相似的名字，比如 map 与 filter。
    流与序列的主要区别在于这些操作符中的代码可以调用挂起函数。

    举例来说，一个请求中的流可以使用 map 操作符映射出结果，
    即使执行一个长时间的请求操作也可以使用挂起函数来实现：



    suspend fun performRequest(request: Int): String {
        delay(1000) // 模仿长时间运行的异步工作
        return "response $request"
    }
    ​
    fun main() = runBlocking<Unit> {
        (1..3).asFlow() // 一个请求流
                .map { request -> performRequest(request) }
                .collect { response -> println(response) }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    它产生以下三行，每一行每秒出现一次：

    response 1
    response 2
    response 3
    转换操作符

    在流转换操作符中，最通用的一种称为 transform。它可以用来模仿简单的转换，
    例如 map 与 filter，以及实施更复杂的转换。
    使用 transform 操作符，我们可以 发射 任意值任意次。

    比如说，使用 transform 我们可以在执行长时间运行的异步请求之前发射一个字符串并跟踪这个响应：


    (1..3).asFlow() // 一个请求流
            .transform { request ->
                emit("Making request $request")
                emit(performRequest(request))
            }
            .collect { response -> println(response) }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码的输出如下：

    Making request 1
    response 1
    Making request 2
    response 2
    Making request 3
    response 3
    限长操作符

    限长过渡操作符（例如 take）在流触及相应限制的时候会将它的执行取消。
    协程中的取消操作总是通过抛出异常来执行，
    这样所有的资源管理函数（如 try {...} finally {...} 块）会在取消的情况下正常运行：


    fun numbers(): Flow<Int> = flow {
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        } finally {
            println("Finally in numbers")
        }
    }
    ​
    fun main() = runBlocking<Unit> {
        numbers()
                .take(2) // 只获取前两个
                .collect { value -> println(value) }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码的输出清楚地表明，numbers() 函数中对 flow {...} 函数体的执行在发射出第二个数字后停止：

    1
    2
    Finally in numbers
    末端流操作符

    末端操作符是在流上用于启动流收集的挂起函数。 collect 是最基础的末端操作符，但是还有另外一些更方便使用的末端操作符：

    转化为各种集合，例如 toList 与 toSet。
    获取第一个（first）值与确保流发射单个（single）值的操作符。
    使用 reduce 与 fold 将流规约到单个值。
    举例来说：


    ​
    val sum = (1..5).asFlow()
            .map { it * it } // 数字 1 至 5 的平方
            .reduce { a, b -> a + b } // 求和（末端操作符）
    println(sum)
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    打印单个数字：

    55
    流是连续的

    流的每次单独收集都是按顺序执行的，除非进行特殊操作的操作符使用多个流。
    该收集过程直接在协程中运行，该协程调用末端操作符。 默认情况下不启动新协程
    。 从上游到下游每个过渡操作符都会处理每个发射出的值然后再交给末端操作符。

    请参见以下示例，该示例过滤偶数并将其映射到字符串：


    ​
    (1..5).asFlow()
            .filter {
                println("Filter $it")
                it % 2 == 0
            }
            .map {
                println("Map $it")
                "string $it"
            }.collect {
                println("Collect $it")
            }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    执行：

    Filter 1
    Filter 2
    Map 2
    Collect string 2
    Filter 3
    Filter 4
    Map 4
    Collect string 4
    Filter 5
    流上下文

    流的收集总是在调用协程的上下文中发生。例如，如果有一个流 simple，
    然后以下代码在它的编写者指定的上下文中运行，
    而无论流 simple 的实现细节如何：


    withContext(context) {
        simple().collect { value ->
            println(value) // 运行在指定上下文中
        }
    }
    流的该属性称为 上下文保存 。

    所以默认的，flow { ... } 构建器中的代码运行在相应流的收集器提供的上下文中。
    举例来说，考虑打印线程的一个 simple 函数的实现， 它被调用并发射三个数字：


    fun simple(): Flow<Int> = flow {
        log("Started simple flow")
        for (i in 1..3) {
            emit(i)
        }
    }
    ​
    fun main() = runBlocking<Unit> {
        simple().collect { value -> log("Collected $value") }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    运行这段代码：

    [main @coroutine#1] Started simple flow
    [main @coroutine#1] Collected 1
    [main @coroutine#1] Collected 2
    [main @coroutine#1] Collected 3
    由于 simple().collect 是在主线程调用的，那么 simple 的流主体也是在主线程调用的。
    这是快速运行或异步代码的理想默认形式，它不关心执行的上下文并且不会阻塞调用者。

    withContext 发出错误

            然而，长时间运行的消耗 CPU 的代码
            也许需要在 Dispatchers.Default 上下文中执行，
            并且更新 UI 的代码也许需要在 Dispatchers.Main 中执行。
            通常，withContext 用于在 Kotlin 协程中改变代码的上下文，
            但是 flow {...} 构建器中的代码必须遵循上下文保存属性，
            并且不允许从其他上下文中发射（emit）。

    尝试运行下面的代码：


    fun simple(): Flow<Int> = flow {
        // 在流构建器中更改消耗 CPU 代码的上下文的错误方式
        kotlinx.coroutines.withContext(Dispatchers.Default) {
            for (i in 1..3) {
                Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
                emit(i) // 发射下一个值
            }
        }
    }
    ​
    fun main() = runBlocking<Unit> {
        simple().collect { value -> println(value) }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    这段代码产生如下的异常：

    Exception in thread "main" java.lang.IllegalStateException: Flow invariant is violated:
    Flow was collected in [CoroutineId(1), "coroutine#1":BlockingCoroutine{Active}@5511c7f8, BlockingEventLoop@2eac3323],
    but emission happened in [CoroutineId(1), "coroutine#1":DispatchedCoroutine{Active}@2dae0000, Dispatchers.Default].
    Please refer to 'flow' documentation or use 'flowOn' instead
    at ...
    flowOn 操作符

            例外的是 flowOn 函数，该函数用于更改流发射的上下文。
            以下示例展示了更改流上下文的正确方法，
            该示例还通过打印相应线程的名字以展示它们的工作方式：


    fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
            log("Emitting $i")
            emit(i) // 发射下一个值
        }
    }.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式
    ​
    fun main() = runBlocking<Unit> {
        simple().collect { value ->
            log("Collected $value")
        }
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    注意，当收集发生在主线程中，flow { ... } 是如何在后台线程中工作的：

    这里要观察的另一件事是 flowOn 操作符已改变流的默认顺序性。
     现在收集发生在一个协程中（“coroutine#1”）
     而发射发生在运行于另一个线程中
     与收集协程并发运行的另一个协程（“coroutine#2”）中。
     当上游流必须改变其上下文中的 CoroutineDispatcher 的时候，
     flowOn 操作符创建了另一个协程。

    缓冲

    从收集流所花费的时间来看，将流的不同部分运行在不同的协程中将会很有帮助，
    特别是当涉及到长时间运行的异步操作时。
    例如，考虑一种情况， 一个 simple 流的发射很慢，
    它每花费 100 毫秒才产生一个元素；
    而收集器也非常慢， 需要花费 300 毫秒来处理元素。
    让我们看看从该流收集三个数字要花费多长时间：


    fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100) // 假装我们异步等待了 100 毫秒
            emit(i) // 发射下一个值
        }
    }
    ​
    fun main() = runBlocking<Unit> {
        val time = measureTimeMillis {
            simple().collect { value ->
                delay(300) // 假装我们花费 300 毫秒来处理它
                println(value)
            }
        }
        println("Collected in $time ms")
    }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    它会产生这样的结果，整个收集过程大约需要 1200 毫秒（3 个数字，每个花费 400 毫秒）：

    1
    2
    3
    Collected in 1220 ms
            我们可以在流上使用 buffer 操作符来并发运行这个 simple 流中发射元素的代码以及收集的代码， 而不是顺序运行它们：


    val time = measureTimeMillis {
        simple()
                .buffer() // 缓冲发射项，无需等待
                .collect { value ->
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println(value)
                }
    }
    println("Collected in $time ms")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    它产生了相同的数字，只是更快了，由于我们高效地创建了处理流水线，
    仅仅需要等待第一个数字产生的 100 毫秒
    以及处理每个数字各需花费的 300 毫秒。
    这种方式大约花费了 1000 毫秒来运行：

    1
    2
    3
    Collected in 1071 ms
            注意，当必须更改 CoroutineDispatcher 时，
            flowOn 操作符使用了相同的缓冲机制，
            但是我们在这里显式地请求缓冲而不改变执行上下文。
    合并

    当流代表部分操作结果或操作状态更新时，
    可能没有必要处理每个值，而是只处理最新的那个。
    在本示例中，当收集器处理它们太慢的时候，
    conflate 操作符可以用于跳过中间值。构建前面的示例：


    val time = measureTimeMillis {
        simple()
                .conflate() // 合并发射项，不对每个值进行处理
                .collect { value ->
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println(value)
                }
    }
    println("Collected in $time ms")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    我们看到，虽然第一个数字仍在处理中，但第二个和第三个数字已经产生，
    因此第二个是 conflated ，只有最新的（第三个）被交付给收集器：

    1
    3
    Collected in 758 ms
            处理最新值

    当发射器和收集器都很慢的时候，合并是加快处理速度的一种方式。
    它通过删除发射值来实现。
    另一种方式是取消缓慢的收集器，
    并在每次发射新值的时候重新启动它。
    有一组与 xxx 操作符执行相同基本逻辑的 xxxLatest 操作符，
    但是在新值产生的时候取消执行其块中的代码。
    让我们在先前的示例中尝试更换 conflate 为 collectLatest：


    val time = measureTimeMillis {
        simple()
                .collectLatest { value -> // 取消并重新发射最后一个值
                    println("Collecting $value")
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println("Done $value")
                }
    }
    println("Collected in $time ms")
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    由于 collectLatest 的函数体需要花费 300 毫秒，
    但是新值每 100 秒发射一次，我们看到该代码块对每个值运行，但是只收集最后一个值：

    Collecting 1
    Collecting 2
    Collecting 3
    Done 3
    Collected in 741 ms
            组合多个流

    组合多个流有很多种方式。

    Zip

    就像 Kotlin 标准库中的 Sequence.zip 扩展函数一样， 流拥有一个 zip 操作符用于组合两个流中的相关值：


    ​
    val nums = (1..3).asFlow() // 数字 1..3
    val strs = flowOf("one", "two", "three") // 字符串
    nums.zip(strs) { a, b -> "$a -> $b" } // 组合单个字符串
            .collect { println(it) } // 收集并打印
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    示例打印如下：

    1 -> one
    2 -> two
    3 -> three
    Combine

    当流表示一个变量或操作的最新值时（请参阅相关小节 conflation），
    可能需要执行计算，这依赖于相应流的最新值，并且每当上游流产生值的时候都需要重新计算。
    这种相应的操作符家族称为 combine。

    例如，先前示例中的数字如果每 300 毫秒更新一次，但字符串每 400 毫秒更新一次，
     然后使用 zip 操作符合并它们，但仍会产生相同的结果， 尽管每 400 毫秒打印一次结果：

    我们在本示例中使用 onEach 过渡操作符来延时每次元素发射并使该流更具说明性以及更简洁。

    ​
    val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
    val startTime = System.currentTimeMillis() // 记录开始的时间
    nums.zip(strs) { a, b -> "$a -> $b" } // 使用“zip”组合单个字符串
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    然而，当在这里使用 combine 操作符来替换 zip：


    ​
    val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
    val startTime = System.currentTimeMillis() // 记录开始的时间
    nums.combine(strs) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    我们得到了完全不同的输出，其中，nums 或 strs 流中的每次发射都会打印一行：

    1 -> one at 452 ms from start
    2 -> one at 651 ms from start
    2 -> two at 854 ms from start
    3 -> two at 952 ms from start
    3 -> three at 1256 ms from start
    展平流

    流表示异步接收的值序列，所以很容易遇到这样的情况：
    每个值都会触发对另一个值序列的请求。
    比如说，我们可以拥有下面这样一个返回间隔 500 毫秒的两个字符串流的函数：


    fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // 等待 500 毫秒
        emit("$i: Second")
    }
    现在，如果我们有一个包含三个整数的流，并为每个整数调用 requestFlow，如下所示：


    (1..3).asFlow().map { requestFlow(it) }
    然后我们得到了一个包含流的流（Flow<Flow<String>>），
    需要将其进行展平为单个流以进行下一步处理。
    集合与序列都拥有 flatten 与 flatMap 操作符来做这件事。
    然而，由于流具有异步的性质，因此需要不同的展平模式， 为此，存在一系列的流展平操作符。

    flatMapConcat

    连接模式由 flatMapConcat 与 flattenConcat 操作符实现。
    它们是相应序列操作符最相近的类似物。它们在等待内部流完成之前开始收集下一个值，如下面的示例所示：


    val startTime = System.currentTimeMillis() // 记录开始时间
    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
            .flatMapConcat { requestFlow(it) }
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    在输出中可以清楚地看到 flatMapConcat 的顺序性质：

    1: First at 121 ms from start
    1: Second at 622 ms from start
    2: First at 727 ms from start
    2: Second at 1227 ms from start
    3: First at 1328 ms from start
    3: Second at 1829 ms from start
    flatMapMerge

    另一种展平模式是并发收集所有传入的流，并将它们的值合并到一个单独的流，
    以便尽快的发射值。
     它由 flatMapMerge 与 flattenMerge 操作符实现。
     他们都接收可选的用于限制并发收集的流的个数的 concurrency 参数（默认情况下，它等于 DEFAULT_CONCURRENCY）。


    val startTime = System.currentTimeMillis() // 记录开始时间
    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
            .flatMapMerge { requestFlow(it) }
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    flatMapMerge 的并发性质很明显：

    1: First at 136 ms from start
    2: First at 231 ms from start
    3: First at 333 ms from start
    1: Second at 639 ms from start
    2: Second at 732 ms from start
    3: Second at 833 ms from start
    注意，flatMapMerge 会顺序调用代码块（本示例中的 { requestFlow(it) }），
    但是并发收集结果流，相当于执行顺序是首先执行 map { requestFlow(it) } 然后在其返回结果上调用 flattenMerge。
    flatMapLatest

    与 collectLatest 操作符类似（在"处理最新值" 小节中已经讨论过），
    也有相对应的“最新”展平模式，在发出新流后立即取消先前流的收集。
    这由 flatMapLatest 操作符来实现。


    val startTime = System.currentTimeMillis() // 记录开始时间
    (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
            .flatMapLatest { requestFlow(it) }
            .collect { value -> // 收集并打印
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    Target platform: JVMRunning on kotlin v. 1.6.0
    可以在这里获取完整代码。
    该示例的输出很好的展示了 flatMapLatest 的工作方式：

    1: First at 142 ms from start
    2: First at 322 ms from start
    3: First at 425 ms from start
    3: Second at 931 ms from start
    注意，flatMapLatest 在一个新值到来时取消了块中的所有代码 (本示例中的 { requestFlow(it) }）。
     这在该特定示例中不会有什么区别，由于调用 requestFlow 自身的速度是很快的，
     不会发生挂起， 所以不会被取消。然而，如果我们要在块中调用诸如 delay 之类的挂起函数，这将会被表现出来。
    流异常

    当运算符中的发射器或代码抛出异常时，流收集可以带有异常的完成。 有几种处理异常的方法。

    收集器 try 与 catch

            收集者可以使用 Kotlin 的 try/catch 块来处理异常：


        fun simple(): Flow<Int> = flow {
            for (i in 1..3) {
                println("Emitting $i")
                emit(i) // 发射下一个值
            }
        }
        ​
        fun main() = runBlocking<Unit> {
            try {
                simple().collect { value ->
                    println(value)
                    check(value <= 1) { "Collected $value" }
                }
            } catch (e: Throwable) {
                println("Caught $e")
            }
        }
        Target platform: JVMRunning on kotlin v. 1.6.0
        可以在这里获取完整代码。
        这段代码成功的在末端操作符 collect 中捕获了异常，并且， 如我们所见，在这之后不再发出任何值：

        Emitting 1
        1
        Emitting 2
        2
        Caught java.lang.IllegalStateException: Collected 2
        一切都已捕获

        前面的示例实际上捕获了在发射器或任何过渡或末端操作符中发生的任何异常。
        例如，让我们修改代码以便将发出的值映射为字符串， 但是相应的代码会产生一个异常：


        fun simple(): Flow<String> =
                flow {
                    for (i in 1..3) {
                        println("Emitting $i")
                        emit(i) // 发射下一个值
                    }
                }
                        .map { value ->
                            check(value <= 1) { "Crashed on $value" }
                            "string $value"
                        }
        ​
        fun main() = runBlocking<Unit> {
            try {
                simple().collect { value -> println(value) }
            } catch (e: Throwable) {
                println("Caught $e")
            }
        }
        Target platform: JVMRunning on kotlin v. 1.6.0
        可以在这里获取完整代码。
        仍然会捕获该异常并停止收集：

        Emitting 1
        string 1
        Emitting 2
        Caught java.lang.IllegalStateException: Crashed on 2
        异常透明性

        但是，发射器的代码如何封装其异常处理行为？

        流必须对异常透明，即在 flow { ... }
        构建器内部的 try/catch 块中发射值是违反异常透明性的。
        这样可以保证收集器抛出的一个异常能被像先前示例中那样的 try/catch 块捕获。

            发射器可以使用 catch 操作符来保留此异常的透明性并允许封装它的异常处理。
            catch 操作符的代码块可以分析异常并根据捕获到的异常以不同的方式对其做出反应：

            可以使用 throw 重新抛出异常。
            可以使用 catch 代码块中的 emit 将异常转换为值发射出去。
            可以将异常忽略，或用日志打印，或使用一些其他代码处理它。
            例如，让我们在捕获异常的时候发射文本：


            simple()
                    .catch { e -> emit("Caught $e") } // 发射一个异常
                    .collect { value -> println(value) }
            Target platform: JVMRunning on kotlin v. 1.6.0
            可以在这里获取完整代码。
            即使我们不再在代码的外层使用 try/catch，示例的输出也是相同的。

                透明捕获

                catch 过渡操作符遵循异常透明性，
                仅捕获上游异常（catch 操作符上游的异常，但是它下面的不是）。
                 如果 collect { ... } 块（位于 catch 之下）抛出一个异常，那么异常会逃逸：


                fun simple(): Flow<Int> = flow {
                    for (i in 1..3) {
                        println("Emitting $i")
                        emit(i)
                    }
                }
                ​
                fun main() = runBlocking<Unit> {
                    simple()
                            .catch { e -> println("Caught $e") } // 不会捕获下游异常
                            .collect { value ->
                                check(value <= 1) { "Collected $value" }
                                println(value)
                            }
                }
                Target platform: JVMRunning on kotlin v. 1.6.0
                可以在这里获取完整代码。
                尽管有 catch 操作符，但不会打印“Caught …”消息：

                Emitting 1
                1
                Emitting 2
                Exception in thread "main" java.lang.IllegalStateException: Collected 2
                at ...
                声明式捕获

                我们可以将 catch 操作符的声明性与处理所有异常的期望相结合，
                将 collect 操作符的代码块移动到 onEach 中，
                并将其放到 catch 操作符之前。
                收集该流必须由调用无参的 collect() 来触发：
                simple()
                        .onEach { value ->
                            check(value <= 1) { "Collected $value" }
                            println(value)
                        }
                        .catch { e -> println("Caught $e") }
                        .collect()
                Target platform: JVMRunning on kotlin v. 1.6.0
                可以在这里获取完整代码。
                现在我们可以看到已经打印了“Caught …”消息，并且我们可以在没有显式使用 try/catch 块的情况下捕获所有异常：

                Emitting 1
                1
                Emitting 2
                Caught java.lang.IllegalStateException: Collected 2
                流完成

                当流收集完成时（普通情况或异常情况），它可能需要执行一个动作。
                你可能已经注意到，它可以通过两种方式完成：命令式或声明式。

                命令式 finally 块

                除了 try/catch 之外，收集器还能使用 finally 块在 collect 完成时执行一个动作。


                    fun simple(): Flow<Int> = (1..3).asFlow()
                    ​
                    fun main() = runBlocking<Unit> {
                        try {
                            simple().collect { value -> println(value) }
                        } finally {
                            println("Done")
                        }
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码打印出 simple 流产生的三个数字，后面跟一个“Done”字符串：

                    1
                    2
                    3
                    Done
                    声明式处理

                    对于声明式，流拥有 onCompletion 过渡操作符，它在流完全收集时调用。

                    可以使用 onCompletion 操作符重写前面的示例，并产生相同的输出：
                    simple()
                            .onCompletion { println("Done") }
                            .collect { value -> println(value) }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    onCompletion 的主要优点是其 lambda 表达式的可空参数
                     Throwable 可以用于确定流收集是正常完成还是有异常发生。
                     在下面的示例中 simple 流在发射数字 1 之后抛出了一个异常：


                    fun simple(): Flow<Int> = flow {
                        emit(1)
                        throw RuntimeException()
                    }
                    ​
                    fun main() = runBlocking<Unit> {
                        simple()
                                .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
                                .catch { cause -> println("Caught exception") }
                                .collect { value -> println(value) }
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    如你所期望的，它打印了：

                    1
                    Flow completed exceptionally
                    Caught exception
                            onCompletion 操作符与 catch 不同，
                            它不处理异常。我们可以看到前面的示例代码，
                            异常仍然流向下游。它将被提供给后面的 onCompletion 操作符，并可以由 catch 操作符处理。

                    成功完成

                    与 catch 操作符的另一个不同点是
                    onCompletion 能观察到所有异常并且仅在上游流成功完成（没有取消或失败）的情况下接收一个 null 异常。


                    fun simple(): Flow<Int> = (1..3).asFlow()
                    ​
                    fun main() = runBlocking<Unit> {
                        simple()
                                .onCompletion { cause -> println("Flow completed with $cause") }
                                .collect { value ->
                                    check(value <= 1) { "Collected $value" }
                                    println(value)
                                }
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    我们可以看到完成时 cause 不为空，因为流由于下游异常而中止：

                    1
                    Flow completed with java.lang.IllegalStateException: Collected 2
                    Exception in thread "main" java.lang.IllegalStateException: Collected 2
                    命令式还是声明式

                    现在我们知道如何收集流，并以命令式与声明式的方式处理其完成及异常情况。
                    这里有一个很自然的问题是，哪种方式应该是首选的？为什么？
                     作为一个库，我们不主张采用任何特定的方式，
                     并且相信这两种选择都是有效的， 应该根据自己的喜好与代码风格进行选择。

                    启动流

                    使用流表示来自一些源的异步事件是很简单的。
                     在这个案例中，我们需要一个类似 addEventListener 的函数，
                     该函数注册一段响应的代码处理即将到来的事件，
                     并继续进行进一步的处理。onEach 操作符可以担任该角色。
                     然而，onEach 是一个过渡操作符。
                     我们也需要一个末端操作符来收集流。 否则仅调用 onEach 是无效的。

                    如果我们在 onEach 之后使用 collect 末端操作符，那么后面的代码会一直等待直至流被收集：
                    // 模仿事件流
                    fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }
                    ​
                    fun main() = runBlocking<Unit> {
                        events()
                                .onEach { event -> println("Event: $event") }
                                .collect() // <--- 等待流收集
                        println("Done")
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    你可以看到它的输出：

                    Event: 1
                    Event: 2
                    Event: 3
                    Done
                    launchIn 末端操作符可以在这里派上用场。
                    使用 launchIn 替换 collect 我们可以在单独的协程中启动流的收集，这样就可以立即继续进一步执行代码：
                    fun main() = runBlocking<Unit> {
                        events()
                                .onEach { event -> println("Event: $event") }
                                .launchIn(this) // <--- 在单独的协程中执行流
                        println("Done")
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    它打印了：

                    Done
                    Event: 1
                    Event: 2
                    Event: 3
                    launchIn 必要的参数 CoroutineScope
                    指定了用哪一个协程来启动流的收集。
                    在先前的示例中这个作用域来自 runBlocking 协程构建器，
                    在这个流运行的时候，runBlocking 作用域等待它的子协程执行完毕并防止 main 函数返回并终止此示例。

                    在实际的应用中，作用域来自于一个寿命有限的实体。
                    在该实体的寿命终止后，相应的作用域就会被取消，
                    即取消相应流的收集。这种成对的 onEach { ... }.launchIn(scope)
                    工作方式就像 addEventListener 一样。而且，
                    这不需要相应的 removeEventListener 函数，
                    因为取消与结构化并发可以达成这个目的。

                    注意，launchIn 也会返回一个 Job，可以在不取消整个作用域的情况下仅取消相应的流收集或对其进行 join。

                    流取消检测

                    为方便起见，流构建器对每个发射值执行附加的 ensureActive
                    检测以进行取消。 这意味着从 flow { ... } 发出的繁忙循环是可以取消的：



                    fun foo(): Flow<Int> = flow {
                        for (i in 1..5) {
                            println("Emitting $i")
                            emit(i)
                        }
                    }
                    ​
                    fun main() = runBlocking<Unit> {
                        foo().collect { value ->
                            if (value == 3) cancel()
                            println(value)
                        }
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整的代码。
                    仅得到不超过 3 的数字，在尝试发出 4 之后抛出 CancellationException；

                    Emitting 1
                    1
                    Emitting 2
                    2
                    Emitting 3
                    3
                    Emitting 4
                    Exception in thread "main" kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled; job="coroutine#1":BlockingCoroutine{Cancelled}@6d7b4f4c
                    但是，出于性能原因，大多数其他流操作不会自行执行其他取消检测。 例如，如果使用 IntRange.asFlow 扩展来编写相同的繁忙循环， 并且没有在任何地方暂停，那么就没有取消的检测；



                    fun main() = runBlocking<Unit> {
                        (1..5).asFlow().collect { value ->
                            if (value == 3) cancel()
                            println(value)
                        }
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整的代码。
                    收集从 1 到 5 的所有数字，并且仅在从 runBlocking 返回之前检测到取消：

                    1
                    2
                    3
                    4
                    5
                    Exception in thread "main" kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled; job="coroutine#1":BlockingCoroutine{Cancelled}@3327bd23
                    让繁忙的流可取消

                    在协程处于繁忙循环的情况下，必须明确检测是否取消。
                    、
可以添加 .onEach { currentCoroutineContext().ensureActive() }， 但是这里提供了一个现成的 cancellable 操作符来执行此操作：



                    fun main() = runBlocking<Unit> {
                        (1..5).asFlow().cancellable().collect { value ->
                            if (value == 3) cancel()
                            println(value)
                        }
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整的代码。
                    使用 cancellable 操作符，仅收集从 1 到 3 的数字：

                    1
                    2
                    3
                    Exception in thread "main" kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled; job="coroutine#1":BlockingCoroutine{Cancelled}@5ec0a365
                    流（Flow）与响应式流（Reactive Streams）

                    对于熟悉响应式流（Reactive Streams）或诸如
                    RxJava 与 Project Reactor 这样的响应式框架的人来说，
                    Flow 的设计也许看起来会非常熟悉。

                    确实，其设计灵感来源于响应式流以及其各种实现。
                    但是 Flow 的主要目标是拥有尽可能简单的设计，
                    对 Kotlin 以及挂起友好且遵从结构化并发。
                    没有响应式的先驱及他们大量的工作，
                    就不可能实现这一目标。你可以阅读 Reactive Streams and Kotlin Flows 这篇文章来了解完成 Flow 的故事。

                    虽然有所不同，但从概念上讲，
                    Flow 依然是响应式流，
                    并且可以将它转换为响应式（规范及符合 TCK）的发布者（Publisher），
                    反之亦然。 这些开箱即用的转换器可以在 kotlinx.coroutines 提供的相关响应式模块
                    （kotlinx-coroutines-reactive 用于 Reactive Streams，
                    kotlinx-coroutines-reactor 用于 Project Reactor，
                    以及 kotlinx-coroutines-rx2/kotlinx-coroutines-rx3
                    用于 RxJava2/RxJava3）中找到。
                    集成模块包含 Flow 与其他实现之间的转换，
                    与 Reactor 的 Context 集成以及与一系列响应式实体配合使用的挂起友好的使用方式。
*/
}

/**
 *通道
 */
fun coroutines6() {
    /*     通道

                    延期的值提供了一种便捷的方法使单个值在多个协程之间进行相互传输。 通道提供了一种在流中传输值的方法。

                    通道基础

                    一个 Channel 是一个和 BlockingQueue 非常相似的概念。
                    其中一个不同是它代替了阻塞的 put 操作并提供了挂起的 send，
                    还替代了阻塞的 take 操作并提供了挂起的 receive。


                    val channel = Channel<Int>()
                    launch {
                        // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
                        for (x in 1..5) channel.send(x * x)
                    }
// 这里我们打印了 5 次被接收的整数：
                    repeat(5) { println(channel.receive()) }
                    println("Done!")
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    1
                    4
                    9
                    16
                    25
                    Done!
                    关闭与迭代通道

                    和队列不同，一个通道可以通过被关闭来表明没有更多的元素将会进入通道。
                    在接收者中可以定期的使用 for 循环来从通道中接收元素。

                    从概念上来说，一个 close 操作就像向通道发送了一个特殊的关闭指令。 这
                    个迭代停止就说明关闭指令已经被接收了。所以这里保证所有先前发送出去的元素都在通道关闭前被接收到。


                    val channel = Channel<Int>()
                    launch {
                        for (x in 1..5) channel.send(x * x)
                        channel.close() // 我们结束发送
                    }
// 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
                    for (y in channel) println(y)
                    println("Done!")
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    构建通道生产者

                    协程生成一系列元素的模式很常见。 这是 生产者——消费者 模式的一部分，
                    并且经常能在并发的代码中看到它。 你可以将生产者抽象成一个函数，并且使通道作为它的参数，但这与必须从函数中返回结果的常识相违悖。

                    这里有一个名为 produce 的便捷的协程构建器，可以很容易的在生产者端正确工作，
                    并且我们使用扩展函数 consumeEach 在消费者端替代 for 循环：
                    val squares = produceSquares()
                    squares.consumeEach { println(it) }
                    println("Done!")
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    管道

                    管道是一种一个协程在流中开始生产可能无穷多个元素的模式：


                    fun CoroutineScope.produceNumbers() = produce<Int> {
                        var x = 1
                        while (true) send(x++) // 在流中开始从 1 生产无穷多个整数
                    }
                    并且另一个或多个协程开始消费这些流，做一些操作，并生产了一些额外的结果。 在下面的例子中，对这些数字仅仅做了平方操作：


                    fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
                        for (x in numbers) send(x * x)
                    }
                    主要的代码启动并连接了整个管道：


                    val numbers = produceNumbers() // 从 1 开始生成整数
                    val squares = square(numbers) // 整数求平方
                    repeat(5) {
                        println(squares.receive()) // 输出前五个
                    }
                    println("Done!") // 至此已完成
                    coroutineContext.cancelChildren() // 取消子协程
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    所有创建了协程的函数被定义在了 CoroutineScope 的扩展上， 所以我们可以依靠结构化并发来确保没有常驻在我们的应用程序中的全局协程。
                    使用管道的素数

                    让我们来展示一个极端的例子——在协程中使用一个管道来生成素数。我们开启了一个数字的无限序列。


                    fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
                        var x = start
                        while (true) send(x++) // 开启了一个无限的整数流
                    }
                    在下面的管道阶段中过滤了来源于流中的数字，删除了所有可以被给定素数整除的数字。


                    fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
                        for (x in numbers) if (x % prime != 0) send(x)
                    }
                    现在我们开启了一个从 2 开始的数字流管道，从当前的通道中取一个素数， 并为每一个我们发现的素数启动一个流水线阶段：

                    numbersFrom(2) -> filter(2) -> filter(3) -> filter(5) -> filter(7) ……
                    下面的例子打印了前十个素数， 在主线程的上下文中运行整个管道。直到所有的协程在该主协程 runBlocking 的作用域中被启动完成。 我们不必使用一个显式的列表来保存所有被我们已经启动的协程。 我们使用 cancelChildren 扩展函数在我们打印了前十个素数以后来取消所有的子协程。


                    var cur = numbersFrom(2)
                    repeat(10) {
                        val prime = cur.receive()
                        println(prime)
                        cur = filter(cur, prime)
                    }
                    coroutineContext.cancelChildren() // 取消所有的子协程来让主协程结束
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    2
                    3
                    5
                    7
                    11
                    13
                    17
                    19
                    23
                    29
                    注意，你可以在标准库中使用 iterator 协程构建器来构建一个相似的管道。 使用 iterator 替换 produce、yield 替换 send、next 替换 receive、 Iterator 替换 ReceiveChannel 来摆脱协程作用域，你将不再需要 runBlocking。 然而，如上所示，如果你在 Dispatchers.Default 上下文中运行它，使用通道的管道的好处在于它可以充分利用多核心 CPU。

                    不过，这是一种非常不切实际的寻找素数的方法。在实践中，管道调用了另外的一些挂起中的调用（就像异步调用远程服务）并且这些管道不能内置使用 sequence/iterator，因为它们不被允许随意的挂起，不像 produce 是完全异步的。

                    扇出

                    多个协程也许会接收相同的管道，在它们之间进行分布式工作。 让我们启动一个定期产生整数的生产者协程 （每秒十个数字）：


                    fun CoroutineScope.produceNumbers() = produce<Int> {
                        var x = 1 // 从 1 开始
                        while (true) {
                            send(x++) // 产生下一个数字
                            delay(100) // 等待 0.1 秒
                        }
                    }
                    接下来我们可以得到几个处理器协程。在这个示例中，它们只是打印它们的 id 和接收到的数字：


                    fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
                        for (msg in channel) {
                            println("Processor #$id received $msg")
                        }
                    }
                    现在让我们启动五个处理器协程并让它们工作将近一秒。看看发生了什么：


                    val producer = produceNumbers()
                    repeat(5) { launchProcessor(it, producer) }
                    delay(950)
                    producer.cancel() // 取消协程生产者从而将它们全部杀死
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    该输出将类似于如下所示，尽管接收每个特定整数的处理器 id 可能会不同：

                    Processor #2 received 1
                    Processor #4 received 2
                    Processor #0 received 3
                    Processor #1 received 4
                    Processor #3 received 5
                    Processor #2 received 6
                    Processor #4 received 7
                    Processor #0 received 8
                    Processor #1 received 9
                    Processor #3 received 10
                    注意，取消生产者协程将关闭它的通道，从而最终终止处理器协程正在执行的此通道上的迭代。

                    还有，注意我们如何使用 for 循环显式迭代通道以在 launchProcessor 代码中执行扇出。 与 consumeEach 不同，这个 for 循环是安全完美地使用多个协程的。如果其中一个处理器协程执行失败，其它的处理器协程仍然会继续处理通道，而通过 consumeEach 编写的处理器始终在正常或非正常完成时消耗（取消）底层通道。

                    扇入

                    多个协程可以发送到同一个通道。 比如说，让我们创建一个字符串的通道，和一个在这个通道中以指定的延迟反复发送一个指定字符串的挂起函数：


                    suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
                        while (true) {
                            delay(time)
                            channel.send(s)
                        }
                    }
                    现在，我们启动了几个发送字符串的协程，让我们看看会发生什么 （在示例中，我们在主线程的上下文中作为主协程的子协程来启动它们）：


                    val channel = Channel<String>()
                    launch { sendString(channel, "foo", 200L) }
                    launch { sendString(channel, "BAR!", 500L) }
                    repeat(6) { // 接收前六个
                        println(channel.receive())
                    }
                    coroutineContext.cancelChildren() // 取消所有子协程来让主协程结束
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    输出如下：

                    foo
                    foo
                    BAR!
                    foo
                    foo
                    BAR!
                    带缓冲的通道

                    到目前为止展示的通道都是没有缓冲区的。无缓冲的通道在发送者和接收者相遇时传输元素（也称“对接”）。如果发送先被调用，则它将被挂起直到接收被调用， 如果接收先被调用，它将被挂起直到发送被调用。

                    Channel() 工厂函数与 produce 建造器通过一个可选的参数 capacity 来指定 缓冲区大小 。缓冲允许发送者在被挂起前发送多个元素， 就像 BlockingQueue 有指定的容量一样，当缓冲区被占满的时候将会引起阻塞。

                    看看如下代码的表现：


                    val channel = Channel<Int>(4) // 启动带缓冲的通道
                    val sender = launch { // 启动发送者协程
                        repeat(10) {
                            println("Sending $it") // 在每一个元素发送前打印它们
                            channel.send(it) // 将在缓冲区被占满时挂起
                        }
                    }
// 没有接收到东西……只是等待……
                    delay(1000)
                    sender.cancel() // 取消发送者协程
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    使用缓冲通道并给 capacity 参数传入 四 它将打印“sending” 五 次：

                    Sending 0
                    Sending 1
                    Sending 2
                    Sending 3
                    Sending 4
                    前四个元素被加入到了缓冲区并且发送者在试图发送第五个元素的时候被挂起。

                    通道是公平的

                    发送和接收操作是 公平的 并且尊重调用它们的多个协程。它们遵守先进先出原则，可以看到第一个协程调用 receive 并得到了元素。在下面的例子中两个协程“乒”和“乓”都从共享的“桌子”通道接收到这个“球”元素。


                    data class Ball(var hits: Int)
                    ​
                    fun main() = runBlocking {
                        val table = Channel<Ball>() // 一个共享的 table（桌子）
                        launch { player("ping", table) }
                        launch { player("pong", table) }
                        table.send(Ball(0)) // 乒乓球
                        delay(1000) // 延迟 1 秒钟
                        coroutineContext.cancelChildren() // 游戏结束，取消它们
                    }
                    ​
                    suspend fun player(name: String, table: Channel<Ball>) {
                        for (ball in table) { // 在循环中接收球
                            ball.hits++
                            println("$name $ball")
                            delay(300) // 等待一段时间
                            table.send(ball) // 将球发送回去
                        }
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里得到完整代码
                    “乒”协程首先被启动，所以它首先接收到了球。甚至虽然“乒” 协程在将球发送会桌子以后立即开始接收，但是球还是被“乓” 协程接收了，因为它一直在等待着接收球：

                    ping Ball(hits=1)
                    pong Ball(hits=2)
                    ping Ball(hits=3)
                    pong Ball(hits=4)
                    注意，有时候通道执行时由于执行者的性质而看起来不那么公平。点击这个提案来查看更多细节。

                    计时器通道

                    计时器通道是一种特别的会合通道，每次经过特定的延迟都会从该通道进行消费并产生 Unit。 虽然它看起来似乎没用，它被用来构建分段来创建复杂的基于时间的 produce 管道和进行窗口化操作以及其它时间相关的处理。 可以在 select 中使用计时器通道来进行“打勾”操作。

                    使用工厂方法 ticker 来创建这些通道。 为了表明不需要其它元素，请使用 ReceiveChannel.cancel 方法。

                    现在让我们看看它是如何在实践中工作的：


                    import kotlinx.coroutines.*
                            import kotlinx.coroutines.channels.*
                    ​
                    fun main() = runBlocking<Unit> {
                        val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) //创建计时器通道
                        var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
                        println("Initial element is available immediately: $nextElement") // no initial delay
                        ​
                        nextElement = withTimeoutOrNull(50) { tickerChannel.receive() } // all subsequent elements have 100ms delay
                        println("Next element is not ready in 50 ms: $nextElement")
                        ​
                        nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
                        println("Next element is ready in 100 ms: $nextElement")
                        ​
                        // 模拟大量消费延迟
                        println("Consumer pauses for 150ms")
                        delay(150)
                        // 下一个元素立即可用
                        nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
                        println("Next element is available immediately after large consumer delay: $nextElement")
                        // 请注意，`receive` 调用之间的暂停被考虑在内，下一个元素的到达速度更快
                        nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
                        println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")
                        ​
                        tickerChannel.cancel() // 表明不再需要更多的元素
                    }
                    可以在这里获取完整代码。
                    它的打印如下：

                    Initial element is available immediately: kotlin.Unit
                    Next element is not ready in 50 ms: null
                    Next element is ready in 100 ms: kotlin.Unit
                    Consumer pauses for 150ms
                            Next element is available immediately after large consumer delay: kotlin.Unit
                    Next element is ready in 50ms after consumer pause in 150ms: kotlin.Unit
                    请注意，ticker 知道可能的消费者暂停，并且默认情况下会调整下一个生成的元素如果发生暂停则延迟，试图保持固定的生成元素率。

                    给可选的 mode 参数传入 TickerMode.FIXED_DELAY 可以保持固定元素之间的延迟。*/
}

/**
 *异常处理与监督
 */
fun coroutines7() {
    /*

                    异常处理

                    本节内容涵盖了异常处理与在异常上取消。 我们已经知道被取消的协程会在挂起点抛出 CancellationException 并且它会被协程的机制所忽略。在这里我们会看看在取消过程中抛出异常或同一个协程的多个子协程抛出异常时会发生什么。

                    异常的传播

                    协程构建器有两种形式：自动传播异常（launch 与 actor）或向用户暴露异常（async 与 produce）。 当这些构建器用于创建一个根协程时，即该协程不是另一个协程的子协程， 前者这类构建器将异常视为未捕获异常，类似 Java 的 Thread.uncaughtExceptionHandler， 而后者则依赖用户来最终消费异常，例如通过 await 或 receive（produce 与 receive 的相关内容包含于通道章节）。

                    可以通过一个使用 GlobalScope 创建根协程的简单示例来进行演示：


                    import kotlinx.coroutines.*
                    ​
                    fun main() = runBlocking {
                        val job = GlobalScope.launch { // launch 根协程
                            println("Throwing exception from launch")
                            throw IndexOutOfBoundsException() // 我们将在控制台打印 Thread.defaultUncaughtExceptionHandler
                        }
                        job.join()
                        println("Joined failed job")
                        val deferred = GlobalScope.async { // async 根协程
                            println("Throwing exception from async")
                            throw ArithmeticException() // 没有打印任何东西，依赖用户去调用等待
                        }
                        try {
                            deferred.await()
                            println("Unreached")
                        } catch (e: ArithmeticException) {
                            println("Caught ArithmeticException")
                        }
                    }
                    可以在这里获取完整代码。
                    这段代码的输出如下（调试）：

                    Throwing exception from launch
                            Exception in thread "DefaultDispatcher-worker-2 @coroutine#2" java.lang.IndexOutOfBoundsException
                    Joined failed job
                    Throwing exception from async
                            Caught ArithmeticException
                            CoroutineExceptionHandler

                    将未捕获异常打印到控制台的默认行为是可自定义的。 根协程中的 CoroutineExceptionHandler 上下文元素可以被用于这个根协程通用的 catch 块，及其所有可能自定义了异常处理的子协程。 它类似于 Thread.uncaughtExceptionHandler 。 你无法从 CoroutineExceptionHandler 的异常中恢复。当调用处理者的时候，协程已经完成并带有相应的异常。通常，该处理者用于记录异常，显示某种错误消息，终止和（或）重新启动应用程序。

                    在 JVM 中可以重定义一个全局的异常处理者来将所有的协程通过 ServiceLoader 注册到 CoroutineExceptionHandler。 全局异常处理者就如同 Thread.defaultUncaughtExceptionHandler 一样，在没有更多的指定的异常处理者被注册的时候被使用。 在 Android 中，uncaughtExceptionPreHandler 被设置在全局协程异常处理者中。

                    CoroutineExceptionHandler 仅在未捕获的异常上调用 — 没有以其他任何方式处理的异常。 特别是，所有子协程（在另一个 Job 上下文中创建的协程）委托<!– 它们的父协程处理它们的异常，然后它们也委托给其父协程，以此类推直到根协程， 因此永远不会使用在其上下文中设置的 CoroutineExceptionHandler。 除此之外，async 构建器始终会捕获所有异常并将其表示在结果 Deferred 对象中， 因此它的 CoroutineExceptionHandler 也无效。

                    在监督作用域内运行的协程不会将异常传播到其父协程，并且会从此规则中排除。本文档的另一个小节——监督提供了更多细节。

                    val handler = CoroutineExceptionHandler { _, exception ->
                        println("CoroutineExceptionHandler got $exception")
                    }
                    val job = GlobalScope.launch(handler) { // 根协程，运行在 GlobalScope 中
                        throw AssertionError()
                    }
                    val deferred = GlobalScope.async(handler) { // 同样是根协程，但使用 async 代替了 launch
                        throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()
                    }
                    joinAll(job, deferred)
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    CoroutineExceptionHandler got java.lang.AssertionError
                    取消与异常

                    取消与异常紧密相关。协程内部使用 CancellationException 来进行取消，这个异常会被所有的处理者忽略，所以那些可以被 catch 代码块捕获的异常仅仅应该被用来作为额外调试信息的资源。 当一个协程使用 Job.cancel 取消的时候，它会被终止，但是它不会取消它的父协程。


                    val job = launch {
                        val child = launch {
                            try {
                                delay(Long.MAX_VALUE)
                            } finally {
                                println("Child is cancelled")
                            }
                        }
                        yield()
                        println("Cancelling child")
                        child.cancel()
                        child.join()
                        yield()
                        println("Parent is not cancelled")
                    }
                    job.join()
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    Cancelling child
                            Child is cancelled
                    Parent is not cancelled
                    如果一个协程遇到了 CancellationException 以外的异常，它将使用该异常取消它的父协程。 这个行为无法被覆盖，并且用于为结构化的并发（structured concurrency） 提供稳定的协程层级结构。 CoroutineExceptionHandler 的实现并不是用于子协程。

                    在本例中，CoroutineExceptionHandler 总是被设置在由 GlobalScope 启动的协程中。将异常处理者设置在 runBlocking 主作用域内启动的协程中是没有意义的，尽管子协程已经设置了异常处理者， 但是主协程也总是会被取消的。
                    当父协程的所有子协程都结束后，原始的异常才会被父协程处理， 见下面这个例子。


                    val handler = CoroutineExceptionHandler { _, exception ->
                        println("CoroutineExceptionHandler got $exception")
                    }
                    val job = GlobalScope.launch(handler) {
                        launch { // 第一个子协程
                            try {
                                delay(Long.MAX_VALUE)
                            } finally {
                                withContext(NonCancellable) {
                                    println("Children are cancelled, but exception is not handled until all children terminate")
                                    delay(100)
                                    println("The first child finished its non cancellable block")
                                }
                            }
                        }
                        launch { // 第二个子协程
                            delay(10)
                            println("Second child throws an exception")
                            throw ArithmeticException()
                        }
                    }
                    job.join()
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    Second child throws an exception
                    Children are cancelled, but exception is not handled until all children terminate
                    The first child finished its non cancellable block
                            CoroutineExceptionHandler got java.lang.ArithmeticException
                    异常聚合

                    当协程的多个子协程因异常而失败时， 一般规则是“取第一个异常”，因此将处理第一个异常。 在第一个异常之后发生的所有其他异常都作为被抑制的异常绑定至第一个异常。


                    import kotlinx.coroutines.*
                            import java.io.*
                    ​
                    fun main() = runBlocking {
                        val handler = CoroutineExceptionHandler { _, exception ->
                            println("CoroutineExceptionHandler got $exception with suppressed ${exception.suppressed.contentToString()}")
                        }
                        val job = GlobalScope.launch(handler) {
                            launch {
                                try {
                                    delay(Long.MAX_VALUE) // 当另一个同级的协程因 IOException  失败时，它将被取消
                                } finally {
                                    throw ArithmeticException() // 第二个异常
                                }
                            }
                            launch {
                                delay(100)
                                throw IOException() // 首个异常
                            }
                            delay(Long.MAX_VALUE)
                        }
                        job.join()
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    注意：上面的代码将只在 JDK7 以上支持 suppressed 异常的环境中才能正确工作。
                    这段代码的输出如下：

                    CoroutineExceptionHandler got java.io.IOException with suppressed [java.lang.ArithmeticException]
                    注意，这个机制当前只能在 Java 1.7 以上的版本中使用。 在 JS 和原生环境下暂时会受到限制，但将来会取消。
                    取消异常是透明的，默认情况下是未包装的：


                    val handler = CoroutineExceptionHandler { _, exception ->
                        println("CoroutineExceptionHandler got $exception")
                    }
                    val job = GlobalScope.launch(handler) {
                        val inner = launch { // 该栈内的协程都将被取消
                            launch {
                                launch {
                                    throw IOException() // 原始异常
                                }
                            }
                        }
                        try {
                            inner.join()
                        } catch (e: CancellationException) {
                            println("Rethrowing CancellationException with original cause")
                            throw e // 取消异常被重新抛出，但原始 IOException 得到了处理
                        }
                    }
                    job.join()
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    Rethrowing CancellationException with original cause
                    CoroutineExceptionHandler got java.io.IOException
                    监督

                    正如我们之前研究的那样，取消是在协程的整个层次结构中传播的双向关系。让我们看一下需要单向取消的情况。

                    此类需求的一个良好示例是在其作用域内定义作业的 UI 组件。如果任何一个 UI 的子作业执行失败了，它并不总是有必要取消（有效地杀死）整个 UI 组件， 但是如果 UI 组件被销毁了（并且它的作业也被取消了），由于它的结果不再被需要了，它有必要使所有的子作业执行失败。

                    另一个例子是服务进程孵化了一些子作业并且需要 监督 它们的执行，追踪它们的故障并在这些子作业执行失败的时候重启。

                    监督作业

                    SupervisorJob 可以用于这些目的。 它类似于常规的 Job，唯一的不同是：SupervisorJob 的取消只会向下传播。这是很容易用以下示例演示：


                    import kotlinx.coroutines.*
                    ​
                    fun main() = runBlocking {
                        val supervisor = SupervisorJob()
                        with(CoroutineScope(coroutineContext + supervisor)) {
                            // 启动第一个子作业——这个示例将会忽略它的异常（不要在实践中这么做！）
                            val firstChild = launch(CoroutineExceptionHandler { _, _ ->  }) {
                                println("The first child is failing")
                                throw AssertionError("The first child is cancelled")
                            }
                            // 启动第二个子作业
                            val secondChild = launch {
                                firstChild.join()
                                // 取消了第一个子作业且没有传播给第二个子作业
                                println("The first child is cancelled: ${firstChild.isCancelled}, but the second one is still active")
                                try {
                                    delay(Long.MAX_VALUE)
                                } finally {
                                    // 但是取消了监督的传播
                                    println("The second child is cancelled because the supervisor was cancelled")
                                }
                            }
                            // 等待直到第一个子作业失败且执行完成
                            firstChild.join()
                            println("Cancelling the supervisor")
                            supervisor.cancel()
                            secondChild.join()
                        }
                    }
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    The first child is failing
                    The first child is cancelled: true, but the second one is still active
                    Cancelling the supervisor
                    The second child is cancelled because the supervisor was cancelled
                    监督作用域

                    对于作用域的并发，可以用 supervisorScope 来替代 coroutineScope 来实现相同的目的。它只会单向的传播并且当作业自身执行失败的时候将所有子作业全部取消。作业自身也会在所有的子作业执行结束前等待， 就像 coroutineScope 所做的那样。


                    import kotlin.coroutines.*
                            import kotlinx.coroutines.*
                    ​
                    fun main() = runBlocking {
                        try {
                            supervisorScope {
                                val child = launch {
                                    try {
                                        println("The child is sleeping")
                                        delay(Long.MAX_VALUE)
                                    } finally {
                                        println("The child is cancelled")
                                    }
                                }
                                // 使用 yield 来给我们的子作业一个机会来执行打印
                                yield()
                                println("Throwing an exception from the scope")
                                throw AssertionError()
                            }
                        } catch(e: AssertionError) {
                            println("Caught an assertion error")
                        }
                    }
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    The child is sleeping
                            Throwing an exception from the scope
                            The child is cancelled
                            Caught an assertion error
                            监督协程中的异常

                    常规的作业和监督作业之间的另一个重要区别是异常处理。 监督协程中的每一个子作业应该通过异常处理机制处理自身的异常。 这种差异来自于子作业的执行失败不会传播给它的父作业的事实。 这意味着在 supervisorScope 内部直接启动的协程确实使用了设置在它们作用域内的 CoroutineExceptionHandler，与父协程的方式相同 （参见 CoroutineExceptionHandler 小节以获知更多细节）。


                    import kotlin.coroutines.*
                            import kotlinx.coroutines.*
                    ​
                    fun main() = runBlocking {
                        val handler = CoroutineExceptionHandler { _, exception ->
                            println("CoroutineExceptionHandler got $exception")
                        }
                        supervisorScope {
                            val child = launch(handler) {
                                println("The child throws an exception")
                                throw AssertionError()
                            }
                            println("The scope is completing")
                        }
                        println("The scope is completed")
                    }
                    可以在这里获取完整代码。
                    这段代码的输出如下：

                    The scope is completing
                            The child throws an exception
                    CoroutineExceptionHandler got java.lang.AssertionError
                    The scope is completed
*/
}

/**
 * 共享的可变状态和并发
 */
fun coroutines8() {
    /*


                            共享的可变状态与并发

                    协程可用多线程调度器（比如默认的 Dispatchers.Default）并发执行。这样就可以提出所有常见的并发问题。主要的问题是同步访问共享的可变状态。 协程领域对这个问题的一些解决方案类似于多线程领域中的解决方案， 但其它解决方案则是独一无二的。

                    问题

                    我们启动一百个协程，它们都做一千次相同的操作。我们同时会测量它们的完成时间以便进一步的比较：


                    suspend fun massiveRun(action: suspend () -> Unit) {
                        val n = 100  // 启动的协程数量
                        val k = 1000 // 每个协程重复执行同一动作的次数
                        val time = measureTimeMillis {
                            coroutineScope { // 协程的作用域
                                repeat(n) {
                                    launch {
                                        repeat(k) { action() }
                                    }
                                }
                            }
                        }
                        println("Completed ${n * k} actions in $time ms")
                    }
                    我们从一个非常简单的动作开始：使用多线程的 Dispatchers.Default 来递增一个共享的可变变量。


                    var counter = 0
                    ​
                    fun main() = runBlocking {
                        withContext(Dispatchers.Default) {
                            massiveRun {
                                counter++
                            }
                        }
                        println("Counter = $counter")
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码最后打印出什么结果？它不太可能打印出“Counter = 100000”，因为一百个协程在多个线程中同时递增计数器但没有做并发处理。

                    volatile 无济于事

                            有一种常见的误解：volatile 可以解决并发问题。让我们尝试一下：


                    @Volatile // 在 Kotlin 中 `volatile` 是一个注解
                    var counter = 0
                    ​
                    fun main() = runBlocking {
                        withContext(Dispatchers.Default) {
                            massiveRun {
                                counter++
                            }
                        }
                        println("Counter = $counter")
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码运行速度更慢了，但我们最后仍然没有得到“Counter = 100000”这个结果，因为 volatile 变量保证可线性化（这是“原子”的技术术语）读取和写入变量，但在大量动作（在我们的示例中即“递增”操作）发生时并不提供原子性。

                    线程安全的数据结构

                    一种对线程、协程都有效的常规解决方法，就是使用线程安全（也称为同步的、 可线性化、原子）的数据结构，它为需要在共享状态上执行的相应操作提供所有必需的同步处理。在简单的计数器场景中，我们可以使用具有 incrementAndGet 原子操作的 AtomicInteger 类：


                    val counter = AtomicInteger()
                    ​
                    fun main() = runBlocking {
                        withContext(Dispatchers.Default) {
                            massiveRun {
                                counter.incrementAndGet()
                            }
                        }
                        println("Counter = $counter")
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这是针对此类特定问题的最快解决方案。它适用于普通计数器、集合、队列和其他标准数据结构以及它们的基本操作。然而，它并不容易被扩展来应对复杂状态、或一些没有现成的线程安全实现的复杂操作。

                    以细粒度限制线程

                    限制线程 是解决共享可变状态问题的一种方案：对特定共享状态的所有访问权都限制在单个线程中。它通常应用于 UI 程序中：所有 UI 状态都局限于单个事件分发线程或应用主线程中。这在协程中很容易实现，通过使用一个单线程上下文：


                    val counterContext = newSingleThreadContext("CounterContext")
                    var counter = 0
                    ​
                    fun main() = runBlocking {
                        withContext(Dispatchers.Default) {
                            massiveRun {
                                // 将每次自增限制在单线程上下文中
                                withContext(counterContext) {
                                    counter++
                                }
                            }
                        }
                        println("Counter = $counter")
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码运行非常缓慢，因为它进行了 细粒度 的线程限制。每个增量操作都得使用 [withContext(counterContext)] 块从多线程 Dispatchers.Default 上下文切换到单线程上下文。

                    以粗粒度限制线程

                    在实践中，线程限制是在大段代码中执行的，例如：状态更新类业务逻辑中大部分都是限于单线程中。下面的示例演示了这种情况， 在单线程上下文中运行每个协程。


                    val counterContext = newSingleThreadContext("CounterContext")
                    var counter = 0
                    ​
                    fun main() = runBlocking {
                        // 将一切都限制在单线程上下文中
                        withContext(counterContext) {
                            massiveRun {
                                counter++
                            }
                        }
                        println("Counter = $counter")
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码运行更快而且打印出了正确的结果。

                    互斥

                    该问题的互斥解决方案：使用永远不会同时执行的 关键代码块 来保护共享状态的所有修改。在阻塞的世界中，你通常会为此目的使用 synchronized 或者 ReentrantLock。 在协程中的替代品叫做 Mutex 。它具有 lock 和 unlock 方法， 可以隔离关键的部分。关键的区别在于 Mutex.lock() 是一个挂起函数，它不会阻塞线程。

                    还有 withLock 扩展函数，可以方便的替代常用的 mutex.lock(); try { …… } finally { mutex.unlock() } 模式：


                    val mutex = Mutex()
                    var counter = 0
                    ​
                    fun main() = runBlocking {
                        withContext(Dispatchers.Default) {
                            massiveRun {
                                // 用锁保护每次自增
                                mutex.withLock {
                                    counter++
                                }
                            }
                        }
                        println("Counter = $counter")
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    此示例中锁是细粒度的，因此会付出一些代价。但是对于某些必须定期修改共享状态的场景，它是一个不错的选择，但是没有自然线程可以限制此状态。

                    Actors

                    一个 actor 是由协程、 被限制并封装到该协程中的状态以及一个与其它协程通信的 通道 组合而成的一个实体。一个简单的 actor 可以简单的写成一个函数， 但是一个拥有复杂状态的 actor 更适合由类来表示。

                    有一个 actor 协程构建器，它可以方便地将 actor 的邮箱通道组合到其作用域中（用来接收消息）、组合发送 channel 与结果集对象，这样对 actor 的单个引用就可以作为其句柄持有。

                    使用 actor 的第一步是定义一个 actor 要处理的消息类。 Kotlin 的密封类很适合这种场景。 我们使用 IncCounter 消息（用来递增计数器）和 GetCounter 消息（用来获取值）来定义 CounterMsg 密封类。 后者需要发送回复。CompletableDeferred 通信原语表示未来可知（可传达）的单个值， 这里被用于此目的。


                    // 计数器 Actor 的各种类型
                    sealed class CounterMsg
                    object IncCounter : CounterMsg() // 递增计数器的单向消息
                    class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // 携带回复的请求
                    接下来我们定义一个函数，使用 actor 协程构建器来启动一个 actor：


                    // 这个函数启动一个新的计数器 actor
                    fun CoroutineScope.counterActor() = actor<CounterMsg> {
                        var counter = 0 // actor 状态
                        for (msg in channel) { // 即将到来消息的迭代器
                            when (msg) {
                                is IncCounter -> counter++
                                is GetCounter -> msg.response.complete(counter)
                            }
                        }
                    }
                    main 函数代码很简单：


                    fun main() = runBlocking<Unit> {
                        val counter = counterActor() // 创建该 actor
                        withContext(Dispatchers.Default) {
                            massiveRun {
                                counter.send(IncCounter)
                            }
                        }
                        // 发送一条消息以用来从一个 actor 中获取计数值
                        val response = CompletableDeferred<Int>()
                        counter.send(GetCounter(response))
                        println("Counter = ${response.await()}")
                        counter.close() // 关闭该actor
                    }
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    actor 本身执行时所处上下文（就正确性而言）无关紧要。一个 actor 是一个协程，而一个协程是按顺序执行的，因此将状态限制到特定协程可以解决共享可变状态的问题。实际上，actor 可以修改自己的私有状态， 但只能通过消息互相影响（避免任何锁定）。

                    actor 在高负载下比锁更有效，因为在这种情况下它总是有工作要做，而且根本不需要切换到不同的上下文。

                    注意，actor 协程构建器是一个双重的 produce 协程构建器。一个 actor 与它接收消息的通道相关联，而一个 producer 与它发送元素的通道相关联。



*/
}

/**
 * select表达式（实验性的）
 */
fun coroutines9() {
    /*
                    select 表达式（实验性的）

                    select 表达式可以同时等待多个挂起函数，并 选择 第一个可用的。

                    Select 表达式在 kotlinx.coroutines 中是一个实验性的特性。这些 API 在 kotlinx.coroutines 库即将到来的更新中可能会发生改变。
                    在通道中 select

                            我们现在有两个字符串生产者：fizz 和 buzz 。其中 fizz 每 300 毫秒生成一个“Fizz”字符串：


                    fun CoroutineScope.fizz() = produce<String> {
                        while (true) { // 每 300 毫秒发送一个 "Fizz"
                            delay(300)
                            send("Fizz")
                        }
                    }
                    接着 buzz 每 500 毫秒生成一个 “Buzz!” 字符串：


                    fun CoroutineScope.buzz() = produce<String> {
                        while (true) { // 每 500 毫秒发送一个"Buzz!"
                            delay(500)
                            send("Buzz!")
                        }
                    }
                    使用 receive 挂起函数，我们可以从两个通道接收 其中一个 的数据。 但是 select 表达式允许我们使用其 onReceive 子句 同时 从两者接收：


                    suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
                        select<Unit> { // <Unit> 意味着该 select 表达式不返回任何结果
                            fizz.onReceive { value ->  // 这是第一个 select 子句
                                println("fizz -> '$value'")
                            }
                            buzz.onReceive { value ->  // 这是第二个 select 子句
                                println("buzz -> '$value'")
                            }
                        }
                    }
                    让我们运行代码 7 次：


                    val fizz = fizz()
                    val buzz = buzz()
                    repeat(7) {
                        selectFizzBuzz(fizz, buzz)
                    }
                    coroutineContext.cancelChildren() // 取消 fizz 和 buzz 协程
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的执行结果如下：

                    fizz -> 'Fizz'
                    buzz -> 'Buzz!'
                    fizz -> 'Fizz'
                    fizz -> 'Fizz'
                    buzz -> 'Buzz!'
                    fizz -> 'Fizz'
                    buzz -> 'Buzz!'
                    通道关闭时 select

                            select 中的 onReceive 子句在已经关闭的通道执行会发生失败，并导致相应的 select 抛出异常。我们可以使用 onReceiveOrNull 子句在关闭通道时执行特定操作。以下示例还显示了 select 是一个返回其查询方法结果的表达式：


                    suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
                            select<String> {
                                a.onReceiveOrNull { value ->
                                    if (value == null)
                                        "Channel 'a' is closed"
                                    else
                                        "a -> '$value'"
                                }
                                b.onReceiveOrNull { value ->
                                    if (value == null)
                                        "Channel 'b' is closed"
                                    else
                                        "b -> '$value'"
                                }
                            }
                    注意，onReceiveOrNull 是一个仅在用于不可空元素的通道上定义的扩展函数，以使关闭的通道与空值之间不会出现意外的混乱。

                    现在有一个生成四次“Hello”字符串的 a 通道， 和一个生成四次“World”字符串的 b 通道，我们在这两个通道上使用它：


                    val a = produce<String> {
                        repeat(4) { send("Hello $it") }
                    }
                    val b = produce<String> {
                        repeat(4) { send("World $it") }
                    }
                    repeat(8) { // 打印最早的八个结果
                        println(selectAorB(a, b))
                    }
                    coroutineContext.cancelChildren()
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的结果非常有趣，所以我们会在更多细节中分析它：

                    a -> 'Hello 0'
                    a -> 'Hello 1'
                    b -> 'World 0'
                    a -> 'Hello 2'
                    a -> 'Hello 3'
                    b -> 'World 1'
                    Channel 'a' is closed
                    Channel 'a' is closed
                    有几个结果可以通过观察得出。

                    首先，select 偏向于 第一个子句，当可以同时选到多个子句时， 第一个子句将被选中。在这里，两个通道都在不断地生成字符串，因此 a 通道作为 select 中的第一个子句获胜。然而因为我们使用的是无缓冲通道，所以 a 在其调用 send 时会不时地被挂起，进而 b 也有机会发送。

                    第二个观察结果是，当通道已经关闭时， 会立即选择 onReceiveOrNull。

                    Select 以发送

                            Select 表达式具有 onSend 子句，可以很好的与选择的偏向特性结合使用。

                    我们来编写一个整数生成器的示例，当主通道上的消费者无法跟上它时，它会将值发送到 side 通道上：


                    fun CoroutineScope.produceNumbers(side: SendChannel<Int>) = produce<Int> {
                        for (num in 1..10) { // 生产从 1 到 10 的 10 个数值
                            delay(100) // 延迟 100 毫秒
                            select<Unit> {
                                onSend(num) {} // 发送到主通道
                                side.onSend(num) {} // 或者发送到 side 通道
                            }
                        }
                    }
                    消费者将会非常缓慢，每个数值处理需要 250 毫秒：


                    val side = Channel<Int>() // 分配 side 通道
                    launch { // 对于 side 通道来说，这是一个很快的消费者
                        side.consumeEach { println("Side channel has $it") }
                    }
                    produceNumbers(side).consumeEach {
                        println("Consuming $it")
                        delay(250) // 不要着急，让我们正确消化消耗被发送来的数字
                    }
                    println("Done consuming")
                    coroutineContext.cancelChildren()
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    让我们看看会发生什么：

                    Consuming 1
                    Side channel has 2
                    Side channel has 3
                    Consuming 4
                    Side channel has 5
                    Side channel has 6
                    Consuming 7
                    Side channel has 8
                    Side channel has 9
                    Consuming 10
                    Done consuming
                            Select 延迟值

                            延迟值可以使用 onAwait 子句查询。 让我们启动一个异步函数，它在随机的延迟后会延迟返回字符串：


                    fun CoroutineScope.asyncString(time: Int) = async {
                        delay(time.toLong())
                        "Waited for $time ms"
                    }
                    让我们随机启动十余个异步函数，每个都延迟随机的时间。


                    fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
                        val random = Random(3)
                        return List(12) { asyncString(random.nextInt(1000)) }
                    }
                    现在 main 函数在等待第一个函数完成，并统计仍处于激活状态的延迟值的数量。注意，我们在这里使用 select 表达式事实上是作为一种 Kotlin DSL， 所以我们可以用任意代码为它提供子句。在这种情况下，我们遍历一个延迟值的队列，并为每个延迟值提供 onAwait 子句的调用。


                    val list = asyncStringsList()
                    val result = select<String> {
                        list.withIndex().forEach { (index, deferred) ->
                            deferred.onAwait { answer ->
                                "Deferred $index produced answer '$answer'"
                            }
                        }
                    }
                    println(result)
                    val countActive = list.count { it.isActive }
                    println("$countActive coroutines are still active")
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    该输出如下：

                    Deferred 4 produced answer 'Waited for 128 ms'
                    11 coroutines are still active
                    在延迟值通道上切换

                    我们现在来编写一个通道生产者函数，它消费一个产生延迟字符串的通道，并等待每个接收的延迟值，但它只在下一个延迟值到达或者通道关闭之前处于运行状态。此示例将 onReceiveOrNull 和 onAwait 子句放在同一个 select 中：


                    fun CoroutineScope.switchMapDeferreds(input: ReceiveChannel<Deferred<String>>) = produce<String> {
                        var current = input.receive() // 从第一个接收到的延迟值开始
                        while (isActive) { // 循环直到被取消或关闭
                            val next = select<Deferred<String>?> { // 从这个 select 中返回下一个延迟值或 null
                                input.onReceiveOrNull { update ->
                                    update // 替换下一个要等待的值
                                }
                                current.onAwait { value ->
                                    send(value) // 发送当前延迟生成的值
                                    input.receiveOrNull() // 然后使用从输入通道得到的下一个延迟值
                                }
                            }
                            if (next == null) {
                                println("Channel was closed")
                                break // 跳出循环
                            } else {
                                current = next
                            }
                        }
                    }
                    为了测试它，我们将用一个简单的异步函数，它在特定的延迟后返回特定的字符串：


                    fun CoroutineScope.asyncString(str: String, time: Long) = async {
                        delay(time)
                        str
                    }
                    main 函数只是启动一个协程来打印 switchMapDeferreds 的结果并向它发送一些测试数据：


                    val chan = Channel<Deferred<String>>() // 测试使用的通道
                    launch { // 启动打印协程
                        for (s in switchMapDeferreds(chan))
                            println(s) // 打印每个获得的字符串
                    }
                    chan.send(asyncString("BEGIN", 100))
                    delay(200) // 充足的时间来生产 "BEGIN"
                    chan.send(asyncString("Slow", 500))
                    delay(100) // 不充足的时间来生产 "Slow"
                    chan.send(asyncString("Replace", 100))
                    delay(500) // 在最后一个前给它一点时间
                    chan.send(asyncString("END", 500))
                    delay(1000) // 给执行一段时间
                    chan.close() // 关闭通道……
                    delay(500) // 然后等待一段时间来让它结束
                    Target platform: JVMRunning on kotlin v. 1.6.0
                    可以在这里获取完整代码。
                    这段代码的执行结果：

                    BEGIN
                    Replace
                    END
                    Channel was closed


*/
}