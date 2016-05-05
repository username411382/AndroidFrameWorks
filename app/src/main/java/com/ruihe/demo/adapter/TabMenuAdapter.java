package com.ruihe.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.bean.ItemTabColumn;
import com.ruihe.demo.common.utils.DensityUtil;
import com.ruihe.demo.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：表格菜单的适配器
 * Created by ruihe on 2016/5/4.
 */
public class TabMenuAdapter extends RecyclerView.Adapter<TabMenuAdapter.TabMenuHolder> {


    private List<ItemTabColumn> mItems;
    private Context mContext;
    private OnColumnListener mOnColumnListener;


    public enum TabMenuColumn {
        TAB_COLUMN_NAME,
        TAB_COLUMN_PRICE,
        TAB_COLUMN_COUNT,
        TAB_COLUMN_TOTAL,
        TAB_COLUMN_PACKAGE
    }

    public TabMenuAdapter(Context context, List<ItemTabColumn> list) {
        mItems = new ArrayList<>();
        mContext = context;
        mItems = list;
        mOnColumnListener = new OnColumnListener();
    }

    public class TabMenuHolder extends RecyclerView.ViewHolder {

        public LinearLayout lyHeader;
        public TextView tvFoodName;
        public TextView tvFoodPrice;
        public TextView tvFoodCount;
        public TextView tvFoodTotalCount;
        public LinearLayout lyItemColumn;

        public TabMenuHolder(View itemView) {
            super(itemView);
            lyHeader = (LinearLayout) itemView.findViewById(R.id.ly_head);

            lyItemColumn = (LinearLayout) itemView.findViewById(R.id.ly_item_column);
            tvFoodName = (TextView) itemView.findViewById(R.id.tv_tab_head_food_name);
            tvFoodPrice = (TextView) itemView.findViewById(R.id.tv_tab_head_food_price);
            tvFoodCount = (TextView) itemView.findViewById(R.id.tv_tab_head_food_count);
            tvFoodTotalCount = (TextView) itemView.findViewById(R.id.tv_tab_head_food_total);


            LinearLayout.LayoutParams ly = (LinearLayout.LayoutParams) lyHeader.getLayoutParams();
            ly.width = (DensityUtil.getScreenWidth(mContext) - DensityUtil.dip2px(mContext, 1)) * 11 / 14;
            lyHeader.setLayoutParams(ly);
            setTabHeadLayoutParams(mContext, tvFoodName, TabMenuColumn.TAB_COLUMN_NAME);
            setTabHeadLayoutParams(mContext, tvFoodPrice, TabMenuColumn.TAB_COLUMN_PRICE);
            setTabHeadLayoutParams(mContext, tvFoodCount, TabMenuColumn.TAB_COLUMN_COUNT);
            setTabHeadLayoutParams(mContext, tvFoodTotalCount, TabMenuColumn.TAB_COLUMN_TOTAL);


        }
    }

    @Override
    public TabMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TabMenuHolder(View.inflate(mContext, R.layout.item_tab_column, null));
    }

    @Override
    public void onBindViewHolder(TabMenuHolder holder, int position) {


        ItemTabColumn itemTabColumn = mItems.get(position);
        holder.tvFoodName.setText(itemTabColumn.foodName);
        holder.tvFoodPrice.setText(itemTabColumn.foodPrice);
        holder.tvFoodCount.setText(itemTabColumn.foodCount);
        holder.tvFoodTotalCount.setText(itemTabColumn.foodTotal);


        holder.lyItemColumn.setTag(position);
        holder.lyItemColumn.setOnClickListener(mOnColumnListener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    private class OnColumnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int position = (int) v.getTag();
            ItemTabColumn itemTabColumn = mItems.get(position);
            ToastUtils.show("--" + itemTabColumn.foodName);

        }
    }


    public static void setTabHeadLayoutParams(Context context, TextView tv, TabMenuColumn tabMenuColumn) {

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();

        switch (tabMenuColumn) {

            case TAB_COLUMN_NAME:
                lp.width = (DensityUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 1)) * 11 / 14 * 4 / 8;
                break;
            case TAB_COLUMN_PRICE:
                lp.width = (DensityUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 1)) * 11 / 14 * 1 / 8;
                break;
            case TAB_COLUMN_COUNT:
                lp.width = (DensityUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 1)) * 11 / 14 * 2 / 8;
                break;
            case TAB_COLUMN_TOTAL:
                lp.width = (DensityUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 1)) * 11 / 14 * 1 / 8;
                break;
            case TAB_COLUMN_PACKAGE:
                lp.width = (DensityUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 1)) * 3 / 14;
                break;
            default:
                break;
        }
        tv.setLayoutParams(lp);
    }

    public void notifyData(List<ItemTabColumn> list) {

        if (list != null && list.size() > 0) {
            mItems.clear();
            mItems.addAll(list);
        }
        notifyDataSetChanged();
    }

}
