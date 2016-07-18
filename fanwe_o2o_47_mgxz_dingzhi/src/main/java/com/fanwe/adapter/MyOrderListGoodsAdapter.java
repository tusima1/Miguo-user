package com.fanwe.adapter;

import java.util.List;

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
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.BaseActModel;
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

public class MyOrderListGoodsAdapter extends SDBaseAdapter<Uc_orderGoodsModel>
{

	private static final int COLOR_ENABLE = R.color.main_color;
	private static final int COLOR_DISABLE = R.color.gray;

	private boolean mShowActions = true;
	
	private int mStatus_value;

	public MyOrderListGoodsAdapter(List<Uc_orderGoodsModel> listModel, boolean showActions, Activity activity,int status_value)
	{
		super(listModel, activity);
		this.mShowActions = showActions;
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
		TextView tv_evaluate = ViewHolder.get(convertView, R.id.tv_evaluate);
		TextView tv_number = ViewHolder.get(convertView, R.id.tv_number);
		TextView tv_sno = ViewHolder.get(convertView, R.id.tv_sno);
		View v_line = ViewHolder.get(R.id.v_line, convertView);
		TextView tv_total_price = ViewHolder.get(convertView, R.id.tv_total_price);
	
		final Uc_orderGoodsModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setImageView(iv_image, model.getDeal_icon());
			SDViewBinder.setTextView(tv_name, model.getSub_name());
			SDViewBinder.setTextView(tv_number, String.valueOf(model.getNumber()));
			SDViewBinder.setTextView(tv_total_price, model.getTotal_priceFormat());
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
			
			if(mShowActions && model.getDeal_id() != 0)
			{
				if(mStatus_value == 0 || mStatus_value == 1)
				{
					SDViewBinder.setTextView(tv_order, "待付款");
				}else if(mStatus_value == 2)
				{
					if(model.getConsume_count() == 0)
					{
						SDViewBinder.setTextView(tv_order, "待消费");
					}else
					{
						if(model.getDp_id() == 0)
						{
							SDViewBinder.setTextView(tv_order, "待评价");
							SDViewUtil.show(mLl_button);
							SDViewUtil.show(tv_evaluate);
							SDViewUtil.show(v_line);
							tv_evaluate.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) 
								{
									Intent intent = new Intent(mActivity, AddCommentActivity.class);
									intent.putExtra(AddCommentActivity.EXTRA_ID, model.getDeal_id());
									intent.putExtra(AddCommentActivity.EXTRA_NAME, model.getName());
									intent.putExtra(AddCommentActivity.EXTRA_TYPE, CommentType.DEAL);
									mActivity.startActivity(intent);
								}
							});
						}else 
						{
							SDViewUtil.hide(mLl_button);
							SDViewUtil.hide(tv_evaluate);
							SDViewUtil.hide(v_line);
							SDViewBinder.setTextView(tv_order, "已评价");
						}
					}
				}else if(mStatus_value == 3)
				{
					SDViewBinder.setTextView(tv_order, "退款中");
					SDViewUtil.hide(mLl_button);
				}else if(mStatus_value == 4)
				{
					SDViewBinder.setTextView(tv_order, "已退款");
					SDViewUtil.hide(mLl_button);
				}else if(mStatus_value == 5)
				{
					if(model.getDp_id() == 0)
					{
						SDViewBinder.setTextView(tv_order, "待评价");
						SDViewUtil.show(mLl_button);
						SDViewUtil.show(tv_evaluate);
						SDViewUtil.show(v_line);
						tv_evaluate.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) 
							{
								Intent intent = new Intent(mActivity, AddCommentActivity.class);
								intent.putExtra(AddCommentActivity.EXTRA_ID, model.getDeal_id());
								intent.putExtra(AddCommentActivity.EXTRA_NAME, model.getName());
								intent.putExtra(AddCommentActivity.EXTRA_TYPE, CommentType.DEAL);
								mActivity.startActivity(intent);
							}
						});
					}else
					{
						SDViewUtil.hide(mLl_button);
						SDViewUtil.hide(tv_evaluate);
						SDViewUtil.hide(v_line);
						SDViewBinder.setTextView(tv_order, "已评价");
					}
				}else if(mStatus_value == 6)
				{
					SDViewBinder.setTextView(tv_order, "已取消");
				}
			}else
			{
				SDViewUtil.hide(mLl_button);
			}
			if(model.getDeal_id() ==0)
			{
				
			}
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
						bundle.putInt(StoreDetailActivity.EXTRA_SHOP_ID, model.getLocation_id());
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