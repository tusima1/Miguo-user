package com.miguo.view;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */

public interface GetInterestingView extends BaseView{

    void getInterestingSuccess(List<HashMap<String, String>> datas);
    void getInterestingError();

}
