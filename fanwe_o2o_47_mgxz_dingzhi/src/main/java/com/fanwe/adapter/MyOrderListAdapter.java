package com.fanwe.adapter;


import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView2;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getOrderInfo.ModelOrderItemIn;
import com.fanwe.user.model.getOrderInfo.ModelOrderItemOut;
import com.fanwe.user.presents.OrderHttpHelper;
import com.fanwe.utils.MGStringFormatter;
import com.miguo.live.views.customviews.MGToast;

import java.math.BigDecimal;
import java.util.List;

public class MyOrderListAdapter extends SDBaseAdapter<ModelOrderItemOut>{

    private List<ModelOrderItemIn> listGoods;

    private boolean isDelect = false;
    private TextView tv_delect;
    private int mOrderMode;//0,1,2,3,

    public MyOrderListAdapter(List<ModelOrderItemOut> listModel, Activity activity, boolean
            isDelect, int orderMode) {
        super(listModel, activity);
        this.isDelect = isDelect;
        this.mOrderMode = orderMode;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_order_list, null);
        }
        LinearLayout ll_goods = ViewHolder.get(convertView, R.id.ll_goods);
        LinearLayout ll_order_list = ViewHolder.get(convertView, R.id.ll_order_list);
        TextView tv_pay_again = ViewHolder.get(R.id.tv_pay_again, convertView);
        TextView tv_money = ViewHolder.get(R.id.tv_money, convertView);
        tv_delect = ViewHolder.get(R.id.tv_delect, convertView);
        TextView tv_pay = ViewHolder.get(R.id.tv_pay, convertView);
//		TextView tv_refund = ViewHolder.get(R.id.tv_refund, convertView);
        TextView tv_cancel_order = ViewHolder.get(R.id.tv_cancel_order, convertView);

        final ModelOrderItemOut model = getItem(position);
        if (model != null) {

            listGoods = model.getDeal_order_item();
            MyOrderListGoodsAdapter adapter = new MyOrderListGoodsAdapter(listGoods, model
                    .getStatus_name(), mActivity, MGStringFormatter.getInt(model.getOrder_status()),
                    mOrderMode);
            if (!SDCollectionUtil.isEmpty(listGoods)) {
                ll_goods.removeAllViews();
                final int size = listGoods.size();
                for (int i = 0; i < size; i++) {
                    ll_goods.addView(adapter.getView(i, null, null));
                    ModelOrderItemIn inItem = listGoods.get(i);
                    if (isDelect) {
                        SDViewUtil.show(tv_delect);
                        SDViewUtil.show(ll_order_list);
                        tv_delect.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //TODO delete
								deleteOrder(model, position);
                            }
                        });
                    } else {
                        SDViewUtil.hide(tv_delect);
                        SDViewUtil.hide(ll_order_list);
                    }
                }
            }
            int inStatus = MGStringFormatter.getInt(model.getOrder_status());
            if (!isDelect) {
                if (inStatus == 0) {
                    SDViewUtil.show(ll_order_list);
                    SDViewUtil.show(tv_pay);
                    tv_pay.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(mActivity, OrderDetailActivity.class);
//                            intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, Integer.valueOf
//									(model.getId()).intValue());
//                            mActivity.startActivity(intent);
                            MGToast.showToast("test:跳转到订单详情");
                        }
                    });
                    SDViewUtil.show(tv_cancel_order);
                    tv_cancel_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO 取消
                            cancelOrder(model, position);
                        }
                    });
                } else {
                    SDViewUtil.hide(tv_pay);
                    SDViewUtil.hide(tv_cancel_order);
                    SDViewUtil.hide(ll_order_list);
                }
                if (inStatus == 1) {
                    BigDecimal bd1 = new BigDecimal(Float.parseFloat(model.getTotal_price())
                            - Float.parseFloat(model.getPay_amount()));
                    bd1 = bd1.setScale(1, BigDecimal.ROUND_HALF_UP);
                    SDViewBinder.setTextView(tv_money, "还需支付" + bd1 + "元");
                    SDViewUtil.show(tv_pay_again);
                    tv_pay_again.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(mActivity, OrderDetailActivity.class);
//                            intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, Integer.valueOf
//									(model.getId()).intValue());
//                            mActivity.startActivity(intent);
                            MGToast.showToast("test:跳转到订单详情");
                        }
                    });
                    SDViewUtil.show(tv_cancel_order);
                    tv_cancel_order.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //取消
                            cancelOrder(model, position);
                        }
                    });
                } else {
                    SDViewBinder.setTextView(tv_money, "");
                    SDViewUtil.hide(tv_pay_again);
                    //SDViewUtil.hide(tv_cancel_order);
                    SDViewUtil.hide(tv_delect);
                }

                if (inStatus == 3 || inStatus == 4
                        || inStatus == 5 || inStatus == 6) {
                    SDViewUtil.hide(ll_order_list);
                } else {
                    SDViewUtil.show(ll_order_list);
                }

            } else {
                if (inStatus == 0 || inStatus == 4 || inStatus == 6 || inStatus == 5) {
                    SDViewUtil.show(ll_order_list);
                    SDViewUtil.show(tv_delect);
                    tv_delect.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOrder(model, position);
                        }
                    });
                } else {
                    if (inStatus == 1) {
                        SDViewUtil.show(ll_order_list);
                        SDViewUtil.show(tv_cancel_order);
                        tv_cancel_order.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                cancelOrder(model, position);
                            }
                        });
                    } else {
                        SDViewUtil.hide(ll_order_list);
                        SDViewUtil.hide(tv_delect);
                        SDViewUtil.hide(tv_cancel_order);
                    }
                }
            }
        }

        /**
         * 状态汇总
         */
        if (tv_cancel_order.getVisibility() == View.GONE && tv_pay.getVisibility() == View.GONE
                && tv_pay_again.getVisibility() == View.GONE && tv_delect.getVisibility() == View
                .GONE) {
            SDViewUtil.hide(ll_order_list);
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
