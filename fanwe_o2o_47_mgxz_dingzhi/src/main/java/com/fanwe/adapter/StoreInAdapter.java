package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.ShopCartActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.StoreIn_list;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.umeng.UmengEventStatistics;
import com.miguo.live.presenters.ShoppingCartHelper;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

public class StoreInAdapter extends SDBaseAdapter<StoreIn_list> implements CallbackView {
    private ShoppingCartHelper mShoppingCartHelper;
    //    private StoreIn_list currStoreIn_list;
    private String fx_id;

    public StoreInAdapter(List<StoreIn_list> listModel, Activity activity) {
        super(listModel, activity);
        mShoppingCartHelper = new ShoppingCartHelper(mActivity, this);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent,
                        final StoreIn_list model) {
        StoreIn_list currStoreIn_list = model;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_store_in, null);
        }
        ImageView iv_image = ViewHolder.get(R.id.iv_image, convertView);
        TextView tv_title = ViewHolder.get(R.id.tv_title, convertView);
        TextView tv_price = ViewHolder.get(R.id.tv_price, convertView);
        TextView tv_priceCurrent = ViewHolder.get(R.id.tv_priceCurrent, convertView);
        TextView tv_priceOrigin = ViewHolder.get(R.id.tv_priceOrigin, convertView);
        LinearLayout layout_priceOrigin = ViewHolder.get(R.id.layout_priceCurrent, convertView);

        Button bt_buy = ViewHolder.get(R.id.bt_buy, convertView);

        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = (int) (width * 0.242 - 5);
        SDViewUtil.setViewHeight(iv_image, height);
        if (model != null) {
            SDViewBinder.setTextView(tv_title, model.getName());
            if ("1".equals(model.getIs_delete())) {
                if (App.getInstance().getmUserCurrentInfo().getUserInfoNew() != null) {
                    fx_id = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
                } else {
                    fx_id = "";
                }
                //代言商品，显示佣金
                layout_priceOrigin.setVisibility(View.VISIBLE);
                SDViewBinder.setTextView(tv_priceCurrent, TextMoney.textFarmat3(model.getSalary()));
            } else {
                fx_id = "";
                layout_priceOrigin.setVisibility(View.GONE);
            }
            SDViewBinder.setTextView(tv_price, TextMoney.textFarmat3(model.getOrigin_price()));
            SDViewBinder.setTextView(tv_priceOrigin, TextMoney.textFarmat3(model.getCurrent_price()));
            SDViewBinder.setImageView(iv_image, model.getImg());
            tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            bt_buy.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickBuyGoods(position);
                }
            });
        }
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getId());
                intent.putExtra(TuanDetailActivity.EXTRA_FX_ID, fx_id);
                mActivity.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * 加入本地购物车。
     *
     * @param position
     */
    private void addToLocalShopping(int position) {
        StoreIn_list currStoreIn_list = mListModel.get(position);
        ShoppingCartInfo shoppingCartInfo = new ShoppingCartInfo();
        shoppingCartInfo.setId(currStoreIn_list.getId());
        shoppingCartInfo.setFx_user_id(fx_id);
        shoppingCartInfo.setNumber("1");
        shoppingCartInfo.setImg(currStoreIn_list.getImg());
        shoppingCartInfo.setLimit_num(currStoreIn_list.getMax_num());
        shoppingCartInfo.setIs_first(currStoreIn_list.getIs_first() + "");
        shoppingCartInfo.setIs_first_price(currStoreIn_list.getIs_first_price() + "");
        shoppingCartInfo.setOrigin_price(currStoreIn_list.getOrigin_price() + "");
        shoppingCartInfo.setTuan_price(currStoreIn_list.getCurrent_price() + "");
        shoppingCartInfo.setTitle(currStoreIn_list.getSub_name());
        shoppingCartInfo.setBuyFlg(currStoreIn_list.getTime_status() + "");

        LocalShoppingcartDao.insertSingleNum(shoppingCartInfo);
    }

    private void clickBuyGoods(int position) {
        StoreIn_list currStoreIn_list = mListModel.get(position);
        if (currStoreIn_list == null || TextUtils.isEmpty(currStoreIn_list.getId())) {
            return;
        }
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            //当前已经登录，
            addGoodsToShoppingPacket(position);
        } else {
            //当前未登录.
            int status = currStoreIn_list.getTime_status();
            if (status == 0) {
                MGToast.showToast("商品活动未开始。");
                return;
            } else if (status == 1) {
                addToLocalShopping(position);
                goToShopping();
            } else if (status == 2) {
                MGToast.showToast("商品已经过期。");
                return;
            }
        }

        UmengEventStatistics.sendEvent(mActivity, UmengEventStatistics.BUY);

    }

    /**
     * 添加到购物车。
     *
     * @param position
     */
    public void addGoodsToShoppingPacket(int position) {
        StoreIn_list currStoreIn_list = mListModel.get(position);
        String lgn_user_id = "";
        if (App.getInstance().getmUserCurrentInfo().getUserInfoNew() != null) {
            lgn_user_id = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
        }
        String goods_id = currStoreIn_list.getId();
        String cart_type = "1";
        String add_goods_num = "1";
        if (mShoppingCartHelper != null) {
            mShoppingCartHelper.addToShoppingCart("", fx_id, lgn_user_id, goods_id, cart_type, add_goods_num,"");
        }
    }

    public void goToShopping() {
        Intent intent = new Intent(mActivity, ShopCartActivity.class);
        mActivity.startActivity(intent);
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

    }

    @Override
    public void onFinish(String method) {

    }
}
