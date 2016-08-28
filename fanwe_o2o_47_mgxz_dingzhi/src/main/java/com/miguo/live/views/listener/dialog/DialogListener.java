package com.miguo.live.views.listener.dialog;

import android.view.View;

import com.miguo.live.views.category.dialog.DialogCategory;

/**
 * Created by zlh on 2016/8/26.
 */
public class DialogListener implements View.OnClickListener{

    DialogCategory category;

    public DialogListener(DialogCategory category){
        this.category = category;
    }

    @Override
    public void onClick(View v) {

    }

    public DialogCategory getCategory() {
        return category;
    }
}
