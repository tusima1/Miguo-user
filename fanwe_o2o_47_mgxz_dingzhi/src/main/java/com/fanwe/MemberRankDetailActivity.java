package com.fanwe;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.dao.barry.MemberDetailDao;
import com.fanwe.dao.barry.impl.MemberDetailDaoImpl;
import com.fanwe.dao.barry.view.MemberDetailView;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.Uc_DistModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class MemberRankDetailActivity extends BaseActivity
{
	@ViewInject(R.id.tv_rank)
	private TextView mTv_rank;
	
	@ViewInject(R.id.webView_dis)
	private WebView mWebView;
	private int mId;

	MemberDetailDao memberDetailDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_rank_detail);
		init();
	}
	private void init()
	{
		bindData();
		initWeb();
		initTitle();
		requestData();
	}

	private void initWeb()
	{
		WebSettings webSettings = mWebView.getSettings(); 
		webSettings.setSavePassword(false); 
		webSettings.setSaveFormData(true); 
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setSupportZoom(false);
		mWebView.setBackgroundColor(Color.parseColor("#ffffff"));
	}

	private void requestData() 
	{
		memberDetailDao = new MemberDetailDaoImpl(new MemberDetailView() {
			@Override
			public void getMemberDetailSuccess(final String html) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
					}
				});
			}

			@Override
			public void getMemberDetailError(String msg) {

			}
		});
		memberDetailDao.getMerberDetail("1");

//		RequestModel model = new RequestModel();
//		model.putCtl("page");
//		model.putAct("fx_intro");
//		model.put("vip", mId);
//		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<Uc_DistModel>()
//				{
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo)
//					{
//						if (actModel.getStatus() == 1)
//						{
//							bindResult(actModel);
//						}
//					}
//
//					@Override
//					public void onFinish()
//					{
//
//					}
//				});
	}

	protected void bindResult(Uc_DistModel actModel)
	{
		mWebView.loadDataWithBaseURL(null, actModel.getBody(), "text/html", "utf-8", null);
	}

	private void bindData()
	{
		mId = getIntent().getIntExtra("int", -1);
		if(mId == 1){
			SDViewBinder.setTextView(mTv_rank, "普通", "未找到");
		}else if(mId == 2){
			SDViewBinder.setTextView(mTv_rank, "白金", "未找到");
		}else if(mId == 3){
			SDViewBinder.setTextView(mTv_rank, "钻石", "未找到");
		}
	}

	private void initTitle()
	{
		mTitle.setMiddleTextBot("会员俱乐部");
	}
}
