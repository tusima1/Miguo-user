package com.fanwe.user.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.base.CallbackView2;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getAttentionFans.ModelFans;
import com.fanwe.user.model.putAttention.ModelAttention;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.user.view.UserHomeActivity;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by didik on 2016/8/25.
 */
public class FansAdapter extends BaseAdapter {

    private final Activity mActivity;
    private List<ModelFans> mFansList;

    public FansAdapter(Activity activity) {
        this.mActivity=activity;
    }

    public void setData(List<ModelFans> fansList){
        this.mFansList=fansList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFansList==null ? 0: mFansList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_fans,null);
            holder.civ_face= (CircleImageView) convertView.findViewById(R.id.iv_user_face);
            holder.tv_userName= (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_subName= (TextView) convertView.findViewById(R.id.tv_subName);
            holder.ll_star= convertView.findViewById(R.id.ll_star);
            holder.ll_user= convertView.findViewById(R.id.ll_user);
            holder.tv_focus= (TextView) convertView.findViewById(R.id.tv_focus);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        final ModelFans modelFans = mFansList.get(position);
        holder.tv_userName.setText(modelFans.getNick());
        String fx_level = modelFans.getFx_level();
        Drawable rankDrawable=null;
        if ("1".equals(fx_level)) {
            rankDrawable=parent.getResources().getDrawable(R.drawable.ic_rank_3);
        } else if ("2".equals(fx_level)) {
            rankDrawable=parent.getResources().getDrawable(R.drawable.ic_rank_2);
        } else if ("3".equals(fx_level)) {
            rankDrawable=parent.getResources().getDrawable(R.drawable.ic_rank_1);
        }
        if (rankDrawable!=null){
            rankDrawable.setBounds(0, 0, rankDrawable.getMinimumWidth(), rankDrawable.getMinimumHeight());
            holder.tv_userName.setCompoundDrawables(null,null,rankDrawable,null);
        }
        holder.tv_subName.setText(modelFans.getPersonality());
        SDViewBinder.setImageView(modelFans.getIcon(),holder.civ_face);

        String attention_status = modelFans.getAttention_status();
        //状态：1：未关注 2：已关注 3：互相关注

//        android:textColor="@color/orange"
//        android:gravity="center_vertical|right"
//        android:text="已关注"
        if ("1".equals(attention_status)){
            //显示关注按钮
            //TODO 关注
            holder.ll_star.setVisibility(View.VISIBLE);
            holder.tv_focus.setVisibility(View.GONE);
            holder.ll_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doFocus(position);
                }
            });

        }else if("2".equals(attention_status)) {
            holder.ll_star.setVisibility(View.GONE);
            holder.tv_focus.setVisibility(View.VISIBLE);
            holder.tv_focus.setText("已关注");
        }else if("3".equals(attention_status)) {
            holder.ll_star.setVisibility(View.GONE);
            holder.tv_focus.setVisibility(View.VISIBLE);
            holder.tv_focus.setText("相互关注");
        }

        holder.ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去粉丝的主页
                Intent intent = new Intent(mActivity, UserHomeActivity.class);
                intent.putExtra("id", modelFans.getFans_user_id());
                mActivity.startActivity(intent);
            }
        });
        return convertView;
    }

    private void doFocus(final int position) {
        ModelFans modelFans = mFansList.get(position);
        String fans_user_id = modelFans.getFans_user_id();
        String attention_status = modelFans.getAttention_status();
//        状态：1：未关注 2：已关注 3：互相关注
        if ("1".equals(attention_status)){
            //TODO 关注
            new UserHttpHelper(null, new CallbackView2() {
                @Override
                public void onSuccess(String responseBody) {

                }

                @Override
                public void onSuccess(String method, List datas) {
                    ModelAttention modelAttention= (ModelAttention) datas.get(0);
                    if (modelAttention!=null){
                        mFansList.get(position).setAttention_status(modelAttention.getAttention_status());
                        mFansList.get(position).setFans_user_id(modelAttention.getFocus_user_id());
                        notifyDataSetChanged();
                    }else {
                        MGToast.showToast("关注失败!");
                    }
                }

                @Override
                public void onFailue(String responseBody) {
                    MGToast.showToast("关注失败!");
                }

                @Override
                public void onFinish(String method) {

                }
            }).putAttention(fans_user_id,"1");
        }
        //取消关注
//        else {
//            new UserHttpHelper(null, new CallbackView2() {
//                @Override
//                public void onSuccess(String responseBody) {
//
//                }
//
//                @Override
//                public void onSuccess(String method, List datas) {
//
//                }
//
//                @Override
//                public void onFailue(String responseBody) {
//
//                }
//
//                @Override
//                public void onFinish(String method) {
//
//                }
//            }).putAttention(fans_user_id,"0");
//        }

    }

//    public void setData(List<ModelShopInfo2> data){
//        mData=data;
//        notifyDataSetChanged();
//    }

    private class ViewHolder{
        public CircleImageView civ_face;
        public TextView tv_userName;
        public TextView tv_subName;
//        public DrawableCenterTextView dctv_star;
        public View ll_star;
        public View ll_user;
        public TextView tv_focus;
    }
}
