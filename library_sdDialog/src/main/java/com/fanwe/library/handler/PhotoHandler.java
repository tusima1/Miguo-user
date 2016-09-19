
package com.fanwe.library.handler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.utils.SDFileUtil;
import com.fanwe.library.utils.SDImageUtil;
import com.fanwe.library.utils.SDIntentUtil;

import java.io.File;

public class PhotoHandler extends OnActivityResultHandler {
    public static final String TAKE_PHOTO_FILE_DIR_NAME = "take_photo";
    public static final int REQUEST_CODE_GET_PHOTO_FROM_CAMERA = 16542;
    public static final int REQUEST_CODE_GET_PHOTO_FROM_ALBUM = REQUEST_CODE_GET_PHOTO_FROM_CAMERA + 1;

    private PhotoHandlerListener mListener;
    private File mTakePhotoFile;
    private File mTakePhotoDir;

    public void setmListener(PhotoHandlerListener mListener) {
        this.mListener = mListener;
    }

    public PhotoHandler(Fragment mFragment) {
        super(mFragment);
        init();
    }

    public PhotoHandler(FragmentActivity mActivity) {
        super(mActivity);
        init();
    }

    private void init() {
        File saveFileDir = SDLibrary.getInstance().getApplication().getExternalCacheDir();
        if (saveFileDir != null) {
            mTakePhotoDir = new File(saveFileDir, TAKE_PHOTO_FILE_DIR_NAME);
            mTakePhotoDir.mkdirs();
        }
    }

    public void getPhotoFromAlbum() {
        Intent intent = SDIntentUtil.getIntentSelectLocalImage();
        startActivityForResult(intent, REQUEST_CODE_GET_PHOTO_FROM_ALBUM);
    }

    private File createTakePhotoFile() {
        long current = System.currentTimeMillis();
        File takePhotoFile = new File(mTakePhotoDir, current + ".jpg");
        try {
            while (takePhotoFile.exists()) {
                current++;
                takePhotoFile = new File(mTakePhotoDir, current + ".jpg");
            }
        } catch (Exception e) {
            if (mListener != null) {
                mListener.onFailure("创建照片文件失败:" + e.toString());
            }
        }
        return takePhotoFile;
    }

    public void getPhotoFromCamera() {
        String name = System.currentTimeMillis() + ".jpg";
        String mPhotoPath = getPath(name);
        File file = new File(mPhotoPath);
        if (file.exists()) {
            file.delete();
        }
        Uri mPhotoUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(intent, REQUEST_CODE_GET_PHOTO_FROM_CAMERA);
        mTakePhotoFile = file;
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

    public void deleteTakePhotoFiles() {
        if (mTakePhotoDir != null) {
            SDFileUtil.deleteFile(mTakePhotoDir);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GET_PHOTO_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (mListener != null) {
                        mListener.onResultFromCamera(mTakePhotoFile);
                    }
                }
                break;
            case REQUEST_CODE_GET_PHOTO_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    String path = SDImageUtil.getImageFilePathFromIntent(data, mActivity);
                    if (mListener != null) {
                        if (TextUtils.isEmpty(path)) {
                            mListener.onFailure("从相册获取图片失败");
                        } else {
                            mListener.onResultFromAlbum(new File(path));
                        }
                    }
                }
                break;

            default:
                break;
        }
    }

    public interface PhotoHandlerListener {
        public void onResultFromAlbum(File file);

        public void onResultFromCamera(File file);

        public void onFailure(String msg);
    }

}
