package com.fanwe;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.SDFormatUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 提现详情
 * 
 * @author Administrator
 * 
 */
public class WithdrawDetailActivity extends BaseActivity
{

	/** 银行 (String) */
	public static final String EXTRA_WITHDRAW_BANK = "extra_withdraw_bank";

	/** 提现金额 (String) */
	public static final String EXTRA_WITHDRAW_MONEY = "extra_withdraw_money";

	/** 银行卡id (int) */
	public static final String EXTRA_BANK_ID = "extra_bank_id";

	@ViewInject(R.id.tv_bank)
	private TextView mTv_bank;

	@ViewInject(R.id.tv_money)
	private TextView mTv_money;

	@ViewInject(R.id.tv_finish)
	private TextView mTv_finish;

	@ViewInject(R.id.cb_save_bank)
	private CheckBox mCb_save_bank;

	/** 是否绑定当前银行卡 1:是，0:否 */
	private int is_bind;

	private int mBankId;

	/** 提现金额 */
	private String mStrMoney;
	/** 银行卡卡号 */
	private String mStrBankAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_withdraw_detail);
		init();
	}

	private void init()
	{
		getIntentData();
		initTitle();
		register();
	}

	private void getIntentData()
	{
		mBankId = getIntent().getIntExtra(EXTRA_BANK_ID, 0);
		mStrMoney = getIntent().getStringExtra(EXTRA_WITHDRAW_MONEY);
		mStrBankAccount = getIntent().getStringExtra(EXTRA_WITHDRAW_BANK);

		if (mBankId > 0)
		{
			SDViewUtil.show(mCb_save_bank);
		} else
		{
			SDViewUtil.hide(mCb_save_bank);
		}

		SDViewBinder.setTextView(mTv_bank, mStrBankAccount);
		SDViewBinder.setTextView(mTv_money, SDFormatUtil.formatMoneyChina(mStrMoney));
	}

	private void register()
	{
		mCb_save_bank.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					is_bind = 1;
				} else
				{
					is_bind = 0;
				}
			}
		});

		mTv_finish.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				clickFinish();
			}
		});

	}

	protected void clickFinish()
	{
		if (mBankId > 0)
		{
			requestBindBank();
		} else
		{
			finish();
			SDToast.showToast("提现提交申请成功，等待审核");
		}
	}

	private void requestBindBank()
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_money");
		model.putAct("do_bind_bank");
		model.put("withdraw_id", mBankId);
		model.put("is_bind", is_bind);
		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<BaseActModel>(false)
		{
			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("");
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() > 0)
				{
					finish();
					SDToast.showToast("绑定银行 卡申请成功，等待审核");
				}
				super.onSuccess(responseInfo);
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
				super.onFinish();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				super.onFailure(error, msg);
				SDToast.showToast(msg);
			}

		});
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("提现详情");

	}

}
