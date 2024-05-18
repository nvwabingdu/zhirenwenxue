package cn.dreamfruits.yaoguo.module.editlocation


import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.EditProfileViewModel
import cn.dreamfruits.yaoguo.module.locationdetail.EditLocationDetailActivity
import cn.dreamfruits.yaoguo.module.main.home.state.FixUserInfoState
import cn.dreamfruits.yaoguo.module.main.home.state.GetUserHomePageSettingState
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.view.switchbutton.SwitchButton
import com.blankj.utilcode.util.StringUtils
import com.gyf.immersionbar.ktx.immersionBar

/**
 * 修改地区页面
 */
class EditLocationActivity : BaseActivity() {

    private lateinit var mLocationLayout: RelativeLayout
    private lateinit var mLocation: TextView
    private lateinit var mShowLocation: SwitchButton
    private lateinit var mSave: TextView

    private var mCity: String? = null
    private var mProvince: String? = null
    private var mCountry: String? = null

    private val viewModel by viewModels<EditProfileViewModel>()
    private val mineViewModel by viewModels<MineViewModel>()

    override fun layoutResId(): Int {
        return R.layout.activity_edit_location
    }

    override fun onCreateBefore() {

    }

    override fun initStatus() {
        immersionBar {
            statusBarDarkFont(true)
            fitsSystemWindows(true)
        }

    }


    override fun initView() {

        mLocationLayout = findViewById(R.id.rl_location)
        mLocation = findViewById(R.id.tv_location)
        mShowLocation = findViewById(R.id.sb_show_location)//是否展示地区按钮
        mSave = findViewById(R.id.tv_title_right)

        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            this@EditLocationActivity.finish()
        }

        mLocationLayout.setOnClickListener {
            val intent = Intent(this, EditLocationDetailActivity::class.java)
            startActivityForResult(intent, 101)
        }

        findViewById<TextView>(R.id.tv_title).text = "编辑地区"

        mSave.text = "保存"
        mSave.setOnClickListener {
            /**更改地址*/
            mineViewModel.updateUserInfo(country = mCountry,province = mProvince,city = mCity)
           /* *//**是否显示地址*//*
            mineViewModel.setUserHomePageSetting(
                1,//0:生日，1:地区
                if(mShowLocation.isChecked){1}else{0},//是否显示；0-不展示，1-展示
                null,//当type为0时，此字段必填；0:年龄，1-星座
            )*/
        }

//        if (mShowLocation.isChecked) {
            if (Singleton.getUserInfo().country != null||Singleton.getUserInfo().province != null||Singleton.getUserInfo().city != null) {
                mLocation.text = "${Singleton.getUserInfo().country?: ""} ${Singleton.getUserInfo().province ?: ""} ${Singleton.getUserInfo().city ?: ""}"
            }else{
                mLocation.text = "请选择你所在地区"
            }
//        } else {
//            mLocation.text = ""
//        }

        /**
         * 点击这里也可以设置是否显示地址
         */
        mShowLocation.setOnCheckedChangeListener { view, isChecked ->
            /**是否显示地址*/
            mineViewModel.setUserHomePageSetting(
                1,//0:生日，1:地区
                if(mShowLocation.isChecked){1}else{0},//是否显示；0-不展示，1-展示
                null,//当type为0时，此字段必填；0:年龄，1-星座
            )


            if (mShowLocation.isChecked) {
                if (Singleton.getUserInfo().country != null||Singleton.getUserInfo().province != null||Singleton.getUserInfo().city != null) {
                    mLocation.text = "${Singleton.getUserInfo().country ?: ""} ${Singleton.getUserInfo().province ?: ""} ${Singleton.getUserInfo().city ?: ""}"
                }else{
                    mLocation.text = "请选择你所在地区"
                }
            } else {
                mLocation.text = ""
            }
        }


        /**点击保存之后回调*/
        mineViewModel.fixUserInfoState.observe(this) {
            when (it) {
                is FixUserInfoState.Success -> {
                    Singleton.centerToast(this, "修改成功")
                    if(!isActivityResult){//不是回调内的保存就关闭当前页面
                        finish()
                    }

                    //更新用户信息
                    mineViewModel.getUserInfo(Singleton.getUserInfo().userId)
                }

                is FixUserInfoState.Fail -> {
                    /**如果不能修改  则改回原本的数据*/
                    if (!StringUtils.isEmpty(Singleton.getUserInfo().country)) {
                        mLocation.text = "${Singleton.getUserInfo().country?: ""} ${Singleton.getUserInfo().province ?: ""} ${Singleton.getUserInfo().city ?: ""}"
                    } else {
                        mSave.isEnabled = false
                        mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))
                    }

                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }

                    //更新用户信息
                    mineViewModel.getUserInfo(Singleton.getUserInfo().userId)
                }
            }
        }


        /**请求设置地区的状态*/
        mineViewModel.getUserHomePageSetting()

        /**地区状态回调*/
        mineViewModel.getUserHomePageSettingState.observe(this) {
            when (it) {
                is GetUserHomePageSettingState.Success -> {
                    mineViewModel.mGetUserHomePageSettingBean!!.list.forEach{
                        if (it.type==1){
                            /**是否展示地区按钮*/
                            mShowLocation.isChecked = it.isShow==1
                        }
                    }
                }
                is GetUserHomePageSettingState.Fail -> {
                    if (it.errorMsg!=null&&it.errorMsg!=""){
                        Singleton.centerToast(this, it.errorMsg.toString())
                    }else{
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }
                }
            }
        }

    }

    override fun initData() {
        if (!StringUtils.isEmpty(Singleton.getUserInfo().country)) {
            mLocation.text = "${Singleton.getUserInfo().country?: ""} ${Singleton.getUserInfo().province ?: ""} ${Singleton.getUserInfo().city ?: ""}"
        } else {
            mSave.isEnabled = false
            mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))
        }
    }


   var isActivityResult = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == 200) {
            mCity = data?.getStringExtra("key_city")
            mProvince = data?.getStringExtra("key_province")
            mCountry = data?.getStringExtra("key_country")

            mLocation.text = "$mCountry ${mProvince ?: ""} ${mCity ?: ""}"


            Log.e("wq2121","mLocation.text=${mLocation.text}")

            isActivityResult = true//表示回调接口已经执行了   在请求数据之后就不关闭页面

            mSave.isEnabled = true
            mSave.setTextColor(resources.getColor(R.color.black_222222))
            //选择后不点击保存按钮
            /**更改地址*/
            mineViewModel.updateUserInfo(country = mCountry,province = mProvince,city = mCity)//wq
        }
    }

}