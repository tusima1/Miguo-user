package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.adapter.MyOrderListGoodsAdapter;
import com.fanwe.adapter.MyRefundListGoodsAdapter;
import com.fanwe.adapter.MyRefundListGoodsAdapter.OnPaymentId;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.StickyScrollView;
import com.fanwe.library.dialog.SDDialogManager;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_orderGoodsModel;
import com.fanwe.model.Uc_orderModel;
import com.fanwe.model.Uc_orderModelParcelable;
import com.fanwe.model.Uc_order_refundActModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.http.ResponseInfo;
import com.sunday.eventbus.SDEventManager;
import com.umeng.socialize.utils.Log;

/**
 * 商品申请退款
 * 
 * @author Administrator
 * 
 */
public class RefundGoodsActivity extends BaseActivity
{

	public static final String EXTRA_ID = "extra_id";

	protected SDStickyScrollView mSsv_all;
	protected LinearLayout mLl_deals;
	protected LinearLayout mLl_coupon;
	protected EditText mEt_content;
	protected TextView mTv_submit;

	protected String mStrContent;

	protected List<Uc_orderModelParcelable> mListModel = new ArrayList<Uc_orderModelParcelable>();
	protected MyRefundListGoodsAdapter mAdapter;

	protected int mId;

	protected Uc_orderModelParcelable uc_orderModel;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_refund_goods);
		init();
	}

	protected void init()
	{
		getIntentData();
		initTitle();
		findViews();
		bindDefaultData();
		initSDStickyScrollView();
		registerClick();
	}

	protected void initTitle()
	{
		mTitle.setMiddleTextTop("申请退款");
	}

	private void getIntentData()
	{
		Bundle bundle = getIntent().getExtras();
		mId  = bundle.getInt(EXTRA_ID,0);
		uc_orderModel = (Uc_orderModelParcelable) bundle.getParcelable("model");
		mListModel.add(uc_orderModel);
		if (mId <= 0)
		{
			MGToast.showToast("id为空");
			finish();
		}
	}
	
	private int payId=0;
	private void bindDefaultData()
	{
		
		mAdapter = new MyRefundListGoodsAdapter(mListModel, mActivity);
		mAdapter.setPayment(new  OnPaymentId() {
			@Override
			public void setPaymentId(int pay_id) {
				payId=pay_id;
			}
		});
		
	}

	private void findViews()
	{
		mSsv_all = (SDStickyScrollView) findViewById(R.id.ssv_all);
		mLl_deals = (LinearLayout) findViewById(R.id.ll_deals);
		mLl_coupon = (LinearLayout) findViewById(R.id.ll_coupon);
		mEt_content = (EditText) findViewById(R.id.et_content);
		mTv_submit = (TextView) findViewById(R.id.tv_submit);
	}

	private void registerClick()
	{
		mTv_submit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				clickSubmit();
			}
		});
	}


	protected void clickSubmit()
	{
		mStrContent = mEt_content.getText().toString();
		if (isEmpty(mStrContent))
		{
			MGToast.showToast("请输入内容");
			return ;
		}
		tuiKuan();//退款
	}
	
	protected void tuiKuan()
	{
		RequestModel model = new RequestModel();
		model.putUser();
		model.putCtl("uc_order");
		model.putAct("do_refund_order");
		model.put("content", mStrContent);
		model.put("order_id", mId);
		model.put("refund_type", payId);

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

	protected void requestSubmit()
	{
		RequestModel model = new RequestModel();
		model.putUser();
		model.putCtl("uc_order");
		model.putAct("do_refund_order");
		model.put("content", mStrContent);
		model.put("order", mId);
		model.put("refund_type", payId);

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

	private void initSDStickyScrollView()
	{
		mSsv_all.setMode(Mode.PULL_FROM_START);
		mSsv_all.setOnRefreshListener(new OnRefreshListener2<StickyScrollView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<StickyScrollView> refreshView)
			{
				requestData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<StickyScrollView> refreshView)
			{

			}
		});
		mSsv_all.setRefreshing();
	}

	protected void requestData()
	{
		RequestModel model = new RequestModel();
		model.putUser();
		model.putCtl("uc_order");
		model.putAct("refund");
		model.put("item_id", mId);

		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<Uc_order_refundActModel>()
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
					mAdapter.setData(mListModel);
					bindData();
					updateTitle(actModel);
				}
			}
		});
	}
	

	protected void updateTitle(BaseActModel actModel)
	{
		if (actModel != null)
		{
			String title = actModel.getPage_title();
			if (!isEmpty(title))
			{
				mTitle.setMiddleTextTop(title);
			}
		}
	}

	protected void bindData()
	{
		if (mAdapter != null)
		{
			mLl_deals.removeAllViews();
			for (int i = 0; i < mAdapter.getCount(); i++)
			{
				View view = mAdapter.getView(i, null, null);
				mLl_deals.addView(view);
			}
		}
	}

}
