package com.miguo.ui.view.notify;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseRelativeLayout;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/14.
 */
public class NotifyContentView extends BaseRelativeLayout {

    private static final int TITLE_ID = 0x000010;
    private static final int TIME_ID = 0x000020;

    TextView mTitle;

    TextView mTime;

    TextView mContent;

    public NotifyContentView(Context context) {
        super(context);
    }

    public NotifyContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        initTitle();
        initTime();
    }

    private void initTitle(){
        mTitle = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(dip2px(17), dip2px(10), 0, 0);
        mTitle.setLayoutParams(params);
        mTitle.setTextSize(14);
        mTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.c_9B9B9B));
        mTitle.setText("佣金通知");
        mTitle.setId(TITLE_ID);
        addView(mTime);
    }

    private void initTime(){
        mTime = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dip2px(10), dip2px(17), 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mTime.setLayoutParams(params);
        mTime.setTextSize(14);
        mTime.setTextColor(ContextCompat.getColor(getContext(), R.color.c_595959));
        mTime.setText("12:00");
        mTime.setId(TIME_ID);
        addView(mTime);
    }

    private void initContent(){
        mTime = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dip2px(10), dip2px(17), 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mTime.setLayoutParams(params);
        mTime.setTextSize(14);
        mTime.setTextColor(ContextCompat.getColor(getContext(), R.color.c_595959));
        mTime.setText("12:00");
        mTime.setId(TIME_ID);
        addView(mTime);
    }

}
