package com.ruihe.demo.adapter;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import com.ruihe.demo.bean.StudentBean;

/**
 * Created by RH on 2018/6/7
 */
public class StudentDataSource extends ItemKeyedDataSource<String,StudentBean> {


    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<StudentBean> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<StudentBean> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<StudentBean> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull StudentBean item) {
        return null;
    }
}
