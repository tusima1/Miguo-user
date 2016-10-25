package com.miguo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.entity.HiShopDetailBean;

import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/21.
 * 商家主页 最底部 在现场
 */
public class HiShopDetailLiveAdapter extends BarryBaseRecyclerAdapter{

    public HiShopDetailLiveAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.activity_hishop_detail_live_item, parent, false);
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
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        updateImageHeight(holder, position);
    }

    private void updateImageHeight(RecyclerView.ViewHolder holder, int position){
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(getImageHeight(), getImageHeight());
        getHolder(holder).image.setLayoutParams(params);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        try{
            if(getItem(position).getCover().contains("http:")){
                SDViewBinder.setImageView(getItem(position).getCover(), getHolder(holder).image);
            }

            getHolder(holder).type.setText(getItem(position).isLive() ? "正在直播" : "点播回放");
            getHolder(holder).type.setBackgroundResource(getItem(position).isLive() ? R.drawable.shape_cricle_bg_yellow: R.drawable.shape_cricle_bg_black_alphe_60);

            getHolder(holder).localtion.setText(getItem(position).getAddress());
        }catch (Exception e){

        }


    }

    public int getItemHeight(){
       return getImageHeight() * getLine();
    }

    public int getLine(){
        return getItemCount() / 3 + (getItemCount() % 3 > 0 ? 1 : 0);
    }

    public int getImageHeight(){
        int screenWidth = getScreenWidth();
        return screenWidth / 3;
    }

    @Override
    public HiShopDetailBean.Result.Live getItem(int position) {
        return (HiShopDetailBean.Result.Live)super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder)super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.image)
        ImageView image;

        @ViewInject(R.id.type)
        TextView type;

        @ViewInject(R.id.location)
        TextView localtion;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

}
