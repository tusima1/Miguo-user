package com.fanwe.user.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.adapters.WelcomePagerAdapter;
import com.fanwe.user.view.customviews.WelcomeView;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {

    List<WelcomeView> mWelList=new ArrayList<>();

    private ViewPager mViewpager;
//    private CircleIndicator mCIndictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initFirstWelcomeViewPager();
    }
    private void initFirstWelcomeViewPager() {
        mViewpager = ((ViewPager) findViewById(R.id.viewpager));
//        mCIndictor = ((CircleIndicator) findViewById(indicator));

        //add image
        TypedArray typedArray = getResources().obtainTypedArray(R.array.first_welcome);
        int indexCount = typedArray.length();
        for (int i = 0; i < indexCount; i++) {
            WelcomeView view=new WelcomeView(this);
            view.setImageRes(typedArray.getResourceId(i,0));
            if (i==indexCount-1){
                view.setNextVisiable();
                view.setOnNextClickListener(new WelcomeView.OnNextClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoMainActivity();
                    }
                });
            }
            view.setOnSkipClickListener(new WelcomeView.OnSkipClickListener() {
                @Override
                public void onClick(View v) {
                    gotoMainActivity();
                }
            });
            mWelList.add(view);
        }
        WelcomePagerAdapter welcomePagerAdapter=new WelcomePagerAdapter();
        welcomePagerAdapter.setData(mWelList);

        mViewpager.setAdapter(welcomePagerAdapter);
//        mCIndictor.setViewPager(mViewpager);
//        welcomePagerAdapter.registerDataSetObserver(mCIndictor.getDataSetObserver());
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, ClassNameFactory.getClass(ClassPath.HOME_ACTIVITY));
        startActivity(intent);
        finish();
    }
}
