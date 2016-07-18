package com.fanwe.dialog;

import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.MyDistributionUser_dataModel;
import com.fanwe.model.Uc_fx_my_fxActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

public class MyDistributionStoreQrcodeDialog extends SDDialogCustom
{

	private ImageView mIv_qrcode;

	private Uc_fx_my_fxActModel mActModel;

	public MyDistributionStoreQrcodeDialog(Activity activity)
	{
		super(activity);
		initDialog();
	}

	private void initDialog()
	{
		View view = mActivity.getLayoutInflater().inflate(R.layout.dialog_my_distribution_store_qrcode, null);
		setCustomView(view);

		mIv_qrcode = (ImageView) view.findViewById(R.id.iv_qrcode);

		setTextTitle("点击二维码分享给好友");
		setTextConfirm("查看小店");
	}

	public void setmActModel(Uc_fx_my_fxActModel actModel)
	{
		this.mActModel = actModel;
		if (mActModel == null)
		{
			return;
		}

		final MyDistributionUser_dataModel userData = mActModel.getUser_data();
		if (userData == null)
		{
			return;
		}
		SDViewBinder.setImageView(mIv_qrcode, userData.getShare_mall_qrcode());
		mIv_qrcode.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dismiss();
				String title = userData.getUserStoreName();
				String content = title + userData.getShare_mall_url();
				String imageUrl = userData.getShare_mall_qrcode();
				String clickUrl = userData.getShare_mall_url();
				UmengShareManager.share(getOwnerActivity(), title, content, clickUrl, UmengShareManager.getUMImage(getOwnerActivity(), imageUrl), null);
			}
		});
	}

}
