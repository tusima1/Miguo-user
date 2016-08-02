package com.fanwe.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;


/*
 *actionsheet样式dialog
 */


public class BottomDialog extends Dialog {
    private String[] mStringItems;
    private DialogClickListner mDialogClickListner;

    private String mTittle;

    public BottomDialog(Context c, String title, String[] strs, DialogClickListner l) {
        super(c, R.style.Translucent_NoTitle_Dialog);
        this.mTittle = title;
        this.mStringItems = strs;
        this.mDialogClickListner = l;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom);
        LinearLayout mLayout = (LinearLayout) findViewById(R.id.dialog_container);
        WindowManager wm = getWindow().getWindowManager();
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = width;
        p.gravity = 80;
        getWindow().setAttributes(p);
        int length = mStringItems.length;
        if (mStringItems != null && length > 0) {
            for (int i = 0; i < length; i++) {
                Button button = new Button(getContext());
                button.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
                button.setTextSize(18);
                button.setGravity(Gravity.CENTER);

                button.setTextColor(getContext().getResources().getColor(R.color.text_333));
                int pad = SDViewUtil.dp2px(10);
                button.setPadding(pad, pad, pad, pad);
                final int position = i;
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mDialogClickListner != null) {
                            mDialogClickListner.onItemClick(position, mStringItems[position]);
                        }
                        dismiss();

                    }
                });
                button.setText(mStringItems[i]);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                mLayout.addView(button, params);

                View dividerView = new View(getContext());
                dividerView.setBackgroundColor(Color.parseColor("#cccccc"));
                LayoutParams p1 = new LayoutParams(LayoutParams.MATCH_PARENT, SDViewUtil.dp2px(1));
                int margin = SDViewUtil.dp2px(12);
                p1.setMargins(margin, 0, margin, 0);
                if (i != length - 1) {
                    mLayout.addView(dividerView, p1);
                }
            }
        }
        findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialogClickListner != null) {
                    mDialogClickListner.onCancelClick();
                }
                dismiss();
            }
        });

    }

    public void setDialogClickListner(DialogClickListner l) {
        this.mDialogClickListner = l;
    }

    public interface DialogClickListner {
        void onCancelClick();

        void onItemClick(int position, String item);
    }
}