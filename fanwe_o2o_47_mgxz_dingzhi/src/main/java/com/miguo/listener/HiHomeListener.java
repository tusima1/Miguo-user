package com.miguo.listener;

import android.content.Intent;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.umeng.UmengEventStatistics;
import com.miguo.category.Category;
import com.miguo.category.HiHomeCategory;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.definition.TabId;
import com.miguo.live.views.LiveStartActivity;
import com.miguo.live.views.LiveStartAuthActivity;
import com.miguo.utils.BaseUtils;
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
//                clickLive();
                clickTab(2);
                break;
            case TabId.TAB_D:
                clickTab(3);
                break;
            case TabId.TAB_E:
                clickTab(4);
                break;
            case TabId.TAB_F:
                clickLive();
                break;
        }
    }

    @Override
    public boolean onInterceptScrollEvent(int tabId) {
        switch (tabId){
            case TabId.TAB_A:
                return true;
            case TabId.TAB_B:
                return true;
            /**
             * 直播不加入
             */
            case TabId.TAB_C:
                return !App.getInstance().getToken().equals("");
            case TabId.TAB_D:
                return true;
            case TabId.TAB_E:
                return !App.getInstance().getToken().equals("");
        }
        return false;
    }

    private void clickLive(){
        UmengEventStatistics.sendEvent(getActivity(), UmengEventStatistics.MAIN_2);

        if (TextUtils.isEmpty(App.getInstance().getToken()))// 未登录 以后加入是不是主播的判断。
        {
            BaseUtils.jumpToNewActivity(getActivity(), new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY)));
        } else {
            String is_host = App.getInstance().getCurrentUser().getIs_host();
            if ("0".equals(is_host)) {
                //未认证
                Intent intent = new Intent(getActivity(), LiveStartAuthActivity.class);
                intent.putExtra("pageType", "start");
                BaseUtils.jumpToNewActivity(getActivity(), intent);
            } else if ("1".equals(is_host)) {
                //已认证
                BaseUtils.jumpToNewActivity(getActivity(), new Intent(getActivity(), LiveStartActivity.class));
            } else {
                //认证中
                Intent intent = new Intent(getActivity(), LiveStartAuthActivity.class);
                intent.putExtra("pageType", "wait");
                BaseUtils.jumpToNewActivity(getActivity(), intent);
            }
        }
    }

    private void clickTab(int position){
//        if(position == 3){
//            if(TextUtils.isEmpty(App.getInstance().getToken())){
//                clickLogin();
//                return;
//            }
//        }
        getCategory().clickTab(position);
    }

    private void clickLogin(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    @Override
    public HiHomeCategory getCategory() {
        return (HiHomeCategory) super.getCategory();
    }
}
