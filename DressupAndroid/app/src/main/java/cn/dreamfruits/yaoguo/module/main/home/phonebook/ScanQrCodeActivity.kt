package cn.dreamfruits.yaoguo.module.main.home.phonebook

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.code
import cn.dreamfruits.yaoguo.module.login.state.GetShortUrlGetState
import cn.dreamfruits.yaoguo.module.login.state.GetShortUrlState
import cn.dreamfruits.yaoguo.module.main.home.state.GetSearchWordBeanState
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.example.zrqrcode.ZrQrCodeActivity
import com.example.zrqrcode.zxing.CameraScan
import com.example.zrqrcode.zxing.CaptureActivity
import com.example.zrqrcode.zxing.util.CodeUtils
import com.example.zrqrcode.zxing.util.LogUtils
import com.google.zxing.Result
import com.gyf.immersionbar.ktx.immersionBar
import org.koin.android.ext.android.get
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.Executors

/**
 * @Author qiwangi
 * @Date 2023/6/23
 * @TIME 02:13
 */
class ScanQrCodeActivity : CaptureActivity() {
    private val contactsViewModel by viewModels<ContactsViewModel>()

    /**
     * 扫码结果回调
     * @param result
     * @return 返回false表示不拦截，将关闭扫码界面并将结果返回给调用界面；
     * 返回true表示拦截，需自己处理逻辑。当isAnalyze为true时，默认会继续分析图像（也就是连扫）。
     * 如果只是想拦截扫码结果回调，并不想继续分析图像（不想连扫），请在拦截扫码逻辑处通过调
     * 用[CameraScan.setAnalyzeImage]，
     * 因为[CameraScan.setAnalyzeImage]方法能动态控制是否继续分析图像。
     */
    var userId = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBar()//设置透明
        setStatusBar()//动态设置状态栏高度
        hideNavigation()//隐藏底部导航
//        // 设置全屏
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView()

        userId = intent.getLongExtra("userId", 0L)
        Log.e("zqr1212   扫码回调：", "onPostResume")
        findViewById<View>(R.id.ll_code).setOnClickListener {
            val intent = Intent(this, QrCodeActivity::class.java)
            intent.putExtra("userId", userId)//这里传递自己的id
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.ll_photo).setOnClickListener {
            //扫码相册内的图片
            checkExternalStoragePermissions()
        }


        /**扫码请求结果回调*/
        contactsViewModel.getShortUrlGetState.observe(this) {
            when (it) {
                is GetShortUrlGetState.Success -> {
                    Log.e("zqr121278", "   扫码回调：" + it.toString())

                    when (contactsViewModel.mQrCodeShortUrlGetBean!!.type) {
                        0 -> {//用户
                            Singleton.startOtherUserCenterActivity(
                                this,
                                contactsViewModel.mQrCodeShortUrlGetBean!!.targetId
                            )
                            finish()
                        }

                        1 -> {//帖子

                        }

                        2 -> {//单品

                        }
                    }
                    isGetShortGetUrl = true
                }

                is GetShortUrlGetState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }

                    isGetShortGetUrl = true
                }
            }
        }

    }

    override fun onScanResultCallback(result: Result?): Boolean {
        Log.e("zqr1212   扫码回调：", result.toString())

        //相册结果回调
        var code = ""
        if (result.toString() != "") {
            code = result.toString().substringAfterLast("/")
        }

        Log.e("zqr121275768", "result:$result")
        Log.e("zqr121275768", "code:$code")
        /**请求结果*/
        if (isGetShortGetUrl) {
            contactsViewModel.getShortGetUrl(code)
        }

        return super.onScanResultCallback(result)
        //Toast.makeText(this,result.toString(),Toast.LENGTH_SHORT).show()
        // return true

        /*因为setAnalyzeImage方法能动态控制是否继续分析图像。
        1. 因为分析图像默认为true，如果想支持连扫，返回true即可。
        当连扫的处理逻辑比较复杂时，请在处理逻辑前调用getCameraScan().setAnalyzeImage(false)，
        来停止分析图像，等逻辑处理完后再调用getCameraScan().setAnalyzeImage(true)来继续分析图像。
        2. 如果只是想拦截扫码结果回调自己处理逻辑，但并不想继续分析图像（即不想连扫），可通过
        调用getCameraScan().setAnalyzeImage(false)来停止分析图像。*/
    }

    override fun onScanResultFailure() {
        Log.e("zqr1212   扫码失败：", "扫码失败")
        super.onScanResultFailure()
    }




    private fun initView() {
        /**返回按键*/
        findViewById<View>(R.id.ivBack).setOnClickListener {
            finish()
        }

        /**初始化扫描*/
        initScan()
    }

    /**
     * 初始化扫描
     */
    private fun initScan() {
        //设置扫描框
        viewfinderView = findViewById(R.id.viewfinderView2)
        viewfinderView.setLaserDrawable(R.drawable.qr_code_line)
        viewfinderView.background = resources.getDrawable(R.drawable.scan2)
    }

    /**复写扫描*/
    override fun initCameraScan() {
        super.initCameraScan()
        cameraScan
            .setPlayBeep(true)
            .setVibrate(true)
    }


    /**复写布局*/
    override fun getLayoutId(): Int = R.layout.activity_scan_qr_code

    /**权限检查*/
    var isCheckCameraPermissions = true
    override fun onResume() {
        super.onResume()
        if (isCheckCameraPermissions) {
            checkCameraPermissions()
        }
    }

    /**
     * 检测拍摄权限
     */
    @AfterPermissionGranted(ZrQrCodeActivity.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            /**开始扫码*/
            cameraScan.startCamera()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(com.example.zrqrcode.R.string.permission_camera),
                ZrQrCodeActivity.RC_CAMERA, *perms
            )
        }
    }

    /**=========功能2============扫码相册内的图片*/
    @AfterPermissionGranted(ZrQrCodeActivity.RC_READ_PHOTO)
    private fun checkExternalStoragePermissions() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            startPhotoCode()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(com.example.zrqrcode.R.string.permission_external_storage),
                ZrQrCodeActivity.RC_READ_PHOTO, *perms
            )
        }
    }

    /**调用相册*/
    private fun startPhotoCode() {
        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(pickIntent, ZrQrCodeActivity.REQUEST_CODE_PHOTO)
    }

    /**照片回调*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                /*ZrQrCodeActivity.REQUEST_CODE_SCAN -> {
                    val result = CameraScan.parseScanResult(data)
                    showToast(result)
                }*/
                ZrQrCodeActivity.REQUEST_CODE_PHOTO -> parsePhoto(data)
            }
        }
    }

    /**分析照片*/
    var isGetShortGetUrl = true
    private fun parsePhoto(data: Intent) {
        val bitmap = MediaStore.Images.Media.getBitmap(
            contentResolver, data.data
        )
        if (bitmap != null) {
            //异步解析
            asyncThread(Runnable {
                var result = ""
                try {
                    result = CodeUtils.parseCode(bitmap)

                    runOnUiThread {
                        //相册结果回调
                        var code = ""
                        if (result != "") {
                            code = result.substringAfterLast("/")

                        }
                        Log.e("zqr121275768", "result:$result")
                        Log.e("zqr121275768", "code:$code")

                        /**请求结果*/
                        if (isGetShortGetUrl) {
                            contactsViewModel.getShortGetUrl(code)
                            isGetShortGetUrl = false
                        }
                    }

                } catch (e: Exception) {
                    Log.e("zqr12122121", "e:$e")

                    runOnUiThread {
                        Singleton.centerToast(this@ScanQrCodeActivity, "解析失败或未检查到二维码")
                        finish()
                    }

                }
            })
        } else {
            Singleton.centerToast(this@ScanQrCodeActivity, "解析失败或无效的二维码")
        }

    }

    private val executor = Executors.newSingleThreadExecutor()

    /**接上*/
    private fun asyncThread(runnable: Runnable) {
        executor.execute(runnable)
    }

    /**
     * 初始化状态栏
     */
    fun initStatus() {
//        immersionBar {
//            fitsSystemWindows(false)
//        }

//        //隐藏虚拟按键，并且全屏
//        val decorView = window.decorView
//        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//        decorView.systemUiVisibility = uiOptions

//        //沉浸状态栏重叠问题
//        findViewById<View>(cn.dreamfruits.yaoguo.R.id.status_bar).layoutParams =
//            LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                BarUtils.getStatusBarHeight()
//            )
    }


    fun setStatusBar() {
        //沉浸状态栏重叠问题
        findViewById<View>(cn.dreamfruits.yaoguo.R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BarUtils.getStatusBarHeight()
            )
    }

    private fun setSystemBar() {//全透状态栏
        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) {
            //19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    private fun hideNavigation() {//隐藏底部导航 * 同时在所有themes文件 都添加 <item name="android:navigationBarColor">@color/transparent</item>
        val params: WindowManager.LayoutParams = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.attributes = params
    }


}