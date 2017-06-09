package com.wxc.review.ui.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wxc.review.R;
import com.wxc.review.entity.User;
import com.wxc.review.utils.Utils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnReg;
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 初始化 BmobSDK,第二个参数是在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "deb29e1f26b7c4365505351d09dd1624");
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReg = (Button) findViewById(R.id.btn_register);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            // 登陆
            case R.id.btn_login:
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                Log.e("TAG","获取输入成功");
                Log.e("TAG", username + "----" + password);
                if (!Utils.isNetworkConnected(this)) {
                    toast("没有网络啊==");
                } else if (username.equals("") || password.equals("")) {
                    toast("请输入正确的赛码名和密码");
                    break;
                } else {
                    final User bu2 = new User();
                    bu2.setUsername(username);
                    bu2.setPassword(password);

                    bu2.login(this, new SaveListener() {
                        public void onSuccess() {
                            toast("赛码面试助手来罗~");
                            // 跳转到主页
                            Intent toHome = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(toHome);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e("TAG", s);
                            toast("输入错误,赛码无法获得身份证~");
                        }

                    });
                }
                break;

            case R.id.btn_register:
                Intent toReg = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(toReg);
                break;
            default:
                break;

        }
    }

    public void toast(String toast) {
        // TODO Auto-generated method stub
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }


}
