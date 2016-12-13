package com.ruihe.demo.common.chat.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ruihe.demo.R;
import com.ruihe.demo.common.ToastUtil;
import com.ruihe.demo.common.chat.activity.ActivityChat;
import com.ruihe.demo.common.utils.SPUtils;
import com.ruihe.demo.fragment.BaseFragment;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 描述：聊天界面底部
 * Created by ruihe on 2016/6/30.
 */
public class BottomFragment extends BaseFragment implements View.OnClickListener, TextWatcher {


    private Button btPressSpeak;
    private Button btSendTxtMessage;
    private ImageView ivKeyBoard;
    private ImageView ivAddImage;
    private EditText etSpeakSth;

    private boolean isClick;


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        btPressSpeak = (Button) view.findViewById(R.id.btn_press_speak);
        btSendTxtMessage = (Button) view.findViewById(R.id.btn_send_txt_message);
        ivKeyBoard = (ImageView) view.findViewById(R.id.iv_bottom_keyboard);
        ivAddImage = (ImageView) view.findViewById(R.id.iv_bottom_extend);
        etSpeakSth = (EditText) view.findViewById(R.id.et_speak_sth);

        initListener();

    }

    private void initListener() {
        ivKeyBoard.setOnClickListener(this);
        btPressSpeak.setOnClickListener(this);
        btSendTxtMessage.setOnClickListener(this);
        etSpeakSth.addTextChangedListener(this);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(etSpeakSth.getText().toString())) {
            btSendTxtMessage.setVisibility(View.VISIBLE);
            ivAddImage.setVisibility(View.GONE);
            return;
        }
        ivAddImage.setVisibility(View.VISIBLE);
        btSendTxtMessage.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btn_send_txt_message://发送消息

                String userId = SPUtils.getInstance().getString(SPUtils.CURRENT_USER_ID, "").equals("1001") ? "1002" : "1001";

                if (TextUtils.isEmpty(etSpeakSth.getText().toString())) {
                    ToastUtil.show("发送内容不能为空");
                    return;
                }

                RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, userId,
                        // TextMessage.obtain("当前用户是:" + userId + "\n发送的时间:" + DateUtil.getStringByFormat(System.currentTimeMillis(), "HH:mm:ss")),
                        TextMessage.obtain(etSpeakSth.getText().toString()),
                        null, null, new RongIMClient.SendMessageCallback() {
                            @Override
                            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                etSpeakSth.setText("");
                                Log.d("ruihe", "---文本消息发送成功---");
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

            case R.id.btn_press_speak://按住说话
                ToastUtil.show("按住说话");
                break;

            case R.id.iv_bottom_keyboard://软键盘切换
                isClick = !isClick;

                if (!isClick) {
                    ivKeyBoard.setImageResource(R.drawable.selector_chat_voice);
                    etSpeakSth.setVisibility(View.VISIBLE);
                    btPressSpeak.setVisibility(View.GONE);
                    return;
                }
                ivKeyBoard.setImageResource(R.drawable.selector_chat_keyboard);
                btPressSpeak.setVisibility(View.VISIBLE);
                etSpeakSth.setVisibility(View.GONE);

                break;

            default:
                break;
        }


    }


}
