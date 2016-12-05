package com.fanwe.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanwe.AlbumActivity;
import com.fanwe.CalendarListActivity;
import com.fanwe.CommentListActivity;
import com.fanwe.StoreConfirmOrderActivity;
import com.fanwe.StoreLocationActivity;
import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.constant.Constant.CommentType;
import com.fanwe.library.customview.SDScaleImageView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDIntentUtil;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.StoreActModel;
import com.fanwe.model.Store_imagesModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.ModelDisplayComment;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 门店详情头部信息 fragment
 *
 * @author js02
 */
public class StoreDetailInfoFragment extends StoreDetailBaseFragment {

    private static int REQUEST_CODE = 898;
    @ViewInject(R.id.siv_image)
    private SDScaleImageView mSiv_image;

    @ViewInject(R.id.tv_name)
    private TextView mTv_name;

    @ViewInject(R.id.rb_star)
    private RatingBar mRb_star;

    @ViewInject(R.id.tv_star_number)
    private TextView mTv_star_number;

    @ViewInject(R.id.tv_image_count)
    private TextView mTv_image_count;

    @ViewInject(R.id.tv_address)
    private TextView mTv_address;

    @ViewInject(R.id.ll_address)
    private LinearLayout mLl_address;

    @ViewInject(R.id.ll_phone)
    private LinearLayout mLl_phone;

    @ViewInject(R.id.tv_pay)
    private TextView mTv_pay;

    @ViewInject(R.id.tv_youhui_pay)
    private TextView mTv_youhui;

    @ViewInject(R.id.tv_youhui_count)
    private TextView mTv_count;

    @ViewInject(R.id.tv_text)
    private TextView mTv_text;

    @ViewInject(R.id.iv_right)
    private ImageView mIv_right;

    @ViewInject(R.id.ll_bug)
    private LinearLayout mLl_bug;

    @ViewInject(R.id.ll_time)
    private LinearLayout mLl_time;

    @ViewInject(R.id.tv_begin_time)
    private TextView mTv_begin;

    @ViewInject(R.id.tv_end_time)
    private TextView mTv_end;

    @ViewInject(R.id.tv_days)
    private TextView mTv_days;

    protected OnStoreDetailListener mListener;
    private ModelDisplayComment modelDisplayComment;

    private SimpleDateFormat format = new SimpleDateFormat("MM-dd");
    private int day = 0;

    public void onSetStoreDetailListener(OnStoreDetailListener listener) {
        this.mListener = listener;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_store_detail_info);
    }

    @Override
    protected void init() {
        super.init();
        initViewState();
        try {
            bindData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        registeClick();
    }

    @Override
    public void setmStoreModel(StoreActModel mStoreModel) {
        super.setmStoreModel(mStoreModel);
        this.modelDisplayComment = mStoreModel.getModelDisplayComment();
    }

    private void initViewState() {
        if (!toggleFragmentView(mInfoModel)) {
            return;
        }
        switch (mInfoModel.getIs_auto_order()) {
            case 1:
                mTv_pay.setVisibility(View.VISIBLE);
                break;

            default:
                mTv_pay.setVisibility(View.GONE);
                break;
        }

        if (getArguments().getInt("type") == 15) {
            mLl_time.setVisibility(View.VISIBLE);
            String begin = format.format(new Date(Long.parseLong(getArguments().getString("begin")) * 1000));
            String end = format.format(new Date(Long.parseLong(getArguments().getString("end")) * 1000));
            SDViewBinder.setTextView(mTv_begin, begin);
            SDViewBinder.setTextView(mTv_end, end);
            String inArr[] = begin.split("-");
            String outArr[] = end.split("-");
            SimpleDateFormat formatTime = new SimpleDateFormat("dd");
            if (inArr[0].equals(outArr[0])) {

                day = Integer.parseInt(outArr[1]) - Integer.parseInt(inArr[1]);
                SDViewBinder.setTextView(mTv_days, "共" + day + "晚");

            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(Long.parseLong(getArguments().getString("begin")) * 1000));
                int sumDay = cal.getActualMaximum(Calendar.DATE);
                day = sumDay - Integer.parseInt(inArr[1]) + Integer.parseInt(outArr[1]);
                mTv_days.setText("共" + day + "晚");

            }
            if (mListener != null) {
                mListener.setOnHotelNumberDay(day);
            }
        } else {
            mLl_time.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        if (!toggleFragmentView(mInfoModel)) {
            return;
        }

        if (mInfoModel.getDiscount_pay() > 0 && mInfoModel.getSalary_money() < 1 && mInfoModel.getOffline() == 1) {
            BigDecimal bd3 = new BigDecimal((100 - mInfoModel.getDiscount_pay()) / 10.0);
            bd3 = bd3.setScale(1, BigDecimal.ROUND_HALF_UP);
            SDViewBinder.setTextView(mTv_count, String.valueOf(bd3));
        } else if (mInfoModel.getSalary_money() >= 1 && mInfoModel.getDiscount_pay() <= 0 && mInfoModel.getOffline() == 1) {
            SDViewBinder.setTextView(mTv_text, "优惠买单");
            SDViewUtil.hide(mTv_count);
        } else if (mInfoModel.getDiscount_pay() > 0 && mInfoModel.getSalary_money() >= 1 && mInfoModel.getOffline() == 1) {
            BigDecimal bd3 = new BigDecimal((100 - mInfoModel.getDiscount_pay()) / 10.0);
            bd3 = bd3.setScale(1, BigDecimal.ROUND_HALF_UP);
            SDViewBinder.setTextView(mTv_count, String.valueOf(bd3));
        } else if (mInfoModel.getDiscount_pay() <= 0 && mInfoModel.getSalary_money() < 1 && mInfoModel.getOffline() == 1) {
            SDViewBinder.setTextView(mTv_text, "优惠买单");
            SDViewUtil.hide(mTv_count);
        } else if (mInfoModel.getOffline() == 0) {
            SDViewUtil.hide(mLl_bug);
        }

        SDViewBinder.setImageView(mSiv_image, mInfoModel.getPreview());

        SDViewBinder.setTextView(mTv_name, mInfoModel.getName());
        float ratingStar = SDTypeParseUtil.getFloat(mInfoModel.getAvg_point());
        SDViewBinder.setRatingBar(mRb_star, ratingStar);
        SDViewBinder.setTextView(mTv_image_count, mInfoModel.getDp_count() + "人评论");
        SDViewBinder.setTextView(mTv_address, mInfoModel.getAddress());
    }

    private void registeClick() {
        mLl_address.setOnClickListener(this);
        mLl_phone.setOnClickListener(this);
        mTv_pay.setOnClickListener(this);
        mTv_image_count.setOnClickListener(this);
        mTv_youhui.setOnClickListener(this);
        mIv_right.setOnClickListener(this);
        mLl_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mLl_address) {
            clickAddress();
        } else if (v == mLl_phone) {
            clickPhone();
        } else if (v == mTv_pay) {
            clickPay();
        } else if (v == mTv_youhui && mInfoModel.getOffline() == 1) {

            clickBuy();
        } else if (v == mIv_right || v == mTv_image_count) {
            clickRight();
        } else if (v == mLl_time) {
            clickTime();
        }
    }

    private void clickTime() {
        Intent intent = new Intent(App.getApplication(), CalendarListActivity.class);
        intent.putExtra("id", 3);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void clickRight() {

        Intent intent = new Intent(App.getApplication(), CommentListActivity.class);
        intent.putExtra(CommentListActivity.EXTRA_ID, mInfoModel.getId());
        intent.putExtra(CommentListActivity.EXTRA_TYPE, CommentType.STORE);
        intent.putExtra("modelDisplayComment", modelDisplayComment);
        startActivity(intent);
    }

    private void clickBuy() {

        if (!AppHelper.isLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
            intent.putExtra(StoreConfirmOrderActivity.EXTRA_ID, mInfoModel.getId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), StoreConfirmOrderActivity.class);
            intent.putExtra(StoreConfirmOrderActivity.EXTRA_ID, mInfoModel.getId());
            startActivity(intent);
        }
    }

    private void clickImage() {
        List<Store_imagesModel> listModel = mInfoModel.getStore_images();
        if (!SDCollectionUtil.isEmpty(listModel)) {
            List<String> listUrl = new ArrayList<String>();
            for (Store_imagesModel model : listModel) {
                listUrl.add(model.getImage());
            }
            Intent intent = new Intent(getActivity(), AlbumActivity.class);
            intent.putExtra(AlbumActivity.EXTRA_IMAGES_INDEX, 0);
            intent.putExtra(AlbumActivity.EXTRA_LIST_IMAGES, (ArrayList<String>) listUrl);
            startActivity(intent);
        }
    }

    private void clickAddress() {
        // TODO 跳到商家地图界面
        Intent intent = new Intent(getActivity(), StoreLocationActivity.class);
        intent.putExtra(StoreLocationFragment.EXTRA_MODEL_MERCHANTITEMACTMODEL, mInfoModel);
        startActivity(intent);
    }

    private void clickPhone() {
        String tel = mInfoModel.getTel();
        if (!TextUtils.isEmpty(tel)) {
            Intent intent = SDIntentUtil.getIntentCallPhone(tel);
            startActivity(intent);
        } else {
            MGToast.showToast("未找到号码");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == CalendarListActivity.Result_CODE) {
                if (data.getIntExtra("success", -1) == 1) {
                    SharedPreferences sp = getActivity().getSharedPreferences("date", Context.MODE_PRIVATE);
                    String inday = sp.getString("dateIn", "");
                    String outday = sp.getString("dateOut", "");
                    SimpleDateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date1 = inputFormatter1.parse(inday);
                        Date date2 = inputFormatter1.parse(outday);
                        if (!"".equals(inday) && !"".equals(outday)) {
                            String begin = format.format(date1);
                            String end = format.format(date2);
                            SDViewBinder.setTextView(mTv_begin, begin);
                            SDViewBinder.setTextView(mTv_end, end);
                            String[] inArr = begin.split("-");
                            String[] outArr = end.split("-");
                            if (inArr[0].equals(outArr[0])) {
                                day = Integer.parseInt(outArr[1]) - Integer.parseInt(inArr[1]);
                                mTv_days.setText("共" + day + "晚");
                            } else {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date1);
                                int sumDay = cal.getActualMaximum(Calendar.DATE);
                                day = sumDay - Integer.parseInt(inArr[1]) + Integer.parseInt(outArr[1]);
                                mTv_days.setText("共" + day + "晚");

                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (mListener != null) {
                        mListener.setOnHotelNumberDay(day);
                    }
                }
            }
        }
    }

    private void clickPay() {
        // TODO 弹出自主下单窗口
    }

    public interface OnStoreDetailListener {
        public void setOnHotelNumberDay(int day);
    }
}