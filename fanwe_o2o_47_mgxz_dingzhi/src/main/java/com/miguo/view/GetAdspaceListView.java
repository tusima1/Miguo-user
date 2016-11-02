package com.miguo.view;

import com.miguo.entity.AdspaceListBean;

import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/27.
 */
public interface GetAdspaceListView extends BaseView{

    void getAdspaceListSuccess(List<AdspaceListBean.Result.Body> body, String type);
    void getAdspaceListError();

}
