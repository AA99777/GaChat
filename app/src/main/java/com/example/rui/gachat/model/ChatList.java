package com.example.rui.gachat.model;

/**
 * Created by Rui on 2017/12/13.
 */

public class ChatList {
    private String userIdName;
    private String chatLogJson;
    //以String格式储存ArrayList<ChatLog> 每个getObject()对象(为ChatLog chatLog)和userIdName一一对应

    public String getUserIdName() {
        return userIdName;
    }

    public void setUserIdName(String userIdName) {
        this.userIdName = userIdName;
    }

    public String getChatLogJson() {
        return chatLogJson;
    }

    public void setChatLogJson(String chatLogJson) {
        this.chatLogJson = chatLogJson;
    }
}
