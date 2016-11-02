package com.miguo.live.model.getLiveListNew;

import com.miguo.live.model.PlaySetInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ModelRecordFile implements Serializable {
    private String duration;
    private String fileId;
    private ArrayList<PlaySetInfo> playSet;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public ArrayList<PlaySetInfo> getPlaySet() {
        return playSet;
    }

    public void setPlaySet(ArrayList<PlaySetInfo> playSet) {
        this.playSet = playSet;
    }
}
