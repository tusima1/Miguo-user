package com.miguo.crash;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/12/30.
 */

public class CrashConstant {
    //crash日志存储路径
    public static final String CRASH_DIR = Environment.getExternalStorageDirectory().toString() + File.separator + "mgxzCrash";

}
