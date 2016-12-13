package com.miguo.view;

import com.miguo.entity.MemberInterestBean;

import java.util.List;

/**
 * Created by zlh on 2016/12/13.
 */

public interface MemberInterestView extends BaseView {
    void getMemberInterestSuccess(List<MemberInterestBean.Result.Body> bodies);
    void getMemberInterestError(String message);
}
