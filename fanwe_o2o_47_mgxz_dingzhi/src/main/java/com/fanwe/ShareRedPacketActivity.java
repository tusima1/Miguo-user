package com.fanwe;

import org.json.JSONException;
import org.json.JSONObject;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.RedPacket_WithdrawModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShareRedPacketActivity extends BaseActivity{
	
	private EditText mEt_number;
	private EditText mEt_money;
	private TextView mTv_money;
	private TextView mTv_commitMoney;
	private Button mBt_commit;
	
	private float money=0f;
	private int commitMoney=0;
	private AlertDialog builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_share_redpacket);
		init();
	}

	private void init() {
		initTitle();
		initView();
		getData();
	}

	private void initView() {
		mEt_number = (EditText) findViewById(R.id.et_number);
		mEt_money = (EditText) findViewById(R.id.et_money);
		mTv_money = (TextView) findViewById(R.id.tv_money);
		mTv_commitMoney = (TextView) findViewById(R.id.tv_commit_money);
		mBt_commit = (Button) findViewById(R.id.bt_commit);
		
		mBt_commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager inputMethodManager = (InputMethodManager) ShareRedPacketActivity.this.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
				btCommit();
			}
		});
		
		//红包金额,可以不填
		mEt_money.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String numTemp = mEt_number.getText().toString();
				String hongbao=s.toString();
				if ("".equals(hongbao) || hongbao.startsWith("0") || "".equals(numTemp) || numTemp.startsWith("0")) {
					mTv_commitMoney.setText("0.00");
					return;
				}
				mTv_commitMoney.setText(TextMoney.textFarmat3(Integer.valueOf(hongbao).intValue()));
			}
		});
		
		//红包个数
		mEt_number.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String tempMoney = mEt_money.getText().toString();
				String hongbao=s.toString();
				
				if ("".equals(tempMoney)||tempMoney.startsWith("0")||"".equals(hongbao) || "0".equals(hongbao)) {
					mTv_commitMoney.setText("0.00");
					return;
				}
				
				mTv_commitMoney.setText(TextMoney.textFarmat3(Integer.valueOf(tempMoney).intValue()));
			}
		});
		
	}
	

	private void btCommit() {
		String number = mEt_number.getEditableText().toString();
		String money = mEt_money.getText().toString();
		
		
		if (number.startsWith("0")||money.startsWith("0")) {
			SDToast.showToast("不能以 0 开头,请输入一个正确的数");
			mTv_commitMoney.setText("0.00");
			return;
		}
		if ("".equals(number)|| "".equals(money)) {
			SDToast.showToast("金额或红包个数不能为空");
			mTv_commitMoney.setText("0.00");
			return;
		}
		
		
		int num = Integer.valueOf(number).intValue();
		commitMoney=Integer.valueOf(money).intValue();
		
		if (this.money <commitMoney) {
			SDToast.showToast("不能超过总金额!");
			mTv_commitMoney.setText("0.00");
			return;
		}
		if (commitMoney<num) {
			SDToast.showToast("金额不得小于红包个数");
			mTv_commitMoney.setText("0.00");
			return;
		}
		mTv_commitMoney.setText(TextMoney.textFarmat3(commitMoney));
		showConfirmDialog(commitMoney, num);
	}
	
	private void showConfirmDialog(final int money,final int num){
		View view = SDViewUtil.inflate(R.layout.dialog_normal, null);
		builder = new AlertDialog.Builder(this).create();
		builder.setCanceledOnTouchOutside(false);
		builder.setView(view);
		builder.show();
		Window window = builder.getWindow();
		TextView tv_content = (TextView)window.findViewById(R.id.tv_content);
		Button bt_confirm = (Button)window.findViewById(R.id.bt_confirm);
		Button bt_cancle = (Button)window.findViewById(R.id.bt_cancle);
		tv_content.setText(num+" 个红包,总计 "+money+" 元");
		bt_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				commitData(money,num);
				builder.dismiss();
			}
		});
		
		bt_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
//		
	}
	
	private void commitData(int money,int num) {
//		requestData={"ctl":"uc_red_packet","act":"do_share_red_packet","money":50,"num":5,
		RequestModel model = new RequestModel();
		model.putCtl("uc_red_packet");
		model.putAct("do_share_red_packet");
		model.put("money", money);
		model.put("num", num);
		model.put("type", 1);
		
		SDRequestCallBack<RedPacket_WithdrawModel> handler=new SDRequestCallBack<RedPacket_WithdrawModel>() {
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus()==1) {
//					SDToast.showToast("红包发送成功!");
					getData();
//					UmengSocialManager.openShare(
//							actModel.getShare_title(),
//							actModel.getShare_info(),
//							actModel.getShare_ico(),
//							actModel.getShare_url(),
//							ShareRedPacketActivity.this, null);
					UmengShareManager.share(ShareRedPacketActivity.this,
							actModel.getShare_title(),
							actModel.getShare_info(),
							actModel.getShare_url(),
							UmengShareManager.getUMImage(ShareRedPacketActivity.this, actModel.getShare_ico()),
							null);
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
		};
		
		InterfaceServer.getInstance().requestInterface(model, handler);
	}
	
	

	private void getData() {
//		requestData={"ctl":"uc_red_packet","act":"share_red_packet"}
		RequestModel model = new RequestModel();
		model.putCtl("uc_red_packet");
		model.putAct("share_red_packet");
		model.put("type", 1);
		InterfaceServer.getInstance().requestInterface(model, new RequestCallBack<String>()
		{
			@Override
			public void onStart()
			{
				super.onStart();
			}
			

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				try {
					JSONObject obj=new JSONObject(responseInfo.result);
					money = (float) obj.getDouble("money_people2");
					mTv_money.setText(TextMoney.textFarmat3(money));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

			@Override
			public void onFinish()
			{
				super.onFinish();
			}
		});
	}

	private void initTitle() {
		mTitle.setMiddleTextBot("营销红包");
	}
	
	
}
