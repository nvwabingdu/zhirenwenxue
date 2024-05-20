package cn.dreamfruits.yaoguo.module.main.home.searchhistory

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.module.main.home.MaxRecyclerView
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.SearchViewModel
import cn.dreamfruits.yaoguo.module.main.home.searchhistory.acidheatindex.AcidHeatIndexAdapter
import cn.dreamfruits.yaoguo.module.main.home.searchhistory.guessyouwanttosearch.GuessYouWantToSearchAdapter
import cn.dreamfruits.yaoguo.module.main.home.searchhistory.historysearch.FlexBoxLayoutMaxLines
import cn.dreamfruits.yaoguo.module.main.home.searchhistory.historysearch.HistorySearchAdapter
import cn.dreamfruits.yaoguo.module.main.home.searchhistory.pop.SearchPopAdapter
import cn.dreamfruits.yaoguo.module.main.home.state.GetGuessHotWordsState
import cn.dreamfruits.yaoguo.module.main.home.state.GetSearchWordBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetYgHotFeedListState
import cn.dreamfruits.yaoguo.repository.MMKVRepository
import cn.dreamfruits.yaoguo.repository.bean.search.GetYgHotFeedListBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchWordBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.setRotateAnimation
import com.blankj.utilcode.util.GsonUtils
import com.google.android.flexbox.FlexWrap
import java.util.Timer
import java.util.TimerTask


open class HomeSearchHistoryActivity : BaseActivity() {

    private val searchViewModel by viewModels<SearchViewModel>()//这里用搜索模糊匹配

    private var searchData: String? = null

    private var isOnCreate: Boolean = true
    override fun initView() {
        searchData = intent.getStringExtra("SearchData")
        Log.e("TAG21212112", "initView: " + searchData)

        Singleton.searchWord = ""//置空不保存数据
        setSearch()
        historySearch()

        guessSearch()
        srbSet()

        isOnCreate = false
    }


    /**
     * 优化
     */
    override fun onRestart() {//重新进入的时候 清空之前的搜索词
        super.onRestart()
        Log.e("w1231231", "onRestart")
        if (searchEt != null) {
            searchEt!!.setText("")
        }
    }


    /**
     * 优化
     */
    override fun onResume() {
        super.onResume()
        if (searchEt != null) {
            if (Singleton.searchWord != "") {
                searchEt!!.setText(Singleton.searchWord)
                searchEt!!.setSelection(Singleton.searchWord.length)
            }

            /**延迟键盘*/
            if (isOnCreate) {
                handler = Handler(Looper.getMainLooper())
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        // 发送消息到主线程
                        handler.post {
                            // 获取输入法管理器
                            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            // 弹出键盘
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                        }
                    }
                }, 500)
            }

        }

        isOnCreate = true
    }

    /**
     * 优化2
     */
    override fun onPause() {
        super.onPause()
        if (searchEt != null) {
            Singleton.searchWord = searchEt!!.text.toString()
        }
    }


    override fun layoutResId(): Int = R.layout.home_activity_search_history
    override fun initData() {}

    private var searchEt: EditText? = null
    private var searchButton: TextView? = null
    private var searchIv: ImageView? = null
    private var viewLine: View? = null//分割线1
    private var historyTitleLayout: View? = null
    private var searchDel: ImageView? = null//删除图标

    /**
     * 搜索框设置
     */
    private fun setSearch() {
        handler = Handler(Looper.getMainLooper())
        viewLine = findViewById(R.id.view_line)//分割线1
        searchEt = findViewById(R.id.search_et)
        searchEt!!.hint = " ${searchData}"
        searchIv = findViewById(R.id.search_iv)//搜索图标
        searchDel = findViewById(R.id.search_del)//删除图标
        searchDel!!.setOnClickListener {
            searchEt!!.setText("")
        }
        searchIv!!.setOnClickListener {
            //搜索图标 走搜索逻辑
            startSearch()//开始搜索
        }
        historyTitleLayout = findViewById(R.id.history_title_layout)

        searchEt!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                Log.e("TAG21212112", "afterTextChanged: " + s.toString())
                //请求
                searchViewModel.searchWord(searchEt!!.text.toString(), 1, 18, null)

                if (searchEt!!.text.toString().isNotEmpty()) {
                    searchDel!!.visibility = View.VISIBLE
                } else {
                    searchDel!!.visibility = View.GONE
                }
            }
        })

        searchButton = findViewById(R.id.search_tv)

        /**
         * 输入框获得焦点 会自动弹出键盘
         */
        searchEt!!.requestFocus()


        /**
         * 右边搜索文字
         */
        searchButton!!.setOnClickListener {
            startSearch()//开始搜索
        }

        /**
         * 键盘上的搜素按钮 把回车修改为搜索并实现监听和隐藏键盘
         */
        searchEt!!.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startSearch()//开始搜索
                return@OnEditorActionListener true
            }
            false
        })

        /**
         * 初始化模糊匹配
         */
        setInitSearchWordView()

        /**
         * 注册模糊匹配的请求结果
         */
        getSearchWord()
    }

    private lateinit var handler: Handler

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 获取搜索匹配词
     */
    private fun getSearchWord() {
        searchViewModel.getSearchWordBeanState.observe(this) {
            when (it) {
                is GetSearchWordBeanState.Success -> {
                    mList!!.clear()
                    mList = searchViewModel.mSearchWordBean!!.list
                    mList!!.forEach {
                        it.key = searchEt!!.text.toString()//装入KEy
                    }
                    //符合条件才显示
                    if (mRecyclerView != null && mList!!.size != 0) {
                        mRecyclerView!!.visibility = View.VISIBLE
                    } else {
                        mRecyclerView!!.visibility = View.GONE
                    }
                    searchWordAdapter!!.setData(mList!!, true)
                }

                is GetSearchWordBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)

                    if (mRecyclerView != null) {//请求出错就隐藏
                        mRecyclerView!!.visibility = View.GONE
                    }
                }
            }
        }
    }

    /**
     * 搜索历史模糊匹配列表pop
     */
    var mRecyclerView: RecyclerView? = null
    var mList: MutableList<SearchWordBean.Item>? = ArrayList()//临时使用集合
    var searchWordAdapter: SearchPopAdapter? = null
    private fun setInitSearchWordView() {
        mRecyclerView = findViewById(R.id.search_history_pop_recyclerView)
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager
        searchWordAdapter = SearchPopAdapter(this, mList!!)
        mRecyclerView!!.adapter = searchWordAdapter

        //跳转到搜索结果页  并保存此条数据
        searchWordAdapter!!.setSearchPopAdapterCallBack(object : SearchPopAdapter.InnerInterface {
            override fun onclick(key: String) {
                setHistoryMMkv(2, key)//用输入文本搜索
                hintKeyBoardAndSkip(key)//后续操作
            }
        })
    }

    /**
     * 开始搜索
     */
    private fun startSearch() {
        val key: String = searchEt!!.getText().toString()
        val hint: String = searchEt!!.getHint().toString()

        when (key) {
            "" -> {
                when (hint) {
                    "" -> {
                        Singleton.centerToast(this, "搜索内容不能为空")
                        return
                    }

                    else -> {
                        setHistoryMMkv(2, hint)//用输入文本搜索
                        hintKeyBoardAndSkip(hint)//后续操作
                    }
                }
            }

            else -> {
                setHistoryMMkv(2, key)//用输入文本搜索
                hintKeyBoardAndSkip(key)//后续操作
            }
        }
    }

    /**
     * 接上面 保存历史搜索  对mmkv的操作
     */
    private fun setHistoryMMkv(tag: Int, str: String): HistoryBean? {
        when (tag) {
            0 -> {
                //初始化时使用
            }

            1 -> {//取出
                val json =
                    MMKVRepository.getCommonMMKV().decodeString(MMKVConstants.HISTORY_BEAN, "")
                return if (json.equals("")) {
                    null
                } else {
                    GsonUtils.fromJson(json, HistoryBean::class.java)
                }
            }

            2 -> {//保存
                //先取出来
                val bean = setHistoryMMkv(1, str)
                if (bean != null) {//不等于空 添加存储
//                    //判断是否已经存在
//                    var isExist=false
//
//                    for (i in bean.list.indices){
//                        if (bean.list[i].mStr==str){
//                            isExist=true
//                            break
//                        }
//                    }
//
//                    if (!isExist){//不存在
//                        bean.list.add(0,HistoryBean.Item(str))
//                        MMKVConstants.initData(MMKVConstants.HISTORY_BEAN,bean)
//                    }


                    // TODO:  优化 如果超过50个就把后面的删除掉 这里需要判断需求
                    if (bean.list.size >= 50) {
                        bean.list.removeAt(bean.list.size - 1)
                    }

                    /**优化 如果存在就删除并添加到第一个*/
                    var isExistIndex = -1
                    for (i in bean.list.indices) {
                        if (bean.list[i].mStr == str) {
                            isExistIndex = i
                            break
                        }
                    }

                    if (isExistIndex != -1) {
                        bean.list.removeAt(isExistIndex)
                    }

                    bean.list.add(0, HistoryBean.Item(str))
                    MMKVConstants.initData(MMKVConstants.HISTORY_BEAN, bean)


                } else {//等于空 直接存储
                    val list: MutableList<HistoryBean.Item> = ArrayList()
                    list.add(0, HistoryBean.Item(str))
                    val mBean = HistoryBean(list)
                    MMKVConstants.initData(MMKVConstants.HISTORY_BEAN, mBean)
                }
            }

            3 -> {//删除单个

            }

            4 -> {//删除全部
                //MMKVConstants.clearData(MMKVConstants.HISTORY_BEAN)//不采用此种方式  应该仅删除数据 保留结构
                //先取出来
                val bean = setHistoryMMkv(1, str)
                if (bean != null) {//不等于空 添加存储
                    bean.list.clear()
                    MMKVConstants.initData(MMKVConstants.HISTORY_BEAN, bean)
                } else {//等于空 直接存储
                    val list: MutableList<HistoryBean.Item> = ArrayList()
                    val mBean = HistoryBean(list)
                    MMKVConstants.initData(MMKVConstants.HISTORY_BEAN, mBean)
                }
            }
        }

        //除了取出状态之外 其他状态都需要刷新适配器
        val mtemp = setHistoryMMkv(1, "")
        if (mtemp != null) {
            if (mtemp.list.size == 0) {
                historyTitleLayout!!.visibility = View.GONE
                viewLine!!.visibility = View.GONE//紧接着的分割线
            } else {
                historyTitleLayout!!.visibility = View.VISIBLE
                viewLine!!.visibility = View.VISIBLE//紧接着的分割线
            }
            mHistoryAdapter!!.setData(mtemp.list, true)
        } else {
            mHistoryAdapter!!.notifyDataSetChanged()
            historyTitleLayout!!.visibility = View.GONE
            viewLine!!.visibility = View.GONE//紧接着的分割线
        }
        return null
    }

    /**
     * 后续操作
     */
    private fun hintKeyBoardAndSkip(str: String) {
        //隐藏键盘
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0)

        // TODO: 之后修改 暂时
        if (str == "") {
            //跳转到搜索详情页
            Singleton.shipSearchDetailsActivity(this, "")
        } else {
            //跳转到搜索详情页
            Singleton.shipSearchDetailsActivity(this, str)
        }
    }

    private var mHistoryRecyclerview: MaxRecyclerView? = null
    private var mFlexboxLayoutManager: FlexBoxLayoutMaxLines? = null
    private var mHistoryAdapter: HistorySearchAdapter? = null
    private var mHistoryList: MutableList<HistoryBean.Item>? = ArrayList()
    private var isHistoryOpen = false//历史搜索是否展开
    private var imgBack: ImageView? = null
    private var historyArrow: ImageView? = null

    /**
     * 历史搜索设置
     */
    private fun historySearch() {
        mHistoryRecyclerview = findViewById<MaxRecyclerView>(R.id.search_history_lsss_recycview)
        //添加一个back键
        imgBack = findViewById<ImageView>(R.id.img_back)
        imgBack!!.setOnClickListener {
            finish()
        }

        //设置适配器
        mHistoryRecyclerview!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        mFlexboxLayoutManager = FlexBoxLayoutMaxLines(this)
        mFlexboxLayoutManager!!.flexWrap = FlexWrap.WRAP // 设置换行方式为换行
        mHistoryRecyclerview!!.layoutManager = mFlexboxLayoutManager
        mHistoryRecyclerview!!.setHasFixedSize(false)//设置item大小是否固定 关键参数  这里设置为不固定
        mHistoryAdapter = HistorySearchAdapter(this, mHistoryList!!)
        mHistoryRecyclerview!!.adapter = mHistoryAdapter

        mHistoryAdapter!!.setHistorySearchAdapterCallBack(object :
            HistorySearchAdapter.InnerInterface {
            override fun onclick(str: String) {
                setHistoryMMkv(2, str)//用输入文本搜索
                hintKeyBoardAndSkip(str)//后续操作
            }
        })

        /**
         * 按钮：历史搜索展开和收起
         */
        historyArrow = findViewById(R.id.history_arrow)//箭头 用于收起和展开历史搜索

        //仅第一次进入时使用
        historyArrow!!.setBackgroundResource(R.drawable.home_arrow_down)
        mFlexboxLayoutManager!!.maxLine = 2//设置为两行
        setHistoryMMkv(0, "")//取出数据并设置

        //点击事件
        historyArrow!!.setOnClickListener {
            //更改展开还是收起的状态
            if (isHistoryOpen) {//如果是展开状态 就收起
                isHistoryOpen = false
                setRotateAnimation(historyArrow!!, 180f, 360f)//向上 收起
                mFlexboxLayoutManager!!.maxLine = 2
            } else {
                isHistoryOpen = true
                setRotateAnimation(historyArrow!!, 0f, 180f)//向下 展开
                mFlexboxLayoutManager!!.maxLine = 4
            }
            setHistoryMMkv(0, "")
        }

        /**
         * 按钮：删除全部历史搜索
         */
        val historyDel: ImageView = findViewById(R.id.history_del)
        historyDel.setOnClickListener {
            showCancelConfirm(this, "是否删除全部历史记录")
        }
    }


    /**
     * 是否删除全部 弹窗
     */
    private fun showCancelConfirm(mActivity: Activity, str: String) {
        val inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_comm_alert, null)

        val content = inflate.findViewById<TextView>(R.id.tv_alert_content)
        content.text = str
        val confirm = inflate.findViewById<TextView>(R.id.tv_alert_confirm)
        confirm.visibility = View.VISIBLE
        confirm.text = "确定"
        val cancel = inflate.findViewById<TextView>(R.id.tv_alert_cancel)
        cancel.text = "取消"


        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        //点击确定
        confirm.setOnClickListener {
            mPopupWindow.dismiss()
            //清空记录
            setHistoryMMkv(4, "")
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
            Singleton.bgAlpha(mActivity, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                Singleton.bgAlpha(mActivity, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        Singleton.lifeCycleSet(mActivity, mPopupWindow)
    }

    //==============以下过时================暂时保留
    /**
     * 猜你想搜设置
     */
    private var cnxsRecyclerview: MaxRecyclerView? = null
    private var changeLayout: LinearLayout? = null
    private var changeIv: ImageView? = null
    private var cnxsList: MutableList<String>? = ArrayList()
    private fun guessSearch() {
        changeLayout = findViewById(R.id.change_layout)//换一批
        changeIv = findViewById(R.id.change_iv)
        changeLayout!!.setOnClickListener {
            startRotation(changeIv!!, 1, 500)// 旋转3次，时长为1秒
            /**
             * 猜你想搜  请求
             */
            searchViewModel.getGuessHotWords(6)
        }

        cnxsRecyclerview = findViewById<MaxRecyclerView>(R.id.search_history_cnxs_recycview)
        //加入点击动画 更符合用户体验
        /* cnxsRecyclerview!!.addItemDecoration(ClickItemDecoration(this))*/
        cnxsRecyclerview!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        //设置适配器
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        cnxsRecyclerview!!.layoutManager = layoutManager
        val adapter = GuessYouWantToSearchAdapter(cnxsList!!)
        cnxsRecyclerview!!.adapter = adapter

        adapter.setGuessYouWantToSearchAdapterCallBack(object :
            GuessYouWantToSearchAdapter.InnerInterface {
            override fun onclick(title: String) {
                setHistoryMMkv(2, title)//用输入文本搜索
                hintKeyBoardAndSkip(title)//后续操作
            }
        })

        /**
         * 猜你想搜  请求
         */
        searchViewModel.getGuessHotWords(6)

        /**
         * 猜你想搜  请求回调
         */
        searchViewModel.getGuessHotWordsState.observe(this) {
            when (it) {
                is GetGuessHotWordsState.Success -> {
                    if (cnxsList!!.size > 0) {
                        cnxsList!!.clear()
                    }

                    cnxsList = searchViewModel.mGetGuessHotWordsBean!!.words

                    //设置hint  初次进来hint 用猜你想搜来
                    if (cnxsList!!.size > 0) {
                        searchEt!!.hint = " " + cnxsList!![0]
                        searchEt!!.setSelection(0)
                    }

                    adapter.setData(cnxsList!!, true)
                }

                is GetGuessHotWordsState.Fail -> {
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

    private fun startRotation(imageView: ImageView, rotationCount: Int, duration: Int) {
        // 计算旋转角度
        val fromDegree = 0.0f
        val toDegree = 360.0f * rotationCount

        // 创建旋转动画
        val rotateAnimation = RotateAnimation(
            fromDegree, toDegree,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = duration.toLong() // 设置旋转时长
        rotateAnimation.fillAfter = true // 让ImageView保持在最后一帧旋转的状态

        // 启动旋转动画
        imageView.startAnimation(rotateAnimation)
    }

    /**
     * 酸热榜设置
     */
    private var srbList: MutableList<GetYgHotFeedListBean.Item>? = ArrayList()
    private var srbRecyclerview: MaxRecyclerView? = null
    private fun srbSet() {
        srbRecyclerview = findViewById<MaxRecyclerView>(R.id.search_history_srb_recycview)
        //加入点击动画 更符合用户体验
        /*srbRecyclerview!!.addItemDecoration(ClickItemDecoration(this))*/
        srbRecyclerview!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        //设置适配器
        var layoutManager = LinearLayoutManager(this)
        srbRecyclerview!!.layoutManager = layoutManager
        val adapter = AcidHeatIndexAdapter(this, srbList!!)
        srbRecyclerview!!.adapter = adapter

        adapter.setAcidHeatIndexAdapterCallBack(object : AcidHeatIndexAdapter.InnerInterface {
            override fun onclick(title: String) {
                setHistoryMMkv(2, title)//用输入文本搜索
                hintKeyBoardAndSkip(title)//后续操作
            }
        })

        /**
         * 获取腰果热榜  请求
         */
        searchViewModel.getYgHotFeedList()


        /**
         * 获取腰果热榜  请求回调
         */
        searchViewModel.getYgHotFeedListState.observe(this) {
            when (it) {
                is GetYgHotFeedListState.Success -> {
                    if (srbList!!.size > 0) {
                        srbList!!.clear()
                    }
                    srbList = searchViewModel.mGetYgHotFeedListBean!!.list
                    adapter.setData(srbList!!, true)
                }

                is GetYgHotFeedListState.Fail -> {
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
     * 重写startActivity方法，取消切换动画
     */
    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(0, 0)
    }


    /**
     * 重写startActivityForResult方法，取消切换动画
     */
    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}