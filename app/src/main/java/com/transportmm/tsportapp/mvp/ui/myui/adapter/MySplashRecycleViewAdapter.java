package com.transportmm.tsportapp.mvp.ui.myui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transportmm.tsportapp.R;
import com.transportmm.tsportapp.mvp.model.entity.MySplashResult;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/9/21.
 */

public class MySplashRecycleViewAdapter extends RecyclerView.Adapter<MySplashRecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<MySplashResult> list = new ArrayList<>();

    public MySplashRecycleViewAdapter(Context context, List<MySplashResult> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_item_mysplash, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MySplashResult result = list.get(position);
        holder.textView_quName.setText(result.getQuName());
        holder.textView_cityName.setText(result.getCityName());
        holder.textView_tem.setText(result.getTem2() + "°-" + result.getTem1() + "°");
        holder.textView_stateDetailed.setText(result.getStateDetailed());
        holder.textView_windState.setText(result.getWindState());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_quName, textView_cityName, textView_tem, textView_stateDetailed, textView_windState;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_quName = itemView.findViewById(R.id.recyclerview_item_myslash_tv_quname);
            textView_cityName = itemView.findViewById(R.id.recyclerview_item_myslash_tv_cityname);
            textView_tem = itemView.findViewById(R.id.recyclerview_item_myslash_tv_tem);
            textView_stateDetailed = itemView.findViewById(R.id.recyclerview_item_myslash_tv_statedetailed);
            textView_windState = itemView.findViewById(R.id.recyclerview_item_myslash_tv_windstate);
        }
    }
}
