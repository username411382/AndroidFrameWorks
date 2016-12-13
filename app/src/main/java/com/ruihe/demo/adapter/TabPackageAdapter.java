package com.ruihe.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.bean.ItemTabColumn;
import com.ruihe.demo.common.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：表格菜单左侧的适配器
 * Created by ruihe on 2016/5/4.
 */
public class TabPackageAdapter extends RecyclerView.Adapter<TabPackageAdapter.TabMenuHolder> {


    private List<ItemTabColumn> mItems;
    private Context mContext;

    public TabPackageAdapter(Context context, List<ItemTabColumn> list) {
        mItems = new ArrayList<>();
        mContext = context;
        mItems = list;
    }

    public class TabMenuHolder extends RecyclerView.ViewHolder {


        public TextView tvContent;
        public View line;
        public View bottomLine;

        public TabMenuHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_tab_package);
            line = itemView.findViewById(R.id.view_tab_package);
            bottomLine = itemView.findViewById(R.id.view_tab_bottom);
        }
    }

    @Override
    public TabMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TabMenuHolder(View.inflate(mContext, R.layout.item_tab_column_package, null));
    }

    @Override
    public void onBindViewHolder(TabMenuHolder holder, int position) {

        ItemTabColumn itemTabColumn = mItems.get(position);

        int packageCount = itemTabColumn.packageNameCount;

        LinearLayout.LayoutParams lpVerticalLine = (LinearLayout.LayoutParams) holder.line.getLayoutParams();
        lpVerticalLine.height = DensityUtil.dip(mContext, 36) * packageCount + DensityUtil.dip(mContext, 1) * (packageCount - 1);
        holder.line.setLayoutParams(lpVerticalLine);

        LinearLayout.LayoutParams lpHorizontalLine = (LinearLayout.LayoutParams) holder.bottomLine.getLayoutParams();
        lpHorizontalLine.width = (DensityUtil.getScreenWidth(mContext) - DensityUtil.dip2px(mContext, 1)) * 3 / 14 - DensityUtil.dip2px(mContext, 1);
        holder.bottomLine.setLayoutParams(lpHorizontalLine);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.tvContent.getLayoutParams();
        lp.width = (DensityUtil.getScreenWidth(mContext) - DensityUtil.dip2px(mContext, 1)) * 3 / 14 - DensityUtil.dip2px(mContext, 1);
        lp.height = DensityUtil.dip(mContext, 36) * packageCount + DensityUtil.dip(mContext, 1) * (packageCount - 1);
        holder.tvContent.setLayoutParams(lp);
        holder.tvContent.setText(itemTabColumn.packageName);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public void notifyData(List<ItemTabColumn> list) {

        if (list != null && list.size() > 0) {
            mItems.clear();
            mItems.addAll(list);
        }
        notifyDataSetChanged();
    }


}
