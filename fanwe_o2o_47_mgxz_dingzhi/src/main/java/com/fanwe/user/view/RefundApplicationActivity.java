package com.fanwe.user.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.BaseActivity;
import com.fanwe.RefundGoodsActivity;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.StickyScrollView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.OrderRefundItemInfo;
import com.fanwe.model.RefundItemInfo;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * 
 * @author didikee
 * 
 *         退款申请
 *
 */
public class RefundApplicationActivity extends BaseActivity {

	private SDStickyScrollView mPtr;
	private TextView mTv_order_idNum;
	private TextView mTv_name;
	private TextView mTv_time;
	private TextView mTv_total_price;
	private TextView mTv_total_num;
	private TextView mTv_used;
	private TextView mTv_has_refund;
	private TextView mTv_refunding;
	private TextView mTv_refund_num;
	private TextView mTv_refund_money;
	private Button mBt_add;
	private Button mBt_dec;
	
	//退款商品id
	private int mOrderItemID=-1;
	private TextView mTv_payed;
	
	private float mFinalRefundMoney=0.0f;//退款金额 =单价*退款数量(mUnitPrice*mNum)
	private String mUnitPrice="";//单价
	private int mNum=1;//退款数量,默认为1.
	private int mGoodsTotalNum=1;
	private String mEtContent="七天无理由退货";//默认为
	private Button mBt_submit;
	private EditText mEt;
	private View mRootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_refund_application);
		init();
	}

	private void init() {
		initTitle();
		initView();
		getIntentData();
		requestData();
	}

	private void initView() {
		mPtr = (SDStickyScrollView) findViewById(R.id.ptr_sv);
		View layoutList = findViewById(R.id.layout_list);
		// 商品编号
		mTv_order_idNum = (TextView) layoutList.findViewById(R.id.tv_order_idNum);
		mTv_name = (TextView) layoutList.findViewById(R.id.tv_name);
		mTv_time = (TextView) layoutList.findViewById(R.id.tv_time);
		mTv_total_price = (TextView) layoutList.findViewById(R.id.tv_total_price);
		mTv_payed = (TextView) layoutList.findViewById(R.id.tv_payed);
		// 商品数量
		mTv_total_num = (TextView) layoutList.findViewById(R.id.tv_total_num);
		mTv_used = (TextView) layoutList.findViewById(R.id.tv_used);
		mTv_has_refund = (TextView) layoutList.findViewById(R.id.tv_has_refund);
		mTv_refunding = (TextView) layoutList.findViewById(R.id.tv_refunding);
		// 退货数量
		mTv_refund_num = (TextView) layoutList.findViewById(R.id.tv_refund_num);
		// 可退款金额
		mTv_refund_money = (TextView) layoutList.findViewById(R.id.tv_refund_money);

		mBt_add = (Button) layoutList.findViewById(R.id.bt_add);
		mBt_dec = (Button) layoutList.findViewById(R.id.bt_dec);
		
		mBt_submit = (Button) findViewById(R.id.bt_submit);
		mEt = (EditText) findViewById(R.id.et_content);
//		mTv_order_idNum.setFocusable(true);
//		mTv_order_idNum.requestFocus();
		
		mBt_add.setOnClickListener(this);
		mBt_dec.setOnClickListener(this);
		mBt_submit.setOnClickListener(this);

		mPtr.setMode(Mode.PULL_FROM_START);
		mPtr.setOnRefreshListener(new OnRefreshListener<StickyScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<StickyScrollView> refreshView) {
				requestData();
			}
		});
		mRootView = findViewById(R.id.froot);
	}
	private void requestData() {
		RequestModel model = new RequestModel();
		model.putCtl("uc_order");
		model.putAct("refund_item");
		if (mOrderItemID==-1) {
			SDToast.showToast("商品id错误!");
			finish();
			return;
		}
		model.put("order_item_id", mOrderItemID);
		SDRequestCallBack<RefundItemInfo> handler = new SDRequestCallBack<RefundItemInfo>() {
			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("请稍等");
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					bindData(actModel.getOrder());
				}else {
					SDToast.showToast(actModel.getInfo());
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
				mPtr.onRefreshComplete();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);

	}

	private void bindData(OrderRefundItemInfo info) {
		if (info==null) {
			return;
		}
		mTv_order_idNum.setText(info.getOrder_sn());
		mTv_name.setText(info.getName());
		mTv_time.setText(info.getCreate_time());
		//商品总数
		mGoodsTotalNum=Integer.valueOf(info.getNumber()).intValue();
		mTv_total_num.setText(info.getNumber());
		mTv_total_price.setText(info.getTotal_price());
		mTv_used.setText(info.getConsume_count());
		mTv_has_refund.setText(info.getRefunded());
		mTv_refunding.setText(info.getRefunding());
		mTv_payed.setText(",已支付:"+info.getPay_amount());
		
		mUnitPrice=info.getUnit_price();
		float refundMoney = getCalculateRefundMoney(info.getUnit_price(), 1);
		mTv_refund_money.setText(""+refundMoney);
	}

	/**
	 * 
	 * @return float
	 *  计算预计可退款金额
	 * 
	 */
	private float getCalculateRefundMoney(String unit,int num){
		if (TextUtils.isEmpty(unit) || num==0) {
			return 0f;
		}
		float unitF = Float.valueOf(unit).floatValue();
		return unitF * num;
	}
	private void getIntentData() {
		mOrderItemID= getIntent().getExtras().getInt(RefundGoodsActivity.EXTRA_ID,-1);
	}

	
	@Override
	public void onClick(View v) {
		if (v==mBt_add) {
			clickAdd();
		}else if (v==mBt_dec) {
			clickDec();
		}else if (v==mBt_submit) {
			submit();
		}
	}
	
	private void clickDec() {
		if (mNum<=1) {
			SDToast.showToast("退货商品数量最少为1");
			return;
		}
		mNum--;
		mTv_refund_num.setText(""+mNum);
		mTv_refund_money.setText(""+getCalculateRefundMoney(mUnitPrice, mNum));
	}

	private void clickAdd() {
		if (mNum>=mGoodsTotalNum) {
			SDToast.showToast("退货商品数量最多为"+mGoodsTotalNum);
			return;
		}
		mNum++;
		mTv_refund_num.setText(""+mNum);
		mTv_refund_money.setText(""+getCalculateRefundMoney(mUnitPrice, mNum));
	}

	
	private void submit(){
		RequestModel model = new RequestModel();
		model.putCtl("uc_order");
		model.putAct("do_refund_item");
		if (mOrderItemID==-1) {
			SDToast.showToast("商品id错误!");
			return;
		}
		model.put("id", mOrderItemID);
		if (mNum < 1 || mNum >mGoodsTotalNum) {
			return;
		}
		model.put("number", mNum);
		if (!TextUtils.isEmpty(mEt.getText().toString())) {
			mEtContent=mEt.getText().toString();
		}
		model.put("content", mEtContent);
		SDRequestCallBack<BaseActModel> handler = new SDRequestCallBack<BaseActModel>() {
			@Override
			public void onStart() {
				SDDialogManager.showProgressDialog("请稍等");
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				SDToast.showToast(actModel.getInfo());
				if (actModel.getStatus() == 1) {
					finish();
				}
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
				mPtr.onRefreshComplete();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);

	}
	private void initTitle() {
		mTitle.setMiddleTextTop("退款申请");
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPtr.setRefreshing();
	}
}
