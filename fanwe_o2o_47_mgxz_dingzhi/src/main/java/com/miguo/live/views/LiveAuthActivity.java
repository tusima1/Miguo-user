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
import android.widget.GridView;

import com.fanwe.base.CallbackView;
import com.fanwe.customview.BottomDialog;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveAuthBinding;
import com.fanwe.utils.Bimp;
import com.fanwe.utils.UriUtil;
import com.miguo.live.adapters.VisitImgAdapter;
import com.miguo.live.model.DataBindingLiveAuth;
import com.miguo.live.presenters.LiveHttpHelper;

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
    private ArrayList<String> datas;
    LiveHttpHelper liveHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveAuthBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_auth);
        mGridView = (GridView) findViewById(R.id.gridView_live_auth);
        dataBindingLiveAuth = new DataBindingLiveAuth();
        dataBindingLiveAuth.interest.set("模特");
        dataBindingLiveAuth.city.set("杭州");
        binding.setLive(dataBindingLiveAuth);

        preData();
        setListener();
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
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
                dataBindingLiveAuth.interest.set("电影");
                dataBindingLiveAuth.city.set("嵊州");
//                liveHttpHelper.getBussDictionInfo("Client");
                break;
        }
    }

    private void preData() {
        liveHttpHelper = new LiveHttpHelper(mContext, this);
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

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {

    }
}
