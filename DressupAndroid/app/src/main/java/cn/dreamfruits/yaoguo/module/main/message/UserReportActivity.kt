package cn.dreamfruits.yaoguo.module.main.message

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemClickListener
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.message.adapter.UserReportAdapter
import cn.dreamfruits.yaoguo.module.main.message.bean.UserReportEntity
import com.blankj.utilcode.util.ToastUtils

/**
 * @author Lee
 * @createTime 2023-07-03 10 GMT+8
 * @desc : 用户举报
 */
class UserReportActivity : BaseActivity() {

    private lateinit var mAdapter: UserReportAdapter
    private lateinit var rv_content: RecyclerView
    var selEntity: UserReportEntity? = null

    companion object {
        @JvmStatic
        fun start(context: Context, userId: String) {
            val starter = Intent(context, UserReportActivity::class.java)
                .putExtra("userId", userId)
            context.startActivity(starter)
        }
    }

    override fun layoutResId(): Int = R.layout.activity_chart_user_report


    override fun initView() {
        findViewById<ImageView>(R.id.iv_back)
            .setOnClickListener { finish() }

        rv_content = findViewById(R.id.rv_content)

        mAdapter = UserReportAdapter()

        rv_content.layoutManager = LinearLayoutManager(this)

        rv_content.adapter = mAdapter

        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                mAdapter.data.forEach {
                    it.isSel = false
                }
                mAdapter.data[position].isSel = true
                mAdapter.notifyDataSetChanged()
                selEntity = mAdapter.data[position]
            }
        })

        findViewById<TextView>(R.id.tv_commit)
            .setOnClickListener {
                if (selEntity == null) {
                    ToastUtils.showShort("请选择")
                    return@setOnClickListener
                }
                ToastUtils.showShort("举报成功，感谢反馈")
            }

    }

    override fun initData() {
        var list: MutableList<UserReportEntity> = arrayListOf()
        list.add(UserReportEntity(false, "违法，色情，咋骗或漫政内容"))
        list.add(UserReportEntity(false, "造谣或欺骗"))
        list.add(UserReportEntity(false, "色情低俗"))
        list.add(UserReportEntity(false, "羞辱诽谤"))
        list.add(UserReportEntity(false, "违规违法"))
        mAdapter.setList(list)
    }
}