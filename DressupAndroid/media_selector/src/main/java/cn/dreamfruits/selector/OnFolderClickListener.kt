package cn.dreamfruits.selector

import com.luck.picture.lib.entity.LocalMediaFolder

interface OnFolderClickListener{
    /**
     * 专辑列表点击事件
     *
     * @param position  下标
     * @param curFolder 当前相册
     */
    fun onItemClick( position:Int, curFolder: LocalMediaFolder);
}