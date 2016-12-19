package com.miguo.web;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.android.annotations.NonNull;
import com.fanwe.o2o.miguo.R;

/**
 * Created by didik 
 * Created time 2016/12/19
 * Description: 
 */

public abstract class BaseWebFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        int layoutId = setContentLayout();
        return inflater.inflate(layoutId, null, false);
    }

    @LayoutRes
    protected int setContentLayout() {
        return R.layout.layout_base_web;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWebView(view);
        initWebView();
        startFlow(view,savedInstanceState);
    }

    protected void initWebView() {

    }

    protected abstract void startFlow(View view, Bundle savedInstanceState);

    protected WebView webView;

    protected void findWebView(@NonNull View contentView) {
        webView = (WebView) contentView.findViewById(R.id.webView);
    }
}
