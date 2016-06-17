package com.ruihe.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ruihe.demo.R;
import com.ruihe.demo.adapter.AdapterSecurityTask;
import com.ruihe.demo.bean.ItemSecurityTask;
import com.ruihe.demo.common.utils.Constants;
import com.ruihe.demo.common.utils.ToastUtil;
import com.ruihe.demo.common.utils.net.VolleyUtils;
import com.ruihe.demo.common.utils.view.FooterListView;
import com.ruihe.demo.test.ItemResponseBase;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述：安保任务
 * Created by ruihe on 2016/5/17.
 */
public class ActivitySecurityTask extends BaseActivity implements AdapterView.OnItemClickListener, FooterListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int DEFAULT_LOAD_COUNT = 20;
    private static final int MSG_REFRESH_SUCCESS = 101;
    private static final int MSG_REFRESH_FAIL = 102;
    private static final int MSG_LOAD_MORE_SUCCESS = 103;
    private static final int MSG_LOAD_MORE_FAIL = 104;
    private static final int MSG_DELAY_TIME = 1500;

    private FooterListView footerLv;
    private SwipeRefreshLayout swipe;
    private View emptyView;
    private AdapterSecurityTask mAdapter;
    private List<ItemSecurityTask> mItems;
    private long mRefreshTime;
    private Activity mActivity;
    private RequestQueue mQueue;


    public static void redirectToActivity(Context context) {
        Intent intent = new Intent(context, ActivitySecurityTask.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case MSG_REFRESH_SUCCESS:
                    swipe.setRefreshing(false);
                    if (mItems == null || mItems.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        swipe.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        swipe.setVisibility(View.VISIBLE);
                    }
                    mAdapter.notifyData(mItems);
                    break;
                case MSG_REFRESH_FAIL:
                    swipe.setRefreshing(false);
                    ToastUtil.show("刷新失败，请重试");
                    break;
                case MSG_LOAD_MORE_SUCCESS:

                    break;
                case MSG_LOAD_MORE_FAIL:

                    break;
                default:
                    break;

            }


        }
    };


    @Override
    public int getViewId() {
        return R.layout.activity_security_task;
    }

    @Override
    public void onActivityViewCreated() {
        mActivity = this;
        mQueue = Volley.newRequestQueue(mActivity);
        mItems = new ArrayList<>();
        mAdapter = new AdapterSecurityTask(mActivity, mItems);

        emptyView = findViewById(R.id.ly_empty_page);
        footerLv = (FooterListView) findViewById(R.id.foot_lv);
        footerLv.setOnItemClickListener(this);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_security_task_list);
        swipe.setColorSchemeResources(R.color.base_blue);
        swipe.setOnRefreshListener(this);
        footerLv.initVariable(DEFAULT_LOAD_COUNT, 3, this, swipe);

        bindData();
    }

    private void bindData() {

        mTitleView.removeAllMenu();
        mTitleView.setTitle("安保任务");
        footerLv.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();
    }

    private void getDataFromServer() {

        HashMap<String, String> params = VolleyUtils.getBaseHttpParams();

        VolleyUtils.post(mQueue, Constants.URL_ROOT, params, new VolleyUtils.OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    ItemResponseBase base = ItemResponseBase.parserBaseResponse(response);
                    if (base.cn == 0) {
                        mItems = ItemSecurityTask.parserSecurityTask(base.data);
                        sendMsgDelay(MSG_REFRESH_SUCCESS);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendMsgDelay(MSG_REFRESH_FAIL);
            }

            @Override
            public void onError(VolleyError error) {
                sendMsgDelay(MSG_REFRESH_FAIL);
            }
        });


    }

    private void getMoreDataFromServer() {


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.refreshItem(position);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClickFooter(FooterListView.FooterStatus status) {

    }

    private void sendMsgDelay(int type) {
        if (System.currentTimeMillis() - mRefreshTime > MSG_DELAY_TIME) {
            mHandler.sendEmptyMessage(type);
        } else {
            mHandler.sendEmptyMessageDelayed(type, MSG_DELAY_TIME - (System.currentTimeMillis() - mRefreshTime));
        }

    }

    @Override
    public void onRefresh() {
        mRefreshTime = System.currentTimeMillis();
        getDataFromServer();
    }
}
