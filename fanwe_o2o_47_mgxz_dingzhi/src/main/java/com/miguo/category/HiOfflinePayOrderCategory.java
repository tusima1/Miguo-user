package com.miguo.category;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.view.MyOrderListActivity;
import com.fanwe.utils.DataFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiOfflinePayOrderActivity;
import com.miguo.dao.OnlinePayCancelDao;
import com.miguo.dao.OnlinePayOrderContinuePaymentDao;
import com.miguo.dao.impl.OnlinePayCancelDaoImpl;
import com.miguo.dao.impl.OnlinePayOrderContinuePaymentDaoImpl;
import com.miguo.definition.PaymentId;
import com.miguo.dialog.OfflinePaySuccessDialog;
import com.miguo.entity.OnlinePayOrderBean;
import com.miguo.entity.OnlinePayOrderPaymentBean;
import com.miguo.listener.HiOfflinePayOrderListener;
import com.miguo.presenters.OnlinePayOrderPaymentPresenter;
import com.miguo.presenters.impl.OnlinePayOrderPaymentPresenterImpl;
import com.miguo.ui.view.RecyclerBounceNestedScrollView;
import com.miguo.ui.view.customviews.RedPacketPopup;
import com.miguo.utils.BaseUtils;
import com.miguo.view.OnlinePayCancelView;
import com.miguo.view.OnlinePayOrderContinuePaymentView;
import com.miguo.view.OnlinePayOrderPaymentPresenterView;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class HiOfflinePayOrderCategory extends Category implements OnlinePayOrderPaymentPresenterView{

    @ViewInject(R.id.shop_name)
    TextView shopName;

    @ViewInject(R.id.amount)
    TextView amount;

    @ViewInject(R.id.order_sn)
    TextView orderSn;

    @ViewInject(R.id.recycler_scrollview)
    RecyclerBounceNestedScrollView recyclerBounceNestedScrollView;

    @ViewInject(R.id.account_text)
    TextView userAccount;

    @ViewInject(R.id.wechat_cb)
    CheckBox wechat;
    @ViewInject(R.id.alipay_cb)
    CheckBox alipay;
    @ViewInject(R.id.amount_cb)
    CheckBox account;

    @ViewInject(R.id.wechat_layout)
    RelativeLayout wechatLayout;

    @ViewInject(R.id.alipay_layout)
    RelativeLayout alipayLayout;

    @ViewInject(R.id.account_layout)
    RelativeLayout accountLayout;

    @ViewInject(R.id.pay_order)
    TextView pay;

    OnlinePayOrderPaymentPresenter onlinePayOrderPaymentPresenter;

    OnlinePayCancelDao onlinePayCancelDao;

    OnlinePayOrderContinuePaymentDao onlinePayOrderContinuePaymentDao;

    OnlinePayOrderBean.Result.Body orderInfo;

    public HiOfflinePayOrderCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        initOnlinePayOrderPaymentPresenter();
        initOnlinePayCancelDao();
        initOnlinePayOrderContinuePaymentDao();
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }


    @Override
    protected void initThisListener() {
        listener = new HiOfflinePayOrderListener(this);
    }

    @Override
    protected void setThisListener() {
        pay.setOnClickListener(listener);
        wechatLayout.setOnClickListener(listener);
        alipayLayout.setOnClickListener(listener);
        accountLayout.setOnClickListener(listener);
        wechat.setOnCheckedChangeListener(listener);
        alipay.setOnCheckedChangeListener(listener);
        account.setOnCheckedChangeListener(listener);
    }

    @Override
    protected void init() {
        initOrderInfo();
    }

    private void initOrderInfo(){
        if(getActivity().isFromOrderList()){
            onlinePayOrderContinuePaymentDao.getOrderInfo(getActivity().getOrderId(), App.getInstance().getToken());
        }
    }

    @Override
    protected void initViews() {
        userAccount.setText(getActivity().getUserAmountString());
        shopName.setText(getActivity().getShopName());
        amount.setText(getActivity().getAmount());
        orderSn.setText(getActivity().getOrderSn());
        if(!getActivity().isFromOrderList()){
            updateUserAccountVisibility(getActivity().getUserAmount() <= 0 ? View.GONE : View.VISIBLE);
        }
        recyclerBounceNestedScrollView.hideLoadingLayout();
    }

    /**
     * 支付presenter
     */
    private void initOnlinePayOrderPaymentPresenter(){
        onlinePayOrderPaymentPresenter = new OnlinePayOrderPaymentPresenterImpl(this);
    }

    private void initOnlinePayOrderContinuePaymentDao(){
        onlinePayOrderContinuePaymentDao = new OnlinePayOrderContinuePaymentDaoImpl(new OnlinePayOrderContinuePaymentView() {
            @Override
            public void getOrderInfoSuccess(OnlinePayOrderBean.Result.Body orderInfo) {
                pay.setEnabled(true);
                HiOfflinePayOrderCategory.this.orderInfo = orderInfo;
                handleUpdateOrderInfo();
            }

            @Override
            public void getOrderInfoError(String message) {
                pay.setEnabled(true);
                HiOfflinePayOrderCategory.this.orderInfo = null;
            }
        });
    }

    private void handleUpdateOrderInfo(){
        shopName.setText(orderInfo.getShop_name());
        orderSn.setText(orderInfo.getOrder_sn());
        amount.setText(orderInfo.getTotal_price() + "");
        userAccount.setText("用户余额" + orderInfo.getUser_account_money());
        updateUserAccountVisibility(orderInfo.getUser_account_money() <= 0 ? View.GONE : View.VISIBLE);
    }

    private void updateUserAccountVisibility(int visibility){
        accountLayout.setVisibility(visibility);
    }

    private void initOnlinePayCancelDao(){
        onlinePayCancelDao = new OnlinePayCancelDaoImpl(new OnlinePayCancelView() {
            @Override
            public void cancelSuccess() {
                getActivity().clickBack2();
            }

            @Override
            public void cancelError(String message) {
                getActivity().clickBack2();
            }
        });
    }

    public void cancelOrder(){
        onlinePayCancelDao.cancelOrder(getActivity().getOrderId(), App.getInstance().getToken());
    }

    @Override
    public void payError(String message) {
        if(getActivity().isFromOrderList()){
            return;
        }
        Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
        intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, "pay_wait");
        BaseUtils.jumpToNewActivityWithFinish(getActivity(), intent);
    }

    String paymentId;

    @Override
    public void paySuccessAlipay(OnlinePayOrderPaymentBean.Result.Body body) {
        if(DataFormat.toDouble(body.getOrder_info().getSalary()) <= 0){
            this.paymentId = PaymentId.ALIPAY;
            return;
        }
        showRedPacketPop(
                body.getShare_info(),
                "老板娘",
                body.getIcon(),
                body.getContent(),
                body.getOrder_info().getOrder_id(),
                body.getOrder_info().getSalary()
        );
    }

    public void paySuccessWechat(){
        paySuccess(onlinePayOrderPaymentPresenter.getBody());
    }

    @Override
    public void paySuccess(OnlinePayOrderPaymentBean.Result.Body body) {
        if(DataFormat.toDouble(body.getOrder_info().getSalary()) <= 0){
            showPaySuccessDialog();
            return;
        }
        showRedPacketPop(
                body.getShare_info(),
                "老板娘",
                body.getIcon(),
                body.getContent(),
                body.getOrder_info().getOrder_id(),
                body.getOrder_info().getSalary()
        );
    }

    public void checkPaySuccessWithAlipay(){
        if(isEmpty(paymentId)) return;
        if(paymentId.equals(PaymentId.ALIPAY)){
            this.paymentId = "";
            showPaySuccessDialog();
        }
    }

    public void showPaySuccessDialog(){
        final OfflinePaySuccessDialog dialog = new OfflinePaySuccessDialog();
        dialog.setOfflinePaySuccessDialogListener(new OfflinePaySuccessDialog.OfflinePaySuccessDialogListener() {
            @Override
            public void onConfirm() {
                BaseUtils.finishActivity(getActivity());
            }
        });
        /**
         * 如果是支付宝支付，是在onSaveInstanceState之后调用这个方法，会抛出
         * java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState异常
         * 需要在onResume调用fragment创建
         */
        dialog.show(getActivity().getSupportFragmentManager(), "pay_success_dialog");
    }

    private View content;
    private RedPacketPopup redPacketPopup;

    private void showRedPacketPop(OnlinePayOrderPaymentBean.Result.Body.Share share,String name,String faceIcon,String showContent,String order_id,String money){
        content = findViewById(android.R.id.content);
        redPacketPopup = new RedPacketPopup(getActivity(),content);
        redPacketPopup.setNeedData(share,name,faceIcon,showContent,order_id,money);
        redPacketPopup.setDismissListener(new RedPacketPopup.OnPopupWindowDismissListener() {
            @Override
            public void whenDismiss() {
                getActivity().finish();
            }
        });
        redPacketPopup.showAtLocation(content, Gravity.CENTER,0,0);
    }

    public void clickPay(){
        if(getActivity().isFromOrderList()){
            if(this.orderInfo == null){
                return;
            }
        }

        pay.setEnabled(false);

        /**
         * 微信和余额混合支付
         */
        if(wechat.isChecked() && account.isChecked()){
            onlinePayOrderPaymentPresenter.wechat(getActivity().isFromOrderList() ? orderInfo.getOrder_id() : getActivity().getOrderId(), 1);
            return;
        }
        /**
         * 支付宝和余额混合支付
         */
        if(alipay.isChecked() && account.isChecked()){
            onlinePayOrderPaymentPresenter.alipay(getActivity().isFromOrderList() ? orderInfo.getOrder_id() : getActivity().getOrderId(), 1);
            return;
        }
        /**
         * 微信支付
         */
        if(wechat.isChecked()){
            onlinePayOrderPaymentPresenter.wechat(getActivity().isFromOrderList() ? orderInfo.getOrder_id() : getActivity().getOrderId(), 0);
            return;
        }
        /**
         * 支付宝支付
         */
        if(alipay.isChecked()){
            onlinePayOrderPaymentPresenter.alipay(getActivity().isFromOrderList() ? orderInfo.getOrder_id() : getActivity().getOrderId(), 0);
            return;
        }
        /**
         * 余额支付
         */
        if(account.isChecked()){
            onlinePayOrderPaymentPresenter.amount(getActivity().isFromOrderList() ? orderInfo.getOrder_id() : getActivity().getOrderId());
            return;
        }
    }

    public void clickBack(){
        getActivity().clickBack();
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.isPressed()){
            switch (buttonView.getId()){
                case R.id.wechat_cb:
                    handleWechatChecked(isChecked);
                    break;
                case R.id.alipay_cb:
                    handleAlipayChecked(isChecked);
                    break;
                case R.id.amount_cb:
                    handleAccountChecked(isChecked);
                    break;
            }
        }
    }

    public void handleClickWechatLayout(){
        wechat.setChecked(!wechat.isChecked());
        handleWechatChecked(wechat.isChecked());
    }

    public void handleClickAlipayLayout(){
        alipay.setChecked(!alipay.isChecked());
        handleAlipayChecked(alipay.isChecked());
    }

    public void handleClickAccountLayout(){
        account.setChecked(!account.isChecked());
        handleAccountChecked(account.isChecked());
    }

    private void handleWechatChecked(boolean isChecked) {
        if (isChecked) {
            if(userHasEnoughAccountMoney()){
                account.setChecked(false);
            }
            alipay.setChecked(false);
            return;
        }

        if(!alipay.isChecked() && !account.isChecked() || (!userHasEnoughAccountMoney() && account.isChecked())){
            wechat.setChecked(true);
        }
    }

    private void handleAlipayChecked(boolean isChecked){
        if (isChecked) {
            if(userHasEnoughAccountMoney()){
                account.setChecked(false);
            }
            wechat.setChecked(false);
            return;
        }

        if(!wechat.isChecked() && !account.isChecked() || (!userHasEnoughAccountMoney() && account.isChecked())){
            alipay.setChecked(true);
        }
    }

    private void handleAccountChecked(boolean isChecked){
        if (isChecked) {
            if(userHasEnoughAccountMoney()){
                alipay.setChecked(false);
                wechat.setChecked(false);
            }
            return;
        }

        if(!wechat.isChecked() && !alipay.isChecked()){
            account.setChecked(true);
        }
    }

    public boolean userHasEnoughAccountMoney(){
        return getActivity().getUserAmount() >= getActivity().getTotalAmount();
    }

    public OnlinePayOrderBean.Result.Body getOrderInfo() {
        return orderInfo;
    }

    @Override
    public HiOfflinePayOrderActivity getActivity() {
        return (HiOfflinePayOrderActivity)super.getActivity();
    }


}
