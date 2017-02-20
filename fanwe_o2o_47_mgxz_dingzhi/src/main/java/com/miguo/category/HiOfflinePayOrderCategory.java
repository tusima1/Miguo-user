package com.miguo.category;

import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiOfflinePayOrderActivity;
import com.miguo.entity.OnlinePayOrderPaymentBean;
import com.miguo.listener.HiOfflinePayOrderListener;
import com.miguo.presenters.OnlinePayOrderPaymentPresenter;
import com.miguo.presenters.impl.OnlinePayOrderPaymentPresenterImpl;
import com.miguo.ui.view.RecyclerBounceNestedScrollView;
import com.miguo.ui.view.customviews.RedPacketPopup;
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

    @ViewInject(R.id.wechat_cb)
    CheckBox wechat;
    @ViewInject(R.id.alipay_cb)
    CheckBox alipay;
    @ViewInject(R.id.amount_cb)
    CheckBox account;

    @ViewInject(R.id.pay_order)
    TextView pay;

    OnlinePayOrderPaymentPresenter onlinePayOrderPaymentPresenter;

    public HiOfflinePayOrderCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        initOnlinePayOrderPaymentPresenter();
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
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {
        shopName.setText(getActivity().getShopName());
        amount.setText(getActivity().getAmount());
        orderSn.setText(getActivity().getOrderSn());
        recyclerBounceNestedScrollView.hideLoadingLayout();
    }

    /**
     * 支付presenter
     */
    private void initOnlinePayOrderPaymentPresenter(){
        onlinePayOrderPaymentPresenter = new OnlinePayOrderPaymentPresenterImpl(this);
    }

    @Override
    public void payError(String message) {
        showToast(message);
    }

    @Override
    public void paySuccess(OnlinePayOrderPaymentBean.Result.Body body) {
        showRedPacketPop(
                body.getShare_info(),
                "老板娘",
                body.getIcon(),
                body.getContent(),
                body.getOrder_info().getOrder_id(),
                body.getOrder_info().getSalary()
        );
    }

    private View content;
    private RedPacketPopup redPacketPopup;

    private void showRedPacketPop(OnlinePayOrderPaymentBean.Result.Body.Share share,String name,String faceIcon,String showContent,String order_id,String money){
        content = findViewById(android.R.id.content);
        redPacketPopup = new RedPacketPopup(getActivity(),content);
        redPacketPopup.setNeedData(share,name,faceIcon,showContent,order_id,money);
        redPacketPopup.showAtLocation(content, Gravity.CENTER,0,0);
    }

    public void clickPay(){
        /**
         * 微信和余额混合支付
         */
        if(wechat.isChecked() && account.isChecked()){
            onlinePayOrderPaymentPresenter.wechat(getActivity().getOrderId(), 1);
            return;
        }
        /**
         * 支付宝和余额混合支付
         */
        if(alipay.isChecked() && account.isChecked()){
            onlinePayOrderPaymentPresenter.alipay(getActivity().getOrderId(), 1);
            return;
        }
        /**
         * 微信支付
         */
        if(wechat.isChecked()){
            onlinePayOrderPaymentPresenter.wechat(getActivity().getOrderId(), 0);
            return;
        }
        /**
         * 支付宝支付
         */
        if(alipay.isChecked()){
            onlinePayOrderPaymentPresenter.alipay(getActivity().getOrderId(), 0);
            return;
        }
        /**
         * 余额支付
         */
        if(account.isChecked()){
            onlinePayOrderPaymentPresenter.amount(getActivity().getOrderId());
            return;
        }
    }

    @Override
    public HiOfflinePayOrderActivity getActivity() {
        return (HiOfflinePayOrderActivity)super.getActivity();
    }


}
