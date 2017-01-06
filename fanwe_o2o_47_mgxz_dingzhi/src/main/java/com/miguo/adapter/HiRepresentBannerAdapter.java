package com.miguo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.entity.RepresentBannerBean;

import java.util.List;

/**
 * Created by zlh on 2017/1/6.
 */

public class HiRepresentBannerAdapter extends BarryBaseRecyclerAdapter {

    public HiRepresentBannerAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.activity_hihome_fragment_hirepresent_banner_item, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return null;
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {

    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        SDViewBinder.setImageView(getItem(position).getImg(), getHolder(holder).image);
        getHolder(holder).title.setText(getItem(position).getName());
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        handleInitImageParams(holder,position);
    }

    private void handleInitImageParams(RecyclerView.ViewHolder holder, int position){
        int width = getImageWidth();
        int height = width;
        LinearLayout.LayoutParams params = getLinearLayoutParams(width, height);
        params.setMargins(0, dip2px(13), 0, 0);
        getHolder(holder).image.setLayoutParams(params);
    }

    public int getPaddingSpace(){
        return (getScreenWidth() - getImageWidth() * 4) / 5;
    }

    private int getImageWidth(){
        return (int)(getScreenWidth() * 0.14);
    }

    @Override
    public RepresentBannerBean.Result.Body.Categories getItem(int position) {
        return (RepresentBannerBean.Result.Body.Categories)super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder)super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.image)
        ImageView image;

        @ViewInject(R.id.title)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
        }



    }

}
