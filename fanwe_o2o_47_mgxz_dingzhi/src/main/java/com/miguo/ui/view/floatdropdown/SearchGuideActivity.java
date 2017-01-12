package com.miguo.ui.view.floatdropdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.floatdropdown.adapter.SearchGuideAdapter;
import com.miguo.ui.view.floatdropdown.decoration.SearchItemDecoration;
import com.miguo.ui.view.floatdropdown.interf.OnTextChangedListener;
import com.miguo.ui.view.floatdropdown.view.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchGuideActivity extends AppCompatActivity implements View.OnClickListener {

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
    private List<String> history;
    private SearchGuideAdapter hotAdapter;


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

        createData();

        searchView.setTextChangedListener(new OnTextChangedListener() {
            @Override
            public void onTextChanged(boolean isEmpty, CharSequence str) {
//                Toast.makeText(SearchGuideActivity.this, "isEmpty: "+ isEmpty +"    str: "+str, Toast.LENGTH_SHORT).show();
            }
        });

        initClick();

        initRV();
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
        hotAdapter = new SearchGuideAdapter(getMaxHotWords(hot));
        rv_hot.setAdapter(hotAdapter);


        rv_history.setLayoutManager(new GridLayoutManager(this,3, GridLayoutManager.VERTICAL,false));
        rv_history.addItemDecoration(new SearchItemDecoration(h,v));
        rv_history.setAdapter(new SearchGuideAdapter(history));
    }

    private void createData() {
        hot = new ArrayList<>();
        int num= 12;
        for (int i = 0; i < num; i++) {
            hot.add("萝卜排骨 "+i);
        }

        history = new ArrayList<>();
        int num2= 9;
        for (int i = 0; i < num2; i++) {
            history.add("我要杀鸡啊");
        }
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
        hotAdapter.setWords(getMaxHotWords(hot));
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

    }

    private void searchAction(){

    }
}
