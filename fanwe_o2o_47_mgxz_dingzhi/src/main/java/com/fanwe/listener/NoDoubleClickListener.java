package com.fanwe.listener;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;

/**
 * 防止二次点击,代替onclick()方法.
 * @author didik
 *
 */
public abstract class NoDoubleClickListener implements OnClickListener{

	public NoDoubleClickListener(long lastClickTime) {
		this.lastClickTime = lastClickTime;
	}

	private final int MIN_CLICK_DELAY_TIME=800;
	private long lastClickTime=0;
	@Override
	public void onClick(View v) {
		long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        } 
	}
	protected abstract void onNoDoubleClick(View v);

}
