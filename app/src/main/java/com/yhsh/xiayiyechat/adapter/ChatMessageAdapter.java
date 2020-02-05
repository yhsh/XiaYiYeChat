package com.yhsh.xiayiyechat.adapter;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yhsh.xiayiyechat.bean.JgMessageBean;
import com.yhsh.xiayiyechat.MyApplication;
import com.yhsh.xiayiyechat.R;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

/**
 * @author 下一页5
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {
    private List<JgMessageBean> allMessage;
    private String avatar;
    private Message message;

    public ChatMessageAdapter(List<JgMessageBean> allMessage, String avatar) {
        this.allMessage = allMessage;
        this.avatar = avatar;
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
        message = allMessage.get(position).getMessage();
        MessageContent allContent = message.getContent();
        if (message.getDirect() == MessageDirect.receive) {
            //拿到接收的消息
            setMsgType(holder, allContent, holder.tvContentReceive, holder.ivReceiveMessageImg, holder.ivReceiveMessageVideo, holder.ivReceiveMessageVoice, holder.ivReceiveMessageVideoPlay, holder.rlReceiveVideo, holder.llReceiveVoiceContent, holder.tvReceiveVoiceLength);
            Glide.with(holder.itemView.getContext()).load(avatar).into(holder.ivReceiveHead);
        } else {
            //拿到发送的消息
            setMsgType(holder, allContent, holder.tvContentSend, holder.ivSendMessageImg, holder.ivSendMessageVideo, holder.ivSendMessageVoice, holder.ivSendMessageVideoPlay, holder.rlSendVideo, holder.llSendVoiceContent, holder.tvSendVoiceLength);
            //取出个人头像地址
            String headUrl = "http://image.biaobaiju.com/uploads/20180122/22/1516629810-pnBjHQktMi.jpg";
            Glide.with(holder.itemView.getContext()).load(headUrl).into(holder.ivSendHead);
            if (allMessage.get(position).isShowBar()) {
                holder.pbSendBar.setVisibility(View.VISIBLE);
            } else {
                holder.pbSendBar.setVisibility(View.GONE);
            }
        }
    }

    private void setMsgType(@NonNull final MessageViewHolder holder, final MessageContent allContent, TextView tvContent, ImageView ivMessageImg, final ImageView ivMessageVideo, final ImageView ivMessageVoice, ImageView ivMessagePlay, RelativeLayout rlVideo, final LinearLayout llVoiceContent, TextView tvVoiceLength) {
        tvContent.setVisibility(View.GONE);
        ivMessageImg.setVisibility(View.GONE);
        ivMessageVideo.setVisibility(View.GONE);
        ivMessageVoice.setVisibility(View.GONE);
        ivMessagePlay.setVisibility(View.GONE);
        rlVideo.setVisibility(View.GONE);
        llVoiceContent.setVisibility(View.GONE);
        if (allContent instanceof TextContent) {
            tvContent.setVisibility(View.VISIBLE);
            TextContent text = (TextContent) allContent;
            String receiveMsg = text.getText();
            if (!TextUtils.isEmpty(receiveMsg)) {
                tvContent.setText(receiveMsg);
            }
        } else if (allContent instanceof ImageContent) {
            ivMessageImg.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(((ImageContent) allContent).getLocalPath())) {
                //如果图片不存在再下载
                ((ImageContent) allContent).downloadOriginImage(message, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int i, String s, File file) {
                        Log.e("打印图片下载", i + "==" + s + "---" + file.getPath());
                    }
                });
            }
            ivMessageImg.setImageBitmap(BitmapFactory.decodeFile(((ImageContent) allContent).getLocalThumbnailPath()));
            ivMessageImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageClickListener.showImage(holder.getLayoutPosition());
                }
            });
        } else if (allContent instanceof VideoContent) {
            if (TextUtils.isEmpty(((VideoContent) allContent).getVideoLocalPath())) {
                //视频原文件不存在再下载
                ((VideoContent) allContent).downloadVideoFile(message, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int i, String s, File file) {
                        Log.e("打印视频下载", i + "==" + s + "---" + file.getPath());
                        Glide.with(holder.itemView.getContext()).load(file.getPath()).into(ivMessageVideo);
                    }
                });
            } else {
                Glide.with(holder.itemView.getContext()).load(((VideoContent) allContent).getVideoLocalPath()).into(ivMessageVideo);
            }
            ivMessageVideo.setVisibility(View.VISIBLE);
            ivMessagePlay.setVisibility(View.VISIBLE);
            rlVideo.setVisibility(View.VISIBLE);
            //设置视频的点击事件
            rlVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoClickListener.playVideo(holder.getLayoutPosition());
                }
            });
        } else if (allContent instanceof VoiceContent) {
            if (TextUtils.isEmpty(((VoiceContent) allContent).getLocalPath())) {
                //录音文件不存在再下载录音文件
                ((VoiceContent) allContent).downloadVoiceFile(message, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int status, String desc, File file) {
                        if (status == 0) {
                            Toast.makeText(MyApplication.getMyContext(), "语音消息同步完成", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MyApplication.getMyContext(), "语音文件同步失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            llVoiceContent.setVisibility(View.VISIBLE);
            ivMessageVoice.setVisibility(View.VISIBLE);
            tvVoiceLength.setText(new StringBuffer("'").append(((VoiceContent) allContent).getDuration()).append("'"));
            llVoiceContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    voicePlayListener.playVoice(((VoiceContent) allContent).getLocalPath(), ivMessageVoice);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return allMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = allMessage.get(position).getMessage();
        if (msg.getDirect() == MessageDirect.send) {
            //拿到发送的消息
            return 0;
        } else {
            //拿到接收的消息
            return 1;
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvContentSend;
        private final TextView tvContentReceive;
        private final ImageView ivSendHead;
        private final ImageView ivReceiveHead;
        private final ImageView ivReceiveMessageImg;
        private final ImageView ivReceiveMessageVideo;
        private final ImageView ivReceiveMessageVoice;
        private final ImageView ivSendMessageVoice;
        private final ImageView ivSendMessageVideo;
        private final ImageView ivSendMessageImg;
        private final ImageView ivSendMessageVideoPlay;
        private final ImageView ivReceiveMessageVideoPlay;
        private final RelativeLayout rlReceiveVideo;
        private final RelativeLayout rlSendVideo;
        private final LinearLayout llSendVoiceContent;
        private final LinearLayout llReceiveVoiceContent;
        private final TextView tvReceiveVoiceLength;
        private final TextView tvSendVoiceLength;
        private final ProgressBar pbSendBar;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContentSend = itemView.findViewById(R.id.tv_content_send);
            ivSendHead = itemView.findViewById(R.id.iv_send_head);
            tvContentReceive = itemView.findViewById(R.id.tv_content_receive);
            ivReceiveHead = itemView.findViewById(R.id.iv_receive_head);
            ivSendMessageImg = itemView.findViewById(R.id.iv_send_message_img);
            ivSendMessageVideo = itemView.findViewById(R.id.iv_send_message_video);
            ivSendMessageVideoPlay = itemView.findViewById(R.id.iv_send_message_video_play);
            ivSendMessageVoice = itemView.findViewById(R.id.iv_send_message_voice);
            ivReceiveMessageImg = itemView.findViewById(R.id.iv_receive_message_img);
            ivReceiveMessageVideo = itemView.findViewById(R.id.iv_receive_message_video);
            ivReceiveMessageVideoPlay = itemView.findViewById(R.id.iv_receive_message_video_play);
            ivReceiveMessageVoice = itemView.findViewById(R.id.iv_receive_message_voice);
            rlSendVideo = itemView.findViewById(R.id.rl_send_video);
            rlReceiveVideo = itemView.findViewById(R.id.rl_receive_video);
            llSendVoiceContent = itemView.findViewById(R.id.ll_send_voice_content);
            llReceiveVoiceContent = itemView.findViewById(R.id.ll_receive_voice_content);
            tvSendVoiceLength = itemView.findViewById(R.id.tv_send_voice_length);
            tvReceiveVoiceLength = itemView.findViewById(R.id.tv_receive_voice_length);
            pbSendBar = itemView.findViewById(R.id.pb_send_bar);
        }
    }

    public interface VideoClickListener {
        /**
         * 点击播放视频
         *
         * @param position 播放哪一个视频
         */
        void playVideo(int position);
    }

    VideoClickListener videoClickListener;

    public void setVideoClickListener(VideoClickListener videoClickListener) {
        this.videoClickListener = videoClickListener;
    }

    public interface ImageClickListener {
        /**
         * 查看图片
         *
         * @param position 位置
         */
        void showImage(int position);
    }

    ImageClickListener imageClickListener;

    public void setImageClickListener(ImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public interface VoicePlayListener {
        /**
         * 播放语音的方法
         *
         * @param playUrl        播放语音的路径
         * @param ivMessageVoice
         */
        void playVoice(String playUrl, ImageView ivMessageVoice);
    }

    VoicePlayListener voicePlayListener;

    public void setVoicePlayListener(VoicePlayListener voicePlayListener) {
        this.voicePlayListener = voicePlayListener;
    }
}
