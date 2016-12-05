package com.tencent.qcloud.suixinbo.utils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 把Hshmap转换成json，拼装后以html格式输出
 *
 */
public class HashmapToJson {
    /**把数据源HashMap转换成json
     * @param map
     */
    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Entry e = (Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            Object  value = e.getValue();
            if(value==null){
                value = "";
            }
            string += "\"" + value + "\",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    /**
     * 拼装json，输出
     * @param list
     */
    public static String toJson(List list) {
        HashmapToJson toJson = new HashmapToJson();
        String jsonString = "[";
        for (int i = 0; i < list.size(); i++) {
            if (i != 0)
                jsonString += ",";
            jsonString += hashMapToJson((HashMap) list.get(i));
        }
        jsonString += "]";
       return jsonString;
    }
}