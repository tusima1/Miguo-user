package com.fanwe.adapter;


import java.math.BigDecimal;
import java.util.List;

import com.fanwe.OrderDetailActivity;
import com.fanwe.RefundGoodsActivity;
import com.fanwe.RefundTuanActivity;
import com.fanwe.common.CommonInterface;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.Uc_orderGoodsModel;
import com.fanwe.model.Uc_orderModel;
import com.fanwe.model.Uc_orderModelParcelable;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyOrderListAdapter extends SDBaseAdapter<Uc_orderModel>
{
	
	private static final int COLOR_DISABLE = R.color.gray;
	private static final int COLOR_ENABLE = R.color.main_color;
	protected static final RefundGoodsActivity SetUc_orderModel = null;
	private List<Uc_orderGoodsModel> listGoods;
	
	private boolean isDelect = false;
	private TextView tv_delect;
	
	public MyOrderListAdapter(List<Uc_orderModel> listModel, Activity activity,boolean isDelect)
	{
		super(listModel, activity);
		this.isDelect = isDelect;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{

		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_my_order_list, null);
		}
		LinearLayout ll_goods = ViewHolder.get(convertView, R.id.ll_goods);
		LinearLayout ll_order_list = ViewHolder.get(convertView, R.id.ll_order_list);
		TextView tv_pay_again = ViewHolder.get(R.id.tv_pay_again, convertView);
		TextView tv_money = ViewHolder.get(R.id.tv_money, convertView);
		tv_delect = ViewHolder.get(R.id.tv_delect, convertView);
		TextView tv_pay = ViewHolder.get(R.id.tv_pay, convertView);
		TextView tv_refund = ViewHolder.get(R.id.tv_refund, convertView); 
		TextView tv_cancel_order = ViewHolder.get(R.id.tv_cancel_order, convertView); 

		final Uc_orderModel model = getItem(position);
		if (model != null)
		{
			
			listGoods = model.getDeal_order_item();
			MyOrderListGoodsAdapter adapter = new MyOrderListGoodsAdapter(listGoods,true,mActivity,model.getStatus_value());
			if (!SDCollectionUtil.isEmpty(listGoods))
			{
				ll_goods.removeAllViews();
				final int size = listGoods.size();
				for (int i = 0; i < size; i++)
				{
					ll_goods.addView(adapter.getView(i, null, null));
					if(listGoods.get(i).getDeal_id()==0 && isDelect)
					{
						SDViewUtil.show(tv_delect);
						SDViewUtil.show(ll_order_list);
						tv_delect.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v)
							{
								deleteOrder(model, position);
							}
						});
					}else
					{
						SDViewUtil.hide(tv_delect);
						SDViewUtil.hide(ll_order_list);
					}
				}
			}
			
			if (!isDelect)
			{
				if(model.getStatus_value() == 0)
				{
					SDViewUtil.show(ll_order_list);
					SDViewUtil.show(tv_pay);
					tv_pay.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) 
						{
							Intent intent = new Intent(mActivity, OrderDetailActivity.class);
							intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, model.getId());
							mActivity.startActivity(intent);
						}
					});
					SDViewUtil.show(tv_cancel_order);
					tv_cancel_order.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							cancelOrder(model, position);
						}
					});
				}else
				{
					SDViewUtil.hide(tv_pay);
					SDViewUtil.hide(tv_cancel_order);
					SDViewUtil.hide(ll_order_list);
				}
				if(model.getStatus_value() ==1)
				{
					BigDecimal bd1 = new BigDecimal(Float.parseFloat(model.getTotal_price())
							-Float.parseFloat(model.getPay_amount()));
					bd1 = bd1.setScale(1,BigDecimal.ROUND_HALF_UP);
					SDViewBinder.setTextView(tv_money, "还需支付"+bd1+"元");
					SDViewUtil.show(tv_pay_again);
					tv_pay_again.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v)
						{
							Intent intent = new Intent(mActivity, OrderDetailActivity.class);
							intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, model.getId());
							mActivity.startActivity(intent);
						}
					});
					SDViewUtil.show(tv_cancel_order);
					tv_cancel_order.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) 
						{
							cancelOrder(model, position);
						}
					});
				}else
				{
					SDViewBinder.setTextView(tv_money, "");
					SDViewUtil.hide(tv_pay_again);
					//SDViewUtil.hide(tv_cancel_order);
					SDViewUtil.hide(tv_delect);
				}
				if(model.getStatus_value() ==2)
				{
					SDViewUtil.show(ll_order_list);
					SDViewUtil.show(tv_refund);
					tv_refund.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							if (listGoods.get(0).isShop())
							{
								Uc_orderModelParcelable uc_orderModel1 =new Uc_orderModelParcelable();
								uc_orderModel1.setId(model.getId());
								uc_orderModel1.setOrder_sn(model.getOrder_sn());
								uc_orderModel1.setCreate_time(model.getCreate_time());
								uc_orderModel1.setC(model.getC());
								uc_orderModel1.setPayment_id(model.getPayment_id());
								uc_orderModel1.setStatus(model.getStatus());
								uc_orderModel1.setTotal_priceFormat(model.getTotal_priceFormat());
								uc_orderModel1.setPay_amountFormat(model.getPay_amountFormat());
								
								Intent intent = new Intent(mActivity, RefundGoodsActivity.class);
								Bundle bundle =new Bundle();
								bundle.putParcelable("model", uc_orderModel1);
								bundle.putInt(RefundGoodsActivity.EXTRA_ID,  model.getId());
								intent.putExtras(bundle);
								mActivity.startActivity(intent);
							} else
							{
								Uc_orderModelParcelable uc_orderModel2 =new Uc_orderModelParcelable();
								uc_orderModel2.setId(model.getId());
								uc_orderModel2.setOrder_sn(model.getOrder_sn());
								uc_orderModel2.setCreate_time(model.getCreate_time());
								uc_orderModel2.setC(model.getC());
								uc_orderModel2.setPayment_id(model.getPayment_id());
								uc_orderModel2.setStatus(model.getStatus());
								uc_orderModel2.setTotal_priceFormat(model.getTotal_priceFormat());
								uc_orderModel2.setPay_amountFormat(model.getPay_amountFormat());
								Intent intent = new Intent(mActivity, RefundTuanActivity.class);
								Bundle bundle =new Bundle ();
								bundle.putParcelable("model", uc_orderModel2);
								bundle.putInt(RefundGoodsActivity.EXTRA_ID,  model.getId());
								intent.putExtras(bundle);
								mActivity.startActivity(intent);
							}
						}
					});
				}else
				{
					SDViewUtil.hide(ll_order_list);
					SDViewUtil.hide(tv_refund);
				}
				
				if(model.getStatus_value() ==3 || model.getStatus_value() ==4 
						|| model.getStatus_value() ==5 || model.getStatus_value() ==6)
				{
					SDViewUtil.hide(ll_order_list);
				}else
				{
					SDViewUtil.show(ll_order_list);
				}
				
			}else
			{
				
				if( model .getStatus_value() ==0 || model.getStatus_value() == 4 ||model.getStatus_value() == 6 || model.getStatus_value() == 5)
				{
					SDViewUtil.show(ll_order_list);
					SDViewUtil.show(tv_delect);
					tv_delect.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) 
						{
							deleteOrder(model, position);
						}
					});
				}else
				{
					if(model.getStatus_value() == 1)
					{
						SDViewUtil.show(ll_order_list);
						SDViewUtil.show(tv_cancel_order);
						tv_cancel_order.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								cancelOrder(model, position);
							}
						});
					}else
					{
						SDViewUtil.hide(ll_order_list);
						SDViewUtil.hide(tv_delect);
						SDViewUtil.hide(tv_cancel_order);
					}
				}
			}
		}
		return convertView;
	}
	

	protected void cancelOrder(final Uc_orderModel model, final int position)
	{
		if (model == null)
		{
			return;
		}

		SDDialogConfirm dialog = new SDDialogConfirm();
		dialog.setTextContent("确定取消订单？");
		dialog.setmListener(new SDDialogCustomListener()
		{

			@Override
			public void onDismiss(SDDialogCustom dialog)
			{
				
			}

			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog)
			{
				requestCanCelOrder(model, position);
			}

			@Override
			public void onClickCancel(View v, SDDialogCustom dialog)
			{
				
			}
		});
		dialog.show();
		
	}

	protected void requestCanCelOrder(final Uc_orderModel model,final int position)
	{
		CommonInterface.requestCanCelOrder(model.getId(), new SDRequestCallBack<BaseActModel>()
				{

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo)
					{
						if (actModel.getStatus() == 1)
						{
							SDToast.showToast("订单取消成功");
							mListModel.remove(position);
							notifyDataSetChanged();
						}
					}

					@Override
					public void onStart()
					{
						SDDialogManager.showProgressDialog("正在取消");
					}

					@Override
					public void onFinish()
					{
						SDDialogManager.dismissProgressDialog();
					}

					@Override
					public void onFailure(HttpException error, String msg)
					{
						
					}
				});
		
	}

	private void deleteOrder(final Uc_orderModel model, final int position)
	{
		if (model == null)
		{
			return;
		}
		
		SDDialogConfirm dialog = new SDDialogConfirm();
		dialog.setTextContent("确定删除订单？");
		dialog.setmListener(new SDDialogCustomListener()
		{

			@Override
			public void onDismiss(SDDialogCustom dialog)
			{
				
			}
			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog)
			{
				requestDeleteOrder(model, position);
			}

			@Override
			public void onClickCancel(View v, SDDialogCustom dialog)
			{
				
			}
		});
		dialog.show();
	}

	private void requestDeleteOrder(final Uc_orderModel model, final int position)
	{
		CommonInterface.requestDeleteOrder(model.getId(), new SDRequestCallBack<BaseActModel>()
		{

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					mListModel.remove(position);
					notifyDataSetChanged();
				}
			}

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("正在删除");
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				
			}
		});
	}
	
	
}
