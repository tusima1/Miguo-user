package com.fanwe;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView.ScaleType;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.Bimp;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getBussDictionInfo.ModelBussDictionInfo;
import com.miguo.live.model.getUpToken.ModelUpToken;
import com.miguo.live.presenters.LiveHttpHelper;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.sunday.eventbus.SDEventManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class UploadUserHeadActivity extends BaseActivity implements CallbackView2, CallbackView {

    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

    @ViewInject(R.id.iv_image)
    private PhotoView mIv_image;

    private String mStrUrl;
    private File mFileOriginal;
    private LiveHttpHelper liveHttpHelper;
    private UserHttpHelper userHttpHelper;
    private UploadManager uploadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_upload_avatar);
        init();
    }

    private void init() {
        uploadManager = new UploadManager();
        userHttpHelper = new UserHttpHelper(this, this);
        liveHttpHelper = new LiveHttpHelper(this, this);
        initTitle();
        initImageView();
//        initImageFileCompresser();
        getIntentData();
    }

    private void initImageView() {
        mIv_image.setScaleType(ScaleType.FIT_CENTER);
    }

    private void getIntentData() {
        mStrUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        if (isEmpty(mStrUrl)) {
            MGToast.showToast("图片不存在");
            finish();
        }

        Bitmap temp = null;
        try {
            temp = Bimp.revitionImageSize(mStrUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String sdcardDataDir = "";
            if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MiGuoPhoto/");
                if (!file.exists()) {
                    if (file.mkdirs()) {
                        sdcardDataDir = file.getAbsolutePath();
                    }
                } else {
                    sdcardDataDir = file.getAbsolutePath();
                }
            }
            String fileTempPath = sdcardDataDir + "/" + System.currentTimeMillis() + ".jpg";
            //保存图片
            Bimp.saveFile(temp, fileTempPath);

            mStrUrl = fileTempPath;
            mFileOriginal = new File(fileTempPath);
            if (!mFileOriginal.exists()) {
                MGToast.showToast("图片不存在");
                finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            temp = null;
        }

        SDViewBinder.setImageView(mIv_image, "file:///" + mStrUrl);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("上传头像");

        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("上传");
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        liveHttpHelper.getBussDictionInfo("Client");
        SDDialogManager.showProgressDialog("正在上传");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    String urlQiNiu, uploadToken, urlIcon;


    private void uploadFile() {
        uploadManager.put(mFileOriginal, null, uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, com.qiniu.android.http.ResponseInfo respInfo,
                                         JSONObject jsonData) {
                        if (respInfo.isOK()) {
                            try {
                                String fileKey = jsonData.getString("key");
                                if (!TextUtils.isEmpty(fileKey)) {
                                    urlIcon = urlQiNiu + fileKey;
                                    userHttpHelper.updateUserInfo("icon", urlIcon);
                                }
                            } catch (JSONException e) {
                            }
                        } else {
                        }
                    }
                }, null);
    }

    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.BUSS_DICTION_INFO.equals(method)) {
            for (ModelBussDictionInfo modelBussDictionInfo : (ArrayList<ModelBussDictionInfo>) datas) {
                if ("DomainName".equals(modelBussDictionInfo.getDic_value())) {
                    urlQiNiu = modelBussDictionInfo.getDic_mean();
                }
            }
            liveHttpHelper.getUpToken();
        } else if (LiveConstants.UP_TOKEN.equals(method)) {
            ArrayList<ModelUpToken> tokens = (ArrayList<ModelUpToken>) datas;
            if (!SDCollectionUtil.isEmpty(tokens)) {
                uploadToken = tokens.get(0).getUptoken();
                if (!TextUtils.isEmpty(uploadToken)) {
                    uploadFile();
                }
            }
        } else if (UserConstants.USER_INFO_METHOD.equals(method)) {
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SDDialogManager.dismissProgressDialog();
                    MGToast.showToast("修改头像成功");
                    finish();
                    App.getInstance().setUserIcon(urlIcon);
                    SDEventManager.post(EnumEventTag.UPLOAD_USER_HEAD_SUCCESS.ordinal());
                    break;
            }
        }
    };

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

}
