package cn.dreamfruits.yaoguo.module.main.mine.editprofile.set.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.module.main.home.NetworkUtilsJava
import cn.dreamfruits.yaoguo.module.main.home.customview.ZrDefaultPageView
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.GetBindRelationState
import cn.dreamfruits.yaoguo.module.main.home.state.GetBlackListState
import cn.dreamfruits.yaoguo.module.main.home.state.GetUserNotificationSettingListState
import cn.dreamfruits.yaoguo.module.main.home.state.OperateBlackListState
import cn.dreamfruits.yaoguo.module.main.home.state.SetFeedbackState
import cn.dreamfruits.yaoguo.module.main.home.state.UnbindThirdPartyState
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.NotificationSetUtil
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.set.notification.blacklist.BlackListAdapter
import cn.dreamfruits.yaoguo.repository.ThridpartyRepository
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.CosUpload
import cn.dreamfruits.yaoguo.util.GlideEngine
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.view.switchbutton.SwitchButton
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.example.zrmultiimagedrag.ImageModel
import com.example.zrmultiimagedrag.UploadMultiImageView
import com.gyf.immersionbar.ktx.immersionBar
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.koin.android.ext.android.inject
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName


/**
 * @Author qiwangi
 * @Date 2023/6/29
 * @TIME 19:32
 */
class SetInnerActivity : BaseActivity() {
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


    /**logic   设置子item设置*/
    private val mineViewModel by viewModels<MineViewModel>()
    private var type: Int = 0//0:关闭 1:开启
    private lateinit var mTitle: TextView
    private lateinit var mTitleRight: TextView
    /**
     * 初始化view
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView() {
        //返回
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
        mTitle = findViewById(R.id.tv_title)
        mTitle.text = intent.getStringExtra("title")
        //各分类布局
        type = intent.getIntExtra("type", 0)
        when (type) {
            0 -> {//账号与安全
                accountAndSecuritySet()
            }
            1 -> {//通知设置
                notificationSet()
            }
            2 -> {//反馈意见
                feedbackSet()
            }
            3 -> {//关于
                aboutAsSet()
            }
            4 -> {//拉黑
                setYouBlack()
            }
        }
    }


    /**
     * 通知设置
     */
    private lateinit var llSettingNotification: LinearLayout
    private lateinit var sw1: SwitchButton
    private lateinit var sw2: SwitchButton
    private lateinit var sw3: SwitchButton
    private lateinit var sw4: SwitchButton
    private lateinit var sw5: SwitchButton
    private lateinit var sw6: SwitchButton
    private lateinit var sw7: SwitchButton
    private lateinit var sw8: SwitchButton
    private lateinit var messageLayout: View
    @RequiresApi(Build.VERSION_CODES.N)
    private fun notificationSet() {
        llSettingNotification = findViewById(R.id.ll_setting_notification);
        llSettingNotification.visibility = View.VISIBLE

        /**
         * 控件初始化
         */
        messageLayout=findViewById<View>(R.id.rl_all_notification)
        sw1 = findViewById(R.id.sw_all_notification)//所有状态
        sw2 = findViewById(R.id.sw_like_notification)//赞和收藏
        sw3 = findViewById(R.id.sw_follow_notification)//新增关注
        sw4 = findViewById(R.id.sw_comment_notification)//评论
        sw5 = findViewById(R.id.sw_at_notification)//@
        sw6 = findViewById(R.id.sw_privacy_message_notification)//私信
        sw7 = findViewById(R.id.sw_follower_feed_notification)//关注的人发动态
        sw8 = findViewById(R.id.sw_official_recommend_notification)//官方推荐

        /**
         * 获取用户通知设置
         */
        if (NotificationSetUtil.isNotificationEnabled(this)){//有通知
            sw1.isChecked=true
            messageLayout.visibility=View.GONE
        }else{
            messageLayout.visibility=View.VISIBLE
            sw1.isChecked= false
            sw1.setOnCheckedChangeListener { view, isChecked ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //判断是否需要开启通知栏功能
                    NotificationSetUtil.OpenNotificationSetting(mContext) {
                        /**通知回调*/
                        sw1.isChecked= true
                    }
                }
            }
        }


        sw2.setOnCheckedChangeListener { view, isChecked ->
            mineViewModel.setUserNotificationSetting(0, if (sw2.isChecked) 1 else 0)
//            sw1.isChecked =
//                (sw2.isChecked || sw3.isChecked || sw4.isChecked || sw5.isChecked || sw6.isChecked || sw7.isChecked || sw8.isChecked)
        }
        sw3.setOnCheckedChangeListener { view, isChecked ->
            mineViewModel.setUserNotificationSetting(1, if (sw3.isChecked) 1 else 0)
//            sw1.isChecked =
//                (sw2.isChecked || sw3.isChecked || sw4.isChecked || sw5.isChecked || sw6.isChecked || sw7.isChecked || sw8.isChecked)
        }
        sw4.setOnCheckedChangeListener { view, isChecked ->
            mineViewModel.setUserNotificationSetting(2, if (sw4.isChecked) 1 else 0)
//            sw1.isChecked =
//                (sw2.isChecked || sw3.isChecked || sw4.isChecked || sw5.isChecked || sw6.isChecked || sw7.isChecked || sw8.isChecked)
        }
        sw5.setOnCheckedChangeListener { view, isChecked ->
            mineViewModel.setUserNotificationSetting(3, if (sw5.isChecked) 1 else 0)
//            sw1.isChecked =
//                (sw2.isChecked || sw3.isChecked || sw4.isChecked || sw5.isChecked || sw6.isChecked || sw7.isChecked || sw8.isChecked)
        }
        sw6.setOnCheckedChangeListener { view, isChecked ->
            mineViewModel.setUserNotificationSetting(4, if (sw6.isChecked) 1 else 0)
//            sw1.isChecked =
//                (sw2.isChecked || sw3.isChecked || sw4.isChecked || sw5.isChecked || sw6.isChecked || sw7.isChecked || sw8.isChecked)
        }
        sw7.setOnCheckedChangeListener { view, isChecked ->
            mineViewModel.setUserNotificationSetting(5, if (sw7.isChecked) 1 else 0)
//            sw1.isChecked =
//                (sw2.isChecked || sw3.isChecked || sw4.isChecked || sw5.isChecked || sw6.isChecked || sw7.isChecked || sw8.isChecked)
        }
        sw8.setOnCheckedChangeListener { view, isChecked ->
            mineViewModel.setUserNotificationSetting(6, if (sw8.isChecked) 1 else 0)
//            sw1.isChecked =
//                (sw2.isChecked || sw3.isChecked || sw4.isChecked || sw5.isChecked || sw6.isChecked || sw7.isChecked || sw8.isChecked)
        }

        /**
         * 请求通知接口
         */
        mineViewModel.getUserNotificationSettingList()

        /**
         * 请求通知回调
         */
        mineViewModel.getUserNotificationSettingListState.observe(this) {
            when (it) {
                is GetUserNotificationSettingListState.Success -> {
                    /**
                     * 控件设置 0-点赞和收藏，1-新粉丝，2-评论，3-@，4-单聊，5-关注人发动态，6-官方
                     */
//                    var isAllOpen = false
//                    mineViewModel.mGetNotificationBean!!.list.forEach {
//                        if (it.isOpen == 1) {
//                            isAllOpen = true
//                        }
//                    }
//                    sw1.isChecked = isAllOpen

                    sw2.isChecked = mineViewModel.mGetNotificationBean!!.list[0].isOpen == 1
                    sw3.isChecked = mineViewModel.mGetNotificationBean!!.list[1].isOpen == 1
                    sw4.isChecked = mineViewModel.mGetNotificationBean!!.list[2].isOpen == 1
                    sw5.isChecked = mineViewModel.mGetNotificationBean!!.list[3].isOpen == 1
                    sw6.isChecked = mineViewModel.mGetNotificationBean!!.list[4].isOpen == 1
                    sw7.isChecked = mineViewModel.mGetNotificationBean!!.list[5].isOpen == 1
                    sw8.isChecked = mineViewModel.mGetNotificationBean!!.list[6].isOpen == 1
                }
                is GetUserNotificationSettingListState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }
    }

    /**
     * 账号与安全
     */
    private lateinit var llSettingAccount: LinearLayout
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvWechat: TextView
    private lateinit var tvWeibo: TextView
    private lateinit var tvQq: TextView

    private var tvWechatTag: Int = 0
    private var tvWeiboTag: Int = 0
    private var tvQqTag: Int = 0
    private var isLoadAccountAndSecuritySet: Boolean = false//含义 是否在resume中加载过账号与安全设置 类似刷新

    override fun onResume() {
        super.onResume()
        Log.e("onResume", "dadadadadadonResume")
        if (isLoadAccountAndSecuritySet){
            Log.e("onResume", "静茹了")
            /**
             * 获取第三方绑定状态
             */
            mineViewModel.getBindRelation()
        }
    }
    private fun accountAndSecuritySet() {

        llSettingAccount = findViewById(R.id.ll_setting_account);
        llSettingAccount.visibility = View.VISIBLE

        tvPhoneNumber = findViewById(R.id.tv_phone_number);

        tvWechat = findViewById<TextView>(R.id.tv_wechat)
        tvWechat.setOnClickListener {
            isLoadAccountAndSecuritySet = true//设置可以刷新
            when ( tvWechatTag) {
                0 -> {//绑定
                    startThirdPartyLogin(1, 111)
                }
                1 -> {//解除绑定 第三方类型 1:微信 2:QQ 3:苹果 4:微博
                    showPop("微信")
                }
            }
        }
        tvWeibo = findViewById<TextView>(R.id.tv_weibo)
        tvWeibo.setOnClickListener {
            isLoadAccountAndSecuritySet = true//设置可以刷新
            Singleton.centerToast(this, "暂未开放")
//            when (tvWeiboTag) {
//                0 -> {//绑定
//                    //写在 三方登录成功回调中
//                    mineViewModel.bindThirdParty(
//                        4,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
//                        null,//微信登录的code，qq登录的token
//                        null//qq的openId
//                    )
//                }
//                1 -> {//解除绑定 第三方类型 1:微信 2:QQ 3:苹果 4:微博
//                    showPop("微博")
//
//                }
//            }
        }
        tvQq = findViewById<TextView>(R.id.tv_qq)
        tvQq.setOnClickListener {
            isLoadAccountAndSecuritySet = true//设置可以刷新
            when (tvQqTag) {
                0 -> {//绑定
                    startThirdPartyLogin(2, 111)
                }
                1 -> {//解除绑定 第三方类型 1:微信 2:QQ 3:苹果 4:微博
                    showPop("QQ")
                }
            }

        }

        /**
         * 获取第三方绑定状态
         */
        mineViewModel.getBindRelation()

        /**
         * 获取第三方绑定状态回调
         */
        mineViewModel.getBindRelationState.observe(this) {
            when (it) {
                is GetBindRelationState.Success -> {

                    tvPhoneNumber.text = mineViewModel.mGetBindRelationBean!!.phoneNumber

                    setBindTv(mineViewModel.mGetBindRelationBean!!.qqName, tvQq, 2)
                    setBindTv(mineViewModel.mGetBindRelationBean!!.wechatName, tvWechat, 1)
                    setBindTv(mineViewModel.mGetBindRelationBean!!.weiboName, tvWeibo, 4)
                }
                is GetBindRelationState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }


        /**
         * 解除绑定
         */
        mineViewModel.unbindThirdPartyState.observe(this) {
            when (it) {
                is UnbindThirdPartyState.Success -> {
                    Log.e("zqr===",it.getScopeId())
                    Log.e("zqr===",it.getScopeName().toString())
                    Singleton.centerToast(this, "解绑成功")

                    mineViewModel.getBindRelation()
                }
                is UnbindThirdPartyState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

    }

    /**
     * 跳转到三方登录页面
     * @param type 微信 =1 qq =2
     * @param infoType 普通登录 = 111 一键登录 = 112
     */
    private fun startThirdPartyLogin(type: Int, infoType: Int) {
        val intent = Intent(this, ThirdActivity::class.java)
        intent.putExtra(RouterConstants.ThirdPartyLogin.KEY_PLATFORM_TYPE, type)
        intent.putExtra(RouterConstants.ThirdPartyLogin.KEY_INTO_TYPE, infoType)
        startActivity(intent)
    }


    /**
     * 设置绑定tv
     */
    private fun setBindTv(bean: Any?, tv: TextView, tag: Int) {
        //tag 1:微信 2:QQ 3:苹果 4:微博
        if (bean == null) {
            tv.text = "绑定"
            tv.setTextColor(Color.parseColor("#ffffff"))
            tv.setBackgroundColor(Color.parseColor("#000000"))
            when (tag) {
                1 -> {//这里的变量值为 0未绑定   1绑定
                    tvWechatTag = 0
                }
                2 -> {
                    tvQqTag = 0
                }
                3 -> {

                }
                4 -> {
                    tvWeiboTag = 0
                }
            }
        } else {
            tv.text = "解绑"
            tv.setTextColor(Color.parseColor("#e6e6e6"))
            tv.setBackgroundResource(R.drawable.shape_stroke_f0f0f0_dp1)
            when (tag) {
                1 -> {
                    tvWechatTag = 1
                }
                2 -> {
                    tvQqTag = 1
                }
                3 -> {

                }
                4 -> {
                    tvWeiboTag = 1
                }
            }
        }
    }


    /**
     * 显示confirm弹窗
     */
    private fun showPop(str: String) {
        val inflate = LayoutInflater.from(this).inflate(R.layout.home_dialog_ok_cancel_black, null)

        val content = inflate.findViewById<TextView>(R.id.content)
        content.text = "解绑后，将不能再用${str}登陆，要继续解除绑定吗？"

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
            mPopupWindow.dismiss()
            when (str) {
                "微信" -> {
                    mineViewModel.unbindThirdParty(1)
                }
                "QQ" -> {
                    mineViewModel.unbindThirdParty(2)
                }
                "微博" -> {
                    mineViewModel.unbindThirdParty(4)
                }
            }
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


    /**
     * 反馈意见
     */
    private lateinit var llSettingFeedback: LinearLayout
    private lateinit var uploadMultiImageView: UploadMultiImageView
    private val mFeedbackList: ArrayList<ImageModel> = ArrayList()
    private var  picUrls: String?=null//可选 图片数组，["a","b"]
    private var etFeedback: EditText? = null
    private var tvFeedbackCount: TextView? = null
    private var isCommit=false//是否提交
    private fun feedbackSet() {
        llSettingFeedback = findViewById(R.id.ll_setting_feedback);
        llSettingFeedback.visibility = View.VISIBLE

        uploadMultiImageView = findViewById(R.id.upload_multi_imageView)
        mTitleRight = findViewById(R.id.tv_title_right)


        tvFeedbackCount = findViewById(R.id.tv_feedback_count)
        mTitleRight.setTextColor(resources.getColor(R.color.gray_B3B3B3))

        etFeedback = findViewById(R.id.et_feedback)//编辑内容

        etFeedback!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

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



                tvFeedbackCount!!.text = "${s!!.length}"//计数

                if (s!!.length in 1..500) {
                    mTitleRight.setTextColor(resources.getColor(R.color.black_222222))
                } else {
                    mTitleRight.setTextColor(resources.getColor(R.color.gray_B3B3B3))
                }


            }

        })









        mTitleRight.text = "提交"
        mTitleRight.setOnClickListener {
            if (isCommit){/**避免重复点击*/
                return@setOnClickListener
            }

            if (etFeedback!!.text.toString() == ""){/**判断是否有文字*/
                Singleton.centerToast(this, "请输入反馈内容")
                return@setOnClickListener
            }
            isCommit=true/**已经在处理中，防止重复点击*/


            if (uploadMultiImageView.childCount>=2){/**至少大于2 表示有图片*/
                /**上传图片*/
                commitFeedBack()

            }else{//没有选择图片 直接请求
                mineViewModel.feedback(etFeedback?.text.toString(), null)
            }
        }

        /**反馈回调*/
        mineViewModel.setFeedbackState.observe(this) {
            when (it) {
                is SetFeedbackState.Success -> {
                    Singleton.centerToast(this, "已提交反馈")
                    finish()
                }
                is SetFeedbackState.Fail -> {
                    //1.是否有断网提示
                    if (!NetworkUtilsJava.isNetworkConnected(this)) {
                        Singleton.isNetConnectedToast(this)
                    }else{
                        Singleton.centerToast(this, "已提交反馈")
                    }
                    finish()
                }
            }
        }


        //初始化装入空数组
        setFeedbackData()
    }

    /**
     * 设置数据
     */
    private fun setFeedbackData() {
        uploadMultiImageView.setImageInfoList(ArrayList(mFeedbackList))
            // 所有属性都可以在代码中再设置
            // 开启拖拽排序
            .setDrag(true)
            // 设置每行3列
            .setColumns(4)
            // 显示新增按钮
            .setShowAdd(true)
            // 设置图片缩放类型 (默认 CENTER_CROP)
            .setScaleType(ImageView.ScaleType.CENTER_CROP)
            // item点击回调
            .setImageItemClickListener { position ->
                // imageview点击
//                Toast.makeText(baseContext, "点击第了${position}个", Toast.LENGTH_SHORT).show()
            }
            // 设置删除点击监听（如果不设置，测试默认移除数据），自己处理数据删除过程
            .setDeleteClickListener { multiImageView, position ->
                Log.e("TAG", "setFeedbackData: " + multiImageView.imageInfoList.size)
                /**直接删除*/
                multiImageView.deleteItem(position)
            }
            // 图片加载
            .setImageViewLoader { context, path, imageView ->
                /**加载图片 加载方式随意*/
                Glide.with(this)
                    .load(Uri.parse(path.toString()))
                    .into(imageView)
            }
            // 新增按钮点击回调
            .setAddClickListener {
                // 模拟新增一条数据
                addNewData(7-uploadMultiImageView.childCount)
            }
            .show()
    }

    /**
     * 新增一条或多条数据
     * count 可选数量
     */
    private fun addNewData(count: Int) {
        /**临时集合*/
        val tempList: ArrayList<ImageModel> = ArrayList()

        /**图片选择器 并返回结果*/
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.instance)
            .setSelectionMode(SelectModeConfig.MULTIPLE)
            .setMaxSelectNum(count)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    /**图片回调*/
                    result?.forEach {
                        Log.e("zqr", "照片回调onResult:" + it.path)//本地路径
                        val model = ImageModel()
                        model.path= it.path
                        /**添加进临时集合*/
                        tempList.add(model)
                    }
                    // 新增数据
                    uploadMultiImageView.addNewData(tempList.toList())
                }
                override fun onCancel() {

                }
            })
    }

    /**
     * 上传图片和文本  意见反馈
     */
    private val thridpartyRepository by inject<ThridpartyRepository>()
    private val stu = StringBuffer()
    @SuppressLint("CheckResult")
    private fun commitFeedBack() {
        /*UriUtils.file2Uri(File(url)).toString()*/

        /***装入需要上传的集合*/
        val filePathList = mutableListOf<String>()
        uploadMultiImageView.imageInfoList.forEach {
            if (it.imagePath.toString().contains("content")) {
                filePathList.add(it.imagePath.toString())
                Log.e("zqr", "newUrl=====>" + it.imagePath.toString())
            }
        }


        stu.setLength(0)//清空
        stu.append("[")
        /**获取腾讯云cos 临时密钥  成功获取之后 上传图片*/
        thridpartyRepository.getTencentTmpSecretKey()
            .subscribe({
                Log.e("zqr", "获取腾讯云cos成功:" + it.toString())
                val cosUpload = CosUpload()
                it.let { cosUpload.initService(it, applicationContext) }
                cosUpload.uploadFiles(filePathList,
                    onSuccess = { filePath, resultUrl ->

                        stu.append("\"$resultUrl\",")

                        Log.e("zqr", "上传成功之后的图片resultUrl$resultUrl")
                    },
                    onFail = {
                        Log.e("zqr", "上传失败")
                        Singleton.centerToast(this, "上传失败")
                    },
                    onProgress = {
                        Log.e("zqr", "上传进度$it")

                    },
                    onComplete = {
                        Log.e("zqr", "上传完成$stu")

                        picUrls= stu.toString().substring(0, stu.toString().length - 1)+"]"


                        Log.e("picUrls1212",picUrls.toString())

                        if (picUrls!!.length<5){
                            mineViewModel.feedback(etFeedback?.text.toString(), null)
                        }else{
                            mineViewModel.feedback(etFeedback?.text.toString(), picUrls)
                        }



                    }
                )
            }, {
//                Log.e("zqr", "获取腾讯云cos 临时密钥失败")
            })
    }



    /**
     * 关于
     */
    private lateinit var llSettingAbout: LinearLayout
    private lateinit var tvVersion: TextView
    private fun aboutAsSet() {
        llSettingAbout = findViewById(R.id.ll_setting_about);
        llSettingAbout.visibility = View.VISIBLE

        //版本号
        var version = "1.0.0"
        try {
            val packageManager: PackageManager = packageManager
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            version = packageInfo.versionName
            val versionCode: Int = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        tvVersion = findViewById(R.id.tv_setting_about_version);
        tvVersion.text = "版本号：$version"
    }

    /**
     * 拉黑
     */
    private var llSettingBlacklist: LinearLayout? = null
    private var mRecyclerView: RecyclerView?=null
    private var mBlackList: MutableList<SearchUserBean.Item>?=ArrayList()
    private var mAdapter: BlackListAdapter? = null
    private var defaultPageView: ZrDefaultPageView? = null
    private  fun setYouBlack(){
        llSettingBlacklist = findViewById(R.id.ll_setting_blacklist)
        llSettingBlacklist?.visibility = View.VISIBLE



        initViewBlack()//1.初始化一些东西
        setCallback()//2.设置回调
        setRefresh() //3.设置刷新 请求数据
        refreshLayout!!.autoRefresh()//4.自动刷新

        /**缺省*/
        defaultPageView = findViewById(R.id.default_page_view)
        defaultPageView!!.setData( getString(R.string.default_user_set_black) ,R.drawable.def_black)
    }

    private fun initViewBlack() {
        mRecyclerView =findViewById(R.id.recyclerview)
        /*mRecyclerView!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突*/
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = BlackListAdapter(this, mBlackList!!)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun  setCallback(){
        /**
         * 回调
         */
        mAdapter!!.setBlackListAdapterCallBack(object :BlackListAdapter.InnerInterface{
            //关注
            override fun onBlackUser(id: Long, isBlack: Boolean) {
                //拉黑
                if (isBlack){//关注
                mineViewModel.operateBlackList(1, id.toString())
                }else{//取消关注
                mineViewModel.operateBlackList(0, id.toString())
                }
            }
        })


        /**拉黑回调*/
        mineViewModel.operateBlackListState.observe(this) {
            when (it) {
                is OperateBlackListState.Success -> {

                }
                is OperateBlackListState.Fail -> {
                    //是否有断网提示
                  Singleton.isNetConnectedToast(this)
                }
            }
        }
    }

    private var refreshLayout: SmartRefreshLayout?= null
    private var refreshState:Int=0//0 刷新  1 加载  2预加载
    private fun  setRefresh(){
        refreshLayout =findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))
        /**
         * 2.请求结果
         */
        mineViewModel.getBlackListState.observe(this){
            when (it) {
                is GetBlackListState.Success -> {
                    when(refreshState){
                        0->{//刷新
                            mBlackList!!.clear()//清空集合
                            mBlackList= mineViewModel.mBlackListBean!!.list.toMutableList()//加载请求后的数据
                            mAdapter!!.setData(mBlackList!!,true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)

                            //缺省
                            setDefaultPageView()
                        }
                        1,2->{//加载 预加载
                            mAdapter!!.setData(mineViewModel.mBlackListBean!!.list.toMutableList(), false)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 2)

                            //缺省
                            setDefaultPageView()
                        }
                    }
                }
                is GetBlackListState.Fail->{
                    //缺省
                    setDefaultPageView()
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.各自的失败处理逻辑
                    when(refreshState){
                        0->{//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 3)
                        }
                        1,2->{//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 4)
                        }
                    }
                }
            }
        }

        /**
         * 3.下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            //1.设置标签为刷新
            refreshState=0

            /**
             * 请求列表
             */
            mineViewModel.getBlackList(
                10,
                null,
            )
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            //设置标签为加载（不刷新）
            refreshState=1
            //2.请求
            // TODO: 请求2
            if (mBlackList!!.size!=0){
                getLoadMoreLogic()
            }
        }
    }

    /**
     * 5.加载更多逻辑
     */
    private fun getLoadMoreLogic() {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (mineViewModel.mBlackListBean == null) {//等于空 说明没有请求过，发出第一次请求
            mineViewModel.getBlackList(
                10,
                null,
            )
        } else if (mineViewModel.mBlackListBean  != null && mineViewModel.mBlackListBean !!.hasNext == 1) {
            mineViewModel.getBlackList(
                10,
                mineViewModel.mBlackListBean!!.lastTime,
            )
        } else if (mineViewModel.mBlackListBean != null && mineViewModel.mBlackListBean!!.hasNext == 0) {//表示没有更多数据了
            Singleton.setRefreshResult(refreshLayout!!, 5)
        }
    }

    /**
     * 设置缺省
     */
    private fun setDefaultPageView(){
        if (mBlackList!!.size==0){
            defaultPageView!!.visibility=View.VISIBLE
        }else{
            defaultPageView!!.visibility=View.GONE
        }
    }

    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */

}