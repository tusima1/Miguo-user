package com.fanwe.utils;

import com.fanwe.seller.model.getCityList.ModelCityList;

import java.util.Comparator;

public class PinyinComparator implements Comparator<ModelCityList> {

    public int compare(ModelCityList o1, ModelCityList o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
