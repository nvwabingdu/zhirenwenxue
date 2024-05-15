package com.example.zrlearn.b_java;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2020-10-09
 * Time: 15:34
 */
class LearnJava {
    /**
     * java学习篇1------------标志符，数据类型，运算符
     */
    public void indentifier() {
//一.标识符
//    1.标识符由 字母  数字  _  $  组成
//    2.数字不能作为开头，区别大小写，长度没有限制，不能有其他字符，不允许插入空白，不允许占用其他特殊关键字
//    3.java源代码使用的是Unicode（65536）,比ASCII码（255）大的多。

//    4.类名，接口名   大驼峰   HelloWrold   Customer    SortClass
//    5.方法名，变量名 小驼峰  setName      getAdress   balance
//    6.常量   BULE_COLOR      (尽量为大写字母，单词之间用_分隔)   用final标记

//二.数据类型
//    基本数据类型
//    类型  类型          默认值              位数
//    整数  byte           0             （8位 1字节）  -2^7~2^7-1     (-128~127)
//         short          0             （16位 2字节） -2^15~-2^15-1  (-32768~32767)
//         int            0             （32位 4字节） -2^31~-2^31-1  (-2147483648~2147483647)
//         long           0L            （64位 8字节） -2^63~-2^63-1  (-9223372036854775808~9223372036854775807)
//    浮点数
//         float          0.0f          （32位 4字节）  1.4e-45f~3.4028235e+38f
//         double         0.0d          （64位 8字节）  4.9e-324d~1.7976931348623157e+308d   (除非写f,否则默认为double类型,也就是说可以不加d)
//    字符
//         char      ('\u0000')(null)   （一个char表示一个Unicode字符）  0-65535
//                                 普通：'a'       转义字符：  \b 退格    \n换行      \r回车     \t水平制表符（tab）
//                                                转义字符：  \\反斜杠\   \'单引号'   \"双引号"
//    布尔类型
//         Boolean         false        （8位）
//    复合数据类型
//         类class        (null)
//         数组[]          (null)
//         接口interface   (null)
//         String         (null)

//三.运算符
//          1.算术运算符
//              +（加）     - （减）    *（乘）     /（除）   % （取模）(取模就是取余数)
//                              3%2=1  15.2%5=0.2    5%-3=2   -5%-3=-2   (结果的符号取第一个操作数的符号)
//                              ++  --         i++(在i使用之后+1)   ++i（在i还没有之前就+1）
//          2.关系运算符
//                 >    <    >=   <=     ==   !=
//                               ==   !=  可以运用于任何数据类型  一般用于引用数据类型  对对象的比较？
//          3.逻辑运算符
//               逻辑非!
//               &和&&：当且仅当 a、b 都为 true 时，结果为 true ；
//               |和|| ： 当且仅当 a、b 都为 false 时，结果为 false ；
//               ^ : a与b 相异时，结果为 true ；
//               短路与&&     短路或||    都只需要计算左边的就好了
//          4.位运算符
//              关于位运算符无非也就
//              与(&)、  同真为真
//              或(|)、  有真为真
//              异或(^)、真假为真
//              取反(~)、真假互换
//              左移(<<)、  12<<2   12向左移动2位
//              右移(>>)、  12>>2   12向右移动2位
//              无符号右移(>>>)
//          5.其他运算符
//              扩展赋值运算符（+=  -=  *=   /=  %=  |=  ^=  >>=   <<=   >>>= ）
//              条件运算符（?:）也叫三元运算符、三目运算符
//                            逻辑表达式？语句1：语句2；   =====》    Boolean ？true：flase；
//                            z = (x>y)?x:y;//z变量存储的就是两个数的大数
//              点运算符（.）
//              实例运算符（instanceof） 判断是不是某人的儿子
//              new运算符、数组下标运算符（[]）……

//
//    知识1：Java位运算是针对于整型（byte、char、short、int、long）数据类型的二进制进行的移位操作。
//    知识2：计算机表示数字正负不是用+ -加减号来表示，而是用最高位数字来表示，0表示正，1表示负 。
//    参考：https://www.cnblogs.com/SunArmy/p/9837348.html
//    参考：https://www.cnblogs.com/yuanhailiang/p/9479105.html
//    运算符的优先级（从高到低）
//    优先级	描述	                 运算符
//    1	括号	            ()、[]
//    2	正负号           +、-
//    3	自增自减，非     	++、--、!
//    4	乘除，取余	    *、/、%
//    5	加减	            +、-
//    6	移位运算	        <<、>>、>>>(>>>表示无符号右移 移位之后补位)
//    7	大小关系        	>、>=、<、<=
//    8	相等关系	        ==、!=   、 .equals
//    9	按位与	        &
//    10按位异或	        ^
//    11按位或           |
//    12逻辑与	        &&
//    13逻辑或	        ||
//    14条件运算	        ?:   z=(x>y)?x:y;
//    15赋值运算	        =、+=、-=、*=、/=、%=
//    16位赋值运算	    &=、|=、<<=、>>=、>>>=

        //关于位运算
        {
            /* 1 = 0000 0001 */
            /* 60 = 0011 1100 */
            int A = 60;
            int B = 13;
            Log.e("TAG", "A&B=" + (A & B));
            Log.e("TAG", "A&B=" + (A & B));
            Log.e("TAG", "A|B=" + (A | B));
            Log.e("TAG", "~A=" + (~A));
            Log.e("TAG", "A^B=" + (A ^ B));
            Log.e("TAG", "A<<B=" + (A << 2));
            Log.e("TAG", "A>>B=" + (A >> 2));
            Log.e("TAG", "A>>>B=" + (A >>> 2));
//          扩展：
//        场景1：判断奇偶数 a&1 结果为 0 ，a就是偶数 结果为 1 ，a就是奇数
//        场景2：求平均数 （x+y）/2 这样吗？考虑过 x+y可能超过int的范围吗？正确的姿势是 (x&y)+((x^y)>>1)
//        场景3：有两个int类型变量x、y,要求两者数字交换，不用临时变量？(当年学java的时候这可是奥数级别的题目) x ^= y; y ^= x; x ^= y;
//        场景4：求绝对值
//        int abs( int x ) { int y= x >> 31 ; return (x^y)-y ; //or: (x+y)^y }
//        场景5：取模 a % (2^n) 等价于 a & (2^n - 1)
//        场景6：快速乘法 a * (2^n) 等价于 a << n
//        场景7：快速除法 a / (2^n) 等价于 a >> n
//        场景8：求相反数 (~x+1)
        }
    }

    /**
     * java学习篇2------------循环，if，foreach
     */
    private void myIf() throws Exception {
//-----if语句
//        if (true) { } else { }
//-----if else if语句
//        int a = 21;
//        if (a > 1) { } else if (a > 3) { } else if (a > 5) { }
//-----switch 语句
//        int b = 0;
//        switch (b) {
//            case 0,100,300,440,55://特殊写法(java高版本适用)
//                xxxx具体的操作
//                break;
//            case 1:
//            case 2:/*你好*/ break;
//            case 3:/*他好*/ break;
//            default:/*大家好*/ break;
//        }
//-------for循环  for(初始语句;条件表达式;迭代器){语句}
//        for (int i = 0; i < 10; i++) {/*10次循环*/}
//        for (int a = 0, b = 0; a < 10; a++, b++) {/*要做什么操作*/}
//-------while (){}语句
//        int c = 10;
//        while (c < 3) {/*你是爱我的*/c++; }

//--------加上do的while循环
//        int d = 0;
//        do {/*我至少执行一次*/} while (d < 2);

//------- foreach语句用法
//        int[] arryA = new int[10];
//        for (int a : arryA) {
//            /*要做什么操作*/
//        }
    }

    /**
     * java学习篇3------------数组Arrays
     */
    private void myArrays() {
        //    一，声明
        int array[] = new int[10];
        int[] array2 = new int[10];//----------用此写法
        int array3[];
        int array4[] = {1, 2, 3, 4, 5, 6};
        //   多维数组
        int anInt[][] = new int[1][];
        int[][] anInt2 = new int[0][1];//-------用此写法
        int[] anInt3[] = new int[0][];
        //        anInt.length 二维数组也有length属性，但是只表示第一维长度。
        array.equals(array2[1]);
        Arrays.sort(array);//升序
        Arrays.fill(array, 1995);//所有值为1995
        Arrays.binarySearch(array, 3);//二分查找3
        String str = "你是一个大傻逼";
        StringBuffer strB = new StringBuffer().append(9);
        strB.capacity();//String没有此方法   StringBuffer有此方法  返回当前容量  一般来说返回的是当前字符串加上16个字符的内容
        // ==是用于判断两个字符串对象是否是同一实例，即它们在内存中的地址是否相同。  判断地址  .equals判断的是文字是否相同？
        // 二，数组的赋值
        // 方法一
        array[1] = 1;
        array[2] = 2;
        //方法二
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        //多维数组用两层循环嵌套，不在赘述

    }

    /**
     * java学习篇4------------常用类
     */
    private void frequentlyClass() {

        /*String类:字符串常量，速度最慢*/
        {
            String str = "无名之辈";
        }

        /*StringBuffer类：字符串变量，线程安全，速度较快*/
        {
            StringBuffer strbuf = new StringBuffer();
            strbuf.append("你好");
        }

        /*StringBuilder：字符串变量，线程不安全，速度最快*/
        {
            StringBuilder strbul = new StringBuilder();
        }


        /*math类*/
        {
            Math.abs(21);
            Math.max(4,2);
        }

        /*Random类*/
        {
            Random random = new Random();
            random.nextInt(10);
            new Random().nextDouble();//简写
        }

        /*Arrays*/
        {
            Arrays.sort(new int[]{21, 21, 12, 3, 43});
            Arrays.binarySearch(new int[]{1, 2, 3, 4}, 2);
        }

        /*Pattern正则类*/
        {
            Pattern p = Pattern.compile("\\d+");//指定正则表达式规则
            Matcher m = p.matcher("22bb23");//返回Matcher类实例
            m.lookingAt();//返回true,因为\d+匹配到了前面的22
            Matcher m2 = p.matcher("aa2223");
            m2.lookingAt();//返回false,因为\d+不能匹配前面的aa
        }
    }

    /**
     * java学习篇5------------IO流
     */
    private void myIoStream() throws Exception {

    }

    /**
     * java学习篇6------------序列化Serializable
     * 序列化作用保存到本地便于永久存储
     */
    private class mySerializable {

        /**
         * 序列化和反序列化
         * @param objOut  new Person("zhangSan")
         * @param objectSerializableFileAddress  "C:\\Users\\wq\\Desktop\\img\\2.txt"
         * @throws Exception
         */
        public  void ObjectStream(Object objOut,String objectSerializableFileAddress) throws Exception{
		/**
		 * 序列化对象
		 */
//		 ObjectOutputStream objectOutputStream =new ObjectOutputStream(new FileOutputStream(new File(objectSerializableFileAddress)));
//		 objectOutputStream.writeObject(objOut);
//		 objectOutputStream.flush();
//		 objectOutputStream.close();


		 /**
		  * 反序列化对象
		  */
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(objectSerializableFileAddress)));
            Object objIn = objectInputStream.readObject();
            objectInputStream.close();
//		    System.out.print(objIn.toString());
        }


        /**
         * @Author wq
         * @Date 2022/11/20-13:10
         * 此类用于序列化测试
         *    1.Thread线程类/static静态类/成员方法/静态成员变量  都不适用于序列化
         *    2.关键字：transient 表示为不用于序列化
         */
         class Person implements Serializable{
            String name;
            int age;
            transient String love;//不参加序列化
            public Person() {//反序列化不加无参构造会报错

            }
            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
            @Override
            public String toString() {
                return "Person{" +
                        "name='" + name + '\'' +
                        ", age=" + age +
                        '}';
            }
        }


        /**
         * //对象集合序列化  扩展知识
         * public class SerTest {
         * 	public static void main(String[] args) throws Exception {
         * 		// 创建 学生对象
         * 		Student student = new Student("老王", "laow");
         * 		Student student2 = new Student("老张", "laoz");
         * 		Student student3 = new Student("老李", "laol");
         *
         * 		ArrayList<Student> arrayList = new ArrayList<>();
         * 		arrayList.add(student);
         * 		arrayList.add(student2);
         * 		arrayList.add(student3);
         * 		// 序列化操作
         * 		// serializ(arrayList);
         *
         * 		// 反序列化
         * 		ObjectInputStream ois  = new ObjectInputStream(new FileInputStream("list.txt"));
         * 		// 读取对象,强转为ArrayList类型
         * 		ArrayList<Student> list  = (ArrayList<Student>)ois.readObject();
         *
         *       	for (int i = 0; i < list.size(); i++ ){
         *           	Student s = list.get(i);
         *         	System.out.println(s.getName()+"--"+ s.getPwd());
         *                }
         * 	}
         *
         * 	private static void serializ(ArrayList<Student> arrayList) throws Exception {
         * 		// 创建 序列化流
         * 		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("list.txt"));
         * 		// 写出对象
         * 		oos.writeObject(arrayList);
         * 		// 释放资源
         * 		oos.close();
         * 	}
         * }
         */

    }

    /**
     * java学习篇7------------多线程Thread
     */
    private class myThread {
/**                       ④阻塞状态
①新建状态	②就绪状态	③运行状态 	⑤终止状态
线程的创建
   1、继承Thread类  Java.lang包中的Thread类，是一个专门用来创建线程的类，该类中提供了线程所用到的属性和方法。我们通过创建该类的子类来实现多线程。
        1) 子类覆盖父类中的run方法，将线程运行的代码存放在run中。
        2) 建立子类对象的同时线程也被创建。
        3) 通过调用start方法开启线程。*/

        /**
         * 用于测试java纯代码
         * new Thread(()->{System.out.println("一行代码搞定线程");}).start(); 简写模式
         */
        private void mainTest() {
            /**
             * 1.匿名线程
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /** 线程任务*/
                }
            }).start();

            /**
             *2.继承线程类，手动写run方法
             */
            class Mthread extends Thread {
                @Override
                public void run() {
                    /* super.run();*/
                    for (int i = 1; i <= 100; i++) {
                        Log.e("TAG", "现在输出的是：" + i + "个数字");

                        /** 加入判断，如果到50 就睡4秒钟*/
                        if (i == 50) {
                            try {
                                sleep(4000);/** 睡觉4秒钟*/


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        /** 加入判断*/
                    }

                    /**-------------用于run内的方法*/
                    try {
                        sleep(500);//线程睡觉
                        wait(1000);//线程等待   和notify  notifyall是一对，先wait 在notify
                        notify();//唤醒某个等待线程
                        notifyAll();//唤醒所有等待线程
                        setPriority(Thread.NORM_PRIORITY);//5
                        join();
                        //other.join();//调用join方法，等待线程other执行完毕
                        //other.join(1000); //等待 other 线程，等待时间是1000毫秒。
                        //yield();//强制终止线程  线程的礼让
                        interrupt();//中断线程
                        interrupted();//判断是否被中断，并清除当前中断状态
                        isInterrupted();//判断是否被中断
                        //stop已经过时不再使用，因为用stop会导致数据异常。
                        //interrupt();//为线程设置中断标志，不会立即结束线程
                        int i = 0;
                        while (true) {
                            i++;
                            if (i == 90) {
                                this.interrupt();//设置中断标志
                            }
                            if (this.isInterrupted()) {//是否有中断标记，有的话就结束？？？？  不如直接设置！！！
                                break;
                            }
                        }
                    } catch (Exception e) {
                    }
                    /**-------------用于run内的方法*/
                }
            }
            Mthread mT = new Mthread();
            mT.start();

            /** -----------------------测试和线程的方法*/
            try {
                mT.start();
                mT.getId();
                mT.getName();
                mT.getPriority();
                mT.interrupt();
                mT.isAlive();//测试当前线程是否在活动
                mT.setDaemon(true);
                mT.wait(100);
                mT.notify();
                mT.notifyAll();
                mT.stop();//已经不再使用
                mT.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /** -----------------------测试和线程的方法*/


            /**
             * 3.实现Runnable接口
             */
            class MthreadForRunnable implements Runnable {
                @Override
                public void run() {
                    for (int i = 1; i <= 100; i++) {
                        Log.e("TAG", "现在输出的是：" + i + "个数字");

                        /** 加入判断，如果到50 就睡4秒钟*/
                        if (i == 50) {
                            try {
                                Thread.sleep(4000);/** 睡觉4秒钟*/
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        /** 加入判断*/
                    }
                }
            }
            MthreadForRunnable mthreadForRunnable = new MthreadForRunnable();
            Thread t = new Thread(mthreadForRunnable);//装入一个线程类
            t.start();

            /**
             * 4.实现callable（实现Callable  好处有返回值   可以抛出异常  可以得到一个Future对象 用于监视线程，但会阻塞 直到得到结果）
             */
            class MthreadForCallable implements Callable {
                @Override
                public Object call() throws Exception {
                    return null;
                }
            }

            MthreadForCallable mthreadForCallable = new MthreadForCallable();
            ExecutorService executorService = Executors.newSingleThreadExecutor();//这两步是固定写法
            Future mFuture = executorService.submit(mthreadForCallable);//Future对象 用于监视线程，但会阻塞 直到得到结果

            try {
                mFuture.get();//等待线程结束 并返回结果
            } catch (Exception e) {
                e.printStackTrace();
            }


            /**
             * 5.简写方法 lambda表达式 (关于lambda表达式的说明 如下：1，用于接口，或者继承于接口的类。  2.此接口只有一个方法，因此简写即可)
             */
            Runnable runnable = () -> {
                /** run方法*/
            };
            runnable.run();
        }


        /** synchronized 同步锁  最好用此种写法*/
        void method11() {
            synchronized (this) {
                //同步方法写在这里
            }
        }
        /** synchronized 同步锁   写法2 简写方式*/
        synchronized void method22() {
        }

        /**
         * ------------------------线程池
         * 创建方式1
         */
        {
            Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
            Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池
            Executors.newFixedThreadPool(10);    //创建固定容量大小的缓冲池
            Executors.newScheduledThreadPool(2);//创建一个定长线程池，支持定时及周期性任务执行
            Executors.newScheduledThreadPool(2).schedule(new Runnable() {
                @Override
                public void run() {
                    //定时为3秒
                }
            }, 3, TimeUnit.SECONDS);
        }

        {
            //使用方式
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //run方法
                }
            });
        }

        /**
         * 简写方式  直接开多线程  直接用这个就好
         */
        {
            Executors.newCachedThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    //run方法
                }
            });
        }

        //一些说明
        class PoolThread extends ThreadPoolExecutor {
            public PoolThread(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
                super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
            }
           /* corePoolSize：核心池的大小，这个参数跟后面讲述的线程池的实现原理有非常大的关系。在创建了线程池后，默认情况下，
           线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，除非调用了prestartAllCoreThreads()
           或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，
           即在没有任务到来之前就创建corePoolSize个线程或者一个线程。
           默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，
           就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；

            maximumPoolSize：线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程；

            keepAliveTime：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，
            只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，
            直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，
            如果一个线程空闲的时间达到keepAliveTime，则会终止，

            直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，
            在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；
            unit：参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：
            复制代码
            TimeUnit.DAYS;               //天
            TimeUnit.HOURS;             //小时
            TimeUnit.MINUTES;           //分钟
            TimeUnit.SECONDS;           //秒
            TimeUnit.MILLISECONDS;      //毫秒
            TimeUnit.MICROSECONDS;      //微妙
            TimeUnit.NANOSECONDS;       //纳秒


            workQueue：一个阻塞队列，用来--存储等待执行的任务--，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：
            1）ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；
　　        2）LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；
　　        3）synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。
　　        ArrayBlockingQueue和PriorityBlockingQueue使用较少，一般使用LinkedBlockingQueue和Synchronous。线程池的排队策略与BlockingQueue有关。

            threadFactory：线程工厂，主要用来创建线程；
            handler：表示当拒绝处理任务时的策略，有以下四种取值：
            ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
            ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
            ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
            ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务*/

            volatile int runState;  //一般用于单例双重锁用来保护安全性    或者  用于不同线程访问和修改的变量
            static final int RUNNING = 0;
            static final int SHUTDOWN = 1;
            static final int STOP = 2;
            static final int TERMINATED = 3;

            /*runState表示当前线程池的状态，它是一个volatile变量用来保证线程之间的可见性；
              下面的几个static final变量表示runState可能的几个取值。
              当创建线程池后，初始时，线程池处于RUNNING状态；
              如果调用了shutdown()方法，则线程池处于SHUTDOWN状态，此时线程池不能够接受新的任务，它会等待所有任务执行完毕；
              如果调用了shutdownNow()方法，则线程池处于STOP状态，此时线程池不能接受新的任务，并且会去尝试终止正在执行的任务；
              当线程池处于SHUTDOWN或STOP状态，并且所有工作线程已经销毁，任务缓存队列已经清空或执行结束后，线程池被设置为TERMINATED状态。*/

            //某些参数和方法：
            // private final BlockingQueue<Runnable> workQueue;              //任务缓存队列，用来存放等待执行的任务
            private final ReentrantLock mainLock = new ReentrantLock();   //线程池的主要状态锁，对线程池状态（比如线程池大小
            //、runState等）的改变都要使用这个锁
            // private final HashSet<Worker> workers = new HashSet<Worker>();  //用来存放工作集
            private volatile long keepAliveTime;    //线程存活时间
            private volatile boolean allowCoreThreadTimeOut;   //是否允许为核心线程设置存活时间
            private volatile int corePoolSize;     //核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
            private volatile int maximumPoolSize;   //线程池最大能容忍的线程数
            private volatile int poolSize;       //线程池中当前的线程数
            private volatile RejectedExecutionHandler handler; //任务拒绝策略
            private volatile ThreadFactory threadFactory;   //线程工厂，用来创建线程
            private int largestPoolSize;   //用来记录线程池中曾经出现过的最大线程数
            private long completedTaskCount;   //用来记录已经执行完毕的任务个数
            //关于线程池的适用是否需要手敲代码感受
        }


        //线程中断   在中断前  先睡10秒  然后中断？？？？
        {
//            Thread thread=new Thread();
//            thread.start();
//            TimeUnit.MICROSECONDS.sleep(1000);//使主线程休眠？？天才
//            thread.interrupt();
        }

//    这个锁作用 公平锁  响应中断  限时等待      https://baijiahao.baidu.com/s?id=1648624077736116382&wfr=spider&for=pc
{
        Lock mLock=new ReentrantLock();
        mLock.lock();
        try {

        }finally {
           mLock.unlock();
        }


}



    }

    /**
     * java学习篇------------关于java1.8的lambda表达式
     */
    private void lambda(){
        /**
         * 1，基本构成：
         * （参数列表）->{代码块};
         * （int a1,int a2）->{int a3 = a1+a2; return a3;};
         * （int a1,int a2）->{return a1+a2;};
         * */

         /** 2,特殊构成：
         * 当代码块只有一条语句时
         * （参数列表）-> 语句
         * （）->System.out.println("Hi");
         * （int a1,int a2）-> a1+a2; // 省略return
          */

        /**
         * 3，当将Lambda使用在接口上时：
         * 要求该接口只有一个抽象方法（函数式接口）：
         * new Thread(new Runnable(){
         *                 public void run() {
         *                           run代码块
         *                  }
         *               }).start();
         * 可简化为：
         * Runnable为函数式接口，所以可以使用Lambda表达式简化：
         *
         * new Thread(()->{run代码块}).start();
         */

        /**
         * 4
         * 总结：
         * 格式  (参数列表) ->{...代码块...};   若代码块只有一条则 {} 可以省略
         * new Thread(new Runnable(){public void run(){..run代码块..} } ).start();
         * 可以使用Lambda表达式简化：
         * new Thread(()->{..run代码块..}).start();
         * Lambda使用与接口时，接口只能有一个抽象方法
         * （只有一个抽象方法的接口：函数式接口）
         */
    }

    /**
     * java学习篇8------------反射机制Reflection
     */
    private void myReflection() {
//        http://www.cnblogs.com/lzq198754/p/5780331.html
//        反射是在运行中  对于任意一个类  都能够知道这个类的所有属性和方法 对于任意一个对象，都能调用他的任意一个方法和属性
//        为什么用反射？
//        系统里不是所有类都是开放的。
//        使用方法  只用知道包名和类名  而不用实例化。

        //测试类，用于测试反射
        class Demo {
            //反射方法测试
            void reflectionMethod() {
                //不通过new生成对象的方式
                //1.反序列化 2.克隆 3.反射

                //2.1反射的方式
                try {
                    Class<?> reflectUser = Class.forName("aaa.bbb.ccc.User");//此处包名随意的
                    User user2 = (User) reflectUser.newInstance();
                    user2.setAge(22);
                    user2.setName("小王");
                    System.out.println(user2);

                    // 2.2取得本类的全部属性  (import java.lang.reflect.Field;)(import java.lang.reflect.Modifier;)
                    Field[] field = reflectUser.getDeclaredFields();
                    for (Field f : field) {
                        System.out.println("访问权限修饰符：" + Modifier.toString(f.getModifiers()));
                        System.out.println("属性类型：" + f.getType());
                        System.out.println("变量名字：" + f.getName());
                    }
                    //通过反射调用方法：（import java.lang.reflect.Method;）
                    Method method = reflectUser.getMethod("show");
                    method.invoke(reflectUser.newInstance());
                    Method method1 = reflectUser.getMethod("show1", int.class, String.class);
                    method1.invoke(reflectUser.newInstance(), 33, "小黄");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            class User {
                private String name;
                private int age;

                public User() {
                    super();
                    // TODO Auto-generated constructor stub
                }

                public User(String name, int age) {
                    super();
                    this.name = name;
                    this.age = age;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getAge() {
                    return age;
                }

                public void setAge(int age) {
                    this.age = age;
                }

                @Override
                public String toString() {
                    return "User [name=" + name + ", age=" + age + "]";
                }

                public void show() {
                    System.out.println("这一定是一个假方法");
                }

                public void show1(int a, String b) {
                    System.out.println("这一定是一个假方法" + a + b);
                }
            }
        }
        //需要注意反射的效率和风险，以后补充
    }

    /**
     * java学习篇9------------文件类File
     */
    private void myFile() throws Exception {
//        File file = new File("D" + File.separator + "路径");//文件地址
//        File file1 = new File(new URI("wwww.wwww.wwww"));//URI也是可以的
//        /*	·  windows下。分隔符是“\”        linux下。分隔符是“/”  */
//        file.exists();//文件或目录是否存在
//        file.isFile();//是否是文件
//        file.isDirectory();//是否是目录
//        file.getName();//获取文件或目录的名字
//        file.getPath();//文件路径
//        file.getAbsolutePath();
//        file.lastModified();//获取最后的修改日期
//        file.length();//获取文件大小，字节为单位
//        String strFile[] = file.list();//如果对象是目录，则返回目录下所有文件和目录名字的列表
//        File file2[] = file.listFiles();//
//        file.mkdir();//创建File中的目录
//        file.mkdirs();//创建File中的目录，如果父目录不存在，还会创建父目录
//        file.renameTo(new File("c:adad/dada/23.txt"));//修改文件的名称，会删除原文件
//        file.createNewFile();//如果File代表文件，则创建一个空文件
//        file.delete();//删除文件或目录(如果目录下包含子目录或文件，则不能删除)
    }

    /**
     * java学习篇10------------java面向对象
     */
    class myObject {
        //定义的常量 变量
        private final int PI = 21;
        private final String TX = "腾讯";
        private String mName, mBlance, mAccount, mMoney;
        private int a, b, c;
        private ArrayList<Integer> MList;

        //    三，引用变量的赋值
        {
            String s = "hello";
            String t = s;
            s = "world";
//    对于引用数据类型  就是对象  数组  String
            if (s.equals(t)) {
                Log.e("tag", "我比较的是值");
            }
            if (s == t) {
                Log.e("tag", "我比较的是空间地址");
            }
/*
 关于equals与==的区别从以下几个方面来说：
（1） 如果是基本类型比较，那么只能用==来比较，不能用equals
（2） 对于基本类型的包装类型，比如Boolean、Character、Byte、Shot、Integer、Long、Float、Double等的引用变量，
     ==是比较地址的，而equals是比较内容的。
（3） 注意：对于String(字符串)、
     StringBuffer(线程安全的可变字符序列)、
     StringBuilder(可变字符序列)这三个类作进一步的说明。
     */
        }

        ;//默认的构造方法

        //构造（没有返回值）
        public myObject() {
        }
        //单参数的构造方法

        public myObject(String a) {
        }
        //多参数的构造方法

        public myObject(int b, double c) {
        }

        //自动生成的构造方法，一般用于对象数组
        public myObject(String mName, String mBlance, String mAccount, String mMoney, int a, int b, int c, ArrayList<Integer> MList) {
            this.mName = mName;
            this.mBlance = mBlance;
            this.mAccount = mAccount;
            this.mMoney = mMoney;
            this.a = a;
            this.b = b;
            this.c = c;
            this.MList = MList;
        }

        ;

        //普通方法，非构造方法
        public void JavaThree(int a) {
        }


/*面向对象的三大特征：封装 继承  多态  还有六大原则 自己看
---------封装：
讲一个类的属性私有化，同时对外提供公共的访问方法。封装的作用：为了保护数据安全。
---------继承：
就是以现有的类为模板，扩展新功能，创建出新的类的过程。其中，模板类被称为父类或者基类，扩展的类被称为子类或者派生类
              子类会完全的继承父类中的所有非私有的数据成员，而且子类无法选择性的继承父类。
              Java中的继承是单继承，也就说一个子类只能直接继承自一个父类。
              继承只能继承属性  不能继承值。
----------多态：
什么是多态？
答：编译时数据类型和运行时的数据类型不一致，就会发生多态。父类对象的位置可以用一个子类对象来代替，
实际运行时，活动的就是这一个子类对象大前提 ：必须有继承或者实现，通常还有一个前提 存在复写
      多态也叫里式替换原则：子类对象完美替换父类对象并且对程序本身不造成任何伤害

      多态：对外一个接口，内部多种实现分为编译时多态和运行时多态
             编译时多态：分为重写和重载
             运行时多态：向上转型和向下转型

              向上转型：Animal   animal  =new  Dog();
              调用的是子类复写的父类方法 比如父类是叫（叫）  子类是叫（吠叫） 调用的结果就是吠叫   不能调用子类的（但是子类复写，相当于扩展了方法）
              适用场景：当不需要面对子类类型时，通过提高扩展性，或者使用父类的功能就能完成相应的操作。

              向下转型：Dog   dog  = (Dog)animal;（必须经过向上转型，拿到Animal的实例）
              调用的是子类所有和父类非私有，也就是子类继承的  说到底还是子类的（这样来说，用向上转型的作用似乎更大）  下面有示例
              适用场景：当要使用子类特有功能时。 而又不能直接拿到子类实例   或者子类开销太大，个人理解。

              Dog   dog  =new Animal();
              此转型有待参考

              ===================多态例子=================》
              public class BtClass {
	    public static void main(String[] args) {

	    	Father father=new Son();//向上转型

	    	System.out.println(father.eat);  	        //父亲的属性：吃饭                              （属性不能继承  有些人胡说八道 ）
	    	father.eatMethod();                        //儿子继承父亲的吃饭的方法：吃肉      （发生多态了，因为儿子重写了这个方法）
	    	father.drinkMethod();                     //父亲私有的方法：喝酒                           （儿子没有重写这个方法，所以还是父亲的喝酒）
	    	//father.play();  这个儿子自己的方法调用不出来的

	    	Son son=(Son) father;//向下转型  (必须经过向上转型)

	    	System.out.println(son.eat);               //儿子的属性：吃肉                                    （并没有继承到父亲的属性，不过可以用super.eat）
	    	son.drinkMethod();                         //父亲的方法：喝酒                                      (没有私有（没有藏起来）所以儿子能拿到，private 修饰之后就拿不到了)
	    	son.eatMethod();                           //儿子继承父亲的吃饭的方法：吃肉
	    	son.playMethod();                          //儿子私有的方法：玩游戏
	    }
}

class Father{
	String eat="父亲的属性：吃饭";

 void eatMethod(){
		System.out.println("父亲吃饭的方法：吃饭");
	}

   void drinkMethod(){
		System.out.println("父亲私有的方法：喝酒");
	}

}

  class Son extends Father{
	String eat="儿子的属性：吃肉";

	@Override
	void eatMethod() {

		// 重写父类的吃的方法
		System.out.println("儿子继承父亲的吃饭的方法：吃肉");
	}

	//儿子自己的方法
	 void playMethod() {
		System.out.println("儿子私有的方法：玩游戏");
	}

}
              《===================多态例子=================


public class demo04 {
    public static void main(String[] args) {
        People p=new Stu();
        p.eat(); //吃水煮肉片
        //调用特有的方法
        Stu s=(Stu)p;
        s.study(); //好好学习
        //((Stu) p).study();
    }
}
class People{
    public void eat(){
        System.out.println("吃饭");
    }
}
class Stu extends People{
    @Override
    public void eat(){
        System.out.println("吃水煮肉片");
    }
    public void study(){
        System.out.println("好好学习");
    }
}
class Teachers extends People{
    @Override
    public void eat(){
        System.out.println("吃樱桃");
    }
    public void teach(){
        System.out.println("认真授课");
    }
}
答案：
吃水煮肉片
好好学习



例题：动物有叫()    狗继承动物  吠叫()方法    鸡继承动物  鸣叫()方法
public class demo1 {
    public static void main(String[] args) {
        A a=new A();
        a.show();  //A
        B b=new B();
        b.show();  //B没有show方法 于是调用到父类的show方法，然后调用show2()方法， B发现自己有了，于是用自己的show2()方法。这点要注意.
    }
}
class A{
    public void show(){show2();}
    public void show2(){System.out.println("A");}
}
class B extends A{
    public void show2(){System.out.println("B");}
}
class C extends B{
    public void show(){super.show();}
    public void show2(){System.out.println("C");}
    }

答案：A B



              */

//    二，关于修饰符的访问权限
//    类型            无修饰符   private    protected    public
//    同类              是        是           是         是
//    同一包中的子类      是                    是          是
//    同一包中的非子类    是                    是          是
//    不同包中的子类                           是           是
//    不同包中的非子类                                      是

        //方法的重载  (方法名相同，参数列表不同)
        public void JavaThree(String a, int b) {
        }

        //    父类：Throwable     子类：异常 Exception     子类：错误 Error
        public void method() throws Throwable {
            int a = 1 + 2;
            try {
                /*有可能出异常的语句*/
            } catch (NullPointerException e) {
                e.getMessage();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.toString();
            } catch (Exception e) {
                e.toString();
                throw e;//抛出到头方法
            } finally {
                a = 56;
            }

        }
    }

    /**
     * java学习篇11------------集合List
     */
    private class myList {
        /*集合继承关系*/
//Collection 接口的接口 对象的集合（单列集合）
//├——-List 接口：元素按进入先后有序保存，可重复
//│—————-├ LinkedList 接口实现类， 链表， 插入删除， 没有同步， 线程不安全，查询慢，增删快
//│—————-├ ArrayList（常用） 接口实现类， 数组， 随机访问， 没有同步， 线程不安全，查询快，增删慢
//│—————-└ Vector 接口实现类 数组， 同步， 线程安全,  性能较低，查询快，增删慢
//│ ———————-└ Stack 是Vector类的实现类
//└——-Set 接口： 仅接收一次，不可重复，并做内部排序 所有的重复内容是靠hashCode()和equals()两个方法区分的     不重复 自动排序
//├—————-└HashSet（常用）使用hash表（数组）存储元素 散列存放 无序
//│————————└ LinkedHashSet 链表维护元素的插入次序
//└ —————-TreeSet（常用） 底层实现为二叉树，元素排好序 （使用TreeSet则对象所在的类必须实现Compable接口，然后再Arrays.sort()）
//
//Map 接口 键值对的集合 （双列集合）
//├———Hashtable 接口实现类， 同步， 线程安全  速度慢 key不允许重复
//├———HashMap （常用）接口实现类 ，没有同步， 线程不安全  快
//│—————–├ LinkedHashMap 双向链表和哈希表实现
//│—————–└ WeakHashMap
//├ ——–TreeMap （常用）红黑树对所有的key进行排序 key不允许重复
//└———IdentifyHashMap


        /*迭代器 用不上 了解下就可以了*/ {
            //Iterator接口的操作原理：Iterator是专门的迭代输出接口，
            //使用迭代器的时候  就不能用自己的删除方法
            List<String> all = new ArrayList<>();
            all.add("hello");
            // Iterator接口
            Iterator<String> iter = all.iterator();
            while (iter.hasNext()) {
                iter.next();//取出当前元素
                iter.remove();//移除
            }
            // ListIterator接口
            ListIterator<String> iter1 = all.listIterator();
            while (iter1.hasNext()) {  //判断是否有下一个元素 由前向后输出
                iter1.set("");// 替换元素
            }
            while (iter1.hasPrevious()) {  // 判断是否有上一个元素  由前向后输出
                iter1.previous();        // 取出内容
                iter1.add("");
                iter1.nextIndex();//返回下一个元素的索引号
                iter1.previousIndex();//返回上一个元素的索引号
            }
        }

        /*foreach的操作*/ {
            List<String> all = new ArrayList<String>();    // 实例化List接口
            all.add("hello");                // 增加元素
            for (String str : all) {                // 输出foreach输出
                //做操作
            }
        }
        //和其他语言使用ASCII码字符集不同，java使用Unicode字符集来表示字符串和字符。
        // ASCII使用（8bit）表示一个字节，可以认为一个字符就是一个字节（byte）
        // Unicode用两个字节（16bit），表示一个字符。一个字符就是两个字节（byte）了。
        // Queue：队列接口:继承了List接口和Queue接口
        //         方法	            类型	描述
        // public E element()	普通	找到链表的表头
        // public boolean offer(E o)	普通	将指定元素增加到链表的结尾
        // public E peek()	普通	找到但并不删除链表的头
        // public E poll()	普通	找到并删除此链表的头
        // public E remove ()	普通	检索并移除表头
        // SortedSet接口：可以对集合中的数据进行排序。

        /*map键值对集合*/ {
            // public void clear()	普通	清空Map集合
            // public boolean containsKey(Object key)	普通	判断指定的key是否存在
            // public boolean containsValue(Object value)	普通	判断指定的value是否存在
            // public Set<Map.Entry<K,V>> entrySet()	普通	将Map对象变为Set集合
            // public boolean equals(Object o)	普通	对象比较
            // public V get(Object key)	普通	根据key取得value
            // public int hashCode()	普通	返回哈希码
            // public boolean isEmpty()	普通	判断集合是否为空
            // public Set<K> keySet()	普通	取得所有的key
            // public V put(K key, V value)	普通	向集合中加入元素
            // public void putAll(Map<? extends K,? extends V> t)	普通	将一个Map集合中的内容加入到另一个Map
            // public V remove(Object key)	普通	根据key删除value
            // public int size()	普通	取出集合的长度
            // public Collection<V> values()	普通	取出全部的value
            /*虽然存入的时候是KEY  和 VALUE 两个对象，但是最后还是以Map.Entry对象保存在集合中的*/

            //Map接口的常用子类：
            // HashMap：无序存放的，是新的操作类，key不允许重复。不安全，异步操作，速度快，key和value可以设置为null
            Map<String, String> map = new HashMap<String, String>();    // key和value是String
            // MAP增加元素：
            map.put("mldn", "www.mldn.cn");//放入数据
            map.get("mldn");    // 根据key求出value
            map.containsKey("mldn");       // 查找指定的key是否存在
            map.containsValue("www.mldn.cn");   // 查找指定的value是否存在
            Set<String> keys = map.keySet(); // 得到全部的key
            Iterator<String> iter = keys.iterator(); // 实例化Iterator
            System.out.print("全部的key：");            // 输出信息
            while (iter.hasNext()) {                // 迭代输出全部的key
                String str = iter.next();            // 取出集合的key
            }
            //把Value变成Collection集合在输出：
            Collection<String> values = map.values();
            Iterator<String> iter2 = values.iterator();        // 实例化Iterator
            System.out.print("全部的value：");            // 输出信息
            while (iter2.hasNext()) {                // 迭代输出
                String str = iter2.next();            // 取出value
                System.out.print(str + "、");        // 输出内容
            }
            //Hashtable：无序存放的，是旧的操作类，key不允许重复。（过时）。安全，同步，速度慢，不可以设置为空
            // TreeMap：可以排序的Map集合，按集合中的key排序，key不允许重复。
            // TreeMap根据Key来对元素进行排序
            // 如果Key是对象那么必须实现 Comparable接口
            {
                Map<String, String> map1 = null;
                map = new TreeMap<String, String>();        // 实例化Map对象
                map.put("A、mldn", "www.mldn.cn");            // 增加内容
                Set<String> keys1 = map1.keySet();            // 得到全部的key
                Iterator<String> iter1 = keys.iterator();        // 实例化Iterator
                while (iter1.hasNext()) {                // 迭代输出
                    String str = iter1.next();            // 取出key
                    System.out.println(str + " --> " + map.get(str));    // 取出key对应的内容
                }
            }
        }

        /*迭代器输出map的方法  不常用迭代吧 个人意见 */ {
            Map<String, String> map = null;        // 声明Map对象，指定泛型类型
            map = new HashMap<String, String>();        // 实例化Map对象
            map.put("mldn", "www.mldn.cn");                        // 增加内容
            map.put("zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
            map.put("mldnjava", "www.mldnjava.cn");        // 增加内容
            Set<Map.Entry<String, String>> allSet = null;    // 声明一个Set集合，指定泛型
            allSet = map.entrySet();            // 将Map接口实例变为Set接口实例
            Iterator<Map.Entry<String, String>> iter = null;    // 声明Iterator对象
            iter = allSet.iterator();            // 实例化Iterator对象
            while (iter.hasNext()) {
                Map.Entry<String, String> me = iter.next();// 找到Map.Entry实例
                System.out.println(me.getKey()
                        + " --> " + me.getValue());    // 输出key和value
            }
            //foreach 输出集合
            Map<String, String> map2 = null;        // 声明Map对象，指定泛型类型
            map2 = new HashMap<String, String>();    // 实例化Map对象
            map2.put("mldn", "www.mldn.cn");        // 增加内容
            map2.put("zhinangtuan", "www.zhinangtuan.net.cn");    // 增加内容
            map2.put("mldnjava", "www.mldnjava.cn");        // 增加内容
            for (Map.Entry<String, String> me : map2.entrySet()) {
                System.out.println(me.getKey() + " --> " + me.getValue());// 输出key和value
            }
        }

        /*Stack类是Vector的子类*/ {
            // public boolean empty()	常量	测试栈是否为空
            // public E peek()	常量	查看栈定，但不删除
            // public E pop()	常量	出栈，同时删除
            // public E push(E item)	普通	入栈
            // public int search(Object o)	普通	在栈中查找
            Stack<String> s = new Stack<>();    // 实例化Stack对象
            s.push("A");                // 入栈
            s.push("B");                // 入栈
            s.push("C");                // 入栈
            System.out.print(s.pop() + "、");        // 出栈
            System.out.print(s.pop() + "、");        // 出栈
            System.out.println(s.pop() + "、");    // 出栈
            System.out.print(s.pop() + "、");        // 错误，出栈，出现异常，栈为空
        }

        void mVector() {
            //向量  Vector类
            //1.可以是不同的对象，不能是基本数据类型 如int
            //2.变长的空间
            //初始为100   超出之后50递增
            Vector<Object> mVetor = new Vector(100, 50);
            mVetor.addElement(123);//添加到尾部
            mVetor.insertElementAt("你好", 34);//指定角标插入
            mVetor.add(12, 34);//指定位置添加
            mVetor.add(12);//
            mVetor.setElementAt("787", 12);//指定位置修改
            mVetor.removeElementAt(21);//移除角标处
            mVetor.removeAllElements();//清空，长度为0
            mVetor.capacity();//容量 ，没有length方法
        }

        void mTreeSet() {
            class Person implements Comparable<Person> {    // 定义Person类，实现比较器
                private String name;            // 定义name属性
                private int age;            // 定义age属性

                public Person(String name, int age) {    // 通过构造方法为属性赋值
                    this.name = name;            // 为name属性赋值
                    this.age = age;            // 为age属性赋值
                }

                // 覆写toString()方法
                public String toString() {
                    return "姓名：" + this.name + "；年龄：" + this.age;
                }

                // 覆写compareTo()方法，指定排序规则 通过年纪排序
                public int compareTo(Person per) {
                    if (this.age > per.age) {
                        return 1;
                    } else if (this.age < per.age) {
                        return -1;
                    } else {
                        return 0;
                    }
                }

                //复写方法
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (!(obj instanceof Person)) {
                        return false;
                    }
                    Person per = (Person) obj;
                    if (per.name.equals(this.name) && per.age == this.age) {
                        return true;
                    } else {
                        return false;
                    }
                }

                //复写方法
                public int hashCode() {
                    return this.name.hashCode() * this.age;
                }
            }
        }
    }

    /**
     * java学习篇12------------Lambda表达式
     */
    private static class myLambda {
        //        Lambda简介#
//        Lambda 表达式是 JDK8 的一个新特性，可以取代大部分的匿名内部类，写出更优雅的 Java 代码，尤其在集合的遍历和其他集合操作中，可以极大地优化代码结构。
//        JDK 也提供了大量的内置函数式接口供我们使用，使得 Lambda 表达式的运用更加方便、高效。
//        对接口的要求#
//        虽然使用 Lambda 表达式可以对某些接口进行简单的实现，但并不是所有的接口都可以使用 Lambda 表达式来实现。
//        Lambda 规定接口中只能有一个需要被实现的方法，不是规定接口中只能有一个方法
//        jdk 8 中有另一个新特性：default， 被 default 修饰的方法会有默认实现，不是必须被实现的方法，所以不影响 Lambda 表达式的使用。
//        @FunctionalInterface#
//        修饰函数式接口的，要求接口中的抽象方法只有一个。 这个注解往往会和 lambda 表达式一起出现。
//        Lambda 基础语法#
//        我们这里给出六个接口，后文的全部操作都利用这六个接口来进行阐述。
//        语法形式为 () -> {}，其中 () 用来描述参数列表，{} 用来描述方法体，-> 为 lambda运算符 ，读作(goes to)。
// 先定义两个接口
        interface LambdaInter1 {
            void method();
        }

        interface LambdaInter2 {
            void method(int a, String sss);
        }

        //        这里是测试方法
        {
            LambdaInter1 lambdaInter1 = () -> {
                /*Tools.showLog("212");*/
            };
            lambdaInter1.method();

            LambdaInter2 lambdaInter2 = (int a, String b) -> {
                /*Tools.showLog("2121");*/
            };
            lambdaInter2.method(12, "1221");

            //线程简写式runnable
            Runnable runnable = () -> {
                //run方法写在这里
            };
            runnable.run();

            //有待加深理解
        }

    }

    /**
     * java学习篇13------------IntStream无限流
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private class myNewJava {
        //volatile申明一个共享数据（变量）
        volatile int asa = 90;

        //Intstream无限流
        {
            IntStream.range(1, 5).forEach(a -> System.out.println(a+""));
            /**
             * 输出结果: IntStream.range(1, 5)    【1,5)半开区间   ||    IntStream.rangeClosed(1, 5)    【1,5】闭区间
             * 1 2 3 4
             */
        }

        //持续生成随机值
        {
            Random random = new Random();
            IntStream.generate(() -> random.nextInt(10)).forEach(value -> {
                System.out.println(value);
            });

            /**
             * 输出结果：
             * 1 2 2 8 9 5 7 3 8 4 。。。。。。。。。
             */
        }

        {
            IntStream.of(9, 90, 876, 12).forEach(v -> System.out.print(v+" "));
            /**
             * 输出结果：
             * 9 90 876 12
             */
        }

        //空流
        {
            int sum = IntStream.empty().sum();
            System.out.println(sum);
            System.out.println("---完美的分割线---");
            IntStream.empty().forEach(v -> System.out.print(v+" "));
            /**
             * 输出结果：
             * 0
             * ---完美的分割线---
             */
        }

        //构造器模式
        {
            IntStream.builder().add(90).add(87).build().forEach(v -> System.out.print(v+" "));
            /**
             * 输出结果：
             * 90 87
             */
        }

        {
            // 第一个参数seed：初始值
            // 第二个参数IntUnaryOperator：用于根据当前元素生成下一个元素的类
            // 依次*2，生成下一个元素
            IntStream iterate = IntStream.iterate(1, operand -> operand * 2);
            // 限制10条，默认无限输出
            IntStream limit = iterate.limit(10);
            limit.forEach(v -> System.out.println(v+"-"));

            /**
             * 输出结果：
             *  1-2-4-8-16-32-64-128-256-512-
             */

        }

        {
            IntStream range = IntStream.range(1, 5);
            IntStream range1 = IntStream.rangeClosed(90, 95);
            IntStream concat = IntStream.concat(range, range1);//合并
            concat.forEach(v -> System.out.println(v+"-"));



            /**
             * 输出结果：
             *  1 2 3 4 90 91 92 93 94 95
             */
        }

        {
            // 过滤奇数
            IntStream.rangeClosed(1, 10).filter(value -> value % 2 == 0).forEach(v -> System.out.print(v+" "));

            /**
             * 输出结果：
             * 2 4 6 8 10
             */

        }

        //map操作用于操作当前所有的元素，根据给定的操作生成新值，并用新值替代旧值，比如每个值都扩大2倍，如下代码和输出：
        //通过mapObj可以将元素转成其他类型，具体转换类型通过实现类确定
        {
            Stream<Object> stringStream = IntStream.rangeClosed(1, 5).mapToObj(value -> String.valueOf(value));
            stringStream.forEach(v -> System.out.println(v + "->" + v.getClass()));
        }

        {
            IntStream originStream = IntStream.rangeClosed(1, 10);//1到10的闭环
            IntStream intStream = originStream.map(v -> v * 2);//用新值 替换旧值
            intStream.forEach(v -> System.out.print(v+" "));

        }
        //1->class java.lang.String
        //2->class java.lang.String
        //3->class java.lang.String
        //4->class java.lang.String
        //5->class java.lang.String

        {
            // 转换为*2的long值
            LongStream longStream = IntStream.rangeClosed(1, 5).mapToLong(v -> v * 2);
            longStream.forEach(v1 -> System.out.println(v1 + " -> " + ((Object)v1).getClass()));
        }
        //2 -> class java.lang.Long
        //4 -> class java.lang.Long
        //6 -> class java.lang.Long
        //8 -> class java.lang.Long
        //10 -> class java.lang.Long

        {//有点意思
            // 这里根据上游的元素扩展出了更多的元素
            // 操作步骤如下：
            // 1：获取值1，执行IntStream.rangeClosed(0, 1)，生成0,1的IntStream
            // 2：获取值2，执行IntStream.rangeClosed(0, 2)，生成0,1,2的IntStream
            // 3：获取值3，执行IntStream.rangeClosed(0 3)，生成0,1,2,3的IntStream
            // 4：合并以上的3个IntStream，生成0,1,0,1,2,0,1,2,3的IntStream为最终结果
            IntStream.of(1, 2, 3).flatMap(e -> IntStream.rangeClosed(0, e)).forEach(System.out::println);
        }
        //0
        //1
        //0
        //1
        //2
        //0
        //1
        //2
        //3
        {
            IntStream.of(1, 2, 3, 3, 4, 2).distinct().forEach(System.out::println);//去重
        }

        {
            IntStream.of(9, 80, 34, 2, 24).sorted().forEach(System.out::println);//排序
        }

        {//该操作可以在获取每个元素后执行给定的操作
            IntStream.of(1, 2, 3, 4, 5)
//         .filter(e -> e >= 3)
                    .peek(value -> System.out.printf("filter element: %d\n", value))
                    .mapToObj(String::valueOf)
                    .forEach(System.out::println);
        }
        //filter element: 1
        //1
        //filter element: 2
        //2
        //filter element: 3
        //3
        //filter element: 4
        //4
        //filter element: 5
        //5

        {//跳过6个数
            IntStream.rangeClosed(1, 9).skip(6).forEach(System.out::println);
        }
        //7
        //8
        //9

        {//并行处理parallel（平行，并行，并联）保证按照原始顺序注意不是排序输出
            System.out.println("parallel 后顺序打乱");
            IntStream.of(1,5,-9,0,-5,2,5,8).parallel().forEach(System.out::println);
            System.out.println("parallel 后顺序保持不变");
            // 在并行遍历时，forEachOrdered将顺序遍历元素
            IntStream.of(1,5,-9,0,-5,2,5,8).parallel().forEachOrdered(System.out::println);
        }
        //parallel 后顺序打乱
        //2
        //-5
        //8
        //5
        //1
        //5
        //-9
        //0
        //parallel 后顺序保持不变
        //1
        //5
        //-9
        //0
        //-5
        //2
        //5
        //8

        {//按照给定规则依次处理每个元素，最终获取一个结果 reduce(减少，降低，简化)
            // 规约操作一定有值
            int sum = IntStream.range(0, 10).reduce(0, (v1, v2) -> v1 + v2);
            System.out.println("sum is: " + sum);
            // 相当于如下代码
            int result = 0;
            for (int i = 0; i < 10; i++) {
                result = result + i;
            }
            System.out.println("result is: " + result);
        }
        //sum is: 45
        //result is: 45

        {//获取最小值
            int[] arr = new int[] { 8, 90, 87, 43, 900, 12 };
//	        int minVal = Integer.MAX_VALUE;
            // 获取最小值，执行过程如下：
            // Integer.min(left, right) -> Integer.min(8, 90) = 8
            // Integer.min(left, right) -> Integer.min(8, 7) = 7
            // Integer.min(left, right) -> Integer.min(7, 43) = 7
            // Integer.min(left, right) -> Integer.min(7, 5) = 5
            // Integer.min(left, right) -> Integer.min(5, 12) = 5 最终结果
            int minVal = IntStream.of(8, 90, 7, 43, 5, 12).reduce((left, right) -> {
                System.out.printf("left: %d, right: %d", left, right);
                System.out.println();
                return Integer.min(left, right);
            }).getAsInt();
            System.out.println("minVal: " + minVal);
        }
        //left: 8, right: 90
        //left: 8, right: 7
        //left: 7, right: 43
        //left: 7, right: 5
        //left: 5, right: 12
        //minVal: 5

        {//装箱操作  boxed用于将流中的元素转换为对应的对象类型
            IntStream.of(2, 7).boxed().forEach(v -> System.out.println(v + " -> " + v.getClass()));
        }

        //2 -> class java.lang.Integer
        //7 -> class java.lang.Integer

        {
            System.out.println("最大值：");
            System.out.println(IntStream.of(3, 90, 988, 1, 76).max().getAsInt());
            System.out.println("最小值：");
            System.out.println(IntStream.of(3, 90, 988, 1, 76).min().getAsInt());
            System.out.println("平均值：");
            System.out.println(IntStream.of(3, 90, 988, 1, 76).average().getAsDouble());
            System.out.println("元素个数：");
            System.out.println(IntStream.of(3, 90, 988, 1, 76).count());
            System.out.println("元素总和：");
            System.out.println(IntStream.of(3, 90, 988, 1, 76).sum());
            System.out.println("使用summaryStatistics进行数学运算：");
            IntSummaryStatistics summaryStatistics = IntStream.of(-2, 2, -9, 10, 9).summaryStatistics();
//        Assert.assertEquals(10, summaryStatistics.getSum());
            System.out.println("总和：" + summaryStatistics.getSum());
            System.out.println("最大值：" + summaryStatistics.getMax());
            System.out.println("最小值：" + summaryStatistics.getMin());
            System.out.println("元素个数：" + summaryStatistics.getCount());
            System.out.println("平均值：" + summaryStatistics.getAverage());

        }
        //最大值：
        //988
        //最小值：
        //1
        //平均值：
        //231.6
        //元素个数：
        //5
        //元素总和：
        //1158
        //使用summaryStatistics进行数学运算：
        //总和：10
        //最大值：10
        //最小值：-9
        //元素个数：5
        //平均值：2.0

        {//判断元素是否满足给定要求
            boolean anyMatch = IntStream.of(-2, 2, -9, 10, 9).anyMatch(e -> e > 0);
//        Assert.assertTrue(result);
            System.out.println("anyMatch: " + anyMatch);
            boolean allMatch = IntStream.of(5, 5, 5, 5, 5).allMatch(e -> e > 0);
            System.out.println("allMatch: " + allMatch);
            boolean noneMatch = IntStream.of(4, 5, 5, 5).noneMatch(e -> e == 4);
            System.out.println("noneMatch: " + noneMatch);
        }
        //anyMatch: true
        //allMatch: true
        //noneMatch: false

        {//查询操作
            int findFirst = IntStream.of(4, 5, 5, 5).findFirst().getAsInt();
            System.out.println("findFirst: " + findFirst);
            int findAny = IntStream.of(42, 25, 52, 54).findAny().getAsInt();
            System.out.println("findAny: " + findAny);
        }
        //findFirst: 4
        //findAny: 42

    }

    /**
     * java学习篇14------------网络编程Socket----http
     */
    private class myNet {
        //一切皆为字节（文本、图片、视频），即皆为二进制数据。
        //简单说明一下，最基本的：利用socket技术  客户端传递一个数据  到服务器端打印出来
        // 服务器端
        private void serverMethod() throws Exception {
            ServerSocket serversocket = new ServerSocket(7000);// 创建一个服务器端的socket
            Socket socket = serversocket.accept();// 设置监听 并用socket接收  (接受请求)
            // 1.socket的输入流 加入缓冲
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            // 2.socket的输出流 加入缓冲
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // 然后开始读取接受到的 socket信息
            dos.writeDouble(dis.readDouble());
            dos.flush();
            socket.close();// 关闭socket
            serversocket.close();// 关闭服务器端socket
        }

        // 客户端
        private void clientSocket() throws Exception {
            //建立socket ip+端口号
            Socket socket = new Socket("localhost", 7000);
            // 1.socket的输入流 加入缓冲
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            // socket的输出流（这里就用简写了）
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            dos.writeDouble(3000);
            dos.flush();

            System.out.println("服务器返回的计算面积为:" + dis.readDouble());
            dos.writeInt(0);
            dos.flush();

            socket.close();
        }

        //线程池服务器端
        public void SocketServerM() throws Exception {
            int clientNo = 1;//客户端1号 用于标记是第几个客户端
            ServerSocket serverSocket = new ServerSocket(7000);
            ExecutorService exec = Executors.newCachedThreadPool();//创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
            try {
                while (true) {//不停的接受 某些客户端的消息
                    Socket socket = serverSocket.accept();
                    exec.execute(new ServerRunnable(socket, clientNo));//线程池启动 开始放入任务
                    clientNo++;
                }
            } finally {
                serverSocket.close();
            }
        }

        //线程池服务器端的任务 runnable
        class ServerRunnable implements Runnable {
            private Socket socket;
            private int clientNo;//标记是第几个客户端

            public ServerRunnable(Socket socket, int clientNo) {
                this.socket = socket;
                this.clientNo = clientNo;
            }

            @Override
            public void run() {
                try {
                    DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    do {
                        dos.writeDouble(dis.readDouble());
                        dos.flush();
                    } while (dis.readInt() != 0);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("与客户端" + clientNo + "通信结束");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //// 读
        //try {
        //	Socket chatSocket = new Socket("127.0.0.1", 5000);		// 127.0.0.1代表本机
        //	InputStreamReader stream = new InputStreamReader(chatSocket.getInputStream());
        //	BufferedReader reader = new BufferedReader(stream);
        //	String msg = reader.readLine();
        //	System.out.println(msg);
        //	reader.close();		// 不要忘记关闭 >_<
        //} catch(Exception e) {
        //	e.printStackTrace();
        //}
        //
        //// 写
        //try {
        //	Socket chatSocket = new Socket("127.0.0.1", 5000);
        //	PrintWriter writer = new PrintWriter(chatSocket.getOutputStream());
        //	writer.println("Loli suki");
        //	writer.println("FBI!OPen the door!");
        //	writer.close();
        //} catch(Exception e) {
        //	e.printStackTrace();
        //}



        URL url;

        {
            try {
                url = new URL("wwww.dada.com");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestMethod("POST");
                /*使用Http的Range头字段指定每条线程从文件的什么位置开始下载，下载到什么位置：
                如：指定从文件的2M位置开始下载，下载的位置(4M-1byte)为止，代码——*/
                connection.setRequestProperty("Range", "bytes=2097152-4194303");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * java学习篇15------------异步操作----AsyncTask
     */
    private abstract class MyAcynvTask extends AsyncTask {

       /**
        *
        * 类中参数为3种泛型类型
 整体作用：控制AsyncTask子类执行线程任务时各个阶段的返回类型
 具体说明：
         a. Params：开始异步任务执行时传入的参数类型，对应excute（）中传递的参数
         b. Progress：异步任务执行过程中，返回下载进度值的类型
         c. Result：异步任务执行完成后，返回的结果类型，与doInBackground()的返回值类型保持一致
 注：
         a. 使用时并不是所有类型都被使用
         b. 若无被使用，可用java.lang.Void类型代替
         c. 若有不同业务，需额外再写1个AsyncTask的子类*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //主线程工作  一般做一些UI标记
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            //线程池中执行   onPreExecute之后  耗时操作  调用publishProgress来更新进度信息
            // 可调用publishProgress（）显示进度, 之后将执行onProgressUpdate（）
            publishProgress(this);//???
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            //主线程执行   调用publishProgress时来将进度更新到 UI上
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //主线程  收尾工作  更新UI
        }

        {
            MyAcynvTask  myAcynvTask=new MyAcynvTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    return super.doInBackground(objects);
                }
            };
            //
            myAcynvTask.execute();
            myAcynvTask.publishProgress();
        }


        /**
         * 博客地址:  https://www.jianshu.com/p/ead95c2cdb86
         * 网上的一个简单的用异步下载的方法
         */
        public class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
            @Override
            protected void onPreExecute() {
//                progressDialog.show();
            }
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    while (true) {
//                        int downloadPercent = doDownload();
//                        publishProgress(downloadPercent);
//                        if (downloadPercent >= 100) {
//                            break;
//                        }
                    }
                } catch (Exception e) {
                    return false;
                }
//                return true;
            }
            @Override
            protected void onProgressUpdate(Integer... values) {
//                progressDialog.setMessage("当前下载进度： " + values[0] + "%");
            }
            @Override
            protected void onPostExecute(Boolean result) {
//                progressDialog.dismiss();
                if (result) {
//                    Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    /**
     * java学习篇17-----------图片下载的demo
     */
    private class GetImg{
        /**
         * 主方法
         * @param args
         * @throws Exception
         */
        public  void main(String[] args) throws Exception {
            downloadFile("https://xinzhuobu.com/wp-content/uploads/2022/06/20220613001.jpg","C:\\Users\\wq\\Desktop\\img");
        }


        /**
         * 简易版图片下载demo
         * @return
         * @throws Exception
         */
        public  void  downloadFile(String internetFileUrl, String fileSaveAddress) throws Exception {
            /**
             * 1.先创建文件夹
             */
            File file = new File(fileSaveAddress);//保存文件的路径 +判断文件夹是否存在
            if (!file.exists()) {
                file.mkdir();//如果不存在就创建一个文件夹
            }

            /**
             * 2.创建连接
             */
            HttpURLConnection httpUrl = (HttpURLConnection) new URL(internetFileUrl).openConnection();
            httpUrl.connect();//建立连接
            BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());//将网络连接装入缓冲流

            /**
             * 3.用于截取文件名  然后拼接文件地址
             * [https:, , xinzhuobu.com, wp-content, uploads, 2022, 06, 20220613002.jpg]
             *  C:\Users\86182\Desktop\img/20220613002.jpg
             */
            String urlSplitArray[] = internetFileUrl.split("/");
            FileOutputStream fos = new FileOutputStream(fileSaveAddress + "/" + urlSplitArray[urlSplitArray.length - 1]);

            /**
             * 4.创建1024的缓冲数组，然后开始读取网络文件数据
             */
            byte[] bufArray = new byte[1024];//创建缓冲数组
            int size = 0;

            while ((size = bis.read(bufArray)) != -1) {
                fos.write(bufArray, 0, size);
            }
            // 记得及时释放资源
            fos.close();
            bis.close();
            httpUrl.disconnect();//断开连接

            System.out.print("work is over.");
        }
    }

    /**
     *  java学习篇18-----------Arrays
     */
    private class ArraysTemp{
        /**
         //int型数组
         int[] arr=new int[]{1,2,3,4,5,8,6,7};
         //1-ToString()-对数组进行遍历,返回[]包裹起来的,逗号分割字符串表示
         System.out.println(Arrays.toString(arr));//[1, 2, 3, 4, 5, 8, 6, 7]
         //2-sort()-数组排序
         Arrays.sort(arr);
         System.out.println(Arrays.toString(arr));//[1, 2, 3, 4, 5, 6, 7, 8]
         //3-binarySearch()-二分法查找[有序数组]-返回对应元素的索引值
         System.out.println(Arrays.binarySearch(arr, 6));//5
         //4-copyOf(数组名,新长度)-数组元素复制[按指定长度复制]
         int[] ints = Arrays.copyOf(arr, 5);
         System.out.println(Arrays.toString(ints));//[1, 2, 3, 4, 5]
         //5-copyOfRange(数组名,起始位置,终止位置)-数组元素复制[指定元素区间]
         int[] ints1 = Arrays.copyOfRange(arr, 2, 8);
         System.out.println(Arrays.toString(ints1));//[3, 4, 5, 6, 7, 8]
         //6-equals(数组1,数组2)-逐一比较两个数组元素的值是否相同
         System.out.println(Arrays.equals(arr, ints));//false
         //7-fill(数组名,元素值)数组元素填充
         Arrays.fill(arr,8);
         System.out.println(Arrays.toString(arr));//[8, 8, 8, 8, 8, 8, 8, 8]
         */

        /**
         int[] arr1 = new int[] { 3, 4, 25, 16, 30, 18 };
         // 对数组arr1进行并发排序
         Arrays.parallelSort(arr1);
         System.out.println(Arrays.toString(arr1));
         int[] arr2 = new int[] { 13, -4, 25, 16, 30, 18 };
         Arrays.parallelPrefix(arr2, new IntBinaryOperator() {
         // left 代表数组中前一个索引处的元素，计算第一个元素时，left为1
         // right代表数组中当前索引处的元素
         public int applyAsInt(int left, int right) {
         return left * right;
         }
         });
         System.out.println(Arrays.toString(arr2));
         int[] arr3 = new int[5];
         Arrays.parallelSetAll(arr3, new IntUnaryOperator() {
         // operand代表正在计算的元素索引
         public int applyAsInt(int operand) {
         return operand * 5;
         }
         });
         System.out.println(Arrays.toString(arr3));
         */


    }

    /**
     *  java学习篇19-----------DecimalFormat
     */
    private class DecimalFormatTemp{
        /**
         符号 位置  本地化  含义
         0  数字   是  阿拉伯数字
         #  数字   是  阿拉伯数字，如果不存在则显示为 0
         .  数字  是  小数分隔符或货币小数分隔符
         -  数字  是  减号
         ,  数字  是  分组分隔符
         E  数字  是  分隔科学计数法中的尾数和指数。在前缀或后缀中无需加引号。
         ;  子模式边界  是  分隔正数和负数子模式
         %  前缀或后缀  是  乘以 100 并显示为百分数
         \u2030  前缀或后缀  是  乘以 1000 并显示为千分数
         ¤ (\u00A4)  前缀或后缀  否  货币记号，由货币符号替换。如果两个同时出现，则用国际货币符号替换。如果出现在某个模式中，则使用货币小数分隔符，而不使用小数分隔符。
         '  前缀或后缀  否  用于在前缀或或后缀中为特殊字符加引号，例如 "'#'#" 将 123 格式化为 "#123"。要创建单引号本身，请连续使用两个单引号："# o''clock"。


         DecimalFormat dec=new DecimalFormat("#.####");
         double a=1.25465456456465;
         System.out.println(dec.format(a));
         //保留小数
         第一种：
         java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");
         double d=3.14159;
         System.out.println(df.format(d));

         第二种：
         java.math.BigDecimal
         BigDecimal bd = new BigDecimal("3.14159265");
         bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);

         第三种：
         long l = Math.round(3.14159*100); //四舍五入
         double ret = l/100.0; //注意：使用 100.0 而不是 100

         第四种：
         double d = 13.4324;
         d=((int)(d*100))/100;

         一般使用前两种，个人认为第一种最好~~

         DecimalFormat df1 = new DecimalFormat("0.0");
         DecimalFormat df2 = new DecimalFormat("#.#");
         DecimalFormat df3 = new DecimalFormat("000.000");
         DecimalFormat df4 = new DecimalFormat("###.###");
         System.out.println(df1.format(12.34));
         System.out.println(df2.format(12.34));
         System.out.println(df3.format(12.34));
         System.out.println(df4.format(12.34));
         结果：
         12.3
         12.3
         012.340
         12.34
         */
    }

    /**
     *  java学习篇20-----------双冒号使用::
     *  1.类名::静态方法名
     *  2.
     */
    private class colon{
        /** ①引用静态方法
         public class Colon{
        @Test
        public void test(){
        List<String> list = Arrays.asList("a","b","c");
        //静态方法引用  ClassName::methodName
        list.forEach(Colon::print);
        //上一行等价于
        //list.forEach((x)->Colon.print(x));
        }
        //静态方法
        public static void print(String s){
        System.out.println(s);
        }
        }

         ②引用特定对象实例方法
         public class Colon{
        @Test
        public void test(){
        List<String> list = Arrays.asList("a","b","c");
        //实例方法引用  instanceRef::methodName
        list.forEach(new Colon()::print);
        //上一行等价于
        //list.forEach((x)->new Colon().print(x));
        }
        //实例方法
        public void print(String s){
        System.out.println(s);
        }
        }

         ③引用特定类型的任意对象的实例方法
         public class Colon{
        @Test
        public void test(){
        String[] arr = { "Barbara", "James", "Mary", "John",
        "Patricia", "Robert", "Michael", "Linda" };
        //引用String类型的任意对象的compareToIgnoreCase方法实现忽略大小写排序
        Arrays.sort(arr, String::compareToIgnoreCase);
        //上一行等价于
        //Arrays.sort(arr, (a,b)->a.compareToIgnoreCase(b));
        //输出
        for(String s:arr){
        System.out.println(s);
        }
        }
        }

         ④引用超类（父类）实例方法
         public class Colon extends BaseColon{
        @Test
        public void test(){
        List<String> list = Arrays.asList("a","b","c");
        //实例方法引用  instanceRef::methodName
        list.forEach(super::print);
        }
        }
         class BaseColon{
         //实例方法
         public void print(String s){
         System.out.println(s);
         }
         }

         ⑤引用类构造方法
         public class Example {
         private String name;
         Example(String name){
         this.name = name;
         }

         public static void main(String[] args) {
         InterfaceExample com =  Example::new;
         Example bean = com.create("hello world");
         System.out.println(bean.name);
         }
         }
         interface InterfaceExample{
         Example create(String name);
         }

         ⑥引用数组构造方法
         public class Colon{
         public static void main(String[] args) {
         Function<Integer,Colon[]> function = Colon[]::new;
         //调用apply方法创建数组，这里的5是数组的长度
         Colon[] arr = function.apply(5);
         //循环输出-初始都为null
         for(Colon c:arr){
         System.out.println(c);
         }
         }
         }*/
    }

    /**
     * String详解
     */
    private class  StringTemp{
        /**
         String : 字符串类型
         一、构造函数
         String(byte[ ] bytes)：通过byte数组构造字符串对象。
         String(char[ ] value)：通过char数组构造字符串对象。
         String(Sting original)：构造一个original的副本。即：拷贝一个original。
         String(StringBuffer buffer)：通过StringBuffer数组构造字符串对象。
         例如：
         byte[] b = {'a','b','c','d','e','f','g','h','i','j'};
         char[] c = {'0','1','2','3','4','5','6','7','8','9'};
         String sb = new String(b);                 //abcdefghij
         String sb_sub = new String(b,3,2);     //de
         String sc = new String(c);                  //0123456789
         String sc_sub = new String(c,3,2);    //34
         String sb_copy = new String(sb);       //abcdefghij
         System.out.println("sb:"+sb);
         System.out.println("sb_sub:"+sb_sub);
         System.out.println("sc:"+sc);
         System.out.println("sc_sub:"+sc_sub);
         System.out.println("sb_copy:"+sb_copy);
         输出结果：sb:abcdefghij
         sb_sub:de
         sc:0123456789
         sc_sub:34
         sb_copy:abcdefghij
         二、方法：
         说明：①、所有方法均为public。
         ②、书写格式： [修饰符] <返回类型><方法名([参数列表])>
         例如：static int parseInt(String s)
         表示此方法(parseInt)为类方法(static),返回类型为(int),方法所需要为String类型。
         1. char charAt(int index) ：取字符串中的某一个字符，其中的参数index指的是字符串中序数。字符串的序数从0开始到length()-1 。
         例如：String s = new String("abcdefghijklmnopqrstuvwxyz");
         System.out.println("s.charAt(5): " + s.charAt(5) );
         结果为： s.charAt(5): f
         2. int compareTo(String anotherString) ：当前String对象与anotherString比较。相等关系返回０；不相等时，从两个字符串第0个字符开始比较，返回第一个不相等的字符差，另一种情况，较长字符串的前面部分恰巧是较短的字符串，返回它们的长度差。
         3. int compareTo(Object o) ：如果o是String对象，和2的功能一样；否则抛出ClassCastException异常。
         例如:String s1 = new String("abcdefghijklmn");
         String s2 = new String("abcdefghij");
         String s3 = new String("abcdefghijalmn");
         System.out.println("s1.compareTo(s2): " + s1.compareTo(s2) ); //返回长度差
         System.out.println("s1.compareTo(s3): " + s1.compareTo(s3) ); //返回'k'-'a'的差
         结果为：s1.compareTo(s2): 4
         s1.compareTo(s3): 10
         4. String concat(String str) ：将该String对象与str连接在一起。
         5. boolean contentEquals(StringBuffer sb) ：将该String对象与StringBuffer对象sb进行比较。
         6. static String copyValueOf(char[] data) ：
         7. static String copyValueOf(char[] data, int offset, int count) ：这两个方法将char数组转换成String，与其中一个构造函数类似。
         8. boolean endsWith(String suffix) ：该String对象是否以suffix结尾。
         例如：String s1 = new String("abcdefghij");
         String s2 = new String("ghij");
         System.out.println("s1.endsWith(s2): " + s1.endsWith(s2) );
         结果为：s1.endsWith(s2): true
         9. boolean equals(Object anObject) ：当anObject不为空并且与当前String对象一样，返回true；否则，返回false。
         10. byte[] getBytes() ：将该String对象转换成byte数组。
         11. void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) ：该方法将字符串拷贝到字符数组中。其中，srcBegin为拷贝的起始位置、srcEnd为拷贝的结束位置、字符串数值dst为目标字符数组、dstBegin为目标字符数组的拷贝起始位置。
         例如：char[] s1 = {'I',' ','l','o','v','e',' ','h','e','r','!'};//s1=I love her!
         String s2 = new String("you!"); s2.getChars(0,3,s1,7); //s1=I love you!
         System.out.println( s1 );
         结果为：I love you!
         12. int hashCode() ：返回当前字符的哈希表码。
         13. int indexOf(int ch) ：只找第一个匹配字符位置。
         14. int indexOf(int ch, int fromIndex) ：从fromIndex开始找第一个匹配字符位置。
         15. int indexOf(String str) ：只找第一个匹配字符串位置。
         16. int indexOf(String str, int fromIndex) ：从fromIndex开始找第一个匹配字符串位置。
         例如：String s = new String("write once, run anywhere!");
         String ss = new String("run");
         System.out.println("s.indexOf('r'): " + s.indexOf('r') );
         System.out.println("s.indexOf('r',2): " + s.indexOf('r',2) );
         System.out.println("s.indexOf(ss): " + s.indexOf(ss) );
         结果为：s.indexOf('r'): 1
         s.indexOf('r',2): 12
         s.indexOf(ss): 12
         17. int lastIndexOf(int ch)
         18. int lastIndexOf(int ch, int fromIndex)
         19. int lastIndexOf(String str)
         20. int lastIndexOf(String str, int fromIndex) 以上四个方法与13、14、15、16类似，不同的是：找最后一个匹配的内容。
         public class CompareToDemo {
         public static void main (String[] args) {
         String s1 = new String("acbdebfg");

         System.out.println(s1.lastIndexOf((int)'b',7));
         }
         }
         运行结果：5
         （其中fromIndex的参数为 7，是从字符串acbdebfg的最后一个字符g开始往前数的位数。既是从字符c开始匹配，寻找最后一个匹配b的位置。所以结果为 5）


         21. int length() ：返回当前字符串长度。
         22. String replace(char oldChar, char newChar) ：将字符号串中第一个oldChar替换成newChar。
         23. boolean startsWith(String prefix) ：该String对象是否以prefix开始。
         24. boolean startsWith(String prefix, int toffset) ：该String对象从toffset位置算起，是否以prefix开始。
         例如：String s = new String("write once, run anywhere!");
         String ss = new String("write");
         String sss = new String("once");
         System.out.println("s.startsWith(ss): " + s.startsWith(ss) );
         System.out.println("s.startsWith(sss,6): " + s.startsWith(sss,6) );
         结果为：s.startsWith(ss): true
         s.startsWith(sss,6): true
         25. String substring(int beginIndex) ：取从beginIndex位置开始到结束的子字符串。
         26.String substring(int beginIndex, int endIndex) ：取从beginIndex位置开始到endIndex位置的子字符串。
         27. char[ ] toCharArray() ：将该String对象转换成char数组。
         28. String toLowerCase() ：将字符串转换成小写。
         29. String toUpperCase() ：将字符串转换成大写。
         例如：String s = new String("Java.lang.Class String");
         System.out.println("s.toUpperCase(): " + s.toUpperCase() );
         System.out.println("s.toLowerCase(): " + s.toLowerCase() );
         结果为：s.toUpperCase(): JAVA.LANG.CLASS STRING
         s.toLowerCase(): java.lang.class string
         30. static String valueOf(boolean b)
         31. static String valueOf(char c)
         32. static String valueOf(char[] data)
         33. static String valueOf(char[] data, int offset, int count)
         34. static String valueOf(double d)
         35. static String valueOf(float f)
         36. static String valueOf(int i)
         37. static String valueOf(long l)
         38. static String valueOf(Object obj)
         以上方法用于将各种不同类型转换成Java字符型。这些都是类方法。


         Java中String类的常用方法:

         public char charAt(int index)
         返回字符串中第index个字符；
         public int length()
         返回字符串的长度；
         public int indexOf(String str)
         返回字符串中第一次出现str的位置；
         public int indexOf(String str,int fromIndex)
         返回字符串从fromIndex开始第一次出现str的位置；
         public boolean equalsIgnoreCase(String another)
         比较字符串与another是否一样（忽略大小写）；
         public String replace(char oldchar,char newChar)
         在字符串中用newChar字符替换oldChar字符
         public boolean startsWith(String prefix)
         判断字符串是否以prefix字符串开头；
         public boolean endsWith(String suffix)
         判断一个字符串是否以suffix字符串结尾；
         public String toUpperCase()
         返回一个字符串为该字符串的大写形式；
         public String toLowerCase()
         返回一个字符串为该字符串的小写形式
         public String substring(int beginIndex)
         返回该字符串从beginIndex开始到结尾的子字符串；
         public String substring(int beginIndex,int endIndex)
         返回该字符串从beginIndex开始到endsIndex结尾的子字符串
         public String trim()
         返回该字符串去掉开头和结尾空格后的字符串
         public String[] split(String regex)
         将一个字符串按照指定的分隔符分隔，返回分隔后的字符串数组
         实例：
         public class SplitDemo{
         public static void main (String[] args) {

         String date = "2008/09/10";
         String[ ] dateAfterSplit= new String[3];
         dateAfterSplit=date.split("/");         //以“/”作为分隔符来分割date字符串，并把结果放入3个字符串中。

         for(int i=0;i<dateAfterSplit.length;i++)
         System.out.print(dateAfterSplit[i]+" ");
         }
         }

         运行结果：2008 09 10          //结果为分割后的3个字符串
         实例：
         TestString1.java:
         程序代码
         public class TestString1
         {
         public static void main(String args[]) {
         String s1 = "Hello World" ;
         String s2 = "hello world" ;
         System.out.println(s1.charAt(1)) ;
         System.out.println(s2.length()) ;
         System.out.println(s1.indexOf("World")) ;
         System.out.println(s2.indexOf("World")) ;
         System.out.println(s1.equals(s2)) ;
         System.out.println(s1.equalsIgnoreCase(s2)) ;
         String s = "我是J2EE程序员" ;
         String sr = s.replace('我','你') ;
         System.out.println(sr) ;
         }
         }
         TestString2.java:
         程序代码
         public class TestString2
         {
         public static void main(String args[]) {
         String s = "Welcome to Java World!" ;
         String s2 = "   magci   " ;
         System.out.println(s.startsWith("Welcome")) ;
         System.out.println(s.endsWith("World")) ;
         String sL = s.toLowerCase() ;
         String sU = s.toUpperCase() ;
         System.out.println(sL) ;
         System.out.println(sU) ;
         String subS = s.substring(11) ;
         System.out.println(subS) ;
         String s1NoSp = s2.trim() ;
         System.out.println(s1NoSp) ;
         }

         ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
         ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
         Java语法总结 -字符串

         Java的String太特别了，也太常用了，所以重要。我初学Java就被它搞蒙了，太多混淆的概念了，比如它的不变性。所以必须深入机制地去理解它。


         1、String中的每个字符都是一个16位的Unicode字符，用Unicode很容易表达丰富的国际化字符集，比如很好的中文支持。甚至Java的标识符都可以用汉字，但是没人会用吧（只在一本清华的《Java2实用教程》看过）。

         2、判断空字符串。根据需要自己选择某个或者它们的组合
         if ( s == null )   //从引用的角度
         if ( s.length() == 0 )    //从长度判别
         if ( s.trim().length () == 0 )    //是否有多个空白字符
         trim()方法的作用是是移除前导和尾部的Unicode值小于'"u0020'的字符，并返回“修剪”好的字符串。这种方法很常用，比如需要用户输入用户名，用户不小心加了前导或者尾部空格，一个好的程序应该知道用户不是故意的，即使是故意的也应该智能点地处理。
         判断空串是很常用的操作，但是Java类库直到1.6才提供了isEmpty()方法。当且仅当 length()为 0 时返回 true。

         3、未初始化、空串""与null。它们是不同的概念。对未初始化的对象操作会被编译器挡在门外；null是一个特殊的初始化值，是一个不指向任何对象的引用，对引用为null的对象操作会在运行时抛出异常NullPointerException；而空串是长度为0的字符串，和别的字符串的唯一区别就是长度为0。
         例子：
         public class StringTest{
         staticString s1;
         publicstatic void main(String[] args) {
         String s2;
         String s3 = "";
         System.out.print(s1.isEmpty());     //运行时异常
         System.out.print(s2.isEmpty());     //编译出错
         System.out.print(s3.isEmpty());     //ok！输出true
         }
         }

         4、String类的方法很多，在编写相关代码的时候看看JDK文档时有好处的，要不然花了大量时间实现一个已经存在的方法是很不值得的，因为编写、测试、维护自己的代码使项目的成本增加，利润减少，严重的话会导致开不出工资……

         5、字符串的比较。
         Java不允许自定义操作符重载，因此字符串的比较要用compareTo() 或者 compareToIgnoreCase()。s1.compareTo(s2)，返回值大于0则，则前者大；等于0，一般大；小于0，后者大。比较的依据是字符串中各个字符的Unicode值。

         6、toString()方法。
         Java的任何对象都有toString()方法，是从Object对象继承而来的。它的作用就是让对象在输出时看起来更有意义，而不是奇怪的对象的内存地址。对测试也是很有帮助的。

         7、String对象是不变的！可以变化的是String对象的引用。
         String name = "ray";
         name.concat("long");  //字符串连接
         System.out.println(name); //输出name，ok，还是"ray"
         name = name.concat("long");  //把字符串对象连接的结果赋给了name引用
         System.out.println(name);  //输出name，oh！，变成了"raylong"
         上述三条语句其实产生了3个String对象，"ray"，"long"，"raylong"。第2条语句确实产生了"raylong"字符串，但是没有指定把该字符串的引用赋给谁，因此没有改变name引用。第3条语句根据不变性，并没有改变"ray"，JVM创建了一个新的对象，把"ray"，"long"的连接赋给了name引用，因此引用变了，但是原对象没变。

         8、String的不变性的机制显然会在String常量内有大量的冗余。如："1" +"2" + "3" +......+ "n"产生了n+(n+1)个String对象！因此Java为了更有效地使用内存，JVM留出一块特殊的内存区域，被称为“String常量池”。对String多么照顾啊！当编译器遇见String常量的时候，它检查该池内是否已经存在相同的String常量。如果找到，就把新常量的引用指向现有的String，不创建任何新的String常量对象。

         那么就可能出现多个引用指向同一个String常量，会不会有别名的危险呢？No problem！String对象的不变性可以保证不会出现别名问题！这是String对象与普通对象的一点区别。

         乍看起来这是底层的机制，对我们编程没什么影响。而且这种机制会大幅度提高String的效率，实际上却不是这样。为连接n个字符串使用字符串连接操作时，要消耗的时间是n的平方级！因为每两个字符串连接，它们的内容都要被复制。因此在处理大量的字符串连接时，而且要求性能时，我们不要用String，StringBuffer是更好的选择。

         8、StringBuffer类。StringBuffer类是可变的，不会在字符串常量池中，而是在堆中，不会留下一大堆无用的对象。而且它可将字符串缓冲区安全地用于多个线程。每个StringBuffer对象都有一定的容量。只要StringBuffer对象所包含的字符序列的长度没有超出此容量，就无需分配新的内部缓冲区数组。如果内部缓冲区溢出，则此容量自动增大。这个固定的容量是16个字符。我给这种算法起个名字叫“添饭算法”。先给你一满碗饭，不够了再给你一满碗饭。
         例子：
         StringBuffer sb = newStringBuffer();    //初始容量为 16个字符
         sb.append("1234");    //这是4个字符，那么16个字符的容量就足够了，没有溢出
         System.out.println(sb.length());    //输出字符串长度是4
         System.out.println(sb.capacity());    //输出该字符串缓冲区的容量是16

         sb.append("12345678901234567");       //这是17个字符，16个字符的容量不够了，扩容为17+16个字符的容量
         System.out.println(sb.length());    //输出字符串长度是17
         System.out.println(sb.capacity());    //输出该字符串缓冲区的容量是34

         sb.append("890").reverse().insert(10,"-");
         System.out.println(sb);        //输出0987654321-09876543214321

         字符串的长度和字符缓冲区的容量是两个概念，注意区别。
         还有串联的方式看起来是不是很酷！用返回值连接起来可以实现这种简洁和优雅。

         10、StringBuilder类。从J2SE 5.0提供了StringBuilder类，它和StringBuffer类是孪生兄弟，很像。它存在的价值在于：对字符串操作的效率更高。不足的是线程安全无法保证，不保证同步。那么两者性能到底差多少呢？很多！
         请参阅：http://book.csdn.NET/bookfiles/135/1001354628.shtml
         实践：
         单个线程的时候使用StringBuilder类，以提高效率，而且它的API和StringBuffer兼容，不需要额外的学习成本，物美价廉。多线程时使用StringBuffer，以保证安全。

         11、字符串的比较。
         下面这条可能会让你晕，所以你可以选择看或者不看。它不会对你的职业生涯造成任何影响。而且谨记一条，比较字符串要用equals()就ok了！一旦用了“==”就会出现很怪异的现象。之所以把这部分放在最后，是想节省大家的时间，因为这条又臭又长。推荐三种人：一、没事闲着型。二、想深入地理解Java的字符串，即使明明知道学了也没用。三、和我一样爱好研究“茴”字有几种写法。

         还是那句老话，String太特殊了，以至于某些规则对String不起作用。个人感觉这种特殊性并不好。看例子：
         例子A：
         String str1 = "java";
         String str2 = "java";
         System.out.print(str1==str2);
         地球上有点Java基础的人都知道会输出false，因为==比较的是引用，equals比较的是内容。不是我忽悠大家，你们可以在自己的机子上运行一下，结果是true！原因很简单，String对象被放进常量池里了，再次出现“java”字符串的时候，JVM很兴奋地把str2的引用也指向了“java”对象，它认为自己节省了内存开销。不难理解吧呵呵
         例子B：
         String str1 = new String("java");
         String str2 = new String("java");
         System.out.print(str1==str2);
         看过上例的都学聪明了，这次肯定会输出true！很不幸，JVM并没有这么做，结果是false。原因很简单，例子A中那种声明的方式确实是在String常量池创建“java”对象，但是一旦看到new关键字，JVM会在堆中为String分配空间。两者声明方式貌合神离，这也是我把“如何创建字符串对象”放到后面来讲的原因。大家要沉住气，还有一个例子。
         例子C：
         String str1 = "java";
         String str2 = "blog";
         String s = str1+str2;
         System.out.print(s=="javablog");


         再看这个例子，很多同志不敢妄言是true还是false了吧。爱玩脑筋急转弯的人会说是false吧……恭喜你，你会抢答了！把那个“吧”字去掉你就完全正确。原因很简单，JVM确实会对型如String str1 ="java"; 的String对象放在字符串常量池里，但是它是在编译时刻那么做的，而String s = str1+str2;是在运行时刻才能知道（我们当然一眼就看穿了，可是Java必须在运行时才知道的，人脑和电脑的结构不同），也就是说str1+str2是在堆里创建的，s引用当然不可能指向字符串常量池里的对象。没崩溃的人继续看例子D。
         例子D：
         String s1 = "java";
         String s2 = newString("java");
         System.out.print(s1.intern()==s2.intern());



         intern()是什么东东？反正结果是true。如果没用过这个方法，而且训练有素的程序员会去看JDK文档了。简单点说就是用intern()方法就可以用“==”比较字符串的内容了。在我看到intern()方法到底有什么用之前，我认为它太多余了。其实我写的这一条也很多余，intern()方法还存在诸多的问题，如效率、实现上的不统一……
         例子E：
         String str1 = "java";
         String str2 = new String("java");
         System.out.print(str1.equals(str2));






         无论在常量池还是堆中的对象，用equals()方法比较的就是内容，就这么简单！看完此条的人一定很后悔，但是在开始我劝你别看了……

         后记：用彪哥的话说“有意思吗？”，确实没劲。在写这段的时候我也是思量再三，感觉自己像孔乙己炫耀“茴”字有几种写法。我查了一下茴，回，囘，囬，还有一种是“口”字里面有个“目”字，后面这四个都加上草字头……


         ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

         ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
         String类主要方法的使用：
         1、获取长度 *.length();//这与数组中的获取长度不同，*.length;
         2、比较字符串(1) equals() //判断内容是否相同
         (2)compareTo() //判断字符串的大小关系
         (3)compareToIgnoreCase(String int) //在比较时忽略字母大小写
         (4)==   //判断内容与地址是否相同
         (5)equalsIgnoreCase() //忽略大小写的情况下判断内容是否相同
         如果想对字符串中的部分内容是否相同进行比较，可以用
         (6)reagionMatches() //有两种 public boolean regionMatches(int toffset, String other,int ooffset,int len);表示如果String对象的一个子字符串与参数other的一个子字符串是相同的字符序列，则为true.要比较的String 对象的字符串从索引toffset开始,other的字符串从索引ooffset开始，长度为len。
         public boolean reagionMatches(boolean ignoreCase,int toffset,String other,int ooffset,int len);//用布尔类型的参数指明两个字符串的比较是否对大小写敏感。
         一、查找字符串中某个位置的字符
         public char charAt(int index);//返回指定索引index位置上的字符，索引范围从0开始
         四、查找指定字符串在字符串中第一次或最后一词出现的位置
         在String类中提供了两种查找指定位置的字符串第一次出现的位置的方法
         (1)public int indexOf(String str);//从字符串开始检索str，并返回第一次出现的位置，未出现返回-1
         (2)public int indexOf(String str,int fromIndex);//从字符串的第fromIndex个字符开始检索str
         查找最后一次出现的位置有两种方法
         (1)public int lastIndexOf(String str);
         (2)public int lastIndexOf(String str,int fromIndex);
         如果不关心字符串的确切位置则可使用public boolean contains(CharSequence s);
         二、检查字符串的起始字符和结束字符
         开始的字符串两种方法
         (1)public boolean starWith(String prefix,int toffset);//如果参数prefix表示的字符串序列是该对象从索引toffset处开始的子字符串，则返回true
         (2)public boolean starWith(String prefix);
         结束的字符串方法
         public boolean endsWith(String suffix);
         三、截取子串
         (1)public String subString(int beginIndex);
         (2)public String subString(int beginIndex,int endIndex);//返回的字符串是从beginIndex开始到endIndex-1的串
         要返回后4位可以这样写Syetem.out.println(*.subString()(*.length()-4));
         四、字符串的替换
         两种方法
         (1)public String replace(char oldChar,char newChar);
         (2)public String replace(CharSequence target,CharSequence replacement);//把原来的etarget子序列替换为replacement序列，返回新串
         (3)public String replaceAll(String regex,String replacement);//用正则表达式实现对字符串的匹配
         五、字符串的大小写替转换
         (1)public String toLowerCase(Locale locale);
         (2)public String toLowerCase();
         (3)public String toupperCase(Locale locale);
         (4)public String toUpperCase();
         六、去除字符串首尾空格
         *.trim();
         七、字符串转换
         1、将字符串转换成字符数组
         public char[] toCharArray();
         2、将字符串转换成字符串数组
         public String[] split(String regex);//regex 是给定的匹配
         3、将其它数据类型转化为字符串
         (1)public static String valueOf(boolean b);
         (2)public static String valueOf(char c);
         (3)public static String valueOf(int i);
         (4)public static String valueOf(long i);
         (5)public static String valueOf(float f);
         (6)public static String valueOf(double d);
         (7)public static String valueOf(char[] data);
         (8)public static String valueOf(Object obj);
         可变字符串的创建和初始化
         两种方法:
         public StringBuffer();
         public StringBuffer(int caoacity);
         StringBuffer类主要方法的使用：
         一、获取可变字符串长度
         (1)public int length();
         (2)public int capacity();
         (3)public void setLength(int newLength);
         二、比较可变字符串
         用String 类的equals()方法比较，但是不同。
         类Object中的equals()方法比较的是两个对象的地址是否相等，而不仅仅是比较内容，但是String类在继承Object类的时候重写了equals()方法，只是比较两个对象的内容是否相等
         而在StringBuffer类中没有重写Object类的equals()方法，所以比较的是地址和内容。
         三、追加和插入字符串
         (1)追加 public StringBuffer append(type t);
         (2)插入 public StringBuffer insert(int offset,type t);//在offset处加入类型为type的字符串
         四、反转和删除字符串
         (1)反转 public StringBuffer reverse();
         (2)删除 public StringBuffer delete(int start,int end);
         五、减少用于可变字符序列的存储空间
         public void trimToSize();
         六、StringBuffer类转换成String类
         public String toString();
         */
    }

}


