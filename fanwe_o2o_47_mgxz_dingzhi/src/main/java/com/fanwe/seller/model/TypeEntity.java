package com.fanwe.seller.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/6.
 */

public class TypeEntity implements Serializable {
    public String id;
    public String typeName;
    public String typeLevel;
    public boolean isChecked=false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeLevel() {
        return typeLevel;
    }

    public void setTypeLevel(String typeLevel) {
        this.typeLevel = typeLevel;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

