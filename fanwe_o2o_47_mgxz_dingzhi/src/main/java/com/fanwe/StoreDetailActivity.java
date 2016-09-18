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
import com.fanwe.library.customview.StickyScrollView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.CommentModel;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.StoreActModel;
import com.fanwe.model.StoreModel;
import com.fanwe.model.Store_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.ModelComment;
import com.fanwe.seller.model.ModelDisplayComment;
import com.fanwe.seller.model.ModelImage;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.checkShopCollect.ModelCheckShopCollect;
import com.fanwe.seller.model.getShopInfo.GoodsModelShopInfo;
import com.fanwe.seller.model.getShopInfo.ResultShopInfo;
import com.fanwe.seller.model.getShopInfo.Share;
import com.fanwe.seller.model.getShopInfo.StoreModelShopInfo;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.utils.DataFormat;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
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
    }

    private void getData() {
        SDDialogManager.showProgressDialog("请稍候...");
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(this, this);
        }
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
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(this, this);
        }
        sellerHttpHelper.checkShopCollect(MerchantID);
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

    /**
     * 分享
     */
    private void clickShare() {
        if (share != null) {
            String content = share.getSummary();
            String imageUrl = share.getImageurl();
            String clickUrl = share.getClickurl();
            String title = share.getTitle();

            UmengShareManager.share(this, title, content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
        }else {
            MGToast.showToast("无分享内容");
        }
    }

    /**
     * 收藏
     */
    private void clickCollect() {
        requestCollect();
    }

    private int isCollect;

    private void requestCollect() {
        if (isCollect == 0) {
            //0代表没有收藏
            sellerHttpHelper.postShopCollect(MerchantID);
        } else if (isCollect == 1) {
            sellerHttpHelper.deleteShopCollect(MerchantID);
        } else {
            return;
        }

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

    List<ModelCheckShopCollect> itemsCheck;

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
        } else if (SellerConstants.SHOP_INFO.equals(method)) {
            results = datas;
            msg.what = 3;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!SDCollectionUtil.isEmpty(itemsCheck)) {
                        ModelCheckShopCollect temp = itemsCheck.get(0);
                        isCollect = DataFormat.toInt(temp.getCollect());
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
                    if (!SDCollectionUtil.isEmpty(results)) {
                        ResultShopInfo result = results.get(0);
                        page_title = result.getPage_title();
                        store_info = result.getStore_info();
                        tuan_list = result.getTuan_list();
                        other_supplier_location = result.getOther_supplier_location();
                        dp_list = result.getDp_list();
                        city_name = result.getCity_name();
                        share = result.getShare();
                        setView();
                        mScrollView.onRefreshComplete();
                        SDDialogManager.dismissProgressDialog();
                    }
                    break;
            }
        }
    };

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

    private List<ResultShopInfo> results;
    private String city_name;
    private String page_title;
    private StoreModelShopInfo store_info;
    private List<GoodsModelShopInfo> tuan_list;
    private List<StoreModelShopInfo> other_supplier_location;
    private List<ModelComment> dp_list;
    private Share share;

    private void setView() {
        StoreActModel actModel = new StoreActModel();
        //Store_infoModel
        Store_infoModel bean = new Store_infoModel();
        bean.setId(store_info.getId());
        bean.setName(store_info.getShop_name());
        bean.setAddress(store_info.getAddress());
        bean.setTel(store_info.getTel());
        bean.setPreview(store_info.getIndex_img());
        bean.setAvg_point(store_info.getAvg_grade());
        bean.setShare_url(store_info.getShare());
        bean.setDp_count(store_info.getDp_count());
        bean.setShare_url(store_info.getShare());
        bean.setXpoint(DataFormat.toDouble(store_info.getGeo_x()));
        bean.setYpoint(DataFormat.toDouble(store_info.getGeo_y()));
        actModel.setStore_info(bean);
        //other_supplier_location
        List<StoreModel> otherStores = new ArrayList<>();
        if (!SDCollectionUtil.isEmpty(other_supplier_location)) {
            for (StoreModelShopInfo store_info : other_supplier_location) {
                StoreModel beanOther = new StoreModel();
                beanOther.setId(store_info.getId());
                beanOther.setName(store_info.getShop_name());
                beanOther.setAddress(store_info.getAddress());
                beanOther.setTel(store_info.getTel());
                beanOther.setPreview(store_info.getPreview());
                beanOther.setXpoint(DataFormat.toDouble(store_info.getGeo_x()));
                beanOther.setYpoint(DataFormat.toDouble(store_info.getGeo_y()));
                otherStores.add(beanOther);
            }
        }
        actModel.setOther_supplier_location(otherStores);
        //tuan_list
        List<GoodsModel> tuanGoodsModels = new ArrayList<>();
        if (!SDCollectionUtil.isEmpty(tuan_list)) {
            for (GoodsModelShopInfo goodsModelShopInfo : tuan_list) {
                GoodsModel beanGoodsModel = new GoodsModel();
                beanGoodsModel.setId(goodsModelShopInfo.getId());
                beanGoodsModel.setName(goodsModelShopInfo.getName());
                beanGoodsModel.setSub_name(goodsModelShopInfo.getShort_name());
                beanGoodsModel.setBrief(goodsModelShopInfo.getTuan_introdution());
                beanGoodsModel.setIcon(goodsModelShopInfo.getIcon());
                beanGoodsModel.setOrigin_price(DataFormat.toDouble(goodsModelShopInfo.getOrigin_price()));
                beanGoodsModel.setCurrent_price(DataFormat.toDouble(goodsModelShopInfo.getTuan_price()));
                beanGoodsModel.setBuy_count(DataFormat.toInt(goodsModelShopInfo.getBuy_count()));

                tuanGoodsModels.add(beanGoodsModel);
            }
        }
        actModel.setTuan_list(tuanGoodsModels);
        //dp_list
        List<CommentModel> commentModels = new ArrayList<>();
        if (!SDCollectionUtil.isEmpty(dp_list)) {
            for (ModelComment commentModelShopInfo : dp_list) {
                CommentModel beanCommentModel = new CommentModel();
                beanCommentModel.setId(commentModelShopInfo.getId());
                beanCommentModel.setCreate_time(commentModelShopInfo.getCreate_time());
                beanCommentModel.setContent(commentModelShopInfo.getContent());
                beanCommentModel.setPoint(commentModelShopInfo.getPoint());
                beanCommentModel.setUser_name(commentModelShopInfo.getNick());
                //缩略图
                List<String> images = new ArrayList<>();
                if (!SDCollectionUtil.isEmpty(commentModelShopInfo.getImages())) {
                    for (ModelImage imageShopInfo : commentModelShopInfo.getImages()) {
                        images.add(imageShopInfo.getImage());
                    }
                }
                beanCommentModel.setImages(images);
                //原图
                List<String> oimages = new ArrayList<>();
                if (!SDCollectionUtil.isEmpty(commentModelShopInfo.getOimages())) {
                    for (ModelImage imageShopInfo : commentModelShopInfo.getOimages()) {
                        oimages.add(imageShopInfo.getImage());
                    }
                }
                beanCommentModel.setOimages(oimages);

                commentModels.add(beanCommentModel);
            }
        }
        actModel.setDp_list(commentModels);
        //ModelDisplayComment
        ModelDisplayComment modelDisplayComment = new ModelDisplayComment();
        modelDisplayComment.setPageTitle(page_title);
        modelDisplayComment.setMessage_count(store_info.getDp_count());
        modelDisplayComment.setBuy_dp_avg(store_info.getAvg_grade());
        modelDisplayComment.setStar_1(store_info.getDp_count_1());
        modelDisplayComment.setStar_2(store_info.getDp_count_2());
        modelDisplayComment.setStar_3(store_info.getDp_count_3());
        modelDisplayComment.setStar_4(store_info.getDp_count_4());
        modelDisplayComment.setStar_5(store_info.getDp_count_5());
        modelDisplayComment.setAllow_dp(DataFormat.toInt("1"));
        actModel.setModelDisplayComment(modelDisplayComment);

        addFragments(actModel);

    }

}