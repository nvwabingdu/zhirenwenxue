package cn.dreamfruits.selector

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.entity.LocalMediaFolder
import com.lxj.xpopup.impl.PartShadowPopupView

/**
 * 相册目录
 */
class FolderPopupView(context:Context,val localMediaFolder: List<LocalMediaFolder>) : PartShadowPopupView(context){

    private lateinit var recyclerView: RecyclerView
    private var mAdapter:FolderListAdapter? =null

    private var mListener:OnFolderClickListener? =null


    override fun getImplLayoutId(): Int {
        return R.layout.folder_popup
    }

    override fun onCreate() {
        super.onCreate()
        recyclerView = findViewById(R.id.recycler_view)

        mAdapter = FolderListAdapter()
        recyclerView.layoutManager= LinearLayoutManager(context)
        recyclerView.adapter = mAdapter

        localMediaFolder.first().folderName = "最近项目"
        mAdapter?.setData(localMediaFolder)
        mAdapter?.setOnIBridgeAlbumWidget(mListener)
        mAdapter?.notifyDataSetChanged()
    }


    /**
     * 相册列表桥接类
     *
     * @param listener
     */
    fun setOnIBridgeAlbumWidget(listener: OnFolderClickListener) {
        this.mListener = listener
    }


}