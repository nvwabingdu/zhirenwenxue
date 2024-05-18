package cn.dreamfruits.yaoguo.module.bindgender

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.ProfileConstants
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.module.login.LoginViewModel
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.EditProfileViewModel
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.ProfileState
import cn.dreamfruits.yaoguo.module.main.MainActivity
import cn.dreamfruits.yaoguo.repository.UserRepository
import cn.dreamfruits.yaoguo.util.LiveDataBus
import cn.dreamfruits.yaoguo.view.dialog.commAlertDialog
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ktx.immersionBar


/**
 * 绑定性别页面
 */
class BindGenderActivity : BaseActivity() {


    private lateinit var mMaleIv: ImageView
    private lateinit var mMale: TextView
    private lateinit var mFemaleIv: ImageView
    private lateinit var mFemale: TextView
    private lateinit var mNextTv: TextView

    private var mSelectGender = -1

    override fun layoutResId() = R.layout.activity_bind_gender

    private val viewModel by viewModels<EditProfileViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    private var userId = -1L

    //类型 type = ”bind“ 第一次选择性别 || type = ”update“更新性别
    private var type = "bind"

    //是否显示过对话框
    private var isShowDialog: Boolean = false

    override fun initStatus() {
        super.initStatus()
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

    override fun initView() {
        mMaleIv = findViewById(R.id.iv_male)
        mMale = findViewById(R.id.tv_male)
        mFemaleIv = findViewById(R.id.iv_female)
        mFemale = findViewById(R.id.tv_female)
        mNextTv = findViewById(R.id.tv_next)


        mMaleIv.setOnClickListener {
            switch(1)
        }

        mFemaleIv.setOnClickListener {
            switch(0)
        }

        mNextTv.setOnClickListener {
//            println("==========================================")
//            println(type)
//            println(mSelectGender)
//            println(UserRepository.userInfo!!.gender)
            switchGender(mSelectGender)
        }

    }


    private fun switchGender(gender: Int) {
        Log.e("BindGenderActivity", "switchGender: $gender")
        Log.e("BindGenderActivity", "type: $type")
        if (StringUtils.equals(type, "update")) {
            if (UserRepository.userInfo!!.gender != gender) {
                commAlertDialog(supportFragmentManager) {
                    content =
                        this@BindGenderActivity.resources.getString(R.string.switch_gender_alert)
                    confirmText = "取消"
                    cancelText = "确认切换"
                    cancelCallback = {
                        viewModel.updateGender(mSelectGender)
                        finish()
                    }
                    confirmCallback = {
                        isShowDialog = false
                    }
                }
            } else {
                finish()
            }
            return
        } else {
            switch(gender)
            viewModel.showLoading(mContext)
            viewModel.updateGender(mSelectGender)

        }
    }


    /**
     * 设置选中状态
     */
    private fun switch(gender: Int) {
        if (gender == 0) {
            mSelectGender = 0
            mFemaleIv.setBackgroundResource(R.drawable.ic_female_selected)
//            mFemale.setTextColor(resources.getColor(R.color.pink_F85A5A))
            mFemale.setTextColor(Color.parseColor("#000000"))

            mMaleIv.setBackgroundResource(R.drawable.ic_male_no_select)
            mMale.setTextColor(resources.getColor(R.color.color_7F7F7F))

            mNextTv.isEnabled = true
            mNextTv.setBackgroundResource(R.drawable.bg_black)

        } else if (gender == 1) {

            mSelectGender = 1
            mMaleIv.setBackgroundResource(R.drawable.ic_male_selected)
//            mMale.setTextColor(resources.getColor(R.color.blue_67B2F7))
            mMale.setTextColor(Color.parseColor("#000000"))

            mFemaleIv.setBackgroundResource(R.drawable.ic_female_no_select)
            mFemale.setTextColor(resources.getColor(R.color.color_7F7F7F))



            mNextTv.isEnabled = true
            mNextTv.setBackgroundResource(R.drawable.bg_black)
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun initData() {
        userId = intent.getLongExtra(RouterConstants.BindGender.KEY_USER_ID, -1)

        type = intent.getStringExtra(RouterConstants.BindGender.KEY_UPDATE_GENDER) ?: "bind"

        if (StringUtils.equals(type, "update")) {
            mNextTv.text = "确定"
            mNextTv.isEnabled = true
            mNextTv.setBackgroundResource(R.drawable.bg_black)

            UserRepository.userInfo?.let { userInfo ->
                switch(userInfo.gender!!)
            }
        }

        viewModel.editProfileState.observe(this) { state ->
            when (state) {
                is ProfileState.Success -> {
                    Log.e("BindGenderActivity", "initData: ${state.data}+++type:${type}")
                    if (StringUtils.equals(type, "bind")) {

                        //缓存用户数据
                        UserRepository.userInfo = state.data
                        loginViewModel.loginIm(mContext, state.data!!.userId.toString())
//                        //跳转首页
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//
//                        ActivityUtils.finishToActivity(MainActivity::class.java, false)

                    } else {
                        UserRepository.userInfo = state.data
                        LiveDataBus.get()
                            .with(ProfileConstants.KEY_PROFILE_CHANGE)
                            .value = ProfileConstants.GENDER
                        this@BindGenderActivity.finish()
                    }
                }

                is ProfileState.Fail -> {
                    ToastUtils.showShort("绑定性别失败")
                }
            }
        }
    }
}