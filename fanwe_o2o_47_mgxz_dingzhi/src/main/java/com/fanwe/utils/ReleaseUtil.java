package com.fanwe.utils;

/**
 * Created by didik 
 * Created time 2016/12/29
 * Description: 
 */

public class ReleaseUtil {
    public static final String Debug="d";
    public static final String Pre="p";

    public static String doVersionName(String versionName){
        String debug="."+Debug;
        String pre="."+Pre;
        String realVersionName;
        if (versionName.endsWith(debug) || versionName.endsWith(pre)){
            realVersionName = versionName.substring(0,versionName.length()-2);
        }else {
            realVersionName=versionName;
        }
        return realVersionName;
    }

}
