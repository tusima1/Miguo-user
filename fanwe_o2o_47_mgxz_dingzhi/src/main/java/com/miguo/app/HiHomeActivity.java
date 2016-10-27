package com.miguo.app;

import android.content.Intent;

import com.fanwe.model.CitylistModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiHomeCategory;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeActivity extends HiBaseActivity{

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hihome_activity);
    }

    @Override
    protected Category initCategory() {
        return new HiHomeCategory(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                /**
                 * 城市列表返回回调
                 */
                case RequestCode.RESUTN_CITY_ID:
                    handlerReturnCityId(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handlerReturnCityId(Intent data){
        CitylistModel model = (CitylistModel)data.getSerializableExtra(IntentKey.RETURN_CITY_DATA);
        showToastWithShortTime(model.getName());
        getCategory().updateFromCityChanged(model);
    }

    @Override
    protected HiHomeCategory getCategory() {
        return (HiHomeCategory) super.getCategory();
    }
}
