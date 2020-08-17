package com.ruihe.demo.common.chat.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;

import com.ruihe.demo.common.chat.mode.UIMessage;

import io.rong.imlib.model.MessageContent;

/**
 * 描述：基类
 * Created by ruihe on 2016/7/1.
 */
public interface IContainerItemProvider<T extends Parcelable> {
    View newView(Context var1, ViewGroup var2);

    void bindView(View var1, int var2, T var3);

    public interface ConversationProvider<T extends Parcelable> extends IContainerItemProvider<T> {
        String getTitle(String var1);

        Uri getPortraitUri(String var1);
    }

    public abstract static class MessageProvider<K extends MessageContent> implements IContainerItemProvider<UIMessage> {
        public MessageProvider() {
        }

        public final void bindView(View v, int position, UIMessage data) {
            this.bindView(v, position, (K) data.getContent(), data);
        }

        public abstract void bindView(View var1, int var2, K var3, UIMessage var4);

        public final Spannable getSummary(UIMessage data) {
            return this.getContentSummary((K) data.getContent());
        }

        public abstract Spannable getContentSummary(K var1);

        public abstract void onItemClick(View var1, int var2, K var3, UIMessage var4);

        public abstract void onItemLongClick(View var1, int var2, K var3, UIMessage var4);
    }
}
