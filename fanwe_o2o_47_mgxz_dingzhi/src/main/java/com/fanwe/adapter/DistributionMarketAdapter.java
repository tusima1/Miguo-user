package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.DistributionStoreInActivity;
import com.fanwe.base.CallbackView;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Supplier_fx;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getRepresentMerchant.RootRepresentMerchant;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

public class DistributionMarketAdapter extends SDBaseAdapter<Supplier_fx> implements CallbackView {
    private SellerHttpHelper sellerHttpHelper;

    public DistributionMarketAdapter(List<Supplier_fx> listModel, Activity activity) {
        super(listModel, activity);
        sellerHttpHelper = new SellerHttpHelper(activity, this);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent, final Supplier_fx model) {
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_distribution_market, null);
        }

        final ImageView siv_image = ViewHolder.get(convertView, R.id.siv_image);
        TextView tv_commission = ViewHolder.get(convertView, R.id.tv_commission);
        Button add_distribution = ViewHolder.get(convertView, R.id.bt_add_distribution);
        Button bt_goStore = ViewHolder.get(R.id.bt_go_store, convertView);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);

        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = (int) (width * 0.242 - 5);
        SDViewUtil.setViewHeight(siv_image, height);
        if (model != null) {
            SDViewBinder.setImageView(siv_image, model.getPreview());

            SDViewBinder.setTextView(tv_name, model.getName());
            SDViewBinder.setTextView(tv_commission, String.valueOf(model.getBuy_count()));

            if (model.getIs_delete() == 1) {
                add_distribution.setText("已代言");
                add_distribution.setBackgroundResource(R.drawable.bg_daiyan);
            } else {
                add_distribution.setText("我要代言");
                add_distribution.setBackgroundResource(R.drawable.layer_main_color_corner_normal);
            }
            add_distribution.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (model.getIs_delete() == 1) {
                        MGToast.showToast("已经代言过了!");
                        return;
                    }
                    currPosition = position;
                    currModel = model;
                    representMerchant();
                }
            });
            bt_goStore.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreInActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("img", model.getPreview());
                    bundle.putString("name", model.getName());
                    bundle.putInt("count", model.getBuy_count());
                    bundle.putString("id", model.getId());
                    bundle.putString("isMyShop", model.getIs_delete() + "");
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
                }
            });

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DistributionStoreInActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("img", model.getPreview());
                    bundle.putString("name", model.getName());
                    bundle.putInt("count", model.getBuy_count());
                    bundle.putString("id", model.getId());
                    bundle.putString("isMyShop", model.getIs_delete() + "");
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private int currPosition;
    private Supplier_fx currModel;

    private void representMerchant() {
        sellerHttpHelper.getRepresentMerchant(currModel.getId());
    }

    private void showDialog(final String location) {
        new SDDialogConfirm()
                .setTextContent(
                        location)
                .setmListener(new SDDialogCustomListener() {
                    @Override
                    public void onDismiss(SDDialogCustom dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog) {
                        Intent intent = new Intent(mActivity, DistributionStoreInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("img", currModel.getPreview());
                        bundle.putString("name", currModel.getName());
                        bundle.putInt("count", currModel.getBuy_count());
                        bundle.putString("id", currModel.getId());
                        bundle.putString("isMyShop", currModel.getIs_delete() + "");
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }

                    @Override
                    public void onClickCancel(View v, SDDialogCustom dialog) {
                    }
                }).show();
    }


    @Override
    public void updateData(List<Supplier_fx> listModel) {
        super.updateData(listModel);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<RootRepresentMerchant> roots;

    @Override
    public void onSuccess(String method, List datas) {
        Message message = new Message();
        if (SellerConstants.REPRESENT_MERCHANT.equals(method)) {
            roots = datas;
            message.what = 0;
        }
        mHandler.sendMessage(message);

    }

    @Override
    public void onFailue(String responseBody) {
    }

    @Override
    public void onFinish(String method) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!SDCollectionUtil.isEmpty(roots)) {
                        //代言失败
                        MGToast.showToast(roots.get(0).getMessage());
                    } else {
                        mListModel.get(currPosition).setIs_delete(1);
                        notifyDataSetChanged();
                        showDialog("代言成功，请进入店铺查看");
                    }
                    break;
            }
        }
    };
}