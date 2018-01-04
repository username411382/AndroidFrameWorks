package com.ruihe.demo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.adapter.TabMenuAdapter;
import com.ruihe.demo.adapter.TabMenuAdapter.TabMenuColumn;
import com.ruihe.demo.adapter.TabPackageAdapter;
import com.ruihe.demo.bean.ItemTabColumn;
import com.ruihe.demo.common.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：首页四
 * Created by ruihe on 2016/4/28.
 */
public class FragmentFour extends BaseFragment {

    private static final int TAB_PACKAGE_NAME = 100;
    private static final int TAB_MENU_NAME = 101;


    private TextView tvTabHeadFoodName;
    private TextView tvTabHeadFoodPrice;
    private TextView tvTabHeadFoodCount;
    private TextView tvTabHeadFoodTotal;
    private TextView tvTabHeadPackageContent;
    private LinearLayout lyHead;


    private RecyclerView ryPackage;
    private RecyclerView ryFood;
    private TabMenuAdapter mAdapter;
    private List<ItemTabColumn> mMenuLists;
    private TabPackageAdapter mPackageAdapter;
    private List<ItemTabColumn> mPackageLists;
    private OnRecycleViewScrollListener mOnScrollistener;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case TAB_MENU_NAME:
                    mAdapter.notifyData(ItemTabColumn.getTabFood());
                    break;
                case TAB_PACKAGE_NAME:
                    mPackageAdapter.notifyData(ItemTabColumn.getTabPackage());
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onAttached() {
    }

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        tvTabHeadPackageContent = view.findViewById(R.id.tv_package_content);
        tvTabHeadFoodName = view.findViewById(R.id.tv_tab_head_food_name);
        tvTabHeadFoodPrice = view.findViewById(R.id.tv_tab_head_food_price);
        tvTabHeadFoodCount = view.findViewById(R.id.tv_tab_head_food_count);
        tvTabHeadFoodTotal = view.findViewById(R.id.tv_tab_head_food_total);
        lyHead = view.findViewById(R.id.ly_head);

        LayoutParams ly = (LayoutParams) lyHead.getLayoutParams();
        ly.width = (DensityUtil.getScreenWidth(holder) - DensityUtil.dip2px(holder, 1)) * 11 / 14;
        lyHead.setLayoutParams(ly);

        TabMenuAdapter.setTabHeadLayoutParams(holder, tvTabHeadPackageContent, TabMenuColumn.TAB_COLUMN_PACKAGE);
        TabMenuAdapter.setTabHeadLayoutParams(holder, tvTabHeadFoodName, TabMenuColumn.TAB_COLUMN_NAME);
        TabMenuAdapter.setTabHeadLayoutParams(holder, tvTabHeadFoodPrice, TabMenuColumn.TAB_COLUMN_PRICE);
        TabMenuAdapter.setTabHeadLayoutParams(holder, tvTabHeadFoodCount, TabMenuColumn.TAB_COLUMN_COUNT);
        TabMenuAdapter.setTabHeadLayoutParams(holder, tvTabHeadFoodTotal, TabMenuColumn.TAB_COLUMN_TOTAL);


        ryPackage = view.findViewById(R.id.ry_column_package);
        ryFood = view.findViewById(R.id.ry_column_food);

        RelativeLayout.LayoutParams rpPackage = (RelativeLayout.LayoutParams) ryPackage.getLayoutParams();
        rpPackage.width = (DensityUtil.getScreenWidth(holder) - DensityUtil.dip2px(holder, 1)) * 3 / 14;
        ryPackage.setLayoutParams(rpPackage);

        ryFood.setPadding((DensityUtil.getScreenWidth(holder) - DensityUtil.dip2px(holder, 1)) * 3 / 14, 0, 0, 0);

        mMenuLists = new ArrayList<>();
        mAdapter = new TabMenuAdapter(holder, mMenuLists);

        mPackageLists = new ArrayList<>();
        mPackageAdapter = new TabPackageAdapter(holder, mPackageLists);

        mOnScrollistener = new OnRecycleViewScrollListener();

        ryFood.setOnScrollListener(mOnScrollistener);

        bindData();
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_four;
    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_fourth);
        ryFood.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ryFood.setLayoutManager(new LinearLayoutManager(holder));
        ryFood.setAdapter(mAdapter);
        ryPackage.setLayoutManager(new LinearLayoutManager(holder));
        ryPackage.setAdapter(mPackageAdapter);
        mHandler.sendEmptyMessageDelayed(TAB_MENU_NAME, 2000);
        mHandler.sendEmptyMessageDelayed(TAB_PACKAGE_NAME, 2000);
    }


    private class OnRecycleViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            ryPackage.scrollBy(0, dy);
        }
    }


    @Override
    public void onDetached() {

    }


}
