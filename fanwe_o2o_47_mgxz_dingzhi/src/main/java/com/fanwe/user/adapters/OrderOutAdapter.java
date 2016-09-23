package com.fanwe.user.adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didikee.uilibs.views.MaxHeightListView;
import com.fanwe.ConfirmOrderActivity;
import com.fanwe.base.CallbackView2;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getOrderInfo.ModelOrderItemIn;
import com.fanwe.user.model.getOrderInfo.ModelOrderItemOut;
import com.fanwe.user.presents.OrderHttpHelper;
import com.fanwe.utils.MGStringFormatter;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

/**
 * Created by didik on 2016/8/26.
 *  按照最新整理的思路
 */
public class OrderOutAdapter extends SDBaseAdapter<ModelOrderItemOut> {

    private List<ModelOrderItemIn> listGoods;

    private boolean isDelect = false;
    private TextView tv_delect;
    private int mOrderMode;//0,1,2,3,

    public OrderOutAdapter(List<ModelOrderItemOut> listModel, Activity activity, boolean
            isDelect, int orderMode) {
        super(listModel, activity);
        this.isDelect = isDelect;
        this.mOrderMode = orderMode;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_order_list_out, null);
        }
        //底部汇总
        LinearLayout ll_order_list = ViewHolder.get(convertView, R.id.ll_order_list);
        //继续支付
        TextView tv_pay_again = ViewHolder.get(R.id.tv_pay_again, convertView);
//        TextView tv_money = ViewHolder.get(R.id.tv_money, convertView);
        //是否为删除模式
        tv_delect = ViewHolder.get(R.id.tv_delect, convertView);
        //付款
        TextView tv_pay = ViewHolder.get(R.id.tv_pay, convertView);
        //取消订单
        TextView tv_cancel_order = ViewHolder.get(R.id.tv_cancel_order, convertView);
        //第二listview
        MaxHeightListView mInnerListView = ViewHolder.get(R.id.max_list, convertView);



        final ModelOrderItemOut model = getItem(position);
        if (model != null) {
        //init inner listview
            int order_status = MGStringFormatter.getInt(model.getOrder_status());
            listGoods = model.getDeal_order_item();
            String order_id = model.getOrder_id();
            OrderInAdapter inAdapter=new OrderInAdapter(listGoods, model
                    .getStatus_name(), mActivity, order_status,mOrderMode,order_id);
            mInnerListView.setAdapter(inAdapter);

//            order_status   0  付款按钮
//            order_status 3   继续支付按钮
//            order_status 0||3 取消订单 按钮
//            order_status 5||6 删除按钮
//            refund_status    2   已退款 状态
//            refund_status    3    退款失败状态
            if (order_status==0){
                tv_pay.setVisibility(View.VISIBLE);
                tv_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(model.getOrder_id())) {
                            Intent intent = new Intent(mActivity, ConfirmOrderActivity.class);
                            intent.putExtra("orderId", model.getOrder_id());
                            mActivity.startActivity(intent);
                        }

                    }
                });
            }
            if (order_status==1){
                tv_pay_again.setVisibility(View.VISIBLE);
                tv_pay_again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(model.getOrder_id())) {
                            Intent intent = new Intent(mActivity, ConfirmOrderActivity.class);
                            intent.putExtra("orderId", model.getOrder_id());
                            mActivity.startActivity(intent);
                        }
                    }
                });
            }
            if (order_status==0 || order_status==1){
                tv_cancel_order.setVisibility(View.VISIBLE);
                tv_cancel_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(model, position);
                    }
                });
            }
            if (order_status==5 || order_status==6){
                tv_delect.setVisibility(View.VISIBLE);
                tv_delect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(model, position);
                    }
                });
            }
        }

        /**
         * 状态汇总
         */
        if (tv_cancel_order.getVisibility() == View.GONE && tv_pay.getVisibility() == View.GONE
                && tv_pay_again.getVisibility() == View.GONE && tv_delect.getVisibility() == View
                .GONE) {
            ll_order_list.setVisibility(View.GONE);
        }else {
            ll_order_list.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    protected void cancelOrder(final ModelOrderItemOut model, final int position) {
        if (model == null) {
            return;
        }

        SDDialogConfirm dialog = new SDDialogConfirm();
        dialog.setTextContent("确定取消订单？");
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {

            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                new OrderHttpHelper(new CallbackView2() {
                    @Override
                    public void onSuccess(String responseBody) {

                    }

                    @Override
                    public void onSuccess(String method, List datas) {
                        if (UserConstants.ORDER_INFO_CANCEL_ORDER.endsWith(method)){
                            //删除订单成功
                            MGToast.showToast("订单取消成功!");
                            mListModel.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailue(String responseBody) {
                        if (UserConstants.ORDER_INFO_CANCEL_ORDER.endsWith(responseBody)){
                            //删除订单失败
                            MGToast.showToast("订单取消失败!");
                        }
                    }

                    @Override
                    public void onFinish(String method) {
                    }

                }).postCancelOrderOperator(model.getOrder_id());
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {

            }
        });
        dialog.show();

    }

    private void deleteOrder(final ModelOrderItemOut model, final int position) {
        if (model == null) {
            return;
        }

        SDDialogConfirm dialog = new SDDialogConfirm();
        dialog.setTextContent("确定删除订单？");
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {

            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                new OrderHttpHelper(new CallbackView2() {
                    @Override
                    public void onSuccess(String responseBody) {

                    }

                    @Override
                    public void onSuccess(String method, List datas) {
                        if (UserConstants.ORDER_INFO.endsWith(method)){
                            //删除订单成功
                            mListModel.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailue(String responseBody) {
                        if (UserConstants.ORDER_INFO.endsWith(responseBody)){
                            //删除订单失败
                            MGToast.showToast("订单删除失败!");
                        }
                    }

                    @Override
                    public void onFinish(String method) {

                    }

                }).postDeleteOrderOperator(model.getOrder_id());
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {

            }
        });
        dialog.show();
    }
}

