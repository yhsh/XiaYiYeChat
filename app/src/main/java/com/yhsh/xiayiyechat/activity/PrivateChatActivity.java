package com.yhsh.xiayiyechat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcw.library.imagepicker.ImagePicker;
import com.lcw.library.imagepicker.utils.MediaFileUtil;
import com.yhsh.xiayiyechat.util.AndroidUtils;
import com.yhsh.xiayiyechat.util.GlideLoader;
import com.yhsh.xiayiyechat.MyApplication;
import com.yhsh.xiayiyechat.R;
import com.yhsh.xiayiyechat.adapter.ChatMessageAdapter;
import com.yhsh.xiayiyechat.util.PlayVoiceUtil;
import com.yhsh.xiayiyechat.util.TakePhotoUtil;
import com.yhsh.xiayiyechat.util.ToastUtil;
import com.yhsh.xiayiyechat.view.SoundTextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;


/**
 * @author xiayiye5
 * 私聊页面
 */
public class PrivateChatActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private RecyclerView rvChatList;
    private ImageView ivChatMorePage;
    private ImageView ivChatTittleBack;
    private TextView tvChatName;
    private ImageView ivChatMessageNoDisturb;
    private final static int MESSAGE_NO_DISTURB = 999;
    private final static int SELECT_IMAGE_CODE = 888;
    private final static int SELECT_CAMERA_CODE = 777;
    private EditText etInputChatText;
    private Conversation conversation;
    private List<Message> allMessage;
    private ChatMessageAdapter chatMessageAdapter;
    private ImageView ivChatTakePhoto;
    private ImageView ivChatSelectImage;
    private MediaPlayer mp = new MediaPlayer();
    /**
     * 选中图片的路径集合
     */
    private ArrayList<String> selectedImgPath;
    private ImageView ivRecordVoice;
    private SoundTextView tvSound;
    private String takePhotoPath;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
        AndroidUtils.getInstance().setTittleTransparent(this);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        userId = getIntent().getStringExtra("user_id");
        if (userId != null) {
            tvChatName.setText(userId);
            //注册消息接收事件
            JMessageClient.registerEventReceiver(this);
            conversation = Conversation.createSingleConversation(userId);
            allMessage = conversation.getAllMessage();
            rvChatList.setLayoutManager(new LinearLayoutManager(this));
            chatMessageAdapter = new ChatMessageAdapter(allMessage, "http://image.biaobaiju.com/uploads/20180122/22/1516629810-pnBjHQktMi.jpg");
            rvChatList.setAdapter(chatMessageAdapter);
            rvChatList.scrollToPosition(allMessage.size() - 1);
            initRecord();
        }
    }

    private void initRecord() {
        tvSound.mConv = conversation;
        tvSound.onNewMessage = new SoundTextView.OnNewMessage() {
            @Override
            public void newMessage(Message message) {
                msgIsSuccess(message);
            }
        };
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        if (audioManager.isSpeakerphoneOn()) {
            audioManager.setSpeakerphoneOn(true);
        } else {
            audioManager.setSpeakerphoneOn(false);
        }
        mp.setAudioStreamType(AudioManager.STREAM_RING);
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    private void msgIsSuccess(final Message message) {
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    //语音发送成功
                    Toast.makeText(MyApplication.getMyContext(), "语音发送成功", Toast.LENGTH_LONG).show();
                    Log.e("打印语音", "语音发送成功");
                    allMessage.add(message);
                    chatMessageAdapter.notifyDataSetChanged();
                    rvChatList.scrollToPosition(allMessage.size() - 1);
                }
            }
        });
    }

    private void initListener() {
        ivChatMorePage.setOnClickListener(this);
        ivChatTittleBack.setOnClickListener(this);
        ivRecordVoice.setOnClickListener(this);
        ivChatTakePhoto.setOnClickListener(this);
        ivChatSelectImage.setOnClickListener(this);
        etInputChatText.setOnEditorActionListener(this);
        //设置点击视频播放的监听
        chatMessageAdapter.setVideoClickListener(new ChatMessageAdapter.VideoClickListener() {
            @Override
            public void playVideo(int position) {
                ToastUtil.show("点击了" + position);
                Intent intent = new Intent(PrivateChatActivity.this, ChatPageVideoPlayActivity.class);
                intent.putExtra("video_path", ((VideoContent) allMessage.get(position).getContent()).getVideoLocalPath());
                startActivity(intent);
            }
        });
        //点击图片的点击事件
        chatMessageAdapter.setImageClickListener(new ChatMessageAdapter.ImageClickListener() {
            @Override
            public void showImage(int position) {
                ToastUtil.show("点击了" + position);
                Intent intent = new Intent(PrivateChatActivity.this, ChatPageVideoPlayActivity.class);
                intent.putExtra("img_path", ((ImageContent) allMessage.get(position).getContent()).getLocalPath());
                startActivity(intent);
            }
        });
        //设置语音播放的方法
        chatMessageAdapter.setVoicePlayListener(new ChatMessageAdapter.VoicePlayListener() {
            @Override
            public void playVoice(String playUrl, final ImageView ivMessageVoice) {
                PlayVoiceUtil.startPlay(mp, ivMessageVoice, playUrl);
            }
        });
    }

    private void initView() {
        rvChatList = findViewById(R.id.rv_chat_list);
        ivChatMessageNoDisturb = findViewById(R.id.iv_chat_message_no_disturb);
        ivChatMorePage = findViewById(R.id.iv_chat_more_page);
        ivChatTittleBack = findViewById(R.id.iv_chat_tittle_back);
        tvChatName = findViewById(R.id.tv_chat_name);
        ivRecordVoice = findViewById(R.id.iv_record_voice);
        tvSound = findViewById(R.id.tv_sound);
        etInputChatText = findViewById(R.id.et_input_chat_text);
        ivChatTakePhoto = findViewById(R.id.iv_chat_take_photo);
        ivChatSelectImage = findViewById(R.id.iv_chat_select_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chat_more_page:
                jumpChatDetail();
                break;
            case R.id.iv_chat_tittle_back:
                finish();
                break;
            case R.id.iv_record_voice:
                //按下显示录音按钮
                showRecordBtn();
                break;
            case R.id.iv_chat_take_photo:
                takePhotoPath = TakePhotoUtil.openCamera(this, SELECT_CAMERA_CODE);
                break;
            case R.id.iv_chat_select_image:
                sendImageMsg();
                break;
            default:
                break;
        }
    }

    private void showRecordBtn() {
        if (tvSound.getVisibility() == View.VISIBLE) {
            tvSound.setVisibility(View.INVISIBLE);
            etInputChatText.setVisibility(View.VISIBLE);
        } else {
            tvSound.setVisibility(View.VISIBLE);
            etInputChatText.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 发送图片消息
     */
    private void sendImageMsg() {
        ImagePicker.getInstance()
                //设置标题
                .setTitle("选择图片")
                //设置是否显示拍照按钮
                .showCamera(true)
                //设置是否展示图片
                .showImage(true)
                //设置是否展示视频
                .showVideo(true)
                //设置图片视频不能同时选择
                .setSingleType(true)
                //设置最大选择图片数目(默认为1，单选)
                .setMaxCount(1)
                //设置自定义图片加载器
                .setImageLoader(new GlideLoader())
                .start(this, SELECT_IMAGE_CODE);
    }

    private void jumpChatDetail() {
//        Intent intent = new Intent(this, ChatDetailActivity.class);
//        intent.putExtra("user_info", userId);
//        startActivityForResult(intent, MESSAGE_NO_DISTURB);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == MESSAGE_NO_DISTURB) {
                //免打扰的回调
                boolean isChecked = data.getBooleanExtra("isChecked", false);
                if (isChecked) {
                    ivChatMessageNoDisturb.setVisibility(View.VISIBLE);
                } else {
                    ivChatMessageNoDisturb.setVisibility(View.INVISIBLE);
                }
            } else if (requestCode == SELECT_IMAGE_CODE) {
                //获取选择器返回的数据
                selectedImgPath = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
                if (selectedImgPath == null) {
                    return;
                }
                Log.e("打印图片路径", selectedImgPath.get(0));
                if (MediaFileUtil.isVideoFileType(selectedImgPath.get(0))) {
                    //视频文件
                    File videoFile = new File(selectedImgPath.get(0));
                    try {
                        VideoContent videoContent = new VideoContent(null, null, videoFile, videoFile.getName(), 10000);
                        final Message videoMessage = conversation.createSendMessage(videoContent);
                        JMessageClient.sendMessage(videoMessage);
                        videoMessage.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    //视频发送成功
                                    Toast.makeText(PrivateChatActivity.this, "视频发送成功", Toast.LENGTH_LONG).show();
                                    allMessage.add(videoMessage);
                                    chatMessageAdapter.notifyDataSetChanged();
                                    rvChatList.scrollToPosition(allMessage.size() - 1);
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                  /*  try {
                        Message videoMessage = JMessageClient.createSingleVideoMessage(userId.getUser_nicename(), "da30797404e2e34568f917f5", null, "jpeg", videoFile, videoFile.getName(), 1000);
                        JMessageClient.sendMessage(videoMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/


                } else {
                    //图片
                    sendImageMethod(selectedImgPath.get(0));
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == SELECT_CAMERA_CODE) {
            //点击拍照的回调
            sendImageMethod(takePhotoPath);
        }
    }

    private void sendImageMethod(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //发送图片
        ImageContent.createImageContentAsync(bitmap, new ImageContent.CreateImageContentCallback() {
            @Override
            public void gotResult(int i, String s, ImageContent imageContent) {
                startSendMsg(imageContent);
            }
        });
    }

    private void startSendMsg(final ImageContent imageContent) {
        final Message imgMessage = conversation.createSendMessage(imageContent);
        JMessageClient.sendMessage(imgMessage);
        imgMessage.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    //发送成功
                    Toast.makeText(PrivateChatActivity.this, "图片发送成功", Toast.LENGTH_LONG).show();
                    allMessage.add(imgMessage);
                    chatMessageAdapter.notifyDataSetChanged();
                    rvChatList.scrollToPosition(allMessage.size() - 1);
                }
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEND) {
            //点击了键盘的发送按键
            String sendMsg = etInputChatText.getText().toString().trim();
            if (TextUtils.isEmpty(sendMsg)) {
                Toast.makeText(this, "请输入消息再点击发送", Toast.LENGTH_LONG).show();
            } else {
                TextContent textContent = new TextContent(sendMsg);
                final Message message = conversation.createSendMessage(textContent);
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            //发送成功
                            etInputChatText.setText("");
                            //添加消息到集合
                            allMessage.add(message);
                            chatMessageAdapter.notifyDataSetChanged();
                            rvChatList.scrollToPosition(allMessage.size() - 1);
                        } else {
                            Toast.makeText(PrivateChatActivity.this, s, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                JMessageClient.sendMessage(message);
            }
        }
        return false;
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
                    chatMessageAdapter.notifyDataSetChanged();
                    rvChatList.scrollToPosition(allMessage.size() - 1);
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
        List<Message> newMessageList = event.getOfflineMessageList();
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
        if (newMessageList.size() > 0) {
            allMessage.addAll(newMessageList);
            chatMessageAdapter.notifyDataSetChanged();
            rvChatList.scrollToPosition(allMessage.size() - 1);
        }
    }
}
