package com.miguo.groupon.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.base.CallbackView;
import com.fanwe.o2o.miguo.R;

import java.util.List;

/**
 * 团购列表
 * Created by qiang.chen on 2017/1/5.
 */

public class FragmentGrouponList extends Fragment implements CallbackView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_groupon_list, container, false);
        setView();
        setListener();
        return view;
    }

    private void setListener() {

    }

    private void setView() {

    }

    /**
     * 刷新数据
     */
    public void refresh() {
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}
