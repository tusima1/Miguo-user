package com.fanwe.common.update;

import com.lidroid.xutils.http.callback.HttpRedirectHandler;
import com.miguo.utils.MGLog;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Created by didik on 2016/10/10.
 * 升级下载apk文件,防止dns劫持
 */

public class NoHttpRedirectHandler implements HttpRedirectHandler {
    private String url;

    public NoHttpRedirectHandler(String url) {
        this.url=url;
    }

    public HttpRequestBase getDirectRequest(HttpResponse response) {
        if(response.containsHeader("Location")) {
            HttpGet request = new HttpGet(url);
            MGLog.e("upgrade","可恶,但是已经阻止了重定向!");
            if(response.containsHeader("Set-Cookie")) {
                String cookie = response.getFirstHeader("Set-Cookie").getValue();
                request.addHeader("Cookie", cookie);
            }
            return request;
        } else {
            return null;
        }
    }
}
