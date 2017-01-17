package com.miguo.ui.view.floatdropdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.cache.CacheUtil;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.search.views.SearchResultActivity;
import com.miguo.entity.HotWordsBean;
import com.miguo.ui.view.floatdropdown.adapter.SearchGuideAdapter;
import com.miguo.ui.view.floatdropdown.decoration.SearchItemDecoration;
import com.miguo.ui.view.floatdropdown.interf.OnRvItemClickListener;
import com.miguo.ui.view.floatdropdown.interf.OnSearchActionListener;
import com.miguo.ui.view.floatdropdown.view.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class SearchGuideActivity extends AppCompatActivity implements View.OnClickListener, OnRvItemClickListener<String> {

    private SearchView searchView;
    private View ll_hot;
    private View ll_history;
    private View clear;
    private View shuffle;
    private View left;
    private View right;
    private RecyclerView rv_hot;
    private RecyclerView rv_history;

    private List<String> hot;
    private List<String> history;//用户的历史搜索数据
    private SearchGuideAdapter hotAdapter;
    private SearchGuideAdapter historyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seacher_guide);

        searchView = ((SearchView) findViewById(R.id.searchView));
        ll_hot = findViewById(R.id.ll_hot);
        ll_history = findViewById(R.id.ll_history);

        clear = findViewById(R.id.clear);
        shuffle = findViewById(R.id.shuffle);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        rv_hot = (RecyclerView)findViewById(R.id.rv_hot);
        rv_history = (RecyclerView)findViewById(R.id.rv_history);


        searchView.setSearchActionListener(new OnSearchActionListener() {
            @Override
            public void doSearch(String keyword) {
                searchAction();
            }
        });

        initClick();

        initRV();

        getHttpData();
    }

    private void initClick() {
        clear.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
    }

    private void initRV(){
        rv_hot.setLayoutManager(new GridLayoutManager(this,3, GridLayoutManager.VERTICAL,false));
        int h = DisplayUtil.dp2px(this, 6.5f);
        int v = DisplayUtil.dp2px(this, 15);
        rv_hot.addItemDecoration(new SearchItemDecoration(h,v));
        hotAdapter = new SearchGuideAdapter(null);
        rv_hot.setAdapter(hotAdapter);

        history = CacheUtil.getInstance().getUserSearchWord();

        rv_history.setLayoutManager(new GridLayoutManager(this,3, GridLayoutManager.VERTICAL,false));
        rv_history.addItemDecoration(new SearchItemDecoration(h,v));
        historyAdapter = new SearchGuideAdapter(history);
        rv_history.setAdapter(historyAdapter);
        if (history.size()<=0){
            ll_history.setVisibility(View.GONE);
            clear.setVisibility(View.GONE);
        }else {
            ll_history.setVisibility(View.VISIBLE);
            clear.setVisibility(View.VISIBLE);
        }

        hotAdapter.setOnRvItemClickListener(this);
        historyAdapter.setOnRvItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left:
                onBackPressed();
            break;
            case R.id.right:
                searchAction();
                break;
            case R.id.clear:
                clearHistory();
                break;
            case R.id.shuffle:
                shuffle();
                break;
        }
    }

    private void shuffle() {
        Collections.shuffle(hot);
        if (hot.size()>6){
            hotAdapter.setWords(getMaxHotWords(hot));
        }

    }

    private List<String> getMaxHotWords(List<String> origin){
        if (origin.size() <=6){
            return origin;
        }
        ArrayList<String> temp =new ArrayList<>();
        int max=6;
        for (int i = 0; i < max; i++) {
            temp.add(origin.get(i));
        }
        return temp;
    }

    private void clearHistory() {
        history.clear();
        updateHistoryAera();
        CacheUtil.getInstance().saveUserSearchWord(history);
    }

    private void searchAction(){
        String searchWord = searchView.getEditText();
        if (TextUtils.isEmpty(searchWord)){
            //do nothing
        }else {
            //跳转
            boolean same =false;
            for (String s : history) {
                if (searchWord.equalsIgnoreCase(s)){
                    same =true;
                    break;
                }
            }
            if (!same){
                if (history.size() == 9){
                    history.remove(8);
                }
                history.add(0,searchWord);
                updateHistoryAera();
                saveUserSearchData();
            }
            //TODO 跳转
            gotoSearchResultActivity(searchWord);
        }
    }

    private void gotoSearchResultActivity(String keyword){
        Intent intent = new Intent();
        intent.putExtra("keyword",keyword);
        intent.setClass(this, SearchResultActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateHistoryAera(){
        if (history.size()>0){
            ll_history.setVisibility(View.VISIBLE);
            clear.setVisibility(View.VISIBLE);
            historyAdapter.setWords(history);
        }else {
            ll_history.setVisibility(View.GONE);
            clear.setVisibility(View.GONE);
        }

    }

    private void saveUserSearchData() {
        CacheUtil.getInstance().saveUserSearchWord(history);
    }

    private void getHttpData(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", "GetHostList");
        params.put("city_id", "69e0405b-de8c-4247-8a0a-91ca45c4b30c");
        OkHttpUtil.getInstance().get(params, new HttpCallback<HotWordsBean>() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccessWithBean(HotWordsBean hotWordsBean) {
                super.onSuccessWithBean(hotWordsBean);
                String hotKeys = "" ;
                try {
                    hotKeys = hotWordsBean.getResult().get(0).getBody().getHotkey();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(hotKeys)){
                    Log.e("test","没有返回热门词汇!");
                    return;
                }
                ll_hot.setVisibility(View.VISIBLE);
                shuffle.setVisibility(View.VISIBLE);
                String[] words = hotKeys.split(",");
                hot = Arrays.asList(words);
                if (hot.size()>6){
                    hotAdapter.setWords(getMaxHotWords(hot));
                    shuffle.setVisibility(View.VISIBLE);
                }else {
                    hotAdapter.setWords(hot);
                    shuffle.setVisibility(View.GONE);
                }

                for (int i = 0; i < words.length; i++) {
                    Log.e("test","word: "+words[i]);
                }
            }
        });
    }

    @Override
    public void onRvItemClick(View view, int position, String s) {
        gotoSearchResultActivity(s);
    }
}
