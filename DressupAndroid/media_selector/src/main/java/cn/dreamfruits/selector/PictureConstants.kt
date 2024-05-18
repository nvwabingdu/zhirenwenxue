package cn.dreamfruits.selector

interface PictureConstants {

    companion object{
        /**
         * 所有的照片或视频
         */
       const val ALL_LOCAL_MEDIA = "all_local_media"

        /**
         * 需要进入预览的 视频或图片
         */
        const val CURRENT_MEDIA_PATH = "current_media_path"

        /**
         * 选择的结果
         */
        const val MEDIA_RESULT_LIST = "media_result_list"

        /**
         * 预览结果 code
         */
        const val MEDIA_RESULT_CODE = 101

        /**
         * 发布页面已选数量
         */
        const val SELECTED_COUNT = "selected_count"


        /**
         * 选择图片result code
         */
        const val IMAGE_RESULT_CODE = 222

        /**
         * 选择视频result code
         */
        const val VIDEO_RESULT_CODE = 333
    }
}