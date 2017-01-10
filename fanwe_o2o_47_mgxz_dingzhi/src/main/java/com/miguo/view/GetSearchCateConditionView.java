package com.miguo.view;

import com.miguo.entity.SearchCateConditionBean;

/**
 * Created by zlh on 2017/1/9.
 */

public interface GetSearchCateConditionView extends BaseView {

    void getSearchCateConditionSuccess(SearchCateConditionBean.Result.Body body);
    void getSearchCateConditionError(String message);

}
