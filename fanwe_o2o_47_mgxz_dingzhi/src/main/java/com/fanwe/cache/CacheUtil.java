package com.fanwe.cache;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.seller.model.getCityList.ModelCityList;
import com.miguo.framework.BaseCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */

public class CacheUtil {

    private static CacheUtil instance;

    private CacheUtil() {
    }

    public static synchronized CacheUtil getInstance() {
        if (instance == null) {
            instance = new CacheUtil();
        }
        return instance;
    }

    private List<ModelCityList> listCity;
    private List<ModelCityList> listCityHot;
    private ModelCityList cityCurr;

    /**
     * 缓存城市列表
     *
     * @param cityList
     */
    public void saveCityList(List<ModelCityList> cityList) {
        if (SDCollectionUtil.isEmpty(cityList)) {
            return;
        }
        Serial.saveObjectByFile(SerialConstant.FILE_CITY_LIST, cityList);
    }

    /**
     * 缓存热门城市列表
     *
     * @param cityListHot
     */
    public void saveCityListHot(List<ModelCityList> cityListHot) {
        if (SDCollectionUtil.isEmpty(cityListHot)) {
            return;
        }
        Serial.saveObjectByFile(SerialConstant.FILE_CITY_LIST_HOT, cityListHot);
    }

    /**
     * 缓存当前城市
     *
     * @param cityCurr
     */
    public void saveCityCurr(ModelCityList cityCurr) {
        if (cityCurr == null) {
            return;
        }
        Serial.saveObjectByFile(SerialConstant.FILE_CITY_CURR, cityCurr);
    }

    /**
     * 读取城市列表
     *
     * @return
     */
    public List<ModelCityList> getCityList() {
        if (SDCollectionUtil.isEmpty(listCity)) {
            try {
                listCity = Serial.readObjectByFile(SerialConstant.FILE_CITY_LIST);
            } catch (Exception e) {
                e.printStackTrace();
                listCity = new ArrayList<>();
            }
        }
        return listCity;
    }

    /**
     * 读取热门城市列表
     *
     * @return
     */
    public List<ModelCityList> getCityListHot() {
        if (SDCollectionUtil.isEmpty(listCityHot)) {
            try {
                listCityHot = Serial.readObjectByFile(SerialConstant.FILE_CITY_LIST_HOT);
            } catch (Exception e) {
                e.printStackTrace();
                listCityHot = new ArrayList<>();
            }
        }
        return listCityHot;
    }

    /**
     * 读取当前城市
     *
     * @return
     */
    public ModelCityList getCityCurr() {
        if (cityCurr == null) {
            try {
                cityCurr = Serial.readObjectByFile(SerialConstant.FILE_CITY_CURR);
            } catch (Exception e) {
                e.printStackTrace();
                cityCurr = new ModelCityList();
            }
        }
        return cityCurr;
    }
    public BaseCity getCurrCity() {//new
        BaseCity currCity = null;
            try {
                currCity = Serial.readObjectByFile(SerialConstant.FILE_CURR_CITY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return currCity;
    }
    public void setCurrCity(BaseCity baseCity) {//new
        if (baseCity == null)return;
        Serial.saveObjectByFile(SerialConstant.FILE_CURR_CITY,baseCity);
    }

    public void saveUserSearchWord(String singleWord) {
        if (TextUtils.isEmpty(singleWord)) {
            return;
        }
        List<String> userSearchWord = getUserSearchWord();
        for (String s : userSearchWord) {
            if (s.equalsIgnoreCase(singleWord)){
                Log.e("test","存在同样的key.");
                return;
            }
        }
        List<String> newList=new ArrayList<>();
        if (userSearchWord.size()>=9){
            for (int i = 0; i < 8; i++) {
                newList.add(userSearchWord.get(i));
            }
        }
        newList.add(0,singleWord);
        Serial.saveObjectByFile(SerialConstant.FILE_HOT_SEARCH_WORD, newList);
    }

    public void saveUserSearchWord(List<String> hotWords) {
        if (hotWords ==null || hotWords.size() == 0) {
            Serial.saveObjectByFile(SerialConstant.FILE_HOT_SEARCH_WORD, new ArrayList<String>());
            return;
        }
        List<String> newList=new ArrayList<>();
        if ( hotWords.size()>=9){
            for (int i = 0; i < 9; i++) {
                newList.add(hotWords.get(i));
            }
            Serial.saveObjectByFile(SerialConstant.FILE_HOT_SEARCH_WORD, newList);
            return;
        }
        Serial.saveObjectByFile(SerialConstant.FILE_HOT_SEARCH_WORD, hotWords);
    }

    public List<String> getUserSearchWord() {
        List<String> hotWords;
        try {
            hotWords = Serial.readObjectByFile(SerialConstant.FILE_HOT_SEARCH_WORD);
        } catch (Exception e) {
            e.printStackTrace();
            hotWords = new ArrayList<>();
        }
        List<String> newList=new ArrayList<>();
        if (hotWords!=null && hotWords.size()>=9){
            for (int i = 0; i < 9; i++) {
                newList.add(hotWords.get(i));
            }
            return newList;
        }
        return hotWords;
    }

}
