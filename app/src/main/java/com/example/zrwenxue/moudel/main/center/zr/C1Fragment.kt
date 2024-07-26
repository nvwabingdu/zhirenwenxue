package com.example.zrwenxue.moudel.main.center.zr


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.TextView
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.BaseFragment
import com.example.zrwenxue.moudel.main.word.MyStatic
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class C1Fragment : BaseFragment() {
    override fun setLayout(): Int {
        return R.layout.fragment_c1
    }

    override fun initView() {
        setZrb()
    }

    private var ciphertextOutput: TextView? = null
    private var tvZrb: TextView? = null
    private var tvZrbCopy: TextView? = null
    private var tvZrbTime: TextView? = null
    private var tvZrbHolder: TextView? = null
    private var tvZrbExplain: TextView? = null
    private var tvZrbBalance: TextView? = null
    private var tvZrbKind: TextView? = null

    //设置输入
    @SuppressLint("SetTextI18n")
    private fun setZrb() {
        ciphertextOutput = fvbi(R.id.ciphertext_output)
        ciphertextOutput!!.text="我的智人币"
        tvZrb = fvbi(R.id.tv_zrb)
        tvZrbCopy = fvbi(R.id.tv_zrb_copy)
        tvZrbTime = fvbi(R.id.tv_zrb_time)
        tvZrbHolder = fvbi(R.id.tv_zrb_holder)
        tvZrbExplain = fvbi(R.id.tv_zrb_explain)
        tvZrbBalance = fvbi(R.id.tv_zrb_balance)
        tvZrbKind = fvbi(R.id.tv_zrb_kind)

        //这里只是做展示  通过共享参数来取
        val sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val time = sharedPreferences.getString("time", null)
        val savedName = sharedPreferences.getString("name", null)
        val savedPassword = sharedPreferences.getString("password", null)
        val balance = sharedPreferences.getString("balance", null)

        tvZrbCopy!!.setOnClickListener {
            val clipboard: ClipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", tvZrb!!.text.toString())
            clipboard.setPrimaryClip(clip)
            Single.centerToast(requireActivity(), "已复制")
        }

        val date = Date(time!!.toLong())//val timestamp = 1624553400000L // 2021-06-23 12:30:00
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        tvZrbTime!!.text = formattedDate

        tvZrbHolder!!.text = savedName
        tvZrbExplain!!.text = "私有财产"
        tvZrbBalance!!.text = balance
        val kind="赠送"
        tvZrbKind!!.text=kind


        /**
         * 普通 赠送 打赏
         */
        val zrbShow = "我爱张娟" + "|" + time + "|" + savedName + "|" + savedPassword + "|" + balance+ "|"+kind+ "|" +"私有财产"
0
//        Log.e("tag4546", "明文：" + zrbShow)

        try {
            //通过AES加密明文   并将密码转为md5  作为密钥
            val zrb = "Zrb-" + Single.encryptAES(
                zrbShow,
                Single.getMD5Hash(savedPassword!!)!!
            ) + Single.getMD5Hash(savedPassword)!!
            tvZrb!!.text = zrb

//            Log.e("tag4546", "zrb：" + zrb)
        } catch (e: Exception) {
            MyStatic.showToast(requireActivity(),e.toString())
//            Log.e("tag4546", e.toString())
        }
    }

}