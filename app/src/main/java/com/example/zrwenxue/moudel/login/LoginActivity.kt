package com.example.zrwenxue.moudel.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newzr.R
import com.example.zrtool.ui.custom.TitleBarView
import com.example.zrtool.utils.ToastUtils
import com.example.zrwenxue.MainActivity
import com.example.zrwenxue.moudel.BaseActivity


class LoginActivity : BaseActivity() {

    override fun layoutResId(): Int = R.layout.activity_login

//     lateinit var viewmodel: LoginViewmodel
//     lateinit var viewmodel2: LoginViewmodel


    override fun init() {
        setTopView("登录")
        setLoginLogic()

//          viewmodel=ViewModelProvider(this).get(LoginViewmodel::class.java)
//          viewmodel.sum=0
//
//          //构造传参的方式
//          viewmodel2=ViewModelProvider(this,LoginViewModelFactory(9)).get(LoginViewmodel::class.java)



    }

    /**
     * 通过此种方式构造传参
     */
    class LoginViewModelFactory(private val sum: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(0) as T
        }
    }

    /**
     * 登录页面逻辑
     */
    private var registerUsername: EditText? = null
    private var registerClear: ImageView? = null

    private var registerMotto: EditText? = null
    private var registerMottoClear: ImageView? = null


//    private var registerPwd: EditText? = null
//    private var registerKey: CheckBox? = null
    private var registerSub: TextView? = null
    private var sharedPreferences: SharedPreferences? = null
   private fun  setLoginLogic(){
       sharedPreferences = getSharedPreferences("MyUserInfo", MODE_PRIVATE)

       //用户名
       registerUsername = findViewById(R.id.registerUsername)
       registerUsername!!.setOnKeyListener { v, keyCode, event ->
           keyCode == KeyEvent.KEYCODE_DEL && registerUsername!!.text.isEmpty()
       }

       registerClear = findViewById(R.id.registerClear)
       registerClear!!.setOnClickListener {
           registerUsername!!.setText("")
       }



       //座右铭
       registerMotto = findViewById(R.id.registerMotto)
       registerMotto!!.setOnKeyListener { v, keyCode, event ->
           keyCode == KeyEvent.KEYCODE_DEL && registerMotto!!.text.isEmpty()
       }

       registerMottoClear = findViewById(R.id.registerMottoClear)
       registerMottoClear!!.setOnClickListener {
           registerMotto!!.setText("")
       }


       //密码
//       registerPwd = findViewById(com.example.zrword.R.id.registerPwd)
//       registerPwd!!.setOnKeyListener { v, keyCode, event ->
//           keyCode == KeyEvent.KEYCODE_DEL && registerPwd!!.text.isEmpty()
//       }
//       registerKey = findViewById(com.example.zrword.R.id.registerKey)
//
//       registerKey!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//           if (isChecked) {
//               // 显示明文
//               registerPwd!!.transformationMethod = null
//           } else {
//               // 显示密文（星号）
//               registerPwd!!.transformationMethod = PasswordTransformationMethod()
//           }
//       })


       registerSub = findViewById(R.id.registerSub)

       registerSub!!.setOnClickListener {

           if (registerUsername!!.text.toString().isNotEmpty()&&
                   registerMotto!!.text.toString().isNotEmpty()){
               // 保存账号密码到共享参数
               val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
               editor.putString("username", registerUsername!!.text.toString())
               editor.putString("usermotto", registerMotto!!.text.toString())
               editor.apply()

               //跳转主页
               MainActivity.startAc(this,"","")
           }else{
               ToastUtils.showCenterToast(this,"用户名或座右铭，不能为空")
           }
       }
    }







    /**
     * 设置顶部
     */
    private var topView: TitleBarView? = null
    private fun setTopView(title: String) {
        topView = findViewById(R.id.title_view)
        topView!!.title = title
        //左边返回
        topView!!.setOnclickLeft(
            View.VISIBLE,
            View.OnClickListener {
                com.example.zrtool.utils.LogUtils.e("dada","我点击了标题")
                MainActivity.startAc(this, "", "")
                finish()
            })
        //右边点击事件
        topView!!.setOnclickRight(
            View.INVISIBLE,
            View.OnClickListener {

            }
        )
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
    override fun isSupportSwipeBack(): Boolean =false
    /**
     * 本页面屏蔽返回键
     */
    override fun onBackPressed() {
        return
    }
}