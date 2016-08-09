package com.miguo.live.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.MyItemClickListener;
import com.miguo.live.model.getHostTags.ModelHostTags;
import com.miguo.live.viewHolder.ViewHolderAuthTag;

import java.util.List;
import java.util.Random;


/**
 * Created by didik on 2016/7/22.
 */
public class AuthTagAdapter extends RecyclerView.Adapter<ViewHolderAuthTag> {

    private Context mContext;
    private List<ModelHostTags> datas;

    private MyItemClickListener mItemClickListener;
    private Random random = new Random();

    public AuthTagAdapter(Context context, List<ModelHostTags> data) {
        this.mContext = context;
        this.datas = data;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    @Override
    public void onBindViewHolder(ViewHolderAuthTag holder, int position) {
        if (position == (datas.size() - 1)) {
            //换一批
            holder.tvChange.setVisibility(View.VISIBLE);
            holder.tvTag.setVisibility(View.GONE);
        } else {
            //普通标签
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(datas.get(position).getDic_mean());
            holder.tvChange.setVisibility(View.GONE);
            setTextColor(holder.tvTag);
        }
    }

    int preResult;

    /**
     * 随机设置标签颜色
     *
     * @param tvTag
     */
    private void setTextColor(TextView tvTag) {
        int result = random.nextInt(7);
        result++;
        if (result == preResult) {
            //如果前后两次相同，再随机一遍
            result = random.nextInt(7);
            result++;
        }
        preResult = result;
        switch (result) {
            case 1:
                tvTag.setBackgroundResource(R.drawable.bg_auth_1);
                tvTag.setTextColor(Color.parseColor("#588fdf"));
                break;
            case 2:
                tvTag.setBackgroundResource(R.drawable.bg_auth_2);
                tvTag.setTextColor(Color.parseColor("#9e7ee6"));
                break;
            case 3:
                tvTag.setBackgroundResource(R.drawable.bg_auth_3);
                tvTag.setTextColor(Color.parseColor("#f87e6c"));
                break;
            case 4:
                tvTag.setBackgroundResource(R.drawable.bg_auth_4);
                tvTag.setTextColor(Color.parseColor("#f8687d"));
                break;
            case 5:
                tvTag.setBackgroundResource(R.drawable.bg_auth_5);
                tvTag.setTextColor(Color.parseColor("#67c4ad"));
                break;
            case 6:
                tvTag.setBackgroundResource(R.drawable.bg_auth_6);
                tvTag.setTextColor(Color.parseColor("#5f6ba3"));
                break;
            case 7:
                tvTag.setBackgroundResource(R.drawable.bg_auth_7);
                tvTag.setTextColor(Color.parseColor("#e563a3"));
                break;
            default:
                tvTag.setBackgroundResource(R.drawable.bg_auth_1);
                tvTag.setTextColor(Color.parseColor("#588fdf"));
                break;
        }
    }

    @Override
    public ViewHolderAuthTag onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auth_tag, parent, false);
        ViewHolderAuthTag vh = new ViewHolderAuthTag(itemView, mItemClickListener);
        return vh;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

}