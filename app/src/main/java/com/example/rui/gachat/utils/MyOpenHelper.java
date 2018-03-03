package com.example.rui.gachat.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rui on 2017/12/12.
 */

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db"; //数据库名称
    private static final int version = 1; //数据库版本
    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table msglog(username varchar(20) not null , msgcontent varchar not null," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
