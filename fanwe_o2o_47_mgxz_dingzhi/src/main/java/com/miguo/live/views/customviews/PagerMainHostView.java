package com.miguo.live.views.customviews;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.SellerDetailInfo;
import com.fanwe.seller.model.checkShopCollect.ModelCheckShopCollect;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.miguo.live.interf.ItemChangeListener;
import com.miguo.utils.MGUIUtil;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.List;


/**
 * Created by didik on 2016/7/29.
 * 主场页面
 */
public class PagerMainHostView extends ScrollView implements View.OnClickListener, ItemChangeListener, CallbackView {
    private Context mContext;
    private ImageView mIv_img;//主图片
    private TextView mTv_title;//大标题
    private TextView mTv_num;//数量
    private TextView mTv_price;//价格
    private TextView mTv_location_type;//地点和类型
    private TextView mTv_keywords;//关键词
    private TextView mTv_location;//地点
    private TextView mTv_phone_num;//电话号码
    private TextView mCollect;//按钮,收藏
    private RatingBar mRatingBar;//评分

    private CallbackView mCallbackView;
    /**
     * 门店详情。
     */
    private SellerDetailInfo mSellerDetailInfo;
    private SellerHttpHelper mSellerHttpHelper;
    /***
     * 当前是否收藏的状态。
     */
    private String collectState = "0";

    public PagerMainHostView(Context context, CallbackView mCallbackView) {
        super(context);
        this.mCallbackView = mCallbackView;
        init(context);
    }

    public PagerMainHostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PagerMainHostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        mSellerHttpHelper = new SellerHttpHelper(mContext, this);
        mSellerHttpHelper.checkShopCollect(CurLiveInfo.getShopID());
        LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_mainhost, this);
        mIv_img = ((ImageView) this.findViewById(R.id.iv_img));
        mTv_title = ((TextView) this.findViewById(R.id.tv_title));
        mRatingBar = ((RatingBar) this.findViewById(R.id.rb));
        mTv_num = ((TextView) this.findViewById(R.id.tv_num));
        mTv_price = ((TextView) this.findViewById(R.id.tv_price));
        mTv_location_type = ((TextView) this.findViewById(R.id.tv_location_type));
        mCollect = ((TextView) this.findViewById(R.id.collect));
        mTv_keywords = ((TextView) this.findViewById(R.id.tv_keywords));
        mTv_location = ((TextView) this.findViewById(R.id.tv_location));
        mTv_phone_num = ((TextView) this.findViewById(R.id.tv_phone_num));
        mCollect.setOnClickListener(this);
        updateViewValue();
    }

    @Override
    public void onClick(View v) {
        if (v == mCollect) {
            clickCollection();
        }
    }


    public void updateViewValue() {
        if (mSellerDetailInfo != null) {
            String img = mSellerDetailInfo.getImg();
            if (!TextUtils.isEmpty(img)) {
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(img, mIv_img);
            } else {
                mIv_img.setImageResource(R.drawable.nopic);
            }
            mTv_title.setText(mSellerDetailInfo.getTitle());
            mTv_num.setText(mSellerDetailInfo.getAvg_grade_num() + "条");
            String avg_grage = mSellerDetailInfo.getAvg_grade();
            if (!TextUtils.isEmpty(avg_grage)) {
                mRatingBar.setRating(3);
            } else {
                float value = Float.valueOf(avg_grage);
                mRatingBar.setRating(value);
            }
            mTv_location_type.setText(mSellerDetailInfo.getAreaName());
            mTv_keywords.setText(Html.fromHtml(mSellerDetailInfo.getMain_buss()));
            mTv_location.setText("地址：" + mSellerDetailInfo.getAddress());
            mTv_phone_num.setText("电话:" + mSellerDetailInfo.getTel());
            String price = mSellerDetailInfo.getRef_avg_price();
            if (!TextUtils.isEmpty(price)) {
                mTv_price.setText("Y " + price + "元/人");
            }
        }
    }

    /**
     * 点击了收藏
     */
    private void clickCollection() {
        if (TextUtils.isEmpty(CurLiveInfo.getShopID())) {
            return;
        }
        if ("1".equals(this.collectState)) {
            mSellerHttpHelper.deleteShopCollect(CurLiveInfo.getShopID());
        } else {
            mSellerHttpHelper.postShopCollect(CurLiveInfo.getShopID());
        }
    }

    public void setmSellerDetailInfo(SellerDetailInfo mSellerDetailInfo) {
        this.mSellerDetailInfo = mSellerDetailInfo;
    }

    @Override
    public void onEntityChange() {
        updateViewValue();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    public void setCollectState(final String collectState) {

        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(collectState) && "1".equals(collectState)) {
                    mCollect.setText("已收藏");
                } else {
                    mCollect.setText("收藏");
                }
            }
        });


    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method) {
            case SellerConstants.CHECK_SHOP_COLLECT:
                if (!SDCollectionUtil.isEmpty(datas)) {
                    ModelCheckShopCollect temp = (ModelCheckShopCollect) datas.get(0);
                    collectState = temp.getCollect();
                    setCollectState(collectState);
                }
                break;
            case SellerConstants.SHOP_COLLECT_DELETE:
                this.collectState = "0";
                setCollectState("0");
                break;
            case SellerConstants.SHOP_COLLECT_POST:
                this.collectState = "1";
                setCollectState("1");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }
}
