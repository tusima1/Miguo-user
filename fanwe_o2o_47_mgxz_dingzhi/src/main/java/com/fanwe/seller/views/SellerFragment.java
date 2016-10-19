package com.fanwe.seller.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.fragment.StoreListFragment;
import com.fanwe.o2o.miguo.R;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SellerFragment extends Fragment {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private View view;
    private TextView tvAll, tvGroupon;
    private View viewAll, viewGroupon;
    private StoreListFragment mFragAll;
    private StoreListFragment mFragGroupon;
    private Bundle bundle;

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
        clickTitle("all");
        return view;
    }

    private void setWidget() {
        tvAll = (TextView) view.findViewById(R.id.tv_all_frag_seller);
        tvGroupon = (TextView) view.findViewById(R.id.tv_groupon_frag_seller);
        viewAll = (View) view.findViewById(R.id.view_all_frag_seller);
        viewGroupon = (View) view.findViewById(R.id.view_groupon_frag_seller);
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

}
