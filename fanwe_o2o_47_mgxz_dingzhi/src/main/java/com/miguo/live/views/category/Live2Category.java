//package com.miguo.live.views.category;
//
//import android.Manifest;
//import android.animation.ObjectAnimator;
//import android.app.Dialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.fanwe.seller.presenters.SellerHttpHelper;
//import com.lidroid.xutils.ViewUtils;
//import com.miguo.live.adapters.HeadTopAdapter;
//import com.miguo.live.adapters.LiveChatMsgListAdapter;
//import com.miguo.live.adapters.PagerBaoBaoAdapter;
//import com.miguo.live.adapters.PagerRedPacketAdapter;
//import com.miguo.live.model.LiveChatEntity;
//import com.miguo.live.presenters.LiveCommonHelper;
//import com.miguo.live.presenters.LiveHttpHelper;
//import com.miguo.live.presenters.TencentHttpHelper;
//import com.miguo.live.views.LiveOrientationHelper;
//import com.miguo.live.views.LiveRecordDialogHelper;
//import com.miguo.live.views.LiveUtil;
//import com.miguo.live.views.customviews.HostMeiToolView;
//import com.miguo.live.views.customviews.HostRedPacketTimeView;
//import com.miguo.live.views.customviews.HostTopView;
//import com.miguo.live.views.customviews.UserHeadTopView;
//import com.miguo.live.views.listener.Live2Listener;
//import com.tencent.av.TIMAvManager;
//import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
//import com.tencent.qcloud.suixinbo.model.LiveInfoJson;
//import com.tencent.qcloud.suixinbo.model.MySelfInfo;
//import com.tencent.qcloud.suixinbo.presenters.EnterLiveHelper;
//import com.tencent.qcloud.suixinbo.presenters.LiveHelper;
//import com.tencent.qcloud.suixinbo.presenters.LoginHelper;
//import com.tencent.qcloud.suixinbo.presenters.ProfileInfoHelper;
//import com.tencent.qcloud.suixinbo.presenters.viewinface.EnterQuiteRoomView;
//import com.tencent.qcloud.suixinbo.presenters.viewinface.LiveView;
//import com.tencent.qcloud.suixinbo.utils.Constants;
//import com.tencent.qcloud.suixinbo.utils.SxbLog;
//import com.tencent.qcloud.suixinbo.views.customviews.HeartLayout;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Created by zlh on 2016/8/28.
// */
//public class Live2Category extends Category implements EnterQuiteRoomView,LiveView {
//
//    String TAG = "Live2Category";
//
//    private EnterLiveHelper mEnterRoomHelper;
////    private LiveListViewHelper mLiveListViewHelper;
//    private LiveHelper mLiveHelper;
//
//    private ArrayList<String> mRenderUserList = new ArrayList<>();
//
//    public Live2Category(AppCompatActivity activity) {
//        super(activity);
//    }
//
//    @Override
//    protected void initFirst() {
//
//    }
//
//    @Override
//    protected void findCategoryViews() {
//        ViewUtils.inject(this, getActivity());
//    }
//
//    @Override
//    protected void initThisListener() {
//        listener = new Live2Listener(this);
//    }
//
//    @Override
//    protected void setThisListener() {
//
//    }
//
//    @Override
//    protected void init() {
//        checkPermission();
//
//        //进出房间的协助类
//        mEnterRoomHelper = new EnterLiveHelper(getActivity(), this);
//        //房间内的交互协助类
//        mLiveHelper = new LiveHelper(getActivity(), this);
//
////        mLiveListViewHelper = new LiveListViewHelper(this);
//
//    }
//
//    @Override
//    protected void initViews() {
//
//    }
//
//
//    /**
//     * 检查权限
//     */
//    private final int REQUEST_PHONE_PERMISSIONS = 0;
//    void checkPermission() {
//        final List<String> permissionsList = new ArrayList<>();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
//                permissionsList.add(Manifest.permission.CAMERA);
//            if ((getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
//                permissionsList.add(Manifest.permission.RECORD_AUDIO);
//            if ((getActivity().checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
//                permissionsList.add(Manifest.permission.WAKE_LOCK);
//            if ((getActivity().checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
//                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
//            if (permissionsList.size() != 0) {
//                getActivity().requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
//                        REQUEST_PHONE_PERMISSIONS);
//            }
//        }
//    }
//
//
//    private LinearLayout mHostCtrView, mNomalMemberCtrView, mVideoMemberCtrlView, mBeautySettings;
//
//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            //AvSurfaceView 初始化成功
//            if (action.equals(Constants.ACTION_SURFACE_CREATED)) {
//                //打开摄像头
//                if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
//                    mLiveHelper.openCameraAndMic();
//                }
//
//            }
//
//            if (action.equals(Constants.ACTION_CAMERA_OPEN_IN_LIVE)) {//有人打开摄像头
//                ArrayList<String> ids = intent.getStringArrayListExtra("ids");
//                //如果是自己本地直接渲染
//                for (String id : ids) {
//                    if (!mRenderUserList.contains(id)) {
//                        mRenderUserList.add(id);
//                    }
//                    updateHostLeaveLayout();
//
//                    if (id.equals(MySelfInfo.getInstance().getId())) {
//                        showVideoView(true, id);
//                        return;
////                        ids.remove(id);
//                    }
//                }
//                //其他人一并获取
////                SxbLog.d(TAG, LogConstants.ACTION_VIEWER_SHOW + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "somebody open camera,need req data"
////                        + LogConstants.DIV + LogConstants.STATUS.SUCCEED + LogConstants.DIV + "ids " + ids.toString());
//                int requestCount = CurLiveInfo.getCurrentRequestCount();
//                mLiveHelper.requestViewList(ids);
//                requestCount = requestCount + ids.size();
//                CurLiveInfo.setCurrentRequestCount(requestCount);
////                }
//            }
//
//            if (action.equals(Constants.ACTION_CAMERA_CLOSE_IN_LIVE)) {//有人关闭摄像头
//                ArrayList<String> ids = intent.getStringArrayListExtra("ids");
//                //如果是自己本地直接渲染
//                for (String id : ids) {
//                    mRenderUserList.remove(id);
//                }
//                updateHostLeaveLayout();
//            }
//
//            if (action.equals(Constants.ACTION_SWITCH_VIDEO)) {//点击成员回调
//                backGroundId = intent.getStringExtra(Constants.EXTRA_IDENTIFIER);
//                SxbLog.v(TAG, "switch video enter with id:" + backGroundId);
//
//                updateHostLeaveLayout();
//
//                if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {//自己是主播
//                    if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//背景是自己
//                        mHostCtrView.setVisibility(View.VISIBLE);
//                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
//                    } else {//背景是其他成员
//                        mHostCtrView.setVisibility(View.INVISIBLE);
//                        mVideoMemberCtrlView.setVisibility(View.VISIBLE);
//                    }
//                } else {//自己成员方式
//                    if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//背景是自己
//                        mVideoMemberCtrlView.setVisibility(View.VISIBLE);
//                        mNomalMemberCtrView.setVisibility(View.INVISIBLE);
//                    } else if (backGroundId.equals(CurLiveInfo.getHostID())) {//主播自己
//                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
//                        mNomalMemberCtrView.setVisibility(View.VISIBLE);
//                    } else {
//                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
//                        mNomalMemberCtrView.setVisibility(View.INVISIBLE);
//                    }
//
//                }
//
//            }
//            if (action.equals(Constants.ACTION_HOST_LEAVE)) {//主播结束
//                quiteLivePassively();
//            }
//
//
//        }
//    };
//
//    /**
//     * 被动退出直播
//     */
//    private void quiteLivePassively() {
//        Toast.makeText(getActivity(), "Host leave Live ", Toast.LENGTH_SHORT);
//        mLiveHelper.perpareQuitRoom(false);
////        mEnterRoomHelper.quiteLive();
//    }
//
//    private boolean bInAvRoom = false, bSlideUp = false, bDelayQuit = false;
//    private String backGroundId;
//    private LinearLayout mHostLayout, mHostLeaveLayout;
//
//
//    private void updateHostLeaveLayout() {
//        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
//            return;
//        } else {
//            // 退出房间或主屏为主播且无主播画面显示主播已离开
//            if (!bInAvRoom || (CurLiveInfo.getHostID().equals(backGroundId) && !mRenderUserList.contains(backGroundId))) {
//                mHostLeaveLayout.setVisibility(View.VISIBLE);
//            } else {
//                mHostLeaveLayout.setVisibility(View.GONE);
//            }
//        }
//    }
//
//
//    private void registerReceiver() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constants.ACTION_SURFACE_CREATED);
//        intentFilter.addAction(Constants.ACTION_HOST_ENTER);
//        intentFilter.addAction(Constants.ACTION_CAMERA_OPEN_IN_LIVE);
//        intentFilter.addAction(Constants.ACTION_CAMERA_CLOSE_IN_LIVE);
//        intentFilter.addAction(Constants.ACTION_SWITCH_VIDEO);
//        intentFilter.addAction(Constants.ACTION_HOST_LEAVE);
//        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
//
//    }
//
//    private void unregisterReceiver() {
//        getActivity().unregisterReceiver(mBroadcastReceiver);
//    }
//
//
//}
