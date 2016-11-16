package com.miguo.live.model.guidelive;

import com.miguo.ui.view.interf.ExpandStatus;

/**
 * Created by didik on 2016/11/16.
 */

public class GuideMark {
    protected int status = ExpandStatus.NORMAL;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
