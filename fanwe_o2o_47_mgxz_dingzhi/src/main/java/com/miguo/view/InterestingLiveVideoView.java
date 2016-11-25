package com.miguo.view;

import com.miguo.entity.HiFunnyLiveVideoBean;
import com.miguo.entity.InterestingLiveVideoBean;
import com.miguo.live.model.getLiveListNew.ModelRoom;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public interface InterestingLiveVideoView extends BaseView {

    void getInterestingLiveVideoSuccess(List<ModelRoom> liveList);
    void getInterestingLiveVideoLoadmoreSuccess(List<ModelRoom> liveList);
    void getInterestingLiveVideoError();

}
