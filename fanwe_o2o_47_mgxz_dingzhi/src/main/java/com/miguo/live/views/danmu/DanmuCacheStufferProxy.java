package com.miguo.live.views.danmu;

import android.text.SpannableStringBuilder;
import android.util.Log;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;

/**
 * Created by zlh on 2016/9/12.
 * 释放弹幕资源控制
 */
public class DanmuCacheStufferProxy extends BaseCacheStuffer.Proxy{

    String TAG = this.getClass().getSimpleName();

    @Override
    public void prepareDrawing(BaseDanmaku danmaku, boolean fromWorkerThread) {
    }



    @Override
    public void releaseResource(BaseDanmaku danmaku) {
        Log.d(TAG, "releaase resource: " + danmaku + " is danmu span: " + (danmaku.text instanceof SpannableStringBuilder));
        if(danmaku.text instanceof SpannableStringBuilder){
            ((SpannableStringBuilder)danmaku.text).clearSpans();
            danmaku.text = null;
        }
    }
}
