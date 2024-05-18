package cn.dreamfruits.yaoguo.module.publish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.publish.state.TopicState
import cn.dreamfruits.yaoguo.module.publish.state.UserState
import cn.dreamfruits.yaoguo.repository.AttentionRepository
import cn.dreamfruits.yaoguo.repository.LabelRepository
import cn.dreamfruits.yaoguo.repository.SearchRepository
import cn.dreamfruits.yaoguo.repository.bean.mention.TopicBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import com.luck.picture.lib.entity.LocalMedia
import org.koin.core.component.inject

class FeedPublishViewModel : BaseViewModel() {

    /**
     * 已选择的视频
     */
    var video: LocalMedia? = null

    var mClothesList: MutableList<ClothesBean>? = null

    //单品属于什么性别 0=女 1 =男
    var gender = -1

    private val labelRepository by inject<LabelRepository>()
    private val searchRepository by inject<SearchRepository>()
    private val attentionRepository by inject<AttentionRepository>()


    private val _hotTopicState = MutableLiveData<TopicState>()
    val hotTopicState: LiveData<TopicState> get() = _hotTopicState

    private val _searchTopicState = MutableLiveData<TopicState>()
    val searchTopicState: LiveData<TopicState> get() = _searchTopicState

    private val _searchUserState = MutableLiveData<UserState>()
    val searchUserState: LiveData<UserState> get() = _searchUserState


    //获取推荐话题
    fun getHotTopicList() {
        val disposable = labelRepository.getRecommendLabelList(10)
            .subscribe({
                val list = it.list.map { TopicBean(it.id, it.name, it.viewCount.toLong()) }

                _hotTopicState.value = TopicState.Success(list)
            }, {
                _hotTopicState.value = TopicState.Fail
            })

        addDisposable(disposable)
    }


    /**
     * 搜索话题
     */
    fun searchTopic(
        keyword: String,
        page: Int = 1,
        size: Int = 10,
        lastTime: Long? = null
    ) {
        val disposable = searchRepository.searchTopic(keyword, page, size, lastTime)
            .subscribe({
                _searchTopicState.value = TopicState.Success(it.list,keyword)
            }, {
                _searchTopicState.value = TopicState.Fail
            })

        addDisposable(disposable)
    }


    /**
     * 搜索用户
     */
    fun searchUser(
        key: String,//搜索词
        page: Int = 1,//页码，第一次穿1，后面传入上一次接口返回得page
        size: Int = 10,//数量
        lastTime: Long? =null,//用于分页，传入上一次接口返回lastTime
        withoutIds: String? = null//过滤的用户的ids，例如：id1,id2,id3
    ) {
        val disposable = searchRepository.getSearchUser(key, page, size, lastTime, withoutIds)
            .subscribe({
                _searchUserState.value = UserState.Success(it.list)
            }, {
                _searchUserState.value = UserState.Fail
            })
        addDisposable(disposable)
    }


    /**
     * 获取@用户列表
     */
    fun getAtUserList(
        size: Int = 10,
        lastTime: Long? =null,
        withoutIds: String?= null
    ) {
        val disposable = attentionRepository.getAtUserList(size, lastTime, withoutIds)
            .subscribe({
                _searchUserState.value = UserState.Success(it.list)
            },{
                _searchUserState.value = UserState.Fail
            })
        addDisposable(disposable)
    }


}