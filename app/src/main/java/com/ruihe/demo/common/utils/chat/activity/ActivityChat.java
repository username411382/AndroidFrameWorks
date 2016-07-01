package com.ruihe.demo.common.utils.chat.activity;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ListView;

import com.ruihe.demo.R;
import com.ruihe.demo.activity.BaseActivity;
import com.ruihe.demo.common.utils.chat.adapter.AdapterChat;
import com.ruihe.demo.common.utils.chat.fragment.BottomFragment;
import com.ruihe.demo.test.SPUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 描述：聊天界面
 * Created by ruihe on 2016/6/30.
 */
public class ActivityChat extends BaseActivity implements RongIMClient.OnReceiveMessageListener {


    private static final int MESSAGE_REFRESH = 1001;


    private ListView listView;


    private Activity mActivity;
    private FragmentManager mFragmentManager;
    public AdapterChat mAdapter;
    private List<Message> mItems;
    private Message mTempMessage;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);


            switch (msg.what) {

                case MESSAGE_REFRESH:

                    if (mTempMessage != null) {
                        mAdapter.notifyMessageData(mTempMessage);
                        mTempMessage = null;
                        return;
                    }
                    mAdapter.notifyMessageData(mItems);
                    break;
                default:
                    break;
            }


        }
    };


    @Override
    public int getViewId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onActivityViewCreated() {
        initVariable();
        initView();
        initListener();
        bindData();
    }


    private void initVariable() {
        mActivity = this;
        mFragmentManager = getSupportFragmentManager();
        mAdapter = new AdapterChat(mActivity);
        mItems = new ArrayList<>();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.chat_lv_content);
        mFragmentManager.beginTransaction().add(R.id.bottom_content, new BottomFragment()).commitAllowingStateLoss();
    }

    private void initListener() {
        RongIMClient.setOnReceiveMessageListener(this);
    }

    private void bindData() {
        String titleId = SPUtils.getInstance().getString(SPUtils.CURRENT_USER_ID, "").equals("1001") ? "1002" : "1001";
        mTitleView.setTitle(titleId);


        listView.setAdapter(mAdapter);

        RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, titleId, 50, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                mItems = messages;
                Log.d("ruihe", "-----获取的消息数-----" + messages.size());
                mHandler.sendEmptyMessage(MESSAGE_REFRESH);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("ruihe", "获取历史消息失败");
            }
        });

    }


    public void refreshView(Message message) {
        mTempMessage = message;
        mHandler.sendEmptyMessage(MESSAGE_REFRESH);
    }


    @Override
    public boolean onReceived(Message message, int i) {

        mTempMessage = message;

        MessageContent messageContent = message.getContent();
        if (messageContent instanceof TextMessage) {
            Log.d("ruihe", "---------聊天消息内容-----------" + ((TextMessage) message.getContent()).getContent());
            mHandler.sendEmptyMessage(MESSAGE_REFRESH);
        } else if (messageContent instanceof VoiceMessage) {

        } else if (messageContent instanceof ImageMessage) {

        }
        return false;
    }
}
