package com.miguo.category;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.customview.MGProgressDialog;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.dao.GetUserLevelDao;
import com.miguo.dao.MemberInterestDao;
import com.miguo.dao.UserUpgradeOrderDao;
import com.miguo.dao.impl.GetUserLevelDaoImpl;
import com.miguo.dao.impl.MemberInterestDaoImpl;
import com.miguo.dao.impl.UserUpgradeOrderDaoImpl;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.entity.MemberInterestBean;
import com.miguo.entity.UserUpgradeOrderBean;
import com.miguo.factory.ClassNameFactory;
import com.miguo.listener.HiRepresentIntroduceListener;
import com.miguo.utils.BaseUtils;
import com.miguo.view.GetUserLevelView;
import com.miguo.view.MemberInterestView;
import com.miguo.view.UserUpgradeOrderView;

import java.util.List;

/**
 * Created by zlh on 2016/12/13.
 */

public class HiRepresentIntroduceCategory extends Category {

    @ViewInject(R.id.tv_update_introduce)
    TextView update;

    /**
     * 等级1可代言店的数量
     */
    @ViewInject(R.id.represent_count_1)
    TextView level1RepresentCount;

    /**
     * 等级2可代言店的数量
     */
    @ViewInject(R.id.represent_count_2)
    TextView level2RepresentCount;

    @ViewInject(R.id.can_withdraw)
    TextView withdraw;
    /**
     * 获取用户等级
     */
    GetUserLevelDao getUserLevelDao;

    /**
     * 获取代言权限
     */
    MemberInterestDao memberInterestDao;

    /**
     * 获取用户代言升级能否一键升级
     */
    UserUpgradeOrderDao userUpgradeOrderDao;

    /**
     * 访问网络时候的Dialog
     */
    MGProgressDialog dialog;


    public HiRepresentIntroduceCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initFirst() {
    }

    @Override
    protected void initThisListener() {
        listener = new HiRepresentIntroduceListener(this);
    }

    @Override
    protected void setThisListener() {
        update.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initUserLevel();
        initLevelParams();
    }

    @Override
    protected void initViews() {
        handleInitWithdraw();
    }

    /**
     * 点击会员升级
     */
    public void clickUpdate(){
        userUpgradeOrderDao = new UserUpgradeOrderDaoImpl(new UserUpgradeOrderView() {
            @Override
            public void getUserUpgradeInfoSuccess(UserUpgradeOrderBean.Result.Body body) {
                /**
                 * 无法一键升级
                 */
                if(!body.canUpdate()){
                    handleUpdate(body);
                    return;
                }
                /**
                 * 一键升级
                 */
                handleUpadteEnoughMoney(body);
            }

            @Override
            public void getUserUpgradeInfoError(String message) {
                showToast(message);
            }
        });
        userUpgradeOrderDao.getUserUpgradeInfo();
    }

    private void showDialog(){
        dialog = new MGProgressDialog(getActivity(),R.style.MGProgressDialog);
        dialog.show();
    }

    private void hideDialog(){
        if(dialog == null){
            return;
        }
        dialog.dismiss();
    }

    /**
     * 付费升级
     * @param body
     */
    private void handleUpdate(UserUpgradeOrderBean.Result.Body body){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.UPDATE_USER_ACTIVITY));
        intent.putExtra(IntentKey.USER_ACCOUNT, body.getUser_account_money());
        intent.putExtra(IntentKey.UPDATE_ACCOUNT, body.getTotal_price());
        BaseUtils.jumpToNewActivityForResult(getActivity(),intent, RequestCode.UPDATE_USER_GO_WITHDRAW_CONDITION);

    }

    /**
     * 一键升级
     * @param body
     */
    private void handleUpadteEnoughMoney(UserUpgradeOrderBean.Result.Body body){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.UPDATE_USER_WITH_ENOUGH_MONEY_ACTIVITY));
        intent.putExtra(IntentKey.USER_ACCOUNT, body.getUser_account_money());
        intent.putExtra(IntentKey.UPDATE_ACCOUNT, body.getTotal_price());
        BaseUtils.jumpToNewActivityForResult(getActivity(),intent, RequestCode.UPDATE_USER_GO_WITHDRAW_CONDITION);
    }

    /**
     * 获取用户等级
     */
    private void initUserLevel(){
        showDialog();
        getUserLevelDao = new GetUserLevelDaoImpl(new GetUserLevelView() {
            @Override
            public void getUserLevelSuccess(String level) {
                hideDialog();
                handleGetUserLevelSuccess(level);
            }

            @Override
            public void getUserLevelError(String message) {
                showToast(message);
                hideDialog();
            }
        });
        getUserLevelDao.getUserLevel();
    }

    private void handleGetUserLevelSuccess(String level){
        App.getInstance().getCurrentUser().setFx_level(level);
        handleHideUpdateTextView();
    }

    /**
     * 初始化代言权限之类接口
     */
    private void initLevelParams(){
        memberInterestDao = new MemberInterestDaoImpl(new MemberInterestView() {
            @Override
            public void getMemberInterestSuccess(List<MemberInterestBean.Result.Body> body) {
                handlegetMemberInterestSuccess(body);
            }

            @Override
            public void getMemberInterestError(String message) {

            }
        });
        memberInterestDao.getMemberInterest();
    }

    private void handlegetMemberInterestSuccess(List<MemberInterestBean.Result.Body> body){
        for(int i = 0; i<body.size(); i++){
            if(body.get(i).getLevel() == 1){
                handleInitLevel(level1RepresentCount, body.get(i));
                continue;
            }
            if(body.get(i).getLevel() == 2){
                handleInitLevel(level2RepresentCount, body.get(i));
                continue;
            }
        }
    }

    private void handleInitLevel(TextView levelRepresentCount, MemberInterestBean.Result.Body body){
        String count = "可代言" + getOringeText(body.getRepresent_num()) + "店铺";
        levelRepresentCount.setText(count);
        setRepresentTextSpan(levelRepresentCount, 0, getOringeText(body.getRepresent_num()).length());
    }

    private void handleHideUpdateTextView(){
        update.setVisibility(getUserLevel() == 2 ? View.GONE : View.VISIBLE);
    }

    private void handleInitWithdraw(){
        /**
         * 收益 2
         * 可提现 3
         */
        setRepresentTextSpan(withdraw, 2, 3);
    }

    /**
     * 设置指定字体颜色
     * @param textView 设置的对象
     * @param oringeCount 黄色部分的长度
     */
    private void setRepresentTextSpan(TextView textView, int start, int oringeCount){
        int startLength = start == 0 ? "可代言".length() : start;
        SpannableStringBuilder spannable = new SpannableStringBuilder(textView.getText().toString());
        spannable.setSpan(new ForegroundColorSpan(getColor(R.color.my_main_color)), startLength, startLength + oringeCount,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }

    private String getOringeText(int num){
        return num + "家";
    }

    private int getUserLevel(){
        try{
            return Integer.parseInt(App.getInstance().getCurrentUser().getFx_level());
        }catch (Exception e){
            return 1;
        }
    }
}
