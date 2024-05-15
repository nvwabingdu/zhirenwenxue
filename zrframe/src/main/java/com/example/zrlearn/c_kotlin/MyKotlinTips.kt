package com.example.zrlearn.c_kotlin

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-11-15
 * Time: 10:07
 */

/**
 * with函数  run函数  apply函数  lateinit延迟初始化  扩展函数  高阶函数
 */
fun main(args: Array<String>) {
    //with 在同一个对象连续调用多个方法时变得更加简单
    val list = listOf("apple", "orange", "pear", "grape")
    val builder = StringBuilder()
    builder.append("start eating fruits.\n")
    for (fruit in list) {
        builder.append(fruit).append("\n")
    }
    builder.append("Ate all frutis.")
    val result = builder.toString()
    println(result)

    //用with简化
    val result2 = with(StringBuilder()) {
        append("Start eating fruits.\n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("eat all fruits.")
        toString()
    }
    println(result2)


    /**
     * run函数  不会直接调用，而是需要在某个对象的基础上调用， 其次run函数只接收一个lambda参数，并且回来lambda表达式中提供调用对象的上下文
     */
//    val result3=obj.run{
//        //这里时obj的上下文
//        "value"//run函数的返回值
//    }
    val result3 = StringBuilder().run {//改动特别小
        append("Start eating fruits.\n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("eat all fruits.")
        toString()
    }
    println(result3)

    /**
     * apply函数 ： 和run相似，都需要在某个对象上调用，并且只接收一个lambda参数，也会在lambda表达式中提供调用对象的上下文
     */
//    val result = obj.apply{
//        //obj的上下文
//    }
//    //result==obj

    val result4 = StringBuilder().apply {//改动特别小
        append("Start eating fruits.\n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("eat all fruits.")
        toString()
    }
    println(result4.toString())


    //lateinit 用在有些地方因为必须要使变量=null的这种情况，  可以省略赋null初始化  加入lateinit表示告诉编译器，我待会儿自动初始化

//    private lateinit var ssss:String

    //判断一个对象是否完成了初始化  if(！::adapter.isInitalized){//逻辑 比如重新初始化等 }


    /**
     * 关于密封类 sealed
     */
    //1.
//    interface Result
//    class A:Result
//    class B:Result
//    fun getResult(result: Result){//某个方法
//        when(result){
//            is A->"121"
//            is B->"dsadada"
//            else-> "此else必须写"
//        }
//    }

    //2.可以相当于编译器自动查找了密封类  不用写else 编译器也知道不用写else
//    sealed class Rusult
//    class A:Result
//    class B:Result
//    fun getResult(result: Result){//某个方法
//        when(result){
//            is A->"121"
//            is B->"dsadada" //不用写else了
//        }
//    }


    /**
     * 扩展函数，直接扩展如math String的方法等  一般写在顶层函数上面
     * className.getCount(){}
     */
    fun String.lettersCount(): Int {
        var count = 0
        for (char in this) {
            if (char.isLetter()) {
                count++
            }
        }
        return count
    }

    val count = "213123131qeqeq".lettersCount()//牛逼


    /**
     * 高阶函数：
     * 1.如果一个函数接收另一个函数作为参数。
     * 2.或者返回值的类型是一个函数
     */
//    如
//    (String,Int)->Unit   某函数   ()->
    fun example(func: (String, Int) -> Unit) {
        func("hello", 21)
    }

    fun example(func: () -> Unit): Int {
        func()
        return 1
    }


    //以下是示例
    fun num1andnum2(num1: Int, num2: Int, operation: (Int, Int) -> Int): Int {
        val result = operation(num1, num2)
        return result
    }

    fun plus(num1: Int, num2: Int): Int {
        return num1 + num2
    }

    fun minus(num1: Int, num2: Int): Int {
        return num1 - num2
    }

    val num1 = 90
    val num2 = 19
    val result11 = num1andnum2(num1, num2, ::plus)
    val result22 =
        num1andnum2(num1, num2, ::minus) //注意这种写法 ::方法名   函数引用方式的写法  是引用式写法  相当于调用minus(num1,num2)


//    省略两个plus 和minus 的方法
    val result33 = num1andnum2(num1, num2) { a, b -> a + b }

}


/**
 * 内联函数  inline   书上269以后实际操作中再深入
 * 使用方法：用于消除高阶函数转换形成的匿名类开销  所以一般放在高阶函数的前面声明即可
 *
 */
inline fun num1andnum3(num1: Int, num2: Int, operation: (Int, Int) -> Int): Int {
    val result = operation(num1, num2)
    return result
}

/**
 * 不内联 noinline   如果多个函数作为参数的高阶函数  其中有函数不需要内联就需要加上此修饰
 * inline:  会在编译时进行代码替换
 * noinline: 非内联函数不会进行替换，就相当于是一个真正的参数 可以传递给其他函数
 *
 * 两者还有一个巨大的区别：inline可以直接使用return进行返回   noinline不能直接进行返回 可使用 return@xxx方法  进行局部返回
 */
inline fun xx(a: (Int) -> Int, noinline b: (Int) -> Int) {

}


/**
 * kotlin基本规则
 * 1.关于变量和输出  kotlin中所有的都为对象 所有变量都为对象
 */
fun main0(args: Array<String>) {

    /**
     * 变量分为可变var    不可变val（可赋值一次）
     */
    val age: Int // 2.没有初始值时不能省略数据类型
    var name = 1.toString() + "李西西"   //此种方式OK

    /**
     * 变长参数
     */
    val letters = ('1'..'9').toList()
    //快捷输出
    print(('1'..'9').toList())
    print(letters)

    /**
     * foreach  in关键字
     */
    for (args in letters) println(args)

    //单个参数的隐式名称  it 后面会讲到
    //lambda表达式
    //总是被{}
    //其参数(有的话)  在->之前申明（参数类型可以省略）
    //函数体（如果存在的话）  在->后面
    //var x:Int -> x+2  错误

    //kotlin所有的参数和变量都不能为null   否则编译不过   如果需要为空 则需要加？ 如 Int?

    fun ss(name: String, age: Int = 9) {}//设置默认值的方式  特别有用   此种方式可以用在构造上面
    ss("", 9)
    ss(name = "121");
//    ss(age=0)//没有默认值的参数必须要传参


    val list = listOf("apple", "orange", "pear")
    val lambda = { fruit: String -> fruit.length }
    val maxl = list.maxBy(lambda)
//    val newList = list.map { it.uppercase(Locale.ROOT) }
    val newList2 =
        list
            .filter { it.length >= 3 }
            .map { it.toUpperCase() }

    //kotlin 匿名线程写法
    Thread(object : java.lang.Runnable {
        override fun run() {
            TODO("Not yet implemented")
        }

    }).start()

    //简写方式
    Thread(Runnable {

    }).start()

    //终极版
    Thread {

    }.start()


}


/**
 * 1.  ::的作用，:: 将函数作为参数来使用，不能直接用来调用函数   作用就是将方法作为参数传入
 * https://www.jianshu.com/p/2efe93c9abf4
 * 总结一下，其实成员引用 :: 很简单，它就是把函数给转成了值，而这个值可以看成是函数类型，这样说就十分好理解了。
 * //直接就会调用People类的getMax函数
 * peopleList.sortBy(People::getMax)
 */
fun tips_1() {
    /**
     * 1.用于直接调用方法，只能作为参数使用
     */
    print(::tips_1)
    print(::tips_1.name)//打印出方法名
    print(::tips_1.returnType)//打印出返回类型

    /**
     * 2.用于databingding的绑定
     */
//    <Button
//    ...
//    onClick="@{data::onClick}"/>

    /**
     * 3.用于反射
     */
//    inline fun <reified T> T.foo3(string: String) {
//        Log.e(T::class.simpleName, string)
//    }
    /**
     * 4.得到 SecondActivity 对象
     */
//    startActivity(Intent(this@MainActivity, SecondActivity::class.java))


}

/**
 * 2.is关键字，用于判断是否有血缘关系
 */
fun tips_2() {
    var result: String = ""
    if (result is String) {
        print(result.length)//在条件分支内result直接就转为string
    }
}

/**
 * 3."""原始字符串用来绝对表示字符串，可以包含换行符和其他任意字符
 */
fun tips_3() {
    val rawString = """ fun helloworld(val name : String){  }""".trimIndent()

    //换行？
    val rawString2 = """ 
        |fun 
        |helloworld(val name : String){  }""".trimMargin()

}

/**
 * 4.$字符串的模板表达式，在字符串""用$符号开始
 */
fun tips_4() {
    val age = 26
    print("$age+岁中国人不骗中国人${age + age}")
}

/**
 * 5.in关键字的用法,用于判断是否在一个范围内，使用方式如下
 */
fun tips_5() {
    val numArray = arrayOf(1, 10, 3, 45, 100)
    val num = 12
    when (12) {
        in 10..100 -> print(num.toString() + "")
        !in 10..100 -> print("")
        in numArray -> print("在这个集合之中")
    }
}

/**
 * 6. =可以用于直接代替return
 */
fun tips_6() {
    /**
     * 方式一
     */
    fun sum(a: Int, b: Int) = a + b
    fun maX(a: Int, b: Int) = if (a > b) a else b
    //调用
    sum(2, 3)
//    max(2, 3)

    /**
     * 方式二
     */
    val sum2 = fun(a: Int, b: Int) = a + b
    val sumf = fun(a: Int, b: Int) = { a + b }
    //调用
    sum2(23, 3)
    print(sum2)
    val newNum = sumf(2, 3)
    print(newNum)
}

/**
 *7.if关键字，可以省略括号，括号内可以是代码块
 */
fun tips_7() {

    /**
     * 多变量赋值
     */
    var (a, b) = listOf(1, 34)

    /**
     * if为一个表达式 可以返回一个值  else 为可选
     */
    var max = if (a > b) a else b
    /**
     * 省略括号
     */
    if (a < b) max = a else max = b

    /**
     * 省略else
     */
    var max1 = a
    if (a < b) max1 = b

    /**
     * if分支可以为代码块，最后的表达式作为该块的值：  when也是可以作为函数=连接
     * 你牛逼爆了
     * 类似三元运算符
     */
    val max2 = if (a > b) {
        print("a")

    } else {
        print("b")
        b
    }

}

/**
 * 8.多变量赋值   相同类型不同变量值的kotlin式简单写法
 */
fun tips_8() {
    /**
     * val方式
     */
    val (a, b, c) = listOf(1, 0, "Sam")
    println("$a, $b, $c") //输出1, true, Sam

    /**
     * var方式
     */
    var (e, f, g) = listOf(true, 234, "我是一个字符串")
}

/**
 * 9.分支判断  when关键字的用法，用于替代java中的while --case --default
 */
fun tips_9(score: Int) {
    /**
     * 用于替代while 的 when  比较重要
     */
    fun method(obj: Any) {
        when (obj) {
            1 -> print("第一项")
            1, 0, 34, 32, 21 -> print("多项加逗号分隔")
            Integer.parseInt(obj as String) -> print("obj is 123")
            Integer.parseInt("123") -> print("obj is 123")
            "hello" -> print("字符串 hello")
            is Long -> print("这是一个long类型的数据")
            !is String -> print("obj is s")
            23..35 -> print("就是这么牛")
            else -> print("else类似于java中的default")//类似于java中的default
        }
    }

    var grade = when (score) {
        9, 10 -> "A"
        in 6..8 -> "B"
        4, 5 -> "C"
        in 1..3 -> "D"
        else -> "E"
    }
}

/**
 * 10.Kotlin中的? 、?. 、?: 、!!、as?  let
 */
fun tips_10() {
    /**
     * ?可以设置为空  可空
     */
    var a1: String? = "abc"
    a1 = null

    var one: Int? = 1
    one = null

    var arryInts: IntArray? = intArrayOf(1, 2, 3)
    arryInts = null

    null is Any    //false
    null is Any?   //true   null是安全空

    var listWithNulls: List<String?> = listOf("a", "b", null)
    listWithNulls.forEach {
        it?.let { print(it) }
    }


    /**
     * ?.判空操作
     */
    val a = "Kotlin"
    val b1: String? = null
    println(b1?.length)
    println(a?.length) // 无需安全调用      ？. 为null什么都不做

    /**
     * ?:Elvis操作符。如果 ?: 左侧表达式为空，则返回右侧表达式，否则返回左侧表达式
     * 很有用
     */
    var x = 90
    val y = x ?: 0

    /**
     * !!非空断言运算符。若值为空则抛出异常  有何用?
     * 用处：在上面做了判空处理，但是下方代码编译不过，因此需要确定的给编译器说，就是不会空，空了你给我抛异常就是了
     */
    val l = b1!!.length

    /**
     * as? 安全的类型转换，如果尝试转换不成功则返回 null
     */
    val aInt: Int? = a as? Int

    //let的适用方法  let表示一个函数哦
    //将原始对象作为参数传递到lambda表达式中
//    obj.let{
//        obj2->//具体的业务逻辑    obj2就是obj 只是这里作为了参数  写成it OK吗
//    }

    "909".let {
        print(it.length)
    }
}

/**
 * 11.关于循环for  while等
 */
fun tips_11(args: Array<String>) {
    /**
     * 这里类似于foreach   for可以对任何带迭代器的对象进行遍历
     */
    for (arg in args) {
        print(arg)
    }
    /**
     * 写法一
     */
    for (i in 0 until 100) {
        print(i)
    }
    for (i in 1..100) {
        "=1 <=100"
    }
    for (i in 1..20 step 2) {
        "=1 <=10 步长为2"
    }

    for (i in 100 downTo 0) {
        "=100 >=0"
    }
    for (i in 100 downTo 0 step 2) {
        "=1 <=10 步长为2"
    }
    /**
     * 写法二：牛批
     */
    args.forEach { print(args) }
    /**
     * 写法三：通过索引遍历一个数组或者一个list
     */
    for (i in args.indices) {
        print(args[i])
    }
    /**
     * 写法四：用库函数
     */
    for ((index, value) in args.withIndex()) {
        print("the element at $index is $value")
    }

    /**
     * break  continue 都是用来控制循环结构
     */
    for (i in 10..20) {
        print(i)
        if (i == 13) {
            break //continue和java一样  调出本次   仅仅这一次的循环
        }
    }

    /**
     * while  do while  基本上和java相同
     */
    do {
    } while (true)
}

/**
 * 12.标签adc@   跟着@的即为标签  不常用标签   java方式为 label：
 * 在 Kotlin 中任何表达式都可以用标签（label）来标记。 标签的格式为标识符后跟 @ 符号，例如：abc@、fooBar@都是有效的标签
 */
fun tips_12() {
    /**
     * 使用标签来限定 break 或 continue 的跳转对象
     */
    loop@ for (i in 1..100) {
        for (j in 1..100) {
            if (j == 3) break@loop
        }
    }

    /**
     * 跳转到foreach标签处
     */
    val array = arrayOf(1, 3, 2, 3, 4, 2, 2, 3, 2, 2)
    array.forEach flag@{ //创建标签
        if (it == 12) {
            return@flag// 跳转到标签处       it为每个值
        }
    }
    /**
     * foreach隐式操作
     */
    fun method3() {
        var arrar = arrayOf(1, 23, 41, 21, 31, 13, 13, 13, 131)
        arrar.forEach {
            if (it == 12) {
                return@forEach//隐式操作
            }
        }
    }
}

/**
 *13.throw关键字 throw是表达式  它的特殊类型是返回值为Nothing   相当于java中的void
 */
fun tips_13() {
    /**
     * 用Nothing来标记无返回值的函数 一般都省略了
     */
    fun fail(msg: String): Nothing {
        throw IllegalAccessException(msg)
    }

    var no: Nothing = throw Exception("")//因为是Nothing  所以不能作为参数传给函数
}

/**
//14.重载操作符的函数需要用          operator 修饰符标记
//把运算符重载  比如+号  可以实现-的功能   一般用于自定义某种函数式的加法  比如name+age
//https://blog.csdn.net/qq_34589749/article/details/103643764     博客地址
 */
fun tips_14() {
    /**
     * person类
     */
    data class Person(var name: String, var age: Int)

    /**
     * 重载函数operator  把系统+设置为 -
     */
    operator fun Int.plus(b: Person): Int {
        return this - b.age
    }

    /**
     * 重载函数operator  把系统-设置为 +
     */
    operator fun Int.minus(b: Person): Int {
        return this - b.age
    }

    /**
     * 测试方法
     */
    fun main() {
        val person1 = Person("A", 3)//对象的调用方法
        val testInt = 5
        println("testInt+person1=${testInt + person1}")
        println("testInt+person1=${testInt - person1}")
    }
}

/**
//15.中缀操作符,标有 infix关键字的成员函数或者扩展函数 可以忽略该调用的点和圆括号
infix的要求
必须是成员函数或扩展函数
函数只能有一个参数
函数的形参不能是变参
函数的形参不能有默认值
在使用infix语法调用时必需指定接收者
 */
fun tips_15() {

    infix fun Int.suffix(subfix: String) = "$this-$subfix"//这是一个扩展函数  只有一个参数
    /**
     * 这个函数的调用可以写成  suffix 附加 后缀 尾标 下标
     */
    99 suffix "sss"  // 99 是 Int , suffix是函数， "sss" 是参数

    /**
     * 在使用infix语法调用时必需指定接收者，如下第8行是个错误
     */
    fun infix_test1_2() {
        class JJJ {
            infix fun prefix(prefix: String) = "$prefix-$this"
            fun test() {
                prefix("EE")    //直接用方法调用  ok
                this prefix "HHH"       //指定接受者调用 ok
                //prefix "jjj"          //error
            }
        }
    }

    /**
     * person类
     */
    class Person(val name: String, val age: Int)

    /**
     * 通过自定义infix函数来实现中缀操作符
     */
    infix fun Person.grow(years: Int): Person {
        return Person(name, age + years)
    }
}

/**
 * 16.扩展函数 和扩展属性  能够扩展一个类的新功能 而无需继承该类 或者使用像装饰者这样的设计模式等
 */
fun tips_16() {
    /**
     * 扩展属性
     */
    /*val<T> List<T>.lastIndex: Int get() = size - 1*/

    /**
     * 为String类型扩展一个notEmpty()的函数
     */
    fun String.notEmpty(): Boolean {
        return !this.isEmpty()
    }
    "".notEmpty()    //false
    "123".notEmpty()   //true

    /**
     * 为MutableList<Int>添加一个swap函数
     */
    fun MutableList<Int>.swap(index1: Int, index2: Int) {
        val tmp = this[index1]//this对应该列表
        this[index1] = this[index2]
        this[index2] = tmp
    }

    /**
     * this关键字在扩展函数内部对应到接受者对象（传过来的，在.符号前的对象）。现在我们对任意MutableList<Int>调用该函数了
     * 当然这个函数对应任何MutableList<T>起作用，我们可以泛化它
     */
    fun <T> MutableList<T>.mswap(index1: Int, index2: Int) {
        val tmp = this[index1]//this对应该列表
        this[index1] = this[index2]
        this[index2] = tmp
    }
}

/**
 *17.所有类型的爹    Any
 */
fun tips_17() {
    val any = Any()
//    结果 any   java.lang.Object@2e377400
//    any::class
//            class kotlin.Any
//    any::class.java
//            clas java.lang.Object
}

/**
 *18.进制，比较是否相等 位运算，==,!=,===，!==
 */
fun tips_18() {
    /**
     *  相等与不相等
     *  引用相等 ===   !==  地址
     *  结构相等 ==   != 内容          a == null 时  自动转为 a===null  ?
     *  "".equals("ee")  还是有的
     */

    /**
     * 十进制：123
     * long：123L
     * 十六进制：0x0F
     * double:123.5    123.5e10
     * float 123.4F  12.3f
     */
    var b2: Char = '\u0000'
    var b3 = 3
    b2 = 'a' + 5 //char字符集的+
    /*'a'+1L=  error  //没有重载+Long的数据*/
    b3 = 'c' - 'a'
    /*不能直接使用++'a'*/

    /**
     * 不能隐式转换  只有用显式转换
     */
    val b: String = "12"
    val i: Int = b.toInt()

    /**
     * 可以使用+   则会自动转大
     */
    val l = 1L + 3   //可以的

    /**
     * 位运算  没有如java般的符号 只有如下调用
    val x=(1  shl   2) and 0x000ff000
    shl(bits)  -----有符号左移（java的<<）
    shr(bits)  -----有符号右移（java的>>）
    ushr(bits)  -----无符号右移（java的>>>）
    and    与
    or     或
    xor    异或
    inv    非
     */
}

/**
 * 19.索引运算符
 */
fun tips_19() {
    var temp = "abc"
    print("输出temp字符串的第一个" + temp[0])
}

/**
 * 20.字符串常规操作
 */
fun tips_20() {
    /**
     * 字符串
     */
    for (a1 in "56678888") {
        print(a1)
    }

    "acb".plus(true)// abctrue

    "aca" + null//acanull

    "abc" + arrayOf(1, 3, 4, 5, 4)//结果为 abc+地址

    "acsada".subSequence(0, 12)

    /**
     * 1、字符串查找
     */
    var str = "Hello World!"
    println(str.first())          //获取第1个元素
    println(str.last())          //获取最后1个元素
    println(str.get(4))          //获取第4个元素
    println(str[4])               //获取第4个元素
    println(str.indexOf('o'))   //查找字符串在原字符串中第1次出现的角标
    println(str.lastIndexOf('o'))//查找字符串在原字符串中最后1次出现的角标
//    H
//    !
//    o
//    o
//    4
//    7
    /**
     * 2、字符串替换
     */
    println(str.replace("World", "Kotlin"))
    println(str.replaceFirst("World", "Kotlin"))
    println(str.replaceBefore("!", "Kotlin"))
    println(str.replaceAfter("Hello ", "Kotlin!"))
//    Hello Kotlin! Hello Kotlin!
//    Hello Kotlin! Hello World!
//    Kotlin! Hello World!
//    Hello Kotlin!

    /**
     * 3、字符串拆分
     */
    val string = "hello.kotlin.heima"
    val split = string.split(".")
    for (s in split) {
        println(s)
    }
//    hello
//    kotlin
//    heima
    /**
     * 注意点：Kotlin中split函数，默认不会把传入的参数当做正则表达式。那如果我就是想把传入的参数当做正则表达式呢？怎么办呢？可以通过toRegex方法
     */
    println("hello.kotlin.heima".split("\\.".toRegex()))
//    [hello, kotlin, heima]

    /**
     * 棒棒的 Kotlin的split方法还允许传入多个拆分符
     */
    println("hello.kotlin-itheima".split(".", "-"))//传入2个分隔符

    /**
     * 4、字符串截取
     */
    val str2 = "hello kotlin"
    println("subString演示：${str2.substring(0, 5)}")

    val path = "c:/基础语法/字符串分裂.kt"
    //获取文件目录
    println("${path.substringBeforeLast("/")}")
    //获取文件全名（带后缀）
    println("${path.substringAfterLast("/")}")

    val fileName = "字符串分裂.kt"
    //获取文件名
    println("${fileName.substringBeforeLast(".")}")
    //获取文件拓展名
    println("${fileName.substringAfterLast(".")}")

//    subString演示：hello
//    c:/基础语法
//    字符串分裂.kt
//    字符串分裂
//    kt

    /**
     * 5、删除空格
     */
    println("${" 内容 ".trim()}")
    println("${" 内容 ".trimStart()}")
    println("${" 内容 ".trimEnd()}")
//    trim：去除字符串前后空格
//    trimStart：去除字符串前面的空格
//    trimEnd：去除字符串后面的空格

    /**
     * 6、转义字符
     */
//    \r：表示回车符，将光标定位到当前行的开头，不会跳到下一行。
//    \n：表示换行符，换到下一行开头。
//    \t：表示制表符，将光标移动到下一个制表符的位置，类似在文档中用Tab键的效果。
//    \b：表示退格符号，类似键盘上的Backspace键。
//    '：表示单引号字符，在Kotlin代码中单引号表示字符的开始和结束，如果直接写单引号字符（'），程序会认为前两个是一对，会报错，因此需要使用转义字符（'）。
//    "：表示双引号字符，Kotlin中双引号表示字符串的开始和结束，包含在字符串中的双引号需要转义，比如""。
//    \：表示反斜杠字符，由于在Kotlin代码中的反斜杠（\）是转义字符，因此需要表示字面意义上的\，就需要使用双反斜杠（\）。
}

/**
 * 21.数组的创建  赋值
 */
fun tips_21() {
    /**
     * 写法一
     */
    arrayOf(1, 2, 3, 4, true)     //就是    array[1,2,3,4]
    listOf(1, 2, 3, 4)         //对应的 集合那边的

    /**
     * 写法二 可以允许不同类型的值
     */
    val arr = arrayOf(12, 2, 3, true)    //允许不同元素  牛批
    arr.forEach { println(it) }     //it代替  牛皮

    /**
     * 写法三：创建固定长度的空数组
     */
    val params2 = arrayOfNulls<String>(12)
    arrayOfNulls<Int>(10).forEach { println(it) }//创建固定长度的 空数组。也有 DoubleArray  FloatArray 等

    /**
     * 写法四
     */
    /**
     * 1、Array构造方法指定数组大小和一个生成元素的lambda表达式这种方法创建的数组，其中的每个元素都是非空的，就像下面这样
     */

    val params3 = Array<String>(3) { i -> "str" + i }

    /**
     * 2、也可以这么写
     */
    val params = Array(3) { "str$it" }

    /**
     * 扩展  还有ByteArray、ShortArray、LongArray、FloatArray、DoubleArray、BooleanArray
     */
    var strArray = arrayOf("java", "kotlin")
    var numArray = arrayOf(1, 2, 3)

    //创建指定长度，元素为null的数组
    var arr2 = arrayOfNulls<String>(6)
    var arr2Duble = arrayOfNulls<Double>(5)

    arr2.set(0, "java")//替换值方法1
    arr2[1] = "Kotlin"//替换值方法2

    val intArray: IntArray = intArrayOf(1, 2)
    val charyArray: CharArray = charArrayOf('H', 'E', 'T', 'Y', 'W', 'K', 'B')

    /**
     * 字符数组转换成字符串
     */
    //charyArray.joinToString
}

/**
 * 22.函数Uint返回值为unit,nothing没有返回值,any是所有对象的爹
 */
fun tips_22() {
    /**
     * 函数Uint相当于void
     */
    fun m1() {}
    fun m2() {
        return Unit
    }

    fun m3(): Unit {
        return Unit
    }

    //Any 最高层  String    Int    MyClass     最底层  Nothing
    //Any?最高层  String?   Int?   MyClass?    最底层  Nothing?
    //as运算符  用于执行引用类型的显示类型转换       类型兼容 转换就成功      类型不兼容  使用as？运算符就会返回null

    //最底层类型  Nothing        Nothing()  is  Any       //error
    // Unit  返回值是   Unit        Nothing没有返回值  几乎和void相同
    // Nothing?=null    是可以的

    open class Father   //父亲类
    class Son : Father()  //儿子类

    val father = Father()  //父亲的实例
    val son = Son()        //儿子的实例

    father as Son        //excption
    father as? Son        //null
    son as Father     //line71$Goo@73dce0e6
}

/**
 * 23.集合重点篇  List不可变   MutableList可变
 */
fun tips_23() {
    /**
     * List不可变
     */
    var mmlist: List<Int> = listOf<Int>()    //不能省略变量类型
    var m2list = listOf(12, 2, 312, 21, 31, 3, 12, 1, "666")
    var m3list = arrayListOf(12, 2, 312, 21, 31, 3, 12, 1, 2)

    /**
     * MutableList可变
     */
    var mmlist1: List<Int> = mutableListOf()    //不能省略变量类型
    var m2list2 = mutableListOf(12, 2, 312, 21, 31, 3, 12, 1, 2, "666")
    var m3list3 = mutableListOf(12)


    /**
     * 互相转化    不可变转化为可变list
     */
    val newList = mmlist.toMutableList()
    val new2list = mmlist1.toList()

    /**
     * 遍历1 用迭代器
     */
    val iterator2 = newList.iterator()
    while (iterator2.hasNext()) {
        print(iterator2.next())
    }
    /**
     * 遍历2
     */
    newList.forEach { print(it) }

    m2list2.forEach { print(it) }

    /**
     * 遍历3 for循环遍历
     */
    for (i in new2list.indices) println(new2list[i])
}

/**
 * 24.集合相关操作汇总
 */
fun tips_24() {
    val mutableList = mutableListOf(1, 2, 3)
    val mutableList2 = mutableListOf(1, 2, 3, 4, 5)
    val list = listOf(1, 2, 3)
    val list2 = listOf("1212", "2121212", "21212")
    val emptylist = listOf<Int>()



    mutableList.add(1)
    mutableList.add(0, 21)
    mutableList.remove(1) //删除元素1
    mutableList.removeAt(1)  //删除角标1处的数据
    mutableList.removeAll(listOf(1, 2, 3)) //删除子集合
    mutableList.addAll(listOf(1, 2, 3))

    mutableList.set(0, 100) //更新下标为0的数据为100

    mutableList.clear()//清空
    mutableList.toList()//将可变集合 变为不可变集合

    /**
     * 取交集   1 2 3
     */
    mutableList.retainAll(mutableList2)

    list.contains(1)

    list.elementAt(1)//查找对应下标的值
    /**
     * 查找不到  返回默认值
     */
    list.elementAtOrElse(1) { 12 }
    /**
     * 找不到 返回null
     */
    list.elementAtOrNull(1)

    list.first()//返回第一个

    emptylist.first()
    emptylist.firstOrNull()

    /**
     * 直接用条件表达式，返回满足条件的
     */
    list.first { it % 2 == 0 }
    list.first { it > 100 }//牛批

    /**
     * 没有就返回-1
     *  list.indexOfFirst{}  返回第一个满足条件的角标
     */
    list2.indexOf("1")
    list.indexOfFirst { it > 10 }//此处条件为Boolean型  -1
    list2.indexOfFirst { it.contains("12") }//此处条件为Boolean型  -1

    list2.lastIndexOf("34")
    list.indexOfLast { it > 12 }//  返回下标
    list2.last()//返回最后一个
    list2.last { it.contains("12") }// 返回符合条件的最后一个元素


    list.single()//只有一个元素就返回这个元素  否则异常
    list2.single { it.contains("dad") }//返回符合条件的数据   没有  或者  不止一个都抛出异常？
    list2.singleOrNull() { it.contains("dad") }//不抛异常了  返回null

    list.any()//至少有一个元素   返回值Bleaoon
    list.any { it > 1 } //是否有满足条件的
    list.all { it > 1 }//是否所有的都满足
    list.none()//判断有无数据
    list.none { it > 1 }//是否所有的元素都不满足条件

    list.count()//返回个数
    list.count { it > 2 }

    /**
     * 累计运算
     * reduce 减少 /降低 /减量化 /简化
     */
    list.reduce { total, s -> total + s }
    list.reduce { sum, next -> sum + next }

    list.reduceRight { total, s -> s + total }//从右边开始累加

    list.fold(100) { sum, next -> sum + next }//带初始值的累加
    list.foldRight(100) { sum, next -> next + sum }//带初始值的累加 从右边

    /**
     * 带条件的foreach
     */
    list2.forEach { print(it) }
    list2.forEachIndexed { index, i -> if (i.contains("da")) println(i) }

//    list.maxOrNull()
//    list.minOrNull()
//
//    //  "a">"b"   "abd"<"abc"    true>false
//    val list3 = listOf(100, -500, 200, 300)
//    list3.maxByOrNull { it }//300
//    list3.maxByOrNull { it < 180 } //100
//    list3.maxByOrNull { it * it }//-500
//    list3.sumBy { it }
//    list3.minByOrNull { it }


    /**
     * 返回前N个元素的子集合
     */
    list.take(2)
    list.takeLast(2)


    /**
     * 返回依次满足条件的 遇到第一个不满足的就跳出
     */
    list.takeWhile { it % 2 == 0 }
    list.takeLastWhile { it % 2 == 0 }

    /**
     * 去掉前面两个  然后返回集合
     */
    list.drop(2)
//        list.dropLast()
//        list.dropWhile {  }
//        list.dropLastWhile {  }


    /**
     * 对集合的切片操作
     * slice 切片 /片 /薄片 /剖切
     */
    list.slice(0..3)//取出0-3角标的集合
    list.slice(listOf(1, 0, 3))//返回指定下标的元素子集合


    /**
     * 筛选
     */
    val list4 = mutableListOf<Int>()
    list.filterTo(list4) { it > 2 }

    list.filter { it > 2 }//筛选
    list.filterNot { it < 3 }//所有不满足条件的
    list.filterNotNull()//过滤掉null


    list.map { it }//映射到map
    list.mapIndexed { index, it -> index + it }//index为下标
    list.mapNotNull { it }//返回无null


    /**
     * 有点意思
     */
//    val list = listOf(1, 2, 3)
//    println(list)
//
//    val list1 = list.map { it -> listOf(it + 1, it + 2, it + 3) }//floatmap [[2, 3, 4], [3, 4, 5], [4, 5, 6]]
//    println(list1)
//
//    val list2 =
//        list.map { it -> listOf(it + 1, it + 2, it + 3) }.flatten()//=================[2, 3, 4, 3, 4, 5, 4, 5, 6]
//    println(list2)

    val list5 = listOf("a", "b", "c")
    list.map { it -> listOf(it + 1, it + 2, it + 3) }
    //[[a1,a2,a3][b1,b2,b3][c1,c2,c3]]
    list.flatMap { it -> listOf(it + 1, it + 2, it + 3) }
    //[a1,a2,a3,b1,b2,b3,c1,c2,c3]


    /**
     * 有别于java的新用法
     */
    val words = listOf("a", "abc", "ab", "def", "abcd")
    val lengthGroup = words.groupBy { it.length }
    println(lengthGroup)//{[1=[a],3=[abc.def],2=[ab],4=[abcd]}

    val containsGroup = words.groupBy({ it.length }, { it.contains("b") })
    println(containsGroup)//{[1=[false],3=[true.false],2=[true],4=[true]}


    /**
     * 统计函数 这几个函数有点类似于python
     */
    val words2 = "one two three four five six seven eight nine ten".split(" ")
    words2.groupingBy { it.first() }.eachCount()    //统计首字母出现的次数
    //{o=1,t=3,f=2,s=2,e=1,n=1}


    /**
     * 排序，反转
     */
    list.reversed()//反转输出
    list.sorted()//升序
    words.sortedBy { it.length }//以长度为规则升序
    list.sortedByDescending { it }//降序排列
    words.sortedByDescending { it.length }//降序排列

    /**
     * 配合函数
     */
    val n = listOf(1, 2, 3, 4)
    val m = listOf("A", "b", "ada")
    n.zip(m)
    //{[1,A][2,b][3,aba]}
    n.zip(listOf<Int>())
    //[]


    val mm = listOf(1, 2, 3, 4, 5, 5)
    mm.partition { it > 4 } //===========================根据条件拆分为两个集合  挺有用
    //{[5,5][1,2,3,4]}


    /**
     * 合并函数
     */
    val qlist = listOf(1, 2, 3)
    val wlist = listOf(4, 5)
    qlist.plus(wlist)
    qlist + wlist//=====================================牛批
    qlist.plus(4)//=====================================牛批
//        [1,2,3,4]

    qlist.plusElement(12)
    qlist + 12//=======================================牛批
    //[1,2,3,12]

    //对list去重  牛皮到爆炸
    list.toSet()
}

/**
 * 25.set集合
 */
fun tips_25() {
    /**
     * 支持序列化
     * 不可变 set
     */
    val emptySet = emptySet<Int>()//空集  size 0    isEmpty() true   hashcode 0
    val setss = setOf<Int>()//空集


    //创建方式如下
    val sets = setOf(1, 2, 3, 4, 4, 4) //牛批  hashcode   equals 皆会判断
    //[1,2,3,4]

    //小于3的时候  初始容量为 n+1
    //n小于2147483647/2+1   ,初始容量为 n+n/3  否则为2147483647

    val hashset1 = hashSetOf(1, 2, 3, 8, 4)     //hashSet 按照hash来存取，存取速度快
//        [1,2,3,4,8]
    val linked1 = linkedSetOf(1, 2, 3, 8, 4)    //linkedSet相当于这个   具有hash的查询速度 内部链表实现  频繁插入，删除的场景使用
//        [1,2,3,8,4]
    /**
     * 可变mutableSET
     */
    val linked2 =
        mutableSetOf(1, 3, 2, 7)    //linkedSet相当于这个加排序    具有hash的查询速度 内部链表实现  频繁插入，删除的场景使用
    // [1,3,2,7]
    val linked3 = sortedSetOf(1, 3, 2, 7)     //treeSet         实现了sorted  可以排序

    sets + 1
    sets + listOf(1, 2, 3)
    sets - 3
    sets - listOf(1, 3)

//        [1,2,3,7]
}

/**
 * 26.map集合
 */
@RequiresApi(Build.VERSION_CODES.N)
fun tips_26() {
    /**
     * map   不可变
     */
    val map1 = mapOf<String, Int>()
    var map2 = mapOf(1 to "a", 2 to "b", 3 to "c")
    map2.entries  //==========================[1=a,2=b,3=c]
    map2[2] //跟java差不多

    /**
     *  mutableMap  可变  可编辑
     */
    val map3 = mutableMapOf<Int, Any?>()
    map3[1] = "da"
    map3[2] = 1

    val map4 = mutableMapOf(1 to "a", 2 to "b", 1 to "c")
    //[1=c,2=b]    //相同的话   后面的直接覆盖前面的

    val map5 = linkedMapOf(1 to 1, 2 to 3)
    val map6 = hashMapOf(1 to 1, 2 to 3)
    val map7 = sortedMapOf(1 to 1, 2 to 3)

    map2.entries//获取所有键值对
    map2.entries.forEach { println("key:" + it.key + "value:" + it.value) }//============牛

    map2.keys//获取所有 key值
    map2.values//获取所有value

    map2[1]

    map2.getOrDefault(1, "12")//为null的时候返回默认值  且默认值要和类型匹配   否则会报错
    map2.containsKey(1)
    map2.containsValue("!2")
    /**
     * 打印键值对  (1, a)(2, b)(3, c)
     */
    map2.entries.forEach { print(it.toPair()) }
    map2.getOrElse(1) { 1 }
    map2.getValue(1)

    for ((k, v) in map2) print("key=$k" + "value=$v")   //=========类似于遍历===============牛批

    map2.mapKeys { it.key + 3 }//会被后面的key值覆盖  就是改变了key?

    map2.mapValues { it.value + 3 }//会被后面的value值覆盖

    map2.filterKeys { it > 2 } //it是key值

    map2.filter { it.key > 2 && it.value.equals("1") }//筛选

    /**
     * 转为可变
     */
    map2.toMutableMap()

    /**
     * 使用pair
     */
    val pairList = listOf(Pair(1, 22), Pair(3, 32), Pair(4, 55))
    pairList.toMutableList()

    map2 + Pair(1, 2)
    map2 + listOf(Pair(1, 22), Pair(3, 32))
    map2 + arrayOf(Pair(1, 22), Pair(3, 32))
    map2 + sequenceOf(Pair(1, 22), Pair(3, 32))//
    map2 + mapOf(1 to 2, 43 to 12)
    map2 += listOf(Pair(45, "22"), Pair(23, "32"))


    val map22 = mutableMapOf(1 to "2", 3 to "")
    map22[1] = "2"//添加 存在就更新
    map22.putAll(map2)  //把map2的所有数据添加到map22的后面   有重复的话 用map2的覆盖map22的


    map22.remove(12) //根据key 删除  不是角标哦
    map22.clear()//清空
}

/**
 * 27.泛型
 */
fun tips_27() {
//    型变：协变，逆变，不变

//    <? extends T>  T和其子类      对应于kotlin的   out          协变
//    <? super T>    T和其父类      对应于kotlin的    in           逆变
//    ?                           对应于kotlin的   *


//泛型    <k,V>这里面的就是泛型   <T>这个也是   作用    解释为：参数化类型
//        <String,Integer>
//        <"a",100>
//        <"b",100>
//        <"c",100>
//        in  T     ====>     ? extends T
//        out T     =====>    ? super T
//         *        =====>    ?

    /**
     * 1、泛型类
     */
    class Animal<T> {}
    /**
     * 2、泛型接口
     */
//    interface IAnimal<T> {}
    /**
     * 3、泛型方法
     */
    fun <T> initAnimal(param: T) {}

    /**
     * 二、泛型约束
     */
//    泛型约束表示我们可以指定泛型类型（T）的上界，即父类型，默认的上界为Any?，如果只有一个上界可以这样指定：
//    fun <T : Animal<T>> initAnimal(param: T) {}
//    即Animal<T>就是上界类型，这里使用了:，在 Java 中对应extends关键字，如果需要指定多个上界类型，就需要使用where语句：
//    fun <T> initAnimal(param: T) where T : Animal<T>, T : IAnimal<T> {}


    /**
     * 三、类型擦除
     */
//    Kotlin 为泛型声明执行的类型安全检测仅在编译期进行， 运行时实例不保留关于泛型类型的任何信息。这一点在 Java 中也是类似的。
//    例如，Array<String>、Array<Int>的实例都会被擦除为Array<*>，这样带来的好处是保存在内存中的类型信息也就减少了。
//    由于运行时泛型信息被擦除，所以在运行时无法检测一个实例是否是带有某个类型参数的泛型类型，所以下面的代码是无法通过编译的
//    （Cannot check for instance of erased type: Array<Int>）：
//    fun isArray(a: Any) {
//        if (a is Array<Int>) {
//            println("is array")
//        }
//    }
//    但我们可以检测一个实例是否是数组，虽然 Kotlin 不允许使用没有指定类型参数的泛型类型，但可以使用星投影*（这个后边会说到）：
//
    fun isArray(a: Any) {
        if (a is Array<*>) {
            println("is array")
        }
    }

    /**
     *  同样原因，由于类型被擦除，我们也无法安全的将一个实现转换成带有某个类型参数的泛型类型：
     */
    fun sumArray(a: Array<*>) {
        val intArray =
            a as? Array<Int> ?: throw IllegalArgumentException("Array的泛型类型必须是Int类型")
        //as?  判断是否是后面的类型 不是返回null        ?: 前面不为null返回前面  为null返回后面
        println(intArray.sum())
    }
//    因为我们无法判断数组a的是不是Array<Int>类型的，所以可能会出现异常的情况。
//    对于泛型函数，如果在函数内需要使用具体的泛型类型，同样由于运行时泛型信息被擦除的原因，
//    你无法直接使用它（Cannot check for instance of erased type: T）：

    //    fun < T> test(param: Any) {
//        if (param is T){
//            println("param type is match")
//        }
//    }
//    但还是有办法的，可以用inline关键字修饰函数，即内联函数，这样编译器会把每一次函数调用都换成函数实际代码实现，
//    同时用reified关键字修饰泛型类型，这样就能保留泛型参数的具体类型了：
//    inline fun <reified T> test(param: Any) {
//        if (param is T){
//            println("param type is match")
//        }
//    }
//    四、型变
//    1、声明处型变
//    型变是泛型中比较重要的概念，首先我们要知道 Kotlin 中的泛型是不型变的，这点和 Java 类似。那什么是型变呢，看个例子：
    open class A
    class B : A()

    val array1: Array<B> = arrayOf(B(), B(), B())
//    val array2: Array<A> = array1
//    你会发现第二个赋值语句会有错误提示，Type mismatch. Required:Array<A> Found:Array<B>类型不匹配，
//    Array<B>并不是Array<A>的子类，就是因为 Kotlin 中的泛型是默认不型变的，
//    无法自动完成类型转换，但B是A的子类，这个赋值操作本质上是合理的、安全的，但编译器似乎并不知道，这必然给我们开发过程中带来了麻烦。
//
//    为什么Array无法正常的赋值，而List、Set、Map可以呢？如下代码，编译器不会有错误提示的：
//
    val list1: List<B> = listOf(B(), B(), B())
    val list2: List<A> = list1
//    我们可以对比一下Array和List在源码中的定义：
//
//    public class Array<T> {}
//
//    public interface List<out E> : Collection<E> {}
//    可以看到List的泛型类型使用了out修饰符，这就是关键所在了。这就是 Kotlin 中的声明处型变，用来向编译器解释这种情况。
//
//    关于out修饰符我们可这样理解，当类、接口的泛型类型参数被声明为out时，
//    则该类型参数是协变的，泛型类型的子类型是被保留的，它只能出现在函数的输出位置，只能作为返回类型，即生产者。
//    带来的好处是，A是B的父类，那么List<A>可以是List<B>的父类。
//    我们修改下上边List赋值的代码：
//
//    val list1: List<A> = listOf(A(), A(), A())
//    val list2: List<B> = list1
//    即反过来赋值，由于B并不是A的父类，会有Type mismatch. Required:List<B> Found:List<A>错误提示。
//    为了应对这种情况，Kotlin 还提供了一个in修饰符。
//    关于in修饰符我们可这样理解，当类、接口的泛型类型参数被声明为in时，则该类型参数是逆变的，泛型类型的父类型是被保留的，
//    它只能出现在函数的输入位置，作为参数，只能作为消费类型，即消费者。
//    其实 Kotlin 中的Comparable接口使用了in修饰符：
//
//    public interface Comparable<in T> {
//        public operator fun compareTo(other: T): Int
//    }
//    写一个测试函数，编译器并不会报错：
//
//    fun test(a: Comparable<A>) {
//        val b: Comparable<B> = a
//    }
//    所以in修饰符和out修饰符的作用看起来的相对的，A是B的父类，那么Comparable<B>可以是Comparable<A>的父类，体会下区别。
//
//    2、使用处型变
//
//    为了能将Array<B>赋值给Array<A>，我们修改下之前的代码：
//
//    val array1: Array<B> = arrayOf(B(), B(), B())
//    val array2: Array<out A> = array1
//    这就是使用处型变，相比声明处型变，使用处型变就要复杂些，为了完成对应的需求，需要每次使用对应类时都添加型变修饰符。而声明处型变在类、接口声明时就做好了这些工作，因而代码会更加简洁。
//
//    再看一个数组拷贝的函数：
//
//    fun copy(from: Array<A>, to: Array<A>) {
//        for (i in from.indices) {
//            to[i] = from[i]
//        }
//    }
//    我们试着执行如下的拷贝操作：
//
//    val array1: Array<B> = arrayOf(B(), B(), B())
//    val array2: Array<A> = arrayOf(A(), A(), A())
//    copy(array1, array2)
//    同样的问题，由于泛型默认不型变的原因，copy(array1, array2)并不能正常工作。
//
//    回想一下，在 Java 中类似的问题可以使用通配符类型参数解决这个问题：
//
//    public void copy(ArrayList<? extends A> from, ArrayList<? super A> to) {}
//    那么在 Kotlin 中我们自然想到的是型变修饰符了：
//
//    Kotlin 中的out A类似于 Java 中的? extends A，即泛型参数类型必须是A或者A的子类，用来确定类型的上限
//    Kotlin 中的in A类似于 Java 中的? super A，即泛型参数类型必须是B或者B的父类，用来确定类型的下限
//    修改上边的 copy函数：
//
//    fun copy(from: List<out A>, to: List<in A>) {
//        for (i in from.indices) {
//            to[i] = from[i]
//        }
//    }
//    这样copy函数就能正常的工作了。使用处型变其实也是一种类型投影，from、in此时都是一个类型受限的投影数组，它们只能返回、接收指定类型的数据。
//
//    这些概念很容易把人搞晕，理解其作用才是关键，而不是套概念。
//
//    五、类型投影
//
//    前边我们已经知道使用处型变也是一种类型投影，除此之外还有一种星投影。
//
//    当我们不知道泛型参数的类型信息时，但仍需要安全的使用它时，可以使用星投影，用星号*表示，星投影和 Java 中的原始类型很像，但星投影是安全。
//
//    官方对星投影语法的解释如下：
//
//    对于 Foo <out T : TUpper>，其中 T 是一个具有上界 TUpper 的协变类型参数，Foo <> 等价于 Foo <out TUpper>。 这意味着当 T 未知时，你可以安全地从 Foo <> 读取 TUpper 的值。
//    对于 Foo <in T>，其中 T 是一个逆变类型参数，Foo <> 等价于 Foo <in Nothing>。 这意味着当 T 未知时，没有什么可以以安全的方式写入 Foo <>。
//    对于 Foo <T : TUpper>，其中 T 是一个具有上界 TUpper 的不型变类型参数，Foo<*> 对于读取值时等价于 Foo<out TUpper> 而对于写值时等价于 Foo<in Nothing>。
//    如果泛型类型具有多个类型参数，则每个类型参数都可以单独投影。 例如，如果类型被声明为 interface Function <in T, out U>，我们可以想象以下星投影：
//
//    Function<*, String> 表示 Function<in Nothing, String>；
//    Function<Int, *> 表示 Function<Int, out Any?>；
//    Function<*, *> 表示 Function<in Nothing, out Any?>
//    我们来看如下的代码：
//
//    val array1: Array<B> = arrayOf(B(), B(), B())
//    val array2: Array<*> = array1
//    使用星投影，我们可以将array1赋值给array2，但由于此时array2并不知道泛型参数的类型，所以不能对array2进行数据写入的操作，但可以从中读取数据：
//
//    array2[0] =A() //编译器会报错
//    val a = array2[0] // 正常
//    可以看出，星投影更适合那些泛型参数的类型不重要的场景。
}

/**
 * 28.类，对象，继承，接口等知识
 */
fun tips_28() {
    //abstract     // 抽象类
    // final       // 类不可继承，默认属性
    // enum        // 枚举类
    // open        // 类可继承，类默认是final的 需要继承必须要写open
    // annotation  // 注解类
    // private     // 仅在同一个文件中可见
    // protected   // 同一个文件中或子类可见
    // public      // 所有调用的地方都可见
    // internal    // 同一个模块中可见

    //修饰符
    //lateinit      //后期初始化
    //open          //可继承open类
    //sealed class  //密封类

    //data class    //数据类   自动生成equals（）   hashcode()  tostring()
    //data class person(name:String,age:Int)
    //object   用于单例类
    //Object Singleton{
    //    fun singletonTest(){
    //    print("我是单例内的方法")
    //    }
    // }

    /**
     * 类的定义方法
     */
    class Runoob constructor(name: String) {  //可以直接在类的声明同时写构造方法
        /**
         * 变量
         */
        var url: String = "http://www.runoob.com"
        var country: String = "CN"
        var siteName = name
        //var (name,url,city)= listOf("王道", "www.ddd", "成都")//此种方式不OK，因为解构/结构声明只能在局部变量，不能在成员变量

        /**
         * 初始化
         */
        init {
            println("初始化网站名: ${name}")
        }

        /*构造函数*/
        /*constructor() */
        //构造函数中带val var修饰的变量  kotlin会自动为它们生成getter setter函数  牛皮

        /*次构造函数*/
        constructor (name: String, alexa: Int) : this(name) {
            println("Alexa 排名 $alexa")
        }

        /*静态用这个包裹*/
//        companion object {
//            fun method1() {
//                println("你好")
//            }
//        }

        /*普通方法*/
        fun printTest() {
            println("我是类的函数")
        }
    }

    /*测试方法*/
    fun main2() {
        val runoob = Runoob("菜鸟教程")//类的调用方法
        println(runoob.siteName)
        println(runoob.url)
        println(runoob.country)
        runoob.printTest()
    }

//    //==================================抽象类
//    abstract class Derived {
//        abstract fun f()
//    }
//    //===================================接口
//    interface TestInterFace {
//        fun test()
//    }

    // this用于引用当前成员变量   super用于引用父类属性
    //内部类用inner关键字  内部类会带有一个对外部类的引用 可以引用外部成员变量和属性
    class Outer {
        private val bar: Int = 1
        var v = "成员属性"

        /**嵌套内部类**/
        inner class Inner {
            fun foo() = bar  // 访问外部类成员
            fun innerTest() {
                var o = this@Outer //获取外部类的成员变量(实例)
                println("内部类可以引用外部类的成员，例如：" + o.v)
            }
        }
    }


    //this  internal修饰的为整个模块
    class Mthis {
        val t = ""

        fun mm() {
            print("这是一个方法")
        }

        fun meth(): Mthis {//==================直接用方法的方式返回实例？
            print(this.t) //引用成员变量
            this.mm()//引用成员函数
            return this  //=====================返回实例
        }

    }


}

interface Fly {
    fun fly()
}

open class Person(name: String) {
    constructor() : this("") {}
    constructor(age: Int) : this() {}
}

/**
 * xx:person     只有次级构造
 * xx:person()  有主构造
 * extends  implement 统一用：冒号
 */
class XX : Person("张三"), Fly {
    override fun fly() {
        TODO("Not yet implemented")
    }

}

/**
 * 29.kotlin中 -> 这个符号可以理解为“转向”的意思。主要用在三个地方：
 */
fun tips_29() {
    /**
     * 1、用于when语句中
     */
    when (2) {
        1 -> println("1") //a==1时，打印"1"
        2 -> println("2")
        else -> println("其他")
    }

    /**
     * 2、文字表达式中（包含lambda）
     */
    fun main(args: Array<String>) {
        var a = 8
        var b = 5
        var c = { x: Int, y: Int -> x + y }//括号内表示定义了两个变量相加
        println(c(a, b)) //13
        println(c(12, 231)) //243
    }

    /**
     * 3、在函数的参数表中或定义变量时表示数据类型的转向关系
     */
    fun test(a: Int, b: (num1: Int, num2: Int) -> Int): Int {
        return a + b(3, 5) //8
        return a + b.invoke(3, 5) //8    为什么要用invoke？？？？
    }
}

/**
 * 30.vararg可变参数
 */
fun tips_30(vararg numbers: Int) {
    val sz = numbers.size//获取长度
    for (i in 0 until sz) {//注意此处写法
        print("fun1 bumber[$i] = ${numbers[i]}")
    }
    for (num in numbers) {
        print("fun1 $num")
    }

    /**
     * 1.简单调用
     */
    tips_30(1, 2, 3, 4, 5)
    //变参可以个数为0
    tips_30()

    /**
     * 2.使用*加数组名调用
     */
    val array = intArrayOf(6, 7, 8, 9, 0)
    tips_30(*array)

    val array2 = ArrayList<Int>()
    tips_30(*array2.toIntArray())

    //1仅能是数组，不能是集合
    //2可变参数的个数只能是一个  fun method(vararg args : Int,vararg args2 : Int) 此种不OK
    //3数组对象可返回，变参不可返回
    fun fun3_4(): IntArray? = null
    //fun fun3_4(a:Int) : (vararg arg : Int)? = null  //error

    //4.可变参数的位置：前，中、后,当可变参数不是最后一个时，其它参数要显示指定才可以  java中只能在最后面 好像是
    fun fun4(vararg args: Int, name: String, i: Int = 0) = Unit
    fun fun4(i: Int = 1, vararg args: Int, name: String) = Unit
    fun fun4(name: String, i: Int = 2, vararg args: Int) = Unit
    fun vararg_test4() {
        fun4(1, name = "前", i = 99)       //调用第1个
        fun4(1, name = "前")              //调用第1个

        fun4(2, 3, name = "中")     //调用第2个
        fun4(2, name = "中")              //调用第2个

//        fun4(name = "后", i = 10, 2, 3, 4)   //调用第3个
        fun4("后", 2, 5, 6, 7)    //调用第3个
    }

    //5.lambda的参数不支持可变参数
    //fun fun5(vararg lambdas : (vararg lmd2 : Int) -> Int)){}   //error


}

/**
 *31.尾递归条件  tailrec 暂时先不看
最后一条语句是函数调用语句
调用的函数是自身
作用：在满足尾递归条件的函数前面加tailrec修饰，编译器会优化该递归成一个快速而高效的基于循环的版本，减少栈消耗。
 */
//fun tips_31() {
//    //2.示例
//    fun factorial1(n: Int): Int {
//        return if (n == 1) 1 else n * factorial1(n - 1)
//    }
//
//    var ret2 = 1
//    tailrec fun factorial2(n: Int): Int {
//        if (n <= 1) return 1
//        ret2 *= n
//        return factorial2(n - 1)
//    }
//
//    var ret3 = 1
//    fun factorial3(n: Int): Int {
//        if (n <= 1) return 1
//        ret3 *= n
//        return factorial3(n - 1)
//    }
//
//    fun tailrec_test() {
//        var start = SystemClock.elapsedRealtimeNanos()
//        val ret1 = factorial1(6)
//        var end = SystemClock.elapsedRealtimeNanos()
////             Log.e(TAG,"ret1 = $ret1 ,takes ${end - start}")
//        println("ret1 = $ret1 ,takes ${end - start}")
//
//
//        start = SystemClock.elapsedRealtimeNanos()
//        factorial2(6)
//        end = SystemClock.elapsedRealtimeNanos()
////             Log.e(TAG_TAILREC,"ret2 = $ret2 ,takes ${end - start}")
//        println("ret2 = $ret2 ,takes ${end - start}")
//        start = SystemClock.elapsedRealtimeNanos()
//        factorial3(6)
//        end = SystemClock.elapsedRealtimeNanos()
////             Log.e(TAG_TAILREC,"ret3 = $ret3 ,takes ${end - start}")
//        println("ret3 = $ret3 ,takes ${end - start}")
//
//    }
//}

/**
 * 32.Thread线程 有意思的地方就是 kotlin的线程不用new
 */
fun tips_32() {
    Thread {
        for (i in 1..10) {
            println("I = $i")
            Thread.sleep(1000)
        }
    }.start()

    Thread {
        for (j in 10..20) {
            println("J = $j")
            Thread.sleep(2000)
        }
        Thread.sleep(1000)
    }.start()
}

/**
 * 33.object关键字在kotlin中的用例
 */
fun tips_33() {
    /**
     * 1.用作类似单例的形式
     */
//    object ObjectKeyTest {
//        override fun toString(): String {
//            return "我是一个ObjectKeyTest"
//        }
//    }


    /**
     * 2.作为伴生对象
     */
//    class CompanionTest {
//        private val string = "伴生对象可以访问我"
//        companion object {
//            fun printStr() {
//                println(CompanionTest().string)
//            }
//        }
//    }

    /**
     * 测试方法
     */
    fun test() {
//      println(ObjectKeyTest.toString())//调用直接通过类.方法；类.属性

//      CompanionTest.printStr()//可以直接调用
    }


}

/**
 * 34.lambda表达式
 */
fun tips_34() {
//    1.总是被{}括起来
//    2.参数，参数类型可以省略->函数体
    val sum = { x: Int, y: Int -> x + y }
    //嵌套类型的柯里化函数
    val sum2 = { x: Int -> { y: Int -> x + y } }

}

/**
 * 35.Anko Anko目前主要用于：Layout布局、SQLite数据库和Coroutines协程三个方面。
 * https://www.jianshu.com/p/d94c48cdca4e
 */
fun tips_35() {
    //实用性不大，不如mvvm databingding
}

/**
 * 36.异常
 */
fun tips_36() {
    class CheckKotlinException {
        fun thisIsAFunWithException() {
            throw Exception("I am an exception in kotlin")
        }

        @Throws(Exception::class)
        fun thisIsAnotherFunWithException() {
            throw Exception("I am Another exception in kotlin")
        }
    }
}

/**
 * 37.java he  kotlin 的互操作 基本注意点
 */
fun tips_37() {
//    1.平台类型表示法(Notation for Platform Type)
//    如上所述,平台类型不能在程序中显式表示,因此在kotlin语言中没有相应语法!
//    但是有时编译器或IDE要在错误/参数信息中显示平台类型,所以可用助记符标记:
//    T!                       表示T 或者 T?
//    (Mutable)Collection<T>!  表示T的Java集合 可变或不可变, 可空或不可空
//    Array<(out) T>!          表示T(或T子类)的Java数组 可空或者不可空
//
//
//    java类型就是平台类型   转过来的时候需要加个叹号
//
//
//            泛型区别
//    java                      kotlin
//    Foo<? extends Bar>   Foo<out Bar!>!
//    Foo<? super Bar>     Foo<in Bar!>!
//    List                   List<*>!
//
//    数组的互操作
//    与java不同，kotlin中的数组是非型变的，即kotlin不允许我们把一个Array<String> 赋值给 array<Any>
//    在java平台上，持有原生数据类型的数组避免了装箱，拆箱操作的开销
//    在kotlin中，对于原生类型的数组都有一个特化的类(IntArray,DoudleArray,CharArray等)来实现同样的功能，但是与
//    Array类无关，并且会编译成java原生类型数组以获得最佳性能。
//
//
//    可变参数的互操作
//    （T... elemnt）
//
//    kotlin
//    有demo  看下
//
//            java会强制抛出异常  kotlin不会  但是会报异常？
//
//
//
//    java中的object类  在kotlin中变成了Any
//            1.wait  notify
//            (Foo as java.lang.Object).wait()
//
//    2.getclass  通过kotlin的反射类kotlin。reflect。Kclass的扩展属性
//    val foolClass=foo::class.java
//
//    val fooClass =foo.java.class    //javaclass扩展属性
//
//    3.clone
//    要覆盖克隆  需要继承Cloneable
//    class Example :Cloneable{
//
//    }
//
//
//    4.finalize
//    要覆盖finalize，只需要声明它即可，不用再写override
//
//    class C{
//        protected fun finalize(){
////终止化逻辑
//        }
//    }
//
//
//    java调用kotlin
//
//    object类.INSTANCE来调用
//
//
//
//    kotlin包级函数
//
//    package com.easy.kotlin包内的 kotlinExample.kt 源文件中声明的所有函数和属性，
//    包括扩展函数都将编译成一个名为com.easy.kotlin.kotlinExampleKt的java类中的静态方法
//
//
//
//
//    @JvmField         //相当于public  可以直接访问了
////    val pages: Int = 0
//
//    @JvmStatic                  //静态
//
//    表示为一个实例字段，不会生成get 和set
//
//    可以使用@JvmName生成注解修改生成的java类的类名
//    MyKotlinExample  有demo
//
//
//            kt      java
//
//            private   private
//            protected  protected
//            internal   public
//            public   public
//
//
//            kotlin  函数自动重载
//
//            method（a:Int=0,b:Int=3）{}
//
//    method(1)
//    method(1,2)
//    method(,2)???
//
//    @JvmOverloads  用此注解在kotlin   java用的时候也有多个重载函数了
//
//            @Throws(Exception::class)//有demo
//
//
//
//            14关键字冲突   java 方法 is(){}       java中可以 'is'.xxxx     谁会这样写？
}

/**
 * 38.coroutines协程的写法和基本点
 */
suspend fun tips_38() {
    //第一点
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


    //第二点   使用 runBlocking 来包装 main 函数的执行
    fun main3() = runBlocking<Unit> { // 开始执行主协程
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
    }

    //第三点 延迟一段时间来等待另一个协程运行并不是一个好的选择。让我们显式（以非阻塞方式）等待所启动的后台 Job 执行结束：
    val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join() // 等待直到子协程执行结束

    //
    fun main4() = runBlocking { // this: CoroutineScope
        launch { // 在 runBlocking 作用域中启动一个新协程
            delay(1000L)
            println("World!")
        }
        println("Hello,")
    }

    //repeat关键字表示重复     此处表示0---9
    fun main5() = runBlocking { // this: CoroutineScope
        repeat(10) { // 在 runBlocking 作用域中启动一个新协程
            println("协程挂起----$it")
            delay(500)
        }
    }

// -------------------------------------------------------------第6点  顺序
//    Log.e("TAG","1---------")
//    main5()
//    Log.e("TAG","6---------")
//
//
//fun main5() = runBlocking { // this: CoroutineScope
//    launch {
////            delay(200L)
//        Log.e("TAG","3---------")
//    }
//    coroutineScope { // 创建一个协程作用域
//        launch {
////                delay(500L)
//            Log.e("TAG","4---------")
//        }
////            delay(100L)
//        Log.e("TAG","2---------") // 这一行会在内嵌 launch 之前输出
//    }
//    Log.e("TAG","5---------") // 这一行在内嵌 launch 执行完毕后才输出
//}

//    //--------------------------------------------------------------------7继续顺序
//    fun main6() = runBlocking {
//        launch { doWorld() }
//        println("Hello,我比launch括号内的先运行，除非给我delay()")
//    }
//
//    // 这是你的第一个挂起函数
//    suspend fun doWorld() {
//        delay(1000L)
//        println("World!")
//    }

    //--------------------------------------------------------------------8启动大量协程 但是轻量
    //    运行以下代码：
    fun main() = runBlocking {
        repeat(100_000) { // 启动大量的协程
            launch {
                delay(5000L)
                print(".")
            }
        }
    }
}

/**
 * 取消协程
 */
fun tips_39() = runBlocking {
    val job1 = launch {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job1.cancel() // 取消该作业
    job1.join() // 等待作业执行结束
//        这里也有一个可以使 Job 挂起的函数 cancelAndJoin 它合并了对 cancel 以及 join 的调用。
    println("main: Now I can quit.")


    //然而，如果协程正在执行计算任务，并且没有检查取消的话，
    //    那么它是不能被取消的，就如如下示例代码所示：
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
    //  while (isActive)   你可以看到，现在循环被取消了。isActive 是一个可以被使用在 CoroutineScope 中的扩展属性。


    //而 withTimeoutOrNull 通过返回 null 来进行超时操作，从而替代抛出一个异常：
    //通过withTimeout抛异常或者withTimeoutOrNull返回null的方式退出协程？？？

    fun main() = runBlocking {
        val result = withTimeoutOrNull(2000) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
            println("done")
            "Done" // 在它运行得到结果之前取消它
        }
        println("done2")
        if (result === null) println("Result is $result") //result关键字用作返回
    }
//    2021-11-30 09:17:15.694 11176-11176/com.example.mykotlintest_2 E/TAG: I'm sleeping 0 ...
//    2021-11-30 09:17:16.227 11176-11176/com.example.mykotlintest_2 E/TAG: I'm sleeping 1 ...
//    2021-11-30 09:17:16.765 11176-11176/com.example.mykotlintest_2 E/TAG: I'm sleeping 2 ...
//    2021-11-30 09:17:17.307 11176-11176/com.example.mykotlintest_2 E/TAG: I'm sleeping 3 ...
//    2021-11-30 09:17:17.741 11176-11176/com.example.mykotlintest_2 E/TAG: done2
//    2021-11-30 09:17:17.741 11176-11176/com.example.mykotlintest_2 E/TAG: Result is null

    //下面是官方的综合示例
    var acquired = 0

    class Resource {
        init {
            acquired++
        } // Acquire the resource

        fun close() {
            acquired--
        } // Release the resource
    }

    fun main2() {
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
//    Target platform: JVMRunning on kotlin v. 1.6.0
//    You can get the full code here.
//    If you run the above code you'll see that it does not always print zero,
//    though it may depend on the timings of your machine you may need to
//    tweak timeouts in this example to actually see non-zero values.
//
//    Note, that incrementing and decrementing acquired counter here from
//    100K coroutines is completely safe,
//    since it always happens from the same main thread.
//    More on that will be explained in the next chapter
//    on coroutine context.
//    To workaround this problem
//    you can store a reference to the resource in the
//    variable as opposed to returning it from the withTimeout block.


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
//    Outside of runBlocking all coroutines have completed
//    println(acquired) // Print the number of resources still acquired
//    Target platform: JVMRunning on kotlin v. 1.6.0
//    You can get the full code here.

}

/**
 *组合挂起函数
 */
suspend fun tips_40() {
    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了一些有用的事
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这里也做了一些有用的事
        return 29
    }

    val time = measureTimeMillis {//计算上面两个挂起协程的时间
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")

        //    你可以使用 .await() 在一个延期的值上得到它的最终结果，
//        val one1 = async { doSomethingUsefulOne() }//=========异步协程
//        val two2 = async { doSomethingUsefulTwo() }//=========异步协程
//        println("The answer is ${one1.await() + two2.await()}")
//        println("The answer is ${one1.await() + two2.await()}")
    }
    println("Completed in $time ms")


//    惰性启动的 async
//            可选的，async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
//    在这个模式下，只有结果通过 await 获取的时候协程才会启动，
//    或者在 Job 的 start 函数调用的时候。运行下面的示例：


//    val time2 = measureTimeMillis {
//        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
//        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
//        // 执行一些计算
//        one.start() // 启动第一个
//        two.start() // 启动第二个
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")


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


}

/**
 *协程上下文和调度器
 */
fun tips_41() = runBlocking {
    /*协程调度器:
    1可以将协程限制在一个特定的线程执行，
    2或将它分派到一个线程池，
    3亦或是让它不受限地运行。*/

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


    // 启动一个协程来处理某种传入请求（request）  父协程会等待子协程完成，父协程取消，子协程也会递归取消
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
    /*这段代码的输出如下：
    job1: I run in GlobalScope and execute independently!
    job2: I am a child of the request coroutine
    job1: I am not affected by cancellation of the request
    main: Who has survived request cancellation?*/


    // 启动一个协程来处理某种传入请求（request）
    val request2 = launch {
        repeat(3) { i -> // 启动少量的子作业
            launch {
                delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still active")
    }
    request.join() // 等待请求的完成，包括其所有子协程
    println("Now processing of the request is complete")
    /*  request: I'm done and I don't explicitly join my children that are still active
        Coroutine 0 is done
        Coroutine 1 is done
        Coroutine 2 is done
        Now processing of the request is complete*/


    /*然而，当一个协程与特定请求的处理相关联时或做一些特定的后台任务，最好将其明确命名以用于调试目的。
    CoroutineName 上下文元素与线程名具有相同的目的。
    当调试模式开启时，它被包含在正在执行此协程的线程名中。*/
    val v1 = async(CoroutineName("v1coroutine")) {
        delay(500)
        println("Computing v1")
        252
    }
    val v2 = async(CoroutineName("v2coroutine")) {
        delay(1000)
        println("Computing v2")
        6
    }
    println("The answer for v1 / v2 = ${v1.await() / v2.await()}")


    //用+号 牛 显式指定一个调度器来启动协程并且同时显式指定一个命名：
    launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }


//    通过创建一个 CoroutineScope 实例来管理协程的生命周期  配合 安卓的activity
//    并使它与 activity 的生命周期相关联。
//    CoroutineScope 可以通过
//            CoroutineScope() 创建或者通过MainScope() 工厂函数。
//    前者创建了一个通用作用域，
//    而后者为使用 Dispatchers.Main 作为默认调度器的 UI 应用程序 创建作用域：

    class Activity {
        private val mainScope = MainScope()

        fun destroy() {
            mainScope.cancel()
        }
        // 继续运行……
//        现在，我们可以使用定义的 scope 在这个 Activity 的作用域内启动协程。
//        对于该示例，我们启动了十个协程，它们会延迟不同的时间：


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

    //测试
    val activity = Activity()
    activity.doSomething() // 运行测试函数
    println("Launched coroutines")
    delay(500L) // 延迟半秒钟
    println("Destroying activity!")
    activity.destroy() // 取消所有的协程
    delay(1000) // 为了在视觉上确认它们没有工作


    // ThreadLocal， asContextElement 扩展函数在这里会充当救兵。

}

/**
 *异步流
 */
@InternalCoroutinesApi
fun tips_42() {
    //同步用sequence
    fun simple(): Sequence<Int> = sequence { // 序列构建器
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们正在计算
            yield(i) // 产生下一个值
        }
    }

    fun main3() {
        simple().forEach { value -> println(value) }
    }

    //异步计算的值流（stream）， 我们可以使用 Flow 类型
    //    运行以下代码
    fun simple2(): Flow<Int> = flow { // 流构建器
        for (i in 1..3) {
            delay(1000) // 假装我们在这里做了一些有用的事情
            emit(i) // 发送下一个值
        }
    }

    fun main() = runBlocking<Unit> {
        // 启动并发的协程以验证主线程并未阻塞
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
        }
        // 收集这个流
        simple2().collect { print("$it----") }
    }
//    2021-11-30 16:13:36.058 29702-29702/com.example.mykotlintest_2 E/TAG: I'm not blocked 1
//    2021-11-30 16:13:36.195 29702-29702/com.example.mykotlintest_2 E/TAG: I'm not blocked 2
//    2021-11-30 16:13:36.338 29702-29702/com.example.mykotlintest_2 E/TAG: I'm not blocked 3
//    2021-11-30 16:13:37.081 29702-29702/com.example.mykotlintest_2 E/TAG: 1----
//    2021-11-30 16:13:38.100 29702-29702/com.example.mykotlintest_2 E/TAG: 2----
//    2021-11-30 16:13:39.139 29702-29702/com.example.mykotlintest_2 E/TAG: 3----


    //    流采用与协程同样的协作取消。
//    像往常一样，流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消。
//    以下示例展示了当 withTimeoutOrNull 块中代码在运行的时候流是如何在超时的情况下取消并停止执行其代码的：
    fun simple3(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }

    fun main4() = runBlocking<Unit> {
        withTimeoutOrNull(250) { // 在 250 毫秒后超时-------------------cancel
            simple3().collect { value -> println(value) }
        }
        println("Done")
    }

    //其他方式流
    suspend fun performRequest(request: Int): String {
        delay(1000) // 模仿长时间运行的异步工作
        return "response $request"
    }
//    fun main5() = runBlocking<Unit> {
//        (1..3).asFlow() // 一个请求流
//                .map { request -> performRequest(request) }
//                .collect { response -> println(response) }
//    }


    //虽然take获取前两个emit 但是这里好像不OK  运行直接到 finally in numbers
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

    fun main6() = runBlocking<Unit> {
        numbers().take(2) // 只获取前两个
//            .collect { value -> println(value) }
            .collect { print(it) }
    }

//   =================================== 求和（末端操作符）
//    val sum = (1..5).asFlow()
//            .map { it * it } // 数字 1 至 5 的平方
//            .reduce { a, b -> a + b } // 求和（末端操作符）

    //==================================filter过滤
//    (1..5).asFlow()
//            .filter {
//                println("Filter $it")
//                it % 2 == 0
//            }
//            .map {
//                println("Map $it")
//                "string $it"
//            }.collect {
//                println("Collect $it")
//            }

//=====================================================流的该属性称为 上下文保存 。
//    withContext(context) {
//        simple().collect { value ->
//            println(value) // 运行在指定上下文中
//        }
//    }

    //flow中不允许withContext修改协程上下文  通过以下方式修改
//    fun simple(): Flow<Int> = flow {
//        for (i in 1..3) {
//            Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
//            log("Emitting $i")
//            emit(i) // 发射下一个值
//        }
//    }.flowOn(Dispatchers.Default) // 在流构建器中改变消耗 CPU 代码上下文的正确方式
//    ​
//    fun main() = runBlocking<Unit> {
//        simple().collect { value ->
//            log("Collected $value")
//        }
//    }


    //====================================通过流水线的方式处理协程的发射 和接受处理 可以有效节约时间
//    val time = measureTimeMillis {
//        simple()
//                .buffer() // 缓冲发射项，无需等待
//                .collect { value ->
//                    delay(300) // 假装我们花费 300 毫秒来处理它
//                    println(value)
//                }
//    }
//    println("Collected in $time ms")


    //================================不用处理全部的值  只用最新的？？？？
//    val time = measureTimeMillis {
//        simple()
//                .conflate() // 合并发射项，不对每个值进行处理
//                .collect { value ->
//                    delay(300) // 假装我们花费 300 毫秒来处理它
//                    println(value)
//                }
//    }
//    println("Collected in $time ms")


    //====================================只处理最后 一个值   conflatlatest
//    val time = measureTimeMillis {
//        simple()
//                .collectLatest { value -> // 取消并重新发射最后一个值
//                    println("Collecting $value")
//                    delay(300) // 假装我们花费 300 毫秒来处理它
//                    println("Done $value")
//                }
//    }
//    println("Collected in $time ms")


    //============================组合值
//    val nums = (1..3).asFlow() // 数字 1..3
//    val strs = flowOf("one", "two", "three") // 字符串
//    nums.zip(strs) { a, b -> "$a -> $b" } // 组合单个字符串
//            .collect { println(it) } // 收集并打印
//    示例打印如下：
//    1 -> one
//    2 -> two
//    3 -> three
//    Combine


//    ======================================让我们在捕获异常的时候发射文本：
//    simple()
//            .catch { e -> emit("Caught $e") } // 发射一个异常
//            .collect { value -> println(value) }

    //=====================================防止下流异常逃逸 此方法适用于捕获全部异常  onEach
//    我们可以将 catch 操作符的声明性与处理所有异常的期望相结合，
//    将 collect 操作符的代码块移动到 onEach 中，
//    并将其放到 catch 操作符之前。
//    收集该流必须由调用无参的 collect() 来触发：
//    simple()
//            .onEach { value ->
//                check(value <= 1) { "Collected $value" }
//                println(value)
//            }
//            .catch { e -> println("Caught $e") }
//            .collect()

//    可以使用 onCompletion 操作符重写前面的示例，并产生相同的输出：（指的是 try{---}finally{ println("Done") }）
//    simple()
//            .onCompletion { println("Done") }  //表示收集完成后做的操作
//            .collect { value -> println(value) }

    // ===============================================如果我们在 onEach 之后使用 collect 末端操作符，
// 那么后面的代码会一直等待直至流被收集：
    //                    // 模仿事件流
    //                    fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }
    //                    ​
    //                    fun main() = runBlocking<Unit> {
    //                        events()
    //                                .onEach { event -> println("Event: $event") }
    //                                .collect() // <--- 等待流收集
    //                        println("Done")
    //                    }


    //launchIn 末端操作符可以在这里派上用场。
    //                    使用 launchIn 替换 collect 我们可以在单独的协程中启动流的收集，这样就可以立即继续进一步执行代码：
    //                    fun main() = runBlocking<Unit> {
    //                        events()
    //                                .onEach { event -> println("Event: $event") }
    //                                .launchIn(this) // <--- 在单独的协程中执行流
    //                        println("Done")}

//    onEach { ... }.launchIn(scope)


    //注意此写法
//    fun main() = runBlocking<Unit> {
//        (1..5).asFlow().collect { value ->
//            if (value == 3) cancel()
//            println(value)
//        }
//    }
//           改写为下面cancellable
//    fun main() = runBlocking<Unit> {
//        (1..5).asFlow().cancellable().collect { value ->
//            if (value == 3) cancel()
//            println(value)
//        }
//    }


}

/**
 *通道  通道提供了一种在流中传输值的方法。
 */
fun tips_43() = runBlocking {
    val channel = Channel<Int>()
    launch {
        // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
        for (x in 1..5) channel.send(x * x)
    }
// 这里我们打印了 5 次被接收的整数：
    repeat(5) { println(channel.receive()) }
    println("Done!")


    //close用于关闭 关闭之后不会有新的进入
    val channel2 = Channel<Int>()
    launch {
        for (x in 1..5) channel2.send(x * x)
        channel2.close() // 我们结束发送
    }
// 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
    for (y in channel) println(y)
    println("Done!")


//    这里有一个名为 produce 的便捷的协程构建器，可以很容易的在生产者端正确工作，
//    并且我们使用扩展函数 consumeEach 在消费者端替代 for 循环：
//    val squares = produceSquares()
//    squares.consumeEach { println(it) }
//    println("Done!")


}

/**
 * by表示代理模式    databingding常用于绑定代理类viewmodels
 */
fun tips_44() {
//    kotlin 中的委托模式依靠by关键字，语法定义
//    val/var <Property name> :<Type>by  <expression>
//    var/val：属性类型(可变/只读)
//    name：属性名称
//    Type：属性的数据类型
//    expression：代理类

//    使用场景:
//    A.延迟加载属性(lazy property): 属性值只在初次访问时才会计算,
//    B.可观察属性(observable property): 属性发生变化时, 可以向监听器发送通知,
//    C.将多个属性保存在一个 map 内, 而不是保存在多个独立的域内.

}


/**
 * 45.lambda推导过程
 */
fun tips_45() {
    val list = listOf("apple", "orange", "pear")
    val lambda = { fruit: String -> fruit.length }
    val maxl1 = list.maxBy(lambda)

    //{参数1：类型，参数2：类型->函数体}

    //移入括号内
    val maxl2 = list.maxBy({ fruit: String -> fruit.length })

    //规定如果lambda是函数的最后一个参数，可以移到函数外部
    val maxl3 = list.maxBy() { fruit: String -> fruit.length }

    //当lambda是唯一参数时，可以将括号省略
    val maxl4 = list.maxBy { fruit: String -> fruit.length }

    //不用声明参数类型
    val maxl5 = list.maxBy { fruit -> fruit.length }

    //只有一个参数时，可以用IT代替
    val maxl6 = list.maxBy { it.length }

}


/**
 * 判空处理
 */
fun tips_46() {
//    在 Kotlin 中，判断一个对象是否为空一般有以下几种方式：
//    使用 if 表达式判断对象是否为 null，例如：
//    kotlin
//    val str: String? = null
//    if (str == null) {
//        // 对象为空的处理逻辑
//    } else {
//        // 对象不为空的处理逻辑
//    }
//    使用安全调用运算符（?.）调用对象的方法或属性，如果对象是 null，则返回 null，例如：
//
//    kotlin
//    val str: String? = null
//
//    val length = str?.length // 如果 str 为 null，则 length 也为 null
//    使用 Elvis 运算符（?:）提供一个默认值，例如：
//
//    kotlin
//    val str: String? = null
//
//    val length = str?.length ?: 0 // 如果 str 为 null，则 length 为 0
//    使用非空断言运算符（!!）标记对象为非空，在对象为空时会抛出 NullPointerException 异常，例如：
//
//    kotlin
//    val str: String? = null
//
//    val length = str!!.length // 如果 str 为 null，则会抛出 NullPointerException 异常
//    需要注意的是，在使用非空断言运算符时，需要确保对象不为空，否则可能会引发运行时异常。推荐使用安全调用运算符或 Elvis 运算符来避免这种情况的发生。


}


