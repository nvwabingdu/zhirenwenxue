package cn.dreamfruits.yaoguo.module.selectclothes


import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.selector.PictureConstants
import cn.dreamfruits.selector.PublishPickerActivity
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.ViewPagerFragmentAdapter
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.HomeSearchDetailsActivity
import cn.dreamfruits.yaoguo.module.publish.FeedPublishActivity
import cn.dreamfruits.yaoguo.module.selectclothes.fragment.AttachGoodsFragment
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.view.dialog.commAlertDialog
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.LogUtils
import com.luck.picture.lib.entity.LocalMedia
import com.tencent.bugly.crashreport.CrashReport
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import java.util.ArrayList


/**
 * 选择版型页面
 */
class SelectClothesActivity : BaseActivity() {

    private lateinit var mBackIv: ImageView

    private lateinit var mNextTv: TextView

    private lateinit var viewPager: ViewPager
    private lateinit var magicIndicator: MagicIndicator
    private lateinit var include_viewpager_toolbar: Toolbar

    var mFragments: MutableList<Fragment> = arrayListOf()//fragment集合
    var mTitleList: MutableList<String> = arrayListOf()//标题集合

    private val clothesViewModel by viewModels<SelectClothesViewModel>()


    override fun layoutResId() = R.layout.activity_select_clothes

    override fun initView() {

        viewPager = findViewById(R.id.view_pager)
        include_viewpager_toolbar = findViewById(R.id.include_viewpager_toolbar)

        magicIndicator = findViewById(R.id.magic_indicator)

        mBackIv = findViewById(R.id.iv_back)

        mNextTv = findViewById(R.id.tv_next)

        mBackIv.setOnClickListener {
            onBackPressed()
        }

        findViewById<TextView>(R.id.tv_search)
            .setOnClickListener {
                SearchClothesActivity.start(
                    mContext,
                    clothesViewModel.selectedClothesList as ArrayList<ClothesBean>
                )
            }


        mNextTv.setOnClickListener {
//            if (clothesViewModel.reSelect) {
            val intent = Intent()
            intent.putParcelableArrayListExtra(
                RouterConstants.FeedPublish.KEY_CLOTHES_LIST,
                clothesViewModel.selectedClothesList as ArrayList<ClothesBean>
            )
            setResult(RouterConstants.FeedPublish.RESULT_SELECT_CLOTHES, intent)
            finish()
//
//            } else {
//                val intent = Intent(this@SelectClothesActivity, PublishPickerActivity::class.java)
//                startActivityForResult(intent, PictureConstants.IMAGE_RESULT_CODE)
//            }


        }
        BusUtils.register(this)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PictureConstants.IMAGE_RESULT_CODE) {
//            val intent = Intent(this@SelectClothesActivity, FeedPublishActivity::class.java)
//
//            val resultList =
//                data?.getParcelableArrayListExtra<LocalMedia>(PictureConstants.MEDIA_RESULT_LIST)
//                    ?: return
//
//            intent.putExtra(
//                RouterConstants.FeedPublish.KEY_CLOTHES_LIST,
//                clothesViewModel.selectedClothesList as ArrayList<ClothesBean>
//            )
//            intent.putExtra(
//                RouterConstants.FeedPublish.KEY_SEL_MEDIA_LIST,
//                resultList as ArrayList<LocalMedia>
//            )
//
//            startActivity(intent)
//        }
//    }


    override fun onDestroy() {
        super.onDestroy()
        BusUtils.unregister(this)
    }

    override fun initData() {
        mTitleList.add("推荐")
        mTitleList.add("我收藏的")
        mTitleList.add("我试穿的")
//        mTitleList.add("我浏览的")
        mTitleList.add("我的diy")
        mFragments.add(AttachGoodsFragment.newInstance(4))
        mFragments.add(AttachGoodsFragment.newInstance(0))
        mFragments.add(AttachGoodsFragment.newInstance(1))
//        mFragments.add(AttachGoodsFragment.newInstance(2))
        mFragments.add(AttachGoodsFragment.newInstance(3))

        viewPager.adapter = ViewPagerFragmentAdapter(supportFragmentManager, mFragments)
        magicIndicator.bindViewPager(viewPager, mTitleList)
        // 设置默认选中的Tab位置
        magicIndicator.onPageSelected(0)
        viewPager.currentItem = 0

        clothesViewModel.changeData.observe(this) { changeData ->
            clothesViewModel.selectedClothesList.let { clothesList ->
                if (clothesList.isNotEmpty()) {
                    mNextTv.text = "完成(${clothesList.size.toString()})"
                    mNextTv.isEnabled = true
                    mNextTv.setBackgroundColor(resources.getColor(R.color.black_222222))
                } else {
                    mNextTv.text = "完成"
                    mNextTv.isEnabled = false
                    mNextTv.setBackgroundColor(resources.getColor(R.color.gray_B3B3B3))
                }
            }
        }

        //重新选择单品
        val clothesList =
            intent?.getParcelableArrayListExtra<ClothesBean>(RouterConstants.FeedPublish.KEY_CLOTHES_LIST)
//        clothesList?.let {
//            //是重新选择
//        clothesViewModel.reSelect = true
//        }
//        if (clothesViewModel.reSelect) {
        clothesList?.let { getSearchData(it) }
//        }
    }


    /**
     * 显示对话框
     */
    private fun showDialog() {
        commAlertDialog(supportFragmentManager) {
            content = "返回后已选择的单品不会保留"
            cancelText = "取消"
            confirmText = "确认"
            cancelCallback = {

            }
            confirmCallback = {
                this@SelectClothesActivity.finish()
            }
        }
    }

    @BusUtils.Bus(tag = "REFRESH_SEL_DATA")
    fun getSearchData(list: List<ClothesBean>) {
        LogUtils.e(">>>>>")
        LogUtils.e(">>>>>" + list.toString())
        clothesViewModel.selectRest(list)
//        list.forEach {
//            clothesViewModel.select(it)
//        }
    }


    override fun onBackPressed() {
//        if (showingFragment() != null) {
//            closeFeature()
//            return
//        }
        if (clothesViewModel.selectedClothesList.isNotEmpty()) {
            showDialog()
        } else {
            super.onBackPressed()
        }
    }

    fun MagicIndicator.bindViewPager(
        viewPager: ViewPager,
        mStringList: List<String> = arrayListOf(),
        action: (index: Int) -> Unit = {},
    ) {
        val commonNavigator = CommonNavigator(context)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mStringList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return HomeSearchDetailsActivity.ScaleTransitionPagerTitleView(context).apply {
                    //设置文本
                    text = mStringList[index].toHtml()
                    //设置为粗体
                    //background=resources.getDrawable()
                    //字体大小
                    textSize = 16.0f
                    //不缩放效果
                    minScale = 1.0f
                    //未选中颜色
                    normalColor = ContextCompat.getColor(
                        getContext(),
                        R.color.color_bfbfbf
                    )
                    //选中颜色
                    selectedColor = ContextCompat.getColor(getContext(), R.color.black_222222)

                    //点击事件
                    setOnClickListener {
                        viewPager.currentItem = index
                        action.invoke(index)
                    }

                }
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    //mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    //线条的宽高度
                    lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
                    lineWidth = UIUtil.dip2px(context, 28.0).toFloat()
                    //// 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
                    yOffset = UIUtil.dip2px(context, 6.0).toFloat()
                    //线条的圆角
                    roundRadius = UIUtil.dip2px(context, 0.0).toFloat()
                    //设置拉长动画
                    // startInterpolator = AccelerateInterpolator()
                    // endInterpolator = DecelerateInterpolator(0.5f)
                    //线条的颜色
                    setColors(
                        ContextCompat.getColor(
                            getContext(),
                            cn.dreamfruits.yaoguo.R.color.black_161614
                        )
                    )
                }
            }
        }

        this.navigator = commonNavigator

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                this@bindViewPager.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                this@bindViewPager.onPageSelected(position)
                action.invoke(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                this@bindViewPager.onPageScrollStateChanged(state)
            }

        })

    }

    fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, flag)
        } else {
            Html.fromHtml(this)
        }
    }
}