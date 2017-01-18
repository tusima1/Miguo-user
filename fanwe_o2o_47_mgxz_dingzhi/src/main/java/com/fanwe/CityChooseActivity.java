package com.fanwe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.app.App;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.test.HorAdapter;
import com.miguo.ui.view.floatdropdown.interf.OnRvItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CityChooseActivity extends AppCompatActivity {
    private ImageView mLeftImageView;
    private TextView mCenterTitleText;
    private TextView mTvLocation;
    private LinearLayout mContent;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose);
        recyclerView = ((RecyclerView) findViewById(R.id.rv));
        init();

    }

    private void init() {
        initView();
        initTitle();
        initRv();
        requestCityInfo();
    }

    private void initRv() {
        List<String> words= new ArrayList<>();
        words.add("米果小站");
        words.add("你好");
        words.add("深圳");
        words.add("江西");
        words.add("湖南");
        words.add("乌鲁木齐");
        words.add("赣州");
        words.add("不想编了");
        words.add("厉害");
        words.add("哈哈哈");
        words.add("米果小站");
        words.add("你好");
        words.add("湖南");
        words.add("乌鲁木齐");
        words.add("赣州");
        words.add("不想编了");
        words.add("哈哈哈");
        words.add("你好");
        words.add("深圳");
        words.add("湖南");
        words.add("乌鲁木齐");
        words.add("赣州");
        words.add("不想编了");
        words.add("厉害");
        words.add("哈哈哈");

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL));
        HorAdapter horAdapter=new HorAdapter(words);
        horAdapter.setOnRvItemClickListener(new OnRvItemClickListener<String>() {
            @Override
            public void onRvItemClick(View view, int position, String s) {
                MGToast.showToast(s);
            }
        });
        recyclerView.setAdapter(horAdapter);
    }

    private void requestCityInfo() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", "CityGroupList");
        OkHttpUtil.getInstance().get(params, new HttpCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                Log.e("test","city: "+response);
            }
        });
    }

    private void initTitle() {
        String title;
        String city = AppRuntimeWorker.getCity_name();
        if (TextUtils.isEmpty(city)) {
            title = "城市列表";
        } else {
            title = "当前城市 - " + city;
        }
        mCenterTitleText.setText(title);
    }

    private void initView() {
        mLeftImageView = (ImageView) findViewById(R.id.left);
        mCenterTitleText = (TextView) findViewById(R.id.tv_title);

        mTvLocation = (TextView) findViewById(R.id.tv_show);
        mContent = (LinearLayout) findViewById(R.id.activity_city_choose);
    }

    private void createAreaView(String areaTitle, List itemCityList){
        if (TextUtils.isEmpty(areaTitle) || itemCityList ==null || itemCityList.size()<=0){
            return;
        }
        mContent.addView(createTextView(areaTitle),getTextParams());
//        mContent.addView();
    }

    private RecyclerView createRV(){
        RecyclerView itemRv=new RecyclerView(this);
//        itemRv.setLayoutManager(new GridLayoutManager(this,));
        return itemRv;
    }

    private TextView createTextView(String text){
        TextView itemTitle = new TextView(this);
        itemTitle.setLines(1);
        itemTitle.setTextSize(14);
        itemTitle.setText(text);
        itemTitle.setEllipsize(TextUtils.TruncateAt.END);
        itemTitle.setTextColor(Color.parseColor("#898989"));
        itemTitle.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        itemTitle.setPadding(DisplayUtil.dp2px(this,8),0,0,0);
        return itemTitle;
    }

    private LinearLayout.LayoutParams getTextParams(){
        LinearLayout.LayoutParams textParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(this,36));
        return textParams;
    }
}
