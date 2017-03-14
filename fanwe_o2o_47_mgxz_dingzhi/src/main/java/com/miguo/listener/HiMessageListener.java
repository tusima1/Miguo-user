package com.miguo.listener;

import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiMessageCategory;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.definition.Source;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.BaseUtils;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/9.
 */

public class HiMessageListener extends Listener {

    public HiMessageListener(Category category) {
        super(category);
    }

    @Override
    protected void onClickThis(View v) {
        switch (v.getId()){
            case R.id.open:
                clickOpen();
                break;
            case R.id.system_message_layout:
                clickSystemMessage();
                break;
            case R.id.amount_message_layout:
                clickAmountMessage();
                break;
            case R.id.close:
                clickClose();
                break;
        }
    }

    private void clickClose(){
        getCategory().clickClose();
    }

    private void clickOpen(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
        try{
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }catch (Exception e){
            intent = new Intent(Settings.ACTION_SETTINGS);
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }

    }

    private void clickSystemMessage(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.MESSAGE_SYSTEM));
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    public void clickAmountMessage(){
        if(TextUtils.isEmpty(App.getInstance().getToken())){
            Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
            intent.putExtra(IntentKey.FROM_SOURCE, Source.AMOUNT_MESSAGE);
            BaseUtils.jumpToNewActivityForResult(getActivity(), intent, RequestCode.AMOUNT_MESSAGE_LOGIN);
            return;
        }
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.MESSAGE_SYSTEM));
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    @Override
    public HiMessageCategory getCategory() {
        return (HiMessageCategory) super.getCategory();
    }
}
