package cn.dreamfruits.selector

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import cn.dreamfruits.selector.util.ItemTouchHelperCallback2
import cn.dreamfruits.selector.util.StatusBarUtil
import com.blankj.utilcode.util.UriUtils
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.utils.PictureFileUtils
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 发布帖子 视频，图片选择页
 */
class PublishPickerActivity : AppCompatActivity() {

    private lateinit var mMagicIndicator: MagicIndicator
    private lateinit var mViewPager2: ViewPager2

    private var fragments: MutableList<Fragment> = mutableListOf()
    private lateinit var mCameraFragment: CameraFragment

    private lateinit var bottomPicture: LinearLayout
    private lateinit var next: TextView
    private lateinit var recyclerView: RecyclerView

    private var mAdapter: PictureSelectedAdapter? = null


    private val viewModel by viewModels<PictureSelectModel>()


    //Tab标题
    private val mTitles = arrayOf("相册", "拍照", "拍视频")

    private var IS_CHOOSE_VIDEO_COVER = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_picker)
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.black_081116))
        initView()
        initData()
    }


    companion object {

        const val FEATURE_PIC = 0 //拍照

        const val FEATURE_VIDEO = 1 //视频
    }


    private fun initView() {
        mMagicIndicator = findViewById(R.id.magic_indicator)
        mViewPager2 = findViewById(R.id.view_pager2)
        bottomPicture = findViewById(R.id.bottom_picture)
        next = findViewById(R.id.next)
        recyclerView = findViewById(R.id.recycler_view)


        next.setOnClickListener {
            handleMedia()
        }
        bottomPicture.setOnClickListener {

        }


        initMagicIndicator()
        initViewPager2()
        initSelectedPictureList()
    }

    private fun initData() {

        IS_CHOOSE_VIDEO_COVER = intent.getIntExtra("IS_CHOOSE_VIDEO_COVER", -1)

        viewModel.selectedCount.observe(this) { count ->
            if (count == 0) {
                bottomPicture.visibility = View.GONE
            } else {
                bottomPicture.visibility = View.VISIBLE
                next.text = String.format("下一步(%s)", count)
            }
        }

        viewModel.changeData.observe(this) { changeData ->
            mAdapter?.dataList?.indexOfFirst { media ->
                changeData.path == media.path
            }?.let { index ->

                if (index == -1) {
                    mAdapter?.dataList?.add(changeData)
                    mAdapter?.notifyItemInserted(mAdapter!!.itemCount)
                    mAdapter?.notifyItemRangeChanged(0, mAdapter!!.dataList.size)
                    recyclerView.scrollToPosition(mAdapter!!.dataList.size - 1)
                } else {
                    mAdapter?.dataList?.remove(changeData)
                    mAdapter?.notifyItemRemoved(index)
                    mAdapter?.notifyItemRangeChanged(0, mAdapter!!.dataList.size)
                }
            }
        }

    }

    /**
     * 初始化已选择列表
     */
    private fun initSelectedPictureList() {

        mAdapter = PictureSelectedAdapter(
            onCancelListener = { data ->
                viewModel.cancel(data)
            },
            onItemMoveListener = { fromPosition, toPosition ->
                viewModel.swap(fromPosition, toPosition)
            }
        )


        var result: ArrayList<LocalMedia>? =
            intent.getParcelableArrayListExtra("KEY_SEL_MEDIA_LIST")

        if (result == null) {

        } else {
            viewModel.addAll(result)
            mAdapter!!.dataList.clear()
            mAdapter!!.dataList.addAll(result)
            mAdapter!!.notifyDataSetChanged()
        }

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerView.addItemDecoration(HorItemDecoration(8))

        val touchHelper = ItemTouchHelper(ItemTouchHelperCallback2(mAdapter!!))
        touchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = mAdapter

    }


    private fun initMagicIndicator() {
        val commonNavigator = CommonNavigator(this)
        mMagicIndicator.setBackgroundColor(resources.getColor(R.color.black_081116))
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val pagerTitleView = ColorTransitionPagerTitleView(context)
                pagerTitleView.normalColor = Color.GRAY
                pagerTitleView.selectedColor = Color.WHITE
                pagerTitleView.text = mTitles[index]
                pagerTitleView.setOnClickListener {
                    // commonNavigator.onPageScrolled(index,0f,0)

                    mMagicIndicator.onPageSelected(index)
                    mMagicIndicator.onPageScrolled(index, 0f, 0)

                    mViewPager2.setCurrentItem(index, false)
                    if (index == 1) {
                        mCameraFragment.refreshFeature(FEATURE_PIC)
                    } else if (index == 2) {
                        mCameraFragment.refreshFeature(FEATURE_VIDEO)
                    }

                }

                return pagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.setColors(Color.WHITE)
                return linePagerIndicator
            }
        }
        mMagicIndicator.navigator = commonNavigator

    }


    private fun initViewPager2() {

        mCameraFragment = CameraFragment.newInstance(1)

        fragments.add(PictureSelectorFragment.newInstance())
        fragments.add(mCameraFragment)

        //禁止左右滑动
        mViewPager2.isUserInputEnabled = false
        mViewPager2.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {

            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }

    }


    /**
     * 处理相册选择的图片 或视频返回结果
     */
    private fun handleMedia() {
        val media = viewModel.selectedList.first()

        //如果选择的是图片
        if (PictureMimeType.isHasImage(media.mimeType)) {

            val intent = Intent()
            intent.putParcelableArrayListExtra(
                PictureConstants.MEDIA_RESULT_LIST,
                viewModel.selectedList as ArrayList<LocalMedia>
            )
            Log.e(">>>>   ", "size = " + viewModel.selectedList.size)
            Log.e(">>>>   ", "" + viewModel.selectedList.toString())
            setResult(PictureConstants.IMAGE_RESULT_CODE, intent)
            this@PublishPickerActivity.finish()
            //如果选择了视频 则需要获取封面图
        } else {
            getVideoThumbnail(videoPath = media.path) { path ->
                media.videoThumbnailPath = path

                val intent = Intent()
                intent.putParcelableArrayListExtra(
                    PictureConstants.MEDIA_RESULT_LIST,
                    viewModel.selectedList as ArrayList<LocalMedia>
                )
                setResult(PictureConstants.IMAGE_RESULT_CODE, intent)
                this@PublishPickerActivity.finish()
            }
        }
    }


    /**
     * 处理拍摄的视频或图片 返回结果
     */
    fun handleShootMedia(media: LocalMedia) {
        val list = mutableListOf<LocalMedia>()
        //如果选择的是图片
        if (PictureMimeType.isHasImage(media.mimeType)) {
            val intent = Intent()
            list.add(media)

            intent.putParcelableArrayListExtra(
                PictureConstants.MEDIA_RESULT_LIST,
                list as ArrayList<LocalMedia>
            )
            setResult(PictureConstants.IMAGE_RESULT_CODE, intent)
            this@PublishPickerActivity.finish()
            //如果选择了视频 则需要获取封面图
        } else {
            getVideoThumbnail(videoPath = media.path) { path ->
                media.videoThumbnailPath = path

                val intent = Intent()
                list.add(media)
                intent.putParcelableArrayListExtra(
                    PictureConstants.MEDIA_RESULT_LIST,
                    list as ArrayList<LocalMedia>
                )
                setResult(PictureConstants.IMAGE_RESULT_CODE, intent)
                this@PublishPickerActivity.finish()
            }
        }
    }


    /**
     * 获取视频封面图
     */
    private fun getVideoThumbnail(videoPath: String, onVideoThumbnail: (String) -> Unit) {
        val targetPath = "${getExternalFilesDir("video_thumbnail")}${File.separator}"
        GlideApp.with(this)
            .asBitmap()
            .sizeMultiplier(0.6f)
            .load(videoPath)
            .into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val stream = ByteArrayOutputStream()
                    resource.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                    var fos: FileOutputStream? = null
                    var result: String? = null
                    try {
                        val targetFile =
                            File(targetPath, "thumbnails_" + System.currentTimeMillis() + ".jpg")
                        fos = FileOutputStream(targetFile)
                        fos.write(stream.toByteArray())
                        fos.flush()
                        result = targetFile.absolutePath
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        PictureFileUtils.close(fos)
                        PictureFileUtils.close(stream)
                    }

                    Log.i("TAG11", "onResourceReady: $result")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        result = UriUtils.file2Uri(File(result)).toString()
                    }

                    onVideoThumbnail.invoke(result!!)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }


}