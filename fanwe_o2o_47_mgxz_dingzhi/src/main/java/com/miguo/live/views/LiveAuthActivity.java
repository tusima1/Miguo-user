package com.miguo.live.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.fanwe.CityListActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.customview.BottomDialog;
import com.fanwe.library.utils.SDCollectionUtil;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveAuthBinding;
import com.fanwe.utils.Bimp;
import com.fanwe.utils.StringTool;
import com.fanwe.utils.UriUtil;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.live.adapters.VisitImgAdapter;
import com.miguo.live.model.DataBindingLiveAuth;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getBussDictionInfo.ModelBussDictionInfo;
import com.miguo.live.model.getUpToken.ModelUpToken;
import com.miguo.live.presenters.LiveHttpHelper;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
    public static ArrayList<String> datas;
    private LiveHttpHelper liveHttpHelper;
    private UploadManager uploadManager;
    private EditText etPhone;
    private TextView tvInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveAuthBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_auth);
        mGridView = (GridView) findViewById(R.id.gridView_live_auth);
        etPhone = (EditText) findViewById(R.id.et_phone_live_auth);
        tvInterest = (TextView) findViewById(R.id.tv_interest_live_auth);
        dataBindingLiveAuth = new DataBindingLiveAuth();
        binding.setLive(dataBindingLiveAuth);

        preData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!datas.contains("add")) {
            if (datas.size() != 3)
                datas.add("add");
        }
        if (mVisitImgAdapter != null) {
            mVisitImgAdapter.notifyDataSetChanged();
        }

    }

    public void onClick(View v) {
        Intent intent;
        int id = v.getId();
        switch (id) {
            case R.id.layout_city_live_auth:
                intent = new Intent(mContext, CityListActivity.class);
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
                submitAuth();
                break;
            case R.id.layout_interest_live_auth:
                //兴趣
                intent = new Intent(mContext, LiveAuthTagActivity.class);
                startActivityForResult(intent, 200);
                break;
        }
    }

    private String phone;

    private void submitAuth() {
        phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(dataBindingLiveAuth.interest.get())) {
            MGToast.showToast("请选择兴趣");
        } else if (TextUtils.isEmpty(cityId)) {
            MGToast.showToast("请选择城市");
        } else if (TextUtils.isEmpty(phone)) {
            MGToast.showToast("请输入手机号");
        } else if (datas.size() < 3) {
            MGToast.showToast("请上传2-3张生活照");
        } else {
            liveHttpHelper.getBussDictionInfo("Client");
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

    String cityId;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 8888) {
            dataBindingLiveAuth.city.set(AppRuntimeWorker.getCity_name());
            cityId = AppRuntimeWorker.getCity_id();
            return;
        }
        if (requestCode == 200 && resultCode == 8888) {
            dataBindingLiveAuth.interest.set(data.getStringExtra("tags"));
            tvInterest.setText(StringTool.getStringFixed(dataBindingLiveAuth.interest.get(), 15, ""));
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == TAKE_PHOTO) {
        } else if (requestCode == SELECT_FILE_CODE) {
            mPhotoUri = data.getData();
            if (Build.VERSION.SDK_INT >= 19) {
                mPhotoPath = UriUtil.getImageAbsolutePath(this, mPhotoUri);
                mPhotoUri = Uri.fromFile(new File(mPhotoPath));
            }
        }
        Bitmap temp = null;
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
    String urlQiNiu;

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
        } else if (LiveConstants.HOST_INFO.equals(method)) {
            MGToast.showToast("申请认证成功");
            finish();
            App.getInstance().getmUserCurrentInfo().getUserInfoNew().setIs_host("2");
        }

    }

    private StringBuffer sbFileKeys;

    private void uploadFile() {
        sbFileKeys = new StringBuffer();
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
                                        sbFileKeys.append(urlQiNiu + fileKey + ",");
                                    }
                                    if (sbFileKeys.toString().indexOf("http:") != -1) {
                                        String[] strs = sbFileKeys.toString().split("http://");
                                        if (datas.contains("add")) {
                                            if ((strs.length - 1) == 2) {
                                                liveHttpHelper.postHostInfo(String.valueOf(dataBindingLiveAuth.mode.get()), phone, sbFileKeys.toString(), cityId, dataBindingLiveAuth.interest.get());
                                            }
                                        } else {
                                            if ((strs.length - 1) == 3) {
                                                liveHttpHelper.postHostInfo(String.valueOf(dataBindingLiveAuth.mode.get()), phone, sbFileKeys.toString(), cityId, dataBindingLiveAuth.interest.get());
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                }
                            } else {
                            }
                        }
                    }, null);
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

}