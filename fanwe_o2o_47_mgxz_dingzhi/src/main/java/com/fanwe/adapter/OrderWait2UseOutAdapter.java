package com.fanwe.adapter;

import java.util.List;

import com.fanwe.common.CommonInterface;
import com.fanwe.customview.MaxHeightListView;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.OrderInItem;
import com.fanwe.model.OrderOutItem;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderWait2UseOutAdapter extends SDBaseAdapter<OrderOutItem> {

	private boolean isDelect = false;
	private MaxHeightListView hListView;
	private Activity mAct;

	public OrderWait2UseOutAdapter(List<OrderOutItem> listModel, Activity activity, boolean isDelect) {
		super(listModel, activity);
		this.isDelect = isDelect;
		this.mAct = activity;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_order_list_out, null);
		}
		LinearLayout ll_goods = ViewHolder.get(convertView, R.id.ll_goods);
		LinearLayout ll_show = ViewHolder.get(R.id.ll_show, convertView);
		TextView tv_delete = ViewHolder.get(R.id.tv_delete, convertView);
		hListView = (MaxHeightListView) ll_goods.findViewById(R.id.h_listview);

		final OrderOutItem item = getItem(position);
		if (item != null) {
			// 判断是否可以显示或者隐藏
			int status_value = item.getStatus_value();
			// 显示删除按钮
			if (isDelect) {
				if (status_value == 0 || status_value == 4 || status_value == 5 || status_value == 6) {
					SDViewUtil.show(ll_show);
					tv_delete.setText("删除");
					tv_delete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							deleteOrder(item, position);
						}
					});
				}
			}
			if (!isDelect) {
				if (status_value == 0) {
					SDViewUtil.show(ll_show);
					tv_delete.setText("取消");
					tv_delete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							cancelOrder(item, position);
						}
					});
				}
			}
			// 不管什么状态
			if (status_value == 1) {
				SDViewUtil.show(ll_show);
				tv_delete.setText("取消");
				tv_delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cancelOrder(item, position);
					}
				});
			}
			
			// 加载子listview
						List<OrderInItem> deal_order_item = item.getDeal_order_item();
						OrderWait2UseInAdapter adapter = new OrderWait2UseInAdapter(deal_order_item, mActivity, item.getStatus(),
								item.getStatus_value());
						hListView.setAdapter(adapter);
		}
		// final Uc_orderModel model = getItem(position);
		// if (model != null) {
		//
		// listGoods = model.getDeal_order_item();
		//
		// if (!SDCollectionUtil.isEmpty(listGoods)) {
		// ll_goods.removeAllViews();
		// final int size = listGoods.size();
		// for (int i = 0; i < size; i++) {
		// ll_goods.addView(adapter.getView(i, null, null));
		// if (listGoods.get(i).getDeal_id() == 0 && isDelect) {
		// SDViewUtil.show(tv_delect);
		// // SDViewUtil.show(ll_order_list);
		// tv_delect.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// deleteOrder(model, position);
		// }
		// });
		// } else {
		// SDViewUtil.hide(tv_delect);
		// // SDViewUtil.hide(ll_order_list);
		// }
		// }
		// }

		// if (!isDelect) {
		// if (model.getStatus_value() == 0) {
		// SDViewUtil.show(ll_order_list);
		// SDViewUtil.show(tv_pay);
		// tv_pay.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(mActivity, OrderDetailActivity.class);
		// intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, model.getId());
		// mActivity.startActivity(intent);
		// }
		// });
		// SDViewUtil.show(tv_cancel_order);
		// tv_cancel_order.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// cancelOrder(model, position);
		// }
		// });
		// } else {
		// SDViewUtil.hide(tv_pay);
		// SDViewUtil.hide(tv_cancel_order);
		// SDViewUtil.hide(ll_order_list);
		// }
		// if (model.getStatus_value() == 1) {
		// BigDecimal bd1 = new BigDecimal(
		// Float.parseFloat(model.getTotal_price()) -
		// Float.parseFloat(model.getPay_amount()));
		// bd1 = bd1.setScale(1, BigDecimal.ROUND_HALF_UP);
		// SDViewBinder.setTextView(tv_money, "还需支付" + bd1 + "元");
		// SDViewUtil.show(tv_pay_again);
		// tv_pay_again.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(mActivity, OrderDetailActivity.class);
		// intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, model.getId());
		// mActivity.startActivity(intent);
		// }
		// });
		// SDViewUtil.show(tv_cancel_order);
		// tv_cancel_order.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// cancelOrder(model, position);
		// }
		// });
		// } else {
		// SDViewBinder.setTextView(tv_money, "");
		// SDViewUtil.hide(tv_pay_again);
		// // SDViewUtil.hide(tv_cancel_order);
		// SDViewUtil.hide(tv_delect);
		// }
		// if (model.getStatus_value() == 2) {
		// SDViewUtil.show(ll_order_list);
		// SDViewUtil.show(tv_refund);
		// tv_refund.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (listGoods.get(0).isShop()) {
		// Uc_orderModelParcelable uc_orderModel1 = new
		// Uc_orderModelParcelable();
		// uc_orderModel1.setId(model.getId());
		// uc_orderModel1.setOrder_sn(model.getOrder_sn());
		// uc_orderModel1.setCreate_time(model.getCreate_time());
		// uc_orderModel1.setC(model.getC());
		// uc_orderModel1.setPayment_id(model.getPayment_id());
		// uc_orderModel1.setStatus(model.getStatus());
		// uc_orderModel1.setTotal_priceFormat(model.getTotal_priceFormat());
		// uc_orderModel1.setPay_amountFormat(model.getPay_amountFormat());
		//
		// // Intent intent = new Intent(mActivity,
		// // RefundGoodsActivity.class);
		// Intent intent = new Intent(mActivity,
		// RefundApplicationActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putParcelable("model", uc_orderModel1);
		// // bundle.putInt(RefundGoodsActivity.EXTRA_ID,
		// // model.getId());
		// bundle.putInt(RefundGoodsActivity.EXTRA_ID,
		// listGoods.get(position).getId());
		// intent.putExtras(bundle);
		// mActivity.startActivity(intent);
		// } else {
		// Uc_orderModelParcelable uc_orderModel2 = new
		// Uc_orderModelParcelable();
		// uc_orderModel2.setId(model.getId());
		// uc_orderModel2.setOrder_sn(model.getOrder_sn());
		// uc_orderModel2.setCreate_time(model.getCreate_time());
		// uc_orderModel2.setC(model.getC());
		// uc_orderModel2.setPayment_id(model.getPayment_id());
		// uc_orderModel2.setStatus(model.getStatus());
		// uc_orderModel2.setTotal_priceFormat(model.getTotal_priceFormat());
		// uc_orderModel2.setPay_amountFormat(model.getPay_amountFormat());
		// // Intent intent = new Intent(mActivity,
		// // RefundTuanActivity.class);
		// Intent intent = new Intent(mActivity,
		// RefundApplicationActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putParcelable("model", uc_orderModel2);
		// // bundle.putInt(RefundGoodsActivity.EXTRA_ID,
		// // model.getId());
		// // bundle.putInt(RefundGoodsActivity.EXTRA_ID,
		// // model.get);
		// intent.putExtras(bundle);
		// mActivity.startActivity(intent);
		// }
		// }
		// });
		// } else {
		// SDViewUtil.hide(ll_order_list);
		// SDViewUtil.hide(tv_refund);
		// }
		//
		// if (model.getStatus_value() == 3 || model.getStatus_value() == 4 ||
		// model.getStatus_value() == 5
		// || model.getStatus_value() == 6) {
		// SDViewUtil.hide(ll_order_list);
		// } else {
		// SDViewUtil.show(ll_order_list);
		// }
		//
		// }

		/***
		 * 
		 */

		// else {
		//
		// if (model.getStatus_value() == 0 || model.getStatus_value() == 4 ||
		// model.getStatus_value() == 6
		// || model.getStatus_value() == 5) {
		// SDViewUtil.show(ll_order_list);
		// SDViewUtil.show(tv_delect);
		// tv_delect.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// deleteOrder(model, position);
		// }
		// });
		// } else {
		// if (model.getStatus_value() == 1) {
		// SDViewUtil.show(ll_order_list);
		// SDViewUtil.show(tv_cancel_order);
		// tv_cancel_order.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// cancelOrder(model, position);
		// }
		// });
		// } else {
		// SDViewUtil.hide(ll_order_list);
		// SDViewUtil.hide(tv_delect);
		// SDViewUtil.hide(tv_cancel_order);
		// }
		// }
		// }
		// }
		return convertView;
	}

	protected void cancelOrder(final OrderOutItem model, final int position) {
		if (model == null) {
			return;
		}

		SDDialogConfirm dialog = new SDDialogConfirm();
		dialog.setTextContent("确定取消订单？");
		dialog.setmListener(new SDDialogCustomListener() {

			@Override
			public void onDismiss(SDDialogCustom dialog) {

			}

			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog) {
				requestCanCelOrder(model, position);
			}

			@Override
			public void onClickCancel(View v, SDDialogCustom dialog) {

			}
		});
		dialog.show();

	}

	/**
	 * 取消订单
	 * 
	 * @param model
	 * @param position
	 */
	protected void requestCanCelOrder(final OrderOutItem model, final int position) {
		CommonInterface.requestCanCelOrder(Integer.valueOf(model.getId()).intValue(),
				new SDRequestCallBack<BaseActModel>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (actModel.getStatus() == 1) {
							MGToast.showToast("订单取消成功");
							mListModel.remove(position);
							notifyDataSetChanged();
						}
					}

					@Override
					public void onStart() {
						SDDialogManager.showProgressDialog("正在取消");
					}

					@Override
					public void onFinish() {
						SDDialogManager.dismissProgressDialog();
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});

	}

	/**
	 * 删除订单
	 * 
	 * @param model
	 * @param position
	 */
	private void deleteOrder(final OrderOutItem model, final int position) {
		if (model == null) {
			return;
		}

		SDDialogConfirm dialog = new SDDialogConfirm();
		dialog.setTextContent("确定删除订单？");
		dialog.setmListener(new SDDialogCustomListener() {

			@Override
			public void onDismiss(SDDialogCustom dialog) {

			}

			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog) {
				requestDeleteOrder(model, position);
			}

			@Override
			public void onClickCancel(View v, SDDialogCustom dialog) {

			}
		});
		dialog.show();
	}

	private void requestDeleteOrder(final OrderOutItem model, final int position) {
		CommonInterface.requestDeleteOrder(Integer.valueOf(model.getId()).intValue(),
				new SDRequestCallBack<BaseActModel>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (actModel.getStatus() == 1) {
							mListModel.remove(position);
							notifyDataSetChanged();
						}
					}

					@Override
					public void onStart() {
						SDDialogManager.showProgressDialog("正在删除");
					}

					@Override
					public void onFinish() {
						SDDialogManager.dismissProgressDialog();
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

}
