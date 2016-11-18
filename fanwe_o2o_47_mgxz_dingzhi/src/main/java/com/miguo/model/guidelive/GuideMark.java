package com.miguo.model.guidelive;

import com.miguo.ui.view.interf.ExpandStatus;

/**
 * Created by didik on 2016/11/16.
 */

public class GuideMark {
    protected int status = ExpandStatus.NORMAL;//展开状态
    protected int av_status=0;//视频播放状态

    public int getAv_status() {
        return av_status;
    }

    public void setAv_status(int av_status) {
        this.av_status = av_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
