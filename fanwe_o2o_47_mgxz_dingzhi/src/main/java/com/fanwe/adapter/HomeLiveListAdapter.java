package com.fanwe.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.home.model.Host;
import com.fanwe.home.model.Room;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.StringTool;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播列表适配器
 */

public class HomeLiveListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Room> datas;

    public HomeLiveListAdapter(Context mContext,
                               LayoutInflater layoutInflater, ArrayList<Room> datas) {
        this.mContext = mContext;
        this.inflater = layoutInflater;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder = null;
        if (null == convertView) {
            mHolder = new Holder();
            convertView = inflater.inflate(
                    R.layout.item_live_view_home_list, null);
            mHolder.ivBg = (ImageView) convertView.findViewById(R.id.iv_bg_item_live);
            mHolder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
            mHolder.tvAdd = (TextView) convertView.findViewById(R.id.tv_shop_item_live_list_home_fragment);
            mHolder.layoutTags = (LinearLayout) convertView.findViewById(R.id.layout_tags_item_live_list_home_fragment);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    private int maxLength = 15;

    private void setData(Holder mHolder, int position) {
        Room room = datas.get(position);
        ImageLoader.getInstance().displayImage(room.getCover(), mHolder.ivBg, null, null, null);
        if (!TextUtils.isEmpty(room.getLbs().getAddress())) {
            mHolder.tvAdd.setText(room.getLbs().getAddress());
        } else {
            mHolder.tvAdd.setText("");
        }
        //直播类型  1 表示直播，2表示点播
        String live_type = room.getLive_type();
        if ("1".equals(live_type)) {
            mHolder.tvType.setText("正在直播");
        } else if ("2".equals(live_type)) {
            mHolder.tvType.setText("点播");
        } else {
            //异常数据
            mHolder.tvType.setText("=_=");
        }
        //标签
        mHolder.layoutTags.removeAllViews();
        Host host = room.getHost();
        if (host != null) {
            List<String> tags = host.getTags();
            if (!SDCollectionUtil.isEmpty(tags)) {
                String totalStr = "";
                String tempStr = "";
                for (int i = 0; i < tags.size(); i++) {
                    if (i == 3) {
                        break;
                    }
                    String tagName = tags.get(i);
                    tempStr = totalStr;
                    totalStr = totalStr + tagName + " ";
                    if (totalStr.length() > maxLength) {
                        tagName = StringTool.getStringFixed(tagName, (maxLength - tempStr.length()), "");
                        mHolder.layoutTags.addView(generalTag(tagName));
                        break;
                    }
                    mHolder.layoutTags.addView(generalTag(tagName));
                }
            }
        }
    }

    /**
     * 生成标签
     *
     * @param tag
     * @return
     */
    private View generalTag(String tag) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = inflater.inflate(R.layout.tv_tag_item_live_home, null);
        TextView tvTag = (TextView) view.findViewById(R.id.tv_describe_item_live_list_home_fragment);
        tvTag.setText(tag);
        view.setLayoutParams(lp);
        return view;
    }

    private static class Holder {
        private ImageView ivBg;
        private TextView tvAdd;
        private TextView tvType;//直播类型  1 表示直播，2表示点播
        private LinearLayout layoutTags;
    }

}
