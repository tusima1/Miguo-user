package com.fanwe.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.CityListActivity;
import com.fanwe.dao.CurrCityModelDao;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.CitylistModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.PinyinComparator;
import com.fanwe.work.AppRuntimeWorker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityListAdapter extends SDBaseAdapter<CitylistModel> {

    private int mTag;
    private Map<Integer, Integer> mMapLettersAsciisFirstPostion = new HashMap<Integer, Integer>();
    private PinyinComparator mComparator = new PinyinComparator();

    public CityListAdapter(List<CitylistModel> listModel, Activity activity, int tag) {
        super(listModel, activity);
        this.mTag = tag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_lv_citylist, null);
        }

        TextView tvLetter = ViewHolder.get(R.id.item_lv_citylist_tv_letter, convertView);
        TextView tvName = ViewHolder.get(R.id.item_lv_citylist_tv_city_name, convertView);
        LinearLayout ll_content = ViewHolder.get(R.id.ll_content, convertView);

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
                    if (mTag == 1) {
                        if (AppRuntimeWorker.setCity_name(model.getName())) {
                            //缓存数据
                            CurrCityModelDao.insertModel(model);
//                            mActivity.setResult(8888);
//                            mActivity.finish();
                            getActivity().setActivityResult(model);
                        } else {
                            MGToast.showToast("设置城市失败");
                        }
                    } else if (mTag == 2) {
//                        mActivity.setResult(8888);
//                        mActivity.finish();
                        getActivity().setActivityResult(model);
                    }
                }
            });

            SDViewBinder.setTextView(tvName, model.getName());
        }

        return convertView;
    }

    public CityListActivity getActivity(){
        return (CityListActivity)mActivity;
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
