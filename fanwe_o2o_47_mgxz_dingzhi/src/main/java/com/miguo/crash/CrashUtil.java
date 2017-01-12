package com.miguo.crash;

import java.io.File;

/**
 * Created by qiang.chen on 2016/12/30.
 */

public class CrashUtil {
    /**
     * 是否存在日志文件
     *
     * @return
     */
    public static boolean isCrashLogExist() {
        File dir = new File(CrashConstant.CRASH_DIR);
        if (!dir.exists()) {
            return false;
        }
        File[] childFiles = dir.listFiles();
        if (childFiles == null || childFiles.length == 0) {
            return false;
        }
        return true;
    }
}
