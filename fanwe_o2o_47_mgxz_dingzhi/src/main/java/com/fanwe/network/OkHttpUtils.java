package com.fanwe.network;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.utils.MD5Util;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * Created by Administrator on 2016/7/22.
 */
public class OkHttpUtils {
    private volatile static OkHttpUtils mInstance;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private  static String TAG="StringRequestUrl";
    private static String APP_KEY = "app_key";
    private static String APP_SECURITY = "app_security";
    private static String TIMESTAMP = "timestamp";
    private static String IMEI = "imei";
    private static String APP_TYPE = "app_type";
    private static String APP_KEY_DEFAULT = "c3e67013-e439-11e5-bbcc-a0d3c1ef5680";
    private static String APP_SECURITY_DEFAULT = "2acabf04914eeaec6b841a81f09711d8";
    /**
     * 加密方式 。
     */
    private static final String ENCRYPT_TYPE = "MD5";
    //"http://192.168.2.43:9080/mgxz.AuthorRPC/";




    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }
    /**
     * 异步POST提交，带TAG 的 请求
     * POST方法发送请求时，仍然使用基本的URL，将参数信息放在请求实体中发送。
     * @param url url 地址
     * @param params params
     * @param mCallback 返回
     */
    public void post(String url, TreeMap<String,String> params, MgCallback mCallback)  {
        post(url,params,mCallback,null);
    }

    /**
     * 同步请求POST。
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String synchronousPost(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return "";
        }
    }
    public void post(String url, TreeMap<String,String> params, MgCallback mCallback,Object tag)  {
        String serverUrl="";
        if(ServerUrl.DEBUG){
            serverUrl = ServerUrl.SERVER_API_TEST_URL;
        }else{
            serverUrl = ServerUrl.SERVER_API_URL_MID;
        }
        //袁浩 测试地址
       // serverUrl = "http://192.168.2.41:8080/mgxz.BussRPC/";
        if(!TextUtils.isEmpty(url)){
            serverUrl +=url;
        }
        if(serverUrl.endsWith("/")){
            serverUrl = serverUrl.substring(0,serverUrl.length()-1);
        }
        //添加公共参数
        params.putAll(commonParams());
        //加密所有的参数
        params = encryptParams(params);

        FormBody.Builder formBody = new FormBody.Builder();
        for(Map.Entry<String,String> entry:params.entrySet()){
            formBody.add(entry.getKey(),entry.getValue().toString());
        }
        Request request =  new Request.Builder()
                .url(serverUrl)
                .post(formBody.build())
                .tag(tag)
                .build();
        Call call = client.newCall(request);
        // 开启异步线程访问网络
        call.enqueue(mCallback);
    }

    /**
     * GET异步请求 GET方法需要用？将参数连接在URL后面，各个参数之间用&连接。
     * @param url utl
     * @param mCallback
     */
    public void get(String url, TreeMap<String,String> params, MgCallback mCallback) {

        String serverUrl="";
        if(ServerUrl.DEBUG){
            serverUrl = ServerUrl.SERVER_API_TEST_URL;
        }else{
            serverUrl = ServerUrl.SERVER_API_URL_MID;
        }
        //袁浩 测试地址
       // serverUrl = "http://192.168.2.41:8080/mgxz.BussRPC";
        if(!TextUtils.isEmpty(url)){
            serverUrl +=url;
        }
        if(serverUrl.endsWith("/")){
            serverUrl = serverUrl.substring(0,serverUrl.length()-1);
        }
        //添加公共参数
        params.putAll(commonParams());
        //加密所有的参数
        params = encryptParams(params);
        StringBuilder paramStr = new StringBuilder();
        for(Map.Entry<String,String> entry:params.entrySet()){
            paramStr.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        serverUrl = serverUrl+"?"+paramStr.substring(0,paramStr.length()-1);

        //创建一个Request
        final Request request = new Request.Builder()
                .url(serverUrl)
                .build();
        //new call
        Call call = client.newCall(request);
        //请求加入调度
        call.enqueue(mCallback);

    }

    /**
     * 删除队列的请求。
     * @param tag
     */
    public void cancelTag(Object tag) {
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }

    /**
     * 公共参数。
     *
     * @return
     */
    public TreeMap<String, String> commonParams() {
        TreeMap<String, String> params = new TreeMap<>();
        params.put(APP_KEY, APP_KEY_DEFAULT);
        params.put(APP_SECURITY, APP_SECURITY_DEFAULT);
        params.put(TIMESTAMP, String.valueOf((new Date()).getTime()));
        params.put(IMEI, App.getInstance().getImei());
        params.put(APP_TYPE, "2");
        return params;
    }


    private TreeMap<String, String> encryptParams(TreeMap<String, String> treeMap) {
        String signString = "";
        Iterator<String> sgIt = treeMap.keySet().iterator();

        while (sgIt.hasNext()) {
            String key = (String) sgIt.next();
            String value = (String) treeMap.get(key);
            if (key.equals("sign")) {
                continue;
            }
            if (signString.isEmpty()) {
                signString = key + ":" + value;
            } else {
                signString = signString + "|" + key + ":" + value;
            }
        }
//		System.err.println(signString.toLowerCase());
        String checkSignString = "";
        switch (ENCRYPT_TYPE) {
            case "MD5":
                checkSignString = MD5Util.MD5(signString.toLowerCase());
                break;

            default:
                checkSignString = MD5Util.MD5(signString.toLowerCase());
                break;
        }
        treeMap.put("sign", checkSignString);
        return treeMap;
    }

    public static String transMapToString(TreeMap map) {
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (java.util.Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("=").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
        }
        return sb.toString();
    }
}
