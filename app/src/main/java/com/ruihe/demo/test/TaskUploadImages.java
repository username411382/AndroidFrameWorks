package com.ruihe.demo.test;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.ruihe.demo.common.utils.Constants;
import com.ruihe.demo.common.utils.net.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskUploadImages {

    private RequestQueue mQueue;
    private OnUploadImagesListener mOnUploadImagesListener;

    private List<String> mImagePaths;
    private List<String> mImageUrls;

    private String mUpyunPath;
    private String mUpyunPolicy;
    private String mUpyunSignature;
    private boolean mIsCanceling;

    public interface OnUploadImagesListener {
        void onGetUpYunSecretSuccess(String upyunPolicy, String upyunSignature);

        void onGetUpYunSecretFail();

        void onSingleSuccess(int size, int index, String url);

        void onSuccess(List<String> imageUrls);

        void onUploadImageError(String message);

        void onCancelSuccess();
    }

    public TaskUploadImages(RequestQueue queue) {
        mQueue = queue;
    }

    public void cancel() {
        mIsCanceling = true;
    }

    public void startUpload(List<String> imagePaths, String upyunPath, OnUploadImagesListener onUploadImagesListener) {
        mImagePaths = imagePaths;
        mUpyunPath = upyunPath;
        mOnUploadImagesListener = onUploadImagesListener;

        mIsCanceling = false;
        mImageUrls = new ArrayList<>();
        getUpyunSecret();
    }

    public void startUpload(String imagePath, String upyunPath, OnUploadImagesListener onUploadImagesListener) {
        List<String> images = new ArrayList<>();
        images.add(imagePath);
        startUpload(images, upyunPath, onUploadImagesListener);
    }

    /**
     * 取得upyun的key
     */
    private void getUpyunSecret() {

        HashMap<String, String> pa = VolleyUtils.getBaseHttpParams();
        pa.put("path", mUpyunPath);

        VolleyUtils.post(mQueue, Constants.URL_ROOT + "/app/get-upyun", pa, new VolleyUtils.OnResponseListener() {

            @Override
            public void onSuccess(String response) {
                try {
                    ItemResponseBase base = ItemResponseBase.parserBaseResponse(response);
                    if (base.cn == 0) {
                        mUpyunPolicy = base.data.getString("policy");
                        mUpyunSignature = base.data.getString("signature");
                        if (mOnUploadImagesListener != null) {
                            mOnUploadImagesListener.onGetUpYunSecretSuccess(mUpyunPolicy, mUpyunSignature);
                        }
                        uploadImage(0);
                    } else {
                        if (mOnUploadImagesListener != null) {
                            mOnUploadImagesListener.onGetUpYunSecretFail();
                        }
                    }
                } catch (JSONException e) {
                    if (mOnUploadImagesListener != null) {
                        mOnUploadImagesListener.onGetUpYunSecretFail();
                    }
                }

            }

            @Override
            public void onError(VolleyError error) {
                if (mOnUploadImagesListener != null) {
                    mOnUploadImagesListener.onGetUpYunSecretFail();
                }
            }
        });
    }

    /**
     * 单独上传图片接口
     */
    private void uploadImage(final int index) {
        if (mIsCanceling) {
            mIsCanceling = false;
            if (mOnUploadImagesListener != null) {
                mOnUploadImagesListener.onCancelSuccess();
            }
            return;
        }

        if (index == mImagePaths.size()) {
            if (mOnUploadImagesListener != null) {
                mOnUploadImagesListener.onSuccess(mImageUrls);
            }
            return;
        }

        HashMap<String, String> pa = new HashMap<>();
        pa.put("policy", mUpyunPolicy);
        pa.put("signature", mUpyunSignature);
        VolleyUtils.uploadSingleFile(FileUtils.getSmallFile(mImagePaths.get(index)), pa,
                "http://v0.api.upyun.com/fitone", new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException e, String arg1) {
                        e.printStackTrace();
                        if (mOnUploadImagesListener != null) {
                            mOnUploadImagesListener.onUploadImageError(arg1);
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        if (mIsCanceling) {
                            mIsCanceling = false;
                            if (mOnUploadImagesListener != null) {
                                mOnUploadImagesListener.onCancelSuccess();
                            }
                            return;
                        }

                        try {
                            VolleyUtils.logLongInfo(arg0.result);
                            JSONObject object = new JSONObject(arg0.result);
                            if (object.getInt("code") == 200) {
                                String url = "http://fitimg.yunimg.cn/" + object.getString("url");
                                if (mOnUploadImagesListener != null) {
                                    mOnUploadImagesListener.onSingleSuccess(mImagePaths.size(), index, url);
                                }
                                mImageUrls.add(url);
                                uploadImage(index + 1);
                            } else {
                                if (mOnUploadImagesListener != null) {
                                    mOnUploadImagesListener.onUploadImageError(object.getString("message"));
                                }
                            }

                        } catch (JSONException e) {
                            if (mOnUploadImagesListener != null) {
                                mOnUploadImagesListener.onUploadImageError("解析图片失败");
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }

}
