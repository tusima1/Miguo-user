package com.miguo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class TwoMode <T extends SingleMode> extends SingleMode implements Serializable{

    private List<T> singleModeList;

    public List<T> getSingleModeList() {
        return singleModeList;
    }

    public void setSingleModeList(List<T> singleModeList) {
        this.singleModeList = singleModeList;
    }

    /**
     * 把数据传给父类统一处理
     * @param modeList 二级分类的数据
     */
    protected void giveList2Parent(List<T> modeList){
        this.singleModeList = modeList;
    }
}