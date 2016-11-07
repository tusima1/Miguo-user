package com.miguo.live.model.getLiveListNew;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ModelRecordFile implements Serializable {
    private String duration;
    private String file_id;
    private String playset;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getPlayset() {
        return playset;
    }

    public void setPlayset(String playset) {
        this.playset = playset;
    }
}
