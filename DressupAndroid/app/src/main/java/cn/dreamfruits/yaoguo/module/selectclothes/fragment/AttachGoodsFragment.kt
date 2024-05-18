package cn.dreamfruits.yaoguo.module.selectclothes.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseFragment
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.module.selectclothes.AttachGoodsViewModel
import cn.dreamfruits.yaoguo.module.selectclothes.SelectClothesViewModel
import cn.dreamfruits.yaoguo.module.selectclothes.adapter.AttachGoodsAdapter
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.util.decodePicUrls
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author Lee
 * @createTime 2023-06-19 19 GMT+8
 * @desc : 关联商品选择
 */
class AttachGoodsFragment() : BaseFragment(), DataCallBack {

    private val clothesViewModel by activityViewModels<SelectClothesViewModel>()
    private val attchViewModel = AttachGoodsViewModel(this)

    private lateinit var mRefresh: SmartRefreshLayout
    private lateinit var rv_recyclerview: RecyclerView
    private lateinit var mAdapter: AttachGoodsAdapter

    private var selList: MutableList<ClothesBean> = arrayListOf()

    //0-我收藏的，1-我试穿的，2-我浏览的，3-我DIY的
    private var type: Int = 0

    private var isRefresh = false
    private var isLoadMore = false

    private lateinit var emptyView: View


    companion object {
        fun newInstance(type: Int): AttachGoodsFragment {
            val args = Bundle()
            val fragment = AttachGoodsFragment()
            args.putInt("type", type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_attach_goods

    override fun initView(convertView: View?, savedInstanceState: Bundle?) {
        mRefresh = convertView!!.findViewById(R.id.refreshLayout)
        rv_recyclerview = convertView.findViewById(R.id.rv_recyclerview)

        emptyView = View.inflate(mContext, R.layout.layout_attch_goods_empty, null)

//        mRefresh.setOnRefreshListener {
//            clothesViewModel.mClothesItemLastTime = 0
//            isRefresh = true
//            clothesViewModel.getMyClothesItem(type)
//        }
        mRefresh.setOnLoadMoreListener {
            attchViewModel.getMyClothesItem(type)
            isLoadMore = true
        }

        mRefresh.setEnableRefresh(false)

    }

    private var list: MutableList<ClothesBean> = arrayListOf()

    override fun initData() {
        type = arguments!!.getInt("type")
        mAdapter = AttachGoodsAdapter(context!!, clothesViewModel)
        rv_recyclerview.layoutManager = GridLayoutManager(context, 2)
        rv_recyclerview.adapter = mAdapter
        mAdapter.setEmptyView(emptyView)


        var tvHint = emptyView.findViewById<TextView>(R.id.tv_desc)
        tvHint.text =
            when (type) {
                0 -> "您暂时还没有收藏哟"
                1 -> "您暂时还没有试穿哟"
                2 -> "您暂时还没有浏览哟"
                4 -> "暂时还没有推荐哟"
                else -> "您暂时还没有Diy哟"
            }

        clothesViewModel.changeData.observe(this) { changeData ->
            clothesViewModel.selectedClothesList.let { clothesList ->
                clothesList.forEach { list ->
                    val find = mAdapter.data.find { it.id == list.id }
                    if (find != null)
                        find.isSel = true
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun getData() {
        attchViewModel.getMyClothesItem(type)
    }

    lateinit var call: DataCallBack
    override fun getdata(data: BasePageResult<ClothesBean>) {
        mRefresh.finishRefresh()
        data.list?.forEach {
            it.coverUrl = it.coverUrl.decodePicUrls()
        }

        clothesViewModel.selectedClothesList.let { clothesList ->
            clothesList.forEach { list ->
                val find = data.list?.find { it.id == list.id }
                if (find != null)
                    find.isSel = true
            }
        }

        if (isRefresh) {
            data.list?.let { it1 -> mAdapter.setList(it1) }
            isRefresh = false
        } else {
            data.list?.let { it1 -> mAdapter.addData(it1) }
        }
        if (data.hasNext == 0) {
            mRefresh.finishLoadMoreWithNoMoreData()
        } else {
            mRefresh.finishLoadMore()
        }
        mAdapter.isUseEmpty = mAdapter.data.isEmpty()
        isLoadMore = false
    }

    override fun fail(msg: String?) {
        ToastUtils.showShort(msg)
    }
}

public interface DataCallBack {
    fun getdata(data: BasePageResult<ClothesBean>)

    fun fail(msg: String?)

}