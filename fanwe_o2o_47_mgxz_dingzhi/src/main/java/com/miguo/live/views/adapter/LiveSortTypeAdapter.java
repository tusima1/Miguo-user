package com.miguo.live.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

/**
 * 有趣页 分类adapter.
 * Created by zhouhy on 2016/10/26.
 */
public class LiveSortTypeAdapter extends RecyclerView.Adapter<LiveSortTypeAdapter.ViewHolder>  {


    private Context mContext;
    private List<ModelHomeClassifyList> mData;
    private int  lastCheckedIndex=0;
    private ModelHomeClassifyList lastcheckedModel=null;
    private LiveSortTypeAdapter.ViewHolder lastViewHolder;

    public LiveSortTypeAdapter(List<ModelHomeClassifyList> data, Context context){
        this.mContext=context;
        this.mData=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_home_index, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
     final   ModelHomeClassifyList modelHomeClassifyList = mData.get(position);
        if(modelHomeClassifyList==null){
            return;
        }

        holder.tvName.setText(TextUtils.isEmpty(modelHomeClassifyList.getName())?"":modelHomeClassifyList.getName());

        if (modelHomeClassifyList.is_checked()) {
            ImageLoader.getInstance().displayImage(modelHomeClassifyList.getImg(), holder.ivImg);
        } else {
            String imageUrl = modelHomeClassifyList.getImg();
            if(!TextUtils.isEmpty(modelHomeClassifyList.getUncheck_img())){
                imageUrl = modelHomeClassifyList.getUncheck_img();
            }
            ImageLoader.getInstance().displayImage(imageUrl,holder.ivImg);
        }
        ImageLoader.getInstance().displayImage(modelHomeClassifyList.getUncheck_img(),holder.ivImg);
        holder.ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改当前的model
                modelHomeClassifyList.setIs_checked(true);
                notifyItemChanged(position);
                SDEventManager.post(modelHomeClassifyList, EnumEventTag.HOME_TYPE_CHANGE.ordinal());
                //修改原 来被选择的model.
                if(lastCheckedIndex!=position){
                    if(lastcheckedModel!=null){
                        lastcheckedModel.setIs_checked(false);
                        ImageLoader.getInstance().displayImage(modelHomeClassifyList.getImg(),lastViewHolder.ivImg);
                        notifyItemChanged(lastCheckedIndex);
                    }                  
                }
                lastCheckedIndex = position;
                lastcheckedModel = modelHomeClassifyList;
                lastViewHolder = holder;
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.mData==null?0:this.mData.size();
    }

    public List<ModelHomeClassifyList> getmData() {
        return mData;
    }

    /**
     * 设置数据即刷新adapter
     * @param mData
     */
    public void setmData(List<ModelHomeClassifyList> mData) {
        //数据不一致才刷新。
        this.mData = mData;
        notifyItemRangeChanged(0,getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public   ImageView ivImg ;
        public   TextView tvName ;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = (ImageView) itemView.findViewById(R.id.item_home_index_iv_image);
            SDViewUtil.setViewWidth(ivImg, SDViewUtil.getScreenWidth() / 6);
            SDViewUtil.setViewHeight(ivImg, SDViewUtil.getScreenWidth() / 6);
            tvName = com.fanwe.library.utils.ViewHolder.get(itemView, R.id.item_home_index_tv_name);
        }
    }
}
