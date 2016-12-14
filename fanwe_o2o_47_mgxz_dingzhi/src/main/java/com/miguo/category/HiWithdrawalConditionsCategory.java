package com.miguo.category;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.dao.MemberInterestDao;
import com.miguo.dao.UserUpgradeOrderDao;
import com.miguo.dao.impl.MemberInterestDaoImpl;
import com.miguo.dao.impl.UserUpgradeOrderDaoImpl;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.entity.MemberInterestBean;
import com.miguo.entity.UserUpgradeOrderBean;
import com.miguo.factory.ClassNameFactory;
import com.miguo.listener.HiWithdrawalConditionsListener;
import com.miguo.utils.BaseUtils;
import com.miguo.view.MemberInterestView;
import com.miguo.view.UserUpgradeOrderView;

import java.util.List;

/**
 * Created by zlh on 2016/12/13.
 */
public class HiWithdrawalConditionsCategory extends Category {

    @ViewInject(R.id.tv_update_introduce)
    TextView update;

    @ViewInject(R.id.can_speak)
    TextView canSpeak;

    /**
     * 获取代言权限
     */
    MemberInterestDao memberInterestDao;

    /**
     * 获取用户升级信息
     */
    UserUpgradeOrderDao userUpgradeOrderDao;



    public HiWithdrawalConditionsCategory(HiBaseActivity activity) {
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
        listener = new HiWithdrawalConditionsListener(this);
    }

    @Override
    protected void setThisListener() {
        update.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initLevelParams();
    }

    @Override
    protected void initViews() {

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
        memberInterestDao.getMemberInterest(2);
    }

    private void handlegetMemberInterestSuccess(List<MemberInterestBean.Result.Body> body){
        for(int i = 0; i<body.size(); i++){
            if(body.get(i).getLevel() == 1){
                continue;
            }
            if(body.get(i).getLevel() == 2){
                handleInitLevel(canSpeak, body.get(i));
                continue;
            }
        }
    }

    private void handleInitLevel(TextView levelRepresentCount, MemberInterestBean.Result.Body body){
        String count = "至" + body.getRepresent_num() + "家";
        levelRepresentCount.setText(count);
    }

    public void clickUpdate(){
        if(TextUtils.isEmpty(App.getInstance().getToken())){

        }
        userUpgradeOrderDao = new UserUpgradeOrderDaoImpl(new UserUpgradeOrderView() {
            @Override
            public void getUserUpgradeInfoSuccess(UserUpgradeOrderBean.Result.Body body) {
                if(!body.canUpdate()){
                    handleUpdate(body);
                    return;
                }
                handleUpadteEnoughMoney();
            }

            @Override
            public void getUserUpgradeInfoError(String message) {

            }
        });
        userUpgradeOrderDao.getUserUpgradeInfo();
    }

    /**
     * 付费升级
     * @param body
     */
    private void handleUpdate(UserUpgradeOrderBean.Result.Body body){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.UPDATE_USER_ACTIVITY));
        intent.putExtra(IntentKey.USER_ACCOUNT, body.getUser_account_money());
        intent.putExtra(IntentKey.UPDATE_ACCOUNT, body.getTotal_price());
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    private void handleUpadteEnoughMoney(){

    }

}
