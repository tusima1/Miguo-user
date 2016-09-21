package com.fanwe.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.adapter.HomeLiveListAdapter;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.customview.SDGridViewInScroll;
import com.fanwe.home.model.Host;
import com.fanwe.home.model.Room;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.utils.DataFormat;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.utils.NetWorkStateUtil;
import com.tencent.imcore.Context;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class HomeFragmentLiveList extends BaseFragment implements CallbackView {
    private View view;
    private TextView tvTitle;
    private SDGridViewInScroll mSDGridViewInScroll;
    private HomeLiveListAdapter mLiveViewAdapter;
    private ArrayList<Room> datas = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private LiveHttpHelper liveHttpHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        // 可以初始化除了view之外的东西
        liveHttpHelper = new LiveHttpHelper(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            initView(inflater, container);
            preView();
            setListener();
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从mView删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void preView() {
        mLiveViewAdapter = new HomeLiveListAdapter(getActivity(), getActivity().getLayoutInflater(), datas);
        mSDGridViewInScroll.setAdapter(mLiveViewAdapter);
    }

    private void setListener() {
        mSDGridViewInScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.isEmpty(App.getInstance().getToken())) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                //判断网络环境
                boolean connected = NetWorkStateUtil.isConnected(getContext());
                if (!connected) {
                    MGToast.showToast("没有网络,请检测网络环境!");
                    return;
                }
                Room room = datas.get(position);
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
        });
    }
    private void gotoLiveActivity(Room room){
        Intent intent = new Intent(getActivity(), LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.MEMBER);
        MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
        addCommonData(room);
        startActivity(intent);
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
        App.getInstance().setCurrentRoomId(room.getId());
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
        startActivity(intent);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = (View) inflater
                .inflate(R.layout.fragment_home_live_list, container, false);
        mSDGridViewInScroll = (SDGridViewInScroll) view.findViewById(R.id.gridView_home_fragment_list);
        tvTitle = (TextView) view.findViewById(R.id.tv_title_live_list);
    }


    public void updateTitle(String title) {
        if (tvTitle != null) {
            if (TextUtils.isEmpty(title)) {
                title = "";
            }
            tvTitle.setText(title);
        }
    }

    public void updateView(boolean isRefresh, List<Room> rooms) {
        if (isRefresh) {
            datas.clear();
        }
        if (!SDCollectionUtil.isEmpty(rooms))
            datas.addAll(rooms);
        mLiveViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
    }

    @Override
    public void onFailue(String responseBody) {

    }
}
