package com.miguo.live.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.fanwe.app.App;
import com.fanwe.customview.tab.ExtTabLayout;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.view.frag.GuidePagerFragment;
import com.miguo.model.GuideTags;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TestLiveActivity extends AppCompatActivity {

    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ExtTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_live);

//        FragmentManager supportFragmentManager = getSupportFragmentManager();
//        supportFragmentManager.beginTransaction().add(R.id.activity_test_live,new GuideLiveFragment()).commit();

        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (ExtTabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(ExtTabLayout.MODE_SCROLLABLE);
        requestTags();
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[]{"tab1","tab2","tab3","tab4","tab5","tab6","tab7"};
        private Context context;
        private List<GuidePagerFragment> pagers=new ArrayList<>();

        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            int size = pagers.size();
            if (size -1 < position){
                GuidePagerFragment guidePagerFragment = GuidePagerFragment.newInstance
                        (tabTitles[position]);
                pagers.add(guidePagerFragment);
                Log.e("test","new fragment :" +position);
            }
            return pagers.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    private void requestTags(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", "InterestingTab");
        OkHttpUtil.getInstance().get(params, new HttpCallback<GuideTags>() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                Log.e("test",response);
            }

            @Override
            public void onSuccessWithBean(GuideTags guideTags) {
                List<GuideTags.ResultBean.GuideTagInfo> body = guideTags.getResult().get(0)
                        .getBody();

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
