package com.miguo.framework;

import java.io.Serializable;

/**
 * Created by didik 
 * Created time 2017/1/18
 * Description: 
 */

public class BaseCity implements Serializable{
    protected String name;
    protected String id;
    protected String unName;

    public BaseCity(String name, String id, String unName) {
        this.name = name;
        this.id = id;
        this.unName = unName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnName() {
        return unName;
    }

    public void setUnName(String unName) {
        this.unName = unName;
    }
}
