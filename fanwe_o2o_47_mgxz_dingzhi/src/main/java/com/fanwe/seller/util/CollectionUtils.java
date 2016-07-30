package com.fanwe.seller.util;

import java.util.Collection;

/**
 * Created by Administrator on 2016/7/29.
 */
public class CollectionUtils {
    public static <T> boolean isValid(Collection<T> c) {
        return !(c == null || c.size() == 0);
    }
}
