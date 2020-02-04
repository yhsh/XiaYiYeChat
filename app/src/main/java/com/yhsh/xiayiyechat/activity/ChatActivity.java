package com.yhsh.xiayiyechat.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yhsh.xiayiyechat.R;
import com.yhsh.xiayiyechat.adapter.MessageAdapter;

import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * @author xiayiye
 */
public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private Conversation conversation;
    private List<Message> allMessage;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //注册消息接收事件
        JMessageClient.registerEventReceiver(this);
        conversation = Conversation.createSingleConversation(getIntent().getStringExtra("user_id"));
        allMessage = conversation.getAllMessage();
        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(allMessage);
        rvList.setAdapter(messageAdapter);
        rvList.scrollToPosition(allMessage.size() - 1);
    }

    public void startSend(View view) {
        final EditText etSendContent = findViewById(R.id.et_send_content);
        String content = etSendContent.getText().toString().trim();
        TextContent textContent = new TextContent(content);
        final Message message = conversation.createSendMessage(textContent);
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.e("打印消息", i + "--" + s);
                if (i == 0) {
                    //发送成功
                    etSendContent.setText("");
                    //添加消息到集合
                    allMessage.add(message);
                    messageAdapter.notifyDataSetChanged();
                    rvList.scrollToPosition(allMessage.size() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });
        JMessageClient.sendMessage(message);
    }

    /**
     * 接收在线消息
     **/
    public void onEvent(final MessageEvent event) {
        //获取事件发生的会话对象
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message newMessage = event.getMessage();//获取此次离线期间会话收到的新消息列表
                if (newMessage != null) {
                    allMessage.add(newMessage);
                    messageAdapter.notifyDataSetChanged();
                    rvList.scrollToPosition(allMessage.size() - 1);
                }
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
        final List<Message> newMessageList = event.getOfflineMessageList();
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (newMessageList.size() > 0) {
//                    allMessage.clear();
//                    allMessage.addAll(newMessageList);
//                    messageAdapter.notifyDataSetChanged();
//                    rvList.scrollToPosition(allMessage.size() - 1);
                }
            }
        });
    }


    /**
     * 接收消息漫游事件
     * 如果在JMessageClient.init时启用了消息漫游功能，则每当一个会话的漫游消息同步完成时
     * sdk会发送此事件通知上层。
     **/
    public void onEvent(ConversationRefreshEvent event) {
        //获取事件发生的会话对象
        Conversation conversation = event.getConversation();
        //获取事件发生的原因，对于漫游完成触发的事件，此处的reason应该是
        //MSG_ROAMING_COMPLETE
        ConversationRefreshEvent.Reason reason = event.getReason();
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到ConversationRefreshEvent事件,待刷新的会话是%s.\n", conversation.getTargetId()));
        System.out.println("事件发生的原因 : " + reason);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑事件注册监听
        JMessageClient.unRegisterEventReceiver(this);
    }
}
