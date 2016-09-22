package com.fanwe.jshandler;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.EventDetailActivity;
import com.fanwe.EventListActivity;
import com.fanwe.GoodsListActivity;
import com.fanwe.MainActivity;
import com.fanwe.NearbyVipActivity;
import com.fanwe.NoticeDetailActivity;
import com.fanwe.NoticeListActivity;
import com.fanwe.ScoresListActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.StoreDetailActivity;
import com.fanwe.StoreListActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.TuanListActivity;
import com.fanwe.YouHuiDetailActivity;
import com.fanwe.YouHuiListActivity;
import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.constant.Constant.IndexType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.EventListFragment;
import com.fanwe.fragment.GoodsListFragment;
import com.fanwe.fragment.ScoresListFragment;
import com.fanwe.fragment.StoreListFragment;
import com.fanwe.fragment.TuanListFragment;
import com.fanwe.fragment.YouHuiListFragment;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.common.SDActivityManager;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.model.Cart_check_cartActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.umeng.UmengShareManager;
import com.lidroid.xutils.http.ResponseInfo;
import com.sunday.eventbus.SDEventManager;

/**
 * app详情页js回调处理类
 * 
 * @author Administrator
 * 
 */
public class AppJsHandler extends BaseJsHandler {
	private static final String DEFAULT_NAME = "米果小站";

	public AppJsHandler(String name, Activity activity) {
		super(name, activity);
	}

	public AppJsHandler(Activity activity) {
		this(DEFAULT_NAME, activity);
	}

	@JavascriptInterface
	public void app_detail(int type, int id) {
		Intent intent = null;
		switch (type) {
		case IndexType.URL:
			break;
		case IndexType.TUAN_LIST:
			intent = new Intent(App.getApplication(), TuanListActivity.class);
			intent.putExtra(TuanListFragment.EXTRA_CATE_ID, id);
			break;
		case IndexType.GOODS_LIST:
			intent = new Intent(App.getApplication(), GoodsListActivity.class);
			intent.putExtra(GoodsListFragment.EXTRA_CATE_ID, id);
			break;
		case IndexType.SCORE_LIST:
			intent = new Intent(App.getApplication(), ScoresListActivity.class);
			intent.putExtra(ScoresListFragment.EXTRA_CATE_ID, id);
			break;
		case IndexType.EVENT_LIST:
			intent = new Intent(App.getApplication(), EventListActivity.class);
			intent.putExtra(EventListFragment.EXTRA_CATE_ID, id);
			break;
		case IndexType.YOUHUI_LIST:
			intent = new Intent(App.getApplication(), YouHuiListActivity.class);
			intent.putExtra(YouHuiListFragment.EXTRA_CATE_ID, id);
			break;
		case IndexType.STORE_LIST:
			intent = new Intent(App.getApplication(), StoreListActivity.class);
			intent.putExtra(StoreListFragment.EXTRA_CATE_ID, id);
			break;
		case IndexType.NOTICE_LIST:
			intent = new Intent(App.getApplication(), NoticeListActivity.class);
			break;
		case IndexType.DEAL_DETAIL:
			intent = new Intent(App.getApplication(), TuanDetailActivity.class);
			intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, id);
			break;
		case IndexType.EVENT_DETAIL:
			intent = new Intent(App.getApplication(), EventDetailActivity.class);
			intent.putExtra(EventDetailActivity.EXTRA_EVENT_ID, id);
			break;
		case IndexType.YOUHUI_DETAIL:
			intent = new Intent(App.getApplication(),
					YouHuiDetailActivity.class);
			intent.putExtra(YouHuiDetailActivity.EXTRA_YOUHUI_ID, id);
			break;
		case IndexType.STORE_DETAIL:
			intent = new Intent(App.getApplication(), StoreDetailActivity.class);
			intent.putExtra(StoreDetailActivity.EXTRA_MERCHANT_ID, id);
			break;
		case IndexType.NOTICE_DETAIL:
			intent = new Intent(App.getApplication(),
					NoticeDetailActivity.class);
			intent.putExtra(NoticeDetailActivity.EXTRA_NOTICE_ID, id);
			break;
		case IndexType.SCAN:
			SDEventManager.post(SDActivityManager.getInstance()
					.getLastActivity().getClass(),
					EnumEventTag.START_SCAN_QRCODE.ordinal());
			return;
		case IndexType.NEARUSER:
			intent = new Intent(App.getApplication(), NearbyVipActivity.class);
			break;
		case IndexType.DISTRIBUTION_STORE:
			intent = new Intent(App.getApplication(),
					DistributionStoreWapActivity.class);
			break;
		/*
		 * case IndexType.DISTRIBUTION_MANAGER: intent = new
		 * Intent(App.getApplication(), DistributionManageActivity.class);
		 * break;
		 */
		default:

			break;
		}
		startActivity(intent);
	}

	@JavascriptInterface
	public void close_page() {
		finish();
	}

	@JavascriptInterface
	public void setPageTitle(String title) {

	}

	@JavascriptInterface
	public void login() {
		Activity activity = SDActivityManager.getInstance().getLastActivity();
		AppHelper.isLogin(activity);
	}

	@JavascriptInterface
	public void page_title(String title) {
	}

	@JavascriptInterface
	public void promote(String location, String title, String summary,
			String pic) {

	}

	@JavascriptInterface
	public void start_main() {
		Intent intent = new Intent(mActivity, MainActivity.class);
		startActivity(intent);
	}

	/**
	 * 去购物车
	 */
	@JavascriptInterface
	public void goCart() {

		Intent intent = new Intent(mActivity, ShopCartActivity.class);
		startActivity(intent);
	}

	/**
	 * 分享
	 * @param url
	 * @param title
	 * @param summary
     * @param pic
     */
	@JavascriptInterface
	public void shareStore(String url, String title, String summary, String pic) {
		UmengShareManager.share(mActivity, title, summary, url,
				UmengShareManager.getUMImage(mActivity, pic), null);
	}

	@JavascriptInterface
	public void goDeal(int id) {
		Intent intent = new Intent(mActivity, StoreDetailActivity.class);
		intent.putExtra(StoreDetailActivity.EXTRA_SHOP_ID, id);
		startActivity(intent);
	}

	@JavascriptInterface
	public void goSupplierLocation(int id) {
		Intent intent = new Intent(mActivity, TuanDetailActivity.class);
		intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, id);
		startActivity(intent);

	}

	/**
	 * 扫我的二维码，先把商品加入购物车，然后 跳转到购物车页面。
	 * 
	 * @param productId
	 * @param userId
	 */
	@JavascriptInterface
	public void addCart(String productId, String userId) {
		// 保存购物车
		RequestModel request = new RequestModel();
		request.putCtl("cart");
		request.putAct("addcart");
		request.put("id", productId);
		request.put("uid", userId);
		request.put("number", 1);

		SDRequestCallBack<Cart_check_cartActModel> handler = new SDRequestCallBack<Cart_check_cartActModel>() {
			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					// 跳转购物车页面。
					goCart();
				} else {
					String message = actModel.getInfo();
					if (TextUtils.isEmpty(message)) {
						message = "服务器君不在状态了，请稍后再试！";
					}

					MGToast.showToast(message);
				}
			}

			@Override
			public void onFinish() {

			}
		};
		InterfaceServer.getInstance().requestInterface(request, handler);

	}

}
