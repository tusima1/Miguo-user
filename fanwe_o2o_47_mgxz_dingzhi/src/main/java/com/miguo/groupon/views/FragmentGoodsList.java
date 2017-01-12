package com.miguo.groupon.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fanwe.adapter.TuanGruopListAdapter;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.GoodsGroupModel;
import com.fanwe.model.GoodsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getGroupList.ModelDealData;
import com.fanwe.seller.model.getGroupList.ModelGroupList;
import com.fanwe.seller.model.getTuanSearch.ResultGetTuanSearch;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.seller.util.CollectionUtils;
import com.fanwe.utils.DataFormat;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miguo.groupon.listener.IDataInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 团购列表
 * Created by qiang.chen on 2017/1/5.
 */

public class FragmentGoodsList extends Fragment implements CallbackView {

    private PullToRefreshListView mPtrlvContent = null;
    private TuanGruopListAdapter mAdapter = null;
    private List<GoodsGroupModel> datas = new ArrayList<>();
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isRefresh;
    private SellerHttpHelper sellerHttpHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_groupon_list, container, false);
        setView(view);
        setListener();
        return view;
    }

    private void setListener() {
        mPtrlvContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMore();
            }
        });
    }

    private void setView(View view) {
        mPtrlvContent = (PullToRefreshListView) view.findViewById(R.id.ptrlv_content);
        mAdapter = new TuanGruopListAdapter(datas, getActivity());
        mPtrlvContent.setAdapter(mAdapter);
        mPtrlvContent.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    public String keyword;
    public String area_one;
    public String area_two;
    public String category_one;
    public String category_two;
    public String filter;
    public String sort_type;

    private void getData() {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(getActivity(), this);
        }
        sellerHttpHelper.getTuanSearch(area_one, area_two, category_one, category_two, filter, keyword, sort_type, pageNum, pageSize);
    }

    public void setData(List<GoodsGroupModel> models) {
        datas.clear();
        datas.addAll(models);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
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
        isRefresh = false;
        if (!SDCollectionUtil.isEmpty(items)) {
            pageNum++;
        }
        getData();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    private List<ModelGroupList> items;
    private List<ResultGetTuanSearch> results;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (SellerConstants.TUAN_SEARCH.equals(method)) {
            results = datas;
            if (CollectionUtils.isValid(results)) {
                ResultGetTuanSearch bean = results.get(0);
                items = bean.getTuan_list();
                msg.what = 0;
            }
        }
        mHandler.sendMessage(msg);
    }

    private boolean flagFirst = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (flagFirst) {
                        flagFirst = false;
                        mPtrlvContent.postDelayed(new Runnable() {
                            public void run() {
                                mPtrlvContent.setFocusable(true);
                            }
                        }, 100);
                    }
                    if (isRefresh) {
                        datas.clear();
                    }
                    convertData();
                    mAdapter.notifyDataSetChanged();
                    mPtrlvContent.onRefreshComplete();
                    if (mIDataInterface != null) {
                        mIDataInterface.verifyData(CollectionUtils.isValid(datas));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void convertData() {
        if (!SDCollectionUtil.isEmpty(items)) {
            for (ModelGroupList bean : items) {
                GoodsGroupModel goodsGroupModel = new GoodsGroupModel();
                goodsGroupModel.setId(bean.getId());
                goodsGroupModel.setName(bean.getName());
                goodsGroupModel.setAddress(bean.getAddress());
                goodsGroupModel.setAvg_point(DataFormat.toFloat(bean.getAvg_grade()));
                goodsGroupModel.setDiscount_pay(DataFormat.toInt(bean.getDiscount_pay()));
                goodsGroupModel.setDistance(DataFormat.toDouble(bean.getDistance()));
                goodsGroupModel.setPreview(bean.getPreview());
                goodsGroupModel.setIs_verify(DataFormat.toInt(bean.getIs_verify()));
                goodsGroupModel.setIs_youhui(DataFormat.toInt(bean.getIs_youhui()));
                goodsGroupModel.setTel(bean.getTel());
                goodsGroupModel.setXpoint(DataFormat.toDouble(bean.getXpoint()));
                goodsGroupModel.setYpoint(DataFormat.toDouble(bean.getYpoint()));

                List<GoodsModel> deal_data = new ArrayList<>();
                if (!SDCollectionUtil.isEmpty(bean.getDeal_data())) {
                    for (ModelDealData temp : bean.getDeal_data()) {
                        GoodsModel goodsModel = new GoodsModel();
                        goodsModel.setId(temp.getId());
                        goodsModel.setName(temp.getName());
                        goodsModel.setSub_name(temp.getSub_name());
                        goodsModel.setIcon(temp.getIcon());
                        goodsModel.setBuy_count(DataFormat.toInt(temp.getBuy_count()));
                        goodsModel.setCurrent_price(DataFormat.toDouble(temp.getCurrent_price()));
                        goodsModel.setOrigin_price(DataFormat.toDouble(temp.getOrigin_price()));
                        goodsModel.setBrief(temp.getBrief());
                        goodsModel.setDistance(temp.getDistance());

                        deal_data.add(goodsModel);
                    }
                }
                goodsGroupModel.setDeal_data(deal_data);

                datas.add(goodsGroupModel);
            }
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

    IDataInterface mIDataInterface;

    public void setIDataInterface(IDataInterface iDataInterface) {
        mIDataInterface = iDataInterface;
    }

}
