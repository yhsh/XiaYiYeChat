package com.yhsh.xiayiyechat.activity;
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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yhsh.xiayiyechat.R;
import com.yhsh.xiayiyechat.adapter.MyChatAdapter;
import com.yhsh.xiayiyechat.util.AndroidUtils;

import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * @author 下一页5（轻飞扬）
 * 创建时间：2020/2/10 14:43
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeChat
 * 文件包名：com.yhsh.xiayiyechat.activity
 * 文件说明：
 */
public class ChatListActivity extends AppCompatActivity {

    private ListView lvChatList;
    private List<Conversation> data;
    private LinearLayout llChatListTittle;
    private MyChatAdapter myChatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        AndroidUtils.getInstance().setTittleTransparent(this);
        //注册消息接收事件
        JMessageClient.registerEventReceiver(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        lvChatList = findViewById(R.id.lv_chat_list);
        llChatListTittle = findViewById(R.id.ll_chat_list_tittle);
    }

    private void initData() {
        data = JMessageClient.getConversationList();
        myChatAdapter = new MyChatAdapter(data);
        lvChatList.setAdapter(myChatAdapter);
    }

    private void initListener() {
        llChatListTittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lvChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //重置已读消息
                data.get(i).resetUnreadCount();
                //跳转聊天页面
                Intent intent = new Intent(ChatListActivity.this, PrivateChatActivity.class);
                intent.putExtra("user_id", data.get(i).getTargetId());
                startActivity(intent);
            }
        });
    }

    /**
     * 接收离线消息。
     * 类似MessageEvent事件的接收，上层在需要的地方增加OfflineMessageEvent事件的接收
     * 即可实现离线消息的接收。
     **/
    public void onEvent(OfflineMessageEvent event) {
        //获取事件发生的会话对象
        Conversation conversation = event.getConversation();
        //获取此次离线期间会话收到的新消息列表
        List<Message> newMessageList = event.getOfflineMessageList();
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
        if (newMessageList.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (conversation.getTargetId().equals(data.get(i).getTargetId())) {
                    for (int j = 0; j < conversation.getAllMessage().size(); j++) {
                        data.get(i).getAllMessage().add(conversation.getMessage(j));
                    }
                }
            }
            myChatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //刷新Ui
        if (myChatAdapter != null) {
            data = JMessageClient.getConversationList();
            myChatAdapter.notifyDataSetChanged();
        }
    }
}
