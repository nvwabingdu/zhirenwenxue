package com.example.zrwenxue.moudel.main.memory

/**
 * @Author qiwangi
 * @Date 2023/8/28
 * @TIME 09:51
 */
data class SoundMarkBean(
    var mark:String,//音标
    val markPronunciationSkill:String,//发音技巧先发"哦"
    var markExample:String,//发此音的e，ee
    var markWorkList:ArrayList<String>,//示例单词
    var markSoundPath:String,//音频位置

)
