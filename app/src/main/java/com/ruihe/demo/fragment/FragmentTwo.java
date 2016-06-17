package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ruihe.demo.R;
import com.ruihe.demo.activity.ActivityPatrolTrace;
import com.ruihe.demo.activity.ActivitySecurityTask;
import com.ruihe.demo.common.utils.Constants;
import com.ruihe.demo.common.utils.StringUtil;
import com.ruihe.demo.common.utils.ToastUtil;
import com.ruihe.demo.common.utils.json.JsonParserBase;
import com.ruihe.demo.common.utils.net.VolleyUtils;
import com.ruihe.demo.common.utils.view.SlidingSeekBar;
import com.ruihe.demo.test.ItemResponseBase;
import com.ruihe.demo.test.ItemUser;
import com.ruihe.demo.test.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 描述：首页二
 * Created by ruihe on 2016/4/28.
 */
public class FragmentTwo extends BaseFragment implements View.OnClickListener, SlidingSeekBar.OnSeekBarStatusListener {


    private TextView tvSecond;
    private View mView;
    private View viewBg;
    private TextView tvSlideToRightHint;
    private SlidingSeekBar slideSeekBar;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            slideSeekBar.setProgress(0);
            tvSlideToRightHint.setAlpha(1.0f);
            viewBg.setAlpha(0.0f);
        }
    };

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {
        mView = view;
        tvSecond = (TextView) view.findViewById(R.id.tv_second);
        initView();
        initListener();
        bindData();
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_two;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }


    private void initView() {
        viewBg = mView.findViewById(R.id.view_bg);
        tvSlideToRightHint = (TextView) mView.findViewById(R.id.tv_slide_to_right);
        slideSeekBar = (SlidingSeekBar) mView.findViewById(R.id.seek_bar);

    }

    private void initListener() {
        slideSeekBar.initSlideToRightUnlock(holder, tvSlideToRightHint, viewBg, this);
        tvSecond.setOnClickListener(this);
    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_third);

    }


    @Override
    public void onClick(View v) {
        // login();
        ActivityPatrolTrace.redirectToActivity(holder, "", 0, 0, 0, 0);
    }

    private void login() {
        RequestQueue mQueue = Volley.newRequestQueue(holder);
        HashMap<String, String> params = VolleyUtils.getBaseHttpParams();
        params.put("username", "13618062160");
        params.put("password", StringUtil.md5("123456"));
        VolleyUtils.post(mQueue, Constants.URL_ROOT + Constants.API_LOGIN, params, new

                        VolleyUtils.OnResponseListener() {
                            @Override
                            public void onSuccess(String response) {
                                try {
                                    ItemResponseBase base = ItemResponseBase.parserBaseResponse(response);
                                    if (base.cn == 0) {
                                        parserSuccessData(base.data);
                                    }
                                } catch (JSONException e) {
                                    ToastUtil.show("解析失败");
                                }

                            }

                            @Override
                            public void onError(VolleyError error) {
                                ToastUtil.show("连接失败");
                            }
                        }
                , "tag_login");
    }


    private void parserSuccessData(JSONObject data) throws JSONException {
        ItemUser user = ItemUser.parserUser(data.getJSONObject("user"));
        user.sess = JsonParserBase.getString(data, "sess");
        user.password = StringUtil.md5("123456");
        SPUtils.getInstance().saveCurrentUser(user);
        ActivitySecurityTask.redirectToActivity(holder);
    }


    @Override
    public void onFinishUnlock() {
        Toast.makeText(holder, "恭喜，解锁成功！", Toast.LENGTH_SHORT).show();
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }
}

