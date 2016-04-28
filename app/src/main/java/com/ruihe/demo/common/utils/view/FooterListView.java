package com.ruihe.demo.common.utils.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.ruihe.demo.R;


/**
 * 自带加载更多footer的listview
 * Functions:1 处理listView和传入的swipeRefreshLayout的滑动冲突, 2 可以设置滑到倒数第几个item时开始加载更多, 3 开始加载更多时调用回调接口, 4 footer点击事件
 * Tips:1 需要调用initVariable()进行初始化, 2 加载更多调用setFooterLoading(), 3 加载数据完成后需要setHasMore(), 4 加载更多失败时调用setLoadError()
 */
public class FooterListView extends ListView implements View.OnClickListener, AbsListView.OnScrollListener {

    public enum FooterStatus {
        GONE,
        LOADING,
        NO_MORE,
        ERROR
    }

    private View viewFooter;
    private CircularProgressView cpvLoading;
    private TextView tvLoadNoMore;
    private TextView tvLoadMoreError;

    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean mHasMore;
    private boolean mIsFooterLoading;
    private FooterStatus mCurrentStatus = FooterStatus.GONE;
    private int mFirstLoadMaxCount;
    private int mLoadMoreBeforeBottomNumber;

    private OnLoadMoreListener mLoadMoreListener;
    private OnScrollListener mOnScrollListener;

    public FooterListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FooterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FooterListView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        viewFooter = View.inflate(context, R.layout.view_listview_footer, null);
        cpvLoading = (CircularProgressView) viewFooter.findViewById(R.id.cpv_footer_load);
        tvLoadNoMore = (TextView) viewFooter.findViewById(R.id.tv_footer_no_data);
        tvLoadMoreError = (TextView) viewFooter.findViewById(R.id.tv_footer_error);
        viewFooter.setOnClickListener(this);
        super.setOnScrollListener(this);
    }

    public void initVariable(int firstLoadMaxCount, int loadMoreBeforeBottomNumber, OnLoadMoreListener loadMoreListener, SwipeRefreshLayout swipeRefreshLayout) {
        this.mFirstLoadMaxCount = firstLoadMaxCount;
        this.mLoadMoreListener = loadMoreListener;
        this.mLoadMoreBeforeBottomNumber = loadMoreBeforeBottomNumber;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
        mIsFooterLoading = false;
        if (!mHasMore) {
            if (getAdapter().getCount() <= mFirstLoadMaxCount / 2) {
                showFooter(FooterStatus.GONE);
            } else {
                showFooter(FooterStatus.NO_MORE);
            }
        }
    }

    /**
     * footer显示加载错误状态
     */
    public void setLoadError() {
        showFooter(FooterStatus.ERROR);
    }

    /**
     * footer显示加载中状态，并且使用加载更多的回调函数
     */
    public void setFooterLoading() {
        showFooter(FooterStatus.LOADING);
    }

    public void setBackgroundColor(int color) {
        viewFooter.setBackgroundColor(color);
    }

    /*public FooterStatus getCurrentFooterStatus() {
        return mCurrentStatus;
    }*/

    @Override
    public void onClick(View v) {
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onClickFooter(mCurrentStatus);
        }
    }

    /**
     * 设置footer的显示状态
     *
     * @param status 本类中FooterStatus枚举值
     */
    private void showFooter(FooterStatus status) {
        try {
            this.removeFooterView(viewFooter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (status == FooterStatus.LOADING) {
            mIsFooterLoading = true;
            this.addFooterView(viewFooter);
            cpvLoading.setVisibility(View.VISIBLE);
            cpvLoading.startAnimation();
            tvLoadNoMore.setVisibility(View.GONE);
            tvLoadMoreError.setVisibility(View.GONE);
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }

        } else if (status == FooterStatus.NO_MORE) {
            this.addFooterView(viewFooter);
            cpvLoading.setVisibility(View.GONE);
            cpvLoading.setIndeterminate(true);
            tvLoadNoMore.setVisibility(View.VISIBLE);
            tvLoadMoreError.setVisibility(View.GONE);

        } else if (status == FooterStatus.ERROR) {
            this.addFooterView(viewFooter);
            cpvLoading.setVisibility(View.GONE);
            cpvLoading.setIndeterminate(true);
            tvLoadNoMore.setVisibility(View.GONE);
            tvLoadMoreError.setVisibility(View.VISIBLE);
        }
        mCurrentStatus = status;
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //避免SwipeRefreshLayout和ListView的滑动冲突
        if (swipeRefreshLayout != null) {
            View firstView = view.getChildAt(firstVisibleItem);
            if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == getPaddingTop())) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        }

        //根据滑动位置，决定是否开始加载更多
        checkShouldLoadMore();

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private void checkShouldLoadMore() {
        if (this.getAdapter() != null && getLastVisiblePosition() >= getAdapter().getCount() - mLoadMoreBeforeBottomNumber) {
            if (mHasMore) {
                if (!mIsFooterLoading && (swipeRefreshLayout == null || !swipeRefreshLayout.isRefreshing())) {
                    showFooter(FooterStatus.LOADING);
                }
            }
        }
    }

    public interface OnLoadMoreListener {

        /** 开始加载更多时的回调 */
        void onLoadMore();

        /** 可以根据listView的status决定点击事件*/
        void onClickFooter(FooterStatus status);
    }
}
