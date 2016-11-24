package com.miguo.view;

import com.miguo.entity.HiFunnyTabBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public interface HiFunnyTabView extends BaseView{

    void getLiveTabListSuccess(List<HiFunnyTabBean.Result.Body> tabs);
    void getGuideTabListSuccess(List<HiFunnyTabBean.Result.Body> tabs);
    void getLiveTabListError();
    void getGuideTabListError();

}
