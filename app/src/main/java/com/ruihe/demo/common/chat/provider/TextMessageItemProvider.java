package com.ruihe.demo.common.chat.provider;

import android.content.Context;
import android.os.Parcelable;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;

import com.ruihe.demo.common.chat.mode.UIMessage;

import io.rong.imlib.model.MessageContent;

/**
 * 描述：
 * Created by ruihe on 2016/7/1.
 */
public class TextMessageItemProvider extends IContainerItemProvider.MessageProvider {
    @Override
    public void bindView(View var1, int var2, MessageContent var3, UIMessage var4) {

    }

    @Override
    public Spannable getContentSummary(MessageContent var1) {
        return null;
    }

    @Override
    public void onItemClick(View var1, int var2, MessageContent var3, UIMessage var4) {

    }

    @Override
    public void onItemLongClick(View var1, int var2, MessageContent var3, UIMessage var4) {

    }

    @Override
    public View newView(Context var1, ViewGroup var2) {
        return null;
    }

    @Override
    public void bindView(View var1, int var2, Parcelable var3) {

    }
}
