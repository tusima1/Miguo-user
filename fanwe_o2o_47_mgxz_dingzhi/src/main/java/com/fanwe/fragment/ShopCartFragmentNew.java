package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.ConfirmOrderActivity;
import com.fanwe.LoginActivity;
import com.fanwe.MainActivity;
import com.fanwe.adapter.ShopCartAdapter;
import com.fanwe.adapter.ShopCartAdapter.ShopCartSelectedListener;
import com.fanwe.app.App;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.HorizontalSlideDeleteListView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.CartGoodsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.RefreshCalbackView;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.utils.SDFormatUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 *
 * @author js02
 */
public class ShopCartFragmentNew extends BaseFragment implements RefreshCalbackView {
    @ViewInject(R.id.lv_cart_goods)
    private HorizontalSlideDeleteListView mLvCartGoods;
    /**
     * 当前结果为空
     */
    @ViewInject(R.id.rl_empty)
    private RelativeLayout mRlEmpty;
    /**
     * 结算
     */
    @ViewInject(R.id.ll_bottom)
    private LinearLayout mLl_count;
    /**
     * 总计：
     */
    @ViewInject(R.id.tv_sum)
    private TextView mTv_sum;

    @ViewInject(R.id.bt_account)
    private Button mBt_account;

    @ViewInject(R.id.iv_xuanze)
    private CheckBox mCb_xuanze;


    @ViewInject(R.id.content_ptr)
    private PullToRefreshScrollView mContentPtr;//内容部分,可下拉刷新

    private ShopCartAdapter mAdapter;
    /**
     * 购物车商品列表。
     */
    private List<ShoppingCartInfo> listModel;

    private OutSideShoppingCartHelper outSideShoppingCartHelper;
    /**
     * 当前操作，是返回，还是结算 进入下一步，默认是结算 1 ，返回2
     */
    private int currentGoTo = 1;
    private boolean ifLogin = false;

    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        setmTitleType(TitleType.TITLE);
        return setContentView(R.layout.frag_shop_cart_new);
    }

    @Override
    protected void init() {
        super.init();
        checkLogin();
        outSideShoppingCartHelper = new OutSideShoppingCartHelper(this);
        initTitle();
        registeClick();
        resetInitData();
        initPull2RefreshSrcollView();
    }

    public void checkLogin() {
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            ifLogin = true;
        } else {
            ifLogin = false;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        resetInitData();
        initPull2RefreshSrcollView();
    }

    /**
     * 添加下拉刷新功能
     */
    private void initPull2RefreshSrcollView() {
        mContentPtr.setMode(Mode.PULL_FROM_START);
        mContentPtr.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                requestData();
                resetInitData();
            }
        });
        mContentPtr.setRefreshing();
    }

    protected void resetInitData() {

        listModel = new ArrayList<>();
        mCb_xuanze.setChecked(false);
        //重置下巴(结算)
        mBt_account.setText("结算");
        mBt_account.setBackgroundColor(getResources().getColor(
                R.color.text_fenxiao));
        mBt_account.setClickable(false);
        mTv_sum.setText("0.00");
        // 初始化adapter.
        mAdapter = new ShopCartAdapter(listModel, getActivity(), this);
        mLvCartGoods.setAdapter(mAdapter);
        getmAdapterListener();
    }

    private void registeClick() {
        mBt_account.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               clickSettleAccounts();
                                           }
                                       }
        );

        // 编辑状态下
        mCb_xuanze.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                boolean isChecked = mCb_xuanze.isChecked();
                BigDecimal bd = checkListModelStateAndSumMoney(isChecked);
                mTv_sum.setText(String.valueOf(bd));
                if (isChecked) {
                    mBt_account.setText("结算" + "（" + listModel.size() + "）");
                    mBt_account.setBackgroundColor(getResources().getColor(
                            R.color.main_color));
                    mBt_account.setClickable(true);
                } else {
                    mBt_account.setText("结算");
                    mBt_account.setBackgroundColor(getResources().getColor(
                            R.color.text_fenxiao));
                    mBt_account.setClickable(false);
                }
            }
        });
    }

    /**
     * 初始化标题栏。
     */
    private void initTitle() {
        mTitle.setMiddleTextTop("购物车");
        if (getActivity() instanceof MainActivity) {
            mTitle.setLeftImageLeft(0);
        }
    }

    /**
     * 改变购物车列表里面的商品是否被选择属性,同时计算总金额。。
     *
     * @param isChecked 是否被选择。
     */
    private BigDecimal checkListModelStateAndSumMoney(boolean isChecked) {
        int size = 0;
        float sumMoney = 0.00f;
        BigDecimal value = new BigDecimal(0.00);
        if (listModel == null || listModel.size() < 1) {
            return value;
        }
        size = listModel.size();
        for (int i = 0; i < size; i++) {
            ShoppingCartInfo model = listModel.get(i);
            model.setChecked(isChecked);

            if (model.isChecked()) {
                sumMoney += model.getSumPrice();
            }
        }
        mAdapter.notifyDataSetChanged();
        value = new BigDecimal(sumMoney);
        value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        return value;
    }

    /**
     * 更新总金额和结算数量,以及是否全选的状态。
     */
    public void updateSumMoneyAndCount() {
        BigDecimal sumMoney = getSumMoney();
        int count = getSumSeleted();
        mTv_sum.setText(String.valueOf(sumMoney));
        if (count > 0) {
            mBt_account.setText("结算" + "（" + count + "）");
            mBt_account.setBackgroundColor(getResources().getColor(
                    R.color.main_color));
            mBt_account.setClickable(true);

        } else {
            mBt_account.setText("结算");
            mBt_account.setBackgroundColor(getResources().getColor(
                    R.color.text_fenxiao));
            mBt_account.setClickable(false);
        }
        if (listModel != null && (count > 0) && (count == listModel.size())) {
            mCb_xuanze.setChecked(true);
        } else {
            mCb_xuanze.setChecked(false);
        }
    }

    /**
     * 获取总金额。
     *
     * @return
     */
    private BigDecimal getSumMoney() {
        int size = 0;
        float sumMoney = 0.00f;
        BigDecimal value = new BigDecimal(0.00);
        if (listModel == null || listModel.size() < 1) {
            return value;
        }
        size = listModel.size();
        for (int i = 0; i < size; i++) {
            ShoppingCartInfo model = listModel.get(i);
            if (model.isChecked()) {
                sumMoney += model.getSumPrice();
            }
        }
        value = new BigDecimal(sumMoney);
        value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        return value;
    }

    /**
     * 获取已经选中听选项.
     *
     * @return
     */
    private Integer getSumSeleted() {
        int size = 0;
        int count = 0;
        if (listModel == null || listModel.size() < 1) {
            return count;
        }
        size = listModel.size();
        for (int i = 0; i < size; i++) {
            ShoppingCartInfo model = listModel.get(i);
            boolean checked = false;
            checked = model.isChecked();

            if (checked) {
                count++;
            }
        }
        return count;
    }

    private String getSumSeletedIds() {
        int size = 0;
        if (listModel == null || listModel.size() < 1) {
            return null;
        }
        StringBuffer selectedIds = new StringBuffer();


        size = listModel.size();
        for (int i = 0; i < size; i++) {
            ShoppingCartInfo model = listModel.get(i);
            boolean checked = false;
            checked = model.isChecked();
            if (checked) {
                selectedIds.append(model.getId() + ",");

            }
        }
        if (selectedIds != null && selectedIds.length() > 1) {
            return selectedIds.substring(0, selectedIds.length() - 1);
        }
        return "";
    }

    /**
     * 计算每一个各类商品的总小计金额。
     */
    private void initSumPrice() {
        int size = 0;
        if (listModel == null || listModel.size() < 1) {
            return;
        }
        size = listModel.size();
        for (int i = 0; i < size; i++) {
            float sumPrice = 0.00f;
            ShoppingCartInfo model = listModel.get(i);

            int firstNum = SDFormatUtil.stringToInteger(model.getIs_first());
            int number = SDFormatUtil.stringToInteger(model.getNumber());


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
            sumPrice = number * SDFormatUtil.stringToFloat(model.getTuan_price()) - sumPrice;
            model.setSumPrice(sumPrice);
        }
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        currentGoTo = 2;
        returnToLastActivity();
        super.onCLickLeft_SDTitleSimple(v);
    }

    public void getmAdapterListener() {
        mAdapter.setOnShopCartSelectedListener(new ShopCartSelectedListener() {
            @Override
            public void onSelectedListener() {
                updateSumMoneyAndCount();
            }

            @Override
            public void onDelSelectedListener(CartGoodsModel model,
                                              boolean isChecked) {
                // 非编辑状态
                int count = getSumSeleted();
                if (count == listModel.size()) {
                    mCb_xuanze.setChecked(true);
                } else {
                    mCb_xuanze.setChecked(false);
                }
            }

            @Override
            public void onTitleNumChangeListener(int num) {
                updateTitleNum(num);
            }
        });

    }

    /**
     * 返回按扭，先提交购物车，后返回。
     */
    private void returnToLastActivity() {
        currentGoTo = 2;
        if (ifLogin) {
            outSideShoppingCartHelper.multiAddShopCart(listModel);
        } else {
            LocalShoppingcartDao.insertModel(listModel);
        }

    }

    /**
     * 确认购物车。
     */
    private void clickSettleAccounts() {
        currentGoTo = 1;
        if (ifLogin) {
            //添加购物车调用接口
            outSideShoppingCartHelper.multiAddShopCart(listModel);
        } else {
            LocalShoppingcartDao.insertModel(listModel);
            gotoLogin();
            setmIsNeedRefreshOnResume(true);
        }
    }


    private void gotoLogin() {
        Intent intent = new Intent(getActivity(),
                LoginActivity.class);
        getContext().startActivity(intent);
    }

    /**
     * 进入商品购买确认页。
     */
    private void startConfirmOrderActivity() {
        if (listModel != null && listModel.size() > 0) {
            String mSeletedGoods = getSumSeletedIds();



            Intent intent = new Intent(getActivity(),
                    ConfirmOrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("list_id", mSeletedGoods);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            requestData();
        }
    }

    private void requestData() {
        if (ifLogin) {
            outSideShoppingCartHelper.getUserShopCartList();
        } else {
            SDDialogManager.dismissProgressDialog();
            List<ShoppingCartInfo> datas = LocalShoppingcartDao.queryModel();
            onSuccess(ShoppingCartconstants.SHOPPING_CART_LIST, datas);
        }
    }

    protected void bindData() {
        if (listModel == null) {
            mTitle.setMiddleTextTop("购物车");
            SDViewUtil.hide(mLl_count);
            mTitle.initRightItem(0);
        } else {
            mTitle.initRightItem(1);
            mTitle.setMiddleTextTop("购物车" + "（" + listModel.size() + "）");
        }
        initSumPrice();
        if (listModel == null || listModel.size() < 1) {
            mRlEmpty.setVisibility(View.VISIBLE);
        } else {
            mRlEmpty.setVisibility(View.GONE);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            requestData();
        }
        super.onHiddenChanged(hidden);
    }


    /**
     * 更新Title上的商品数量
     *
     * @param num
     */
    private void updateTitleNum(int num) {
        if (num == 0) {
            SDViewUtil.hide(mLl_count);
            mTitle.setMiddleTextTop("购物车");
        } else {
            mTitle.setMiddleTextTop("购物车" + "（" + num + "）");
        }
    }


    @Override
    protected void onNeedRefreshOnResume() {
        requestData();
        super.onNeedRefreshOnResume();
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        outSideShoppingCartHelper = null;
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method,final List datas) {
        switch (method) {
            case ShoppingCartconstants.SHOPPING_CART_LIST:
                this.listModel = datas;
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter != null) {
                            listModel = datas;
                            mAdapter.setData(listModel);
                            bindData();
                            mAdapter.notifyDataSetChanged();
                        }
                        mContentPtr.onRefreshComplete();
                    }
                });

                break;
            case ShoppingCartconstants.SHOPPING_CART_DELETE:
                this.listModel.removeAll(datas);
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter != null) {
                            mAdapter.setData(listModel);
                            mAdapter.notifyDataSetChanged();
                            updateTitleNum(listModel.size());
                        }
                    }
                });
                break;
            case ShoppingCartconstants.BATCH_SHOPPING_CART:
                if (currentGoTo == 1) {
                    startConfirmOrderActivity();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }


    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    @Override
    public void onFailue(String method, String responseBody) {
        switch (method) {
            case ShoppingCartconstants.SHOPPING_CART_LIST:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContentPtr.onRefreshComplete();

                    }
                });
                break;
            case ShoppingCartconstants.SHOPPING_CART_DELETE:
                MGToast.showToast(responseBody);
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestData();

                    }
                });
                break;
            case ShoppingCartconstants.BATCH_SHOPPING_CART:
                MGToast.showToast(responseBody);
                break;
            default:
                break;
        }
    }
}