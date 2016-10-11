package com.fanwe.dao.barry.view;

import com.fanwe.model.SpecialListModel;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface GetSpecialListView {
    void getSpecialListSuccess(SpecialListModel.Result result);
    void getSpecialListLoadmoreSuccess(SpecialListModel.Result result);
    void getSpecialListError(String msg);
}
