package com.example.zrwenxue.moudel.main.home.lottery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-06-02
 * Time: 18:50
 */
class LotteryChoice {

    //大乐透 35 12   双色球 33 16
    ArrayList<Integer> beforTemp = new ArrayList<>();
    ArrayList<Integer> lastTemp = new ArrayList<>();
    public  ArrayList<Integer> dlt_ssq(int redMax, int blueMax) {
        if (beforTemp != null) {
            beforTemp.clear();
        }
        if (lastTemp != null) {
            lastTemp.clear();
        }
        //装球  大乐透 35 12   双色球 33 16
        setball(redMax, blueMax);
        // 创建随机函数
        Random random = new Random();
        // 轮询取球
        if(redMax==35){//说明是大乐透
            for (int i = 35; i > 30; i--) {
                int tempA = random.nextInt(i);
                beforTemp.add(beforball.get(tempA));
                beforball.remove(tempA);
                if (i > 33) {
                    int tempB = random.nextInt(i - 23);
                    lastTemp.add(lastball.get(tempB));
                    lastball.remove(tempB);
                }
            }
        }else {//说明是双色球
            for (int i = 33; i > 27; i--) {
                int tempA = random.nextInt(i);
                beforTemp.add(beforball.get(tempA));
                beforball.remove(tempA);
                if (i > 32) {
                    int tempB = random.nextInt(i - 17);
                    lastTemp.add(lastball.get(tempB));
                    lastball.remove(tempB);
                }
            }
        }

        //排序
        Collections.sort(beforTemp);
        Collections.sort(lastTemp);
        //合区
        beforTemp.addAll(lastTemp);
        return beforTemp;
    }

    //排列三参数为3   排列五参数为5  七星彩参数为7
    public  ArrayList<Integer> pl3_pl5(int num) {
        ArrayList<Integer> ballList = new ArrayList<>();
        if (ballList != null) {
            ballList.clear();
        }

        if(num==7){
            Random random = new Random();
            for (int i = 0; i < num-1; i++) {
                int temp = random.nextInt(10);
                ballList.add(temp);
            }
            ballList.add(random.nextInt(15));
        }else {
            Random random = new Random();
            for (int i = 0; i < num; i++) {
                int temp = random.nextInt(10);
                ballList.add(temp);
            }
        }


        System.out.println("----中奖号码" + ballList.toString());
        return ballList;
    }

    //装球   redMax 红球最大个数      blueMax  蓝球最大个数
    ArrayList<Integer> beforball = new ArrayList<>();
    ArrayList<Integer> lastball = new ArrayList<>();

    //装球
    private  void setball(int redMax, int blueMax) {
        if (beforball != null) {
            beforball.clear();
        }
        if (lastball != null) {
            lastball.clear();
        }

        for (int i = 0; i < redMax; i++) {
            beforball.add(i + 1);
            if (i < blueMax) {
                lastball.add(i + 1);
            }
        }


    }


}
