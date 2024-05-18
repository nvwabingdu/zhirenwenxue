package cn.dreamfruits.yaoguo.adapter.base.listener

import androidx.recyclerview.widget.GridLayoutManager

/**
 * @author Lee
 * @createTime 2023-06-20 14 GMT+8
 * @desc :
 */
interface GridSpanSizeLookup {

    fun getSpanSize(gridLayoutManager: GridLayoutManager, viewType: Int, position: Int): Int
}