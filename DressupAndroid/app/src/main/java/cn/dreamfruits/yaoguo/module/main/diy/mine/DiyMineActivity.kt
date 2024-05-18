package cn.dreamfruits.yaoguo.module.main.diy.mine

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.ViewPagerFragmentAdapter
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * @Author qiwangi
 * @Date 2023/6/5
 * @TIME 10:32
 */
open class DiyMineActivity: BaseActivity() {

    override fun layoutResId(): Int = R.layout.activity_diy_mine
    override fun initData() {}

    override fun initView() {
        top()
        init()
    }

    /**
     * 顶部
     */
    fun top(){
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }

    private var viewPager: ViewPager?=null
    private var magicIndicator: MagicIndicator?=null
    private var fragments: ArrayList<Fragment> = arrayListOf()//fragment集合
    private var mDataList: ArrayList<String> = arrayListOf()//标题集合
    /**
     * 初始化
     */
    private fun init(){
        if (mDataList.size==0){
            mDataList.add("全部")
            mDataList.add("公开")
            mDataList.add("秘密")
        }

        if(fragments.size==0){
            for (i in 0..2) {
                val mDiyMineFragment = DiyMineFragment()
                val bundle = Bundle()
                bundle.putString("key", mDataList[i])
                mDiyMineFragment.arguments = bundle
                fragments.add(mDiyMineFragment)
            }
        }

        //联动模块
        viewPager=findViewById<ViewPager>(R.id.view_pager)
        magicIndicator=findViewById<MagicIndicator>(R.id.magic_indicator)
        //初始化viewpager2
        viewPager!!.adapter = ViewPagerFragmentAdapter(this?.supportFragmentManager!!, fragments)
        //初始化 magic_indicator
        magicIndicator!!.bindViewPager(viewPager!!, mDataList)
        viewPager!!.offscreenPageLimit=3//预加载写死 避免切换的时候，状态改变
    }

    /**
     * 指示器绑定viewpager
     */
    fun MagicIndicator.bindViewPager(
        viewPager: ViewPager,
        mStringList: List<String> = arrayListOf(),
        action: (index: Int) -> Unit = {}) {
        val commonNavigator = CommonNavigator(applicationContext)
        //均分屏幕宽度的模式
        commonNavigator.isAdjustMode = true//是否平分屏幕宽度，默认为 true，设为 false 则表示不平分
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return  mStringList.size
            }
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(applicationContext!!).apply {
                    //设置文本
                    text = mStringList[index].toHtml()
                    //设置为粗体
                    //background=resources.getDrawable()
                    //字体大小
                    textSize = 23f
                    //未选中颜色
                    // normalColor = ContextCompat.getColor(getContext(), R.color.color_pitch)
                    normalColor = ContextCompat.getColor(getContext(), cn.dreamfruits.yaoguo.R.color.color_tab_title_pitch)
                    //选中颜色
                    // selectedColor =  ContextCompat.getColor(getContext(), R.color.color_pitch_ed)
                    selectedColor =  ContextCompat.getColor(getContext(), cn.dreamfruits.yaoguo.R.color.color_tab_title_pitch_ed)
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
                    lineHeight = UIUtil.dip2px(applicationContext, 2.0).toFloat()
                    lineWidth = UIUtil.dip2px(applicationContext, 28.0).toFloat()
                    //// 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
                    yOffset= UIUtil.dip2px(applicationContext, 4.0).toFloat()
                    //线条的圆角
                    roundRadius = UIUtil.dip2px(applicationContext, 0.0).toFloat()

                    //设置拉长动画
                    // startInterpolator = AccelerateInterpolator()
                    // endInterpolator = DecelerateInterpolator(0.5f)
                    //线条的颜色
                    setColors(ContextCompat.getColor(getContext(), R.color.color_tab_indicator_line))
                }
            }

        }

        this.navigator = commonNavigator

        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
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

    /**
     * 设置标题文字
     */
    class ScaleTransitionPagerTitleView(context: Context) : ColorTransitionPagerTitleView(context) {
        //设置为粗体
        override fun setTypeface(tf: Typeface?) {
            super.setTypeface(Typeface.DEFAULT)
        }

        var minScale = 0.7f
        override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
            super.onEnter(index, totalCount, enterPercent, leftToRight)    // 实现颜色渐变
            scaleX = minScale + (1.0f - minScale) * enterPercent
            scaleY = minScale + (1.0f - minScale) * enterPercent
        }

        override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
            super.onLeave(index, totalCount, leavePercent, leftToRight)    // 实现颜色渐变
            scaleX = 1.0f + (minScale - 1.0f) * leavePercent
            scaleY = 1.0f + (minScale - 1.0f) * leavePercent
        }
    }

    fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, flag)
        } else {
            Html.fromHtml(this)
        }
    }
}