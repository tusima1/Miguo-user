package com.fanwe.library.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

public abstract class SDBaseFragmentStatePagerAdapter<T> extends FragmentStatePagerAdapter
{

	protected List<T> mListModel = new ArrayList<T>();
	protected LayoutInflater mInflater;
	protected Activity mActivity;
	protected FragmentManager mFragmentManager;
	protected View mView;

	public SDBaseFragmentStatePagerAdapter(List<T> listModel, Activity activity, FragmentManager fm)
	{
		super(fm);
		setData(listModel);
		this.mActivity = activity;
		this.mFragmentManager = fm;
		this.mInflater = mActivity.getLayoutInflater();
	}

	public void setmView(View mView)
	{
		this.mView = mView;
	}

	public void updateData(List<T> listModel)
	{
		setData(listModel);
		this.notifyDataSetChanged();
	}

	public List<T> getData()
	{
		return mListModel;
	}

	public void setData(List<T> listModel)
	{
		if (listModel != null)
		{
			this.mListModel = listModel;
		} else
		{
			this.mListModel = new ArrayList<T>();
		}
	}

	public T getItemModel(int position)
	{
		if (mListModel != null && position >= 0 && mListModel.size() > position)
		{
			return mListModel.get(position);
		} else
		{
			return null;
		}
	}

	@Override
	public int getItemPosition(Object object)
	{
		return POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position)
	{
		return getItemFragment(position, getItemModel(position));
	}

	public abstract Fragment getItemFragment(int position, T model);

	@Override
	public int getCount()
	{
		if (mListModel != null)
		{
			return mListModel.size();
		} else
		{
			return 0;
		}
	}

}
