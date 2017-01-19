package com.miguo.framework;

import com.fanwe.cache.CacheUtil;

/**
 * Created by didik 
 * Created time 2017/1/18
 * Description: 
 */

public class CityManager {

    private BaseCity mHoldCity;

    private CityManager() {
    }
    private static class Singleton{
        private static final CityManager cityManager=new CityManager();
    }
    public static CityManager getInstance(){
        return Singleton.cityManager;
    }

    public void saveCurrCity(String cityName,String cityId,String unName){
        mHoldCity = new BaseCity(cityName,cityId,unName);
        saveCurrCity(mHoldCity);
    }

    public void saveCurrCity(BaseCity city){
        if (city == null)return;
        mHoldCity = city;
        CacheUtil.getInstance().setCurrCity(mHoldCity);
    }

    public BaseCity getCurrCity(){
        if (mHoldCity == null){
            mHoldCity = getFromLocal();
        }//还是可能再次为null
        return mHoldCity;
    }

    private BaseCity getFromLocal() {
        return CacheUtil.getInstance().getCurrCity();
    }


}
