package cn.dreamfruits.selector


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.selector.preview.PicturePreviewActivity
import cn.dreamfruits.selector.util.SpacesItemDecoration
import com.blankj.utilcode.util.SizeUtils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnQueryDataResultListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 相册列表
 */
class AlbumFragment : Fragment() {

    /**
     * 1 =全部
     * 2 = 视频
     * 3 = 图片
     */
    private var type: Int? = null


    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRefreshLayout: SmartRefreshLayout
    private var mAdapter: PictureListAdapter? = null

    private var page = 1


    private val viewModel by activityViewModels<PictureSelectModel>()


    companion object {
        @JvmStatic
        fun newInstance(type: Int): AlbumFragment {
            val args = Bundle()

            val fragment = AlbumFragment()
            args.putInt("key_type", type)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRefreshLayout = view.findViewById(R.id.refresh_layout)

        type = arguments?.getInt("key_type")

        mAdapter = PictureListAdapter()
        mAdapter?.setOnPictureClickListener(object : PictureListAdapter.OnPictureClickListener {

            override fun onCheck(data: LocalMedia) {
                if (viewModel.selectedList.contains(data)) {
                    viewModel.cancel(data)
                } else {
                    viewModel.select(data)
                }
            }

            override fun onPreview(data: LocalMedia) {
                val intent = Intent(requireContext(), PicturePreviewActivity::class.java)
                intent.putParcelableArrayListExtra(
                    PictureConstants.ALL_LOCAL_MEDIA,
                    mAdapter?.dataList as ArrayList<LocalMedia>
                )
                intent.putExtra(PictureConstants.CURRENT_MEDIA_PATH, data.path)
                startActivityForResult(intent, 120)
            }
        })


        val layoutManager = GridLayoutManager(context, 4)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.addItemDecoration(
            SpacesItemDecoration(
                SizeUtils.dp2px(2f),
                SizeUtils.dp2px(2f),
                4
            )
        )
        mRecyclerView.adapter = mAdapter

        initRefreshLayout()
    }

    /**
     * 是否加载过照片或视频
     */
    private var isDataLoaded = false

    override fun onResume() {
        super.onResume()
        //viewpage2 懒加载
        if (!isDataLoaded) {
            mRefreshLayout.autoLoadMore()
        }

        /**
         * 切换页面后 刷新选中状态
         * */
        mAdapter?.dataList?.let { list ->
            for ((index, media) in list.withIndex()) {
                //先取消之前选中的
                if (media.isChecked) {
                    media.isChecked = false
                    mAdapter?.notifyItemChanged(index)
                }

                viewModel.selectedList.indexOfFirst {
                    it.path == media.path
                }.also { selectedIndex ->
                    if (selectedIndex != -1){
                        media.isChecked = true
                        media.num = selectedIndex + 1
                        mAdapter?.notifyItemChanged(index)
                    }
                }
            }
        }

    }


    /**
     * 刷新选中状态
     */
    fun refreshState(changeData: LocalMedia) {
        mAdapter?.dataList?.indexOfFirst { data ->
            changeData?.path == data.path
        }?.let { index ->
            if (index != -1) {
                val data = mAdapter?.dataList?.get(index)
                //如果是已添加的 照片或视频 则更新状态
                if (viewModel.selectedList.contains(data)) {
                    data?.isChecked = true
                    data?.num = viewModel.selectedList.size
                    mAdapter?.notifyItemChanged(index)

                    //如果是是取消 则更新所有选中 照片或视频 的状态
                } else {
                    //刷新点击的图片状态
                    data?.isChecked = false
                    mAdapter?.notifyItemChanged(index)
                    //遍历所有已选择图片或视频列表 并重新将数字排序
                    for ((selectedIndex, value) in viewModel.selectedList.withIndex()) {
                        mAdapter?.dataList?.indexOfFirst {
                            value.path == it.path
                        }?.let { index ->
                            if (index != -1) {
                                mAdapter?.dataList?.get(index)?.let { media ->
                                    media.num = selectedIndex + 1
                                    mAdapter?.notifyItemChanged(index)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 刷新交换位置
     */
    fun swapItem(fromPosition: Int, toPosition: Int) {
        refreshNum(fromPosition)
        refreshNum(toPosition)
    }


    /**
     * 刷新照片上的数字
     */
    private fun refreshNum(position: Int) {
        mAdapter?.dataList?.indexOfFirst {
            it.path == viewModel.selectedList[position].path
        }?.let { index ->
            if (index != -1) {
                val data = mAdapter?.dataList?.get(index)
                data?.let { media ->
                    val selectedIndex = viewModel.selectedList.indexOfFirst {
                        media.path == it.path
                    }
                    media.num = selectedIndex + 1
                    mAdapter?.notifyItemChanged(index)
                }
            }
        }
    }


    private fun initRefreshLayout() {
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setOnLoadMoreListener {

            getMedia(isAdd = true)
        }
    }



    /**
     * 切换相册
     */
    fun switchAlbum() {
        /**
         * 重置状态
         */
        mRefreshLayout.setNoMoreData(false)
        page = 1
        getMedia(isAdd = false)
    }


    /**
     * @param isAdd 是否追加数据
     */
    private fun getMedia(
        pageSize: Int = 100,
        isAdd: Boolean
    ) {
        val selectMimeType = when (type) {
            1 -> SelectMimeType.ofAll()

            2 -> SelectMimeType.ofVideo()

            3 -> SelectMimeType.ofImage()

            else -> throw IllegalArgumentException("type is not correct")
        }

        PictureSelector.create(this)
            .dataSource(selectMimeType)
            .buildMediaLoader()
            .loadPageMediaData(viewModel.bucketId, page, pageSize,
                object : OnQueryDataResultListener<LocalMedia>() {

                    override fun onComplete(result: ArrayList<LocalMedia>, isHasMore: Boolean) {
                        //如果是第一次加载数据 切换后更新选中状态
                       // if (!isDataLoaded) {
                            for (media in result) {
                                viewModel.selectedList.indexOfFirst {
                                    it.path == media.path
                                }.also { index ->
                                    if (index != -1){
                                        media.num = index + 1
                                        media.isChecked = true
                                    }
                                }
                            }
                      //  }

                        mAdapter?.setData(result, isAdd)

                        if (isHasMore) mRefreshLayout.finishLoadMore() else mRefreshLayout.setNoMoreData(
                            true
                        )
                        isDataLoaded = true
                        page++

                    }
                })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == PictureConstants.MEDIA_RESULT_CODE) {
            val resultList =
                data?.getParcelableArrayListExtra<LocalMedia>(PictureConstants.MEDIA_RESULT_LIST)

            if (resultList != null) {
                //删除 取消选中的照片或 视频
                for (index in viewModel.selectedList.size - 1 downTo 0) {
                    val media = viewModel.selectedList[index]
                    if (resultList.size == 0 || !resultList.contains(media)) {
                        viewModel.cancel(media)
                    }
                }
                //增加已选中的 照片或视频
                for (media in resultList) {
                    if (!viewModel.selectedList.contains(media)) {
                        viewModel.select(media)
                    }
                }
            }
        }
    }
}