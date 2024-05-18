package cn.dreamfruits.yaoguo.module.main.mine

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.*
import bolts.Task.forResult
import cn.dreamfruits.selector.PictureConstants
import cn.dreamfruits.selector.PublishPickerActivity
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.cropbitmap.CropBitmapActivity
import cn.dreamfruits.yaoguo.module.main.home.cropbitmap.CropBitmapActivity2
import cn.dreamfruits.yaoguo.module.main.home.cropbitmap.CropBitmapActivity3
import cn.dreamfruits.yaoguo.module.main.home.cropbitmap.TestBean
import cn.dreamfruits.yaoguo.util.GlideEngine
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.UriUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.zrsinglephoto.easyphotos.EasyPhotos
import com.example.zrsinglephoto.easyphotos.models.album.entity.Photo
import com.example.zrsinglephoto.easyphotos.setting.Setting.albumItemsAdView
import com.gyf.immersionbar.ktx.immersionBar
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener

/**
 * @Author qiwangi
 * @Date 2023/6/27
 * @TIME 14:57
 */
class IconCropActivity : BaseActivity() {

    override fun initData() {}

    override fun layoutResId(): Int = R.layout.activity_icon_crop

    override fun initView() {
        init()
    }

    fun init() {
        /**返回*/
        findViewById<ImageView>(R.id.back).setOnClickListener {
            finish()
        }


        val icon = findViewById<ImageView>(R.id.icon)


        val url = intent.getStringExtra("iconUrl")


        val confirm = findViewById<TextView>(R.id.confirm)


        if (url.equals("crop")) {
            icon.setImageBitmap(TestBean.bitmap)
        } else {
            Glide.with(this)
                .asBitmap()
                .dontAnimate()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(ColorDrawable(Color.WHITE))
                .load(url)
                .into(icon)
        }


        confirm.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.instance)
                .setSelectionMode(SelectModeConfig.SINGLE)
//                    .setCropEngine(CropBitmapActivity::class.java)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: ArrayList<LocalMedia>?) {

                        Log.e(
                            "zqr",
                            "onResult:" + UriUtils.uri2File(Uri.parse(result!!.get(0).path)).absolutePath
                        )

                        //跳转到裁剪页面
                        val intent = Intent(this@IconCropActivity, CropBitmapActivity3::class.java)
                        intent.putExtra(
                            "path",
                            UriUtils.uri2File(Uri.parse(result!!.get(0).path)).absolutePath
                        ) //传递图片路径
                        startActivity(intent)
                        finish()
                    }

                    override fun onCancel() {

                    }

                })
        }
    }

//
//    private val photosAdView: RelativeLayout? = null
//    private  var albumItemsAdView:RelativeLayout? = null
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
////        if (albumItemsAdView != null) {
////            if (albumItemsAdView!!.getParent() != null) {
////                (albumItemsAdView!!.getParent() as FrameLayout).removeAllViews()
////            }
////        }
////        if (photosAdView != null) {
////            if (photosAdView.parent != null) {
////                (photosAdView.parent as FrameLayout).removeAllViews()
////            }
////        }
////        if (RESULT_OK == resultCode) {
////            //相机或相册回调
////            if (requestCode == 101) {
////                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
////                val resultPhotos = data!!.getParcelableArrayListExtra<Photo>(EasyPhotos.RESULT_PHOTOS)
////                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
//////                boolean selectedOriginal =
//////                        data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
////                Log.e("zqr", "onActivityResult: " + resultPhotos!![0].path)
////                //跳转到裁剪页面
////                val intent = Intent(this, CropBitmapActivity2::class.java)
////                intent.putExtra("path", resultPhotos[0].path) //传递图片路径
////                startActivity(intent)
////                //                return;
////            }
////        } else if (RESULT_CANCELED == resultCode) {
////            Toast.makeText(applicationContext, "cancel", Toast.LENGTH_SHORT).show()
////        }
//    }
//    //==========================以上图片裁剪


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