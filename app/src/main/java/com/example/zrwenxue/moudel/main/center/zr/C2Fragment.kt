package com.example.zrwenxue.moudel.main.center.zr


import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.newzr.R
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.BaseFragment
import com.example.zrwenxue.moudel.main.word.MyStatic
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class C2Fragment : BaseFragment() {
    
    override fun setLayout(): Int {
        return R.layout.fragment_c2
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
    private var btu1: Button? = null

    //设置输入
    @SuppressLint("SetTextI18n")
    private fun getZrb() {
        ciphertextOutput = fvbi(R.id.ciphertext_output)
        ciphertextOutput!!.text="将智人币粘贴在这里"
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
        btu1 = fvbi(R.id.btu1)
        btu1!!.visibility= View.GONE




        //这里只是做展示  通过共享参数来取
        val sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val time = sharedPreferences.getString("time", null)
        val savedName = sharedPreferences.getString("name", null)
        val savedPassword = sharedPreferences.getString("password", null)
        val balance = sharedPreferences.getString("balance", null)
        val tempTime = sharedPreferences.getString("tempTime", null)




        btu1!!.setOnClickListener { //收取智人币
            //1.比较两个时间戳   临时币中的时间戳 和现在的时间对比   1 大于现在的时间 说明是预约时间可以领取  提示 多少时间开始领取
            //2.符合时间的情况下   如果当前时间大于临时币中的时间10分钟  则不可以领取   赠送的情况下 需要判断种类后面的时间戳是否是自己的时间戳      赠送15456454
            //3.打赏的情况    一般可以定义时间  到了时间有10分钟可以领取    赏金123456

            if (zrb!=""){
                when(zrb.split("|")[5]){
                    in "赠送" -> {//包含赠送

                        if (!MyStatic.isMoreThan10Minutes(zrb.split("|")[1].toLong())){//大于十分钟

                            if (zrb.split("|")[5].contains(time.toString())){//包含自己的时间戳

                                if (tempTime!=null&&tempTime!=""){
                                    if (!tempTime.contains(zrb.split("|")[1])){//表示已经领取过了


                                        val tempBalance=balance!!.toDouble()+zrb.split("|")[4].toDouble()
                                        // 保存用户信息到共享参数
                                        val editor = sharedPreferences.edit()
                                        editor.putString("balance", tempBalance.toString())
                                        editor.putString("tempTime", tempTime+"|"+zrb.split("|")[1])
                                        editor.apply()

                                        MyStatic.showToast(requireActivity(),"已收取"+zrb.split("|")[4].toDouble()+"个智人币")
                                    }else{
                                        MyStatic.showToast(requireActivity(),"重复领取")
                                    }
                                }else{

                                    val tempBalance=balance!!.toDouble()+zrb.split("|")[4].toDouble()
                                    // 保存用户信息到共享参数
                                    val editor = sharedPreferences.edit()
                                    editor.putString("balance", tempBalance.toString())
                                    editor.putString("tempTime", zrb.split("|")[1])
                                    editor.apply()

                                    MyStatic.showToast(requireActivity(),"已收取"+zrb.split("|")[4].toDouble()+"个智人币")
                                }

                            }else{
                                MyStatic.showToast(requireActivity(),"馈赠对象有误，并非赠送给自己")
                            }
                        }else{
                            MyStatic.showToast(requireActivity(),"已超过领取时间")
                        }

                    }
                    in "赏金" -> {

                        if (!MyStatic.isMoreThan10Minutes(Single.extractSubstring(zrb.split("|")[5]).toLong())){//大于十分钟

                                if (tempTime!=null&&tempTime!=""){
                                    if (!tempTime.contains(Single.extractSubstring(zrb.split("|")[5]))){//表示已经领取过了


                                        val tempBalance=balance!!.toDouble()+zrb.split("|")[4].toDouble()
                                        // 保存用户信息到共享参数
                                        val editor = sharedPreferences.edit()
                                        editor.putString("balance", tempBalance.toString())
                                        editor.putString("tempTime", tempTime+"|"+Single.extractSubstring(zrb.split("|")[5]))
                                        editor.apply()

                                        MyStatic.showToast(requireActivity(),"已收取"+zrb.split("|")[4].toDouble()+"个智人币")
                                    }else{
                                        MyStatic.showToast(requireActivity(),"重复领取")
                                    }
                                }else{

                                    val tempBalance=balance!!.toDouble()+zrb.split("|")[4].toDouble()
                                    // 保存用户信息到共享参数
                                    val editor = sharedPreferences.edit()
                                    editor.putString("balance", tempBalance.toString())
                                    editor.putString("tempTime", Single.extractSubstring(zrb.split("|")[5]))
                                    editor.apply()

                                    MyStatic.showToast(requireActivity(),"已收取"+zrb.split("|")[4].toDouble()+"个智人币")
                                }

                        }else{
                            MyStatic.showToast(requireActivity(),"不在领取时间内，参考生效时间")
                        }

                    }
                }
            }
        }

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
    var zrb=""
    private fun parseZrb(input: String){
        if (input==""){
            MyStatic.showToast(requireActivity(),"未获取到智人币信息")
        }else{
            zrb = Single.decryptAES(
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


            when(zrb.split("|")[5]){
                    in "赠送" -> {
                        tvZrbKind!!.text="赠送"
                        btu1!!.visibility= View.VISIBLE
                        btu1!!.text="收取馈赠"
                    }
                    in "赏金" -> {
                        tvZrbKind!!.text="赏金"
                        btu1!!.visibility= View.VISIBLE
                        btu1!!.text="领取赏金"
                    }
            }
            tvZrbExplain!!.text = zrb.split("|")[6]
        }
    }




}