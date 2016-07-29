package com.fanwe.seller.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;

/**
 * 我代言的店铺
 * Created by Administrator on 2016/7/29.
 */
public class FragmentMineShopList extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            initView(inflater, container);
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从mView删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = (View) inflater.inflate(R.layout.fragment_mine_shop_list, container, false);

    }
}
