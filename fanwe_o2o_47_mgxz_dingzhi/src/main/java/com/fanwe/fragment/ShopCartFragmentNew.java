package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.ConfirmOrderActivity;
import com.fanwe.MainActivity;
import com.fanwe.adapter.ShopCartAdapter;
import com.fanwe.adapter.ShopCartAdapter.ShopCartSelectedListener;
import com.fanwe.app.AppHelper;
import com.fanwe.base.CallbackView;
import com.fanwe.common.CommonInterface;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.HorizontalSlideDeleteListView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.BaseActModel;
import com.fanwe.model.CartGoodsModel;
import com.fanwe.model.Cart_check_cartActModel;
import com.fanwe.model.Cart_indexActModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.fanwe.shoppingcart.presents.OutSideShoppingCartHelper;
import com.fanwe.utils.SDFormatUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 *
 * @author js02
 *
 */
public class ShopCartFragmentNew extends BaseFragment implements CallbackView {
    @ViewInject(R.id.lv_cart_goods)
    private HorizontalSlideDeleteListView mLvCartGoods;
    @ViewInject(R.id.rl_empty)
    private RelativeLayout mRlEmpty;


    /** 结算 */
    @ViewInject(R.id.ll_bottom)
    private LinearLayout mLl_count;
    @ViewInject(R.id.tv_sum)
    private TextView mTv_sum;

    @ViewInject(R.id.bt_account)
    private Button mBt_account;

    @ViewInject(R.id.cb_xuanze)
    private CheckBox mCb_xuanze;

    @ViewInject(R.id.bt_addTo_collect)
    private Button mBt_addToCollect;

    @ViewInject(R.id.bt_delect)
    private Button mBt_delect;

    @ViewInject(R.id.content_ptr)
    private PullToRefreshScrollView mContentPtr;//内容部分,可下拉刷新

    private ShopCartAdapter mAdapter;
    /**
     * 购物车商品列表。
     */
    private List<ShoppingCartInfo> listModel;

    private OutSideShoppingCartHelper outSideShoppingCartHelper;

    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        setmTitleType(TitleType.TITLE);
        return setContentView(R.layout.frag_shop_cart_new);
    }

    @Override
    protected void init() {
        super.init();
        outSideShoppingCartHelper= new OutSideShoppingCartHelper(this);
        initTitle();
        registeClick();
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
            }
        });
        mContentPtr.setRefreshing();
    }

    protected void resetInitData() {

        listModel=new ArrayList<>();
        mCb_xuanze.setChecked(false);
        //重置下巴(结算)
        mBt_account.setText("结算");
        mBt_account.setBackgroundColor(getResources().getColor(
                R.color.text_fenxiao));
        mBt_account.setClickable(false);
        mTv_sum.setText("0.00");
        // 初始化adapter.
        mAdapter = new ShopCartAdapter(listModel, getActivity(), this);
        getmAdapterListener();
        mLvCartGoods.setAdapter(mAdapter);
    }




    private void registeClick() {
        mBt_account.setOnClickListener(this);
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
     * @param isChecked
     *            是否被选择。
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
        int count = getSumSeleted(1);
        mCb_xuanze.setClickable(true);

        mTv_sum.setText(String.valueOf(sumMoney));
        if (count > 0) {
            mBt_account.setText("结算" + "（" + count + "）");
            mBt_account.setBackgroundColor(getResources().getColor(
                    R.color.main_color));
            mBt_account.setClickable(true);
            mBt_delect.setClickable(true);
        } else {
            mBt_account.setText("结算");
            mBt_account.setBackgroundColor(getResources().getColor(
                    R.color.text_fenxiao));
            mBt_account.setClickable(false);
            mBt_delect.setClickable(false);
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
     * @param type
     *            type=1 获取非编辑状态下的已经被选择的数量
     * @return
     */
    private Integer getSumSeleted(int type) {
        int size = 0;
        int count = 0;
        if (listModel == null || listModel.size() < 1) {
            return count;
        }
        size = listModel.size();
        for (int i = 0; i < size; i++) {
            ShoppingCartInfo model = listModel.get(i);
            boolean checked = false;
            if (type == 1) {
                checked = model.isChecked();
            }
            if (checked) {
                count++;
            }
        }
        return count;
    }

    private ArrayList<String> getSumSeletedIds(int type) {
        int size = 0;
        if (listModel == null || listModel.size() < 1) {
            return null;
        }
        ArrayList<String> mSeletedGoods = new ArrayList<String>();
        size = listModel.size();
        for (int i = 0; i < size; i++) {
            ShoppingCartInfo model = listModel.get(i);
            boolean checked = false;
            if (type == 1) {
                checked = model.isChecked();
            }
            if (checked) {
                if (type == 1) {
                    mSeletedGoods.add(model.getId());
                }
            }
        }
        return mSeletedGoods;
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
            model.setSumPrice(sumPrice);
        }
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        super.onCLickLeft_SDTitleSimple(v);
        requestCheckCart2();
    }

    private void requestCheckCart2()
    {
        if(mAdapter == null)
        {
            return;
        }

        RequestModel request = new RequestModel();
        request.putCtl("cart");
        request.putAct("check_cart");
        request.putUser();
        if(mAdapter != null)
        {
            request.put("num", mAdapter.getMapNumber());
        }
        SDRequestCallBack<Cart_check_cartActModel> handler = new SDRequestCallBack<Cart_check_cartActModel>()
        {
            @Override
            public void onStart()
            {

            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                if (actModel.getStatus() == 1)
                {

                }
            }

            @Override
            public void onFinish()
            {

            }
        };
        InterfaceServer.getInstance().requestInterface(request, handler);
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

                    int count = getSumSeleted(2);
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
        mLvCartGoods.setAdapter(mAdapter);
    }

    /**
     * 去结算
     */
    private void clickSettleAccounts() {
        // TODO 去结算
        requestCheckCart();
    }

    /**
     * 确认购物车。
     */
    private void requestCheckCart() {


        RequestModel model = new RequestModel();
        model.putCtl("cart");
        model.putAct("check_cart");
        model.putUser();
        if (mAdapter != null) {
            model.put("num", mAdapter.getMapNumber());
        }


        SDRequestCallBack<Cart_check_cartActModel> handler = new SDRequestCallBack<Cart_check_cartActModel>() {
            @Override
            public void onStart() {
                SDDialogManager.showProgressDialog("请稍候");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {
                    CommonInterface.updateCartNumber();
                    User_infoModel model = actModel.getUser_data();
                    if (model != null) {
                        LocalUserModel.dealLoginSuccess(model, false);
                    }
                           // TODO 跳到确认订单界面
                    startConfirmOrderActivity();
                }
            }

            @Override
            public void onFinish() {
                SDDialogManager.dismissProgressDialog();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    /**
     * 进入商品购买确认页。
     */
    private void startConfirmOrderActivity() {
        if (listModel != null && listModel.size() > 0 ) {
            ArrayList<String> mSeletedGoods = getSumSeletedIds(1);

            Intent intent = new Intent(getActivity(),
                    ConfirmOrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("list_id", mSeletedGoods);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            requestData();
        }
    }

    private void requestData() {
        outSideShoppingCartHelper.getUserShopCartList();
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
        SDViewUtil.toggleEmptyMsgByList(listModel, mRlEmpty);

        mAdapter.notifyDataSetChanged();

    }





    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            requestData();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_unlogin_buy:
                clickSettleAccounts();
                break;
            case R.id.bt_account:
                clickSettleAccounts();
                break;
            default:
                break;
        }

    }
    /**
     * 更新Title上的商品数量
     * @param num
     */
    private void updateTitleNum(int num){
        if (num == 0) {
            SDViewUtil.hide(mLl_count);
            mTitle.setMiddleTextTop("购物车");
        }else {
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
    public void onSuccess(String method, List datas) {
        switch (method){
            case ShoppingCartconstants.SHOPPING_CART_LIST:
                this.listModel = datas;

                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mAdapter!=null){
                            mAdapter.setData(listModel);
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
                        if(mAdapter!=null){
                            mAdapter.removeItems(listModel);
                        }
                    }
                });



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

}