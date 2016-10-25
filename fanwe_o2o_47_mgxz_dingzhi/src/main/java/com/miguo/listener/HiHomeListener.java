package com.miguo.listener;

import com.miguo.category.Category;
import com.miguo.category.HiHomeCategory;
import com.miguo.live.definition.TabId;
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
            case TabId.TAB_C:
                clickTab(2);
                break;
            case TabId.TAB_D:
                clickTab(3);
                break;
            case TabId.TAB_E:
                clickTab(4);
                break;
        }
    }

    private void clickTab(int position){
        getCategory().clickTab(position);
    }

    @Override
    public HiHomeCategory getCategory() {
        return (HiHomeCategory) super.getCategory();
    }
}
