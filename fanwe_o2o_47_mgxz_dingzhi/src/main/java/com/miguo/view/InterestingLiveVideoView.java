package com.miguo.view;

import com.miguo.entity.InterestingLiveVideoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public interface InterestingLiveVideoView extends BaseView {

    void getInterestingLiveVideoSuccess(List<InterestingLiveVideoBean.Result.Body.Live> liveList);
    void getInterestingLiveVideoLoadmoreSuccess(List<InterestingLiveVideoBean.Result.Body.Live> liveList);
    void getInterestingLiveVideoError();

}
