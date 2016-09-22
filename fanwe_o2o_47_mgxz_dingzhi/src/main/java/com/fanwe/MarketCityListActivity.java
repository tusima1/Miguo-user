package com.fanwe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SideBar;
import com.fanwe.customview.SideBar.OnTouchingLetterChangedListener;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.MarketFragment;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.customview.FlowLayout;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.CitylistModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.CharacterParser;
import com.fanwe.utils.PinyinComparator;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

/**
 * 城市列表
 *
 * @author js02
 */
public class MarketCityListActivity extends BaseActivity {

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

    @ViewInject(R.id.act_city_list_lv_citys)
    private ListView mLvCitys;

    @ViewInject(R.id.act_city_list_tv_touched_letter)
    private TextView mTvTouchedLetter;

    @ViewInject(R.id.act_city_list_sb_letters)
    private SideBar mSbLetters;


    private List<CitylistModel> mListModel = new ArrayList<CitylistModel>();
    private List<CitylistModel> mListModelHotCity;
    private List<CitylistModel> mListFilterModel = new ArrayList<CitylistModel>();


    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_city_list);
        init();
    }

    private void init() {
        initTitle();
        bindDefaultData();
        initViewState();
        bindDataFromDb();
        initSlideBar();
        initCurrentLocation();
        registeEtSearchListener();
        registeClick();
    }

    private void initViewState() {
        mListModelHotCity = AppRuntimeWorker.getCitylistHot();

        if (!SDCollectionUtil.isEmpty(mListModelHotCity)) {
            SDViewUtil.show(mLl_hot_city);
            mFlow_hot_city.removeAllViews();
            for (CitylistModel model : mListModelHotCity) {
                View cityView = createHotCityButton(model);
                if (cityView != null) {
                    mFlow_hot_city.addView(cityView);
                }
            }
        } else {
            SDViewUtil.hide(mLl_hot_city);
        }
    }

    private TextView createHotCityButton(final CitylistModel model) {
        TextView btn = null;
        if (model != null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    SDViewUtil.dp2px(50), SDViewUtil.dp2px(40));
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
                    // if (AppRuntimeWorker.setCity_name(model.getName()))
                    // {
                    // finish();
                    // } else
                    // {
                    // MGToast.showToast("设置失败");
                    // }
                    setResultCity(model.getName(), model.getId());
//					if (listener!=null) {
//						listener.onCityChange(model.getName(), model.getId());
//					}
                }
            });
        }
        return btn;
    }

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

    private void registeEtSearchListener() {
        mEtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });
    }

    protected void filterData(String key) {
        mListFilterModel.clear();
        if (TextUtils.isEmpty(key)) {
            mListFilterModel.addAll(mListModel);
        } else {
            for (CitylistModel city : mListModel) {
                String name = city.getName();
                if (name.indexOf(key) != -1
                        || CharacterParser.convertChs2PinYin(name).startsWith(
                        key)) {
                    mListFilterModel.add(city);
                }
            }
        }
        mAdapter.updateData(mListFilterModel);
    }

    private void initSlideBar() {
        mSbLetters.setTextView(mTvTouchedLetter);
        mSbLetters
                .setOnTouchingLetterChangedListener(new CityListActivity_OnTouchingLetterChangedListener());
    }

    private void bindDataFromDb() {
        List<CitylistModel> listDbModel = AppRuntimeWorker.getCitylist();
        if (listDbModel != null && listDbModel.size() > 0) {
            mListModel.addAll(listDbModel);
        } else {
            mListModel.clear();
        }
        mAdapter.updateData(mListModel);
    }

    private void bindDefaultData() {
        mAdapter = new MarketCityListAdapter(mListModel, this, 1);
        mLvCitys.setAdapter(mAdapter);
    }

    private void initTitle() {
        String title = null;
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
                    String cityId = AppRuntimeWorker
                            .getCityIdByCityName(locationCity);
                    if (TextUtils.isEmpty(cityId)) {
                        MGToast.showToast("不支持当前城市:" + locationCity);
                    } else {
                        setResultCity(locationCity, cityId);
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

    class CityListActivity_OnTouchingLetterChangedListener implements
            OnTouchingLetterChangedListener {
        @Override
        public void onTouchingLetterChanged(String s) {
            int position = mAdapter.getLettersAsciisFirstPosition(s.charAt(0));
            if (position != -1) {
                mLvCitys.setSelection(position);
            }
        }

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

    private void setResultCity(String city, String cityID) {
        data = new Intent();
        Bundle cityData = new Bundle();
        cityData.putString("city", city);
        cityData.putString("cityID", cityID);
        data.putExtras(cityData);
        AppRuntimeWorker.setCity_name(city);
        setResult(MarketFragment.CITY_RESULT, data);
        finish();
    }

    private MarketCityListAdapter mAdapter;

    private OnCityChangeListener listener;

    private class MarketCityListAdapter extends SDBaseAdapter<CitylistModel> {

        private int mTag;
        private Map<Integer, Integer> mMapLettersAsciisFirstPostion = new HashMap<Integer, Integer>();
        private PinyinComparator mComparator = new PinyinComparator();

        public MarketCityListAdapter(List<CitylistModel> listModel, Activity activity, int tag) {
            super(listModel, activity);
            this.mTag = tag;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_lv_citylist, null);
            }

            TextView tvLetter = ViewHolder.get(convertView, R.id.item_lv_citylist_tv_letter);
            TextView tvName = ViewHolder.get(convertView, R.id.item_lv_citylist_tv_city_name);
            LinearLayout ll_content = ViewHolder.get(convertView, R.id.ll_content);

            final CitylistModel model = getItem(position);
            if (model != null) {
                // 根据position获取分类的首字母的Char ascii值
                int modelFirstLettersAscii = getModelFirstLettersAscii(model);

                // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if (position == getLettersAsciisFirstPosition(modelFirstLettersAscii)) {
                    SDViewUtil.show(tvLetter);
                    tvLetter.setText(model.getSortLetters());
                } else {
                    SDViewUtil.hide(tvLetter);
                }

                ll_content.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//						if(mTag==1){
//							if (AppRuntimeWorker.setCity_name(model.getName()))
//							{
//								mActivity.finish();
//							} else
//							{
//								MGToast.showToast("设置城市失败");
//							}
//						}else if(mTag==2)
//						{
//							Intent intent = new Intent();
//							intent.putExtra("city", model.getName());
//							mActivity.setResult(HoltelSearchCityActivity.result_Code, intent);
//							mActivity.finish();
//						}
                        setResultCity(model.getName(), model.getId());
//						if (listener!=null) {
//							listener.onCityChange(model.getName(), model.getId());
//						}
                    }
                });

                SDViewBinder.setTextView(tvName, model.getName());
            }

            return convertView;
        }

        public int getModelFirstLettersAscii(CitylistModel model) {
            if (model != null) {
                String letters = model.getSortLetters();
                if (!TextUtils.isEmpty(letters) && letters.length() > 0) {
                    return letters.charAt(0);
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }

        public int getLettersAsciisFirstPosition(int lettersAscii) {
            Integer position = mMapLettersAsciisFirstPostion.get(lettersAscii);
            if (position == null) {
                boolean isFound = false;
                for (int i = 0; i < mListModel.size(); i++) {
                    String sortStr = mListModel.get(i).getSortLetters();
                    char firstChar = sortStr.toUpperCase().charAt(0);
                    if (firstChar == lettersAscii) {
                        isFound = true;
                        position = i;
                        mMapLettersAsciisFirstPostion.put(lettersAscii, position);
                        break;
                    }
                }
                if (!isFound) {
                    position = -1;
                }
            }
            return position;
        }

        @Override
        public void notifyDataSetChanged() {
            if (mListModel != null) {
                Collections.sort(mListModel, mComparator);
            }
            super.notifyDataSetChanged();
        }

    }

    interface OnCityChangeListener {
        void onCityChange(String city, int cityID);
    }

    public void setOnCityChangeListener(OnCityChangeListener listener) {
        this.listener = listener;
    }

}