package com.ruihe.demo.common.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.ruihe.demo.R;


/**
 * 自带加载更多footer的ListView,外层防止在向上滑动时的冲突
 * Functions:1 处理listView和传入的swipeRefreshLayout的滑动冲突（没有时可传入null）, 2 可以设置滑到倒数第几个item时开始加载更多, 3 开始加载更多时调用回调接口, 4 footer点击事件
 * Tips:1 需要调用initVariable()进行初始化, 2 加载更多调用setFooterLoading(), 3 加载数据完成后需要setHasMore(), 4 加载更多失败时调用setLoadError()
 */
public class ListViewCanScroll extends ListView implements View.OnClickListener, AbsListView.OnScrollListener {

    private boolean allowDragTop = true; // 如果是true，则允许拖动至底部的下一页
    private float downY = 0;
    private boolean needConsumeTouch = true; // 是否需要承包touch事件，needConsumeTouch一旦被定性，则不会更改

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

    public ListViewCanScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ListViewCanScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListViewCanScroll(Context context) {
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getRawY();
            needConsumeTouch = true; // 默认情况下，listView内部的滚动优先，默认情况下由该listView去消费touch事件
            allowDragTop = isAtTop();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!needConsumeTouch) {
                // 在最顶端且向上拉了，则这个touch事件交给父类去处理
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            } else if (allowDragTop) {
                // needConsumeTouch尚未被定性，此处给其定性
                // 允许拖动到底部的下一页，而且又向上拖动了，就将touch事件交给父view
                if (ev.getRawY() - downY > 2) {
                    // flag设置，由父类去消费
                    needConsumeTouch = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            }
        }

        // 通知父view是否要处理touch事件
        getParent().requestDisallowInterceptTouchEvent(needConsumeTouch);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断listView是否在顶部
     *
     * @return 是否在顶部
     */
    private boolean isAtTop() {
        boolean resultValue = false;
        int childNum = getChildCount();
        if (childNum == 0) {
            // 没有child，肯定在顶部
            resultValue = true;
        } else {
            if (getFirstVisiblePosition() == 0) {
                // 根据第一个childView来判定是否在顶部
                View firstView = getChildAt(0);
                if (Math.abs(firstView.getTop() - getTop()) < 2) {
                    resultValue = true;
                }
            }
        }

        return resultValue;
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
}
