package cn.dreamfruits.yaoguo.module.main.home.phonebook

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.module.login.state.GetContactsListState
import cn.dreamfruits.yaoguo.module.login.state.UploadAllContactsState
import cn.dreamfruits.yaoguo.module.login.state.UploadContactsState
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.customview.ZrDefaultPageView
import cn.dreamfruits.yaoguo.module.main.home.phonebook.finduser.FindUserAdapter
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.FollowUserBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.UnfollowUserBeanState
import cn.dreamfruits.yaoguo.repository.MMKVRepository
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.example.phonecontact.ContactBean
import com.gyf.immersionbar.ktx.immersionBar
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ForwardToSettingsCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.request.ForwardScope
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.util.ArrayList

/**
 * @Author qiwangi
 * @Date 2023/6/23
 * @TIME 01:56
 */
class PhoneBookActivity: BaseActivity(){
    /**
     * 初始化状态栏
     */
//    override fun initStatus() {
//        immersionBar {
//            fitsSystemWindows(false)
//            statusBarDarkFont(true)
//        }
//        //沉浸状态栏重叠问题
//        findViewById<View>(cn.dreamfruits.yaoguo.R.id.status_bar).layoutParams =
//            LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                BarUtils.getStatusBarHeight()
//            )
//    }

    private var defaultPageView: ZrDefaultPageView? = null
    override fun initData() {}
    override fun layoutResId(): Int = R.layout.activity_phone_find_user
    override fun initView() {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        /**不同点*/
        findViewById<TextView>(R.id.tv_title).text="通讯录好友"

        //用于通讯录中
        findViewById<LinearLayout>(R.id.ll_top_other).visibility=View.GONE

        /**初始化顶部布局*/
        initTop()

        /**初始化用户列表*/
        initUserList()

        /**缺省*/
        defaultPageView = findViewById(R.id.default_page_view)
        defaultPageView!!.setData( getString(R.string.default_no_contacts) ,R.drawable.default_no_contact)

        /**刷新*/
        refreshLayout!!.autoRefresh()
    }

    /**
     * 1.初始化顶部按钮
     */
    private fun initTop() {
        //通讯录
        findViewById<ImageView>(R.id.img_phone_book).setOnClickListener {
            val intent = Intent(this, PhoneBookActivity::class.java)
            startActivity(intent)
        }
        //二维码
        findViewById<ImageView>(R.id.img_qr_code).setOnClickListener {
            val intent = Intent(this, QrCodeActivity::class.java)
            startActivity(intent)
        }
        //扫一扫
        findViewById<ImageView>(R.id.img_scan_qr_code).setOnClickListener {
            val intent = Intent(this, ScanQrCodeActivity::class.java)
            startActivity(intent)
        }
    }


    /**
     * 2.初始化下部分的用户列表
     */
    private var mRecyclerView: RecyclerView? = null
    private var mList: MutableList<SearchUserBean.Item>? = ArrayList()
    private val contactsViewModel by viewModels<ContactsViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private var mAdapter: FindUserAdapter? = null
    private fun initUserList() {
        mRecyclerView = findViewById(R.id.recyclerview)
        /*mRecyclerView!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突*/
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = FindUserAdapter(this, mList!!)
        mRecyclerView!!.adapter = mAdapter

        setCallback()

        setRefresh()
    }

    private fun setCallback() {
        /**
         * 回调
         */
        mAdapter!!.setFindUserAdapterCallBack(object :
            FindUserAdapter.InnerInterface{
            //关注
            override fun onCareUser(id: Long, isCare: Boolean) {
                if (isCare) {//关注
                    commonViewModel.getFollowUser(id)
                } else {//取消关注
                    commonViewModel.getUnfollowUser(id)
                }
            }

        })


        /**
         * 请求结果   关注
         */
        commonViewModel.followUserBeanState.observe(this) {
            when (it) {
                is FollowUserBeanState.Success -> {
                    Singleton.centerToast(this, Singleton.CARE_TEXT)
                }

                is FollowUserBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }


        /**
         * 请求结果  取消关注
         */
        commonViewModel.unfollowUserBeanState.observe(this) {
            when (it) {
                is UnfollowUserBeanState.Success -> {
                    Singleton.centerToast(this, Singleton.UN_CARE_TEXT)
                }

                is UnfollowUserBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

    }

    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */
    private var refreshLayout: SmartRefreshLayout? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载

    private fun setRefresh() {
        refreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))
        /**
         * 2.请求结果
         */
        contactsViewModel.getContactsListState.observe(this) {
            when (it) {
                is GetContactsListState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            mList!!.clear()//清空集合
                            mList = contactsViewModel.mGetContactsListBean!!.list.toMutableList()//加载请求后的数据
                            mAdapter!!.setData(mList!!, true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }

                        1, 2 -> {//加载 预加载
                            mAdapter!!.setData(
                                contactsViewModel.mGetContactsListBean!!.list.toMutableList(),
                                false
                            )//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                        }
                    }

                    //缺省
                    setDefaultPageView()
                    //弹窗请求
                    initPop()
                }

                is GetContactsListState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.各自的失败处理逻辑
                    when (refreshState) {
                        0 -> {//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 3)
                        }

                        1, 2 -> {//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 4)
                        }
                    }

                    //缺省
                    setDefaultPageView()
                    //弹窗请求
                    initPop()
                }
            }
        }

        /**
         * 3.下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            //1.设置标签为刷新
            refreshState = 0

            /**请求通讯录*/
            contactsViewModel.getContactsList()
        }


        /**设置不加载更多*/
        refreshLayout!!.setEnableLoadMore(false)//设置不加载

        // TODO: 先注释
        /**
         * 4.上拉加载
         */
//        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
//            //设置标签为加载（不刷新）
//            //2.请求
//            if (mList!!.size!=0){
//                refreshState=1
//                getLoadMoreLogic()
//            }
//        }
    }

//    /**
//     * 5.加载更多逻辑
//     */
//    private fun getLoadMoreLogic() {
//        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
//     if (contactsViewModel.mGetContactsListBean != null && contactsViewModel.mGetContactsListBean!!.hasNext == 1) {
//         contactsViewModel.getContactsList(
//             contactsViewModel.mGetContactsListBean!!.lastTime
//         )
//        } else if (contactsViewModel.mGetContactsListBean != null && contactsViewModel.mGetContactsListBean!!.hasNext == 0) {//表示没有更多数据了
//            Singleton.setRefreshResult(refreshLayout!!, 5)
//        }
//    }

    /**
     * 0.初始化pop
     *  token null is not valid; is your activity running?  在初始化时报错  不能和activity一起初始化
     *  oncreat onresume 不行 采用获取焦点方式 且停留一秒后弹出
     */
    private var popupWindow: PopupWindow? = null
    var isShowPop = false
    private fun initPop() {
        /**优化1 只能进入一次*/
        if (isShowPop){
            return
        }

        isShowPop = true

        /**优化2 这里进入先判断是否有权限  有权限 上传全量或者差量*/
//        if (PermissionX.isGranted(this, Manifest.permission.READ_CONTACTS)){
        if (hasContactsPermission()){
            /**上传通讯录*/
            upContactInfo()
            return
        }

        /**没有权限 这里弹窗提醒要获取权限*/
        val inflateView: View = layoutInflater.inflate(R.layout.dialog_phone_book_permission, null)
        /**
         * pop实例
         */
        popupWindow = PopupWindow(
            inflateView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        inflateView.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            //取消
            popupWindow!!.dismiss()
        }
        inflateView.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            //确定
            popupWindow!!.dismiss()
            requestContactsPermission()
        }

        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.isTouchable = true
        popupWindow!!.isFocusable = true
        popupWindow!!.showAtLocation(inflateView, Gravity.CENTER, 0, 0)
        Singleton.bgAlpha(this, 0.5f) //设置透明度0.5
        popupWindow!!.setOnDismissListener {
            Singleton.bgAlpha(this, 1f) //恢复透明度
        }

        //在pause时关闭
        Singleton.lifeCycleSet(this, popupWindow!!)
    }


    /**检查通讯录权限*/
    private fun hasContactsPermission(): Boolean {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    /**
     * ----------------------------------------4.通讯录逻辑
     */
    private fun requestContactsPermission() {
        //请求权限
        PermissionX.init(this).permissions(
            Manifest.permission.READ_CONTACTS
        ).onForwardToSettings(object : ForwardToSettingsCallback {
            override fun onForwardToSettings(scope: ForwardScope, deniedList: MutableList<String>) {
                Singleton.centerToast(this@PhoneBookActivity, "您永久拒绝了该权限，请到手机设置更改……")
            }
        })
            .request(object : RequestCallback {
                override fun onResult(
                    allGranted: Boolean,
                    grantedList: List<String>,
                    deniedList: List<String>
                ) {
                    if (allGranted) {
                        /**上传通讯录*/
                        upContactInfo()
                    } else {
                        // 有权限被拒绝
                        Singleton.centerToast(this@PhoneBookActivity, "您拒绝了如下权限：$deniedList")
                    }
                }
            }
            )

    }
    /***
     * 通讯录操作
     */
    var tempList: MutableList<ContactBean.Item>? = ArrayList()//转载集合
    private fun performContactOperation(): ContactBean {
        // 在这里编写您要执行的通讯录操作代码
        // 您可以读取、写入或修改用户的通讯录数据
        val contentResolver: ContentResolver = this.contentResolver
        var cursor: Cursor? = null
        try {
            // 查询联系人数据
            cursor =
                contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    // 获取联系人ID
                    @SuppressLint("Range") val contactId = cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts._ID
                        )
                    )

                    // 获取联系人姓名
                    @SuppressLint("Range") val name = cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )

                    // 查询联系人的电话号码
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(contactId),
                        null
                    )
                    if (phoneCursor != null && phoneCursor.moveToNext()) {
                        // 获取第一个电话号码
                        @SuppressLint("Range") val phoneNumber = phoneCursor.getString(
                            phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )

                        /**添加到集合  重要逻辑*/
                        tempList!!.add(ContactBean.Item(name, phoneNumber, 1))

                        // 打印联系人信息
                        Log.e("TAG", "getContacts: Name: $name, Phone Number: $phoneNumber")
                    }
                    phoneCursor?.close()
                }
            }
        } finally {
            cursor?.close()
        }

        return tempList!!.let { ContactBean(it) }

    }


    /**请求权限成功之后调取这个方法 上传全量 或者差量*/
    private fun upContactInfo(){
        /**已经全部授予了 这里操作通讯录  获取通讯录的对象*/
        var bean= performContactOperation()
        Log.e("zqr", "当前通讯录对象："+bean.toString())


        /**上传全量*/
        try {
            val json = MMKVRepository.getCommonMMKV().decodeString(MMKVConstants.CONTACT_BEAN, "")
            Log.e("zqr","上次上传的通讯录："+json)
            val oldBean = GsonUtils.fromJson(json, ContactBean::class.java)
            Log.e("zqr","上次上传的通讯录：contactBean对象："+oldBean.toString())

            if (oldBean!=null){
                /**说明上报过了 上报差量*/
                upDiff(bean,oldBean)
            }else{
                /**上报全量*/
                upAll(bean)
            }
        }catch (e:Exception){
            Log.e("zqr","上传全量或差量失败："+e.message)
        }

        /**用mmkv保存本次获取的通讯录*/
        MMKVConstants.initData(MMKVConstants.CONTACT_BEAN,bean)
    }


    /***
     * 上报通讯录-全量   api
    [{"name":"张三","phone":"18582998776","type":0}]
    name ----> 通讯录中的名字
    phone ----> 手机号
    type ----> 0-删除，1-新增
     */
    private fun upAll(bean: ContactBean){
        val stu = StringBuffer()
        stu.append("[")
        bean.list.forEach {
            stu.append("{\"name\":\"${it.name}\",\"phone\":\"${it.phone}\",\"type\":${1}},")
        }
        var resultStr = stu.toString().substring(0, stu.toString().length - 1)
        resultStr = "$resultStr]"

        Log.e("zqr999", "-----上报全量数据：" + resultStr)

        /**上报全量请求*/
        contactsViewModel.uploadAllContacts(resultStr)

        /**全量网络回调*/
        contactsViewModel.uploadAllContactsState.observe(this) {
            when (it) {
                is UploadAllContactsState.Success -> {
                    /**上传成功之后 重新请求数据 刷新*/
                    refreshState=0
                    contactsViewModel.getContactsList()
                }
                is UploadAllContactsState.Fail -> {

                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }
    }
    /***
     * 上报差量
     */
    private fun  upDiff(bean: ContactBean, oldBean: ContactBean){
        val addList = bean.list.subtract(oldBean.list.toSet())//取出与第二个列表不同的元素
        Log.e("zqr", "-----上报新增差量数据difference1：" + addList.toString())
        val stu = StringBuffer()
        var isDiff=false
        stu.append("[")
        if (addList.size>0){
            isDiff=true
            /**说明有新增的*/
            addList.forEach {
                stu.append("{\"name\":\"${it.name}\",\"phone\":\"${it.phone}\",\"type\":${1}},")
            }
        }


        val delList = oldBean.list.subtract(bean.list.toSet())//取出与第二个列表不同的元素
        Log.e("zqr", "-----上报删除差量数据difference2：" + delList.toString())

        if (delList.size>0){
            isDiff=true
            /**说明有修改 或者需要删除的*/
            delList.forEach {
                stu.append("{\"name\":\"${it.name}\",\"phone\":\"${it.phone}\",\"type\":${0}},")
            }
        }

        var resultStr = stu.toString().substring(0, stu.toString().length - 1)

        resultStr = "$resultStr]"

        Log.e("zqr999", "-----上报差量数据1：" + resultStr)

        if (isDiff){//说明有需要上报的差量
            contactsViewModel.uploadContacts(resultStr)
        }

        /**全量网络回调*/
        contactsViewModel.uploadContactsState.observe(this) {
            when (it) {
                is UploadContactsState.Success -> {
                    /**上传成功之后 重新请求数据 刷新*/
                    refreshState=0
                    contactsViewModel.getContactsList()
                }
                is UploadContactsState.Fail -> {

                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

    }

    /**
     * 设置缺省
     */
    private fun setDefaultPageView(){
        if (mList!!.size==0){
            defaultPageView!!.visibility=View.VISIBLE
        }else{
            defaultPageView!!.visibility=View.GONE
        }
    }





}