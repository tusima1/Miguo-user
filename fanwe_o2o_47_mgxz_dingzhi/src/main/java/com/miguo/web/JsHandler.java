package com.miguo.web;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by didik 
 * Created time 2016/12/19
 * Description: 
 */

public class JsHandler {

    @JavascriptInterface
    public void goTopic(String topic_id) {
//        Intent intent = new Intent(App.getApplication(), ClassNameFactory.getClass(ClassPath.SPECIAL_TOPIC_ACTIVITY));
//        intent.putExtra(IntentKey.SPECIAL_TOPIC_ID, topic_id);
//        startActivity(intent);
        Log.e("testjs","topic_id: "+topic_id);
    }

    @JavascriptInterface
    public void setTitle(String title) {
        Log.e("testjs","title: "+title);
    }
}
