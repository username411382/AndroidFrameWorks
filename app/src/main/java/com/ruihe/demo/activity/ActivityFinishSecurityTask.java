package com.ruihe.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.Constants;
import com.ruihe.demo.common.utils.DensityUtil;
import com.ruihe.demo.common.utils.ToastUtil;
import com.ruihe.demo.common.utils.file.ImageFileUtils;
import com.ruihe.demo.common.utils.net.VolleyUtils;
import com.ruihe.demo.test.ImageUtils;
import com.ruihe.demo.test.ItemResponseBase;
import com.ruihe.demo.test.SPUtils;
import com.ruihe.demo.test.TaskUploadImages;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 描述：鹰眼轨迹的截图
 * Created by ruihe on 2016/5/17.
 */
public class ActivityFinishSecurityTask extends BaseActivity {


    private static String INTENT_TASK_SHOT_IMAGE = "shot_map";
    private static String INTENT_TASK_CONTENT = "task_describe";
    private static String INTENT_TASK_TOTAL_DIATANCE = "total_distance";
    private static String INTTNET_TASK_ID = "task_id";
    private static String INTENT_TASK_RECORD_ID = "task_record_id";
    private static String INTENT_MERCHANT_ID = "merchant_id";
    private static String INTENT_SERVICE_ID = "service_id";


    private Activity mActivity;
    private RequestQueue mQueue;
    private ImageView ivTask;
    private TextView tvTask;

    private Bitmap mBitMap;
    private String mImageUrl;
    private String mTaskContent;
    private int mTaskTotalDistance;
    private int mTaskId;
    private int mTaskRecordId;
    private int mMerchantId;
    private int mServiceId;
    private int mTraceId;
    private String mImageUrlFromUpyun;


    public static void redirectToActivity(Context context, Bitmap bmp, String taskContent, int totalDistance, int taskId, int taskRecordId, int merchantId, int serviceId) {
        Intent intent = new Intent(context, ActivityFinishSecurityTask.class);
        intent.putExtra(INTENT_TASK_SHOT_IMAGE, ImageFileUtils.bitmapToBytesArray(bmp));
        intent.putExtra(INTENT_TASK_CONTENT, taskContent);
        intent.putExtra(INTENT_TASK_TOTAL_DIATANCE, totalDistance);
        intent.putExtra(INTTNET_TASK_ID, taskId);
        intent.putExtra(INTENT_TASK_RECORD_ID, taskRecordId);
        intent.putExtra(INTENT_MERCHANT_ID, merchantId);
        intent.putExtra(INTENT_SERVICE_ID, serviceId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public int getViewId() {
        return R.layout.activity_finish_security_task;
    }

    @Override
    public void onActivityViewCreated() {

        mActivity = this;
        mQueue = Volley.newRequestQueue(mActivity);
        mBitMap = ImageFileUtils.bytesArrayToBitmap(getIntent().getByteArrayExtra(INTENT_TASK_SHOT_IMAGE));
        mTaskContent = getIntent().getStringExtra(INTENT_TASK_CONTENT);
        mTaskTotalDistance = getIntent().getIntExtra(INTENT_TASK_TOTAL_DIATANCE, 0);
        mTaskId = getIntent().getIntExtra(INTTNET_TASK_ID, 0);
        mTaskRecordId = getIntent().getIntExtra(INTENT_TASK_RECORD_ID, 0);
        mMerchantId = getIntent().getIntExtra(INTENT_MERCHANT_ID, 0);
        mServiceId = getIntent().getIntExtra(INTENT_SERVICE_ID, 0);
        mTraceId = SPUtils.getInstance().getInt(SPUtils.USER_ID, 0);


        ivTask = (ImageView) findViewById(R.id.iv_finish_task);
        tvTask = (TextView) findViewById(R.id.tv_finish_task);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivTask.getLayoutParams();
        lp.height = (int) (DensityUtil.getScreenWidth(mActivity) * 0.3f);
        lp.width = (int) (DensityUtil.getScreenWidth(mActivity) * 0.3f);
        ivTask.setLayoutParams(lp);

        bindData();
    }

    private void bindData() {
        mTitleView.removeAllMenu();
        mTitleView.setTitle("完成任务");

        tvTask.setText(getIntent().getStringExtra(INTENT_TASK_CONTENT));
        ivTask.setImageBitmap(mBitMap);

        try {
            mImageUrl = ImageUtils.saveMyBitmap(System.currentTimeMillis() + "", mBitMap);
        } catch (IOException e) {
            mImageUrl = "";
            e.printStackTrace();
        }

        mTitleView.addRightText(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TaskUploadImages(mQueue).startUpload(mImageUrl,
                        "merchant/service/patrol/record/{year}/{mon}/{day}/" + System.currentTimeMillis() + "{random}{.suffix}", new OnUploadSecurityTaskListener());
            }
        }, "提交");


    }


    private class OnUploadSecurityTaskListener implements TaskUploadImages.OnUploadImagesListener {


        @Override
        public void onGetUpYunSecretSuccess(String upyunPolicy, String upyunSignature) {

        }

        @Override
        public void onGetUpYunSecretFail() {

            ToastUtil.show("上传安全巡检图片失败");
        }

        @Override
        public void onSingleSuccess(int size, int index, String url) {
            mImageUrlFromUpyun = url;
            sendSecurityTaskToServer();
        }

        @Override
        public void onSuccess(List<String> imageUrls) {

        }

        @Override
        public void onUploadImageError(String message) {
            ToastUtil.show("上传安全巡检图片失败" + message);
        }

        @Override
        public void onCancelSuccess() {

        }
    }


    private void sendSecurityTaskToServer() {

        HashMap<String, String> params = VolleyUtils.getBaseHttpParams();

        params.put("merchantId", mMerchantId + "");
        params.put("serviceId", mServiceId + "");
        params.put("taskId", mTaskId + "");
        params.put("trackId", mTraceId + "");
        params.put("distance", mTaskTotalDistance + "");
        params.put("image", mImageUrlFromUpyun);
        params.put("content", mTaskContent);
        params.put("recordId", mTaskRecordId + "");

        VolleyUtils.post(mQueue, Constants.URL_ROOT, params, new VolleyUtils.OnResponseListener() {
            @Override
            public void onSuccess(String response) {

                try {
                    ItemResponseBase itemResponseBase = ItemResponseBase.parserBaseResponse(response);
                    if (itemResponseBase.cn == 0) {
                        ToastUtil.show("提交成功");
                        mActivity.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.show("提交失败，请重试");
            }
        });


    }


}
