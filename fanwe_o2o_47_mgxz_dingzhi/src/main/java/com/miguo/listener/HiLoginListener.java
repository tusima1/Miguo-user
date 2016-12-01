package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiLoginCategory;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by zlh on 2016/11/30.
 */

public class HiLoginListener extends Listener {

    public HiLoginListener(Category category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_find_password:
                clickFindPassword();
                break;
            case R.id.back:
                clickBack();
                break;
            case R.id.register:
                clickRegister();
                break;
            case R.id.qq_login:
                clickQQLogin();
                break;
            case R.id.weibo_login:
                clickWeiboLogin();
                break;
            case R.id.weixin_login:
                clickWechatLogin();
                break;
        }
    }

    private void clickFindPassword(){
        getCategory().clickFindPassword();
    }

    private void clickBack(){
        getCategory().clickBack();
    }

    private void clickRegister(){
        getCategory().clickRegister();
    }

    private void clickQQLogin(){
        getCategory().clickQQLogin();
    }

    private void clickWeiboLogin(){
        getCategory().clickWeiboLogin();
    }

    private void clickWechatLogin(){
        getCategory().clickWechatLogin();
    }

    @Override
    public HiLoginCategory getCategory() {
        return (HiLoginCategory) super.getCategory();
    }
}
