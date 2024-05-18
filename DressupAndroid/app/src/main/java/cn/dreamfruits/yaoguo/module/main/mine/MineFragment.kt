package cn.dreamfruits.yaoguo.module.main.mine

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.ShareWebContent
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.login.SetState
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.ViewPagerFragmentAdapter
import cn.dreamfruits.yaoguo.module.main.home.phonebook.QrCodeActivity
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManager
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManagerListener
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.HomeSearchDetailsActivity
import cn.dreamfruits.yaoguo.module.main.home.state.FollowUserBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetUserInfoState
import cn.dreamfruits.yaoguo.module.main.home.state.UnfollowUserBeanState
import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity
import cn.dreamfruits.yaoguo.module.main.home.unity.ImageUtils
import cn.dreamfruits.yaoguo.module.main.message.ChartActivity
import cn.dreamfruits.yaoguo.module.main.message.bean.ShareUserEntity
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartSetModel
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.main.message.pop.ConfrimPop
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.edit.EditProfileActivity
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.edit.EditUserInfoActivity
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.set.UserInfoSetActivity
import cn.dreamfruits.yaoguo.module.main.mine.fans.UserCenterFansActivity
import cn.dreamfruits.yaoguo.module.main.mine.feed.MineCollectFragment
import cn.dreamfruits.yaoguo.module.main.mine.feed.MinePostFragment
import cn.dreamfruits.yaoguo.module.share.*
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.module.share.state.ShareShortUrlState
import cn.dreamfruits.yaoguo.module.share.state.ShareUseRecomendListState
import cn.dreamfruits.yaoguo.repository.UserRepository.Companion.userInfo
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.isMine
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.util.singleClick
import cn.dreamfruits.yaoguo.view.LoadingPop
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.zrqrcode.zxing.util.LogUtils
import com.example.zrsinglephoto.easyphotos.utils.Color.ColorUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.header.TwoLevelHeader
import com.scwang.smart.refresh.header.listener.OnTwoLevelListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnMultiListener
import com.xiaomi.push.it
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * Toolbar
 *  app：layout_scrollflags=""
 *    1.scroll  跟着recyclerview滚动并隐藏
 *    2.enterAlways 向下滚动时会重新显示和滚动
 *    3.snap 表示当前toolbar还没有完全隐藏的时候或者显示的事后，会根据当前的滚动的距离，自动选择隐藏还是显示
 */
open class MineFragment : Fragment() {
    private lateinit var mStatusHeight: View//状态栏高度
    private lateinit var mToolbar: Toolbar
    private lateinit var mAppBar: AppBarLayout
    private lateinit var mTopBar: RelativeLayout
    private lateinit var mAvatar: ImageView//头像
    private lateinit var mFansNum: TextView//粉丝数量
    private lateinit var mIvQrcode: ImageView//二维码
    private lateinit var mFollowNum: TextView//关注数
    private lateinit var mllPraiseAndCollect: View//wq 赞与收藏 弹出pop
    private lateinit var mPraiseAndCollect: TextView//点赞
    private lateinit var mNickName: TextView//名字
    private lateinit var mNumber: TextView
    private lateinit var mSignature: TextView
    private lateinit var mSignatureImg: ImageView
    private lateinit var mGender: ImageView
    private lateinit var mAge: TextView
    private lateinit var mAddress: TextView
    private lateinit var topShare: ImageView//头部
    private lateinit var mTopIcon: ImageView
    private lateinit var mTopBack: ImageView
    private var topCareChatLl: View? = null// <!--关注 聊天-->
    private var topCareChat: View? = null// <!--关注 聊天-->
    private var topCareLl: View? = null
    private var topChatLl: View? = null
    private var topSendCare: View? = null// <!--关注 聊天-->
    private var topSendLl: View? = null
    private var topCareLl2: View? = null
    private var llEditSetting: View? = null//  <!-- 编辑资料 设置-->  //中部
    private lateinit var mEditProfile: LinearLayout
    private lateinit var mSetting: ImageView
    private var messageCare: View? = null// <!-- 发消息 关注-->
    private var llSendMessage: View? = null
    private var llCare: View? = null
    private var followChat: View? = null// <!--关注 聊天-->
    private var followLl: View? = null
    private var chatLl: View? = null
    private var ivUnityCover: ImageView? = null// <!--unity封面-->
    private var llMoveTop: LinearLayout? = null//拖动
    private var tabLayout: LinearLayout? = null//拖动

    private var ivUserCenterTag: ImageView? = null


    private var llFans: View? = null

    private val mineViewModel by viewModels<MineViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private var refreshLayout: SmartRefreshLayout? = null
    private var llTabLayout: View? = null//
    private var isMine = 0//状态 默认是自己
    private var userId = 0L//用户id


    private val shareViewModel by viewModels<ShareViewModel>()//h获取分享相关数据


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /**
         * 获取传递过来的userId
         */
        userId = try {
            arguments!!.getLong("userId", 0L)//获取传递过来的id
        } catch (e: Exception) {
            0L
        }

        /**
         * userid不为空 则是别人的主页
         */
        if (userId != 0L) {
            if (userId == Singleton.getLoginInfo().userId) {//表示是他人主页页面时点击了自己的id 还是自己的主页
                isMine = 0
            } else {
                isMine = 1
            }

        }

        mStatusHeight = view.findViewById(R.id.view_status_height)
        mToolbar = view.findViewById(R.id.toolbar)
        mAppBar = view.findViewById(R.id.app_bar)
        mTopBar = view.findViewById(R.id.ll_top_bar)
        mAvatar = view.findViewById(R.id.iv_avatar)
        mTopBack = view.findViewById(R.id.top_back)
        topShare = view.findViewById(R.id.top_share)//顶部分享
        llEditSetting = view.findViewById(R.id.ll_edit_user_info_and_setting)//自己主页的编辑资料
        mEditProfile = view.findViewById(R.id.ll_edit_user_info)
        mSetting = view.findViewById(R.id.iv_setting)
        mTopIcon = view.findViewById(R.id.top_icon)
        mFansNum = view.findViewById(R.id.tv_fans_num)
        mIvQrcode = view.findViewById(R.id.iv_qrcode)//二维码
        mFollowNum = view.findViewById(R.id.tv_follow_num)
        mPraiseAndCollect = view.findViewById(R.id.tv_praise_and_collect_num)
        mNickName = view.findViewById(R.id.tv_nickname)//用户名
        mNumber = view.findViewById(R.id.tv_number)//微酸号
        mSignature = view.findViewById(R.id.tv_signature)//签名
        mSignatureImg = view.findViewById(R.id.edit_signature)
        mGender = view.findViewById(R.id.iv_gender)
        mAge = view.findViewById(R.id.tv_age)
        mAddress = view.findViewById(R.id.tv_address)
        llTabLayout = view.findViewById(R.id.ll_top)//仅用于变色
        ivUnityCover = view.findViewById(R.id.iv_unity_cover)//封面
        llMoveTop = view.findViewById(R.id.ll_move_top)//拖动
        tabLayout = view.findViewById(R.id.tab_layout)//拖动

        ivUserCenterTag = view.findViewById(R.id.iv_user_center_tag)//拖动


        //发消息 关注
        messageCare = view.findViewById(R.id.message_care)
        llSendMessage = view.findViewById(R.id.ll_send_message)
        llCare = view.findViewById(R.id.ll_care)

        //关注 聊天
        followChat = view.findViewById(R.id.follow_chat)
        followLl = view.findViewById(R.id.follow_ll)
        chatLl = view.findViewById(R.id.chat_ll)

        //顶部
        topCareChatLl = view.findViewById(R.id.top_care_chat_ll)
        topCareChat = view.findViewById(R.id.top_care_chat)
        topCareLl = view.findViewById(R.id.top_care_ll)
        topChatLl = view.findViewById(R.id.top_chat_ll)

        //顶部
        topSendCare = view.findViewById(R.id.top_send_care)
        topSendLl = view.findViewById(R.id.top_send_ll)
        topCareLl2 = view.findViewById(R.id.top_care_ll2)

        //赞与收藏
        mllPraiseAndCollect = view.findViewById(R.id.ll_praise_and_collect)//wq
        mllPraiseAndCollect.setOnClickListener {
            showPraiseAndCollectPop()
        }

        //复制到剪贴板
        mNickName.setOnClickListener {
            val clipboard: ClipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", mNickName.text.toString())
            clipboard.setPrimaryClip(clip)
            Singleton.centerToast(requireActivity(), "用户名已复制")
        }

        //复制到剪贴板
        mNumber.setOnClickListener {
            val clipboard: ClipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", mNumber.text.toString())
            clipboard.setPrimaryClip(clip)
            Singleton.centerToast(requireActivity(), "ID已复制")
        }

        //wq 刷新
        refreshLayout = view.findViewById(R.id.refresh_layout)
        refreshLayout!!.setEnableLoadMore(false)// 禁止底部刷新
        //设置阻尼
        refreshLayout!!.setDragRate(0.9f)
//        swipeRefresh = view.findViewById(R.id.swipe_refresh)//仅用来展示效果 实际刷新是用的refreshLayout

        // TODO: 临时添加
        refreshLayout!!.setEnableHeaderTranslationContent(false)//是否下拉Header的时候向下平移列表或者内容

        /**
         * 3.下拉刷新
         */
//        refreshLayout?.setOnRefreshListener { refreshlayout ->
//            setGetUserInfo()
//        }

        /**
         * 设置状态栏和关注聊天显示
         */
        initStatusBar()


        /**
         * 设置联动布局
         */
        init(view)

        /**
         * 请求回调
         */
        mineViewModel.getUserInfoState.observe(this) {
            when (it) {
                is GetUserInfoState.Success -> {
                    /**头像鉴权*/
                    Singleton.UserAvtarUrl =
                        Singleton.getUrlX(mineViewModel.mUserInfoBean!!.avatarUrl, true)
                    /**头像*/
                    Glide.with(requireActivity())
                        .asBitmap()
                        .dontAnimate()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .error(R.drawable.temp_icon)
                        .load(Singleton.UserAvtarUrl)
                        .into(mAvatar)
                    /**顶部头像*/
                    Glide.with(requireActivity())
                        .asBitmap()
                        .dontAnimate()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .error(R.drawable.temp_icon)
                        .load(Singleton.UserAvtarUrl)
                        .into(mTopIcon)

                    if (mineViewModel.mUserInfoBean!!.backgroundUrl != null) {
                        var coverUrl = Singleton.getUrlX(
                            mineViewModel.mUserInfoBean!!.backgroundUrl.toString(),
                            true,
                            100
                        )
                        /**封面*/
                        Glide.with(requireActivity())
                            .asBitmap()
                            .dontAnimate()
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .circleCrop()
                            .error(R.drawable.temp_icon)
                            .load(coverUrl)
                            .into(ivUnityCover!!)
                    }
                    /**昵称*/
                    mNickName.text = mineViewModel.mUserInfoBean!!.nickName
                    /**腰果id*/
                    mNumber.text = mineViewModel.mUserInfoBean!!.ygId
                    /**粉丝数*/
                    mFansNum.text = Singleton.setNumRuler(
                        (mineViewModel.mUserInfoBean!!.followerCount).toInt(),
                        "0"
                    )
                    /**关注数*/
                    mFollowNum.text = Singleton.setNumRuler(
                        (mineViewModel.mUserInfoBean!!.followCount).toInt(),
                        "0"
                    )
                    /**获赞数*/
                    mPraiseAndCollect.text = Singleton.setNumRuler(
                        (mineViewModel.mUserInfoBean!!.feedLaudedCount + mineViewModel.mUserInfoBean!!.feedCollectedCount).toInt(),
                        "0"
                    )
                    /**性别*/
                    if (mineViewModel.mUserInfoBean!!.gender == 0) {
                        mGender.setImageResource(R.drawable.ic_female)
                    } else {
                        mGender.setImageResource(R.drawable.ic_male)
                    }

                    /**是否显示位置 不为空则表示设置的为显示*/
                    if (mineViewModel.mUserInfoBean!!.country != null || mineViewModel.mUserInfoBean!!.province != null || mineViewModel.mUserInfoBean!!.city != null) {
                        mAddress.visibility = View.VISIBLE
                        if (mineViewModel.mUserInfoBean!!.country != null && mineViewModel.mUserInfoBean!!.province != null && mineViewModel.mUserInfoBean!!.city != null) {
                            mAddress.text =
                                " ${Singleton.getUserInfo().province?:""} ${Singleton.getUserInfo().city ?: ""} "
                        } else {
                            mAddress.text =
                                " ${Singleton.getUserInfo().country?: ""} ${Singleton.getUserInfo().province ?: ""} ${Singleton.getUserInfo().city ?: ""} "
                        }
                    } else {
                        mAddress.visibility = View.GONE
                    }

                    /**是否显示星座 不为空则表示设置的为显示*/
                    if (mineViewModel.mUserInfoBean!!.constellation != null || mineViewModel.mUserInfoBean!!.age != null) {
                        mAge.visibility = View.VISIBLE
                        if (mineViewModel.mUserInfoBean!!.constellation != null) {
                            mAge.text = mineViewModel.mUserInfoBean!!.constellation.toString()
                        }
                        if (mineViewModel.mUserInfoBean!!.age != null) {
                            mAge.text = mineViewModel.mUserInfoBean!!.age.toString() + "岁"
                        }
                    } else {
                        mAge.visibility = View.GONE
                    }

                    when (isMine) {
                        0 -> {
                            /**编辑个性签名*/
                            if (mineViewModel.mUserInfoBean!!.descript.equals("")) {
                                mSignature.text = "请用一句话介绍自己"
                                mSignature.setTextColor(Color.parseColor("#7F7F7F"))
                                /**
                                 * 在没有设置签名的情况下点击文本 也可跳转到编辑个性签名
                                 */
                                mSignature.setOnClickListener {
                                    val intent =
                                        Intent(requireActivity(), EditUserInfoActivity::class.java)
                                    intent.putExtra("title", "编辑签名")
                                    intent.putExtra("tag", 1)
                                    startActivity(intent)
                                }
                            } else {
                                mSignature.text = mineViewModel.mUserInfoBean!!.descript.toString()
                                mSignature.setTextColor(Color.parseColor("#464646"))
                                mSignatureImg.visibility = View.GONE
                            }

                            /**
                             * 编辑图标也可以点击
                             */
                            mSignatureImg.setOnClickListener {
                                val intent =
                                    Intent(requireActivity(), EditUserInfoActivity::class.java)
                                intent.putExtra("title", "编辑签名")
                                intent.putExtra("tag", 1)
                                startActivity(intent)
                            }
                        }

                        1 -> {
                            /**
                             * 个性签名
                             */
                            if (mineViewModel.mUserInfoBean!!.descript.equals("")) {
                                mSignature.text = "这个人很懒，什么都没留下……"
                                mSignature.setTextColor(Color.parseColor("#464646"))
                            } else {
                                mSignature.text = mineViewModel.mUserInfoBean!!.descript.toString()
                                mSignature.setTextColor(Color.parseColor("#464646"))
                            }

                            /**
                             * 关注状态
                             */
                            when (mineViewModel.mUserInfoBean!!.relation) {//关系 0:未关注 1:我关注他 2:他关注我 3:互相关注
                                0L -> {
                                    Log.e("TAGzqr", "进入0")
                                    //发消息 关注
                                    setCareState(0)
                                }

                                1L -> {
                                    Log.e("TAGzqr", "进入1")
                                    //已关注 聊天
                                    setCareState(1)
                                }

                                2L -> {
                                    Log.e("TAGzqr", "进入2")
                                    //发消息 关注
                                    setCareState(0)
                                }

                                3L -> {
                                    Log.e("TAGzqr", "进入3")
                                    //已关注 聊天
                                    setCareState(1)
                                }
                            }
                        }
                    }
                    Singleton.setRefreshResult(refreshLayout!!, 1)
                    // 刷新完成后，调用setRefreshing(false)来停止刷新动画
//                    swipeRefresh!!.isRefreshing = false

                    if (viewpagerAdapter != null) {
                        val currentPosition = viewPager!!.currentItem
                        viewpagerAdapter = null
                        fragments.clear()

                        /**设置fragment实例*/
                        setFragment()


                        /**viewpager适配器*/
                        viewpagerAdapter = ViewPagerFragmentAdapter(childFragmentManager, fragments)
                        viewPager!!.adapter = viewpagerAdapter
                        magicIndicator!!.bindViewPager(viewPager!!, mTitleList)
                        /**刷新当前tab*/
                        viewpagerAdapter!!.notifyDataSetChanged()
                        viewPager!!.currentItem = currentPosition
                    } else {
                        /**设置fragment实例*/
                        setFragment()
                        /**viewpager适配器*/
                        viewpagerAdapter = ViewPagerFragmentAdapter(childFragmentManager, fragments)
                        viewPager!!.adapter = viewpagerAdapter
                        magicIndicator!!.bindViewPager(viewPager!!, mTitleList)
                    }

                }

                is GetUserInfoState.Fail -> {
                    // 刷新完成后，调用setRefreshing(false)来停止刷新动画
//                    swipeRefresh!!.isRefreshing = false
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
                    Singleton.setRefreshResult(refreshLayout!!, 3)
                }
            }
        }

        /**
         * 判断是自己还是他人  需要做出的不同判断
         */
        when (isMine) {
            0 -> {
                /**点击头像*/
                mTopIcon.setOnClickListener {
                    if (userInfo == null) {
                        return@setOnClickListener
                    }
                    val intent = Intent(context, IconCropActivity::class.java)
                    intent.putExtra("iconUrl", Singleton.UserAvtarUrl)
                    startActivity(intent)
                }
                /**点击顶部头像*/
                mAvatar.setOnClickListener {
                    if (userInfo == null) {
                        return@setOnClickListener
                    }
                    val intent = Intent(context, IconCropActivity::class.java)
                    intent.putExtra("iconUrl", Singleton.UserAvtarUrl)
                    startActivity(intent)
                }
                /**显示编辑资料*/
                llEditSetting!!.visibility = View.VISIBLE
                /**显示顶部分享*/
                topShare.visibility = View.VISIBLE
                // TODO:  这里要做分享的点击弹窗
                topShare.singleClick {
                    //获取私信推荐用户
                    if (mineViewModel.mUserInfoBean == null) {
                        ToastUtils.showShort("请稍后...")
                        return@singleClick
                    }
                    shareViewModel.getRecommendShareUserList()
                }
                /**跳转二维码*/
                mIvQrcode.setOnClickListener {
                    val intent = Intent(requireActivity(), QrCodeActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                }
                /**编辑资料*/
                mEditProfile.setOnClickListener {
                    val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                    startActivity(intent)
                }
                /**设置*/
                mSetting.setOnClickListener {
                    val intent = Intent(requireActivity(), UserInfoSetActivity::class.java)
                    startActivity(intent)
                }
                /**点击粉丝*/
                view.findViewById<View>(R.id.ll_fans).setOnClickListener {
                    val intent = Intent(requireActivity(), UserCenterFansActivity::class.java)
                    intent.putExtra("userId", Singleton.getUserInfo().userId)
                    requireActivity().startActivity(intent)
                }
            }

            1 -> {//他人主页
                /**隐藏签名图标*/
                mSignatureImg.visibility = View.GONE
                /**隐藏二维码*/
                mIvQrcode.visibility = View.GONE
                /**显示返回图标 点击返回*/
                mTopBack.visibility = View.VISIBLE//显示返回图标
                mTopBack.setOnClickListener {
                    requireActivity().finish()
                }
                topShare.visibility = View.VISIBLE
                topShare.singleClick {
                    //获取私信推荐用户
                    if (mineViewModel.mUserInfoBean == null) {
                        ToastUtils.showShort("请稍后...")
                        return@singleClick
                    }
                    shareViewModel.getRecommendShareUserList()
                }
                /**关注聊天逻辑*/
                /**点击关注 这里是未关注的状态*/
                followLl!!.setOnClickListener {
                    commonViewModel.getFollowUser(userId)
                }

                // TODO: 点击聊天 这里是未关注的聊天 应该是只能发送一条消息
                chatLl!!.setOnClickListener {
                    ChartActivity.start(requireActivity(), userId.toString())
                }

                // TODO: 点击聊天 这里是关注后的聊天
                llSendMessage!!.setOnClickListener {
                    ChartActivity.start(requireActivity(), userId.toString())
                }
                /**这里是已经关注的图标  点击之后取消关注*/
                llCare!!.setOnClickListener {
                    commonViewModel.getUnfollowUser(userId)
                }

                /**顶部关注聊天逻辑*/
                /**点击关注 这里是未关注的状态*/
                topCareLl!!.setOnClickListener {
                    commonViewModel.getFollowUser(userId)
                }

                // TODO: 点击聊天 这里是未关注的聊天 应该是只能发送一条消息
                topChatLl!!.setOnClickListener {
                    ChartActivity.start(requireActivity(), userId.toString())
                }

                // TODO: 点击聊天 这里是关注后的聊天
                topSendLl!!.setOnClickListener {
                    ChartActivity.start(requireActivity(), userId.toString())
                }
                /**这里是已经关注的图标  点击之后取消关注*/
                topCareLl2!!.setOnClickListener {
                    commonViewModel.getUnfollowUser(userId)
                }
                /**点击粉丝*/
                view.findViewById<View>(R.id.ll_fans).setOnClickListener {
                    val intent = Intent(requireActivity(), UserCenterFansActivity::class.java)
                    intent.putExtra("userId", userId)
                    requireActivity().startActivity(intent)
                }

            }
        }

        /**
         * 请求结果   关注
         */
        commonViewModel.followUserBeanState.observe(this) {
            when (it) {
                is FollowUserBeanState.Success -> {
                    Singleton.centerToast(requireActivity(), Singleton.CARE_TEXT)
                    setCareState(1)
                }

                is FollowUserBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
                }
            }
        }

        /**
         * 请求结果  取消关注
         */
        commonViewModel.unfollowUserBeanState.observe(this) {
            when (it) {
                is UnfollowUserBeanState.Success -> {
                    Singleton.centerToast(requireActivity(), Singleton.UN_CARE_TEXT)
                    setCareState(0)
                }

                is UnfollowUserBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
                }
            }
        }

        /**
         * 上二楼
         */
        val header: TwoLevelHeader = view.findViewById(R.id.header)
        refreshLayout!!.setOnMultiListener(object : OnMultiListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                /**请求写在这里*/
                setGetUserInfo()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {

            }

            @SuppressLint("RestrictedApi")
            override fun onStateChanged(
                refreshLayout: RefreshLayout,
                oldState: RefreshState,
                newState: RefreshState,
            ) {
                if (oldState === RefreshState.TwoLevel) {
                    view.findViewById<View>(R.id.second_floor_content).animate().alpha(0f)
                        .setDuration(1000)
                }
            }

            override fun onHeaderMoving(
                header: RefreshHeader?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int,
            ) {
                /* Log.e("zqr-------1", "是否在拖动：isDragging: $isDragging" )
                 Log.e("zqr-------1", "当前位置百分比：percent: $percent" )
                 Log.e("zqr-------1", "当前位置：offset: $offset" )
                 Log.e("zqr-------1", "头部高度：headerHeight: $headerHeight" )
                 Log.e("zqr-------1", "最大拖动高度：maxDragHeight: $maxDragHeight" )*/
                /**重要逻辑 拖动时 头部背景图片放大*/
                moveTextBasedOnIntValue(offset)
            }

            override fun onHeaderReleased(
                header: RefreshHeader?,
                headerHeight: Int,
                maxDragHeight: Int,
            ) {
                /*Log.e("zqr-------2", "头部高度：headerHeight: $headerHeight" )
                Log.e("zqr-------2", "最大拖动高度：maxDragHeight: $maxDragHeight" )*/
            }

            override fun onHeaderStartAnimator(
                header: RefreshHeader?,
                headerHeight: Int,
                maxDragHeight: Int,
            ) {


            }

            override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {

            }

            override fun onFooterMoving(
                footer: RefreshFooter?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                footerHeight: Int,
                maxDragHeight: Int,
            ) {
            }

            override fun onFooterReleased(
                footer: RefreshFooter?,
                footerHeight: Int,
                maxDragHeight: Int,
            ) {
            }

            override fun onFooterStartAnimator(
                footer: RefreshFooter?,
                footerHeight: Int,
                maxDragHeight: Int,
            ) {
            }

            override fun onFooterFinish(footer: RefreshFooter?, success: Boolean) {
            }

        })
        header.setOnTwoLevelListener(object : OnTwoLevelListener {
            override fun onTwoLevel(refreshLayout: RefreshLayout): Boolean {
                startUnity()
                return false //true 将会展开二楼状态 false 关闭刷新
            }
        })

        /**点击封面  打开二楼*/
        ivUnityCover!!.setOnClickListener {
            header.openTwoLevel(true)//打开二楼
        }

        //初始化分享相关
        initShareData()


    }

    override fun onResume() {
        super.onResume()
        /**
         * 请求数据
         */
        setGetUserInfo()
    }

    private fun setFragment() {
        if (fragments.size == 0) {
            (0 until mTitleList.size).forEach {

                when (it) {
                    1 -> {//收藏额外写一个fragment
                        val f = MineCollectFragment()
                        val bundle = Bundle()
                        if (userId == 0L) {
                            bundle.putLong("targetId", Singleton.getLoginInfo().userId)
                        } else {
                            bundle.putLong("targetId", userId)
                        }
                        bundle.putString("title", mTitleList[it])
                        bundle.putString("collectKey", "帖子")
                        f.arguments = bundle
                        fragments.add(f)
                    }

                    else -> {//帖子和赞过  用一个fragment
                        val f = MinePostFragment()
                        val bundle = Bundle()
                        if (userId == 0L) {
                            bundle.putLong("targetId", Singleton.getLoginInfo().userId)
                        } else {
                            bundle.putLong("targetId", userId)
                        }
                        bundle.putString("title", mTitleList[it])
                        bundle.putString("collectKey", "帖子")
                        f.arguments = bundle
                        fragments.add(f)
                    }
                }
            }
        }

    }

    fun startUnity() {
        when (isMine) {
            0 -> {//进入自己的衣橱
                Singleton.isNewScene = true
                val intent = Intent(requireActivity(), AndroidBridgeActivity::class.java)
                intent.putExtra("entryType", "EnterMyWardrobe")
                requireActivity().startActivity(intent)
            }

            1 -> {//进入他人衣橱
                Singleton.isNewScene = true
                val intent = Intent(requireActivity(), AndroidBridgeActivity::class.java)
                intent.putExtra("entryType", "EnterOthersWardrobe")
                intent.putExtra("userId", userId.toString())
                requireActivity().startActivity(intent)
            }
        }
    }

    /**
     * 拖动联动
     */
    private var currentHeight = 0
    fun moveTextBasedOnIntValue(value: Int) {
        //跟随移动 现在不用
//        Log.e("zqr-------3", "当前位置：value: $value")
        val translationY = when {
            value > 0 -> value.toFloat() // 根据输入的 int 值增大而向下移动
            value < 0 -> -value.toFloat() // 根据输入的 int 值减小而向上移动
            else -> 0f // 如果输入的 int 值为 0，则不进行移动
        }
        ivUserCenterTag!!.translationY = translationY
//        tabLayout!!.translationY = translationY
//        viewPager!!.translationY = translationY


        if (currentHeight == 0) {
            currentHeight = ivUnityCover!!.height
        }
        val newHeight = currentHeight + value // 将当前高度与增加值相加得到新的高度
        val layoutParams = ivUnityCover!!.layoutParams
        layoutParams.height = newHeight
        ivUnityCover!!.layoutParams = layoutParams
    }


    /**
     * 设置他人主页的关注状态  用于布局的显示隐藏
     */
    private fun setCareState(state: Int) {
        when (state) {
            0 -> {//未关注
                topCareChat!!.visibility = View.VISIBLE
                followChat!!.visibility = View.VISIBLE

                messageCare!!.visibility = View.GONE
                topSendCare!!.visibility = View.GONE
            }

            1 -> {//已关注
                messageCare!!.visibility = View.VISIBLE
                topSendCare!!.visibility = View.VISIBLE

                topCareChat!!.visibility = View.GONE
                followChat!!.visibility = View.GONE
            }
        }
    }


    /**
     * 请求个人数据
     */
    private fun setGetUserInfo() {
        if (userId == 0L) {
            mineViewModel.getUserInfo(null)
        } else {
            mineViewModel.getUserInfo(userId)
            mTopBack.visibility = View.VISIBLE//显示返回键
        }
    }

    /**
     * 初始化状态栏
     */
    private fun initStatusBar() {
        mStatusHeight.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            BarUtils.getStatusBarHeight()
        )

        mToolbar.updateLayoutParams<CollapsingToolbarLayout.LayoutParams> {
            topMargin = BarUtils.getStatusBarHeight() + SizeUtils.dp2px(1f)
        }

        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val ratio = Math.abs(verticalOffset) * 1.0f / appBarLayout.totalScrollRange
            mStatusHeight.setBackgroundColor(
                ColorUtils.blendARGB(
                    Color.TRANSPARENT,
                    Color.parseColor("#7D7D7D"),
                    ratio
                )
            )
            mTopBar.setBackgroundColor(
                ColorUtils.blendARGB(
                    Color.TRANSPARENT,
                    Color.parseColor("#7D7D7D"),
                    ratio
                )
            )
            llTabLayout!!.setBackgroundColor(
                ColorUtils.blendARGB(
                    Color.TRANSPARENT,
                    Color.parseColor("#7D7D7D"),
                    ratio
                )
            )
            //mTopBarNickname.visibility = if (ratio >= 0.5) View.VISIBLE else View.INVISIBLE
            mTopIcon.visibility = if (ratio >= 0.5) View.VISIBLE else View.INVISIBLE
            //顶部显示的是谁 就给谁动画  这里用你布局包裹  在请求的时候 在对应包裹内的布局显示即可
            if (isMine != 0) {
                topShare!!.visibility = if (ratio >= 0.5) View.INVISIBLE else View.VISIBLE
                topCareChatLl!!.visibility = if (ratio >= 0.5) View.VISIBLE else View.INVISIBLE
            }
        })
    }


    /**
     * 显示点赞和收藏统计弹窗
     */
    private var popupWindow: PopupWindow? = null
    private fun showPraiseAndCollectPop() {
        val inflateView: View = layoutInflater.inflate(R.layout.dialog_laud_collect, null)
        /**
         * pop实例
         */
        popupWindow = PopupWindow(
            inflateView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        inflateView.findViewById<TextView>(R.id.laud_num).text =
            " ${mineViewModel.mUserInfoBean!!.feedLaudedCount}"
        inflateView.findViewById<TextView>(R.id.collect_num).text =
            " ${mineViewModel.mUserInfoBean!!.feedCollectedCount}"
        inflateView.findViewById<TextView>(R.id.tv_know).setOnClickListener {
            //取消
            popupWindow!!.dismiss()
        }

        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.isTouchable = true
        popupWindow!!.isFocusable = true
        popupWindow!!.showAtLocation(inflateView, Gravity.CENTER, 0, 0)
        Singleton.bgAlpha(requireActivity(), 0.5f) //设置透明度0.5
        popupWindow!!.setOnDismissListener {
            Singleton.bgAlpha(requireActivity(), 1f) //恢复透明度
        }
        //在pause时关闭
        Singleton.lifeCycleSet(requireActivity(), popupWindow!!)
    }


    /**
     * tab+viewpager联动
     */
    private var viewpagerAdapter: ViewPagerFragmentAdapter? = null
    private var viewPager: ViewPager? = null
    private var magicIndicator: MagicIndicator? = null
    var fragments: MutableList<Fragment> = arrayListOf()//fragment集合
    var mTitleList: MutableList<String> = arrayListOf()//标题集合
    private fun init(view: View) {
        viewPager = view.findViewById(R.id.view_pager)
        magicIndicator = view.findViewById<MagicIndicator>(R.id.magic_indicator)
        //前两个是关注和推荐
        if (mTitleList.size == 0) {
            mTitleList.add("帖子")
            mTitleList.add("收藏")
            if (isMine == 0) {
                mTitleList.add("赞过")
            }
        }

    }

    /**
     * 指示器绑定viewpager
     */
    fun MagicIndicator.bindViewPager(
        viewPager: ViewPager,
        mStringList: List<String> = arrayListOf(),
        action: (index: Int) -> Unit = {},
    ) {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.isAdjustMode = true//是否平分屏幕宽度，默认为 true，设为 false 则表示不平分
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mStringList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return HomeSearchDetailsActivity.ScaleTransitionPagerTitleView(activity!!).apply {
                    //设置文本
                    text = mStringList[index].toHtml()
                    //设置为粗体
                    //background=resources.getDrawable()

                    //字体大小
                    textSize = 16.0f
                    //不缩放效果
                    minScale = 1.0f
                    //未选中颜色
                    normalColor = ContextCompat.getColor(
                        getContext(),
                        R.color.color_pitch2
                    )
                    //选中颜色
                    selectedColor = ContextCompat.getColor(getContext(), R.color.color_pitch_ed2)

                    //点击事件
                    setOnClickListener {
                        viewPager.currentItem = index
                        action.invoke(index)
                    }

                }
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    //mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    //线条的宽高度
                    lineHeight = UIUtil.dip2px(activity, 2.0).toFloat()
//                    lineWidth = UIUtil.dip2px(activity, 100.0).toFloat()
                    //// 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
                    yOffset = UIUtil.dip2px(activity, 0.0).toFloat()
                    //线条的圆角
                    roundRadius = UIUtil.dip2px(activity, 0.0).toFloat()
                    //设置拉长动画
                    // startInterpolator = AccelerateInterpolator()
                    // endInterpolator = DecelerateInterpolator(0.5f)
                    //线条的颜色
                    setColors(
                        ContextCompat.getColor(
                            getContext(),
                            cn.dreamfruits.yaoguo.R.color.color_tab_indicator_line3
                        )
                    )
                }
            }
        }

        this.navigator = commonNavigator

        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                this@bindViewPager.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                this@bindViewPager.onPageSelected(position)
                action.invoke(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                this@bindViewPager.onPageScrollStateChanged(state)
            }
        })
    }

    /**
     * 设置标题文字
     */
    class ScaleTransitionPagerTitleView(context: Context) : ColorTransitionPagerTitleView(context) {
        //设置为粗体
        override fun setTypeface(tf: Typeface?) {
            super.setTypeface(Typeface.DEFAULT)
        }

        var minScale = 0.7f
        override fun onEnter(
            index: Int,
            totalCount: Int,
            enterPercent: Float,
            leftToRight: Boolean,
        ) {
            super.onEnter(index, totalCount, enterPercent, leftToRight)    // 实现颜色渐变
            scaleX = minScale + (1.0f - minScale) * enterPercent
            scaleY = minScale + (1.0f - minScale) * enterPercent
        }

        override fun onLeave(
            index: Int,
            totalCount: Int,
            leavePercent: Float,
            leftToRight: Boolean,
        ) {
            super.onLeave(index, totalCount, leavePercent, leftToRight)    // 实现颜色渐变
            scaleX = 1.0f + (minScale - 1.0f) * leavePercent
            scaleY = 1.0f + (minScale - 1.0f) * leavePercent
        }
    }

    fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, flag)
        } else {
            Html.fromHtml(this)
        }
    }

    private val chartViewModel by viewModels<ChartViewModel>()
    lateinit var mTvBlack: TextView
    private fun initShareData() {

        shareViewModel.shortUrl.observe(this)
        {
            when (it) {
                is ShareShortUrlState.Success -> {
                    if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_COPY) {

                        var content = "【腰果】${it.shortUrl}${
                            "@${mineViewModel.mUserInfoBean!!.nickName}"
                        }的腰果个人主页点击链接直接打开"
                        ClipboardUtils.copyText(content)
                        ToastUtils.showShort("复制成功，快去分享给好友吧")

                    } else if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_USER_CENTER) {
                        XPopup.Builder(requireContext())
                            .isDestroyOnDismiss(true)
                            .asCustom(
                                SharePersonalCenterPop(
                                    requireContext(),
                                    it.shortUrl
                                )
                            )
                            .show()
                    } else if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT
                        || it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT_CIRCLE
                        || it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_QQ
                    ) {
                        share(it.shareType, it.shortUrl)
                    }
                }

                is ShareShortUrlState.Fail -> {
                    ToastUtils.showShort(it.msg)
                }
            }
        }


        val shareAllPop = ShareAllPop(
            mContext = requireContext(),
            shareType = if (isMine == 0) TIMCommonConstants.SHARE_TYPE_USER else TIMCommonConstants.SHARE_TYPE_USER_OTHER,
            shareOnClick = object : ShareAllPop.ShareOnClick {

                override fun onClickBlack(tv_black: TextView) {
                    mTvBlack = tv_black
                    //0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
//                    if ()
                    black()

                }

                override fun onClickCreatePost() {

                    shareViewModel.getShortUrl(
                        mineViewModel.mUserInfoBean!!.userId.toString(),
                        TIMCommonConstants.SHARE_TYPE_USER,
                        TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_USER_CENTER
                    )
                }

                override fun onClickCopyLink() {
                    shareViewModel.getShortUrl(
                        mineViewModel.mUserInfoBean!!.userId.toString(),
                        TIMCommonConstants.SHARE_TYPE_USER,
                        TIMCommonConstants.SHARE_CLICK_TYPE_COPY
                    )
                }

                override fun onClickTipOff() {

                }

                override fun onClickShare(shareType: Int) {
                    shareViewModel.getShortUrl(
                        mineViewModel.mUserInfoBean!!.userId.toString(),
                        TIMCommonConstants.SHARE_TYPE_POST,
                        shareType
                    )
                }

                override fun onSendMsg(
                    userListSel: List<UserInfo>,
                    content: String,
                    shareType: Int,
                ) {
                    val customData: MutableMap<String, ShareUserEntity> =
                        mutableMapOf()
                    val gson = Gson()

                    var postEntity = shareUserEntity()

                    customData.put(
                        TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY,
                        postEntity
                    )
                    var userIdList: MutableList<String> = arrayListOf()
                    userListSel.forEach {
                        userIdList.add(it.id)
                    }
                    chartViewModel.sendCustomMsg(
                        gson.toJson(customData).toByteArray(),
                        userIdList,
                        content
                    )

                    XPopup.Builder(requireContext())
                        .asCustom(
                            LoadingPop(mContext = requireContext()).setTitle("发送中")
                                .setStyle(LoadingPop.Style.Spinner)
                        )
                        .show()
                        .delayDismissWith(1500) {
                            ToastUtils.showShort("发送成功")
                        }

                }

                //去私信好友
                override fun onClickToFriend() {
                    val customData: MutableMap<String, ShareUserEntity> =
                        mutableMapOf()
                    val gson = Gson()
                    var postEntity = shareUserEntity()
                    customData[TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY] =
                        postEntity
                    ShareToActivity.start(
                        requireContext(),
                        gson.toJson(customData)
                    )
                }
            }
        )

        shareViewModel.recomendUserList.observe(this) {
            when (it) {
                is ShareUseRecomendListState.Success -> {
                    if (it.recommendUserListBean != null)
                        shareAllPop.userList = it.recommendUserListBean

                    shareAllPop.blackRel = mineViewModel.mUserInfoBean!!.blackRelation

                    XPopup.Builder(requireContext())
                        .isDestroyOnDismiss(true)
                        .asCustom(
                            shareAllPop
                        )
                        .show()
                }

                is ShareUseRecomendListState.Fail -> {
                    ToastUtils.showShort(it.msg)
                }
            }
        }


        chartSetModel.setState.observe(this)
        {
            when (it) {
                is SetState.Success -> {

                    //0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
                    if (mineViewModel.mUserInfoBean!!.blackRelation == 1) {
                        mineViewModel.mUserInfoBean!!.blackRelation = 0
                    } else if (mineViewModel.mUserInfoBean!!.blackRelation == 2) {
                        mineViewModel.mUserInfoBean!!.blackRelation = 3
                    } else if (mineViewModel.mUserInfoBean!!.blackRelation == 3) {
                        mineViewModel.mUserInfoBean!!.blackRelation = 2
                    } else if (mineViewModel.mUserInfoBean!!.blackRelation == 0) {
                        mineViewModel.mUserInfoBean!!.blackRelation = 1
                    }

                    if (mineViewModel.mUserInfoBean!!.blackRelation == 0 ||mineViewModel.mUserInfoBean!!.blackRelation == 2) {
                        mTvBlack.text="拉黑"
                    } else {
                        mTvBlack.text = "取消拉黑"
                    }

                    shareAllPop.blackRel = mineViewModel.mUserInfoBean!!.blackRelation

                }
                is SetState.Fail -> {
                    ToastUtils.showShort("${it.code}----" + it.errorMsg)
                }
            }
        }


    }

    private fun shareUserEntity(): ShareUserEntity {

        var avatarUrlOrigin =
            if (mineViewModel.mUserInfoBean!!.avatarUrl.contains("?sign")) {
                mineViewModel.mUserInfoBean!!.avatarUrl.split("?sign")[0]
            } else
                mineViewModel.mUserInfoBean!!.avatarUrl


        return ShareUserEntity(
            avatarUrl = avatarUrlOrigin,
            nickName = mineViewModel.mUserInfoBean!!.nickName,
            userId = mineViewModel.mUserInfoBean!!.userId.toString(),
            descript = if (TextUtils.isEmpty(mineViewModel.mUserInfoBean!!.descript)) {
                ""
            } else {
                mineViewModel.mUserInfoBean!!.descript!!
            },
            followerCount = mineViewModel.mUserInfoBean!!.followerCount,
            feedCount = mineViewModel.mUserInfoBean!!.feedCount,
            backgroundUrl = mineViewModel.mUserInfoBean!!.backgroundUrl.toString()

        )
    }

    private fun share(shareType: Int, shortUrl: String) {

        var content = ShareWebContent()
        content.title = "@" + mineViewModel.mUserInfoBean!!.nickName + "的个人主页"

        content.description =
            "万千好物一键上身，美好生活即刻分享"
        Thread {
            AndroidDownloadManager(
                requireActivity(),
                mineViewModel.mUserInfoBean!!.avatarUrl.decodePicUrls()
            )
                .setListener(object : AndroidDownloadManagerListener {
                    override fun onPrepare() {
                        Log.d("downloadVideo", "onPrepare")
                    }

                    override fun onSuccess(path: String) {
                        val thumbBmp: Bitmap = ImageUtils.getBitmap(path)
                        LogUtils.e(">>> " + path)
                        content.img = thumbBmp
                        content.webPageUrl = shortUrl
//                        if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_QQ) {
                        var file = ImageUtils.save2Album(
                            ImageUtils.getBitmap(path),
                            Bitmap.CompressFormat.JPEG
                        )
                        content.url = file!!.path
//                        }
//                        FileUtils.delete(path)

                        shareCallback(shareType, content)
                    }

                    override fun onFailed(throwable: Throwable?) {
                        ToastUtils.showShort("图片下载失败，请重新下载分享！")
                        Log.e("downloadVideo", "onFailed", throwable)
                    }
                }).download()
        }.start()

    }

    private fun shareCallback(
        shareType: Int,
        content: ShareWebContent,
    ) {
        Social.share(requireContext(),
            if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT) PlatformType.WEIXIN
            else if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT_CIRCLE) PlatformType.WEIXIN_CIRCLE
            else PlatformType.QQ,
            content = content,
            onSuccess = { type ->
                LogUtils.e(">>>> onSuccess = type = " + type)
                when (type) {
                    PlatformType.WEIXIN -> {
                        ToastUtils.showShort("分享成功")
                    }

                    PlatformType.QQ -> {
                        ToastUtils.showShort("分享成功")
                    }

                    PlatformType.WEIXIN_CIRCLE -> {
                        ToastUtils.showShort("分享成功")
                    }

                    PlatformType.QQ_ZONE -> TODO()
                    PlatformType.SINA_WEIBO -> TODO()
                    PlatformType.ALI -> TODO()
                }
            },
            onCancel = {
                LogUtils.e(">>>> onCancel = 用户取消 ")
                ToastUtils.showShort("用户取消")
            },
            onError = { _, _, msg ->
                LogUtils.e(">>>> onError =  $msg")
                ToastUtils.showShort("$msg")
            }
        )
    }

    private val chartSetModel by viewModels<ChartSetModel>()
    fun black() {

        //0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
        if (mineViewModel.mUserInfoBean!!.blackRelation == 1 || mineViewModel.mUserInfoBean!!.blackRelation == 3) {
            chartSetModel.addToBlackList(
                mineViewModel.mUserInfoBean!!.userId.toString(),
                operation = 1
            )
        } else
            XPopup.Builder(requireContext())
                .asCustom(
                    ConfrimPop(requireContext(),
                        content = "拉黑后，对方将无法搜到你\n也不能再给你发消息",
                        leftString = "在忍忍",
                        rightString = "确认拉黑",
                        onLeftClick = {

                        }, onRightClick = {
                            chartSetModel.addToBlackList(mineViewModel.mUserInfoBean!!.userId.toString())
                        })
                )
                .show()
    }


}