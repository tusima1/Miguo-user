package com.fanwe.user.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.BaseActivity;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.library.customview.StickyScrollView;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getRefundPage.ModelRefundPage;
import com.fanwe.user.model.getRefundPage.ResultRefundPage;
import com.fanwe.user.presents.OrderHttpHelper;
import com.fanwe.utils.MGStringFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.miguo.live.views.customviews.MGToast;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author didikee
 * 
 *         退款申请
 *
 */
public class RefundApplicationActivity extends BaseActivity implements CallbackView2 {

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
	private OrderHttpHelper httpHelper;

	private String mOrder_id;
	private String mTuan_id;
	private int mRefunding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_refund_application);
		init();
	}

	private void init() {
		initTitle();
		httpHelper = new OrderHttpHelper(this);
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
		if ("".endsWith(mOrder_id)|| "".endsWith(mTuan_id)){
			MGToast.showToast("订单参数缺失!");
			finish();
			return;
		}
		httpHelper.getOrderItemTuangou(mOrder_id,mTuan_id);
	}

	private void bindData(ModelRefundPage info) {
		if (info==null) {
			return;
		}
//		private String balance_unit_price;// 退款单价
//		private String total_price;// 订单总价
//		private String refunding;// 退款中
//		private String pay_amount;// 已支付
//		private String maxrefundnumber;// 退款最大件数
//		private String number;// 商品数量
//		private String refund_unit_price;// 结算单价
//		private String consume_count;// 已使用
//		private String refunded; // 已退款
		mTv_order_idNum.setText(info.getOrder_sn());
		mTv_name.setText(info.getName());
		mTv_time.setText(info.getCreate_time());
		//商品总数
		mGoodsTotalNum=Integer.valueOf(info.getNumber()).intValue();
		mTv_total_num.setText(info.getNumber());
		mTv_total_price.setText(info.getTotal_price());
		//已使用
		int consume_count =Integer.valueOf(info.getConsume_count()).intValue();
		mTv_used.setText(consume_count+"");
		//已退款
		int refunded = Integer.valueOf(info.getRefunded()).intValue();
		mTv_has_refund.setText(refunded+"");

		String refunding = info.getRefunding();
		mRefunding = MGStringFormatter.getInt(refunding);
		mTv_refunding.setText(refunding);
		mTv_payed.setText("  ,  已支付:"+info.getPay_amount());
		
		mUnitPrice=info.getRefund_unit_price();
		float refundMoney = getCalculateRefundMoney(mUnitPrice, 1);
		mTv_refund_money.setText(""+refundMoney);

		//可退款商品总数 商品数量- 已使用 - 已退款 - 退款中;
		mGoodsTotalNum=mGoodsTotalNum -mRefunding -consume_count - refunded;
		if (mGoodsTotalNum<1){
			finish();
		}
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
		return getFloat2(unitF * num);
	}

	private float getFloat2(float num){
		BigDecimal b   =   new   BigDecimal(num);
		return b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
	}
	private void getIntentData() {
//		mOrderItemID= getIntent().getExtras().getInt(RefundGoodsActivity.EXTRA_ID,-1);
		Bundle extras = getIntent().getExtras();
		mOrder_id= extras.getString("extra_order_id", "");
		mTuan_id= extras.getString("extra_tuan_id", "");
//		mTuan_id="fb9e5ee2-1518-458d-ad68-d811a8c3ea9b";
//		mOrder_id="4591be94-7366-4d0c-9334-eb7f629e3334";
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
			MGToast.showToast("退货商品数量最少为1");
			return;
		}
		mNum--;
		mTv_refund_num.setText(""+mNum);
		mTv_refund_money.setText(""+getCalculateRefundMoney(mUnitPrice, mNum));
	}

	private void clickAdd() {
		if (mNum>=mGoodsTotalNum) {
			MGToast.showToast("退货商品数量最多为"+mGoodsTotalNum);
			return;
		}
		mNum++;
		mTv_refund_num.setText(""+mNum);
		mTv_refund_money.setText(""+getCalculateRefundMoney(mUnitPrice, mNum));
	}

	
	private void submit(){
		if (mNum < 1 || mNum >mGoodsTotalNum) {
			return;
		}
		if (!TextUtils.isEmpty(mEt.getText().toString())) {
			mEtContent=mEt.getText().toString();
		}
		httpHelper.postRefundApply(mNum+"",mOrder_id,mTuan_id);

	}
	private void initTitle() {
		mTitle.setMiddleTextTop("退款申请");
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPtr.setRefreshing();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		httpHelper.onDestroy();
	}

	@Override
	public void onSuccess(String responseBody) {

	}

	@Override
	public void onSuccess(String method, List datas) {
		if (UserConstants.REFUND_APPLICATION_PAGE.endsWith(method)){
			ResultRefundPage refundPage = (ResultRefundPage) datas.get(0);
			String title=refundPage.getTitle();
			Log.e("test","title:"+title);
			List<ModelRefundPage> body = refundPage.getBody();
			if (body!=null && body.size()>0){
				ModelRefundPage modelRefundPage = body.get(0);
				bindData(modelRefundPage);
			}
		}
		if (UserConstants.REFUND_APPLICATION.endsWith(method)){
			MGToast.showToast("申请退款成功!");
			finish();
		}
	}

	@Override
	public void onFailue(String responseBody) {
		if (UserConstants.REFUND_APPLICATION.endsWith(responseBody)){
			MGToast.showToast("申请失败!");
		}
	}

	@Override
	public void onFinish(String method) {
		mPtr.onRefreshComplete();
	}
}
