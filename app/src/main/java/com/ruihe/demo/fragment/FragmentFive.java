package com.ruihe.demo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.activity.ActivityAnimation;
import com.ruihe.demo.activity.ActivityCreateVcf;
import com.ruihe.demo.activity.ActivityRandom;
import com.ruihe.demo.activity.ActivityRecycleLoadMore;
import com.ruihe.demo.activity.ActivityScanCode;
import com.ruihe.demo.activity.ActivityShowCustomView;
import com.ruihe.demo.activity.ActivityMain;
import com.ruihe.demo.activity.ActivityPay;
import com.ruihe.demo.activity.ActivityPullToRefresh;
import com.ruihe.demo.activity.ActivityShoppingCart;
import com.ruihe.demo.common.utils.Constants;
import com.ruihe.demo.common.chat.activity.ActivityChat;
import com.ruihe.demo.common.utils.SPUtils;
import com.ruihe.demo.common.view.FlowLayout;

/**
 * 描述：首页五
 * Created by ruihe on 2016/4/28.
 */
public class FragmentFive extends BaseFragment implements View.OnClickListener {

    private TextView tvShoppingCar;
    private TextView tvContactSort;
    private FlowLayout layFlow;

    @Override
    public void onAttached() {
    }

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {
        layFlow = view.findViewById(R.id.lay_flow);
        view.findViewById(R.id.tv_shopping_car).setOnClickListener(this);
        view.findViewById(R.id.tv_load_more).setOnClickListener(this);
        view.findViewById(R.id.tv_scan_code).setOnClickListener(this);
        view.findViewById(R.id.tv_random).setOnClickListener(this);
        view.findViewById(R.id.tv_vcf).setOnClickListener(this);
        bindData();
    }



    @Override
    public int getContentViewId() {
        return R.layout.fragment_five;
    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_fifth);

    }


    @Override
    public void onDetached() {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_shopping_car:
                ActivityShoppingCart.redirectToActivity(holder);
                break;
            case R.id.tv_sort:
                holder.redirectToActivity(holder, ActivityAnimation.class);
                break;
            case R.id.tv_custom_pull_to_refresh:
                holder.redirectToActivity(holder, ActivityPullToRefresh.class);
                break;
            case R.id.tv_first_user:
                SPUtils.getInstance().putString(SPUtils.CURRENT_USER_ID, "1001");
                ((ActivityMain) holder).connectRongIM(Constants.userTokens[0]);
                holder.redirectToActivity(holder, ActivityChat.class);
                break;
            case R.id.tv_second_user:
                SPUtils.getInstance().putString(SPUtils.CURRENT_USER_ID, "1002");
                ((ActivityMain) holder).connectRongIM(Constants.userTokens[1]);
                holder.redirectToActivity(holder, ActivityChat.class);
                break;

            case R.id.tv_custom_view://自定义View
                holder.redirectToActivity(holder, ActivityShowCustomView.class);
                break;

            case R.id.tv_pay_view:
                holder.redirectToActivity(holder, ActivityPay.class);
                break;
            case R.id.tv_load_more://加载更多
                holder.redirectToActivity(holder, ActivityRecycleLoadMore.class);
                break;
            case R.id.tv_scan_code://扫码
                ActivityScanCode.redirectToActivity(holder);
                break;
            case R.id.tv_random://随机事件
                ActivityRandom.redirectToActivity(holder);
                break;
            case R.id.tv_vcf://转vcf
                holder.redirectToActivity(holder, ActivityCreateVcf.class);
                break;
            default:
                break;

        }

    }
}
