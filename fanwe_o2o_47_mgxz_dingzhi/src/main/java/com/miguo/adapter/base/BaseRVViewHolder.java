package com.miguo.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;

/**
 * Created by didik on 2016/11/11.
 */

public class BaseRVViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    public BaseRVViewHolder(View itemView){
        super(itemView);
        this.views = new SparseArray<View>();
    }
    @SuppressWarnings("unchecked")
    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
    public void setTextView(int viewId,CharSequence str){
        setTextView(viewId,str,null);
    }
    public void setTextView(int viewId,CharSequence str,CharSequence empty){
        TextView textView = retrieveView(viewId);
        if (textView==null)return;
        if (!TextUtils.isEmpty(str)){
            textView.setText(str);
        }else if (TextUtils.isEmpty(empty)){
            empty="";
            textView.setText(empty);
        }
    }
    public void setImageView(int viewId,String url){
        ImageView imageView = retrieveView(viewId);
        if (imageView==null)return;
        SDViewBinder.setImageView(url,imageView);
    }

    public TextView getTextView(int viewId) {
        return retrieveView(viewId);
    }

    public Button getButton(int viewId) {
        return retrieveView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return retrieveView(viewId);
    }

    public View getView(int viewId) {
        return retrieveView(viewId);
    }


}
