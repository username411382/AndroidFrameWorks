package com.ruihe.demo.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.DensityUtil;

/**
 * RecycleView加载更多的适配器 AdapterMallGoodsList
 * Created by RH on 2018/6/11.
 */

public abstract class AdpterRecycleViewMore extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_FOOTER = 0x383ffe;
    private View viewFooter;
    private TextView tvPullUpLoadMore;
    private TextView tvLoadNoMore;
    private TextView tvLoadMoreError;
    private Context mContext;
    private OnLoadMoreListener mOnLoadMoreListener;
    private FooterStatus mCurrentStatus = FooterStatus.GONE;
    private boolean mHasMore = true;

    protected AdpterRecycleViewMore(Context context, OnLoadMoreListener onLoadMoreListener) {
        mContext = context;
        mOnLoadMoreListener = onLoadMoreListener;

        viewFooter = View.inflate(mContext, R.layout.view_recycle_footer, null);
        tvPullUpLoadMore = viewFooter.findViewById(R.id.tv_footer_pull_up_get_more);
        tvLoadNoMore = viewFooter.findViewById(R.id.tv_footer_no_more);
        tvLoadMoreError = viewFooter.findViewById(R.id.tv_footer_error);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new HolderFooter(viewFooter);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            refreshFooterView();
            if (viewFooter.getLayoutParams() != null) {
                viewFooter.getLayoutParams().width = DensityUtil.getScreenWidth(mContext);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCurrentStatus == FooterStatus.GONE ? getRealItemCount() : getRealItemCount() + 1;
    }

    /**
     * @return 真实item数量
     */
    public abstract int getRealItemCount();

    /**
     * 只要子类实现了getRealItemCount(),这个方法直接return super.getItemViewType(position)就行
     */
    @Override
    public int getItemViewType(int position) {
        return position == getRealItemCount() ? TYPE_FOOTER : getRealItemViewType(position);
    }

    /**
     * @return item类型
     */
    public abstract int getRealItemViewType(int position);


    /**
     * 页面刷新数据后记得设置是否还有更多，没有时就不会自动加载更多
     */
    public void setHasMore(boolean hasMore, boolean showFooter) {
        mHasMore = hasMore;
        if (mHasMore || !showFooter) {
            setFooterGone();
        } else {
            setFooterNoMore();
        }
    }

    /**
     * 返回footer当前状态
     */
    public FooterStatus getFooterStatus() {
        return mCurrentStatus;
    }

    /**
     * footer当前是否是可见状态
     */
    public boolean getIsShowingFooter() {
        return mCurrentStatus != FooterStatus.GONE;
    }

    /**
     * footer显示没有更多
     */
    public void setFooterNoMore() {
        showFooter(FooterStatus.NO_MORE);
    }

    /**
     * footer隐藏
     */
    public void setFooterGone() {
        showFooter(FooterStatus.GONE);
    }

    /**
     * footer显示加载错误状态
     */
    public void setFooterError() {
        mHasMore = false;
        showFooter(FooterStatus.ERROR);
    }

    /**
     * footer显示加载中状态，并且使用加载更多的回调函数
     *
     * @param isForce 如果是强制加载就不需要判断mHasMore
     */
    public void setFooterLoading(boolean isForce) {
        if ((isForce || mHasMore) && mCurrentStatus != FooterStatus.LOADING) {
            showFooter(FooterStatus.LOADING);
        }
    }

    public void setBackgroundColor(int color) {
        viewFooter.setBackgroundColor(color);
    }

    /**
     * 设置footer的显示状态
     *
     * @param status 本类中FooterStatus枚举值
     */
    private void showFooter(FooterStatus status) {
        mCurrentStatus = status;
        refreshFooterView();
        if (status == FooterStatus.LOADING && mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
        notifyDataSetChanged();
    }

    /**
     * 根据mCurrentStatus，更新footer的显示
     */
    private void refreshFooterView() {
        if (mCurrentStatus == FooterStatus.LOADING) {
            tvPullUpLoadMore.setVisibility(View.VISIBLE);
            tvLoadNoMore.setVisibility(View.GONE);
            tvLoadMoreError.setVisibility(View.GONE);
        } else if (mCurrentStatus == FooterStatus.NO_MORE) {
            tvPullUpLoadMore.setVisibility(View.GONE);
            tvLoadNoMore.setVisibility(View.VISIBLE);
            tvLoadMoreError.setVisibility(View.GONE);
        } else if (mCurrentStatus == FooterStatus.ERROR) {
            tvPullUpLoadMore.setVisibility(View.GONE);
            tvLoadNoMore.setVisibility(View.GONE);
            tvLoadMoreError.setVisibility(View.VISIBLE);
        }
    }

    public enum FooterStatus {
        GONE,
        LOADING,
        NO_MORE,
        ERROR
    }


    public interface OnLoadMoreListener {

        /**
         * 开始加载更多时的回调
         */
        void onLoadMore();

        /**
         * 可以根据listView的status决定点击事件
         */
        void onClickFooter(FooterStatus status);
    }

    private class HolderFooter extends RecyclerView.ViewHolder implements View.OnClickListener {

        HolderFooter(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onClickFooter(mCurrentStatus);
            }
        }
    }

}
