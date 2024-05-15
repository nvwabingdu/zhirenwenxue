package com.example.zrlearn.b_java.design_mode;

/**
  懒汉式是典型的时间换空间，也就是每次获取实例都会进行判断，
 看是否需要创建实例。如果一直没有人使用的话，那就不会创建
 实例，节约内存空间。（延迟加载: 开始不加载资源或者数据，
 等到马上就要使用这个资源或数据了才加载，所以也称Lazy
 Load(延迟加载)）从线程安全性上讲，不加同步的懒汉式是线程不安全的
 */
public class LazySingle {
    private static LazySingle single = null;
    private LazySingle(){}
    synchronized public static LazySingle getInstance(){
        if(single==null){
            single = new LazySingle();
        }
        return single;
    }
}
