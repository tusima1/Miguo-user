package com.miguo.ui.view;

import android.graphics.Color;
import android.support.v4.view.LoopViewPager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RelativeLayout;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseRelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/12/20.
 */
public class AutoBanner extends BaseRelativeLayout implements ViewPager.OnPageChangeListener{

    /**
     * 轮播间隔时间
     */
    private final static long LOOP_DURATION = 3000;
    private final static long IGNORE_PEROID = 5000;
    private boolean isAuto = true;
    private boolean isPause;
    private boolean autoPaging;
    private long touchTime;
    private int barHight;

    private LoopViewPager pager;
    private LinearLayout indicatorBar;
    private int curIdx;
    private Timer timer;

    public AutoBanner(Context context) {
        this(context, null);
    }

    public AutoBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        initView();
    }

    public void setAuto(final boolean auto) {
        isAuto = auto;
    }

    public void pauseAuto() {
        isPause = true;
    }

    public void resumeAuto() {
        isPause = false;
        if (isAuto) {
            startTimer();
        }
    }

    private void initView() {
        barHight = dip2px(8);
        pager = new LoopViewPager(getContext());
        pager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        pager.setId(0x123456);
        pager.setOnPageChangeListener(this);
        addView(pager);

        indicatorBar = new LinearLayout(getContext());
        indicatorBar.setFocusable(false);
        indicatorBar.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams barParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, wrapContent());
        barParams.addRule(RelativeLayout.ALIGN_BOTTOM, pager.getId());
        barParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        indicatorBar.setLayoutParams(barParams);
        indicatorBar.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(indicatorBar);
    }

    public void hidePoint(){
        indicatorBar.setVisibility(View.GONE);
    }

    private void startTimer() {
        if (null != timer && isAuto && !isPause) {
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPause) {
                    pager.post(new Runnable() {
                        @Override
                        public void run() {
                            nextPage();
                        }
                    });
                } else {
                    timer.cancel();
                    timer = null;
                }
            }
        }, LOOP_DURATION, IGNORE_PEROID);
    }

    private void nextPage() {
        if (System.currentTimeMillis() - touchTime > IGNORE_PEROID) {
            autoPaging = true;
            pager.setCurrentItem(pager.getCurrentItem() == pager.getAdapter().getCount() - 1 ? 0 : pager.getCurrentItem() + 1);
        }
    }

    public void setAdapter(final PagerAdapter adapter) {
        if (null != adapter) {
            adapter.registerDataSetObserver(new DataObserver());
            changeCount(adapter.getCount());
        } else {
            changeCount(0);
        }
        pager.setAdapter(adapter);
    }

    private void changeCount(final int count) {
        indicatorBar.removeAllViews();
        for(int i = 0; i<count; i++){
            indicatorBar.addView(makePoint());
        }

        if (curIdx >= count) {
            curIdx = count - 1;
        }
        if (count > 0) {
            setPointSelected(curIdx, true);
            resumeAuto();
        }else {
            pauseAuto();
        }
    }

    private View makePoint() {
        ImageView btn = new ImageView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(barHight, barHight);
        params.setMargins(dip2px(3), 0, dip2px(3), dip2px(10));
        btn.setLayoutParams(params);
        btn.setScaleType(ImageView.ScaleType.CENTER_CROP);
        btn.setImageResource(R.drawable.indicator_point);
        return btn;
    }

    private void setPointSelected(int i, boolean selected) {
        try{
            indicatorBar.getChildAt(i).setSelected(selected);
        }catch (Exception e){

        }
    }

    private class DataObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            changeCount(pager.getAdapter().getCount());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int size = indicatorBar.getChildCount();
        int relPos = position % size;
        while (relPos < 0) {
            relPos = size + relPos;
        }
        setPointSelected(curIdx, false);
        curIdx = relPos;
        setPointSelected(curIdx, true);
        if (autoPaging) {
            autoPaging = false;
        } else {
            touchTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
