package com.miguo.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.miguo.listener.Listener;
import com.miguo.live.views.adapter.HijasonBaseRecyclerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public class HomeADView2Adapter extends HijasonBaseRecyclerAdapter{

    public HomeADView2Adapter(Context activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {

    }

    @Override
    protected Listener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return null;
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, Listener listener) {

    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
