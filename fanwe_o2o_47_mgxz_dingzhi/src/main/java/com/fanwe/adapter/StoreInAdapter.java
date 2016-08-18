package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.common.CommonInterface;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.StoreIn_list;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.sunday.eventbus.SDEventManager;

public class StoreInAdapter extends SDBaseAdapter<StoreIn_list>
{

	public StoreInAdapter(List<StoreIn_list> listModel, Activity activity) {
		super(listModel, activity);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent,
			final StoreIn_list model)
	{
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_store_in, null);
		}
		ImageView iv_image = ViewHolder.get(R.id.iv_image, convertView);
		TextView tv_title = ViewHolder.get(R.id.tv_title, convertView);
		TextView tv_price = ViewHolder.get(R.id.tv_price, convertView);
		TextView tv_priceCurrent = ViewHolder.get(R.id.tv_priceCurrent, convertView);
		TextView tv_priceOrigin = ViewHolder.get(R.id.tv_priceOrigin, convertView);
		Button bt_buy = ViewHolder.get(R.id.bt_buy, convertView);
		
		DisplayMetrics metric = new DisplayMetrics();
	     mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int width = metric.widthPixels;
	    int height = (int) (width * 0.242 - 5);
	    SDViewUtil.setViewHeight(iv_image,height);
		if(model != null)
		{
			SDViewBinder.setTextView(tv_title,model.getName());
			SDViewBinder.setTextView(tv_priceCurrent, TextMoney.textFarmat3(model.getSalary()));
			SDViewBinder.setTextView(tv_price, TextMoney.textFarmat3(model.getOrigin_price()));
			SDViewBinder.setTextView(tv_priceOrigin,TextMoney.textFarmat3(model.getCurrent_price()));
			SDViewBinder.setImageView(iv_image, model.getImg());
			tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			bt_buy.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					requestAddCart(model.getId());
				}
			});
		}
		convertView.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(mActivity,TuanDetailActivity.class);
				intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getId());
				mActivity.startActivity(intent);
			}
		});
		return convertView;
	}

	protected void requestAddCart(String  id)
	{
		RequestModel model = new RequestModel();
		model.putCtl("cart");
		model.putAct("addcart");
		model.putUser();
		model.put("id", id);
		model.put("number", 1);
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
