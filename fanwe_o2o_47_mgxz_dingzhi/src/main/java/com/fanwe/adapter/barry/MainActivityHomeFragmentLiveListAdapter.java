package com.fanwe.adapter.barry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.app.App;
import com.fanwe.home.model.Host;
import com.fanwe.home.model.Room;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.StringTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.utils.NetWorkStateUtil;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.List;

/**
 * Created by zlh on 2016/9/23.
 */
public class MainActivityHomeFragmentLiveListAdapter extends BarryBaseRecyclerAdapter{

    public static final String LIVE = "1";
    public static final String PLAY_BACK = "2";

    public MainActivityHomeFragmentLiveListAdapter(Activity activity, List datas) {
        super(activity, datas);
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

    private void setImageBgParams(RecyclerView.ViewHolder holder, int position){
        int width = getImageHeight();
        int height = width;
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(width, height);
        params.setMargins(dip2px(8), getMarginTop(), 0, 0);
        getHolder(holder).image.setLayoutParams(params);
    }

    public int getHeight(){
        return getItemCount() * (getImageHeight() + getMarginTop());
    }

    private int getMarginTop(){
        return dip2px(10);
    }

    private int getImageHeight(){
        return getScreenWidth() - dip2px(8 * 2);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        SDViewBinder.setImageView(getItem(position).getCover(), getHolder(holder).image);
        getHolder(holder).tvAdd.setText(getShopName(position));
        getHolder(holder).tvType.setText(getLiveType(position));
        getHolder(holder).tvType.setBackgroundResource(getLiveTypeColor(position));

    }

    private void setTags(RecyclerView.ViewHolder holder, int position){
        Host host = getItem(position).getHost();
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
        view.setPadding(dip2px(3), 0, dip2px(3), 0);
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 直播类型
     * @param position
     * @return
     */
    private String getLiveType(int position){
        try{
            return getItem(position).getLive_type().equals(LIVE) ? "正在直播" : "精彩视频";
        }catch (NullPointerException e){
            return "正在直播";
        }
    }

    /**
     * 直播类型
     * @param position
     * @return
     */
    private int  getLiveTypeColor(int position){
        try{
            return getItem(position).getLive_type().equals(LIVE) ? R.drawable.bg_orange : R.drawable.bg_grey_big;
        }catch (NullPointerException e){
            return  R.drawable.bg_orange ;
        }
    }

    /**
     * 地址
     * @param position
     * @return
     */
    private String getAddress(int position){
        try{
            return null == getItem(position).getLbs().getAddress() ? "" : getItem(position).getLbs().getAddress();
        }catch (NullPointerException e){
            return "";
        }
    }

    /**
     *  店名称。
     * @param position
     * @return
     */

    private String getShopName(int position){
        if(getItem(position).getLbs()==null){
            return "";
        }
        try{
            return null == getItem(position).getLbs().getShop_name() ? "" : getItem(position).getLbs().getShop_name();
        }catch (NullPointerException e){
            return "";
        }
    }
    @Override
    public Room getItem(int position) {
        return (Room)super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder)super.getHolder(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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

    public class MainActivityHomeFragmentLiveListAdapterListener extends BarryListener{

        public MainActivityHomeFragmentLiveListAdapterListener(BarryBaseRecyclerAdapter adapter,RecyclerView.ViewHolder holder, int position) {
            super(adapter,holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_bg_item_live:
                    clickItem();
                    break;
            }
        }

        private void clickItem(){
            if (TextUtils.isEmpty(App.getInstance().getToken())) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                BaseUtils.jumpToNewActivity(getActivity(), intent);
//                startActivity(intent);
                return;
            }
            //判断网络环境
            boolean connected = NetWorkStateUtil.isConnected(getActivity());
            if (!connected) {
                MGToast.showToast("没有网络,请检测网络环境!");
                return;
            }
            Room room = getAdapter().getItem(position);
            //分点播和直播 直播类型  1 表示直播，2表示点播
            String live_type = room.getLive_type();
            if ("1".equals(live_type)){
                //直播
                gotoLiveActivity(room);
            }else if ("2".equals(live_type)){
                //点播
                gotoPlayBackActivity(room);
            }else {
                //异常数据
                MGToast.showToast("异常数据");
                return;
            }
        }

        private void gotoLiveActivity(Room room){
            Intent intent = new Intent(getActivity(), LiveActivity.class);
            intent.putExtra(Constants.ID_STATUS, Constants.MEMBER);
            MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
            addCommonData(room);
            BaseUtils.jumpToNewActivity(getActivity(), intent);
//            startActivity(intent);
        }

        /**
         * 进入点播页面
         * @param room
         */
        private void gotoPlayBackActivity(Room room){
            addCommonData(room);
            String chat_room_id = room.getChat_room_id();//im的id
            String file_size = room.getFile_size();//文件大小
            String duration = room.getDuration();//时长
            String file_id = room.getFile_id();
            String vid = room.getVid();
            String playset = room.getPlayset();

            Intent intent=new Intent(getActivity(), PlayBackActivity.class);
            Bundle data=new Bundle();
            data.putString("chat_room_id",chat_room_id);
            data.putString("file_size",file_size);
            data.putString("duration",duration);
            data.putString("file_id",file_id);
            data.putString("vid",vid);
            data.putString("playset",playset);
            intent.putExtras(data);
//            startActivity(intent);
            BaseUtils.jumpToNewActivity(getActivity(), intent);
        }

        private void addCommonData(Room room){
            Host host = room.getHost();
            String nickName = App.getInstance().getUserNickName();
            String avatar = "";
            if (App.getInstance().getmUserCurrentInfo() != null) {
                UserCurrentInfo currentInfo = App.getInstance().getmUserCurrentInfo();
                if (currentInfo.getUserInfoNew() != null) {
                    avatar = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon();
                }
            }
            MySelfInfo.getInstance().setAvatar(avatar);
            MySelfInfo.getInstance().setNickName(nickName);
            MySelfInfo.getInstance().setJoinRoomWay(false);
            CurLiveInfo.setHostID(host.getHost_user_id());
            CurLiveInfo.setHostName(host.getNickname());

            CurLiveInfo.setHostAvator(room.getHost().getAvatar());

            App.getInstance().addLiveRoomIdList(room.getId());
            CurLiveInfo.setRoomNum(DataFormat.toInt(room.getId()));
            if (room.getLbs() != null) {
                CurLiveInfo.setShopID(room.getLbs().getShop_id());
                ModelStoreList modelStoreList = new ModelStoreList();
                modelStoreList.setShop_name(room.getLbs().getShop_name());
                modelStoreList.setId(room.getLbs().getShop_id());
                CurLiveInfo.setModelShop(modelStoreList);
            }
            CurLiveInfo.setLive_type(room.getLive_type());

            CurLiveInfo.setHostUserID(room.getHost().getUid());
//                CurLiveInfo.setMembers(item.getWatchCount() + 1); // 添加自己
            CurLiveInfo.setMembers(1); // 添加自己
//                CurLiveInfo.setAddress(item.getLbs().getAddress());
            if (room.getLbs() != null && !TextUtils.isEmpty(room.getLbs().getShop_id())) {
                CurLiveInfo.setShopID(room.getLbs().getShop_id());
            }
            CurLiveInfo.setAdmires(1);
        }

        @Override
        public MainActivityHomeFragmentLiveListAdapter getAdapter() {
            return (MainActivityHomeFragmentLiveListAdapter) super.getAdapter();
        }
    }

}
