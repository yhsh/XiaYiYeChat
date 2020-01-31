package com.yhsh.xiayiyechat.model;

import android.util.Log;

import com.yhsh.xiayiyechat.contract.LoginContract;
import com.yhsh.xiayiyechat.presenter.LoginPresenter;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

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

/**
 * @author 下一页5（轻飞扬）
 * 创建时间：2020/1/31 11:53
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeChat
 * 文件包名：com.yhsh.xiayiyechat.model
 * 文件说明：
 */
public class LoginModel implements LoginContract.Model {
    private LoginPresenter loginPresenter;

    public LoginModel(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void requestLogin(String account, String pwd) {
        JMessageClient.login(account, pwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.e("登录：", i + "==" + s);
                if (i == 0) {
                    loginPresenter.loginSuccess(s);
                } else {
                    loginPresenter.loginFail(s);
                }
            }
        });
    }

    @Override
    public void requestRegister(String account, String pwd) {
        JMessageClient.register(account, pwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.e("注册：", i + "==" + s);
                if (i == 0) {
                    loginPresenter.registerSuccess(s);
                } else {
                    loginPresenter.registerFail(s);
                }
            }
        });
    }
}
