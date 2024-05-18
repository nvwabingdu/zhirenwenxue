package cn.dreamfruits.yaoguo.module.main.message

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.login.BlackListState
import cn.dreamfruits.yaoguo.module.login.DeleteFromBlackListState
import cn.dreamfruits.yaoguo.module.login.SetState
import cn.dreamfruits.yaoguo.module.main.home.state.GetUserInfoState
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartSetModel
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.main.message.pop.ConfrimPop
import cn.dreamfruits.yaoguo.module.main.mine.otherusercenter.UserCenterActivity
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.util.loadRoundImg
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.zrqrcode.zxing.util.LogUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.tencent.imsdk.v2.*
import com.tencent.imsdk.v2.V2TIMFriendCheckResult.*


/**
 * @author Lee
 * @createTime 2023-06-29 12 GMT+8
 * @desc :
 */
class ChartSetActiivity : BaseActivity() {

    var userId = ""
    var conversationID = ""

    private lateinit var mHeaderImg: ImageView
    private lateinit var mIvSwitchBlack: ImageView
    private lateinit var mIvSwitchTop: ImageView
    private lateinit var mNickName: TextView

    private val chartSetModel by viewModels<ChartSetModel>()

    companion object {
        @JvmStatic
        fun start(context: Context, userId: String, conversationID: String) {
            val starter = Intent(context, ChartSetActiivity::class.java)
                .putExtra("userId", userId)
                .putExtra("conversationID", conversationID)
            context.startActivity(starter)
        }
    }

    override fun layoutResId(): Int = R.layout.activity_chart_set

    override fun initView() {
        mHeaderImg = findViewById(R.id.iv_head_img)
        mNickName = findViewById(R.id.tv_nick_name)
        mIvSwitchBlack = findViewById(R.id.iv_switch_black)
        mIvSwitchTop = findViewById(R.id.iv_switch_top)

        findViewById<TextView>(R.id.tv_report)
            .setOnClickListener {
                UserReportActivity.start(mContext, userId)
            }
        findViewById<ImageView>(R.id.iv_back)
            .setOnClickListener {
                finish()
            }
        findViewById<LinearLayout>(R.id.llayout_user)
            .setOnClickListener {
                Singleton.startOtherUserCenterActivity(this, userId.toLong())
            }

        findViewById<ImageView>(R.id.iv_switch_black)
            .setOnClickListener {

                if (mIvSwitchBlack.isSelected) {
                    chartSetModel.addToBlackList(userId, operation = 1)
                } else
                    XPopup.Builder(this)
                        .asCustom(
                            ConfrimPop(this,
                                content = "拉黑后，对方将无法搜到你\n也不能再给你发消息",
                                leftString = "在忍忍",
                                rightString = "确认拉黑",
                                onLeftClick = {
                                    LogUtils.e("")
                                }, onRightClick = {
                                    chartSetModel.addToBlackList(userId)
                                })
                        )
                        .show()
            }


//        chartSetModel.deleteFromBlackListState.observe(this) {
//            when (it) {
//                is DeleteFromBlackListState.Success -> {
//                    mIvSwitchBlack.isSelected = false
//                    mIvSwitchBlack.setImageResource(R.drawable.switch_un_sel)
//
//                }
//                is DeleteFromBlackListState.Fail -> {
//                    ToastUtils.showShort("${it.code}----" + it.errorMsg)
//                }
//            }
//        }

        chartSetModel.setState.observe(this)
        {
            when (it) {
                is SetState.Success -> {
                    if (it.operation == 0) {
                        mIvSwitchBlack.isSelected = true
                        mIvSwitchBlack.setImageResource(R.drawable.switch_sel)
                    } else {
                        mIvSwitchBlack.isSelected = false
                        mIvSwitchBlack.setImageResource(R.drawable.switch_un_sel)
                    }
                }
                is SetState.Fail -> {
                    ToastUtils.showShort("${it.code}----" + it.errorMsg)
                }
            }
        }

        chartSetModel.blackListState.observe(this)
        {
            when (it) {
                is BlackListState.Success -> {
                    val inBlackList = chartSetModel.isInBlackList(userId, it.list)
                    if (inBlackList) {
                        mIvSwitchBlack.isSelected = true
                        mIvSwitchBlack.setImageResource(R.drawable.switch_sel)
                    } else {
                        mIvSwitchBlack.isSelected = false
                        mIvSwitchBlack.setImageResource(R.drawable.switch_un_sel)
                    }
                }
                is BlackListState.Fail -> {
                    ToastUtils.showShort("${it.code}----" + it.errorMsg)
                }
            }
        }

        chartSetModel.getUserInfoState.observe(this) {
            when (it) {
                is GetUserInfoState.Success -> {
                    mHeaderImg.loadRoundImg(
                        this@ChartSetActiivity,
                        chartSetModel.mUserInfoBean!!.avatarUrl.decodePicUrls()
                    )
                    mNickName.text =
                        if (TextUtils.isEmpty(chartSetModel.mUserInfoBean!!.nickName))
                            chartSetModel.mUserInfoBean!!.userId.toString()
                        else
                            chartSetModel.mUserInfoBean!!.nickName

                    //0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
                    if (chartSetModel.mUserInfoBean!!.blackRelation == 1 || chartSetModel.mUserInfoBean!!.blackRelation == 3) {
                        mIvSwitchBlack.isSelected = true
                        mIvSwitchBlack.setImageResource(R.drawable.switch_sel)
                    } else {
                        mIvSwitchBlack.isSelected = false
                        mIvSwitchBlack.setImageResource(R.drawable.switch_un_sel)
                    }

                }
                is GetUserInfoState.Fail -> {
                    ToastUtils.showShort(it.errorMsg)
                }
            }
        }
    }

    override fun initData() {

        userId = intent
            .getStringExtra("userId")!!

        conversationID = intent
            .getStringExtra("conversationID")!!

        val userIDList: MutableList<String> = ArrayList()
        userIDList.add(userId)

//        V2TIMManager.getFriendshipManager().getFriendsInfo(
//            userIDList,
//            object : V2TIMValueCallback<List<V2TIMFriendInfoResult>> {
//                override fun onSuccess(listResult: List<V2TIMFriendInfoResult>) {
//                    if (!listResult.isNullOrEmpty()) {
//                        val first = listResult.first()
//
//                        val friendInfo = first.friendInfo
//
//                        val userProfile = friendInfo.userProfile
//
//                        mHeaderImg.loadRoundImg(this@ChartSetActiivity, userProfile.faceUrl)
//                        mNickName.text =
//                            if (TextUtils.isEmpty(userProfile.nickName)) userProfile.userID else userProfile.nickName
//                        LogUtils.e(">> relation =" + first.relation)
//
//                        when (first.relation) {
//                            //不是好友。
//                            V2TIM_FRIEND_RELATION_TYPE_BOTH_WAY -> {
//
//                            }
//                            //互为好友。
//                            V2TIM_FRIEND_RELATION_TYPE_BOTH_WAY -> {
//
//                            }
//                            //对方在我的好友列表中。
//                            V2TIM_FRIEND_RELATION_TYPE_IN_MY_FRIEND_LIST -> {
//
//                            }
//                            //我在对方的好友列表中。
//                            V2TIM_FRIEND_RELATION_TYPE_IN_OTHER_FRIEND_LIST -> {
//
//                            }
//                        }
//
//
//                    }
//                }
//
//
//                override fun onError(code: Int, desc: String) {
//                    // 检查好友关系失败
//                }
//            })
//        chartSetModel.getBlackList()
        chartSetModel.getUserInfo(userId.toLong())
    }
}