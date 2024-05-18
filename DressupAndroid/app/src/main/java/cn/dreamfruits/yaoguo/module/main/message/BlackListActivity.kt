package cn.dreamfruits.yaoguo.module.main.message

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemChildClickListener
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.login.BlackListServerState
import cn.dreamfruits.yaoguo.module.login.SetState
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartSetModel
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author Lee
 * @createTime 2023-07-03 19 GMT+8
 * @desc :
 */
class BlackListActivity : BaseActivity() {

    private lateinit var mAdapter: BlackListAdapter
    private lateinit var emptyView: View

    private val messageViewModel by viewModels<MessageViewModel>()

    private val chartSetModel by viewModels<ChartSetModel>()

    var size = 10;
    var page = 0;
    var isRefresh = false
    var isLoadMore = false
    private lateinit var srl_refresh: SmartRefreshLayout

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, BlackListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun layoutResId(): Int = R.layout.activity_stranger_msg

    override fun initView() {

        findViewById<TextView>(R.id.tv_title)
            .text = "黑名单"

        srl_refresh = findViewById<SmartRefreshLayout>(R.id.srl_refresh)

        findViewById<ImageView>(R.id.iv_back)
            .setOnClickListener { finish() }

        emptyView = View.inflate(this, R.layout.layout_attch_goods_empty, null)
        emptyView.findViewById<ImageView>(R.id.iv_cover)
            .setImageResource(R.drawable.empty_black_list)
        emptyView.findViewById<TextView>(R.id.tv_desc).text = "你暂时没有拉黑任何人哟～"

        mAdapter = BlackListAdapter(this)
        val rv_content = findViewById<RecyclerView>(R.id.rv_content)
        rv_content.layoutManager = LinearLayoutManager(this)
        rv_content.adapter = mAdapter
        mAdapter.setEmptyView(emptyView)

        srl_refresh.setOnRefreshListener {
            isRefresh = true
            messageViewModel.lastTime = null
            messageViewModel.getBlackList(page, size)
        }

        srl_refresh.setOnLoadMoreListener {
            isLoadMore = true
            messageViewModel.getBlackList(page, size)
        }

        mAdapter.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int,
            ) {
                if (view.id == R.id.tv_black) {
                    if (mAdapter.data[position].relation == 1
                        || mAdapter.data[position].relation == 3
                    ) {
                        chartSetModel.addToBlackList(mAdapter.data[position].id, position, 1)
                    } else {
                        chartSetModel.addToBlackList(mAdapter.data[position].id, position, 0)
                    }
                }
            }
        })

        chartSetModel.setState.observe(this) {
            when (it) {
                is SetState.Success -> {
//                    //0-无，1-我拉黑了他，2-他拉黑了我，3-互相拉黑
//                    if (mAdapter.data[it.position].relation == 0) {
//                        mAdapter.data[it.position].relation = 1
//                    } else if (mAdapter.data[it.position].relation == 1) {
//                        mAdapter.data[it.position].relation = 0
//                    } else if (mAdapter.data[it.position].relation == 2) {
//                        mAdapter.data[it.position].relation = 3
//                    } else if (mAdapter.data[it.position].relation == 3) {
//                        mAdapter.data[it.position].relation = 2
//                    }
                    mAdapter.removeAt(it.position)
//                    mAdapter.notifyItemChanged(it.position)
                    mAdapter.isUseEmpty = mAdapter.data.isEmpty()
                }
                is SetState.Fail -> {
                    ToastUtils.showShort("${it.code}----" + it.errorMsg)
                }
            }
        }

    }

    override fun initData() {

        messageViewModel.blackListServer.observe(this) {
            when (it) {
                is BlackListServerState.Success -> {
                    if (isRefresh) {
                        mAdapter.setList(it.list.list)
                        isRefresh = false
                        srl_refresh.finishRefresh()
                    } else {
                        mAdapter.addData(it.list.list!!)
                        isLoadMore = false
                        srl_refresh.finishLoadMore()
                    }
                    if (it.list.hasNext == 0) {
                        srl_refresh.finishLoadMoreWithNoMoreData()
                    }
                    mAdapter.isUseEmpty = mAdapter.data.isEmpty()

                }
                is BlackListServerState.Fail -> {

                }
            }
        }

        messageViewModel.getBlackList(page, size)
    }
}