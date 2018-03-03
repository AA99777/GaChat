package com.example.rui.gachat.dao;

import android.database.Cursor;

import com.example.rui.gachat.ChatingActivity;
import com.example.rui.gachat.MainActivity;
import com.example.rui.gachat.model.ChatLog;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.example.rui.gachat.MainActivity.db;

/**
 * Created by Rui on 2017/12/14.
 */

public class LoadChatLog {
    /**
     * @param useridname 登陆的用户ID
     * @param chatIdName 要加载好友的ID
     * @return 该好友的聊天记录列表Arraylist
     */
    public static ArrayList<ChatLog> saveChatLog(String useridname, String chatIdName) {
        ArrayList<ChatLog> chatLogs = new ArrayList<ChatLog>();
        String checkbiao = "select * from " + useridname;
        //查询是否已经创建该用户表.返回结果大于0则表示已创建该用户表
        try {
            Cursor cursorb = MainActivity.db.rawQuery(checkbiao, null);//查询并获得游标
            if (cursorb.getCount() > 0) {
                //通过好友ID查询该好友的Json格式保存的聊天记录
                //如果聊天记录为空，则不赋值
                String where = " chatLogName = '" + chatIdName + "'";
                Cursor cursord = MainActivity.db.query(useridname, null, where, null, null, null, null);//查询并获得游标
                if (cursord.getCount() > 0) {
                    while ((cursord.moveToNext())) {
                        String asd = cursord.getString(cursord.getColumnIndex("chatLogJson"));
                        chatLogs = (ArrayList<ChatLog>) MainActivity.gson.fromJson(asd, new TypeToken<ArrayList<ChatLog>>() {
                        }.getType());
                    }
                } else {

                }
            } else {
                //插入聊天记录

            }
        } catch (Exception e) {
            //新用户则创建表
            String sql = "create table " + useridname + "(chatLogName varchar not null ,chatLogJson varchar not null );";
//执行SQL语句
            db.execSQL(sql);
            ChatLog chatLog = new ChatLog(0, 0, "newbiao");
            chatLogs.add(chatLog);
            String strjson = (String) MainActivity.gson.toJson(chatLogs);
            String stu_sql = "insert into " + useridname + " (chatLogName,chatLogJson) values('fristbiao','" + strjson + "')";
            db.execSQL(stu_sql);
        }
        return chatLogs;
    }

    public static void addcontent(int chatIdName, int msgtype, String content, String friendname) {
        ChatLog chatLog = new ChatLog(chatIdName, msgtype, content);
        if (ChatingActivity.chatlist.size() > 0) {
            //将新的消息插入到消息列表上
            ChatingActivity.chatlist.add(chatLog);
            //更新消息列表，并定位到最后一条消息
            ChatingActivity.crva.addData();
            ChatingActivity.recyclerView.scrollToPosition(ChatingActivity.crva.getItemCount() - 1);
            //将聊天记录同步到数据库
            String strjson = (String) MainActivity.gson.toJson(ChatingActivity.chatlist);
            String sql = "update " + MainActivity.myUser.getUsername() + " set chatLogJson = '" + strjson + "' where  chatLogName = '" + friendname + "'";
            MainActivity.db.execSQL(sql);
        } else {
            ChatingActivity.chatlist.add(chatLog);
            ChatingActivity.crva.addData();
            ChatingActivity.recyclerView.scrollToPosition(ChatingActivity.crva.getItemCount() - 1);
            String strjson = (String) MainActivity.gson.toJson(ChatingActivity.chatlist);
            String stu_sql = "insert into " + MainActivity.myUser.getUsername() + " (chatLogName,chatLogJson) values('" + friendname + "','" + strjson + "')";
            MainActivity.db.execSQL(stu_sql);
        }
//        ChatLog chatLog = new ChatLog(0, 0, et_mycontent.getText().toString());
//        chatlist.add(chatLog);
//        chatlist.add(chatLog);
//        crva.addData();
//        recyclerView.scrollToPosition(crva.getItemCount() - 1);
//        String strjson = (String) gson.toJson(chatlist);
//        try {
//            String sql = "update " + myUser.getUsername() + " set chatLogJson = '" + strjson + "' where  chatLogName = '" + friendList.getFriendName()+ "'";
//            db.execSQL(sql);
//        } catch (Exception e){
//            String stu_sql = "insert into "+myUser.getUsername()+" (chatLogName,chatLogJson) values('"+friendList.getFriendName()+"','" + strjson + "')";
//            db.execSQL(stu_sql);
//        ToastUtils.toast(ChatingActivity.this,"储存出错"+e.toString());
//        }
    }
}
