package com.miguo.utils;

import java.io.File;

/**
 * Created by qiang.chen on 2016/12/30.
 */

public class FileUtil {
    /**
     * 删除文件、文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
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
                deleteFile(childFiles[i]);
            }
            file.delete();
        }
    }
}
