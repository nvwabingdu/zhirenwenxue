package com.example.zrwenxue.moudel.main.word.roomdata;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.zrwenxue.moudel.main.word.Wordbean;

import java.util.List;


/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-16
 * Time: 12:59
 */
@Dao
public interface WordDao {

    /**
     * 插入数据
     * @param wordbeans
     */
    @Insert
     void insertData(Wordbean... wordbeans);

    /**
     * 删除数据
     * @param wordbeans
     */
    @Delete
     void deleteData(Wordbean... wordbeans);


    /**
     * 修改数据
     * @param wordbeans
     */
    @Update
     void updateData(Wordbean... wordbeans);

    /**
     * 查询某个单词
     * @param word
     * @return
     */
    @Query("SELECt * FROM word_table WHERE word=:word")
     Wordbean queryWord(String word);

    /**
     * 查询所有单词
     * @return
     */
    @Query("SELECt * FROM word_table")
    List<Wordbean> queryAllWord();

    /**
     * 查询柯林斯星级的反馈集合
     * @param word_star
     * @return
     */
    @Query("SELECt * FROM word_table WHERE word_star=:word_star")
    List<Wordbean> queryWordStar(String word_star);

    /**
     * 查询记忆次数的反馈集合
     * @param word_times
     * @return
     */
    @Query("SELECt * FROM word_table where word_times=:word_times")
     List<Wordbean> queryWordTimes(String word_times);

    /**
     * 查询单词长度的反馈集合
     * @param word_length
     * @return
     */
    @Query("SELECT * FROM word_table where word_length=:word_length")
    List<Wordbean> queryWordLength(String word_length);


    /**
     * 查询收藏的集合
     * @param word_collete
     * @return
     */
    @Query("SELECT * FROM word_table where word_collete=:word_collete")
    List<Wordbean> queryWordCollete(String word_collete);



}
