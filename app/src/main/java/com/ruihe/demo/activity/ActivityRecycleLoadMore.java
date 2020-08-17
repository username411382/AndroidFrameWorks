package com.ruihe.demo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ruihe.demo.R;

/**
 * Created by RH on 2018/6/6
 */
public class ActivityRecycleLoadMore extends BaseActivity {

    private RecyclerView rcLoadMore;
   // private PagedList<StudentBean> mPagedList;
   // private PositionDataSource mDataSource;

    //private PagedListAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    @Override
    public int getViewId() {
        return R.layout.activity_recycle_load_more;
    }

    @Override
    public void onActivityViewCreated() {
      //  DataSource;
      //  PageKeyedDataSource;
        mTitleView.setTitle("RecycleView加载更多");

        rcLoadMore =  findViewById(R.id.rc_load_more);

    }
}
