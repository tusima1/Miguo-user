package com.fanwe.jshandler;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.GoodsListActivity;
import com.fanwe.NoticeDetailActivity;
import com.fanwe.NoticeListActivity;
import com.fanwe.ScoresListActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.StoreListActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.TuanListActivity;
import com.fanwe.YouHuiDetailActivity;
import com.fanwe.YouHuiListActivity;
import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.constant.Constant.IndexType;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.constant.ServerUrl;
import com.fanwe.fragment.GoodsListFragment;
import com.fanwe.fragment.ScoresListFragment;
import com.fanwe.fragment.StoreListFragment;
import com.fanwe.fragment.TuanListFragment;
import com.fanwe.fragment.YouHuiListFragment;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.utils.MGDictUtil;
import com.miguo.app.HiHomeActivity;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

/**
 * app详情页js回调处理类
 *
 * @author Administrator
 */
public class AppJsHandler extends BaseJsHandler {
    private static final String DEFAULT_NAME = "米果小站";

    private boolean ifLogin = false;

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
                intent = new Intent(App.getApplication(), GoodsDetailActivity.class);
                intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, id);
                break;
            case IndexType.EVENT_DETAIL:

                break;
            case IndexType.YOUHUI_DETAIL:
                intent = new Intent(App.getApplication(),
                        YouHuiDetailActivity.class);
                intent.putExtra(YouHuiDetailActivity.EXTRA_YOUHUI_ID, id);
                break;
            case IndexType.STORE_DETAIL:
                intent = new Intent(App.getApplication(), HiShopDetailActivity.class);
                intent.putExtra(HiShopDetailActivity.EXTRA_MERCHANT_ID, id);
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
                //NearbyVipActivity
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

    /**
     * 去购物车
     */
    @JavascriptInterface
    public void goCart() {
        Intent intent = new Intent(mActivity, ShopCartActivity.class);
        startActivity(intent);
    }

	public void goLogin(){
		Intent intent = new Intent(mActivity, ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
		startActivity(intent);
	}


    @JavascriptInterface
    public void goDeal(int id) {
        Intent intent = new Intent(mActivity, HiShopDetailActivity.class);
        intent.putExtra(HiShopDetailActivity.EXTRA_SHOP_ID, id);
        startActivity(intent);
    }

    @JavascriptInterface
    public void goSupplierLocation(int id) {
        Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
        intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, id);
        startActivity(intent);

    }

    /**
     * 活动页添加并跳转购物 车。
     *
     * @param goods_id
     * @param add_goods_num
     * @param fx_user_id
     * @param share_record_id
     */
    @JavascriptInterface
    public void addCart(String goods_id, String add_goods_num, String fx_user_id, String share_record_id) {
        ShoppingCartInfo cartInfo = new ShoppingCartInfo();
        cartInfo.setNumber(add_goods_num);
        cartInfo.setPro_id(goods_id);
        cartInfo.setFx_user_id(fx_user_id);
        cartInfo.setShare_record_id(share_record_id);
        checkLogin();
        if (ifLogin) {
            OutSideShoppingCartHelper outSideShoppingCartHelper = new OutSideShoppingCartHelper(new RefreshCalbackView() {
                @Override
                public void onSuccess(String responseBody) {

                }

                @Override
                public void onSuccess(String method, List datas) {
                    goCart();
                }

                @Override
                public void onFailue(String responseBody) {
                    SDToast.showToast(responseBody);
                }

                @Override
                public void onFinish(String method) {

                }

                @Override
                public void onFailue(String method, String responseBody) {

                }
            });
            outSideShoppingCartHelper.addShopCart(
                    fx_user_id,
                    App.getApplication().getCurrentUser().getUser_id(),
                    App.getApplication().getToken(), goods_id, "1", add_goods_num, share_record_id);

        } else {
            if (LocalShoppingcartDao.insertSingleNum(cartInfo)) {
                goLogin();
            }
        }

    }

    /**
     * 扫我的二维码，先把商品加入购物车，然后 跳转到购物车页面。
     *
     * @param productId
     * @param userId
     */
    @JavascriptInterface
    public void addCart2(String productId, String add_goods_num, String userId, String share_record_id) {
        addCart(productId, add_goods_num, userId, share_record_id);
    }


    public void checkLogin() {
        ifLogin = !TextUtils.isEmpty(App.getInstance().getToken());
    }

    @JavascriptInterface
    public void close_page() {
        finish();
    }

    @JavascriptInterface
    public void setPageTitle(String title) {

    }


    @JavascriptInterface
    public void start_main() {
        Intent intent = new Intent(mActivity, HiHomeActivity.class);
        startActivity(intent);
    }

    /**
     * 分享
     *
     * @param url
     * @param title
     * @param summary
     * @param pic
     */
    @JavascriptInterface
    public void shareStore(String url, String title, String summary, String pic) {
        if (TextUtils.isEmpty(title)) {
            title = "米果小站";
        }
        if (TextUtils.isEmpty(summary)) {
            summary = "欢迎来到米果小站";
        }
        if (TextUtils.isEmpty(pic)) {
            pic = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
            if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                pic = MGDictUtil.getShareIcon();
            }
        } else if (!pic.startsWith("http")) {
            pic = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
            if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                pic = MGDictUtil.getShareIcon();
            }
        }
        if (TextUtils.isEmpty(url)) {
            url = ServerUrl.getAppH5Url();
        }
        final String finalTitle = title;
        final String finalSummary = summary;
        final String finalUrl = url;
        final String finalPic = pic;
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UmengShareManager.share(mActivity, finalTitle, finalSummary, finalUrl, UmengShareManager.getUMImage(mActivity, finalPic), null);
            }
        });
    }


}
