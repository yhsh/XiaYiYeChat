package com.yhsh.xiayiyechat.adapter;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yhsh.xiayiyechat.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

/**
 * @author 下一页5（轻飞扬）
 * 创建时间：2020/1/31 14:24
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeChat
 * 文件包名：com.yhsh.xiayiyechat.adapter
 * 文件说明：
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    List<Message> allMessage;

    public MessageAdapter(List<Message> allMessage) {
        this.allMessage = allMessage;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = View.inflate(parent.getContext(), R.layout.item_chat_send, null);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_chat_receive, null);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = allMessage.get(position);
        if (message.getDirect() == MessageDirect.receive) {
            //拿到接收的消息
            TextContent text = (TextContent) message.getContent();
            String sendMsg = text.getText();
            if (!TextUtils.isEmpty(sendMsg)) {
                holder.tvContentReceive.setText(sendMsg);
            }
        } else {
            //拿到发送的消息
            TextContent text = (TextContent) message.getContent();
            String receiveMsg = text.getText();
            if (!TextUtils.isEmpty(receiveMsg)) {
                holder.tvContentSend.setText(receiveMsg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return allMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = allMessage.get(position);
        if (msg.getDirect() == MessageDirect.send) {
            //拿到发送的消息
            TextContent text = (TextContent) msg.getContent();
            String sendMsg = text.getText();
            Log.e("发送的消息", sendMsg + "");
            return 0;
        } else {
            //拿到接收的消息
            TextContent text = (TextContent) msg.getContent();
            String receiveMsg = text.getText();
            Log.e("接收的消息", receiveMsg + "");
            return 1;
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvContentSend;
        private final TextView tvContentReceive;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContentSend = itemView.findViewById(R.id.tv_content_send);
            tvContentReceive = itemView.findViewById(R.id.tv_content_receive);
        }
    }
}
