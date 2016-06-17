package com.ruihe.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ruihe.demo.R;
import com.ruihe.demo.activity.ActivityPatrolTrace;
import com.ruihe.demo.bean.ItemSecurityTask;
import com.ruihe.demo.common.utils.Constants;
import com.ruihe.demo.common.utils.DateUtil;
import com.ruihe.demo.common.utils.ToastUtil;
import com.ruihe.demo.common.utils.json.JsonParserBase;
import com.ruihe.demo.common.utils.net.VolleyUtils;
import com.ruihe.demo.test.ItemResponseBase;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述：安保任务的适配器
 * Created by ruihe on 2016/5/17.
 */
public class AdapterSecurityTask extends BaseAdapter {


    private Context mContext;
    private List<ItemSecurityTask> mItems;
    private OnStartSecurityTaskListener mStartSecurity;
    private int mRefreshPosition = -1;
    private RequestQueue mQueue;


    public AdapterSecurityTask(Context context, List<ItemSecurityTask> listData) {
        mItems = new ArrayList<>();
        mItems = listData;
        mContext = context;
        mStartSecurity = new OnStartSecurityTaskListener();
        mQueue = Volley.newRequestQueue(mContext);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ItemSecurityTask getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_security_task, null);
            holder.tvSecurityTaskName = (TextView) convertView.findViewById(R.id.tv_task_name);
            holder.tvSecurityTaskTime = (TextView) convertView.findViewById(R.id.tv_task_time);
            holder.tvSecurityTaskMonthCount = (TextView) convertView.findViewById(R.id.tv_task_count);
            holder.btnStartSecurityTask = (Button) convertView.findViewById(R.id.btn_start_security_task);
            holder.itemTopView = convertView.findViewById(R.id.item_top_view);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemSecurityTask itemSecurityTask = mItems.get(position);
        holder.itemTopView.setVisibility(View.GONE);
        if (position == 0) {
            holder.itemTopView.setVisibility(View.VISIBLE);
        }

        holder.btnStartSecurityTask.setTag(position);
        holder.btnStartSecurityTask.setOnClickListener(mStartSecurity);

        if (mRefreshPosition == position) {
            holder.btnStartSecurityTask.setVisibility(View.VISIBLE);
            mRefreshPosition = -1;
        } else {
            holder.btnStartSecurityTask.setVisibility(View.INVISIBLE);
        }

        holder.tvSecurityTaskName.setText(itemSecurityTask.securityTaskName);
        String taskTime = DateUtil.getTimeByOutput((double) itemSecurityTask.lastPatrolTime);
        holder.tvSecurityTaskTime.setText(taskTime);
        if (taskTime.contains("发布")) {
            holder.tvSecurityTaskTime.setText(taskTime.substring(0, taskTime.length() - 2));
        }
        holder.tvSecurityTaskMonthCount.setText(itemSecurityTask.currentMothCount + "次");

        return convertView;
    }

    private class OnStartSecurityTaskListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            startSecurityTask(position);
        }
    }

    private void startSecurityTask(final int startPosition) {
        final String securityName = mItems.get(startPosition).securityTaskName;
        final int securityTaskId = mItems.get(startPosition).taskId;
        final int merchantId = mItems.get(startPosition).merchantId;
        final int serviceId = mItems.get(startPosition).serviceId;

        HashMap<String, String> params = VolleyUtils.getBaseHttpParams();
        params.put("merchantId", merchantId + "");
        params.put("serviceId", serviceId + "");
        params.put("taskId", securityTaskId + "");

        VolleyUtils.post(mQueue, Constants.URL_ROOT, params, new VolleyUtils.OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    ItemResponseBase itemResponseBase = ItemResponseBase.parserBaseResponse(response);
                    if (itemResponseBase.cn == 0) {
                        int recordId = JsonParserBase.getInt(itemResponseBase.data, "recordId");

                        ActivityPatrolTrace.redirectToActivity(mContext, securityName, securityTaskId, merchantId, serviceId, recordId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.show("任务开始失败,请重试");
            }
        });
    }


    class ViewHolder {
        private TextView tvSecurityTaskName;
        private TextView tvSecurityTaskTime;
        private TextView tvSecurityTaskMonthCount;
        private Button btnStartSecurityTask;
        private View itemTopView;
    }

    public void notifyData(List<ItemSecurityTask> listData) {
        if (listData != null && listData.size() > 0) {
            mItems.clear();
            mItems.addAll(listData);
        }
        super.notifyDataSetChanged();
    }

    public void notifyMoreData(List<ItemSecurityTask> listData) {
        if (listData != null && listData.size() > 0) {
            mItems.addAll(listData);
        }
        super.notifyDataSetChanged();
    }


    public void refreshItem(int position) {
        mRefreshPosition = position;
        super.notifyDataSetChanged();
    }


}
