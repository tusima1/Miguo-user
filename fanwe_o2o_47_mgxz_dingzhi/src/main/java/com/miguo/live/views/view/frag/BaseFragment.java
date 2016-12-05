package com.miguo.live.views.view.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by didik on 2016/11/9.
 */

public abstract class BaseFragment extends Fragment {
    private View content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        content = inflater.inflate(setLayoutResId(), container, false);
        if (getBundleData(getArguments())){
            initView(content);
            startFlow();
        }
        return content;
    }

    protected abstract int setLayoutResId();
    protected abstract void initView(View content);
    protected abstract void startFlow();
    protected boolean getBundleData(Bundle args){
        return true;
    }
}
