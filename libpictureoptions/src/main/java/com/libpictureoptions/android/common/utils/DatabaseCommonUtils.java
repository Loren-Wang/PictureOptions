package com.libpictureoptions.android.common.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Iterator;


/**
 * @author 负责数据库的创建以及版本更新的帮助类
 * 
 */
public class DatabaseCommonUtils extends SQLiteOpenHelper {

    private static String TAG;
    /**
	 * 数据库名称
	 */
	private static String DB_NAME = "yudao.db";

	/**
	 * 数据库版本，必须》=1
	 */
	private static int DB_VERSION = 1;

	private static DatabaseCommonUtils dataBaseUtils;
	private SQLiteDatabase sqLiteDatabase;

	/**
	 * 创建数据库
	 *
	 * @param context
	 */
	private DatabaseCommonUtils(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		sqLiteDatabase = getWritableDatabase();//获得SQLiteDatabase
        TAG = getClass().getName();
// TODO Auto-generated constructor stub
	}

	public static DatabaseCommonUtils getInstance(Context context){
		if(dataBaseUtils == null && context != null){
			dataBaseUtils = new DatabaseCommonUtils(context);
		}
		return dataBaseUtils;
	}

	public SQLiteDatabase getSqLiteDatabase() {
		return sqLiteDatabase;
	}

	/**
	 * 创建表以及初始化工作,只执行一次，在数据库第一次创建的时候执行
	 * 
	 * @param db 数据库对象
	 *            ,该对象包含创建，删除以及操作其他 的sql语句的方法
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		sqLiteDatabase = db;
//		String sql = "create table ad(title varchar(100),id varchar(20),image varchar(100),content varchar(100),image_s varchar(100),name varchar(100))";
//		// 执行sq语句，创建表
//		db.execSQL(sql);
	}

	/**
	 * 删除数据库
	 */
	public void deleteDatabase(){
		if(sqLiteDatabase != null) {
			String sql = "drop database " + DB_NAME;
			sqLiteDatabase.execSQL(sql);
			dataBaseUtils = null;
		}
	}

	/**
	 * 删除表
	 * @param tableName
     */
	public void deleteDatabaseTable(String tableName) {
		if(sqLiteDatabase != null) {
			try {
				select2(tableName,null,null,null,null,null,null);//查找表，如果抛出异常则代表不存在这个表，否则的话就是存在表不做处理
				String sql = "drop table " + tableName;
				sqLiteDatabase.execSQL(sql);
			}catch (Exception e) {
			}
		}
	}

	/**
	 * 清空表数据
	 * @param tableName
     */
	public void deleteDatabaseTableData(String tableName) {
		if(sqLiteDatabase != null) {
			try {
				select2(tableName,null,null,null,null,null,null);//查找表，如果抛出异常则代表不存在这个表，否则的话就是存在表不做处理
				String sql = "delete from " + tableName;
				sqLiteDatabase.execSQL(sql);
				String sql1 = "update " + tableName + "set seq = 0 where name = _id" ;//自增长ID为0
				sqLiteDatabase.execSQL(sql1);
			}catch (Exception e) {
				LogUtils.logD(TAG,e.getMessage());
			}
		}
	}

	/**
	 * 版本更新,判断newVersion是否大于oldVerson
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void execSQL(String sql){
		sqLiteDatabase.execSQL(sql);
	}

	/**
	 * 插入数据
	 * @param table
	 * @param values
	 * @return 插入,返回值为行号
	 */
	public long insert(String table, ContentValues values) {
		// String sql = "insert into person(name,age) values('rose',21)";
		// db.execSQL(sql);
        addTableColumns(table,values);//添加字段

        return sqLiteDatabase.insert(table, null, values);
	}

	/**
	 * 更新
	 *
	 * @param table
	 *            表名
	 * @param values
	 *            修改后的值
	 * @param whereClause
	 *            条件语句,可以使用占位符，？
	 * @param whereArgs
	 *            使用数组中的值替换占位符
	 * @return 返回值为影响数据表的行数
	 */
	public int update(String table, ContentValues values, String whereClause,
					  String[] whereArgs) {
		// String sql = "update person set age=21 where _id=1";
		// db.execSQL(sql);

        addTableColumns(table,values);//添加字段

		// 表名
		// values,修改后的值
		// whereClause:条件语句,可以使用占位符，？
		// whereArgs:使用数组中的值替换占位符
		return sqLiteDatabase.update(table, values, whereClause, whereArgs);
	}

	/**
	 * 删除
	 *
	 * @param table
	 *            表名
	 * @param whereClause
	 *            条件语句,可以使用占位符，？
	 * @param whereArgs
	 *            使用数组中的值替换占位符
	 * @return 返回影响表的行数
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		// String sql = "delete from person where _id=1";
		// db.execSQL(sql);
		return sqLiteDatabase.delete(table, whereClause, whereArgs);
	}

	/**
	 * 查询
	 *
	 * @param sql
	 *            sql语句，查询语句，可以包含条件,sql语句不用使用分号结尾，系统自动添加
	 * @param selectionArgs
	 *            sql的查询条件可以使用占位符，占位符可以使用selectionArgs替代
	 * @return 返回值，Cursor，游标，可以比作结果集
	 */
	public Cursor select1(String sql, String[] selectionArgs) {
		// 1.sql语句，查询语句，可以包含条件,sql语句不用使用分号结尾，系统自动添加
		// 2.selectionArgs,sql的查询条件可以使用占位符，占位符可以使用selectionArgs替代
		// select * from person where name=?
		return sqLiteDatabase.rawQuery(sql, selectionArgs);
	}

	/**
	 * 查询
	 *
	 * distinct：消除重复数据（去掉重复项）
	 *
	 * limit：进行分页查询
	 *
	 * @param 1、table，表名
	 * @param 2、columns，查询的列（字段）*
	 * @param 3、selection：where后的条件子句，可以使用占位符
	 * @param 4、selectionArgs,替换占位符的值，
	 * @param 5、groupBy：根据某个字段进行分组
	 * @param 6、having：分组之后再进一步过滤
	 * @param 7、orderby:排序
	 *
	 * @return
	 */
	public Cursor select2(String table, String[] columns, String selection,
						  String[] selectionArgs, String groupBy, String having,
						  String orderBy) {

		// distinct：消除重复数据（去掉重复项）
		// 1、table，表名
		// 2、columns，查询的列（字段）
		// 3、selection：where后的条件子句，可以使用占位符
		// 4、selectionArgs,替换占位符的值，
		// 5、groupBy：根据某个字段进行分组
		// 6、having：分组之后再进一步过滤
		// 7、orderby:排序
		// limit：进行分页查询

		return sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);

	}

    /**
     * 添加字段
     * @param tableName 表名
     * @param columns 字段名
     * @param columnsType 字段类型
     */
	public void addTableColumn(String tableName,String columns,String columnsType){
		String sql = "alter table " + tableName + " add " + columns + " " + columnsType;
		sqLiteDatabase.execSQL(sql);
	}
    /**
     * 添加字段
     * @param tableName 表名
     * @param values 字段类型
     */
	public void addTableColumns(String tableName,ContentValues values){
        Iterator<String> iterator = values.keySet().iterator();
        while (iterator.hasNext()){
            String keyName = iterator.next();
            if(!judgeTableExistColumns(tableName,keyName)) {//如果不存在该字段
                String columnsType = null;
                if (values.get(keyName) instanceof Integer) {
                    columnsType = "int";
                }
                if (values.get(keyName) instanceof Boolean) {
                    columnsType = "boolean";
                }
                if (values.get(keyName) instanceof Short) {
                    columnsType = "short";
                }
                if (values.get(keyName) instanceof Double) {
                    columnsType = "double";
                }
                if (values.get(keyName) instanceof Float) {
                    columnsType = "float";
                }
                if (values.get(keyName) instanceof Long) {
                    columnsType = "long";
                }
                if (values.get(keyName) instanceof Byte) {
                    columnsType = "Byte";
				}
                if (values.get(keyName) instanceof String) {
					Integer length = (  ((String) values.get(keyName)).length() > 100 ? 1000 : 100  )  > 1000 ? 10000 : 1000;
                    columnsType = "varchar(" + length + ")";
				}
				if(columnsType != null) {
					sqLiteDatabase.execSQL("alter table " + tableName + " add " + keyName + " " + columnsType);
				}
            }

        }


	}

    /**
     * 判断表中是否存在指定字段
     * @param tableName 表名
     * @param column 字段名
     * @return
     */
    public boolean judgeTableExistColumns(String tableName,String column){
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = sqLiteDatabase.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
                    , null );
            result = cursor != null && cursor.getColumnIndex(column) != -1 ;
        }catch (Exception e){
            Log.e(TAG,"checkColumnExists1..." + e.getMessage()) ;
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }

        return result ;
    }


}
