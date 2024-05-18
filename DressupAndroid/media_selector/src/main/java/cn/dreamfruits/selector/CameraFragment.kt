package cn.dreamfruits.selector


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.dreamfruits.selector.PublishPickerActivity.Companion.FEATURE_PIC
import cn.dreamfruits.selector.PublishPickerActivity.Companion.FEATURE_VIDEO
import cn.dreamfruits.selector.view.CircularProgressView
import com.blankj.utilcode.util.StringUtils
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.utils.DateUtils
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.FileCallback
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.size.AspectRatio
import com.otaliastudios.cameraview.size.SizeSelectors
import java.io.File


class CameraFragment : Fragment() {

    private lateinit var bottomStart: CircularProgressView
    private lateinit var cameraView: CameraView

    private lateinit var ratio: ImageView
    private lateinit var toggleCameraLayout: LinearLayout
    private lateinit var duration: TextView

    //录制视频的总时长
    private val maxDecoration = 30 * 1000

    //默认画面比例
    private var mRatio = RATIO_1_1


    companion object {
        @JvmStatic
        fun newInstance(type: Int): CameraFragment {
            val args = Bundle()
            args.putInt("key_type", type)

            val fragment = CameraFragment()
            fragment.arguments = args
            return fragment
        }

        const val RATIO_3_4 = "3:4"

        const val RATIO_1_1 = "1:1"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_camera, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomStart = view.findViewById(R.id.bottom_start)
        cameraView = view.findViewById(R.id.camera_view)
        ratio = view.findViewById(R.id.ratio)
        toggleCameraLayout = view.findViewById(R.id.toggle_camera_layout)
        duration = view.findViewById(R.id.tv_duration)

        ratio.setOnClickListener {
            triggerRatio()
        }

        bottomStart.setOnClickListener {
            clickCircleStart()
        }

        toggleCameraLayout.setOnClickListener {
            toggleCamera()
        }

        refreshFeature(mCurrentFeature)
        initCamera()
    }


    private fun initCamera() {
        cameraView.setLifecycleOwner(viewLifecycleOwner)
        cameraView.addCameraListener(Listener())
        cameraView.setPictureSize(SizeSelectors.aspectRatio(AspectRatio.parse(mRatio), 0f))

    }


    /**
     * 点击圆形
     */
    private fun clickCircleStart() {
        if (mCurrentFeature == FEATURE_PIC) {
            cameraView.takePicture()

        } else if (mCurrentFeature == FEATURE_VIDEO) {
            //如果正在拍摄视频
            if (cameraView.isTakingVideo) {
                cameraView.stopVideo()
            } else {
                val fileName =
                    "${requireContext().getExternalFilesDir("video")?.absoluteFile}${File.separator}${System.currentTimeMillis()}.mp4"
                cameraView.takeVideo(File(fileName))
                timestamp = System.currentTimeMillis()
                handler.post(timeRunnable)
                bottomStart.setBackgroundResource(R.drawable.ic_green_rectangle)
            }
        }
    }


    /**
     * 切换视频 宽高比
     */
    private fun triggerRatio() {
        if (StringUtils.equals(mRatio, RATIO_1_1)) {
            ratio.setBackgroundResource(R.drawable.ic_ratio_3_4)
            mRatio = RATIO_3_4
        } else {
            ratio.setBackgroundResource(R.drawable.ic_ratio_1_1)
            mRatio = RATIO_1_1
        }
        cameraView.setPictureSize(SizeSelectors.aspectRatio(AspectRatio.parse(mRatio), 0f))
        cameraView.close()//关闭预览
        cameraView.open()//开启预览 使得修改生效
    }


    private val handler = Handler(Looper.getMainLooper())
    private var timestamp: Long = 0


    private val timeRunnable = object : Runnable {

        override fun run() {
            val videoDuration = System.currentTimeMillis() - timestamp
            duration.text = String.format("%s/5:00", DateUtils.formatDurationTime(videoDuration))

            bottomStart.setMaxDuration(maxDecoration)
            bottomStart.progress = videoDuration.toInt()

            //如果当前录制的时间 小于总时长 则继续更新时长
            if (videoDuration <= maxDecoration) {
                handler.postDelayed(this, 100)
            }
        }

    }


    /**
     * 相机状态监听回调
     */
    private inner class Listener : CameraListener() {

        override fun onCameraClosed() {

        }


        override fun onPictureTaken(result: PictureResult) {

            val dir = requireContext().getExternalFilesDir("picture")
            val filePath =
                "${dir?.absolutePath}${File.separator}picture_${System.currentTimeMillis()}.jpg"

            //  result.toFile()
            result.toFile(File(filePath), FileCallback { file ->
                val localMedia = LocalMedia().also {
                    it.width = result.size.width
                    it.height = result.size.height
                    it.path = filePath
                    it.mimeType = "image/jpg"
                }
                (requireActivity() as? PublishPickerActivity)?.handleShootMedia(localMedia)
            })


        }


        override fun onVideoTaken(result: VideoResult) {
            bottomStart.setBackgroundResource(R.drawable.ic_green_camera)

            val localMedia = LocalMedia().also {
                it.width = result.size.width
                it.height = result.size.height
                it.path = result.file.absolutePath
                it.duration = result.maxDuration.toLong()
                it.mimeType = "video/mp4"
            }
            (requireActivity() as? PublishPickerActivity)?.handleShootMedia(localMedia)
        }
    }


    /***
     * 切换摄像头
     */
    private fun toggleCamera() {
        if (cameraView.isTakingPicture || cameraView.isTakingVideo) {
            return
        }
        cameraView.toggleFacing()
    }


    /**
     * 拍照 还是拍视频
     */
    private var mCurrentFeature = FEATURE_PIC


    /**
     * 设置页面状态
     */
    fun refreshFeature(currentFeature: Int) {
        mCurrentFeature = currentFeature

        if (view == null) {
            return
        }
        if (mCurrentFeature == FEATURE_PIC) {
            //设置为拍照模式
            cameraView.mode = Mode.PICTURE

            bottomStart.setBackColor(R.color.color_white)
            bottomStart.setBackgroundResource(R.drawable.ic_green_circle)
            ratio.visibility = View.VISIBLE
            duration.visibility = View.GONE

        } else if (mCurrentFeature == FEATURE_VIDEO) {
            //设置为拍视频模式
            cameraView.mode = Mode.VIDEO
            cameraView.videoMaxDuration = maxDecoration

            bottomStart.setBackColor(R.color.black_73)
            bottomStart.setBackgroundResource(R.drawable.ic_green_camera)
            ratio.visibility = View.GONE
            duration.visibility = View.VISIBLE
            duration.text = "00:00/05:00"
        }

    }

}