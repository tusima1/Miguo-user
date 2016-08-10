package com.miguo.utils.test;

/**
 * Created by didik on 2016/8/9.
 */
public class MGTimer {

    private static long methodA;
    public static long getCurrent(){
        return System.currentTimeMillis();
    }

    /**
     * 展示当前时间
     */
    public static void showTime(){
        System.out.println("showTime :"+getCurrent());
    }

    //-------------------------------------------
    public static void  methodStart(){
        methodA=System.currentTimeMillis();
    }

    public static void methodEndShow(){
        long methodTime = System.currentTimeMillis() - methodA;
        System.out.println("MethodTime :"+methodTime);
    }
    //---------------------------------------------
}
