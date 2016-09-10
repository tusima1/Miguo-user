package com.fanwe.library.alipay.easy;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.fanwe.library.utils.SDHandlerUtil;
import com.fanwe.library.utils.SDViewUtil;

public class SDAlipayer
{
	private Activity mActivity;

	public SDAlipayer(Activity activity)
	{
		this.mActivity = activity;
	}

	// ---------------gettter setter
	private SDAlipayerListener mListener;

	public SDAlipayerListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SDAlipayerListener mListener)
	{
		this.mListener = mListener;
	}

	public void pay(String orderSpec, String sign)
	{
		pay(orderSpec, sign, "RSA");
	}

	public void pay(String orderSpec, String sign, String signType)
	{
		if (mListener != null)
		{
			mListener.onStart();
		}
		if (TextUtils.isEmpty(orderSpec))
		{
			notifyFailure(null, "order_spec为空");
			return;
		}
		if (TextUtils.isEmpty(sign))
		{
			notifyFailure(null, "sign为空");
			return;
		}
		if (TextUtils.isEmpty(signType))
		{
			notifyFailure(null, "signType为空");
			return;
		}
		String info = orderSpec + "&sign=" + "\"" + sign + "\"" + "&" + "sign_type=" + "\"" + signType + "\"";

		pay(info);
	}

	public void pay(final String payInfo)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				PayTask alipay = new PayTask(mActivity);
				String result = alipay.pay(payInfo);
				notifyFinish(new PayResult(result));
			}
		}).start();
	}

	// ------------------------notify

	private void notifyFailure(final Exception e, final String msg)
	{
		if (mListener != null)
		{
			if (SDViewUtil.isUIThread())
			{
				mListener.onFailure(e, msg);
				notifyFinish(null);
			} else
			{
				SDHandlerUtil.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						mListener.onFailure(e, msg);
						notifyFinish(null);
					}
				});
			}
		}
	}

	private void notifyFinish(final PayResult result)
	{
		if (mListener != null)
		{
			if (SDViewUtil.isUIThread())
			{
				mListener.onFinish(result);
			} else
			{
				SDHandlerUtil.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						mListener.onFinish(result);
					}
				});
			}
		}
	}

	public interface SDAlipayerListener
	{
		public void onStart();

		public void onFailure(Exception e, String msg);

		public void onFinish(PayResult result);
	}

}
