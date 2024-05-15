package com.example.zrwenxue.moudel.main.word.roomdata;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.zrwenxue.moudel.main.word.Wordbean;


/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-16
 * Time: 13:04
 * room------
 * 注解Database告诉系统这是Room数据库对象
 * entities指定该数据库有哪些表，多张表就逗号分隔
 * version指定数据库版本号，升级时需要用到
 * 数据库继承自RoomDatabase
 */
@Database(entities = {Wordbean.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "word_db";
    private static MyDatabase databaseInstance;

    /**
     * 结合单例模式完成创建数据库实例
     * @param context
     * @return
     */
    public static synchronized MyDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, DATABASE_NAME).build();
        }
        return databaseInstance;
    }


    /**
     * 将创建的Dao对象以抽象方法的形式返回
     * @return
     */
    public abstract WordDao getWordDao();

}

