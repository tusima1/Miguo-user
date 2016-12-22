package com.fanwe.constant;

/**
 * 网络地址配置类。如果非debug请直接改成DEBUG=false;
 *
 * @author Administrator
 */
public class ServerUrl {
    /**************************** !!!上线需要修改模式!!!****************************/
    public static final boolean DEBUG = true;//默认开启debug,上线改为false即可,其他接口API不要改动

    /*************************
     * 打测试包
     ******************************/
    public static final boolean TEST = true;//给测试使用请设置为true

    /***********************
     * Do Not Modify
     ***********************/
    public static final String KEY_AES = "FANWE5LMUQC436IM";
    private static final String SERVER_API_URL_ONLINE = "http://mapi.mgxz.com";
    private static final String SERVER_H5_ONLINE = "http://m.mgxz.com/";//线上
    /***********************
     * Do Not Modify
     ***********************/
    private static String SERVER_H5_USING = SERVER_H5_ONLINE;
    private static String SERVER_API_USING = SERVER_API_URL_ONLINE;

    public static void setServerApi(String api) {
        if (!DEBUG) return;
        SERVER_API_USING = api;
    }

    public static void setServerH5Using(String h5api) {
        if (!DEBUG) return;
        SERVER_H5_USING = h5api;
    }

    /*************
     * Add End
     ************/

    public static String getAppServerApiUrl() {
        if (DEBUG) {
            return SERVER_API_USING;
        } else {
            //线上正式
            return SERVER_API_URL_ONLINE;
        }
    }

    public static String getAppH5Url() {
        if (DEBUG) {
            return SERVER_H5_USING;
        } else {
            return SERVER_H5_ONLINE;
        }
    }
}
