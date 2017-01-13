package com.miguo.ui.view.floatdropdown.helper;

/**
 * Created by didik 
 * Created time 2017/1/13
 * Description: 
 */

public class DropDownMarkBean {
    private int levelOne;
    private int levelTwo;
    private String name;

    public DropDownMarkBean(int levelOne, int levelTwo, String name) {
        this.levelOne = levelOne;
        this.levelTwo = levelTwo;
        this.name = name;
    }

    public int getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(int levelOne) {
        this.levelOne = levelOne;
    }

    public int getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(int levelTwo) {
        this.levelTwo = levelTwo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
