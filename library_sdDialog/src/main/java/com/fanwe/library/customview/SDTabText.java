package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;

public class SDTabText extends SDViewBase
{

	public TextView mTv_title;

	public SDTabText(Context context)
	{
		this(context, null);
	}

	public SDTabText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		setContentView(R.layout.view_tab_text);
		mTv_title = (TextView) findViewById(R.id.tv_title);

		setDefaultConfig();
	}

	private void setDefaultConfig()
	{
		getViewConfig(mTv_title).setmTextColorNormalResId(R.color.gray);
		getViewConfig(mTv_title).setmTextColorSelected(mLibraryConfig.getmMainColor());
	}

	public SDViewBaseConfig getViewConfigTvTitle()
	{
		return getViewConfig(mTv_title);
	}

	public void setTextSizeTitleSp(int textSizeSp)
	{
		SDViewUtil.setTextSizeSp(mTv_title, textSizeSp);
	}

	public void setTextTitle(CharSequence content)
	{
		SDViewBinder.setTextView(mTv_title, content);
	}

	@Override
	public void onNormal()
	{
		mTv_title.setTextColor(getViewConfig(mTv_title).getmTextColorNormal());
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		mTv_title.setTextColor(getViewConfig(mTv_title).getmTextColorSelected());
		super.onSelected();
	}

}
