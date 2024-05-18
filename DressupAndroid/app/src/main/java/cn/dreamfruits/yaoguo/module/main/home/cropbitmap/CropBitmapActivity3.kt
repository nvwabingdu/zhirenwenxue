package cn.dreamfruits.yaoguo.module.main.home.cropbitmap

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.state.FixUserInfoState
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.repository.ThridpartyRepository
import cn.dreamfruits.yaoguo.util.CosUpload
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.UriUtils
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * @Author qiwangi
 * @Date 2023/7/6
 * @TIME 11:29
 */
class CropBitmapActivity3: AppCompatActivity(), View.OnClickListener {

    private lateinit var likeView: LikeQQCropView
    private lateinit var btClip: ImageButton
    private lateinit var originPath: String
    private val mineViewModel by viewModels<MineViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen() // 设置全屏 wq
        setContentView(R.layout.activity_crop_bitmap)
        initView()
    }

    private fun initView() {
        likeView = findViewById(R.id.likeView)

        // 返回
        findViewById<View>(R.id.crop_back).setOnClickListener { finish() }

        btClip = findViewById(R.id.btClip)
        btClip.setOnClickListener(this)

        val path = intent.getStringExtra("path")
        originPath = path!!
        try {
            likeView.setBitmapForWidth(path, 1080)
            likeView.setRadius(0F) // 设置圆角为0
        } catch (e: Exception) {
            Toast.makeText(this, "裁剪图片失败", Toast.LENGTH_SHORT).show()
            finish()
        }
    }



    private val thridpartyRepository by inject<ThridpartyRepository>()
    private var isClip=false
    @SuppressLint("CheckResult")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btClip -> {
                if (isClip){
                    return
                }
                isClip=true
                // 裁剪
                TestBean.bitmap = likeView.clip()
                // 这里将bitmap保存在本地相册
               saveCropImg(TestBean.bitmap) // 转为图片地址
            }
        }
    }

    /**
     * 设置沉浸式 wq
     */
    private fun setFullScreen() {
        val decorView = window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // 设置布局全屏显示
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    /**
     * 通过传递bitmap过来实现添加像素
     * @return
     */
    private fun saveCropImg(tempBitmap: Bitmap){
        var url=""
        try {
            var stream: OutputStream? = null
            val fileName = "wei_suan_" + System.currentTimeMillis() + ".png"
            var path: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.e("zqr", "addTransparentBorderAndSaveToGallery()" + "安卓版本>=10.0")
                val resolver: ContentResolver = contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES
                )
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                stream = resolver.openOutputStream(uri!!)
                path = uri.toString()
            } else {
                Log.e("zqr", "addTransparentBorderAndSaveToGallery()" + "安卓版本<10.0")
                val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File(directory, fileName)
                stream = FileOutputStream(file)
                path = file.absolutePath
            }

            if (stream != null) {
                tempBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // 获取文件的实际路径
                    val cursor: Cursor? = contentResolver.query(
                        Uri.parse(path),
                        arrayOf(MediaStore.Images.Media.DATA),
                        null,
                        null,
                        null
                    )
                    if (cursor != null && cursor.moveToFirst()) {
                        path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                        cursor.close()
                    }
                }
                Log.e("zqr", "addTransparentBorderAndSaveToGallery()" + "加入像素之后的地址：" + path)
                if (path != null) {
                    url= path
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
            return
        }
        upPhoto(url)
    }

    @SuppressLint("CheckResult")
    private fun upPhoto(url:String){
        if (url == "") {
            Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show()
            return
        }

        /**点击保存之后回调*/
        mineViewModel.fixUserInfoState.observe(this) {
            when (it) {
                is FixUserInfoState.Success -> {
                    Singleton.centerToast(this, "修改成功")
                    //1.修改之后删除裁剪图片

                    try {
                        val imageFile: File = File(url)
                        if (imageFile.exists()) {
                            imageFile.delete()
                            Log.e("unity", "删除相册地址成功")
                        }
                    } catch (e: java.lang.Exception) {
                        Log.e("unity", "删除相册地址---异常" + e.message)
                    }

                    finish()
                }

                is FixUserInfoState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }

                    try {
                        val imageFile: File = File(url)
                        if (imageFile.exists()) {
                            imageFile.delete()
                            Log.e("unity", "删除相册地址成功")
                        }
                    } catch (e: java.lang.Exception) {
                        Log.e("unity", "删除相册地址---异常" + e.message)
                    }
                    finish()
                }
            }
        }

        var newUrl=""
        if (!url.contains("content")){
            newUrl=UriUtils.file2Uri(File(url)).toString()
        }
        //上传图片
        val filePathList = mutableListOf<String>()
        filePathList.add(newUrl)
        Log.e("zqr", "newUrl=====>"+newUrl)
        //获取腾讯云cos 临时密钥  成功获取之后 上传图片
        thridpartyRepository.getTencentTmpSecretKey()
            .subscribe({
                Log.e("zqr", "获取腾讯云cos 成功………………"+it.toString())
                val cosUpload = CosUpload()
                it.let { cosUpload.initService(it, applicationContext) }
                val resultUrlMap = mutableMapOf<String, String>()
                cosUpload.uploadFiles(filePathList,
                    onSuccess = { filePath, resultUrl ->
                        resultUrlMap[filePath] = resultUrl!!
                        Log.e("zqr", "上传成功之后的图片resultUrlMap$resultUrlMap")
                        Log.e("zqr", "上传成功之后的图片resultUrl$resultUrl")
                        mineViewModel.updateUserInfo(avatarUrl = resultUrl)
                    },
                    onFail = {
                        Log.e("zqr", "上传失败")
                        Singleton.centerToast(this, "上传失败")
                    },
                    onProgress = {
                        Log.e("zqr", "上传进度$it")

                    },
                    onComplete = {
//                        onSuccess.invoke(resultUrlMap)
                    }
                )
            }, {
//                Log.e("zqr", "获取腾讯云cos 临时密钥失败")
            })
    }

}