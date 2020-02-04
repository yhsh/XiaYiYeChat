package com.yhsh.xiayiyechat.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yhsh.xiayiyechat.R;
import com.yhsh.xiayiyechat.contract.LoginContract;
import com.yhsh.xiayiyechat.presenter.LoginPresenter;
import com.yhsh.xiayiyechat.util.AndroidUtils;
import com.yhsh.xiayiyechat.util.ToastUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * @author xiayiye
 */
public class MainActivity extends AppCompatActivity implements LoginContract.View {

    private LoginPresenter loginPresenter;
    private EditText etChatAccount;
    private String accountChat;
    private static final int GET_RECODE_AUDIO = 1;
    private static String[] PERMISSION_AUDIO = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etChatAccount = findViewById(R.id.et_chat_account);
        AndroidUtils.getInstance().setTittleTransparent(this);
        loginPresenter = new LoginPresenter(this);
        verifyAudioPermissions();
    }

    @Override
    public void accountError() {
        Toast.makeText(this, "账号错误", Toast.LENGTH_LONG).show();
    }

    @Override
    public void pwdError() {
        Toast.makeText(this, "密码错误", Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerSuccess(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerFail(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        //跳转首页
//        startActivity(new Intent(this, ChatActivity.class));
        Intent intent = new Intent(this, PrivateChatActivity.class);
        intent.putExtra("user_id", accountChat);
        startActivity(intent);
    }

    @Override
    public void loginFail(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void clickLogin(View view) {
        EditText etAccount = findViewById(R.id.et_account);
        EditText etPwd = findViewById(R.id.et_pwd);
        accountChat = etChatAccount.getText().toString().trim();
        if (TextUtils.isEmpty(accountChat)) {
            ToastUtil.show("聊天账号不能为空！,没有账号可输入默认账号：xiayiye");
            return;
        }
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        if (loginPresenter.inject(account, pwd)) {
            loginPresenter.login(account, pwd);
        }
    }

    public void clickCreate(View view) {
        EditText etAccount = findViewById(R.id.et_account);
        EditText etPwd = findViewById(R.id.et_pwd);
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        accountChat = etChatAccount.getText().toString().trim();
        if (TextUtils.isEmpty(accountChat)) {
            ToastUtil.show("聊天账号不能为空！,没有账号可输入默认账号：xiayiye");
            return;
        }
        if (loginPresenter.inject(account, pwd)) {
            loginPresenter.register(account, pwd);
        }
    }

    public void verifyAudioPermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION_AUDIO, GET_RECODE_AUDIO);
        }
    }
}
