package com.example.zrwenxue.moudel.main.center.tetris


/**
 * @Author qiwangi
 * @Date 2023/5/16
 * @TIME 11:59
 */
data class TetrisBean(
    val color: String,
    val content: String,
    val height: Int,
    val mPosition:Int,
    val isClick:Boolean=false,//是否可以点击
)
