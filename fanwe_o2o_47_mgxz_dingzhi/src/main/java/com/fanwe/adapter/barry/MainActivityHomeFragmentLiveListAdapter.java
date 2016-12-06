package com.fanwe.adapter.barry;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.StringTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.model.getLiveListNew.ModelHost;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.views.LiveUtil;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.utils.DisplayUtil;
import com.miguo.utils.NetWorkStateUtil;

import java.util.List;

/**
 * Created by zlh on 2016/9/23.
 */
public class MainActivityHomeFragmentLiveListAdapter extends BarryBaseRecyclerAdapter {

    public static final String LIVE = "1";
    public static final String LIVE_PLAY_BACK = "1";
    public static final String PLAY_BACK = "2";
    public int width =400;

    public MainActivityHomeFragmentLiveListAdapter(Activity activity, List datas) {
        super(activity, datas);
        width = BaseUtils.getWidth(activity);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.activity_main_fragment_main_item_live_view_home_list, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return new MainActivityHomeFragmentLiveListAdapterListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).image.setOnClickListener(listener);
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        getHolder(holder).layoutTags.removeAllViews();
        setTags(holder, position);
        setImageBgParams(holder, position);
    }

    private void setImageBgParams(RecyclerView.ViewHolder holder, int position) {
        int width = SDViewUtil.getScreenWidth();
        int height = getImageHeight();
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(width, height);
        params.setMargins(0, getMarginTop(), 0, 0);
        getHolder(holder).image.setLayoutParams(params);
    }

    public int getHeight() {
        return getItemCount() * (getImageHeight() + getMarginTop());
    }

    private int getMarginTop() {
        return dip2px(10);
    }

    private int getImageHeight() {
        return dip2px(228);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        ModelRoom room = (ModelRoom) datas.get(position);
        String url = "";
        if(!TextUtils.isEmpty(room.getCover())){
            url = DisplayUtil.qiniuUrlExchange(room.getCover(),400,228);
        }
        SDViewBinder.setImageView(url, getHolder(holder).image);
        getHolder(holder).tvAdd.setText(getShopName(room));
        getHolder(holder).tvType.setText(LiveUtil.getLiveType(room));
        getHolder(holder).tvType.setBackgroundResource(LiveUtil.getLiveTypeColor(room));

    }

    private void setTags(RecyclerView.ViewHolder holder, int position) {
        ModelHost host = getItem(position).getHost();
        int maxLength = 15;
        if (host != null) {
            List<String> tags = host.getTags();
            if (!SDCollectionUtil.isEmpty(tags)) {
                String totalStr = "";
                String tempStr;
                for (int i = 0; i < tags.size(); i++) {
                    if (i == 3) {
                        break;
                    }
                    String tagName = tags.get(i);
                    tempStr = totalStr;
                    totalStr = totalStr + tagName + " ";
                    if (totalStr.length() > maxLength) {
                        tagName = StringTool.getStringFixed(tagName, (maxLength - tempStr.length()), "");
                        getHolder(holder).layoutTags.addView(generalTag(tagName));
                        break;
                    }
                    getHolder(holder).layoutTags.addView(generalTag(tagName));
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
        tvTag.setTextSize(BaseUtils.px2dip(getActivity(),24));
        view.setPadding(dip2px(3), 0, dip2px(3), 0);
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 地址
     *
     * @param position
     * @return
     */
    private String getAddress(int position) {
        try {
            return null == getItem(position).getLbs().getAddress() ? "" : getItem(position).getLbs().getAddress();
        } catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * 店名称。
     *
     * @param room
     * @return
     */

    private String getShopName(ModelRoom room) {
        if (room.getLbs() == null) {
            return "";
        }
        try {
            return null == room.getLbs().getShop_name() ? "" : room.getLbs().getShop_name();
        } catch (NullPointerException e) {
            return "";
        }
    }

    @Override
    public ModelRoom getItem(int position) {
        return (ModelRoom) super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder) super.getHolder(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.iv_bg_item_live)
        ImageView image;
        @ViewInject(R.id.tv_shop_item_live_list_home_fragment)
        TextView tvAdd;
        @ViewInject(R.id.tv_type)
        TextView tvType;//直播类型  1 表示直播，2表示点播
        @ViewInject(R.id.layout_tags_item_live_list_home_fragment)
        LinearLayout layoutTags;


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class MainActivityHomeFragmentLiveListAdapterListener extends BarryListener {

        public MainActivityHomeFragmentLiveListAdapterListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_bg_item_live:
                    clickItem();
                    break;
            }
        }

        private void clickItem() {
            if (TextUtils.isEmpty(App.getInstance().getToken())) {
                Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
                BaseUtils.jumpToNewActivity(getActivity(), intent);
                return;
            }
            //判断网络环境
            boolean connected = NetWorkStateUtil.isConnected(getActivity());
            if (!connected) {
                MGToast.showToast("没有网络,请检测网络环境!");
                return;
            }
            ModelRoom room = getAdapter().getItem(position);
            LiveUtil.clickRoom(room, getActivity());
        }

        @Override
        public MainActivityHomeFragmentLiveListAdapter getAdapter() {
            return (MainActivityHomeFragmentLiveListAdapter) super.getAdapter();
        }
    }
}
