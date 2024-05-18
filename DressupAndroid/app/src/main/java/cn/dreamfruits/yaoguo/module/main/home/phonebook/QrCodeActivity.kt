package cn.dreamfruits.yaoguo.module.main.home.phonebook

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.state.GetUserInfoState
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.module.share.ShareViewModel
import cn.dreamfruits.yaoguo.module.share.state.ShareShortUrlState
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.zrqrcode.zxing.util.CodeUtils
import com.gyf.immersionbar.ktx.immersionBar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @Author qiwangi
 * @Date 2023/6/23
 * @TIME 02:12
 */
class QrCodeActivity : BaseActivity() {
    private var ivBg: ImageView? = null
    private var mSave: ImageView? = null
    private val mineViewModel by viewModels<MineViewModel>()
    private var zrQrCodeView: ZrQrCodeView? = null
    var isCanSave = false//是否可以保存  优化保存逻辑
    var userId = 0L//下面数据用于生成
    var userName = ""
    var userAvatarUrl = ""

    private val shareViewModel by viewModels<ShareViewModel>()//h获取分享相关数据

    override fun initData() {}
    override fun layoutResId(): Int = R.layout.activity_qr_code


    override fun initView() {
        initLayout()

        initNet()
    }

    /**初始化*/
    private fun initLayout() {
        /**返回*/
        findViewById<ImageView>(R.id.back).setOnClickListener {
            finish()
        }

        /**有二维码海报自定义控件*/
        zrQrCodeView = findViewById<ZrQrCodeView>(R.id.qr_code_pic)

        /**用头像做背景*/
        ivBg = findViewById(R.id.iv_bg)

        /**获取传递过来的用户id*/
        userId = intent.getLongExtra("userId", 0L)

        /**保存图片*/
        mSave = findViewById(R.id.save)

        /** 保存二维码*/
        mSave!!.setOnClickListener {
            if (isCanSave) {
                // 将自定义控件转换为图片并显示在 ImageView 中
                val bitmapX: Bitmap = Bitmap.createBitmap(
                    zrQrCodeView!!.getWidth(),
                    zrQrCodeView!!.getHeight(),
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmapX)
                zrQrCodeView!!.draw(canvas)

                saveBitmapToGallery(this, bitmapX, "微酸二维码" + System.currentTimeMillis())
                //zrQrCodeView.setImageBitmap(bitmap)
            } else {
                Singleton.centerToast(this, "正在生成二维码，请稍后……")
            }
        }

        /** 跳转到扫一扫 */
        findViewById<ImageView>(R.id.scan).setOnClickListener {
            startActivity(Intent(this, ScanQrCodeActivity::class.java))
            finish()
        }

    }

    private fun initNet() {
        /**获取用户信息*/
        mineViewModel.getUserInfo(userId)
        /**用户回调信息*/
        mineViewModel.getUserInfoState.observe(this) {
            when (it) {
                is GetUserInfoState.Success -> {
                    /**背景鉴权*/
                    var bgUrl = Singleton.getUrlX(mineViewModel.mUserInfoBean!!.avatarUrl, true)

                    /**背景*/
                    Glide.with(this)
                        .asBitmap()
                        .dontAnimate()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .circleCrop()
                        .error(R.drawable.temp_icon)
                        .load(bgUrl)
                        .into(ivBg!!)

                    userName = mineViewModel.mUserInfoBean!!.nickName

                    userAvatarUrl = mineViewModel.mUserInfoBean!!.avatarUrl

                    /**生成二维码逻辑   请求短连接接口  获取生成二维码的内容*/
                    shareViewModel.getShortUrl(
                        mineViewModel.mUserInfoBean!!.userId.toString(),
                        TIMCommonConstants.SHARE_TYPE_USER,
                        TIMCommonConstants.SHARE_CLICK_TYPE_COPY
                    )
                }

                is GetUserInfoState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        shareViewModel.shortUrl.observe(this) {
            when (it) {
                is ShareShortUrlState.Success -> {
                    /**生成二维码*/
                    createQRCode(it.shortUrl, userName, userAvatarUrl)
                    /**这里之后就可以点击保存了*/
                    isCanSave = true
                }

                is ShareShortUrlState.Fail -> {
                    ToastUtils.showShort(it.msg)
                }
            }
        }
    }

    /**生成二维码*/
    private fun createQRCode(str: String, name: String, avatarUrl: String) {
        Thread {
            //生成二维码相关放在子线程里面
            // val logo = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background)
            val bitmap = CodeUtils.createQRCode(str, 600)
            runOnUiThread {
                //设置
                zrQrCodeView!!.setData(avatarUrl, name, bitmap)
                //ivCode!!.setImageBitmap(bitmap)
            }
        }.start()
    }

    /**
     * 保存bitmap方法
     */
    private val REQUEST_WRITE_EXTERNAL_STORAGE: Int = 1
    private fun saveBitmapToGallery(context: Context, bitmap: Bitmap, fileName: String) {
        // 判断是否有写入外部存储的权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            !== PackageManager.PERMISSION_GRANTED
        ) {
            // 如果没有，请求权限
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        }

        // 创建保存文件的目录
        var galleryDirectory: File? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android Q及以上版本使用MediaStore API操作
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            contentValues.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_DCIM + "/BAIChat"
            )
            val uri: Uri? = context.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            try {
                context.getContentResolver().openOutputStream(uri!!).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    Toast.makeText(context, "图片已保存至相册", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Android Q以下版本直接保存到指定路径
            galleryDirectory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "BAIChat"
            )
            if (!galleryDirectory.exists()) {
                galleryDirectory.mkdirs()
            }
            val file = File(galleryDirectory, "$fileName.png")
            try {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    // 更新图库
                    MediaScannerConnection.scanFile(
                        context,
                        arrayOf<String>(file.getAbsolutePath()),
                        null,
                        null
                    )
                    Toast.makeText(context, "图片已保存至相册", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 初始化状态栏
     */
    override fun initStatus() {
        immersionBar {
            fitsSystemWindows(false)
        }
        //沉浸状态栏重叠问题
        findViewById<View>(cn.dreamfruits.yaoguo.R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BarUtils.getStatusBarHeight()
            )
    }
}