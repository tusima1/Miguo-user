package com.fanwe.library.alipay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;
import com.fanwe.library.utils.SDHandlerUtil;
import com.fanwe.library.utils.SDViewUtil;

public class SDAlipayer
{
	private Activity mActivity;
	private boolean mIsPaying = false;
	private Object mLocker = new Object();
	private IAlixPay mAlixPay;
	private SDAlipayerHelper mHelper = new SDAlipayerHelper();

	private ServiceConnection mAlixPayConnection = new ServiceConnection()
	{
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			synchronized (mLocker)
			{
				mAlixPay = IAlixPay.Stub.asInterface(service);
				mLocker.notify();
			}
		}

		public void onServiceDisconnected(ComponentName className)
		{
			mAlixPay = null;
		}
	};

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
		if (!mHelper.isAlipayExist())
		{
			notifyFailure(null, null);
			return;
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

	private void pay(final String strOrderInfo)
	{
		if (mIsPaying)
		{
			return;
		}
		mIsPaying = true;
		bindService();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				String result = null;
				try
				{
					synchronized (mLocker)
					{
						if (mAlixPay == null)
						{
							mLocker.wait();
						}
					}
					mAlixPay.registerCallback(mCallback);
					result = mAlixPay.Pay(strOrderInfo);
					mAlixPay.unregisterCallback(mCallback);
					mActivity.unbindService(mAlixPayConnection);
				} catch (final Exception e)
				{
					notifyFailure(e, e.getMessage());
				} finally
				{
					mIsPaying = false;
					if (TextUtils.isEmpty(result))
					{
						notifyFinish(null);
					} else
					{
						notifyFinish(new ResultChecker(result));
					}
				}
			}
		}).start();

	}

	private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub()
	{
		public void startActivity(String packageName, String className, int iCallingPid, Bundle bundle) throws RemoteException
		{
			Intent intent = new Intent(Intent.ACTION_MAIN, null);

			if (bundle == null)
			{
				bundle = new Bundle();
			}
			try
			{
				bundle.putInt("CallingPid", iCallingPid);
				intent.putExtras(bundle);
			} catch (Exception e)
			{
				notifyFailure(e, e.getMessage());
			}
			intent.setClassName(packageName, className);
			mActivity.startActivity(intent);
		}

		@Override
		public boolean isHideLoadingScreen() throws RemoteException
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void payEnd(boolean arg0, String arg1) throws RemoteException
		{
			// TODO Auto-generated method stub

		}
	};

	private void bindService()
	{
		if (mAlixPay == null)
		{
			mActivity.bindService(new Intent(IAlixPay.class.getName()), mAlixPayConnection, Context.BIND_AUTO_CREATE);
		}
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

	private void notifyFinish(final ResultChecker result)
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

		public void onFinish(ResultChecker result);
	}

}
