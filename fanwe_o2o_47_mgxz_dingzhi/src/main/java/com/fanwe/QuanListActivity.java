package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.fanwe.adapter.QuanListAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.dao.InitActModelDao;
import com.fanwe.model.Init_indexActModel;
import com.fanwe.model.QuansModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;

public class QuanListActivity extends BaseActivity
{

	/** select_result */
	public static String EXTRA_SELECT_RESULT = "EXTRA_SELECT_RESULT";
	/** quan_id */
	public static String EXTRA_QUAN_ID = "EXTRA_QUAN_ID";
	/** is_merchant */
	public static String EXTRA_IS_MERCHANT = "EXTRA_IS_MERCHANT";
	/** key */
	public static String EXTRA_KEY = "EXTRA_KEY";

	@ViewInject(R.id.act_cate_lv)
	private PullToRefreshListView mLv = null;

	private QuanListAdapter mAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_cate);
		init();

	}

	private void init()
	{
		initTitle();
		bindDefaultData();
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("商圈列表");
	}

	private void bindDefaultData()
	{
		Init_indexActModel model = InitActModelDao.queryModel();
		QuansModel q = new QuansModel();
		List<QuansModel> quanModel = new ArrayList<QuansModel>();
		q.setId("0");
		q.setName("全部商圈");
		quanModel.add(q);
		quanModel.addAll(model.getQuanlist());
		mAdapter = new QuanListAdapter(quanModel, this);
		mLv.setAdapter(mAdapter);
		mAdapter.updateData(quanModel);
	}

}