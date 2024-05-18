package cn.dreamfruits.selector.preview

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import cn.dreamfruits.selector.PictureConstants
import cn.dreamfruits.selector.R
import cn.dreamfruits.selector.util.StatusBarUtil
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import java.util.ArrayList

/**
 * 预览页面
 */
class PicturePreviewActivity : AppCompatActivity() {

    private lateinit var checkLayout: LinearLayout
    private lateinit var checkImage: ImageView
    private lateinit var checkCount: TextView
    private lateinit var back: ImageView
    private lateinit var viewPager2: ViewPager2

    private var localMedia: List<LocalMedia>? = null
    private var currentMediaPath: String? = null
    private var mAdapter: PreviewPageAdapter? = null

    private var selectedList = mutableListOf<LocalMedia>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_preview)
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.black_081116))

        viewPager2 = findViewById(R.id.view_pager2)
        checkLayout = findViewById(R.id.check_layout)
        checkImage = findViewById(R.id.check_image)
        checkCount = findViewById(R.id.check_count)
        back = findViewById(R.id.back)


        localMedia = intent?.getParcelableArrayListExtra(PictureConstants.ALL_LOCAL_MEDIA)
        currentMediaPath = intent?.getStringExtra(PictureConstants.CURRENT_MEDIA_PATH)


        back.setOnClickListener {
            setResult()
        }
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                localMedia?.get(position)?.let { media ->
                    val media = selectedList.find { media.path == it.path }
                    if (media != null) {
                        switchState(true, media.num)
                    } else {
                        switchState(false, 0)
                    }
                }
            }
        })

        //点击选中或取消
        checkLayout.setOnClickListener {
            val position = viewPager2.currentItem
            localMedia?.get(position)?.let { media ->
                //找到该资源 设置取消选中 并将数字重新排序
                if (selectedList.contains(media)) {
                    switchState(false, 0)
                    selectedList.remove(media)
                    for ((index, value) in selectedList.withIndex()) {
                        value.num = index + 1
                    }
                } else {
                    if (selectedList.size < 9) {

                        if (selectedList.isNotEmpty()) {
                            if (selectedList.first().mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_IMAGE)
                                && !media.mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_IMAGE)
                            ) {
                                return@setOnClickListener
                            }
                            if (selectedList.first().mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_VIDEO)
                                && !media.mimeType.startsWith(
                                    PictureMimeType.MIME_TYPE_PREFIX_VIDEO
                                )
                            ) {
                                return@setOnClickListener
                            }
                        }
                        if (selectedList.isNotEmpty())
                            if (selectedList.first().mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_VIDEO) ) {
                                if (selectedList.size >= 1) {
                                    return@setOnClickListener
                                }
                            }

                        selectedList.add(media)
                        switchState(true, selectedList.size)
                        setResult()
                    }
                }
            }
        }

        //找出已选中的照片或视频
        localMedia?.filter {
            it.isChecked
        }?.let { list ->
            selectedList.addAll(list)
        }
        /**
         * 设置数据
         */
        localMedia?.let { media ->
            mAdapter = PreviewPageAdapter(this, media)
            viewPager2.adapter = mAdapter
        }

        //选中页面
        currentMediaPath?.let { path ->

            localMedia?.indexOfFirst {
                path == it.path
            }?.let { index ->
                viewPager2.setCurrentItem(index, false)
            }
        }

    }

    /**
     * 设置选中状态
     */
    private fun switchState(isCheck: Boolean, num: Int) {
        if (isCheck) {
            checkImage.setBackgroundResource(R.drawable.ic_media_selected)
            checkCount.text = num.toString()
        } else {
            checkImage.setBackgroundResource(R.drawable.ic_media_unselected)
            checkCount.text = ""
        }
    }


    override fun onBackPressed() {
        setResult()
    }


    /**
     * 设置数据并返回
     */
    private fun setResult() {
        val data = Intent()
        data.putParcelableArrayListExtra(
            PictureConstants.MEDIA_RESULT_LIST,
            selectedList as ArrayList<LocalMedia>
        )
        this@PicturePreviewActivity.setResult(PictureConstants.MEDIA_RESULT_CODE, data)
        this@PicturePreviewActivity.finish()
    }


}