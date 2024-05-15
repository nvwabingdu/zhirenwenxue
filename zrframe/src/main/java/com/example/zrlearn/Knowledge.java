package com.example.zrlearn;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-06-16
 * Time: 15:34
 */
class Knowledge {
    /**
     * java环境变量配置
     */
    void knowledge_1(){
        /**
         （使用Windows图标+R，快速打开“运行”操作界面，并输入cmd，回车确认。
         在命令行输入java –version；如果能显示java的版本信息，则表示不需要配置）

         第一步：系统变量  下找到变量名为path的变量  双击进入   新建两次，每次输入一行
         %JAVA_HOME%\bin
         %JAVA_HOME%\jre\bin

         第二步：新建系统变量
         变量名：             CLASSPATH
         变量值：             .;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;

         变量名：             JAVA_HOME
         变量值：             C:\64位java\Java\jdk1.8.0_45                      （实际的jdk安装路径）
         */
        //新版的jdk安装后会自动配置环境变量 as也不用配置
    }

    /**
     * 导入module
     */
    void knowledge_2(){
        /**
         1.File–>New–>Import Module
         选择Import Module

         2.然后把build.gradle(注意是Module的这个build.gradle)这个文件的第一行:
         apply plugin: ‘com.android.application’
         修改为
         apply plugin: ‘com.android.library’


         3.然后去掉defaultConfig中的
         applicationId “com.dzzchao.demo”
         4.然后Sync Project

         5.右键当前工程（比如APP）
         然后open module setting
         Dependencies   + 添加 需要的module
         */
        //目前使用的方法是先创建一个library 然后把工程复制进去
    }

    /**
     * 相对路径的写法
     */
    void knowledge_3(){
        /**
         ◆以“./”开头，代表当前目录和文件目录在同一个目录里，“./”也可以省略不写！
         ◆以"../"开头：向上走一级，代表目标文件在当前文件所在的上一级目录；
         ◆以"../../"开头：向上走两级，代表父级的父级目录，也就是上上级目录，再说明白点，就是上一级目录的上一级目录
         ◆以"/”开头，代表根目录
         */
    }

    /**
     * 排列组合计算公式
     */
    void knowledge_4(){
        /**
         m                                 公式                       2
         A n   排列  从n个数中按顺序取m个数   (n!)/(n-m)!              A 5          5! / 3！  =20

                                           公式
         m                                 m                         2
         C n   组合  从8个数中选出4个（无序）  A n  /  m!              C 5         20/2!      =10
         */
    }

    /**
     *关于ASCLL GB2312 UTF-8 UTF-16等基本认知
     *https://blog.csdn.net/m0_46202073/article/details/107353201
     *
     * 转码：
     * String str=“中文中文”；
     * byte b[]=str.getbytes(“UTF-8”);
     */
    void knowledge_5(){
        /**
         ASCII码随着第一台意义上的计算机一同诞生。
         起初，它被编到了0~127，一共128个。包括33个控制字符和95个可打印字符。
         后来计算机得到初步推广， 一些国家的字符也请求加入ASCII。
         ASCII最终被扩展到了256个——0~127的"先辈"，与128~255的扩展字符集，共同组成了它
         */

        /**
         GB2312
         当计算机走向世界时，什么"美帝信息互换标准代码"，肯定满足不了世界人民的要求了。
         于是，中国人毫不留情的将ASCII的127之后的扩展字符集砍掉。并且规定：
         当两个大于127的字符连在一起时，就表示一个汉字（▲）。前面的字节称为高字节，后面的字节称为低字节。
         不行，美帝的标点符号我们也不用！英文字母和阿拉伯数字都要本土化！>_<
         于是，将0~127中的英文字母、数字、标点符号全部重新编码，看作一种特殊的汉字———改为双字节
         因此，这种编码方式中，字母、数字、标点符号是两种形态共存的———这就是半角与全角
         理解： GB2312是ASCII最早的中文扩展，但是只包含了6000多个常用字，不包括繁体字。
         */

        /**
         GBK
         汉字文化博大精深，不像256的ASCII就能承载英文。GB2312标准还不够。
         于是，又规定，只要第一个字节(高字节)是大于127的，不管第二位(低字节)，它们都会共同表示一个汉字（▲）。
         理解： GBK包含了GB2312的所有字符，并新加入了20000个汉字及汉字符号。(☑ 完全兼容GB2312)
         */

        /**
         GB18030
         中国是一个统一的多民族的国家。字符编码自然要带上少数民族的文字。
         理解： GB18030又基本包含了GBK的所有字符。(☑ 基本兼容GBK)

         GB2312→GBK→GB18030，是后者(兼容)包含前者的关系。变化当然不止这些。
         GB2312和GBK都是双字节编码（其实是单双并存，毕竟0~127的ASCII部分天生单字节）
         GB18030则是一二四字节变长编码（每种长度分别对应ASCII部分、GBK部分、新增部分）
         */

        /**  unicode
         unicode是一个字符集(Character set)。也仅仅是一个字符集。它实现了二进制代码(通常用16进制表示)与字符的一一对应。
         即数字与字符双向唯一的映射。（★）
         下面给出一句我们获取曾经奉为真理的话，现在我们要彻彻底底的推翻它：
         对于unicode码，每个字母、数字、符号，甚至中文，全部都占两个字节。
         unicode仅仅是一个字符集，仅仅提供映射关系。至于怎样编码，如何占用字节…这与unicode毫无关系。 记住，毫无关系。
         因此，这句话的语境从根本上就是一个极其错误的认知。
         事实上，在unicode字符集出现后的很长一段时间内，unicode的实现方式是极其混乱的——数字与字符的映射虽然已经统一，
         但这个数字如何分配字节去存储？读取的时候又如何知道几个字节对应一个字符呢？
         这是与unicode字符集完全不同层次的概念：unicode的实现方式，也称为编码方案
         */

        /**
         unicode的实现方式

         UTF-8

         Unicode编码(十六进制)	 UTF-8 字节流(二进制)
         000000-00007F	          0xxxxxxx
         000080-0007FF	          110xxxxx 10xxxxxx
         000800-00FFFF	          1110xxxx 10xxxxxx 10xxxxxx
         010000-10FFFF	          11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
         不难看出UTF-8编码的特点：对不同范围的字符使用不同长度的编码。这也被称之为字节变长编码。
         UTF-8存储字符，也就是存储字符映射的数字时，可以是1， 2， 3， 4字节长度
         一般来说，欧洲字符是1~2个字节，亚洲字符是3个字节，附加字符是4个字节

         UCS-2

         UCS-2中，所有字符都是严格的16位长度，也就是所有字符长度都为2字节
         极具误导性的狭义unicode，就是指的unicode的UCS-2实现方式（▲注意）

         UTF-16
         UTF-16是UCS-2中缺少的附加字符的补充。也就是说，是UCS-2的严格父集。
         UTF-16中的字符是严格的16位或32位，也就是说，UTF-16的字符长度为严格的2字节或4字节
         */
    }

    /**
     *网络请求的8种方式，常见的返回码 1xx 2xx 3xx 4xx 5xx  404 200等
     */
    void knowledge_6(){
        /**
         GET 请求获取Request-URI 所标识的资源；
         POST 在Request-URI 所标识的资源后附加新的数据；
         HEAD 请求获取由Request-URI 所标识的资源的响应消息报头；
         PUT 请求服务器存储一个资源，并用Request-URI作为其标识；
         DELETE 请求服务器删除Request-URI 所标识的资源；
         TRACE 请求服务器回送收到的请求信息，主要用于测试或诊断；
         CONNECT 保留将来使用；
         OPTIONS 请求查询服务器的性能，或者查询不资源相关的选项和需求。

        1xx：指示信息--表示请求已接收，继续处理
        2xx：成功--表示请求已被成功接收、理解、接受
        3xx：重定向--要完成请求必须迚行更迚一步的操作
        4xx：客户端错误--请求有语法错误戒请求无法实现
        5xx：服务器端错误--服务器未能实现合法的请求

        200 OK 客户端请求成功
        400 Bad Request 客户端请求有语法错误，丌能被服务器所理解
        401 Unauthorized 请求未经授权，这个状态代码必须和WWW-Authenticate 报//头域一起使用
        403 Forbidden 服务器收到请求，但是拒绝提供服务
        404 Not Found 请求资源丌存在，eg：输入了错误的URL
        500 Internal Server Error 服务器发生丌可预期的错误
        503 Server Unavailable 服务器当前丌能处理客户端的请求，一段时间后， //可能恢复正常
         */
    }

    /**
     *  代码块的执行顺序
     */
    void knowledge_7(){
        /**
         * 父类静态属性    static
         * 父类静态代码块  static method()
         *
         * 子类静态属性   son static
         * 子类静态代码块 son static  method()
         *
         * 父类代码块    {}
         * 父类构造函数  public Father{}
         * 父类普通方法  void method()
         *
         * 子类代码块   son {}
         * 子类构造函数 son public Son{}
         * 子类普通方法 son void method()
         */
    }

    /**
     *自动装箱，拆箱问题
     */
    void knowledge_8(){
        /**
         基本数据类型   和对应的   基本数据类
         自动装箱：
         Float a=1.2545f；
         自动拆箱：
         float b=a;
         */
    }

    /**
     * 格林威治标准时间 1970 年 1 月 1 日的 00:00:00.000，格里高利历
     */
    void knowledge_9(){
        /**
         即格林威治标准时间 1970 年 1 月 1 日的 00:00:00.000，格里高利历
         在 GregorianCalendar 中，字段的默认值与历元起始部分的字段值相同：即 YEAR = 1970、MONTH = JANUARY、DAY_OF_MONTH = 1
         一月：January
         二月：February
         三月：March
         四月：April
         五月：May
         六月：June
         七月：July
         八月：August
         九月：September
         十月：October
         十一月：November
         十二月：December

         一 monday
         二 tuesday
         三 wednesday
         四 thursday
         五 friday
         六 saturday
         日 sunday

         2、时区认识

         GMT时间：即格林威治平时(Greenwich Mean Time)。
         平太阳时是与视太阳时对应的，由于地球轨道非圆形，
         运行速度随地球与太阳距离改变而出现变化，因此视太阳时欠缺均匀性。
         为了纠正这种不均匀 性，天文学家就计算地球非圆形轨迹与极轴倾斜对视太阳时的效应，
         而平太阳时就是指经修订之后的视太阳时。在格林威治子午线上的平太阳时称为世界时(UTC)，
         又叫格林威治平时(GMT)。

         原文链接：https://blog.csdn.net/weixin_28862501/article/details/114150827
         */
    }

    /**
     * 各种类 比如java.awt.* 做了解
     */
    void knowledge_10(){
        /**
         * java.awt.* 一系列:
         * Button,Frame,Graphics,Menu,Panel,TextField,List,Event,Color,ActionEvent,KeyAdpter,KeyEvent,MouseAdpter,MouseEvent,TextLayout
         *
         * java.io 一系列:
         * File,BufferReader,BufferWriter,FileInputStream,FileOutputStream,FileReader,FileWriter,Reader,Writer
         *
         * java.lang.* 一系列:
         * System,String,Integer,Float,Double,Thread,Long,Math,Object,Class,Boolean
         *
         * java.net 一系列:
         * Socket,ServerSocket,SocketAddress,URL,URLConnection
         *
         * java.sql 一系列:
         * Date,Time,DriverManager
         *
         * java.util.* 一系列:
         * ArrayList,Arrays,LinkedList,HashSet,Date,HashMap,Matcher,Pattern
         *
         * javax.swing.* 一系列:
         * JFrame,JButton,Jpanel,JList,JMenu ,JCheckerBox,JDialog
         */
    }

    /**
     * java内存分析
     */
    void knowledge_11(){
        /**
          寄存器
          栈stack
          堆heap
          静态域
          常量池
          String

         寄存器：我们在程序中无法控制
          栈stack：存放基本类型的数据和对象的引用，但对象本身不存放在栈中，而是存放在堆中   引用
          堆heap：存放用new产生的对象或数组  实例？                                  实体
          静态域：存放在对象中用static定义的静态成员
          常量池：存放常量
          非RAM存储：硬盘等永久存储空间

         栈stack： 洗碗机
         在函数中定义的一些基本类型的变量数据（8种基本类型）和对象的引用变量都在函数的栈内存中分配。
          该区域具有先进后出的特性.
          当在一段代码块定义一个变量时，Java就在栈中为这个变量分
         配内存空间，当该变量退出该作用域后，Java会自动释放掉为
         该变量所分配的内存空间，该内存空间可以立即被另作他用。

         堆heap：
         堆内存用来存放由new创建的对象和数组。在堆中分配的内存，由Java虚拟机的自动垃圾回收器来管理。
          在堆中产生了一个数组或对象后，还可以 在栈中定义一个特殊的变量，让栈中这个变量的取值等于数组或对象在堆内存
         中的首地址，栈中的这个变量就成了数组或对象的引用变量。

         引用变量就相当于是为数组或对象起的一个名称，以后就可以在程序中使用栈中的引用变量来访问堆中的数组或对
         象。引用变量就相当于是为数组或者对象起的一个名称。引用变量是普通的变量，定义时在栈中分配，引用变量在
         程序运行到其作用域乊外后被释放。而数组和对象本身在堆中分配，即使程序运行到使用 new 产生数组或者对象
         的语句所在的代码块乊外，数组和对象本身占据的内存不会被释放，数组和对象在没有引用变量指向它的时候，
         才变为垃圾，不能在被使用，但仍然占据内存空间不放，在随后的一个不确定的时间被垃圾回收器收走（释放掉）。
         这也是 Java 比较占内存的原因。

          实际上，栈中的变量指向堆内存中的变量，这就是Java中的指针！

         堆和栈的比较：
         Java的堆是一个运行时数据区,类的对象从中分配空间，它们不需要程序代码来显式的释放，堆是由垃圾回收来负责的。
          堆的优势是可以动态地分配内存 大小，生存期也不必事先告诉编译器，因为它是在运行时动态分配内存的，
         Java的垃圾收集器会自动收走这些不再使用的数据。
          但缺点是，由亍要在运行时动态 分配内存，存取速度较慢。

         栈中主要存放一些基本类型的变量数据（int, short, long, byte,float, double, boolean, char）和对象句柄(引用)。
          栈的优势是，存取速度比堆要快，仅次亍寄存器，栈数据可以共享。
          但缺点是，存在栈中的数据大小与生存期必须是 确定的，缺乏灵活性。

         栈有一个很重要的特殊性，就是存在栈中的数据可以共享。假设我们同时
         定义：
          int a = 3；
          int b = 3；
          编译器先处理int a = 3；首先它会在栈中创建一个变量为a的引用，然
         后查找栈中是否有3这个值，如果没找到，就将3存放迚来，然后将a指
         向3。接着处理int b = 3；在创建完b的引用变量后，因为在栈中已经有
         3这个值，便将b直接指向3。这样，就出现了a与b同时均指向3的情况。
          这时，如果再令 a=4；那么编译器会重新搜索栈中是否有4值，如果没
         有，则将4存放迚来，并令a指向4；如果已经有了，则直接将a指向这个
         地址。因此a值的改变不会影响 到b的值。

         注意：这种数据的共享与两个对象的引用同时指向一个对象的这种共享是不同的，
         因为这种情况a的修改并不会影响到b, 它是由编译器完成的，它有利于节省空间。
         而一个对象引用变量修改了这个对象的内部状态，会影响到另一个对象引用变量。

         ===》常量池：
         指的是在编译期被确定，并被保存在已编译的.class文件中的一些数据。除了包含代码中所定义的各种
         基本类型（如int、long等等）和对象型（如String及数组）的常量值(final)还包含一些以文本形式出现的符号引用，比如：
          类和接口的全限定名；
          字段的名称和描述符；
          方法和名称和描述符。

         ====》String
         String常量，它的值是在常量池中的。
          String是一个特殊的包装类数据。可以用：

         String str=new String("abc");
         String str="abc";

          第一种是用new()来新建对象的，它会存放于堆中。每调用一次就会创建一个新的对象。

          而第二种是先在栈中创建一个对String类的对象引用变量str，然后通过符号引用去字符串常量池 里找有没有"abc",
         如果没有，则将"abc"存放进字符串常量池 ，并令str指向”abc”，如果已经有”abc” 则直接令str指向“abc”。


         String s0 = "kvill";
         String s1 = "kvill";
         String s2 = "kv" + "ill";
         System.out.println(s0 == s1);
         System.out.println(s0 == s2);

         String s00 = "kvill";
         String s10 = new String("kvill");
         String s20 = "kv" + new String("ill");
         System.out.println(s00 == s10); // false
         System.out.println(s00 == s20); // false
         System.out.println(s10 == s20); // false

         //结果
         true
         true
         false
         false
         false

         */
    }

    /**
     *Android中的进程间通信方式
     */
    void knowledge_12(){
        /**
         1.2 Android中的IPC机制
         Android系统是基于Linux内核的，在Linux内核基础上，又拓展出了一些IPC机制。Android系统除了支
         持套接字，还支持序列化、Messenger、AIDL、Bundle、文件共享、ContentProvider、Binder等。
         Binder会在后面介绍，先来了解前面的IPC机制。
         序列化
         序列化指的是Serializable/Parcelable，Serializable是Java提供的一个序列化接口，是一个空接口，为
         对象提供标准的序列化和反序列化操作。Parcelable接口是Android中的序列化方式，更适合在Android
         平台上使用，用起来比较麻烦，效率很高。
         Messenger
         Messenger在Android应用开发中的使用频率不高，可以在不同进程中传递Message对象，在Message
         中加入我们想要传的数据就可以在进程间的进行数据传递了。Messenger是一种轻量级的IPC方案并对
         AIDL进行了封装。
         AIDL
         AIDL全名为Android interface definition Language，即Android接口定义语言。Messenger是以串行的
         方式来处理客户端发来的信息，如果有大量的消息发到服务端，服务端仍然一个一个的处理再响应客户
         端显然是不合适的。另外还有一点，Messenger用来进程间进行数据传递但是却不能满足跨进程的方法
         调用，这个时候就需要使用AIDL了。
         Bundle
         Bundle实现了Parcelable接口，所以它可以方便的在不同的进程间传输。Acitivity、Service、Receiver
         都是在Intent中通过Bundle来进行数据传递。
         文件共享
         两个进程通过读写同一个文件来进行数据共享，共享的文件可以是文本、XML、JOSN。文件共享适用于
         对数据同步要求不高的进程间通信。
         ContentProvider
         ContentProvider为存储和获取数据了提供统一的接口，它可以在不同的应用程序之间共享数据，本身
         就是适合进程间通信的。ContentProvider底层实现也是Binder，但是使用起来比AIDL要容易许多。系
         统中很多操作都采用了ContentProvider，例如通讯录，音视频等，这些操作本身就是跨进程进行通
         信。
         */
    }

    /**
     *
     */
    void knowledge_13(){
        /**

         */
    }

    /**
     *
     */
    void knowledge_14(){
        /**

         */
    }
}
