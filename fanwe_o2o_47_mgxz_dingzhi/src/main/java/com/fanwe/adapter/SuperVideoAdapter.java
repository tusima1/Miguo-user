package com.fanwe.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.model.guidelive.GuideInnerGoods;
import com.miguo.model.guidelive.GuideOutModel;
import com.miguo.ui.view.ActGuideLayout;
import com.miguo.ui.view.interf.ExpandListener;
import com.miguo.ui.view.interf.ExpandStatus;
import com.superplayer.library.utils.SuperPlayerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author Super南仔
 * @time 2016-9-19
 */
public class SuperVideoAdapter extends RecyclerView.Adapter<SuperVideoAdapter.VideoViewHolder> {
    private final Context mContext;
    private List<GuideOutModel> preData =new ArrayList<>();
    private int page =1;
    private List<GuideOutModel> dataList=new ArrayList<>();
    private View emptyLayout;
    private HashMap<Integer,List<GuideInnerGoods>> innerData=new HashMap<>();

    public SuperVideoAdapter(Context context, List<GuideOutModel> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public SuperVideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.adapter_listview_layout, null);
        VideoViewHolder holder = new VideoViewHolder(view);
        view.setTag(holder);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SuperVideoAdapter.VideoViewHolder holder, int position) {
        final GuideOutModel outModel = dataList.get(position);
        SDViewBinder.setTextView(holder.tvTitle, outModel.getTitle(), "");
        SDViewBinder.setImageView(outModel.getImg(), holder.ivCover);
        holder.update(position);
        holder.act_guide.bindData(position,outModel.getId(),outModel.getStatus(),innerData.get(position),outModel.getDescript());
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
        holder.act_guide.setOnChildReceiveDataListener(new ActGuideLayout.ActLayoutReceiveDataListener() {
            @Override
            public void onChildReceiveData(int position, List<GuideInnerGoods> childData) {
                innerData.put(position,childData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlayPlayerControl;
        private RelativeLayout rlayPlayer;
        private ImageView ivCover;
        private TextView tvTitle;
        private ActGuideLayout act_guide;

        public VideoViewHolder(View itemView) {
            super(itemView);
            act_guide = (ActGuideLayout) itemView.findViewById(R.id.act_guide);
            rlayPlayerControl = (RelativeLayout) itemView.findViewById(R.id.adapter_player_control);
            rlayPlayer = (RelativeLayout) itemView.findViewById(R.id.adapter_super_video_layout);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover_view_video_player);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title_view_video_player);
            if (rlayPlayer != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlayPlayer.getLayoutParams();
                layoutParams.height = (int) (SuperPlayerUtils.getScreenWidth((Activity) mContext) * 0.5652f);//这值是网上抄来的，我设置了这个之后就没有全屏回来拉伸的效果，具体为什么我也不太清楚
                rlayPlayer.setLayoutParams(layoutParams);
            }
        }

        public void update(final int position) {
            //点击回调 播放视频
            rlayPlayerControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playclick != null)
                        playclick.onPlayclick(position, rlayPlayerControl);
                }
            });
        }
    }

    private onPlayClick playclick;

    public void setPlayClick(onPlayClick playclick) {
        this.playclick = playclick;
    }

    public interface onPlayClick {
        void onPlayclick(int position, RelativeLayout image);
    }

    /****************** 刷新 ********************/
    private void clear(){
        preData.clear();
        dataList.clear();
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
        if (dataList.isEmpty() && emptyLayout.getVisibility()==View.GONE){
            emptyLayout.setVisibility(View.VISIBLE);
        }else if (!dataList.isEmpty() && emptyLayout.getVisibility()==View.VISIBLE){
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
            dataList.addAll(news);
        }else {
            dataList.removeAll(preData);
            dataList.addAll(news);
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
