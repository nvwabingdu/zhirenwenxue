package com.example.zrlearn.b_java.design_mode;

/**
 * @Author qiwangi
 * @Date 2023/8/11
 * @TIME 10:58
 */
public class Single {
   /* *//**单例中使用context 最好用app的context 不然容易造成内存泄露*//*

    *//**
     * 饿汉式  简单方便. 启动时就实例化, 耗资源.
     *//*
    public class Singleton1 {
        private static final Singleton1 INSTANCE = new Singleton1();
        // 类的外部禁止实例化 (如果使用反射, 还是可以实例化的)
        private Singleton1() {}
        // 提供一个获取类的实例的方法
        public static Singleton1 getInstance() {
            return INSTANCE;
        }
        // 其他实例方法
        public void method1() {}
        public void method2() {}
    }

    *//**
     * 饿汉模式(变种)  优缺点同上, 只是实例化时, 利用了"static block"而已, 没有区别.
     *//*
    public class Singleton2 {
        private static final Singleton2 INSTANCE;
        // 和前一种写法类似, 只不过把类静态变量INSTANCE的实例化代码放在静态块中
        static {
            INSTANCE = new Singleton2();
        }

        // 构造方法设为private, 禁外部实例化此类
        private Singleton2() {}

        // 对外提供一个获取实例的方法
        public static Singleton2 getInstance() {
            return INSTANCE;
        }

        // 其他实例方法
        public void method() {}
    }

    *//**
     * 懒汉, 同步方法  lazy load 且 线程安全 ！ (由于同步, 导致效率低下!)
     *//*
    public class Singleton3 {
        private static Singleton3 INSTANCE;

        private Singleton3() {}

        public synchronized static Singleton3 getInstance() {
            if(INSTANCE == null) INSTANCE = new Singleton3();
            return INSTANCE;
        }

        // 其他实例方法
        public void method() {}
    }

    *//**
     * 懒汉 (非线程安全) 有lazy load效果, 但是这种写法在多线程环境下是非线程安全的, 如果多个线程同时调用getInstance()方法, 可能导致多个实例的创建.
     *//*
    public class Singleton4 {
        private static Singleton4 INSTANCE;

        private Singleton4() {}

        public static Singleton4 getInstance() {
            if(INSTANCE == null) INSTANCE = new Singleton4();
            return INSTANCE;
        }

        private void method() {}
    }


    *//**
     * 静态内部类 推荐使用
     * lazy load 且 线程安全.
     * 这种方式利用了ClassLoader 的原理
     * java中的类一般都是你用的时候才加载 (手动加载除外 Class.forName ), 因此加载Singleton5时并不会实例化, 而等到调用getInstance方法(此处调用了静态内部类InstanceHolder, 仅此会加载 InstanceHolder, 当然就会实例化其内部的静态变量INSTANCE了) 的时候才会实例化, 这样就达到了lazy load的效果
     * 同步是利用ClassLoader实现的 (ClassLoader加载类的过程是同步的, 即线程安全的). 因此, 这里两个功能都具备.
     *//*
    public class Singleton5 {

        private Singleton5() {}

        // 利用类加载器来加载InstanceHolder 并 创建 INSTANCE 对象
        // 类加载器加载类的过程是线程全的:
        *//**
         // java.lang.ClassLoader类的 loadClass() 方法的部分代码:
         protected Class<?> loadClass(String name, boolean resolve)
         throws ClassNotFoundException
         {
         // 同步块 (线程安全)
         synchronized (getClassLoadingLock(name)) {
         // First, check if the class has already been loaded
         Class<?> c = findLoadedClass(name);
         ......
         }
         }
         *//*
        private static class InstanceHolder {
            private static Singleton5 INSTANCE = new Singleton5();
        }

        public static Singleton5 getInstance() {
            return InstanceHolder.INSTANCE;
        }

        public void method() {
        }
    }


    *//**
     * 枚举
     * 线程安全. 此种方式比较少见.
     *//*
    public enum Singleton6 {
        INSTANCE; // 枚举的实例可以看做是 "public static" 修饰的, 因此加载枚举类时, 它的实例 INSTANCE 就实例化了

        // 枚举是无法在枚举外部实例化的, 其实例在定义时就实例化了, 如上面的INSTANCE, 或者如下的例子: Colors 的 RED,GREEN,BLUE
        // private Singleton6() {} //

        // 其他实例方法
        public void method() {
        }
    }

    public enum Colors {
        RED,GREEN,BLUE
    }

    *//**
     * 双重校验加锁
     * lazy load 且 线程安全. 但是, 由于JVM底层模型原因, 偶尔会出问题, 不建议使用.
     *//*
    public class Singleton7 {

        private volatile static Singleton7 INSTANCE;

        private Singleton7() {
        }

        public static Singleton7 getInstance() {
            if(INSTANCE == null) {
                synchronized (Singleton7.class) {
                    if(INSTANCE == null) INSTANCE = new Singleton7();
                }
            }
            return INSTANCE;
        }

        public void method() {

        }
    }*/

}
