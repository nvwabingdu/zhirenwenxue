package cn.dreamfruits.yaoguo.repository

import com.tencent.mmkv.MMKV


object MMKVRepository{

    /**
     * 获取公共的MMKV
     */
    fun getCommonMMKV(): MMKV {
        return MMKV.defaultMMKV()
    }

    /**
     * 获取用户的MMKV
     */
    fun getUserMMKV(): MMKV {
        return MMKV.mmkvWithID(OauthRepository.getUserId().toString())
    }

}