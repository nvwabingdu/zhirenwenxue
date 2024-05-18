package cn.dreamfruits.selector.preview


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia

class PreviewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val dataList = mutableListOf<LocalMedia>()


    constructor(
        fragmentActivity: FragmentActivity,
        list: List<LocalMedia>
    ) : this(fragmentActivity){
        dataList.addAll(list)
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

    override fun createFragment(position: Int): Fragment {
        val media = dataList[position]

        return if (PictureMimeType.isHasImage(media.mimeType)){
            ImageFragment.newInstance(media)
        }else{
            VideoFragment.newInstance(media)
        }
    }


}