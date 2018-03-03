package com.example.rui.gachat.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Rui on 2017/12/8.
 */

public class MyUser extends BmobUser {
    private String name; //好友名字;
    private boolean loginState;         //登录状态
    private boolean sex;                //false - male,  true - female
    private String nick_URL_NET;        //头像,将图像上传至服务器后会返回URL 保存
    private Integer age;
    private String signature;

    public boolean isLoginState() {
        return loginState;
    }

    public void setLoginState(boolean loginState) {
        this.loginState = loginState;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getNick_URL_NET() {
        return nick_URL_NET;
    }

    public void setNick_URL_NET(String nick_URL_NET) {
        this.nick_URL_NET = nick_URL_NET;
    }

    public String getNick_URL_LOCAL() {
        return nick_URL_LOCAL;
    }

    public void setNick_URL_LOCAL(String nick_URL_LOCAL) {
        this.nick_URL_LOCAL = nick_URL_LOCAL;
    }

    private String nick_URL_LOCAL;      //头像,将图像保存至本地   路径

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
