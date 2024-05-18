package cn.dreamfruits.selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import java.util.*

class PictureSelectModel : ViewModel() {

    private val _selectedList = mutableListOf<LocalMedia>()
    val selectedList: List<LocalMedia> get() = _selectedList

    private val _selectedCount = MutableLiveData<Int>()
    val selectedCount: LiveData<Int> get() = _selectedCount

    private val _changeData = MutableLiveData<LocalMedia>()
    val changeData: LiveData<LocalMedia> get() = _changeData

    private val _swapItemIndex = MutableLiveData<Pair<Int, Int>>()
    val swapItemIndex: LiveData<Pair<Int, Int>> get() = _swapItemIndex


    private val _AddAllData = MutableLiveData<MutableList<LocalMedia>>()
    val addAllData: LiveData<MutableList<LocalMedia>> get() = _AddAllData


    //默认 全部相册
    var bucketId: Long = -1


    init {
        _selectedCount.value = 0
    }


    fun select(localMedia: LocalMedia) {
//        LogUtils.e(">>>" + _selectedList.size)
//        if (_selectedList.size >= 9) {
//            return
//        }  
        if (_selectedCount.value!! >= 9) {
            return
        }

        if (_selectedList.isNotEmpty()) {
            if (_selectedList.first().mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_IMAGE)
                && !localMedia.mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_IMAGE)
            ) {
                return
            }
            if (_selectedList.first().mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_VIDEO)
                && !localMedia.mimeType.startsWith(
                    PictureMimeType.MIME_TYPE_PREFIX_VIDEO
                )
            ) {
                return
            }
        }

        if (_selectedList.isNotEmpty())
            if (_selectedList.first().mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_VIDEO)) {
                if (_selectedCount.value!! >= 1) {
                    return
                }
            }

        _selectedList.add(localMedia)
        _changeData.value = localMedia
        _selectedCount.value = _selectedCount.value!! + 1
    }

    fun addAll(localMedia: MutableList<LocalMedia>) {
        _selectedList.addAll(localMedia)
        _selectedCount.value = _selectedList.size
    }

    fun cancel(localMedia: LocalMedia) {
        _selectedList.remove(localMedia)
        _changeData.value = localMedia
        _selectedCount.value = selectedCount.value!! - 1
    }


    fun swap(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(_selectedList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(_selectedList, i, i - 1)
            }
        }
        _swapItemIndex.value = Pair(fromPosition, toPosition)
    }

}