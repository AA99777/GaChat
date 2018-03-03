package com.example.rui.gachat.model;

import java.util.Date;

/**
 * Created by Rui on 2017/12/13.
 */

public class ChatLog {
    private int chatIdName; //说话人id;0代表本人，1为好友
    private int msgtype; //消息类型（文本，图片，语音）
    private String content; //消息内容（文字，图片地址，语音地址）
    private Date sendDate; //发送时间（年月日时分秒）
    public  ChatLog(int chatIdName,int msgtype,String content){
        this.chatIdName=chatIdName;
        this.msgtype=msgtype;
        this.content=content;
        this.setSendDate(new Date(System.currentTimeMillis()));

    }

    public int getChatIdName() {
        return chatIdName;
    }

    public void setChatIdName(int chatIdName) {
        this.chatIdName = chatIdName;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}
