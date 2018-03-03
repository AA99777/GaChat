package com.example.rui.gachat.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.rui.gachat.model.ChatLog;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Rui on 2017/12/15.
 */

public class UpdateChatLog {
    /**
     * @param useridname 用户名作为表名
     * @param chatIdName 好友id 作为查询条件值
     * @param db         传入的数据库对象
     * @param oldChatLog 该好友旧的聊天记录
     */
    public void updataChatLog(String useridname, String chatIdName, SQLiteDatabase db, ArrayList<ChatLog> oldChatLog) {
        ArrayList<ChatLog> newChatLog = oldChatLog;

        Gson gson = new Gson();
        String mynewChatLog = gson.toJson(newChatLog);
        //通过好友ID查询该好友的Json格式保存的聊天记录
        //INSERT INTO Persons VALUES ('Gates', 'Bill', 'Xuanwumen 10', 'Beijing')
        String aa = "insert into " + useridname + " values ('" + chatIdName + "'" + " '" + mynewChatLog + " ')";
        db.execSQL(aa);
    }
}
