package com.miguo.live.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.miguo.model.guidelive.GuideOutModel;
import com.miguo.ui.view.ActGuideLayout;
import com.miguo.ui.view.interf.ExpandListener;
import com.miguo.ui.view.interf.ExpandStatus;
import com.miguo.view.video.IPlayEvent;
import com.miguo.view.video.ModelVideoPlayer;
import com.miguo.view.video.VideoPlayerView;

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
    private int preLivePosition=-2;//之前直播的
    private RecyclerView mRecyclerView;
//    private int curPosition=-1;
//    private HashMap<Integer,List<>>

    public int getPlayingPosition(){
        return preLivePosition;
    }
    public void resetPlayingPosition(){
        preLivePosition=-2;
    }

    public void setRecycerView(RecyclerView recycerView){
        this.mRecyclerView=recycerView;
    }

    public void setFirstVisiblePosition(int position){
        Log.e("test","FirstVisiblePosition:"+position);
        if (preLivePosition == position-1){

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_act_guide_live,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GuideOutModel outModel = data.get(position);
        //            "extend":"",//扩展子u按
//            "img":"图片",//封面图
//            "create_time":"111111111",
//            "is_effect":"1",
//            "id":"主键",
//            "video":"视频地址",
//            "sort":"1",
//            "title":"标题",
//            "descript":"描述"
        holder.av_live.setData(crateAVModel(outModel));
        holder.av_live.setIPlayEvent(new IPlayEvent() {
            @Override
            public void onAvChange(int status, Object object) {
                switch (status){
                    case IPlayEvent.START:

                        break;
                    case IPlayEvent.PLAYING:
                        break;
                    case IPlayEvent.PAUSE:
                        break;
                    case IPlayEvent.FINISH:
                        break;
                    default:
                        break;
                }
                preLivePosition =position;
            }
        });
        holder.act_guide.bindData(null,outModel.getStatus());
        holder.act_guide.setExpandListener(new ExpandListener() {
            @Override
            public void expandStart() {
                outModel.setStatus(ExpandStatus.EXPANDED);
                holder.av_live.stopPlay();
            }

            @Override
            public void shrinkStart() {
                //TODO nothing
            }
        });
        holder.act_guide.setRecycerView(mRecyclerView);

    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : 3;
    }


    private ModelVideoPlayer crateAVModel(GuideOutModel outModel){
//        private List<ModelRecordFile> fileSet;
//        private String coverUrl;
//        private String title;
        ModelVideoPlayer result=new ModelVideoPlayer();
        result.setCoverUrl(outModel.getImg());
        result.setTitle(outModel.getTitle());
        result.setPlayUrl(outModel.getVideo());
//        List<ModelRecordFile> fileSet=new ArrayList<>();
//        fileSet.add()
        return  result;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ActGuideLayout act_guide;
        private VideoPlayerView av_live;
        public ViewHolder(View itemView) {
            super(itemView);
            act_guide = (ActGuideLayout) itemView.findViewById(R.id.act_guide);
            av_live = (VideoPlayerView) itemView.findViewById(R.id.av_live);
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
