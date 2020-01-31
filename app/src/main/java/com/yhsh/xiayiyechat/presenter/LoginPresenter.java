package com.yhsh.xiayiyechat.presenter;
/*
 * Copyright (c) 2020, smuyyh@gmail.com All Rights Reserved.
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG            #
 * #                                                   #
 */

import android.text.TextUtils;

import com.yhsh.xiayiyechat.contract.LoginContract;
import com.yhsh.xiayiyechat.model.LoginModel;

/**
 * @author 下一页5（轻飞扬）
 * 创建时间：2020/1/31 11:32
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeChat
 * 文件包名：com.yhsh.xiayiyechat.presenter
 * 文件说明：
 */
public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private LoginContract.Model model = new LoginModel(this);

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public boolean inject(String account, String pwd) {
        if (TextUtils.isEmpty(account)) {
            view.accountError();
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            view.pwdError();
            return false;
        }
        return true;
    }

    @Override
    public void login(String account, String pwd) {
        model.requestLogin(account, pwd);
    }

    @Override
    public void register(String account, String pwd) {
        model.requestRegister(account, pwd);
    }

    public void loginSuccess(String s) {
        view.loginSuccess(s);
    }

    public void loginFail(String s) {
        view.loginFail(s);
    }

    public void registerSuccess(String s) {
        view.registerSuccess(s);
    }

    public void registerFail(String s) {
        view.registerFail(s);
    }
}
