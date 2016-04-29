package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.ijkPlayer.ActivityVideoPlayer;

/**
 * 描述：首页五
 * Created by ruihe on 2016/4/28.
 */
public class FragmentFive extends BaseFragment implements View.OnClickListener {


    private TextView tv;


    @Override
    public void onAttached() {

    }


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        tv = (TextView) view.findViewById(R.id.tv);
        tv.setOnClickListener(this);


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

            case R.id.tv:
                ActivityVideoPlayer.redirectToActivity(holder, "测试标题", "http://7xsg5m.media1.z0.glb.clouddn.com/fish_2jtjf.mp4");
                break;
            default:
                break;

        }

    }
}
