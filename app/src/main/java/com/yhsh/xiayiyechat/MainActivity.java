package com.yhsh.xiayiyechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yhsh.xiayiyechat.activity.ChatActivity;
import com.yhsh.xiayiyechat.contract.LoginContract;
import com.yhsh.xiayiyechat.presenter.LoginPresenter;

/**
 * @author xiayiye
 */
public class MainActivity extends AppCompatActivity implements LoginContract.View {

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginPresenter = new LoginPresenter(this);
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
        startActivity(new Intent(this, ChatActivity.class));
    }

    @Override
    public void loginFail(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void clickLogin(View view) {
        EditText etAccount = findViewById(R.id.et_account);
        EditText etPwd = findViewById(R.id.et_pwd);
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
        if (loginPresenter.inject(account, pwd)) {
            loginPresenter.register(account, pwd);
        }
    }
}
