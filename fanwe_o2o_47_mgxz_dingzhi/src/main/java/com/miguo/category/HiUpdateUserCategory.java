package com.miguo.category;

import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiUpdateUserActivity;
import com.miguo.listener.HiUpdateUserListener;
import com.miguo.presenters.UserUpgradePresenter;
import com.miguo.presenters.impl.UserUpgradePresenterImpl;
import com.miguo.utils.BaseUtils;
import com.miguo.view.UserUpgradePresenterView;

/**
 * Created by zlh on 2016/12/14.
 * 升级代言人
 */
public class HiUpdateUserCategory extends Category implements UserUpgradePresenterView{

    /**
     * 您的余额可以抵扣x元
     */
    @ViewInject(R.id.user_account)
    TextView userAccount;

    /**
     * 预扣0元，后续收益慢慢还
     */
    @ViewInject(R.id.update_by_withholding)
    TextView updateByWithholding;

    /**
     * 微信支付0元
     */
    @ViewInject(R.id.update_by_wechat)
    TextView updateByWechat;
    /**
     * 支付宝支付0元
     */
    @ViewInject(R.id.update_by_alipay)
    TextView updateByAlipay;


    /**
     * 用户升级presenter
     */
    UserUpgradePresenter userUpgradePresenter;

    public HiUpdateUserCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initFirst() {
        userUpgradePresenter = new UserUpgradePresenterImpl(this);
    }

    @Override
    protected void initThisListener() {
        listener = new HiUpdateUserListener(this);
    }

    @Override
    protected void setThisListener() {
        updateByWithholding.setOnClickListener(listener);
        updateByWechat.setOnClickListener(listener);
        updateByAlipay.setOnClickListener(listener);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {
        userAccount.setText(getUserAccount());
        updateByWithholding.setText(getUpdateByWithholding());
        updateByWechat.setText(getUpdateByWechat());
        updateByAlipay.setText(getUpdateByAlipay());
    }

    /**
     * 预扣费支付
     * 这里不会涉及到全额支付，全额支付是一键升级
     */
    public void clickUpdateByWithholding(){
        /**
         * {@link com.miguo.presenters.impl.UserUpgradePresenterImpl}
         * {@link #userUpgradeSuccess(String)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradePresenter.userUpgradeByWithholding();
    }

    /**
     * 微信支付
     */
    public void clickUpdateByWechat(){
        /**
         * {@link com.miguo.presenters.impl.UserUpgradePresenterImpl}
         * {@link #userUpgradeSuccess(String)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradePresenter.userUpgradeByWechat();
    }

    /**
     * 支付宝支付
     */
    public void clickByUpdateByAlipay(){
        /**
         * {@link com.miguo.presenters.impl.UserUpgradePresenterImpl}
         * {@link #userUpgradeSuccess(String)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradePresenter.userUpgradeByAlipay();
    }

    @Override
    public void userUpgradeError(String message) {
        showToast(message);
    }

    @Override
    public void userUpgradeSuccess(String message) {
        showToast(message);
        BaseUtils.finishActivity(getActivity());
    }

    @Override
    public HiUpdateUserActivity getActivity() {
        return (HiUpdateUserActivity) super.getActivity();
    }

    private String getUserAccount(){
        return "您的余额可以抵扣" + ( getActivity().getUserAccount() < 0 ? "0" : DataFormat.toDoubleTwo(getActivity().getUserAccount()) ) + "元";
    }

    private String getUpdateByWithholding(){
        return "预扣" + ( DataFormat.toDoubleTwo(getActivity().getUserAccount() < 0 ? getActivity().getUpdateAccount() : getActivity().getUpdateAccount() - getActivity().getUserAccount()) ) + "元，后续收益慢慢还";
    }

    private String getUpdateByWechat(){
        return "微信支付 " + DataFormat.toDoubleTwo(getActivity().getUpdateAccount()) + "元";
    }

    private String getUpdateByAlipay(){
        return "支付宝支付 " + DataFormat.toDoubleTwo(getActivity().getUpdateAccount()) + "元";
    }

}
