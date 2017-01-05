package com.miguo.groupon.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.base.CallbackView;
import com.fanwe.base.PageBean;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.util.CollectionUtils;
import com.fanwe.utils.DataFormat;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.entity.BodyFeaturedGroupBuy;
import com.miguo.entity.ModelFeaturedGroupBuy;
import com.miguo.entity.ResultFeaturedGroupBuy;
import com.miguo.groupon.adapters.GrouponFeaturedAdapter;
import com.miguo.groupon.model.GrouponConstants;
import com.miguo.groupon.presenters.GrouponHttpHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 精选推荐
 * Created by qiang.chen on 2017/1/5.
 */
public class FragmentFeaturedGroupon extends Fragment implements CallbackView {
    private GrouponHttpHelper grouponHttpHelper;
    private RecyclerView recyclerView;
    private GrouponFeaturedAdapter mGrouponFeaturedAdapter;
    private List<ModelFeaturedGroupBuy> datas = new ArrayList<>();
    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 10;
    private String keyword;
    private String m_longitude = BaiduMapManager.getInstance().getLongitude() + "";
    private String m_latitude = BaiduMapManager.getInstance().getLatitude() + "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_featured_groupon, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_frag_featured_groupon);
        setView();
        setListener();
        return view;
    }

    private boolean isLoadingMore;

    private void setListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //剩下1个item自动加载，各位自由选择 dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoadingMore) {
                        isLoadingMore = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void getData() {
        if (grouponHttpHelper == null) {
            grouponHttpHelper = new GrouponHttpHelper(getActivity(), this);
        }
        grouponHttpHelper.getFeaturedGroupBuy(AppRuntimeWorker.getCity_id(), String.valueOf(pageNum), String.valueOf(pageSize), keyword, m_longitude, m_latitude);
    }

    LinearLayoutManager mLayoutManager;

    private void setView() {
        mGrouponFeaturedAdapter = new GrouponFeaturedAdapter(getActivity(), datas);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mGrouponFeaturedAdapter);
    }

    public void setData(List<ModelFeaturedGroupBuy> models) {
        datas.clear();
        datas.addAll(models);
        if (mGrouponFeaturedAdapter != null)
            mGrouponFeaturedAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        isRefresh = true;
        pageNum = 1;
        getData();
    }

    /**
     * 加载更多数据
     */
    public void loadMore() {
        if (!canLoadMore()) {
            return;
        }
        isRefresh = false;
        if (!SDCollectionUtil.isEmpty(items)) {
            pageNum++;
        }
        getData();
    }

    /**
     * 判断能不能继续加载更多
     *
     * @return
     */
    private boolean canLoadMore() {
        if (pageBean != null && CollectionUtils.isValid(datas) && DataFormat.toInt(pageBean.getData_total()) <= datas.size()) {
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    private List<ModelFeaturedGroupBuy> items;
    private PageBean pageBean;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (GrouponConstants.FEATURED_GROUP_BUG.equals(method)) {
            if (CollectionUtils.isValid(datas)) {
                ResultFeaturedGroupBuy reslut = (ResultFeaturedGroupBuy) datas.get(0);
                List<BodyFeaturedGroupBuy> bodys = reslut.getBody();
                if (CollectionUtils.isValid(bodys)) {
                    items = bodys.get(0).getTuan_list();
                    pageBean = bodys.get(0).getPage();
                }
            }
            msg.what = 0;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isLoadingMore = false;
                    if (isRefresh) {
                        datas.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(items)) {
                        datas.addAll(items);
                    }
                    mGrouponFeaturedAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
