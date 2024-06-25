package com.example.zrwenxue.moudel.main.center.crypt

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CryptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypt)

        //设置智人币
        setZrb()
    }


    private var ciphertextOutput: TextView? = null
    private var tvZrb: TextView? = null
    private var tvZrbCopy: TextView? = null
    private var tvZrbTime: TextView? = null
    private var tvZrbHolder: TextView? = null
    private var tvZrbHolderPassword: TextView? = null
    private var tvZrbBalance: TextView? = null
    //设置输入
    @SuppressLint("SetTextI18n")
    private fun setZrb(){
        ciphertextOutput = findViewById(R.id.ciphertext_output)
        tvZrb = findViewById(R.id.tv_zrb)
        tvZrbCopy = findViewById(R.id.tv_zrb_copy)
        tvZrbTime = findViewById(R.id.tv_zrb_time)
        tvZrbHolder = findViewById(R.id.tv_zrb_holder)
        tvZrbHolderPassword = findViewById(R.id.tv_zrb_holder_password)
        tvZrbBalance = findViewById(R.id.tv_zrb_balance)


        //这里只是做展示  通过共享参数来取
        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val time = sharedPreferences.getString("time", null)
        val savedName = sharedPreferences.getString("name", null)
        val savedPassword = sharedPreferences.getString("password", null)
        val balance = sharedPreferences.getString("balance", null)

        tvZrbCopy!!.setOnClickListener {
            val clipboard: ClipboardManager =
                this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", tvZrb!!.text.toString())
            clipboard.setPrimaryClip(clip)
            Single.centerToast(this, "已复制")
        }

//        val timestamp = 1624553400000L // 2021-06-23 12:30:00
        val date = Date(time!!.toLong())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        tvZrbTime!!.text=formattedDate

        tvZrbHolder!!.text=savedName
        tvZrbHolderPassword!!.text=savedPassword
        tvZrbBalance!!.text=balance

        val zrbShow="我爱张娟"+"|"+time+"|"+savedName+"|"+savedPassword+"|"+balance

        Log.e("tag4546","明文："+zrbShow)

        try {
            //通过AES加密明文   并将密码转为md5  作为密钥
            val zrb="Zrb-"+Single.encryptAES(zrbShow,Single.getMD5Hash(savedPassword!!)!!)+Single.getMD5Hash(savedPassword)!!
            tvZrb!!.text=zrb

            Log.e("tag4546","zrb："+zrb)
        }catch (e:Exception){
            Log.e("tag4546",e.toString())
        }
    }
}

