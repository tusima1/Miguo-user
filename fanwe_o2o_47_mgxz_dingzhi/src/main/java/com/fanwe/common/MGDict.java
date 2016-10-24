package com.fanwe.common;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.common.model.getMGDict.DictArray;
import com.fanwe.common.model.getMGDict.DictModel;
import com.google.gson.Gson;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by didik on 2016/9/27.
 * 返回客户端字典集合
 */

public class MGDict {

    public static final String MG_DICT="mg_dict";

    public static void save2File(String jsonStr){
        if (TextUtils.isEmpty(jsonStr)){return;}
        //居然没有版本号
//        if (isExist()){
//            return;
//        }
        jsonStr="{\"dict\":"+jsonStr+"}";
        try{

            FileOutputStream fileOutputStream =App.getInstance().getApplicationContext().openFileOutput(MG_DICT, Context.MODE_PRIVATE);

            byte [] bytes = jsonStr.getBytes();

            fileOutputStream.write(bytes);

            fileOutputStream.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static List<DictModel> getDict(){
        String res="";
        try{
            FileInputStream fin = App.getInstance().getApplicationContext().openFileInput(MG_DICT);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        DictArray dictArray=null;
        try {
            Gson gson=new Gson();
            dictArray = gson.fromJson(res, DictArray.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (dictArray==null){
            return null;
        }
        List<DictModel> dictList = dictArray.getDict();
        return dictList;
    }

    private static boolean isExist(){
        File mg_dict=new File(App.getInstance().getFilesDir(),MG_DICT);
        return mg_dict.exists();
    }
}
