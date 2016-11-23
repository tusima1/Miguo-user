package com.miguo.live.views.view.frag;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fanwe.adapter.SuperVideoAdapter;
import com.fanwe.app.App;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.model.guidelive.GuideOutBody;
import com.miguo.model.guidelive.GuideOutModel;
import com.miguo.model.guidelive.GuideOutRoot;
import com.superplayer.library.SuperPlayer;
import com.superplayer.library.SuperPlayerManage;
import com.superplayer.library.mediaplayer.IjkVideoView;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/11/17.
 */

public class GuidePagerFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String PAGE_REQUEST_ID = "page_request_id";
    private String mRequestID="";
    private RecyclerView mRV;
    private View mEmptyLayout;
    private DataRequestCompleteListener requestDataListener;
    SwipeToLoadLayout swipeToLoadLayout;

    public static GuidePagerFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString(PAGE_REQUEST_ID, tag);
        GuidePagerFragment pageFragment = new GuidePagerFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestID = getArguments().getString(PAGE_REQUEST_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mFragmentView = inflater.inflate(R.layout.frag_guide_live, container, false);
        mEmptyLayout = mFragmentView.findViewById(R.id.common_empty);
        swipeToLoadLayout = (SwipeToLoadLayout) mFragmentView.findViewById(R.id.swipeToLoadLayout);
        mRV = (RecyclerView)  mFragmentView.findViewById(R.id.swipe_target);

        swipeToLoadLayout.setSwipeStyle(SwipeToLoadLayout.STYLE.ABOVE);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        requestGuideLives();
        return mFragmentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (player ==null)return;
        if (isVisibleToUser){
            player.onResume();
        }else {
            player.onPause();
        }
    }

    private void startFlow(List<GuideOutModel> guide_list){
        if (guide_list == null)return;
        initPlayer();
        initAdapter(guide_list);
        mRV.setHasFixedSize(true);
        final LinearLayoutManager llManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRV.setLayoutManager(llManager);
        mRV.setAdapter(adapter);
    }

    public void refreshData(){
        adapter.resetPage();
        requestGuideLives();
    }

    public void loadMoreData(){
        requestGuideLives();
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000);
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
    private void requestGuideLives(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("tab_id", "");
        params.put("page_size", "10");
        params.put("page", adapter ==null ? "1":adapter.getRequestPage()+"");
        params.put("method", "InterestingGuideVideo");
        OkHttpUtil.getInstance().get(params, new HttpCallback<GuideOutRoot>() {
            @Override
            public void onSuccessWithBean(GuideOutRoot guideOutRoot) {
                List<GuideOutBody> body = guideOutRoot.getResult().get(0).getBody();
                if (body !=null  && body.size()>0){
                    List<GuideOutModel> guide_list = body.get(0).getGuide_list();
                    if (adapter==null){
                        startFlow(guide_list);
                    }else {
                        adapter.setData(guide_list);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (requestDataListener!=null)requestDataListener.requestComplete();
            }
        });
    }

    public interface DataRequestCompleteListener{
        void requestComplete();
    }

    /**
     * 如果parent需要知道数据请求完毕
     * 使用此回调
     * @param listener
     */
    public void setOnDataRequestCompleteListener(DataRequestCompleteListener listener){
        this.requestDataListener=listener;
    }

    /********************************* COPY ******************************************/
    private SuperVideoAdapter adapter;
    private SuperPlayer player;
    private int postion = -1;
    private int lastPostion = -1;

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        player = SuperPlayerManage.getSuperManage().initialize(getContext());
        player.setShowTopControl(false).setSupportGesture(false);
    }
    /**
     * 初始化适配器
     */
    private void initAdapter(final List<GuideOutModel> guide_list) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRV.setLayoutManager(mLayoutManager);
        adapter = new SuperVideoAdapter(getActivity(), guide_list);
        adapter.setEmptyLayout(mEmptyLayout);
        mRV.setAdapter(adapter);
        adapter.setPlayClick(new SuperVideoAdapter.onPlayClick() {
            @Override
            public void onPlayclick(int position, RelativeLayout image) {
                image.setVisibility(View.GONE);
                if (player.isPlaying() && lastPostion == position) {
                    return;
                }

                postion = position;
                if (player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                    if (position != lastPostion) {
                        player.stopPlayVideo();
                        player.release();
                    }
                }
                if (lastPostion != -1) {
                    player.showView(R.id.adapter_player_control);
                }

                View view = mRV.findViewHolderForAdapterPosition(position).itemView;
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.adapter_super_video);
                frameLayout.removeAllViews();
                player.showView(R.id.adapter_player_control);
                frameLayout.addView(player);
                player.play(guide_list.get(position).getVideo());
                lastPostion = position;
            }
        });
        /**
         * 播放完设置还原播放界面
         */
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                ViewGroup last = (ViewGroup) player.getParent();//找到videoitemview的父类，然后remove
                if (last != null && last.getChildCount() > 0) {
                    last.removeAllViews();
                    View itemView = (View) last.getParent();
                    if (itemView != null) {
                        itemView.findViewById(R.id.adapter_player_control).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        /***
         * 监听列表的下拉滑动
         */
        mRV.addOnChildAttachStateChangeListener(new SuperRecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                int index = mRV.getChildAdapterPosition(view);
                View controlview = view.findViewById(R.id.adapter_player_control);
                if (controlview == null) {
                    return;
                }
                view.findViewById(R.id.adapter_player_control).setVisibility(View.VISIBLE);
                if (index == postion) {
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.adapter_super_video);
                    frameLayout.removeAllViews();
                    if (player != null &&
                            ((player.isPlaying()) || player.getVideoStatus() == IjkVideoView.STATE_PAUSED)) {
                        view.findViewById(R.id.adapter_player_control).setVisibility(View.GONE);
                    }
                    if (player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                        if (player.getParent() != null)
                            ((ViewGroup) player.getParent()).removeAllViews();
                        frameLayout.addView(player);
                        return;
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int index = mRV.getChildAdapterPosition(view);
                if ((index) == postion) {
                    if (player != null) {
                            player.stop();
                            player.release();
                            player.showView(R.id.adapter_player_control);}
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 下面的这几个Activity的生命状态很重要
     */
    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

}
