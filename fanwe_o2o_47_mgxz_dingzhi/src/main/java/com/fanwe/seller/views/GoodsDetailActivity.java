package com.fanwe.seller.views;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.customview.SScrollView;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.GoodsDetailPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailActivity extends Activity {

    private ViewPager mViewpager;
    private CircleIndicator mCirIndictor;
    private ImageView mIvTitleLeft;
    private ImageView mIvTitleShare;//标题分享
    private ImageView mIvTitleCollect;//标题收藏
    private TextView mTvTitleMiddle;//标题文字
    private SScrollView mSScrollView;
    private View mVGTitle;
    private View mFlViewpager;

    private int mTitleHeight;
    private int mFLViewpagerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_detail);
        init();
    }

    private void init() {
        initTitleLayout();
        initSScrollView();
    }

    private void initTitleLayout() {
        mIvTitleLeft = ((ImageView) findViewById(R.id.iv_left));
        mIvTitleShare = ((ImageView) findViewById(R.id.iv_share));
        mIvTitleCollect = ((ImageView) findViewById(R.id.iv_collect));
        mTvTitleMiddle = ((TextView) findViewById(R.id.tv_middle));

        mVGTitle = findViewById(R.id.fr_top_title);
    }

    private void initSScrollView() {
        initTopLayout();


        mSScrollView = ((SScrollView) findViewById(R.id.scrollView));
        mFlViewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mFlViewpager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mTitleHeight = mVGTitle.getHeight(); //height is ready
                mFLViewpagerHeight = mFlViewpager.getHeight();
                Log.e("test", "height:" + mTitleHeight + "----" + mFLViewpagerHeight);
                setTitleAction();
            }
        });


    }

    private void setTitleAction() {
        if (mFLViewpagerHeight <= 0) {
            return;
        }
        mSScrollView.setOnScrollListener(new SScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                Log.e("test", "y: " + y);
                Log.e("test", "oldY: " + oldY);
                double fff = y * 1.0 / mFLViewpagerHeight;
                Log.e("test","fff:"+fff);
                if (fff >= 0 && fff <= 1) {
                    int percent=(int) (fff * 100);
                    String alphaWhite;
                    if (fff *100<10){
                        alphaWhite="00";
                    }else {
                        alphaWhite= getAlphaWhite(percent);
                    }
                    int alphaWhiteColor = Color.parseColor("#"+alphaWhite + "FFFFFF");
                    mVGTitle.setBackgroundColor(alphaWhiteColor);
                    Log.e("test","percent:"+percent);
//                    mVGTitle.getBackground().setAlpha((int) fff);
                } else {
                    Log.e("test", "fff:" + fff + "----" + fff * 100 + "%");
                }

            }
        });
    }

    private String getAlphaWhite(int percent) {
        if (percent >= 100) {
            percent = 100;
        }
        if (percent <= 0) {
            percent = 0;
        }
        float temp = 255 * percent * 1.0f / 100f;
        int round = Math.round(temp);//四舍五入
        String hexString = Integer.toHexString(round);
        if (hexString.length() < 2) {
            hexString += "0";
        }
        return hexString;
    }

    private void initTopLayout() {
        mViewpager = ((ViewPager) findViewById(R.id.viewpager));

        mCirIndictor = ((CircleIndicator) findViewById(R.id.indicator_circle));

        mFlViewpager = findViewById(R.id.fl_viewpager);

        GoodsDetailPagerAdapter goodsDetailPagerAdapter = new GoodsDetailPagerAdapter(getTestData
                ());

        mViewpager.setAdapter(goodsDetailPagerAdapter);

        mCirIndictor.setViewPager(mViewpager);

        goodsDetailPagerAdapter.registerDataSetObserver(mCirIndictor.getDataSetObserver());


    }

    private List<ImageView> getTestData() {

        List<ImageView> data = new ArrayList<>();

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.v1);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        data.add(imageView);
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.v2);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        data.add(imageView2);
        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.v3);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
        data.add(imageView3);
        ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.drawable.v4);
        imageView4.setScaleType(ImageView.ScaleType.FIT_XY);
        data.add(imageView4);
        ImageView imageView5 = new ImageView(this);
        imageView5.setImageResource(R.drawable.v5);
        imageView5.setScaleType(ImageView.ScaleType.FIT_XY);
        data.add(imageView5);

        return data;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mVGTitle.getBackground().setAlpha(0);
    }
}
