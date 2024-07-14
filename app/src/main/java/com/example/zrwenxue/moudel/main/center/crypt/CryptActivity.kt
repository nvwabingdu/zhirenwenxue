package com.example.zrwenxue.moudel.main.center.crypt

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.app.TitleBarView
import com.example.zrwenxue.moudel.BaseActivity
import com.example.zrwenxue.moudel.main.word.MyStatic
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CryptActivity : BaseActivity() {


    override fun layoutResId(): Int = R.layout.activity_crypt

    override fun init() {
        //设置顶部
        setTopView()
        //设置智人币
        setZrb()
    }

    /**
     * 设置顶部
     */
    var topView: TitleBarView? = null
    private fun setTopView() {
        topView = findViewById(R.id.title_bar)
        topView!!.setTitle("智人币")
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


    private var b1: Button? = null
    private var b2: Button? = null
    private var b3: Button? = null
    private var b4: Button? = null


    //设置输入
    @SuppressLint("SetTextI18n")
    private fun setZrb() {
        ciphertextOutput = findViewById(R.id.ciphertext_output)
        tvZrb = findViewById(R.id.tv_zrb)
        tvZrbCopy = findViewById(R.id.tv_zrb_copy)
        tvZrbTime = findViewById(R.id.tv_zrb_time)
        tvZrbHolder = findViewById(R.id.tv_zrb_holder)
        tvZrbHolderPassword = findViewById(R.id.tv_zrb_holder_password)
        tvZrbBalance = findViewById(R.id.tv_zrb_balance)

        b1 = findViewById(R.id.b1)
        b1!!.setOnClickListener {
            MyStatic.setActivityString(
                this,
                ZrbTradeActivity::class.java, "", ""
            )
        }

        b2 = findViewById(R.id.b2)
        b2!!.setOnClickListener {
            MyStatic.setActivityString(
                this,
                ZrbGraffitiActivity::class.java, "", ""
            )
        }

        b3 = findViewById(R.id.b3)
        b3!!.setOnClickListener {
            MyStatic.setActivityString(
                this,
                GetZrbActivity::class.java, "", ""
            )
        }

        b4 = findViewById(R.id.b4)
        b4!!.setOnClickListener {
            MyStatic.setActivityString(
                this,
                BuyZrbActivity::class.java, "", ""
            )
        }


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

        val date = Date(time!!.toLong())//val timestamp = 1624553400000L // 2021-06-23 12:30:00
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        tvZrbTime!!.text = formattedDate

        tvZrbHolder!!.text = savedName
        tvZrbHolderPassword!!.text = savedPassword
        tvZrbBalance!!.text = balance

        val zrbShow = "我爱张娟" + "|" + time + "|" + savedName + "|" + savedPassword + "|" + balance+ "|"+"我这辈子最喜欢张娟"

        Log.e("tag4546", "明文：" + zrbShow)

        try {
            //通过AES加密明文   并将密码转为md5  作为密钥
            val zrb = "Zrb-" + Single.encryptAES(
                zrbShow,
                Single.getMD5Hash(savedPassword!!)!!
            ) + Single.getMD5Hash(savedPassword)!!
            tvZrb!!.text = zrb

            Log.e("tag4546", "zrb：" + zrb)
        } catch (e: Exception) {
            MyStatic.showToast(this,e.toString())
            Log.e("tag4546", e.toString())
        }
    }
}

