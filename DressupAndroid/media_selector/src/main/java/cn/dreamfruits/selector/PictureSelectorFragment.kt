package cn.dreamfruits.selector

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.SizeUtils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMediaFolder
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * 照片 视频选择
 */
internal class PictureSelectorFragment : Fragment() {

    private lateinit var magicIndicator: MagicIndicator
    private lateinit var viewPager2: ViewPager2
    private lateinit var folderName:TextView
    private lateinit var arrows: ImageView
    private lateinit var topBar: RelativeLayout
    private lateinit var mExit: ImageView


    private val mTitles = arrayOf("全部", "视频", "照片")
    private val fragments = arrayListOf<Fragment>()

    private var allListFragment: AlbumFragment? = null
    private var videoListFragment: AlbumFragment? = null
    private var imageListFragment: AlbumFragment? = null
    private var folderPopupView: FolderPopupView? = null



    private val viewModel by activityViewModels<PictureSelectModel>()


    companion object {
        @JvmStatic
        fun newInstance(): PictureSelectorFragment {
            val args = Bundle()

            val fragment = PictureSelectorFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        magicIndicator = view.findViewById(R.id.magic_indicator)
        viewPager2 = view.findViewById(R.id.view_pager2)
        folderName = view.findViewById(R.id.tv_folder_name)
        arrows = view.findViewById(R.id.arrows)
        topBar = view.findViewById(R.id.top_app_bar)
        mExit = view.findViewById(R.id.iv_exit)

        arrows.setOnClickListener {
            folderName.performClick()
        }

        folderName.setOnClickListener {
            if (folderPopupView != null){
                XPopup.Builder(requireContext())
                    .atView(topBar)
                    .shadowBgColor(resources.getColor(R.color.black_081116))
                    .popupPosition(PopupPosition.Bottom)
                    .asCustom(folderPopupView)
                    .show()
            }
        }
        mExit.setOnClickListener {
            requireActivity().finish()
        }


        initIndicator()
        initViewPager2()
        initData()
    }


    private fun initData(){
        //选中或取消选中
        viewModel.changeData.observe(viewLifecycleOwner) { changeData ->
            when(viewPager2.currentItem){
                0 -> allListFragment?.refreshState(changeData)
                1 -> videoListFragment?.refreshState(changeData)
                2 -> imageListFragment?.refreshState(changeData)
            }
        }
        /**
         * 刷新交换后 数字
         */
        viewModel.swapItemIndex.observe(viewLifecycleOwner){index ->
            when(viewPager2.currentItem){
                0 -> allListFragment?.swapItem(index.first,index.second)
                1 -> videoListFragment?.swapItem(index.first,index.second)
                2 -> imageListFragment?.swapItem(index.first,index.second)
            }
        }

        PictureSelector.create(this)
            .dataSource(SelectMimeType.ofAll())
            .buildMediaLoader()
            .loadAllAlbum { localMediaFolder ->

                folderPopupView = FolderPopupView(requireContext(),localMediaFolder)
                folderPopupView?.setOnIBridgeAlbumWidget(object :OnFolderClickListener{
                    override fun onItemClick(position: Int, curFolder: LocalMediaFolder) {
                        folderPopupView?.dismiss()
                        //如果点击的是相同相册 则不做处理
                        if (curFolder.bucketId == viewModel.bucketId) return

                        folderName.text = curFolder.folderName

                        /**
                         * 切换相册
                         */
                        viewModel.bucketId = curFolder.bucketId
                        when(viewPager2.currentItem){
                            0 -> allListFragment?.switchAlbum()
                            1 -> videoListFragment?.switchAlbum()
                            2 -> imageListFragment?.switchAlbum()
                        }
                    }
                })

            }


    }


    private fun initIndicator() {
        val commonNavigator = CommonNavigator(requireContext())
        magicIndicator.setBackgroundColor(resources.getColor(R.color.black_081116))
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val pagerTitleView = ColorTransitionPagerTitleView(context)
                pagerTitleView.normalColor = Color.GRAY
                pagerTitleView.selectedColor = Color.WHITE
                pagerTitleView.text = mTitles[index]
                pagerTitleView.setOnClickListener {
                    viewPager2.currentItem = index
                }

                return pagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? = null
        }

        magicIndicator.navigator = commonNavigator

        val titleContainer = commonNavigator.titleContainer
        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return SizeUtils.dp2px(44f)
            }
        }
    }


    private fun initViewPager2() {
        allListFragment = AlbumFragment.newInstance(1)
        videoListFragment = AlbumFragment.newInstance(2)
        imageListFragment = AlbumFragment.newInstance(3)
        fragments.add(allListFragment!!)
        fragments.add(videoListFragment!!)
        fragments.add(imageListFragment!!)
        viewPager2.offscreenPageLimit = 1
        viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                magicIndicator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                magicIndicator.onPageScrollStateChanged(state)
            }
        })

    }

}