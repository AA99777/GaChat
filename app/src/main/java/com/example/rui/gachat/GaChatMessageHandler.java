package com.example.rui.gachat;

import android.os.Handler;

import com.example.rui.gachat.model.ChatLog;
import com.example.rui.gachat.dao.LoadChatLog;
import com.example.rui.gachat.utils.MsgTypeUtils;
import com.example.rui.gachat.utils.StaticCode;

import java.util.ArrayList;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

/**
 * Created by Rui on 2017/12/8.
 */

public class GaChatMessageHandler extends BmobIMMessageHandler {
    Handler mHandler = new Handler();
    ArrayList<ChatLog> chatLogArrayList = new ArrayList<>();
    ChatLog chatLog;

    @Override
    public void onMessageReceive(final MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);

        new Thread() {

            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (MainActivity.opfriendname.equals(messageEvent.getFromUserInfo().getUserId())) {
                            //将接收到的消息显示到当前聊天界面中并插入到表中
                            LoadChatLog.addcontent(StaticCode.RECEIVER_CODE, MsgTypeUtils.msgType(messageEvent.getMessage().getMsgType()), messageEvent.getMessage().getContent(), messageEvent.getFromUserInfo().getUserId());
                           // Toast.makeText(getApplicationContext(), messageEvent.getMessage().getContent() + "     " + messageEvent.getMessage().getMsgType()+"ttttt"+messageEvent.getFromUserInfo().getUserId(), Toast.LENGTH_LONG).show();
                        } else {
                            insertmsg(messageEvent);
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);

    }
    //将接收到的聊天内容插入到数据表中
        public void insertmsg( MessageEvent messageEvent){
            chatLogArrayList = LoadChatLog.saveChatLog(MainActivity.myUser.getUsername(), messageEvent.getFromUserInfo().getUserId());
            chatLog = new ChatLog(StaticCode.RECEIVER_CODE,  MsgTypeUtils.msgType(messageEvent.getMessage().getMsgType()), messageEvent.getMessage().getContent());
            if (chatLogArrayList.size() > 0) {
                chatLogArrayList.add(chatLog);
                String strjson = (String) MainActivity.gson.toJson(chatLogArrayList);
                String sql = "update " + MainActivity.myUser.getUsername() + " set chatLogJson = '" + strjson + "' where  chatLogName = '" + messageEvent.getFromUserInfo().getUserId() + "'";
                MainActivity.db.execSQL(sql);
            } else {
                chatLogArrayList.add(chatLog);
                String strjson = (String) MainActivity.gson.toJson(chatLogArrayList);
                String stu_sql = "insert into " + MainActivity.myUser.getUsername() + " (chatLogName,chatLogJson) values('" + messageEvent.getFromUserInfo().getUserId() + "','" + strjson + "')";
                MainActivity.db.execSQL(stu_sql);
            }
        }

}