package cn.dreamfruits.selector.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import cn.dreamfruits.baselib.network.imageloader.glide.GlideApp
import cn.dreamfruits.selector.R
import com.luck.picture.lib.entity.LocalMedia

/**
 * 图片预览
 */
class ImageFragment : Fragment() {

    private lateinit var image: ImageView


    companion object {
        @JvmStatic
        fun newInstance(media: LocalMedia): ImageFragment {
            val args = Bundle()

            val fragment = ImageFragment()
            args.putParcelable("media", media)

            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        image = view.findViewById(R.id.preview_image)

        val path = arguments?.getParcelable("media") as? LocalMedia
        GlideApp.with(requireContext())
            .load(path?.path)
            .into(image)
    }


}