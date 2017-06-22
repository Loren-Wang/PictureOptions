package com.libpictureoptions.android.pictureSelect.database;

import android.content.Context;
import android.database.Cursor;

import com.libpictureoptions.android.common.utils.DatabaseCommonUtils;


/**
 * Created by wangliang on 0013/2017/3/13.
 * 创建时间： 0013/2017/3/13 16:28
 * 创建人：王亮（Loren wang）
 * 功能作用：数据库操作功能的基类
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class DbBase {
    protected static Context context;
    protected DatabaseCommonUtils databaseUtils;
    protected final String _id = "_id";

    protected String TAG = getClass().getName();
    //创建表语句,其中的表名使用“%s”代替
    private String createTableSql;

    /**
     * 初始化基础数据
     * @param context
     * @param createTableSql
     */
    protected void init(Context context, String createTableSql) {
        if(context != null) {
            databaseUtils = DatabaseCommonUtils.getInstance(context);
        }
        if(createTableSql != null && !"".equals(createTableSql)) {
            this.createTableSql = createTableSql;
        }
        getInfo();
    }

    /**
     * 检查传入参数是否为空
     * @param objects
     * @return
     */
    protected boolean check(Object... objects){
        if(databaseUtils == null){
            return false;
        }
        for(int i = 0 ; i < objects.length ; i++){
            if(objects[i] == null){
                return false;
            }
        }

        return true;
    }

    /**
     * 获取用户信息（不使用抽象函数，因为其子中有不需要该方法的）
     */
    protected void getInfo(){

    }

    /**
     * 获取通用数据库表
     * @param tableName
     * @return
     */
    protected String getCommonTableName(String tableName){
        if(check(tableName)) {
            return judgeOrCreateTable(tableName);
        }
        return "";
    }

    /**
     * 判断或者创建表，没有表则创建表
     * @param tableName
     * @return 返回表名
     */
    private String judgeOrCreateTable(String tableName){
        if(check(tableName,createTableSql)) {
            try {
                Cursor cursor = databaseUtils.select2(tableName, null, null, null, null, null, null);//查找表，如果抛出异常则代表不存在这个表，否则的话就是存在表不做处理
                cursor.close();
                return tableName;
            }catch (Exception e){
                if(e != null && e.getMessage() != null && e.getMessage().contains("no such table")) {//抛出异常则代表不存在表则重新创建
                    databaseUtils.execSQL(createTableSql.replace("%s",tableName));
                    return tableName;
                }
            }
        }
        return "";
    }

}
