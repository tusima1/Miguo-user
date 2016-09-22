package com.fanwe.adapter;

import java.math.BigDecimal;
import java.util.List;













import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.app.App;
import com.fanwe.common.CommonInterface;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.Deal_attrModel;
import com.fanwe.model.Event_edtailModelList;
import com.fanwe.model.Event_indexActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.sunday.eventbus.SDEventManager;

public class EventDetailAdapter extends SDSimpleBaseAdapter<Event_edtailModelList> {
	
	public  boolean isShow;
	public EventDetailAdapter(List<Event_edtailModelList> listModel,
			Activity activity, boolean isShow) {
		super(listModel, activity);
		this.isShow=isShow;
	}
	
	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent) {
		
		return R.layout.item_event_detail;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent,
			final Event_edtailModelList model) {
		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		TextView tv_brief =  ViewHolder.get(convertView, R.id.tv_brief);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		LinearLayout ll_goshop = ViewHolder.get(convertView, R.id.ll_goshop);
		TextView  mTv_tag = ViewHolder.get(convertView, R.id.tv_money_tag);
		TextView tv_number = ViewHolder.get(convertView, R.id.tv_original_price);
		TextView tv_distance = ViewHolder.get(convertView, R.id.tv_distance);
		TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
		TextView tv_totalMoney = ViewHolder.get(convertView, R.id.tv_totalmoney);
		
		if(model != null)
		{
			
			SDViewBinder.setImageView(iv_image, model.getSpecial_icon());
			SDViewBinder.setTextView(tv_name, model.getSpecial_name());
			SDViewBinder.setTextView(tv_brief, model.getSpecial_desc());
			SDViewBinder.setTextView(tv_number, model.getBuy_count());
			SDViewBinder.setTextView(tv_distance, model.getDistance());
			BigDecimal bd1 = new BigDecimal(model.getSpecial_price());
			bd1 = bd1.setScale(1,BigDecimal.ROUND_HALF_UP);
			SDViewBinder.setTextView(tv_money, bd1+"");
			BigDecimal bd2 = new BigDecimal(model.getCurrent_price());
			bd2 = bd2.setScale(1,BigDecimal.ROUND_HALF_UP);
			tv_totalMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			SDViewBinder.setTextView(tv_totalMoney, bd2+"");
			if(isShow)
			{
				ll_goshop.setBackgroundResource(R.drawable.my_event_detail_now);
				tv_money.setTextColor(Color.parseColor("#fc6f07"));
				mTv_tag.setTextColor(Color.parseColor("#fc6f07"));
				
				ll_goshop.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						requestAddCart(model);
					}
				});
			}else
			{
				ll_goshop.setBackgroundResource(R.drawable.my_event_edtail_no);
				tv_money.setTextColor(Color.parseColor("#999999"));
				mTv_tag.setTextColor(Color.parseColor("#999999"));
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(App.getApplication(), TuanDetailActivity.class);
					
					intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getDeal_id());
					mActivity.startActivity(intent);
					
				}
			});
			
		}
		
	}
	private void requestAddCart(Event_edtailModelList model2)
	{
		// TODO 请求接口
		RequestModel model = new RequestModel();
		model.putCtl("cart");
		model.putAct("addcart");
		model.putUser();
		model.put("id", model2.getDeal_id());
		
		SDRequestCallBack<BaseActModel> handler = new SDRequestCallBack<BaseActModel>()
		{

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				SDDialogManager.dismissProgressDialog();
				Intent intent = null;
				switch (actModel.getStatus())
				{
				case -1:
					intent = new Intent(mActivity, LoginActivity.class);
					mActivity.startActivity(intent);
					break;
				case 0:

					break;
				case 1:
					CommonInterface.updateCartNumber();
					SDEventManager.post(EnumEventTag.ADD_CART_SUCCESS.ordinal());
					intent = new Intent(mActivity, ShopCartActivity.class);
					mActivity.startActivity(intent);
					break;

				default:
					break;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				SDDialogManager.dismissProgressDialog();
			}

			@Override
			public void onFinish()
			{

			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);

	}

}
