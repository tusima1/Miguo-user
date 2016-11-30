package com.fanwe.seller.model.getCityList;

import android.text.TextUtils;

import com.fanwe.utils.CharacterParser;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelCityList implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uname;

    private String is_hot;

    private String name;

    private String is_effect;

    private String pid;

    private String id;

    private String sort;

    private String is_default;

    private String is_delete;

    private String sortLetters;

    public String getSortLetters() {
        if (TextUtils.isEmpty(sortLetters)) {
            findSortLetters();
        }
        return sortLetters;
    }

    private void findSortLetters() {
        String firstLetters;
        if (!TextUtils.isEmpty(uname) && uname.length() > 1) {
            firstLetters = uname.substring(0, 1).toUpperCase();
        } else {
            firstLetters = CharacterParser.getFirstLetters(name);
        }

        if (TextUtils.isEmpty(firstLetters)) {
            firstLetters = "#";
        }
        sortLetters = firstLetters;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUname() {
        return this.uname;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getIs_hot() {
        return this.is_hot;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIs_effect(String is_effect) {
        this.is_effect = is_effect;
    }

    public String getIs_effect() {
        return this.is_effect;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return this.pid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return this.sort;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getIs_default() {
        return this.is_default;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getIs_delete() {
        return this.is_delete;
    }

}
