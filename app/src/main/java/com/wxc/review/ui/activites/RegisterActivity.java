package com.wxc.review.ui.activites;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wxc.review.R;
import com.wxc.review.entity.User;
import com.wxc.review.utils.ShowToas;
import com.wxc.review.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnReg;
    private EditText nickNmae, phoneEdit, passEdit, repassEdit;
    private String password = null;
    private String comfirmPsd = null;
    private String phone = null;

    private int icon_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        picIntoSd();
        initView();
//        nickname = (EditText) findViewById(R.id.et_username);
//        etPassword = (EditText) findViewById(R.id.et_password);
//        etComfirmPsd = (EditText) findViewById(R.id.et_comfirm_psd);
//        etPhone = (EditText) findViewById(R.id.et_phone);
//        btnReg = (Button) findViewById(R.id.btn_reg_now);
//        btnReg.setOnClickListener(this);
    }

    private void initView() {
        nickNmae = (EditText) findViewById(R.id.et_username);
        repassEdit = (EditText) findViewById(R.id.et_password);
        passEdit = (EditText) findViewById(R.id.et_comfirm_psd);
        phoneEdit = (EditText) findViewById(R.id.et_phone);
        btnReg = (Button) findViewById(R.id.btn_reg_now);
        //set click listeren
//        getCodeBut.setOnClickListener(this);
        btnReg.setOnClickListener(this);

        //添加文本监听接口
        nickNmae.addTextChangedListener(new MyEditWatcher());
        phoneEdit.addTextChangedListener(new MyEditWatcher());
        passEdit.addTextChangedListener(new MyEditWatcher());
        repassEdit.addTextChangedListener(new MyEditWatcher());
//        authcodeEdit.addTextChangedListener(new MyEditWatcher());

//        phoneEdit.addTextChangedListener(new MyEditWatcher2());

        btnReg.setClickable(false);
//        getCodeBut.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_reg_now:
                register();
                break;

            default:
                break;
        }
    }

    private String nickname;
    private String username;
    private String pass;

    private void register() {

        nickname = nickNmae.getText().toString().trim();
        username = phoneEdit.getText().toString().trim();
        pass = passEdit.getText().toString().trim();
        String repass = repassEdit.getText().toString().trim();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)) {
            ShowToas.showToast(this, "输入信息不能为空");
            return;
        }
        if (username.length() != 11) {
            ShowToas.showToast(this, "请输入有效的手机号");
            return;
        }
        if (!pass.equals(repass)) {
            ShowToas.showToast(this, "两次密码不一致");
            return;
        }
        //提交验证码，调用此方法后执行其回掉方法将结果返回
//        submitCode("86", username, code);
        registerUser();
    }

    //EditText的文本观察家
    class MyEditWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s.toString())) {
                btnReg.setPressed(false);
                btnReg.setClickable(false);
            } else {
                btnReg.setPressed(true);
                btnReg.setClickable(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    //注册bmob
    public void registerUser() {
        //6.0系统
        if (Build.VERSION.SDK_INT >= 23) {
            User user = new User();
            user.setNickname(nickname);
            user.setUsername(username);
            user.setPassword(pass);
            user.setSex("男");
            user.setBrithday("2001-1-1");
            user.setIcon(null);
            user.setType(icon_id);
//            Log.e("TAG","注册设置");
//            final BmobFile bmobFile = new BmobFile(file);//头像文件
//            user.setIcon(bmobFile);
//            Log.e("TAG","注册信息设置正确");
            user.setMotto("***我的个性签名***");

            user.signUp(RegisterActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.v("TAG", "注册成功");

                    ShowToas.showToast(RegisterActivity.this, "注册成功");
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.v("TAG", "注册失败" + i + "---" + s.toString());
                    if (i == 202) {
                        ShowToas.showToast(RegisterActivity.this, "此号码已经注册过了，换个试试");
                    }

                }
            });
            return;
        }
        //6.0以下系统

        File iconfile = new File(pic_sd_path);
        final BmobFile bmobFile = new BmobFile(file);//头像文件
        Log.e("TAG","bmobfile");
        bmobFile.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                User user = new User();
                user.setNickname(nickname);
                user.setUsername(username);
                user.setPassword(pass);
                user.setIcon(bmobFile);
                user.setType(0);
                user.setSex("男");
                user.setBrithday("2001-1-1");
                user.setMotto("***我的个性签名***");
                Log.e("TAG", nickname + "-----" + "username" + "---------" + "pass");
                user.signUp(RegisterActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {

                        ShowToas.showToast(RegisterActivity.this, "注册成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (i == 202) {
                            ShowToas.showToast(RegisterActivity.this, "此号码已经注册过了，换个试试");
                        }

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                if (i == 9015) {
                    ShowToas.showToast(RegisterActivity.this, "网络链接异常，请检查你的网络链接");
                }

            }
        });

    }

    /**
     * 判断外置sdcard是否可以正常使用
     *
     * @return
     */
    public Boolean existsSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }

    //将图片加载进入sd卡最后在取出来作为头像
    private File file;
    private String pic_sd_path;

    private void picIntoSd() {
        //版本大于6.0的情况 6.0系统在权限和sd卡读写方面做了改变
        if (Build.VERSION.SDK_INT >= 23) {
            Random random = new Random();
            int icon_num = random.nextInt(12) + 1;

            switch (icon_num) {
                case 1:
                    icon_id = R.drawable.icon_head1;
                    break;
                case 2:
                    icon_id = R.drawable.icon_head2;
                    break;
                case 3:
                    icon_id = R.drawable.icon_head3;
                    break;
                case 4:
                    icon_id = R.drawable.icon_head4;
                    break;
                case 5:
                    icon_id = R.drawable.icon_head5;
                    break;
                case 6:
                    icon_id = R.drawable.icon_head6;
                    break;
                case 7:
                    icon_id = R.drawable.icon_head7;
                    break;
                case 8:
                    icon_id = R.drawable.icon_head8;
                    break;
                case 9:
                    icon_id = R.drawable.icon_head9;
                    break;
                case 10:
                    icon_id = R.drawable.icon_head10;
                    break;
                case 11:
                    icon_id = R.drawable.icon_head11;
                    break;
                case 12:
                    icon_id = R.drawable.icon_head12;
                    break;
            }
            Utils.drawable_icon = icon_id;
            return;
        }
        InputStream is = null;
        Random random = new Random();
        int icon_num = random.nextInt(12) + 1;
        String icon_name = null;
        switch (icon_num) {
            case 1:
                icon_name = "icon_head1.jpg";
                break;
            case 2:
                icon_name = "icon_head2.jpg";
                break;
            case 3:
                icon_name = "icon_head3.jpg";
                break;
            case 4:
                icon_name = "icon_head4.jpg";
                break;
            case 5:
                icon_name = "icon_head5.jpg";
                break;
            case 6:
                icon_name = "icon_head6.jpg";
                break;
            case 7:
                icon_name = "icon_head7.jpg";
                break;
            case 8:
                icon_name = "icon_head8.jpg";
                break;
            case 9:
                icon_name = "icon_head9.jpg";
                break;
            case 10:
                icon_name = "icon_head10.jpg";
                break;
            case 11:
                icon_name = "icon_head11.jpg";
                break;
            case 12:
                icon_name = "icon_head12.jpg";
                break;
        }
        try {
            is = getResources().getAssets().open(icon_name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pic_sd_path = Environment.getExternalStorageDirectory().getPath() + "/" + icon_name;
        file = new File(pic_sd_path);
        FileOutputStream out = null;

        if (!file.exists()) {
            int len;
            try {
                byte[] bytes = new byte[1024];
                out = new FileOutputStream(file);
                while ((len = is.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Log.v("TAG", "图片已经存在sd卡 " + file.length() + file.getAbsolutePath());
        }
    }

    public void toast(String toastText) {
        // TODO Auto-generated method stub
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }


}
