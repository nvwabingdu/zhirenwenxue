//package com.example.zrword.net.repository
//
//import androidx.lifecycle.liveData
//import com.example.newzr.net.GetApi
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.coroutineScope
//import kotlin.coroutines.CoroutineContext
//
///**
// * @Author qiwangi
// * @Date 2023/8/5
// * @TIME 21:46
// */
//object NetworkRepository {
//
//    /***
//     * 通过fire函数 统一调用try catch
//     */
//    fun getAppData()= fire(Dispatchers.IO){
//        val appData = GetApi.getAppData()
//        if (appData.states=="ok"){
//            val data = appData.data
//            Result.success(data)
//        }else{
//            Result.failure(RuntimeException("response status is ${appData.states}"))
//        }
//    }
//
//     private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
//        liveData<Result<T>>(context) {
//            val result = try {
//                block()
//            } catch (e: Exception) {
//                Result.failure<T>(e)
//            }
//            emit(result)
//        }
//
//    fun getWordData()= fire(Dispatchers.IO){
//       coroutineScope {
//
//
//
//       }
//    }
//
//
//
//    /**
//     * 正常写法
//     */
////    fun getAppData()= liveData(Dispatchers.IO){
////        val result=try {
////            val appData = GetApi.getAppData()
////
////            if (appData.states=="ok"){
////                val data = appData.data
////                Result.success(data)
////            }else{
////                Result.failure(RuntimeException("response status is ${appData.states}"))
////            }
////
////            //Result.success(appData)
////        }catch (e:Exception){
////            Result.failure<List<AppData>>(e)
////
////            //Result.failure(e)
////        }
////        emit(result)//发送数据 类似于postValue
////    }
//
//
//
//
//}