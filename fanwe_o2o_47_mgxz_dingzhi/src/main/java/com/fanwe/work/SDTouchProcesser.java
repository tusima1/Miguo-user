package com.fanwe.work;

import android.view.MotionEvent;

public class SDTouchProcesser
{

	private float mStartX = 0;
	private float mStartY = 0;
	private float mLastX = 0;
	private float mLastY = 0;

	public float getmStartX()
	{
		return mStartX;
	}

	public float getmStartY()
	{
		return mStartY;
	}

	public void actionDown(MotionEvent e)
	{
		mStartX = e.getX();
		mStartY = e.getY();

		mLastX = mStartX;
		mLastY = mStartY;
	}

	public float[] actionMove(MotionEvent e)
	{
		float dx;
		float dy;

		float newX = e.getX();
		float newY = e.getY();

		dx = newX - mLastX;
		dy = newY - mLastY;

		mLastX = newX;
		mLastY = newY;
		return new float[] { dx, dy };
	}

}
