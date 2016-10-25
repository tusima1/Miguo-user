package com.miguo.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.StoreLocationActivity;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.ServerUrl;
import com.fanwe.fragment.StoreLocationFragment;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.library.utils.SDIntentUtil;
import com.fanwe.model.Store_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.MGDictUtil;
import com.fanwe.view.LoadMoreRecyclerView;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiShopDetailLiveAdapter;
import com.miguo.adapter.HiShopDetailRecommendAdapter;
import com.miguo.adapter.ShopDetailPagerAdapter;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.dao.CollectShopDao;
import com.miguo.dao.HiShopDetailDao;
import com.miguo.dao.RepresentMerchantDao;
import com.miguo.dao.impl.CollectShopDaoImpl;
import com.miguo.dao.impl.HiShopDetailDaoImpl;
import com.miguo.dao.impl.RepresentMerchantDaoImpl;
import com.miguo.entity.HiShopDetailBean;
import com.miguo.fake.ShopDetailPagerItemFakeData;
import com.miguo.fragment.ShopDetailPagerItemFragmet;
import com.miguo.listener.HiShopDetailListener;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.ui.view.ShopDetailTagView;
import com.miguo.ui.view.ShopDetailViewPager;
import com.miguo.view.CollectShopView;
import com.miguo.view.HiShopDetailView;
import com.miguo.view.RepresentMerchantView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/19.
 */
public class HiShopDetailCategory extends Category implements HiShopDetailView, CollectShopView, RepresentMerchantView{


    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    @ViewInject(R.id.back)
    ImageView back;

    @ViewInject(R.id.share)
    ImageView share;

    /**
     * 轮播图
     */
    @ViewInject(R.id.viewpager)
    ShopDetailViewPager viewPager;

    ShopDetailPagerAdapter viewpagerAdapter;
    @ViewInject(R.id.indicator_circle)
    CircleIndicator indicator;

    /**
     * 标题
     */
    @ViewInject(R.id.shop_name)
    TextView shopName;


    /**
     * 标签
     */
    @ViewInject(R.id.tags)
    ShopDetailTagView tags;

    /**
     * 人均消费
     */
    @ViewInject(R.id.ava_price)
    TextView price;

    /**
     * 人气值
     */
    @ViewInject(R.id.hot)
    TextView hots;

    @ViewInject(R.id.collect)
    ImageView collect;

    /**
     * 精选推荐
     */
    @ViewInject(R.id.recmmend)
    LoadMoreRecyclerView recommend;
    HiShopDetailRecommendAdapter recommendAdapter;
    /**
     * 地址
     */
    @ViewInject(R.id.address)
    TextView address;
    /**
     * 营业时间
     */
    @ViewInject(R.id.time)
    TextView time;
    /**
     * 距离
     */
    @ViewInject(R.id.distance)
    TextView distance;

    /**
     * call打电话
     */
    @ViewInject(R.id.call)
    ImageView call;

    /**
     * 地图
     */
    @ViewInject(R.id.location)
    ImageView location;

    /**
     * 适合人群
     */
    @ViewInject(R.id.crowd_people)
    TextView crowdPeople;

    /**
     * 在现场
     */
    @ViewInject(R.id.live)
    LoadMoreRecyclerView live;
    HiShopDetailLiveAdapter liveAdapter;
    /**
     * 是否已代言
     */
    @ViewInject(R.id.represent_message)
    TextView representMessage;
    @ViewInject(R.id.represent)
    TextView represent;

    HiShopDetailDao shopDetailDao;
    HiShopDetailBean.Result result;


    CollectShopDao collectShopDao;

    RepresentMerchantDao representMerchantDao;

    public HiShopDetailCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        getIntentData();
        shopDetailDao = new HiShopDetailDaoImpl(this);
        collectShopDao = new CollectShopDaoImpl(this);
        representMerchantDao = new RepresentMerchantDaoImpl(this);
        List list = new ArrayList();
        recommendAdapter = new HiShopDetailRecommendAdapter(getActivity(), list);

        List list2 = new ArrayList();
        liveAdapter = new HiShopDetailLiveAdapter(getActivity(), list2);
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {
        listener = new HiShopDetailListener(this);
    }

    @Override
    protected void setThisListener() {
        call.setOnClickListener(listener);
        location.setOnClickListener(listener);
        collect.setOnClickListener(listener);
        back.setOnClickListener(listener);
        share.setOnClickListener(listener);
        represent.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initShopDetail();
    }

    @Override
    protected void initViews() {
        setTitlePadding(titleLayout);
        initRecommendRecyclerView();
        initLiveRecyclerView();
    }


    private int mShopId = -1;

    private int mType;

    private String MerchantID = "";

    private String begin;

    private String end;

    private void getIntentData() {
        mShopId = getActivity().getIntent().getExtras().getInt(HiShopDetailActivity.EXTRA_SHOP_ID, -1);
        mType = getActivity().getIntent().getExtras().getInt("type");
        MerchantID = getActivity().getIntent().getExtras().getString(HiShopDetailActivity.EXTRA_MERCHANT_ID);
        if (mType == 15) {
            begin = getActivity().getIntent().getExtras().getString("begin_time");
            end = getActivity().getIntent().getExtras().getString("end_time");
        }
    }

    private void initShopDetail(){
        try{
            shopDetailDao.getShopDetail(MerchantID,
                    BaiduMapManager.getInstance().getBDLocation().getLongitude() + "",
                    BaiduMapManager.getInstance().getBDLocation().getLatitude() + ""
                    );
        }catch (Exception e){
            shopDetailDao.getShopDetail("",
                    "",
                    ""
                    );
        }

    }

    /**
     * 回调数据后加载
     */
    private void initViewPager(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        if(result.getShop_images() == null || result.getShop_images().size() == 0){
            HiShopDetailBean.Result.ShopImage banner = new HiShopDetailBean().new Result().new ShopImage();
            banner.setImage_url("http://www.xxx.com/1/img");
            result.getShop_images().add(banner);
        }
        for(int i = 0; i< result.getShop_images().size(); i++){
            ShopDetailPagerItemFragmet fragmet = new ShopDetailPagerItemFragmet();
            Bundle bundle = new Bundle();
            bundle.putSerializable("images",result.getShop_images().get(i));
            fragmet.setArguments(bundle);
            fragments.add(fragmet);
        }
        viewpagerAdapter = new ShopDetailPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        viewPager.setAdapter(viewpagerAdapter);
        indicator.setViewPager(viewPager);
        viewpagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    private void initRecommendRecyclerView(){
        recommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        recommend.setAdapter(recommendAdapter);
        updateRecommendRecyclerViewHeight();
    }

    private void initLiveRecyclerView(){
        live.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        live.setAdapter(liveAdapter);
        updateLiveRecycleViewrHeight();
    }


    /**
     * 点击代言
     */
    public void clickRepresent(){
        if(TextUtils.isEmpty(App.getInstance().getToken())){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            BaseUtils.jumpToNewActivity(getActivity(), intent);
            return;
        }

        representMerchantDao.getRepresentMerchant(result.getEnt_id(), result.getId());

    }

    /**
     * 点击返回
     */
    public void clickBack(){
        BaseUtils.finishActivity(getActivity());
    }

    /**
     * 点击分享
     */
    public void clickShare(){
        if (result.getShare() != null) {
            String content = result.getShare().getSummary();
            if (TextUtils.isEmpty(content)) {
                content = "欢迎来到米果小站";
            }
            String imageUrl = result.getShare().getImageurl();
            if (TextUtils.isEmpty(imageUrl)) {
                imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
                if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                    imageUrl = MGDictUtil.getShareIcon();
                }
            } else if (!imageUrl.startsWith("http")) {
                imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
                if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                    imageUrl = MGDictUtil.getShareIcon();
                }
            }
            String clickUrl = result.getShare().getClickurl();
            if (TextUtils.isEmpty(clickUrl)) {
                clickUrl = ServerUrl.SERVER_H5;
            } else {
                clickUrl = clickUrl + "/ref_id/" + App.getApplication().getmUserCurrentInfo().getUserInfoNew().getUser_id();
            }
            String title = result.getShare().getTitle();
            if (TextUtils.isEmpty(title)) {
                title = "米果小站";
            }

            UmengShareManager.share(getActivity(), title, content, clickUrl, UmengShareManager.getUMImage(getActivity(), imageUrl), null);
        } else {
            MGToast.showToast("无分享内容");
        }
    }

    /**
     * 点击位置
     */
    public void clickLocation(){
        if(result != null){
            Intent intent = new Intent(getActivity(), StoreLocationActivity.class);
            Store_infoModel store_infoModel=new Store_infoModel();
            store_infoModel.setAddress(result.getAddress());
            store_infoModel.setXpoint(DataFormat.toDouble(result.getGeo_x()));
            store_infoModel.setYpoint(DataFormat.toDouble(result.getGeo_y()));
            store_infoModel.setId(result.getId());
            store_infoModel.setName(result.getShop_name());
            store_infoModel.setTel(result.getTel());

            intent.putExtra(StoreLocationFragment.EXTRA_MODEL_MERCHANTITEMACTMODEL, store_infoModel);
            BaseUtils.jumpToNewActivity(getActivity(),intent);
        }
    }

    /**
     * 点击电话
     */
    public void clickCall(){
        if(result == null){
            return;
        }
        if(TextUtils.isEmpty(result.getTel())){
            showToast("没有电话！");
            return ;
        }
        Intent intent = SDIntentUtil.getIntentCallPhone(result.getTel());
        SDActivityUtil.startActivity(getActivity(), intent);
    }

    /**
     * 点击收藏
     */
    public void clickCollect(){
        if(TextUtils.isEmpty(App.getInstance().getToken())){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            BaseUtils.jumpToNewActivity(getActivity(), intent);
            return;
        }

        if(!result.isCollect()){
            collectShopDao.collectShop(result.getId());
        }else {
            collectShopDao.unCollectShop(result.getId());
        }

    }

    /**
     * 更新推荐商品列表高度
     */
    private void updateRecommendRecyclerViewHeight(){
        int height = recommendAdapter.getItemHeight();
        LinearLayout.LayoutParams params = getLineaLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        params.setMargins(0, dip2px(15), 0, 0);
        recommend.setLayoutParams(params);
    }

    /**
     * 更新在现场 直播列表高度
     */
    private void updateLiveRecycleViewrHeight(){
        int height = liveAdapter.getItemHeight();
        LinearLayout.LayoutParams params = getLineaLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        params.setMargins(0, dip2px(15), 0, 0);
        live.setLayoutParams(params);
    }

    /**
     * 回调成功
     */
    @Override
    public void getShopDetailSuccess(final HiShopDetailBean.Result result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updatePage(result);
            }
        });
    }

    /**
     * 更新数据
     * @param result
     */
    private void updatePage(final HiShopDetailBean.Result result){
        this.result = result;
        /**
         * 轮播图
         */
        initViewPager();
        /**
         * 商店名字
         */
        shopName.setText(result.getShop_name());
        /**
         * 门店标签
         */
        if(result.getShop_tags().getTags() != null && result.getShop_tags().getTags().length > 0){
            this.tags.init(result.getShop_tags().getTags());
        }
        /**
         * 价格
         */
        price.setText(result.getRef_avg_price() + "元/每人");
        /**
         * 人气值
         */
        hots.setText("人气值 " + result.getCollect_number());
        /**
         *  收藏
         */
        collect.setImageResource(result.isCollect() ? R.drawable.ic_collect_yes : R.drawable.ic_collect_no);
        /**
         * 地址
         */
        address.setText(result.getAddress());
        /**
         * 营业时间
         */
        time.setText("营业时间：" + result.getTrade_day());
        /**
         * 距离
         */
        distance.setText("距离 " + result.getDistanceFormat());
        /**
         * 精选列表
         */
        if(result.getTuan_list() != null && result.getTuan_list().size() > 0 ){
            recommendAdapter.notifyDataSetChanged(result.getTuan_list());
            updateRecommendRecyclerViewHeight();
        }
        /**
         * 适合人群
         */
        crowdPeople.setText(getCrowdPeopleText(result));
        /**
         * 在现场
         */
        if(result.getLive_list() != null && result.getLive_list().size() > 0){
            liveAdapter.notifyDataSetChanged(result.getLive_list());
            updateLiveRecycleViewrHeight();
        }
        /**
         * 是否已代言
         */
        updateRepresent();
    }

    public void updateRepresent(){
        /**
         * 是否已代言
         */
        represent.setVisibility(result.isEndorsement() ? View.GONE : View.VISIBLE);
        representMessage.setText(result.isEndorsement() ? "您已是这家店的代言人，快带领亲朋好友走上人生巅峰" : "建议消费过后，再申请代言人资格");
    }

    private String getCrowdPeopleText(final HiShopDetailBean.Result result){
        String tag = "";
        if(result.getCrowd_tags() != null && result.getCrowd_tags().getTags() != null && result.getCrowd_tags().getTags().length > 0){
            for(int i = 0; i<result.getCrowd_tags().getTags().length; i++){
                tag = i == 0 ? tag + result.getCrowd_tags().getTags()[i] : tag + " " + result.getCrowd_tags().getTags()[i];
            }
        }
        return tag;
    }

    @Override
    public void getShopDetailError() {

    }

    /**
     * 收藏和取消收藏
     */
    @Override
    public void collectSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                collect.setImageResource(R.drawable.ic_collect_yes);
                result.setIs_collect("1");
            }
        });
    }

    @Override
    public void collectError() {

    }

    @Override
    public void unCollectSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                collect.setImageResource(R.drawable.ic_collect_no);
                result.setIs_collect("0");
            }
        });
    }

    @Override
    public void unCollectError() {

    }

    /**
     * 点击代言回调
     */
    @Override
    public void getRepresentMerchantSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.setIs_endorsement("1");
                updateRepresent();
            }
        });


    }

    @Override
    public void getRepresentMerchantError(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(message);
            }
        });
    }
}
