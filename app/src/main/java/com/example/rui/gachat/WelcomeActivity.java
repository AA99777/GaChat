package com.example.rui.gachat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.rui.gachat.model.Friend;
import com.example.rui.gachat.model.FriendList;
import com.example.rui.gachat.model.MyUser;
import com.example.rui.gachat.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class WelcomeActivity extends AppCompatActivity {
    String userIdName;
    String userpw;
    Timer timer = new Timer();
    ArrayList<FriendList> arrayfriend;
    ArrayList<FriendList> arraymsg;
    Intent intent_main;
    Boolean loginstatus=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //先登陆加载数据，然后定时跳转
        //登陆失败，则跳转到login页面
        load();
        timer.schedule(task, 2000 ); // 1s后执行task,经过1s再次执行
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (loginstatus){
                    startActivity(intent_main);
                    finish();
                }
            else {
                    loginstatus=false;
                    starLoginView();
                }
            }
            super.handleMessage(msg);
        };
    };

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    /**
     * 加载取得SharedPreferences内的登陆账号密码
     * 加载到则执行登陆方法
     */
    public void load() {
        //取得SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        /*
         * getString,参数意义表示用key=第一个参数去取值，如果取不到值，就返回第二个参数
         * 因为有了第二个参数，所以我们也不需要判断是否存在info文件
         * */
        userIdName= sp.getString("name", "");
        userpw = sp.getString("pwd", "");
        if (userIdName !=null || !userIdName.isEmpty()||userpw!=null||!userpw.isEmpty()){
            userlogin();
        }
    }

    /**
     * 登陆方法
     * 加载成功，则查询好友会话列表
     */
    public void userlogin() {
        MyUser myUser = new MyUser();
        myUser.setUsername(userIdName);
        myUser.setPassword(userpw);
        myUser.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                    queryFriend(myUser);
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                }else{
                    //loge(e);
                }
            }
        });
    }
    /**
     * 登陆成功后进行好友列表查询
     *
     * @param myUser
     */
    public void queryFriend(final MyUser myUser) {
        BmobQuery<Friend> query = new BmobQuery<Friend>();
        query.getObject("a2aa24f008", new QueryListener<Friend>() {
            @Override
            public void done(Friend object, BmobException e) {
                if (e == null) {
                    arrayfriend = (ArrayList<FriendList>) object.getFriends();
                    arraymsg = (ArrayList<FriendList>) object.getMsglist();
                    intent_main = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent_main.putExtra("friendlist", arrayfriend);
                    intent_main.putExtra("msglist", arraymsg);
                    intent_main.putExtra("myuser", myUser);
                    loginstatus=true;
                    ToastUtils.toast(WelcomeActivity.this,"登录成功:"+myUser.getUsername());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });

    }
    /**
     * 没有成功获取到账号密码登陆失败后的跳转到输入登陆界面
     */
    public  void starLoginView(){
        Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
