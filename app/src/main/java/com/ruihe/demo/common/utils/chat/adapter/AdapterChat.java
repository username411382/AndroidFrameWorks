package com.ruihe.demo.common.utils.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.chat.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 描述：聊天适配器
 * Created by ruihe on 2016/6/30.
 */
public class AdapterChat extends BaseAdapter {

    private static final int MESSAGE_TYPE_TEXT_LEFT = 0;
    private static final int MESSAGE_TYPE_TEXT_RIGHT = 1;


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
    public int getItemViewType(int position) {

        MessageContent messageContent = getItem(position).getContent();

        if (messageContent instanceof TextMessage) {
            return getItem(position).getMessageDirection() == Message.MessageDirection.SEND ? MESSAGE_TYPE_TEXT_RIGHT : MESSAGE_TYPE_TEXT_LEFT;
        }
        return -1;
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
            convertView = createItemView(uiMessage);
            if (messageContent instanceof TextMessage) {
                holder.tvMessageContent = (TextView) convertView.findViewById(R.id.tv_item_message_content);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            } else if (messageContent instanceof ImageMessage) {


            } else if (messageContent instanceof VoiceMessage) {

            }

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        MessageContent messageContent = uiMessage.getContent();

        holder.tvMessageContent.setText(((TextMessage) messageContent).getContent());

        long currentTime = uiMessage.getSentTime();
        holder.tvTime.setVisibility(View.VISIBLE);
        holder.tvTime.setText(DateUtil.getTimestampString(new Date(currentTime)));
        if (position > 0 && currentTime - getItem(position - 1).getSentTime() < 60 * 1000) {
            holder.tvTime.setVisibility(View.GONE);
        }

        return convertView;
    }


    private View createItemView(Message message) {

        MessageContent messageContent = message.getContent();
        if (messageContent instanceof TextMessage) {
            return View.inflate(mContext, message.getMessageDirection() == Message.MessageDirection.SEND
                    ? R.layout.item_chat_right_text_message : R.layout.item_chat_left_text_message, null);
        } else if (messageContent instanceof ImageMessage) {

        } else if (messageContent instanceof VoiceMessage) {

        }
        return null;
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
        }
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView tvMessageContent;
        TextView tvTime;

    }


}
