package com.example.zrlearn.b_java.design_mode.factory;

/**
 * 一家生产海尔电视的   一家生产小米电视的    我不需要知道具体的生产过程，然后只需要在生产电视的代理工厂
 * 定义一个方法，通过一个简单的名字参数，比如"海尔"  ”小米“  就可以生产出对应的电视。
 *
 *  一个软件系统可以提供多个外观不同的按钮（如圆形按钮、矩
 * 形按钮、菱形按钮等），这些按钮都源自同一个基类，不过在
 * 继承基类后不同的子类修改了部分属性从而使得它们可以呈现
 * 不同的外观
 *  如果我们希望在使用这些按钮时，不需要知道这些具体按钮类
 * 的名字，只需要知道表示该按钮类的一个参数，并提供一个调
 * 用方便的方法，把该参数传入方法即可返回一个相应的按钮对
 * 象
 *  此时，就可以使用简单工厂模式
 *
 *
 *
 * 被创建的类 通常具有同一个父类
 *
 */
class TVFactory {

    public static TV productTV(String tvName){
        //TV tv;
        if(tvName.equals("海尔")){return new HaierTV();}
        if(tvName.equals("小米")){return new XiaomiTV();}
        return null;
    }
}
