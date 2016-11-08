package com.fanwe.seller.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.didikee.uilibs.views.MaxHeightListView;
import com.fanwe.base.CallbackView2;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.SpecialTopicAdapter;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getSpecialTopic.DetailListBean;
import com.fanwe.seller.model.getSpecialTopic.ModelSpecialTopic;
import com.fanwe.seller.model.getSpecialTopic.PageBean;
import com.fanwe.seller.model.getSpecialTopic.TopicBean;
import com.fanwe.seller.presenters.SellerNewHttpHelper;
import com.fanwe.utils.DataFormat;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.definition.IntentKey;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;

import java.util.List;

public class SpecialTopicActivity extends Activity implements View.OnClickListener, CallbackView2, AdapterView.OnItemClickListener {

    private PullToRefreshScrollView mPscrollView;
    private TextView mTv_show;
    private TextView mTv_show_sub;
    private ImageView mIv_img;
    private ImageView mIv_left;
    private MaxHeightListView mMaxListView;
    private SellerNewHttpHelper httpHelper;

    private boolean isLoadMore=false;
    private SpecialTopicAdapter adapter;
//    private String id="c0d21dfd-86d7-48ac-8c8c-085759df1243";//请求id
    private String id="";//请求id
    private PageBean page;
    private List<DetailListBean> temp_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_topic);
        init();
    }

    private void init() {
        initView();
        httpHelper = new SellerNewHttpHelper(this);
        getIntentData();
    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            id = extras.getString(IntentKey.SPECIAL_TOPIC_ID, "");
        }
        if (TextUtils.isEmpty(id)){
            finish();
        }
    }

    private void initView() {
        mPscrollView = ((PullToRefreshScrollView) findViewById(R.id.ps_all));
        mTv_show = ((TextView) findViewById(R.id.tv_show));
        mTv_show_sub = ((TextView) findViewById(R.id.tv_sub_show));
        mIv_img = ((ImageView) findViewById(R.id.iv_img));

        mIv_left = ((ImageView) findViewById(R.id.iv_left));
        mMaxListView = ((MaxHeightListView) findViewById(R.id.max_listView));
        mMaxListView.setOnItemClickListener(this);


        mIv_left.setOnClickListener(this);

        mPscrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPscrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                isLoadMore=false;
                httpHelper.getTopicDetail(id,"1","10","","");
                mPscrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                isLoadMore=true;
                if (page==null){
                    isLoadMore=false;
                    httpHelper.getTopicDetail(id,"1","10","","");
                }else {
                    int currentPage = DataFormat.toInt(page.getPage());
                    int totalPage = DataFormat.toInt(page.getPage_total());
                    if (currentPage<totalPage){
                        httpHelper.getTopicDetail(id,currentPage+1+"","10","","");
                    }else if (currentPage==totalPage){
                        httpHelper.getTopicDetail(id,currentPage+"","10","","");
                    }else {
                        MGToast.showToast("没有更多数据");
                        mPscrollView.onRefreshComplete();
                    }
                }
            }
        });
        mPscrollView.setRefreshing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onSuccess(String responseBody) {
    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method){
            case SellerConstants.SPECIAL_TOPIC:
                BindData(datas);
                break;

        }
    }

    private void BindData(List datas) {
        if (datas!=null && datas.size()>0){
            ModelSpecialTopic modelSpecialTopic = (ModelSpecialTopic) datas.get(0);
            if (modelSpecialTopic==null){
                MGToast.showToast("数据错误!");
                finish();
                return;
            }

            if (isLoadMore){
                //TODO update data
                int currentPage = DataFormat.toInt(page.getPage());
                int totalPage = DataFormat.toInt(page.getPage_total());
                if (currentPage == totalPage){
                    adapter.removeData(temp_list);
                }
                temp_list = modelSpecialTopic.getDetail_list();
                if (adapter!=null){
                    adapter.addData(temp_list);
                }
            }else {
                page = modelSpecialTopic.getPage();
                //bind top layout data
                TopicBean topic = modelSpecialTopic.getTopic();
                SDViewBinder.setImageView(topic.getIcon(),mIv_img);
                mTv_show.setText(topic.getTitle());
                mTv_show_sub.setText(topic.getDescript());

                //bind listView
                adapter = new SpecialTopicAdapter();
                mMaxListView.setAdapter(adapter);
                mMaxListView.setFocusable(false);
                temp_list = modelSpecialTopic.getDetail_list();
                adapter.setData(temp_list);

            }
        }
    }

    @Override
    public void onFailue(String responseBody) {
        mPscrollView.onRefreshComplete();
    }

    @Override
    public void onFinish(String method) {
        mPscrollView.onRefreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<DetailListBean> data = adapter.getData();
        if (data!=null){
            DetailListBean detailListBean = data.get(position);
            String type_id = detailListBean.getType_id();
            String type = detailListBean.getType();
            if (TextUtils.isEmpty(type_id)){
                MGLog.e("专题跳转id为null!");
                return;
            }
            if ("1".equals(type)){
                //TODO 跳转商品详情
                Intent intent=new Intent(this,GoodsDetailActivity.class);
                intent.putExtra(GoodsDetailActivity.EXTRA_GOODS_ID,type_id);
                startActivity(intent);
            }else if ("2".equals(type)){
                //TODO 跳转门店列表
                Intent intent=new Intent(this,HiShopDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString(HiShopDetailActivity.EXTRA_MERCHANT_ID,type_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }else {
                MGLog.e("专题跳转异常!");
            }
        }
    }
}
