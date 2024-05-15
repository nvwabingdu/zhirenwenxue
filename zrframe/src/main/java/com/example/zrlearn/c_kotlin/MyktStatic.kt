package com.example.zrlearn.c_kotlin

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-11-12
 * Time: 13:30
 */
fun getNow(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())//返回当前日期

    println(
        "水密码客服小樱	814	438	162	36.99%	11715.63	723	800	0	100.00%	97.78%	9秒	1	0	0".split(
            "\t"
        )
    )
}


/**
 * 薪资计算 payroll("水密码客服小樱\t814\t438\t162\t36.99%\t11715.63\t723\t800\t0\t100.00%\t97.78%\t9秒\t1\t0\t0",31.5)
 */
fun payroll(str: String, days: Double) {
    val bean = str.split("\t")


    //分数集合
    var gradeList = mutableListOf<Int>()

    //客单价
    when (bean[5].toDouble() / bean[3].toDouble()) {
        in 70.0..Double.MAX_VALUE -> gradeList.add(100)
        in 60.0..69.9 -> gradeList.add(80)
        else -> gradeList.add(50)
    }
    //询单转化率
    when (bean[4].replace("%", "").trim().toDouble()) {
        in 42.0..Double.MAX_VALUE -> gradeList.add(500)
        in 39.0..41.99 -> gradeList.add(450)
        in 35.0..38.99 -> gradeList.add(350)
        else -> gradeList.add(200)
    }

    //三分钟人工回复率
    when (bean[9].replace("%", "").trim().toDouble()) {
        in 99.9..Double.MAX_VALUE -> gradeList.add(200)
        in 98.0..99.89 -> gradeList.add(150)
        else -> gradeList.add(50)
    }

    //30秒应答率
    when (bean[10].replace("%", "").trim().toDouble()) {
        in 95.0..Double.MAX_VALUE -> gradeList.add(50)
        in 85.0..94.99 -> gradeList.add(40)
        in 75.0..84.99 -> gradeList.add(30)
        else -> gradeList.add(20)
    }

    //平均人工响应时长
    when (bean[11].replace("秒", "").trim().toInt()) {
        in 35..Int.MAX_VALUE -> gradeList.add(20)
        in 25..34 -> gradeList.add(30)
        in 18..24 -> gradeList.add(40)
        else -> gradeList.add(50)
    }

    var dayMoney = 0
    //计算薪资
    when (gradeList.sum()) {
        in 900..1000 -> dayMoney = 160
        in 800..899 -> dayMoney = 136
        in 650..799 -> dayMoney = 120
        else -> dayMoney = 96
    }
    println("————————————————————————————————————————————————————————————————————————————————————————————————————————————")
    println("您好${bean[0]}")
    println("——")
    println("您本月的客单价为:${bean[5].toDouble() / bean[3].toDouble()}元;所得分数为：${gradeList[0]}")
    println(
        "您本月的询单转化率为:${
            bean[4].replace("%", "").trim().toDouble()
        }%;所得分数为：${gradeList[1]}"
    )
    println(
        "您本月的三分钟人工回复率为:${
            bean[10].replace("%", "").trim().toDouble()
        }%;所得分数为：${gradeList[2]}"
    )
    println(
        "您本月的30秒应答率为:${
            bean[10].replace("%", "").trim().toDouble()
        }%;所得分数为：${gradeList[3]}"
    )
    println(
        "您本月的平均人工响应时长为:${
            bean[11].replace("秒", "").trim().toInt()
        };所得分数为：${gradeList[4]}"
    )
    println("——")
    println("您本月的总分为:${gradeList.sum()}分,每天的工资为${dayMoney}")
    println("您本月工作了:${days}天")
    println("您本月的工资为：${dayMoney * days}元")
}
