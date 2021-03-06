package com.fanwe.model;

import android.text.TextUtils;

import com.fanwe.utils.CharacterParser;

/**
 * Created by Administrator on 2016/12/1.
 */

public class CitylistModel {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String py;
    // ===========add
    private String sortLetters;

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getSortLetters() {
        if (TextUtils.isEmpty(sortLetters)) {
            findSortLetters();
        }
        return sortLetters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void findSortLetters() {
        String firstLetters;
        if (!TextUtils.isEmpty(py) && py.length() > 1) {
            firstLetters = py.substring(0, 1).toUpperCase();
        } else {
            firstLetters = CharacterParser.getFirstLetters(name);
        }

        if (TextUtils.isEmpty(firstLetters)) {
            firstLetters = "#";
        }
        sortLetters = firstLetters;
    }
}
