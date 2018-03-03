package com.example.rui.gachat.bean;

import android.os.Handler;

import com.example.rui.gachat.ChatingActivity;
import com.example.rui.gachat.dao.LoadChatLog;
import com.example.rui.gachat.utils.StaticCode;
import com.example.rui.gachat.utils.ToastUtils;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Rui on 2017/12/27.
 */

public class Sendmsg {

    public static void sendmsg(BmobIMConversation mBmobIMConversation,int msgtype,String content,String friendname){
        if (msgtype==StaticCode.MSGTYPE_TXT){
            sendtxt(mBmobIMConversation,content,friendname);
        }
        else if (msgtype==StaticCode.MSGTYPE_IMG){
            sendimg(mBmobIMConversation,content,friendname);
        }
    }

    public static void sendtxt(BmobIMConversation mBmobIMConversation, final String txt, final String friendname){
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(txt);
        mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e != null) {

                } else {
                    LoadChatLog.addcontent(StaticCode.SENDER_CODE, StaticCode.MSGTYPE_TXT, txt, friendname);
                }
            }
        });
    }
    public static void sendimg(final BmobIMConversation mBmobIMConversation, final String imageurl, final String friendname){
        final BmobIMImageMessage imageMessage=new BmobIMImageMessage(imageurl);
        final Handler mHandlers = new Handler();
        new Thread() {
            public void run() {
                mHandlers.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mBmobIMConversation.sendMessage(imageMessage, new MessageSendListener() {
                            @Override
                            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                                if (e!=null){}
                                else {
                                    //放图片到聊天界面
                                    LoadChatLog.addcontent(StaticCode.SENDER_CODE, StaticCode.MSGTYPE_IMG, imageurl, friendname);
                                }
                            }
                        });
                    }
                });
            }
        }.start();
    }
    public static void sendvoice(){}
    public static void sendaudio(){}
    public static void sendfile(){}


}
