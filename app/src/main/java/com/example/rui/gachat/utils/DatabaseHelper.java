package com.example.rui.gachat.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rui on 2017/12/13.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db"; //数据库名称
    private static final int version = 1; //数据库版本
    public DatabaseHelper(Context context) {
        super(context,DB_NAME,null,version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(useridname varchar(30) not null , chatlogjson varchar not null );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
