package com.example.zrwenxue.moudel.main.center.crypt

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.BaseActivity
import com.example.zrwenxue.moudel.main.word.MyStatic
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GetZrbActivity : BaseActivity() {

    override fun layoutResId(): Int = R.layout.activity_get_zrb

    override fun init() {
        setTopView()
        getZrb()
    }

    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView() {
        topView = findViewById(R.id.title_bar)
        topView!!.setTitle("获取智人币")
        //左边返回
        topView!!.setOnclickLeft(
            View.VISIBLE,
            View.OnClickListener { finish() })
        //右边弹出pop
        topView!!.setOnclickRight(View.INVISIBLE, View.OnClickListener { })
    }


    private var ciphertextOutput: TextView? = null
    private var tvZrb: TextView? = null
    private var tvZrbCopy: TextView? = null
    private var tvZrbTime: TextView? = null
    private var tvZrbHolder: TextView? = null
    private var tvZrbHolderPassword: TextView? = null
    private var tvZrbBalance: TextView? = null
    private var zrbExplain: View? = null

    //设置输入
    @SuppressLint("SetTextI18n")
    private fun getZrb() {
        ciphertextOutput = findViewById(R.id.ciphertext_output)
        tvZrb = findViewById(R.id.tv_zrb)
        tvZrbCopy = findViewById(R.id.tv_zrb_copy)
        tvZrbTime = findViewById(R.id.tv_zrb_time)
        tvZrbHolder = findViewById(R.id.tv_zrb_holder)
        tvZrbHolderPassword = findViewById(R.id.tv_zrb_holder_password)
        tvZrbBalance = findViewById(R.id.tv_zrb_balance)
        zrbExplain = findViewById(R.id.zrb_explain)
        zrbExplain!!.visibility=View.GONE
        tvZrbCopy!!.text="粘贴"


        tvZrbCopy!!.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager// 获取剪贴板管理器
            if (clipboardManager.hasPrimaryClip()) {// 检查剪贴板是否有内容
                // 获取剪贴板的内容
                val clipData = clipboardManager.primaryClip
                var clipText=""
                if (clipData != null && clipData.itemCount > 0) {
                    // 获取第一个剪贴板项目的文本
                     clipText = clipData.getItemAt(0).text.toString()
                    // 将剪贴板的文字设置到文本控件
                    tvZrb!!.text = clipText
                }


                try {
                    //开始解析
                    parseZrb(clipText)
                }catch (e:Exception){
                    tvZrb!!.text = "您本次复制的内容无效，请复制有效智人币（此文本仅作提示）"
                    MyStatic.showToast(this,"非法智人币")
                }

            }
        }













//        //这里只是做展示  通过共享参数来取
//        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
//        val time = sharedPreferences.getString("time", null)
//        val savedName = sharedPreferences.getString("name", null)
//        val savedPassword = sharedPreferences.getString("password", null)
//        val balance = sharedPreferences.getString("balance", null)
//
//
//
//        val date = Date(time!!.toLong())//val timestamp = 1624553400000L // 2021-06-23 12:30:00
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//        val formattedDate = dateFormat.format(date)
//        tvZrbTime!!.text = formattedDate
//        tvZrbHolder!!.text = savedName
//        tvZrbHolderPassword!!.text = savedPassword
//        tvZrbBalance!!.text = balance
//
//        val zrbShow = "我爱张娟" + "|" + time + "|" + savedName + "|" + savedPassword + "|" + balance
//
//        Log.e("tag4546", "明文：" + zrbShow)
//
//        try {
//            //通过AES加密明文   并将密码转为md5  作为密钥
//            val zrb = "Zrb-" + Single.encryptAES(
//                zrbShow,
//                Single.getMD5Hash(savedPassword!!)!!
//            ) + Single.getMD5Hash(savedPassword)!!
//            tvZrb!!.text = zrb
//
//            Log.e("tag4546", "zrb：" + zrb)
//        } catch (e: Exception) {
//            MyStatic.showToast(this,e.toString())
//            Log.e("tag4546", e.toString())
//        }
    }


    /**
     * 解析智人币
     */
    private fun parseZrb(input: String){
        if (input==""){
            MyStatic.showToast(this,"未获取到智人币信息")
        }else{
            val zrb=Single.decryptAES(
                MyStatic.extractSubstring(input),
                MyStatic.trimAndTakeLast32(input)
            )

            Log.e("tag4546", "明文：" + MyStatic.extractSubstring(input))
            Log.e("tag4546", "密码：" + MyStatic.trimAndTakeLast32(input))


            val date = Date(zrb.split("|")[1].toLong())//val timestamp = 1624553400000L // 2021-06-23 12:30:00
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(date)

            tvZrbTime!!.text = formattedDate
            tvZrbHolder!!.text = zrb.split("|")[2]
            tvZrbBalance!!.text = zrb.split("|")[4]
            tvZrbHolderPassword!!.text = zrb.split("|")[5]
        }

    }

}