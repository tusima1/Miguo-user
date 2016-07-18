package com.fanwe;

import android.os.Bundle;

import com.fanwe.constant.Constant.SearchTypeMap;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.MapSearchEventFragment;
import com.fanwe.fragment.MapSearchFragment;
import com.fanwe.fragment.MapSearchStoreFragment;
import com.fanwe.fragment.MapSearchTuanFragment;
import com.fanwe.fragment.MapSearchYouhuiFragment;
import com.fanwe.o2o.miguo.R;

/**
 * 地图附近
 * 
 * @author js02
 * 
 */
public class MapSearchActivity extends BaseActivity
{
	/**
	 * 传进来的值请引用SearchTypeMap类的常量
	 */
	public static final String EXTRA_SEARCH_TYPE = "extra_search_type";
	
	public static final String EXTRA_CT_ID ="ct_id";

	private int mSearchType;

	private MapSearchFragment mFragMapSearch;

	private int ct_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setmTitleType(TitleType.TITLE_NONE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_container);
		init();
	}

	private void init()
	{
		getIntentData();
		addFragment();
	}

	private void addFragment()
	{
		switch (mSearchType)
		{
		case SearchTypeMap.EVENT:
			mFragMapSearch = new MapSearchEventFragment();
			break;
		case SearchTypeMap.STORE:
			mFragMapSearch = new MapSearchStoreFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("id", ct_ID);
			mFragMapSearch.setArguments(bundle);
			break;
		case SearchTypeMap.TUAN:
			mFragMapSearch = new MapSearchTuanFragment();
			break;
		case SearchTypeMap.YOU_HUI:
			mFragMapSearch = new MapSearchYouhuiFragment();
			break;

		default:
			break;
		}
		if (mFragMapSearch != null)
		{
			mFragMapSearch.setmSearchType(mSearchType);
			getSDFragmentManager().replace(R.id.view_container_fl_content, mFragMapSearch);
		}
	}

	private void getIntentData()
	{
		mSearchType = getIntent().getIntExtra(EXTRA_SEARCH_TYPE, SearchTypeMap.TUAN);
		ct_ID = getIntent().getIntExtra(EXTRA_CT_ID, -1);
	}
}