package com.miguo.crash;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.fanwe.InitAdvsMultiActivity;
import com.fanwe.app.ActivityLifeManager;
import com.fanwe.app.App;
import com.miguo.utils.SharedPreferencesUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 异常捕获
 * Created by qiang.chen on 2016/12/30.
 */

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private Context mContext;
    private static CrashHandler mInstance = new CrashHandler();
    private CrashHandler() {
    }

    /**
     * 单例模式，保证只有一个CrashHandler实例存在
     *
     * @return
     */
    public static CrashHandler getInstance() {
        return mInstance;
    }

    /**
     * 异常发生时，系统回调的函数，我们在这里处理一些操作
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        showCrashToast();
        saveCrashReport2SD(mContext, ex);
        restartApp();
    }

    /**
     * 为我们的应用程序设置自定义Crash处理
     */
    public void initCrashHandler(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void restartApp() {
        Intent intent = new Intent(App.getInstance().getApplicationContext(),
                InitAdvsMultiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(App.getInstance()
                .getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //重启应用
        AlarmManager mgr = (AlarmManager) App.getInstance().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent);

        //清空Activity栈,防止系统自动重启至崩溃页面,导致崩溃再次出现.
        ActivityLifeManager.getInstance().finishAllActivity();
        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        System.gc();
    }

    private void showCrashToast() {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(App.getInstance().getApplicationContext(), "很抱歉,程序出现异常,即将重启",
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(1000);//Toast展示的时间
        } catch (InterruptedException e) {
        }
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在LinkedHashMap中
     *
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        map.put("APP", "米果小站");
        map.put("用户名", SharedPreferencesUtils.getInstance().getUserName());
        map.put("品牌", "" + Build.BRAND);
        map.put("型号", "" + Build.MODEL);
        map.put("SDK版本", "" + Build.VERSION.SDK_INT);
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("crash时间", parserTime(System.currentTimeMillis()));

        return map;
    }


    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        Log.e(TAG, mStringWriter.toString());
        return mStringWriter.toString();
    }

    /**
     * 保存获取的 软件信息，设备信息和出错信息保存在SDcard中
     *
     * @param context
     * @param ex
     * @return
     */
    private void saveCrashReport2SD(Context context, Throwable ex) {
        File bugFile ;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : obtainSimpleInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        sb.append(obtainExceptionInfo(ex));
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(CrashConstant.CRASH_DIR);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
                if (!mkdirs){
                    Log.e("test","创建文件夹和文件名失败!");
                    return ;
                }
            }
            try {
                bugFile = new File(dir,parserTime(System.currentTimeMillis()) +".txt");
                FileOutputStream fos = new FileOutputStream(bugFile);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式，并在后缀加入随机数
     *
     * @param milliseconds
     * @return
     */
    private String parserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(new Date(milliseconds));
    }
}