package com.fanwe.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fanwe.adapter.ShopCartAdapter;

public class HorizontalSlideDeleteListView extends ListView 
{
	
	private ShopCartAdapter mAdapter;

	public HorizontalSlideDeleteListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(mAdapter.mLockOnTouch){
			if (ev.getAction() == MotionEvent.ACTION_DOWN
					|| ev.getAction() == MotionEvent.ACTION_MOVE) {
				
				if (mAdapter.mScrollView != null) {
					mAdapter.scrollView(mAdapter.mScrollView,
							HorizontalScrollView.FOCUS_LEFT);
					mAdapter.mScrollView = null;
				}
				return true;
			}
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				mAdapter.mLockOnTouch = false;
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (ShopCartAdapter) adapter;
	}
}
