package com.example.zrframe.persistence.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zrframe.XArchApplication
import com.example.zrframe.bean.User
import com.example.zrframe.persistence.database.dao.UserDao

@Database(entities = [User::class], version = 1)
abstract class XDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private val db: XDatabase by lazy {
            Room.databaseBuilder(
                XArchApplication.instance,
                XDatabase::class.java, "database-name"
            ).build()
        }

        fun userDao(): UserDao {
            return db.userDao()
        }
    }

}