package com.fanwe.constant;

/**
 * 网络地址配置类。如果非debug请直接改成DEBUG=false;
 *
 * @author Administrator
 */
public class ServerUrl {
    public static final boolean DEBUG = false;
    public static final String SERVER_API_TEST_URL = "w2.mgxz.com";

    //开发环境。
          public static final String SERVER_API_JAVA_TEST_URL = "http://mapi.dev.mgxz.com/";
    //测试环境。
//     public static final String SERVER_API_JAVA_TEST_URL = "http://mapi.test.mgxz.com/";
    //H5地址 开发
    public static final String SERVER_H5 = "http://m.dev.mgxz.com/";
    //H5地址 测试
//    public static final String SERVER_H5 = "http://m.test.mgxz.com/";


    /*袁浩*/
  //   public static final String  SERVER_API_JAVA_TEST_URL ="http://192.168.90.36:8080/mgxz.BussRPC/";

//      public static final String  SERVER_API_JAVA_TEST_URL ="http://192.168.90.36:8080/mgxz.BussRPC/";

    // public static final String  SERVER_API_JAVA_TEST_URL ="http://192.168.90.58:8080/mgxz.BussRPC/";


    //	public static final String SERVER_API_TEST_URL="pre.mgxz.com";
    public static final String SERVER_API_URL_MID = "http://mapi.mgxz.com";
    public static final String SERVER_API_URL_PRE = "http://";
    public static final String SERVER_API_URL_END = "/mapi/index.php";
    public static final String URL_PART_MOB = "/mob/main.html?from=app";
    public static final String URL_PART_WAP = "/wap/index.php";
    public static final String KEY_AES = "FANWE5LMUQC436IM";

    private static final String SERVER_API_URL_ONLINE = SERVER_API_URL_MID + SERVER_API_URL_END;


    public static String getServerApiUrl() {
        if (DEBUG) {
            return SERVER_API_URL_PRE + SERVER_API_TEST_URL + SERVER_API_URL_END;
        } else {
            //线上正式
            return SERVER_API_URL_ONLINE;
        }
    }
}
