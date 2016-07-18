package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 底部菜单栏
 * 
 * @author js02
 * 
 */
public class SDTabBottom extends SDViewBase<SDTabBottom.Config>
{

	public ImageView mIvTitle;
	public TextView mTvTitle;
	public TextView mTvNumbr;

	public SDTabBottom(Context context)
	{
		super(context);
		init();
	}

	public SDTabBottom(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		setContentView(R.layout.view_tab_bottom);
		mIvTitle = find(R.id.iv_title);
		mTvTitle = find(R.id.tv_title);
		mTvNumbr = find(R.id.tv_number);

		SDViewUtil.hide(mTvNumbr);
		setBackgroundTextTitleNumber(mConfig.getmBackgroundTextNumberResId());
		onNormal();
	}

	@Override
	public void updateViewState()
	{
		setBackgroundTextTitleNumber(mConfig.getmBackgroundTextNumberResId());
		super.updateViewState();
	}

	public void setTextTitleNumber(String content)
	{
		SDViewBinder.setTextViewsVisibility(mTvNumbr, content);
	}

	public void setTextTitle(String content)
	{
		SDViewBinder.setTextViewsVisibility(mTvTitle, content);
	}

	public void setImageTitle(int resId)
	{
		SDViewBinder.setImageViewsVisibility(mIvTitle, resId);
	}

	public void setBackgroundTextTitleNumber(int resId)
	{
		mTvNumbr.setBackgroundResource(resId);
	}

	public void setTextSizeTitleSp(int sizeSp)
	{
		setTextSizeSp(mTvTitle, sizeSp);
	}

	public void setTextSizeNumberSp(int sizeSp)
	{
		setTextSizeSp(mTvNumbr, sizeSp);
	}

	// ----------------------states

	@Override
	public void onNormal()
	{
		mIvTitle.setImageResource(mConfig.getmImageNormalResId());
		mTvTitle.setTextColor(mConfig.getmTextColorNormal());
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		mIvTitle.setImageResource(mConfig.getmImageSelectedResId());
		mTvTitle.setTextColor(mConfig.getmTextColorSelected());
		super.onSelected();
	}

	public static class Config extends SDViewConfig
	{
		private int mTextColorNormal;
		private int mTextColorSelected;

		private int mTextColorNormalResId;
		private int mTextColorSelectedResId;

		private int mImageNormalResId;
		private int mImageSelectedResId;

		private int mBackgroundTextNumberResId;

		@Override
		public void setDefaultConfig()
		{
			mTextColorSelected = mLibraryConfig.getmMainColor();
		}

		public int getmTextColorNormal()
		{
			return mTextColorNormal;
		}

		public void setmTextColorNormal(int mTextColorNormal)
		{
			this.mTextColorNormal = mTextColorNormal;
		}

		public int getmTextColorSelected()
		{
			return mTextColorSelected;
		}

		public void setmTextColorSelected(int mTextColorSelected)
		{
			this.mTextColorSelected = mTextColorSelected;
		}

		public int getmTextColorNormalResId()
		{
			return mTextColorNormalResId;
		}

		public void setmTextColorNormalResId(int mTextColorNormalResId)
		{
			this.mTextColorNormalResId = mTextColorNormalResId;
			this.mTextColorNormal = SDResourcesUtil.getColor(mTextColorNormalResId);
		}

		public int getmTextColorSelectedResId()
		{
			return mTextColorSelectedResId;
		}

		public void setmTextColorSelectedResId(int mTextColorSelectedResId)
		{
			this.mTextColorSelectedResId = mTextColorSelectedResId;
			this.mTextColorSelected = SDResourcesUtil.getColor(mTextColorSelectedResId);
		}

		public int getmImageNormalResId()
		{
			return mImageNormalResId;
		}

		public void setmImageNormalResId(int mImageNormalResId)
		{
			this.mImageNormalResId = mImageNormalResId;
		}

		public int getmImageSelectedResId()
		{
			return mImageSelectedResId;
		}

		public void setmImageSelectedResId(int mImageSelectedResId)
		{
			this.mImageSelectedResId = mImageSelectedResId;
		}

		public int getmBackgroundTextNumberResId()
		{
			return mBackgroundTextNumberResId;
		}

		public void setmBackgroundTextNumberResId(int mBackgroundTextNumberResId)
		{
			this.mBackgroundTextNumberResId = mBackgroundTextNumberResId;
		}

		@Override
		public Config clone()
		{
			return (Config) super.clone();
		}

	}

}
