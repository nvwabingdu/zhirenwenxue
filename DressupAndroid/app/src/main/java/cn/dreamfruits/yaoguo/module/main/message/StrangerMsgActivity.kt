package cn.dreamfruits.yaoguo.module.main.message

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.login.DeleteConversionState
import cn.dreamfruits.yaoguo.module.main.home.state.ConversationListState
import cn.dreamfruits.yaoguo.module.main.home.state.IsStrangerBeanState
import cn.dreamfruits.yaoguo.module.main.message.adapter.MessageAdapter
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.reflect.TypeToken
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author Lee
 * @createTime 2023-07-03 18 GMT+8
 * @desc :
 */
class StrangerMsgActivity : BaseActivity() {

    private val messageViewModel by viewModels<MessageViewModel>()
    private var mRecyclerView: RecyclerView? = null
    private lateinit var mAdapter: MessageAdapter
    private lateinit var refreshLayout: SmartRefreshLayout

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, StrangerMsgActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun layoutResId(): Int = R.layout.activity_stranger_msg

    override fun initView() {
        refreshLayout = findViewById(R.id.srl_refresh)
        mRecyclerView = findViewById(R.id.rv_content)
        findViewById<ImageView>(R.id.iv_back)
            .setOnClickListener { finish() }

        mRecyclerView!!.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        mAdapter = MessageAdapter(this, object : MessageAdapter.OnChildItemClick {

            override fun onDelete(item: ConversionEntity, position: Int) {
                LogUtils.e(">>>> item = " + " --- pos = " + position)
//                mAdapter.removeAt(position - 1)
                messageViewModel.deleteConversation(
                    item.conversation!!.conversationID,
                    position
                )
            }

            override fun onToTop(item: ConversionEntity, position: Int) {
                LogUtils.e(">>>> item = " + " --- pos = " + position)
            }

            override fun onCancelTop(item: ConversionEntity, position: Int) {
                LogUtils.e(">>>> item = " + " --- pos = " + position)
            }

            override fun onContent(item: ConversionEntity, position: Int) {
                LogUtils.e(">>>> item111 = " + " --- pos = " + position)
                ChartActivity.start(this@StrangerMsgActivity, item.conversation!!.userID)
            }
        })

        mRecyclerView!!.adapter = mAdapter

        refreshLayout.setOnRefreshListener { refreshlayout ->
            messageViewModel.getConversationList(page)
        }

        /**
         * 4.上拉加载
         */
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            //请求聊天信息 IM
            messageViewModel.getConversationList(page)
        }
    }

    var page = 100

    override fun initData() {
        messageViewModel.getConversationList(page)

        messageViewModel.conversation.observe(this) {
            when (it) {
                is ConversationListState.Success -> {
                    val list: MutableList<ConversionEntity> = arrayListOf()
                    val listId: MutableList<String> = arrayListOf()

                    it.v2TIMConversationResult.conversationList.forEach {
                        listId.add(it.userID)
                        val item = ConversionEntity()
                        item.headerImgUrl = it.faceUrl
                        item.conversation = it
                        list.add(item)
                    }
                    messageViewModel.getIsStranger(GsonUtils.toJson(listId), list)
                }
                is ConversationListState.Fail -> {
                    LogUtils.e(">>>> code = ${it.code}", "desc = ${it.errorMsg}")
                }
            }
        }


        messageViewModel.isStrangerBeanState.observe(this) {
            when (it) {
                is IsStrangerBeanState.Success -> {

                    LogUtils.e(">>>>> ids = " + it.isStrangerBean.ids)
                    if (TextUtils.isEmpty(it.isStrangerBean.ids)) {
                        mAdapter.setList(it.list)
                        refreshLayout.finishLoadMore(true)
                    } else {
                        val type = object : TypeToken<MutableList<String>>() {}.type
                        var list: MutableList<String> =
                            GsonUtils.fromJson(it.isStrangerBean.ids, type)
                        val listConversation: MutableList<ConversionEntity> = arrayListOf()
                        val listStranger: MutableList<ConversionEntity> = arrayListOf()
                        it.list.forEach { conversion ->
                            LogUtils.e(">>> userID =" + conversion.conversation!!.userID)
                            var find = list.find { conversion.conversation!!.userID == it }
                            if (find == null) {
                                listConversation.add(conversion)
                            } else {
                                listStranger.add(conversion)
                            }
                        }

                        mAdapter.setList(listStranger)
                        refreshLayout.finishLoadMore(true)
                    }
                    refreshLayout.finishRefresh()
                }
                is IsStrangerBeanState.Fail -> {

                }

            }
        }
        //删除会话结果
        messageViewModel.deleteConversation.observe(this) {
            when (it) {
                is DeleteConversionState.Success -> {
                    mAdapter.removeAt(it.position)

                }
                is DeleteConversionState.Fail -> {
                    ToastUtils.showShort("[${it.code}],错误：" + it.errorMsg)
                }
            }
        }

    }
}