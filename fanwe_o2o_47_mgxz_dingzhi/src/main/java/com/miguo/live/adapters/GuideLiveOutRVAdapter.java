package com.miguo.live.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.guidelive.GuideOutModel;
import com.miguo.ui.view.ActGuideLayout;
import com.miguo.ui.view.interf.ExpandListener;
import com.miguo.ui.view.interf.ExpandStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/11/16.
 */

public class GuideLiveOutRVAdapter extends RecyclerView.Adapter<GuideLiveOutRVAdapter.ViewHolder> {
    private List<GuideOutModel> preData =new ArrayList<>();
    private int page =1;
    protected List<GuideOutModel> data=new ArrayList<>();
    private View emptyLayout;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_act_guide_live,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GuideOutModel outModel = data.get(position);
        holder.act_guide.bindData(null,outModel.getStatus());
        holder.act_guide.setExpandListener(new ExpandListener() {
            @Override
            public void expandStart() {
                outModel.setStatus(ExpandStatus.EXPANDED);
            }

            @Override
            public void shrinkStart() {
                //TODO nothing
            }
        });
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private ImageView iv_bg;
        private FrameLayout av_live_control;
        private ActGuideLayout act_guide;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_act_title);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_live_bg);
            av_live_control = (FrameLayout) itemView.findViewById(R.id.av_live_control);
            act_guide = (ActGuideLayout) itemView.findViewById(R.id.act_guide);
        }
    }

    private void clear(){
        preData.clear();
        data.clear();
    }
    public void setEmptyLayout(View emptyView){
        this.emptyLayout=emptyView;
    }

    public void setData(List<GuideOutModel> body) {
        if (page==1){
            clear();
        }
        if (compareOldNewData(body)){
            notifyDataSetChanged();
        }else {
            preData.clear();
            SDToast.showToast("没有更多数据");
        }
        toggleEmptyLayout();
    }
    protected void toggleEmptyLayout(){
        if (emptyLayout==null)return;
        if (data.isEmpty() && emptyLayout.getVisibility()==View.GONE){
            emptyLayout.setVisibility(View.VISIBLE);
        }else if (!data.isEmpty() && emptyLayout.getVisibility()==View.VISIBLE){
            emptyLayout.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @param news 新数据集
     * @return false 没有新数据
     *          true 有新数据
     */
    private boolean compareOldNewData(List<GuideOutModel> news){
        if (news ==null)return false;
        if (news.isEmpty())return false;
        if (news.size() ==10){
            page++;
            data.addAll(news);
        }else {
            data.removeAll(preData);
            data.addAll(news);
            preData.clear();
            preData.addAll(news);
        }
        return true;
    }
    public int getRequestPage(){
        return page;
    }

    public void resetPage(){
        page=1;
    }
}
