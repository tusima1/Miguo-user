package com.miguo.live.views;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.SDFormatUtil;
import com.miguo.live.interf.IHelper;

/**
 * Created by didik on 2016/8/17.
 * 用户抢完红包后结果展示界面
 */
public class UserRobRedPacketEndDialogHelper implements IHelper {
    /**
     * 是否抢到了红包。
     */
    private boolean mRob;
    private Activity mActivity;
    private Dialog dialog;
    private TextView mTv_num;//数字
    private TextView mTv_tag;//折扣券  拼接为 数字+折扣券
    private TextView mTv_info;//展示的信息
    private String type;
    private String num;

    public UserRobRedPacketEndDialogHelper(Activity activity ,boolean isRobed) {
        this.mActivity = activity;
        this.mRob=isRobed;
        createDialog();
    }

    /**
     * 设置展示在Layout上的数据
     * @param type 类型
     * @param num 数量
     */
    public void setData(String type,String num,String info){
        this.type=type;
        this.num=num;
        if (mRob){
            setQuan(num,type,info);
        }
    }

    @Override
    public void onDestroy() {

    }

    private void createDialog() {
        dialog = new Dialog(mActivity, R.style.floag_dialog);
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout
                .dialog_live_rob_red_packet_end, null);
        bindContentView_V2(contentView);
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void bindContentView_V2(View contentView) {
        View robView = LayoutInflater.from(mActivity).inflate(mRob ? R.layout.dialog_item_robed_v2 :
                R.layout
                        .dialog_item_unrob_v2, null);
        FrameLayout container = (FrameLayout) contentView.findViewById(R.id.fr_container);
        container.addView(robView);
        mTv_num = ((TextView) robView.findViewById(R.id.tv_num));
        mTv_tag = ((TextView) robView.findViewById(R.id.tv_tag));
        mTv_info = ((TextView) robView.findViewById(R.id.tv_info));
        robView.findViewById(R.id.tv_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 设置顶部的优惠券信息
     * @param num
     * @param tag
     */
    public void setQuan(String num,String tag,String info){
        if (mTv_num!=null && mTv_tag!=null && !TextUtils.isEmpty(num) && !TextUtils.isEmpty(tag)){
            mTv_num.setText(SDFormatUtil.formatNumberString(num,2));
            if("1".equals(type)) {
                mTv_tag.setText("折扣券");
            }else if("2".equals(type)){
                mTv_tag.setText("现金券");
            }
        }
        SDViewBinder.setTextView(mTv_info,info,"");
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            mRob=false;
        }
    }
}
