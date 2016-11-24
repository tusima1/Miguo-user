package com.fanwe.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.PortraitView;

/**
 * Created by Administrator on 2016/11/24.
 */

public class ShopFansFragment extends Fragment {
    private TextView tvDown;
    private ImageView ivArrow;
    private LinearLayout layoutDetail;
    private PortraitView portraitView,portraitViewFans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_shop_fans, container, false);
        tvDown = (TextView) view.findViewById(R.id.tv_down_frag_shop_fans);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow_frag_shop_fans);
        layoutDetail = (LinearLayout) view.findViewById(R.id.layout_detail_frag_shop_fans);
        portraitView = (PortraitView) view.findViewById(R.id.portraitView);
        portraitViewFans = (PortraitView) view.findViewById(R.id.portraitView_fans);
        setListener();
        setView();
        return view;
    }

    private void setView() {
        portraitView.initData(5);
        portraitViewFans.initDataFans(5);
    }

    private void setListener() {
        tvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDown.setVisibility(View.GONE);
                layoutDetail.setVisibility(View.VISIBLE);
            }
        });
        ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDown.setVisibility(View.VISIBLE);
                layoutDetail.setVisibility(View.GONE);
            }
        });
    }

    public void setShopId(String shopId) {
        MGToast.showToast(shopId);
    }
}
