package com.ruihe.demo.common.utils.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihe.demo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 描述：聊天适配器
 * Created by ruihe on 2016/6/30.
 */
public class AdapterChat extends BaseAdapter {

    private Context mContext;
    private List<Message> mList;

    public AdapterChat(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Message getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Message uiMessage = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            MessageContent messageContent = uiMessage.getContent();

            if (messageContent instanceof TextMessage) {
                switch (uiMessage.getMessageDirection()) {
                    case SEND:
                        convertView = View.inflate(mContext, R.layout.item_chat_right_text_message, null);
                        holder.tvMessageContent = (TextView) convertView.findViewById(R.id.tv_item_message_content);
                        break;
                    case RECEIVE:
                        convertView = View.inflate(mContext, R.layout.item_chat_left_text_message, null);
                        holder.tvMessageContent = (TextView) convertView.findViewById(R.id.tv_item_message_left_content);
                        break;
                    default:
                        break;
                }
            }
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        MessageContent messageContent = uiMessage.getContent();

        holder.tvMessageContent.setText(((TextMessage) messageContent).getContent());

        return convertView;
    }


    public void notifyMessageData(List<Message> messages) {

        if (messages != null) {
            mList.addAll(messages);
            Collections.reverse(mList);
        }
        notifyDataSetChanged();
    }

    public void notifyMessageData(Message message) {

        if (message != null) {
            mList.add(message);
            Collections.reverse(mList);
        }
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView tvMessageContent;

    }


}
