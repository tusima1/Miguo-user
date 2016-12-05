package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.ShopCartActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.app.App;
import com.fanwe.common.CommonInterface;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

public class HotelListTuanAdapter extends SDSimpleBaseAdapter<GoodsModel>{

	private int mNumber = 0;
	public HotelListTuanAdapter(List<GoodsModel> listModel, Activity activity,int number) {
		super(listModel, activity);
		this.mNumber = number;
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{
		
		return R.layout.item_hotel_list;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent,
			final GoodsModel model)
	{
		TextView tv_title = ViewHolder.get(R.id.tv_title, convertView);
		TextView tv_time = ViewHolder.get(R.id.tv_time, convertView);
		TextView tv_money = ViewHolder.get(R.id.tv_money, convertView);
		Button mTn_confirm = ViewHolder.get(R.id.act_confirm_order_btn_search, convertView);
		View v_line = ViewHolder.get(R.id.v_line, convertView);
		
		if(position == mListModel.size()-1)
		{
			SDViewUtil.hide(v_line);
			
		}else
		{
			SDViewUtil.show(v_line);
		}
		if(model.getAuto_order() == 1)
		{
			SDViewUtil.hide(tv_time);
		}else
		{
			SDViewUtil.show(tv_time);
		}
		SDViewBinder.setTextView(tv_title, model.getSub_name());
		SDViewBinder.setTextView(tv_money, model.getCurrent_price_format());
		mTn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				
				requestAddCart(model);
			}
			
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(App.getApplication(), GoodsDetailActivity.class);
				intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getId());
				intent.putExtra(TuanDetailActivity.EXTRA_HOTEL_NUM, mNumber);
				mActivity.startActivity(intent);
			}
		});
	}
	private void requestAddCart(GoodsModel data)
	{
		// TODO 请求接口
				RequestModel model = new RequestModel();
				model.putCtl("cart");
				model.putAct("addcart");
				model.putUser();
				model.put("id", data.getId());
				model.put("deal_attr", null);
				model.put("number",mNumber);
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
						Intent intent;
						switch (actModel.getStatus())
						{
						case -1:
							intent = new Intent(mActivity, ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
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
