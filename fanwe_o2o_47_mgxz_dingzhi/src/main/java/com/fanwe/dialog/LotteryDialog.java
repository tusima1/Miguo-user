package com.fanwe.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fanwe.mine.views.LotteryActivity;
import com.fanwe.o2o.miguo.R;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.views.category.dialog.DialogCategory;
import com.miguo.live.views.dialog.BaseDialog;
import com.miguo.live.views.utils.BaseUtils;

/**
 * 购物后分享得佣金
 */
public class LotteryDialog extends BaseDialog {
    private Context mContext;
    private TextView tvCancle, tvDo;
    private String url;

    public LotteryDialog(Context mContext) {
        super(mContext, R.style.floag_dialog, Gravity.TOP);
        this.mContext = mContext;
        preView();
        setListener();
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent intent = new Intent(mContext, ClassNameFactory.getClass(ClassPath.WEB_PAGE_ACTIVITY));
                Bundle bundle = new Bundle();
                bundle.putString(IntentKey.HOME_BANNER_WEB_PAGE, url);
                intent.putExtras(bundle);
                BaseUtils.jumpToNewActivityForResult((Activity)mContext, intent, RequestCode.HOME_WEB_PAGE);
            }
        });
    }

    private void preView() {
        tvCancle = (TextView) findViewById(R.id.tv_cancle_dialog_lottery);
        tvDo = (TextView) findViewById(R.id.tv_do_dialog_lottery);
    }

    @Override
    protected DialogCategory initCategory() {
        return null;
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.dialog_lottery;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
