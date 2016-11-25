package com.fanwe.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getShopMemberInfo.ModelFans;
import com.fanwe.seller.model.getShopMemberInfo.ModelMember;
import com.fanwe.seller.model.getShopMemberInfo.ModelShopMemberInfo;
import com.fanwe.seller.model.getShopMemberInfo.ModelTag;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.miguo.ui.view.CircleImageView;
import com.miguo.ui.view.PortraitView;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiang.chen on 2016/11/24.
 */

public class ShopFansFragment extends Fragment implements CallbackView {

    public interface ShowListener {
        void showSomething();
    }

    private ShowListener mShowListener;

    private TextView tvDown;
    private ImageView ivArrow;
    private LinearLayout layoutDetail, layoutFans, layoutFansEmpty;
    private PortraitView portraitView, portraitViewFans;
    private SellerHttpHelper sellerHttpHelper;
    private CircleImageView ivBoss;
    private Button btnSome;

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
        layoutFans = (LinearLayout) view.findViewById(R.id.layout_fans_frag_shop_fans);
        layoutFansEmpty = (LinearLayout) view.findViewById(R.id.layout_fans_empty_frag_shop_fans);
        portraitView = (PortraitView) view.findViewById(R.id.portraitView);
        portraitViewFans = (PortraitView) view.findViewById(R.id.portraitView_fans);
        ivBoss = (CircleImageView) view.findViewById(R.id.iv_portrait_item_shop_fans);
        btnSome = (Button) view.findViewById(R.id.btn_something_frag_shop_fans);
        setListener();
        return view;
    }

    public void setShowListener(ShowListener showListener) {
        mShowListener = showListener;
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
        btnSome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowListener != null) {
                    mShowListener.showSomething();
                }
            }
        });
    }

    public void setShopId(String shopId) {
        if (TextUtils.isEmpty(shopId)) {
            return;
        }
        if (sellerHttpHelper == null) {
            sellerHttpHelper = new SellerHttpHelper(getActivity(), this);
        }
        sellerHttpHelper.getShopMemberInfo(shopId);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    ArrayList<ModelMember> shopMemberInfoList;
    ArrayList<ModelFans> fansInfoList;
    ArrayList<ModelTag> dataMember, dataFans;

    @Override
    public void onSuccess(String method, List datas) {
        if (SellerConstants.GET_SHOP_MEMBER_INFO.equals(method)) {
            List<ModelShopMemberInfo> items = datas;
            if (!SDCollectionUtil.isEmpty(items)) {
                ModelShopMemberInfo bean = items.get(0);
                fansInfoList = bean.getFansInfoList();
                shopMemberInfoList = bean.getShopMemberInfoList();
                generalData();
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBossView();
                        portraitView.initData(dataMember);
                        portraitViewFans.initDataFans(dataFans);
                        if (SDCollectionUtil.isEmpty(dataFans)) {
                            layoutFansEmpty.setVisibility(View.VISIBLE);
                            layoutFans.setVisibility(View.GONE);
                        } else {
                            layoutFansEmpty.setVisibility(View.GONE);
                            layoutFans.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }

    private void setBossView() {
        if (boss != null) {
            SDViewBinder.setImageView(boss.getIcon(), ivBoss);
        }
    }

    ModelMember boss;

    private void generalData() {
        dataMember = new ArrayList<>();
        dataFans = new ArrayList<>();
        if (!SDCollectionUtil.isEmpty(shopMemberInfoList)) {
            boss = shopMemberInfoList.get(0);
            shopMemberInfoList.remove(0);
            for (ModelMember bean : shopMemberInfoList) {
                ModelTag modelTag = new ModelTag();
                modelTag.setIconUrl(bean.getIcon());
                modelTag.setTagName(bean.getTab());
                dataMember.add(modelTag);
            }
        }
        if (!SDCollectionUtil.isEmpty(fansInfoList)) {
            for (ModelFans bean : fansInfoList) {
                ModelTag modelTag = new ModelTag();
                modelTag.setIconUrl(bean.getIcon());
                dataFans.add(modelTag);
            }
            generalFansData();
        }
    }

    private void generalFansData() {
        //没有粉丝，或者粉丝数量大于5，不需要填充
        if (SDCollectionUtil.isEmpty(dataFans) || dataFans.size() >= 5) {
            return;
        }
        if (dataFans.size() == 1) {
            ModelTag modelTag = new ModelTag();
            modelTag.setTagName("铁杆粉丝");
            dataFans.add(modelTag);
            generalFansData();
        } else {
            ModelTag modelTag = new ModelTag();
            modelTag.setTagName("神秘粉丝");
            dataFans.add(modelTag);
            generalFansData();
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }
}
