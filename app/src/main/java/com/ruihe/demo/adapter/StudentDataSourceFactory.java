package com.ruihe.demo.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.ruihe.demo.bean.StudentBean;

import java.util.concurrent.Executor;

import javax.xml.datatype.DatatypeFactory;

/**
 * Created by RH on 2018/6/7
 */
public class StudentDataSourceFactory extends DataSource.Factory<String,StudentBean> {

    private final MutableLiveData sourceLiveData;
    private final Executor retryExecutor;

    public StudentDataSourceFactory( Executor retryExecutor) {
        this.retryExecutor = retryExecutor;
        this.sourceLiveData = new MutableLiveData();
    }

    public MutableLiveData getSourceLiveData() {
        return sourceLiveData;
    }

    @Override
    public DataSource<String, StudentBean> create() {
        return null;
    }
}
