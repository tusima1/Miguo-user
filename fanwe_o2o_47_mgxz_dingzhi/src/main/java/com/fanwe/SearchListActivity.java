package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.BusinessCircleFragment;
import com.fanwe.fragment.BusinessCircleFragment.OnSearchListListener;
import com.fanwe.fragment.SearchHistoryFragment;
import com.fanwe.fragment.SearchHistoryFragment.OnSearchHistoryFragmentListener;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.Contance;
import com.fanwe.utils.HistoryHelper;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * 搜索列表
 * @author cxk
 *
 */
public class SearchListActivity extends BaseActivity
{
	
	public static int result_CODE = 656;
	
	public static int mId;

	public static int CODE = 854;
	
	@ViewInject(R.id.tv_holtel)
	private TextView mTv_holtel;
	
	@ViewInject(R.id.tv_history)
	private TextView mTv_history;

	private BusinessCircleFragment mFragHoltel;

	private SearchHistoryFragment mFragHistory;

	private HistoryHelper helper;

	private String cityName;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_search_lv);
		init();
	}

	private void init()
	{
		initGetIntent();
		initTitle();
		ddFragments();
		registeClick();
	}


	private void initGetIntent()
	{
		Bundle bundle = getIntent().getExtras();
		mId = bundle.getInt("id", 0);
		cityName = bundle.getString("city");
	}

	private void ddFragments()
	{
		mFragHoltel = new BusinessCircleFragment();
		mFragHoltel.setOnSearchData(new OnSearchListListener() {
			
			@Override
			public void setSearchData(Bundle bundle) {
				Intent intent = new Intent();
				if(mId == 22)
				{
					intent.putExtras(bundle);
					SearchListActivity.this.setResult(result_CODE, intent);
					finish();
				}else if(mId == 33)
				{
					intent.putExtras(bundle);
					SearchListActivity.this.setResult(CODE, intent);
					finish();
				}
			}
		});
		Bundle bundle = new Bundle();
		bundle.putString("city", cityName);
		bundle.putInt("id", mId);
		mFragHoltel.setArguments(bundle);
		mTv_holtel.setTextColor(getResources().getColor(R.color.main_color));
		getSDFragmentManager().toggle(R.id.frag_holtel_lv,null, mFragHoltel);
		mFragHistory = new SearchHistoryFragment();
		mFragHistory.setOnsearchHistoryListener(new OnSearchHistoryFragmentListener() {
			
			@Override
			public void setSearchHistory(Bundle bundle) {
				Intent intent = new Intent();
				if(mId == 22)
				{
					intent.putExtras(bundle);
					SearchListActivity.this.setResult(result_CODE, intent);
					finish();
				}else if(mId == 33)
				{
					intent.putExtras(bundle);
					SearchListActivity.this.setResult(CODE, intent);
					finish();
				}
			}
		});
	}
		
	private void registeClick() 
	{
		mTv_holtel.setOnClickListener(this);
		mTv_history.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.tv_holtel:
			mTv_holtel.setTextColor(getResources().getColor(R.color.main_color));
			mTv_history.setTextColor(Color.parseColor("#898989"));
			mTv_holtel.setEnabled(false);
			mTv_history.setEnabled(true);
			Bundle bundle = new Bundle();
			bundle.putString("city", cityName);
			bundle.putInt("id", mId);
			mFragHoltel.setArguments(bundle);
			getSDFragmentManager().toggle(R.id.frag_holtel_lv, null,mFragHoltel);
			break;
		case R.id.tv_history:
			mTv_history.setTextColor(getResources().getColor(R.color.main_color));
			mTv_holtel.setTextColor(Color.parseColor("#898989"));
			mTv_holtel.setEnabled(true);
			mTv_history.setEnabled(false);
			getSDFragmentManager().toggle(R.id.frag_holtel_search_history,null, mFragHistory);
			break;
		default:
			break;
		}
	}
	private void initTitle() 
	{
		mTitle.setCustomViewMiddle(R.layout.view_search_lv);
		
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
					SDViewUtil.hideInputMethod(mEdt_Search,SearchListActivity.this);
					insertHistory(String.valueOf(mEdt_Search.getText()));
					//跳转activity  
					   Intent intent = new Intent();
					   Bundle bundle = new Bundle();
					   bundle.putString("search", String.valueOf(mEdt_Search.getText()));
					   intent.putExtras(bundle);
					   if(mId == 22)
						{
							intent.putExtras(bundle);
							SearchListActivity.this.setResult(result_CODE, intent);
							
						}else if(mId == 33)
						{
							intent.putExtras(bundle);
							SearchListActivity.this.setResult(CODE, intent);
						}
					   finish();
					return true;
				}
				return false;
			}
		});
	}
	private void insertHistory(String search)
	{
		helper = new HistoryHelper(getApplicationContext());
		// 插入数据库
		SQLiteDatabase db = helper.getWritableDatabase();
		
		int count = 0;
		// 查询数据库，判断edittext的内容是否已经存在，如果存在了，就不写了，如果不存在，就插入数据库
		// 取回查询存放history表的h_name列的list集合
		List<String> list = queryHistorySql();
		for (int i = 0; i < list.size(); i++) {
			// 获取搜索框的输入内容，和数据已经存在的记录比对，如果有一样的，就count增加；
			if (list.get(i).equals(search)) {
				count++;
			}
		}
		// 如果count == 0，说明没有重复的数据，就可以插入数据库history表中
		if (count == 0) {
			db.execSQL("insert into " + Contance.HISTORY_TABLENAME
					+ " values(?,?)", new Object[] { null, search });
		} else {
			
		}
		db.close();
	}
	private List<String> queryHistorySql()
	{
		helper = new HistoryHelper(getApplicationContext());
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ Contance.HISTORY_TABLENAME, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			//查询数据库，取出h_name这一列，然后全部放到list集合中，在前面调用此方法的时候，用来判断
			String name = cursor.getString(cursor.getColumnIndex("h_name"));
			list.add(name);
			cursor.moveToNext();
		}
		db.close();
		// 返回一个list集合
		return list;
	}
}
