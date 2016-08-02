package com.miguo.live.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fanwe.CaptureResultWebActivity;
import com.fanwe.CityListActivity;
import com.fanwe.MyCaptureActivity;
import com.fanwe.StoreConfirmOrderActivity;
import com.fanwe.app.AppConfig;
import com.fanwe.base.CallbackView;
import com.fanwe.customview.BottomDialog;
import com.fanwe.fragment.HomeFragment;
import com.fanwe.fragment.MarketFragment;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveAuthBinding;
import com.fanwe.utils.Bimp;
import com.fanwe.utils.UriUtil;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.live.adapters.VisitImgAdapter;
import com.miguo.live.model.DataBindingLiveAuth;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getUpToken.ModelUpToken;
import com.miguo.live.presenters.LiveHttpHelper;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.AsyncRun;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播认证
 * Created by Administrator on 2016/8/1.
 */
public class LiveAuthActivity extends Activity implements VisitImgAdapter.AdddMore, CallbackView {
    DataBindingLiveAuth dataBindingLiveAuth;
    private Context mContext = LiveAuthActivity.this;
    private Uri mPhotoUri;
    private String mPhotoPath;
    public final int TAKE_PHOTO = 0x8;
    public final int SELECT_FILE_CODE = 0x32;
    private GridView mGridView;
    private VisitImgAdapter mVisitImgAdapter;
    private ArrayList<String> datas;
    private LiveHttpHelper liveHttpHelper;
    private UploadManager uploadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveAuthBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_auth);
        mGridView = (GridView) findViewById(R.id.gridView_live_auth);
        dataBindingLiveAuth = new DataBindingLiveAuth();
        binding.setLive(dataBindingLiveAuth);

        preData();
        setListener();
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_city_live_auth:
                Intent intent = new Intent(mContext, CityListActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.iv_arrow_left_bar:
                finish();
                break;
            case R.id.iv_beauty_live_auth:
                dataBindingLiveAuth.mode.set(dataBindingLiveAuth.BEAUTY);
                break;
            case R.id.iv_smart_live_auth:
                dataBindingLiveAuth.mode.set(dataBindingLiveAuth.SMART);
                break;
            case R.id.iv_excellent_live_auth:
                dataBindingLiveAuth.mode.set(dataBindingLiveAuth.EXCELLENT);
                break;
            case R.id.btn_submit_live_auth:
//                liveHttpHelper.getBussDictionInfo("Client");
//                liveHttpHelper.getUpToken();
                liveHttpHelper.postHostInfo("12b9d278-53e9-11e6-beb8-9e71128cae77", "13123211253", "http://ob23v88s3.bkt.clouddn.com/FuKbWokGhm0tzgKc5JinPh3rBdXO", "123", "f4564d66-53e8-11e6-beb8-9e72328cae77");
//                finish();
                break;
        }
    }

    private void preData() {
        liveHttpHelper = new LiveHttpHelper(mContext, this);
        uploadManager = new UploadManager();
        datas = new ArrayList<>();
        datas.add("add");
        mVisitImgAdapter = new VisitImgAdapter(mContext, getLayoutInflater(), datas);
        mGridView.setAdapter(mVisitImgAdapter);
        mVisitImgAdapter.setAddMore(this);
    }

    private void setListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(datas.get(position))) {
                    Intent intent = new Intent(mContext, ImgViewActivity.class);
                    intent.putExtra("path", datas.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    private void showChooseDialog() {
        String[] arr = new String[2];
        arr[0] = "拍    照";
        arr[1] = "从相册中选择";
        new BottomDialog(this, "", arr, new BottomDialog.DialogClickListner() {

            @Override
            public void onItemClick(int position, String item) {
                if (position == 0) {
                    takePhoto();
                } else if (position == 1) {
                    pickPhoto();
                }
            }

            @Override
            public void onCancelClick() {
            }
        }).show();
    }

    public String getPath(String name) {
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
        return sdcardDataDir += "/" + name;
    }

    public void takePhoto() {
        String name = "uploadimg.jpg";
        mPhotoPath = getPath(name);
        File file = new File(mPhotoPath);
        if (file.exists()) {
            file.delete();
        }
        mPhotoUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, SELECT_FILE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 8888) {
            dataBindingLiveAuth.city.set(AppRuntimeWorker.getCity_name());
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Bitmap temp = null;
        if (requestCode == TAKE_PHOTO) {
        } else if (requestCode == SELECT_FILE_CODE) {
            mPhotoUri = data.getData();
            if (Build.VERSION.SDK_INT >= 19) {
                mPhotoPath = UriUtil.getImageAbsolutePath(this, mPhotoUri);
                mPhotoUri = Uri.fromFile(new File(mPhotoPath));
            }
        }
        try {
            temp = Bimp.revitionImageSize(UriUtil.getImageAbsolutePath(LiveAuthActivity.this, mPhotoUri));
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

            datas.add(fileTempPath);

            datas.remove("add");
            if (datas.size() != 3)
                datas.add("add");

            mVisitImgAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            temp = null;
        }
    }

    @Override
    public void add() {
        showChooseDialog();
    }

    @Override
    public void delete(int position) {
        datas.remove(position);
        if (!datas.contains("add"))
            datas.add("add");
        mVisitImgAdapter.notifyDataSetChanged();
    }

    private void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    String uploadToken;

    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.UP_TOKEN.equals(method)) {
            ArrayList<ModelUpToken> tokens = (ArrayList<ModelUpToken>) datas;
            if (!SDCollectionUtil.isEmpty(tokens)) {
                uploadToken = tokens.get(0).getUptoken();
                if (!TextUtils.isEmpty(uploadToken)) {
                    uploadFile();
                }
            }
        }

    }

    ArrayList<String> fileKeys = new ArrayList<>();

    private void uploadFile() {
        fileKeys.clear();
        for (int i = 0; i < datas.size(); i++) {
            File uploadFile = new File(datas.get(i));
            uploadManager.put(uploadFile, null, uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo respInfo,
                                             JSONObject jsonData) {
                            if (respInfo.isOK()) {
                                try {
                                    String fileKey = jsonData.getString("key");
                                    if (!TextUtils.isEmpty(fileKey)) {
                                        fileKeys.add(fileKey);
                                    }
                                } catch (JSONException e) {
                                }
                            } else {

                            }
                        }
                    }, null);
        }
        Log.d("fileKeys", fileKeys.toString());
    }

    @Override
    public void onFailue(String responseBody) {

    }

}