package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.fanwe.adapter.SearchHistoryAdapter;
import com.fanwe.adapter.SearchHistoryAdapter.OnSearchHistoryListener;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.HistoryShowBean;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.Contance;
import com.fanwe.utils.HistoryHelper;
import com.lidroid.xutils.view.annotation.ViewInject;
	/**
	 * 搜索历史记录
	 * @author Administrator
	 *
	 */
public class SearchHistoryFragment extends BaseFragment
{

	@ViewInject(R.id.lv_business)
	private ListView mLv_business;
	
	@ViewInject(R.id.bt_clear)
	private Button mBtn_clear;
	private SearchHistoryAdapter mAdapter;
	private List<String>list = new ArrayList<String>();
	
	private OnSearchHistoryFragmentListener mListener;
	private HistoryHelper helper;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 点击清除记录按钮，刷新界面
				mAdapter.refresh(queryHistoryData());
				break;
				
			default:
				break;
			}
		}
	};
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_business);
	}
	
		@Override
		protected void init() 
		{
			super.init();
			bindData();
			registeClick();
		}
		
		private void registeClick() 
		{
			mBtn_clear.setOnClickListener(this);
		}
	
		@Override
		public void onClick(View v)
		{
			switch (v.getId()) {
			case R.id.bt_clear:
				clickClear();
				break;
	
			default:
				break;
			}
		}
	
		private void clickClear()
		{
			helper = new HistoryHelper(getActivity());
			SQLiteDatabase db = helper.getWritableDatabase();
			// 删除表。
			db.execSQL("delete from " + Contance.HISTORY_TABLENAME);
			new Thread(new Runnable() {
	
				@Override
				public void run() {
					// 此处handler发送一个message，用来更新ui
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			}).start();
			db.close();
		}
		private void bindData()
		{
			
			SDViewUtil.show(mBtn_clear);
			mAdapter = new SearchHistoryAdapter(queryHistoryData(), getActivity());
			mAdapter.setOnSearchHistoryData(new OnSearchHistoryListener() {

				@Override
				public void setOnHistoryData(Bundle bundle) {
					if(mListener != null)
					{
						mListener.setSearchHistory(bundle);
					}
				}
			});
			mLv_business.setAdapter(mAdapter);
		}
		
		private List<HistoryShowBean> queryHistoryData() {
			helper = new HistoryHelper(getActivity());
			List<HistoryShowBean> his_list = new ArrayList<HistoryShowBean>();
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor his_c = db.rawQuery("select * from "
					+ Contance.HISTORY_TABLENAME, null);

			his_c.moveToFirst();
			while (!his_c.isAfterLast()) {
				
				String h_name = his_c.getString(his_c.getColumnIndex("h_name"));

				// 用一个HistoryShowBean类来封装得到的数据
				final HistoryShowBean his_bean = new HistoryShowBean();
				his_bean.setJingdian(h_name);
				his_list.add(his_bean);
				his_c.moveToNext();
			}
			if (his_list.size() == 0) {
				mBtn_clear.setVisibility(View.GONE);
			}
			db.close();
			return his_list;
		}
		public void setOnsearchHistoryListener(OnSearchHistoryFragmentListener listener)
		{
			this.mListener = listener;
		}
		public interface OnSearchHistoryFragmentListener
		{
			void setSearchHistory(Bundle bundle);
		}
		@Override
		protected String setUmengAnalyticsTag() {
			return this.getClass().getName().toString();
		}
}
