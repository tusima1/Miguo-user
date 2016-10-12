package com.fanwe.adapter.barry;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwe.app.App;
import com.miguo.live.views.utils.BaseUtils;

import java.util.List;

/**
 * Created by 狗蛋哥/zlh on 16/3/25.
 */
public abstract class BarryBaseRecyclerAdapter extends RecyclerView.Adapter{

    protected Activity activity;
    protected List datas;
    protected App app;
    protected LayoutInflater inflater;
    protected String tag = this.getClass().getSimpleName();

    public BarryBaseRecyclerAdapter(Activity activity, List datas){
        this.activity = activity;
        this.datas = datas;
        this.app = App.getApplication();
        inflater = activity.getLayoutInflater();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflatView(parent, viewType);
        RecyclerView.ViewHolder holder = initHolder(view, viewType);
        findHolderViews(view, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BarryListener listener = initHolderListener(holder, position);
        setHolderListener(holder, position, listener);
        doThings(holder, position);
        setHolderViews(holder, position);
    }

    protected abstract RecyclerView.ViewHolder initHolder(View view, int viewTyp);
    protected abstract View inflatView(ViewGroup parent, int viewType);
    protected abstract void findHolderViews(View view, RecyclerView.ViewHolder holder , int viewType);
    protected abstract BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position);
    protected abstract void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener);
    protected abstract void doThings(RecyclerView.ViewHolder holder, int position);
    protected abstract void setHolderViews(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public Object getItem(int position){
        return datas!=null? datas.get(position) : null;
    }

    public void notifyDataSetChanged(List datas){
        if(datas!=null){
            this.datas.clear();
            this.datas.addAll(datas);
            this.notifyDataSetChanged();
        }
    }

    protected String trim(String str){
        try{
            if(str == null){
                return "";
            }
            return str.trim();
        }catch (Exception e){
            return "";
        }

    }

    public RecyclerView.ViewHolder getHolder(RecyclerView.ViewHolder holder){
        return holder;
    }

    public void notifyDataSetChangedLoadmore(List datas){
        if(datas!=null){
            this.datas.addAll(datas);
            this.notifyDataSetChanged();
        }
    }


    public Activity getActivity(){
        return activity;
    }


    public void notifyDataSetChangedClear(){
        if(datas!=null && datas.size() >0){
            datas.clear();
            this.notifyDataSetChanged();
        }
    }

    public void notifyDataSetChangedToFirstOne(List datas){
        if(datas!=null){
            this.datas.add(0, datas);
            this.notifyDataSetChanged();
        }
    }

    public void notifyDataSetChangedToLastOne(List datas){
        if(datas != null){
            this.datas.add(this.datas.size(), datas);
            this.notifyDataSetChanged();
        }
    }

    public int dip2px(int dp){
        return BaseUtils.dip2px(activity, dp);
    }

    public int px2dip(int px){
        return BaseUtils.px2dip(activity, px);
    }

    public Drawable getDrawable(int resId){
        return getResources().getDrawable(resId);
    }

    public int getColor(int resId){
        return getResources().getColor(resId);
    }

    public String getString(int resId){
        return getResources().getString(resId);
    }

    public int getDimensionPixelSize(int resId){
        return getResources().getDimensionPixelSize(resId);
    }

    public int getScreenWidth(){
        return BaseUtils.getWidth(activity);
    }

    public int geScreenHeight(){
        return BaseUtils.getHeight(activity);
    }

    public RelativeLayout.LayoutParams getRelativeLayoutParams(int width, int height){
        return new RelativeLayout.LayoutParams(width, height);
    }

    public int wrapContent(){
        return RelativeLayout.LayoutParams.WRAP_CONTENT;
    }

    public int matchParent(){
        return RelativeLayout.LayoutParams.MATCH_PARENT;
    }

    public LinearLayout.LayoutParams getLinearLayoutParams(int width, int height){
        return new LinearLayout.LayoutParams(width, height);
    }

    public Resources getResources(){
        return activity.getResources();
    }


    /**
     * RecyclerView模板Listener
     */
    public class BarryListener implements View.OnClickListener{

        protected RecyclerView.ViewHolder holder;
        protected BarryBaseRecyclerAdapter adapter;
        protected int position;


        public BarryListener(BarryBaseRecyclerAdapter adapter,RecyclerView.ViewHolder holder, int position) {
            this.adapter = adapter;
            this.holder = holder;
            this.position = position;
        }

        public BarryBaseRecyclerAdapter getAdapter() {
            return adapter;
        }

        @Override
        public void onClick(View v) {

        }
    }

}
