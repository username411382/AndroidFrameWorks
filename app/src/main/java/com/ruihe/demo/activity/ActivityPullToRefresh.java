package com.ruihe.demo.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.DateUtil;
import com.ruihe.demo.common.view.customPullToRefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：自定义下拉刷新
 * Created by ruihe on 2016/6/20.
 */
public class ActivityPullToRefresh extends BaseActivity implements PullToRefreshListView.OnRefreshListener {

    private PullToRefreshListView mListView;
    private List<String> mDatas;
    private static ArrayAdapter<String> mAdapter;
    private final static int REFRESH_COMPLETE = 0;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    mDatas.add(0, "new data" + DateUtil.getStringByFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    mListView.setOnRefreshComplete();
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(0);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public int getViewId() {
        return R.layout.activity_pulltorefresh;
    }

    @Override
    public void onActivityViewCreated() {
        initVariable();
        initView();
        initListener();
        bindData();
    }

    private void initVariable() {
        String[] data = new String[20];
        for (int i = 0; i < 20; i++) {
            data[i] = "Hello Word " + i + "!";
        }
        mDatas = new ArrayList<>(Arrays.asList(data));
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDatas);
    }

    private void initView() {
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
    }

    private void initListener() {
        mListView.setOnRefreshListener(this);
    }

    private void bindData() {
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 3000);
    }
}
