package com.fanwe.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.MyTuanDetailDialog;
import com.fanwe.adapter.DealAttrAdapter;
import com.fanwe.adapter.DealAttrGroupAdapter;
import com.fanwe.adapter.TuanDetailCombinedPackagesAdapter;
import com.fanwe.adapter.TuanDetailCombinedPackagesAdapter.TuanDetailCombinedPackagesAdapterListener;
import com.fanwe.adapter.TuanDetailCombinedPackagesPageAdapter;
import com.fanwe.fragment.TuanDetailAttrsFragment.TuanDetailAttrsFragment_onClickAttrItemListener;
import com.fanwe.library.customview.SDSlidingPlayView;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogListView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDNumberUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.Deal_attrModel;
import com.fanwe.model.Deal_attrValueModel;
import com.fanwe.model.Deal_indexActModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.SDFormatUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 团购详情（最佳组合套餐）
 *
 * @author js02
 */
public class TuanDetailCombinedPackagesFragment extends TuanDetailBaseFragment {

    @ViewInject(R.id.spv_content)
    private SDSlidingPlayView mSpv_content;

    @ViewInject(R.id.tv_number)
    private TextView mTv_number;

    @ViewInject(R.id.tv_price_combined)
    private TextView mTv_price_combined;

    @ViewInject(R.id.tv_price_original)
    private TextView mTv_price_original;

    @ViewInject(R.id.btn_buy)
    private TextView mBtn_buy;

    private int requestCode = 111;

    protected TuanDetailCombinedPackagesPageAdapter mAdapter;

    private List<Deal_indexActModel> mListModel;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_tuan_detail_combined_packages);
    }

    @Override
    protected void init() {
        super.init();
        if (!hasData()) {
            return;
        }
        initSlidingPlayView();
        bindData();
        registerClick();
    }

    private void initSlidingPlayView() {
        mSpv_content.setmImageNormalResId(R.drawable.ic_main_dot2_normal);
        mSpv_content.setmImageSelectedResId(R.drawable.ic_main_dot2_foused);
    }

    private void registerClick() {
        mBtn_buy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickBuy();
            }
        });

        mTv_price_original.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        TuanDetailAttrsFragment fragAttr = getTuanDetailAttrsFragment();
        if (fragAttr != null) {
            fragAttr.setmListenerOnClickAttrItem(new TuanDetailAttrsFragment_onClickAttrItemListener() {

                @Override
                public void onClickItem(View v, int position, Deal_attrValueModel model, DealAttrAdapter adapter) {
                    updatePrice();
                }
            });
        }
    }

    private void clickBuy() {
        if (!validateMainGoods()) {
            return;
        }

        List<Deal_indexActModel> listSelectedModel = getSelectedModel();
        if (isEmpty(listSelectedModel)) {
            MGToast.showToast("请选择组合商品");
            return;
        }

        listSelectedModel.add(mDealModel);

        // TODO 遍历选择的商品，和主商品如果有属性的把属性id数据取出来后请求接口
        List<String> listIds = new ArrayList<String>();
        Map<String, Map<String, Integer>> mapIds = new HashMap<String, Map<String, Integer>>();

        for (Deal_indexActModel model : listSelectedModel) {
            String id = String.valueOf(model.getId());
            listIds.add(id);
            if (model.hasAttr()) {
                mapIds.put(id, model.getSelectedAttrId());
            }
        }

        RequestModel model = new RequestModel();
        model.putCtl("cart");
        model.putAct("addcartByRelate");
        model.put("ids", listIds);
        if (!mapIds.isEmpty()) {
            model.put("deal_attr", mapIds);
        }
    }

    public boolean validateMainGoods() {
        if (mDealModel.hasAttr()) // 有属性
        {
            List<Deal_attrModel> listUnSelected = mDealModel.getUnSelectedAttr();
            if (!SDCollectionUtil.isEmpty(listUnSelected)) // 有属性未被选中
            {
                MGToast.showToast("请选择主商品" + listUnSelected.get(0).getName());
                scrollToAttr();
                return false;
            }
        }
        return true;
    }

    private List<Deal_indexActModel> getSelectedModel() {
        List<Deal_indexActModel> listSelectedModel = new ArrayList<Deal_indexActModel>();
        for (Deal_indexActModel model : mListModel) {
            if (model.isSelected()) {
                listSelectedModel.add(model);
            }
        }
        return listSelectedModel;
    }

    public void updatePrice() {
        int count = 0;
        double currentPrice = 0;
        double originalPrice = 0;

        List<Deal_indexActModel> listSelectedModel = getSelectedModel();
        if (!isEmpty(listSelectedModel)) {
            count = listSelectedModel.size();
            currentPrice = mDealModel.getCurrentPriceTotal();
            originalPrice = mDealModel.getOriginalPriceTotal();
            for (Deal_indexActModel model : listSelectedModel) {
                currentPrice = SDNumberUtil.add(currentPrice, model.getCurrentPriceTotal(), 2);
                originalPrice = SDNumberUtil.add(originalPrice, model.getOriginalPriceTotal(), 2);
            }
        }
        SDViewBinder.setTextView(mTv_number, String.valueOf(count));
        SDViewBinder.setTextView(mTv_price_combined, SDFormatUtil.formatMoneyChina(currentPrice));
        SDViewBinder.setTextView(mTv_price_original, SDFormatUtil.formatMoneyChina(originalPrice));
    }

    private void bindData() {
        // 移除主商品
        for (int i = 0; i < mListModel.size(); i++) {
            Deal_indexActModel model = mListModel.get(i);
            if (model.getId() == mDealModel.getId()) {
                mListModel.remove(model);
                break;
            }
        }

        List<List<Deal_indexActModel>> listPage = SDCollectionUtil.splitList(mListModel, 3);

        mAdapter = new TuanDetailCombinedPackagesPageAdapter(listPage, getActivity());
        mAdapter.setmListener(new TuanDetailCombinedPackagesAdapterListener() {

            private void setModelSelected(boolean selected, int position, final Deal_indexActModel model,
                                          final TuanDetailCombinedPackagesAdapter adapter) {
                model.setSelected(selected);
                adapter.updateItem(position);
                updatePrice();
            }

            @Override
            public void onClickSelected(View view, final int position, final Deal_indexActModel model, final TuanDetailCombinedPackagesAdapter adapter) {
                if (model.isSelected()) {
                    if (model.hasAttr()) {
                        model.clearSelectedAttr();
                    }
                    setModelSelected(false, position, model, adapter);
                } else {
                    if (!model.hasAttr()) {
                        setModelSelected(true, position, model, adapter);
                    } else {
                        // TODO 弹出属性窗口
                        DealAttrGroupAdapter adapterAttr = new DealAttrGroupAdapter(model.getDeal_attr(), getActivity());

                        SDDialogListView dialog = new SDDialogListView(getActivity());
                        dialog.setTextTitle("请选择");
                        dialog.setmDismissAfterClick(false);
                        dialog.setmListener(new SDDialogCustomListener() {

                            @Override
                            public void onDismiss(SDDialogCustom dialog) {
                            }

                            @Override
                            public void onClickConfirm(View v, SDDialogCustom dialog) {
                                List<Deal_attrModel> listUnSelected = model.getUnSelectedAttr();
                                if (!SDCollectionUtil.isEmpty(listUnSelected)) // 有属性未被选中
                                {
                                    dialog.showTip("请选择" + listUnSelected.get(0).getName());
                                    return;
                                } else {
                                    setModelSelected(true, position, model, adapter);
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onClickCancel(View v, SDDialogCustom dialog) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setAdapter(adapterAttr);
                        dialog.show();
                    }
                }
            }

            @Override
            public void onClickItem(View view, int position, Deal_indexActModel model, TuanDetailCombinedPackagesAdapter adapter) {
                Intent intent = new Intent(getActivity(), MyTuanDetailDialog.class);
                intent.putExtra(MyTuanDetailDialog.EXTRA_GOODS_ID, model.getId());
                startActivityForResult(intent, requestCode);

            }
        });
        mSpv_content.setAdapter(mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.requestCode == requestCode) {
            if (resultCode == MyTuanDetailDialog.resultCode) {
                String mId = data.getStringExtra("mId");
                if (data.getBooleanExtra("add", false)) {
                    for (int i = 0; i < mListModel.size(); i++) {
                        Deal_indexActModel model = mListModel.get(i);

                        if (model.getId().equals(mId)) {
                            model.setSelected(true);
                            bindData();
                            updatePrice();
                        }
                    }
                }
            }
        }

    }

    private boolean hasData() {
        if (!toggleFragmentView(mDealModel)) {
            return false;
        }

        mListModel = mDealModel.getListRelateModel();
        if (!toggleFragmentView(mListModel)) {
            return false;
        }
        return true;
    }
}
