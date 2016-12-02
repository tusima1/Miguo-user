package com.fanwe.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.fanwe.seller.model.getCityList.ModelCityList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作工具类
 *
 * @Title:
 * @Description:
 * @Author:08861
 * @Since:2015年1月26日
 * @Version:1.0.0
 */
public class DatabaseManager {

    protected DBHelper dbHelper;
    public static DatabaseManager instance = null;
    protected SQLiteDatabase sqliteDatabase;

    /**
     * @param context
     */
    public DatabaseManager(Context context) {
        dbHelper = new DBHelper(context);
        sqliteDatabase = dbHelper.getWritableDatabase();
    }

    /***
     * 获取本类对象实例
     *
     * @param context 上下文对象
     * @return
     */
    public static DatabaseManager getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseManager(context);
        return instance;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (sqliteDatabase.isOpen())
            sqliteDatabase.close();
        if (dbHelper != null)
            dbHelper.close();
        if (instance != null)
            instance = null;
    }

    /**
     * 查询数据(适用于单条记录)，返回实体类
     *
     * @param tableName  表名
     * @param className  类名
     * @param tableField 表列名
     * @param selection  查询条件值
     * @return
     * @throws Exception
     * @describe
     * @author ling xiangfeng
     * @time 2015-4-22 下午2:12:35
     * @TODO
     */
    public <T> Object queryData2Object(String tableName, Class<T> className,
                                       String tableField, String[] selection) throws Exception {
        T object = null;
        if (sqliteDatabase.isOpen()) {
            // 查询指定表中的所有数据
            Cursor cursor = null;
            if (null == tableField) {
                cursor = sqliteDatabase.rawQuery("select * from " + tableName,
                        new String[0]);
            } else {
                cursor = sqliteDatabase.rawQuery("select * from " + tableName
                        + " where " + tableField + " = ?", selection);
            }
            Field[] f;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    object = className.newInstance();
                    f = object.getClass().getDeclaredFields();
                    for (int i = 0; i < f.length; i++) {
                        // 为JavaBean 设值
                        invokeSet(object, f[i].getName(),
                                cursor.getString(cursor.getColumnIndex(f[i]
                                        .getName())));
                    }
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return object;
    }

    /**
     * 多个条件查询数据(适用于单条记录)，返回实体类
     *
     * @param tableName   表名
     * @param className   类名
     * @param tableFields 表列名
     * @param selection   查询条件值
     * @return
     * @throws Exception
     * @describe
     * @author ling xiangfeng
     * @time 2015-4-22 上午9:58:54
     * @TODO
     */
    public <T> Object queryData2ObjectWithFields(String tableName,
                                                 Class<T> className, String[] tableFields, String[] selection)
            throws Exception {
        T object = null;
        if (sqliteDatabase.isOpen()) {
            // 查询指定表中的所有数据
            Cursor cursor = null;
            // 生成查询语句
            StringBuffer sb = new StringBuffer();
            sb.append("select * from " + tableName + " where ");
            for (int i = 0; i < tableFields.length; i++) {
                if (i != tableFields.length - 1) {
                    sb.append(tableFields[i] + " = ? and ");
                } else {
                    sb.append(tableFields[i] + " = ? ");
                }
            }
            cursor = sqliteDatabase.rawQuery(sb.toString(), selection);

            Field[] f;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    object = className.newInstance();
                    f = object.getClass().getDeclaredFields();
                    for (int i = 0; i < f.length; i++) {
                        // 为JavaBean 设值
                        invokeSet(object, f[i].getName(),
                                cursor.getString(cursor.getColumnIndex(f[i]
                                        .getName())));
                    }
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return object;
    }

    /**
     * 查询表中一列数据(适用于多条记录)
     * <p>
     * 只需指定表明、类名
     *
     * @param tableName   表名
     * @param conlumName  要查询表列名
     * @param tableFields 表列名
     * @param selection   查询条件值
     * @return List<T> 返回查询结果
     * @throws Exception
     * @Description:
     * @Author:08861
     * @Since:2015年1月26日
     */
    public List<String> querySingleData2List(String tableName,
                                             String conlumName, String tableField, String[] selection)
            throws Exception {
        List<String> mList = new ArrayList<String>();
        if (sqliteDatabase.isOpen()) {
            // 查询指定表中的所有数据
            Cursor cursor = null;
            if (null == tableField) {
                cursor = sqliteDatabase.rawQuery("select distinct "
                        + conlumName + " from " + tableName + " order by "
                        + conlumName, new String[0]);
            } else {
                cursor = sqliteDatabase.rawQuery("select distinct "
                                + conlumName + " from " + tableName + " where "
                                + tableField + " = ?" + " order by " + conlumName,
                        selection);
            }
            Field[] f;
            int index = cursor.getColumnIndex(conlumName);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String value = cursor.getString(index);
                    mList.add(value);
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return mList;
    }

    /**
     * 查询数据(适用于多条记录)，返回实体类list
     * <p>
     * 只需指定表明、类名
     *
     * @param tableName   表名
     * @param className   类名
     * @param tableFields 表列名
     * @param selection   查询条件值
     * @return List<T> 返回查询结果
     * @throws Exception
     * @Description:
     * @Author:08861
     * @Since:2015年1月26日
     */
    public <T> List<T> queryData2List(String tableName, Class<T> className,
                                      String tableField, String[] selection) throws Exception {
        List<T> mList = new ArrayList<T>();
        T object;
        if (sqliteDatabase.isOpen()) {
            // 查询指定表中的所有数据
            Cursor cursor = null;
            if (null == tableField) {
                cursor = sqliteDatabase.rawQuery("select * from " + tableName,
                        new String[0]);
            } else {
                cursor = sqliteDatabase.rawQuery("select * from " + tableName
                        + " where " + tableField + " = ?", selection);
            }
            cursor.getColumnNames();
            Field[] f;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    object = className.newInstance();
                    f = object.getClass().getDeclaredFields();
                    for (int i = 0; i < f.length; i++) {
                        // 为JavaBean 设值
                        String filed = f[i].getName();
                        int index = cursor.getColumnIndex(f[i].getName());
                        invokeSetWithType(f[i], object, filed, index, cursor);
                    }
                    mList.add(object);
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return mList;
    }

    /**
     * 按数据类型调用set方法
     *
     * @describe
     * @author ling xiangfeng
     * @time 2015-4-22 下午1:50:55
     * @TODO
     */
    private void invokeSetWithType(Field f, Object object, String filed,
                                   int index, Cursor cursor) {
        String type = f.getGenericType().toString();
        if ("class java.lang.String".equals(type)) {
            invokeSet(object, filed, cursor.getString(index));
        } else if ("class java.lang.Long".equals(type)) {
            invokeSet(object, filed, cursor.getLong(index));
        } else if ("class java.lang.Integer".equals(type)) {
            invokeSet(object, filed, cursor.getInt(index));
        } else if ("class java.lang.Float".equals(type)) {
            invokeSet(object, filed, cursor.getFloat(index));
        }
    }

    /**
     * 多个条件查询数据(适用于多条记录)，返回实体类list
     * <p>
     * 只需指定表明、类名
     *
     * @param tableName   表名
     * @param className   类名
     * @param tableFields 表列名
     * @param selection   查询条件值
     * @return List<T> 返回查询结果
     * @throws Exception
     * @Description:
     * @Author:08861
     * @Since:2015年1月26日
     */
    public <T> List<T> queryData2ListWithFields(String tableName,
                                                Class<T> className, String[] tableFields, String[] selection)
            throws Exception {
        List<T> mList = new ArrayList<T>();
        T object;
        if (sqliteDatabase.isOpen()) {
            // 查询指定表中的所有数据
            Cursor cursor = null;
            // 生成查询语句
            StringBuffer sb = new StringBuffer();
            sb.append("select * from " + tableName + " where ");
            for (int i = 0; i < tableFields.length; i++) {
                if (i != tableFields.length - 1) {
                    sb.append(tableFields[i] + " = ? and ");
                } else {
                    sb.append(tableFields[i] + " = ? ");
                }
            }
            cursor = sqliteDatabase.rawQuery(sb.toString(), selection);

            Field[] f;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    object = className.newInstance();
                    f = object.getClass().getDeclaredFields();
                    for (int i = 0; i < f.length; i++) {
                        // 为JavaBean 设值
                        invokeSet(object, f[i].getName(),
                                cursor.getString(cursor.getColumnIndex(f[i]
                                        .getName())));
                    }
                    mList.add(object);
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return mList;
    }

    /**
     * 模糊查询数据(适用于多条记录)，返回实体类list
     * <p>
     * 只需指定表明、类名
     *
     * @param tableName   表名
     * @param className   类名
     * @param tableFields 表列名
     * @param selection   查询条件值
     * @return List<T> 返回查询结果
     * @throws Exception
     * @Description:
     * @Author:08861
     * @Since:2015年1月26日
     */
    public <T> List<T> queryData2ListFuzzily(String tableName,
                                             String tableField, String selection, Class<T> className)
            throws Exception {
        List<T> mList = new ArrayList<T>();
        T object;
        Cursor cursor = null;
        if (sqliteDatabase.isOpen()) {
            // 查询指定表中的所有数据
            if (!"".equals(selection)) {
                cursor = sqliteDatabase.rawQuery("select  *  from " + tableName
                        + " where " + tableField + " like '%" + selection
                        + "%'", null);
            } else {
                cursor = sqliteDatabase.rawQuery(
                        "select  *  from " + tableName, null);
            }
            Field[] f;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    object = className.newInstance();
                    f = object.getClass().getDeclaredFields();
                    for (int i = 0; i < f.length; i++) {
                        // 为JavaBean 设值
                        invokeSet(object, f[i].getName(),
                                cursor.getString(cursor.getColumnIndex(f[i]
                                        .getName())));
                    }
                    mList.add(object);
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return mList;
    }

    /**
     * 查询数据(适用于单条记录)
     * <p>
     * 需编写sql语句、指定类名
     *
     * @param tableName
     * @param className
     * @return object
     * @throws Exception
     * @Description:
     * @Author:08861
     * @Since:2015年1月26日
     */
    public <T> Object queryData2ObjectBySQL(String sql, String[] selectionArgs,
                                            Class<T> className) throws Exception {
        T object = null;
        if (sqliteDatabase.isOpen()) {
            // 查询指定表中的所有数据
            Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
            Field[] f;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    object = className.newInstance();
                    f = object.getClass().getDeclaredFields();
                    for (int i = 0; i < f.length; i++) {
                        // 为JavaBean 设值
                        invokeSet(object, f[i].getName(),
                                cursor.getString(cursor.getColumnIndex(f[i]
                                        .getName())));
                    }
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return object;
    }

    /**
     * 查询数据(适用于多条记录)
     * <p>
     * 需编写sql语句、指定类名
     *
     * @param sql           执行查询操作的sql语句
     * @param selectionArgs 查询条件
     * @param object        Object的对象
     * @return List<Object> 返回查询结果
     */
    public <T> List<T> queryData2ListBySQL(String sql, String[] selectionArgs,
                                           Class<T> className) throws Exception {
        List<T> mList = new ArrayList<T>();
        T object;
        if (sqliteDatabase.isOpen()) {
            Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
            Field[] f;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    object = className.newInstance();
                    f = object.getClass().getDeclaredFields();
                    for (int i = 0; i < f.length; i++) {
                        // 为JavaBean 设值
                        invokeSet(object, f[i].getName(),
                                cursor.getString(cursor.getColumnIndex(f[i]
                                        .getName())));
                    }
                    mList.add(object);
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return mList;
    }

    /**
     * 查询数据表中的ids，返回id数组
     *
     * @param tableName  表名
     * @param className  类名
     * @param tableField 表列名
     * @param selection  查询条件值
     * @return
     * @throws Exception
     * @Description:
     * @Author:08861
     * @Since:2015年3月3日
     */
    public <T> List<String> queryTableIds(String tableName, Class<T> className,
                                          String tableField, String[] selection) throws Exception {
        List<String> mList = new ArrayList<String>();
        T object;
        if (sqliteDatabase.isOpen()) {
            // 查询指定表中的所有数据
            Cursor cursor = null;
            if (null == tableField) {
                cursor = sqliteDatabase.rawQuery("select * from " + tableName,
                        new String[0]);
            } else {
                cursor = sqliteDatabase.rawQuery("select * from " + tableName
                        + " where " + tableField + " = ?", selection);
            }
            Field[] f;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    object = className.newInstance();
                    f = object.getClass().getDeclaredFields();
                    for (int i = 0; i < f.length; i++) {
                        // 为JavaBean 设值
                        String filed = f[i].getName();
                        if ("id".equals(filed)) {
                            int index = cursor.getColumnIndex(f[i].getName());
                            mList.add(cursor.getString(index));
                        }
                    }
                }
            }
            cursor.close();
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return mList;
    }

    /**
     * 插入数据
     *
     * @param sql      执行更新操作的sql语句
     * @param bindArgs sql语句中的参数,参数的顺序对应占位符顺序
     * @return result 返回新添记录的行号，与主键id无关
     */
    public Long insertDataBySql(String sql, String[] bindArgs) throws Exception {
        long result = 0;
        if (sqliteDatabase.isOpen()) {
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
            if (bindArgs != null) {
                int size = bindArgs.length;
                for (int i = 0; i < size; i++) {
                    // 将参数和占位符绑定，对应
                    statement.bindString(i + 1, bindArgs[i]);
                }
                result = statement.executeInsert();
                statement.close();
            }
        } else {
            //DebugTool.info("数据库已关闭");
        }
        return result;
    }

    /**
     * 插入数据
     *
     * @param tableName 表名
     * @param values    要插入的数据
     * @return result 返回新添记录的行号，与主键id无关
     */
    public Long insertData(String tableName, ContentValues values) {
        long result = 0;
        if (sqliteDatabase.isOpen()) {
            result = sqliteDatabase.insert(tableName, null, values);
        }
        return result;
    }

    /**
     * 将一个实体类插入到数据
     *
     * @param tableName 表名
     * @param object    要插入的实体类
     * @return result 返回新添记录的行号，与主键id无关
     */
    public Long insertObject2Data(String tableName, Object object) {
        long result = 0;
        Field[] f;
        ContentValues cv = new ContentValues();
        f = object.getClass().getDeclaredFields();
        for (int i = 0; i < f.length; i++) {
            Method m;
            try {
                String filed = f[i].getName();
                m = (Method) object.getClass().getMethod(
                        "get" + getMethodName(filed));
                invokeGetWithType(m, object, cv, filed);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sqliteDatabase.isOpen()) {
            result = sqliteDatabase.insert(tableName, null, cv);
        }
        return result;
    }

    /**
     * 将List插入到数据
     *
     * @param tableName
     * @param datas
     * @return
     */
    public Long insertList2Data(String tableName, List<ModelCityList> datas) {
        long result = 0;
        sqliteDatabase.beginTransaction();
        for (Object object : datas) {
            Field[] f;
            ContentValues cv = new ContentValues();
            f = object.getClass().getDeclaredFields();
            for (int i = 0; i < f.length; i++) {
                Method m;
                try {
                    String filed = f[i].getName();
                    m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(filed));
                    invokeGetWithType(m, object, cv, filed);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sqliteDatabase.isOpen()) {
                result = sqliteDatabase.insert(tableName, null, cv);
            }
        }
        sqliteDatabase.setTransactionSuccessful();
        sqliteDatabase.endTransaction();
        return result;
    }


    /**
     * 根据方法返回值设置插入数据类型
     *
     * @param m
     * @param object
     * @param filed
     * @param cv
     * @describe
     * @author ling xiangfeng
     * @time 2015-4-22 上午11:22:29
     * @TODO
     */
    private void invokeGetWithType(Method m, Object object, ContentValues cv,
                                   String filed) {
        String[] type = m.getGenericReturnType().toString().split(".");
        if (type.length > 0) {
            String str = type[type.length - 1];
            try {
                if ("class java.lang.String".equals(str)) {
                    cv.put(filed, (Long) m.invoke(object));
                } else if ("class java.lang.Integer".equals(str)) {
                    cv.put(filed, (Integer) m.invoke(object));
                } else if ("class java.lang.Float".equals(str)) {
                    cv.put(filed, (Float) m.invoke(object));
                } else if ("class java.lang.Long".equals(str)) {
                    cv.put(filed, (Long) m.invoke(object));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    /**
     * 更新数据
     *
     * @param sql      执行更新操作的sql语句
     * @param bindArgs sql语句中的参数,参数的顺序对应占位符顺序
     */
    public void updateDataBySql(String sql, String[] bindArgs) throws Exception {
        if (sqliteDatabase.isOpen()) {
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
            if (bindArgs != null) {
                int size = bindArgs.length;
                for (int i = 0; i < size; i++) {
                    statement.bindString(i + 1, bindArgs[i]);
                }
                statement.execute();
                statement.close();
            }
        } else {
            //DebugTool.info("数据库已关闭");
        }
    }

    /**
     * 更新数据
     *
     * @param tableName   表名
     * @param values      表示更新的数据
     * @param whereClause 表示SQL语句中条件部分的语句
     * @param selections  表示占位符的值
     * @return
     */
    public int updataData(String tableName, ContentValues values,
                          String whereClause, String[] selections) {
        int result = 0;
        if (sqliteDatabase.isOpen()) {
            result = sqliteDatabase.update(tableName, values, whereClause,
                    selections);
        }
        return result;
    }

    /**
     * 更新实体类
     *
     * @param tableName   表名
     * @param object      要更新的实体类
     * @param whereClause 条件语句
     * @param selections  表示占位符的值
     * @describe
     * @author ling xiangfeng
     * @time 2015-4-22 下午4:11:38
     * @TODO
     */
    public void updateDataWithObject(String tableName, Object object,
                                     String[] tableFields, String[] selections) {
        // 先删除数据
        deleteDataSimply(tableName, tableFields, selections);
        // 再添加
        insertObject2Data(tableName, object);
    }

    /**
     * 删除数据
     *
     * @param sql      执行更新操作的sql语句
     * @param bindArgs sql语句中的参数,参数的顺序对应占位符顺序
     */
    public void deleteDataBySql(String sql, String[] bindArgs) throws Exception {
        if (sqliteDatabase.isOpen()) {
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
            if (bindArgs != null) {
                int size = bindArgs.length;
                for (int i = 0; i < size; i++) {
                    statement.bindString(i + 1, bindArgs[i]);
                }
                Method[] mm = statement.getClass().getDeclaredMethods();
                for (Method method : mm) {
                    /**
                     * 反射查看是否能获取executeUpdateDelete方法 查看源码可知
                     * executeUpdateDelete是public的方法，但是好像被隐藏了所以不能被调用，
                     * 利用反射貌似只能在root以后的机器上才能调用，小米是可以，其他机器却不行，所以还是不能用。
                     */
                }
                statement.execute();
                statement.close();
            }
        } else {
            //DebugTool.info("数据库已关闭");
        }
    }

    /**
     * 删除数据
     *
     * @param tableName   表名
     * @param whereClause 表示SQL语句中条件部分的语句
     * @param whereArgs   表示占位符的值
     * @return
     */
    public int deleteData(String tableName, String whereClause,
                          String[] whereArgs) {
        int result = 0;
        if (sqliteDatabase.isOpen()) {
            result = sqliteDatabase.delete(tableName, whereClause, whereArgs);
        }
        return result;
    }

    /**
     * 删除数据，写法较为简单
     *
     * @param tableName   表名
     * @param tableFields 表列名
     * @param selections  条件数据
     * @return
     * @describe
     * @author ling xiangfeng
     * @time 2015-4-22 下午4:28:27
     * @TODO
     */
    public int deleteDataSimply(String tableName, String[] tableFields,
                                String[] selections) {
        int result = 0;
        // 生成查询语句
        StringBuffer sb = new StringBuffer();
        sb.append(" where ");
        for (int i = 0; i < tableFields.length; i++) {
            if (i != tableFields.length - 1) {
                sb.append(tableFields[i] + " = ? and ");
            } else {
                sb.append(tableFields[i] + " = ? ");
            }
        }
        if (sqliteDatabase.isOpen()) {
            result = sqliteDatabase
                    .delete(tableName, sb.toString(), selections);
        }
        return result;
    }

    /**
     * java反射bean的set方法
     *
     * @param objectClass
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = objectClass
                    .getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行set方法
     *
     * @param object    执行对象
     * @param fieldName 属性
     * @param value     值
     */
    public static void invokeSet(Object object, String fieldName, Object value) {
        Method method = getSetMethod(object.getClass(), fieldName);
        try {
            method.invoke(object, new Object[]{value});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
