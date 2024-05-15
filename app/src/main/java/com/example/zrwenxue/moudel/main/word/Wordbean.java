package com.example.zrwenxue.moudel.main.word;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-16
 * Time: 12:30
 */
//Entity注解将Wordbean类和数据库中的表Word_table对应起来，tableName可以指定别名，
//亦可不指定，若不指定就默认使用类名作为表名
//每个实体必须定义至少1个字段作为主键。即使只有1个字段，仍然需要用@PrimaryKey注解字段。
//此外，如果您想Room自动分配IDs给实体，则可以设置@ PrimaryKey的autoGenerate属性。
//如果实体具有复合主键，则可以使用@Entity注解的primaryKeys属性，如下面的代码片段所示：
//@Entity(primaryKeys = {"firstName", "lastName"})

//根据访问数据的方式，您可能需要索引数据库中的某些字段以加快查询速度。若要向实体添加索引，请在@Entity注释中包含索引属性，列出要包含在索引或复合索引中的列的名称。下面的代码片段演示了这个注解过程：
//@Entity(indices = {@Index("name"),
//@Index(value = {"last_name", "address"})})


//有时，数据库中的某些字段或字段组必须是唯一的。可以通过将@Index注解的唯一属性设置为true来强制执行此唯一性属性。下面的代码示例防止表中包含两个行，它们包含firstName和lastName列的相同值集：
//@Entity(indices = {@Index(value = {"first_name", "last_name"},unique = true)})

//例如，如果有另一个实体称为Book，则可以使用@ForeignKey 注解定义其与用户实体的关系，如下面的代码片段所示：
//@Entity(foreignKeys = @ForeignKey(entity = User.class,
//parentColumns = "id",
//childColumns = "user_id"))

@Entity(tableName = "word_table")
public class Wordbean {

    //PrimaryKey主键，autoGenerate自增长
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    public int id;//单词
    //ColumnInfo用于指定该字段存储在表中的名字，并指定类型
    @ColumnInfo(name = "soundmark", typeAffinity = ColumnInfo.TEXT)
    public String word;//单词

    @ColumnInfo(name = "word_sound_mark", typeAffinity = ColumnInfo.TEXT)
    public String word_sound_mark;//音标

    @ColumnInfo(name = "word_irregular", typeAffinity = ColumnInfo.TEXT)
    public String word_irregular;//不规则形式

    @ColumnInfo(name="word_explain_list",typeAffinity =ColumnInfo.TEXT)
    public String word_explain_list;//含义集合

    @ColumnInfo(name = "word_length", typeAffinity = ColumnInfo.TEXT)
    public String word_length;//单词长度

    @ColumnInfo(name = "word_star", typeAffinity = ColumnInfo.TEXT)
    public String word_star;//星级

    @ColumnInfo(name = "word_times", typeAffinity = ColumnInfo.INTEGER)
    public int word_times;//记忆次数

    @ColumnInfo(name = "word_diy_type", typeAffinity = ColumnInfo.TEXT)
    public String word_diy_type;//自定义分类

    @ColumnInfo(name = "word_help_remember", typeAffinity = ColumnInfo.TEXT)
    public String word_help_remember;//助记

    @ColumnInfo(name = "word_synonym", typeAffinity = ColumnInfo.TEXT)
    public String word_synonym;//同义词

    @ColumnInfo(name = "word_phrase", typeAffinity = ColumnInfo.TEXT)

    public String word_phrase;//短语
    @ColumnInfo(name = "word_match", typeAffinity = ColumnInfo.TEXT)

    public String word_match;//常用搭配
    @ColumnInfo(name = "word_collete", typeAffinity = ColumnInfo.TEXT)

    public String word_collete;//收藏

    //Ignore亦可注解字段，让Room忽略此方法或者字段
    //由于Room只能识别一个构造器，如果需要定义多个构造器，可以使用Ignore注解让Room忽略这个构造器
    //Room不会持久化被注解Ignore标记过的字段
    @Ignore
    public Wordbean(String name, String age) {

    }

    public Wordbean(String word, String word_sound_mark, String word_irregular, String word_explain_list, String word_length, String word_star, int word_times, String word_diy_type, String word_help_remember, String word_synonym, String word_phrase, String word_match, String word_collete) {
        this.word = word;
        this.word_sound_mark = word_sound_mark;
        this.word_irregular = word_irregular;
        this.word_explain_list = word_explain_list;
        this.word_length = word_length;
        this.word_star = word_star;
        this.word_times = word_times;
        this.word_diy_type = word_diy_type;
        this.word_help_remember = word_help_remember;
        this.word_synonym = word_synonym;
        this.word_phrase = word_phrase;
        this.word_match = word_match;
        this.word_collete = word_collete;
    }

    public String getWord() {
        return word;
    }

    public String getWord_sound_mark() {
        return word_sound_mark;
    }



    public String getWord_explain_list() {
        return word_explain_list;
    }

    public String getWord_length() {
        return word_length;
    }

    public String getWord_star() {
        return word_star;
    }

    public int getWord_times() {
        return word_times;
    }

    public String getWord_diy_type() {
        return word_diy_type;
    }

    public String getWord_help_remember() {
        return word_help_remember;
    }

    public String getWord_synonym() {
        return word_synonym;
    }

    public String getWord_phrase() {
        return word_phrase;
    }

    public String getWord_match() {
        return word_match;
    }

    public String getWord_collete() {
        return word_collete;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setWord_sound_mark(String word_sound_mark) {
        this.word_sound_mark = word_sound_mark;
    }

    public void setWord_irregular(String word_irregular) {
        this.word_irregular = word_irregular;
    }

    public String getWord_irregular() {
        return word_irregular;
    }

    public void setWord_explain_list(String word_explain_list) {
        this.word_explain_list = word_explain_list;
    }

    public void setWord_length(String word_length) {
        this.word_length = word_length;
    }

    public void setWord_star(String word_star) {
        this.word_star = word_star;
    }

    public void setWord_times(int word_times) {
        this.word_times = word_times;
    }

    public void setWord_diy_type(String word_diy_type) {
        this.word_diy_type = word_diy_type;
    }

    public void setWord_help_remember(String word_help_remember) {
        this.word_help_remember = word_help_remember;
    }

    public void setWord_synonym(String word_synonym) {
        this.word_synonym = word_synonym;
    }

    public void setWord_phrase(String word_phrase) {
        this.word_phrase = word_phrase;
    }

    public void setWord_match(String word_match) {
        this.word_match = word_match;
    }

    public void setWord_collete(String word_collete) {
        this.word_collete = word_collete;
    }

    /***
     * toString
     * @return
     */
    @Override
    public String toString() {
        return "Wordbean{" +
                "word='" + word + '\'' +
                ", word_sound_mark='" + word_sound_mark + '\'' +
                ", word_speech='" + word_irregular + '\'' +
                ", word_explain_list=" + word_explain_list +
                ", word_length='" + word_length + '\'' +
                ", word_star='" + word_star + '\'' +
                ", word_times='" + word_times + '\'' +
                ", word_diy_type='" + word_diy_type + '\'' +
                ", word_help_remember='" + word_help_remember + '\'' +
                ", word_synonym='" + word_synonym + '\'' +
                ", word_phrase='" + word_phrase + '\'' +
                ", word_match='" + word_match + '\'' +
                ", word_collete='" + word_collete + '\'' +
                '}';
    }

}

//    实体类中可能用到注解有：
//    @Entity 将实体类定义为数据库实类
//    @Entity(tableName = "表名") 将数据库名称进行修改
//    @PrimaryKey(autoGenerate = true) 设置主键自增
//    @ColumnInfo(name = "列名称") 将列名称进行修改
//    @Ignore 忽略字段
//    概念 封装于SQLLite  谷歌自带？？
//    Entity：一个Entity对应数据库中的一张表。Entity类是Sqlite表结构对Java类的映射，在Java中可以看作一个Model类。
//    Dao：数据访问对象（Data Access Objects），我们通过它来访问数据。
//    关系：
//    一个Entity代表一张表，而每张表都需要一个Dao对象，用于对表进行增删改查；
//    Room数据库在被实例化之后我们就可以通过数据库实例获取Dao对象，然后通过Dao进行数据库操作。
