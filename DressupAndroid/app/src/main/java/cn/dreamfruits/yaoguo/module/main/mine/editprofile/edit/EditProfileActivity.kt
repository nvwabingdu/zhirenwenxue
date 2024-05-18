package cn.dreamfruits.yaoguo.module.main.mine.editprofile.edit

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.module.bindgender.BindGenderActivity
import cn.dreamfruits.yaoguo.module.editlocation.EditLocationActivity
import cn.dreamfruits.yaoguo.module.main.home.state.GetUserInfoState
import cn.dreamfruits.yaoguo.module.main.mine.IconCropActivity
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * 编辑资料页面
 */
class EditProfileActivity : BaseActivity() {
    private val mineViewModel by viewModels<MineViewModel>()
    private lateinit var mAvatar: ImageView
    private lateinit var mNicknameLayout: RelativeLayout
    private lateinit var mDescriptionLayout: RelativeLayout
    private lateinit var mNewIdLayout: RelativeLayout
    private lateinit var mBirthdayLayout: RelativeLayout
    private lateinit var mEditAddressLayout: RelativeLayout
    private lateinit var mEditGenderLayout: RelativeLayout

    private lateinit var mNickname: TextView
    private lateinit var mDescription: TextView
    private lateinit var mNewId: TextView
    private lateinit var mGender: TextView
    private lateinit var mBirthday: TextView
    private lateinit var mEditAddress: TextView

    override fun layoutResId(): Int {
        return R.layout.activity_edit_profile
    }

    private fun initStatusBar(){
        //沉浸状态栏重叠问题
        findViewById<View>(R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight())
    }

    override fun initView() {
        initStatusBar()
        mAvatar = findViewById(R.id.iv_avatar)
        mAvatar.setOnClickListener {
            val intent = Intent(this, IconCropActivity::class.java)
            intent.putExtra("iconUrl",Singleton.UserAvtarUrl)
            startActivity(intent)
        }
        mNicknameLayout = findViewById(R.id.ll_edit_nick_name)
        mDescriptionLayout = findViewById(R.id.rl_edit_description)
        mNewIdLayout = findViewById(R.id.rl_edit_new_id)
        mBirthdayLayout = findViewById(R.id.rl_edit_birthday)
        mEditAddressLayout = findViewById(R.id.rl_edit_address)
        mEditGenderLayout = findViewById(R.id.edit_gender_layout)

        mNickname = findViewById(R.id.tv_nick_name)
        mDescription = findViewById(R.id.tv_description)
        mNewId = findViewById(R.id.tv_id_number)
        mGender = findViewById(R.id.tv_gender)
        mBirthday = findViewById(R.id.tv_birthday)
        mEditAddress = findViewById(R.id.tv_address)


        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        mNicknameLayout.setOnClickListener {
            startAc(0)
        }

        mDescriptionLayout.setOnClickListener {
            startAc(1)
        }

        mNewIdLayout.setOnClickListener {
            startAc(2)
        }

        mEditGenderLayout.setOnClickListener {
            startAc(3)
        }

        mBirthdayLayout.setOnClickListener {
            startAc(4)
        }

        mEditAddressLayout.setOnClickListener {
            startAc(5)
        }

    }

    override fun onResume() {
        super.onResume()
        /**头像*/
        Glide.with(this)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load( Singleton.UserAvtarUrl)
            .into(mAvatar)

        mineViewModel.getUserInfo(Singleton.getUserInfo().userId)
        /**请求回调*/
        mineViewModel.getUserInfoState.observe(this) {
            when (it) {
                is GetUserInfoState.Success -> {
                    initProfile()
                }
                is GetUserInfoState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    initProfile()
                }
            }
        }

    }
    private fun startAc(tag :Int){
        when(tag){
            0->{
                val intent = Intent(this, EditUserInfoActivity::class.java)
                intent.putExtra("title","编辑名字")
                intent.putExtra("tag",0)
                startActivity(intent)
            }
            1->{
//                val intent = Intent(this, EditDescriptionActivity::class.java)
                val intent = Intent(this, EditUserInfoActivity::class.java)
                intent.putExtra("title","编辑签名")
                intent.putExtra("tag",1)
                startActivity(intent)
            }
            2->{
//                val intent = Intent(this, EditNewIdActivity::class.java)
                val intent = Intent(this, EditUserInfoActivity::class.java)
                intent.putExtra("title","编辑微酸号")
                intent.putExtra("tag",2)
                startActivity(intent)
            }
            3->{
                val intent = Intent(this,BindGenderActivity::class.java)
                intent.putExtra(RouterConstants.BindGender.KEY_UPDATE_GENDER,"update")
                intent.putExtra("tag",3)
                startActivity(intent)
            }
            4->{
//                val intent = Intent(this, EditNewBirthDayActivity::class.java)
                val intent = Intent(this, EditUserInfoActivity::class.java)
                intent.putExtra("title","编辑生日")
                intent.putExtra("tag",4)
                startActivity(intent)
            }
            5->{
                val intent = Intent(this, EditLocationActivity::class.java)
                intent.putExtra("title","编辑地区")
                intent.putExtra("tag",5)
                startActivity(intent)
            }
        }
    }

    override fun initData() {

    }


    /**
     * 初始化个人资料
     */
    private fun initProfile() {

        /**头像*/
        Glide.with(this)
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load( Singleton.UserAvtarUrl)
            .into(mAvatar)

        /**昵称*/
        mNickname.text = Singleton.getUserInfo().nickName

        /**简介*/
        if (Singleton.getUserInfo().descript!=null) {
            mDescription.text =Singleton.getUserInfo().descript
            mDescription.setTextColor(resources.getColor(R.color.black_222222))
        } else {
            mDescription.text = "有趣的简介可以吸引粉丝"
            mDescription.setTextColor(resources.getColor(R.color.gray_B3B3B3))
        }

        /**腰果号*/
        mNewId.text = Singleton.getUserInfo().ygId

        /**设置性别*/
        mGender.text = if (Singleton.getUserInfo().gender == 0) "女" else "男"

        /**设置生日*/
        if (Singleton.getUserInfo().birthday!=null) {
            mBirthday.text = Singleton.getUserInfo().birthday
            mBirthday.setTextColor(resources.getColor(R.color.black_222222))
        } else {
            mBirthday.text = "选择生日"
            mBirthday.setTextColor(resources.getColor(R.color.gray_B3B3B3))
        }

        /**设置地区*/
        if (Singleton.getUserInfo().country!=null) {
            mEditAddress.text = "${Singleton.getUserInfo().country?:""} ${Singleton.getUserInfo().province ?: ""} ${Singleton.getUserInfo().city ?: ""}"
            mEditAddress.setTextColor(resources.getColor(R.color.black_222222))
        } else {
            mEditAddress.text = "请选择你所在的地区"
            mEditAddress.setTextColor(resources.getColor(R.color.gray_B3B3B3))
        }

    }

}