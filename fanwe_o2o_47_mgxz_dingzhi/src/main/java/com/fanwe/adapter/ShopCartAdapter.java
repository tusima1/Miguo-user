package com.fanwe.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.app.App;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.CartGoodsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.utils.MGStringFormatter;
import com.fanwe.utils.SDFormatUtil;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.DisplayUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCartAdapter extends SDBaseAdapter<ShoppingCartInfo> {

	/** 屏幕宽度 */
	public int mScreenWidth;

	/** HorizontalScrollView左右滑动事件 */
	public ScrollViewScrollImpl mScrollImpl;

	/** 布局参数,动态让HorizontalScrollView中的LinearLayout宽度包裹父容器 */
	public LinearLayout.LayoutParams mParams;

	/** 记录滑动出删除按钮的itemView */
	public HorizontalScrollView mScrollView;

	/** touch事件锁定,如果已经有滑动出删除按钮的itemView,就屏蔽下一整次(down,move,up)的onTouch操作 */
	public boolean mLockOnTouch = false;

	protected ShopCartSelectedListener mListener;

	public OutSideShoppingCartHelper mShoppingCartHelper;

	public RefreshCalbackView mCallbackview;

	public void setOnShopCartSelectedListener(ShopCartSelectedListener listener) {
		this.mListener = listener;
	}

	private HorizontalScrollView scrollView;

	public ShopCartAdapter(List<ShoppingCartInfo> listModel, Activity activity,RefreshCalbackView callbackView) {
		super(listModel, activity);
		this.mCallbackview = callbackView;
		// 搞到屏幕宽度
		Display defaultDisplay = mActivity.getWindowManager()
				.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		defaultDisplay.getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		mParams = new LinearLayout.LayoutParams(mScreenWidth,
				LinearLayout.LayoutParams.MATCH_PARENT);
		// 初始化删除按钮事件与item滑动事件
		mScrollImpl = new ScrollViewScrollImpl();
	}

	// 获取所有的购物车id
	public ArrayList<Integer> getId() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (mListModel != null) {
			for (ShoppingCartInfo model : mListModel) {
				list.add(Integer.valueOf(model.getId()));
			}
		}
		return list;
	}

	public Map<String, String> getMapNumber() {
		Map<String, String> mapNumber = new HashMap<String, String>();
		if (mListModel != null) {
			for (ShoppingCartInfo model : mListModel) {
				mapNumber.put(String.valueOf(model.getId()), model.getNumber());
			}
		}
		return mapNumber;
	}

	public String getBigDecimal(Float money) {
		BigDecimal bd = new BigDecimal(money);
		if (String.valueOf(money).matches("^\\d+$$")) {
			bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
		} else {
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if (Integer.parseInt(String.valueOf(money).split("\\.")[0]) == 0) {
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return String.valueOf(bd);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.item_shop_cart, null);
		scrollView = (HorizontalScrollView) convertView;
		scrollView.setOnTouchListener(mScrollImpl);

		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		// 无效显示按钮。
		Button ineffectiveBtn = ViewHolder
				.get(convertView, R.id.ineffectiveBtn);
		LinearLayout ll_percent = ViewHolder.get(R.id.ll_percent, convertView);
		TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
		TextView tv_max = ViewHolder.get(convertView, R.id.tv_max);
		final CheckBox cb_check = ViewHolder.get(R.id.cb_check, convertView);
		Button bt_delect = ViewHolder.get(R.id.bt_delect, convertView);
		final TextView tv_sunMoney = ViewHolder.get(convertView,
				R.id.tv_sunMoney);
		TextView tv_add_number = ViewHolder
				.get(convertView, R.id.tv_add_number);
		TextView tv_minus_number = ViewHolder.get(convertView,
				R.id.tv_minus_number);
		final EditText et_number = ViewHolder.get(convertView, R.id.et_number);
		final TextView tv_actualPrice = ViewHolder.get(convertView,
				R.id.tv_actualPrice);
		final TextView tv_originalPrice = ViewHolder.get(convertView,
				R.id.tv_originalPrice);

		final ShoppingCartInfo model = getItem(position);
		if (model != null) {
			ll_percent.setLayoutParams(mParams);
			DisplayMetrics metric = new DisplayMetrics();
			mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
			int width = metric.widthPixels;
			int height = (int) (width * 0.242 - 5);
			SDViewUtil.setViewHeight(iv_image, height);
			tv_title.setText(model.getTitle());
			tv_originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			String url =model.getImg();
			if(!TextUtils.isEmpty(url)){
				url = DisplayUtil.qiniuUrlExchange(url,100,62);
			}
			SDViewBinder.setImageView(url, iv_image);

			if (SDFormatUtil.stringToInteger(model.getLimit_num()) <= -1) {
				SDViewUtil.hide(tv_max);
			} else {
				SDViewUtil.show(tv_max);
				SDViewBinder.setTextView(tv_max,
						"限购" + model.getLimit_num() + "件");
			}
			if(TextUtils.isEmpty(App.getInstance().getToken())){
				SDViewUtil.hide(tv_max);
			}

			et_number.setText(model.getNumber());
			SDViewBinder.setTextView(tv_originalPrice,
					SDFormatUtil.formatMoneyChina(model.getOrigin_price()));
			setPrice(tv_actualPrice, tv_sunMoney, model);
			scrollView.scrollTo(0, 0);
			bt_delect.setOnClickListener(new DeleteOnClickListener(position));


			if (!ineffectiveCheck(model)) {
				ineffectiveBtn.setVisibility(View.VISIBLE);
				cb_check.setVisibility(View.GONE);
				tv_add_number.setVisibility(View.GONE);
				tv_minus_number.setVisibility(View.GONE);
				et_number.setVisibility(View.GONE);
				ll_percent.setBackgroundColor(Color.LTGRAY);
			} else {
				ineffectiveBtn.setVisibility(View.GONE);
				cb_check.setVisibility(View.VISIBLE);
				tv_add_number.setVisibility(View.VISIBLE);
				tv_minus_number.setVisibility(View.VISIBLE);
				et_number.setVisibility(View.VISIBLE);
			}

           cb_check.setChecked(model.isChecked());

			cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {


						model.setChecked(isChecked);
						if (mListener != null) {
							mListener.onSelectedListener();
						}
				}
			});
			tv_add_number.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int curNumber = getNumberFromEditText(et_number);
					if(curNumber>=999){
						MGToast.showToast("该宝贝不能购买更多哦！",Toast.LENGTH_LONG);
						return;
					}
					int maxNumber = SDFormatUtil.stringToInteger(model.getLimit_num());
					// -1表示无数量限制.
					if ((maxNumber <= -1)
							|| ((maxNumber > -1) && curNumber < maxNumber)||TextUtils.isEmpty(App.getInstance().getToken())) {
						curNumber++;
						et_number.setText(String.valueOf(curNumber));
						model.setNumber(curNumber+"");
					}
				}
			});

			tv_minus_number.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int curNumber = getNumberFromEditText(et_number);
					if (curNumber > 1) {
						curNumber--;
						et_number.setText(String.valueOf(curNumber));
						model.setNumber(curNumber+"");
					}
				}
			});

			et_number.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					if (TextUtils.isEmpty(s)) {
						return;
					} else {
						int maxNumber = SDFormatUtil.stringToInteger(model.getLimit_num());
						if (s.toString().length() > 4) {
							int index = s.toString().length();
							s.delete(index - 1, index);
						}
						int inputNum = Integer.valueOf(s.toString());
						// 输入数量必须大于0
						if (inputNum > 0) {
							if ((maxNumber > -1 && inputNum > maxNumber)||TextUtils.isEmpty(App.getInstance().getToken())){
								// 无限制
								inputNum = maxNumber;
							}
							model.setNumber(inputNum+"");
							setPrice(tv_actualPrice, tv_sunMoney, model);
							if (cb_check.isChecked()) {
								mListener.onSelectedListener();
							}
						}
					}
				}

			});
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (cb_check.isChecked()) {
						cb_check.setChecked(false);
					} else {
						cb_check.setChecked(true);
					}
				}
			});
		}
		return convertView;
	}

	public interface ShopCartSelectedListener {
		public void onSelectedListener();

		/**
		 * 删除状态下的被选择状态。
		 * 
		 * @param model
		 * @param isChecked
		 */
		public void onDelSelectedListener(CartGoodsModel model,
				boolean isChecked);

		/**
		 * 购物车标题栏的数量变化
		 * 
		 * @param num
		 *            变化后的数量
		 */
		public void onTitleNumChangeListener(int num);
	}

	private void setPrice(TextView tvSinglePrice, TextView tvTotalPrice,
			ShoppingCartInfo model) {
		if (model != null && tvSinglePrice != null && tvTotalPrice != null) {
			float sumPrice;
			int firstNum = SDFormatUtil.stringToInteger(model.getIs_first());
			int number= SDFormatUtil.stringToInteger(model.getNumber());
			if (firstNum > 0) {
				if (firstNum > number) {
					firstNum = number;
				}
			} else {
				firstNum = 0;
			}
			/**
			 * 总金额-首几单 优惠 金额 - = 总金额。
			 */
			sumPrice = firstNum * SDFormatUtil.stringToFloat(model.getIs_first_price());
			sumPrice = number* SDFormatUtil.stringToFloat(model.getTuan_price())-sumPrice;
			if(sumPrice<0){
				sumPrice = 0;
			}

				model.setSumPrice(sumPrice);
				SDViewBinder.setTextView(tvSinglePrice, model.getTuan_price());
				SDViewBinder.setTextView(tvTotalPrice, MGStringFormatter.getFloat2(sumPrice));
			}
	}

	private int getNumberFromEditText(EditText et) {
		int num = 0;
		if (et != null) {
			num = SDTypeParseUtil.getInt(et.getText().toString(), 0);
		}
		return num;
	}

	class DeleteOnClickListener implements OnClickListener {
		private int nPosition;

		public DeleteOnClickListener(int position) {
			this.nPosition = position;
		}

		@Override
		public void onClick(View v) {
			// TODO 删除商品
			if(mShoppingCartHelper ==null){
				mShoppingCartHelper = new OutSideShoppingCartHelper(mCallbackview);
			}
				 ShoppingCartInfo model = getItem(nPosition);
				if(TextUtils.isEmpty(App.getInstance().getToken())){
					LocalShoppingcartDao.deleteModel(model);
					List<ShoppingCartInfo> datas = new ArrayList<ShoppingCartInfo>();
					datas.add(model);
					mCallbackview.onSuccess(ShoppingCartconstants.SHOPPING_CART_DELETE,datas);
				}else{
					mShoppingCartHelper.doDeleteShopCart(model.getId(),model);
				}




		}
	}

	// 删除一批数据
	public void removeItems(List<ShoppingCartInfo> list) {
		mListModel.removeAll(list);
		notifyDataSetChanged();
	}

	/** HorizontalScrollView的滑动事件 */
	private class ScrollViewScrollImpl implements OnTouchListener {
		/** 记录开始时的坐标 */
		private float startX = 0;

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {


				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 如果有划出删除按钮的itemView,就让他滑回去并且锁定本次touch操作,解锁会在父组件的dispatchTouchEvent中进行
					if (mScrollView != null) {
						scrollView(mScrollView, HorizontalScrollView.FOCUS_LEFT);
						mScrollView = null;
						mLockOnTouch = true;
						return true;
					}
					mLockOnTouch = false;
					startX = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					HorizontalScrollView view = (HorizontalScrollView) v;
					// 当前是编辑状态，并且滑动了>70个像素,就显示出删除按钮
					if ((startX > event.getX() + 70)) {
						startX = 0;// 因为公用一个事件处理对象,防止错乱,还原startX值
						scrollView(view, HorizontalScrollView.FOCUS_RIGHT);
						mScrollView = view;

					} else {
						// scrollView(view, HorizontalScrollView.FOCUS_LEFT);
					}
					break;

				}
				return false;

		}
	}

	/** HorizontalScrollView左右滑动 */
	public void scrollView(final HorizontalScrollView view, final int parameter) {
		view.post(new Runnable() {
			@Override
			public void run() {
				view.pageScroll(parameter);
			}
		});
	}

	/**
	 * 判断当前订单是否有效。 1为可以购买，0为不可
	 * 
	 * @param model
	 * @return
	 */
	public boolean ineffectiveCheck(ShoppingCartInfo model) {
		if (!TextUtils.isEmpty(model.getBuyFlg())&&"1".equals(model.getBuyFlg())) {
			return true;
		}
		return false;
	}

}