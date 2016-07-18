package com.fanwe.library.alipay;

import android.view.View;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDResourcesUtil;

public class SDAlipayerHelper
{

	public boolean isAlipayExist()
	{
		boolean isAlipayExist = SDPackageUtil.isPackageExist("com.alipay.android.app");
		if (!isAlipayExist)
		{
			String cacheFile = SDLibrary.getInstance().getApplication().getCacheDir().getAbsolutePath() + "/temp_alipay.apk";
			if (SDResourcesUtil.copyAssetsFileTo(PartnerConfig.ALIPAY_PLUGIN_NAME, cacheFile))
			{
				showInstallDialog(cacheFile);
			}
		}
		return isAlipayExist;
	}

	private void showInstallDialog(final String cacheFile)
	{
		String content = "为保证您的交易安全，需要您安装支付宝安全支付服务，才能进行付款。\n\n点击确定，立即安装。";

		new SDDialogConfirm().setTextContent(content).setmListener(new SDDialogCustomListener()
		{

			@Override
			public void onDismiss(SDDialogCustom dialog)
			{
			}

			@Override
			public void onClickConfirm(View v, SDDialogCustom dialog)
			{
				SDPackageUtil.chmod("777", cacheFile);
				SDPackageUtil.installApkPackage(cacheFile);
			}

			@Override
			public void onClickCancel(View v, SDDialogCustom dialog)
			{
			}
		}).show();
	}
}
