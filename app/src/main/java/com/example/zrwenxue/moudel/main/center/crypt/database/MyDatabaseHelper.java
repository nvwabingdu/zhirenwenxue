package com.example.zrwenxue.moudel.main.center.crypt.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "tuya";
    public static final String COLUMN_TEXT = "text_column";


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name_data";
    public static final String COLUMN_DESCRIPTION = "description_data";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
//                + COLUMN_TEXT + " TEXT)";
//        db.execSQL(createTableQuery);
//    }

    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_TEXT + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}