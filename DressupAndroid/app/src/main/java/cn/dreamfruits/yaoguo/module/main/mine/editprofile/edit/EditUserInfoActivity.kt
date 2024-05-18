package cn.dreamfruits.yaoguo.module.main.mine.editprofile.edit

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.ProfileConstants
import cn.dreamfruits.yaoguo.module.main.home.state.FixUserInfoState
import cn.dreamfruits.yaoguo.module.main.home.state.GetUserHomePageSettingState
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.EditProfileViewModel
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.ProfileState
import cn.dreamfruits.yaoguo.repository.UserRepository
import cn.dreamfruits.yaoguo.util.LiveDataBus
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.view.switchbutton.SwitchButton
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ktx.immersionBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


/**
 * 设置页面 修改昵称 地址 签名 生日等
 */
class EditUserInfoActivity : BaseActivity() {
    //公用部分
    private lateinit var mTitle: TextView
    private lateinit var mSave: TextView
    private val editProfileViewModel by viewModels<EditProfileViewModel>()
    private val mineViewModel by viewModels<MineViewModel>()

    override fun initData() {}
    override fun initStatus() {
        immersionBar {
            statusBarDarkFont(true)
            fitsSystemWindows(false)
        }
        //沉浸状态栏重叠问题
        findViewById<View>(R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight())
    }

    override fun layoutResId(): Int {
        return R.layout.activity_edit_new_name
    }

    override fun initView() {
        mTitle = findViewById(R.id.tv_title)
        mSave = findViewById(R.id.tv_title_right)
        mTitle.text = intent.getStringExtra("title")
        mSave.text = "保存"

        when(intent.getIntExtra("tag",0)){
            0->{//修改昵称
                setName()
            }
            1->{//修改签名
                setDescription()
            }
            2->{//修改微酸号
                setNewId()
            }
            3->{//修改性别
                setGender()
            }
            4->{//修改生日
                setBirthday()
            }
            5->{//修改地址
                setAddress()
            }
        }

    }


    //修改昵称
    private lateinit var mNickname: EditText
    private lateinit var mNickNameCount: TextView
    private fun setName(){
        findViewById<View>(R.id.ll_1).visibility= View.VISIBLE

        mNickname = findViewById(R.id.et_nickname)
        mNickname.setText(Singleton.getUserInfo().nickName)
        mNickNameCount = findViewById(R.id.tv_nickname_count)
        mNickNameCount.text = "${mNickname.text.length}/20"
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
        mNickname.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SPACE) && event.action == KeyEvent.ACTION_DOWN) {
                // 拦截换行符和空格输入
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        mSave.isEnabled = false
        mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))

        mSave.setOnClickListener {
            println("-------------------------------------------------")
            println()
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
            mineViewModel.updateUserInfo(nickName = mNickname.text.toString())
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

                //如果输入的昵称一样 则不可保存
                if (StringUtils.equals(s, Singleton.getUserInfo().nickName) || s?.length == 0) {
                    mSave.isEnabled = false
                    mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))
                } else {
                    mSave.isEnabled = true
                    mSave.setTextColor(resources.getColor(R.color.black_222222))
                }
            }
        })



        /**点击保存之后回调*/
        mineViewModel.fixUserInfoState.observe(this) {
            when (it) {
                is FixUserInfoState.Success -> {
                    Singleton.centerToast(this, "修改成功")
                    finish()
                }

                is FixUserInfoState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }

                }
            }
        }

        editProfileViewModel.editProfileState.observe(this){ state ->
            when(state){
                is ProfileState.Success ->{
                    UserRepository.userInfo = state.data

                    LiveDataBus.get()
                        .with(ProfileConstants.KEY_PROFILE_CHANGE,String::class.java)
                        .value = ProfileConstants.NICKNAME
                    this@EditUserInfoActivity.finish()
                }

                is ProfileState.Fail ->{
                    ToastUtils.showShort("修改失败")
                }
            }
        }
    }

    private var isFirstLine = true
    //修改签名
    private lateinit var mDescription: EditText
    private lateinit var mDescriptionCount: TextView
    private fun setDescription(){
        //编辑签名布局可见
        findViewById<View>(R.id.ll_3).visibility= View.VISIBLE

        mTitle = findViewById(R.id.tv_title)
        mSave = findViewById(R.id.tv_title_right)
        mDescription = findViewById(R.id.et_description)
        mDescription.setText(Singleton.getUserInfo().descript.toString())
        mDescriptionCount = findViewById(R.id.tv_description_count)
        mDescriptionCount.text = "${mDescription.text.length}"

        mTitle.text = "编辑签名"
        mSave.text = "保存"
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
        mSave.isEnabled = false
        mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))


        mDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 在文本改变之前执行的操作
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mDescriptionCount.text = "${s?.length}"

                //如果输入的昵称一样 则不可保存
                if (StringUtils.equals(s, Singleton.getUserInfo().descript.toString()) || s?.length == 0) {
                    mSave.isEnabled = false
                    mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))
                } else {
                    mSave.isEnabled = true
                    mSave.setTextColor(resources.getColor(R.color.black_222222))
                }
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
        val maxLines = 4
        setMaxLinesWithFilter(mDescription, maxLines)



        mSave.setOnClickListener {
            mineViewModel.updateUserInfo(description = mDescription.text.toString())
        }

        /**点击保存之后回调*/
        mineViewModel.fixUserInfoState.observe(this) {
            when (it) {
                is FixUserInfoState.Success -> {
                    finish()
                }

                is FixUserInfoState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }

                }
            }
        }

        editProfileViewModel.editProfileState.observe(this){ state ->
            when(state){
                is ProfileState.Success ->{
                    UserRepository.userInfo = state.data
                    LiveDataBus.get()
                        .with(ProfileConstants.KEY_PROFILE_CHANGE,String::class.java)
                        .value = ProfileConstants.DESCRIPTION
                    finish()
                }

                is ProfileState.Fail ->{
                    ToastUtils.showShort("修改失败")
                }
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
                            Singleton.centerToast(this, "最多输入${maxLines}行")
                            return@InputFilter ""
                        }
                    }
                }
                null // 允许输入字符
            }

        // 应用过滤器
        editText.filters = arrayOf(filter)
    }



    private lateinit var mEditId: EditText
    private lateinit var mIdCount: TextView
    private val mMaxIdCount = 17
    //修改微酸号
    private fun  setNewId(){
        //修改微酸号布局可见
        findViewById<View>(R.id.ll_2).visibility= View.VISIBLE

        mEditId = findViewById(R.id.et_edit_id)
        mIdCount = findViewById(R.id.tv_id_count)

        mTitle.text = "编辑微酸号"
        mSave.text = "保存"
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        mEditId.setText(Singleton.getUserInfo().ygId)
        mIdCount.text = "${mEditId.text.length}/$mMaxIdCount"

        mSave.isEnabled = false
        mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))

        mEditId.addTextChangedListener { text ->

            text?.let {
                mIdCount.text = "${it.length}/$mMaxIdCount"

                if (it.length in 6..mMaxIdCount) {
                    mSave.isEnabled = true
                    mSave.setTextColor(resources.getColor(R.color.black_222222))
                } else {
                    mSave.isEnabled = false
                    mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))
                }
            }
        }

        mSave.setOnClickListener {
            val regex = "[a-zA-Z0-9_]*".toRegex()

//            if (mEditId.text.length < 6){
//                ToastUtils.showShort("微酸号长度为6-15位")
//                return@setOnClickListener
//            }
            if (!mEditId.text.matches(regex)){
                ToastUtils.showShort("只能使用大小写字母、数字、下划线")
                return@setOnClickListener
            }

            /**
             * 修改微酸号
             */
            mineViewModel.updateUserInfo(ygId = mEditId.text.toString())
        }


        /**点击保存之后回调*/
        mineViewModel.fixUserInfoState.observe(this) {
            when (it) {
                is FixUserInfoState.Success -> {
                    Singleton.centerToast(this, "修改成功")
                    finish()
                }

                is FixUserInfoState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }

                }
            }
        }

        //是否可以修改微酸号 0可以   1不可以
        if(Singleton.getUserInfo().canModifyYgId==0L){
            mEditId.isEnabled = true
            mSave.setTextColor(resources.getColor(R.color.black_222222))
        }else{
            mEditId.isEnabled = false
            mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))
        }

//        UserRepository.userInfo?.let { userInfo ->
//            mEditId.setText(userInfo.ygId)
//            if (userInfo.canModifyYgId != 1L){
//                mEditId.isEnabled = true
//
//
//            }
//        }

//        editProfileViewModel.editProfileState.observe(this){ state ->
//            when(state){
//                is ProfileState.Success ->{
//                    //缓存数据
//                    UserRepository.userInfo = state.data
//                    LiveDataBus.get()
//                        .with(ProfileConstants.KEY_PROFILE_CHANGE,String::class.java)
//                        .value = ProfileConstants.YG_ID
//                  finish()
//                }
//
//                is ProfileState.Fail ->{
//
//
//                }
//            }
//        }

    }

    //修改性别
    private fun setGender(){

    }

    private lateinit var mSwitchButton: SwitchButton
    private lateinit var mTagLl: LinearLayout
    private lateinit var mShowAge: TextView
    private lateinit var mShowConstellation: TextView
    private lateinit var mBirthdayLayout: RelativeLayout
    private lateinit var mBirthday: TextView

    private var mBirthdayTag: Int? = null
    private var pvTimePicker: TimePickerView? = null

    private var mSelectedBirthday: Long? = null

    private var subType: Int = 0
    private var isShow: Int = 0
    //修改生日
    private fun setBirthday(){

        //修改生日布局可见
        findViewById<View>(R.id.ll_5).visibility= View.VISIBLE

        mSwitchButton = findViewById(R.id.sb_birthday)//是否展示生日标签
        mTagLl = findViewById(R.id.ll_birthday_tag)
        mShowAge = findViewById(R.id.tv_show_age)//生日
        mShowConstellation = findViewById(R.id.tv_show_constellation)//星座
        mBirthdayLayout = findViewById(R.id.rl_birthday_layout)
        mBirthday = findViewById(R.id.tv_birthday)

        /**
         * 设置生日
         */
        if (Singleton.getUserInfo().birthday != null && Singleton.getUserInfo().birthday != ""){
            mBirthday.text = Singleton.getUserInfo().birthday
        }else{
            mBirthday.text ="点击设置生日"
        }

        mTitle.text = "编辑生日"
        mSave.text = "保存"
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        /**保存是否显示生日信息*/
        mSave.setOnClickListener {

            /**
             * 修改生日
             */
            mineViewModel.updateUserInfo(birthday = mSelectedBirthday)

            /**
             * 请求接口
             */
            mineViewModel.setUserHomePageSetting(
                0,//0:生日，1:地区
                isShow,//是否显示；0-不展示，1-展示
                subType,//当type为0时，此字段必填；0:年龄，1-星座
            )
        }

        /**请求设置生日的状态*/
        mineViewModel.getUserHomePageSetting()

        /**生日状态回调*/
        mineViewModel.getUserHomePageSettingState.observe(this) {
            when (it) {
                is GetUserHomePageSettingState.Success -> {
                    mineViewModel.mGetUserHomePageSettingBean!!.list.forEach{
                        if (it.type==0){
                            /**是否展示生日按钮*/
                            mSwitchButton.isChecked = it.isShow==1
                            if (mSwitchButton.isChecked){
                                mTagLl.visibility = View.VISIBLE
                            }else{
                                mTagLl.visibility = View.GONE
                            }

                            /**是展示生日  还是星座*/
                            subType=it.subType
                            when(it.subType){
                                0->{
                                    setTagChecked(1)
                                }
                                1->{
                                    setTagChecked(2)
                                }
                            }
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

        /**
         * 生日标签
         */
        mSwitchButton.setOnCheckedChangeListener { view, isChecked ->
           isShow =if (mSwitchButton.isChecked){1}else{0}
            if (mSwitchButton.isChecked){
                mTagLl.visibility = View.VISIBLE
            }else{
                mTagLl.visibility = View.GONE
            }

            /**
             * 请求接口
             */
            mineViewModel.setUserHomePageSetting(
                0,//0:生日，1:地区
                isShow,//是否显示；0-不展示，1-展示
                subType,//当type为0时，此字段必填；0:年龄，1-星座
            )
        }

        mShowAge.setOnClickListener {
            setTagChecked(1)
            subType=0
            /**
             * 请求接口
             */
            mineViewModel.setUserHomePageSetting(
                0,//0:生日，1:地区
                isShow,//是否显示；0-不展示，1-展示
                0,//当type为0时，此字段必填；0:年龄，1-星座
            )
        }

        mShowConstellation.setOnClickListener {
            setTagChecked(2)
            subType=1
            /**
             * 请求接口
             */
            mineViewModel.setUserHomePageSetting(
                0,//0:生日，1:地区
                isShow,//是否显示；0-不展示，1-展示
                1,//当type为0时，此字段必填；0:年龄，1-星座
            )
        }

        /** 点击展示选择生日器*/
        mBirthdayLayout.setOnClickListener {
            pvTimePicker?.show()
        }

        //选择器初始化
        initCustomTimePicker()

        /**点击保存之后回调*/
        mineViewModel.fixUserInfoState.observe(this) {
            when (it) {
                is FixUserInfoState.Success -> {
                    Singleton.centerToast(this, "修改成功")
                }

                is FixUserInfoState.Fail -> {
                    /**
                     * 请求失败 生日还原
                     */
                    if (Singleton.getUserInfo().birthday != null && Singleton.getUserInfo().birthday != ""){
                        mBirthday.text = Singleton.getUserInfo().birthday
                    }else{
                        mBirthday.text ="点击设置生日"
                    }


                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }

                }
            }
        }
    }

    /**
     * 设置选中的生日标签
     * @param 0:年龄，1-星座
     */
    private fun setTagChecked(birthdayTag: Int) {
        if (birthdayTag == 1) {
            mBirthdayTag = 1
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_green_selected)
            drawable?.apply {
                setBounds(0, 0, SizeUtils.dp2px(19f), SizeUtils.dp2px(19f))
            }

            val drawable1 = ContextCompat.getDrawable(this, R.drawable.sel)
            drawable1?.apply {
                setBounds(0, 0, SizeUtils.dp2px(19f), SizeUtils.dp2px(19f))
            }

            mShowAge.setCompoundDrawables(null, null, drawable, null)
            mShowConstellation.setCompoundDrawables(null, null, drawable1, null)

        } else if (birthdayTag == 2) {
            mBirthdayTag = 2
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_green_selected)
            drawable?.apply {
                setBounds(0, 0, SizeUtils.dp2px(19f), SizeUtils.dp2px(19f))
            }

            val drawable1 = ContextCompat.getDrawable(this, R.drawable.sel)
            drawable1?.apply {
                setBounds(0, 0, SizeUtils.dp2px(19f), SizeUtils.dp2px(19f))
            }

            mShowConstellation.setCompoundDrawables(null, null, drawable, null)
            mShowAge.setCompoundDrawables(null, null, drawable1, null)

        }
    }

    private fun initCustomTimePicker() {

        val selectData: Calendar? = Calendar.getInstance()

        Singleton.getUserInfo().let { userInfo ->
            if (!StringUtils.isEmpty(userInfo.birthday)) {
                val date = SimpleDateFormat("yyyy-MM-dd").parse(userInfo.birthday)
                selectData?.time = date
                mBirthday.text = userInfo.birthday

            } else {
                mSave.isEnabled = false
                mSave.setTextColor(resources.getColor(R.color.gray_B3B3B3))
            }

        }


        val startDate = Calendar.getInstance()
        startDate.set(1949, 4, 1)
        val endDate = Calendar.getInstance()

        pvTimePicker = TimePickerBuilder(this) { date: Date?, v: View? ->
            mSelectedBirthday = date?.time
            val dateStr = SimpleDateFormat("yyyy-MM-dd").format(date)

            mBirthday.text = dateStr
        }.setLayoutRes(R.layout.birthday_time_pick_layout) { view: View ->
                view.findViewById<TextView>(R.id.tv_selected)?.setOnClickListener {
                    pvTimePicker?.returnData()
                    pvTimePicker?.dismiss()

                    mSave.isEnabled = true
                    mSave.setTextColor(resources.getColor(R.color.black_222222))

                    /**
                     * 修改生日
                     */
                    mineViewModel.updateUserInfo(birthday = mSelectedBirthday)
                }
                view.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
                    pvTimePicker?.dismiss()

                }
            }
            .setDate(selectData)
            .setItemVisibleCount(5)
            .isAlphaGradient(true)
            .setContentTextSize(20)
            .setLineSpacingMultiplier(2f)
            .setRangDate(startDate, endDate)
            .isDialog(false)
            .build()
    }

    //修改地址
    private fun setAddress(){

    }


}