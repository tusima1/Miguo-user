package com.fanwe;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ShoppingMallActivity extends BaseActivity
{
	@ViewInject(R.id.ptrlv_grid)
	private PullToRefreshGridView mPtrlv_grid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_shopping_mall);
		init();
	}

	private void init()
	{
		initTitle();
	}

	private void initTitle()
	{
			mTitle.setCustomViewMiddle(R.layout.view_shop_tab);
			mTitle.mTitleMiddle.setGravity(Gravity.CENTER);
			final ClearEditText mEdt_Search = (ClearEditText) mTitle.findViewById(R.id.searchlist);
			DisplayMetrics metric = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(metric);
	        int width = metric.widthPixels;
			int maxMiddleWidth = width - mTitle.mLlLeft.getWidth();
			mEdt_Search.setMinimumWidth(maxMiddleWidth);
			SDViewUtil.showInputMethod(mEdt_Search);
			mEdt_Search.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId ==EditorInfo.IME_ACTION_SEARCH)
					{
						SDViewUtil.hideInputMethod(mEdt_Search,ShoppingMallActivity.this);
						
						return true;
					}
					return false;
				}
			});
		}
		
	
}
