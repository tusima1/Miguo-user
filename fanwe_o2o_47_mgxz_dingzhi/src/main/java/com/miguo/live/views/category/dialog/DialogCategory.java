package com.miguo.live.views.category.dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.miguo.live.views.dialog.BaseDialog;
import com.miguo.live.views.listener.dialog.DialogListener;

/**
 * Created by Administrator on 2016/8/26.
 */
public abstract class DialogCategory{

    BaseDialog dialog;
    DialogListener listener;

    protected String TAG = getClass().getSimpleName();

    public DialogCategory(BaseDialog dialog){
        this.dialog = dialog;
        initFirst();
        findViews();
        initThisListener();
        setListener();
        init();
    }

    protected void initFirst(){

    }

    public final <E extends View> E findViewsById (int id) {
        try {
            return (E) getDialog().findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }


    private void initThisListener(){
        listener = initListener();
    }

    protected abstract void findViews();
    protected abstract DialogListener initListener();
    protected abstract void setListener();
    protected abstract void init();

    public void showToast(String msg){
        Toast.makeText(getDialog().getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public BaseDialog getDialog() {
        return dialog;
    }

    public Context getContext(){
        return dialog.getContext();
    }

}
