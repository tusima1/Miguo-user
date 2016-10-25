package com.miguo.utils;

import android.support.annotation.NonNull;

/**
 * Created by didik on 2016/10/20.
 */

public class UIChars {

    public static final char RMB=165;//RMB
    public static final char JIN=35;//#
    public static final char DOLLAR=36;//$
    public static final char AND=38;//&
    public static final char AT=64;//@


    @NonNull
    public static String getUIChar(char ch){
        return String.valueOf(ch);
    }
}
