package com.fanwe.adapter;

import java.util.List;

import com.fanwe.AddCommentActivity;
import com.fanwe.AppWebViewActivity;
import com.fanwe.RefundApplicationActivity;
import com.fanwe.StoreDetailActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.constant.Constant.CommentType;
import com.fanwe.event.EnumEventTag;
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
import com.fanwe.model.BaseActModel;
import com.fanwe.model.OrderInItem;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_orderGoodsModel;
import com.fanwe.model.Uc_order_check_deliveryActModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.http.ResponseInfo;
import com.sunday.eventbus.SDEventManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyOrderListGoodsAdapter extends SDBaseAdapter<OrderInItem>
{

	private boolean mShowActions = true;
	
	private boolean isPayed=false;
	
	private int mStatus_value;//订单的状态
	private int mOrderMode;//0,1,2,3,

	public MyOrderListGoodsAdapter(List<OrderInItem> listModel, String isPayed, Activity activity,int status_value,int orderMode)
	{
		super(listModel, activity);
		if ("已支付".equalsIgnoreCase(isPayed)) {
			this.isPayed = true;
		}
		this.mOrderMode=orderMode;
//		this.mShowActions = showActions;
		this.mStatus_value = status_value;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
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
	
		final OrderInItem model = getItem(position);
		int refund_status = model.getRefund_status();
		if (model != null)
		{
			SDViewBinder.setImageView(iv_image, model.getDeal_icon());
			SDViewBinder.setTextView(tv_name, model.getSub_name());
			SDViewBinder.setTextView(tv_number, String.valueOf(model.getNumber()));
			SDViewBinder.setTextView(tv_total_price, ""+model.getTotal_price());
			SDViewBinder.setTextView(tv_sno, model.getOrder_sn());
			SDViewBinder.setTextView(tv_order_title, model.getSlname());
			switch (model.getCate_id())
			{
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
			
			if(model.getDeal_id() == 0)
			{
				iv_img.setImageResource(R.drawable.bg_order_hui);
			}
			/**
			 * 状态说明 0 没有申请退款  1:退款中,2:已退款,3,退款失败
			 */
			String goodsStatus="";
			int refundStatus = model.getRefund_status();
			switch (mOrderMode) {
				case 1:
					goodsStatus="";
					SDViewBinder.setTextView(tv_order, goodsStatus);
					break;
				
		
			case 3:
				SDViewUtil.show(mLl_button);
				if (model.getDp_id()==1) {
					SDViewUtil.show(tv_evaluate);
					tv_evaluate.setText("追评");
				}
				SDViewUtil.show(tv_evaluate);
				tv_evaluate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//TODO 评价
						Intent intent = new Intent(mActivity, AddCommentActivity.class);
						intent.putExtra(AddCommentActivity.EXTRA_ID, model.getDeal_id());
						intent.putExtra(AddCommentActivity.EXTRA_NAME, model.getName());
						intent.putExtra(AddCommentActivity.EXTRA_TYPE, CommentType.DEAL);
						mActivity.startActivity(intent);
					}
				});
				break;
				
				case 4:
					if (refundStatus==1) {
						goodsStatus="退款中";
					}else if (refundStatus==2) {
						goodsStatus="已退款";
					}else if (refundStatus==3) {
						goodsStatus="退款失败";
					}else {
						goodsStatus="";
//						goodsStatus = getGoodsStatus(Integer.valueOf(model.getNumber()), Integer.valueOf(model.getConsume_count()), model.getDp_id(),model.getRefund_status());
					}
					SDViewBinder.setTextView(tv_order, goodsStatus);
					defaultShow(model, mLl_button, tv_tuikuan);
					break;
			default:
				if (mStatus_value!=0) {//已支付
					goodsStatus = getGoodsStatus(Integer.valueOf(model.getNumber()), Integer.valueOf(model.getConsume_count()), model.getDp_id(),model.getRefund_status());
				}else {
					goodsStatus="";
				}
				SDViewBinder.setTextView(tv_order, goodsStatus);
				defaultShow(model,mLl_button,tv_tuikuan);
				break;
			}
			
			// 0 没有申请退款 1:退款中,2:已退款
						
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (model.getDeal_id() > 0)
					{
						Intent intent = new Intent(mActivity, TuanDetailActivity.class);
						intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getDeal_id());
						mActivity.startActivity(intent);
					} else if(model.getDeal_id() == 0)
					{
						Intent intent = new Intent(mActivity, StoreDetailActivity.class);
						Bundle bundle=new Bundle();
						bundle.putInt(StoreDetailActivity.EXTRA_SHOP_ID, Integer.valueOf(model.getLocation_id()).intValue());
						bundle.putInt("type",0);
						intent.putExtras(bundle);
						mActivity.startActivity(intent);
					}else
					{
						SDToast.showToast("未找到商品ID");
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
	private void defaultShow(final OrderInItem model, LinearLayout mLl_button, TextView tv_tuikuan){
		if (model.getDeal_id() != 0 && mStatus_value!=0) {
			int judgement=Integer.valueOf(model.getNumber()).intValue() - Integer.valueOf(model.getConsume_count()).intValue()- model.getRefunded() -model.getRefunding();
			if (judgement > 0 && model.getIs_refund() == 1) {
				SDViewUtil.show(mLl_button);
				SDViewUtil.show(tv_tuikuan);
//				SDViewUtil.show(v_line);

				// TODO 退款
				tv_tuikuan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int id=0;
						try {
							id = Integer.valueOf(model.getId()).intValue();
						} catch (NumberFormatException e) {
							SDToast.showToast("id错误!");
							return;
						}
						gotoRefundApplication(id);
					}
				});
			}

		}
	}
	/**
	 * 获取订单的状态
	 * @return
	 */
	private String getGoodsStatus(int totalNum,int usedNum,int dianPing,int status){
//		1. 待消费
//		2. 已消费
//		3.已评价
//		4. 退款中
//		5. 已退款
//		6. 退款失败
		//状态说明 0 没有申请退款  1:退款中,2:已退款, 
		if (status==1) {
			return "退款中";
		}
		if (status==2) {
			return "已退款";
		}
		if (status==3) {
			return "退款失败";
		}
		if (status==0) {
			if (usedNum < totalNum) {
				return "待消费";
			}
			if (usedNum==totalNum) {
				if (dianPing==1) {
					return "已消费";
				}else {
					return "已评价";
				}
			}
		}
//		switch (mOrderMode) {
//		case 0:
//			
//			break;
//		case 1:
//			
//			break;
//		case 2:
//	
//			break;
//		case 3:
//			return "";
//		case 4:
//	
//			break;
//
//		default:
//			break;
//		}
		
		return "";
	}

	/**
	 * 去退款页面
	 * @param goodsID
	 */
	private void gotoRefundApplication(int goodsID){
		Intent intent=new Intent(mActivity,RefundApplicationActivity.class);
		Bundle data=new Bundle();
		data.putInt("extra_id", goodsID);
		intent.putExtras(data);
		mActivity.startActivity(intent);
	}
	
	/**
	 * 点击确认收货
	 * 
	 * @param model
	 */
	protected void clickConfirmationReceipt(final Uc_orderGoodsModel model)
	{
		if (model == null)
		{
			return;
		}

		SDDialogConfirm dialog = new SDDialogConfirm();
		dialog.setTextContent("确认收货?");
		dialog.setmListener(new SDDialogCustomListener()
		{
			@Override
			public void onDismiss(SDDialogCustom dialog)
			{

			}

			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog)
			{
				requestConfirmationReceipt(model);
			}

			@Override
			public void onClickCancel(View v, SDDialogCustom dialog)
			{

			}
		});
		dialog.show();
	}

	/**
	 * 确认收货
	 * 
	 * @param model
	 */
	protected void requestConfirmationReceipt(final Uc_orderGoodsModel model)
	{
		if (model == null)
		{
			return;
		}

		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_order");
		requestModel.putAct("verify_delivery");
		requestModel.put("item_id", model.getId());
		InterfaceServer.getInstance().requestInterface(requestModel, new SDRequestCallBack<BaseActModel>()
		{
			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("");
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					// TODO 刷新列表
					SDEventManager.post(EnumEventTag.REFRESH_ORDER_LIST.ordinal());
				}
			}
		});
	}

	/**
	 * 查看物流
	 * 
	 * @param model
	 */
	protected void requestQueryLogistics(Uc_orderGoodsModel model)
	{
		if (model == null)
		{
			return;
		}
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_order");
		requestModel.putAct("check_delivery");
		requestModel.put("item_id", model.getId());
		InterfaceServer.getInstance().requestInterface(requestModel, new SDRequestCallBack<Uc_order_check_deliveryActModel>()
		{
			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("");
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					String url = actModel.getUrl();
					if (!TextUtils.isEmpty(url))
					{
						Intent intent = new Intent(mActivity, AppWebViewActivity.class);
						intent.putExtra(AppWebViewActivity.EXTRA_URL, url);
						intent.putExtra(AppWebViewActivity.EXTRA_TITLE, "查看物流");
						mActivity.startActivity(intent);
					}
				}
			}
		});
	}
}