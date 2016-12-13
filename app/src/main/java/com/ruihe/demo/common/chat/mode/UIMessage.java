package com.ruihe.demo.common.chat.mode;

import android.os.Parcel;
import android.text.SpannableStringBuilder;

import com.ruihe.demo.common.chat.util.AndroidEmoji;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 描述：聊天消息实体
 * Created by ruihe on 2016/6/30.
 */
public class UIMessage extends Message {
    private SpannableStringBuilder textMessageContent;
    private UserInfo mUserInfo;
    private int mProgress;


    public static final Creator<Message> CREATOR = new Creator() {
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public UIMessage() {
    }

    public Message getMessage() {
        Message message = new Message();
        message.setConversationType(this.getConversationType());
        message.setTargetId(this.getTargetId());
        message.setMessageId(this.getMessageId());
        message.setObjectName(this.getObjectName());
        message.setContent(this.getContent());
        message.setSentStatus(this.getSentStatus());
        message.setSenderUserId(this.getSenderUserId());
        message.setReceivedStatus(this.getReceivedStatus());
        message.setMessageDirection(this.getMessageDirection());
        message.setReceivedTime(this.getReceivedTime());
        message.setSentTime(this.getSentTime());
        message.setExtra(this.getExtra());
        return message;
    }

    public static UIMessage obtain(Message message) {
        UIMessage uiMessage = new UIMessage();
        uiMessage.setConversationType(message.getConversationType());
        uiMessage.setTargetId(message.getTargetId());
        uiMessage.setMessageId(message.getMessageId());
        uiMessage.setObjectName(message.getObjectName());
        uiMessage.setContent(message.getContent());
        uiMessage.setSentStatus(message.getSentStatus());
        uiMessage.setSenderUserId(message.getSenderUserId());
        uiMessage.setReceivedStatus(message.getReceivedStatus());
        uiMessage.setMessageDirection(message.getMessageDirection());
        uiMessage.setReceivedTime(message.getReceivedTime());
        uiMessage.setSentTime(message.getSentTime());
        uiMessage.setExtra(message.getExtra());
        uiMessage.setUserInfo(message.getContent().getUserInfo());
        return uiMessage;
    }

    public SpannableStringBuilder getTextMessageContent() {
        if (this.textMessageContent == null && this.getContent() instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) this.getContent();
            if (textMessage.getContent() != null) {
                SpannableStringBuilder spannable = new SpannableStringBuilder(textMessage.getContent());
                AndroidEmoji.ensure(spannable);
                this.setTextMessageContent(spannable);
            }
        }

        return this.textMessageContent;
    }

    public void setTextMessageContent(SpannableStringBuilder textMessageContent) {
        this.textMessageContent = textMessageContent;
    }

    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        if (this.getUserInfo() == null) {
            this.mUserInfo = userInfo;
        }

    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public int getProgress() {
        return this.mProgress;
    }
}
