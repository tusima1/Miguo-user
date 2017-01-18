package com.fanwe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import com.fanwe.library.customview.FlowLayout;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getCityList.ModelCityList;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.ResultCode;
import com.miguo.entity.CityGroupListBean;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.BaseUtils;

import java.util.List;
import java.util.TreeMap;

public class CityChooseActivity extends AppCompatActivity {
    private ImageView mLeftImageView;
    private TextView mCenterTitleText;
    private TextView mTvLocation;
    private LinearLayout mContent;
    private RecyclerView recyclerView;
    private int dp8;
    private boolean fromAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose);
        recyclerView = ((RecyclerView) findViewById(R.id.rv));
        init();

    }

    private void init() {
        dp8 = DisplayUtil.dp2px(this, 8);
        initView();
        initTitle();
        preData();
        requestCityInfo();
    }
    private void preData() {
        if (getIntent() != null) {
            fromAuth = getIntent().getBooleanExtra("fromAuth", false);
        }
    }

    private void requestCityInfo() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", "CityGroupList");
        OkHttpUtil.getInstance().get(params, new HttpCallback<CityGroupListBean>() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccessWithBean(CityGroupListBean cityGroupListBean) {
                List<CityGroupListBean.ResultBean.BodyBean> body =null;
                try {
                    body = cityGroupListBean.getResult()
                            .get(0).getBody();
                } catch (Exception e) {
                    Log.e("test","数据异常");
                }

                if (body == null)return;
                for (CityGroupListBean.ResultBean.BodyBean bodyBean : body) {
                    createFlowView(bodyBean.getGroup_name(),bodyBean.getGroup_item());
                }
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

    private void createFlowView(String areaTitle, List<CityGroupListBean.ResultBean.BodyBean.GroupItemBean> itemCityList){
        if (TextUtils.isEmpty(areaTitle) || itemCityList ==null || itemCityList.size()<=0){
            return;
        }
        mContent.addView(createTextView(areaTitle),getTextParams());

        FlowLayout flowLayout=new FlowLayout(this);
        flowLayout.setSpace(dp8,dp8);

        LinearLayout.LayoutParams flParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.setMargins(dp8,dp8,dp8,dp8);
        mContent.addView(flowLayout,flParams);

    }

    private TextView createFlowItem(CityGroupListBean.ResultBean.BodyBean.GroupItemBean item){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SDViewUtil.dp2px(50), SDViewUtil.dp2px(40));
        TextView textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setText(item.getName());
        textView.setTag(item);
        SDViewUtil.setTextSizeSp(textView, 13);
        textView.setTextColor(SDResourcesUtil.getColor(R.color.gray));
        textView.setBackgroundResource(R.drawable.selector_white_gray_stroke_all);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fromAuth) {
//                    AppRuntimeWorker.setCityNameByModel(model);
//                    setActivityResult(model);
                } else {
//                    SDEventManager.post(model, EnumEventTag.CITY_RESIDENT.ordinal());
//                    finish();
                }
            }
        });
        return null;
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

    public void setActivityResult(ModelCityList tempBean) {
        Intent intent = new Intent(this, ClassNameFactory.getClass(ClassPath.HOME_ACTIVITY));
        intent.putExtra(IntentKey.RETURN_CITY_DATA, tempBean);
        setResult(ResultCode.RESUTN_OK, intent);
        BaseUtils.finishActivity(this);
    }
}
