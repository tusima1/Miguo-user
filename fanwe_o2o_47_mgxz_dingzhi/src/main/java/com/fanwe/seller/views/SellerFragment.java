package com.fanwe.seller.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.HomeSearchActivity;
import com.fanwe.constant.Constant;
import com.fanwe.fragment.StoreListFragment;
import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiHomeActivity;
import com.miguo.live.views.utils.BaseUtils;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SellerFragment extends Fragment {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private View view;
    private TextView tvAll, tvGroupon;
    private View viewAll, viewGroupon;
    private ImageView ivSearch, ivBack;
    private StoreListFragment mFragAll;
    private StoreListFragment mFragGroupon;
    private Bundle bundle;
    private LinearLayout titleLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        bundle = getActivity().getIntent().getExtras();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_seller, container, false);
        setWidget();
        setListener();
        setTitlePadding(titleLayout);
        clickTitle("groupon");
        setView();
        return view;
    }

    private void setView() {
        //主界面，隐藏返回按钮
        if (getActivity() instanceof HiHomeActivity) {
            ivBack.setVisibility(View.GONE);
        } else {
            ivBack.setVisibility(View.VISIBLE);
        }
    }

    private void setWidget() {
        titleLayout = (LinearLayout) view.findViewById(R.id.title_layout);
        tvAll = (TextView) view.findViewById(R.id.tv_all_frag_seller);
        tvGroupon = (TextView) view.findViewById(R.id.tv_groupon_frag_seller);
        viewAll = (View) view.findViewById(R.id.view_all_frag_seller);
        viewGroupon = (View) view.findViewById(R.id.view_groupon_frag_seller);
        ivSearch = (ImageView) view.findViewById(R.id.iv_search_frag_seller);
        ivBack = (ImageView) view.findViewById(R.id.iv_back_frag_seller);
    }

    private void setListener() {
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTitle("all");
                tvAll.setTextColor(Color.parseColor("#2e2e2e"));
                tvGroupon.setTextColor(Color.parseColor("#999999"));
                viewAll.setVisibility(View.VISIBLE);
                viewGroupon.setVisibility(View.INVISIBLE);
            }
        });
        tvGroupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTitle("groupon");
                tvGroupon.setTextColor(Color.parseColor("#2e2e2e"));
                tvAll.setTextColor(Color.parseColor("#999999"));
                viewGroupon.setVisibility(View.VISIBLE);
                viewAll.setVisibility(View.INVISIBLE);
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHomeSearchActivity();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void startHomeSearchActivity() {
        Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(HomeSearchActivity.EXTRA_SEARCH_TYPE, Constant.SearchTypeNormal.MERCHANT);
        startActivity(intent);
    }

    private void clickTitle(String flag) {
        ft = fm.beginTransaction();
        if ("all".equals(flag)) {
            if (mFragAll == null) {
                mFragAll = new StoreListFragment();
                mFragAll.setArguments(bundle);
                mFragAll.getArguments().putString(StoreListFragment.EXTRA_STORE_TYPE, "0");
                ft.add(R.id.layout_content_frag_seller, mFragAll);
            } else {
                ft.show(mFragAll);
            }
            if (mFragGroupon != null && !mFragGroupon.isHidden()) {
                ft.hide(mFragGroupon);
            }
        } else {
            if (mFragGroupon == null) {
                mFragGroupon = new StoreListFragment();
                mFragGroupon.setArguments(bundle);
                mFragGroupon.getArguments().putString(StoreListFragment.EXTRA_STORE_TYPE, "1");
                ft.add(R.id.layout_content_frag_seller, mFragGroupon);
            } else {
                ft.show(mFragGroupon);
            }
            if (mFragAll != null && !mFragAll.isHidden()) {
                ft.hide(mFragAll);
            }
        }
        ft.commit();
    }

    public void handlerCateIdChanged(String cate_id){
        if(mFragAll != null){
            mFragAll.handlerCateIdChanged(cate_id);
        }
    }

    /**
     * 沉浸式标题栏效果需要设置padding
     */
    protected void setTitlePadding(View view) {
        if(getActivity() instanceof HiHomeActivity){
            if (view != null) {
                view.setPadding(0, BaseUtils.getStatusBarHeight(getActivity()), 0, 0);
            }
        }
    }


}
