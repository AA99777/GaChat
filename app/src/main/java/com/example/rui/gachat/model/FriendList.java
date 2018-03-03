package com.example.rui.gachat.model;

import java.io.Serializable;

/**
 * Created by Rui on 2017/12/13.
 */

public class FriendList implements Serializable {
    private String friendIDname; //好友登陆注册id;
    private String friendName; //好友呢称
    private String friendIcon; //好友头像
    private String lastMsg; //最后一条消息显示，应用于消息列表，好友列表忽略

    public String getFriendIDname() {
        return friendIDname;
    }

    public void setFriendIDname(String friendIDname) {
        this.friendIDname = friendIDname;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendIcon() {
        return friendIcon;
    }

    public void setFriendIcon(String friendIcon) {
        this.friendIcon = friendIcon;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
}
