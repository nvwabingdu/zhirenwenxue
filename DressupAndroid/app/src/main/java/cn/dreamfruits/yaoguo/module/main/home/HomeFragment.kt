//package cn.dreamfruits.yaoguo.module.main.home
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Typeface
//import android.os.Bundle
//import android.text.Html
//import android.text.Spanned
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import androidx.viewpager2.widget.ViewPager2
//import cn.dreamfruits.yaoguo.module.main.home.child.RecommendFragment
//import cn.dreamfruits.yaoguo.module.main.home.child.AttentionFragment
//import cn.dreamfruits.yaoguo.module.main.home.searchhistory.HomeSearchHistoryActivity
//import cn.dreamfruits.yaoguo.module.main.home.textbanerview.TextBannerView
//import net.lucode.hackware.magicindicator.MagicIndicator
//import net.lucode.hackware.magicindicator.buildins.UIUtil
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
//
//
//class HomeFragment : Fragment() {
//    private var bannerTextView: TextBannerView?=null
//
//    private var view_pager:ViewPager2?=null
//    private var magic_indicator: MagicIndicator?=null
//
//    var fragments: ArrayList<Fragment> = arrayListOf()//fragment集合
//    var mDataList: ArrayList<String> = arrayListOf()//标题集合
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view=inflater.inflate(cn.dreamfruits.yaoguo.R.layout.home_fragment_home, container, false)
//        //联动模块
//        view_pager=view.findViewById<ViewPager2>(cn.dreamfruits.yaoguo.R.id.view_pager)
//        magic_indicator=view.findViewById<MagicIndicator>(cn.dreamfruits.yaoguo.R.id.magic_indicator)
//        //文字轮播
//        bannerTextView=view.findViewById<TextBannerView>(cn.dreamfruits.yaoguo.R.id.tv_banner)
//        setBannerText()
//
//        return view
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//
//
//
//        bannerTextView?.startViewAnimator() //bannerTextView防止文字重影
//
//        if(fragments.size==0){
//            fragments.add(AttentionFragment())
//            fragments.add(RecommendFragment())
//            fragments.add(RecommendFragment())
//            fragments.add(RecommendFragment())
//            fragments.add(RecommendFragment())
//            fragments.add(RecommendFragment())
//        }
//
//        if (mDataList.size==0){
//            mDataList.add("关注")
//            mDataList.add("推荐")
//            mDataList.add("Y2K")
//            mDataList.add("新中式")
//            mDataList.add("朋克")
//            mDataList.add("嬉皮士")
//        }
//
//        //初始化viewpager2
//        view_pager!!.init(this, fragments)
//        //初始化 magic_indicator
//        magic_indicator!!.bindViewPager2(view_pager!!, mDataList)
//
//    }
//
//
//    fun ViewPager2.init(fragment: Fragment, fragments: ArrayList<Fragment>, isUserInputEnabled: Boolean = true): ViewPager2 {
//        //是否可滑动
//        this.isUserInputEnabled = isUserInputEnabled
//        //设置适配器
//        adapter = object : FragmentStateAdapter(fragment) {
//            override fun createFragment(position: Int) = fragments[position]
//            override fun getItemCount() = fragments.size
//        }
//        return this
//    }
//
//
//    fun MagicIndicator.bindViewPager2(
//        viewPager: ViewPager2,
//        mStringList: List<String> = arrayListOf(),
//        action: (index: Int) -> Unit = {}) {
//        val commonNavigator = CommonNavigator(activity)
//        commonNavigator.adapter = object : CommonNavigatorAdapter() {
//            override fun getCount(): Int {
//                return  mStringList.size
//            }
//            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
//                return ScaleTransitionPagerTitleView(activity!!).apply {
//                    //设置文本
//                    text = mStringList[index].toHtml()
//                    //设置为粗体
//                    //background=resources.getDrawable()
//                    //字体大小
//                    textSize = 23f
//                    //未选中颜色
//                    // normalColor = ContextCompat.getColor(getContext(), R.color.color_pitch)
//                    normalColor = ContextCompat.getColor(getContext(), cn.dreamfruits.yaoguo.R.color.color_tab_title_pitch)
//                    //选中颜色
//                    // selectedColor =  ContextCompat.getColor(getContext(), R.color.color_pitch_ed)
//                    selectedColor =  ContextCompat.getColor(getContext(), cn.dreamfruits.yaoguo.R.color.color_tab_title_pitch_ed)
//                    //点击事件
//                    setOnClickListener {
//                        viewPager.currentItem = index
//                        action.invoke(index)
//                    }
//                }
//            }
//
//            override fun getIndicator(context: Context): IPagerIndicator {
//                return LinePagerIndicator(context).apply {
//                    mode = LinePagerIndicator.MODE_EXACTLY
//                    //mode = LinePagerIndicator.MODE_WRAP_CONTENT
//                    //线条的宽高度
//                    lineHeight = UIUtil.dip2px(activity, 2.0).toFloat()
//                    lineWidth = UIUtil.dip2px(activity, 28.0).toFloat()
//                    //// 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
//                    yOffset=UIUtil.dip2px(activity, 4.0).toFloat()
//                    //线条的圆角
//                    roundRadius = UIUtil.dip2px(activity, 0.0).toFloat()
//                    //设置拉长动画
//                    // startInterpolator = AccelerateInterpolator()
//                    // endInterpolator = DecelerateInterpolator(0.5f)
//                    //线条的颜色
//                    setColors(ContextCompat.getColor(getContext(), cn.dreamfruits.yaoguo.R.color.color_tab_indicator_line))
//                }
//            }
//        }
//
//        this.navigator = commonNavigator
//
//
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                this@bindViewPager2.onPageSelected(position)
//                action.invoke(position)
//            }
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//                this@bindViewPager2.onPageScrollStateChanged(state)
//            }
//        })
//    }
//
//
//    class ScaleTransitionPagerTitleView(context: Context) : ColorTransitionPagerTitleView(context) {
//        //设置为粗体
//        override fun setTypeface(tf: Typeface?) {
//            super.setTypeface(Typeface.DEFAULT)
//        }
//
//        var minScale = 0.7f
//        override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
//            super.onEnter(index, totalCount, enterPercent, leftToRight)    // 实现颜色渐变
//            scaleX = minScale + (1.0f - minScale) * enterPercent
//            scaleY = minScale + (1.0f - minScale) * enterPercent
//        }
//
//        override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
//            super.onLeave(index, totalCount, leavePercent, leftToRight)    // 实现颜色渐变
//            scaleX = 1.0f + (minScale - 1.0f) * leavePercent
//            scaleY = 1.0f + (minScale - 1.0f) * leavePercent
//        }
//    }
//
//
//    fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
//        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            Html.fromHtml(this, flag)
//        } else {
//            Html.fromHtml(this)
//        }
//    }
//
//
//    fun setBannerText(){
//        //初始化TextBannerView
//        //已在上面初始化
//
//        //设置数据
//        val list: MutableList<String> = ArrayList()
//
//        list.add("搜索")
//        list.add("Y2K")
//        list.add("新中式")
//        list.add("成路")
//        list.add("你是最棒的")
//
//        //调用setDatas(List<String>)方法后,TextBannerView自动开始轮播 //注意：此方法目前只接受List<String>类型
//        bannerTextView?.setDatas(list)
//
//        //设置TextBannerView点击监听事件，返回点击的data数据, 和position位置
//        bannerTextView?.setItemOnClickListener { data, position ->
//
//            val intent =  Intent(activity, HomeSearchHistoryActivity::class.java)
//            startActivity(intent)
//
////            val popWindow = CommonPopupWindow.PopupWindowBuilder(activity)
////                .setView(R.layout.dialog_net_error)
////                .setFocusable(true)
////                .setOutsideTouchable(true)
////                .create();
////            popWindow.showAsDropDown(bannerTextView,0,10)
//
//
//            Log.i("点击了：", "$position>>$data") }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        bannerTextView?.stopViewAnimator()//防止文字重影2
//    }
//}