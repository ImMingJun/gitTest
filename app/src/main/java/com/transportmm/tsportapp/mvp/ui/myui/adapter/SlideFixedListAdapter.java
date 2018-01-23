package com.transportmm.tsportapp.mvp.ui.myui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transportmm.tsportapp.R;

/**
 * Created by Administrator on 2018/1/3.
 */

public class SlideFixedListAdapter extends RecyclerView.Adapter<SlideFixedListAdapter.ViewHolder> {

    private Context mContext;
    private String[] strings;

    public SlideFixedListAdapter(Context mContext, String[] strings) {
        this.mContext = mContext;
        this.strings = strings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_slide_fixed,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(strings[position]);
        if (position==5)
            holder.tv.setTextColor(Color.RED);
        else
            holder.tv.setTextColor(Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return strings.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.tv);
        }
    }
}
