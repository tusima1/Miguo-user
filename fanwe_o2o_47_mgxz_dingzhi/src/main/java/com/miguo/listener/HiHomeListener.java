package com.miguo.listener;

import android.content.Intent;
import android.text.TextUtils;

import com.fanwe.LoginActivity;
import com.fanwe.app.App;
import com.miguo.category.Category;
import com.miguo.category.HiHomeCategory;
import com.miguo.live.definition.TabId;
import com.miguo.live.views.LiveStartActivity;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.ui.view.BarryTab;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeListener extends Listener implements BarryTab.OnTabClickListener{

    public HiHomeListener(Category category) {
        super(category);
    }

    @Override
    public void onTabClick(int tabId) {
        switch (tabId){
            case TabId.TAB_A:
                clickTab(0);
                break;
            case TabId.TAB_B:
                clickTab(1);
                break;
            /**
             * 直播不加入
             */
            case TabId.TAB_C:
                clickLive();
                break;
            case TabId.TAB_D:
                clickTab(2);
                break;
            case TabId.TAB_E:
                clickTab(3);
                break;
        }
    }

    @Override
    public boolean onInterceptScrollEvent(int tabId) {
        switch (tabId){
            case TabId.TAB_A:
                return false;
            case TabId.TAB_B:
                return false;

            /**
             * 直播不加入
             */
            case TabId.TAB_C:
                return false;
            case TabId.TAB_D:
                return false;

            case TabId.TAB_E:
                return App.getInstance().getToken().equals("");
        }
        return false;
    }

    private void clickLive(){
        Intent intent = new Intent(getActivity(), LiveStartActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    private void clickTab(int position){
        if(position == 3){
            if(TextUtils.isEmpty(App.getInstance().getToken())){
                clickLogin();
                return;
            }
        }
        getCategory().clickTab(position);
    }

    private void clickLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    @Override
    public HiHomeCategory getCategory() {
        return (HiHomeCategory) super.getCategory();
    }
}
