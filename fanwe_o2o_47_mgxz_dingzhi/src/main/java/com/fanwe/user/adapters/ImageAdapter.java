package com.fanwe.user.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MViewHolder> {

    private Context context;
    private List<String> listData;

    public ImageAdapter(Context context, List<String> mList) {
        super();
        this.context = context;
        this.listData = mList;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {

        View view = View.inflate(viewGroup.getContext(),
                R.layout.item_image_collect_list, null);
        // 创建一个ViewHolder
        MViewHolder holder = new MViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MViewHolder mViewHolder, int position) {
        ImageLoader.getInstance().displayImage(listData.get(position), mViewHolder.image);
    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MViewHolder(View view) {
            super(view);
            this.image = (ImageView) itemView.findViewById(R.id.iv_shop_item_image_collect_list);

        }
    }

}