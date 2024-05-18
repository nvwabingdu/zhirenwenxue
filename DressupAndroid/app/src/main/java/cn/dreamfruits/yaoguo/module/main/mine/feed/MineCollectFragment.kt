package cn.dreamfruits.yaoguo.module.main.mine.feed

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.ViewPagerFragmentAdapter
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.HomeSearchDetailsActivity
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.mine.feed.incollect.MineCollectInnerFragment
import cn.dreamfruits.yaoguo.util.Singleton
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView


class MineCollectFragment : Fragment() {
    private var mRootView: View? = null
    private var targetId:Long? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_user_center_collect, container, false)


        Log.e("zqr", "onCreateView: " )

        /**获取传递过来的targetId*/
        targetId= arguments!!.getLong("targetId")


        /**初始化view*/
        initLayout(mRootView!!)


        return mRootView
    }


//    var magicIndicator = findViewById<View>(R.id.magic_indicator8) as MagicIndicator
//    magicIndicator.setBackgroundColor(android.graphics.Color.WHITE)
//    var commonNavigator = CommonNavigator(this)
//    commonNavigator.setScrollPivotX(0.35f)
//    commonNavigator.setAdapter(
//    object : CommonNavigatorAdapter() {
//        override fun getCount(): Int {
//            return if (mDataList == null) 0 else mDataList.size
//        }
//
//        override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
//            val simplePagerTitleView = SimplePagerTitleView(context)
//            simplePagerTitleView.setText(mDataList.get(index))
//            simplePagerTitleView.setNormalColor(Color.parseColor("#333333"))
//            simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"))
//            simplePagerTitleView.setOnClickListener(View.OnClickListener {
//                mViewPager.setCurrentItem(
//                    index
//                )
//            })
//            return simplePagerTitleView
//        }
//
//        override fun getIndicator(context: Context?): IPagerIndicator? {
//            val indicator = WrapPagerIndicator(context)
//            indicator.setFillColor(Color.parseColor("#ebe4e3"))
//            return indicator
//        }
//    })
//    magicIndicator.setNavigator(commonNavigator)
//    ViewPagerHelper.bind(magicIndicator, mViewPager)
//
    //============================




    /**
     * tab+viewpager联动
     */
    private var viewpagerAdapter: ViewPagerFragmentAdapter? = null
    private var viewPager: ViewPager? = null
    private var magicIndicator: MagicIndicator? = null
    var fragments: MutableList<Fragment> = arrayListOf()//fragment集合
    var mTitleList: MutableList<String> = arrayListOf()//标题集合
    private fun initLayout(view: View) {
        viewPager = view.findViewById(R.id.view_pager)
        magicIndicator = view.findViewById<MagicIndicator>(R.id.magic_indicator)


        if (mTitleList.size == 0) {
            mTitleList.add("帖子")
            if (targetId == Singleton.getUserInfo().userId){
                mTitleList.add("话题")
            }
        }

        if (fragments.size == 0) {
            (0 until mTitleList.size).forEach {
                val f = MineCollectInnerFragment()
                val bundle = Bundle()
                if (targetId == 0L) {
                    bundle.putLong("targetId", Singleton.getLoginInfo().userId)
                } else {
                    bundle.putLong("targetId", targetId!!)
                }
                bundle.putString("title", mTitleList[it])
                f.arguments = bundle
                fragments.add(f)
            }
        }

        /**viewpager适配器*/
        viewpagerAdapter = ViewPagerFragmentAdapter(childFragmentManager, fragments)
        viewPager!!.adapter = viewpagerAdapter
        magicIndicator!!.bindViewPager(viewPager!!, mTitleList)
    }

    /**
     * 指示器绑定viewpager
     */
    fun MagicIndicator.bindViewPager(
        viewPager: ViewPager,
        mStringList: List<String> = arrayListOf(),
        action: (index: Int) -> Unit = {}
    ) {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.isAdjustMode = false//是否平分屏幕宽度，默认为 true，设为 false 则表示不平分
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mStringList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return SimplePagerTitleView(activity!!).apply {
                    //设置文本
                    text = mStringList[index].toHtml()
                    //设置为粗体
                    //background=resources.getDrawable()
//                    background = resources.getDrawable(R.drawable.shape_solid_efefef)

                    //字体大小
                    textSize = 16.0f
                    //不缩放效果
//                    minScale = 1.0f
                    //未选中颜色
                    normalColor = ContextCompat.getColor(getContext(), R.color.color_pitch2)
                    //选中颜色
                    selectedColor = ContextCompat.getColor(getContext(), R.color.color_pitch_ed2)

                    //点击事件
                    setOnClickListener {
                        viewPager.currentItem = index
                        action.invoke(index)
                    }

                }
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return WrapPagerIndicator(context).apply {

                    // 创建指示器视图


//                    mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    //mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    //线条的宽高度
//                    lineHeight = UIUtil.dip2px(activity, 2.0).toFloat()
//                    lineWidth = UIUtil.dip2px(activity, 100.0).toFloat()
                    //// 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
//                    yOffset = UIUtil.dip2px(activity, 0.0).toFloat()
                    //线条的圆角
//                    roundRadius = UIUtil.dip2px(activity, 0.0).toFloat()
                    //设置拉长动画
                    // startInterpolator = AccelerateInterpolator()
                    // endInterpolator = DecelerateInterpolator(0.5f)
                    //线条的颜色
//                    setColors(
//                        ContextCompat.getColor(
//                            getContext(),
//                           R.color.transparent
//                        )
//                    )
//                    val indicator = WrapPagerIndicator(context)
                    fillColor = Color.parseColor("#efefef")


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
        override fun onEnter(
            index: Int,
            totalCount: Int,
            enterPercent: Float,
            leftToRight: Boolean
        ) {
            super.onEnter(index, totalCount, enterPercent, leftToRight)    // 实现颜色渐变
            scaleX = minScale + (1.0f - minScale) * enterPercent
            scaleY = minScale + (1.0f - minScale) * enterPercent
        }

        override fun onLeave(
            index: Int,
            totalCount: Int,
            leavePercent: Float,
            leftToRight: Boolean
        ) {
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