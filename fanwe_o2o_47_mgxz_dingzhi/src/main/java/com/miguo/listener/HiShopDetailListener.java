package com.miguo.listener;

import android.content.Intent;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiShopDetailCategory;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.BaseUtils;

/**
 * Created by Administrator on 2016/10/21.
 */
public class HiShopDetailListener extends Listener {

    public HiShopDetailListener(Category category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call:
                clickCall();
                break;
            case R.id.location:
                clickLocation();
                break;
            case R.id.collect:
                clickCollect();
                break;
            case R.id.back:
            case R.id.back_bg:
                clickBack();
                break;
            case R.id.share:
            case R.id.share_bg:
            case R.id.tv_share:
                clickShare();
                break;
            case R.id.tv_represent:
                clickRepresent();
                break;
            case R.id.iv_represent:
                clickRepresentBtn();
                break;
            case R.id.tv_mine_shop:
                clickMineShopBtn();
                break;
            case R.id.offline_layout:
                clickOfflineLayout();
                break;
        }
    }

    private void clickOfflineLayout(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.OFFLINE_PAY));
        intent.putExtra(IntentKey.OFFLINE_SHOP_ID, getCategory().getMerchantID());
        intent.putExtra(IntentKey.OFFLINE_SHOP_NAME, getCategory().getResult().getShop_name());
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    private void clickMineShopBtn() {
        getCategory().clickMineShopBtn();
    }

    private void clickRepresent() {
        getCategory().clickRepresent();
    }

    private void clickRepresentBtn() {
        getCategory().clickRepresentBtn();
    }

    private void clickBack() {
        getCategory().clickBack();
    }

    private void clickShare() {
        getCategory().clickShare();
    }

    private void clickCall() {
        getCategory().clickCall();
    }

    private void clickLocation() {
        getCategory().clickLocation();
    }

    private void clickCollect() {
        getCategory().clickCollect();
    }

    @Override
    public HiShopDetailCategory getCategory() {
        return (HiShopDetailCategory) super.getCategory();
    }
}
