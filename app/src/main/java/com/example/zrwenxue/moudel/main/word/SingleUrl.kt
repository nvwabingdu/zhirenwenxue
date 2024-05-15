package com.example.zrwenxue.moudel.main.word

import android.app.Activity
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import java.io.IOException
import java.util.Arrays
import java.util.Random


/**
 * @Author qiwangi
 * @Date 2023/8/27
 * @TIME 00:21
 */
object SingleUrl {
    /**
     * 随机图片
     */
    fun getRandomImageUrl(): String? {
        // 将多个图片链接存储在列表中
        val imageUrls: List<String> = Arrays.asList(
            "https://img2.woyaogexing.com/2023/04/09/55f771c75e6a4883c167aa6a6766cb22.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/7adb5986b2cd7ab25c9d2cba0e8af8e1.jpeg",
            "https://img2.woyaogexing.com/2023/04/08/16a5092608019b35f7172476bf48968e.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/13359d49e99a3d23d37799fff41fcd78.png",
            "https://img2.woyaogexing.com/2023/04/08/c626834b97f48a3f00bda3cd1580db44.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/c0df9e9f51bf18ac6882fb1b9dffe3e2.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/13359d49e99a3d23d37799fff41fcd78.png",
            "https://img2.woyaogexing.com/2023/04/09/2599eb37c7f6cbe0c52006270ec6cf4f.png",
            "https://img2.woyaogexing.com/2023/04/08/ab2c6520fb3d05a998ed0f2ce6d2d007.jpeg"
        )

        // 随机选择一个链接并返回
        val random = Random()
        return imageUrls[random.nextInt(imageUrls.size)]
    }

    /**
     * 打开assets下的音乐mp3文件
     */
    public fun openAssetMusics(mActivity: Activity,fileLoad:String) {
        var mediaPlayer:MediaPlayer?=null
        try {
//播放 assets/a2.mp3 音乐文件
            val fd: AssetFileDescriptor = mActivity.assets.openFd("identification_success.mp3")
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    public fun playSoundMark(mActivity: Activity,tag:String){
        when(tag){
            ""->{
                openAssetMusics(mActivity,"soundmark/.mp3")
            }
        }
    }

    var dd2=
    """
    <1>[ɪ]<2>发音类似拼音"初一"取"一"音，发音短促<3>常发此音的字母及字母组合有：i, y<4>sit [sɪt] 坐#gift [gɪft] 礼物#big [bɪg] 大的#miss [mɪs] 想念#pig [pɪg] 猪#listen [ˋlɪsən] 聆听<5>i-sound.mp3
    <1>[e]<2>发音类似拼音"诶" ，尾音拉长<3>常发此音的字母及字母组合有：a, ai, ay, ea<4>sale [sel] 拍卖#pain [pen] 痛苦#snake [snek] 蛇#fake [fek] 仿冒品#rain [ren] 下雨#great [gret] 很棒的<5>e-sound.mp3
    <1>[ʌ]<2>发音类似汉语拼音符号e与a之间，稍微偏向a音<3>常发此音的字母及字母组合有：u, o, ou, oo<4>sun [sʌn] 太阳#run [rʌn] 跑#bus [bʌs] 公车#fun [fʌn] 乐趣#cup [kʌp] 杯子#love [lʌv] 爱；喜爱<5>^-sound.mp3
    <1>[ə]<2>发音时牙床半开，双唇略扁平，舌身平放发非重读的音<3>常发此音的字母及字母组合有:a,e,o,u,or,er<4>along [ə'lɒŋ] 沿着#open ['əʊpən] 公开的#today [tə'deɪ] 今天#support [sə'pɔːt] 支持<5>e^-sound.mp3
    <1>[ɒ]<2>发音时双唇稍收圆,舌身降低后缩，发出类似“噢”的音，发/ɒ/嘴唇张得比/ʊ/大<3>常发此音的字母和字母组合有:o,a<4>hot [hɒt] 热的#dot [dɒt] 点#clock [klɒk] 时钟#want [wɒnt] 需要#wash [wɒʃ] 洗涤#what [wɒt] 什么<5>o-sound.mp3
    <1>[ʊ]<2>发音类似拼音的u，嘴巴微张，用发e音的位置,发u<3>常发此音的字母和字母组合有：u, oo<4>put [pʊt] 放置#foot [fʊt] 腳；英尺#look [lʊk] 看#hood [hʊd] 兜帽#good [gʊd] 好的#wood [wʊd] 木头<5>u-sound.mp3
    <1>[æ]<2>发音类似拼音"俺"，嘴巴张大巴下拉<3>常发此音的字母及字母组合有：a<4>fat [fæt] 胖的#sad [sæd] 伤心的#bad [bæd] 还的#snack [snæk] 点心#pan [pæn] 平底锅#dance [dæns] 跳舞<5>an-sound.mp3
    <1>[iː]<2>发音类似拼音"一" ，尾音拉长<3>常发此音的字母及字母组合有：ea, ee, i<4>seat [sit] 座位#sleep [slip] 睡觉#beat [bit] 打；敲#sweet [swit] 甜的#meet [mit] 遇见#machine [məˋʃin] 机器<5>i-sound2.mp3
    <1>[ɜː]<2>发音类似拼音"诶"，发音短促，嘴巴半开<3>常发此音的字母及字母组合有：e, ea<4>sell [sɛl] 贩卖#head [hɛd] 头#bed [bɛd] 床#bear [bɛr] 熊#pen [pɛn] 笔#sweater [ˋswɛtɚ] 毛衣<5>er-sound.mp3
    <1>[ɔː]<2>发音类似于汉语拼音的o，嘴巴张大<3>常发此音的字母和字母组合有：a, o, au, ou, aw<4>dog [dɔg] 狗#small [smɔl] 小的#soft [sɔft] 软的#fault [fɔlt] 错过#talk [tɔk] 说话#cough [kɔf] 咳嗽<5>o-sound2.mp3
    <1>[uː]<2>发音类似拼音的u，嘴巴嘟起來，尾音拉长<3>常发此音的字母和字母组合有：o, u, ou, oo<4>too [tu] 也；太#rude [rud] 无礼的#soup [sup] 汤#lose [luz] 失去#move [muv] 移动#food [fud] 食物<5>u-sound2.mp3
    <1>[ɑː]<2>发音类似于汉字的"啊"，嘴巴张大，尾音稍微拉长<3>常发此音的字母和字母组合有：a, o<4>hot [hɑt] 热的#mop [mɑp] 拖把#box [bɑks] 盒子#park [pɑrk] 公园#lock [lɑk] 锁上#smart [smɑrt] 聪明的<5>a-sound2.mp3
    <1>[eɪ]<2>嘴巴张开成半圆形，开始发/e/音<3>常发此音的字母和字母组合有:a,ai,ay,ei<4>fate [feɪt] 命运#same [seɪm] 相同的#famous [feɪməs] 著名的#laid [leɪd] 松弛的#main [meɪn] 主要的#rain [reɪn] 雨<5>ei.mp3
    <1>[aɪ]<2>发音类似汉语拼音的ai<3>常发此音的字母和字母组合有：i, y<4>bike [baɪk] 腳踏车#hire [haɪr] 雇用#kite [kaɪt] 风筝#fly [flaɪ] 飞行#light [laɪt] 电灯#type [taɪp] 打字；类型<5>ai.mp3
    <1>[ɔɪ]<2>发音类似于汉语拼音的oi<3>常发此音的字母和字母组合有：oi, oy<4>coin [kɔɪn] 钱币#boy [bɔɪ] 男孩#noise [nɔɪz] 噪音#toy [tɔɪ] 玩具#point [pɔɪnt] 尖端#joy [dʒɔɪ] 喜悦<5>oi.mp3
    <1>[aʊ]<2>发音类似于汉语拼音的ao<3>常发此音的字母和字母组合有：ou, ow<4>house [haʊs] 房子#now [naʊ] 现在#mouse [maʊs] 老鼠#tower [ˈtaʊɚ] 高塔#sound [saʊnd] 声音#flower [ˈflaʊɚ] 花<5>ao.mp3
    <1>[əʊ]<2>首先发/ʊ/的音，然后慢慢滑向/ə/的音就可以了<3>常发此音的字母和字母组合有:our,oor,ure<4>moor [mʊə] 沼泽#poor [pʊə] 贫穷的#tour [tʊə] 旅游#fury ['fjʊərɪ] 狂怒#Europe ['jʊrəp] 欧洲#cure [kjʊə] 治疗<5>eu.mp3
    <1>[ɪə]<2>舌位由元音/ɪ/向元音/ə/滑动，不到/ə/音即告发音结束<3>发/ɪə/音的字母和字母组合ear,eer<4>rear [rɪə] 向后#near [nɪə] 近的#clear [klɪə] 清楚的#here [hɪə] 在这里#sincere [sɪn'sɪə] 真诚的#atmosphere ['ætməsfɪə]气氛<5>ir.mp3
    <1>[eə]<2>发音时先发/e/然后滑向/ə/音，口型由大变小<3>发/eə/这个音的常见字母组合有are,air,ear,ere<4>share [ʃeə] 分享#dare [deə] 敢冒#care [keə] 照顾#repair [rɪ'peə] 修理#air [eə] 空气#hair[heə] 头发<5>er.mp3
    <1>[ʊə]<2>口型由收圆变化为最后半开<3>发/ʊə/这个音的常见字母组合有our,oor,ure<4>moor [mʊə] 沼泽#poor [pʊə] 贫穷的#tour [tʊə] 旅游#fury ['fjʊərɪ] 狂怒#Europe ['jʊrəp] 欧洲#cure [kjʊə] 治疗<5>uer.mp3
    <1>[p]<2>发音类似汉语拼音的p，无声<3>常发此音的字母和字母组合有：p , pp<4>pick [pɪk] 摘；选择#help [hɛlp] 帮忙#pork [pork] 猪肉#keep [kip] 维持#push [pʊʃ] 推#camp [kæmp] 露营<5>p.mp3
    <1>[t]<2>发音类似汉语拼音符号ｔ，无声<3>常发此音的字母和字母组合有：t,tt<4>tear [tɪr] 眼泪#host [host] 主持人#tool [tul] 工具#meat [mit] 肉#tennis [ˋtɛnɪs] 网球#toast [tost] 吐司<5>t.mp3
    <1>[k]<2>发音类似拼音符号k，无声<3>发音类似汉语拼音k,是无声辅音<4>kill [kɪl] 杀人#dark [dɑrk] 黑暗的#cost [kɔst] 花费#bank [bæŋk] 银行#cash [kæʃ] 现金#neck [nɛk] 脖子<5>k.mp3
    <1>[f]<2>发音类似拼音的f ，[f]是轻辅音，声带不震动<3>发音类似汉语拼音的f，轻辅音<4>fox [fɑks] 狐狸#leaf [lif] 葉子#fast [fæst] 快的#wolf [wʊlf] 野狼#fight [faɪt] 战斗#thief [θif] 小偷<5>f.mp3
    <1>[θ]<2>发音类似汉语拼音的 s，用上下齿轻碰舌头发音，声带不震动，轻辅音<3>是轻辅音，常发此音的字母及字母组合有：th<4>thin [θɪn] 瘦的#with [wɪθ] 和；跟#thank [θæŋk] 谢谢#both [boθ] 两者都#through [θru] 穿过#month [mʌnθ] 月份<5>si.mp3
    <1>[s]<2>发音类似汉语拼音的s，是轻辅音，发音时声带不震动<3>是轻辅音，发音时声带不震动，常发此音的字母有：s, c<4>cent [sɛnt] 分(货币单位)#gas [gæs] 气体#safe [sef] 安全的#kiss [kɪs] 亲吻#smoke [smok] 抽烟#desk [dɛsk] 书桌<5>s.mp3
    <1>[ts]<2>发音时上下齿自然合拢，嘴唇微张开，舌端贴住齿龈，堵住气流，然后舌尖略微下降，气流随之泄出成音。<3>第三人称单数时，我们通常在单词后加s,这时单词结尾的字母组合ts就发/ts/<4>hats [hæts] 帽子#starts [stɑːts] 开始#parts [pɑːts] 零件#sweats [swets] 水珠#pets [pets] 宠物#goats [gəʊts] 山羊<5>ts.mp3
    <1>[tr]<2>[tr]的发音与汉语拼音“chuo” 的音相近，发[tr]时声带不振动<3>常见字母组合有:tr<4>tree [tri:] 树#trip [trip] 旅行#factory [fæktri] 工厂#introduce ['intrədju:s] 介绍#true [tru:] 真的#trunk [trʌŋk] 枝干<5>tr.mp3
    <1>[ʃ]<2>和汉语中的声母sh很相近，不卷舌，是轻辅音<3>是轻辅音，常发此音的字母及字母组合有：sh, ci, si, ti<4>shoe [ʃu] 鞋子#fish [fɪʃ] 鱼；钓鱼#shirt [ʃɝt] 衬衫#wash [wɑʃ] 洗#shop [ʃɑp] 商店#brush [brʌʃ] 刷；刷子<5>ss.mp3
    <1>[tʃ]<2>在元音前，与元音拼音，发音类似qie<3>是轻辅音，常发此音的字母及字母组合有：ch, tu<4>chat [tʃæt] 聊天#watch [wɑtʃ] 看#chair [tʃɛr] 椅子#lunch [lʌntʃ] 午餐#cheap [tʃip] 便宜的#touch [tʌtʃ] 碰触<5>tss.mp3
    <1>[b]<2>发音类似汉语拼音的b，有声<3>常发此音的字母和字母组合有：b,bb<4>bug [bʌg]web [wɛb] 蜘蛛网#beef [bif] 牛#club [klʌb] 俱乐部#band [bænd] 乐队#crab [kræb] 螃蟹<5>b.mp3
    <1>[d]<2>发音类似汉语拼音符号d，有声<3>常发此音的字母和字母组合有：d,dd<4>dig [dɪg] 挖掘#cold [kold] 冷的#doll [dɑl] 洋娃娃#kind [kaɪnd] 善良的#door [dor] 门#send [sɛnd] 送；寄<5>d.mp3
    <1>[g]<2>发音类似拼音符号g，有声<3>发音类似汉语拼音g<4>guy [gaɪ] 家伙#bag [bæg] 袋子#goat [got] 山羊#flag [flæg] 旗子#guess [gɛs] 猜测#frog [frɑg] 青蛙<5>g.mp3
    <1>[v]<2>用发[f]的嘴形发[v]，声带震动<3>是唇齿摩擦辅音，是浊辅音<4>vest [vɛst] 背心#save [sev] 储存#voice [vɔɪs] 声音#solve [sɑlv] 解决#visit [ˋvɪzɪt] 拜访#glove [glʌv] 手套<5>v.mp3
    <1>[ð]<2>发音类似于汉语拼音的 r，不卷舌，声带震动，浊辅音<3>是浊辅音，常发此音的字母及字母组合有：th<4>this [ðɪs] 这#bathe [beð] 浸洗#that [ðæt] 那#clothe [kloð] 穿衣服#father [ˋfɑðɚ] 父亲#breathe [brið] 呼吸<5>qq.mp3
    <1>[z]<2>发音类似汉语拼音的z，用 [s]发音位发音，发成有声，是浊辅音<3>浊辅音，发音时声带震动,常发此音的字母有：s, z<4>zoo [zu] 动物园#size [saɪz] 尺寸#zebra [ˋzibrə] 斑马#jazz [dʒæz] 爵士#busy [ˋbɪzɪ] 忙的#choose [tʃuz] 选择<5>z.mp3
    <1>[dz]<2>舌尖先抵住上齿，堵住气流，使气流从舌尖和齿龈间送出<3>第三人称单数时在词尾加s,单词结尾的字母组合ds发/dz/<4>reads /ri:dz/ vt. 阅读(第三人称)hands [hændz] 手#heads [hedz] 头#roads [rəʊdz] 道路#cards [kɑ:dz] 卡片#pounds [paʊndz] 磅<5>dz.mp3
    <1>[dr]<2>双唇收圆向前突出，舌尖上翘抵住上齿龈，采取伐[r]的姿势，但声带振动<3>一般只有字母组合dr发/dr/音<4>rop [drɒp] 滴#drive [draɪv] 开车#drum [drʌm] 击鼓#dreg [drɛg] 渣滓#dree [driː] 凄凉的#drift [drɪft] 漂流<5>dr.mp3
    <1>[ʒ]<2>用 [ʃ] 的位置发音，发成有声即声带震动，是浊辅音[ʒ] 如果是在单词尾时，音会很轻，不要用力，轻轻发音即可<3>是浊辅音，常发此音的字母及字母组合有：g, s, su, si<4>garage [gəˋrɑʒ] 车库#usually [ˋjuʒʊəlɪ]  通常#television [ˋtɛlə͵vɪʒən] 电视#measure [ˋmɛʒɚ] 测量#treasure [ˋtrɛʒɚ] 宝藏#pleasure [ˋplɛʒɚ] 愉快；荣幸<5>n3.mp3
    <1>[dʒ]<2>不与元音拼音时，或在词尾时，发音类似汉语拼音的ji<3>是浊辅音，常发此音的字母及字母组合有：j, g<4>jam [dʒæm] 果酱#joke [dʒok] 笑话#giant [ˋdʒaɪənt] 巨人#energy [ˋɛnɚdʒɪ] 能量#large [lɑrdʒ] 大的#college [ˋkɑlɪdʒ] 学院#orange [ˋɔrɪndʒ] 柳橙#garbage [ˋgɑrbɪdʒ] 垃圾<5>d3.mp3
    <1>[m]<2>在元音前，与元音拼音，发音类似汉语拼音的m，是浊辅音。不与元音拼音时，或在词尾时，发音类似"嗯"的音，紧闭双唇<3>是浊辅音，在元音前后发不同音<4>map [mæp] 地图#slim [slɪm] 苗条的#milk [mɪlk] 牛奶#team [tim] 队伍#mind [maɪnd] 心智#swim [swɪm] 游泳#mode [mod] 方式#home [hom] 家<5>m.mp3
    <1>[n]<2>元音前，与元音拼音，发音类似拼音的n，是浊辅音。不与元音拼音时，或在词尾时，发音类似"嗯"的音，舌尖轻抵上齿龈<3>是浊辅音，在元音前后发不同音<4>nine [naɪn] 九#corn [kɔrn] 玉米#nest [nɛst] 鸟巢#green [grin] 绿色#name [nem] 名字#noon [nun] 正午#north [nɔrθ] 北方#moon [mun] 月亮<5>n.mp3
    <1>[ŋ]<2>发音类似拼音的eng，在鼻腔的共鸣位置比 [n] 高，是浊辅音。<3>浊辅音，常发此音的字母和字母组合有：ng, nk<4>song [sɔŋ] 歌曲#link [lɪŋk] 连接#hang [hæŋ] 悬挂#tank [tæŋk] 坦克#thing [θɪŋ] 事情；东西#pink [pɪŋk] 粉红色#among [əˋmʌŋ] 在~之中#meaning [ˋminɪŋ] 意思#monkey [ˋmʌŋkɪ] 猴子<5>ng.mp3
    <1>[h]<2>发音类似汉语拼音的h，是轻辅音<3>发音类似于汉语拼音的h,是轻辅音<4>hat [hæt] 帽子#hear [hɪr] 听见#hide [haɪd] 躲藏#hope [hop] 希望#hurt [hɝt] 伤害#heart [hɑrt] 心脏<5>h.mp3
    <1>[l]<2>在元音前，与元音拼音，发音类似汉语拼音的le，是浊辅音。不与元音拼音或在单词尾时，发音类似拼音的ou韵母<3>是浊辅音，在元音前后发不同音<4>lie [laɪ] 说谎#tall [tɔl] 高的#late [let] 迟的；晚的#pale [pel] 脸色苍白的#land [lænd] 降落#well [wɛl] 好地#learn [lɝn] 学习#meal [mil] 一餐<5>l.mp3
    <1>[r]<2>在元音前，与元音拼音，发音类似汉语拼音的rue，是浊辅音。不与元音拼音或在词尾时，发音类似汉语拼音的er<3>是浊辅音，在元音前后发不同音<4>rat [ræt] 大老鼠#beer [bɪr] 啤酒#read [rid] 阅读#hire [haɪr] 雇用#road [rod] 道路#fork [fɔrk] 叉子#right [raɪt] 对的#bark [bɑrk] 吠叫<5>r.mp3
    <1>[j]<2>发音类似拼音的ye，是浊辅音。<3>是浊辅音，常发此音的字母及字母组合有：y, i<4>yet [jɛt] 尚未#yummy [ˋjʌmɪ] 好吃的#year [jɪr]  年，岁#senior [ˋsinjɚ] 年长的#yard [jɑrd] 院子#genius [ˋdʒinjəs] 天才#young [jʌŋ] 年轻的<5>j.mp3
    <1>[w]<2>发音类似汉语拼音的uo，是浊辅音<3>发音类似汉语拼音的uo，是浊辅音<4>wet [wɛt] 湿的#warm [wɔrm] 暖和的#wise [waɪz] 有智慧的#water [ˋwɑtɚ] 水#week [wik] 星期#woman [ˋwʊmən] 女人<5>w.mp3
    """
}