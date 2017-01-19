package com.fanwe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.EnumEventTag;
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
import com.miguo.framework.BaseCity;
import com.miguo.framework.CityManager;
import com.miguo.utils.BaseUtils;
import com.sunday.eventbus.SDEventManager;

import java.util.List;
import java.util.TreeMap;

public class CityChooseActivity extends AppCompatActivity {
    private ImageView mLeftImageView;
    private TextView mCenterTitleText;
    private TextView mTvLocation;
    private LinearLayout mContent;
    private int dp8;
    private boolean fromAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose);
        init();

    }

    private void init() {
        dp8 = DisplayUtil.dp2px(this, 8);
        initView();
        initTitle();
        initCurrentLocation();
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

        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    /**
     * 当前位置
     */
    private void initCurrentLocation() {
        if (!BaiduMapManager.getInstance().hasLocationSuccess()) {
            locationCity();
        } else {
            updateLocationTextView();
        }
    }
    protected void locationCity() {
        mTvLocation.setText("定位中...");
        BaiduMapManager.getInstance().startLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                updateLocationTextView();
                BaiduMapManager.getInstance().stopLocation();
            }
        });
    }
    private void updateLocationTextView() {
        if (BaiduMapManager.getInstance().hasLocationSuccess()) {
            String dist = BaiduMapManager.getInstance().getDistrict();
            String city = BaiduMapManager.getInstance().getCity();
            String address = BaiduMapManager.getInstance().getCurAddress();
            Log.e("test","dist: "+dist);
            if (!TextUtils.isEmpty(address)) {
                mTvLocation.setText("当前位置: "+address);
            } else {
                mTvLocation.setText("当前位置: "+city+dist);
            }
        } else {
            mTvLocation.setText("定位失败，点击重试");
        }
    }

    private void createFlowView(String areaTitle, List<CityGroupListBean.ResultBean.BodyBean.GroupItemBean> itemCityList){
        if (TextUtils.isEmpty(areaTitle) || itemCityList ==null || itemCityList.size()<=0){
            return;
        }
        mContent.addView(createTextView(areaTitle),getTextParams());

        FlowLayout flowLayout=new FlowLayout(this);
        flowLayout.setSpace(dp8,dp8);
        for (CityGroupListBean.ResultBean.BodyBean.GroupItemBean groupItemBean : itemCityList) {
            flowLayout.addView(createFlowItem(groupItemBean),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, SDViewUtil.dp2px(36)));
        }
        LinearLayout.LayoutParams flParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.setMargins(dp8,dp8,dp8,dp8);
        mContent.addView(flowLayout,flParams);
    }

    private TextView createFlowItem(CityGroupListBean.ResultBean.BodyBean.GroupItemBean item){
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(item.getName());
        textView.setTag(item);
        textView.setMinWidth(DisplayUtil.dp2px(this,56));
        int dp8 = DisplayUtil.dp2px(this, 8);
        textView.setPadding(dp8,0,dp8,0);
        SDViewUtil.setTextSizeSp(textView, 13);
        textView.setTextColor(SDResourcesUtil.getColor(R.color.gray));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            textView.setBackgroundResource(R.drawable.ripple_v21_test);
        }else {
            textView.setBackgroundResource(R.drawable.selector_city_choose_btn);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityGroupListBean.ResultBean.BodyBean.GroupItemBean city = (CityGroupListBean.ResultBean.BodyBean.GroupItemBean) v.getTag();
                if (city==null)return;
                CityManager.getInstance().saveCurrCity(new BaseCity(city.getName(),city.getId(),city.getUname()));
                ModelCityList modelCityList = new ModelCityList();
                modelCityList.setName(city.getName());
                modelCityList.setId(city.getId());
                modelCityList.setUname(city.getUname());
                if (!fromAuth) {
                    AppRuntimeWorker.setCityNameByModel(modelCityList);
                    setActivityResult(modelCityList);
                } else {
                    SDEventManager.post(modelCityList,EnumEventTag.CITY_RESIDENT.ordinal());
                    finish();
                }
            }
        });
        return textView;
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
