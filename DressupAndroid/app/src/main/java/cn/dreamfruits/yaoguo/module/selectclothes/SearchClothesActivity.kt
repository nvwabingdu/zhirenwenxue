package cn.dreamfruits.yaoguo.module.selectclothes

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.selectclothes.adapter.AttachGoodsAdapter
import cn.dreamfruits.yaoguo.module.selectclothes.state.SearchClothesItemState
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.view.dialog.commAlertDialog
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.util.ArrayList

/**
 * @author Lee
 * @createTime 2023-06-20 20 GMT+8
 * @desc :
 */
class SearchClothesActivity : BaseActivity() {
    private lateinit var mBackIv: ImageView

    private lateinit var mNextTv: TextView
    private lateinit var mEtEarch: EditText

    private val clothesViewModel by viewModels<SelectClothesViewModel>()

    private var list: ArrayList<ClothesBean> = arrayListOf()

    private lateinit var mRefresh: SmartRefreshLayout
    private lateinit var rv_recyclerview: RecyclerView
    private lateinit var mAdapter: AttachGoodsAdapter
    private lateinit var emptyView: View
    private var isRefresh = false
    private var isLoadMore = false
    var page: Int = 1

    override fun layoutResId(): Int = R.layout.activity_search_clothes

    companion object {
        @JvmStatic
        fun start(context: Context, list: ArrayList<ClothesBean>) {
            val starter = Intent(context, SearchClothesActivity::class.java)
            starter.putExtra(
                RouterConstants.FeedPublish.KEY_CLOTHES_LIST,
                list
            )
            context.startActivity(starter)
        }
    }

    override fun initView() {

        mBackIv = findViewById(R.id.iv_back)

        mEtEarch = findViewById(R.id.et_search)

        mNextTv = findViewById(R.id.tv_next)

        mRefresh = findViewById(R.id.refreshLayout)

        rv_recyclerview = findViewById(R.id.rv_recyclerview)

        emptyView = View.inflate(mContext, R.layout.layout_attch_goods_empty, null)

        mRefresh.setOnRefreshListener {
            clothesViewModel.mSearchClothesItemLastTime = 0
            page = 1
            isRefresh = true
            var key = mEtEarch.text.toString()
            clothesViewModel.searchClothesItem(key, page)
        }
        mRefresh.setOnLoadMoreListener {
            var key = mEtEarch.text.toString()
            page++
            clothesViewModel.searchClothesItem(key, page)
            isLoadMore = true
        }

        mRefresh.setEnableRefresh(true)

        mBackIv.setOnClickListener {
            onBackPressed()
        }

        mNextTv.setOnClickListener {
            BusUtils.post("REFRESH_SEL_DATA", clothesViewModel.selectedClothesList)
            finish()
        }

        mEtEarch.addTextChangedListener {
            if (it!!.isNotEmpty()) {
                page = 1
                isRefresh = true
                search(it.toString())
            } else {

            }
        }

    }

    private fun search(key: String) {
        clothesViewModel.searchClothesItem(key, page)
    }


    override fun initData() {

        mAdapter = AttachGoodsAdapter(mContext, clothesViewModel)
        rv_recyclerview.layoutManager = GridLayoutManager(mContext, 2)
        rv_recyclerview.adapter = mAdapter
        mAdapter.setEmptyView(emptyView)

        list =
            intent.getSerializableExtra(RouterConstants.FeedPublish.KEY_CLOTHES_LIST)
                    as ArrayList<ClothesBean>

        LogUtils.e(">>>>" + list.size)
        LogUtils.e(">>>>$list")

        list.forEach {
            clothesViewModel.select(it)
        }

        clothesViewModel.changeData.observe(this) { changeData ->
            clothesViewModel.selectedClothesList.let { clothesList ->
                if (clothesList.isNotEmpty()) {
                    mNextTv.text = "完成(${clothesList.size.toString()})"
                    mNextTv.isEnabled = true
                    mNextTv.setBackgroundColor(resources.getColor(R.color.black_222222))
                } else {
                    mNextTv.text = "下一步"
                    mNextTv.isEnabled = false
                    mNextTv.setBackgroundColor(resources.getColor(R.color.gray_B3B3B3))
                }
            }
        }

        clothesViewModel.searchClothesItem.observe(this) {
            when (it) {
                is SearchClothesItemState.Success -> {
                    mRefresh.finishRefresh()
                    it.clothesList!!.list?.forEach { item ->
                        item.coverUrl = item.coverUrl.decodePicUrls()
                        list.forEach { list ->
                            if (item.id == list.id) {
                                item.isSel = true
                            }

                        }
                    }

                    if (isRefresh) {
                        it.clothesList.list?.let { it1 -> mAdapter.setList(it1) }
                        isRefresh = false
                    } else {
                        it.clothesList.list?.let { it1 -> mAdapter.addData(it1) }
                    }
                    if (it.clothesList.hasNext == 0) {
                        mRefresh.finishLoadMoreWithNoMoreData()
                    } else {
                        mRefresh.finishLoadMore()
                    }
                    mAdapter.isUseEmpty = mAdapter.data.isEmpty()
                    isLoadMore = false
                }
                is SearchClothesItemState.Fail -> {
                    ToastUtils.showShort(it.msg)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (clothesViewModel.selectedClothesList.isNotEmpty()) {
            showDialog()
        } else {
            super.onBackPressed()
        }

    }

    /**
     * 显示对话框
     */
    private fun showDialog() {
        commAlertDialog(supportFragmentManager) {
            content = "返回后已选择的单品不会保留"
            cancelText = "取消"
            confirmText = "确认"
            cancelCallback = {

            }
            confirmCallback = {
                this@SearchClothesActivity.finish()
            }
        }
    }
}