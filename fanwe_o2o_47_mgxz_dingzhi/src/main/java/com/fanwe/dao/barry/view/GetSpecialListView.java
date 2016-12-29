package com.fanwe.dao.barry.view;

import com.fanwe.model.SpecialListModel;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface GetSpecialListView {
    void getSpecialListSuccess(String httpUuid,SpecialListModel.Result result);
    void getSpecialListLoadmoreSuccess(String httpUuid,SpecialListModel.Result result);
    void getSpecialListError(String httpUuid,String msg);
    void getSpecialListNoData(String httpUuid,String msg);
}
