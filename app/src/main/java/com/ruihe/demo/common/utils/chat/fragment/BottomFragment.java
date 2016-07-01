package com.ruihe.demo.common.utils.chat.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.DateUtil;
import com.ruihe.demo.common.utils.ToastUtil;
import com.ruihe.demo.common.utils.chat.activity.ActivityChat;
import com.ruihe.demo.fragment.BaseFragment;
import com.ruihe.demo.test.SPUtils;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 描述：聊天界面底部
 * Created by ruihe on 2016/6/30.
 */
public class BottomFragment extends BaseFragment implements View.OnClickListener {


    private Button btPressSpeak;


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        btPressSpeak = (Button) view.findViewById(R.id.btn_press_speak);
        btPressSpeak.setOnClickListener(this);

    }

    @Override
    public int getContentViewId() {
        return R.layout.chat_bottom_content;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_press_speak:

                String userId = SPUtils.getInstance().getString(SPUtils.CURRENT_USER_ID, "").equals("1001") ? "1002" : "1001";

                RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, userId,
                        TextMessage.obtain("当前用户是:" + userId + "\n发送的时间:" + DateUtil.getStringByFormat(System.currentTimeMillis(), "HH:mm:ss")),
                        null, null, new RongIMClient.SendMessageCallback() {
                            @Override
                            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                ToastUtil.show("发送成功");
                            }
                        }, new RongIMClient.ResultCallback<Message>() {
                            @Override
                            public void onSuccess(Message message) {
                                ((ActivityChat) holder).refreshView(message);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });

                break;
            default:
                break;
        }


    }
}
