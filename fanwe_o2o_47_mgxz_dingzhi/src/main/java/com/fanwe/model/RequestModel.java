package com.fanwe.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.app.AppConfig;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.work.AppRuntimeWorker;

public class RequestModel {
    public static final class RequestDataType {
        public static final int BASE64 = 0;
        public static final int AES = 4;
    }

    public static final class ResponseDataType {
        public static final int BASE64 = 0;
        public static final int JSON = 1;
        public static final int AES = 4;
    }

    // -----------------------------------------
    private int mRequestDataType = RequestDataType.BASE64;
    private int mResponseDataType = ResponseDataType.JSON;

    private Map<String, Object> mData = new HashMap<String, Object>();
    private Map<String, File> mDataFile = new HashMap<String, File>();

    private List<MultiFile> mMultiFile = new ArrayList<MultiFile>();

    private boolean mIsNeedCache = true;
    private boolean mIsNeedShowErrorTip = true;
    private boolean mIsNeedCheckLoginState = false;

    public List<MultiFile> getmMultiFile() {
        return mMultiFile;
    }

    // 构造方法start
    public RequestModel(Map<String, Object> mData) {
        super();
        this.mData = mData;
        init();
    }

    public RequestModel() {
        super();
        init();
    }

    // 构造方法end

    private void init() {
        putUser();
        putSessionId();
        putAct("index");
        putCityId();
        putLocation();
        putRefId();
        put("from", "android");
        put("version", SDPackageUtil.getVersionName());
    }

    private void putRefId() {
        String refId = AppConfig.getRefId();
        if (!TextUtils.isEmpty(refId)) {
            put("ref_uid", refId);
        }
    }

    public boolean ismIsNeedCache() {
        return mIsNeedCache;
    }

    public void setmIsNeedCache(boolean mIsNeedCache) {
        this.mIsNeedCache = mIsNeedCache;
    }

    public boolean ismIsNeedShowErrorTip() {
        return mIsNeedShowErrorTip;
    }

    public void setmIsNeedShowErrorTip(boolean mIsNeedShowErrorTip) {
        this.mIsNeedShowErrorTip = mIsNeedShowErrorTip;
    }

    public boolean ismIsNeedCheckLoginState() {
        return mIsNeedCheckLoginState;
    }

    public void setmIsNeedCheckLoginState(boolean mIsNeedCheckLoginState) {
        this.mIsNeedCheckLoginState = mIsNeedCheckLoginState;
    }

    public Map<String, File> getmDataFile() {
        return mDataFile;
    }

    public void setmDataFile(Map<String, File> mDataFile) {
        this.mDataFile = mDataFile;
    }

    public Map<String, Object> getmData() {
        return mData;
    }

    public void setmData(Map<String, Object> mData) {
        this.mData = mData;
    }

    public int getmRequestDataType() {
        return mRequestDataType;
    }

    public void setmRequestDataType(int mRequestDataType) {
        this.mRequestDataType = mRequestDataType;
    }

    public int getmResponseDataType() {
        return mResponseDataType;
    }

    public void setmResponseDataType(int mResponseDataType) {
        this.mResponseDataType = mResponseDataType;
    }

    public void put(String key, Object value) {
        mData.put(key, value);
    }

    public Object get(String key) {
        return mData.get(key);
    }

    public void putFile(String key, File file) {
        mDataFile.put(key, file);
    }

    public void putMultiFile(String key, File file) {
        mMultiFile.add(new MultiFile(key, file));
    }

    public void putSessionId() {
        String sessionId = AppConfig.getSessionId();
        if (!TextUtils.isEmpty(sessionId)) {
            put("sess_id", sessionId);
        }
    }

    public void putUser() {
        LocalUserModel user = App.getApplication().getmLocalUser();
        if (user != null) {
            put("email", user.getUser_name());
            put("pwd", user.getUser_pwd());
        }
    }

    public void putCityId() {
        put("city_id", AppRuntimeWorker.getCity_id());
    }

    public void putLocation() {
        if (BaiduMapManager.getInstance().hasLocationSuccess()) {
            put("m_latitude", BaiduMapManager.getInstance().getLatitude());
            put("m_longitude", BaiduMapManager.getInstance().getLongitude());
        }
    }

    public void putLocationScreen() {
        if (BaiduMapManager.getInstance().hasLocationSuccess()) {
            double m_latitude = BaiduMapManager.getInstance().getLatitude();
            double m_longitude = BaiduMapManager.getInstance().getLongitude();

            put("latitude_top", m_latitude + 0.2);
            put("latitude_bottom", m_latitude - 0.2);
            put("longitude_left", m_longitude - 0.2);
            put("longitude_right", m_longitude + 0.2);
        }
    }

    public void putPage(int page) {
        put("page", page);
    }

    public void putPage(PageModel page) {
        putPage(page.getPage());
    }

    public void putCtl(String ctl) {
        put("ctl", ctl);
    }

    public void putAct(String act) {
        put("act", act);
    }

    public class MultiFile {
        public final String key;
        public final File file;

        public MultiFile(String key, File file) {
            this.key = key;
            this.file = file;
        }

        public String getKey() {
            return this.key;
        }

        public File getFile() {
            return this.file;
        }
    }

}
