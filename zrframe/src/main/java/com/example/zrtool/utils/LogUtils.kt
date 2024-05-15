package com.example.zrtool.utils

import android.util.Log
/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 11:12
 */
object LogUtils {
    private const val VERBOSE=1
    private const val DEBUG=2
    private const val INFO=3
    private const val WARN=4
    private const val ERROR=5
    private var level=VERBOSE//默认打印（正式上线时使用ERROR）

    fun showLog(mClass: Any, content: String) {
        val className = mClass.javaClass.name
        Log.e(className, content)
    }

    fun v(tag:String,msg:String){
        if (level<= VERBOSE){
            Log.v(tag,msg)
        }
    }

    fun d(tag:String,msg:String){
        if (level<= DEBUG){
            Log.d(tag,msg)
        }
    }

    fun i(tag:String,msg:String){
        if (level<= INFO){
            Log.i(tag,msg)
        }
    }

    fun w(tag:String,msg:String){
        if (level<= WARN){
            Log.w(tag,msg)
        }
    }

    fun e(tag:String,msg:String){
        if (level<= ERROR){
            Log.e(tag,msg)
        }
    }
}