package cn.dreamfruits.yaoguo.module.selectclothes.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.selectclothes.SelectClothesActivity
import cn.dreamfruits.yaoguo.module.selectclothes.SelectClothesViewModel
import cn.dreamfruits.yaoguo.module.selectclothes.adapter.ClothesListAdapter
import cn.dreamfruits.yaoguo.module.selectclothes.state.ClothesState
import cn.dreamfruits.yaoguo.view.decoration.GridDividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 服装列表
 */
@Deprecated("修改为最新的UI")
class ClothesListFragment : Fragment() {

    private lateinit var mRefreshLayout: SmartRefreshLayout
    private lateinit var mClothesRv: RecyclerView
    private var mAdapter: ClothesListAdapter? = null

    //版型id
    private var clothesId: Long? = null


    private val mClothesViewModel by activityViewModels<SelectClothesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clothes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        view.setOnClickListener {
//            (activity as SelectClothesActivity).closeFeature()
//        }

        mRefreshLayout = view.findViewById(R.id.clothes_refreshLayout)
        mClothesRv = view.findViewById(R.id.recy_clothes_list)


//        view.findViewById<ImageView>(R.id.ic_close_clothes_list).setOnClickListener {
//            (activity as SelectClothesActivity).closeFeature()
//        }

        initRefreshLayout()
        initClothesList()
        initData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden){
            //初始化 状态
            mClothesViewModel.mClothesListLastTime = null
            mRefreshLayout.setNoMoreData(false)
        }
    }


    companion object {

        fun newInstance(): ClothesListFragment {
            return ClothesListFragment()
        }

    }


    private fun initRefreshLayout() {
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setRefreshFooter(ClassicsFooter(activity))
        mRefreshLayout.setOnLoadMoreListener { refreshLayout ->

            mClothesViewModel.getClothesList(clothesId!!)
        }

    }

    /**
     * 初始化单品列表
     */
    private fun initClothesList() {
        mAdapter = ClothesListAdapter(onAddClick = {
//            mClothesViewModel.select(it)
        })

        val layoutManager = GridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)

        mClothesRv.addItemDecoration(
            GridDividerItemDecoration(
                activity
            )
        )

        mClothesRv.layoutManager = layoutManager
        mClothesRv.adapter = mAdapter

    }


    private fun initData() {
        mClothesViewModel.clothesCategoryId.observe(viewLifecycleOwner) {
            this.clothesId = it
            mAdapter?.clearData()
            mClothesViewModel.getClothesList(clothesId!!)
        }

        mClothesViewModel.clothesList.observe(viewLifecycleOwner) { clothesState ->

            when (clothesState) {

                is ClothesState.Success -> {
                    clothesState.clothesList?.let {
                        if (it.hasNext == 0) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData()
                        } else {
                            mRefreshLayout.finishLoadMore()
                        }
                        it.list?.let { data ->
                            mAdapter?.addData(data)
                        }
                    }
                }
                is ClothesState.Fail -> {
                    ToastUtils.showShort("${clothesState.msg}")
                    mRefreshLayout.finishLoadMore()
                }
            }
        }
    }


}