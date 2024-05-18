package cn.dreamfruits.yaoguo.module.share

import android.content.AbstractThreadedSyncAdapter
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.ShareWebContent
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemChildClickListener
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemClickListener
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.share.adapter.ShareUserRecomendAdapter
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.util.singleClick
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.core.BottomPopupView

/**
 * @author Lee
 * @createTime 2023-07-06 11 GMT+8
 * @desc :
 */
class ShareAllPop(
    var mContext: Context,
    var shareType: Int,
    var userList: List<UserInfo> = arrayListOf(),
    var shareOnClick: ShareOnClick,
    var blackRel: Int = 0,
    var mTag: Int = 0,//tag 0分享我的   1其他用户
) :
    BottomPopupView(mContext) {

    lateinit var rv_share_list: RecyclerView
    lateinit var tv_share_friend: TextView
    lateinit var tv_share_wechat_friend: TextView
    lateinit var tv_share_wechat_circle: TextView
    lateinit var tv_share_qq: TextView
    lateinit var tv_create_poster: TextView
    lateinit var tv_copy_link: TextView
    lateinit var tv_copy_tip_off: TextView
    lateinit var tv_black: TextView
    lateinit var btn_send: Button
    lateinit var et_content: EditText

    //wq
    private var tvEdit: TextView? = null
    private var tvChangeName: TextView? = null
    private var tvPrivate: TextView? = null
    private var tvDelete: TextView? = null
    private var tvPublic: TextView? = null

    lateinit var mAdapter: ShareUserRecomendAdapter


    var userListSel: MutableList<UserInfo> = arrayListOf()
    override fun getImplLayoutId(): Int = R.layout.layout_share_all_pop

    override fun onCreate() {
        super.onCreate()
        findViewById<ImageView>(R.id.iv_close).setOnClickListener { dismiss() }
        rv_share_list = findViewById(R.id.rv_share_list)
        tv_share_friend = findViewById(R.id.tv_share_friend)
        tv_share_wechat_friend = findViewById(R.id.tv_share_wechat_friend)
        tv_share_wechat_circle = findViewById(R.id.tv_share_wechat_circle)
        tv_share_qq = findViewById(R.id.tv_share_qq)
        tv_create_poster = findViewById(R.id.tv_create_poster)
        tv_copy_link = findViewById(R.id.tv_copy_link)
        tv_copy_tip_off = findViewById(R.id.tv_copy_tip_off)
        tv_black = findViewById(R.id.tv_black)
        //wq
        tvEdit = findViewById(R.id.tv_edit)
        tvChangeName = findViewById(R.id.tv_change_name)
        tvPrivate = findViewById(R.id.tv_private)
        tvDelete = findViewById(R.id.tv_delete)
        tvPublic = findViewById(R.id.tv_public)


        btn_send = findViewById(R.id.btn_send)
        et_content = findViewById(R.id.et_content)

        mAdapter = ShareUserRecomendAdapter(mContext)
        rv_share_list.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rv_share_list.adapter = mAdapter
        mAdapter.setList(userList)

        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

                if (mAdapter.data[position].isSel) {
                    mAdapter.data[position].isSel = false
                    userListSel.remove(mAdapter.data[position])
                } else {
                    if (userListSel.size >= 9) {
                        ToastUtils.showShort("最多只能同时分享9个用户")
                        return
                    }
                    mAdapter.data[position].isSel = true
                    userListSel.add(mAdapter.data[position])
                }
                if (userListSel.isEmpty()) {
                    btn_send.visibility = View.GONE
                    et_content.visibility = View.GONE
                } else {
                    btn_send.visibility = View.VISIBLE
                    et_content.visibility = View.VISIBLE
                }
                mAdapter.notifyItemChanged(position)
            }
        })


        btn_send.singleClick {
            dismiss()
            et_content.setText("")
            mAdapter.data.forEach { it.isSel = false }
            mAdapter.notifyDataSetChanged()
            btn_send.visibility = View.GONE
            et_content.visibility = View.GONE
            shareOnClick.onSendMsg(userListSel, et_content.text.toString(), shareType)
        }

        tv_copy_tip_off.singleClick {
            shareOnClick.onClickTipOff()
        }

        tv_create_poster.singleClick {
            dismiss()
            shareOnClick.onClickCreatePost()
        }

        tv_share_qq.singleClick {
            dismiss()
            shareOnClick.onClickShare(TIMCommonConstants.SHARE_CLICK_TYPE_QQ)
        }
        tv_share_wechat_circle.singleClick {
            dismiss()
            shareOnClick.onClickShare(TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT_CIRCLE)
        }
        tv_share_wechat_friend.singleClick {
            dismiss()
            shareOnClick.onClickShare(TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT)
        }

        tv_share_friend.singleClick {
            dismiss()
            shareOnClick.onClickToFriend()
        }
        tv_copy_link.singleClick {
            dismiss()
            shareOnClick.onClickCopyLink()
        }

        tv_black.singleClick {
            shareOnClick.onClickBlack(tv_black)
        }
        //wq
        tvEdit!!.singleClick {
            dismiss()
            shareOnClick.onClickEdit()
        }

        tvChangeName!!.singleClick {
            dismiss()
            shareOnClick.onClickChangeName()
        }

        tvPrivate!!.singleClick {
            dismiss()
            shareOnClick.onClickPrivate()
        }

        tvDelete!!.singleClick {
            dismiss()
            shareOnClick.onClickDelete()
        }

        tvPublic!!.singleClick {
            dismiss()
            shareOnClick.onClickPublic()
        }


        //分享话题时，只展示复制链接
        when (shareType) {
            TIMCommonConstants.SHARE_TYPE_TOPIC -> {
                tv_create_poster.visibility = View.GONE
                tv_copy_link.visibility = View.VISIBLE
                tv_copy_tip_off.visibility = View.GONE
                //分享自己的名片
            }
            TIMCommonConstants.SHARE_TYPE_USER -> {
                tv_create_poster.visibility = View.VISIBLE
                tv_copy_link.visibility = View.VISIBLE
                tv_copy_tip_off.visibility = View.GONE
                tv_black.visibility = View.GONE
                //分享他人的名片
            }
            TIMCommonConstants.SHARE_TYPE_USER_OTHER -> {
                tv_create_poster.visibility = View.VISIBLE
                tv_copy_link.visibility = View.VISIBLE
                tv_copy_tip_off.visibility = View.VISIBLE
                tv_black.visibility = View.VISIBLE

                //0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
                if (blackRel == 0 || blackRel == 2) {
                    tv_black.text = "拉黑"
                } else {
                    tv_black.text = "取消拉黑"
                }
            }
            TIMCommonConstants.SHARE_TYPE_EDIT_NAME -> {//wq
                tvEdit!!.visibility = View.VISIBLE
                tvChangeName!!.visibility = View.VISIBLE
                tvPrivate!!.visibility = View.VISIBLE
                tvDelete!!.visibility = View.VISIBLE
                tvPublic!!.visibility = View.VISIBLE

                tv_create_poster.visibility = View.VISIBLE
                tv_copy_link.visibility = View.VISIBLE
                tv_copy_tip_off.visibility = View.VISIBLE
                tv_black.visibility = View.GONE
            }
            else -> {
                tv_create_poster.visibility = View.VISIBLE
                tv_copy_link.visibility = View.VISIBLE

                if (mTag == 0) {

                    tv_copy_tip_off.visibility = View.GONE
                    tvDelete!!.visibility = View.VISIBLE
                } else {
                    tvDelete!!.visibility = View.GONE
                    tv_copy_tip_off.visibility = View.VISIBLE
                }

                tv_black.visibility = View.GONE
            }
        }

        tv_copy_tip_off.visibility = View.GONE

    }


    interface ShareOnClick {
        fun onClickCreatePost()
        fun onClickCopyLink()
        fun onClickTipOff()
        fun onClickShare(shareType: Int)
        fun onSendMsg(userListSel: List<UserInfo>, content: String, shareType: Int)
        fun onClickToFriend()

        fun onClickBlack(tv_black: TextView) {

        }

        //wq
        fun onClickEdit() {}
        fun onClickChangeName() {}
        fun onClickPrivate() {}
        fun onClickDelete() {}
        fun onClickPublic() {}
    }

//    private fun share(content: ShareWebContent, shareType: Int) {
//
//        Social.share(mContext,
//            if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT) PlatformType.WEIXIN
//            else if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT_CIRCLE) PlatformType.WEIXIN_CIRCLE
//            else PlatformType.QQ,
//            content = content,
//            onSuccess = { type ->
//                LogUtils.e(">>>> onSuccess = type = " + type)
//                when (type) {
//                    PlatformType.WEIXIN -> {
//                        ToastUtils.showShort("分享成功")
//                    }
//                    PlatformType.QQ -> {
//                        ToastUtils.showShort("分享成功")
//                    }
//                    PlatformType.WEIXIN_CIRCLE -> {
//                        ToastUtils.showShort("分享成功")
//                    }
//                    PlatformType.QQ_ZONE -> TODO()
//                    PlatformType.SINA_WEIBO -> TODO()
//                    PlatformType.ALI -> TODO()
//                }
//            },
//            onCancel = {
//                LogUtils.e(">>>> onCancel = 用户取消 ")
//                ToastUtils.showShort("用户取消")
//            },
//            onError = { _, _, msg ->
//                LogUtils.e(">>>> onError =  $msg")
//                ToastUtils.showShort("$msg")
//            }
//        )
//    }


}