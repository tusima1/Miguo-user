package com.miguo.live.views.view.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.app.App;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.GuideLiveOutRVAdapter;
import com.miguo.model.guidelive.GuideOutBody;
import com.miguo.model.guidelive.GuideOutModel;
import com.miguo.model.guidelive.GuideOutRoot;
import com.miguo.view.video.VideoPlayerView;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/11/17.
 */

public class GuidePagerFragment extends Fragment {
    public static final String PAGE_REQUEST_ID = "page_request_id";
    private String mRequestID="";
    private View mFragmentView;
    private RecyclerView mRV;
    private View mEmptyLayout;
    private GuideLiveOutRVAdapter adapter;
    private DataRequestCompleteListener requestDataListener;

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
        if (null == mFragmentView) {
            mFragmentView = inflater.inflate(R.layout.frag_guide_live, container, false);
            mRV = (RecyclerView) mFragmentView.findViewById(R.id.recyclerview);
            mEmptyLayout = mFragmentView.findViewById(R.id.common_empty);
        }
        requestGuideLives();
        return mFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null !=mFragmentView){
            ((ViewGroup)(mFragmentView.getParent())).removeView(mFragmentView);
        }
    }

    private void startFlow(List<GuideOutModel> guide_list){
        if (guide_list == null)return;
        adapter = new GuideLiveOutRVAdapter();
        adapter.setEmptyLayout(mEmptyLayout);
        adapter.setData(guide_list);
        mRV.setHasFixedSize(true);
        final LinearLayoutManager llManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRV.setLayoutManager(llManager);
        mRV.setAdapter(adapter);
        mRV.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int
                    oldScrollY) {
                if (llManager !=null){
                    int firstVisibleItemPosition = llManager.findFirstVisibleItemPosition();
                    int playingPosition = adapter.getPlayingPosition();
                    Log.e("test","play :"+" visible item ="+(firstVisibleItemPosition-1) +"   playing item: "+playingPosition);
                    if (firstVisibleItemPosition -1 ==playingPosition){
                        View childAt = mRV.getChildAt(playingPosition);
                        VideoPlayerView viewById = (VideoPlayerView) childAt.findViewById(R.id.av_live);
                        viewById.stopPlay();
                        adapter.resetPlayingPosition();
                    }

//                    adapter.setFirstVisiblePosition(firstVisibleItemPosition);
//                    Log.e("test","first: "+firstVisibleItemPosition);
                }
            }
        });
//        mRV.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                float x = event.getX();
//                float y = event.getY();
//                float rawX = event.getRawX();
//                float rawY = event.getRawY();
//                Log.e("test-out","x: "+x+"   y:"+y+"   rawX:"+rawX + "   rawY: "+rawY);
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        mRV.requestDisallowInterceptTouchEvent(true);
//                        break;
//                }
//                return false;
//            }
//        });
    }

    public void refreshData(){
        adapter.resetPage();
        requestGuideLives();
    }

    public void loadMoreData(){
        requestGuideLives();
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
}
