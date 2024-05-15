package com.example.zrframe.module.discovery

import androidx.lifecycle.viewModelScope
import com.example.zrframe.R
import com.example.zrframe.base.list.base.BaseRecyclerViewModel
import com.example.zrframe.base.list.base.BaseViewData
import com.example.zrframe.bean.CatagoryBean
import com.example.zrframe.bean.GoodsBean
import com.example.zrframe.constant.PageName
import com.example.zrframe.item.CatagoryListViewData
import com.example.zrframe.item.CatagoryViewData
import com.example.zrframe.item.GoodsViewData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiscoveryViewModel : BaseRecyclerViewModel() {

    override fun loadData(isLoadMore: Boolean, isReLoad: Boolean, page: Int) {
        viewModelScope.launch {
            delay(1000L)
            val viewDataList = mutableListOf<BaseViewData<*>>()
            if (!isLoadMore) {
                val categoryList = mutableListOf<CatagoryViewData>()
                categoryList.add(CatagoryViewData(CatagoryBean(R.drawable.icon_girl, "萌妹")))
                categoryList.add(CatagoryViewData(CatagoryBean(R.drawable.icon_cat, "撸猫")))
                categoryList.add(CatagoryViewData(CatagoryBean(R.drawable.icon_bodybuilding, "健身")))
                categoryList.add(CatagoryViewData(CatagoryBean(R.drawable.icon_movie, "电影")))
                categoryList.add(CatagoryViewData(CatagoryBean(R.drawable.icon_music, "音乐")))
                categoryList.add(CatagoryViewData(CatagoryBean(R.drawable.icon_game, "游戏")))
                categoryList.add(CatagoryViewData(CatagoryBean(R.drawable.icon_photography, "摄影")))
                categoryList.add(CatagoryViewData(CatagoryBean(R.drawable.icon_learn, "学习")))
                viewDataList.add(CatagoryListViewData(categoryList))

                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))

                postData(isLoadMore, viewDataList)
            } else {
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                viewDataList.add(GoodsViewData(GoodsBean("", "", 100, 50000L)))
                postData(isLoadMore, viewDataList)
            }
        }
    }

    @PageName
    override fun getPageName() = PageName.DISCOVERY
}