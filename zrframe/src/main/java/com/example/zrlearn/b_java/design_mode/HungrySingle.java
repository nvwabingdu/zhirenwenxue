package com.example.zrlearn.b_java.design_mode;

/**
 *  饿汉式是典型的空间换时间，当类装载的时候就会创建类实例，
 * 不管你用不用，先创建出来，然后每次调用的时候，就不需要再
 * 判断，节省运行时间。 饿汉式是线程安全的，因为虚拟机保证了只会装载一次，在装载类的时候是不会发生并发的。
 */
public class HungrySingle {
        private static HungrySingle single = new HungrySingle();

        private HungrySingle(){} //私有构造函数
        //静态公有工厂方法，返回唯一实例
        public static HungrySingle getInstance(){
            return single;
        }
}
