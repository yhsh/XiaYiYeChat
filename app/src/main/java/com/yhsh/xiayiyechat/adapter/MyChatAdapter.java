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

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yhsh.xiayiyechat.R;
import com.yhsh.xiayiyechat.util.HeadPictureUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;

/**
 * @author 下一页5（轻飞扬）
 * 创建时间：2020/2/10 14:48
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeChat
 * 文件包名：com.yhsh.xiayiyechat.adapter
 * 文件说明：
 */
public class MyChatAdapter extends BaseAdapter {
    List<Conversation> chatData;

    public MyChatAdapter(List<Conversation> data) {
        chatData = data;
    }

    @Override
    public int getCount() {
        return chatData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ChatHolder chatHolder;
        String contentStr = "";
        if (view == null) {
            chatHolder = new ChatHolder();
            view = View.inflate(viewGroup.getContext(), R.layout.adapter_center_chat_listview, null);
            chatHolder.ivChatListLikeIcon = view.findViewById(R.id.iv_chat_list_like_icon);
            chatHolder.tvChatListMessageText = view.findViewById(R.id.tv_chat_list_message_text);
            chatHolder.tvChatListContentName = view.findViewById(R.id.tv_chat_list_content_name);
            chatHolder.tvChatListMessage = view.findViewById(R.id.tv_chat_list_message);
            chatHolder.tvChatListTime = view.findViewById(R.id.tv_chat_list_time);
            view.setTag(chatHolder);
        } else {
            chatHolder = (ChatHolder) view.getTag();
        }
        Conversation conversation = chatData.get(i);
        int unReadMsgCnt = conversation.getUnReadMsgCnt();
        ContentType latestType = conversation.getLatestType();
        switch (latestType) {
            case image:
                contentStr = "[图片]";
                break;
            case voice:
                contentStr = "[语音]";
                break;
            case text:
                contentStr = conversation.getLatestText();
                break;
            case location:
                contentStr = "[位置]";
                break;
            case file:
                contentStr = "[文件]";
                break;
            case video:
                contentStr = "[视频]";
                break;
            default:
                break;
        }


        Glide.with(viewGroup.getContext()).load(HeadPictureUtil.getInstance().getReceiveHeadUrl()).into(chatHolder.ivChatListLikeIcon);
        chatHolder.tvChatListContentName.setText(chatData.get(i).getTargetId());
        if (unReadMsgCnt > 0) {
            chatHolder.tvChatListMessageText.setVisibility(View.VISIBLE);
            String count = String.valueOf(unReadMsgCnt);
            chatHolder.tvChatListMessageText.setText(count);
        } else {
            chatHolder.tvChatListMessageText.setVisibility(View.GONE);
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        chatHolder.tvChatListTime.setText(time);
        chatHolder.tvChatListMessage.setText(contentStr);
        return view;
    }
}

class ChatHolder {
    ImageView ivChatListLikeIcon;
    TextView tvChatListMessageText, tvChatListContentName, tvChatListMessage, tvChatListTime;
}
