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
import android.widget.Toast;

import com.fanwe.DaiYanStoreWapActivity;
import com.fanwe.DistributionStoreInActivity;
import com.fanwe.LoginActivity;
import com.fanwe.base.CallbackView;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.AddStoreModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Supplier_fx;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

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
//						openTheStore(model.getUrl());
                        SDToast.showToast("已经代言过了!");
                        return;
                    }
//                    if (AppHelper.isLogin(mActivity)) {
//                        clickAddDistribution(model, position);
//                    } else {
//                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
//                    }
                    currPosition = position;
                    currModel = model;
                    representMerchant();
                }
            });

            bt_goStore.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (model.getIs_delete() == 0) {
                        openTheStore(model.getUrl());
                    } else {
                        Intent intent = new Intent(mActivity, DistributionStoreInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("img", model.getPreview());
                        bundle.putString("name", model.getName());
                        bundle.putInt("count", model.getBuy_count());
                        bundle.putString("id", model.getId());
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }

                }
            });

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getIs_delete() == 0) {
                        openTheStore(model.getUrl());
                    } else {
                        Intent intent = new Intent(mActivity, DistributionStoreInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("img", model.getPreview());
                        bundle.putString("name", model.getName());
                        bundle.putInt("count", model.getBuy_count());
                        bundle.putString("id", model.getId());
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }
                }
            });
        }
        return convertView;
    }

    private int currPosition;
    private Supplier_fx currModel;

    private void representMerchant() {
        sellerHttpHelper.getRepresentMerchant(currModel.getId(), "");
    }

    /**
     * 打开对应的小店
     **/
    protected void openTheStore(String url) {
        Intent intent = new Intent(mActivity, DaiYanStoreWapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putInt("user_id", 0);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    /**
     * 添加分销商品
     */
    protected void clickAddDistribution(Supplier_fx actModel, final int position) {
        RequestModel model = new RequestModel();
        model.putCtl("uc_fxshop");
        model.putAct("add_fx");
        model.put("id", actModel.getId());
        SDRequestCallBack<AddStoreModel> handler = new SDRequestCallBack<AddStoreModel>(false) {

            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SDDialogManager.dismissProgressDialog();
                Intent intent = null;
                switch (actModel.getStatus()) {
                    case -1:
                        intent = new Intent(mActivity, LoginActivity.class);
                        mActivity.startActivity(intent);
                        break;
                    case 0://代言失败
                        SDToast.showToast(this.actModel.getInfo(), Toast.LENGTH_LONG);
                        break;
                    case 1:
                        mListModel.get(position).setIs_delete(0);
                        mListModel.get(position).setUrl(this.actModel.getUrl());
                        notifyDataSetChanged();
                        showDialog("代言成功，请进入我的小店查看", mListModel.get(position).getUrl());
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                SDDialogManager.dismissProgressDialog();
            }

            @Override
            public void onFinish() {

            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);

    }

    private void showDialog(final String location, final String url) {
        new SDDialogConfirm()
                .setTextContent(
                        location)
                .setmListener(new SDDialogCustomListener() {
                    @Override
                    public void onDismiss(SDDialogCustom dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog) {
//						Intent intent = new Intent(mActivity, DistributionStoreWapActivity.class);
//						mActivity.startActivity(intent);
                        openTheStore(url);
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

    @Override
    public void onSuccess(String method, List datas) {
        Message message = new Message();
        if (SellerConstants.REPRESENT_MERCHANT.equals(method)) {
            message.what = 0;
        }
        mHandler.sendMessage(message);

    }

    @Override
    public void onFailue(String responseBody) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mListModel.get(currPosition).setIs_delete(1);
                    notifyDataSetChanged();
                    showDialog("代言成功，请进入我的小店查看", mListModel.get(currPosition).getUrl());
                    break;
            }
        }
    };
}