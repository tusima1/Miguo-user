package com.miguo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.app.App;
import com.miguo.app.HiBaseActivity;
import com.miguo.category.fragment.FragmentCategory;

/**
 * Created by zlh/Barry/狗蛋哥 on 16/3/16.
 */
public abstract class HiBaseFragment extends Fragment {


    protected String tag = this.getClass().getSimpleName();
    /**
     * View 缓存View;
     */
    protected View cacheView;

    /**
     * FragmentCategory
     */
    protected FragmentCategory category;

    /**
     * 初始化
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(cacheView == null) {
            cacheView = createView(inflater, container, savedInstanceState);
        }else {
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) cacheView.getParent();
            if(parent != null){
                parent.removeView(cacheView);
            }
        }
        initFragmentCategory();
        return cacheView;
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    protected abstract void initFragmentCategory();


    public App getApp(){
        return (App) getActivity().getApplicationContext();
    }


    public HiBaseActivity getIActivity(){
        return (HiBaseActivity)super.getActivity();
    }


    public FragmentCategory getCategory(){
        return category;
    }

}
