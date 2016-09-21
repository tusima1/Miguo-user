package com.miguo.utils;

import com.miguo.live.model.PlaySetInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class RTMPUtils {
    public static final int  MOBILE_MP4=10;
    public static final int  SD_MP4=10;
    public static final int  HD_MP4=10;

    public static final int  MOBILE_FLV=110;
    public static final int  SD_FLV=120;
    public static final int  HD_FLV=130;


    public String checkUrlByWIFI(List<PlaySetInfo> playSetInfoList){
        int networkClass = NetWorkStateUtil.getNetworkClass();
        //默认110 手机  FLV
        int definition=MOBILE_FLV;

        String type = "未知";
        switch (networkClass) {
            case NetWorkStateUtil.NETWORK_CLASS_UNAVAILABLE:
                definition = MOBILE_FLV;
               // type = "无";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_WIFI:
                definition = HD_FLV;
              //  type = "Wi-Fi";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_2_G:
                definition = MOBILE_FLV;
              //  type = "2G";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_3_G:
                definition = MOBILE_FLV;

                // type = "3G";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_4_G:
                definition = HD_FLV;

                //  type = "4G";
                break;
            case NetWorkStateUtil.NETWORK_CLASS_UNKNOWN:
                definition = MOBILE_FLV;
                //   type = "未知";
                break;
        }
        return type;

    }
}
