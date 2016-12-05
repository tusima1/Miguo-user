package com.fanwe.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fanwe.seller.model.getCityList.ModelCityList;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/12/2.
 */

public class DBHelper extends SQLiteOpenHelper {
    //数据库的版本
    private final static int DB_VERSION = 1;
    //数据库名
    private final static String DB_NAME = "miguo.db";

    private Context mContext;

    //我们直接用super调用父类的构造方法，这样我们在实例化DBHelper的时候只需要传入一个上下文参数就可以了
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    //数据库不存在的时候，调用这个方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db, 0, 0);
    }


    //版本号发生变化的时候，调用这个方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //1.删除原来的表
        //2.调用onCreate重新创建数据库
    }

    /**
     * 建表语句，只需要一行就能建一个表
     */
    private void createTables(SQLiteDatabase db, int oldVersion, int newVersion) {
        //createTable(Movie.class)返回的是sql建表语句
        //db.execSQL(sql) 执行这条建表语句
        db.execSQL(createTable(ModelCityList.class));
    }

    /**
     * 如果没传表明的话，默认使用类名作为表明
     *
     * @param clazz 实体类
     * @return
     */
    private <T> String createTable(Class<T> clazz) {
        return createTable(clazz, clazz.getSimpleName());
    }

    /**
     * 真正的建表方法
     *
     * @param clazz     实体类
     * @param tableName 表明
     * @return sql建表语句
     */
    private <T> String createTable(Class<T> clazz, String tableName) {
        //实例化一个容器，用来拼接sql语句
        StringBuffer sBuffer = new StringBuffer();
        //sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
        sBuffer.append("create table if not exists " + tableName + " " +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
        //得到实体类中所有的公有属性
        Field[] fields = clazz.getFields();
        //遍历所有的公有属性
        for (Field field : fields) {
            //如果属性不为_id的话，说明是新的字段
            if (!field.getName().equals("_id")) {
                //得到属性的基本数据类型
                String type = field.getType().getSimpleName();
                //如果是String类型的属性，就把字段类型设置为TEXT
                if (type.equals("String")) {
                    sBuffer.append(field.getName() + " TEXT,");
                    //如果是int类型的属性，就把字段类型设置为INTEGER
                } else if (type.equals("int")) {
                    sBuffer.append(field.getName() + " INTEGER,");
                }
            }
        }
        //将最后的逗号删除
        sBuffer.deleteCharAt(sBuffer.length() - 1);
        //替换成); 表明sql语句结束
        sBuffer.append(");");
        //返回这条sql语句
        return sBuffer.toString();
    }

}