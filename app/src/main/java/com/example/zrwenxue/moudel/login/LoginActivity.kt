package com.example.zrwenxue.moudel.login

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.blankj.utilcode.util.ToastUtils
import com.example.newzr.R
import com.example.zrwenxue.MainActivity
import com.example.zrwenxue.app.Single
import com.example.zrwenxue.moudel.BaseActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException


class LoginActivity : BaseActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button


    override fun layoutResId(): Int = R.layout.activity_login


    override fun init() {
        // 找到 UI 元素
        nameEditText = findViewById(R.id.name_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)

        // 检查是否已经保存了用户信息
        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val savedName = sharedPreferences.getString("name", null)
        val savedPassword = sharedPreferences.getString("password", null)

        // 如果已经保存了用户信息,直接跳转到 MainActivity
        if (savedName != null && savedPassword != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        val blockCharacterSet = " \n" // 定义要阻止输入的字符集合

        /**禁止输入换行和空格*/
        nameEditText.addTextChangedListener(object : TextWatcher {
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
                    nameEditText.setText(newText)
                    nameEditText.setSelection(start) // 设置光标位置
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })



        passwordEditText.addTextChangedListener(object : TextWatcher {
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
                    passwordEditText.setText(newText)
                    passwordEditText.setSelection(start) // 设置光标位置
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })


        // 设置登录按钮的点击事件
        loginButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val password = passwordEditText.text.toString()



            if (name != "" && password != "") {
                if (
                    name.contains(">")
                    || name.contains("<")
                    || name.contains("@")
                    || name.contains("/")
                    || name.contains("#")
                    || name.contains("|")
                ) {
                    ToastUtils.showShort("昵称包含</|>@#等特殊字符,请重新输入")
                    return@setOnClickListener
                }

                if (
                    password.contains(">")
                    || password.contains("<")
                    || password.contains("@")
                    || password.contains("/")
                    || password.contains("#")
                    || password.contains("|")
                ) {
                    ToastUtils.showShort("密码包含</|>@#等特殊字符,请重新输入")
                    return@setOnClickListener
                }


                // 保存用户信息到共享参数
                val editor = sharedPreferences.edit()
                editor.putString("name", name)
                editor.putString("password", password)
                editor.putString("time", System.currentTimeMillis().toString())
                editor.putString("balance", "0")
                editor.apply()


                // 跳转到 MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Single.centerToast(this, "输入不能为空，请重新输入")
            }
        }
    }


    /**
     * 需要跳转到本页面的时候调用此方法
     */
    companion object {
        fun startAc(mContent: Context, data1: String, data2: String) {
            //灵活运用apply
            val intent = Intent(mContent, LoginActivity::class.java).apply {
                putExtra("param1", data1)
                putExtra("param1", data2)
            }
            mContent.startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            var type = intent.getIntExtra("param1", -1)
            var position = intent.getIntExtra("param1", 0)
        }
    }

    /**
     * 本页面不需要左滑返回
     */
    override fun isSupportSwipeBack(): Boolean = false

    /**
     * 本页面屏蔽返回键
     */
    override fun onBackPressed() {
        return
    }


    fun getNetTime() {
        //获取网络时间戳
        //创建 OkHttpClient
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        // 定义请求
        val request = Request.Builder()
            .url("http://worldtimeapi.org/api/ip")
            .build()

        // 发送请求
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 处理错误
                println("Error: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val timestamp = response.body?.string()?.let { parseTimestamp(it) }
                    // 使用获取的时间戳
                    Log.e("TAG456456","Timestamp: $timestamp")




                } else {
                    // 处理错误
                    Log.e("TAG456456","Error: ${response.code} - ${response.message}")
                }
            }
        })
    }


    // 解析 JSON 响应
    private fun parseTimestamp(json: String): Long {
        val jsonObject = JSONObject(json)
        return jsonObject.getLong("unixtime")
    }


}

//

//
////     lateinit var viewmodel: LoginViewmodel
////     lateinit var viewmodel2: LoginViewmodel
//
//
//    override fun init() {
//        setTopView("登录")
//        setLoginLogic()
//
////          viewmodel=ViewModelProvider(this).get(LoginViewmodel::class.java)
////          viewmodel.sum=0
////
////          //构造传参的方式
////          viewmodel2=ViewModelProvider(this,LoginViewModelFactory(9)).get(LoginViewmodel::class.java)
//
//
//
//    }
//
//    /**
//     * 通过此种方式构造传参
//     */
//    class LoginViewModelFactory(private val sum: Int) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return LoginViewModel(0) as T
//        }
//    }
//
//    /**
//     * 登录页面逻辑
//     */
//    private var registerUsername: EditText? = null
//    private var registerClear: ImageView? = null
//
//    private var registerMotto: EditText? = null
//    private var registerMottoClear: ImageView? = null
//
//
////    private var registerPwd: EditText? = null
////    private var registerKey: CheckBox? = null
//    private var registerSub: TextView? = null
//    private var sharedPreferences: SharedPreferences? = null
//   private fun  setLoginLogic(){
//       sharedPreferences = getSharedPreferences("MyUserInfo", MODE_PRIVATE)
//
//       //用户名
//       registerUsername = findViewById(R.id.registerUsername)
//       registerUsername!!.setOnKeyListener { v, keyCode, event ->
//           keyCode == KeyEvent.KEYCODE_DEL && registerUsername!!.text.isEmpty()
//       }
//
//       registerClear = findViewById(R.id.registerClear)
//       registerClear!!.setOnClickListener {
//           registerUsername!!.setText("")
//       }
//
//
//
//       //座右铭
//       registerMotto = findViewById(R.id.registerMotto)
//       registerMotto!!.setOnKeyListener { v, keyCode, event ->
//           keyCode == KeyEvent.KEYCODE_DEL && registerMotto!!.text.isEmpty()
//       }
//
//       registerMottoClear = findViewById(R.id.registerMottoClear)
//       registerMottoClear!!.setOnClickListener {
//           registerMotto!!.setText("")
//       }
//
//
//       //密码
////       registerPwd = findViewById(com.example.zrword.R.id.registerPwd)
////       registerPwd!!.setOnKeyListener { v, keyCode, event ->
////           keyCode == KeyEvent.KEYCODE_DEL && registerPwd!!.text.isEmpty()
////       }
////       registerKey = findViewById(com.example.zrword.R.id.registerKey)
////
////       registerKey!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
////           if (isChecked) {
////               // 显示明文
////               registerPwd!!.transformationMethod = null
////           } else {
////               // 显示密文（星号）
////               registerPwd!!.transformationMethod = PasswordTransformationMethod()
////           }
////       })
//
//
//       registerSub = findViewById(R.id.registerSub)
//
//       registerSub!!.setOnClickListener {
//
//           if (registerUsername!!.text.toString().isNotEmpty()&&
//                   registerMotto!!.text.toString().isNotEmpty()){
//               // 保存账号密码到共享参数
//               val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
//               editor.putString("username", registerUsername!!.text.toString())
//               editor.putString("usermotto", registerMotto!!.text.toString())
//               editor.apply()
//
//               //跳转主页
//               MainActivity.startAc(this,"","")
//           }else{
//               ToastUtils.showCenterToast(this,"用户名或座右铭，不能为空")
//           }
//       }
//    }
//
//
//
//
//
//
//
//    /**
//     * 设置顶部
//     */
//    private var topView: TitleBarView? = null
//    private fun setTopView(title: String) {
//        topView = findViewById(R.id.title_view)
//        topView!!.title = title
//        //左边返回
//        topView!!.setOnclickLeft(
//            View.VISIBLE,
//            View.OnClickListener {
//                com.example.zrtool.utils.LogUtils.e("dada","我点击了标题")
//                MainActivity.startAc(this, "", "")
//                finish()
//            })
//        //右边点击事件
//        topView!!.setOnclickRight(
//            View.INVISIBLE,
//            View.OnClickListener {
//
//            }
//        )
//    }
//
//

