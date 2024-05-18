package cn.dreamfruits.yaoguo.module.login

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.bindgender.BindGenderActivity
import cn.dreamfruits.yaoguo.module.main.home.state.FixUserInfoState
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.ProfileState
import cn.dreamfruits.yaoguo.repository.OauthRepository
import cn.dreamfruits.yaoguo.repository.UserRepository
import cn.dreamfruits.yaoguo.util.singleClick
import com.blankj.utilcode.util.ToastUtils
import org.w3c.dom.Text

/**
 * @author Lee
 * @createTime 2023-07-12 18 GMT+8
 * @desc :
 */
class BindInviteCodeActivity : BaseActivity() {

    private lateinit var et_invite_code: EditText
    private lateinit var et_invite_code_bg: TextView
    private lateinit var tv_commit: TextView
    private val mineViewModel by viewModels<MineViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()
    var isPerfect = 0

    companion object {
        @JvmStatic
        fun start(context: Context, isPerfect: Int) {
            val starter = Intent(context, BindInviteCodeActivity::class.java)
                .putExtra("isPerfect", isPerfect)
            context.startActivity(starter)
        }
    }


    override fun layoutResId(): Int = R.layout.activity_bind_invite_code

    override fun initView() {
        findViewById<TextView>(R.id.tv_from).text = "微信搜索公众号\"微酸App\"获取邀请码"
        et_invite_code = findViewById(R.id.et_invite_code)
        et_invite_code_bg = findViewById(R.id.et_invite_code_bg)
        tv_commit = findViewById(R.id.tv_commit)

        et_invite_code.addTextChangedListener(afterTextChanged = { text ->
            if (TextUtils.isEmpty(et_invite_code.text.toString())) {
                et_invite_code_bg.visibility = View.VISIBLE
            } else {
                et_invite_code_bg.visibility = View.GONE
            }
        })


        tv_commit.singleClick {
            var code = et_invite_code.text.toString()

            if (TextUtils.isEmpty(code)) {
                ToastUtils.showShort("请输入邀请码")
                return@singleClick
            }
            mineViewModel.showLoading(mContext)
            mineViewModel.updateUserInfo(inviteCode = code)

        }

        mineViewModel.fixUserInfoState.observe(this)
        {
            when (it) {
                is FixUserInfoState.Success -> {
                    if (mineViewModel.mFixUserInfoBean!!.isPerfect == 1) {
                        loginViewModel.getUserInfo(
                            mineViewModel.mFixUserInfoBean!!.userId!!
                        )
                    } else {
                        val intent = Intent(this, BindGenderActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                is FixUserInfoState.Fail -> {
                    ToastUtils.showShort(it.errorMsg)
                }
            }
        }
        loginViewModel.profileState.observe(this) { state ->
            when (state) {
                is ProfileState.Success -> {
                    loginViewModel.getTencentCDNSecretKey()//wq 请求腾讯sdn
                    loginViewModel.loginIm(this, state.data!!.userId.toString())
                }

                is ProfileState.Fail -> {
                    ToastUtils.showShort("获取用户信息失败，请重试")
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun initData() {
    }

}