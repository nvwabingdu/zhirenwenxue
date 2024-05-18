package cn.dreamfruits.selector

import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.selector.util.SpacesItemDecoration
import cn.dreamfruits.selector.util.StatusBarUtil
import com.blankj.utilcode.util.SizeUtils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.entity.LocalMediaFolder
import com.luck.picture.lib.interfaces.OnQueryDataResultListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.util.ArrayList


/**
 * 图片选择 activity
 */
class AlbumActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mExit: ImageView
    private lateinit var mFolderName: TextView
    private lateinit var mNext: TextView
    private lateinit var mRefreshLayout: SmartRefreshLayout
    private lateinit var mTopBar: RelativeLayout
    private var folderPopupView: FolderPopupView? =null
    private var mAdapter: PictureListAdapter? =null




    private var selectedCount: Int = 0

    //分页
    private var page = 1

    private val viewModel by viewModels<PictureSelectModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        StatusBarUtil.setStatusBarColor(this,resources.getColor(R.color.black_081116))

        mRecyclerView = findViewById(R.id.recycler_view)
        mExit = findViewById(R.id.iv_exit)
        mFolderName = findViewById(R.id.tv_folder_name)
        mNext = findViewById(R.id.next)
        mRefreshLayout = findViewById(R.id.smart_refresh_layout)
        mTopBar = findViewById(R.id.top_app_bar)

        mFolderName.setOnClickListener {

            if (folderPopupView != null){
                XPopup.Builder(this)
                    .atView(mTopBar)
                    .shadowBgColor(resources.getColor(R.color.black_081116))
                    .popupPosition(PopupPosition.Bottom)
                    .asCustom(folderPopupView)
                    .show()
            }
        }

        mExit.setOnClickListener {
            this@AlbumActivity.finish()
        }

        intent?.let {
            selectedCount = it.getIntExtra(PictureConstants.SELECTED_COUNT, 0)
        }

        //设置适配器
        mAdapter = PictureListAdapter()
        mAdapter?.setOnPictureClickListener(object: PictureListAdapter.OnPictureClickListener{
            override fun onCheck(data: LocalMedia) {

            }

            override fun onPreview(data: LocalMedia) {

            }
        })
        //设置layoutManager
        val layoutManager = GridLayoutManager(this, 4)
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
        loadMediaFolder()
    }


    private fun initRefreshLayout(){
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setOnLoadMoreListener {
            loadImage()
        }

    }


    /**
     * 加载相册列表
     */
    private fun loadMediaFolder(){
        PictureSelector.create(this)
            .dataSource(SelectMimeType.ofImage())
            .buildMediaLoader()
            .loadAllAlbum {list ->
                folderPopupView = FolderPopupView(this,list)
                folderPopupView?.setOnIBridgeAlbumWidget(object :OnFolderClickListener{

                    override fun onItemClick(position: Int, curFolder: LocalMediaFolder) {
                        folderPopupView?.dismiss()
                        //如果点击的位置一样 则不做处理
                        if (curFolder.bucketId == viewModel.bucketId) return

                        viewModel.bucketId = curFolder.bucketId
                        //重置相册
                        page = 1
                        mRefreshLayout.setNoMoreData(false)

                        loadImage(isAdd = false)
                    }
                })

                mRefreshLayout.autoLoadMore()
            }

    }


    /**
     * 加载相册数据
     */
    private fun loadImage(
        pageSie: Int = 100,
        isAdd:Boolean = true
    ) {

        PictureSelector.create(this)
            .dataSource(SelectMimeType.ofImage())
            .buildMediaLoader()
            .loadPageMediaData(
                viewModel.bucketId,
                page,
                pageSie,
                object : OnQueryDataResultListener<LocalMedia>() {
                    override fun onComplete(result: ArrayList<LocalMedia>, isHasMore: Boolean) {
                        if (isHasMore) mRefreshLayout.finishLoadMore() else mRefreshLayout.setNoMoreData(true)
                        page ++
                        mAdapter?.setData(result,isAdd)

                    }
                })


    }


}