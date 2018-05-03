package com.ruihe.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruihe.demo.R;

/**
 * Created by RH on 2018/5/3.
 */

public class AdapterItemLeftIn extends RecyclerView.Adapter<AdapterItemLeftIn.ViewHolder> {


    private LayoutInflater mInflater;

    public AdapterItemLeftIn(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_left_in, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvPosition.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPosition = (TextView) itemView.findViewById(R.id.tv_position);
        }

    }

}
