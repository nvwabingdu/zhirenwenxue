package com.example.zrlearn.b_java.design_mode.facede;

/**
外观模式（门面模式）：外观模式提供了一个统一的接口，用来访问子系统中的一群接口。外观模式定义一个高层接口，让子系统更容易使用。
 比如装一台电脑   需要去三个地方买东西  主机   硬盘   显示器

 有外观模式     就只需要去电脑城就可以办好三件事

 一家卖面的   一家卖肉的  一家卖菜的 你需要卖这三件东西，就要去这三个地方， 要是有一个上家，你只需要告诉他你需要这三件东西，他就
 依次到这三家地方去买。
 */
class Facade {
    private StockA stockA = null;
    private StockB stockB = null;
    private StockC stockC = null;

    public Facade() {
        stockA = new StockA();
        stockB = new StockB();
        stockC = new StockC();
    }

    public void buyAll(int count) {
        stockA.buy(count);
        stockB.buy(count);
        stockC.buy(count);
    }

    public void sellAll(int count) {
        stockA.sell(count);
        stockB.sell(count);
        stockC.sell(count);
    }

    public void buyStockA(int count) {
        stockA.buy(count);
    }

    public void buyStockB(int count) {
        stockB.buy(count);
    }

    public void buyStockC(int count) {
        stockC.buy(count);
    }

}
