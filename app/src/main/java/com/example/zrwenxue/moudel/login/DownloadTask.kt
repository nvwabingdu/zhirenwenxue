package com.example.zrwenxue.moudel.login

import android.os.AsyncTask
import java.lang.Exception

/**
 * @Author qiwangi
 * @Date 2023/7/26
 * @TIME 11:03
 */
/**
 * 1.params 在执行AsyncTask时需要传入的参数，可用于在后台任务中显示. (这里用Unit表示不需要)
 * 2.progress 在后台任务时需要显示的进度信息，这里使用指定的泛型作为进度单位. (用Int表示用int做进度单位)
 * 3.result 当任务结束时，如果需要对结果进行返回 则使用这里指定的泛型作为返回值类型. (这里用布尔值反馈结果)
 *
 * 用于学习 方法已过时
 *
 *
 * 使用:  DownloadTask().execute()
 */
class DownloadTask : AsyncTask<Unit, Int, Boolean>() {

    override fun onPreExecute() {
        super.onPreExecute()
        //进行界面上的初始化操作
//        progressDialog.show()  //显示进度条
    }

    override fun doInBackground(vararg params: Unit?): Boolean {

        //这个方法中的所有代码都会在子线程中运行，我们应该在这里处理耗时操作
        //不可以进行UI操作
        try {
            while (true) {
//            val downloadPercent=doDownload()//这是一个虚构的方法 具体的需要实现
//            publishProgress(downloadPercent)
//            if (downloadPercent>=100){
//                break
//            }
            }
        } catch (e: Exception) {
            //
        }


        return true//false  如果DownloadTask: AsyncTask<Unit, Int, Unit>()第三个参数为Unit则不用返回值
    }


    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        //操作UI  比如更新下载进度
//        progressDialog.sentmessage("download ${current}%")
    }

    override fun onPostExecute(result: Boolean?) {
//        super.onPostExecute(result)//注释
//        progressDialog,dismiss()//关闭进度对话框
        //在这里提示下载结果
        if (result!!) {
            //成功
        } else {
            //失败
        }

    }


}