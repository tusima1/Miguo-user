package com.miguo.live.views.view;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/20.
 */
public class RTMPBaseActivity extends Fragment {
    private static final String TAG = RTMPBaseActivity.class.getSimpleName();
    static final int ACTIVITY_TYPE_PUBLISH      = 1;
    static final int ACTIVITY_TYPE_LIVE_PLAY    = 2;
    static final int ACTIVITY_TYPE_VOD_PLAY     = 3;


    public TextView mLogViewStatus;
    public TextView       mLogViewEvent;
    protected int         mActivityType;

    StringBuffer          mLogMsg = new StringBuffer("");
    private final int mLogMsgLenLimit = 3000;



    /**
     * 实现EVENT VIEW的滚动显示
     * @param scroll
     * @param inner
     */
    public static void scroll2Bottom(final ScrollView scroll, final View inner) {
        if (scroll == null || inner == null) {
            return;
        }
        int offset = inner.getMeasuredHeight() - scroll.getMeasuredHeight();
        if (offset < 0) {
            offset = 0;
        }
        scroll.scrollTo(0, offset);
    }


}

