package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.TuanDetailAttrsFragment;
import com.fanwe.fragment.TuanDetailBuyNoticelFragment;
import com.fanwe.fragment.TuanDetailCombinedPackagesFragment;
import com.fanwe.fragment.TuanDetailComboFragment;
import com.fanwe.fragment.TuanDetailCommentFragment;
import com.fanwe.fragment.TuanDetailDetailFragment;
import com.fanwe.fragment.TuanDetailImagePriceFragment;
import com.fanwe.fragment.TuanDetailMoreDetailFragment;
import com.fanwe.fragment.TuanDetailOtherMerchantFragment;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.StickyScrollView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Deal_indexActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.checkShopCollect.ModelCheckShopCollect;
import com.fanwe.seller.model.getGroupBuyDetail.ModelGroupBuyDetail;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.umeng.UmengShareManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import java.util.List;

public class TuanDetailActivity extends BaseActivity implements CallbackView {

    /**
     * 商品id (int)
     */
    public static final String EXTRA_GOODS_ID = "extra_goods_id";
    public static final String EXTRA_HOTEL_NUM = "extra_number";

    @ViewInject(R.id.ll_add_distribution)
    private LinearLayout mLl_add_distribution;

    @ViewInject(R.id.btn_add_distribution)
    private Button mBtn_add_distribution;

    @ViewInject(R.id.act_tuan_detail_ssv_scroll)
    private SDStickyScrollView mScrollView;


    @ViewInject(R.id.act_tuan_detail_fl_attr)
    private FrameLayout mFlAttr;

    private String mId;
    private Deal_indexActModel mGoodsModel;

    private TuanDetailImagePriceFragment mFragImagePrice;
    private TuanDetailDetailFragment mFragDetail;
    //private TuanDetailRatingFragment mFragRating;

    private TuanDetailOtherMerchantFragment mFragOtherMerchant;
    private TuanDetailAttrsFragment mFragAttr;
    private TuanDetailComboFragment mFragCombo;
    private TuanDetailCombinedPackagesFragment mFragCombinedPackages;
    private TuanDetailMoreDetailFragment mFragMoreDetail;
    private TuanDetailBuyNoticelFragment mFragBuyNotice;
    private TuanDetailCommentFragment mFragComment;
    private int mNumber = 1;

    public TuanDetailAttrsFragment getTuanDetailAttrsFragment() {
        return mFragAttr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_tuan_detail);
        init();
        changeTitle();
    }

    SellerHttpHelper sellerHttpHelper;

    private void getData() {
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(this, this);
        }
        sellerHttpHelper.getGroupBuyDetail(mId);
        sellerHttpHelper.checkShopCollect(mId);
    }

    private void init() {
        getIntentData();
        if (TextUtils.isEmpty(mId)) {
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

    public void scrollToAttr() {
        SDViewUtil.scrollToViewY(mScrollView.getRefreshableView(), (int) mFlAttr.getY(), 100);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        init();
        super.onNewIntent(intent);
    }

    private void initTitle() {
        String title = SDResourcesUtil.getString(R.string.detail);

        mTitle.setMiddleTextTop(title);
        mTitle.initRightItem(2);
        mTitle.getItemRight(0).setImageLeft(R.drawable.ic_tuan_detail_share);
        mTitle.getItemRight(1).setImageLeft(R.drawable.ic_tuan_detail_un_collection);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        switch (index) {
            case 0:
                clickShare();
                break;
            case 1:
                clickCollect();
                break;

            default:
                break;
        }
    }

    private void changeTitle() {
        if (mGoodsModel != null) {
            String title = SDResourcesUtil.getString(R.string.detail);
            switch (mGoodsModel.getIs_shop()) {
                case 0:
                    title = SDResourcesUtil.getString(R.string.tuan_gou_detail);
                    break;
                case 1:
                    title = SDResourcesUtil.getString(R.string.goods_detail);
                    break;
                default:
                    break;
            }
            mTitle.setMiddleTextTop(title);
        }
    }

    private void initCollectBtn(int isCollect) {
        switch (isCollect) {
            case 0:
                mTitle.getItemRight(1).setImageLeft(R.drawable.ic_tuan_detail_un_collection);
                break;
            case 1:
                mTitle.getItemRight(1).setImageLeft(R.drawable.ic_tuan_detail_collection);
                break;

            default:
                break;
        }
    }

    /**
     * 请求商品详情接口
     */
    private void requestDetail() {
        RequestModel model = new RequestModel();
        model.putCtl("deal");
        model.put("data_id", mId);
        model.putUser();
        model.putLocation();
        SDRequestCallBack<Deal_indexActModel> handler = new SDRequestCallBack<Deal_indexActModel>() {

            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    mGoodsModel = actModel;
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

    private void getIntentData() {
        Intent intent = getIntent();
        mId = intent.getStringExtra(EXTRA_GOODS_ID);
        mNumber = intent.getIntExtra(EXTRA_HOTEL_NUM, 1);
    }

    /**
     * 添加fragment
     */
    private void addFragments(Deal_indexActModel model) {
        if (model == null) {
            return;
        }

        mFragImagePrice = new TuanDetailImagePriceFragment();
        mFragImagePrice.setmDealModel(model);
        Bundle bundle = new Bundle();
        bundle.putInt("number", mNumber);
        mFragImagePrice.setArguments(bundle);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_image_price, mFragImagePrice);

        mFragDetail = new TuanDetailDetailFragment();
        mFragDetail.setmDealModel(model);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_detail, mFragDetail);
        /*
        // ---------------评分----------------
		mFragRating = new TuanDetailRatingFragment();
		mFragRating.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_rating, mFragRating);*/

        // ---------------商品属性----------------
        mFragAttr = new TuanDetailAttrsFragment();
        mFragAttr.setmDealModel(model);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_attr, mFragAttr);

        //----------------套餐内容-------------
        mFragCombo = new TuanDetailComboFragment();
        mFragCombo.setmDealModel(model);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_combo, mFragCombo);

        // ---------------组合推荐----------------
        mFragCombinedPackages = new TuanDetailCombinedPackagesFragment();
        mFragCombinedPackages.setmDealModel(model);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_combined_packages, mFragCombinedPackages);

        // ---------------购买须知----------------
        mFragBuyNotice = new TuanDetailBuyNoticelFragment();
        mFragBuyNotice.setmDealModel(model);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_buy_notice, mFragBuyNotice);

        // ---------------更多详情----------------
        mFragMoreDetail = new TuanDetailMoreDetailFragment();
        mFragMoreDetail.setmDealModel(model);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_more_detail, mFragMoreDetail);

        // ---------------其他门店----------------
        mFragOtherMerchant = new TuanDetailOtherMerchantFragment();
        mFragOtherMerchant.setmDealModel(model);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_other_merchant, mFragOtherMerchant);

        // ---------------评论----------------
        mFragComment = new TuanDetailCommentFragment();
        mFragComment.setmDealModel(model);
        getSDFragmentManager().replace(R.id.act_tuan_detail_fl_comment, mFragComment);
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 分享
     */
    private void clickShare() {
        if (mGoodsModel != null) {
            String content = mGoodsModel.getName() + mGoodsModel.getShare_url();
            String imageUrl = null;
            String clickUrl = mGoodsModel.getShare_url();

            imageUrl = mGoodsModel.getIcon();

            if (TextUtils.isEmpty(imageUrl)) {
                List<String> listImages = mGoodsModel.getImages();
                if (!SDCollectionUtil.isEmpty(listImages)) {
                    imageUrl = listImages.get(0);
                }
            }

//			UmengSocialManager.openShare("分享", content, imageUrl, clickUrl, this, null);
            UmengShareManager.share(this, "分享", content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
        }
    }

    /**
     * 收藏
     */
    private void clickCollect() {
        requestCollect();
    }

    private void requestCollect() {
        if (isCollect == 0) {
            //0代表没有收藏
            sellerHttpHelper.postShopCollect(mId);
        } else if (isCollect == 1) {
            sellerHttpHelper.deleteShopCollect(mId);
        } else {
            return;
        }

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case COMMENT_SUCCESS:
                getData();
                break;
            case ADD_DISTRIBUTION_GOODS_SUCCESS:
                getData();
            default:
                break;
        }
    }


    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelCheckShopCollect> itemsCheck;
    List<ModelGroupBuyDetail> itemsDetail;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (SellerConstants.CHECK_SHOP_COLLECT.equals(method)) {
            //检查收藏情况
            itemsCheck = datas;
            msg.what = 0;
        } else if (SellerConstants.SHOP_COLLECT_DELETE.equals(method)) {
            //取消收藏
            msg.what = 1;
        } else if (SellerConstants.SHOP_COLLECT_POST.equals(method)) {
            //收藏
            msg.what = 2;
        } else if (SellerConstants.GROUP_BUY_DETAIL.equals(method)) {
            //团购详情
            itemsDetail = datas;
            msg.what = 3;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    private int isCollect;
    private ModelGroupBuyDetail modelGroupBuyDetail;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!SDCollectionUtil.isEmpty(itemsCheck)) {
                        ModelCheckShopCollect temp = itemsCheck.get(0);
                        isCollect = Integer.valueOf(temp.getCollect());
                        initCollectBtn(isCollect);
                    }
                    break;
                case 1:
                    //取消收藏
                    isCollect = 0;
                    initCollectBtn(isCollect);
                    break;
                case 2:
                    //收藏
                    isCollect = 1;
                    initCollectBtn(isCollect);
                    break;
                case 3:
                    if (!SDCollectionUtil.isEmpty(itemsDetail)) {
                        modelGroupBuyDetail = itemsDetail.get(0);
                        Deal_indexActModel model = new Deal_indexActModel();
                        //基本信息
                        model.setId(modelGroupBuyDetail.getId());
                        model.setName(modelGroupBuyDetail.getName());
                        model.setShare_url(modelGroupBuyDetail.getShare_url());
                        model.setCurrent_price(modelGroupBuyDetail.getTuan_price());
                        model.setOrigin_price(modelGroupBuyDetail.getOrigin_price());
                        model.setIcon(modelGroupBuyDetail.getIcon());

                        addFragments(model);
                    }
                    mScrollView.onRefreshComplete();
                    break;
            }
        }
    };
}