package com.fanwe.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.fanwe.base.CallbackView;
import com.fanwe.common.HttpManagerX;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.getUpgradeVersion.ModelVersion;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.common.update.NoHttpRedirectHandler;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.OtherUtils;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;

import java.io.File;
import java.util.List;

/**
 * 更新服务
 *
 * @author yhz
 */
public class AppUpgradeService extends Service implements CallbackView {
    public static final String EXTRA_SERVICE_START_TYPE = "extra_service_start_type";
    public static final int mNotificationId = 100;
    private static final int DEFAULT_START_TYPE = 0;
    private String localFileName="米果小站";
    private int mStartType = DEFAULT_START_TYPE; // 0代表启动app时候程序自己检测，1代表用户手动检测版本
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private PendingIntent mPendingIntent;
    private ModelVersion modelVersion;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getIntentData(intent);
        requestServerVersion();
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestServerVersion() {
        CommonHttpHelper upgradeAppHelper=new CommonHttpHelper(this,this);
        upgradeAppHelper.getUpgradeAPK();
    }

    private void getIntentData(Intent intent) {
        if (intent != null) {
            mStartType = intent.getIntExtra(EXTRA_SERVICE_START_TYPE, DEFAULT_START_TYPE);
        }
    }

    private String urlApk="";

    protected void dealResult() {
        if (modelVersion == null) {
            MGLog.e("intent error");
            return;
        }
        urlApk=modelVersion.getDown_url();
        if (TextUtils.isEmpty(urlApk) || !urlApk.contains("http")){
            MGLog.e("upgrade","apk url error!");
            stopSelf();
        }
        //TODO  Online Code
        if ("1".equals(modelVersion.getHas_upgrade())){
            if (TextUtils.isEmpty(urlApk)){
                MGLog.e("未找到下载地址");
                stopSelf();
            }else {
                //TODO update
                showDialogUpgrade();
            }
        }else if(mStartType == 1) {
            MGToast.showToast("当前已是最新版本!");
            stopSelf();
        }

    }

    private void showDialogUpgrade() {
        if (modelVersion == null) {
            return;
        }
        SDDialogConfirm dialog = new SDDialogConfirm();
        if ("1".equals(modelVersion.getForced_upgrade())) {
            dialog.setTextCancel(null).setCancelable(false);
        }
        String upgrade_content = modelVersion.getUpgrade().replace("\\n","\n");
        if (TextUtils.isEmpty(upgrade_content)){
            upgrade_content="最新版本 米果小站"+modelVersion.getServer_version();
        }
        String title = modelVersion.getTitle();
        if (TextUtils.isEmpty(title)){
            title="发现新版本";
        }
        dialog.setTextContent(upgrade_content).setTextTitle(title);
        dialog.setmListener(new SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {

            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                startDownload();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {

            }
        }).show();
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = R.drawable.icon;
        mNotification.tickerText = localFileName + "正在下载中";
        mNotification.contentView = new RemoteViews(getApplication().getPackageName(), R.layout
                .service_download_view);

        Intent completingIntent = new Intent();
        completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        completingIntent.setClass(getApplication().getApplicationContext(), AppUpgradeService
                .class);
        mPendingIntent = PendingIntent.getActivity(AppUpgradeService.this, R.string.app_name,
                completingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.contentIntent = mPendingIntent;

        mNotification.contentView.setTextViewText(R.id.upgradeService_tv_appname, localFileName);
        mNotification.contentView.setTextViewText(R.id.upgradeService_tv_status, "下载中");
        mNotification.contentView.setProgressBar(R.id.upgradeService_pb, 100, 0, false);
        mNotification.contentView.setTextViewText(R.id.upgradeService_tv, "0%");
        mNotification.contentView.setTextColor(R.id.upgradeService_tv_status, Color.BLACK);
        mNotification.contentView.setTextColor(R.id.upgradeService_tv_appname, Color.BLACK);
        mNotification.contentView.setTextColor(R.id.upgradeService_tv, Color.BLACK);

        mNotificationManager.cancel(mNotificationId);
        mNotificationManager.notify(mNotificationId, mNotification);

    }

    private void startDownload() {
        final String target = OtherUtils.getDiskCacheDir(getApplicationContext(), "") + localFileName;
        deleteOldApk();
        //mModel.getFilename()
        HttpManagerX.getHttpUtils().configHttpRedirectHandler(new NoHttpRedirectHandler(urlApk)).download(urlApk, target, true, new
                RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        initNotification();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        int progress = (int) ((current * 100) / (total));
                        mNotification.contentView.setProgressBar(R.id.upgradeService_pb, 100,
                                progress,
                                false);
                        mNotification.contentView.setTextViewText(R.id.upgradeService_tv,
                                progress + "%");
                        mNotificationManager.notify(mNotificationId, mNotification);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        dealDownloadSuccess(responseInfo.result.getAbsolutePath());
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (error != null) {
                            if (error.getExceptionCode() == 416) // 文件已经存在
                            {
                                dealDownloadSuccess(target);
                                return;
                            }
                        }
                        MGToast.showToast("下载失败");
                    }

                });

    }

    /*删掉之前的安装包*/
    private void deleteOldApk() {
        String target = OtherUtils.getDiskCacheDir(getApplicationContext(), "");
        File cacheDir=new File(target);
        if (cacheDir.isDirectory()){
            Log.e("test","cacheDir: "+cacheDir.getAbsolutePath());
            File[] files = cacheDir.listFiles();
            for (File file : files) {
                String name = file.getName();
                if (name.contains("米果小站_")){
                    Log.d("test","deleteFile: "+file.getName());
                    file.delete();
                }
            }
        }
    }

    public void dealDownloadSuccess(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            mNotification.contentView.setViewVisibility(R.id.upgradeService_pb, View.GONE);
            mNotification.defaults = Notification.DEFAULT_SOUND;
            mNotification.contentIntent = mPendingIntent;
            mNotification.contentView.setTextViewText(R.id.upgradeService_tv_status, "下载完成");
            mNotification.contentView.setTextViewText(R.id.upgradeService_tv, "100%");
            mNotificationManager.notify(mNotificationId, mNotification);
            mNotificationManager.cancel(mNotificationId);
            if (!filePath.endsWith(".apk")){
                File  oldFile=new File(filePath);
                filePath+=".apk";
                File newFile=new File(filePath);
                oldFile.renameTo(newFile);
            }
            SDPackageUtil.installApkPackage(filePath);
            MGToast.showToast("下载完成");
            stopSelf();
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (CommonConstants.UPGRADE_VERSION.equals(method)) {
            if (datas != null) {
                modelVersion = (ModelVersion) datas.get(0);
                localFileName="米果小站_"+modelVersion.getServer_version();
                dealResult();
            } else {
                MGLog.e("更新数据错误!");
                stopSelf();
            }
        }
    }

    @Override
    public void onFailue(String responseBody) {
        stopSelf();
    }
}