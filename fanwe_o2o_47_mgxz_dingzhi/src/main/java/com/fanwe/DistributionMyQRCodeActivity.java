package com.fanwe;

import java.util.Arrays;

import com.fanwe.app.AppConfig;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class DistributionMyQRCodeActivity extends BaseActivity implements OnLongClickListener
{
	
	@ViewInject(R.id.iv_image_qr)
	private ImageView iv_image_qr;
	private String imgUrl;
	private String imgCard;
	@ViewInject(R.id.share_btn)
	private Button share_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_qrcode);
		init();
	}
	
	private void init()
	{
		initTitle();
		initGetIntent();
		initClick();
	}

	
	private void initClick()
	{
		iv_image_qr.setOnLongClickListener(this);
		share_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
	}
	private void clickImage()
	{
		startActivity(new Intent(this,DistributionStoreWapActivity.class));
	}

	private void initGetIntent()
	{
		Bundle bundle =getIntent().getExtras();
		imgCard = bundle.getString("card");
		SDViewBinder.setImageView(iv_image_qr,imgCard);
		imgUrl = bundle.getString("photo");
		
	}
	private void initTitle() {
		mTitle.setMiddleTextTop("我的二维码");
		mTitle.initRightItem(1);
		mTitle.getItemRight(0).setImageRight(R.drawable.bg_share_img);
	}
	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		super.onCLickRight_SDTitleSimple(v, index);
		showDialog();
	}
	
	private void showDialog() 
	{
		SDDialogMenu dialog = new SDDialogMenu(this);
		String[] arrItem = new String[]{ "分享", "保存到相册" };
		SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(Arrays.asList(arrItem), this);
		dialog.setAdapter(adapter);
		dialog.setmListener(new SDDialogMenuListener()
		{
			@Override
			public void onItemClick(View v, int index, SDDialogMenu dialog)
			{
				switch (index)
				{
				case 0:
					showShare();
					break;
				case 1:
					clickSave();
					break;

				default:
					break;
				}
			}

			@Override
			public void onDismiss(SDDialogMenu dialog)
			{
				
			}

			@Override
			public void onCancelClick(View v, SDDialogMenu dialog)
			{
				
			}
		});
		dialog.showBottom();
	}

	protected void clickSave()
	{
		
	}
	protected void showShare()
	{
		String title = AppConfig.getUserName() + "的小店";
		String content = AppConfig.getUserName();
		String imageUrl = imgUrl;
		String clickUrl = imgCard;
//		UmengSocialManager.openShare(title, content, imageUrl, clickUrl,this, null);
		UmengShareManager.share(this, title, content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
	}

	@Override
	public boolean onLongClick(View v) {
		if(v== iv_image_qr)
		{
			clickImage();
			return true;
		}
		return false;
	}
}
