package com.miguo.view;

import com.miguo.entity.MenuBean;

import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 */
public interface GetMenuListView extends BaseView{

    void getMenuListSuccess(List<MenuBean.Result.Body> list);
    void getMenuListError();

}
