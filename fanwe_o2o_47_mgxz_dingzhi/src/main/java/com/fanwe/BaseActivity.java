package com.fanwe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fanwe.app.ActivityLifeManager;
import com.fanwe.app.App;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.title.SDTitleSimple.SDTitleSimpleListener;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.ScanResultHandler;
import com.fanwe.work.SystemBarTintManager;
import com.lidroid.xutils.ViewUtils;
import com.miguo.app.HiHomeActivity;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.sunday.eventbus.SDBaseEvent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

public class BaseActivity extends SDBaseActivity implements SDTitleSimpleListener {

    /**
     * 是否是当作广告页面启动 (boolean)
     */
    public static final String EXTRA_IS_ADVS = "extra_is_advs";
    private boolean mIsStartByAdvs = false;
    private TitleType mTitleType = TitleType.TITLE_NONE;
    protected SDTitleSimple mTitle;
    private ScanResultHandler mScanResultHandler;
    //umeng页面统计标签
    protected String umengTag = "";

    public TitleType getmTitleType() {
        return mTitleType;
    }

    public void setmTitleType(TitleType mTitleType) {
        this.mTitleType = mTitleType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseInit() {
        mScanResultHandler = new ScanResultHandler(this);
        initStatusBar();
    }

    private void startPushIntent() {
        SDActivityUtil.startActivity(this, App.getApplication().mPushIntent);
        App.getApplication().mPushIntent = null;
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main_color);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.main_color));
        }
    }

    private void setTranslucentStatus(boolean b) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (b) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void baseGetIntentData() {
        mIsStartByAdvs = getIntent().getBooleanExtra(EXTRA_IS_ADVS, false);
    }

    @Override
    protected View onCreateTitleView() {
        View viewTitle = null;
        switch (getmTitleType()) {
            case TITLE:
                viewTitle = LayoutInflater.from(this).inflate(R.layout.title_simple_sd, null);
                mTitle = (SDTitleSimple) viewTitle.findViewById(R.id.title);
                if (mTitle != null) {
                    mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
                    mTitle.setmListener(this);
                }
                break;
            default:
                break;
        }
        return viewTitle;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ViewUtils.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!TextUtils.isEmpty(umengTag)) {
            MobclickAgent.onPageEnd(umengTag);
        }
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(umengTag)) {
            MobclickAgent.onPageStart(umengTag);
        }
        MobclickAgent.onResume(this);
    }

    @Override
    public void finish() {
        if (mIsStartByAdvs) {
            mIsStartByAdvs = false;
            startActivity(new Intent(this, HiHomeActivity.class));
        }
        super.finish();
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case EXIT_APP:
                SharedPreferences sp = getSharedPreferences("date", Context.MODE_PRIVATE);
                if (sp != null) {
                    sp.edit().remove("dateIn");

                    sp.edit().remove("dateOut");

                    sp.edit().commit();
                }
                finish();
                break;
            case LOGOUT:
                if (!App.getApplication().mListClassNotFinishWhenLoginState0.contains(this.getClass())) {
                    finish();
                }
                break;
            case START_SCAN_QRCODE:
                if (this.getClass() == event.getData()) {
                    Intent intent = new Intent(getApplicationContext(), MyCaptureActivity.class);
                    mScanResultHandler.startScan(intent);
                }
                break;
            case TOKEN_FAILUE:
//                if(ActivityLifeManager.getInstance().getLastActivity().equals(ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY))||ActivityLifeManager.getInstance().getLastActivity().equals(RegisterActivity.class)){
//                    return;
//                }else {
//                    Intent intent = new Intent(this, ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
//                    startActivity(intent);
//                }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        mScanResultHandler.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        finish();
    }

    @Override
    public void onCLickMiddle_SDTitleSimple(SDTitleItem v) {

    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {

    }

    /**
     * 需要的时候调用,触摸空白地方隐藏软键盘
     **/
    public void hideKeyboradOnTouchOutside(Activity activity, View rootView) {
        initUI(rootView, activity);
    }

    private void initUI(View view, final Activity activity) {

        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);

                initUI(innerView, activity);
            }
        }
    }

    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

}
