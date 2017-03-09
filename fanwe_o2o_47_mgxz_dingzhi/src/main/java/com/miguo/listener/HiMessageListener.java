package com.miguo.listener;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiMessageCategory;
import com.miguo.definition.ClassPath;
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

    private void clickAmountMessage(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.MESSAGE_SYSTEM));
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    @Override
    public HiMessageCategory getCategory() {
        return (HiMessageCategory) super.getCategory();
    }
}
