package com.example.rui.gachat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rui.gachat.model.Friend;
import com.example.rui.gachat.model.FriendList;
import com.example.rui.gachat.model.MyUser;
import com.example.rui.gachat.utils.ToastUtils;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Rui on 2017/12/8.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText login_userid, login_userpw;
    Button login_btn;
    TextView forget_userpw, register_user;
    String user_num;
    String user_password;
    ArrayList<FriendList> arrayfriend;
    ArrayList<FriendList> arraymsg;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);
        initUi();
    }

    public void initUi() {
        login_userid = (EditText) findViewById(R.id.login_id);
        login_userpw = (EditText) findViewById(R.id.login_pw);
        login_btn = (Button) findViewById(R.id.login_btn);
        forget_userpw = (TextView) findViewById(R.id.forget_pw);
        register_user = (TextView) findViewById(R.id.register);
        login_btn.setOnClickListener(this);
        forget_userpw.setOnClickListener(this);
        register_user.setOnClickListener(this);
    }

    /**
     * 判断账号密码后登陆
     * 然后保存登陆密码账号
     * 接着查询好友会话列表
     *
     * @param user_num
     * @param user_password
     */
    private void userlogin(final String user_num, final String user_password) {
            if (user_num.isEmpty() || user_password.isEmpty()) {
                ToastUtils.toast(this, "密码或账号不能为空!");
                return;
            }
        final MyUser myUser = new MyUser();
        myUser.setUsername(user_num);
        myUser.setPassword(user_password);
        myUser.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser1, BmobException e) {
                if (e == null) {
                    SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("name", user_num);
                    ed.putString("pwd", user_password);
                    ed.commit();
                    queryFriend(myUser1);
                    ToastUtils.toast(LoginActivity.this, " 登录成功");
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                } else {
                    ToastUtils.toast(LoginActivity.this, " 登录失败" + e.toString());
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
                    Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                    intent_main.putExtra("friendlist", arrayfriend);
                    intent_main.putExtra("msglist", arraymsg);
                    intent_main.putExtra("myuser", myUser);
                    //intent_main.putExtra("newuser",false);
                    startActivity(intent_main);
                    finish();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                user_num = login_userid.getText().toString();
                user_password = login_userpw.getText().toString();
                userlogin(user_num, user_password);
                break;
            case R.id.forget_pw:
                break;
            case R.id.register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
