package com.miguo.utils;


import android.os.Handler;
import android.text.TextUtils;

import com.fanwe.seller.util.CollectionUtils;
import com.miguo.crash.CrashConstant;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传文件到七牛云
 * Created by qiang.chen on 2016/12/30.
 */

public class UploadUtil {
    private static UploadUtil instance;
    private static UploadManager uploadManager;
    private static String uploadToken;

    private UploadUtil() {
    }

    public static synchronized UploadUtil getInstance() {
        if (instance == null) {
            instance = new UploadUtil();
            uploadManager = new UploadManager();
        }
        return instance;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    private int uploadSize;
    public static int UPLOAD_FAILURE = 0;
    public static int UPLOAD_SUCCESS = 1;

    /**
     * 上传文件
     *
     * @param files 文件路径
     * @return
     */
    public void uploadFile(ArrayList<String> files, final Handler handler) {
        final android.os.Message msg = new android.os.Message();
        if (!CollectionUtils.isValid(files) || TextUtils.isEmpty(uploadToken)) {
            msg.what = UPLOAD_FAILURE;
            handler.sendMessage(msg);
        }
        final int size = files.size();
        uploadSize = 0;
        for (int i = 0; i < size; i++) {
            File uploadFile = new File(files.get(i));
            uploadManager.put(uploadFile, null, uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo respInfo,
                                             JSONObject jsonData) {
                            if (respInfo.isOK()) {
                                uploadSize++;
                                if (uploadSize >= size) {
                                    msg.what = UPLOAD_SUCCESS;
                                    handler.sendMessage(msg);
                                }
                            } else {
                                msg.what = UPLOAD_FAILURE;
                                handler.sendMessage(msg);
                            }
                        }
                    }, null);
        }
    }

    public void uploadCrashLog(String uploadToken) {
        this.uploadToken = uploadToken;
        uploadCrashLog();
    }

    /**
     * 将crash log上传到七牛云
     */
    public void uploadCrashLog() {
        final File dir = new File(CrashConstant.CRASH_DIR);
        if (!dir.exists() || TextUtils.isEmpty(uploadToken)) {
            return;
        }
        File[] files = dir.listFiles();
        final int fileSize = files.length;
        if (fileSize == 0) {
            return;
        }
        for (int i = 0; i < fileSize; i++) {
            File uploadFile = files[i];
            Map<String, String> params = new HashMap<>();
            params.put("x:time", uploadFile.getName());
            UploadOptions uploadOptions = new UploadOptions(params, "txt", false, null, null);
            uploadManager.put(uploadFile, null, uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo respInfo,
                                             JSONObject jsonData) {
                            if (respInfo.isOK()) {
                                uploadSize++;
                                if (uploadSize >= fileSize) {
                                    //log上传完成，清除文件夹
                                    FileUtil.deleteFile(dir);
                                }
                            }
                        }
                    }, uploadOptions);
        }
    }

}
