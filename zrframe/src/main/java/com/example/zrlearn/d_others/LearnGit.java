package com.example.zrlearn.d_others;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-06-16
 * Time: 15:35
 */
class LearnGit {
    /**
     * 打印ssh
     */
    void git_1(){
        /**
         * 生成
         * 1.配置
         *
         * git config --global user.name "nvwabingdu"
         * git config --global user.email "1661646196@qq.com"
         *
         * 注：查下是否配置成功，用命令
         * git config --global --list
         *
         * 2.生成秘钥
         * ssh-keygen -t rsa -C  "1661646196@qq.com"
         * 注：执行上面命令后，连续回车3次
         *
         * 打印
         * 1、进入ssh
         * cd ~/.ssh
         * 2、打印ssh
         * cat id_rsa.pub
         */
    }

    /**
     * Git规范待补充
     */
    void git_2(){
        /**
         * Git规范
         * 1.对于大功能，必须拉分支，分支请按自己的名字拼音命名，建议命名如下
         * wangqi/feature/功能1
         * xiaoming/optimize/优化1
         * xiaohua/bugfix/修复bug1
         *
         * 2.提交日志用适当的tag打头，描述清晰，示例如下
         * feature: 新加单词点击功能
         * bugfix: 修复网络断链
         * optimize：优化小红点
         */
    }

    /**
     * smartGit操作流程
     */
    void git_3(){
        /**
         * 比较好的博客：
         * https://www.cnblogs.com/upstudy/p/15870787.html
         * https://www.cnblogs.com/sebastian-tyd/p/7967583.html
         * https://www.w3cschool.cn/isrekq/
         *
         * git学习：（.git文件夹：git的版本控制相关内容）
         * 前置：（仓库—编辑git配置文件—用户）配置
         *
         * [user] name = nvwabingdu email = 1661646196@qq.com
         * [gc] autoDatach=false
         * [core] quotepath=false ignorecase=false
         * [pull] rebase=true #本地分支将会变基到远端
         *
         * 同样的—————————————仓库 需要配置
         * [pull] rebase=true #本地分支将会变基到远端
         * [core] ignorecase=false
         *
         * 1.fork分支
         *
         * 2.仓库—clone— 复制链接—goon
         *
         * 3.检出origin分支 命名方式：wanqi/branch001 check out （定位到当前分支的意思）
         *
         * 4.git标准流程：
         * —-工作doing
         * —暂存add
         * —提交commit
         * —-（ pull时要确定clean）拉取pull—-合并分支（解决冲突）——测试test （默认使用rebase(冲突不好解决) merge（会导致不必要的merge提交））
         * —-推送push
         *
         * 5.commit high push lower
         *
         * 6.冲突解决方案：
         *
         * 1.找到最新分支—-重置提交（高级）——重置索引树（hard）——-
         *
         * 2.单击冲突文件—-解决——goon
         *
         * 7.建议创建分支操作，双击mater（或者需要创建的分支），或者右键—-创建分支（wangqi/分支名）
         */
    }

    /**
     * 报错：有些项目clone时需要ssl
     */
    void git_4(){
        /**
         直接解除
         git config --global http.sslVerify "false"
         */
    }

    /**
     * 一款好用的mvvm框架
     *
     */
    void git_5(){
        /**
         在主项目app的build.gradle中依赖
         ```gradle
         dependencies {
         ...
         implementation 'com.github.goldze:MVVMHabit:4.0.0'
         }
         ```
         或

         下载例子程序，在主项目app的build.gradle中依赖例子程序中的**mvvmhabit**：
         ```gradle
         dependencies {
         ...
         implementation project(':mvvmhabit')
         }
         ```
         */
    }

    /**
     * git流程
     */
    void git_6(){
        /**
         git init   // 初始化版本库

         git add .   // 添加文件到版本库（只是添加到缓存区），.代表添加文件夹下所有文件

         git commit -m "first commit" // 把添加的文件提交到版本库，并填写提交备注

         git remote add origin 你的远程库地址  // 把本地库与远程库关联

         git push -u origin master    // 第一次推送时

         git push origin master  // 第一次推送后，直接使用该命令即可推送修改
         */
    }

    /**
     *
     */
    void git_7(){
        /**

         */
    }

    /**
     *
     */
    void git_8(){
        /**

         */
    }

    /**
     *
     */
    void git_9(){
        /**

         */
    }

    /**
     *
     */
    void git_10(){
        /**

         */
    }

    /**
     *
     */
    void git_11(){
        /**

         */
    }

    /**
     *
     */
    void git_12(){
        /**

         */
    }

    /**
     *
     */
    void git_13(){
        /**

         */
    }

    /**
     *
     */
    void git_14(){
        /**

         */
    }

    /**
     *
     */
    void git_15(){
        /**

         */
    }

    /**
     *
     */
    void git_16(){
        /**

         */
    }

    /**
     *
     */
    void git_17(){
        /**

         */
    }

    /**
     *
     */
    void git_18(){
        /**

         */
    }

    /**
     *
     */
    void git_19(){
        /**

         */
    }

}
