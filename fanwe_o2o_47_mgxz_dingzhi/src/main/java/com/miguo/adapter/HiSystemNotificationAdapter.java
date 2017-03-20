package com.miguo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.definition.ClassPath;
import com.miguo.entity.MessageListBean;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.BaseUtils;

import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/15.
 */

public class HiSystemNotificationAdapter extends BarryBaseRecyclerAdapter {


    public HiSystemNotificationAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.item_hisystem_notifycation, parent, false);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return new HiSystemNotificationAdapterListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).itemLayout.setOnClickListener(listener);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        getHolder(holder).title.setText(getItem(position).getTitle());
        getHolder(holder).describe.setText(getItem(position).getContent());
        getHolder(holder).time.setText(getItem(position).getTime());
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public MessageListBean.Result.Body getItem(int position) {
        return (MessageListBean.Result.Body)super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder)super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        @ViewInject(R.id.item_layout)
        RelativeLayout itemLayout;

        @ViewInject(R.id.title)
        TextView title;

        @ViewInject(R.id.time)
        TextView time;

        @ViewInject(R.id.describe)
        TextView describe;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class HiSystemNotificationAdapterListener extends BarryListener{

        public HiSystemNotificationAdapterListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_layout:
                    clickItem();
                    break;
            }
            super.onClick(v);
        }

        private void clickItem(){
            Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.MESSAGE_SYSTEM));
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }

    }

}
