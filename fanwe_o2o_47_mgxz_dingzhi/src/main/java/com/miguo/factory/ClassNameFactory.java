package com.miguo.factory;

import com.miguo.app.HiHomeActivity;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/26.
 * 通过package.classname反射获取类名
 * 解决问题：
 * 当app中有几十个上百个界面跳转到同一个界面时（如商品详情页）
 * 涉及到这个页面变动，或者二次升级改版而需要保留两份界面文件，这个时候对于整个应用的改动量就非常大了
 * 如果用反射方式获取类名，用常量宏定义类路径，那需要修改的时候只需要修改路径即可
 * 注：类名请统一放到 {@link com.miguo.definition.ClassPath}
 */
public class ClassNameFactory {

    public static Class getClass(String clz){
        try{
            return Class.forName(clz);
        }catch (ClassNotFoundException e){
            return HiHomeActivity.class;
        }
    }

}
