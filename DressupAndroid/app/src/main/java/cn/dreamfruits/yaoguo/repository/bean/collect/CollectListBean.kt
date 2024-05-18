package cn.dreamfruits.yaoguo.repository.bean.collect

data class CollectListBean(
    val hasNext: Int, // 1
    val lastTime: Long, // 1675132459894
    val list: List<Item>,
    val totalCount: Int // 3
) {
    data class Item(
        val coverUrl: String, // https://testfile.dreamfruits.cn/app/323250141807248/dfcda237af0c5424497cc19e1290b8f4
        val createTime: Long, // 1675132568922
        val id: Long, // 431356059010192
        val isCollected: Int, // 0
        val outfitBaseInfo: OutfitBaseInfo,
        val poseInfo: Any, // null
        val sceneInfo: SceneInfo,
        val state: Int, // 1
        val updateTime: Long, // 1675132568922
        val userInfo: UserInfo
    ) {
        data class OutfitBaseInfo(
            val bagInfo: Any, // null
            val braceletInfo: Any, // null
            val createTime: Long, // 1675132568921
            val earringInfo: Any, // null
            val glassesInfo: Any, // null
            val hairInfo: HairInfo,
            val hatInfo: Any, // null
            val id: Long, // 431356059002000
            val insideInfo: Any, // null
            val insidePantsInfo: Any, // null
            val middleInfo: MiddleInfo,
            val necklaceInfo: Any, // null
            val outerInfo: Any, // null
            val outerPantsInfo: OuterPantsInfo,
            val ringInfo: Any, // null
            val scarfInfo: Any, // null
            val shoeInfo: ShoeInfo,
            val sockInfo: Any, // null
            val sort: String, // 123
            val state: Int, // 1
            val updateTime: Long // 1675132568921
        ) {
            data class HairInfo(
                val coverUrl: String, // https://testfile.dreamfruits.cn/admin/298308396081344/b980b2365a63a5f0609076f23acc74e1.png
                val createTime: Long, // 1669204954000
                val creatorId: Long, // 298308396081344
                val hairId: Int, // 36
                val id: Int, // 6
                val name: String, // 高马尾
                val state: Int, // 1
                val type: Int, // 0
                val updateTime: Long, // 1669372902000
                val url: String // https://testfile.dreamfruits.cn/admin/298308396081344/g_w_tf_221017021_bla_89df6262d9dd8b9cf05b809ec2b619e5.ab
            )

            data class MiddleInfo(
                val cLength: Any, // null
                val coverUrl: String,
                val createTime: Long, // 1672223768702
                val id: Long, // 407527044221744
                val isCollect: Int, // 0
                val labelsList: Any, // null
                val modelList: List<Model>,
                val name: String, // 短款上衣
                val picUrls: List<String>,
                val position: Int, // 1
                val state: Int, // 1
                val videoUrls: String
            ) {
                data class Model(
                    val floor: Int, // 2
                    val id: Long, // 407527167617840
                    val resourceUrl: String, // https://testfile.dreamfruits.cn/admin/298308396081344/g_w_22110103_khi_sh2_c_k_a_a_94d472441904a7fda7e0117f9d91f70e.ab
                    val singleProductId: Long, // 407527044221744
                    val status: Int // 1
                )
            }

            data class OuterPantsInfo(
                val cLength: Any, // null
                val coverUrl: String,
                val createTime: Long, // 1673086430443
                val id: Long, // 414594067915024
                val isCollect: Int, // 0
                val labelsList: Any, // null
                val modelList: List<Model>,
                val name: String, // 工装短裤
                val picUrls: List<String>,
                val position: Int, // 2
                val state: Int, // 1
                val videoUrls: String
            ) {
                data class Model(
                    val floor: Int, // 2
                    val id: Long, // 414594092597520
                    val resourceUrl: String, // https://testfile.dreamfruits.cn/admin/298308396081344/g_w_22110104_Khi_xh2_k_a_281B92F048267172326E997416DA3696.ab
                    val singleProductId: Long, // 414594067915024
                    val status: Int // 21
                )
            }

            data class ShoeInfo(
                val cLength: Any, // null
                val coverUrl: String,
                val createTime: Long, // 1672371936603
                val id: Long, // 408740932422624
                val isCollect: Int, // 0
                val labelsList: Any, // null
                val modelList: List<Model>,
                val name: String, // 西部牛仔靴
                val picUrls: List<String>,
                val position: Int, // 3
                val state: Int, // 1
                val videoUrls: String
            ) {
                data class Model(
                    val floor: Int, // 1
                    val id: Long, // 408740959063008
                    val resourceUrl: String, // https://testfile.dreamfruits.cn/admin/298308396081344/g_w_22110106_bro_xz_a_9dc5b3359ac64c731f92512833489614.ab
                    val singleProductId: Long, // 408740932422624
                    val status: Int // 0
                )
            }
        }

        data class SceneInfo(
            val coverUrl: String,
            val createTime: Long, // 1669639586000
            val id: Int, // 7
            val name: String, // 默认场景
            val state: Int, // 1
            val updateTime: Long, // 1673233853000
            val url: String // https://testfile.dreamfruits.cn/admin/10086/nut_g_w_cj_2022083002_4_B9D74F44B09AC908203A09E6D64ACB38.ab
        )

        data class UserInfo(
            val avatarUrl: String, // https://testfile.dreamfruits.cn/admin/10086/936cda026ef96e6225648cf5a3571b67.png
            val id: Long, // 323250141807248
            val nickName: String // 鱼鱼鱼鱼鱼鱼鱼鱼
        )
    }
}