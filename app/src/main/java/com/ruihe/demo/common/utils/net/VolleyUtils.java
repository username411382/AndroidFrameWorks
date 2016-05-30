package com.ruihe.demo.common.utils.net;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ruihe.demo.MyApplication;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.VersionUtils;
import com.ruihe.demo.common.utils.view.RoundedImageView;
import com.ruihe.demo.common.utils.view.TransitionFadeInDrawable;
import com.ruihe.demo.test.SPUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 基于Volley网络请求的工具类
 */
public class VolleyUtils {

    private static final String TAG = "VolleyRequest";

    //图片加载的url
    public static final int TAG_IMAGE_URL = R.id.id_volley_load_image_tag;

    public static ImageCacheUtil bitmapCache = new ImageCacheUtil();// 图片缓存类对象


    //Volley网络请求无tag
    public static void post(RequestQueue queue, final String url, final HashMap<String, String> params,
                            final OnResponseListener listener) {
        post(queue, url, params, listener, null);
    }


    //Volley网络请求有tag
    public static void post(RequestQueue queue, final String url, final HashMap<String, String> params,
                            final OnResponseListener listener, String tag) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "url:" + url);
                logLongInfo("data:" + response);
                if (listener != null) {
                    listener.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (listener != null) {
                    listener.onError(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                if (params != null) {
                    return params;
                } else {
                    return getBaseHttpParams();
                }
            }
        };
        if (tag != null) {
            stringRequest.setTag(tag);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }

    /**
     * 上传单个文件
     */
    public static void uploadSingleFile(String filePath, Map<String, String> params, String url, RequestCallBack<String> callBack) {
        RequestParams requestParams = new RequestParams();
        for (String key : params.keySet()) {
            requestParams.addBodyParameter(key, params.get(key));
        }
        requestParams.addBodyParameter("file", new File(filePath));
        HttpUtils http = new HttpUtils(20 * 1000);
        http.send(HttpRequest.HttpMethod.POST, url, requestParams, callBack);
    }

    /**
     * 只从缓存中取得图片，不会网络请求
     */
    public static boolean loadImageFromCache(String url, final ImageView view, final int defaultImageID) {

        view.setImageResource(defaultImageID);

        //与volley保存时命名对应
        String realUrl = new StringBuilder(url.length() + 12).append("#W").append(0)
                .append("#H").append(0).append("#S").append(ImageView.ScaleType.CENTER_INSIDE.ordinal()).append(url)
                .toString();
        Bitmap bitmap = bitmapCache.getBitmapFromMemoryCache(realUrl);
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
            view.setTag(TAG_IMAGE_URL, url);
            return true;
        } else {
            view.setTag(TAG_IMAGE_URL, null);
            return false;
        }
    }

    /**
     * 加载图片(三级缓存)
     */
    public static void loadImage(RequestQueue queue, String url, final ImageView view, final int defaultImageID) {
        loadImage(queue, url, view, defaultImageID, null);
    }

    /**
     * 加载图片, 有加载回调
     */
    public static void loadImage(RequestQueue queue, final String url, final ImageView view, final int defaultImageID,
                                 final OnLoadImageListener listener) {
        loadImage(queue, url, view, defaultImageID, false, listener);
    }

    /**
     * 加载图片, 有加载回调，可以设置渐变显示
     */
    public static void loadImage(RequestQueue queue, final String url, final ImageView view, final int defaultImageID,
                                 final boolean needFadeIn, final OnLoadImageListener listener) {
        if (!URLUtil.isHttpUrl(url)) {
            view.setImageResource(defaultImageID);
            return;
        }
        String tag = (String) view.getTag(TAG_IMAGE_URL);
        if (tag != null) {
            if (tag.equals(url)) {
                return;
            } else {
                queue.cancelAll(tag);
            }
        }
        view.setTag(TAG_IMAGE_URL, url);

        ImageLoader imageLoader = new ImageLoader(queue, bitmapCache);
        if (listener != null) {
            listener.onStart();
        }

        imageLoader.get(url, new ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (defaultImageID != 0) {
                    view.setImageResource(defaultImageID);
                }
                if (listener != null) {
                    listener.onError(error);
                }
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    if (view.getTag(TAG_IMAGE_URL) == null || view.getTag(TAG_IMAGE_URL).equals(url)) {
                        if (needFadeIn) {
                            //view.setImageBitmap(response.getBitmap());
                            if (defaultImageID == 0) {
                                setImageBitmapFadeIn(view, response.getBitmap(), new ColorDrawable(Color.TRANSPARENT));
                            } else {
                                setImageBitmapFadeIn(view, response.getBitmap(), view.getResources().getDrawable(defaultImageID));
                            }
                        } else {
                            view.setImageBitmap(response.getBitmap());
                        }
                    }
                    if (listener != null) {
                        listener.onSuccess(response, isImmediate);
                    }
                } else if (defaultImageID != 0) {
                    view.setImageResource(defaultImageID);
                }

            }
        });
    }

    /**
     * 渐变加载bitmap到imageView
     */
    private static void setImageBitmapFadeIn(ImageView imageView, Bitmap bitmap, Drawable defaultDrawable) {
        if (!(imageView instanceof RoundedImageView)) {
            TransitionFadeInDrawable.setImageBitmap(imageView, bitmap, defaultDrawable);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public interface OnLoadImageListener {
        void onStart();

        void onSuccess(ImageContainer response, boolean isImmediate);

        void onError(VolleyError error);
    }

    public interface OnResponseListener {
        void onSuccess(String response);

        void onError(VolleyError error);
    }


    /**
     * 每次请求网络必须携带的数据
     */
    public static HashMap<String, String> getBaseHttpParams() {
        HashMap<String, String> pa = new HashMap<>();

       /* pa.put("sys_v", Build.VERSION.SDK_INT + "");// 客户端的系统版本(version)
        pa.put("sys_p", "a");// 手机系统平台(platform)，如果为`a`，表示android，如果是`i`，表示ios
        pa.put("sys_m", Build.MODEL);// 手机的型号(model)
        pa.put("sys_c", "1111");// 渠道id
        pa.put("cli_v", VersionUtils.getVersionName(MyApplication.getInstance()));// 客户端安装的应用的版本
         String sess = SPUtils.getInstance().getString(SPUtils.USER_SESS, "");
		pa.put("sess", sess);// 加密的会话id*/
        pa.put("v", VersionUtils.getVersionName(MyApplication.getInstance()));
        pa.put("sys", "a");
        String sess = SPUtils.getInstance().getString(SPUtils.SESS, "");
        pa.put("sess", sess);
        return pa;
    }

    /**
     * 分段输出超过logcat最大长度的log
     */
    public static void logLongInfo(String str) {
        if (str.length() > 4000) {
            Log.i(TAG, str.substring(0, 4000));
            logLongInfo(str.substring(4000));
        } else {
            Log.i(TAG, str);
        }
    }

}
