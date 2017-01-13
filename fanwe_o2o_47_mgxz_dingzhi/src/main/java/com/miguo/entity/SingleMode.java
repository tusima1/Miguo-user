package com.miguo.entity;

import java.io.Serializable;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class SingleMode implements Serializable{
    private String singleId;
    private boolean checked;
    protected String name;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingleId() {
        return singleId;
    }

    public void setSingleId(String singleId) {
        this.singleId = singleId;
    }
}
