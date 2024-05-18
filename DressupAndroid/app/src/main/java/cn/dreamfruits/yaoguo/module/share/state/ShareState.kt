package cn.dreamfruits.yaoguo.module.share.state

import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.module.selectclothes.state.ClothesCategoryState
import cn.dreamfruits.yaoguo.module.share.bean.ShareUserListBean
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesCategoryBean

sealed class ShareUserListState {

    data class Success(val shareUserListBean: ShareUserListBean) : ShareUserListState()

    class Fail(val msg: String?) : ShareUserListState()

}

sealed class ShareUseRecomendListState {

    data class Success(val recommendUserListBean: List<UserInfo>?) : ShareUseRecomendListState()

    class Fail(val msg: String?) : ShareUseRecomendListState()

}

sealed class ShareShortUrlState {

    data class Success(val shortUrl: String, val shareType: Int) : ShareShortUrlState()

    class Fail(val msg: String?) : ShareShortUrlState()

}

