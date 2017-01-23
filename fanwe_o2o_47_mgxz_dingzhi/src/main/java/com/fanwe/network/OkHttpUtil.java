package com.fanwe.network;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;
import com.miguo.utils.NetWorkStateUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by didik on 2016/11/10.
 */

public class OkHttpUtil {
    private volatile static OkHttpUtil mInstance;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final int GET = 0;
    private static final int POST = 1;
    private static final int PUT = 2;
    private static final int DELETE = 3;
    private static final int THIRD_GET = 4;

    private static String TAG = "StringRequestUrl";
    private static final String APP_KEY = "app_key";
    private static final String APP_SECURITY = "app_security";
    private static final String TIMESTAMP = "timestamp";
    private static final String IMEI = "imei";
    private static final String APP_TYPE = "app_type";
    private static final String APP_KEY_DEFAULT = "c3e67013-e439-11e5-bbcc-a0d3c1ef5680";
    private static final String APP_SECURITY_DEFAULT = "2acabf04914eeaec6b841a81f09711d8";
    //    java接口增加公共参数
    private static final String CITY_ID="city_id";
    /**
     * longitude经度
     */
    private static final String LONGITUDE="longitude";
    /**
     * latitude纬度
     */
    private static final String LATITUDE="latitude";
    /**
     * app_version终端的版本号
     */
    private static final String APP_VERSION="app_version";
    /**
     * 加密方式 。
     */
    private static final String ENCRYPT_TYPE = "MD5";
    //"http://192.168.2.43:9080/mgxz.AuthorRPC/";
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/x-www-form-urlencoded");


    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    public static OkHttpUtil getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtil();
        }
        return mInstance;
    }

    public void get(TreeMap<String, String> params, HttpCallback mCallback) {
        get(params, mCallback, false);
    }

    public void get(TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin) {
        get(null, params, mCallback, isNeedLogin);
    }

    public void get(String url, TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin) {
        get(url, params, mCallback, isNeedLogin, null);
    }

    //TODO for oldVersion
    public void get(String url, TreeMap<String, String> params, HttpCallback mCallback) {
        get(url, params, mCallback, false, null);
    }

    public void get(String url, TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin, Object tag) {
        httpHandle(GET, url, params, mCallback, isNeedLogin, tag);
    }

    public void put(TreeMap<String, String> params, HttpCallback mCallback) {
        put(params, mCallback, false);
    }

    public void put(TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin) {
        put(null, params, mCallback, isNeedLogin);
    }

    public void put(String url, TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin) {
        put(url, params, mCallback, isNeedLogin, null);
    }

    //TODO for oldVersion
    public void put(String url, TreeMap<String, String> params, HttpCallback mCallback) {
        put(url, params, mCallback, false, null);
    }

    public void put(String url, TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin, Object tag) {
        httpHandle(PUT, url, params, mCallback, isNeedLogin, tag);
    }

    public void post(TreeMap<String, String> params, HttpCallback mCallback) {
        post(params, mCallback, false);
    }

    public void post(TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin) {
        post(null, params, mCallback, isNeedLogin);
    }

    public void post(String url, TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin) {
        post(url, params, mCallback, isNeedLogin, null);
    }

    //TODO for oldVersion
    public void post(String url, TreeMap<String, String> params, HttpCallback mCallback) {
        post(url, params, mCallback, false, null);
    }

    public void post(String url, TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin, Object tag) {
        httpHandle(POST, url, params, mCallback, isNeedLogin, tag);
    }

    public void delete(TreeMap<String, String> params, HttpCallback mCallback) {
        delete(params, mCallback, false);
    }

    public void delete(TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin) {
        delete(null, params, mCallback, isNeedLogin);
    }

    public void delete(String url, TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin) {
        delete(url, params, mCallback, isNeedLogin, null);
    }

    //TODO for oldVersion
    public void delete(String url, TreeMap<String, String> params, HttpCallback mCallback) {
        delete(url, params, mCallback, false, null);
    }

    public void delete(String url, TreeMap<String, String> params, HttpCallback mCallback, boolean isNeedLogin, Object tag) {
        httpHandle(DELETE, url, params, mCallback, isNeedLogin, tag);
    }

    public void thirdUrlGet(String url, TreeMap<String, String> params, HttpCallback mCallback) {
        thirdUrlGet(url, params, mCallback, null);
    }

    public void thirdUrlGet(String url, TreeMap<String, String> params, HttpCallback mCallback, Object tag) {
        httpHandle(THIRD_GET, url, params, mCallback, false, tag);
    }

    private void httpHandle(int method, String url, TreeMap<String, String> params, final HttpCallback callback, boolean isNeedLogin, Object tag) {
        if (NetWorkStateUtil.isConnected(App.getInstance())) {
            if (isNeedLogin) {
                String token = App.getInstance().getToken();
                if (TextUtils.isEmpty(token)) {
                }
            }
            String serverUrl = doCommonURL(url);
            TreeMap<String, String> finalParams = doCommonParams(params);
            switch (method) {
                case GET:
                    if (serverUrl.endsWith("/")) {
                        serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
                    }
                    handleHttpGet(serverUrl, finalParams, callback, tag);
                    break;
                case POST:
                    handleHttpPost(serverUrl, finalParams, callback, tag);
                    break;
                case PUT:
                    handleHttpPut(serverUrl, finalParams, callback, tag);
                    break;
                case DELETE:
                    handleHttpDelete(serverUrl, finalParams, callback, tag);
                    break;
                case THIRD_GET:
                    handleHttpThirdUrlGet(url, params, callback, tag);
                    break;
            }
        } else {
            if (callback != null) {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(null,new IOException());
                        callback.onFinish();
                    }
                });
            }
            MGToast.showToast("没有网络,请检测网络环境!");
        }
    }

    /**
     * 用从第三方的URL 取值。
     *
     * @param url
     * @param params
     * @param mCallback
     */
    private void handleHttpThirdUrlGet(String url, TreeMap<String, String> params, Callback mCallback, Object tag) {
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                paramStr.append(entry.getKey() + "=" + "" + "&");
            } else {
                try {
                    paramStr.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        url = url + "?" + paramStr.substring(0, paramStr.length() - 1);
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        Call call = client.newCall(request);
        //请求加入调度
        call.enqueue(mCallback);
    }

    private void handleHttpDelete(String url, TreeMap<String, String> params, Callback mCallback, Object tag) {
        FormBody.Builder build = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue())) {
                try {
                    build.add(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                build.add(entry.getKey(), "");
            }
        }

        RequestBody requestBodyPut = build.build();
        Request requestPut = new Request.Builder()
                .url(url)
                .delete(requestBodyPut)
                .build();
        client.newCall(requestPut).enqueue(mCallback);
    }

    private void handleHttpPost(String url, TreeMap<String, String> params, Callback mCallback, Object tag) {
        StringBuilder requestStr = new StringBuilder("");
        FormBody.Builder build = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue())) {
                try {
                    requestStr.append("key:" + entry.getKey() + "  value:" + entry.getValue());
                    build.add(entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                build.add(entry.getKey(), "");
            }
        }
        RequestBody requestBodyPost = build.build();
        Request requestPost = new Request.Builder()
                .url(url)
                .post(requestBodyPost)
                .build();
        if (ServerUrl.DEBUG) {
            String method = params.get("method");
            Log.e(TAG, "method :+=" + method + " post:" + requestStr.toString());
        }
        client.newCall(requestPost).enqueue(mCallback);
    }

    private void handleHttpPut(String url, TreeMap<String, String> params, Callback mCallback, Object tag) {
        StringBuilder requestStr = new StringBuilder("");
        FormBody.Builder build = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue())) {
                try {
                    requestStr.append("key:" + entry.getKey() + "  value:" + entry.getValue());
                    build.add(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                build.add(entry.getKey(), "");
            }
        }
        RequestBody requestBodyPut = build.build();
        Request requestPut = new Request.Builder()
                .url(url)
                .put(requestBodyPut)
                .build();
        if (ServerUrl.DEBUG) {
            String method = params.get("method");
            Log.e(TAG, "method :+=" + method + " put:" + requestStr.toString());
        }
        client.newCall(requestPut).enqueue(mCallback);
    }

    private void handleHttpGet(String url, TreeMap<String, String> params, Callback callback, Object tag) {
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                paramStr.append(entry.getKey() + "=" + "" + "&");
            } else {
                try {
                    paramStr.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        url = url + "?" + paramStr.substring(0, paramStr.length() - 1);
        if (ServerUrl.DEBUG) {
            String method = params.get("method");
            Log.e(TAG, "method :+=" + method + " get:" + url);

        }
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        Call call = client.newCall(request);
        //请求加入调度
        call.enqueue(callback);
    }

    private String doCommonURL(String url) {
        String serverUrl = ServerUrl.getAppServerApiUrl();
        if (!TextUtils.isEmpty(url)) {
            serverUrl += url;
        }
        return serverUrl;
    }

    private TreeMap<String, String> doCommonParams(TreeMap<String, String> params) {
        //添加公共参数
        params.putAll(commonParams());
        //加密所有的参数
        params = encryptParams(params);
        return params;
    }

    /**
     * 同步请求POST。
     *
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

    /**
     * 删除队列的请求。
     *
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
        params.put(CITY_ID, AppRuntimeWorker.getCity_id());
        params.put(LONGITUDE, BaiduMapManager.getInstance().getLongitude()+"");
        params.put(LATITUDE,BaiduMapManager.getInstance().getLatitude()+"");
        String versionName = SDPackageUtil.getVersionName();
        if(TextUtils.isEmpty(versionName)){
            versionName = SDPackageUtil.getCurrentPackageInfo().versionName;
        }
        params.put(APP_VERSION, versionName);
//        TreeMap<String, String> params = new TreeMap<>();
//        params.put(APP_KEY, APP_KEY_DEFAULT);
//        params.put(APP_SECURITY, APP_SECURITY_DEFAULT);
//        params.put(TIMESTAMP, String.valueOf((new Date()).getTime()));
//        params.put(IMEI, App.getApplication().getImei());
//        params.put(APP_TYPE, "2");
        return params;
    }


    private TreeMap<String, String> encryptParams(TreeMap<String, String> treeMap) {
        String signString = "";
        Iterator<String> sgIt = treeMap.keySet().iterator();

        while (sgIt.hasNext()) {
            String key = (String) sgIt.next();
            String value = (String) treeMap.get(key);
            if (value == null) {
                value = "";
            }
            if (key.equals("sign")) {
                continue;
            }
            if (signString.isEmpty()) {
                signString = key + ":" + value;
            } else {
                signString = signString + "|" + key + ":" + value;
            }
        }
        String checkSignString;
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
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("=").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
        }
        return sb.toString();
    }

    public OkHttpClient getClient() {
        return client;
    }


}
