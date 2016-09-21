package com.miguo.utils;

import android.text.TextUtils;

import com.miguo.live.model.PlaySetInfo;

import java.util.HashMap;
import java.util.List;

/**
 * 点播视频处理。
 * Created by Administrator on 2016/9/21.
 */
public class RTMPUtils {
    public static final int  MOBILE_MP4=10;
    public static final int  SD_MP4=20;
    public static final int  HD_MP4=30;

    public static final int  MOBILE_HLS=210;
    public static final int  SD_HLS=220;
    public static final int  HD_HLS=230;

    /**
     * 根据当前 用户的网络情况 选择拉流的地址。
     * @param playSetInfoList
     * @return
     */
    public static PlaySetInfo checkUrlByWIFI(HashMap<Integer,PlaySetInfo> playSetInfoList){

        if(playSetInfoList==null||playSetInfoList.size()<1){
            return null;
        }
        int networkClass = NetWorkStateUtil.getNetworkClass();
        //默认110 手机  HLS
        int definition=MOBILE_MP4;

        String type = "未知";
        switch (networkClass) {
            case NetWorkStateUtil.NETWORK_CLASS_UNAVAILABLE:
                definition = MOBILE_MP4;
               // type = "无";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_WIFI:
                definition = SD_MP4;
              //  type = "Wi-Fi";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_2_G:
                definition = MOBILE_MP4;
              //  type = "2G";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_3_G:
                definition = MOBILE_MP4;

                // type = "3G";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_4_G:
                definition = MOBILE_MP4;

                //  type = "4G";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_UNKNOWN:
                definition = MOBILE_MP4;
                //   type = "未知";
                break;
        }
        PlaySetInfo playSetInfo = playSetInfoList.get(definition);
        if(playSetInfo!=null&&!TextUtils.isEmpty(playSetInfo.getUrl())) {
            return playSetInfo;
        }else{
            PlaySetInfo playSetInfo2 = playSetInfoList.get(0);
            return playSetInfo2;
        }

    }
}
