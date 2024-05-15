package com.example.zrlearn.b_java.design_mode.facede;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-06-17
 * Time: 19:25
 */
class Customer {

    public static void main(String[] args) {
        //不用外观模式
        StockA stockA = new StockA();//股票A
        StockB stockB = new StockB();//股票B
        StockC stockC = new StockC();//股票C

        stockA.buy(100);
        stockB.buy(200);
        stockC.buy(300);

        stockA.sell(100);
        stockB.sell(200);
        stockC.sell(300);


        //用外观模式的好处 简单方便  缺点，所有操作需要都击中在门面类
        Facade facade = new Facade();
        facade.buyAll(120);
        facade.buyStockA(50);
        facade.sellAll(80);
    }

}
