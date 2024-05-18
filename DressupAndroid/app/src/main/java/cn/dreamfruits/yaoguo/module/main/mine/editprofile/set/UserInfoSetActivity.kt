package cn.dreamfruits.yaoguo.module.main.mine.editprofile.set

import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.login.LoginViewModel
import cn.dreamfruits.yaoguo.module.main.message.BlackListActivity
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.set.notification.SetInnerActivity
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.gyf.immersionbar.ktx.immersionBar

/**
 * @Author qiwangi
 * @Date 2023/6/29
 * @TIME 19:32
 */
class UserInfoSetActivity : BaseActivity() {
    override fun initData() {}
    override fun layoutResId(): Int = R.layout.activity_userinfo_set

    /**
     * 初始化状态栏
     */
    override fun initStatus() {
        val params: WindowManager.LayoutParams = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.attributes = params
        immersionBar {
            statusBarDarkFont(true)
            fitsSystemWindows(false)
        }
        //沉浸状态栏重叠问题
        findViewById<View>(R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BarUtils.getStatusBarHeight()
            )
    }

    /**
     * 初始化view
     */
    override fun initView() {
        set()
    }


    /**
     * 设置
     */
    private val loginViewModel by viewModels<LoginViewModel>()//公共的viewmodel


    private lateinit var llSet: LinearLayout
    private fun set() {
        //显示设置布局
        llSet = findViewById(R.id.ll_set);
        llSet.visibility = View.VISIBLE

        //返回
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        //账号安全
        findViewById<RelativeLayout>(R.id.rl_account_safe).setOnClickListener {
            val intent = Intent(this, SetInnerActivity::class.java)
            intent.putExtra("type", 0)
            intent.putExtra("title", "账号安全")
            startActivity(intent)
        }
        //通知设置
        findViewById<RelativeLayout>(R.id.rl_notification_set).setOnClickListener {
            val intent = Intent(this, SetInnerActivity::class.java)
            intent.putExtra("type", 1)
            intent.putExtra("title", "通知设置")
            startActivity(intent)
        }
        //意见反馈
        findViewById<RelativeLayout>(R.id.rl_feedback_set).setOnClickListener {
            val intent = Intent(this, SetInnerActivity::class.java)
            intent.putExtra("type", 2)
            intent.putExtra("title", "意见反馈")
            startActivity(intent)
        }
        //关于
        findViewById<RelativeLayout>(R.id.rl_about_set).setOnClickListener {
            val intent = Intent(this, SetInnerActivity::class.java)
            intent.putExtra("type", 3)
            intent.putExtra("title", "关于")
            startActivity(intent)
        }
        //拉黑
        findViewById<RelativeLayout>(R.id.rl_set_black_set).setOnClickListener {
//            val intent = Intent(this, SetInnerActivity::class.java)
//            intent.putExtra("type", 4)
//            intent.putExtra("title","黑名单")
//            startActivity(intent)

            BlackListActivity.start(this@UserInfoSetActivity)

        }
        //退出账号
        findViewById<TextView>(R.id.tv_exit_logout).setOnClickListener {
            logout()
        }
    }

    /**
     * 退出登录
     */
    private fun logout() {
        showLogoutPop()
    }

    /**
     * 显示退出登录弹窗
     */
    private fun showLogoutPop() {
        val inflate = LayoutInflater.from(this).inflate(R.layout.home_dialog_ok_cancel, null)
        val tips = inflate.findViewById<TextView>(R.id.tips)
        tips.visibility = View.VISIBLE
        tips.text = "退出登录"
        val content = inflate.findViewById<TextView>(R.id.content)
        content.text = "确定退出当前账号？"

        val confirm = inflate.findViewById<TextView>(R.id.confirm)
//        confirm.text="确定"
        val cancel = inflate.findViewById<TextView>(R.id.cancel)
//        cancel.text="取消"

        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        //点击确定
        confirm.setOnClickListener {
            Singleton.isMine=0
            mPopupWindow.dismiss()
            //退出登录
            loginViewModel.logout()
        }

        //点击取消
        cancel.setOnClickListener {
            mPopupWindow.dismiss()
        }

        //动画
        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.CENTER, 0, 0)
            Singleton.bgAlpha(this, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                Singleton.bgAlpha(this, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        Singleton.lifeCycleSet(this, mPopupWindow)
    }

}