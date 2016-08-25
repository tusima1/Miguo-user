package com.fanwe.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.AlbumActivity;
import com.fanwe.LoginActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.common.CommonInterface;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDBasePagerAdapter.SDBasePagerAdapterOnItemClickListener;
import com.fanwe.library.adapter.SDSimpleAdvsAdapter;
import com.fanwe.library.customview.SDSlidingPlayView;
import com.fanwe.library.customview.SDSlidingPlayView.SDSlidingPlayViewOnPageChangeListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.Deal_attrModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.umeng.UmengEventStatistics;
import com.fanwe.utils.SDFormatUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.model.pagermodel.BaoBaoEntity;
import com.miguo.live.presenters.ShoppingCartHelper;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDEventManager;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TuanDetailImagePriceFragment extends TuanDetailBaseFragment implements CallbackView {
    @ViewInject(R.id.frag_tuan_detail_first_spv_image)
    private SDSlidingPlayView mSpvImage = null;

    @ViewInject(R.id.frag_tuan_detail_first_tv_current_price)
    private TextView mTvCurrentPrice = null;

    @ViewInject(R.id.frag_tuan_detail_first_tv_original_price)
    private TextView mTvOriginalPrice = null;

    @ViewInject(R.id.frag_tuan_detail_first_btn_buy_goods)
    private Button mBtnBuyGoods = null;

    private SDSimpleAdvsAdapter<String> mAdapter;

    private String youhuiPrice;

    private String curPriceFormat;
    /**
     * 分销用户ID。
     */
    private String fx_user_id;

    private ShoppingCartHelper mShoppingCartHelper;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_tuan_detail_image_price);
    }

    @Override
    protected void init() {
        mTvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        initSlidingPlayView();
        registeClick();
        mShoppingCartHelper = new ShoppingCartHelper(getContext(), this);
        bindDataByGoodsModel();
    }

    private void bindDataByGoodsModel() {
        if (!toggleFragmentView(mDealModel)) {
            return;
        }

        bindGoodsImage(mDealModel.getImages());
        if (mDealModel.getIs_first() - mDealModel.getCheck_first() > 0) {
            youhuiPrice = "￥" + mDealModel.getIs_first_price();
        } else {
            curPriceFormat = mDealModel.getCurrent_priceFormat();
        }

        String oriPriceFormat = mDealModel.getOrigin_priceFormat();

        switch (mDealModel.getBuy_type()) {
            case 1:
                SDViewBinder.setTextView(mTvOriginalPrice, null);
                SDViewBinder.setTextView(mTvCurrentPrice, mDealModel.getReturn_score_show() + "积分");
                mBtnBuyGoods.setText("立即兑换");
                break;
            default:
                if (mDealModel.getIs_first() - mDealModel.getCheck_first() > 0) {
                    SDViewBinder.setTextView(mTvCurrentPrice, youhuiPrice, "未找到");
                } else {
                    SDViewBinder.setTextView(mTvCurrentPrice, curPriceFormat, "未找到");
                }
                SDViewBinder.setTextView(mTvOriginalPrice, oriPriceFormat, "未找到");
                mBtnBuyGoods.setText("立即购买");
                break;
        }
    }

    /**
     * 绑定轮播图片
     *
     * @param listUrl
     */
    private void bindGoodsImage(List<String> listUrl) {
        if (SDCollectionUtil.isEmpty(listUrl)) {
            String icon = mDealModel.getIcon();
            if (!TextUtils.isEmpty(icon)) {
                listUrl = new ArrayList<String>();
                listUrl.add(mDealModel.getIcon());
            }
        }

        mAdapter = new SDSimpleAdvsAdapter<String>(listUrl, getActivity());
        mAdapter.setmView(mSpvImage);
        mAdapter.setmListenerOnItemClick(new SDBasePagerAdapterOnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mDealModel != null) {
                    List<String> listOimage = mDealModel.getOimages();
                    if (!SDCollectionUtil.isEmpty(listOimage)) {
                        Intent intent = new Intent(getActivity(), AlbumActivity.class);
                        intent.putExtra(AlbumActivity.EXTRA_IMAGES_INDEX, position);
                        intent.putStringArrayListExtra(AlbumActivity.EXTRA_LIST_IMAGES, (ArrayList<String>) listOimage);
                        startActivity(intent);
                    }
                }
            }
        });
        mSpvImage.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        if (mSpvImage != null) {
            mSpvImage.startPlay();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mSpvImage != null) {
            mSpvImage.stopPlay();
        }
        super.onPause();
    }

    private void initSlidingPlayView() {
        mSpvImage.setmImageNormalResId(R.drawable.ic_main_dot2_normal);
        mSpvImage.setmImageSelectedResId(R.drawable.ic_main_dot2_foused);

        mSpvImage.setmListenerOnPageChange(new SDSlidingPlayViewOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void registeClick() {
        mBtnBuyGoods.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_tuan_detail_first_btn_buy_goods:
                clickBuyGoods();
                break;

            default:
                break;
        }
    }

    /**
     * 加入本地购物车。
     */
    private void addToLocalShopping() {
        ShoppingCartInfo shoppingCartInfo = new ShoppingCartInfo();
        shoppingCartInfo.setId(mDealModel.getId());
        shoppingCartInfo.setFx_user_id(this.fx_user_id);
        shoppingCartInfo.setNumber("1");
        shoppingCartInfo.setImg(mDealModel.getIcon());
        shoppingCartInfo.setLimit_num(mDealModel.getMax_num());
        shoppingCartInfo.setIs_first(mDealModel.getIs_first() + "");
        shoppingCartInfo.setIs_first_price(mDealModel.getIs_first_price() + "");
        shoppingCartInfo.setOrigin_price(mDealModel.getOrigin_price());
        shoppingCartInfo.setTuan_price(mDealModel.getCurrent_price());
        shoppingCartInfo.setTitle(mDealModel.getSub_name());
        shoppingCartInfo.setBuyFlg(mDealModel.getTime_status()+"");

        LocalShoppingcartDao.insertModel(shoppingCartInfo);
    }

    private void clickBuyGoods() {
        if (mDealModel == null || TextUtils.isEmpty(mDealModel.getId())) {
            return;
        }
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            //当前已经登录，
            addGoodsToShoppingPacket();
        } else {
            //当前未登录.
            int status = mDealModel.getTime_status();
            if (status == 0) {
                SDToast.showToast("商品活动未开始。");
                return;
            } else if (status == 1) {
                addToLocalShopping();
                goToShopping();
            } else if (status == 2) {
                SDToast.showToast("商品已经过期。");
                return;
            }
        }

        UmengEventStatistics.sendEvent(getActivity(), UmengEventStatistics.BUY);

    }

    /**
     * 添加到购物车。
     */
    public void addGoodsToShoppingPacket() {
        String lgn_user_id = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
        String goods_id = mDealModel.getId();
        String cart_type = "1";
        String add_goods_num = "1";
        if (mShoppingCartHelper != null) {
            mShoppingCartHelper.addToShoppingCart("", fx_user_id, lgn_user_id, goods_id, cart_type, add_goods_num);
        }
    }



    public void goToShopping() {

        Intent intent = new Intent(getActivity(),
                ShopCartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (mSpvImage != null) {
            mSpvImage.stopPlay();
        }
        super.onDestroy();
    }


    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method) {
            case ShoppingCartconstants.SHOPPING_CART:
                goToShopping();
                break;
            case ShoppingCartconstants.SHOPPING_CART_ADD:
                goToShopping();
                break;
            default:
                break;
        }

    }

    @Override
    public void onFailue(String responseBody) {
        SDToast.showToast(responseBody);

    }
}