package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ruihe.demo.R;
import com.ruihe.demo.activity.ActivityPatrolTrace;
import com.ruihe.demo.activity.ActivitySecurityTask;
import com.ruihe.demo.common.utils.Constants;
import com.ruihe.demo.common.utils.StringUtils;
import com.ruihe.demo.common.utils.ToastUtils;
import com.ruihe.demo.common.utils.json.JsonParserBase;
import com.ruihe.demo.common.utils.net.VolleyUtils;
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
public class FragmentTwo extends BaseFragment implements View.OnClickListener {


    private TextView tvSecond;

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        tvSecond = (TextView) view.findViewById(R.id.tv_second);
        tvSecond.setOnClickListener(this);


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


    @Override
    public void onClick(View v) {
        // login();
        ActivityPatrolTrace.redirectToActivity(holder, "", 0, 0, 0, 0);
    }

    private void login() {
        RequestQueue mQueue = Volley.newRequestQueue(holder);
        HashMap<String, String> params = VolleyUtils.getBaseHttpParams();
        params.put("username", "13618062160");
        params.put("password", StringUtils.md5("123456"));
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
                                    ToastUtils.show("解析失败");
                                }

                            }

                            @Override
                            public void onError(VolleyError error) {
                                ToastUtils.show("连接失败");
                            }
                        }
                , "tag_login");
    }


    private void parserSuccessData(JSONObject data) throws JSONException {
        ItemUser user = ItemUser.parserUser(data.getJSONObject("user"));
        user.sess = JsonParserBase.getString(data, "sess");
        user.password = StringUtils.md5("123456");
        SPUtils.getInstance().saveCurrentUser(user);
        ActivitySecurityTask.redirectToActivity(holder);
    }


}

