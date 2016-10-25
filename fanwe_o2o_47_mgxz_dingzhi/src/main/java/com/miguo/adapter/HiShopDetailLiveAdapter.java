package com.miguo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.app.App;
import com.fanwe.home.model.Host;
import com.fanwe.home.model.Room;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.utils.DataFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.entity.HiShopDetailBean;
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
 * Created by zlh/Barry/狗蛋哥 on 2016/10/21.
 * 商家主页 最底部 在现场
 */
public class HiShopDetailLiveAdapter extends BarryBaseRecyclerAdapter{

    public HiShopDetailLiveAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.activity_hishop_detail_live_item, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return new HiShopDetailLiveAdapterListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).image.setOnClickListener(listener);
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        updateImageHeight(holder, position);
    }

    private void updateImageHeight(RecyclerView.ViewHolder holder, int position){
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(getImageHeight(), getImageHeight());
        getHolder(holder).image.setLayoutParams(params);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        try{
            if(getItem(position).getCover().contains("http:")){
                SDViewBinder.setImageView(getItem(position).getCover(), getHolder(holder).image);
            }

            getHolder(holder).type.setText(getItem(position).isLive() ? "正在直播" : "点播回放");
            getHolder(holder).type.setBackgroundResource(getItem(position).isLive() ? R.drawable.shape_cricle_bg_yellow: R.drawable.shape_cricle_bg_black_alphe_60);

            getHolder(holder).localtion.setText(getItem(position).getAddress());
        }catch (Exception e){

        }


    }

    public int getItemHeight(){
       return getImageHeight() * getLine();
    }

    public int getLine(){
        return getItemCount() / 3 + (getItemCount() % 3 > 0 ? 1 : 0);
    }

    public int getImageHeight(){
        int screenWidth = getScreenWidth();
        return screenWidth / 3;
    }

    @Override
    public HiShopDetailBean.Result.Live getItem(int position) {
        return (HiShopDetailBean.Result.Live)super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder)super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.image)
        ImageView image;

        @ViewInject(R.id.type)
        TextView type;

        @ViewInject(R.id.location)
        TextView localtion;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    class HiShopDetailLiveAdapterListener extends BarryListener{


        public HiShopDetailLiveAdapterListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.image:
                    clickImage();
                    break;
            }
        }

        private void clickImage(){
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
            HiShopDetailBean.Result.Live room = getAdapter().getItem(position);
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

        private void gotoLiveActivity(HiShopDetailBean.Result.Live room){
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
        private void gotoPlayBackActivity(HiShopDetailBean.Result.Live room){
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

        private void addCommonData(HiShopDetailBean.Result.Live room){
            HiShopDetailBean.Result.Host host = room.getHost();
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


//            if (room.getLbs() != null) {
//                CurLiveInfo.setShopID(room.getLbs().getShop_id());
//                ModelStoreList modelStoreList = new ModelStoreList();
//                modelStoreList.setShop_name(room.getLbs().getShop_name());
//                modelStoreList.setId(room.getLbs().getShop_id());
//                CurLiveInfo.setModelShop(modelStoreList);
//            }


            CurLiveInfo.setLive_type(room.getLive_type());

            CurLiveInfo.setHostUserID(room.getHost().getUid());
//                CurLiveInfo.setMembers(item.getWatchCount() + 1); // 添加自己
            CurLiveInfo.setMembers(1); // 添加自己
//                CurLiveInfo.setAddress(item.getLbs().getAddress());


//            if (room.getLbs() != null && !TextUtils.isEmpty(room.getLbs().getShop_id())) {
//                CurLiveInfo.setShopID(room.getLbs().getShop_id());
//            }



            CurLiveInfo.setAdmires(1);
        }

        @Override
        public HiShopDetailLiveAdapter getAdapter() {
            return (HiShopDetailLiveAdapter)super.getAdapter();
        }
    }

}
