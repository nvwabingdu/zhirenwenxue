package com.example.zrwenxue.moudel.main.center.crypt


import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.BaseFragment
import com.example.zrwenxue.moudel.main.word.MyStatic
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class C3Fragment : BaseFragment() {

    override fun setLayout(): Int {
        return R.layout.fragment_c3
    }

    override fun initView() {
        getZrb()

    }

    private var ciphertextOutput: TextView? = null
    private var tvZrb: TextView? = null
    private var tvZrbCopy: TextView? = null
    private var tvZrbTime: TextView? = null
    private var tvZrbHolder: TextView? = null
    private var tvZrbExplain: TextView? = null
    private var tvZrbBalance: TextView? = null
    private var tvZrbKind: TextView? = null
    private var zrbExplain: View? = null
    private var zrbTopView: View? = null

    //设置输入
    @SuppressLint("SetTextI18n")
    private fun getZrb() {
        ciphertextOutput = fvbi(R.id.ciphertext_output)
        ciphertextOutput!!.text="步骤1：将卖家的智人币粘贴在这里"
        tvZrb = fvbi(R.id.tv_zrb)
        tvZrbCopy = fvbi(R.id.tv_zrb_copy)
        tvZrbTime = fvbi(R.id.tv_zrb_time)
        tvZrbHolder = fvbi(R.id.tv_zrb_holder)
        tvZrbExplain = fvbi(R.id.tv_zrb_explain)
        tvZrbBalance = fvbi(R.id.tv_zrb_balance)
        zrbExplain = fvbi(R.id.zrb_explain)
        tvZrbKind = fvbi(R.id.tv_zrb_kind)
        zrbTopView = fvbi(R.id.zrb_top_view)
        zrbExplain!!.visibility= View.GONE
        zrbTopView!!.visibility= View.GONE
        tvZrbCopy!!.text="粘贴"


        tvZrbCopy!!.setOnClickListener {
            val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager// 获取剪贴板管理器
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
                    MyStatic.showToast(requireActivity(),"非法智人币")
                }

            }
        }
    }


    /**
     * 解析智人币
     */
    private fun parseZrb(input: String){
        if (input==""){
            MyStatic.showToast(requireActivity(),"未获取到智人币信息")
        }else{
            val zrb= Single.decryptAES(
                MyStatic.extractSubstring(input),
                MyStatic.trimAndTakeLast32(input)
            )

            Log.e("tag4546", "明文：" + MyStatic.extractSubstring(input))
            Log.e("tag4546", "密码：" + MyStatic.trimAndTakeLast32(input))

            zrbTopView!!.visibility= View.VISIBLE

            val date = Date(zrb.split("|")[1].toLong())//val timestamp = 1624553400000L // 2021-06-23 12:30:00
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(date)

            tvZrbTime!!.text = formattedDate
            tvZrbHolder!!.text = zrb.split("|")[2]
            tvZrbBalance!!.text = zrb.split("|")[4]
            tvZrbKind!!.text=zrb.split("|")[5]
            tvZrbExplain!!.text = zrb.split("|")[6]

        }

    }



}