package com.fanwe;

import android.os.Bundle;
import android.text.TextUtils;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.AppWebViewFragment;
import com.fanwe.model.Notice_detailActModel;
import com.fanwe.model.Notice_detailResultModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.customviews.MGToast;

/**
 * 公告内容
 * 
 * @author js02
 * 
 */
public class NoticeDetailActivity extends BaseActivity
{

	/** 公告id(int) */
	public static final String EXTRA_NOTICE_ID = "extra_notice_id";

	private AppWebViewFragment mFragWebview;
	private int mNoticeId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setmTitleType(TitleType.TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_notice_detail);
		init();
	}

	private void init()
	{
		getIntentData();
		initTitle();
		requestData();
	}

	private void getIntentData()
	{
		mNoticeId = getIntent().getIntExtra(EXTRA_NOTICE_ID, 0);
		if (mNoticeId <= 0)
		{
			MGToast.showToast("id 为空");
			finish();
		}
	}

	private void requestData()
	{

	}

	private void addWebViewFragment(Notice_detailActModel actModel)
	{
		Notice_detailResultModel model = actModel.getResult();
		if (model == null)
		{
			return;
		}

		String content = model.getContent();
		String pageTitle = actModel.getPage_title();
		if (!TextUtils.isEmpty(content))
		{
			mFragWebview = new AppWebViewFragment();
			mFragWebview.setHtmlContent(content);
			getSDFragmentManager().replace(R.id.act_news_detail_fl_content, mFragWebview);
		}
		if (!TextUtils.isEmpty(pageTitle))
		{
			mTitle.setMiddleTextTop(pageTitle);
		}
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("公告内容");
	}

}