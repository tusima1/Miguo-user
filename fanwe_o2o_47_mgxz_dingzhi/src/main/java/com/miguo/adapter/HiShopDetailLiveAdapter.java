package com.miguo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.views.LiveUtil;
import com.miguo.utils.DisplayUtil;

import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/21.
 * 商家主页 最底部 在现场
 */
public class HiShopDetailLiveAdapter extends BarryBaseRecyclerAdapter {

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
        return new HiShopDetailLiveAdapterListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).image.setOnClickListener(listener);
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        updateImageHeight(holder, position);
    }

    private void updateImageHeight(RecyclerView.ViewHolder holder, int position) {
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(getImageHeight(), getImageHeight());
        getHolder(holder).image.setLayoutParams(params);
        getHolder(holder).image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        try {
            ModelRoom room = getItem(position);
            String url="";
                if(!TextUtils.isEmpty(room.getCover())&&room.getCover().startsWith("http://")){
                   url = DisplayUtil.qiniuUrlExchange(room.getCover(),150,150);
                }
                SDViewBinder.setImageView(url, getHolder(holder).image);

            getHolder(holder).type.setText(LiveUtil.getLiveType(room));
            getHolder(holder).type.setBackgroundResource(LiveUtil.getLiveTypeColor(room));
            SDViewBinder.setTextView(getHolder(holder).localtion, room.getLbs().getAddress(), "");
        } catch (Exception e) {

        }
    }

    public int getItemHeight() {
        return getImageHeight() * getLine();
    }

    public int getLine() {
        return getItemCount() / 3 + (getItemCount() % 3 > 0 ? 1 : 0);
    }

    public int getImageHeight() {
        int screenWidth = getScreenWidth();
        return screenWidth / 3;
    }

    @Override
    public ModelRoom getItem(int position) {
        return (ModelRoom) super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder) super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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

    class HiShopDetailLiveAdapterListener extends BarryListener {
        private int index;

        public HiShopDetailLiveAdapterListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
            index = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image:
                    clickImage();
                    break;
            }
        }

        private void clickImage() {
            ModelRoom room = getAdapter().getItem(index);
            LiveUtil.clickRoom(room, getActivity());
        }


        @Override
        public HiShopDetailLiveAdapter getAdapter() {
            return (HiShopDetailLiveAdapter) super.getAdapter();
        }
    }

}
