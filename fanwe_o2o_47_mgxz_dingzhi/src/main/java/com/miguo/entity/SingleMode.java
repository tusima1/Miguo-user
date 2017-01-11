package com.miguo.entity;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class SingleMode {
    private int index;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
