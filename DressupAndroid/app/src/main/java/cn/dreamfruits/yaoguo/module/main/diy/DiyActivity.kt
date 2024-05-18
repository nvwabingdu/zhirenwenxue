package cn.dreamfruits.yaoguo.module.main.diy

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import cn.dreamfruits.selector.PictureConstants
import cn.dreamfruits.selector.PublishPickerActivity
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.module.publish.FeedPublishActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.luck.picture.lib.entity.LocalMedia
import com.permissionx.guolindev.PermissionX


open class DiyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_stroll)

        initView()
        //类似弹窗效果
//         showPop()
    }

    fun initView() {
        //隐藏虚拟按键，并且全屏
        BarUtils.transparentStatusBar(this)

        val rl1 = findViewById<View>(R.id.rl_stroll_1)
        val rl2 = findViewById<View>(R.id.rl_stroll_2)
        val imgStrollBack = findViewById<ImageView>(R.id.img_stroll_back)

        //点击晒穿搭
        rl1.setOnClickListener {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        val intent = Intent(this@DiyActivity, PublishPickerActivity::class.java)
                        startActivityForResult(intent, PictureConstants.IMAGE_RESULT_CODE)
                    } else {
                        val list: MutableList<String> = mutableListOf()
                        deniedList.forEach {
                            if (it == Manifest.permission.CAMERA) {
                                list.add("相机")
                            }
                            if (it == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                                list.add("读取内存")
                            }
                            if (it == Manifest.permission.READ_EXTERNAL_STORAGE) {
                                list.add("写入内存")
                            }
                        }

                        ToastUtils.showShort("未开启" + list.toString() + "权限,请前往设置开启")

                    }
                }
        }

        //点击DIY
        rl2.setOnClickListener {
//            val intent = Intent(this, AndroidBridgeActivity::class.java)
//            intent.putExtra("entryType", "EnterDIY")
//            intent.putExtra("data", "2")
//            this.startActivity(intent)
            val intent = Intent(this, DiyFirstGradeActivity::class.java)
            startActivity(intent)
        }

        imgStrollBack.setOnClickListener {
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PictureConstants.IMAGE_RESULT_CODE) {
            val intent = Intent(this@DiyActivity, FeedPublishActivity::class.java)
            val resultList =
                data?.getParcelableArrayListExtra<LocalMedia>(PictureConstants.MEDIA_RESULT_LIST)
                    ?: return
            intent.putExtra(
                RouterConstants.FeedPublish.KEY_SEL_MEDIA_LIST,
                resultList as ArrayList<LocalMedia>
            )
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }

}