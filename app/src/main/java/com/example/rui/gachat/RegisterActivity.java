package com.example.rui.gachat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.rui.gachat.model.MyUser;
import com.example.rui.gachat.utils.ToastUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Rui on 2017/12/8.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText register_id, register_pw;
    Button register_btn;
    RadioGroup radioGroupSex;
    RadioButton radioButton_boy, radioButton_gril;
    MyUser myUser;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerview);
        initUi();
    }

    private void initUi() {
        //获取实例
        register_id = findViewById(R.id.register_id);
        register_pw = findViewById(R.id.register_pw);
        register_btn = findViewById(R.id.register_btn);
        radioGroupSex = (RadioGroup) findViewById(R.id.rg_sex);
        radioButton_boy = (RadioButton) findViewById(R.id.rg_boy);
        radioButton_gril = (RadioButton) findViewById(R.id.rg_gril);
        //设置监听
        register_btn.setOnClickListener(this);
        radioGroupSex.setOnCheckedChangeListener(new RadioGroupListener());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_btn) {
            register();
        }
    }

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == radioButton_boy.getId()) {
                System.out.println("选中了boy");
            } else if (checkedId == radioButton_gril.getId()) {
                System.out.println("选中了gril!");
            }
        }
    }

    /**
     * 注册成功后，将注册账号密码保存到SharedPreferences同时跳转到好友会话列表
     */
    private void register() {
        final String user_num = register_id.getText().toString();
        final String user_password = register_pw.getText().toString().trim();
        // 非空验证
        if (user_num.isEmpty() || user_password.isEmpty()) {
            ToastUtils.toast(this, "密码或账号不能为空!");
            return;
        }
        // 使用BmobSDK提供的注册功能
        myUser = new MyUser();
        myUser.setUsername(user_num);
        myUser.setPassword(user_password);
        myUser.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser s, BmobException e) {
                if (e == null) {
                    SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("name", user_num);
                    ed.putString("pwd", user_password);
                    ed.commit();
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("myuser", myUser);
                    intent.putExtra("newuser", true);
                    startActivity(intent);
                    finish();
                } else {
                    //loge(e);
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

