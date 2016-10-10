package com.fanwe.user.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.AddCommentActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.constant.Constant;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getOrderInfo.ModelOrderItemIn;
import com.fanwe.user.view.RefundApplicationActivity;
import com.fanwe.utils.MGStringFormatter;

import java.util.List;

/**
 * Created by didik on 2016/8/26.
 * 8.30 虽然添加了3个字段,但是没有什么用....
 */
public class OrderInAdapter extends SDBaseAdapter<ModelOrderItemIn> {


    private String mOrder_id;
    private boolean isPayed = false;
    private int mStatus_value;//订单的状态
    private int mOrderMode;//0,1,2,3,

    public OrderInAdapter(List<ModelOrderItemIn> listModel, String isPayed, Activity
            activity, int status_value, int orderMode, String order_id) {
        super(listModel, activity);
        if ("已支付".equalsIgnoreCase(isPayed)) {
            this.isPayed = true;
        }
        this.mOrder_id = order_id;
        this.mOrderMode = orderMode;
        this.mStatus_value = status_value;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_order_list_in, null);
        }

        ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
        LinearLayout mLl_button = ViewHolder.get(convertView, R.id.ll_button);
        ImageView iv_img = ViewHolder.get(convertView, R.id.iv_img);
        TextView tv_order_title = ViewHolder.get(convertView, R.id.tv_order_title);
        TextView tv_order = ViewHolder.get(convertView, R.id.tv_order);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_title);
//        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        //底部显示的按钮
        TextView tv_evaluate = ViewHolder.get(convertView, R.id.tv_evaluate);
        TextView tv_tuikuan = ViewHolder.get(convertView, R.id.tv_tuikuan);

        TextView tv_number = ViewHolder.get(convertView, R.id.tv_number);
        TextView tv_sno = ViewHolder.get(convertView, R.id.tv_sno);
//		View v_line = ViewHolder.get(R.id.v_line, convertView);
        TextView tv_total_price = ViewHolder.get(convertView, R.id.tv_total_price);

        final ModelOrderItemIn model = getItem(position);
        if (model != null) {
            SDViewBinder.setImageView(model.getIcon(), iv_image);
            tv_name.setText(model.getName());//单个商品的名称
            tv_order_title.setText(model.getBuss_name());//商家名称
            tv_sno.setText(model.getOrder_sn());
            tv_total_price.setText(model.getTotal_price());
            tv_number.setText(model.getNumber());
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
//            order_status (3||4) && (dp_id=0 ||dp_id = null)&&( consume_count>0)
//            评价按钮  已消费状态
//            order_status (3||4) && (consume_count<(number- refunded- refunding))
//            退款按钮    待消费状态
//            dp_id>0  已评价状态
//            refunding >0退款中状态
//            refund_status    2   已退款 状态
//            refund_status    3    退款失败状态
            int consume_count = MGStringFormatter.getInt(model.getConsume_count());
            Log.e("test", "count:" + consume_count);
            int number = MGStringFormatter.getInt(model.getNumber());
            int refunded = MGStringFormatter.getInt(model.getRefunded());
            int refunding = MGStringFormatter.getInt(model.getRefunding());

            /*是否可评价：0：不可评价，1：可评价*/
            String can_comment = model.getCan_comment();
            /*是否可消费：0，不可消费，1，可消费*/
            String can_consume = model.getCan_consume();
            /*是否可退款：0：不可退款，1：可退款*/
            String can_refund = model.getCan_refund();

            int dp_id = -1;
            String str_dp_id = model.getDp_id();
            if (TextUtils.isEmpty(str_dp_id)) {
                dp_id = 0;
            } else {
                //表示已经点评
                dp_id = 1;
            }

            if ((mStatus_value == 3 || mStatus_value == 4) && dp_id == 0 && consume_count > 0) {
                tv_evaluate.setVisibility(View.VISIBLE);
                tv_order.setText("已消费");
                tv_evaluate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, AddCommentActivity.class);
                        intent.putExtra(AddCommentActivity.EXTRA_ID, model.getDetail_id());
                        intent.putExtra(AddCommentActivity.TUAN_ID, model.getTuan_id());
                        intent.putExtra(AddCommentActivity.EXTRA_NAME, model.getName());
                        intent.putExtra(AddCommentActivity.EXTRA_TYPE, Constant.CommentType.DEAL);
                        mActivity.startActivity(intent);
                    }
                });
            } else {
                tv_evaluate.setVisibility(View.GONE);
            }
//            int cal_temp = number - refunded - refunding;
//            if ((mStatus_value == 3 || mStatus_value == 4) && cal_temp - consume_count > 0) {
            if ("1".equals(can_consume)) {
                tv_tuikuan.setVisibility(View.VISIBLE);
                tv_order.setText("待消费");
                tv_tuikuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tuan_id = model.getTuan_id();
                        gotoRefundApplication(tuan_id);
                    }
                });
            } else {
                tv_tuikuan.setVisibility(View.GONE);
            }
            if (dp_id > 0) {
                tv_order.setText("已评价");
            }
            if (refunding > 0) {
                tv_order.setText("退款中");
            }
            int refund_status = MGStringFormatter.getInt(model.getRefund_status());
            if (refund_status == 2) {
                tv_order.setText("已退款");
            } else if (refund_status == 3) {
                tv_order.setText("退款失败");
            }
        }
        if (tv_evaluate.getVisibility() == View.GONE && tv_tuikuan.getVisibility() == View.GONE) {
            mLl_button.setVisibility(View.GONE);
        } else {
            mLl_button.setVisibility(View.VISIBLE);
        }

        // 0 没有申请退款 1:退款中,2:已退款
        convertView.setOnClickListener(new View.OnClickListener() {
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
        return convertView;
    }

    /**
     * 去退款页面
     *
     * @param tuan_id
     */
    private void gotoRefundApplication(String tuan_id) {
        Intent intent = new Intent(mActivity, RefundApplicationActivity.class);
        Bundle data = new Bundle();
        data.putString("extra_order_id", mOrder_id);
        data.putString("extra_tuan_id", tuan_id);
        intent.putExtras(data);
        mActivity.startActivity(intent);
    }
}
