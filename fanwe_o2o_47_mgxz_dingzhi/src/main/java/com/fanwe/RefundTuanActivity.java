package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;

import com.fanwe.adapter.MyRefundListGoodsAdapter;
import com.fanwe.adapter.MyRefundListGoodsAdapter.OnPaymentId;
import com.fanwe.adapter.RefundCouponAdapter;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.OrderCoupon_listModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_orderModel;
import com.fanwe.model.Uc_orderModelParcelable;
import com.fanwe.model.Uc_order_refund_couponActModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.http.ResponseInfo;
import com.sunday.eventbus.SDEventManager;

/**
 * 团购申请退款
 * 
 * @author Administrator
 * 
 */
public class RefundTuanActivity extends RefundGoodsActivity
{
	
	private MyRefundListGoodsAdapter mAdapterCoupon;
	
	protected List<Uc_orderModelParcelable> mListModel = new ArrayList<Uc_orderModelParcelable>();
	
	private Uc_orderModelParcelable uc_orderModel;
	private int payId;

	@Override
	protected void init()
	{   
		
		bindDefaultTuanData();
		super.init();
		
	}
	private void bindDefaultTuanData()
	{
		mAdapterCoupon = new MyRefundListGoodsAdapter(mListModel, mActivity);
		mAdapterCoupon.setPayment(new OnPaymentId() {
			
			@Override
			public void setPaymentId(int pay)
			{
				payId = pay;
			}
		});
	}

	@Override
	protected void requestData()
	{
		RequestModel model = new RequestModel();
		model.putUser();
		model.putCtl("uc_order");
		model.putAct("refund_coupon");
		model.put("item_id", mId);
		model.put("refund_type",payId);

		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<Uc_order_refund_couponActModel>()
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
				mSsv_all.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					//mListModel = actModel.getListItem();
					mAdapterCoupon.setData(mListModel);
					bindData();
					bindCoupons();
					updateTitle(actModel);
				}
			}
		});
	}

	protected void bindCoupons()
	{
		if (mAdapterCoupon == null)
		{
			SDViewUtil.hide(mLl_coupon);
			return;
		}

		SDViewUtil.show(mLl_coupon);
		mLl_coupon.removeAllViews();
		for (int i = 0; i < mAdapterCoupon.getCount(); i++)
		{
			View view = mAdapterCoupon.getView(i, null, null);
			mLl_coupon.addView(view);
		}
	}
	
	
	@Override
	protected void requestSubmit()
	{
		if (mAdapter == null)
		{
			return;
		}
		RequestModel model = new RequestModel();
		model.putUser();
		model.putCtl("uc_order");
		model.putAct("do_refund_order");
		model.put("content", mStrContent);
		model.put("order_id", mId);

		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<BaseActModel>()
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
					SDEventManager.post(EnumEventTag.REFRESH_ORDER_LIST.ordinal());
					finish();
				}
			}
		});
	}

}
