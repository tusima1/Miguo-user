package com.miguo.live.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.utils.SDHandlerUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.getAudienceList.ModelAudienceInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by didik on 2016/7/22.
 */
public class HeadTopAdapter extends RecyclerView.Adapter<HeadTopAdapter.ViewHolder> {

    private Context mContext;
    private List<ModelAudienceInfo> mData;

    public HeadTopAdapter(List<ModelAudienceInfo> data, Context context){
        this.mContext=context;
        this.mData=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_act_live_headtop, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelAudienceInfo modelAudienceInfo = mData.get(position);
        if(modelAudienceInfo!=null&&!TextUtils.isEmpty(modelAudienceInfo.getIcon())){
            ImageLoader.getInstance().displayImage(modelAudienceInfo.getIcon(),holder.mCIV);
        }else {
            holder.mCIV.setImageResource(R.drawable.app_icon);
        }
    }

    @Override
    public int getItemCount() {
        return this.mData==null?0:this.mData.size();
    }

    public List<ModelAudienceInfo> getmData() {
        return mData;
    }

    /**
     * 设置数据即刷新adapter
     * @param mData
     */
    public void setmData(List<ModelAudienceInfo> mData) {
        //数据不一致才刷新。
            this.mData = mData;
        notifyItemRangeChanged(0,getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mCIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mCIV= (CircleImageView) itemView.findViewById(R.id.item_avatar);
        }
    }
}
