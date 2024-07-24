package com.example.zrwenxue.others.zrdrawingboard

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.example.newzr.R
import com.example.zrwenxue.moudel.BaseActivity
import com.example.zrwenxue.moudel.main.word.MyStatic

class SetDoodleInfoActivity : BaseActivity() {
    override fun layoutResId(): Int = R.layout.activity_set_doodle_info

    override fun init() {
        //设置名称
        setName()
        //设置简介
        setDescription()
    }


    private lateinit var mSave: Button
    private lateinit var mNickname: EditText
    private lateinit var mNickNameCount: TextView
    private fun setName(){
        mSave = findViewById(R.id.but_save)

        mNickname = findViewById(R.id.et_nickname)
        mNickNameCount = findViewById(R.id.tv_nickname_count)
        mNickNameCount.text = "${mNickname.text.length}/20"
        mNickname.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SPACE) && event.action == KeyEvent.ACTION_DOWN) {
                // 拦截换行符和空格输入
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        val blockCharacterSet = " \n" // 定义要阻止输入的字符集合

        /**禁止输入换行和空格*/
        mNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // 不需要实现
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                // 每次用户输入字符时进行检查
                if (count > 0 && blockCharacterSet.indexOf(s[start]) >= 0) {
                    // 如果用户输入的字符是空格或换行符，则立即删除该字符
                    val newText =
                        s.subSequence(0, start).toString() + s.subSequence(start + count, s.length)
                            .toString()
                    mNickname.setText(newText)
                    mNickname.setSelection(start) // 设置光标位置
                }


            }

            override fun afterTextChanged(s: Editable) {
                // 不需要实现
                mNickNameCount.text = "${s?.length}/20"
            }
        })
    }

    //修改签名
    private lateinit var mDescription: EditText
    private lateinit var mDescriptionCount: TextView
    private fun setDescription(){
        //编辑签名布局可见
        findViewById<View>(R.id.ll_3).visibility= View.VISIBLE

        mDescription = findViewById(R.id.et_description)
        mDescriptionCount = findViewById(R.id.tv_description_count)
        mDescriptionCount.text = "${mDescription.text.length}"

        mDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 在文本改变之前执行的操作
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mDescriptionCount.text = "${s?.length}"
            }

            override fun afterTextChanged(s: Editable?) {
                // 在文本变化之后执行的逻辑
                val text = s.toString()
                // 不能以空格开头
                if (text.startsWith(" ")) {
                    // 删除首个字符（即空格）
                    s!!.delete(0, 1)
                }
                //不能以换行开头
                if (text.startsWith("\r")){
                    // 删除首个字符（即空格）
                    s!!.delete(0, 1)
                }
                //不能以换行开头
                if (text.startsWith("\n")){
                    // 删除首个字符（即空格）
                    s!!.delete(0, 1)
                }
                //不能以换行开头
                if (text.startsWith("\r\n")){
                    // 删除首个字符（即空格）
                    s!!.delete(0, 1)
                }
            }
        })

        //设置最大行数
        val maxLines = 6
        setMaxLinesWithFilter(mDescription, maxLines)


        mSave.setOnClickListener {
            if (
                mNickname.text.contains(">")
                || mNickname.text.contains("<")
                || mNickname.text.contains("@")
                || mNickname.text.contains("/")
                || mNickname.text.contains("#")
            ){
                ToastUtils.showShort("昵称包含@、#等特殊字符,请重新输入")
                return@setOnClickListener
            }


            if (mDescription.text.toString()!=""&&mNickname.text.toString()!=""){
                MyStatic.showToast(this,"保存成功")
            }else{
                MyStatic.showToast(this,"名称和介绍不能为空")
            }
        }
    }

    private fun setMaxLinesWithFilter(editText: EditText, maxLines: Int) {
        val filter =
            InputFilter { source: CharSequence, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int ->
                // 检查输入的字符是否为回车或换行符
                for (i in start until end) {
                    val character = source[i]
                    if (character == '\n' || character == '\r') {
                        // 获取当前编辑框中已有的行数
                        val lines = editText.lineCount
                        // 如果已有行数达到最大行数，则禁止输入回车符
                        if (lines >= maxLines) {
                            MyStatic.showToast(this,"最多输入${maxLines}行")
                            return@InputFilter ""
                        }
                    }
                }
                null // 允许输入字符
            }

        // 应用过滤器
        editText.filters = arrayOf(filter)
    }
}