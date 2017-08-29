package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.activity.ActivityAnimation;
import com.ruihe.demo.activity.ActivityDragView;
import com.ruihe.demo.activity.ActivityMain;
import com.ruihe.demo.activity.ActivityPay;
import com.ruihe.demo.activity.ActivityPullToRefresh;
import com.ruihe.demo.activity.ActivityShoppingCart;
import com.ruihe.demo.common.utils.Constants;
import com.ruihe.demo.common.chat.activity.ActivityChat;
import com.ruihe.demo.common.utils.SPUtils;

/**
 * 描述：首页五
 * Created by ruihe on 2016/4/28.
 */
public class FragmentFive extends BaseFragment implements View.OnClickListener {


    private TextView tvShoppingCar;
    private TextView tvContactSort;

    @Override
    public void onAttached() {

    }


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {


        tvShoppingCar = (TextView) view.findViewById(R.id.tv_shopping_car);
        tvShoppingCar.setOnClickListener(this);

        tvContactSort = (TextView) view.findViewById(R.id.tv_sort);
        tvContactSort.setOnClickListener(this);

        view.findViewById(R.id.tv_custom_pull_to_refresh).setOnClickListener(this);
        view.findViewById(R.id.tv_first_user).setOnClickListener(this);
        view.findViewById(R.id.tv_second_user).setOnClickListener(this);
        view.findViewById(R.id.tv_custom_view).setOnClickListener(this);
        view.findViewById(R.id.tv_pay_view).setOnClickListener(this);

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
                holder.redirectToActivity(holder, ActivityDragView.class);
                break;

            case R.id.tv_pay_view:
                holder.redirectToActivity(holder, ActivityPay.class);
                break;
            default:
                break;

        }

    }
}
