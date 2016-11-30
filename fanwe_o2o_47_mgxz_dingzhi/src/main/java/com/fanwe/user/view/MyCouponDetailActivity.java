package com.fanwe.user.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.BaseActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.MaxHeightListView;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.user.adapters.ShopListAdapter;
import com.fanwe.user.model.getGroupBuyCoupon.ModelGroupCoupon;
import com.fanwe.user.model.getGroupBuyCoupon.ModelShopInfo2;
import com.fanwe.utils.MGStringFormatter;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.QRCodeMaker;

import java.util.List;

public class MyCouponDetailActivity extends BaseActivity {
    /** CouponlistActItemModel 实体 */
    public static final String EXTRA_COUPONLISTACTITEMMODEL = "extra_couponlistactitemmodel";

    @ViewInject(R.id.ll_top)
    private LinearLayout mLlTop = null;

    @ViewInject(R.id.act_my_coupon_detail_iv_coupon_image)
    private ImageView mIvCouponImage = null;

    @ViewInject(R.id.act_my_coupon_detail_tv_coupon_brief)
    private TextView mTvCouponBrief = null;

    @ViewInject(R.id.act_my_coupon_detail_tv_coupon_limit_time)
    private TextView mTvCouponLimitTime = null;

    @ViewInject(R.id.act_my_coupon_detail_tv_coupon_code)
    private TextView mTvCouponCode = null;

    @ViewInject(R.id.act_my_coupon_detail_iv_coupon_code_image)
    private ImageView mIvCouponQRCode = null;

    private ModelGroupCoupon mModel = null;
    private MaxHeightListView mListView;
    private List<ModelShopInfo2> mShopList;
    private ShopListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_my_coupon_detail);
        init();
    }

    private void init() {
        initView();
        getIntentData();
        initTitle();
        bindData();
        registerClick();
    }

    private void initView() {
        mListView = ((MaxHeightListView) findViewById(R.id.listView));
        mListView.setFocusable(false);
        mAdapter = new ShopListAdapter(mShopList);
        mListView.setAdapter(mAdapter);
    }

    private void registerClick() {
        mLlTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModel != null) {
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, mModel.getTuan_id());
                    startActivity(intent);
                }
            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mModel = (ModelGroupCoupon) intent.getSerializableExtra(EXTRA_COUPONLISTACTITEMMODEL);
        if (mModel == null) {
            MGToast.showToast("实体为空");
            finish();
        }
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(SDResourcesUtil.getString(R.string.tuan_gou_coupon_detail));
    }

    private void bindData() {
        SDViewBinder.setImageView(mModel.getIcon(), mIvCouponImage);
        //TODO 自己生成
        String password = mModel.getPassword();
        if (!TextUtils.isEmpty(password)){
            Bitmap qrCode = QRCodeMaker.createQRCode(mModel.getPassword());
            mIvCouponQRCode.setImageBitmap(qrCode);
        }
        SDViewBinder.setTextView(mTvCouponBrief, mModel.getName());

        String end_time = mModel.getEnd_time();
        if ("0".endsWith(end_time)){
            end_time="永久有效";
        }else {
            end_time= MGStringFormatter.getDate(end_time);
        }
        SDViewBinder.setTextView(mTvCouponLimitTime, end_time);
        SDViewBinder.setTextView(mTvCouponCode, password);

        mShopList= mModel.getShop_list();
        mAdapter.setData(mShopList);
    }

}