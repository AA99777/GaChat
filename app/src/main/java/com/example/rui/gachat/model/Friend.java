package com.example.rui.gachat.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Rui on 2017/12/8.
 */

public class Friend extends BmobObject {
    //用户
    private MyUser user;
    //好友
    private List<FriendList> friends;
    //消息列表
    private List<FriendList> msglist;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public List<FriendList> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendList> friends) {
        this.friends = friends;
    }

    public List<FriendList> getMsglist() {
        return msglist;
    }

    public void setMsglist(List<FriendList> msglist) {
        this.msglist = msglist;
    }
}
