package com.ruihe.demo.common.utils.chat.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 描述：融云IM工具类
 * Created by ruihe on 2016/6/30.
 */
public class RongIMUtil {


    /**
     * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
     * io.rong.push 为融云 push 进程名称，不可修改。
     */

    public static void initRongIMClient(Context context) {
        if (context.getApplicationInfo().packageName.equals(getCurProcessName(context)) ||
                "io.rong.push".equals(getCurProcessName(context))) {
            RongIMClient.init(context);
            RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                @Override
                public boolean onReceived(Message message, int i) {


                    MessageContent messageContent = message.getContent();

                    if (messageContent instanceof TextMessage) {
                        Log.d("ruihe", "---------收到的消息内容-----------" + ((TextMessage) message.getContent()).getContent());
                    } else if (messageContent instanceof VoiceMessage) {

                    } else if (messageContent instanceof ImageMessage) {

                    }


                    return false;
                }
            });
        }
    }


    /**
     * 当前进程名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
