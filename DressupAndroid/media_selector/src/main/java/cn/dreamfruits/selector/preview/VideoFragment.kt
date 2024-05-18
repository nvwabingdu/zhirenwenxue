package cn.dreamfruits.selector.preview

import android.graphics.SurfaceTexture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import cn.dreamfruits.selector.R
import cn.dreamfruits.selector.preview.play.IVideoPlayer
import cn.dreamfruits.selector.preview.play.VideoPlayer
import cn.dreamfruits.selector.preview.play.VideoPlayerListener
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.UriUtils
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.utils.DateUtils

/**
 * 视频预览
 */
class VideoFragment : Fragment() {

    private lateinit var gestureLayout: FrameLayout
    private lateinit var textureView: TextureView
    private lateinit var centerPlay: ImageView
    private lateinit var state: ImageView
    private lateinit var currentDuration: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var totalDuration: TextView
    private lateinit var videoThumbnailView: ImageView
    private var videoPlayer: IVideoPlayer? = null
    private var videoSurface: Surface? = null


    private var data: LocalMedia? = null


    companion object {
        @JvmStatic
        fun newInstance(media: LocalMedia): VideoFragment {
            val args = Bundle()

            val fragment = VideoFragment()
            args.putParcelable("media", media)

            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gestureLayout = view.findViewById(R.id.gesture_layout)
        textureView = view.findViewById(R.id.video_texture)
        centerPlay = view.findViewById(R.id.center_play)
        state = view.findViewById(R.id.state)
        currentDuration = view.findViewById(R.id.current_duration)
        seekBar = view.findViewById(R.id.seek_bar)
        totalDuration = view.findViewById(R.id.total_duration)
        videoThumbnailView = view.findViewById(R.id.iv_video_thumbnail)

        data = arguments?.getParcelable("media")

        gestureLayout.setOnClickListener {
            videoPlayer?.run {

                if (!isPlaying()) play() else pause()
            }
        }

        data?.let {
            showThumbnail(it.path)
            videoPlayer = VideoPlayer()
            initPlayer(it.path)

            textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    texture: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                    videoPlayer?.also { player ->
                        val surface = Surface(texture)
                        videoSurface = surface
                        player.setSurface(surface)
                    }
                }

                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) = Unit

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = true

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit
            }
        }
    }


    override fun onPause() {
        super.onPause()
        videoPlayer?.run {
            if (isPlaying()) pause()
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private fun initPlayer(path: String) {
        videoPlayer?.run {
            val filePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                UriUtils.uri2File(Uri.parse(path)).absolutePath
            } else {
                path
            }
            setDataSource(filePath)
            setVideoListener(object : VideoPlayerListener.Default() {
                override fun onPrepared(player: IVideoPlayer) {
                    updateRenderSize(player.videoWidth, player.videoHeight)
                    //设置视频总时长
                    totalDuration.text =
                        DateUtils.formatDurationTime(videoPlayer?.duration?.toLong() ?: 0)
                }

                override fun onRenderStart() {
                    videoThumbnailView.visibility = View.GONE
                }

                override fun onError() {

                }

                override fun onStart() {
                    handler.postDelayed(timeRunnable, 200)
                    centerPlay.visibility = View.GONE
                }

                override fun onStop() {
                    handler.removeCallbacks(timeRunnable)
                    centerPlay.visibility = View.VISIBLE
                }

                override fun onCompletion() {
                    //重置状态
                    handler.removeCallbacks(timeRunnable)
                    currentDuration.text = DateUtils.formatDurationTime(videoPlayer!!.playedDuration.toLong())
                    seekBar.progress = 0
                    videoPlayer?.seekTo(0)
                }
            })
        }
    }

    private val timeRunnable = object : Runnable {

        override fun run() {
            val duration = videoPlayer?.duration ?: 0
            val playedDuration = videoPlayer?.playedDuration ?: 0

            if (duration <= 0){
                return
            }
            seekBar.progress = ((playedDuration.toFloat() / duration) * 100).toInt()

            currentDuration.text = DateUtils.formatDurationTime(playedDuration!!.toLong())

            handler.postDelayed(this, 200)
        }
    }


    private fun updateRenderSize(videoWidth: Int, videoHeight: Int) {
        if (videoWidth <= 0 || videoHeight <= 0) return
        var resultWidth = 0
        var resultHeight = 0
        val screenHeight = ScreenUtils.getScreenHeight()
        val screenWidth = ScreenUtils.getScreenWidth()
        val tempHeight = ScreenUtils.getScreenWidth().toFloat() * videoHeight / videoWidth
        if (tempHeight >= screenHeight) {
            // 应该以屏幕高进行缩放
            resultHeight = screenHeight
            resultWidth = (resultHeight.toFloat() * videoWidth / videoHeight).toInt()
        } else {
            // 应该以屏幕宽进行缩放
            resultWidth = screenWidth
            resultHeight = tempHeight.toInt()
        }
        val oldParams = textureView.layoutParams
        if (oldParams != null && (oldParams.width != resultWidth || oldParams.height != resultHeight)) {
            oldParams.width = resultWidth
            oldParams.height = resultHeight
            textureView.layoutParams = oldParams
        }
    }


    private fun showThumbnail(path: String) {

        videoThumbnailView.visibility = View.VISIBLE
        GlideApp.with(requireContext())
            .load(path)
            .override(videoThumbnailView.width, videoThumbnailView.height)
            .into(videoThumbnailView)
    }


    override fun onDestroy() {
        super.onDestroy()
        videoPlayer?.stop()
        videoPlayer?.release()
        videoSurface?.release()
    }

}