package com.miguo.view.video;

import com.miguo.live.model.getLiveListNew.ModelRecordFile;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ModelVideoPlayer {
    private List<ModelRecordFile> fileSet;
    private String coverUrl;
    private String title;

    public List<ModelRecordFile> getFileSet() {
        return fileSet;
    }

    public void setFileSet(List<ModelRecordFile> fileSet) {
        this.fileSet = fileSet;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
