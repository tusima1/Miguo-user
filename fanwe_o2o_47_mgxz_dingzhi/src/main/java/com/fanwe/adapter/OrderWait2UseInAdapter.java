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
import com.fanwe.AppWebViewActivity;
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
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.OrderInItem;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_orderGoodsModel;
import com.fanwe.model.Uc_order_check_deliveryActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.view.RefundApplicationActivity;
import com.lidroid.xutils.http.ResponseInfo;
import com.miguo.live.views.customviews.MGToast;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

public class OrderWait2UseInAdapter extends SDBaseAdapter<OrderInItem> {

	private boolean isPayed = false;
	private int mOrderStatus = 2;

	public OrderWait2UseInAdapter(List<OrderInItem> listModel, Activity activity, String isPayed, int orderStatus) {
		super(listModel, activity);
		if ("已支付".equalsIgnoreCase(isPayed)) {
			this.isPayed = true;
		}
		this.mOrderStatus = orderStatus;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_order_list_in, null);
		}

		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		LinearLayout mLl_button = ViewHolder.get(convertView, R.id.ll_button);
		ImageView iv_img = ViewHolder.get(convertView, R.id.iv_img);
		TextView tv_order_title = ViewHolder.get(convertView, R.id.tv_order_title);
		TextView tv_order = ViewHolder.get(convertView, R.id.tv_order);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_title);
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		TextView tv_rate = ViewHolder.get(convertView, R.id.tv_rate);
		TextView tv_tuikuan = ViewHolder.get(convertView, R.id.tv_tuikuan);
		TextView tv_number = ViewHolder.get(convertView, R.id.tv_number);
		TextView tv_sno = ViewHolder.get(convertView, R.id.tv_sno);
		View v_line = ViewHolder.get(R.id.v_line, convertView);
		TextView tv_total_price = ViewHolder.get(convertView, R.id.tv_total_price);

		final OrderInItem model = getItem(position);
		int refund_status = model.getRefund_status();
		if (model != null) {
			SDViewBinder.setImageView(iv_image, model.getDeal_icon());
			SDViewBinder.setTextView(tv_name, model.getSub_name());
			SDViewBinder.setTextView(tv_number, String.valueOf(model.getNumber()));
			SDViewBinder.setTextView(tv_total_price, "" + model.getTotal_price());
			SDViewBinder.setTextView(tv_sno, model.getOrder_sn());
			SDViewBinder.setTextView(tv_order_title, model.getSlname());
			switch (model.getCate_id()) {
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

			if (model.getDeal_id() == 0) {
				iv_img.setImageResource(R.drawable.bg_order_hui);
			}

			/**
			 * 状态说明 0:未支付,1部分支付,2:已支付,3:退款中,4:已退款,5:已完结,6:已取消
			 */
			String status = "";
			switch (mOrderStatus) {
			case 0:
				status = "未支付";
				break;
			case 1:
				status = "部分支付";
				break;
			case 2:
				status = "已支付";
				break;
			case 3:
				status = "退款中";
				break;
			case 4:
				status = "已退款";
				break;
			case 5:
				status = "已完结";
				break;
			case 6:
				status = "已取消";
				break;

			default:
				status = "已支付";
				break;
			}
			SDViewBinder.setTextView(tv_order, status);
			// 0 没有申请退款 1:退款中,2:已退款
			if (model.getDeal_id() != 0 && isPayed) {
				//
				int judgement=Integer.valueOf(model.getNumber()).intValue() - Integer.valueOf(model.getConsume_count()).intValue()- model.getRefunded() -model.getRefunding();
				
				if (judgement > 0 && model.getIs_refund() == 1) {
					SDViewUtil.show(mLl_button);
					SDViewUtil.show(tv_tuikuan);
					SDViewUtil.show(v_line);

					// TODO 退款
					tv_tuikuan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int id=0;
							try {
								id = Integer.valueOf(model.getId()).intValue();
							} catch (NumberFormatException e) {
								MGToast.showToast("id错误!");
								return;
							}
							gotoRefundApplication(id);
						}
					});
				} else if (refund_status == 1) {
					// 退款中
				} else if (refund_status == 2) {
					// 已经退款
				}

			}

			/**
			 * 点评
			 */
//			if (model.getDp_id() == 0 && model.getConsume_count() != 0) {
				// 0代表 :没有点评
				// Consume_count 不是0就表示已经消费,已经消费就可以点评
				SDViewUtil.show(mLl_button);
				SDViewUtil.show(tv_rate);
				tv_rate.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mActivity, AddCommentActivity.class);
						intent.putExtra(AddCommentActivity.EXTRA_ID, model.getDeal_id());
						intent.putExtra(AddCommentActivity.EXTRA_NAME, model.getName());
						intent.putExtra(AddCommentActivity.EXTRA_TYPE, CommentType.DEAL);
						mActivity.startActivity(intent);
					}
				});
//			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (model.getDeal_id() > 0) {
						Intent intent = new Intent(mActivity, TuanDetailActivity.class);
						intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getDeal_id());
						mActivity.startActivity(intent);
					} else if (model.getDeal_id() == 0) {
						Intent intent = new Intent(mActivity, StoreDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt(StoreDetailActivity.EXTRA_SHOP_ID,
								Integer.valueOf(model.getLocation_id()).intValue());
						bundle.putInt("type", 0);
						intent.putExtras(bundle);
						mActivity.startActivity(intent);
					} else {
						MGToast.showToast("未找到商品ID");
					}
				}
			});
		}
		return convertView;
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
	protected void clickConfirmationReceipt(final Uc_orderGoodsModel model) {
		if (model == null) {
			return;
		}

		SDDialogConfirm dialog = new SDDialogConfirm();
		dialog.setTextContent("确认收货?");
		dialog.setmListener(new SDDialogCustomListener() {
			@Override
			public void onDismiss(SDDialogCustom dialog) {

			}

			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog) {
				requestConfirmationReceipt(model);
			}

			@Override
			public void onClickCancel(View v, SDDialogCustom dialog) {

			}
		});
		dialog.show();
	}

	/**
	 * 确认收货
	 * 
	 * @param model
	 */
	protected void requestConfirmationReceipt(final Uc_orderGoodsModel model) {
		if (model == null) {
			return;
		}

		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_order");
		requestModel.putAct("verify_delivery");
		requestModel.put("item_id", model.getId());
		InterfaceServer.getInstance().requestInterface(requestModel, new SDRequestCallBack<BaseActModel>() {
			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("");
			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
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
	protected void requestQueryLogistics(Uc_orderGoodsModel model) {
		if (model == null) {
			return;
		}
		RequestModel requestModel = new RequestModel();
		requestModel.putCtl("uc_order");
		requestModel.putAct("check_delivery");
		requestModel.put("item_id", model.getId());
		InterfaceServer.getInstance().requestInterface(requestModel,
				new SDRequestCallBack<Uc_order_check_deliveryActModel>() {
					@Override
					public void onStart() {
						SDDialogManager.showProgressDialog("");
					}

					@Override
					public void onFinish() {
						SDDialogManager.dismissProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (actModel.getStatus() == 1) {
							String url = actModel.getUrl();
							if (!TextUtils.isEmpty(url)) {
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