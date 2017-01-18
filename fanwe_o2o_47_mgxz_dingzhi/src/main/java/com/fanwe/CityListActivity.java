package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.customview.FlowLayout;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getCityList.ModelCityList;
import com.fanwe.utils.CharacterParser;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.ResultCode;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.BaseUtils;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 城市列表
 *
 * @author js02
 */
public class CityListActivity extends BaseActivity {

    @ViewInject(R.id.ll_location_city)
    private LinearLayout mLl_location_city;

    @ViewInject(R.id.ll_hot_city)
    private LinearLayout mLl_hot_city;

    @ViewInject(R.id.flow_hot_city)
    private FlowLayout mFlow_hot_city;

    @ViewInject(R.id.tv_location)
    private TextView mTv_location;

    @ViewInject(R.id.act_city_list_et_search)
    private ClearEditText mEtSearch;


    private List<ModelCityList> mListModel = new ArrayList<ModelCityList>();
    private List<ModelCityList> mListModelHotCity;
    private List<ModelCityList> mListFilterModel = new ArrayList<ModelCityList>();

    private boolean fromAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_city_list);
        init();
    }

    private void init() {
        initTitle();
        initViewState();
        bindDataFromDb();
        initCurrentLocation();
        registeEtSearchListener();
        registeClick();
        preData();
    }

    private void preData() {
        if (getIntent() != null) {
            fromAuth = getIntent().getBooleanExtra("fromAuth", false);
        }
    }

    /**
     * 热门城市
     */
    private void initViewState() {
        mListModelHotCity = AppRuntimeWorker.getCitylistHot();

        if (!SDCollectionUtil.isEmpty(mListModelHotCity)) {
            SDViewUtil.show(mLl_hot_city);
            mFlow_hot_city.removeAllViews();
            for (ModelCityList model : mListModelHotCity) {
                View cityView = createHotCityButton(model);
                if (cityView != null) {
                    mFlow_hot_city.addView(cityView);
                }
            }
        } else {
            SDViewUtil.hide(mLl_hot_city);
        }
    }

    /**
     * 创建热门城市
     */
    private TextView createHotCityButton(final ModelCityList model) {
        TextView btn = null;
        if (model != null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SDViewUtil.dp2px(50), SDViewUtil.dp2px(40));
            btn = new TextView(getApplicationContext());
            btn.setLayoutParams(params);
            btn.setGravity(Gravity.CENTER);
            btn.setText(model.getName());
            SDViewUtil.setTextSizeSp(btn, 13);
            btn.setTextColor(SDResourcesUtil.getColor(R.color.gray));
            btn.setBackgroundResource(R.drawable.selector_white_gray_stroke_all);
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!fromAuth) {
                        AppRuntimeWorker.setCityNameByModel(model);
                        setActivityResult(model);
                    } else {
                        SDEventManager.post(model, EnumEventTag.CITY_RESIDENT.ordinal());
                        finish();
                    }
                }
            });
        }
        return btn;
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
        mTv_location.setText("定位中");
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
            String dist = BaiduMapManager.getInstance().getDistrictShort();
            if (!TextUtils.isEmpty(AppRuntimeWorker.getCityIdByCityName(dist))) {
                mTv_location.setText(dist);
            } else {
                String city = BaiduMapManager.getInstance().getCityShort();
                mTv_location.setText(city);
            }
        } else {
            mTv_location.setText("定位失败，点击重试");
        }
    }

    /**
     * 搜索框
     */
    private void registeEtSearchListener() {
        mEtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });
    }

    /**
     * 数据过滤
     *
     * @param key
     */
    protected void filterData(String key) {
        mListFilterModel.clear();
        if (TextUtils.isEmpty(key)) {
            mListFilterModel.addAll(mListModel);
        } else {
            for (ModelCityList city : mListModel) {
                String name = city.getName();
                if (name.indexOf(key) != -1 || CharacterParser.convertChs2PinYin(name).startsWith(key)) {
                    mListFilterModel.add(city);
                }
            }
        }
    }


    /**
     * 从数据库取数据
     */
    private void bindDataFromDb() {
        List<ModelCityList> listDbModel = AppRuntimeWorker.getCitylist();
        if (listDbModel != null && listDbModel.size() > 0) {
            mListModel.addAll(listDbModel);
        } else {
            mListModel.clear();
        }
    }


    private void initTitle() {
        String title;
        String city = AppRuntimeWorker.getCity_name();
        if (TextUtils.isEmpty(city)) {
            title = "城市列表";
        } else {
            title = "当前城市 - " + city;
        }
        mTitle.setMiddleTextTop(title);
    }

    private void registeClick() {
        mLl_location_city.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaiduMapManager.getInstance().hasLocationSuccess()) {
                    String locationCity = mTv_location.getText().toString();
                    ModelCityList bean = AppRuntimeWorker.getCityByCityName(locationCity);
                    if (TextUtils.isEmpty(bean.getId())) {
                        MGToast.showToast("不支持当前城市:" + locationCity);
                    } else {
                        AppRuntimeWorker.setCityNameByModel(bean);
                        Set<String> tagSet = new LinkedHashSet<String>();
                        String sArray[] = {"city_" + bean.getId()};
                        for (String sTagItme : sArray) {
                            if (isEmpty(sTagItme)) {
                                return;
                            }
                            tagSet.add(sTagItme);
                        }
                        JPushInterface.setTags(getApplicationContext(), tagSet, new TagAliasCallback() {
                            @Override
                            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                            }
                        });
                        //缓存数据
                        if (!fromAuth) {
                            setActivityResult(bean);
                        } else {
                            SDEventManager.post(bean, EnumEventTag.CITY_RESIDENT.ordinal());
                            finish();
                        }
                    }
                } else {
                    locationCity();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public void setActivityResult(ModelCityList tempBean) {
        Intent intent = new Intent(this, ClassNameFactory.getClass(ClassPath.HOME_ACTIVITY));
        intent.putExtra(IntentKey.RETURN_CITY_DATA, tempBean);
        setResult(ResultCode.RESUTN_OK, intent);
        BaseUtils.finishActivity(this);
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case CITY_CHANGE:
                initTitle();
                break;

            default:
                break;
        }
    }

}