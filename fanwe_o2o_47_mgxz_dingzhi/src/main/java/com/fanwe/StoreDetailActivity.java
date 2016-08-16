package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.HoltelDetailFragment;
import com.fanwe.fragment.StoreDetailBriefFragment;
import com.fanwe.fragment.StoreDetailCommentFragment;
import com.fanwe.fragment.StoreDetailGoodsFragment;
import com.fanwe.fragment.StoreDetailInfoFragment;
import com.fanwe.fragment.StoreDetailInfoFragment.OnStoreDetailListener;
import com.fanwe.fragment.StoreDetailOtherStoreFragment;
import com.fanwe.fragment.StoreDetailTuanFragment;
import com.fanwe.fragment.StoreDetailYouhuiFragment;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.StickyScrollView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.CommentModel;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.StoreActModel;
import com.fanwe.model.StoreModel;
import com.fanwe.model.Store_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getShopInfo.CommentModelShopInfo;
import com.fanwe.seller.model.getShopInfo.GoodsModelShopInfo;
import com.fanwe.seller.model.getShopInfo.ImageShopInfo;
import com.fanwe.seller.model.getShopInfo.ResultShopInfo;
import com.fanwe.seller.model.getShopInfo.StoreModelShopInfo;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 门店详情
 *
 * @author js02
 */
public class StoreDetailActivity extends BaseActivity implements CallbackView {

    /**
     * 商家id (int)
     */
    public static final String EXTRA_MERCHANT_ID = "extra_merchant_id";
    public static final String EXTRA_SHOP_ID = "extra_shop_id";

    @ViewInject(R.id.ssv_scroll)
    private SDStickyScrollView mScrollView;

    private StoreDetailInfoFragment mFragInfo;
    private StoreDetailOtherStoreFragment mFragOtherSupplier;
    private StoreDetailBriefFragment mFragBrief;
    private StoreDetailTuanFragment mFragTuan;
    private StoreDetailGoodsFragment mFragGoods;
    private StoreDetailYouhuiFragment mFragYouhui;
    private StoreDetailCommentFragment mFragComment;

    private StoreActModel mActModel;

    private int mShopId = -1;

    private int mType;

    private String MerchantID = "";

    private String begin;

    private String end;

    protected int mDay;

    protected HoltelDetailFragment mFragHotel2;
    private SellerHttpHelper sellerHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_store_detail);
        init();
        getData();
    }

    private void getData() {
        SDDialogManager.showProgressDialog("请稍候...");

        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(this, this);
        }

        MerchantID = "4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2";

        sellerHttpHelper.getShopInfo(MerchantID, AppRuntimeWorker.getCity_id());
    }

    private void init() {
        getIntentData();
        if (mShopId <= 0 && TextUtils.isEmpty(MerchantID)) {
            SDToast.showToast("id为空");
            finish();
            return;
        }
        initTitle();
        initScrollView();
    }

    private void initScrollView() {
        mScrollView.setMode(Mode.PULL_FROM_START);
        mScrollView.setOnRefreshListener(new OnRefreshListener2<StickyScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<StickyScrollView> refreshView) {
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<StickyScrollView> refreshView) {

            }
        });
        mScrollView.setRefreshing();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(SDResourcesUtil.getString(R.string.store_detail));
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setImageLeft(R.drawable.ic_tuan_detail_share);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        clickShare();
    }

    /**
     * 分享
     */
    private void clickShare() {
        if (mActModel == null) {
            SDToast.showToast("未找到可分享内容");
            return;
        }

        Store_infoModel infoModel = mActModel.getStore_info();
        if (infoModel == null) {
            SDToast.showToast("未找到可分享内容");
            return;
        }

        String content = infoModel.getName() + infoModel.getShare_url();
        String imageUrl = infoModel.getPreview();
        String clickUrl = infoModel.getShare_url();

        UmengShareManager.share(this, "分享", content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
    }

    private void getIntentData() {
        mShopId = getIntent().getExtras().getInt(EXTRA_SHOP_ID, -1);
        mType = getIntent().getExtras().getInt("type");
        MerchantID = getIntent().getExtras().getString(EXTRA_MERCHANT_ID);
        if (mType == 15) {
            begin = getIntent().getExtras().getString("begin_time");
            end = getIntent().getExtras().getString("end_time");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        init();
        super.onNewIntent(intent);
    }

    /**
     * 加载商家详细信息
     */
    private void requestDetail() {
        RequestModel model = new RequestModel();
        model.putCtl("store");
        model.putUser();
        model.put("cate_id", mType);

        if (TextUtils.isEmpty(MerchantID)) {
            model.put("data_id", mShopId);
        } else if (mShopId <= 0) {
            model.put("data_id", MerchantID);
        } else {
            return;
        }
        //model.put("data_id",mShopId);
        SDRequestCallBack<StoreActModel> handler = new SDRequestCallBack<StoreActModel>() {

            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    mActModel = actModel;
                    addFragments(actModel);
                }
            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
                mScrollView.onRefreshComplete();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    private void addFragments(final StoreActModel model) {
        if (model == null) {
            return;
        }

        // 商家信息
        mFragInfo = new StoreDetailInfoFragment();
        mFragInfo.onSetStoreDetailListener(new OnStoreDetailListener() {

            @Override
            public void setOnHotelNumberDay(int day) {
                mDay = day;
                mFragHotel2 = new HoltelDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("number", mDay);
                bundle.putInt("type", mType);
                mFragHotel2.setArguments(bundle);
                mFragHotel2.setmStoreModel(model);
                getSDFragmentManager().replace(R.id.act_holtel_detail_fl_home, mFragHotel2);
            }
        });
        mFragInfo.setmStoreModel(model);
        Bundle bundle1 = new Bundle();
        bundle1.putInt("type", mType);
        if (mType == 15) {
            bundle1.putString("begin", begin);
            bundle1.putString("end", end);
        }
        mFragInfo.setArguments(bundle1);
        getSDFragmentManager().replace(R.id.act_store_detail_fl_info, mFragInfo);

        //其他门店
        mFragOtherSupplier = new StoreDetailOtherStoreFragment();
        mFragOtherSupplier.setmStoreModel(model);
        getSDFragmentManager().replace(R.id.act_store_detail_fl_other_merchant, mFragOtherSupplier);

        //商家介绍
        mFragBrief = new StoreDetailBriefFragment();
        mFragBrief.setmStoreModel(model);
        getSDFragmentManager().replace(R.id.act_store_detail_fl_brief, mFragBrief);
        /*//酒店房型
        mFragHotel = new HoltelDetailFragment();
		mFragHotel.setmStoreModel(model);
		getSDFragmentManager().replace(R.id.act_holtel_detail_fl_home, mFragHotel);*/

        //商家其他团购
        mFragTuan = new StoreDetailTuanFragment();
        mFragTuan.setmStoreModel(model);
        getSDFragmentManager().replace(R.id.act_store_detail_fl_tuan, mFragTuan);

        // 商家其他商品
        mFragGoods = new StoreDetailGoodsFragment();
        mFragGoods.setmStoreModel(model);
        getSDFragmentManager().replace(R.id.act_store_detail_fl_goods, mFragGoods);

        // 商家的优惠券
        mFragYouhui = new StoreDetailYouhuiFragment();
        mFragYouhui.setmStoreModel(model);
        getSDFragmentManager().replace(R.id.act_store_detail_fl_youhui, mFragYouhui);

        // 商家评价
        mFragComment = new StoreDetailCommentFragment();
        mFragComment.setmStoreModel(model);
        getSDFragmentManager().replace(R.id.act_store_detail_fl_comment, mFragComment);

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case COMMENT_SUCCESS:
                setmIsNeedRefreshOnResume(true);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onNeedRefreshOnResume() {
        getData();
        super.onNeedRefreshOnResume();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (SellerConstants.SHOP_INFO.equals(method)) {
            results = datas;
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!SDCollectionUtil.isEmpty(results)) {
                        ResultShopInfo result = results.get(0);
                        store_info = result.getStore_info();
                        tuan_list = result.getTuan_list();
                        other_supplier_location = result.getOther_supplier_location();
                        dp_list = result.getDp_list();
                        city_name = result.getCity_name();
                        setView();
                        mScrollView.onRefreshComplete();
                        SDDialogManager.dismissProgressDialog();
                    }
                    break;
            }
        }
    };

    private List<ResultShopInfo> results;
    private String city_name;
    private String page_title;
    private StoreModelShopInfo store_info;
    private List<GoodsModelShopInfo> tuan_list;
    private List<StoreModelShopInfo> other_supplier_location;
    private List<CommentModelShopInfo> dp_list;

    private void setView() {
        StoreActModel actModel = new StoreActModel();
        //Store_infoModel
        Store_infoModel bean = new Store_infoModel();
        bean.setName(store_info.getShop_name());
        bean.setAddress(store_info.getAddress());
        bean.setTel(store_info.getTel());
        bean.setPreview(store_info.getPreview());
        actModel.setStore_info(bean);
        //other_supplier_location
        List<StoreModel> otherStores = new ArrayList<>();
        if (!SDCollectionUtil.isEmpty(other_supplier_location)) {
            for (StoreModelShopInfo store_info : other_supplier_location) {
                StoreModel beanOther = new StoreModel();
                beanOther.setName(store_info.getShop_name());
                beanOther.setAddress(store_info.getAddress());
                beanOther.setTel(store_info.getTel());
                beanOther.setPreview(store_info.getPreview());

                otherStores.add(beanOther);
            }
        }
        actModel.setOther_supplier_location(otherStores);
        //tuan_list
        List<GoodsModel> tuanGoodsModels = new ArrayList<>();
        if (!SDCollectionUtil.isEmpty(tuan_list)) {
            for (GoodsModelShopInfo goodsModelShopInfo : tuan_list) {
                GoodsModel beanGoodsModel = new GoodsModel();
                beanGoodsModel.setName(goodsModelShopInfo.getName());
                beanGoodsModel.setIcon(goodsModelShopInfo.getIcon());

                tuanGoodsModels.add(beanGoodsModel);
            }
        }
        actModel.setTuan_list(tuanGoodsModels);
        //dp_list
        List<CommentModel> commentModels = new ArrayList<>();
        if (!SDCollectionUtil.isEmpty(dp_list)) {
            for (CommentModelShopInfo commentModelShopInfo : dp_list) {
                CommentModel beanCommentModel = new CommentModel();
                beanCommentModel.setContent(commentModelShopInfo.getComment());
                beanCommentModel.setPoint(commentModelShopInfo.getPoint());
                //缩略图
                List<String> images = new ArrayList<>();
                if (!SDCollectionUtil.isEmpty(commentModelShopInfo.getImages())) {
                    for (ImageShopInfo imageShopInfo : commentModelShopInfo.getImages()) {
                        images.add(imageShopInfo.getImage());
                    }
                }
                beanCommentModel.setImages(images);
                //原图
                List<String> oimages = new ArrayList<>();
                if (!SDCollectionUtil.isEmpty(commentModelShopInfo.getOimages())) {
                    for (ImageShopInfo imageShopInfo : commentModelShopInfo.getOimages()) {
                        oimages.add(imageShopInfo.getImage());
                    }
                }
                beanCommentModel.setOimages(oimages);

                commentModels.add(beanCommentModel);
            }
        }
        actModel.setDp_list(commentModels);

        addFragments(actModel);
    }

}