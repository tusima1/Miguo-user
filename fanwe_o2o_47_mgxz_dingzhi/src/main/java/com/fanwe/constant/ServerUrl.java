package com.fanwe.constant;

/**
 * 网络地址配置类。如果非debug请直接改成DEBUG=false;
 *
 * @author Administrator
 */
public class ServerUrl {
    /*********************** Do Not Modify ***********************/
    public static final String SERVER_API_TEST_URL = "w2.mgxz.com";
    public static final String SERVER_API_URL_PRE = "http://";
    public static final String URL_PART_MOB = "/mob/main.html?from=app";
    public static final String URL_PART_WAP = "/wap/index.php";
    public static final String KEY_AES = "FANWE5LMUQC436IM";
    private static final String SERVER_API_URL_ONLINE = "http://mapi.mgxz.com";
    private static final String SERVER_API_JAVA_TEST_URL = "http://mapi.test.mgxz.com/";
    private static final String SERVER_API_JAVA_DEV_URL = "http://mapi.dev.mgxz.com/";
    private static final String SERVER_H5_TEST="http://m.test.mgxz.com/";//测试
    private static final String SERVER_H5_DEV="http://m.dev.mgxz.com/";//开发
    private static final String SERVER_H5_ONLINE="http://m.mgxz.com/";//线上
    private static final String SERVER_API_37="http://192.168.90.37:8080/mgxz.BussRPC/";
    private static final String SERVER_API_36="http://192.168.90.36:8080/mgxz.BussRPC/";
    private static final String SERVER_API_58="http://192.168.90.58:8080/mgxz.BussRPC/";
//    public static final String SERVER_API_58="http://192.168.90.37:8080/mgxz.BussRPC/";//袁浩
    /*********************** Do Not Modify ***********************/
    public static boolean DEBUG =true;//默认开启debug
    public static String SERVER_H5=SERVER_H5_DEV;//默认开发
    private static String SERVER_API_USING=SERVER_API_JAVA_DEV_URL;//默认开发

    public static boolean getDebug(){
        return DEBUG;
    }
    public static void setDebug(boolean debug){
        DEBUG=debug;
    }

    public static void setServerApi(String api){
        SERVER_API_USING=api;
    }
    public static String getServerApi(){
        return SERVER_API_USING;
    }

    public static void setServerH5(String h5api){
        SERVER_H5=h5api;
    }

    public static String getServerH5(){
        return SERVER_H5;
    }
    /************* Add End ************/



    public static String getAppServerApiUrl() {
        if (DEBUG) {
            return SERVER_API_USING;
        } else {
            //线上正式
            return SERVER_API_URL_ONLINE;
        }
    }
}
