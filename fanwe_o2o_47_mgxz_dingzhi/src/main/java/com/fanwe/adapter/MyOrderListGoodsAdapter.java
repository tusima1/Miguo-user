package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.AddCommentActivity;
import com.fanwe.user.view.RefundApplicationActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.constant.Constant.CommentType;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getOrderInfo.ModelOrderItemIn;
import com.fanwe.utils.MGStringFormatter;

import java.util.List;

public class MyOrderListGoodsAdapter extends SDBaseAdapter<ModelOrderItemIn> {

    private boolean mShowActions = true;

    private boolean isPayed = false;

    private int mStatus_value;//订单的状态
    private int mOrderMode;//0,1,2,3,

    public MyOrderListGoodsAdapter(List<ModelOrderItemIn> listModel, String isPayed, Activity
            activity, int status_value, int orderMode) {
        super(listModel, activity);
        if ("已支付".equalsIgnoreCase(isPayed)) {
            this.isPayed = true;
        }
        this.mOrderMode = orderMode;
//		this.mShowActions = showActions;
        this.mStatus_value = status_value;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_order_detail_goods, null);
        }

        ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
        LinearLayout mLl_button = ViewHolder.get(convertView, R.id.ll_button);
        ImageView iv_img = ViewHolder.get(convertView, R.id.iv_img);
        TextView tv_order_title = ViewHolder.get(convertView, R.id.tv_order_title);
        TextView tv_order = ViewHolder.get(convertView, R.id.tv_order);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_title);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        //底部显示的按钮
        TextView tv_evaluate = ViewHolder.get(convertView, R.id.tv_evaluate);
        TextView tv_tuikuan = ViewHolder.get(convertView, R.id.tv_tuikuan);
        TextView tv_number = ViewHolder.get(convertView, R.id.tv_number);
        TextView tv_sno = ViewHolder.get(convertView, R.id.tv_sno);
//		View v_line = ViewHolder.get(R.id.v_line, convertView);
        TextView tv_total_price = ViewHolder.get(convertView, R.id.tv_total_price);

        final ModelOrderItemIn model = getItem(position);
        if (model != null) {
            SDViewBinder.setImageView(iv_image, model.getIcon());
            SDViewBinder.setTextView(tv_name, model.getName());
            SDViewBinder.setTextView(tv_number, model.getNumber());
            SDViewBinder.setTextView(tv_total_price, model.getTotal_price());
            SDViewBinder.setTextView(tv_sno, model.getOrder_sn());
            SDViewBinder.setTextView(tv_order_title, model.getBuss_name());//商家名称
            int cate_id = MGStringFormatter.getInt(model.getCate_id());
            switch (cate_id) {
                case 8:
                    iv_img.setImageResource(R.drawable.bg_order_food);
                    break;
                case 9:
                    iv_img.setImageResource(R.drawable.bg_order_recreation);
                    break;
                case 10:
                    iv_img.setImageResource(R.drawable.bg_order_lift);
                    break;
                case 15:
                    iv_img.setImageResource(R.drawable.bg_order_hotel);
                    break;
                case 18:
                    iv_img.setImageResource(R.drawable.bg_order_li);
                    break;
                case 20:
                    iv_img.setImageResource(R.drawable.bg_order_near);
                    break;
                case 21:
                    iv_img.setImageResource(R.drawable.bg_order_massage);
                    break;
                case 22:
                    iv_img.setImageResource(R.drawable.bg_order_tip);
                    break;
                case 23:
                    iv_img.setImageResource(R.drawable.bg_order_ktv);
                    break;
                case 24:
                    iv_img.setImageResource(R.drawable.bg_order_movie);
                    break;
                case 25:
                    iv_img.setImageResource(R.drawable.bg_order_noce);
                    break;
                default:
                    break;
            }

//			if(model.getDeal_id() == 0)
//			{
//				iv_img.setImageResource(R.drawable.bg_order_hui);
//			}
            /**
             * 状态说明 0 没有申请退款  1:退款中,2:已退款,3,退款失败
             */
            String goodsStatus;
            int refundStatus = MGStringFormatter.getInt(model.getRefund_status());
            switch (mOrderMode) {
                case 1:
                    goodsStatus = "";
                    SDViewBinder.setTextView(tv_order, goodsStatus);
                    break;
                case 3:
                    SDViewUtil.show(mLl_button);
                    if (MGStringFormatter.getInt(model.getDp_id()) == 1) {
                        SDViewUtil.show(tv_evaluate);
                        tv_evaluate.setText("追评");
                    }
                    SDViewUtil.show(tv_evaluate);
                    tv_evaluate.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //TODO 评价
                            Intent intent = new Intent(mActivity, AddCommentActivity.class);
                            intent.putExtra(AddCommentActivity.EXTRA_ID, model.getTuan_id());
                            intent.putExtra(AddCommentActivity.EXTRA_NAME, model.getName());
                            intent.putExtra(AddCommentActivity.EXTRA_TYPE, CommentType.DEAL);
                            mActivity.startActivity(intent);
                        }
                    });
                    break;

                case 4:
                    if (refundStatus == 1) {
                        goodsStatus = "退款中";
                    } else if (refundStatus == 2) {
                        goodsStatus = "已退款";
                    } else if (refundStatus == 3) {
                        goodsStatus = "退款失败";
                    } else {
                        goodsStatus = "";
//						goodsStatus = getGoodsStatus(Integer.valueOf(model.getNumber()), Integer
// .valueOf(model.getConsume_count()), model.getDp_id(),model.getRefund_status());
                    }
                    SDViewBinder.setTextView(tv_order, goodsStatus);
                    defaultShow(model, mLl_button, tv_tuikuan);
                    break;
                default:
                    if (mStatus_value != 0) {//已支付
                        goodsStatus = getGoodsStatus(Integer.valueOf(model.getNumber()), Integer
								.valueOf(model.getConsume_count()), MGStringFormatter.getInt(model
								.getDp_id()), MGStringFormatter.getInt(model.getRefund_status()));
                    } else {
                        goodsStatus = "";
                    }
                    SDViewBinder.setTextView(tv_order, goodsStatus);
                    defaultShow(model, mLl_button, tv_tuikuan);
                    break;
            }

            // 0 没有申请退款 1:退款中,2:已退款
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(model.getTuan_id())) {
                        Intent intent = new Intent(mActivity, TuanDetailActivity.class);
                        intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getTuan_id());
                        intent.putExtra(TuanDetailActivity.EXTRA_DETAIL_ID, model.getDetail_id());
                        mActivity.startActivity(intent);
                    }
                }
            });
        }
        return convertView;
    }

    /**
     * 显示默认的
     * @param model
     * @param mLl_button
     * @param tv_tuikuan
     */
    private void defaultShow(final ModelOrderItemIn model, LinearLayout mLl_button, TextView tv_tuikuan) {
        if (mStatus_value != 0) {
            int judgement = Integer.valueOf(model.getNumber()).intValue() - Integer.valueOf(model.getConsume_count()).intValue() - MGStringFormatter.getInt(model.getRefunded()) - MGStringFormatter.getInt(model.getRefunding());
            if (judgement > 0 && MGStringFormatter.getInt(model.getIs_refund()) == 1) {
                SDViewUtil.show(mLl_button);
                SDViewUtil.show(tv_tuikuan);
//				SDViewUtil.show(v_line);

                // TODO 退款
                tv_tuikuan.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String tuan_id = model.getTuan_id();
                        String detail_id = model.getDetail_id();
//						gotoRefundApplication(id);
                    }
                });
            }

        }
    }

    /**
     * 获取订单的状态
     * @return
     */
    private String getGoodsStatus(int totalNum, int usedNum, int dianPing, int status) {
//		1. 待消费
//		2. 已消费
//		3.已评价
//		4. 退款中
//		5. 已退款
//		6. 退款失败
        //状态说明 0 没有申请退款  1:退款中,2:已退款,
        if (status == 1) {
            return "退款中";
        }
        if (status == 2) {
            return "已退款";
        }
        if (status == 3) {
            return "退款失败";
        }
        if (status == 0) {
            if (usedNum < totalNum) {
                return "待消费";
            }
            if (usedNum == totalNum) {
                if (dianPing == 1) {
                    return "已消费";
                } else {
                    return "已评价";
                }
            }
        }
        return "";
    }

    /**
     * 去退款页面
     * @param goodsID
     */
    private void gotoRefundApplication(int goodsID) {
        Intent intent = new Intent(mActivity, RefundApplicationActivity.class);
        Bundle data = new Bundle();
        data.putInt("extra_id", goodsID);
        intent.putExtras(data);
        mActivity.startActivity(intent);
    }
}