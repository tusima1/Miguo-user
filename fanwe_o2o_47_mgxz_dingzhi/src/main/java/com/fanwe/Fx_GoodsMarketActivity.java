package com.fanwe;


public class Fx_GoodsMarketActivity extends BaseActivity{
/*	private GridView mPullGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_fx_market);
		init();
	}

	private void init() {
		initTitle();
		initView();
	}
	
	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what==0) {
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			}
		}
	};

	private void initView() {
		mPullGridView = (GridView) findViewById(R.id.gridview);
		pullToRefreshLayout = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
		pullToRefreshLayout.setOnRefreshListener(new MyListener() {
			@Override
			public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for (int i = 0; i < 10; i++)
							{
								items.add("新item啊啊啊啊啊  " + i);
							}
						mHandler.sendEmptyMessage(0);
						
					}
				}).start();
//				for (int i = 0; i < 10; i++)
//				{
//					items.add("新item " + i);
//				}
//				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//				adapter.notifyDataSetChanged();
			}
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				for (int i = 0; i < 10; i++)
				{
					items.add(0,"这里是item " + i);
				}
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			}
		});
		items = new ArrayList<String>();
		for (int i = 0; i < 20; i++)
		{
			items.add("这里是item " + i);
		}
		mPullGridView.setAdapter(adapter);
	}
	
	private BaseAdapter adapter=new BaseAdapter() {

		@Override
		public int getCount() {
			return items==null?0:items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView=View.inflate(Fx_GoodsMarketActivity.this,R.layout.grid_item_fx_market,null);
				holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_number=(TextView) convertView.findViewById(R.id.tv_count);
				holder.tv_money=(TextView) convertView.findViewById(R.id.tv_money);
				holder.iv_img=(ImageView) convertView.findViewById(R.id.iv_img);
				holder.commit_fx=(Button) convertView.findViewById(R.id.commit_fx);
				holder.commit_buy=(Button) convertView.findViewById(R.id.commit_buy);
				holder.ll_item=(LinearLayout) convertView.findViewById(R.id.ll_item);
				convertView.setTag(holder);
			}
			holder=(ViewHolder) convertView.getTag();
			
			String string = items.get(position);
			holder.tv_name.setText(string);
			
			holder.commit_fx.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SDToast.showToast("position-fx:"+position);
				}
			});
			holder.commit_buy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SDToast.showToast("position-buy:"+position);
				}
			});
			
			return convertView;
		}
	};
	private List<String> items;
	private PullToRefreshLayout pullToRefreshLayout;
	
	private class ViewHolder{
		public TextView tv_name;
		public TextView tv_money;
		public TextView tv_number;
		public TextView tv_yongjin;
		public ImageView iv_img;
		public Button commit_fx;
		public Button commit_buy;
		public LinearLayout ll_item;
	}

	private void initTitle() {
		
	}*/

}
