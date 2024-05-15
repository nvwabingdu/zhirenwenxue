package com.example.zrlearn.b_java.design_mode.facede;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-06-17
 * Time: 19:23
 */
class StockB {
    private int stockCount = 0;

    public void sell(int count){
        stockCount -= count;
        System.out.println("卖了" + count + "支 StockB 股票");
    }

    public void buy(int count){
        stockCount += count;
        System.out.println("买了" + count + "支 StockB 股票");
    }

    public int getStockCount() {
        return stockCount;
    }
}
