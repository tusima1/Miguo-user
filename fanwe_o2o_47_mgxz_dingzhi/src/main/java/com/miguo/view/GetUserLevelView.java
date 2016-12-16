package com.miguo.view;

/**
 * Created by zlh on 2016/12/13.
 */

public interface GetUserLevelView extends BaseView {

    void getUserLevelSuccess(String level);
    void getUserLevelError(String message);

}
