package cn.dreamfruits.yaoguo.module.share

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.ShareImageContent
import cn.dreamfruits.sociallib.entiey.content.ShareWebContent
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.share.adapter.ShareGoodsItemAdapter
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.util.loadRectImg
import cn.dreamfruits.yaoguo.util.loadRoundImg
import cn.dreamfruits.yaoguo.util.singleClick
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.zrqrcode.zxing.util.CodeUtils
import com.lxj.xpopup.core.BottomPopupView
import com.permissionx.guolindev.PermissionX
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * @author Lee
 * @createTime 2023-07-06 11 GMT+8
 * @desc :
 */
class SharePostPop(
    var mContext: Context,
    var mFeedDetailsBean: WaterfallFeedBean.Item.Info,
    var shareType: Int = TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_POST,
    var shortUrl: String,
) :
    BottomPopupView(mContext) {

    private lateinit var iv_cover_img: ImageView
    private lateinit var rv_share_list: RecyclerView
    private lateinit var iv_head_img: ImageView
    private lateinit var iv_share_code: ImageView
    private lateinit var tv_nick_name: TextView
    private lateinit var tv_desc: TextView
    private lateinit var tv_share_save: TextView
    private lateinit var tv_share_qq: TextView
    private lateinit var tv_share_wechat_circle: TextView
    private lateinit var tv_share_wechat_friend: TextView
    private lateinit var cl_content: ConstraintLayout
    private lateinit var mAdapter: ShareGoodsItemAdapter

    override fun getImplLayoutId(): Int = R.layout.layout_share_post

    override fun onCreate() {
        super.onCreate()
        findViewById<ImageView>(R.id.iv_close).setOnClickListener { dismiss() }
        iv_cover_img = findViewById(R.id.iv_cover_img)
        rv_share_list = findViewById(R.id.rv_share_list)
        iv_head_img = findViewById(R.id.iv_head_img)
        iv_share_code = findViewById(R.id.iv_share_code)
        tv_nick_name = findViewById(R.id.tv_nick_name)
        tv_desc = findViewById(R.id.tv_desc)
        tv_share_save = findViewById(R.id.tv_share_save)
        tv_share_qq = findViewById(R.id.tv_share_qq)
        tv_share_wechat_circle = findViewById(R.id.tv_share_wechat_circle)
        tv_share_wechat_friend = findViewById(R.id.tv_share_wechat_friend)
        cl_content = findViewById(R.id.cl_content)

        tv_desc.text = mFeedDetailsBean.content
        iv_cover_img.loadRectImg(mContext, mFeedDetailsBean.picUrls.first().url.decodePicUrls())
        iv_head_img.loadRoundImg(mContext, mFeedDetailsBean.userInfo.avatarUrl.decodePicUrls())
        tv_nick_name.text = mFeedDetailsBean.userInfo.nickName
        createQRCode(shortUrl)


        mAdapter = ShareGoodsItemAdapter(mContext)
        rv_share_list.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rv_share_list.adapter = mAdapter


        var list: MutableList<String> = arrayListOf()
        mFeedDetailsBean.singleList.forEach {
            list.add(it.coverUrl)
        }

        mAdapter.setList(list)



        tv_share_save.singleClick {
            PermissionX.init(mContext as FragmentActivity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        saveBitmap2file(ImageUtils.view2Bitmap(cl_content))
                    } else {
                        ToastUtils.showShort("权限被拒绝，保存失败！")
                    }
                }
        }

        tv_share_wechat_circle.singleClick {
            share(
                TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT_CIRCLE,
                ImageUtils.view2Bitmap(cl_content)
            )
        }
        tv_share_wechat_friend.singleClick {
            share(TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT, ImageUtils.view2Bitmap(cl_content))
        }
        tv_share_qq.singleClick {

            PermissionX.init(mContext as FragmentActivity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        share(
                            TIMCommonConstants.SHARE_CLICK_TYPE_QQ,
                            ImageUtils.view2Bitmap(cl_content)
                        )
                    } else {
                        ToastUtils.showShort("权限被拒绝，保存失败！")
                    }
                }

        }
    }

    /**
     * 生成二维码/条形码
     * @param isQRCode
     */
    private fun createQRCode(content: String) {
        Observable.create<Bitmap?> { emitter: ObservableEmitter<Bitmap?> ->
            var bitmap = CodeUtils.createQRCode(content, 600)
            emitter.onNext(bitmap)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { bitmap: Bitmap? ->
                iv_share_code.setImageBitmap(bitmap)
            }
    }

    /**
     * 保存图片到相册
     */
    fun saveBitmap2file(bmp: Bitmap): String {
        val file = ImageUtils.save2Album(bmp, Bitmap.CompressFormat.JPEG)
        ToastUtils.make().setBgColor(Color.parseColor("#80000000"))
            .setTextColor(Color.WHITE).show(if (file != null) "保存成功！" else "保存失败！请重新保存")
        return file!!.path
    }


    private fun share(shareType: Int, bitmap: Bitmap) {

        dismiss()

        var content = ShareImageContent(bitmap)
        if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_QQ) {
            content.description = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.JPEG)!!.path
        }

        Social.share(mContext,
            if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT) PlatformType.WEIXIN
            else if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT_CIRCLE) PlatformType.WEIXIN_CIRCLE
            else PlatformType.QQ,

            content = content,
            onSuccess = { type ->
                LogUtils.e(">>>> onSuccess = type = " + type)
                when (type) {
                    PlatformType.WEIXIN -> {
                        ToastUtils.showShort("分享成功")
                    }
                    PlatformType.QQ -> {
                        ToastUtils.showShort("分享成功")
                    }
                    PlatformType.WEIXIN_CIRCLE -> {
                        ToastUtils.showShort("分享成功")
                    }
                    PlatformType.QQ_ZONE -> TODO()
                    PlatformType.SINA_WEIBO -> TODO()
                    PlatformType.ALI -> TODO()
                }
            },
            onCancel = {
                LogUtils.e(">>>> onCancel = 用户取消 ")
                ToastUtils.showShort("用户取消")
            },
            onError = { _, _, msg ->
                LogUtils.e(">>>> onError =  $msg")
                ToastUtils.showShort("$msg")
            }
        )
    }

}