package com.miguo.live.interf;

import android.view.View;

import com.miguo.live.model.getGiftInfo.GiftListBean;

/**
 * Created by didik on 2016/9/11.
 */
public interface GiftListener {
    void onItemSelected(int position, GiftListBean info);
    void onItemClickListener(View view, int position);
}
