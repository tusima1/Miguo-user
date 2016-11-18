package com.miguo.live.views.view.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.app.App;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.GuideLiveOutRVAdapter;
import com.miguo.model.guidelive.GuideOutModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/11/17.
 */

public class GuidePagerFragment extends Fragment {
    public static final String PAGE_REQUEST_ID = "page_request_id";
    private String mRequestID="";
    private RecyclerView mRV;
    private View mEmptyLayout;

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
        View view = inflater.inflate(R.layout.frag_guide_live, container, false);
        mRV = (RecyclerView) view.findViewById(R.id.recyclerview);
        mEmptyLayout = view.findViewById(R.id.common_empty);
        requestGuideLives();
        return view;
    }

    private void startFlow(){
        List<GuideOutModel> data=new ArrayList<>();
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        GuideLiveOutRVAdapter adapter=new GuideLiveOutRVAdapter();
        adapter.setEmptyLayout(mEmptyLayout);
        adapter.setData(data);
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
                    Log.e("test","first: "+firstVisibleItemPosition);
                }
            }
        });
        mRV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                Log.e("test-out","x: "+x+"   y:"+y+"   rawX:"+rawX + "   rawY: "+rawY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mRV.requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }
        });
    }

    private void requestGuideLives(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("tab_id", "");
        params.put("page_size", "10");
        params.put("page", "1");
        params.put("method", "InterestingGuideVideo");
        OkHttpUtil.getInstance().get(params, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                startFlow();
                Log.e("test",response);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
